package com.cashlessride.booking.data

import com.google.gson.annotations.Expose

/**
 * Created on 5/18/2019.
 */
data class RegisterForm (
    @Expose var firstname: String?,
    @Expose var lastname: String?,
    @Expose var email: String?,
    @Expose var password: String?,
    @Expose var c_password: String?
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