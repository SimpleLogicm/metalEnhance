package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.CustomerTraitModel
import java.util.*

class AdapterCustomerTrait(ctx: Context, private val imageModelArrayList: ArrayList<CustomerTraitModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<AdapterCustomerTrait.MyViewHolder>() {
    var context : Context = ctx
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCustomerTrait.MyViewHolder {

        val view = inflater.inflate(R.layout.adapter_ordercustomertrait, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterCustomerTrait.MyViewHolder, position: Int) {
        holder.custTraitOrderno.setText(imageModelArrayList[position]?.order_no)
        holder.custTraitDate.setText(imageModelArrayList[position]?.date)
        holder.custTraitQty.setText(imageModelArrayList[position]?.quantity)
        holder.custTraitPrice.setText(imageModelArrayList[position]?.price)
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var custTraitOrderno: TextView
        var custTraitDate: TextView
        var custTraitQty: TextView
        var custTraitPrice: TextView

        init {
            custTraitOrderno = itemView.findViewById(R.id.custtrait_orderno) as TextView
            custTraitDate = itemView.findViewById(R.id.custtrait_date) as TextView
            custTraitQty = itemView.findViewById(R.id.custtrait_qty) as TextView
            custTraitPrice = itemView.findViewById(R.id.custtrait_price) as TextView
        }
    }
}