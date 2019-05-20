package com.cashlessride.booking.ui

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashlessride.booking.R
import com.cashlessride.booking.adapter.TopupListAdapter
import kotlinx.android.synthetic.main.activity_topup_history.*

class TopupHistoryActivity : BaseActivity() {
    private lateinit var adapter: TopupListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topup_history)

        adapter = TopupListAdapter()

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

        apiManager.getTopupList { response ->
            main.post { swipe_refresh.isRefreshing = false }

            if (response.success == true){
                main.post {
                    adapter.topupList = response.data
                    adapter.notifyDataSetChanged()
                }
            } else {
                main.post { showToast(response.getErrorMessage()) }
            }
        }
    }
}
