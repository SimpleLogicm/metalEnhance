package com.msimplelogic.activities.kotlinFiles

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.activities.Sales_Dash
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import com.msimplelogic.adapter.Visit_schedule_adaptor
import com.msimplelogic.helper.MyLayoutOperation
import com.msimplelogic.model.Visit_Schedule_model
import com.msimplelogic.webservice.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_visit__schedule.*
import kotlinx.android.synthetic.main.content_visit__schedule.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Visit_Schedule : BaseActivity() {
    var rv_visit_shedule: RecyclerView? = null
    var tv_view_all: TextView? = null
    var et_date: EditText? = null
    var et_time: EditText? = null
    var et_discription: EditText? = null
    var et_riminder: EditText? = null
    var btn_save: Button? = null
    var card: CardView? = null
    var list: ArrayList<Visit_Schedule_model>? = null
    var adaptor: Visit_schedule_adaptor? = null
    var i = Int
    var timeset: String? = null
    var tv_history: TextView? = null
    var spin_time: Spinner? = null
    var dialog: ProgressDialog? = null
    var context: Context? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var sp:SharedPreferences?=null

    @SuppressLint("WrongConstant", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit__schedule)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        //   setTitle(resources.getString(R.string.Visit_schedule))

        rv_visit_shedule = findViewById(R.id.rv_visit_shedule)
        tv_view_all = findViewById(R.id.tv_view_all)
        et_date = findViewById(R.id.et_date)
        et_time = findViewById(R.id.et_time)
        et_discription = findViewById(R.id.et_discription)
        et_riminder = findViewById(R.id.et_riminder)
        btn_save = findViewById(R.id.btn_save)
        card = findViewById(R.id.card)
        spin_time = findViewById(R.id.spin_time)
        tv_history = findViewById(R.id.tv_history)
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
//            val img: Drawable =   context!!.resources.getDrawable(R.drawable.ic_baseline_date_range_24_dark)
//
//            et_date!!.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        btn_save!!.setOnClickListener {
            validation()
        }

        tv_history!!.setOnClickListener {
            var intent = Intent(context, Visit_schedule_history::class.java)
            startActivity(intent)
            finish()
        }
   //     var dateFormat =  SimpleDateFormat("hh:mm a")

        val sdf = SimpleDateFormat("hh:mm a")
        val sdfmin = SimpleDateFormat("mm")
        val sdfho = SimpleDateFormat("hh")


        val sdfdate = SimpleDateFormat("dd-M-yyyy")
        val currnttime = sdf.format(Date())
        val currntmin = sdfmin.format(Date())
        var currnthour = sdfho.format(Date())

        if(currnthour.length==1){
            currnthour="0"+currnthour
        }

        val currntdate = sdf.format(Date())


        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val ap = mcurrentTime.get(Calendar.AM_PM)

