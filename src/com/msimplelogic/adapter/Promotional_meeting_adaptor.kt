package com.msimplelogic.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.msimplelogic.activities.Check_Null_Value
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.activities.kotlinFiles.MeetingSaveNew
import com.msimplelogic.activities.kotlinFiles.Promotional_meeting_add
import com.msimplelogic.activities.kotlinFiles.Promotional_save
import com.msimplelogic.model.Promotional_Model
import com.msimplelogic.services.getServices
import kotlinx.android.synthetic.main.content_planner.*

class Promotional_meeting_adaptor(context: Context, private val array: ArrayList<Promotional_Model>) : androidx.recyclerview.widget.RecyclerView.Adapter<Promotional_meeting_adaptor.ViewHolder>() {


    var context: Context = context
    private val inflater: LayoutInflater
    var sp:SharedPreferences?=null



    init {

        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Promotional_meeting_adaptor.ViewHolder {

        val view = inflater.inflate(R.layout.row_promotional_meeting_adaptor, parent, false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: Promotional_meeting_adaptor.ViewHolder, position: Int) {
        sp = context.getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            holder.callimg?.setImageResource(R.drawable.call_dark)
            holder.locationimg?.setImageResource(R.drawable.locationdayschedulemap_dark)



//ll.setBackgroundResource(R.drawable.dark_theme_background);
        }


        holder.c_name?.setText(array[position].topics)
        holder.c_address?.setText(array[position].c_address)
        holder.c_date?.setText(array[position].date)
        holder.c_time?.setText(array[position].time)

        holder.reschedule?.setOnClickListener {

            val intent = Intent(context, Promotional_meeting_add::class.java)

            Global_Data.PlannerUpdate = "Yes"
            intent.putExtra("code", array[position].code)
            intent.putExtra("et_topic", array[position].topics)
            intent.putExtra("et_date", array[position].date)
            intent.putExtra("et_time", array[position].time)
            intent.putExtra("et_location", array[position].c_address)
            intent.putExtra("type_spin", array[position].m_type)
            intent.putExtra("arrays", array[position].arrays.toString())


             Global_Data.Order_hashmap.clear()
//            getServices.check_flag = array[position].check_flag;
//            getServices.action_item = array[position].action_item;
//            getServices.assign_to = array[position].assign_to;
//            getServices.status = array[position].status;
//            getServices.description = array[position].description;
//            getServices.pdf_path = array[position].pdf_path;
//            getServices.mp3_path = array[position].mp3_path;
            getServices.id = array[position].code;
            getServices.m_type = array[position].m_type;
            getServices.due_date = array[position].date
            // intent.putExtra("reminder", getData.get(TaskSwipeAdapter.TAG_Reminder))


            context.startActivity(intent)
        }
        holder.locationmap?.setOnClickListener {

            try {
                var latitude = array[position].latitude
                var longitude = array[position].longitude

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(latitude) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(longitude)) {
                    val intent = Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=latitude,longitude"))
                    context.startActivity(intent)
                }
                else
                {
                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.latlong_not_foundnew), "Yes")
                }



            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        holder.call?.setOnClickListener {
            Global_Data.Custom_Toast(context, "call", "Yes")
        }

        holder.cardView?.setOnClickListener {

            Global_Data.PlannerUpdate = "Yes"
//            getServices.check_flag = array[position].check_flag;
//            getServices.action_item = array[position].action_item;
//            getServices.assign_to = array[position].assign_to;
//            getServices.status = array[position].status;
//            getServices.description = array[position].description;
//            getServices.pdf_path = array[position].pdf_path;
//            getServices.mp3_path = array[position].mp3_path;
            getServices.id = array[position].code;
            getServices.m_type = array[position].m_type;
            getServices.due_date = array[position].date
            Global_Data.Order_hashmap.clear()


            if(!array[position].m_type.equals("Executive",ignoreCase = false))
            {
                context.startActivity(Intent(context, MeetingSaveNew::class.java))
            }
            else
            {
                context.startActivity(Intent(context, Promotional_save::class.java))
            }

        }
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var c_name: TextView? = null
        var c_address: TextView? = null
        var c_date: TextView? = null
        var c_time: TextView? = null
        var cardView: LinearLayout? = null
        var reschedule: TextView? = null
        var locationmap: LinearLayout? = null
        var call: LinearLayout? = null
        var callimg:ImageView?=null
        var locationimg:ImageView?=null


        init {
            c_name = itemView.findViewById(R.id.c_name)
            c_address = itemView.findViewById(R.id.c_address)
            c_date = itemView.findViewById(R.id.date)
            c_time = itemView.findViewById(R.id.time)
            cardView = itemView.findViewById(R.id.cardView)
            reschedule = itemView.findViewById(R.id.reschedule)
            locationmap = itemView.findViewById(R.id.locationmap)
            call = itemView.findViewById(R.id.call)
            callimg=itemView.findViewById(R.id.callimg);
            locationimg=itemView.findViewById(R.id.locationimg);






        }

    }

}