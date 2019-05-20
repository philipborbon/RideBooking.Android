package com.cashlessride.booking.ui

import android.os.Bundle
import android.view.View
import com.cashlessride.booking.R
import com.cashlessride.booking.data.UserForm
import com.cashlessride.booking.view.inputLayout
import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        button_update.setOnClickListener {
            update()
        }

        display_email.text = userStore.email
        input_firstname.setText(userStore.firstname)
        input_lastname.setText(userStore.lastname)
    }

    private fun update(){
        val firstname = input_firstname.text.toString()
        val lastname = input_lastname.text.toString()
        val password = input_password.text.toString()
        val cPassword = input_confirm_password.text.toString()

        var shouldSubmit = true

        if (firstname.isBlank()) {
            shouldSubmit = false

            input_firstname.inputLayout?.error = "First Name is required."
        } else {
            input_firstname.inputLayout?.error = ""
        }

        if (lastname.isBlank()) {
            shouldSubmit = false

            input_lastname.inputLayout?.error = "Last Name is required."
        } else {
            input_lastname.inputLayout?.error = ""
        }

        if (shouldSubmit) {
            view_loading.visibility = View.VISIBLE

            val registerForm = UserForm (
                firstname = firstname,
                lastname = lastname,
                password = password,
                c_password = cPassword
            )

            apiManager.updateUser(registerForm) { response ->
                view_loading.visibility = View.GONE

                if (response.success == true) {
                    val data = response.data

                    data?.let {
                        userStore.setUser(it)

                        authorizationStore.username = it.email
                        authorizationStore.password = password
                    }

                    main.post { showToast(response.message) }
                } else {
                    main.post { showToast(response.getErrorMessage()) }
                }
            }
        }
    }
}
