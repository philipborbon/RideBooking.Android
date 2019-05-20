package com.cashlessride.booking.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cashlessride.booking.R
import com.cashlessride.booking.adapter.RideScheduleAdapter
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var adapter: RideScheduleAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val header = nav_view.getHeaderView(0)
        header.display_email.text = userStore.email
        header.display_name.text = "${userStore.firstname} ${userStore.lastname}"

        adapter = RideScheduleAdapter()

        adapter.onBookClick = {
            val intent = Intent(this, BookActivity::class.java)
            intent.putExtra(BookActivity.DATA_SCHEDULE, it)

            startActivity(intent)
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter

        swipe_refresh.setColorSchemeColors (
            ContextCompat.getColor(this, R.color.material_green_500),
            ContextCompat.getColor(this, R.color.material_red_500),
            ContextCompat.getColor(this, R.color.material_blue_500)
        )

        swipe_refresh.setOnRefreshListener {
            refreshSchedule()
        }

        refreshSchedule()
    }

    private fun refreshSchedule(){
        swipe_refresh.isRefreshing = true

        apiManager.getRideSchedules { response ->
            main.post { swipe_refresh.isRefreshing = false }

            if (response.success == true) {
                main.post {
                    adapter.scheduleList = response.data
                    adapter.notifyDataSetChanged()
                }
            } else {
                main.post {
                    showToast(response.getErrorMessage())
                }
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> logout()
            R.id.nav_wallet -> {
                val intent = Intent(this, WalletActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_topup_history -> {
                val intent = Intent(this, TopupHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_booking_history -> {
                val intent = Intent(this, BookingHistory::class.java)
                startActivity(intent)
            }
            R.id.nav_redeem_history -> {
                val intent = Intent(this, RedeemHistoryActivity::class.java)
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun logout(){
        userStore.clear()
        authorizationStore.clear()

        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)

        finish()
    }
}