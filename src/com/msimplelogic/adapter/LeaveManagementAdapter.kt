package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.LeaveManagementModel
import java.util.*

class LeaveManagementAdapter(ctx: Context, private val imageModelArrayList: ArrayList<LeaveManagementModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<LeaveManagementAdapter.MyViewHolder>() {
    var context : Context = ctx
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveManagementAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.leavemanagement_adapter, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaveManagementAdapter.MyViewHolder, position: Int) {

        holder.leaveType.setText(imageModelArrayList[position].leave_type)
        holder.totalLeave.setText(imageModelArrayList[position].total)
        holder.balanceLeave.setText(imageModelArrayList[position].pending_leave)
        holder.pandingforaprove.setText(imageModelArrayList[position].pending_for_approval)

    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var leaveType: TextView
        var totalLeave: TextView
        var balanceLeave: TextView
        var pandingforaprove: TextView

        init {
            leaveType = itemView.findViewById(R.id.leave_type) as TextView
            totalLeave = itemView.findViewById(R.id.total) as TextView
            balanceLeave = itemView.findViewById(R.id.balance) as TextView
            pandingforaprove = itemView.findViewById(R.id.pandingforaprove) as TextView
        }
    }
}