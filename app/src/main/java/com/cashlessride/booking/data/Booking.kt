package com.cashlessride.booking.data

import com.google.gson.annotations.Expose

/**
 * Created on 5/19/2019.
 */
data class Booking (
    @Expose var id: Int? = null,
    @Expose var userid: Int? = null,
    @Expose var ridescheduleid: Int? = null,
    @Expose var transactionid: Int? = null,
    @Expose var payment: Double? = null,
    @Expose var bookingcode: String? = null,
    @Expose var approved: Boolean? = null,
    @Expose var closed: Boolean? = null
)