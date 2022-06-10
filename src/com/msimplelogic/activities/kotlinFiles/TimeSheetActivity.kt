package com.msimplelogic.activities.kotlinFiles

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.msimplelogic.activities.R
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import com.msimplelogic.adapter.AdapterTimesheet
import com.msimplelogic.model.TimeSheetModel
import com.msimplelogic.webservice.ConnectionDetector
import com.view.calender.horizontal.umar.horizontalcalendarview.DayDateMonthYearModel
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarListener
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarView
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_timesheet.*
import kotlinx.android.synthetic.main.content_timesheet.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.File
import java.io.FileOutputStream
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class TimeSheetActivity : BaseActivity() , HorizontalCalendarListener {
    private var adapter: AdapterTimesheet? = null
    var list: ArrayList<TimeSheetModel>? = null
    lateinit var currentMonthTextView : TextView
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var dialog: ProgressDialog? = null
    var dateStr:String?=null
    var m_btn_share:Button?=null
    private var sharePath = "no"
    var sp:SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timesheet)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        cd = ConnectionDetector(Day_sheduler@this)
        m_btn_share=findViewById(R.id.btnsave_timesheet)
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            filterimg.setImageResource(R.drawable.filterordertype_icon_dark)
            hedder_theameh.setImageResource(R.drawable.dark_hedder)

