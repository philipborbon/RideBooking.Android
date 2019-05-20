package com.cashlessride.booking.data

import com.google.gson.annotations.Expose
import java.util.*

/**
 * Created on 5/19/2019.
 */
data class PassengerType (
    @Expose var id: Int? = null,
    @Expose var name: String? = null,
    @Expose var discount: Int? = null,
    @Expose var created_at: Date? = null,
    @Expose var updated_at: Date? = null
)