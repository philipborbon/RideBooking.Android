package com.cashlessride.booking.data

import com.google.gson.annotations.Expose
import java.util.*

/**
 * Created on 5/19/2019.
 */
data class Route (
    @Expose var id: Int? = null,
    @Expose var created_at: Date? = null,
    @Expose var updated_at: Date? = null,
    @Expose var startlocation: String? = null,
    @Expose var endlocation: String? = null,
    @Expose var distance: Double? = null,
    @Expose var eta: Double? = null,
    @Expose var regularfare: Double? = null
) {
    val description: String?
        get() = "$startlocation To $endlocation"
}