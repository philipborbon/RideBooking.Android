package com.cashlessride.booking.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cashlessride.booking.BuildConfig
import com.cashlessride.booking.R
import com.cashlessride.booking.util.Util
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

        button_topup.setOnClickListener {
            val intent = Intent(this, TopupActivity::class.java)
            startActivity(intent)
        }

        button_redeem.setOnClickListener {
            val intent = Intent(this, RedeemActivity::class.java)
            startActivity(intent)
        }

        if ( BuildConfig.FLAVOR == Util.FLAVOR_DRIVER ) {
            button_topup.visibility = View.GONE
        }

        if ( BuildConfig.FLAVOR == Util.FLAVOR_PASSENGER ) {
            button_redeem.visibility = View.GONE
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
                    display_balance.text = Util.formatAmount(data?.amount)
                }
            } else {
                main.post { showToast(response.getErrorMessage()) }
            }
        }
    }
}
