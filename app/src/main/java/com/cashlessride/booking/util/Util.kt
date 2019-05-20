package com.cashlessride.booking.util

/**
 * Created on 5/20/2019.
 */
class Util {
    companion object {
        const val FLAVOR_PASSENGER = "passenger"
        const val FLAVOR_DRIVER = "driver"

        fun formatAmount(amount: Double?): String {
            return String.format("Php %,.2f", amount)
        }
    }
}