package com.msimplelogic.activities.kotlinFiles

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import com.msimplelogic.activities.R
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.harrywhewell.scrolldatepicker.MonthScrollDatePicker
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import com.msimplelogic.adapter.ViewTargetAdapter
import com.msimplelogic.model.ViewTargetModel
import com.msimplelogic.webservice.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_viewtarget.*
import kotlinx.android.synthetic.main.content_viewtarget.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ActivityViewTarget : BaseActivity() {
    private val product_category_list = ArrayList<String>()
    private val target_amount_list = ArrayList<String>()
    private val target_grpby_list = ArrayList<String>()

    var adapter_product_category: ArrayAdapter<String>? = null
    var adapter_target_amount: ArrayAdapter<String>? = null
    var adapter_target_grpby: ArrayAdapter<String>? = null

    var product_category = ""
    var dbvoc = DataBaseHelper(this)
    var loginDataBaseAdapter: LoginDataBaseAdapter? = null
    var dialog: ProgressDialog? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var date: DatePickerDialog.OnDateSetListener? = null
    var date1: android.app.DatePickerDialog.OnDateSetListener? = null
    var myCalendar: Calendar? = null
    var myCalendar1: MonthScrollDatePicker? = null
    private var viewTargetAdapter: ViewTargetAdapter? = null
    var dialogFilter: Dialog? = null

    //private var viewTargetList: MutableList<ViewTargetModel>? = null
    var t_total = 0.0
    var achived_total = 0.0
    var agen_total = 0.0
    var age_value = ""
    var target_value = ""
    var achieved_value = ""
    var viewTargetList: ArrayList<ViewTargetModel>? = null

    //var result: List<TargetValue_info> = ArrayList()
    var Target_From_Month = ""
    var Target_To_Month = ""
    var Target_Product_Category = ""
    var Target_Year = ""
    var Target_TO_YEAR = ""
    var Target_TO_MONTHNEW = ""
    var fromYear: Int = 0
    var fromMonth: Int = 0
    var toYear: Int = 0
    var toMonth: Int = 0
    var sp: SharedPreferences? = null

    lateinit var Target_TO_YEAR_ARRAY: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewtarget)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        cd = ConnectionDetector(ActivityViewTarget@ this)
        isInternetPresent = cd!!.isConnectingToInternet()

        loginDataBaseAdapter = LoginDataBaseAdapter(this@ActivityViewTarget)
        loginDataBaseAdapter = loginDataBaseAdapter!!.open()

        viewTargetList = ArrayList<ViewTargetModel>()
        viewTargetAdapter = ViewTargetAdapter(this, viewTargetList as java.util.ArrayList<ViewTargetModel>)
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)

        }


        val mLayoutManager = LinearLayoutManager(this)
        viewtarget_list!!.layoutManager = mLayoutManager
        //viewtarget_list.addItemDecoration(DividerItemDecoration(this@ActivityViewTarget, LinearLayoutManager.VERTICAL))
        viewtarget_list!!.itemAnimator = DefaultItemAnimator()
        viewtarget_list!!.adapter = viewTargetAdapter

