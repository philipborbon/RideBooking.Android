package com.cashlessride.booking.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cashlessride.booking.manager.APIManager
import com.cashlessride.booking.manager.AuthorizationStore
import com.cashlessride.booking.manager.UserStore

/**
 * Created on 5/18/2019.
 */
open class BaseActivity : AppCompatActivity() {
    protected lateinit var apiManager: APIManager
    protected lateinit var userStore: UserStore
    protected lateinit var authorizationStore: AuthorizationStore
    protected lateinit var main: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userStore = UserStore.getInstance(this)
        authorizationStore = AuthorizationStore.getInstance(this)
        apiManager = APIManager.getInstance(this)

        main = Handler(mainLooper)
    }

    protected fun showToast(errorString: String?) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    protected fun hideKeyboard() {
        currentFocus?.let {
            val inputManager:InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.SHOW_FORCED)
        }
    }
}