//        mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
//            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
//                var format = "AM"
//
//                if (hourOfDay > 11) {
//                    // If the hour is greater than or equal to 12
//                    // Then the current AM PM status is PM
//                    format = "PM";
//                }
//
//                // Initialize a new variable to hold 12 hour format hour value
//                var hour_of_12_hour_format: Int
//                if (hourOfDay > 11) {
//
//                    // If the hour is greater than or equal to 12
//                    // Then we subtract 12 from the hour to make it 12 hour format time
//                    hour_of_12_hour_format = hourOfDay - 12;
//                } else {
//                    hour_of_12_hour_format = hourOfDay;
//                }
//
//
//                //         et_time!!.setText(String.format("%d : %d", hour, minute," "+ap))
//                var ho = (String.format(hour_of_12_hour_format.toString()))
//                var min = (String.format(minute.toString()))
//                var datenew = format
//                if (ho.length == 1) {
//                    ho="0"+ho
//                   // et_time!!.setText("0" + ho + " : " + min + " " + datenew)
//                }
//                if (min.length==1){
//                    min="0"+min
//                }
//
//
//                    et_time!!.setText(ho + " : " + min + " " + datenew)
//
//
//
//             var et=   et_time!!.text.toString()
//                if (et.contains("AM")){
//                    if(currnttime.contains("a.m")){
//                        if (ho >= currnthour)
//                        {
//                            if (currntmin>min){
//                                Global_Data.Custom_Toast(this@Visit_Schedule,"Invalid Time","yes");
//                                et_time!!.setText("")
//                            }
//
//                        }
//                    }else{
//                        Global_Data.Custom_Toast(this@Visit_Schedule,"Invalid Time","yes");
//                        et_time!!.setText("")
//                    }
//
//                }else{
//                    if(currnttime.contains("p.m")){
//                        if (ho >= currnthour)
//                        {
//                            if (currntmin>min){
//                                Global_Data.Custom_Toast(this@Visit_Schedule,"Invalid Time","yes");
//                                et_time!!.setText("")
//                            }
//
//                        }else{
//                            Global_Data.Custom_Toast(this@Visit_Schedule,"Invalid Time","yes");
//                            et_time!!.setText("")
//
//                        }
//                    }
//                }
//
//
////                if (et_time!!.text.toString() < currnttime)
////                {
////                    Global_Data.Custom_Toast(this@Visit_Schedule,"Invalid Time","yes");
////                    et_time!!.setText("")
////                }
//
//            }
//        }, hour, minute, false)
        var pickerfrom: TimePickerDialog


        et_time!!.setOnClickListener {

           // mTimePicker.show()
            val cldr = Calendar.getInstance()
            val hour = cldr[Calendar.HOUR_OF_DAY]
            val minutes = cldr[Calendar.MINUTE]
            // time picker dialog
            pickerfrom = TimePickerDialog(this@Visit_Schedule,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(tp: TimePicker?, sHour: Int, sMinute: Int) {
                            et_time!!.setText("$sHour:$sMinute")

                            MyLayoutOperation.dsf = "$sHour:$sMinute"
                        }
                    }, hour, minutes, true)
            pickerfrom.show()



        }

        tv_view_all!!.setOnClickListener {

            if (Kot_Gloval.listfull!!.size > 3) {
                Kot_Gloval.activity_flag = "schedule"
                var intent = Intent(context, ListDetailsView::class.java)
                startActivity(intent)
            }

        }



        et_date!!.setOnClickListener {

            val now = Calendar.getInstance()
            val dpd = DatePickerDialog(
                    Visit_Schedule@ this,
                    DatePickerDialog.OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        var mo = month
                        ++mo
                        Log.d("Orignal", mo.toString())
                        var date_from = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;
                        et_date!!.setText(date_from)

                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)

            )
            dpd.datePicker.setMinDate(System.currentTimeMillis() - 1000)
            dpd.show()
            //     datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        }

        context = Visit_Schedule@ this
        cd = ConnectionDetector(context)
        dialog = ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)

        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {

            GetVisitScheduleData()
        } else {
            Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")
        }


        rv_visit_shedule?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


    }

    private fun validation() {
        if (et_date!!.getText().length == 0) run {

            Global_Data.Custom_Toast(this@Visit_Schedule, resources.getString(R.string.Select_Date), "yes")

        } else
            if (et_time!!.getText().length == 0) run {

                Global_Data.Custom_Toast(this@Visit_Schedule, resources.getString(R.string.please_Select_time), "yes")

            } else
                if (et_discription!!.getText().length == 0) run {

                    Global_Data.Custom_Toast(this@Visit_Schedule, resources.getString(R.string.please_enter_description), "yes")

                } else
                    if (et_riminder!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Visit_Schedule, resources.getString(R.string.plz_set_reminder), "yes")
                    }
                    else
                        if (spin_time!!.selectedItem.toString().equals("Select Time")) run {

                            Global_Data.Custom_Toast(this@Visit_Schedule, resources.getString(R.string.plz_set_reminder_type), "yes")
                        }
                        else {
                            isInternetPresent = cd!!.isConnectingToInternet
                            if (isInternetPresent) {
                                SyncVisitSchedule()
                            } else {
                                Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")

                            }

                        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp = this@Visit_Schedule.getSharedPreferences("SimpleLogic", 0)
                try {
                    val target = Math.round(sp.getFloat("Target", 0f)).toInt()
                    val achieved = Math.round(sp.getFloat("Achived", 0f)).toInt()
                    val age_float = sp.getFloat("Achived", 0f) / sp.getFloat("Target", 0f) * 100
                    if (age_float.toString().equals("infinity", ignoreCase = true)) {
                        val age = Math.round(age_float!!).toInt()
                        if (Global_Data.rsstr.length > 0) {
                            targetNew = "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [infinity") + "%" + "]"
                            //todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        } else {
                            targetNew = "T/A : Rs " + String.format("$target/$achieved [infinity") + "%" + "]"
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                    } else {
                        val age = Math.round(age_float!!).toInt()
                        if (Global_Data.rsstr.length > 0) {
                            targetNew = "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [$age") + "%" + "]"
                            // todaysTarget.setText();
                        } else {
                            targetNew = "T/A : Rs " + String.format("$target/$achieved [$age") + "%" + "]"
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

                val view: View = findViewById(R.id.add)
                // val yourView = findViewById(R.id.add)
                SimpleTooltip.Builder(this)
                        .anchorView(view)
                        .text(targetNew)
                        .gravity(Gravity.START)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show()

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun GetVisitScheduleData() {

        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()

        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null)!!
        val shop_code: String = sp.getString("shopcode", null)!!

        val Cust_domain = sp.getString("Cust_Service_Url", "")
        var service_url = Cust_domain + "metal/api/v1/"

        try {
            service_url = service_url + "visit_schedules/get_visit_schedule?email=" + URLEncoder.encode(user_email, "UTF-8") + "&type=future" +"&customer_code="+shop_code

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }


        Log.d("Server url", "Server url" + service_url)
        var stringRequest: StringRequest? = null
        stringRequest = StringRequest(service_url,
                Response.Listener { response ->
                    Log.d("jV", "JV length" + response.length)
                    try {
                        val json = JSONObject(JSONTokener(response))
                        try {
                            var response_result = ""
                            if (json.has("result")) {
                                response_result = json.getString("result")

                                dialog!!.hide()
                                // Global_Data.Custom_Toast(context, resources.getString(R.string.Sorry_No_Record_Found), "Yes")
                                //finish().toString()
                            } else {
                                val visits = json.getJSONArray("visits")

                                Log.i("volley", "response visits Length: " + visits.length())

                                Log.d("users", "customer_stocks$visits")

                                list = ArrayList<Visit_Schedule_model>()
                                Kot_Gloval.listfull = ArrayList<Visit_Schedule_model>()
                                for (i in 0 until visits.length()) {
                                    val jsonObject = visits.getJSONObject(i)



                                    if (i < 3) {
                                        list!!.add(Visit_Schedule_model(jsonObject.getString("schedule_date"), jsonObject.getString("schedule_time"), jsonObject.getString("description"), ""))
                                    }
                                    if (i > 2) {
                                        tv_view_all!!.visibility = View.VISIBLE

                                    } else {
                                        tv_view_all!!.visibility = View.GONE

                                    }
                                    Kot_Gloval.listfull!!.add(Visit_Schedule_model(jsonObject.getString("schedule_date"), jsonObject.getString("schedule_time"), jsonObject.getString("description"), ""))

                                }
                                adaptor = Visit_schedule_adaptor(this, list!!)
                                rv_visit_shedule?.adapter = adaptor

                                dialog!!.dismiss()

                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                            //								Intent launch = new Intent(Video_Main_List.this,MainActivity.class);
//								startActivity(launch);
                            finish()
                            dialog!!.dismiss()
                        }
                        dialog!!.dismiss()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        //  finish();
                        dialog!!.dismiss()
                    }
                    dialog!!.dismiss()
                },
                Response.ErrorListener { error ->
                    //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    if (error is TimeoutError || error is NoConnectionError) {
                        Global_Data.Custom_Toast(context,
                                "Network Error","yes")
                    } else if (error is AuthFailureError) {
                        Global_Data.Custom_Toast(context,
                                "Server AuthFailureError  Error","yes")
                    } else if (error is ServerError) {
                        Global_Data.Custom_Toast(context,
                                resources.getString(R.string.Server_Errors),"yes")
                    } else if (error is NetworkError) {
                        Global_Data.Custom_Toast(context,
                                "Network Error","yes")
                    } else if (error is ParseError) {
                        Global_Data.Custom_Toast(context,
                                "ParseError   Error","yes")
                    } else {
                        Global_Data.Custom_Toast(context,
                                error.message,"yes")
                    }
                    //						Intent launch = new Intent(Video_Main_List.this,MainActivity.class);
//						startActivity(launch);
                    finish()
                    dialog!!.dismiss()
                    // finish();
                })
        val requestQueue = Volley.newRequestQueue(context)
        val socketTimeout = 300000 //30 seconds - change to what you want
        val policy: RetryPolicy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.retryPolicy = policy
        // requestQueue.se
//requestQueue.add(jsObjRequest);
        stringRequest.setShouldCache(false)
        requestQueue.cache.clear()
        //requestQueue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest)
    }


    fun SyncVisitSchedule() {
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

            domain = service_url
            var jsObjRequest: JsonObjectRequest? = null
            try {
                Log.d("Server url", "Server url" + domain + "visit_schedules/create_reminder")
                val COLLECTION_RECJARRAY = JSONArray()
                val COLLECTION_RECJOBJECT = JSONObject()
                //val Schedule_JOB = JSONObject()
                COLLECTION_RECJOBJECT.put("latitude", Global_Data.GLOvel_LATITUDE)
                COLLECTION_RECJOBJECT.put("longitude", Global_Data.GLOvel_LONGITUDE)
                COLLECTION_RECJOBJECT.put("schedule_date", et_date!!.text)
                COLLECTION_RECJOBJECT.put("schedule_time", et_time!!.text)
                COLLECTION_RECJOBJECT.put("description", et_discription!!.text)
                COLLECTION_RECJOBJECT.put("reminder_value", et_riminder!!.text)
                COLLECTION_RECJOBJECT.put("reminder_type", spin_time!!.selectedItem.toString().toLowerCase())


                // COLLECTION_RECJARRAY.put(Schedule_JOB)

                // COLLECTION_RECJOBJECT.put("visit_schedules", COLLECTION_RECJARRAY)
                COLLECTION_RECJOBJECT.put("customer_code", shopcode)
                COLLECTION_RECJOBJECT.put("email", user_email)
                COLLECTION_RECJOBJECT.put("device_fcm_key", fcm_token)

                Log.d("schedule Service", COLLECTION_RECJOBJECT.toString())
                jsObjRequest = JsonObjectRequest(Request.Method.POST, domain + "visit_schedules/create_reminder", COLLECTION_RECJOBJECT, Response.Listener { response ->
                    Log.i("volley", "response: $response")
                    Log.d("jV", "JV length" + response.length())
                    try {
                        var response_result = ""
                        response_result = if (response.has("result")) {
                            response.getString("result")
                        } else {
                            "data"
                        }
                        if (response_result.equals("VisitSchedule Saved Successfully", ignoreCase = true)) {
                            dialog!!.dismiss()
                            Global_Data.Custom_Toast(context, response_result, "Yes")
                            val i = Intent(context, Sales_Dash::class.java)
                            startActivity(i)
                            finish()
                        } else {
                            dialog!!.dismiss()
                            Global_Data.Custom_Toast(context, response_result, "Yes")

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

    override fun onBackPressed() {

        // val i = Intent(UpdateStockScreen2@ this, UpdateStockScreen1::class.java)
        // startActivity(i)
        finish()
    }

}
