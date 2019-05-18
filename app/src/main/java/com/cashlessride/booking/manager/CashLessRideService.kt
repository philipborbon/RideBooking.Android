package com.cashlessride.booking.manager

import com.cashlessride.booking.BuildConfig
import com.cashlessride.booking.data.LoginResponse
import com.cashlessride.booking.data.ServiceResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


/**
 * Created on 5/17/2019.
 */
interface CashLessRideService {
    companion object {
        val instance: CashLessRideService by lazy {
            val logging = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
            }

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.SERVER_URL)
                .client(httpClient.build())
                .build()

            retrofit.create(CashLessRideService::class.java)
        }
    }

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("username") username: String?, @Field("password") password: String?): Call<ServiceResponse<LoginResponse>>
}