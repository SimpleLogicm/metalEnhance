package com.msimplelogic.activities.kotlinFiles

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
import com.github.vipulasri.timelineview.TimelineView
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.MainActivity
import com.msimplelogic.activities.R
import com.msimplelogic.adapter.AdapterMyActivities
import com.msimplelogic.adapter.AdapterSummary
import com.msimplelogic.model.MyActivityModel
import com.msimplelogic.model.SummaryModel
import com.msimplelogic.webservice.ConnectionDetector
import com.view.calender.horizontal.umar.horizontalcalendarview.DayDateMonthYearModel
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarListener
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarView
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_myactivities.*
import kotlinx.android.synthetic.main.content_myactivites.*
import kotlinx.android.synthetic.main.content_myactivites.summary_btn
import kotlinx.android.synthetic.main.content_myactivites.summary_list
import kotlinx.android.synthetic.main.content_summary.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*


class MyActivities : BaseActivity(), HorizontalCalendarListener {
    private var viewTargetList: MutableList<MyActivityModel>? = null
    lateinit var currentMonthTextView : TextView
    private var summaryAdapter: AdapterMyActivities? = null
    var dialog: ProgressDialog? = null
    var listMyActivity: ArrayList<MyActivityModel>? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var dateStr:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myactivities)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")

        currentMonthTextView = findViewById(R.id.month)
        val  hcv = findViewById<HorizontalCalendarView>(R.id.horizontalcalendarview)
        hcv.setContext(this@MyActivities)
        hcv.setBackgroundColor(resources.getColor(R.color.white))
        hcv.showControls(true)
        hcv.setControlTint(R.color.light_grey)
        hcv.changeAccent(R.color.black)

        viewTargetList = java.util.ArrayList()
        summaryAdapter = AdapterMyActivities(this, viewTargetList as ArrayList<MyActivityModel>)

        val mLayoutManager = LinearLayoutManager(this)
        summary_list!!.layoutManager = mLayoutManager
        //viewtarget_list.addItemDecoration(DividerItemDecoration(this@ActivityViewTarget, LinearLayoutManager.VERTICAL))
        summary_list!!.itemAnimator = DefaultItemAnimator()
        summary_list!!.adapter = summaryAdapter

        viewTargetList()

        summary_btn.setOnClickListener {
            startActivity(Intent(this@MyActivities, ActivitySummary::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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
                val sp: SharedPreferences = this@MyActivities.getSharedPreferences("SimpleLogic", 0)
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
        val i = Intent(this@MyActivities, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i)
        finish()
    }

    override fun updateMonthOnScroll(selectedDate: DayDateMonthYearModel?) {
        currentMonthTextView.text = ""+ selectedDate?.month + " " + selectedDate?.year

    }

    override fun newDateSelected(selectedDate: DayDateMonthYearModel?) {
       // Toast.makeText(this@MyActivities ,selectedDate?.date +""+ selectedDate?.month + " " + selectedDate?.year , Toast.LENGTH_LONG).show()

        Global_Data.Custom_Toast(this@MyActivities ,selectedDate?.date +""+ selectedDate?.month + " " + selectedDate?.year ,"")
    }
    private fun viewTargetList(){
        var a = MyActivityModel("12:15 pm", "Link Road, Bandra","0.02 km distance covered","0 shop visited")
        viewTargetList!!.add(a)

        a = MyActivityModel("12:57 pm", "Link Road, Bandra", "0.02 km distance covered","0 shop visited")
        viewTargetList!!.add(a)

        a = MyActivityModel("1:50 pm", "Link Road, Bandra","0.02 km distance covered","0 shop visited")
        viewTargetList!!.add(a)

    }

    fun SummaryResult(type: String) {
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
            service_url = service_url + "customer_traits/customer_traits_order?email=" + (URLEncoder.encode(user_email, "UTF-8"))+"&customer_code="+shop_code+"&type="+type
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
//                                listMyActivity!!.clear()
//                                summaryAdapter!!.notifyDataSetChanged()
//                                this@MyActivities.runOnUiThread(java.lang.Runnable {
//                                    listMyActivity!!.clear()
//                              summaryAdapter!!.notifyDataSetChanged()
//                                })
                                finish()

                             //   summary_list!!.visibility=View.GONE

                            } else {
                              //  summary_list!!.visibility=View.VISIBLE

                                val visits = json.getJSONArray("data")
                                Log.i("volley", "response visits Length: " + visits.length())
                                Log.d("users", "customer_stocks$visits")

                                if(visits.length()>0) {
                                    //if (type.equals("quotation")) {

                                    listMyActivity = ArrayList<MyActivityModel>()
                                    for (i in 0 until visits.length()) {
                                        val jsonObject = visits.getJSONObject(i)
                                        listMyActivity!!.add(MyActivityModel("Quote No : " + jsonObject.getString("quote_number"), jsonObject.getString("create_date"), "Status : " + jsonObject.getString("status"),""))
                                    }
                                    summaryAdapter = AdapterMyActivities(this, listMyActivity!!)
                                    summary_list?.adapter = summaryAdapter
                                    dialog!!.dismiss()

                                    //}
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
                    if (error is TimeoutError || error is NoConnectionError) {
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@this,
                                "Network Error","yes")
                    } else if (error is AuthFailureError) {
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@this,
                                "Server AuthFailureError  Error","yes")
                    } else if (error is ServerError) {
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@this,
                                resources.getString(R.string.Server_Errors),"yes")
                    } else if (error is NetworkError) {
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@this,
                                "Network Error","yes")
                    } else if (error is ParseError) {
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@this,
                                "ParseError   Error","yes")
                    } else {
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