package com.cashlessride.booking.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.cashlessride.booking.R
import com.cashlessride.booking.data.Authorization
import com.cashlessride.booking.manager.APIManager
import com.cashlessride.booking.manager.AuthorizationStore
import com.cashlessride.booking.manager.UserStore
import kotlinx.android.synthetic.main.activity_login.*
import java.net.HttpURLConnection

class LoginActivity : BaseActivity() {
    private lateinit var apiManager: APIManager
    private lateinit var userStore: UserStore
    private lateinit var authorizationStore: AuthorizationStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userStore = UserStore.getInstance(this)
        authorizationStore = AuthorizationStore.getInstance(this)
        apiManager = APIManager.getInstance(this)

        button_login.setOnClickListener {
            login()
        }
    }

    private fun showLoginFailed(errorString: String?) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun login(){
        val username = input_username.text.toString()
        val password = input_password.text.toString()

        apiManager.login(username, password) { response ->
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

                main.post {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            } else {
                main.post {
                    if (response.status == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        showLoginFailed(getString(R.string.invalid_username_password))
                    } else {
                        response.error?.let { error ->
                            showLoginFailed(error.localizedMessage)
                        } ?: run {
                            showLoginFailed("Status Code: ${response.status}")
                        }
                    }
                }
            }
        }
    }
}
