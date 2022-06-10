package com.msimplelogic.activities.kotlinFiles

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.adapter.TourAdapter
import com.msimplelogic.model.TourModel
import cpm.simplelogic.helper.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_tour.*
import kotlinx.android.synthetic.main.content_task.*
import kotlinx.android.synthetic.main.content_tour.*
import kotlinx.android.synthetic.main.content_tour.hedder_theame
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class ActivityTour : BaseActivity() {
    private var adapter: TourAdapter? = null
    private var albumList: MutableList<TourModel>? = null
    private var albumListnew: MutableList<TourModel>? = null
    var sp : SharedPreferences?=null

    var textview_date: TextView? = null
    var cal = Calendar.getInstance()
    var activity_flag = ""
    var clear: Button? = null
    var context: Context? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        context = ActivityTask@ this
        cd = ConnectionDetector(context)
        dialog = ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        clear = findViewById(R.id.clear);
        tourplus_btn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@ActivityTour, ActivityTaskDetails::class.java))
            Global_Data.PlannerName = "Tour"
        })
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {


            hedder_theame.setImageResource(R.drawable.dark_hedder)

//ll.setBackgroundResource(R.drawable.dark_theme_background);
        }

//        search_variants.setOnClickListener(View.OnClickListener {
//            DatePickerDialog(this@ActivityTour,
//                    dateSetListener,
//                    // set DatePickerDialog to point to today's date when it loads up
//                    cal.get(Calendar.YEAR),
//                    cal.get(Calendar.MONTH),
//                    cal.get(Calendar.DAY_OF_MONTH)).show()
//
//        })

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                var day = String.format("%02d", dayOfMonth)


                var mo = monthOfYear
                ++mo
                Log.d("Orignal", mo.toString())
                //var date_from = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;
                var date_from = "$day ${Global_Data.getMonth(mo)} $year";
                updateDateInView(date_from)
            }
        }

        search_variants!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@ActivityTour,
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        clear!!.setOnClickListener {
            finish();
            startActivity(getIntent());
        }


        albumList = java.util.ArrayList()
        adapter = TourAdapter(this, albumList as java.util.ArrayList<TourModel>)

        val mLayoutManager = LinearLayoutManager(this)
        tour_list!!.layoutManager = mLayoutManager
        tour_list.addItemDecoration(DividerItemDecoration(this@ActivityTour, LinearLayoutManager.VERTICAL))
        tour_list!!.itemAnimator = DefaultItemAnimator()
        tour_list!!.adapter = adapter

        //populateList()

        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {

            GetActivityPlannerData()
        } else {
            Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")
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
                val sp: SharedPreferences = this@ActivityTour.getSharedPreferences("SimpleLogic", 0)
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


//    private fun populateList(){
//        var a = TourModel("Banglore Trip", "20 Feb - 27 Feb")
//        albumList!!.add(a)
//
//        a = TourModel("Jaipur Trip ", "11 Feb - 13 Feb")
//        albumList!!.add(a)
//
//        a = TourModel("Jalpur Trip", "11 Feb - 13 Feb")
//        albumList!!.add(a)
//
//    }

    private fun updateDateInView(dates: String) {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        search_variants!!.text = dates
        var recordcheck = ""
        val name: String = search_variants.getText().toString()
        albumListnew = java.util.ArrayList()
        albumListnew!!.clear()
        var getData = HashMap<String, String>()
        for (i in albumList!!.indices) {
            //var datess = albumList!!.get(i).from_date.split(" ")
            var datess = albumList!!.get(i).from_date
            var datessto = albumList!!.get(i).to_date
            //val dates_f = datess[1] + " "+datess[2]
            if (dates.equals(datess, ignoreCase = true)) {

                var a = TourModel(albumList!!.get(i).cityname_trip, albumList!!.get(i).date_trip, albumList!!.get(i).from_date, albumList!!.get(i).to_date, albumList!!.get(i).mode, albumList!!.get(i).ticket_no, albumList!!.get(i).details)

                albumListnew!!.add(a)

                adapter = TourAdapter(this, albumListnew as java.util.ArrayList<TourModel>)
                tour_list!!.adapter = adapter
                adapter!!.notifyDataSetChanged()
                recordcheck = "yes";
                clear!!.visibility = View.VISIBLE
                break
            } else if (dates.equals(datessto, ignoreCase = true)) {
                var a = TourModel(albumList!!.get(i).cityname_trip, albumList!!.get(i).date_trip, albumList!!.get(i).from_date, albumList!!.get(i).to_date, albumList!!.get(i).mode, albumList!!.get(i).ticket_no, albumList!!.get(i).details)

                albumListnew!!.add(a)

                adapter = TourAdapter(this, albumListnew as java.util.ArrayList<TourModel>)
                tour_list!!.adapter = adapter
                adapter!!.notifyDataSetChanged()
                recordcheck = "yes";
                clear!!.visibility = View.VISIBLE

                break
            }
        }


        if (!recordcheck.equals("yes")) {
            Global_Data.Custom_Toast(this@ActivityTour, "Selected date Tour data not found.", "Yes")
            search_variants!!.setText("")
            clear!!.visibility = View.GONE
            finish();
            startActivity(getIntent());
        }


    }

    fun GetActivityPlannerData() {

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
            service_url = service_url + "tour_programs/get_tour_program?email=" + URLEncoder.encode(user_email, "UTF-8")


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

                            } else {


                                val activities = json.getJSONArray("programs")

                                Log.i("volley", "response programs Length: " + activities.length())

                                Log.d("users", "programs$activities")

                                for (i in 0 until activities.length()) {
                                    val jsonObject = activities.getJSONObject(i)
                                    var from_d = jsonObject.getString("from_date").split(" ")
                                    var to_d = jsonObject.getString("to_date").split(" ")

                                    val from_f = from_d[1] + " " + from_d[2]
                                    val to_f = to_d[1] + " " + to_d[2]

                                    var a = TourModel(jsonObject.getString("name"), jsonObject.getString("from_date") + " - " + jsonObject.getString("to_date"), jsonObject.getString("from_date"), jsonObject.getString("to_date"), jsonObject.getString("mode"), jsonObject.getString("ticket_no"), jsonObject.getString("details"))
                                    albumList!!.add(a)


                                }

                                adapter!!.notifyDataSetChanged()

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
                    if (error is TimeoutError || error is NoConnectionError) { //							Toast.makeText(Video_Main_List.this,
//									"Network Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                "Network Error","yes")
                    } else if (error is AuthFailureError) { //							Toast.makeText(Video_Main_List.this,
//									"Server AuthFailureError  Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                "Server AuthFailureError  Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                "Server AuthFailureError  Error","yes")
                    } else if (error is ServerError) { //							Toast.makeText(Video_Main_List.this,
//									"Server   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                resources.getString(R.string.Server_Errors),
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                resources.getString(R.string.Server_Errors),"yes")
                    } else if (error is NetworkError) { //							Toast.makeText(Video_Main_List.this,
//									"Network   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                "Network Error","yes")
                    } else if (error is ParseError) { //							Toast.makeText(Video_Main_List.this,
//									"ParseError   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                "ParseError   Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                "ParseError   Error","yes")
                    } else { //Toast.makeText(Video_Main_List.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                error.message,
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
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


    override fun onBackPressed() { // TODO Auto-generated method stub
        val i = Intent(this@ActivityTour, ActivityPlanner::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        finish()
    }
}