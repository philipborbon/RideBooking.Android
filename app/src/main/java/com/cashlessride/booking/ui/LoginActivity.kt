package com.cashlessride.booking.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cashlessride.booking.BuildConfig
import com.cashlessride.booking.R
import com.cashlessride.booking.data.Authorization
import com.cashlessride.booking.util.Util
import kotlinx.android.synthetic.main.activity_login.*
import java.net.HttpURLConnection

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button_login.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin(){
        view_loading.visibility = View.VISIBLE

        val username = input_username.text.toString()
        val password = input_password.text.toString()

        apiManager.login(username, password) { response ->
            main.post { view_loading.visibility = View.GONE }

            if (response.success == true) {
                val data = response.data

                data?.user?.let {
                    if (BuildConfig.FLAVOR == Util.FLAVOR_DRIVER) {
                        if (it.usertype != "driver") {
                            main.post { showToast("Only drivers are allowed to login in this application.") }

                            return@login
                        }
                    } else if (BuildConfig.FLAVOR == Util.FLAVOR_PASSENGER) {
                        if (it.usertype != "passenger") {
                            main.post { showToast("Only passengers are allowed to login in this application.") }

                            return@login
                        }
                    }

                    userStore.setUser(it)
                }

                val authorization = Authorization (
                    tokentype = data?.tokentype,
                    token = data?.token,
                    expiresin = data?.expiresin
                )

                authorizationStore.username = username
                authorizationStore.password = password

                authorizationStore.setAuthorization(authorization)

                main.post {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    finish()
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
    }
}
