package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.vipulasri.timelineview.TimelineView
import com.msimplelogic.activities.R
import com.msimplelogic.model.MyActivityModel
import java.util.*

class AdapterMyActivities(ctx: Context, private val imageModelArrayList: ArrayList<MyActivityModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<AdapterMyActivities.MyViewHolder>() {
    var context : Context = ctx
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterMyActivities.MyViewHolder {
        val view = inflater.inflate(R.layout.adapter_myactivities, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterMyActivities.MyViewHolder, position: Int) {

        holder.tvmyactivityTime.setText(imageModelArrayList[position]?.myactivities_time)
        holder.tvmyactivityAddress.setText(imageModelArrayList[position]?.myactivities_address)
        holder.tvtvmyactivityDistance.setText(imageModelArrayList[position]?.myactivities_distance)
        holder.tvmyactivityShopvisited.setText(imageModelArrayList[position]?.myactivities_shopvisited)
        holder.mTimelineView?.initLine(position);
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var tvmyactivityTime: TextView
        var tvmyactivityAddress: TextView
        var tvtvmyactivityDistance: TextView
        var tvmyactivityShopvisited: TextView
        var mTimelineView: TimelineView? = null
        init {
            tvmyactivityTime = itemView.findViewById(R.id.tvmyactivity_time) as TextView
            tvmyactivityAddress = itemView.findViewById(R.id.tvmyactivity_address) as TextView
            tvtvmyactivityDistance = itemView.findViewById(R.id.tvmyactivity_distance) as TextView
            tvmyactivityShopvisited = itemView.findViewById(R.id.tvmyactivity_shopvisited) as TextView
            mTimelineView = itemView.findViewById<View>(R.id.timeline) as TimelineView
        }
    }
}