package com.cashlessride.booking.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.cashlessride.booking.manager.AuthorizationStore


class SplashActivity : AppCompatActivity() {

    private lateinit var authorizationStore: AuthorizationStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authorizationStore = AuthorizationStore.getInstance(this)

        if (authorizationStore.token == null) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
