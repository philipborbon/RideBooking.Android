package com.cashlessride.booking.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created on 5/19/2019.
 */
@Parcelize
data class ScheduleRoute (
    @Expose var id: Int? = null,
    @Expose var scheduleid: Int? = null,
    @Expose var routeid: Int? = null,
    @Expose var isMain: Int? = null,
    @Expose var created_at: Date? = null,
    @Expose var updated_at: Date? = null,
    @Expose var route: Route? = null
) : Parcelable