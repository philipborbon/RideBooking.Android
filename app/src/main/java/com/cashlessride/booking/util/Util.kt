package com.cashlessride.booking.util

/**
 * Created on 5/20/2019.
 */
class Util {
    companion object {
        fun formatAmount(amount: Double?): String {
            return String.format("Php %,.2f", amount)
        }
    }
}