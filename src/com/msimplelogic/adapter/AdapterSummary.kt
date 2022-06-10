package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.SummaryModel
import java.util.*

class AdapterSummary(ctx: Context, private val imageModelArrayList: ArrayList<SummaryModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<AdapterSummary.MyViewHolder>() {
    var context : Context = ctx
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSummary.MyViewHolder {

        val view = inflater.inflate(R.layout.adapter_summary, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterSummary.MyViewHolder, position: Int) {

        holder.tvsummaryShopname.setText(imageModelArrayList[position]?.summary_shopname)
        holder.tvsummaryAddress.setText(imageModelArrayList[position]?.summary_address)
        holder.tvsummaryAmount.setText(imageModelArrayList[position]?.summary_amount)
        holder.tvsummaryOrdertype.setText(imageModelArrayList[position]?.summary_ordertype)

    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var tvsummaryShopname: TextView
        var tvsummaryAddress: TextView
        var tvsummaryAmount: TextView
        var tvsummaryOrdertype: TextView

        init {
            tvsummaryShopname = itemView.findViewById(R.id.tvsummary_shopname) as TextView
            tvsummaryAddress = itemView.findViewById(R.id.tvsummary_address) as TextView
            tvsummaryAmount = itemView.findViewById(R.id.tvsummary_amount) as TextView
            tvsummaryOrdertype = itemView.findViewById(R.id.tvsummary_ordertype) as TextView

        }
    }
}