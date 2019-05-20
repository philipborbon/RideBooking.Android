package com.cashlessride.booking.data

import com.google.gson.annotations.Expose
import java.util.*

/**
 * Created on 5/20/2019.
 */
data class BookingSeat (
    @Expose var id: Int? = null,
    @Expose var bookingid: Int? = null,
    @Expose var routeid: Int? = null,
    @Expose var typeid: Int? = null,
    @Expose var count: Int? = null,
    @Expose var created_at: Date? = null,
    @Expose var updated_at: Date? = null,
    @Expose var route: Route? = null,
    @Expose var type: PassengerType? = null
)