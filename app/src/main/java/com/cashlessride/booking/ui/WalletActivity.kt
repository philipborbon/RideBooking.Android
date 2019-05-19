package com.cashlessride.booking.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cashlessride.booking.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view_loading
import kotlinx.android.synthetic.main.activity_wallet.*

class WalletActivity : BaseActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        display_greeting.text = "Hi ${userStore.firstname}!"
        display_balance.text = "Loading..."

        button_refresh.setOnClickListener {
            loadWallet()
        }

        loadWallet()
    }

    private fun loadWallet(){
        view_loading.visibility = View.VISIBLE

        apiManager.getWallet { response ->
            main.post { view_loading.visibility = View.GONE }

            if (response.success == true) {
                val data = response.data

                main.post {
                    display_balance.text = String.format("Php %.2f", data?.amount ?: 0)
                }
            } else {
                main.post {
                    response.error?.let { error ->
                        showToast(error.localizedMessage)
                    } ?: run {
                        showToast("Status Code: ${response.status}")
                    }
                }
            }
        }
    }
}
