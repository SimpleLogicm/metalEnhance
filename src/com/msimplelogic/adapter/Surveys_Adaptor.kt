package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.Surveys_Model

class Surveys_Adaptor (context: Context, private val array:ArrayList<Surveys_Model>): androidx.recyclerview.widget.RecyclerView.Adapter<Surveys_Adaptor.ViewHolder>() {
    var  context: Context = context
    private val inflater: LayoutInflater


    init {

        inflater = LayoutInflater.from(context)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Surveys_Adaptor.ViewHolder {

        val view = inflater.inflate(R.layout.row_surveys, parent, false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
return array.size
    }

    override fun onBindViewHolder(holder: Surveys_Adaptor.ViewHolder, position: Int) {
        holder.product_name?.setText(array[position].Product_name)
        holder.tv_Date?.setText(array[position].survey_date)
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var product_name:TextView?=null
        var tv_Date:TextView?=null

        init {
        product_name=itemView.findViewById(R.id.product_name)
            tv_Date=itemView.findViewById(R.id.tv_Date)

        }
    }

}