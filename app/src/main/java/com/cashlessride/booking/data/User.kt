package com.cashlessride.booking.data

import com.google.gson.annotations.Expose

/**
 * Created on 5/17/2019.
 */
data class User (
    @Expose var id: Int? = null,
    @Expose var firstname: String? = null,
    @Expose var lastname: String? = null,
    @Expose var email: String? = null,
    @Expose var usertype: String? = null
)