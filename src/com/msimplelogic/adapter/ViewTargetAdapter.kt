package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.ViewTargetModel
import java.util.*

class ViewTargetAdapter(ctx: Context, private val viewTargetArrayList: ArrayList<ViewTargetModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<ViewTargetAdapter.MyViewHolder>() {
    var context : Context = ctx
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewTargetAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.adapter_viewtarget, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewTargetAdapter.MyViewHolder, position: Int) {

        holder.prodName.setText(viewTargetArrayList[position].target_name)
        holder.target.setText(viewTargetArrayList[position].target)
        holder.achieved.setText(viewTargetArrayList[position].achieved)
        holder.percentage.setText(viewTargetArrayList[position].percentage)
    }

    override fun getItemCount(): Int {
        return viewTargetArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var prodName: TextView
        var target: TextView
        var achieved: TextView
        var percentage: TextView

        init {
            prodName = itemView.findViewById(R.id.prod_name) as TextView
            target = itemView.findViewById(R.id.tv_target_adapter) as TextView
            achieved = itemView.findViewById(R.id.tv_achieved_adapter) as TextView
            percentage = itemView.findViewById(R.id.tv_percentage_adapter) as TextView
        }
    }
}