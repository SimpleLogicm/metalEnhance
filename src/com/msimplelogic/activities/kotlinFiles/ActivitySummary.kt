package com.msimplelogic.activities.kotlinFiles

import  com.msimplelogic.activities.R
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import com.msimplelogic.adapter.AdapterSummary
import com.msimplelogic.model.SummaryModel
import com.msimplelogic.webservice.ConnectionDetector
import com.view.calender.horizontal.umar.horizontalcalendarview.DayDateMonthYearModel
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarListener
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarView
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_summary.*
import kotlinx.android.synthetic.main.content_summary.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ActivitySummary : BaseActivity(), HorizontalCalendarListener {
   // private var viewTargetList: MutableList<SummaryModel>? = null
    lateinit var currentMonthTextView : TextView
    private var summaryAdapter: AdapterSummary? = null
    var dialog: ProgressDialog? = null
    var listSummary: ArrayList<SummaryModel>? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var dateStr:String?=null
    var value=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")

        currentMonthTextView = findViewById(R.id.month)
        val  hcv = findViewById<HorizontalCalendarView>(R.id.horizontalcalendarview)
        hcv.setContext(this@ActivitySummary)
        hcv.setBackgroundColor(resources.getColor(R.color.white))
        hcv.showControls(true)
        hcv.setControlTint(R.color.light_grey)
        hcv.changeAccent(R.color.black)

        cd = ConnectionDetector(ActivitySummary@this)
        listSummary = java.util.ArrayList()
        summaryAdapter = AdapterSummary(this, listSummary as ArrayList<SummaryModel>)
        dialog = ProgressDialog(ActivitySummary@this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        var currentDate=sdf.format(Date())
        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {
            SummaryResult(currentDate)
        }
        else
        {
            Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error), "Yes")
        }

//        isInternetPresent = cd!!.isConnectingToInternet
//        if (isInternetPresent) {
//            SummaryResult("")
//        }
//        else
//        {
//            Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error), "Yes")
//        }

//        activities_btn.setOnClickListener {
//            startActivity(Intent(this@ActivitySummary, MyActivities::class.java))
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//        }

        val mLayoutManager = LinearLayoutManager(this)
        summary_list!!.layoutManager = mLayoutManager
        //viewtarget_list.addItemDecoration(DividerItemDecoration(this@ActivityViewTarget, LinearLayoutManager.VERTICAL))
        summary_list!!.itemAnimator = DefaultItemAnimator()
        summary_list!!.adapter = summaryAdapter

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
                val sp: SharedPreferences = this@ActivitySummary.getSharedPreferences("SimpleLogic", 0)
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

    override fun onBackPressed() { // TODO Auto-generated method stub
        val i = Intent(this@ActivitySummary, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i)
        finish()
    }


    override fun updateMonthOnScroll(selectedDate: DayDateMonthYearModel?) {
        currentMonthTextView.text = ""+ selectedDate?.month + " " + selectedDate?.year
    }

    override fun newDateSelected(selectedDate: DayDateMonthYearModel?) {
        dateStr=selectedDate?.date+"-"+selectedDate?.monthNumeric+"-"+selectedDate?.year

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        var currentDate=sdf.format(Date())

        if(currentDate.equals(dateStr))
        {
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                SummaryResult(currentDate!!)
            }
            else
            {
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error), "Yes")
            }

        }else{
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                SummaryResult(dateStr!!)
            }
            else
            {
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error), "Yes")
            }
        }



       // Toast.makeText(this@ActivitySummary ,dateStr , Toast.LENGTH_LONG).show()
    }

//    private fun viewTargetList(){
//        var a = SummaryModel("Mohan Electricals", "Link Road, Bandra","$20000","Order")
//        viewTargetList!!.add(a)
//
//        a = SummaryModel("Mohan Electricals", "Link Road, Bandra", "0","No Order")
//        viewTargetList!!.add(a)
//
//        a = SummaryModel("Mohan Electricals", "Link Road, Bandra","$2000","Order")
//        viewTargetList!!.add(a)
//    }

    fun SummaryResult(date1:String) {
        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()


        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null)!!
      //  val shop_code: String = sp.getString("shopcode", null)

        val Cust_domain = sp.getString("Cust_Service_Url", "")
        var service_url = Cust_domain + "metal/api/v1/"

        try {
            service_url = service_url + "users/get_fields_sales_summary?email=" + (URLEncoder.encode(user_email, "UTF-8"))+"&date="+date1
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
                                //finish().toString()
                                this@ActivitySummary.runOnUiThread(java.lang.Runnable {
                                //    listSummary!!.clear()
                              summaryAdapter!!.notifyDataSetChanged()
                              })

                                //finish()
                            } else {

                                val dfgsg:String=json.getString("shop_visited_count")
                                val dfgsddg:String=json.getString("shop_visited_count")

                                tv_shopvisited.setText(json.getString("shop_visited_count"))
                                tv_ordertaken.setText("₹ " + json.getString("total_order_amount"))

                                val summaryData = json.getJSONArray("records")
                                Log.i("volley", "response visits Length: " + summaryData.length())
                                Log.d("users", "customer_stocks$summaryData")

                                if(summaryData.length()>0) {

                                        listSummary = ArrayList<SummaryModel>()
                                        listSummary!!.clear()
                                        for (i in 0 until summaryData.length()) {
                                            val jsonObject = summaryData.getJSONObject(i)
                                            listSummary!!.add(SummaryModel(jsonObject.getString("shop_name"), jsonObject.getString("location"), "₹ " + jsonObject.getString("amount"),jsonObject.getString("summary_type")))
                                        }
                                        summaryAdapter = AdapterSummary(this, listSummary!!)
                                        summary_list?.adapter = summaryAdapter
                                        dialog!!.dismiss()

                                }else{
                                    Global_Data.Custom_Toast(this, resources.getString(R.string.Sorry_No_Record_Found), "Yes")
                                    listSummary!!.clear()
                                    summaryAdapter = AdapterSummary(this, listSummary!!)
                                    summary_list?.adapter = summaryAdapter
                                    summaryAdapter!!.notifyDataSetChanged()
//                                    this@ActivitySummary.runOnUiThread(java.lang.Runnable {
//                                        listSummary!!.clear()
//                                        summaryAdapter = AdapterSummary(this, listSummary!!)
//                                        summary_list?.adapter = summaryAdapter
//                                       summaryAdapter!!.notifyDataSetChanged()
//                                    })
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
//                        toast.show()
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
        requestQueue.add(stringRequest);
        //AppController.getInstance().addToRequestQueue(stringRequest)
    }
}