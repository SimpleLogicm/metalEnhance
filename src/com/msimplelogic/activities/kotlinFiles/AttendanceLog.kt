package com.msimplelogic.activities.kotlinFiles

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.BasicMapDemoActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.adapter.Attendance_Adaptor
import com.msimplelogic.model.AttendanceModel
import com.msimplelogic.webservice.ConnectionDetector
import com.whiteelephant.monthpicker.MonthPickerDialog
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_attendance_log.*
import kotlinx.android.synthetic.main.content_attendance_log.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.DateFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class AttendanceLog : BaseActivity() {
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var recycler_view: RecyclerView? = null
    var tv_date: TextView? = null
    var Img_date: ImageView? = null
    var list: ArrayList<AttendanceModel>? = null
    var tv_total: TextView? = null
    var adaptor :Attendance_Adaptor?=null
    var selected_month : Int = 0
    var dialog: ProgressDialog? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_log)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //setTitle(resources.getString(R.string.Attendance_Log))
        setTitle("")

        recycler_view = findViewById(R.id.recycler_view)
        tv_date = findViewById(R.id.tv_date)
        Img_date = findViewById(R.id.Img_date)
        tv_total = findViewById(R.id.tv_total)
        cd = ConnectionDetector(AttendanceLog@this)
        Img_date?.setOnClickListener { }

        recycler_view?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recycler_view?.addItemDecoration(DividerItemDecoration(recycler_view?.getContext(), DividerItemDecoration.VERTICAL))
        list= ArrayList<AttendanceModel>()

        adaptor = Attendance_Adaptor(this, list!!)
        recycler_view?.adapter = adaptor

        dialog = ProgressDialog(Customer_Traits@this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {

            AttendanceLogResult("","")
        }
        else
        {
            Global_Data.Custom_Toast(CustomerServicesTraits@this, resources.getString(R.string.internet_connection_error), "Yes")
        }

        alog_date.setOnClickListener {
            val builder = MonthPickerDialog.Builder(this@AttendanceLog, MonthPickerDialog.OnDateSetListener {selectedMonth, selectedYear ->
                Log.d("RDATE","RDATE"+selectedMonth+" "+selectedYear);
                val years = Calendar.getInstance().get(Calendar.YEAR);
                selected_month = selectedMonth;
                val months = DateFormatSymbols().getMonths()[selectedMonth]
                at_tv_date.setText("$months $years")

                dialog = ProgressDialog(Customer_Traits@this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                if (isInternetPresent) {

                    AttendanceLogResult(months,years.toString())
                }
                else
                {
                    Global_Data.Custom_Toast(CustomerServicesTraits@this, resources.getString(R.string.internet_connection_error), "Yes")
                }


            },  /* activated number in year */Calendar.YEAR, Calendar.MONTH)
                    .setActivatedMonth(Calendar.MONTH+2)
                    .setActivatedYear(Calendar.YEAR)
            builder.showMonthOnly()
                    .build()
                    .show()

            Log.i("calender","calender"+Calendar.MONTH.toString())

        }

        att_mapat.setOnClickListener {
            val i = Intent(this@AttendanceLog, BasicMapDemoActivity::class.java)
            startActivity(i)
        }

        att_shftro.setOnClickListener {
//            val i = Intent(this@AttendanceLog, ShiftRoaster::class.java)
//            startActivity(i)

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
                val sp = this@AttendanceLog.getSharedPreferences("SimpleLogic", 0)
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

    override fun onBackPressed() {
        val i = Intent(this@AttendanceLog, BasicMapDemoActivity::class.java)
        startActivity(i)
        super.onBackPressed()
    }

   fun AttendanceLogResult(month:String,year:String) {
        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()

        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null)!!

        val Cust_domain = sp.getString("Cust_Service_Url", "")
        var service_url = Cust_domain + "metal/api/v1/"

       var selectMonth:String?=null

       if (month.equals("January", ignoreCase = true)) {
           selectMonth = "01"
       } else if (month.equals("February", ignoreCase = true)) {
           selectMonth = "02"
       } else if (month.equals("March", ignoreCase = true)) {
           selectMonth = "03"
       } else if (month.equals("April", ignoreCase = true)) {
           selectMonth = "04"
       } else if (month.equals("May", ignoreCase = true)) {
           selectMonth = "05"
       } else if (month.equals("June", ignoreCase = true)) {
           selectMonth = "06"
       } else if (month.equals("July", ignoreCase = true)) {
           selectMonth = "07"
       } else if (month.equals("August", ignoreCase = true)) {
           selectMonth = "08"
       } else if (month.equals("September", ignoreCase = true)) {
           selectMonth = "09"
       } else if (month.equals("October", ignoreCase = true)) {
           selectMonth = "10"
       } else if (month.equals("November", ignoreCase = true)) {
           selectMonth = "11"
       } else if (month.equals("December", ignoreCase = true)) {
           selectMonth = "12"
       }

        try {
            if(month.length>0)
            {
                service_url = service_url + "attendances/user_attendance_log?email=" + (URLEncoder.encode(user_email, "UTF-8"))+"&month="+selectMonth+"&year="+year
            }else{
                service_url = service_url + "attendances/user_attendance_log?email=" + (URLEncoder.encode(user_email, "UTF-8"))
            }

            Log.i("service "," "+service_url);
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
                                Global_Data.Custom_Toast(this, resources.getString(R.string.Sorry_No_Record_Found), "Yes")
                                list!!.clear()
                                adaptor!!.notifyDataSetChanged()
                                //finish()
                            } else {

                                val attendanceLog = json.getJSONArray("attendance_log")
                                Log.i("volley", "response attendanceLog Length: " + attendanceLog.length())
                                Log.d("users", "attendanceLog $attendanceLog")
                                list?.clear()
                                if(attendanceLog.length()>0) {

                                        for (i in 0 until attendanceLog.length()) {
                                            val jsonObject = attendanceLog.getJSONObject(i)
                                            list?.add(AttendanceModel(jsonObject.getString("punched_on"), "",  jsonObject.getString("in_time"),jsonObject.getString("out_time"),jsonObject.getString("total_hours_worked")))
                                        }

                                        adaptor = Attendance_Adaptor(this, list!!)
                                        recycler_view?.adapter = adaptor

                                        dialog!!.dismiss()

                                }
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
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
                    if (error is TimeoutError || error is NoConnectionError) { //							Toast.makeText(Video_Main_List.this,
//									"Network Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@this,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@this,
                                "Network Error","yes")
                    } else if (error is AuthFailureError) { //							Toast.makeText(Video_Main_List.this,
//									"Server AuthFailureError  Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@this,
//                                "Server AuthFailureError  Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@this,
                                "Server AuthFailureError  Error","yes")
                    } else if (error is ServerError) { //							Toast.makeText(Video_Main_List.this,
//									"Server   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@this,
//                                resources.getString(R.string.Server_Errors),
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@this,
                                resources.getString(R.string.Server_Errors),"yes")
                    } else if (error is NetworkError) { //							Toast.makeText(Video_Main_List.this,
//									"Network   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@this,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@this,
                                "Network Error","yes")
                    } else if (error is ParseError) { //							Toast.makeText(Video_Main_List.this,
//									"ParseError   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@this,
//                                "ParseError   Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
                      //  toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@this,
                                "ParseError   Error","yes")
                    } else { //Toast.makeText(Video_Main_List.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@this,
//                                error.message,
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@this,
                                error.message,"yes")
                    }
                    //						Intent launch = new Intent(Video_Main_List.this,MainActivity.class);
//						startActivity(launch);
                    finish()
                    dialog!!.dismiss()
                    // finish();
                })
        val requestQueue = Volley.newRequestQueue(QuoteAddRetailerTrait@this)
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
}
