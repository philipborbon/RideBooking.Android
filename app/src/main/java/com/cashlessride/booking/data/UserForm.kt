package com.cashlessride.booking.data

import com.google.gson.annotations.Expose

/**
 * Created on 5/18/2019.
 */
data class UserForm (
    @Expose var firstname: String? = null,
    @Expose var lastname: String? = null,
    @Expose var email: String? = null,
    @Expose var password: String? = null,
    @Expose var c_password: String? = null
) {
    fun formData(): HashMap<String, String?>{
        return hashMapOf(
            "firstname" to firstname,
            "lastname" to lastname,
            "email" to email,
            "password" to password,
            "c_password" to c_password
        )
    }
}