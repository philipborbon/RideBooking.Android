package com.cashlessride.booking.data

import com.google.gson.annotations.Expose

/**
 * Created on 5/17/2019.
 */

data class ServiceResponse <T> (
    @Expose var message: String? = null,
    @Expose var data: T? = null,
    var success: Boolean? = null,
    var status: Int? = null,
    var error: Throwable? = null
)