//        T_from_date.setText(Target_From_Month + " " + Target_Year)
//        T_to_date.setText(Target_To_Month)

        fab.setOnClickListener { view ->
            if (Global_Data.target_quarter.length > 0) {
                //ViewTargetFiterDialog()
                ViewTargetFiterDialog(fromYear.toString(), fromMonth.toString(), toYear.toString(), toMonth.toString())

                // ViewTargetFiterDialog("","","","")
            } else {
                Global_Data.Custom_Toast(this, resources.getString(R.string.Please_Select_Duration), "Yes")
            }
        }

        btn_daycoverage.setOnClickListener(View.OnClickListener {
            //startActivity(Intent(this@ActivityViewTarget, ActivityTarget::class.java))Target_REYC_Main
            startActivity(Intent(this@ActivityViewTarget, ActivityTarget::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        btn_dashboard.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@ActivityViewTarget, TargetAchievementActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        btn_monthly.setOnClickListener(View.OnClickListener {
            try {
                Kot_Gloval.hideKeyboard(context = ActivityLeaveManagement@ this, view = it)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            btn_monthly.setBackgroundResource(R.drawable.leavem_bg)
            btn_quarterly.setBackgroundResource(R.drawable.leavebal_bg)
            btn_halfyearly.setBackgroundResource(R.drawable.leavebal_bg)
            btn_yearly.setBackgroundResource(R.drawable.leavebal_bg)
            Global_Data.target_quarter = "Monthly"
        })

        btn_quarterly.setOnClickListener(View.OnClickListener {
            try {
                Kot_Gloval.hideKeyboard(context = ActivityLeaveManagement@ this, view = it)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            btn_monthly.setBackgroundResource(R.drawable.leavebal_bg)
            btn_quarterly.setBackgroundResource(R.drawable.leavem_bg)
            btn_halfyearly.setBackgroundResource(R.drawable.leavebal_bg)
            btn_yearly.setBackgroundResource(R.drawable.leavebal_bg)
            Global_Data.target_quarter = "Quarterly"
        })

        btn_halfyearly.setOnClickListener(View.OnClickListener {
            try {
                Kot_Gloval.hideKeyboard(context = ActivityLeaveManagement@ this, view = it)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            btn_monthly.setBackgroundResource(R.drawable.leavebal_bg)
            btn_quarterly.setBackgroundResource(R.drawable.leavebal_bg)
            btn_halfyearly.setBackgroundResource(R.drawable.leavem_bg)
            btn_yearly.setBackgroundResource(R.drawable.leavebal_bg)
            Global_Data.target_quarter = "Half Yearly"
        })

        btn_yearly.setOnClickListener(View.OnClickListener {
            try {
                Kot_Gloval.hideKeyboard(context = ActivityLeaveManagement@ this, view = it)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            btn_monthly.setBackgroundResource(R.drawable.leavebal_bg)
            btn_quarterly.setBackgroundResource(R.drawable.leavebal_bg)
            btn_halfyearly.setBackgroundResource(R.drawable.leavebal_bg)
            btn_yearly.setBackgroundResource(R.drawable.leavem_bg)
            Global_Data.target_quarter = "Yearly"
        })

        back_viewtarget.setOnClickListener(View.OnClickListener {
            try {
                Kot_Gloval.hideKeyboard(context = ActivityLeaveManagement@ this, view = it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val i = Intent(this@ActivityViewTarget, ActivityTarget::class.java)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i)
        })

        more_viewtarget.setOnClickListener(View.OnClickListener {
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {

                if (target_month_to.text.length > 0) {
                    val i = Intent(this@ActivityViewTarget, Target_Summary2::class.java)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    i.putExtra("FROM_YEAR", fromYear.toString())
                    i.putExtra("FROM_MONTH", fromMonth.toString())
                    i.putExtra("TO_YEAR", toYear.toString())
                    i.putExtra("TO_MONTH", toMonth.toString())
                    i.putExtra("FROM_DATE", et_fromviewtargetdate.text.toString())
                    i.putExtra("TO_DATE", target_month_to.text.toString())
                    // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i)
                } else {
                    Global_Data.Custom_Toast(this, resources.getString(R.string.Please_Select_Duration), "Yes")
                }


            } else {
//                val toast = Toast.makeText(this@ActivityViewTarget, resources.getString(R.string.internet_connection_error), Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.CENTER, 0, 0)
//                toast.show()
                Global_Data.Custom_Toast(this@ActivityViewTarget, resources.getString(R.string.internet_connection_error), "yes")
            }
        })

//        myCalendar = Calendar.getInstance()
//        date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//            myCalendar!!.set(Calendar.YEAR, year)
//            myCalendar!!.set(Calendar.MONTH, monthOfYear)
//            myCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            updateLabel()
//        }
//
//        date1 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//            myCalendar!!.set(Calendar.YEAR, year)
//            myCalendar!!.set(Calendar.MONTH, monthOfYear)
//            myCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            updateLabel1()
//        }
//        et_fromviewtargetdate.setOnClickListener(View.OnClickListener {
//            DatePickerDialog(this@ActivityViewTarget, date, myCalendar
//                    !!.get(Calendar.YEAR), myCalendar!!.get(Calendar.MONTH),
//                    myCalendar!!.get(Calendar.DAY_OF_MONTH)).show()
//        })
//
//        et_toviewtargetdate.setOnClickListener(View.OnClickListener {
//            DatePickerDialog(this@ActivityViewTarget, date1, myCalendar
//                    !!.get(Calendar.YEAR), myCalendar!!.get(Calendar.MONTH),
//                    myCalendar!!.get(Calendar.DAY_OF_MONTH)).show()
//        })

        viewTargetList = java.util.ArrayList()
        viewTargetAdapter = ViewTargetAdapter(this, viewTargetList as ArrayList<ViewTargetModel>)

//        val mLayoutManager = LinearLayoutManager(this)
//        viewtarget_list!!.layoutManager = mLayoutManager
//        //viewtarget_list.addItemDecoration(DividerItemDecoration(this@ActivityViewTarget, LinearLayoutManager.VERTICAL))
//        viewtarget_list!!.itemAnimator = DefaultItemAnimator()
//        viewtarget_list!!.adapter = viewTargetAdapter
//        viewTargetList()

        viewtarget_list.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        viewtarget_list.setLayoutManager(llm)

        val current_year = Calendar.getInstance()[Calendar.YEAR]
        month_date_picker.setStartDate(1, current_year)
        month_date_picker.setEndDate(12, current_year)

//        month_date_picker.setStartDate(10, 10, 2010)
//        month_date_picker.setEndDate(11, 11, 2011)

        // month_date_picker.setShowFullDate()


        month_date_picker.getSelectedDate(OnDateSelectedListener { date ->
            if (date != null) { // do something with selected date

                val formateDate: String = SimpleDateFormat("MMMM").format(date)
                val formateDate1: String = SimpleDateFormat("yyyy").format(date)

                if (Global_Data.target_quarter.length > 0) {

                    if (Global_Data.target_quarter.equals(
                                    resources.getString(R.string.Monthly),
                                    ignoreCase = true
                            )
                    ) {
                        target_month_to.setText(formateDate + " " + formateDate1)
                        toYear = formateDate1.toInt()
                        toMonth = (SimpleDateFormat("MM").format(date).toInt())
                    } else if (Global_Data.target_quarter.equals(
                                    resources.getString(R.string.Quarterly),
                                    ignoreCase = true
                            )
                    ) {
                        val calendar = Calendar.getInstance()
                        calendar.clear()
                        try {
                            calendar.time = SimpleDateFormat("MMM").parse(formateDate)
                            val monthInt = calendar[Calendar.MONTH]
                            // toMonthCount= calendar[Calendar.MONTH+1].toString()
                            calendar[Calendar.MONTH] = monthInt
                            calendar[Calendar.YEAR] = formateDate1.toInt()
                            val date1 = calendar.time
                            println(
                                    "Current date : " + calendar[Calendar.MONTH]
                                            + "-"
                                            + calendar[Calendar.DATE]
                                            + "-"
                                            + calendar[Calendar.YEAR]
                            )
                            calendar.add(Calendar.MONTH, 2)
                            println(
                                    "date after 12 months : " + calendar[Calendar.MONTH]
                                            + "-"
                                            + calendar[Calendar.DATE]
                                            + "-"
                                            + calendar[Calendar.YEAR]
                            )
                            val date2 = calendar.time
                            toYear = calendar[Calendar.YEAR]
                            toMonth = (SimpleDateFormat("MM").format(date2).toInt())
                            target_month_to.setText(SimpleDateFormat("MMMM").format(date2) + " " + calendar[Calendar.YEAR])
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    } else if (Global_Data.target_quarter.equals(
                                    resources.getString(R.string.Half_Yearly),
                                    ignoreCase = true
                            )
                    ) {
                        val calendar = Calendar.getInstance()
                        calendar.clear()
                        try {
                            calendar.time = SimpleDateFormat("MMM").parse(formateDate)
                            val monthInt = calendar[Calendar.MONTH]
                            //toMonthCount= calendar[Calendar.MONTH+1].toString()
                            calendar[Calendar.MONTH] = monthInt
                            calendar[Calendar.YEAR] = formateDate1.toInt()
                            val date1 = calendar.time
                            println(
                                    "Current date : " + calendar[Calendar.MONTH]
                                            + "-"
                                            + calendar[Calendar.DATE]
                                            + "-"
                                            + calendar[Calendar.YEAR]
                            )
                            calendar.add(Calendar.MONTH, 5)
                            println(
                                    "date after 12 months : " + calendar[Calendar.MONTH]
                                            + "-"
                                            + calendar[Calendar.DATE]
                                            + "-"
                                            + calendar[Calendar.YEAR]
                            )
                            val date2 = calendar.time
                            toYear = calendar[Calendar.YEAR]
                            toMonth = (SimpleDateFormat("MM").format(date2).toInt())
                            target_month_to.setText(SimpleDateFormat("MMMM").format(date2) + " " + calendar[Calendar.YEAR])
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    } else if (Global_Data.target_quarter.equals(
                                    resources.getString(R.string.Yearly),
                                    ignoreCase = true
                            )
                    ) {
                        val calendar = Calendar.getInstance()
                        calendar.clear()
                        try {
                            calendar.time = SimpleDateFormat("MMM").parse(formateDate)
                            val monthInt = calendar[Calendar.MONTH]
                            // toMonthCount= calendar[Calendar.MONTH+1].toString()
                            calendar[Calendar.MONTH] = monthInt
                            calendar[Calendar.YEAR] = formateDate1.toInt()
                            val date1 = calendar.time
                            println(
                                    "Current date : " + calendar[Calendar.MONTH]
                                            + "-"
                                            + calendar[Calendar.DATE]
                                            + "-"
                                            + calendar[Calendar.YEAR]
                            )
                            calendar.add(Calendar.MONTH, 11)
                            println(
                                    "date after 12 months : " + calendar[Calendar.MONTH]
                                            + "-"
                                            + calendar[Calendar.DATE]
                                            + "-"
                                            + calendar[Calendar.YEAR]
                            )
                            val date2 = calendar.time

                            toYear = calendar[Calendar.YEAR]
                            toMonth = (SimpleDateFormat("MM").format(date2).toInt())
                            target_month_to.setText(SimpleDateFormat("MMMM").format(date2) + " " + calendar[Calendar.YEAR])
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    }

                    et_fromviewtargetdate.setText(formateDate + " " + formateDate1)
                    fromYear = formateDate1.toInt()
                    fromMonth = (date.month + 1)

                    dialog = ProgressDialog(Day_sheduler@ this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    if (isInternetPresent) {
                        DayScheduleResult(formateDate1, (date.month + 1).toString(), toYear.toString(), toMonth.toString())
                    } else {
//        val toast = Toast.makeText(this@ActivityViewTarget, resources.getString(R.string.internet_connection_error), Toast.LENGTH_LONG)
//        toast.setGravity(Gravity.CENTER, 0, 0)
//        toast.show()
                        Global_Data.Custom_Toast(this@ActivityViewTarget, resources.getString(R.string.internet_connection_error), "yes")
                    }

                } else {
                    Global_Data.Custom_Toast(this, resources.getString(R.string.Please_Select_Duration), "Yes")
                }
                Global_Data.Target_Product_Category = ""
                Global_Data.target_amount = ""
                Global_Data.target_grpby = ""
                // Toast.makeText(this@ActivityViewTarget, "Its toast!"+dff, Toast.LENGTH_SHORT).show()
            }
        })

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
                val sp: SharedPreferences = this@ActivityViewTarget.getSharedPreferences("SimpleLogic", 0)
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
        val i = Intent(this@ActivityViewTarget, ActivityTarget::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i)
        finish()
    }

    fun DayScheduleResult(from_year: String, from_month: String, to_year: String, to_month: String) {
        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()

        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null)!!

        val Cust_domain = sp.getString("Cust_Service_Url", "")
        var service_url = Cust_domain + "metal/api/v1/"
        Target_Product_Category = Global_Data.Target_Product_Category
        var product_category_final = " "
        product_category_final = if (Target_Product_Category.equals(resources.getString(R.string.All_Product_Category), ignoreCase = true)) {
            " "
        } else {
            Target_Product_Category
        }
        var date_flag = ""
        if (Global_Data.target_grpby.equals(resources.getString(R.string.By_Product), ignoreCase = true)) {
            date_flag = "false"
            service_url = service_url + "targets/get_targets_by_product?email=" + user_email + "&from_year=" + from_year + "&from_month=" + from_month + "&to_year=" + to_year + "&to_month=" + to_month + "&primary_category=" + URLEncoder.encode(product_category_final, "UTF-8")
        } else if (Global_Data.target_grpby.equals(resources.getString(R.string.By_Date), ignoreCase = true)) {
            date_flag = "true"
            service_url = service_url + "targets/get_targets?email=" + user_email + "&from_year=" + from_year + "&from_month=" + from_month + "&to_year=" + to_year + "&to_month=" + to_month + "&primary_category=" + URLEncoder.encode(product_category_final, "UTF-8")
        } else {
            try {
                service_url = service_url + "targets/get_targets_by_product?email=" + (URLEncoder.encode(user_email, "UTF-8")) + "&from_year=" + from_year + "&from_month=" + from_month + "&to_year=" + to_year + "&to_month=" + to_month
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
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
                                tv_target.setText("0")
                                tv_achieved.setText("0")
                                tv_percentage.setText("0")
                                //Global_Data.Target_Product_Category=""
                                //Global_Data.target_amount = ""
                                //Global_Data.target_grpby=""
                                //finish().toString()
                                //finish()
                            } else {

                                val summaryData = json.getJSONArray("targets")
                                Log.i("volley", "response visits Length: " + summaryData.length())
                                Log.d("users", "visite_schedules$summaryData")
                                viewTargetList?.clear()
                                if (summaryData.length() > 0) {

                                    viewTargetList = ArrayList<ViewTargetModel>()
                                    for (i in 0 until summaryData.length()) {
                                        val jsonObject = summaryData.getJSONObject(i)

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("achieved").toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("target").toString())) {
                                            if (!jsonObject.getString("achieved").equals("null", ignoreCase = true) && !jsonObject.getString("achieved").equals(null, ignoreCase = true) and !jsonObject.getString("achieved").equals("", ignoreCase = true) and !jsonObject.getString("achieved").equals(" ", ignoreCase = true) && !jsonObject.getString("target").equals("null", ignoreCase = true) && !jsonObject.getString("target").equals(null, ignoreCase = true) and !jsonObject.getString("target").equals("", ignoreCase = true) and !jsonObject.getString("target").equals(" ", ignoreCase = true)) {
                                                val age_n = java.lang.Double.valueOf(jsonObject.getString("achieved").toString()) / java.lang.Double.valueOf(jsonObject.getString("target").toString()) * 100
                                                age_value = String.format("%.2f", age_n)
                                                //                                            agen_total +=  (Double.valueOf(jsonObject.getString("achieved").toString()))/(Double.valueOf(jsonObject.getString("target").toString()));
                                            } else {
                                                age_value = "0.0"
                                                agen_total += java.lang.Double.valueOf("0.0")
                                            }
                                            //ci.agegrp_str = "$age_value%"
                                        } else {
                                            age_value = "0.0"
                                        }

                                        //                                    if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("products_sub_category").toString()))
//                                    {
//                                        if(!jsonObject.getString("products_sub_category").equalsIgnoreCase("null") && !jsonObject.getString("products_sub_category").equalsIgnoreCase(null) & !jsonObject.getString("products_sub_category").equalsIgnoreCase("") & !jsonObject.getString("products_sub_category").equalsIgnoreCase(" "))
//                                        {
//                                            product_subcategory = jsonObject.getString("products_sub_category").toString();
//                                        }
//                                        else
//                                        {
//
//                                            product_subcategory = "";
//                                        }
//
//                                        ci.product_Sub_value = product_subcategory;
//                                    }
                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("target").toString())) {
                                            if (!jsonObject.getString("target").equals("null", ignoreCase = true) && !jsonObject.getString("target").equals(null, ignoreCase = true) and !jsonObject.getString("target").equals("", ignoreCase = true) and !jsonObject.getString("target").equals(" ", ignoreCase = true)) {
                                                if (Global_Data.target_amount.equals("In Crores", ignoreCase = true)) {
                                                    t_total += java.lang.Double.valueOf(jsonObject.getString("target").toString()) / 10000000
                                                    target_value = String.format("%.2f", java.lang.Double.valueOf(jsonObject.getString("target").toString()) / 10000000)
                                                } else if (Global_Data.target_amount.equals("In Lakhs", ignoreCase = true)) {
                                                    t_total += java.lang.Double.valueOf(jsonObject.getString("target").toString()) / 100000
                                                    target_value = String.format("%.2f", java.lang.Double.valueOf(jsonObject.getString("target").toString()) / 100000)
                                                } else if (Global_Data.target_amount.equals("In Thousands", ignoreCase = true)) {
                                                    t_total += java.lang.Double.valueOf(jsonObject.getString("target").toString()) / 1000
                                                    target_value = String.format("%.2f", java.lang.Double.valueOf(jsonObject.getString("target").toString()) / 1000)
                                                } else if (Global_Data.target_amount.equals("In Ruppes", ignoreCase = true)) {
                                                    t_total += java.lang.Double.valueOf(jsonObject.getString("target").toString())
                                                    target_value = String.format("%.2f", java.lang.Double.valueOf(jsonObject.getString("target").toString()))
                                                } else {
                                                    t_total += java.lang.Double.valueOf(jsonObject.getString("target").toString())
                                                    target_value = String.format("%.2f", java.lang.Double.valueOf(jsonObject.getString("target").toString()))

                                                }
                                                //                                                t_total +=Double.valueOf(jsonObject.getString("target").toString());
//                                               // target_value = String.valueOf(Double.valueOf(jsonObject.getString("target").toString()));
//                                                target_value = String.format("%.2f",Double.valueOf(jsonObject.getString("target").toString())/1000);
                                            } else {
                                                t_total += java.lang.Double.valueOf("0.00")
                                                target_value = java.lang.Double.valueOf("0.00").toString()
                                            }
                                            //ci.targetgrp_str = target_value
                                        } else {
                                            t_total += java.lang.Double.valueOf("0.00")
                                            target_value = java.lang.Double.valueOf("0.00").toString()
                                            //ci.targetgrp_str = target_value
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("achieved").toString())) {
                                            if (!jsonObject.getString("achieved").equals("null", ignoreCase = true) && !jsonObject.getString("achieved").equals(null, ignoreCase = true) and !jsonObject.getString("achieved").equals("", ignoreCase = true) and !jsonObject.getString("achieved").equals(" ", ignoreCase = true)) {
                                                if (Global_Data.target_amount.equals(resources.getString(R.string.In_Crores), ignoreCase = true)) {
                                                    achived_total += java.lang.Double.valueOf(jsonObject.getString("achieved").toString()) / 10000000
                                                    achieved_value = String.format("%.2f", java.lang.Double.valueOf(jsonObject.getString("achieved").toString()) / 10000000)
                                                } else if (Global_Data.target_amount.equals(resources.getString(R.string.In_Lakhs), ignoreCase = true)) {
                                                    achived_total += java.lang.Double.valueOf(jsonObject.getString("achieved").toString()) / 100000
                                                    achieved_value = String.format("%.2f", java.lang.Double.valueOf(jsonObject.getString("achieved").toString()) / 100000)
                                                } else if (Global_Data.target_amount.equals(resources.getString(R.string.In_Thousands), ignoreCase = true)) {
                                                    achived_total += java.lang.Double.valueOf(jsonObject.getString("achieved").toString()) / 1000
                                                    achieved_value = String.format("%.2f", java.lang.Double.valueOf(jsonObject.getString("achieved").toString()) / 1000)
                                                } else if (Global_Data.target_amount.equals(resources.getString(R.string.In_Ruppes), ignoreCase = true)) {
                                                    achived_total += java.lang.Double.valueOf(jsonObject.getString("achieved").toString())
                                                    achieved_value = String.format("%.2f", java.lang.Double.valueOf(jsonObject.getString("achieved").toString()))
                                                } else {
                                                    achived_total += java.lang.Double.valueOf(jsonObject.getString("achieved").toString())
                                                    achieved_value = String.format("%.2f", java.lang.Double.valueOf(jsonObject.getString("achieved").toString()))
                                                }
                                                //                                                achived_total +=Double.valueOf(jsonObject.getString("achieved").toString());
//                                                //achieved_value = String.valueOf(Double.valueOf(jsonObject.getString("achieved").toString()));
//                                                achieved_value = String.format("%.2f",Double.valueOf(jsonObject.getString("achieved").toString()));
                                            } else {
                                                achived_total += java.lang.Double.valueOf("0.00")
                                                achieved_value = java.lang.Double.valueOf("0.00").toString()
                                            }
                                            //ci.achievedgrp_str = achieved_value
                                        } else {
                                            achived_total += java.lang.Double.valueOf("0.00")
                                            achieved_value = java.lang.Double.valueOf("0.00").toString()
                                            //ci.achievedgrp_str = achieved_value
                                        }

                                        if (Global_Data.target_grpby.equals(resources.getString(R.string.By_Product), ignoreCase = true)) {
                                            viewTargetList!!.add(ViewTargetModel(jsonObject.getString("products_primary_category"), jsonObject.getString("target"), jsonObject.getString("achieved"), age_value + "%"))

                                        } else if (Global_Data.target_grpby.equals(resources.getString(R.string.By_Date), ignoreCase = true)) {
                                            viewTargetList!!.add(ViewTargetModel(jsonObject.getString("month") + " " + jsonObject.getString("year"), jsonObject.getString("target"), jsonObject.getString("achieved"), age_value + "%"))

                                        } else {
                                            viewTargetList!!.add(ViewTargetModel(jsonObject.getString("products_primary_category"), jsonObject.getString("target"), jsonObject.getString("achieved"), age_value + "%"))

                                        }

                                    }

                                    tv_target.setText(String.format("%.2f", t_total))
                                    tv_achieved.setText(String.format("%.2f", achived_total))

                                    val agen_total_final = java.lang.Double.valueOf(achived_total) / java.lang.Double.valueOf(t_total) * 100

                                    tv_percentage.setText(String.format("%.2f", agen_total_final) + "%")

                                    viewTargetAdapter = ViewTargetAdapter(this, viewTargetList!!)
                                    viewtarget_list?.adapter = viewTargetAdapter
                                    dialog!!.dismiss()
                                    //Global_Data.Target_Product_Category=""
                                    //Global_Data.target_amount = ""
                                    //Global_Data.target_grpby=""

                                } else {

                                    tv_target.setText("0")
                                    tv_achieved.setText("0")
                                    tv_percentage.setText("0")
                                    //Global_Data.Target_Product_Category=""
                                    //Global_Data.target_amount = ""
                                    //Global_Data.target_grpby=""

                                    Global_Data.Custom_Toast(this, resources.getString(R.string.Sorry_No_Record_Found), "Yes")
                                    viewTargetAdapter!!.notifyDataSetChanged()
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
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                "Network Error", "yes")
                    } else if (error is AuthFailureError) { //							Toast.makeText(Video_Main_List.this,
//									"Server AuthFailureError  Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@this,
//                                "Server AuthFailureError  Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                "Server AuthFailureError  Error", "yes")
                    } else if (error is ServerError) { //							Toast.makeText(Video_Main_List.this,
//									"Server   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@this,
//                                resources.getString(R.string.Server_Errors),
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                resources.getString(R.string.Server_Errors), "yes")
                    } else if (error is NetworkError) { //							Toast.makeText(Video_Main_List.this,
//									"Network   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@this,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                "Network Error", "yes")
                    } else if (error is ParseError) { //							Toast.makeText(Video_Main_List.this,
//									"ParseError   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@this,
//                                "ParseError   Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                "ParseError   Error", "yes")
                    } else { //Toast.makeText(Video_Main_List.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@this,
//                                error.message,
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                error.message, "yes")
                    }
                    //						Intent launch = new Intent(Video_Main_List.this,MainActivity.class);
//						startActivity(launch);
                    finish()
                    dialog!!.dismiss()
                    // finish();
                })
        val requestQueue = Volley.newRequestQueue(QuoteAddRetailerTrait@ this)
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

    fun ViewTargetFiterDialog(from_year: String, from_month: String, to_year: String, to_month: String) {
        //fun ViewTargetFiterDialog() {
        dialogFilter = Dialog(this@ActivityViewTarget)
        dialogFilter!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFilter!!.setContentView(R.layout.viewtarget_filter)

        val btnOkDialog = dialogFilter!!.findViewById<View>(R.id.btnok_useremaildialog) as Button
        val btnCalcelDialog = dialogFilter!!.findViewById<View>(R.id.btncancel_useremaildialog) as Button

        val targetProductCategory = dialogFilter!!.findViewById<View>(R.id.target_product_category) as Spinner
        val targetAmount = dialogFilter!!.findViewById<View>(R.id.target_amount) as Spinner
        val targetGrpby = dialogFilter!!.findViewById<View>(R.id.target_grpby) as Spinner

        target_amount_list.clear()
        target_amount_list.add(resources.getString(R.string.Please_Select_Amount))
        target_amount_list.add(resources.getString(R.string.In_Crores))
        target_amount_list.add(resources.getString(R.string.In_Lakhs))
        target_amount_list.add(resources.getString(R.string.In_Thousands))
        target_amount_list.add(resources.getString(R.string.In_Ruppes))

        target_grpby_list.clear()
        target_grpby_list.add(resources.getString(R.string.By_Date))
        target_grpby_list.add(resources.getString(R.string.By_Product))

        adapter_target_amount = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, target_amount_list)
        adapter_target_amount!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        targetAmount.setAdapter(adapter_target_amount)
        targetAmount.setSelection(3)
        //targetAmount.setOnItemSelectedListener(this)

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.target_amount)) {
            val spinnerPosition = adapter_target_amount!!.getPosition(Global_Data.target_amount)
            targetAmount.setSelection(spinnerPosition)
        }

        adapter_target_grpby = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, target_grpby_list)
        adapter_target_grpby!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        targetGrpby.setAdapter(adapter_target_grpby)
        //targetGrpby.setOnItemSelectedListener(this)

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.target_grpby)) {
            val spinnerPosition = adapter_target_grpby!!.getPosition(Global_Data.target_grpby)
            targetGrpby.setSelection(spinnerPosition)
        } else {
            val spinnerPosition = adapter_target_grpby!!.getPosition(resources.getString(R.string.By_Product))
            targetGrpby.setSelection(spinnerPosition)
        }

        targetGrpby.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item: Any = parent.getItemAtPosition(pos)

                if(item.equals(resources.getString(R.string.By_Product))){

                    targetProductCategory.visibility=View.VISIBLE

                }else{

                    targetProductCategory.visibility=View.GONE

                }

                //println("it works...   ")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        if (targetGrpby.selectedItem.toString().equals(resources.getString(R.string.By_Product))){
            targetProductCategory.visibility=View.VISIBLE


        }else{
            targetProductCategory.visibility=View.GONE

        }



        product_category_list.clear()
        val contacts1 = dbvoc.HSS_DescriptionITEM()
        product_category_list.add(resources.getString(R.string.Select_Product_Category))
        for (cn in contacts1) {
            if (!cn.stateName.equals("", ignoreCase = true) && !cn.stateName.equals(" ", ignoreCase = true)) {
                val str_categ = "" + cn.stateName
                product_category_list.add(str_categ)

            }
        }

        if (product_category_list.size > 2) {
            product_category_list.add(resources.getString(R.string.All_Product_Category))
        }
        adapter_product_category = ArrayAdapter(this, android.R.layout.simple_spinner_item, product_category_list)
        adapter_product_category!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        targetProductCategory.setAdapter(adapter_product_category)

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.Target_Product_Category)) {
            val spinnerPosition = adapter_product_category!!.getPosition(Global_Data.Target_Product_Category)
            targetProductCategory.setSelection(spinnerPosition)
        } else {
            if (product_category_list.contains(resources.getString(R.string.All_Product_Category))) {
                val spinnerPosition = adapter_product_category!!.getPosition(resources.getString(R.string.All_Product_Category))
                targetProductCategory.setSelection(spinnerPosition)
            }
        }

        btnOkDialog.setOnClickListener {
            dialog = ProgressDialog(Day_sheduler@ this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
            Global_Data.Target_Product_Category = targetProductCategory.getSelectedItem().toString().trim();
            Global_Data.target_amount = targetAmount.getSelectedItem().toString().trim();
            Global_Data.target_grpby = targetGrpby.getSelectedItem().toString().trim();

            if (isInternetPresent) {
                dialogFilter!!.dismiss()
                DayScheduleResult(from_year, from_month, to_year, to_month)
            } else {
//                val toast = Toast.makeText(this@ActivityViewTarget, resources.getString(R.string.internet_connection_error), Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.CENTER, 0, 0)
//                toast.show()
                Global_Data.Custom_Toast(this@ActivityViewTarget, resources.getString(R.string.internet_connection_error), "yes")
            }
        }

        btnCalcelDialog.setOnClickListener { dialogFilter!!.dismiss() }
        dialogFilter!!.show()
    }
}