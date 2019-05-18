package com.cashlessride.booking.manager

import android.content.Context
import com.cashlessride.booking.common.SingletonHolder
import com.cashlessride.booking.data.Authorization
import com.cashlessride.booking.data.LoginResponse
import com.cashlessride.booking.data.RegisterForm
import com.cashlessride.booking.data.ServiceResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.net.HttpURLConnection

/**
 * Created on 5/17/2019.
 */

const val LOG_TAG = "APIManager"

class APIManager private constructor(context: Context) {
    companion object : SingletonHolder<APIManager, Context>(::APIManager)

    private val service = CashLessRideService.instance
    private val authorizationStore =  AuthorizationStore.getInstance(context)

    private var _onUnauthorized: (() -> Unit)? = null
    private var _onError: ((ServiceResponse<*>) -> Unit)? = null

    private fun provideToken(completion: (authorization: Authorization) -> Unit){
        val authorization = authorizationStore.getAuthorization()

        if (authorization.isExpired()) {
            login(authorizationStore.username, authorizationStore.password) { response ->
                if (response.success == true) {
                    val data = response.data

                    authorizationStore.setAuthorization(Authorization(
                        tokentype = data?.tokentype,
                        token = data?.token,
                        expiresin = data?.expiresin
                    ))

                    completion(authorization)
                } else if (response.status == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    _onUnauthorized?.let { onUnauthorized ->
                        onUnauthorized()
                    }
                } else {
                    _onError?.let { onError ->
                        onError(response)
                    }
                }
            }
        } else {
            completion(authorization)
        }
    }

    fun onUnauthorized(completion: () -> Unit){
        _onUnauthorized = completion
    }

    fun onError(completion: (ServiceResponse<*>) -> Unit){
        _onError = completion
    }

    fun login(username: String?, password: String?, completion: (ServiceResponse<LoginResponse>) -> Unit){
        val call = service.login(username, password)
        call.enqueue(object: Callback<ServiceResponse<LoginResponse>> {
            override fun onFailure(call: Call<ServiceResponse<LoginResponse>>, t: Throwable) {
                completion(ServiceResponse(success = false, error = t))
            }

            override fun onResponse(
                call: Call<ServiceResponse<LoginResponse>>,
                response: Response<ServiceResponse<LoginResponse>>) {
                val serviceResponse = response.body() ?: ServiceResponse()
                serviceResponse.status = response.code()
                serviceResponse.success = response.code() == HttpURLConnection.HTTP_OK

                response.errorBody()?.string()?.let {
                    Timber.tag(LOG_TAG).e(it)

                    val errorResponse = Gson().fromJson(it, ServiceResponse::class.java)
                    serviceResponse.message = errorResponse.message
                }

                completion(serviceResponse)
            }
        })
    }

    fun register(form: RegisterForm, completion: (ServiceResponse<LoginResponse>) -> Unit){
        val call = service.register(form)
        call.enqueue(object: Callback<ServiceResponse<LoginResponse>> {
            override fun onFailure(call: Call<ServiceResponse<LoginResponse>>, t: Throwable) {
                Timber.tag(LOG_TAG).e(t)
                completion(ServiceResponse(success = false, error = t))
            }

            override fun onResponse(
                call: Call<ServiceResponse<LoginResponse>>,
                response: Response<ServiceResponse<LoginResponse>>
            ) {
                val serviceResponse = response.body() ?: ServiceResponse()
                serviceResponse.status = response.code()
                serviceResponse.success = response.code() == HttpURLConnection.HTTP_OK

                response.errorBody()?.string()?.let {
                    Timber.tag(LOG_TAG).e(it)

                    val errorResponse = Gson().fromJson(it, ServiceResponse::class.java)
                    serviceResponse.message = errorResponse.message
                }

                completion(serviceResponse)
            }

        })
    }
}