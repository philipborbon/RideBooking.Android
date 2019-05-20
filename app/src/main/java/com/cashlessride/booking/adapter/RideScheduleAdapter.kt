package com.cashlessride.booking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cashlessride.booking.R
import com.cashlessride.booking.data.RideSchedule
import kotlinx.android.synthetic.main.layout_schedule.view.*
import java.text.SimpleDateFormat


/**
 * Created on 5/19/2019.
 */
class RideScheduleAdapter : RecyclerView.Adapter<RideScheduleAdapter.ViewHolder>() {
    var scheduleList: ArrayList<RideSchedule>? = null

    private val dateParser = SimpleDateFormat("yyyy-MM-dd")
    private val dateFormatter = SimpleDateFormat("MM/dd/yy")

    private val timeParser = SimpleDateFormat("HH:mm:ss")
    private val timeFormatter = SimpleDateFormat("HH:mm")

    var onBookClick: ((RideSchedule) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = scheduleList?.count() ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        scheduleList?.get(position)?.let {
            holder.bindData(it)
        }
    }

    inner class ViewHolder(private var view: View): RecyclerView.ViewHolder(view){
        fun bindData(data: RideSchedule){
            view.display_route.text = data.getMainRoute()?.description
            view.display_date.text = dateFormatter.format(dateParser.parse(data.date))
            view.display_time.text = timeFormatter.format(timeParser.parse(data.departuretime))
            view.display_id.text = "${data.id}"
            view.display_departed.text = if (data.departed == 1) "Yes" else "No"

            if (data.departed == 1) {
                view.button_book.visibility = View.GONE
            } else {
                view.button_book.visibility = View.VISIBLE
            }

            view.button_book.setOnClickListener {
                onBookClick?.let {
                    it(data)
                }
            }
        }
    }
}