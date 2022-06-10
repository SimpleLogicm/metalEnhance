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
import com.msimplelogic.model.Visit_Schedule_model

class Visit_schedule_adaptor(context: Context, private val array: ArrayList<Visit_Schedule_model>) : androidx.recyclerview.widget.RecyclerView.Adapter<Visit_schedule_adaptor.ViewHolder>() {


    var context: Context = context
    private val inflater: LayoutInflater
    private var animationUp: Animation? = null
    private var animationDown: Animation? = null


    init {

        inflater = LayoutInflater.from(context)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Visit_schedule_adaptor.ViewHolder {
        val view2 = inflater.inflate(R.layout.row_vist_schedule, parent, false)

        return ViewHolder(view2)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_times!!.setText(array.get(position).time)
        holder.tv_dates!!.setText(array.get(position).date)
        holder.ss_description!!.setText(array.get(position).description)

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
        var tv_times:TextView?=null
        var ss_description:TextView?=null
        var ss_time:TextView?=null
        var tv_dates:TextView?=null
        var L1: LinearLayout? = null
        var lin2: LinearLayout? = null


        init {
            tv_dates= itemView.findViewById(R.id.tv_dates)
            tv_times= itemView.findViewById(R.id.tv_times)
            ss_description= itemView.findViewById(R.id.ss_description)
            ss_time= itemView.findViewById(R.id.ss_time)
            L1= itemView.findViewById(R.id.lin1)
            lin2= itemView.findViewById(R.id.lin2)


        }
    }
}