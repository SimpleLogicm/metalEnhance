package com.msimplelogic.activities.kotlinFiles

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.content.ContextCompat
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Check_Null_Value
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import cpm.simplelogic.helper.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_taskdetails.*
import kotlinx.android.synthetic.main.content_taskdetails.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class ActivityTaskDetails : BaseActivity(), TimePickerDialog.OnTimeSetListener {
    var hourString = "";
    var minuteString = ""
    var datep = ""
    var adapter_state2: ArrayAdapter<String>? = null
    private val results = ArrayList<String>()
    var adapter_state1: ArrayAdapter<String>? = null
    private val results1 = ArrayList<String>()
    var dialog: ProgressDialog? = null
    var context: Context? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var time_date = "";
    var code = "";
    var sp:SharedPreferences?=null
    val dfDate = java.text.SimpleDateFormat("dd-MM-yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_taskdetails)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //order_toolbar_title.setText(resources.getString(R.string.Tour))


        context = ActivityTask@ this
        cd = ConnectionDetector(context)
        dialog = ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)

        results.add("Select Type")
        results.add("Type 1")
        results1.add("Select Type")
        results1.add("Type 1")
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {


            hedder_theame.setImageResource(R.drawable.dark_hedder)

//ll.setBackgroundResource(R.drawable.dark_theme_background);
        }
        adapter_state2 = ArrayAdapter<String>(this,
                R.layout.spinner_item, results)
        adapter_state2!!.setDropDownViewResource(R.layout.spinner_item)
        et_tasktype.setAdapter(adapter_state2)

        adapter_state1 = ArrayAdapter<String>(this,
                R.layout.spinner_item, results1)
        adapter_state1!!.setDropDownViewResource(R.layout.spinner_item)
        et_tourtype.setAdapter(adapter_state1)

        if (Global_Data.PlannerName.equals("Task")) {
            order_toolbar_title.setText(resources.getString(R.string.Task11))

            if (Global_Data.PlannerUpdate.equals("TaskUpdate")) {
                order_toolbar_title.setText(resources.getString(R.string.Task11))
                val extras = intent.extras
                val i = this.intent
                if (extras != null) {
                    try {
                        add_taskd_click.setText(getResources().getString(R.string.Update))
                        code = extras.getString("code")!!
                        et_taskname.setText(extras.getString("activity_name"))
                        et_taskfrom.setText(extras.getString("from"))
                        et_taskto.setText(extras.getString("to"))
                        et_taskdescription.setText(extras.getString("description"))
                        et_tasklocation.setText(extras.getString("location"))
                        et_tasksetreminder.setText(extras.getString("reminder"))
                        time_date = extras.getString("reminder")!!
                        var spin = extras.getString("type");

                        if (spin != null) {
                            var spinnerPosition = adapter_state2!!.getPosition(spin);
                            et_tasktype.setSelection(spinnerPosition);
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            task_card.visibility = View.VISIBLE
            note_card.visibility = View.GONE
            tour_card.visibility = View.GONE
        } else if (Global_Data.PlannerName.equals("Note")) {
            order_toolbar_title.setText(resources.getString(R.string.Note))

            if (Global_Data.PlannerUpdate.equals("NotesUpdate")) {
                order_toolbar_title.setText(resources.getString(R.string.Note))

                val extras = intent.extras
                val i = this.intent
                if (extras != null) {

                    try {

                        add_taskd_click.setText(getResources().getString(R.string.Update))
                        code = extras.getString("code")!!
                        txt_notetitle.setText(extras.getString("title"))
                        txt_notedescription.setText(extras.getString("description"))

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            note_card.visibility = View.VISIBLE
            task_card.visibility = View.GONE
            tour_card.visibility = View.GONE
        } else {
            order_toolbar_title.setText(resources.getString(R.string.Tour))

            tour_card.visibility = View.VISIBLE
            task_card.visibility = View.GONE
            note_card.visibility = View.GONE
        }



        et_tasktype.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                (view as TextView).setTextColor(ContextCompat.getColor(applicationContext, android.R.color.darker_gray)) //Change selected text color
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        et_tourtype.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                (view as TextView).setTextColor(ContextCompat.getColor(applicationContext, android.R.color.darker_gray)) //Change selected text color
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })


        et_taskfrom!!.setOnClickListener {



            val now = Calendar.getInstance()
            val dpd = DatePickerDialog(
                    ActivityTaskDetails@ this,
                    DatePickerDialog.OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        var mo = month
                        ++mo
                        Log.d("Orignal", mo.toString())
                        var date_from = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;
                        et_taskfrom!!.setText(date_from)

                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)

            )
            dpd.datePicker.setMinDate(System.currentTimeMillis() - 1000)
            dpd.show()

        }

        et_taskto!!.setOnClickListener {
            val now = Calendar.getInstance()
            val dpd = DatePickerDialog(
                    ActivityTaskDetails@ this,
                    DatePickerDialog.OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        var mnth=month+1
                        val fromtime=dfDate.parse(et_taskfrom.text.toString())
                        val totime=dfDate.parse("$dayOfMonth-$mnth-$year")
                        if(fromtime.after(totime)) {
                            Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.From_date_should_be_always_less_than_To_date), "Yes")
                            et_taskto.setText("")
                        }else{
                            var mo = month
                            ++mo
                            Log.d("Orignal", mo.toString())
                            var date_to = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;
                            et_taskto!!.setText(date_to)
                        }
                                             },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)

            )
            dpd.datePicker.setMinDate(System.currentTimeMillis() - 1000)
            dpd.show()

        }

        et_tourfrom!!.setOnClickListener {
            val now = Calendar.getInstance()
            DatePickerDialog(
                    ActivityTaskDetails@ this,
                    DatePickerDialog.OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        var mo = month
                        ++mo
                        Log.d("Orignal", mo.toString())
                        var date_from = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;
                        et_tourfrom.setText(date_from)

                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        et_tourto!!.setOnClickListener {
            val now = Calendar.getInstance()
            var det = DatePickerDialog(
                    ActivityTaskDetails@ this,
                    DatePickerDialog.OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        var mo = month
                        ++mo
                        Log.d("Orignal", mo.toString())
                        var date_to = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;
                        et_tourto.setText(date_to)

                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            )
            det.datePicker.setMinDate(System.currentTimeMillis() - 1000)
            det.show()
        }

        et_tasksetreminder!!.setOnClickListener {
            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_taskfrom.text.toString())) {
                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_From_Date), "Yes")
            } else
                if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_taskto.text.toString())) {
                    Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_To_Date), "Yes")
                } else {
                    val now = Calendar.getInstance()
            //  val now = Calendar.getInstance()
            var det = DatePickerDialog(
                    ActivityTaskDetails@ this,
                    DatePickerDialog.OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        var mo = month
                        ++mo
                        Log.d("Orignal", mo.toString())

                        var mnth = month + 1
                        //val fromtime=dfDate.parse(et_taskfrom.text.toString())
                        val date_to1 = dfDate.parse("$dayOfMonth-$mnth-$year")

                        val fromtime = dfDate.parse(et_taskfrom.text.toString())
                        var totime = dfDate.parse(et_taskto.text.toString())
                        val sdf = SimpleDateFormat("dd-MM-yyyy")
                        val currentDate = sdf.format(Date())
                        System.out.println(" C DATE is  "+currentDate)
                        var date_to = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;
                        if (fromtime.after(date_to1)) {
                            Global_Data.Custom_Toast(ActivityTaskDetails@ this, "Reminder should be range in between From date and To date", "Yes")
                            et_tasksetreminder.setText("")
                        } else {
                            if (totime.before(date_to1)) {
                                Global_Data.Custom_Toast(ActivityTaskDetails@ this, "Reminder should be range in between From date and To date", "Yes")
                                et_tasksetreminder.setText("")
                            } else {
                                et_tasksetreminder.setText(date_to)
                                android.app.TimePickerDialog(
                                        ActivityTaskDetails@ this,
                                        android.app.TimePickerDialog.OnTimeSetListener { view11: TimePicker?, hourOfDay: Int, minute: Int ->
                                            Log.d("Original", "Got clicked")
if(currentDate.equals(date_to))
{
    val date = Date()
    val dateFormat = SimpleDateFormat("HH:mm")
    dateFormat.format(date)
    val sdf = SimpleDateFormat("HH:MM")
    val currentTime = dateFormat.format(Date())
    System.out.println(" C DATE is  "+currentTime)

    hourString = if (hourOfDay < 10) "0$hourOfDay" else "" + hourOfDay
    minuteString = if (minute < 10) "0$minute" else "" + minute
    //val secondString = if (second < 10) "0$second" else "" + second
    val time = datep + " " + hourString + " h " + minuteString + " m"
    val time1 = hourString + ":" + minuteString
    time_date = datep + " " + hourString + ":" + minuteString;

    val currentTimen=dateFormat.parse(currentTime)
    val time1n=dateFormat.parse(time1)

    if ((time1n).before(currentTimen)) {
        Global_Data.Custom_Toast(applicationContext, "Selected Time should be Greater than current Time", "Yes")
        et_tasksetreminder.setText("")
    } else {
        et_tasksetreminder.setText(date_to + " " + time)
    }

}else{
    hourString = if (hourOfDay < 10) "0$hourOfDay" else "" + hourOfDay
    minuteString = if (minute < 10) "0$minute" else "" + minute
    //val secondString = if (second < 10) "0$second" else "" + second
    val time = datep + " " + hourString + " h " + minuteString + " m"
    time_date = datep + " " + hourString + ":" + minuteString;
    et_tasksetreminder.setText(date_to + " " + time)
}


                                        },
                                        now[Calendar.HOUR_OF_DAY],
                                        now[Calendar.MINUTE],
                                        true
                                ).show()
                            }
                        }
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            )
            det.datePicker.setMinDate(System.currentTimeMillis() - 1000)
            det.show()


//            DatePickerDialog(
//                    ActivityTaskDetails@ this,
//                    DatePickerDialog.OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
//                        var mo = month
//                        ++mo
//                        Log.d("Orignal", mo.toString())
//                        datep = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;
//                        et_tasksetreminder.setText(datep)
//                        android.app.TimePickerDialog(
//                                ActivityTaskDetails@ this,
//                                android.app.TimePickerDialog.OnTimeSetListener { view11: TimePicker?, hourOfDay: Int, minute: Int ->
//                                    Log.d("Original", "Got clicked")
//                                    hourString = if (hourOfDay < 10) "0$hourOfDay" else "" + hourOfDay
//                                    minuteString = if (minute < 10) "0$minute" else "" + minute
//                                    //val secondString = if (second < 10) "0$second" else "" + second
//                                    val time = datep + " " + hourString + " h " + minuteString + " m"
//                                    time_date = datep + " " + hourString + ":" + minuteString;
//                                    et_tasksetreminder.setText(time)
//
//                                },
//                                now[Calendar.HOUR_OF_DAY],
//                                now[Calendar.MINUTE],
//                                true
//                        ).show()
//
//                    },
//                    now.get(Calendar.YEAR),
//                    now.get(Calendar.MONTH),
//                    now.get(Calendar.DAY_OF_MONTH)
//
//            ).show()
        }
        }

        add_taskd_click.setOnClickListener {
            if (Global_Data.PlannerName.equals("Task")) {
                if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_taskname.text.toString())) {
                    Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.please_task_name), "Yes")
                } else
                    if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_taskfrom.text.toString())) {
                        Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_From_Date), "Yes")
                    } else
                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_taskto.text.toString())) {
                            Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_To_Date), "Yes")
                        } else
                            if(dfDate.parse(et_taskto.text.toString()).before(dfDate.parse(et_taskfrom.text.toString()))){
                                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.From_date_should_be_always_less_than_To_date), "Yes")

                            }else
                            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_taskdescription.text.toString())) {
                                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.please_task_description), "Yes")
                            } else
                                if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_tasklocation.text.toString())) {
                                    Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.please_task_location), "Yes")
                                } else
                                    if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_tasksetreminder.text.toString())) {
                                        Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.please_Select_task_reminder), "Yes")
                                    } else
                                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(time_date)) {
                                            Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.please_Select_task_remi_date), "Yes")
                                        } else
                                            if (et_tasktype.getSelectedItem().toString().equals(resources.getString(R.string.Select_Type), ignoreCase = true)) {
                                                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_Type), "Yes")
                                            } else {
//                                                val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm")
//                                                var final_date = datep + " " + hourString + ":" + minuteString;
//                                                // var datesss = formatter.parse(final_date);
//                                                var datesss = final_date;
//                                                Log.d("DATEEE", datesss.toString())
//                                                // Log.d("TIMEEEE", datesss.time.toString())
//                                                var sdf =  SimpleDateFormat ("dd-MM-yyyy HH:mm");
//                                                var timeMilli: Long? = null
//                                                var datenew: Date? = null
//
//                                                datenew =sdf.parse(datesss)
//                                                timeMilli=datenew.getTime()
//
//                                                var now = Calendar.getInstance();
//                                                now.add(Calendar.MINUTE, minuteString.toInt());
//                                                now.add(Calendar.HOUR, hourString.toInt());
//                                                var date = now.getTime();
//
//                                                val answer: String = formatter.format(date)
//                                                Log.d("answer", answer)
//
//                                                val notifyIntent = Intent(this, MyReceiver::class.java)
//                                                val pendingIntent = PendingIntent.getBroadcast(ActivityTaskDetails@ this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//                                                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeMilli,
//                                                        1000 * 60 * 60 * 24.toLong(), pendingIntent)

                                                isInternetPresent = cd!!.isConnectingToInternet
                                                if (isInternetPresent) {

                                                    SyncTask()
                                                } else {
                                                    Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")
                                                }

                                            }
            } else if (Global_Data.PlannerName.equals("Note")) {
                if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(txt_notetitle.text.toString())) {
                    Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Please_notes_title), "Yes")
                } else
                    if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(txt_notedescription.text.toString())) {
                        Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Enter_Description), "Yes")
                    } else {

                        isInternetPresent = cd!!.isConnectingToInternet
                        if (isInternetPresent) {
                            SyncTask()
                        } else {
                            Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")
                        }
                    }
            } else if (Global_Data.PlannerName.equals("Tour")) {
                if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_tourname.text.toString())) {
                    Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Please_trip_name), "Yes")
                } else
                    if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_tourfrom.text.toString())) {
                        Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_From_Date), "Yes")
                    } else
                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_tourto.text.toString())) {
                            Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_To_Date), "Yes")
                        } else
                            if(dfDate.parse(et_tourto.text.toString()).before(dfDate.parse(et_tourfrom.text.toString()))){
                                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.From_date_should_be_always_less_than_To_date), "Yes")

                            }else
                                if (et_tourtype.getSelectedItem().toString().equals(resources.getString(R.string.Select_Type), ignoreCase = true)) {
                                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_Type), "Yes")
                            } else
                                if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_tourmode.text.toString())) {
                                    Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Please_mode_mandate), "Yes")
                                } else
                                    if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_tourticketno.text.toString())) {
                                        Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Please_ticket_no), "Yes")
                                    } else
                                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_tourdetails.text.toString())) {
                                            Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Please_Tour_Details), "Yes")
                                        } else {
                                            isInternetPresent = cd!!.isConnectingToInternet
                                            if (isInternetPresent) {
                                                SyncTask()
                                            } else {
                                                Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")

                                            }
                                        }
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp: SharedPreferences = this@ActivityTaskDetails.getSharedPreferences("SimpleLogic", 0)
                try {
                    val target = Math.round(sp.getFloat("Target", 0f))
                    val achieved = Math.round(sp.getFloat("Achived", 0f))
                    val age_float = sp.getFloat("Achived", 0f) / sp.getFloat("Target", 0f) * 100
                    targetNew = if (age_float.toString().equals("infinity", ignoreCase = true)) {
                        val age = Math.round(age_float)
                        if (Global_Data.rsstr.length > 0) {
                            "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [infinity") + "%" + "]"
                        } else {
                            "T/A : Rs " + String.format("$target/$achieved [infinity") + "%" + "]"
                        }
                    } else {
                        val age = Math.round(age_float)
                        if (Global_Data.rsstr.length > 0) {
                            "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [$age") + "%" + "]"
                        } else {
                            "T/A : Rs " + String.format("$target/$achieved [$age") + "%" + "]"
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                val yourView = findViewById<View>(R.id.add)
                SimpleTooltip.Builder(this)
                        .anchorView(yourView)
                        .text(targetNew)
                        .gravity(Gravity.START)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onBackPressed() {
        if (Global_Data.PlannerName.equals("Tour")) {
            val i = Intent(this@ActivityTaskDetails, ActivityTour::class.java)
            startActivity(i)
            finish()
        } else {
            val i = Intent(this@ActivityTaskDetails, ActivityTask::class.java)
            startActivity(i)
            finish()
        }

    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        val hourString = if (hourOfDay < 10) "0$hourOfDay" else "" + hourOfDay
        val minuteString = if (minute < 10) "0$minute" else "" + minute
        val secondString = if (second < 10) "0$second" else "" + second
        val time = "You picked the following time: " + hourString + "h" + minuteString + "m" + secondString + "s"
        et_tasksetreminder.setText(time)
    }

    private fun getCalendarUriBase(act: Activity): String? {
        var calendarUriBase: String? = null
        var calendars = Uri.parse("content://calendar/calendars")
        var managedCursor: Cursor? = null
        try {
            managedCursor = act.managedQuery(calendars, null, null, null, null)
        } catch (e: Exception) {
        }
        if (managedCursor != null) {
            calendarUriBase = "content://calendar/"
        } else {
            calendars = Uri.parse("content://com.android.calendar/calendars")
            try {
                managedCursor = act.managedQuery(calendars, null, null, null, null)
            } catch (e: Exception) {
            }
            if (managedCursor != null) {
                calendarUriBase = "content://com.android.calendar/"
            }
        }
        return calendarUriBase
    }


    fun SyncTask() {
        System.gc()
        val reason_code = ""
        try {
            dialog!!.setMessage(resources.getString(R.string.Please_Wait))
            dialog!!.setTitle(resources.getString(R.string.app_name))
            dialog!!.setCancelable(false)
            dialog!!.show()
            var domain = ""
            var device_id = ""
            val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
            val Cust_domain = sp.getString("Cust_Service_Url", "")
            val service_url = Cust_domain + "metal/api/v1/"
            val spf: SharedPreferences = context!!.getSharedPreferences("SimpleLogic", 0)
            val user_email = spf.getString("USER_EMAIL", null)
            val shopcode = spf.getString("shopcode", null)
            val fcm_token = spf.getString("fcm_token", null)

            val randomPIN = System.currentTimeMillis()
            val PINString = "T$randomPIN"
            var jsObjRequest: JsonObjectRequest? = null
            try {
                Log.d("Server url", "Server url" + domain)
                val COLLECTION_RECJARRAY = JSONArray()
                val COLLECTION_RECJOBJECT = JSONObject()
                //val Schedule_JOB = JSONObject()

                if (Global_Data.PlannerName.equals("Task")) {
                    if (Global_Data.PlannerUpdate.equals("TaskUpdate")) {
                        domain = service_url + "activity_planners/create_activity"
                        COLLECTION_RECJOBJECT.put("code", code)
                    } else {
                        domain = service_url + "activity_planners/create_activity"
                        COLLECTION_RECJOBJECT.put("code", PINString)
                    }

                    try {
                        val lati = Global_Data.getLocationFromAddress(context, et_tasklocation!!.text.toString())
                        Log.d("LATI", "LATI" + lati)
                        if (!lati.equals("")) {
                            val s = lati.split(",")
                            COLLECTION_RECJOBJECT.put("location_latitude", s[0])
                            COLLECTION_RECJOBJECT.put("location_longitude", s[1])
                        } else {
                            COLLECTION_RECJOBJECT.put("location_latitude", "")
                            COLLECTION_RECJOBJECT.put("location_longitude", "")
                        }
                    } catch (e: Exception) {
                        Log.e("DATA", e.message.toString())
                        COLLECTION_RECJOBJECT.put("location_latitude", "")
                        COLLECTION_RECJOBJECT.put("location_longitude", "")
                    }


                    COLLECTION_RECJOBJECT.put("latitude", Global_Data.GLOvel_LATITUDE)
                    COLLECTION_RECJOBJECT.put("longitude", Global_Data.GLOvel_LONGITUDE)
                    COLLECTION_RECJOBJECT.put("name", et_taskname!!.text)
                    COLLECTION_RECJOBJECT.put("from_date", et_taskfrom!!.text)
                    COLLECTION_RECJOBJECT.put("to_date", et_taskto!!.text)
                    COLLECTION_RECJOBJECT.put("description", et_taskdescription!!.text)
                    COLLECTION_RECJOBJECT.put("location", et_tasklocation!!.text)
                    COLLECTION_RECJOBJECT.put("reminder_date", time_date)
                    COLLECTION_RECJOBJECT.put("device_fcm_key", fcm_token)
                    COLLECTION_RECJOBJECT.put("planner_type", et_tasktype!!.selectedItem.toString())


                    // COLLECTION_RECJARRAY.put(Schedule_JOB)

                    // COLLECTION_RECJOBJECT.put("visit_schedules", COLLECTION_RECJARRAY)
                    // COLLECTION_RECJOBJECT.put("customer_code", shopcode)
                    COLLECTION_RECJOBJECT.put("email", user_email)
                    COLLECTION_RECJOBJECT.put("device_fcm_key", fcm_token)
                } else if (Global_Data.PlannerName.equals("Tour")) {
                    domain = service_url + "tour_programs/create_tour"
                    COLLECTION_RECJOBJECT.put("latitude", Global_Data.GLOvel_LATITUDE)
                    COLLECTION_RECJOBJECT.put("longitude", Global_Data.GLOvel_LONGITUDE)
                    COLLECTION_RECJOBJECT.put("name", et_tourname!!.text)
                    COLLECTION_RECJOBJECT.put("from_date", et_tourfrom!!.text)
                    COLLECTION_RECJOBJECT.put("to_date", et_tourto!!.text)
                    COLLECTION_RECJOBJECT.put("mode", et_tourmode!!.text)
                    COLLECTION_RECJOBJECT.put("ticket_no", et_tourticketno!!.text)
                    COLLECTION_RECJOBJECT.put("details", et_tourdetails!!.text)
                    COLLECTION_RECJOBJECT.put("type", et_tourtype!!.selectedItem.toString())


                    // COLLECTION_RECJARRAY.put(Schedule_JOB)

                    // COLLECTION_RECJOBJECT.put("visit_schedules", COLLECTION_RECJARRAY)
                    //COLLECTION_RECJOBJECT.put("customer_code", shopcode)
                    COLLECTION_RECJOBJECT.put("email", user_email)
                } else {
                    domain = service_url + "notes/create_notes"
                    val notes_sT = JSONObject()

                    if (Global_Data.PlannerUpdate.equals("NotesUpdate")) {
                        notes_sT.put("id", code)
                    }


                    notes_sT.put("latitude", Global_Data.GLOvel_LATITUDE)
                    notes_sT.put("longitude", Global_Data.GLOvel_LONGITUDE)
                    notes_sT.put("name", txt_notetitle!!.text)
                    notes_sT.put("description", txt_notedescription!!.text)
                    notes_sT.put("type ", "user")
                    notes_sT.put("email", user_email)

                    COLLECTION_RECJARRAY.put(notes_sT)

                    COLLECTION_RECJOBJECT.put("notes", COLLECTION_RECJARRAY)
                    COLLECTION_RECJOBJECT.put("email", user_email)
                }


                Log.d("Activity Service", COLLECTION_RECJOBJECT.toString())
                jsObjRequest = JsonObjectRequest(Request.Method.POST, domain, COLLECTION_RECJOBJECT, Response.Listener { response ->
                    Log.i("volley", "response: $response")
                    Log.d("jV", "JV length" + response.length())
                    try {
                        var response_result = ""
                        response_result = if (response.has("result")) {
                            response.getString("result")
                        } else {
                            "data"
                        }
                        if (Global_Data.PlannerName.equals("Task")) {
                            if (response_result.equals("ActivityPlanner Saved Successfully", ignoreCase = true) || response_result.equals("ActivityPlanner Updated Successfully", ignoreCase = true)) {
                                dialog!!.dismiss()
                                // Global_Data.Custom_Toast(context, response_result, "Yes")
                                if (!Global_Data.PlannerUpdate.equals("TaskUpdate")) {
                                    Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Task_created), "Yes")
                                } else {
                                    Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Task_Updated), "Yes")

                                }

                                val i = Intent(context, ActivityTask::class.java)
                                startActivity(i)
                                finish()
                            } else {
                                dialog!!.dismiss()
                                Global_Data.Custom_Toast(context, response_result, "Yes")

                            }
                        } else if (Global_Data.PlannerName.equals("Tour")) {
                            if (response_result.equals("TourProgram Saved Successfully", ignoreCase = true)) {
                                dialog!!.dismiss()
                                // Global_Data.Custom_Toast(context, response_result, "Yes")

                                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Tour_Created_Succesfully), "Yes")


                                val i = Intent(context, ActivityTour::class.java)
                                startActivity(i)
                                finish()
                            } else {
                                dialog!!.dismiss()
                                Global_Data.Custom_Toast(context, response_result, "Yes")

                            }
                        } else {
                            if (response_result.equals("Note Saved Successfully", ignoreCase = true) || response_result.equals("Note Updated Successfully", ignoreCase = true)) {
                                dialog!!.dismiss()
                                // Global_Data.Custom_Toast(context, response_result, "Yes")
                                if (!Global_Data.PlannerUpdate.equals("NotesUpdate")) {
                                    Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.notes_created), "Yes")
                                } else {
                                    Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.notes_update), "Yes")
                                }

                                val i = Intent(context, ActivityTask::class.java)
                                startActivity(i)
                                finish()
                            } else {
                                dialog!!.dismiss()
                                Global_Data.Custom_Toast(context, response_result, "Yes")

                            }
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        dialog!!.dismiss()
                    }
                    dialog!!.dismiss()
                    dialog!!.dismiss()
                }, Response.ErrorListener { error ->
                    //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    if (error is TimeoutError || error is NoConnectionError) {
                        Global_Data.Custom_Toast(context,
                                "your internet connection is not working, saving locally. Please sync when Internet is available", "")
                        //                            Toast.makeText(Cash_Collect.this,
                        //                                    "your internet connection is not working, saving locally. Please sync when Internet is available",
                        //                                    Toast.LENGTH_LONG).show();
                    } else if (error is AuthFailureError) {
                        Global_Data.Custom_Toast(context,
                                "Server AuthFailureError  Error", "")
                        //                            Toast.makeText(Cash_Collect.this,
                        //                                    "Server AuthFailureError  Error",
                        //                                    Toast.LENGTH_LONG).show();
                    } else if (error is ServerError) {
                        Global_Data.Custom_Toast(context,
                                resources.getString(R.string.Server_Errors), "")
                        //                            Toast.makeText(Cash_Collect.this,
                        //                                    getResources().getString(R.string.Server_Errors),
                        //                                    Toast.LENGTH_LONG).show();
                    } else if (error is NetworkError) {
                        Global_Data.Custom_Toast(context,
                                "your internet connection is not working, saving locally. Please sync when Internet is available", "")
                        //                            Toast.makeText(Cash_Collect.this,
                        //                                    "your internet connection is not working, saving locally. Please sync when Internet is available",
                        //                                    Toast.LENGTH_LONG).show();
                    } else if (error is ParseError) {
                        Global_Data.Custom_Toast(context,
                                "ParseError   Error", "")
                        //                            Toast.makeText(Cash_Collect.this,
                        //                                    "ParseError   Error",
                        //                                    Toast.LENGTH_LONG).show();
                    } else {
                        Global_Data.Custom_Toast(context, error.message, "")
                        //Toast.makeText(Cash_Collect.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    dialog!!.dismiss()
                    // finish();
                })
                val requestQueue = Volley.newRequestQueue(context)
                val socketTimeout = 150000 //90 seconds - change to what you want
                val policy: RetryPolicy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                jsObjRequest.retryPolicy = policy
                // requestQueue.se
//requestQueue.add(jsObjRequest);
                jsObjRequest.setShouldCache(false)
                requestQueue.cache.clear()
                requestQueue.add(jsObjRequest)
            } catch (e: Exception) {
                e.printStackTrace()
                dialog!!.dismiss()
            }
        } catch (e: Exception) { // TODO: handle exception
            Log.e("DATA", e.message.toString())
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        hidePDialog()

    }

    private fun hidePDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null
        }
    }


}