package com.cashlessride.booking.data

import com.google.gson.annotations.Expose
import java.util.*

/**
 * Created on 5/17/2019.
 */
data class Authorization (
    @Expose var tokentype: String? = null,
    @Expose var token: String? = null,
    @Expose var expiresin: Int? = null,
    var expirationDate: Date? = null
) {
    fun isExpired(): Boolean {
        val currentDate = Calendar.getInstance().time
        val compare = currentDate.compareTo(expirationDate)

        return compare >= 0
    }

    fun getAccessToken(): String {
        return "$tokentype $token"
    }
}