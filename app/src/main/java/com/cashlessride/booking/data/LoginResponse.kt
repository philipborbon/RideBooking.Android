package com.cashlessride.booking.data

import com.google.gson.annotations.Expose

/**
 * Created on 5/17/2019.
 */
data class LoginResponse (
    @Expose var tokentype: String? = null,
    @Expose var token: String? = null,
    @Expose var expiresin: Int? = null,
    @Expose var user: User? = null
)