package com.cashlessride.booking.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created on 5/19/2019.
 */
@Parcelize
data class Route (
    @Expose var id: Int? = null,
    @Expose var created_at: Date? = null,
    @Expose var updated_at: Date? = null,
    @Expose var startlocation: String? = null,
    @Expose var endlocation: String? = null,
    @Expose var distance: Double? = null,
    @Expose var eta: Double? = null,
    @Expose var regularfare: Double? = null
) : Parcelable {
    val description: String?
        get() = "$startlocation To $endlocation"
}