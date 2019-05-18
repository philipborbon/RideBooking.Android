package com.cashlessride.booking.manager

import android.content.Context
import android.content.SharedPreferences
import com.cashlessride.booking.BuildConfig
import com.cashlessride.booking.common.SingletonHolder
import com.cashlessride.booking.data.User

/**
 * Created on 5/17/2019.
 */
class UserStore private constructor(context: Context) {
    private val preference: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    companion object : SingletonHolder<UserStore, Context>(::UserStore) {
        const val PREFS_FILENAME = "${BuildConfig.APPLICATION_ID}.userstore"
        const val ID = "data-id"
        const val FIRSTNAME = "data-firstname"
        const val LASTNAME = "data-lastname"
        const val EMAIL = "data-email"
        const val USERTYPE = "data-usertype"
    }

    var id: Int
        get() = preference.getInt(ID, -1)
        set(value) = preference.edit().putInt(ID, value).apply()

    var firstname: String?
        get() = preference.getString(FIRSTNAME, null)
        set(value) = preference.edit().putString(FIRSTNAME, value).apply()

    var lastname: String?
        get() = preference.getString(LASTNAME, null)
        set(value) = preference.edit().putString(LASTNAME, value).apply()

    var email: String?
        get() = preference.getString(EMAIL, null)
        set(value) = preference.edit().putString(EMAIL, value).apply()

    var usertype: String?
        get() = preference.getString(USERTYPE, null)
        set(value) = preference.edit().putString(USERTYPE, value).apply()

    fun setUser(user: User){
        id = user.id ?: -1
        firstname = user.firstname
        lastname = user.lastname
        email = user.email
        usertype = user.usertype
    }

    fun getUser(): User {
        return User (
            id = id,
            firstname = firstname,
            lastname = lastname,
            email = email,
            usertype = usertype
        )
    }
}