package com.cashlessride.booking.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.cashlessride.booking.BuildConfig
import com.cashlessride.booking.R
import com.cashlessride.booking.data.Booking
import com.cashlessride.booking.data.BookingSeat
import com.cashlessride.booking.data.Route
import com.cashlessride.booking.util.Util
import kotlinx.android.synthetic.main.layout_booking.view.*
import kotlinx.android.synthetic.main.layout_booking_route.view.*
import java.text.SimpleDateFormat

/**
 * Created on 5/20/2019.
 */
class BookingAdapter(private var context: Context) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {
    var bookingList: ArrayList<Booking>? = null

    private val dateParser = SimpleDateFormat("yyyy-MM-dd")
    private val dateFormatter = SimpleDateFormat("MM/dd/yy")

    private val timeParser = SimpleDateFormat("HH:mm:ss")
    private val timeFormatter = SimpleDateFormat("HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_booking, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = bookingList?.count() ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bookingList?.get(position)?.let {
            holder.bindData(it)
        }
    }

    inner class ViewHolder(private var view: View): RecyclerView.ViewHolder(view){
        @SuppressLint("InlinedApi")
        fun bindData(data: Booking){
            view.display_code.text = data.bookingcode
            view.display_amount.text = Util.formatAmount(data.payment)

            if ( BuildConfig.FLAVOR == Util.FLAVOR_PASSENGER ) {
                view.display_is_approved.visibility = View.VISIBLE
                view.container_vehicle.visibility = View.VISIBLE
            } else if ( BuildConfig.FLAVOR == Util.FLAVOR_DRIVER ) {
                view.container_vehicle.visibility = View.GONE
                view.display_is_approved.visibility = View.GONE
            }

            val approvedText = when {
                data.approved == 1 -> "Confirmed"
                data.closed == 1 -> "Cancelled"
                else -> "Unconfirmed"
            }

            view.display_is_approved.text = approvedText

            view.display_date.text = dateFormatter.format(dateParser.parse(data.schedule?.date))
            view.display_time.text = timeFormatter.format(timeParser.parse(data.schedule?.departuretime))
            view.display_boarding_time.text = HtmlCompat.fromHtml("Boarding time starts at <b>${timeFormatter.format(timeParser.parse(data.schedule?.boardingtime))}</b>.", Html.FROM_HTML_MODE_LEGACY)
            view.display_vehicle.text = data.schedule?.vehicle?.description
            view.display_cabnumber.text = HtmlCompat.fromHtml("Cab #: <b>${data.schedule?.vehicle?.cabnumber}</b>", Html.FROM_HTML_MODE_LEGACY)

            val routes = HashMap<Route?, ArrayList<BookingSeat?>?>()

            data.seats?.let {
                for (seat in it){
                    val route = if (routes[seat.route] == null) {
                        routes[seat.route] = arrayListOf()
                        routes[seat.route]
                    } else {
                        routes[seat.route]
                    }

                    route?.add(seat)
                }
            }

            view.container_route.removeAllViews()

            for ((route, types) in routes) {
                view.container_route.addView(createRouteView(route, types))
            }
        }

        @SuppressLint("InlinedApi")
        fun createRouteView(route: Route?, seats: ArrayList<BookingSeat?>?): View {
            val view = LayoutInflater.from(context).inflate(R.layout.layout_booking_route, null)
            view.display_route.text = route?.description

            var typeString = ""

            seats?.let {
                it.forEachIndexed { index, seat ->
                    if (index != 0) {
                        typeString += ", "
                    }

                    typeString += "${seat?.type?.name}: <b>${seat?.count}</b>"
                }
            }

            view.display_passenger.text = HtmlCompat.fromHtml(typeString, Html.FROM_HTML_MODE_LEGACY)

            return view
        }
    }
}