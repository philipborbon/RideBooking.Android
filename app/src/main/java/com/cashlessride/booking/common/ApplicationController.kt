package com.cashlessride.booking.common

import android.app.Application
import android.content.Intent
import android.os.Handler
import com.cashlessride.booking.BuildConfig
import com.cashlessride.booking.manager.APIManager
import com.cashlessride.booking.manager.AuthorizationStore
import com.cashlessride.booking.manager.UserStore
import com.cashlessride.booking.ui.SplashActivity
import timber.log.Timber



/**
 * Created on 5/18/2019.
 */
class ApplicationController : Application() {
    private lateinit var apiManager: APIManager
    private lateinit var userStore: UserStore
    private lateinit var authorizationStore: AuthorizationStore
    private lateinit var main: Handler

    override fun onCreate() {
        super.onCreate()

        userStore = UserStore.getInstance(this)
        authorizationStore = AuthorizationStore.getInstance(this)
        apiManager = APIManager.getInstance(this)

        main = Handler(mainLooper)

        apiManager.onUnauthorized {
            userStore.clear()
            authorizationStore.clear()

            main.post {
                val intent = Intent(this, SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
            }
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}