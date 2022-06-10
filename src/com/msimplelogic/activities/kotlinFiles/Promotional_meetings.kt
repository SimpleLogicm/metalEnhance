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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.MainActivity
import com.msimplelogic.activities.R
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval.Companion.Listresults
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval.Companion.ListresultsHashMap
import com.msimplelogic.adapter.Promotional_meeting_adaptor
import com.msimplelogic.model.Promotional_Model
import cpm.simplelogic.helper.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_day_sheduler.*
import kotlinx.android.synthetic.main.content_planner.*
import kotlinx.android.synthetic.main.content_promotional_meetings.*
import kotlinx.android.synthetic.main.content_promotional_meetings.hedder_theame
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class Promotional_meetings : BaseActivity() {


    var btn_add: ImageView? = null
    var recycler_view: RecyclerView? = null
    var adaptor: Promotional_meeting_adaptor?= null
    var list :ArrayList<Promotional_Model>? = null
    var listFull :ArrayList<Promotional_Model>? = null
    var context: Context? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var dialog: ProgressDialog? = null
    var tab_click_flag = "";
    var sp:SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promotional_meetings)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        context = ActivityTask@ this
        cd = ConnectionDetector(context)
        dialog = ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        Global_Data.Order_hashmap.clear()

        recycler_view=findViewById(R.id.recycler_view)
        btn_add=findViewById(R.id.btn_add)
        recycler_view?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        list = ArrayList<Promotional_Model>()
        listFull =  ArrayList<Promotional_Model>()
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {

            hedder_theame.setImageResource(R.drawable.dark_hedder)
        }


//        adaptor = Promotional_meeting_adaptor(this, list!!)
//        recycler_view?.adapter = adaptor
//        adaptor!!.notifyDataSetChanged()



        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {
           GetMeetingListData()
        } else {
            Global_Data.Custom_Toast(context, "You don't have internet connection.","")
        }

        adaptor = Promotional_meeting_adaptor(this, list!!)
        recycler_view?.adapter = adaptor

        btn_add?.setOnClickListener {
            Global_Data.Order_hashmap.clear()
            Global_Data.PlannerUpdate = "";
            startActivity(Intent(this@Promotional_meetings, Promotional_meeting_add::class.java))
            finish()
        }

        tv_btn_viewhistory!!.setOnClickListener {

            if(listFull!!.size > 3)
            {

                tv_btn_viewhistory!!.visibility = View.GONE
                adaptor = Promotional_meeting_adaptor(this, listFull!!)
                recycler_view?.adapter = adaptor
                adaptor!!.notifyDataSetChanged()
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
                Global_Data.PlannerUpdate = "";
               val i = Intent(this@Promotional_meetings, MainActivity::class.java)
                startActivity(i)
                super.onBackPressed()
            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp = this@Promotional_meetings.getSharedPreferences("SimpleLogic", 0)
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

    fun GetMeetingListData() {

        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()

        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null)!!
        // val shop_code: String = sp.getString("shopcode", null)

        val Cust_domain = sp.getString("Cust_Service_Url", "")
        var service_url = Cust_domain + "metal/api/v1/"

        try {

            service_url = service_url + "promotional_meetings/display_user_meetings?email=" + URLEncoder.encode(user_email, "UTF-8")

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
                               // Global_Data.Custom_Toast(context, response_result, "Yes")
                                dialog!!.hide()

                            } else {


                                Listresults.clear()
                                ListresultsHashMap.clear()
                                Listresults.add(resources.getString(R.string.Select_Type))
                                val meetings = json.getJSONArray("meetings")

                               val meeting_types = json.getJSONArray("meeting_types")

                                Log.i("volley", "response activity_planner Length: " + meetings.length())

                                Log.d("users", "holidays$meetings")

//                                Log.i("volley", "response activity_planner Length: " + leave_types.length())
//
//                                Log.d("users", "activity_planner$leave_types")

                                for (i in 0 until meetings.length()) {
                                    val jsonObject = meetings.getJSONObject(i)

                                    if(i < 3)
                                    {
                                        list!!.add(Promotional_Model(jsonObject.getString("location"),jsonObject.getString("meeting_date"),jsonObject.getString("meeting_time"),jsonObject.getString("name"),jsonObject.getJSONArray("attendees"),jsonObject.getString("meeting_id"),jsonObject.getString("location_latitude"),jsonObject.getString("location_longitude"),jsonObject.getString("meeting_type")))

                                    }


                                   listFull!!.add(Promotional_Model(jsonObject.getString("location"),jsonObject.getString("meeting_date"),jsonObject.getString("meeting_time"),jsonObject.getString("name"),jsonObject.getJSONArray("attendees"),jsonObject.getString("meeting_id"),jsonObject.getString("location_latitude"),jsonObject.getString("location_longitude"),jsonObject.getString("meeting_type")))


                                }

                                for (i in 0 until meeting_types.length()) {
                                    val jsonObject = meeting_types.getJSONObject(i)

                                    Listresults.add(jsonObject.getString("name"));
                                    ListresultsHashMap.put(jsonObject.getString("name"),jsonObject.getString("meeting_type_id"))
                                }

                                adaptor = Promotional_meeting_adaptor(this, list!!)
                                recycler_view?.adapter = adaptor
                                adaptor!!.notifyDataSetChanged()

                                dialog!!.dismiss()

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
        val i = Intent(context, ActivityPlanner::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        finish()
    }
}
