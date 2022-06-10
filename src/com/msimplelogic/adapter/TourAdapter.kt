package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.TourModel
import java.util.*

class TourAdapter(ctx: Context, private val imageModelArrayList: ArrayList<TourModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<TourAdapter.MyViewHolder>() {
    var context : Context = ctx
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.tour_adapter, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: TourAdapter.MyViewHolder, position: Int) {

        holder.cityTrip.setText(imageModelArrayList[position].cityname_trip)
        holder.dateTrip.setText(imageModelArrayList[position].date_trip)

//        holder.time.setOnClickListener { view ->
//            val intent = Intent(context,Order_CustomerList::class.java)
//            intent.putExtra("Beat_NAme",imageModelArrayList[position].name)
//            intent.putExtra("Beat_Code",imageModelArrayList[position].code)
//            context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var cityTrip: TextView
        var dateTrip: TextView

        init {

            cityTrip = itemView.findViewById(R.id.city_trip) as TextView
            dateTrip = itemView.findViewById(R.id.trip_date) as TextView

        }

    }
}