//ll.setBackgroundResource(R.drawable.dark_theme_background);
        }

        timesheet_list.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@TimeSheetActivity, ActivityTaskDetails::class.java))
            Global_Data.PlannerName ="Tour"
        })

        imgplus_timesheet.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@TimeSheetActivity, TimesheetDetailsActivity::class.java))
        })

        m_btn_share!!.setOnClickListener {
            // requestReadPermissions()
            takeScreenshot()
        }

        currentMonthTextView = findViewById(R.id.month)
        val  hcv = findViewById<HorizontalCalendarView>(R.id.horizontalcalendarview)
        hcv.setContext(this@TimeSheetActivity)
        hcv.setBackgroundColor(resources.getColor(R.color.white))
        hcv.showControls(true)
        hcv.setControlTint(R.color.light_grey)
        hcv.changeAccent(R.color.black)

        switch_comments?.setOnCheckedChangeListener({ _, isChecked ->
            // val message = if (isChecked) "Switch1:ON" else "Switch1:OFF"
// Toast.makeText(this@TimeSheetActivity, message,
// Toast.LENGTH_SHORT).show()

            if (isChecked) {
                adapter!!.activateButtons(true)

            } else {
                adapter!!.activateButtons(false)
            }
        })

        list = ArrayList<TimeSheetModel>()
        adapter = AdapterTimesheet(this, list as java.util.ArrayList<TimeSheetModel>)

        val mLayoutManager = LinearLayoutManager(this)
        timesheet_list!!.layoutManager = mLayoutManager
        //timesheet_list.addItemDecoration(DividerItemDecoration(this@TimeSheetActivity, LinearLayoutManager.VERTICAL))
        timesheet_list!!.itemAnimator = DefaultItemAnimator()
        timesheet_list!!.adapter = adapter

        dialog = ProgressDialog(Day_sheduler@this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        isInternetPresent = cd!!.isConnectingToInternet

        if (isInternetPresent) {
            TimesheetResult("")
        }
        else
        {
            Global_Data.Custom_Toast(TimeSheetActivity@this, resources.getString(R.string.internet_connection_error), "Yes")
        }

//        day_date_picker.getSelectedDate(OnDateSelectedListener { date ->
//            if (date != null) { // do something with selected date
//
//                Toast.makeText(this@TimeSheetActivity, "Its toast!"+date, Toast.LENGTH_SHORT).show()
//            }
//        })
    }

    private fun requestReadPermissions() {

        Dexter.withActivity(this)
                .withPermissions( Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            Global_Data.Custom_Toast(applicationContext, "All permissions are granted by user!","yes")
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).withErrorListener(object : PermissionRequestErrorListener {
                    override fun onError(error: DexterError) {
                        Global_Data.Custom_Toast(applicationContext, "Some Error! ","yes")
                    }
                })
                .onSameThread()
                .check()
    }

    private fun takeScreenshot() {
        val now = Date()
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)

        try {
            // image naming and path  to include sd card  appending name you choose for file
            val mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg"

            // create bitmap screen capture
            val v1 = window.decorView.rootView
            v1.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(v1.drawingCache)
            v1.isDrawingCacheEnabled = false

            val imageFile = File(mPath)

            val outputStream = FileOutputStream(imageFile)
            val quality = 100
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()

            //setting screenshot in imageview
            val filePath = imageFile.path

            val ssbitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            //    iv!!.setImageBitmap(ssbitmap)
            sharePath = filePath

            Log.d("ffff", sharePath)
            val file = File(sharePath)
           // val uri = Uri.fromFile(file)

            val photoURI = FileProvider.getUriForFile(this@TimeSheetActivity,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file)
         //   val outputFileUri: Uri = FileProvider.getUriForFile(this@TimeSheetActivity, BuildConfig.APPLICATION_ID, file)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, photoURI)
            //     intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "" + m.getTitle())
            //  sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, "Share via"))
            //startActivity(intent)

          //  Global_Data.Custom_Toast(this@TimeSheetActivity,"yes hua","h")
        } catch (e: Throwable) {
            Log.i("yes","yes"+e.toString())
            // Several error may come out with file handling or DOM
            e.printStackTrace()
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
                val sp: SharedPreferences = this@TimeSheetActivity.getSharedPreferences("SimpleLogic", 0)
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
//super.onBackPressed();
        val i = Intent(this@TimeSheetActivity, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i)
        finish()
    }

    override fun updateMonthOnScroll(selectedDate: DayDateMonthYearModel?) {
        currentMonthTextView.text = ""+ selectedDate?.month + " " + selectedDate?.year
    }

    override fun newDateSelected(selectedDate: DayDateMonthYearModel?) {
        //Toast.makeText(this@TimeSheetActivity ,selectedDate?.date +""+ selectedDate?.month + " " + selectedDate?.year , Toast.LENGTH_LONG).show()
        dateStr=selectedDate?.date+"-"+selectedDate?.monthNumeric+"-"+selectedDate?.year

        if (isInternetPresent) {

            TimesheetResult(dateStr!!)
        }
        else
        {
            Global_Data.Custom_Toast(CustomerServicesTraits@this, resources.getString(R.string.internet_connection_error), "Yes")
        }

    }

    fun TimesheetResult(date1:String) {
        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()

        val sdf = SimpleDateFormat("dd-MM-yyyy")
//        val currentDate = sdf.format(Date())
//        System.out.println(" C DATE is  "+currentDate)

//        val current = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        if(date1?.length==0)
        {
            //dateStr = current.format(formatter)
            dateStr = sdf.format(Date())
        }

        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null)!!
        //  val shop_code: String = sp.getString("shopcode", null)

        val Cust_domain = sp.getString("Cust_Service_Url", "")
        var service_url = Cust_domain + "metal/api/v1/"

        try {
            service_url = service_url + "time_sheets?email=" + (URLEncoder.encode(user_email, "UTF-8"))+"&date="+dateStr
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
                                dialog!!.dismiss()
                                Global_Data.Custom_Toast(this, resources.getString(R.string.Sorry_No_Record_Found), "Yes")
                                //finish().toString()
                                //finish()
                            } else {

                                val summaryData = json.getJSONArray("time_sheet")
                                Log.i("volley", "response visits Length: " + summaryData.length())
                                Log.d("users", "visite_schedules$summaryData")
                                list?.clear()
                                if(summaryData.length()>0) {

                                    list = ArrayList<TimeSheetModel>()
                                    for (i in 0 until summaryData.length()) {
                                        val jsonObject = summaryData.getJSONObject(i)
                                        list!!.add(TimeSheetModel(jsonObject.getString("task_title"), jsonObject.getString("details1"), jsonObject.getString("details2"),jsonObject.getString("working_hours")))
                                    }
                                    adapter = AdapterTimesheet(this, list!!)
                                    timesheet_list?.adapter = adapter
                                    dialog!!.dismiss()
                                    switch_comments!!.visibility=View.VISIBLE

                                }else{
                                    Global_Data.Custom_Toast(this, resources.getString(R.string.Sorry_No_Record_Found), "Yes")
                                    switch_comments!!.visibility=View.GONE
                                    adapter!!.notifyDataSetChanged()
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