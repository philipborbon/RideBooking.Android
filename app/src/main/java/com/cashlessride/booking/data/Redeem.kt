package com.cashlessride.booking.data

import com.google.gson.annotations.Expose
import java.util.*

/**
 * Created on 5/21/2019.
 */
data class Redeem (
    @Expose var id: Int? = null,
    @Expose var walletid: Int? = null,
    @Expose var transactionid: Int? = null,
    @Expose var amount: Double? = null,
    @Expose var redeemcode: String? = null,
    @Expose var approved: Int? = null,
    @Expose var created_at: Date? = null,
    @Expose var updated_at: Date?
)