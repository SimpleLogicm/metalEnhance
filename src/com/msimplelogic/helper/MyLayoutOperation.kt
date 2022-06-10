package com.msimplelogic.helper

import android.app.Activity
import android.app.TimePickerDialog
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.msimplelogic.activities.DataBaseHelper
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.LoginDataBaseAdapter
import com.msimplelogic.services.getServices
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.google.android.youtube.player.internal.v
import com.msimplelogic.activities.R


object MyLayoutOperation {
    var dsf: String? = null
    //var dbvoc = DataBaseHelper(this)
    var loginDataBaseAdapter: LoginDataBaseAdapter? = null

    fun display(activity: Activity, btn: TextView) {
        btn.setOnClickListener {
            if (Global_Data.date_arr.size > 0) {
                loginDataBaseAdapter = LoginDataBaseAdapter(activity)
                loginDataBaseAdapter = loginDataBaseAdapter!!.open()
                val scrollViewlinerLayout = activity.findViewById<View>(com.msimplelogic.activities.R.id.linearLayoutForm) as LinearLayout
                val from_arr = ArrayList<String>()
                val to_arr = ArrayList<String>()
                val task_arr = ArrayList<String>()
                val details1_arr = ArrayList<String>()
                val details2_arr = ArrayList<String>()
                val remark_arr = ArrayList<String>()
                val favstatus_arr = ArrayList<String>()

                for (i in 0 until scrollViewlinerLayout.childCount) {
                    val innerLayout = scrollViewlinerLayout.getChildAt(i) as LinearLayout
                    val from = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.tmdetails_from) as EditText
                    val to = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.tmdetails_to) as EditText

                    val task = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.et_task) as EditText
                    val details1 = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.et_details1) as EditText

                    val details2 = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.et_details2) as EditText
                    val remark = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.et_remark) as EditText
                    val fav_status = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.fav_status) as TextView

//                if(from_arr.contains(from.text.toString()))
//                {
//                    System.out.println("no")
//                }else{
//                    from_arr.add(from.text.toString())
//                }

                    from_arr.add(from.text.toString())
                    to_arr.add(to.text.toString())
                    task_arr.add(task.text.toString())
                    details1_arr.add(details1.text.toString())
                    details2_arr.add(details2.text.toString())
                    remark_arr.add(remark.text.toString())
                    favstatus_arr.add(fav_status.text.toString())

//                details2_arr.add(from_arr.get(i))
//                details2_arr.add(to_arr.get(i))
//                details2_arr.add(task_arr.get(i))

                    if (!from_arr.get(i).trim().equals("") && !to_arr.get(i).trim().equals("")) {

                        val format = SimpleDateFormat("hh:mm")
                        var formatmin = SimpleDateFormat("mm")
                        val date1 = format.parse(from_arr.get(i))
                        val date2 = format.parse(to_arr.get(i))
                        val mills = date2.time - date1.time

                        var time1min = formatmin.parse(from_arr.get(i))
                        var time2min = formatmin.parse(to_arr.get(i))
                        //  val tm1 =formatmin(from_arr.get(i))
//                    Log.v("Data1", "" + date1.time)
//                    Log.v("Data2", "" + date2.time)

                        var mintime = time2min.time - time1min.time

                        val hours = (mills / (1000 * 60 * 60)).toInt()
                        val mins = (mills / (1000 * 60)).toInt() % 60

                        val diff = "$hours:$mins" // updated value every1 second
//                    txtCurrentTime.setText(diff)
                        var ho = "$hours"
                        var mintsfs="$mins"
//                    val f = DateTimeFormatter.ofPattern("hh:mm")
//                    val start = LocalTime.parse(from_arr.get(i), f)
//                    val stop = LocalTime.parse(to_arr.get(i), f)
//                    var du1=     Duration.between(start,stop)
                        //   var str= diff.toString()
//                        if (ho <= 0.toString() ) {
//                            if(ho<0.toString()){
//                                Global_Data.Custom_Toast(activity, "To time Should be grater than from time ", "yes")
//
//                            }else if (mintsfs < 0.toString()) {
//                                Global_Data.Custom_Toast(activity, "To time Should be grater than from time ", "yes")
//                            }else{
//                                loginDataBaseAdapter?.insertTimesheet(from_arr.get(i), to_arr.get(i), task_arr.get(i), details1_arr.get(i), details2_arr.get(i), remark_arr.get(i), favstatus_arr.get(i))
//
//                            }
//                        } else {

                            loginDataBaseAdapter?.insertTimesheet(from_arr.get(i), to_arr.get(i), task_arr.get(i), details1_arr.get(i), details2_arr.get(i), remark_arr.get(i), favstatus_arr.get(i))

                        //}
                    }
                    //  loginDataBaseAdapter?.insertTimesheet(from_arr.get(i), to_arr.get(i),task_arr.get(i), details1_arr.get(i),details2_arr.get(i),remark_arr.get(i),favstatus_arr.get(i))
                }

                val dbvoc = DataBaseHelper(activity)
                val timesheet_con = dbvoc.allTimesheet
                if (timesheet_con.size > 0) {
                    getServices.SyncTimesheetRecord(activity)

                } else {
                    //Toast.makeText(activity, activity.getResources().getString(R.string.No_record_found_for_sync), Toast.LENGTH_SHORT).show();
                    Global_Data.Custom_Toast(activity, "Please Fill Timesheet", "Yes")
                }

            } else {
                Global_Data.Custom_Toast(activity, "Please select Date", "Yes")
            }

