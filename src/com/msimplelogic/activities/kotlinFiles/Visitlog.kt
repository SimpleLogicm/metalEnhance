package com.msimplelogic.activities.kotlinFiles
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import com.msimplelogic.activities.R
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import com.msimplelogic.adapter.Visitlogadaptor
import com.msimplelogic.model.Visitlog_model
import com.msimplelogic.services.StartLocationAlert
import com.msimplelogic.webservice.ConnectionDetector
import com.view.calender.horizontal.umar.horizontalcalendarview.DayDateMonthYearModel
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarListener
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarView
import cpm.simplelogic.helper.GPSTracker
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_day_sheduler.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Visitlog : BaseActivity(), HorizontalCalendarListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    var recycler_view1: RecyclerView? = null
    var isInternetPresent = false
    var lat: Double? = null
    var longi: Double? = null
    var str: StringBuilder? = null

    var cd: ConnectionDetector? = null
    private var dataadaptor: Visitlogadaptor? = null
    var dateStr: String? = null
    var latLng: LatLng? = null
    var currLocationMarker: Marker? = null
    var sp: SharedPreferences? = null
    var sp2: SharedPreferences? = null
    var mGoogleMap: GoogleMap? = null
    var at_in: Button? = null
    var context: Context? = null
    var at_out: Button? = null
    var hedder_theame: ImageView? = null
    lateinit var currentMonthTextView: TextView
    var dialog: ProgressDialog? = null
    var list: ArrayList<Visitlog_model>? = null
    var gps: GPSTracker? = null
    var serviceurlsubmit: String? = null
    var mLocationRequest: LocationRequest? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var device_dialog: Dialog? = null
    var timecurrent: TextView? = null
    var chekoutbtn: Button? = null
    var cancel: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visitlog)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        context = this@Visitlog
        dialog = ProgressDialog(this@Visitlog, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        recycler_view1 = findViewById(R.id.recycler_view1)
        at_in = findViewById<Button>(R.id.at_in)
        at_out = findViewById<Button>(R.id.at_out)
        currentMonthTextView = findViewById(R.id.month1)
        hedder_theame = findViewById(R.id.hedder_theame)
        val hcv1 = findViewById<HorizontalCalendarView>(R.id.horizontalcalendarview1)
        hcv1.setContext(this@Visitlog)
        hcv1.setBackgroundColor(resources.getColor(R.color.white))
        hcv1.showControls(true)
        hcv1.setControlTint(R.color.light_grey)
        hcv1.changeAccent(R.color.black)


        val bundle = intent.extras
        Log.i("exep","exep"+bundle);

        if (bundle != null) {
            val text11 = bundle.getString("Notification_intent")

            Log.i("exep","exep"+text11);


            if (text11.equals("SHOW")) {



                try {
                    if (device_dialog != null && device_dialog!!.isShowing) {
                        device_dialog!!.dismiss()
                    }



                    device_dialog = Dialog(this@Visitlog)
                    device_dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    device_dialog!!.setCancelable(false)
                    device_dialog!!.setContentView(R.layout.visitlogoutdialog)
                    timecurrent = device_dialog!!.findViewById(R.id.timecurrent);
                    cancel = device_dialog!!.findViewById<View>(R.id.snooz) as Button
                    chekoutbtn = device_dialog!!.findViewById<View>(R.id.logout) as Button
                    device_dialog!!.show()

                } catch (e: Exception) {
                    Log.i("exep","exep"+e.toString());
                    e.printStackTrace()
                }

                val sdf2 = SimpleDateFormat("hh:mm a")
                val date = Date()
                val daten2 = sdf2.format(date)

                timecurrent!!.setText(daten2)

                cancel!!.setOnClickListener {
                    device_dialog!!.cancel()
                }

                chekoutbtn!!.setOnClickListener {
                    isInternetPresent = cd!!.isConnectingToInternet()
                    if (isInternetPresent) {
                        gps = GPSTracker(this@Visitlog)
                        if (!gps!!.canGetLocation()) {
                            //  gps_redirection_flag = "in"
                            val mContext: Activity = this@Visitlog  //change this your activity name
                            val startLocationAlert = StartLocationAlert(mContext)
                            //startLocationAlert.
                        } else {
                            //AttinCall()
                            device_dialog!!.dismiss()
                            Atoutcall()
                        }
                    } else {
                        device_dialog!!.dismiss()
                        Global_Data.Custom_Toast(this@Visitlog, "You don't have internet connection.", "")
                    }


                }


            }


        }


//        val spf: SharedPreferences = this@Visitlog.getSharedPreferences("SimpleLogic", 0)
//        val time = spf.getString("Logouthour", null)
//
//        if (time != null) {
//            try {
//                val periodicWorkRequest = PeriodicWorkRequest.Builder(
//                        MyPeriodicwork::class.java, time.toLong() * 60, TimeUnit.MINUTES
//                ).addTag("otpValidator").build()
//                WorkManager.getInstance().enqueueUniquePeriodicWork("Work",
//                        ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest)
//            } catch (ex: Exception) {
//                ex.printStackTrace()
//            }
//
//        }


        sp2 = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp2!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {

            hedder_theame!!.setImageResource(R.drawable.dark_hedder)

//ll.setBackgroundResource(R.drawable.dark_theme_background);
        }



        cd = ConnectionDetector(Visitlog@ this)

        at_in!!.setOnClickListener {

            isInternetPresent = cd!!.isConnectingToInternet()
            if (isInternetPresent) {
                gps = GPSTracker(this@Visitlog)
                if (!gps!!.canGetLocation()) {
                    //  gps_redirection_flag = "in"
                    val mContext: Activity = this@Visitlog  //change this your activity name
                    val startLocationAlert = StartLocationAlert(mContext)
                    //startLocationAlert.
                } else {
                    AttinCall()
                }
            } else {
                Global_Data.Custom_Toast(this@Visitlog, "You don't have internet connection.", "")
            }

        }

        at_out!!.setOnClickListener {
            isInternetPresent = cd!!.isConnectingToInternet()
            if (isInternetPresent) {
                gps = GPSTracker(this@Visitlog)
                if (!gps!!.canGetLocation()) {
                    //  gps_redirection_flag = "in"
                    val mContext: Activity = this@Visitlog  //change this your activity name
                    val startLocationAlert = StartLocationAlert(mContext)
                    //startLocationAlert.
                } else {
                    //AttinCall()
                    Atoutcall()
                }
            } else {
                Global_Data.Custom_Toast(this@Visitlog, "You don't have internet connection.", "")
            }
        }
        list = ArrayList<Visitlog_model>()

        dataadaptor = Visitlogadaptor(this, list as java.util.ArrayList<Visitlog_model>)

        val mLayoutManager = LinearLayoutManager(this)
        recycler_view1!!.layoutManager = mLayoutManager
        //viewtarget_list.addItemDecoration(DividerItemDecoration(this@ActivityViewTarget, LinearLayoutManager.VERTICAL))
        recycler_view1!!.itemAnimator = DefaultItemAnimator()
        recycler_view1!!.adapter = dataadaptor



        list!!.clear()

//        list!!.add(Visitlog_model("01","New Test","09:20 Am","12:20 Pm"))
//        list!!.add(Visitlog_model("01","New Test","09:20 Am","12:20 Pm"))
//        list!!.add(Visitlog_model("01","New Test","09:20 Am","12:20 Pm"))
//        list!!.add(Visitlog_model("01","New Test","09:20 Am","12:20 Pm"))
//        list!!.add(Visitlog_model("01","New Test","09:20 Am","12:20 Pm"))
//        list!!.add(Visitlog_model("01","New Test","09:20 Am","12:20 Pm"))
//        list!!.add(Visitlog_model("01","New Test","09:20 Am","12:20 Pm"))
//
//        dataadaptor = Visitlogadaptor(this, list as java.util.ArrayList<Visitlog_model>)
//
//        val mLayoutManager = LinearLayoutManager(this)
//        recycler_view1!!.layoutManager = mLayoutManager
//        //viewtarget_list.addItemDecoration(DividerItemDecoration(this@ActivityViewTarget, LinearLayoutManager.VERTICAL))
//        recycler_view1!!.itemAnimator = DefaultItemAnimator()
//        recycler_view1!!.adapter = dataadaptor
//


        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var currentDate = sdf.format(Date())


        isInternetPresent = cd!!.isConnectingToInternet


//        if (isInternetPresent) {
//            showdata(currentDate)
//        } else {
//            Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error), "Yes")
//        }


    }

    private fun Atoutcall() {


        val spf2: SharedPreferences = this@Visitlog.getSharedPreferences("SimpleLogic", 0)
        val timeout = spf2.getString("Dateout", null)

        if (timeout != null) {

            Global_Data.Custom_Toast(applicationContext, "Please Check-In first", "Yes")


        } else {
            val appLocationManager = AppLocationManager(this@Visitlog)


            //    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this@Visitlog)
            at_out!!.setBackgroundColor(Color.parseColor("#075b97"));
            at_out!!.setTextColor(Color.WHITE);
            at_in!!.setBackgroundColor(Color.WHITE);
            at_in!!.setTextColor(Color.BLACK);


//                        String user_email = prefManager.get_USERNAME().trim();
//                        String conference_id = prefManager.get_ORDER_TYPE_ID().trim();
//                        String vertical_id = prefManager.get_BRAND_ID().trim();
            val spf: SharedPreferences = this@Visitlog.getSharedPreferences("SimpleLogic", 0)
            val user_email = spf.getString("USER_EMAIL", null)

            val sdf = SimpleDateFormat("hh:mm a yyyy-MM-dd")
            val sdf2 = SimpleDateFormat("hh:mm")
            val date_only: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = Date()
            val daten = sdf.format(date)
            val daten2 = sdf2.format(date)
            val date_only_s = date_only.format(date)
            val datenn = sdf.format(date)


            Log.i("date time", "Datetime" + daten + " " + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE + " " + Global_Data.address)
            Log.i("time", "Datetime" + daten2 + " " + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE + " " + Global_Data.address)

            val editor: SharedPreferences.Editor = spf.edit()
            editor.putString("Dateout", daten)
            editor.putString("Timeout", daten2)
            editor.commit()


            Global_Data.GLOvel_LATITUDE
            Global_Data.GLOvel_LONGITUDE
            Global_Data.address
            Global_Data.Instatus = "OUT"




            outservicecall(daten)


        }
    }

    private fun outservicecall(daten: String) {

        dialog = ProgressDialog(this@Visitlog, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        dialog!!.setMessage(this@Visitlog.getResources().getString(R.string.data_Sync_in_Progress))
        dialog!!.setTitle(this@Visitlog.getResources().getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()


        val spf: SharedPreferences = this@Visitlog.getSharedPreferences("SimpleLogic", 0)
        val user_email = spf.getString("USER_EMAIL", null)
        val main = JSONObject()
        val content = JSONObject()
        val b5: ByteArray


        try {
            content.put("email", user_email)
            content.put("code", Global_Data.Code)
            content.put("check_out_time", daten)
            content.put("check_out_latitude", Global_Data.GLOvel_LATITUDE)
            content.put("check_out_longitude", Global_Data.GLOvel_LONGITUDE)
            content.put("status", Global_Data.Instatus)
            content.put("check_in_address", Global_Data.address)
        } catch (e: JSONException) {
            e.printStackTrace()
        }


//            main.put("email",user_email);
//            main.put("visit_log",content);


//        try {
//           // main.put("email",user_email);
//          //  main.put("visit_log",content);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        val domain = resources.getString(R.string.service_domain)

        try {
            serviceurlsubmit = domain + "visit_logs/checkout?email=" + URLEncoder.encode(user_email, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        Log.i("serviceirl", "serviceurl$serviceurlsubmit$main")

        val jsObjRequest = JsonObjectRequest(Request.Method.POST, serviceurlsubmit, content, Response.Listener { response ->
            Log.i("volley", "response: $response")
            var response_result = ""
            //if (response.has("result")) {
            try {
                response_result = response.getString("result")

//                         if (response_result.equalsIgnoreCase("Device not found.")) {
//                             Toast toast = Toast.makeText(context, "Device Not Found", Toast.LENGTH_LONG);
//                             toast.setGravity(Gravity.CENTER, 0, 0);
//                             toast.show();
//                             dialog.dismiss();
//                         }
            } catch (e: JSONException) {
                e.printStackTrace()
                (context as Activity).runOnUiThread { dialog!!.dismiss() }
            }
            if (response_result.equals("Device not found.", ignoreCase = true)) {
                (context as Activity).runOnUiThread {
                    Global_Data.Custom_Toast(context, "Device Not Found", "yes")
                    dialog!!.dismiss()
                }
            } else {
//                 else
//                 {
//                     response_result = "data";
//                 }
                (context as Activity).runOnUiThread {
                    Global_Data.Custom_Toast(context, "VisitLog Updated Successfully", "")
                    val settings = context!!.getSharedPreferences("SimpleLogic", 0)
                    settings.edit().remove("Datein").commit()

                    val settings2 = context!!.getSharedPreferences("SimpleLogic", 0)
                    settings2.edit().remove("Logouthour").commit()


                    val a = Intent(context, MainActivity::class.java)
                    context?.startActivity(a)
                    (context as Activity).finish()

                    dialog!!.dismiss()
                }

                //sendSMS("8454858739",sms_body,context);

                //Uri uri = Uri.parse("file://"+Environment.getExternalStorageDirectory()+"/test.png");
                //sendLongSMS("8454858739",sms_body,context);
                (context as Activity).runOnUiThread { dialog!!.dismiss() }
            }
        }, Response.ErrorListener { error ->
            if (error is TimeoutError || error is NoConnectionError) {
                (context as Activity).runOnUiThread {
                    Global_Data.Custom_Toast(context,
                            application.getResources().getString(R.string.internet_connection_error), "")
                }
            } else if (error is AuthFailureError) {
                (context as Activity).runOnUiThread {
                    Global_Data.Custom_Toast(context,
                            "Server AuthFailureError  Error", "")
                }
            } else if (error is ServerError) {
                (context as Activity).runOnUiThread {
                    Global_Data.Custom_Toast(context,
                            application.getResources().getString(R.string.Server_Errors), "")
                }
            } else if (error is NetworkError) {
                (context as Activity).runOnUiThread {
                    Global_Data.Custom_Toast(context,
                            application.getResources().getString(R.string.internet_connection_error), "")
                }
            } else if (error is ParseError) {
                (context as Activity).runOnUiThread {
                    Global_Data.Custom_Toast(context,
                            "ParseError   Error", "")
                }
            } else {
                (context as Activity).runOnUiThread { Global_Data.Custom_Toast(context, error.message, "") }
            }
            (context as Activity).runOnUiThread { dialog!!.dismiss() }
            // finish();
        })

        val requestQueue = Volley.newRequestQueue(context)
        // queue.add(jsObjRequest);
        // queue.add(jsObjRequest);
        val socketTimeout = 30000 //30 seconds - change to what you want

        val policy: RetryPolicy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        jsObjRequest.retryPolicy = policy
        requestQueue.add(jsObjRequest)


    }

    private fun AttinCall() {

        val spf2: SharedPreferences = this@Visitlog.getSharedPreferences("SimpleLogic", 0)
        val time = spf2.getString("Datein", null)

        if (time != null) {

            Global_Data.Custom_Toast(applicationContext, "Please Check-Out first", "Yes")


        } else {
            val appLocationManager = AppLocationManager(this@Visitlog)


            //    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this@Visitlog)
            at_out!!.setBackgroundColor(Color.parseColor("#075b97"))
            at_out!!.setTextColor(Color.WHITE)
            at_in!!.setBackgroundColor(Color.WHITE)
            at_in!!.setTextColor(Color.BLACK)


//                        String user_email = prefManager.get_USERNAME().trim();
//                        String conference_id = prefManager.get_ORDER_TYPE_ID().trim();
//                        String vertical_id = prefManager.get_BRAND_ID().trim();
            val spf: SharedPreferences = this@Visitlog.getSharedPreferences("SimpleLogic", 0)
            val user_email = spf.getString("USER_EMAIL", null)

            val sdf = SimpleDateFormat("hh:mm a yyyy-MM-dd")
            val sdf2 = SimpleDateFormat("hh:mm")
            val date_only: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = Date()
            val daten = sdf.format(date)
            val daten2 = sdf2.format(date)
            val date_only_s = date_only.format(date)
            val datenn = sdf.format(date)


            Log.i("date time", "Datetime" + daten + " " + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE + " " + Global_Data.address)
            Log.i("time", "Datetime" + daten2 + " " + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE + " " + Global_Data.address)

            val editor: SharedPreferences.Editor = spf.edit()
            editor.putString("Datein", daten)
            editor.putString("Time", daten2)
            editor.commit()


            Global_Data.GLOvel_LATITUDE
            Global_Data.GLOvel_LONGITUDE
            Global_Data.address
            Global_Data.Instatus = "IN"



            startActivity(Intent(this@Visitlog, Visitlogsubmit::class.java))
            finish()

        }


    }

    override fun updateMonthOnScroll(selectedDate: DayDateMonthYearModel?) {
        currentMonthTextView.text = "" + selectedDate?.month + " " + selectedDate?.year
    }

    override fun newDateSelected(selectedDate: DayDateMonthYearModel?) {
        dateStr = selectedDate?.year + "-" + selectedDate?.monthNumeric + "-" + selectedDate?.date

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var currentDate = sdf.format(Date())

        if (currentDate.equals(dateStr)) {
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                showdata(currentDate!!)
            } else {
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error), "Yes")
            }

        } else {
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                showdata(dateStr!!)
            } else {
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error), "Yes")
            }
        }
    }

    private fun showdata(dateStr: String) {

        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()

        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null).toString()
        // val shop_code: String = sp.getString("shopcode", null)

        val Cust_domain = sp.getString("Cust_Service_Url", "")
        var service_url = Cust_domain + "metal/api/v1/"

        try {
            service_url = service_url + "visit_logs/display_visit_log?email=" + URLEncoder.encode(user_email, "UTF-8") + "&log_date=" + dateStr

            Log.i("service", "service" + service_url);

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
                            list!!.clear()
                            var response_result = ""
                            if (json.has("result")) {
                                response_result = json.getString("result")
                                Global_Data.Custom_Toast(context,
                                        response_result, "yes")

                                list!!.clear()
                                dataadaptor!!.notifyDataSetChanged()

                                dialog!!.hide()

                            } else {

                                // list!!.clear()

                                val activities = json.getJSONArray("visit")

                                Log.i("volley", "response programs Length: " + activities.length())

                                Log.d("users", "programs$activities")
                                list = java.util.ArrayList<Visitlog_model>()

                                for (i in 0 until activities.length()) {
                                    val jsonObject = activities.getJSONObject(i)
                                    var a = Visitlog_model(jsonObject.getString("visit_number"), jsonObject.getString("visit_type"), jsonObject.getString("check_in_time"), jsonObject.getString("check_out_time"), jsonObject.getString("code"))
                                    list!!.add(a)
                                }

                                dataadaptor = Visitlogadaptor(this, list!!)
                                recycler_view1?.adapter = dataadaptor
                                dataadaptor!!.notifyDataSetChanged()

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
                        Global_Data.Custom_Toast(context,
                                "Network Error", "yes")
                    } else if (error is AuthFailureError) { //							Toast.makeText(Video_Main_List.this,
//
                        Global_Data.Custom_Toast(context,
                                "Server AuthFailureError  Error", "yes")
                    } else if (error is ServerError) { //							Toast.makeText(Video_Main_List.this,
                        Global_Data.Custom_Toast(context,
                                resources.getString(R.string.Server_Errors), "yes")
                    } else if (error is NetworkError) { //							Toast.makeText(Video_Main_List.this,
                        Global_Data.Custom_Toast(context,
                                "Network Error", "yes")
                    } else if (error is ParseError) { //							Toast.makeText(Video_Main_List.this,
                        Global_Data.Custom_Toast(context,
                                "ParseError   Error", "yes")
                    } else { //Toast.makeText(Video_Main_List.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Global_Data.Custom_Toast(context,
                                error.message, "yes")
                    }
                    //						Intent launch = new Intent(Video_Main_List.this,MainActivity.class);
