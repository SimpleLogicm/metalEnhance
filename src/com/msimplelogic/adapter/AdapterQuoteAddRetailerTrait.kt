package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.CustomerTraitModel
import java.util.*

class AdapterQuoteAddRetailerTrait(ctx: Context, private val imageModelArrayList: ArrayList<CustomerTraitModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<AdapterQuoteAddRetailerTrait.MyViewHolder>() {
    var context : Context = ctx
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterQuoteAddRetailerTrait.MyViewHolder {

        val view = inflater.inflate(R.layout.adapter_customerservicestraits, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterQuoteAddRetailerTrait.MyViewHolder, position: Int) {


        holder.custSevicesTraitRemark.setText(imageModelArrayList[position]?.quantity)
        holder.custSevicesTraitDate.setText(imageModelArrayList[position]?.date)
        holder.custSevicesTraitQuoteNo.setText(imageModelArrayList[position]?.order_no)

    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var custSevicesTraitRemark: TextView
        var custSevicesTraitDate: TextView
        var custSevicesTraitQuoteNo: TextView

        init {
            custSevicesTraitRemark = itemView.findViewById(R.id.tv_remark) as TextView
            custSevicesTraitDate = itemView.findViewById(R.id.tv_date) as TextView
            custSevicesTraitQuoteNo = itemView.findViewById(R.id.txt_remark) as TextView

        }
    }
}