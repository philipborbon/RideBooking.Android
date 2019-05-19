package com.cashlessride.booking.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cashlessride.booking.R
import com.cashlessride.booking.data.Authorization
import kotlinx.android.synthetic.main.activity_login.*
import java.net.HttpURLConnection

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button_login.setOnClickListener {
            login()
        }
    }

    private fun login(){
        view_loading.visibility = View.VISIBLE

        val username = input_username.text.toString()
        val password = input_password.text.toString()

        apiManager.login(username, password) { response ->
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

                authorizationStore.username = username
                authorizationStore.password = password

                authorizationStore.setAuthorization(authorization)

                main.post {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
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
