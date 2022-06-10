package com.msimplelogic.activities.kotlinFiles

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import com.msimplelogic.adapter.Visit_schedule_adaptor
import com.msimplelogic.model.Visit_Schedule_model
import com.msimplelogic.webservice.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip

import kotlinx.android.synthetic.main.activity_visit_schedule_history.*
import kotlinx.android.synthetic.main.content_visit__schedule.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class Visit_schedule_history : BaseActivity() {
    var list: ArrayList<Visit_Schedule_model>? = null
    var adaptor: Visit_schedule_adaptor? = null
    var dialog: ProgressDialog? = null
    var context: Context? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var rv_visit_shedule: RecyclerView? = null
var sp:SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_schedule_history)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        rv_visit_shedule = findViewById(R.id.rv_visit_shedule)

        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
//            val img: Drawable =   context!!.resources.getDrawable(R.drawable.ic_baseline_date_range_24_dark)
//
//            et_date!!.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
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
            service_url = service_url + "visit_schedules/get_visit_schedule?email=" + URLEncoder.encode(user_email, "UTF-8")+"&type=past"+"&customer_code="+shop_code

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
                                 Global_Data.Custom_Toast(context, resources.getString(R.string.Sorry_No_Record_Found), "Yes")
                                finish().toString()

                            } else {
                                val visits = json.getJSONArray("visits")

                                Log.i("volley", "response visits Length: " + visits.length())

                                Log.d("users", "customer_stocks$visits")

                                list = ArrayList<Visit_Schedule_model>()
                               // Kot_Gloval.listfull = ArrayList<Visit_Schedule_model>()
                                for (i in 0 until visits.length()) {
                                    val jsonObject = visits.getJSONObject(i)




                                    list!!.add(Visit_Schedule_model(jsonObject.getString("schedule_date"), jsonObject.getString("schedule_time"), jsonObject.getString("description"), ""))

                                }
                                adaptor = Visit_schedule_adaptor(this@Visit_schedule_history, list!!)
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
                                error.message,"Yes")
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
                val sp = this@Visit_schedule_history.getSharedPreferences("SimpleLogic", 0)
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

         val i = Intent(this@Visit_schedule_history, Visit_Schedule::class.java)
         startActivity(i)
        finish()
    }
}
