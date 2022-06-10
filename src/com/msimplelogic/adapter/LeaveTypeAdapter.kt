package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.LeaveManagementModel
import java.util.*

class LeaveTypeAdapter(ctx: Context, private val imageModelArrayList: ArrayList<LeaveManagementModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<LeaveTypeAdapter.MyViewHolder>() {
    var context : Context = ctx
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveTypeAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.leavetype_adapter, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaveTypeAdapter.MyViewHolder, position: Int) {
      val vas: String=imageModelArrayList[position].leave_type

      System.out.println(vas)
        holder.leaveType.setText(imageModelArrayList[position].leave_type)
        holder.leaveDate.setText(imageModelArrayList[position].total)

    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var leaveType: TextView
        var leaveDate: TextView

        init {

            leaveType = itemView.findViewById(R.id.leave_type) as TextView
            leaveDate = itemView.findViewById(R.id.leave_date) as TextView

        }
    }
}