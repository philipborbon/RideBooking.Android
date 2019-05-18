package com.cashlessride.booking.data

import com.google.gson.annotations.Expose
import java.util.*

/**
 * Created on 5/19/2019.
 */
data class RideSchedule (
    @Expose var id: Int? = null,
    @Expose var created_at: Date? = null,
    @Expose var updated_at: Date? = null,
    @Expose var vehicleid: Int? = null,
    @Expose var departuretime: String? = null,
    @Expose var boardingtime: String? = null,
    @Expose var date: String? = null,
    @Expose var departed: Int? = null,
    @Expose var active: Int? = null,
    @Expose var vehicle: Vehicle? = null,
    @Expose var routes: ArrayList<ScheduleRoute>? = null
) {
    fun getMainRoute(): Route? {
        routes?.let {
            for (route in it) {
                if (route.isMain == 1) {
                    return route.route
                }
            }
        }

        return null
    }
}