//						startActivity(launch);
                    //     finish()
                    dialog!!.dismiss()
                    // finish();
                })
        val requestQueue = Volley.newRequestQueue(context)
        val socketTimeout = 300000 //30 seconds - change to what you want
        val policy: RetryPolicy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.retryPolicy = policy
        stringRequest.setShouldCache(false)
        requestQueue.cache.clear()
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
                super.onBackPressed()
            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp = this@Visitlog.getSharedPreferences("SimpleLogic", 0)
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

    override fun onMapReady(p0: GoogleMap?) {
        try {
            mGoogleMap = p0
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mGoogleMap!!.setMyLocationEnabled(true)
            buildGoogleApiClient()
            mGoogleApiClient!!.connect()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun buildGoogleApiClient() {

        // Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mGoogleMap!!.isMyLocationEnabled = true
            mGoogleMap!!.isTrafficEnabled = true
            mGoogleMap!!.isIndoorEnabled = true
            mGoogleMap!!.isBuildingsEnabled = true
            mGoogleMap!!.uiSettings.isZoomControlsEnabled = true
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onConnected(p0: Bundle?) {
        try {

            // Toast.makeText(this,"onConnected",Toast.LENGTH_SHORT).show();
            val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient)
            if (mLastLocation != null) {
                //place marker at current position
                //mGoogleMap.clear();
                latLng = LatLng(mLastLocation.latitude, mLastLocation.longitude)
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng!!)
                markerOptions.title(resources.getString(R.string.Current_Position))
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                currLocationMarker = mGoogleMap!!.addMarker(markerOptions)
            }
            mLocationRequest = LocationRequest()
            mLocationRequest!!.interval = 5000 //5 seconds
            mLocationRequest!!.fastestInterval = 3000 //3 seconds
            mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

    override fun onLocationChanged(p0: Location?) {
        //place marker at current position
        //mGoogleMap.clear();


        //place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker!!.remove()
        }
        latLng = p0?.getLongitude()?.let { LatLng(p0?.getLatitude(), it) }
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng!!)

        try {
            lat = p0?.getLatitude()
            longi = p0?.getLongitude()

//            prefManager.setLATITUDE(String.valueOf(lat));
//            prefManager.setLONGITUDE(String.valueOf(longi));
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                try {
                    //  LocationAddress locationAddress = new LocationAddress();
//                    locationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(),
//                            BasicMapDemoActivity.this, new BasicMapDemoActivity.GeocoderHandler());
                    val geo = Geocoder(this@Visitlog.getApplicationContext(), Locale.getDefault())
                    val addresses = geo.getFromLocation(p0!!.getLatitude(), p0!!.getLongitude(), 1)
                    if (addresses.isEmpty()) {
                        //yourtextfieldname.setText("Waiting for Location");
                        markerOptions.title(resources.getString(R.string.Waiting_for_Location))
                        // markerOptions.title("Current Position");
                    } else {
                        if (addresses.size > 0) {
//                            Address returnAddress = addresses.get(0);
//                            String localityString = returnAddress.getLocality();
//                            String city = returnAddress.getCountryName();
//                            String region_code = returnAddress.getCountryCode();
//                            String zipcode = returnAddress.getPostalCode();
                            str = StringBuilder()
                            str?.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses[0].getAddressLine(0)) + " ")
                            if (str?.indexOf(addresses[0].locality)!! <= 0) {
                                str?.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses[0].locality) + " ")
                            }
                            if (str?.indexOf(addresses[0].adminArea)!! <= 0) {
                                str?.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses[0].adminArea) + " ")
                            }
                            if (str?.indexOf(addresses[0].countryName)!! <= 0) {
                                str?.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses[0].countryName) + " ")
                            }
                            if (str?.indexOf(addresses[0].postalCode)!! <= 0) {
                                str?.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses[0].postalCode) + " ")
                            }
                            str?.append("\n")
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(str.toString())) {
                                Global_Data.address = str.toString()
                            }
                            markerOptions.title(str.toString())
                        } else {
                            if (Global_Data.address == null) {
                                getaddress()
                            }
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            currLocationMarker = mGoogleMap!!.addMarker(markerOptions)

            //Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();
            Log.d("Location change event", "Location Change")

            //zoom to current position:
            val cameraPosition = CameraPosition.Builder()
                    .target(latLng).zoom(16f).build()
            mGoogleMap!!.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition))
        } catch (e: Exception) {
            e.printStackTrace() // getFromLocation() may sometimes fail
        }
    }

    private fun getaddress() {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder.getFromLocation(lat!!, longi!!, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        val address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        val city = addresses[0].locality
        val state = addresses[0].adminArea
        val country = addresses[0].countryName
        val postalCode = addresses[0].postalCode
        val knownName = addresses[0].featureName

        Global_Data.address = address
        Global_Data.address = str.toString()
    }

}

