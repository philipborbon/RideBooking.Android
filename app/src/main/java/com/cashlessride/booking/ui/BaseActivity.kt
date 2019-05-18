package com.cashlessride.booking.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

/**
 * Created on 5/18/2019.
 */
open class BaseActivity : AppCompatActivity() {
    protected lateinit var main: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        main = Handler(mainLooper)
    }
}