//            val t = Toast.makeText(activity.applicationContext, details2_arr.toString(), Toast.LENGTH_SHORT)
//            t.show()
        }
    }

    fun add(activity: Activity, btn: TextView) {
        val linearLayoutForm = activity.findViewById<View>(R.id.linearLayoutForm) as LinearLayout
        btn.setOnClickListener {
            val newView = activity.layoutInflater.inflate(com.msimplelogic.activities.R.layout.timesheetrowdetails, null) as LinearLayout
            newView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val btnRemove = newView.findViewById<View>(com.msimplelogic.activities.R.id.btnRemove) as ImageButton
            btnRemove.setOnClickListener { linearLayoutForm.removeView(newView) }

            val tmdetails_from = newView.findViewById<View>(com.msimplelogic.activities.R.id.tmdetails_from) as EditText
            val tmdetails_to = newView.findViewById<View>(com.msimplelogic.activities.R.id.tmdetails_to) as EditText
            val et_task = newView.findViewById<View>(com.msimplelogic.activities.R.id.et_task) as EditText
            val et_details1 = newView.findViewById<View>(com.msimplelogic.activities.R.id.et_details1) as EditText
            val et_details2 = newView.findViewById<View>(com.msimplelogic.activities.R.id.et_details2) as EditText
            val et_remark = newView.findViewById<View>(com.msimplelogic.activities.R.id.et_remark) as EditText

            var pickerfrom: TimePickerDialog

            tmdetails_from.setOnClickListener(View.OnClickListener {
                val cldr = Calendar.getInstance()
                val hour = cldr[Calendar.HOUR_OF_DAY]
                val minutes = cldr[Calendar.MINUTE]
                // time picker dialog
                pickerfrom = TimePickerDialog(activity,
                        object : TimePickerDialog.OnTimeSetListener {
                            override fun onTimeSet(tp: TimePicker?, sHour: Int, sMinute: Int) {
                                tmdetails_from.setText("$sHour:$sMinute")

                                dsf = "$sHour:$sMinute"
                            }
                        }, hour, minutes, true)
                pickerfrom.show()
            })

            var pickerto: TimePickerDialog
            tmdetails_to.setOnClickListener(View.OnClickListener {
                val cldr = Calendar.getInstance()
                val hour = cldr[Calendar.HOUR_OF_DAY]
                val minutes = cldr[Calendar.MINUTE]
                // time picker dialog
                pickerto = TimePickerDialog(activity,
                        object : TimePickerDialog.OnTimeSetListener {
                            override fun onTimeSet(tp: TimePicker?, sHour: Int, sMinute: Int) {
                                tmdetails_to.setText("$sHour:$sMinute")
                            }
                        }, hour, minutes, true)
                pickerto.show()
            })
            linearLayoutForm.addView(newView)

        }
    }

    fun addd(activity: Activity) {
        loginDataBaseAdapter = LoginDataBaseAdapter(activity)
        loginDataBaseAdapter = loginDataBaseAdapter!!.open()

        for (i in 0 until Global_Data.ShowTimesheetArr.size) {

            val linearLayoutForm = activity.findViewById<View>(com.msimplelogic.activities.R.id.linearLayoutForm) as LinearLayout
            val newView = activity.layoutInflater.inflate(com.msimplelogic.activities.R.layout.timesheetrowdetails, null) as LinearLayout
            newView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val tmdetails_from = newView.findViewById<View>(com.msimplelogic.activities.R.id.tmdetails_from) as EditText
            val tmdetails_to = newView.findViewById<View>(com.msimplelogic.activities.R.id.tmdetails_to) as EditText
            val et_task = newView.findViewById<View>(com.msimplelogic.activities.R.id.et_task) as EditText
            val et_details1 = newView.findViewById<View>(com.msimplelogic.activities.R.id.et_details1) as EditText
            val et_details2 = newView.findViewById<View>(com.msimplelogic.activities.R.id.et_details2) as EditText
            val et_remark = newView.findViewById<View>(com.msimplelogic.activities.R.id.et_remark) as EditText
            val favourite = newView.findViewById<View>(com.msimplelogic.activities.R.id.fav_img) as ImageView
            val favourite_red = newView.findViewById<View>(com.msimplelogic.activities.R.id.fav_img_red) as ImageView

            val btnRemove = newView.findViewById<View>(com.msimplelogic.activities.R.id.btnRemove) as ImageButton
            val favouriteHeart = newView.findViewById<View>(com.msimplelogic.activities.R.id.fav_img) as ImageView
            btnRemove.visibility=View.GONE
            favouriteHeart.visibility=View.GONE

            try {

                                                       //"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            val outputFormat = SimpleDateFormat("hh:mm")

            val fromdate = inputFormat.parse(Global_Data.ShowTimesheetArr.get(i).from_time)
            val fromFormattedTime = outputFormat.format(fromdate)

            val todate = inputFormat.parse(Global_Data.ShowTimesheetArr.get(i).to_time)
            val toFormattedTime = outputFormat.format(todate)

            tmdetails_from.setText(fromFormattedTime)
            tmdetails_to.setText(toFormattedTime)
            et_task.setText(Global_Data.ShowTimesheetArr.get(i).task_name)
            et_details1.setText(Global_Data.ShowTimesheetArr.get(i).detail1)
            et_details2.setText(Global_Data.ShowTimesheetArr.get(i).detail2)
            et_remark.setText(Global_Data.ShowTimesheetArr.get(i).remark)
            //et_remark.setText(Global_Data.ShowTimesheetArr.get(i).remark)

            }catch (e:Exception)
            {
                e.printStackTrace()
            }


            var pickerfrom: TimePickerDialog

            tmdetails_from.setOnClickListener(View.OnClickListener {
                val cldr = Calendar.getInstance()
                val hour = cldr[Calendar.HOUR_OF_DAY]
                val minutes = cldr[Calendar.MINUTE]
                // time picker dialog
                pickerfrom = TimePickerDialog(activity,
                        object : TimePickerDialog.OnTimeSetListener {
                            override fun onTimeSet(tp: TimePicker?, sHour: Int, sMinute: Int) {
                                tmdetails_from.setText("$sHour:$sMinute")
                            }
                        }, hour, minutes, true)
                pickerfrom.show()
            })

            var pickerto: TimePickerDialog
            tmdetails_to.setOnClickListener(View.OnClickListener {
                val cldr = Calendar.getInstance()
                val hour = cldr[Calendar.HOUR_OF_DAY]
                val minutes = cldr[Calendar.MINUTE]
                // time picker dialog
                pickerto = TimePickerDialog(activity,
                        object : TimePickerDialog.OnTimeSetListener {
                            override fun onTimeSet(tp: TimePicker?, sHour: Int, sMinute: Int) {
                                tmdetails_to.setText("$sHour:$sMinute")
                            }
                        }, hour, minutes, true)
                pickerto.show()
            })

            favourite.setOnClickListener {

                if (favourite.getVisibility() == View.VISIBLE) {
                    // Its visible
                    favourite.visibility = View.GONE
                    favourite_red.visibility = View.VISIBLE

                } else {
                    favourite.visibility = View.VISIBLE
                    favourite_red.visibility = View.GONE
                    // Either gone or invisible
                    //favourite.setImageResource(R.drawable.ic_favorite_red_24dp)
                }

                //linearLayoutForm.removeView(newView)
            }
            linearLayoutForm.addView(newView)
        }
    }
}