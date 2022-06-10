package com.msimplelogic.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.model.Visitlog_model
import java.util.ArrayList

class Visitlogadaptor(ctx: Context, private val imageModelArrayList: ArrayList<Visitlog_model>) : androidx.recyclerview.widget.RecyclerView.Adapter<Visitlogadaptor.MyViewHolder>() {
    var context: Context = ctx
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Visitlogadaptor.MyViewHolder {

        val view = inflater.inflate(R.layout.rawvisitlog, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: Visitlogadaptor.MyViewHolder, position: Int) {

        Global_Data.Code = imageModelArrayList[0]?.code
        holder.numbercount.setText(imageModelArrayList[position]?.number)
        holder.visittype.setText(imageModelArrayList[position]?.type)
        holder.intime.setText(imageModelArrayList[position]?.intime)
        holder.outTime.setText(imageModelArrayList[position]?.outtime)

    }

    override fun getItemCount(): Int {
        return imageModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var numbercount: TextView
        var visittype: TextView
        var intime: TextView
        var outTime: TextView

        init {
            numbercount = itemView.findViewById(R.id.numbercount) as TextView
            visittype = itemView.findViewById(R.id.visittype) as TextView
            intime = itemView.findViewById(R.id.intime) as TextView
            outTime = itemView.findViewById(R.id.outTime) as TextView

        }
    }
}