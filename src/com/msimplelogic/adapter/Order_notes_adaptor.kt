package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.Order_notes_model

class Order_notes_adaptor (context: Context, private val array: ArrayList<Order_notes_model>) : androidx.recyclerview.widget.RecyclerView.Adapter<Order_notes_adaptor.ViewHolder>(){
    var context: Context = context
    private val inflater: LayoutInflater
    private var animationUp: Animation? = null
    private var animationDown: Animation? = null


    init {

        inflater = LayoutInflater.from(context)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Order_notes_adaptor.ViewHolder {
        val view2 = inflater.inflate(R.layout.order_notes_row, parent, false)

        return ViewHolder(view2)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_ordertitle!!.setText(array[position].name)
        holder.tv_date!!.setText(array[position].created_date)
        holder.ss_description!!.setText(array[position].notestitle)
        holder.ss_time!!.setText(array[position].datenotes)

        animationUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        animationDown = AnimationUtils.loadAnimation(context , R.anim.slide_down)
        holder.L1!!.setOnClickListener(View.OnClickListener {
            if (holder.lin2!!.isShown()) {
                holder.lin2!!.setVisibility(View.GONE)
                holder.lin2!!.startAnimation(animationUp)
            } else {
                holder.lin2!!.setVisibility(View.VISIBLE)
                holder.lin2!!.startAnimation(animationDown)
            }
        })
    }


    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var tv_ordertitle: TextView?=null
        var tv_date: TextView?=null
        var ss_description: TextView?=null
        var ss_time: TextView?=null
        var L1: LinearLayout? = null
        var lin2: LinearLayout? = null


        init {
            tv_ordertitle= itemView.findViewById(R.id.tv_ordertitle)
            tv_date= itemView.findViewById(R.id.tv_date)
            ss_description= itemView.findViewById(R.id.ss_description)
            ss_time= itemView.findViewById(R.id.ss_time)
            L1= itemView.findViewById(R.id.lin1)
            lin2= itemView.findViewById(R.id.lin2)


        }
    }
}