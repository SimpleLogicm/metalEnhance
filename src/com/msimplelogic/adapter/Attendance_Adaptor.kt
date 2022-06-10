package com.msimplelogic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.msimplelogic.activities.R
import com.msimplelogic.model.AttendanceModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Attendance_Adaptor (context: Context, private val array:ArrayList<AttendanceModel>): androidx.recyclerview.widget.RecyclerView.Adapter<Attendance_Adaptor.ViewHolder>(){
    var  context: Context = context
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Attendance_Adaptor.ViewHolder {
        val view = inflater.inflate(R.layout.row_attendance_adaptor, parent, false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return  array.size
    }

    override fun onBindViewHolder(holder: Attendance_Adaptor.ViewHolder, position: Int) {

        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val date: Date = dateFormat.parse(array[position].date) //You will get date object relative to server/client timezone wherever it is parsed
        val formatter: DateFormat = SimpleDateFormat("MMMM d, yyyy") //If you need time just put specific format for time like 'HH:mm:ss'
        val dateStr: String = formatter.format(date)

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter1 = SimpleDateFormat("h:mm a")

        if(array[position].intime=="null"){
            holder.tv_in?.setText("null")
        }else{
            val timeIn = formatter1.format(parser.parse(array[position].intime))
//            val sdf = SimpleDateFormat("h:mm a")
//            sdf.format(dateObject)
            //val timeIn1: String = LocalTime.parse(timeIn, DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"))
            holder.tv_in?.setText(timeIn)
        }

        if(array[position].outtime=="null"){
            holder.tv_out?.setText("null")
        }else{
            val timeOut = formatter1.format(parser.parse(array[position].outtime))
            //val timeOut1: String = LocalTime.parse(timeOut, DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"))
            holder.tv_out?.setText(timeOut)
        }

        holder.tv_date?.setText(dateStr)
        holder.tv_shift?.setText(array[position].shift)
        //holder.tv_in?.setText(timeIn1)
        holder.tv_hours?.setText(array[position].hours)

    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var tv_date:TextView?=null
        var tv_shift:TextView?=null
        var tv_in:TextView?=null
        var tv_out:TextView?=null
        var tv_hours:TextView?=null

        init {

            tv_date = itemView.findViewById(R.id.tv_date) as TextView
            tv_shift = itemView.findViewById(R.id.tv_shift) as TextView
            tv_in = itemView.findViewById(R.id.tv_in) as TextView
            tv_out = itemView.findViewById(R.id.tv_out) as TextView
            tv_hours = itemView.findViewById(R.id.tv_hours) as TextView

        }
    }
}