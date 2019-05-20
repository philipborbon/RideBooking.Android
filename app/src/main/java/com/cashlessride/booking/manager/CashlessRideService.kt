package com.cashlessride.booking.manager

import android.content.Context
import com.cashlessride.booking.BuildConfig
import com.cashlessride.booking.data.*
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


/**
 * Created on 5/17/2019.
 */
interface CashlessRideService {
    companion object {
        private lateinit var instance: CashlessRideService

        fun getInstance(context: Context): CashlessRideService {
            if ( !(::instance.isInitialized) ) {
                val authorizationStore = AuthorizationStore.getInstance(context)

                val gsonBuilder = GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")

                val httpClient = OkHttpClient.Builder()
                httpClient.addInterceptor {
                    val request = it.request()
                        .newBuilder()
                        .addHeader("Accept", "application/json")

                    authorizationStore.token?.let {
                        val authorization = authorizationStore.getAuthorization()
                        request.addHeader("Authorization", authorization.getAccessToken())
                    }

                    it.proceed(request.build())
                }

                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    httpClient.addInterceptor(logging)
                }

                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .baseUrl(BuildConfig.SERVER_URL)
                    .client(httpClient.build())
                    .build()

                instance = retrofit.create(CashlessRideService::class.java)
            }

            return instance
        }
    }

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("username") username: String?, @Field("password") password: String?): Call<ServiceResponse<LoginResponse>>

    @POST("register")
    fun register(@Body form: RegisterForm): Call<ServiceResponse<LoginResponse>>

    @GET("schedule/list")
    fun getRideSchedules(): Call<ServiceResponse<ArrayList<RideSchedule>>>

    @GET("wallet/detail")
    fun getWallet(): Call<ServiceResponse<Wallet>>

    @FormUrlEncoded
    @POST("wallet/topup")
    fun topup(@Field("amount") amount: Double?): Call<ServiceResponse<String>>

    @GET("wallet/topups")
    fun getTopupList(): Call<ServiceResponse<ArrayList<Topup>>>

    @POST("booking/create")
    fun createBooking(@Body form: BookingForm): Call<ServiceResponse<Booking>>

    @GET("booking/passengerTypes")
    fun getPassengerTypes(): Call<ServiceResponse<ArrayList<PassengerType>>>

    @GET("booking/history")
    fun getBookingHistory(): Call<ServiceResponse<ArrayList<Booking>>>

    @FormUrlEncoded
    @POST("wallet/redeem")
    fun redeem(@Field("amount") amount: Double?): Call<ServiceResponse<String>>

    @GET("wallet/redeems")
    fun getRedeemList(): Call<ServiceResponse<ArrayList<Redeem>>>

    @GET("booking/confirmed")
    fun getConfirmedBooking(): Call<ServiceResponse<ArrayList<Booking>>>
}