package com.cashlessride.booking.ui

import android.os.Bundle
import android.view.View
import com.cashlessride.booking.R
import com.cashlessride.booking.view.inputLayout
import kotlinx.android.synthetic.main.activity_topup.*

class TopupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topup)

        button_topup.setOnClickListener {
            topup()
        }
    }

    private fun topup(){
        hideKeyboard()

        val amountText = input_amount.text.toString()

        if (amountText.isBlank()) {
            input_amount.inputLayout?.error = "Amount should not be empty."
        } else {
            val amount = amountText.toDouble()

            if (amount == 0.0){
                input_amount.inputLayout?.error = "Amount should not be zero."
            } else {
                input_amount.inputLayout?.error = ""

                view_loading.visibility = View.VISIBLE

                apiManager.topup(amount) { response ->
                    main.post { view_loading.visibility = View.GONE }

                    if (response.success == true) {
                        main.post {
                            container_code.visibility = View.VISIBLE
                            display_code.text = response.data

                            input_amount.setText("")
                        }
                    } else {
                        main.post { showToast(response.getErrorMessage()) }
                    }
                }
            }
        }
    }
}
