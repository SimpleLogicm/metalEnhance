package com.msimplelogic.activities.kotlinFiles

import  com.msimplelogic.activities.R
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import com.msimplelogic.webservice.ConnectionDetector
import com.view.calender.horizontal.umar.horizontalcalendarview.DayDateMonthYearModel
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarListener
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarView
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_target.*
import kotlinx.android.synthetic.main.content_beat_selection.*
import kotlinx.android.synthetic.main.content_target.*
import kotlinx.android.synthetic.main.content_target.btn_dashboard
import kotlinx.android.synthetic.main.content_target.hedder_theame
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ActivityTarget : BaseActivity(), HorizontalCalendarListener {
    lateinit var currentMonthTextView : TextView
    var dateStr:String?=null
    var dialog: ProgressDialog? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var sp:SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        cd = ConnectionDetector(Day_sheduler@this)
        dialog = ProgressDialog(ActivitySummary@this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)


        }

//        sectorprogress_productivity.percent= 25F
//        sectorprogress_productivity.animateIndeterminate()
//        Handler().postDelayed({
//            sectorprogress_productivity.stopAnimateIndeterminate()
//        }, 1000)

//        calender_event.initCalderItemClickCallback(CalenderDayClickListener {
//            dayContainerModel ->
//            //Toast.makeText(this, dayContainerModel.date, Toast.LENGTH_SHORT).show()
//
//        })

        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {
            DayCoverageResult("")
        }
        else
        {
            Global_Data.Custom_Toast(CustomerServicesTraits@this, resources.getString(R.string.internet_connection_error), "Yes")
        }


        setUpPieChartBilledOutletData()
        setUpPieChartUnbilledOutletData()
        //setUpPieChartNewtData()
        //setUpPieChartCoverageData()
        //setUpPieChartProductivityData()

        piechart_productivity.setNoDataText("No Data Available")
        val p: Paint = piechart_productivity.getPaint(Chart.PAINT_INFO)
        p.textSize= 15F

        piechart_coverage.setNoDataText("No Data Available")
        val p1: Paint = piechart_coverage.getPaint(Chart.PAINT_INFO)
        p1.textSize= 15F

        piechart_new.setNoDataText("No Data Available")
        val p2: Paint = piechart_new.getPaint(Chart.PAINT_INFO)
        p2.textSize= 15F

        piechart_buildoutlet.setNoDataText("No Data Available")
        val p3: Paint = piechart_buildoutlet.getPaint(Chart.PAINT_INFO)
        p3.textSize= 15F

        piechart_unbuildoutlet.setNoDataText("No Data Available")
        val p4: Paint = piechart_unbuildoutlet.getPaint(Chart.PAINT_INFO)
        p4.textSize= 15F

        btn_viewtarget.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@ActivityTarget, ActivityViewTarget::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        btn_dashboard.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@ActivityTarget, TargetAchievementActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        currentMonthTextView = findViewById(R.id.month)
        val  hcv = findViewById<HorizontalCalendarView>(R.id.horizontalcalendarview)
        hcv.setContext(this@ActivityTarget)
        hcv.setBackgroundColor(resources.getColor(R.color.white))
        hcv.showControls(true)
        hcv.setControlTint(R.color.light_grey)
        hcv.changeAccent(R.color.black)

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
                val sp: SharedPreferences = this@ActivityTarget.getSharedPreferences("SimpleLogic", 0)
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
        val i = Intent(this@ActivityTarget, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i)
        finish()
    }

    private fun setUpPieChartBilledOutletData() {
        val yVals = ArrayList<PieEntry>()
        yVals.add(PieEntry(30f))
        yVals.add(PieEntry(12f))
        yVals.add(PieEntry(5f))

        val dataSet = PieDataSet(yVals, "")
        dataSet.valueTextSize=8f
        //dataSet.valueTextColor=Color.parseColor("#ffffff")
        //dataSet.valueTypeface= Typeface.DEFAULT_BOLD

        val colors = java.util.ArrayList<Int>()
        colors.add(Color.parseColor("#9BC8FF"))
        colors.add(Color.parseColor("#075B97"))
        colors.add(Color.parseColor("#E9E9E9"))

        dataSet.setColors(colors)
        val data = PieData(dataSet)
        piechart_buildoutlet.data = data

        data.setValueFormatter(PercentFormatter())

        piechart_buildoutlet.centerTextRadiusPercent = 10f
        piechart_buildoutlet.isDrawHoleEnabled = false
        piechart_buildoutlet.legend.isEnabled = false
        piechart_buildoutlet.description.isEnabled = false
        piechart_buildoutlet.animateXY(1400, 1400)
    }

    private fun setUpPieChartUnbilledOutletData() {
        val yVals = ArrayList<PieEntry>()
        yVals.add(PieEntry(30f))
        yVals.add(PieEntry(12f))
        yVals.add(PieEntry(5f))

        val dataSet = PieDataSet(yVals, "")
        dataSet.valueTextSize=0f
        val colors = java.util.ArrayList<Int>()
        colors.add(Color.parseColor("#9BC8FF"))
        colors.add(Color.parseColor("#075B97"))
        colors.add(Color.parseColor("#E9E9E9"))

        dataSet.setColors(colors)
        val data = PieData(dataSet)
        piechart_unbuildoutlet.data = data
        piechart_unbuildoutlet.centerTextRadiusPercent = 0f
        piechart_unbuildoutlet.isDrawHoleEnabled = false
        piechart_unbuildoutlet.legend.isEnabled = false
        piechart_unbuildoutlet.description.isEnabled = false
        piechart_unbuildoutlet.animateXY(1400, 1400)
    }

    private fun setUpPieChartNewtData() {
        val yVals = ArrayList<PieEntry>()
        yVals.add(PieEntry(30f))
        yVals.add(PieEntry(12f))
        //yVals.add(PieEntry(5f))

        val dataSet = PieDataSet(yVals, "")
        dataSet.valueTextSize=8f
        val colors = java.util.ArrayList<Int>()
        colors.add(Color.parseColor("#9BC8FF"))
        colors.add(Color.parseColor("#075B97"))
        //colors.add(Color.parseColor("#E9E9E9"))

        dataSet.setColors(colors)
        val data = PieData(dataSet)
        piechart_new.data = data
        piechart_new.centerTextRadiusPercent = 0f
        piechart_new.isDrawHoleEnabled = false
        piechart_new.legend.isEnabled = false
        piechart_new.description.isEnabled = false
        piechart_new.animateXY(1400, 1400)
    }

    private fun setUpPieChartCoverageData(txt_coverage:String) {

        var dff=txt_coverage
        var dffd=(100-txt_coverage.toFloat())

//        # coverage_dark_blue = (100 - coverage.to_i)
//        # coverage_light_blue = coverage.to_i

        val yVals = ArrayList<PieEntry>()
        yVals.add(PieEntry(dff.toFloat()))
        yVals.add(PieEntry(dffd))
        //yVals.add(PieEntry(5f))

        val dataSet = PieDataSet(yVals, "")
        dataSet.valueTextSize=8f
        val colors = java.util.ArrayList<Int>()
        colors.add(Color.parseColor("#9BC8FF"))
        colors.add(Color.parseColor("#075B97"))
        //colors.add(Color.parseColor("#E9E9E9"))

        dataSet.setColors(colors)
        val data = PieData(dataSet)
        piechart_coverage.data = data
        piechart_coverage.centerTextRadiusPercent = 0f
        piechart_coverage.isDrawHoleEnabled = false
        piechart_coverage.legend.isEnabled = false
        piechart_coverage.description.isEnabled = false
        piechart_coverage.animateXY(1400, 1400)
    }

    private fun setUpPieChartProductivityData(prod_txt:String) {
        val yVals = ArrayList<PieEntry>()
        var dff=prod_txt
        var dffd=(100-prod_txt.toFloat())

//        # productivity_dark_blue = (100 - productivity.to_i)
//        # productivity_light_blue = productivity.to_i
//
        yVals.add(PieEntry(dff.toFloat()))
        yVals.add(PieEntry(dffd))
        //yVals.add(PieEntry(5f))

        val dataSet = PieDataSet(yVals, "")
        dataSet.valueTextSize=8f
        val colors = java.util.ArrayList<Int>()
        colors.add(Color.parseColor("#9BC8FF"))
        colors.add(Color.parseColor("#075B97"))
        //colors.add(Color.parseColor("#E9E9E9"))

        dataSet.setColors(colors)
        val data = PieData(dataSet)
        piechart_productivity.data = data
//        val p: Paint = piechart_productivity.getPaint(Chart.PAINT_INFO)
//        p.setTextSize(5f)
        piechart_productivity.centerTextRadiusPercent = 0f
        piechart_productivity.isDrawHoleEnabled = false
        piechart_productivity.legend.isEnabled = false
        piechart_productivity.description.isEnabled = false
        piechart_productivity.animateXY(1400, 1400)
    }

    override fun updateMonthOnScroll(selectedDate: DayDateMonthYearModel?) {
        currentMonthTextView.text = ""+ selectedDate?.month + " " + selectedDate?.year

    }

    override fun newDateSelected(selectedDate: DayDateMonthYearModel?) {
        dateStr=selectedDate?.date+"-"+selectedDate?.monthNumeric+"-"+selectedDate?.year
        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {
            DayCoverageResult(dateStr!!)
        }
        else
        {
            Global_Data.Custom_Toast(CustomerServicesTraits@this, resources.getString(R.string.internet_connection_error), "Yes")
        }
    }

    fun DayCoverageResult(date1:String) {
        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()

        val sdf = SimpleDateFormat("dd-MM-yyyy")
//        val current = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        if(date1?.length==0)
        {
            dateStr = sdf.format(Date())
        }

        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null)!!

        val Cust_domain = sp.getString("Cust_Service_Url", "")
        var service_url = Cust_domain + "metal/api/v1/"

        try {
            service_url = service_url + "targets/day_coverage?email=" + (URLEncoder.encode(user_email, "UTF-8"))+"&date="+dateStr

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
                                //Global_Data.Custom_Toast(this, resources.getString(R.string.Sorry_No_Record_Found), "Yes")
                                //finish().toString()
                                //finish()
                            } else {

//                                val dfgsg:String=json.getString("productivity")
//                                val dfgsg1:String=json.getString("coverage")
//
//                                tv_shopvisited.setText(json.getString("coverage"))

                                        tv_visited_planned.setText("Visit \n Planned \n"+json.getString("visit_planned_count"))
                                        tv_visit_completed.setText("Visit \n Completed \n"+json.getString("visit_completed_count"))
                                        tv_positive_outcome.setText("Positive \n Outcome \n"+json.getString("positive_outcome_count"))

                                setUpPieChartProductivityData(json.getString("productivity"))

                                setUpPieChartCoverageData(json.getString("coverage"))


//                                val summaryData = json.getJSONArray("records")
//                                Log.i("volley", "response visits Length: " + summaryData.length())
//                                Log.d("users", "customer_stocks$summaryData")

//                                if(summaryData.length()>0) {
//
//                                    listSummary = ArrayList<SummaryModel>()
//                                    for (i in 0 until summaryData.length()) {
//                                        val jsonObject = summaryData.getJSONObject(i)
//                                        listSummary!!.add(SummaryModel(jsonObject.getString("shop_name"), jsonObject.getString("location"), "$ " + jsonObject.getString("amount"),jsonObject.getString("summary_type")))
//                                    }
//                                    summaryAdapter = AdapterSummary(this, listSummary!!)
//                                    summary_list?.adapter = summaryAdapter
//                                    dialog!!.dismiss()
//
//                                }else{
//                                    Global_Data.Custom_Toast(this, resources.getString(R.string.Sorry_No_Record_Found), "Yes")
//                                    dialog!!.dismiss()
//                                }
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
////									Toast.LENGTH_LONG).show();
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
        //requestQueue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest)
    }

}