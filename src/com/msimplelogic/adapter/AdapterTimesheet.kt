package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.TimeSheetModel
import java.util.*

class AdapterTimesheet(ctx: Context, private val timesheetArrayList: ArrayList<TimeSheetModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<AdapterTimesheet.MyViewHolder>() {
    var context : Context = ctx
    private val inflater: LayoutInflater
    var activate: Boolean? = null

    fun activateButtons(activate: Boolean) {
        this.activate = activate
        notifyDataSetChanged() //need to call it for the child views to be re-created with buttons.
    }

    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterTimesheet.MyViewHolder {

        val view = inflater.inflate(R.layout.adapter_timesheet, parent, false)



        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterTimesheet.MyViewHolder, position: Int) {

        holder.taskName.setText(timesheetArrayList[position].taskname)
        holder.detail1.setText(timesheetArrayList[position].detail1)
        holder.detail2.setText(timesheetArrayList[position].detail2)
        holder.hours.setText(timesheetArrayList[position].hours+" hrs")


        if (activate==true) {
            holder.detail1.setVisibility(View.VISIBLE)
            holder.detail2.setVisibility(View.VISIBLE)
        }else{
            holder.detail1.setVisibility(View.GONE)
            holder.detail2.setVisibility(View.GONE)
        }

    }

    override fun getItemCount(): Int {
        return timesheetArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var taskName: TextView
        var detail1: TextView
        var detail2: TextView
        var hours: TextView

        init {
            taskName = itemView.findViewById(R.id.txt_taskname) as TextView
            detail1 = itemView.findViewById(R.id.txt_taskdetail1) as TextView
            detail2 = itemView.findViewById(R.id.txt_taskdetail2) as TextView
            hours = itemView.findViewById(R.id.txt_hrs) as TextView
        }
    }
}