package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.Invoice_model

class Invoice_adaptor(context: Context, private val array: ArrayList<Invoice_model>) : androidx.recyclerview.widget.RecyclerView.Adapter<Invoice_adaptor.ViewHolder>() {
    var context: Context = context
    private val inflater: LayoutInflater


    init {

        inflater = LayoutInflater.from(context)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Invoice_adaptor.ViewHolder {
        val view2 = inflater.inflate(R.layout.invoice_row, parent, false)

        return ViewHolder(view2)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.code!!.setText(array[position].code)
        holder.quantity!!.setText(array[position].quantity)
        holder.price!!.setText("₹"+array[position].price)
        holder.amount!!.setText("₹"+array[position].amount)
        holder.name!!.setText(array[position].name)
    }


    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var code:TextView?=null
        var quantity: TextView?=null
        var price: TextView?=null
        var amount: TextView?=null
        var name: TextView?=null


        init {
            code= itemView.findViewById(R.id.code)
            quantity= itemView.findViewById(R.id.quantity)
            price= itemView.findViewById(R.id.price)
            amount= itemView.findViewById(R.id.amount)
            name= itemView.findViewById(R.id.name)


        }
    }
}