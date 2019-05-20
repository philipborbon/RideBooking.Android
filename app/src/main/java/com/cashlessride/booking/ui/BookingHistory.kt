package com.cashlessride.booking.ui

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashlessride.booking.R
import com.cashlessride.booking.adapter.BookingAdapter
import kotlinx.android.synthetic.main.activity_booking_history.*

class BookingHistory : BaseActivity() {
    private lateinit var adapter: BookingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_history)

        adapter = BookingAdapter(this)

        val layoutManager = LinearLayoutManager(this)

        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))

        recycler_view.adapter = adapter

        swipe_refresh.setColorSchemeColors (
            ContextCompat.getColor(this, R.color.material_green_500),
            ContextCompat.getColor(this, R.color.material_red_500),
            ContextCompat.getColor(this, R.color.material_blue_500)
        )

        swipe_refresh.setOnRefreshListener {
            pullList()
        }

        pullList()
    }

    private fun pullList(){
        swipe_refresh.isRefreshing = true

        apiManager.getBookingHistory { response ->
            main.post { swipe_refresh.isRefreshing = false }

            if (response.success == true){
                main.post {
                    adapter.bookingList = response.data
                    adapter.notifyDataSetChanged()
                }
            } else {
                main.post { showToast(response.getErrorMessage()) }
            }
        }
    }
}
