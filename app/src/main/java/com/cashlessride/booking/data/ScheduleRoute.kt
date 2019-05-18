package com.cashlessride.booking.data

import com.google.gson.annotations.Expose
import java.util.*

/**
 * Created on 5/19/2019.
 */
data class ScheduleRoute (
    @Expose var id: Int? = null,
    @Expose var scheduleid: Int? = null,
    @Expose var routeid: Int? = null,
    @Expose var isMain: Int? = null,
    @Expose var created_at: Date? = null,
    @Expose var updated_at: Date? = null,
    @Expose var route: Route? = null
)