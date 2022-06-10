package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.Product_comparison_Model

class Product_comparison_adaptor( context: Context,private val array:ArrayList<Product_comparison_Model>): androidx.recyclerview.widget.RecyclerView.Adapter<Product_comparison_adaptor.ViewHolder>(){

    var  context: Context = context
    private val inflater: LayoutInflater


    init {

        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Product_comparison_adaptor.ViewHolder {
        val view = inflater.inflate(R.layout.row_product_comparison, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
return array.size
    }

    override fun onBindViewHolder(holder: Product_comparison_adaptor.ViewHolder, position: Int) {
        holder.product_name?.setText(array[position].varientname)
        holder.tv_rp?.setText(array[position].RP)
        holder.tv_mrp?.setText(array[position].MRP)
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var product_name:TextView?=null
        var tv_rp:TextView?=null
        var tv_mrp:TextView?=null
        init {
            product_name=itemView.findViewById(R.id.product_name)
            tv_rp=itemView.findViewById(R.id.tv_rp)
            tv_mrp=itemView.findViewById(R.id.tv_mrp)


        }
    }


    }