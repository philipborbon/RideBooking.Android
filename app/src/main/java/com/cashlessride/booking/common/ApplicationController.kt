package com.cashlessride.booking.common

import android.app.Application
import com.cashlessride.booking.BuildConfig
import timber.log.Timber



/**
 * Created on 5/18/2019.
 */
class ApplicationController : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}