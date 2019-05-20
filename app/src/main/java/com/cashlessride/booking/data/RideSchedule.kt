package com.cashlessride.booking.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created on 5/19/2019.
 */
@Parcelize
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
) : Parcelable {
    fun getMainScheduleRoute(): ScheduleRoute? {
        return Companion.getMainScheduleRoute(routes)
    }

    fun getMainRoute(): Route? {
        return getMainScheduleRoute()?.route
    }

    companion object {
        fun getMainScheduleRoute(routes: ArrayList<ScheduleRoute>?): ScheduleRoute? {
            routes?.let {
                for (route in it) {
                    if (route.isMain == 1) {
                        return route
                    }
                }
            }

            return null
        }

        fun getMainRoute(routes: ArrayList<ScheduleRoute>?): Route? {
            return getMainScheduleRoute(routes)?.route
        }
    }

}