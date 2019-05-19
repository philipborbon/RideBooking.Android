package com.cashlessride.booking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cashlessride.booking.R
import com.cashlessride.booking.data.Topup
import kotlinx.android.synthetic.main.layout_topup.view.*
import java.text.SimpleDateFormat

/**
 * Created on 5/19/2019.
 */
class TopupListAdapter : RecyclerView.Adapter<TopupListAdapter.ViewHolder>() {
    var topupList: ArrayList<Topup>? = null

    private val dateFormatter = SimpleDateFormat("MM/dd/yy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_topup, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = topupList?.count() ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        topupList?.get(position)?.let {
            holder.bindData(it)
        }
    }

    inner class ViewHolder(private var view: View): RecyclerView.ViewHolder(view){
        fun bindData(data: Topup){
            view.display_code.text = data.topupcode
            view.display_amount.text = String.format("%.2f", data.amount)
            view.display_date.text = dateFormatter.format(data.created_at)
            view.display_is_paid.text = if (data.approved == 1) "Paid" else "Unpaid"
        }
    }
}