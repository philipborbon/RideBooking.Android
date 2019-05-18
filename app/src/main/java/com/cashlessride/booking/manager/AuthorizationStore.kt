package com.cashlessride.booking.manager

import android.content.Context
import android.content.SharedPreferences
import com.cashlessride.booking.BuildConfig
import com.cashlessride.booking.common.SingletonHolder
import com.cashlessride.booking.data.Authorization
import java.util.*

/**
 * Created on 5/17/2019.
 */
class AuthorizationStore private constructor(context: Context){
    private val preference: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    companion object : SingletonHolder<AuthorizationStore, Context>(::AuthorizationStore) {
        const val PREFS_FILENAME = "${BuildConfig.APPLICATION_ID}.authentication.store"
        const val TOKEN_TYPE = "data-token-type"
        const val TOKEN = "data-token"
        const val EXPIRES_IN = "data-expires-in"
        const val EXPIRATION_DATE = "data-expiration-date"
        const val USERNAME = "data-username"
        const val PASSWORD = "data-password"
    }

    var tokenType: String?
        get() = preference.getString(TOKEN_TYPE, null)
        set(value) = preference.edit().putString(TOKEN_TYPE, value).apply()

    var token: String?
        get() = preference.getString(TOKEN, null)
        set(value) = preference.edit().putString(TOKEN, value).apply()

    var expiresIn: Int
        get() = preference.getInt(EXPIRES_IN, 0)
        set(value) = preference.edit().putInt(EXPIRES_IN, value).apply()

    var expirationDate: Date?
        get() = Date(preference.getLong(EXPIRATION_DATE, 0))
        set(value) = preference.edit().putLong(EXPIRATION_DATE, value?.time ?: 0).apply()

    var username: String?
        get() = preference.getString(USERNAME, null)
        set(value) = preference.edit().putString(USERNAME, value).apply()

    var password: String?
        get() = preference.getString(PASSWORD, null)
        set(value) = preference.edit().putString(PASSWORD, value).apply()

    fun getAuthorization(): Authorization {
        return Authorization (
            tokentype = tokenType,
            token = token,
            expiresin = expiresIn,
            expirationDate = expirationDate
        )
    }

    fun setAuthorization(authorization: Authorization){
        tokenType = authorization.tokentype
        token = authorization.token
        expiresIn = authorization.expiresin ?: 0

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, if (expiresIn > 10) expiresIn - 10 else expiresIn)

        expirationDate = calendar.time
    }

    fun clear(){
        preference.edit().clear().apply()
    }
}