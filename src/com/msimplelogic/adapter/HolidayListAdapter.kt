package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.LeaveManagementModel
import java.util.*

class HolidayListAdapter(ctx: Context, private val imageModelArrayList: ArrayList<LeaveManagementModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<HolidayListAdapter.MyViewHolder>() {
    var context : Context = ctx
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayListAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.holidaylist_adapter, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HolidayListAdapter.MyViewHolder, position: Int) {

        holder.holidayName.setText(imageModelArrayList[position].leave_type)
        holder.holidayDate.setText(imageModelArrayList[position].total)

    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var holidayName: TextView
        var holidayDate: TextView

        init {

            holidayName = itemView.findViewById(R.id.holiday_name) as TextView
            holidayDate = itemView.findViewById(R.id.holiday_date) as TextView

        }
    }
}