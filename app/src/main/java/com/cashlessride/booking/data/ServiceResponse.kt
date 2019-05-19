package com.cashlessride.booking.data

import com.google.gson.annotations.Expose
import java.net.HttpURLConnection

/**
 * Created on 5/17/2019.
 */

data class ServiceResponse <T> (
    @Expose var message: String? = null,
    @Expose var data: T? = null,
    var success: Boolean? = null,
    var status: Int? = null,
    var error: Throwable? = null
) {
    fun getErrorMessage(): String?{
        if (status == HttpURLConnection.HTTP_BAD_REQUEST) {
            return message
        } else {
            error?.let {
                return it.localizedMessage
            } ?: run {
                return "Status Code: $status"
            }
        }
    }
}