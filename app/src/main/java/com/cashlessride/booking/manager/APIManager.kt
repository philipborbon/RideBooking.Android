package com.cashlessride.booking.manager

import android.content.Context
import com.cashlessride.booking.common.SingletonHolder
import com.cashlessride.booking.data.*
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

    private val service = CashlessRideService.getInstance(context)
    private val authorizationStore =  AuthorizationStore.getInstance(context)
    private val userStore =  UserStore.getInstance(context)

    private var _onUnauthorized: (() -> Unit)? = null
    private var _onError: ((ServiceResponse<*>) -> Unit)? = null

    private fun verifyToken(completion: (authorization: Authorization) -> Unit){
        val authorization = authorizationStore.getAuthorization()

        if (authorization.isExpired()) {
            login(authorizationStore.username, authorizationStore.password) { response ->
                when {
                    response.success == true -> {
                        val data = response.data

                        authorizationStore.setAuthorization(Authorization(
                            tokentype = data?.tokentype,
                            token = data?.token,
                            expiresin = data?.expiresin
                        ))

                        data?.user?.let {
                            userStore.setUser(it)
                        }

                        completion(authorization)
                    }

                    response.status == HttpURLConnection.HTTP_UNAUTHORIZED -> _onUnauthorized?.let { onUnauthorized ->
                        onUnauthorized()
                    }

                    else -> _onError?.let { onError ->
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

    fun getRideSchedules(completion: (ServiceResponse<ArrayList<RideSchedule>>) -> Unit){
        verifyToken {
            val call = service.getRideSchedules()

            call.enqueue(object: Callback<ServiceResponse<ArrayList<RideSchedule>>> {
                override fun onFailure(call: Call<ServiceResponse<ArrayList<RideSchedule>>>, t: Throwable) {
                    Timber.tag(LOG_TAG).e(t)
                    completion(ServiceResponse(success = false, error = t))
                }

                override fun onResponse(
                    call: Call<ServiceResponse<ArrayList<RideSchedule>>>,
                    response: Response<ServiceResponse<ArrayList<RideSchedule>>>
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

    fun getWallet(completion: (ServiceResponse<Wallet>) -> Unit){
        verifyToken {
            val call = service.getWallet()

            call.enqueue(object: Callback<ServiceResponse<Wallet>> {
                override fun onFailure(call: Call<ServiceResponse<Wallet>>, t: Throwable) {
                    Timber.tag(LOG_TAG).e(t)
                    completion(ServiceResponse(success = false, error = t))
                }

                override fun onResponse(
                    call: Call<ServiceResponse<Wallet>>,
                    response: Response<ServiceResponse<Wallet>>
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

    fun topup(amount: Double?, completion: (ServiceResponse<String>) -> Unit){
        verifyToken {
            val call = service.topup(amount)

            call.enqueue(object: Callback<ServiceResponse<String>> {
                override fun onFailure(call: Call<ServiceResponse<String>>, t: Throwable) {
                    Timber.tag(LOG_TAG).e(t)
                    completion(ServiceResponse(success = false, error = t))
                }

                override fun onResponse(
                    call: Call<ServiceResponse<String>>,
                    response: Response<ServiceResponse<String>>
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

    fun getTopupList(completion: (ServiceResponse<ArrayList<Topup>>) -> Unit){
        verifyToken {
            val call = service.getTopupList()

            call.enqueue(object: Callback<ServiceResponse<ArrayList<Topup>>> {
                override fun onFailure(call: Call<ServiceResponse<ArrayList<Topup>>>, t: Throwable) {
                    Timber.tag(LOG_TAG).e(t)
                    completion(ServiceResponse(success = false, error = t))
                }

                override fun onResponse(
                    call: Call<ServiceResponse<ArrayList<Topup>>>,
                    response: Response<ServiceResponse<ArrayList<Topup>>>
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

    fun createBooking(form: BookingForm, completion: (ServiceResponse<Booking>) -> Unit){
        verifyToken {
            val call = service.createBooking(form)

            call.enqueue(object: Callback<ServiceResponse<Booking>> {
                override fun onFailure(call: Call<ServiceResponse<Booking>>, t: Throwable) {
                    Timber.tag(LOG_TAG).e(t)
                    completion(ServiceResponse(success = false, error = t))
                }

                override fun onResponse(
                    call: Call<ServiceResponse<Booking>>,
                    response: Response<ServiceResponse<Booking>>
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

    fun getPassengerTypes(completion: (ServiceResponse<ArrayList<PassengerType>>) -> Unit){
        verifyToken {
            val call = service.getPassengerTypes()

            call.enqueue(object: Callback<ServiceResponse<ArrayList<PassengerType>>> {
                override fun onFailure(call: Call<ServiceResponse<ArrayList<PassengerType>>>, t: Throwable) {
                    Timber.tag(LOG_TAG).e(t)
                    completion(ServiceResponse(success = false, error = t))
                }

                override fun onResponse(
                    call: Call<ServiceResponse<ArrayList<PassengerType>>>,
                    response: Response<ServiceResponse<ArrayList<PassengerType>>>
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

    fun getBookingHistory(completion: (ServiceResponse<ArrayList<Booking>>) -> Unit){
        verifyToken {
            val call = service.getBookingHistory()

            call.enqueue(object: Callback<ServiceResponse<ArrayList<Booking>>> {
                override fun onFailure(call: Call<ServiceResponse<ArrayList<Booking>>>, t: Throwable) {
                    Timber.tag(LOG_TAG).e(t)
                    completion(ServiceResponse(success = false, error = t))
                }

                override fun onResponse(
                    call: Call<ServiceResponse<ArrayList<Booking>>>,
                    response: Response<ServiceResponse<ArrayList<Booking>>>
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

    fun redeem(amount: Double?, completion: (ServiceResponse<String>) -> Unit){
        verifyToken {
            val call = service.redeem(amount)

            call.enqueue(object: Callback<ServiceResponse<String>> {
                override fun onFailure(call: Call<ServiceResponse<String>>, t: Throwable) {
                    Timber.tag(LOG_TAG).e(t)
                    completion(ServiceResponse(success = false, error = t))
                }

                override fun onResponse(
                    call: Call<ServiceResponse<String>>,
                    response: Response<ServiceResponse<String>>
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

    fun getRedeemList(completion: (ServiceResponse<ArrayList<Redeem>>) -> Unit){
        verifyToken {
            val call = service.getRedeemList()

            call.enqueue(object: Callback<ServiceResponse<ArrayList<Redeem>>> {
                override fun onFailure(call: Call<ServiceResponse<ArrayList<Redeem>>>, t: Throwable) {
                    Timber.tag(LOG_TAG).e(t)
                    completion(ServiceResponse(success = false, error = t))
                }

                override fun onResponse(
                    call: Call<ServiceResponse<ArrayList<Redeem>>>,
                    response: Response<ServiceResponse<ArrayList<Redeem>>>
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

    fun getConfirmedBooking(completion: (ServiceResponse<ArrayList<Booking>>) -> Unit){
        verifyToken {
            val call = service.getConfirmedBooking()

            call.enqueue(object: Callback<ServiceResponse<ArrayList<Booking>>> {
                override fun onFailure(call: Call<ServiceResponse<ArrayList<Booking>>>, t: Throwable) {
                    Timber.tag(LOG_TAG).e(t)
                    completion(ServiceResponse(success = false, error = t))
                }

                override fun onResponse(
                    call: Call<ServiceResponse<ArrayList<Booking>>>,
                    response: Response<ServiceResponse<ArrayList<Booking>>>
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
}