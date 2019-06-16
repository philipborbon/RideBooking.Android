package com.cashlessride.booking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cashlessride.booking.R
import com.cashlessride.booking.data.Redeem
import com.cashlessride.booking.util.Util
import kotlinx.android.synthetic.main.layout_redeem.view.*
import java.text.SimpleDateFormat

/**
 * Created on 5/19/2019.
 */
class RedeemListAdapter : RecyclerView.Adapter<RedeemListAdapter.ViewHolder>() {
    var redeemList: ArrayList<Redeem>? = null

    private val dateFormatter = SimpleDateFormat("MM/dd/yy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_redeem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = redeemList?.count() ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        redeemList?.get(position)?.let {
            holder.bindData(it)
        }
    }

    inner class ViewHolder(private var view: View): RecyclerView.ViewHolder(view){
        fun bindData(data: Redeem){
            view.display_code.text = data.redeemcode
            view.display_amount.text = Util.formatAmount(data.amount)
            view.display_date.text = dateFormatter.format(data.created_at)
            view.display_is_collected.text = if (data.approved == 1) "Redeemed" else "Unredeemed"
        }
    }
}