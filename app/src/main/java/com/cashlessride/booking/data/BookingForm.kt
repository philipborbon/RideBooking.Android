package com.cashlessride.booking.data

import com.google.gson.annotations.Expose

/**
 * Created on 5/19/2019.
 */
data class BookingForm (
    @Expose var ridescheduleid: Int? = null,
    @Expose var seats: ArrayList<BookingSeatForm>? = null
)