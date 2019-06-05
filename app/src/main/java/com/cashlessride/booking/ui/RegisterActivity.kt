package com.cashlessride.booking.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.cashlessride.booking.R
import com.cashlessride.booking.data.Authorization
import com.cashlessride.booking.data.UserForm
import com.cashlessride.booking.view.inputLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_register.*
import java.net.HttpURLConnection

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        button_register.setOnClickListener {
            register()
        }
    }

    private fun register(){
        val firstname = input_firstname.text.toString()
        val lastname = input_lastname.text.toString()
        val email = input_email.text.toString()
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

        if (email.isBlank()) {
            shouldSubmit = false

            input_email.inputLayout?.error = "Email is required."
        } else {
            input_email.inputLayout?.error = ""
        }

        if (password.isBlank()) {
            shouldSubmit = false

            input_password.inputLayout?.error = "Password is required."
        } else {
            input_password.inputLayout?.error = ""
        }

        if (cPassword.isBlank()) {
            shouldSubmit = false

            input_confirm_password.inputLayout?.error = "Confirm password is required."
        } else {
            input_confirm_password.inputLayout?.error = ""
        }

        if (shouldSubmit) {
            view_loading.visibility = View.VISIBLE

            val registerForm = UserForm (
                firstname = firstname,
                lastname = lastname,
                email = email,
                password = password,
                c_password = cPassword
            )

            apiManager.register(registerForm) { response ->
                main.post { view_loading.visibility = View.GONE }

                if (response.success == true) {
                    val data = response.data

                    data?.user?.let {
                        userStore.setUser(it)
                    }

                    val authorization = Authorization (
                        tokentype = data?.tokentype,
                        token = data?.token,
                        expiresin = data?.expiresin
                    )

                    authorizationStore.setAuthorization(authorization)

                    main.post { view_loading.visibility = View.VISIBLE }

                    FirebaseInstanceId.getInstance().instanceId
                        .addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                main.post {
                                    view_loading.visibility = View.GONE
                                    showToast(task.exception?.localizedMessage)
                                }
                                return@OnCompleteListener
                            }

                            // Get new Instance ID token
                            val token = task.result?.token

                            apiManager.updatePushToken(token) { response ->
                                main.post { view_loading.visibility = View.GONE }

                                if (response.success == true) {
                                    main.post {
                                        val builder = AlertDialog.Builder(this)
                                        builder.setTitle("Success")
                                        builder.setMessage("Your account has been created.")
                                        builder.setPositiveButton("OK") { dialog, which ->
                                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)

                                            finish()
                                        }

                                        builder.show()
                                    }
                                } else {
                                    main.post {
                                        if (response.status == HttpURLConnection.HTTP_UNAUTHORIZED) {
                                            showToast(getString(R.string.invalid_username_password))
                                        } else {
                                            showToast(response.getErrorMessage())
                                        }
                                    }
                                }
                            }
                        })
                } else {
                    main.post {
                        if (response.status == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            showToast(response.message)
                        } else {
                            showToast(response.getErrorMessage())
                        }
                    }
                }
            }
        }
    }
}
