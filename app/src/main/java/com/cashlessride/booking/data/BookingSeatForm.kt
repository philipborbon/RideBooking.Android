package com.cashlessride.booking.data

import com.google.gson.annotations.Expose

/**
 * Created on 5/19/2019.
 */
data class BookingSeatForm (
    @Expose var routeid: Int? = null,
    @Expose var typeid: Int? = null,
    @Expose var count: Int? = null
)