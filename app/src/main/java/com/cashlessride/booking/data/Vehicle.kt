package com.cashlessride.booking.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created on 5/19/2019.
 */
@Parcelize
class Vehicle(
    @Expose var id: Int? = null,
    @Expose var created_at: Date? = null,
    @Expose var updated_at: Date? = null,
    @Expose var description: String? = null,
    @Expose var driverid: Int? = null,
    @Expose var seats: Int? = null,
    @Expose var platenumber: String? = null,
    @Expose var cabnumber: String? = null,
    @Expose var available: Int? = null,
    @Expose var operatorid: Int? = null,
    @Expose var boundary: Double? = null
) : Parcelable