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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import com.msimplelogic.adapter.HolidayListAdapter
import com.msimplelogic.adapter.LeaveManagementAdapter
import com.msimplelogic.adapter.LeaveTypeAdapter
import com.msimplelogic.model.LeaveManagementModel
import cpm.simplelogic.helper.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_leavemanagement.*
import kotlinx.android.synthetic.main.content_leavemanagement.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*
import kotlin.collections.HashMap
import  com.msimplelogic.activities.R
import kotlinx.android.synthetic.main.content_planner.*
import kotlinx.android.synthetic.main.content_taskdetails.*

class ActivityLeaveManagement : BaseActivity() {
    private var leaveManagementAdapter: LeaveManagementAdapter? = null
    private var leaveManagementList: MutableList<LeaveManagementModel>? = null

    private var leaveTypeAdapter: LeaveTypeAdapter? = null
    private var leaveTypeList: MutableList<LeaveManagementModel>? = null

    private var holidayListAdapter: HolidayListAdapter? = null
    private var holidayList: MutableList<LeaveManagementModel>? = null

    var adapter_state2: ArrayAdapter<String>? = null
    private var results = ArrayList<String>()
    var resultsHashMap: HashMap<String, String> = HashMap<String, String>()

    var adapter_state3: ArrayAdapter<String>? = null
    private val results2 = ArrayList<String>()
    var context: Context? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var dialog: ProgressDialog? = null
    var tab_click_flag = "";
    var full_day_click = false;
    var half_day_click = false;
    var approval_name = "";
    var hod_name = "";
    var datet = 0
    var hrmanager_name = "";
    var sp: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leavemanagement)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        context = ActivityTask@ this
        cd = ConnectionDetector(context)
        dialog = ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)


        results.add(resources.getString(R.string.Select_Type))
        //results.add("Type 1")
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theamae.setImageResource(R.drawable.dark_hedder)

            order.setImageResource(R.drawable.leave_balance_dark)
            order2.setImageResource(R.drawable.apply_leaves_dark)
            orders.setImageResource(R.drawable.holiday_list_dark)
        }


//        results2.add(resources.getString(R.string.Select_Reason))
//        results2.add("Reason 1")

        adapter_state2 = ArrayAdapter<String>(this,
                R.layout.spinner_item, results)
        adapter_state2!!.setDropDownViewResource(R.layout.spinner_item)
        et_selecttype.setAdapter(adapter_state2)

//        adapter_state3 = ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, results2)
//        adapter_state3!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        et_reason.setAdapter(adapter_state3)


        et_selecttype.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                (view as TextView).setTextColor(ContextCompat.getColor(applicationContext, android.R.color.black)) //Change selected text color
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        from_halfday.setOnClickListener { view ->
            if (!et_applyfrom.text.toString().equals("") && !et_applyto.text.toString().equals("")) {
                if (et_applyfrom.text.toString().equals(et_applyto.text.toString())) {
                    // full_day_click = true
                    //half_day_click = false

                    if (full_day_click == true) {
                        full_day_click = false
                        from_halfday.isChecked = false

                    } else {
                        full_day_click = true
                        from_halfday.isChecked = true
                    }

                    if (to_halfday.isChecked == true && from_halfday.isChecked == true) {
                        half_day_click = false
                        to_halfday.isChecked = false

                    } else
                        if (to_halfday.isChecked == true && from_halfday.isChecked == false) {
                            half_day_click = true
                            to_halfday.isChecked = true
                        }


                } else {
                    //full_day_click = true
                    //from_halfday.isChecked = true

                    if (full_day_click == true) {
                        full_day_click = false
                        from_halfday.isChecked = false

                    } else {
                        full_day_click = true
                        from_halfday.isChecked = true
                    }


                }
            } else {
                from_halfday.isChecked = false
                to_halfday.isChecked = false
                full_day_click = false
                half_day_click = false

                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_Date), "Yes")
            }
        }
//        from_halfday.setOnCheckedChangeListener { buttonView, isChecked ->
//
//
//
//        }

        to_halfday.setOnClickListener { view ->
            if (!et_applyfrom.text.toString().equals("") && !et_applyto.text.toString().equals("")) {
                if (et_applyfrom.text.toString().equals(et_applyto.text.toString())) {
                    if (half_day_click == true) {
                        half_day_click = false
                        to_halfday.isChecked = false

                    } else {
                        half_day_click = true
                        to_halfday.isChecked = true
                    }

                    if (from_halfday.isChecked == true && to_halfday.isChecked == true) {
                        full_day_click = false
                        from_halfday.isChecked = false

                    } else
                        if (from_halfday.isChecked == true && to_halfday.isChecked == false) {
                            full_day_click = true
                            from_halfday.isChecked = true
                        }
                } else {
                    if (half_day_click == true) {
                        half_day_click = false
                        to_halfday.isChecked = false

                    } else {
                        half_day_click = true
                        to_halfday.isChecked = true
                    }

                }
            } else {
                from_halfday.isChecked = false
                to_halfday.isChecked = false
                full_day_click = false
                half_day_click = false
                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_Date), "Yes")
            }
        }

//        to_halfday.setOnCheckedChangeListener { buttonView, isChecked ->
//           // Toast.makeText(this,isChecked.toString(),Toast.LENGTH_SHORT).show()
//
//        }

//        et_reason.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
//                (view as TextView).setTextColor(ContextCompat.getColor(applicationContext, android.R.color.darker_gray)) //Change selected text color
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        })

        et_applyfrom!!.setOnClickListener {
            val now = Calendar.getInstance()

            val det = DatePickerDialog(
                    ActivityTaskDetails@ this,
                    DatePickerDialog.OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        var mo = month
                        ++mo
                        Log.d("Orignal", mo.toString())
                        var date_from = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;
                        et_applyfrom.setText(date_from)

                        full_day_click = false
                        half_day_click = false
                        from_halfday.isChecked = false
                        to_halfday.isChecked = false

                        if (!et_applyfrom.equals("") && !et_applyto.equals("")) {
                            var time = Global_Data.getdiff(et_applyfrom.text.toString(), et_applyto.text.toString())
                            var days = (time / (1000 * 60 * 60 * 24)) + 1;
                            et_totaldays.setText(days.toString());
                            var str = et_totaldays.text.toString()
                            datet = str.toInt()
                        }

                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)

            )
            det.datePicker.setMinDate(System.currentTimeMillis() - 1000)
            det.show()
            //det.d.setMinDate(System.currentTimeMillis() - 1000);
        }

        et_applyto!!.setOnClickListener {
            if (!et_applyfrom.text.toString().equals("")) {


                val now = Calendar.getInstance()
                val dpt = DatePickerDialog(
                        ActivityTaskDetails@ this,
                        DatePickerDialog.OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                            var mo = month
                            ++mo
                            Log.d("Orignal", mo.toString())
                            var date_to = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;

                            et_applyto.setText(date_to)
                            full_day_click = false
                            half_day_click = false
                            from_halfday.isChecked = false
                            to_halfday.isChecked = false

                            if (!et_applyfrom.equals("") && !et_applyto.equals("")) {
                                var time = Global_Data.getdiff(et_applyfrom.text.toString(), et_applyto.text.toString())
                                var days = (time / (1000 * 60 * 60 * 24)) + 1;
                                et_totaldays.setText(days.toString());
                                var str = et_totaldays.text.toString()
                                datet = str.toInt()

                            }


                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                )
                dpt.datePicker.setMinDate(System.currentTimeMillis() - 1000)
                dpt.show()

            } else {
                Global_Data.Custom_Toast(this@ActivityLeaveManagement, "Please select from date first", "yes")

            }

        }

        card_leavebalance.setBackgroundResource(R.drawable.leavem_bg)
        card_applyleaves.setBackgroundResource(R.drawable.leavebal_bg)
        //card_applyleaves.setCardBackgroundColor(Color.WHITE)
        card_holidayslist.setBackgroundResource(R.drawable.leavebal_bg)
        card_leavetype.visibility = View.GONE
        card_totalbalance.visibility = View.VISIBLE
        btn_viewhistory.visibility = View.VISIBLE
        apllyleave_card.visibility = View.GONE
        holidaylists_card.visibility = View.GONE

        leaveManagementList = java.util.ArrayList()
        leaveManagementAdapter = LeaveManagementAdapter(this, leaveManagementList as java.util.ArrayList<LeaveManagementModel>)
        val mLayoutManager = LinearLayoutManager(this)
        leavebalance_list!!.layoutManager = mLayoutManager
        leavebalance_list.addItemDecoration(DividerItemDecoration(this@ActivityLeaveManagement, LinearLayoutManager.VERTICAL))
        leavebalance_list!!.itemAnimator = DefaultItemAnimator()
        leavebalance_list!!.adapter = leaveManagementAdapter

        tab_click_flag = "LeaveBalance"
        leaveManagementList()

        card_leavebalance.setOnClickListener(View.OnClickListener {
            try {
                Kot_Gloval.hideKeyboard(context = ActivityLeaveManagement@ this, view = it)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            card_leavebalance.setBackgroundResource(R.drawable.leavem_bg)
            card_applyleaves.setBackgroundResource(R.drawable.leavebal_bg)
            //card_applyleaves.setCardBackgroundColor(Color.WHITE)
            card_holidayslist.setBackgroundResource(R.drawable.leavebal_bg)
            card_leavetype.visibility = View.GONE
            card_totalbalance.visibility = View.VISIBLE
            btn_viewhistory.visibility = View.VISIBLE
            apllyleave_card.visibility = View.GONE
            holidaylists_card.visibility = View.GONE

            leaveManagementList = java.util.ArrayList()
            leaveManagementAdapter = LeaveManagementAdapter(this, leaveManagementList as java.util.ArrayList<LeaveManagementModel>)
            val mLayoutManager = LinearLayoutManager(this)
            leavebalance_list!!.layoutManager = mLayoutManager
            leavebalance_list.addItemDecoration(DividerItemDecoration(this@ActivityLeaveManagement, LinearLayoutManager.VERTICAL))
            leavebalance_list!!.itemAnimator = DefaultItemAnimator()
            leavebalance_list!!.adapter = leaveManagementAdapter

            tab_click_flag = "LeaveBalance"
            leaveManagementList()

        })

        card_applyleaves.setOnClickListener(View.OnClickListener {
            try {
                Kot_Gloval.hideKeyboard(context = ActivityLeaveManagement@ this, view = it)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            card_applyleaves.setBackgroundResource(R.drawable.leavem_bg)
            card_leavebalance.setBackgroundResource(R.drawable.leavebal_bg)
            card_holidayslist.setBackgroundResource(R.drawable.leavebal_bg)

            apllyleave_card.visibility = View.VISIBLE
            holidaylists_card.visibility = View.GONE
            card_leavetype.visibility = View.GONE
            card_totalbalance.visibility = View.GONE
            btn_viewhistory.visibility = View.GONE

            tab_click_flag = "ApplyLeave"
            GetHolidayListData()
        })

        card_holidayslist.setOnClickListener(View.OnClickListener {
            try {
                Kot_Gloval.hideKeyboard(context = ActivityLeaveManagement@ this, view = it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            card_holidayslist.setBackgroundResource(R.drawable.leavem_bg)
            card_leavebalance.setBackgroundResource(R.drawable.leavebal_bg)
            card_applyleaves.setBackgroundResource(R.drawable.leavebal_bg)

            apllyleave_card.visibility = View.GONE
            holidaylists_card.visibility = View.VISIBLE
            card_leavetype.visibility = View.GONE
            card_totalbalance.visibility = View.GONE
            btn_viewhistory.visibility = View.GONE

            holidayList = java.util.ArrayList()
            holidayListAdapter = HolidayListAdapter(this, holidayList as java.util.ArrayList<LeaveManagementModel>)
            val mLayoutManager1 = LinearLayoutManager(this)
            holidaylist_list!!.layoutManager = mLayoutManager1
            holidaylist_list.addItemDecoration(DividerItemDecoration(this@ActivityLeaveManagement, LinearLayoutManager.VERTICAL))
            holidaylist_list!!.itemAnimator = DefaultItemAnimator()
            holidaylist_list!!.adapter = holidayListAdapter
            //leavetype_list.hideShimmerAdapter()
            tab_click_flag = "HolidayList"
            holidayList()


        })

        btn_viewhistory.setOnClickListener(View.OnClickListener {

            try {
                Kot_Gloval.hideKeyboard(context = ActivityLeaveManagement@ this, view = it)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            card_leavetype.visibility = View.VISIBLE
            card_totalbalance.visibility = View.GONE
            btn_viewhistory.visibility = View.GONE
            apllyleave_card.visibility = View.GONE

            leaveTypeList = java.util.ArrayList()
            leaveTypeAdapter = LeaveTypeAdapter(this, leaveTypeList as java.util.ArrayList<LeaveManagementModel>)

            val mLayoutManager = LinearLayoutManager(this)
            leavetype_list!!.layoutManager = mLayoutManager
            leavetype_list.addItemDecoration(DividerItemDecoration(this@ActivityLeaveManagement, LinearLayoutManager.VERTICAL))
            leavetype_list!!.itemAnimator = DefaultItemAnimator()
            leavetype_list!!.adapter = leaveTypeAdapter
            //leavetype_list.hideShimmerAdapter()
            tab_click_flag = "History"
            leaveTypeList()


        })

        add_leave_click.setOnClickListener {
            if (et_selecttype.getSelectedItem().toString().equals(resources.getString(R.string.Select_Type), ignoreCase = true)) {
                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_Type), "Yes")
            } else
                if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_applyfrom.text.toString())) {
                    Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_From_Date), "Yes")
                } else
                    if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_applyto.text.toString())) {
                        Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_To_Date), "Yes")
                    } else
                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_totaldays.text.toString())) {
                            Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Please_Total_Days), "Yes")
                        } else if (datet < 0) {
                            Global_Data.Custom_Toast(this@ActivityLeaveManagement, "To date must be grater than from date", "yes")

                        } else
                            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_reason.text.toString())) {
                                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Please_enter_reason), "Yes")
                            } else {
                                SyncLeave()

                            }

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
                val sp: SharedPreferences = this@ActivityLeaveManagement.getSharedPreferences("SimpleLogic", 0)
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

        if (tab_click_flag == "History") {
            val i = Intent(this@ActivityLeaveManagement, this@ActivityLeaveManagement::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
            finish()
        } else {
            val i = Intent(this@ActivityLeaveManagement, ActivityPlanner::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
            finish()

        }

    }

    private fun leaveManagementList() {

        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {
            GetHolidayListData()
        } else {
            //     Toast.makeText(context, "You don't have internet connection.", Toast.LENGTH_SHORT).show()
            Global_Data.Custom_Toast(context, "You don't have internet connection.", "")
        }


    }

    private fun leaveTypeList() {
        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {
            GetHolidayListData()
        } else {
            //    Toast.makeText(context, "You don't have internet connection.", Toast.LENGTH_SHORT).show()
            Global_Data.Custom_Toast(context, "You don't have internet connection.", "")
        }

    }

    private fun holidayList() {


        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {
            GetHolidayListData()
        } else {
            //   Toast.makeText(context, "You don't have internet connection.", Toast.LENGTH_SHORT).show()
            Global_Data.Custom_Toast(context, "You don't have internet connection.", "")
        }
    }


    fun GetHolidayListData() {

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

            if (tab_click_flag.equals("HolidayList")) {
                service_url = service_url + "leave_management/holiday_lists?email=" + URLEncoder.encode(user_email, "UTF-8")

            } else
                if (tab_click_flag.equals("LeaveBalance")) {

                    service_url = service_url + "leave_management/leave_type_lists?email=" + URLEncoder.encode(user_email, "UTF-8")

                } else
                    if (tab_click_flag.equals("History")) {

                        service_url = service_url + "leave_management/display_leaves?email=" + URLEncoder.encode(user_email, "UTF-8")

                    } else
                        if (tab_click_flag.equals("ApplyLeave")) {

                            service_url = service_url + "leave_management/leave_type_lists?email=" + URLEncoder.encode(user_email, "UTF-8")

                        }
            Log.i("volley", "response service: " + service_url)

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
                                Global_Data.Custom_Toast(context, response_result, "Yes")
                                dialog!!.hide()

                            } else {

                                leaveTypeList = java.util.ArrayList()
                                leaveManagementList = java.util.ArrayList()
                                holidayList = java.util.ArrayList()

                                leaveManagementList!!.clear()
                                holidayList!!.clear()
                                leaveTypeList!!.clear()
                                results.clear()
                                results.add(resources.getString(R.string.Select_Type))
                                resultsHashMap.clear()

                                if (tab_click_flag.equals("HolidayList")) {

                                    val holidays = json.getJSONArray("holidays")

                                    Log.i("volley", "response activity_planner Length: " + holidays.length())

                                    Log.d("users", "holidays$holidays")

                                    for (i in 0 until holidays.length()) {
                                        val jsonObject = holidays.getJSONObject(i)

                                        holidayList!!.add(LeaveManagementModel(jsonObject.getString("name"), jsonObject.getString("date"), "", ""))
                                    }

                                    holidayListAdapter = HolidayListAdapter(this, holidayList as java.util.ArrayList<LeaveManagementModel>)
                                    holidaylist_list.addItemDecoration(DividerItemDecoration(this@ActivityLeaveManagement, LinearLayoutManager.VERTICAL))
                                    holidaylist_list!!.itemAnimator = DefaultItemAnimator()
                                    holidaylist_list!!.adapter = holidayListAdapter
                                    holidayListAdapter!!.notifyDataSetChanged()

                                } else
                                    if (tab_click_flag.equals("LeaveBalance")) {

                                        val leave_types = json.getJSONArray("leave_types")

                                        Log.i("volley", "response activity_planner Length: " + leave_types.length())

                                        Log.d("users", "activity_planner$leave_types")

                                        if (json.has("manager")) {
                                            approval_name = json.getString("manager")
                                            approval_names.setText(approval_name)
                                        }

                                        for (i in 0 until leave_types.length()) {
                                            val jsonObject = leave_types.getJSONObject(i)

                                            leaveManagementList!!.add(LeaveManagementModel(jsonObject.getString("name"), jsonObject.getString("total"), jsonObject.getString("pending_leave"), jsonObject.getString("pending_for_approval")))
                                        }

                                        leaveManagementAdapter = LeaveManagementAdapter(this, leaveManagementList as java.util.ArrayList<LeaveManagementModel>)
                                        leavebalance_list.addItemDecoration(DividerItemDecoration(this@ActivityLeaveManagement, LinearLayoutManager.VERTICAL))
                                        leavebalance_list!!.itemAnimator = DefaultItemAnimator()
                                        leavebalance_list!!.adapter = leaveManagementAdapter

                                        leaveManagementAdapter!!.notifyDataSetChanged()

                                    } else
                                        if (tab_click_flag.equals("History")) {

                                            val leave_manages = json.getJSONArray("leave_manages")

                                            Log.i("volley", "response leave_manages Length: " + leave_manages.length())

                                            Log.d("users", "leave_manages$leave_manages")

                                            for (i in 0 until leave_manages.length()) {
                                                val jsonObject = leave_manages.getJSONObject(i)

                                                leaveTypeList!!.add(LeaveManagementModel(jsonObject.getString("leave_type"), jsonObject.getString("created_at"), "", ""))
                                            }

                                            leaveTypeAdapter = LeaveTypeAdapter(this, leaveTypeList as java.util.ArrayList<LeaveManagementModel>)
                                            leavetype_list.addItemDecoration(DividerItemDecoration(this@ActivityLeaveManagement, LinearLayoutManager.VERTICAL))
                                            leavetype_list!!.itemAnimator = DefaultItemAnimator()
                                            leavetype_list!!.adapter = leaveTypeAdapter

                                            leaveTypeAdapter!!.notifyDataSetChanged()

                                        } else
                                            if (tab_click_flag.equals("ApplyLeave")) {

                                                val leave_types = json.getJSONArray("leave_types")

                                                Log.i("volley", "response activity_planner Length: " + leave_types.length())

                                                Log.d("users", "activity_planner$leave_types")

                                                if (json.has("manager")) {
                                                    approval_name = json.getString("manager")
                                                    approval_name?.let {
                                                        approval_names.setText(approval_name)
                                                    }
                                                }

                                                if (json.has("hod")) {
                                                    hod_name = json.getString("hod")
                                                    hod_name?.let {
                                                        hod_names.setText(hod_name)
                                                    }

                                                }

                                                if (json.has("hr_manager")) {
                                                    hrmanager_name = json.getString("hr_manager")

                                                    hrmanager_name?.let {
                                                        hr_names.setText(hrmanager_name)
                                                    }

                                                }

                                                for (i in 0 until leave_types.length()) {
                                                    val jsonObject = leave_types.getJSONObject(i)

                                                    results.add(jsonObject.getString("name"));
                                                    resultsHashMap.put(jsonObject.getString("name"), jsonObject.getString("id"))

                                                }

                                                adapter_state2 = ArrayAdapter<String>(this,
                                                        R.layout.spinner_item, results)
                                                adapter_state2!!.setDropDownViewResource(R.layout.spinner_item)
                                                et_selecttype.setAdapter(adapter_state2)

                                            }


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
                                "Network Error", "")
                    } else if (error is AuthFailureError) { //							Toast.makeText(Video_Main_List.this,
//									"Server AuthFailureError  Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                "Server AuthFailureError  Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                "Server AuthFailureError  Error", "yes")
                    } else if (error is ServerError) { //							Toast.makeText(Video_Main_List.this,
//									"Server   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                resources.getString(R.string.Server_Errors),
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                resources.getString(R.string.Server_Errors), "yes")
                    } else if (error is NetworkError) { //							Toast.makeText(Video_Main_List.this,
//									"Network   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                "Network Error", "yes")
                    } else if (error is ParseError) { //							Toast.makeText(Video_Main_List.this,
//									"ParseError   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                "ParseError   Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                "ParseError   Error", "yes")
                    } else { //Toast.makeText(Video_Main_List.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                error.message,
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                error.message, "yes")
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

    fun SyncLeave() {
        System.gc()
        val reason_code = ""
        try {
            dialog!!.setMessage(resources.getString(R.string.Please_Wait))
            dialog!!.setTitle(resources.getString(R.string.app_name))
            dialog!!.setCancelable(false)
            dialog!!.show()
            var domain = ""
            var device_id = ""
            val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
            val Cust_domain = sp.getString("Cust_Service_Url", "")
            val service_url = Cust_domain + "metal/api/v1/"
            val spf: SharedPreferences = context!!.getSharedPreferences("SimpleLogic", 0)
            val user_email = spf.getString("USER_EMAIL", null)
            val shopcode = spf.getString("shopcode", null)
            val fcm_token = spf.getString("fcm_token", null)

            val randomPIN = System.currentTimeMillis()
            val PINString = "T$randomPIN"
            var jsObjRequest: JsonObjectRequest? = null
            try {
                Log.d("Server url", "Server url" + domain)
                val COLLECTION_RECJARRAY = JSONArray()
                val COLLECTION_RECJOBJECT = JSONObject()
                //val Schedule_JOB = JSONObject()


                domain = service_url + "leave_management/apply_leave"
                //val notes_sT = JSONObject()
                var id = "";
                if (!et_selecttype.getSelectedItem().toString().equals(resources.getString(R.string.Select_Type), ignoreCase = true)) {
                    id = resultsHashMap.get(et_selecttype.getSelectedItem().toString())!!
                }


                COLLECTION_RECJOBJECT.put("latitude", Global_Data.GLOvel_LATITUDE)
                COLLECTION_RECJOBJECT.put("longitude", Global_Data.GLOvel_LONGITUDE)
                COLLECTION_RECJOBJECT.put("leave_type_id", id)
                COLLECTION_RECJOBJECT.put("from_date", et_applyfrom!!.text)
                COLLECTION_RECJOBJECT.put("to_date", et_applyto!!.text)
                COLLECTION_RECJOBJECT.put("total_days", et_totaldays!!.text)
                COLLECTION_RECJOBJECT.put("reason", et_reason!!.text)
                COLLECTION_RECJOBJECT.put("from_half", full_day_click)
                COLLECTION_RECJOBJECT.put("to_half ", half_day_click)
                COLLECTION_RECJOBJECT.put("email", user_email)

                //COLLECTION_RECJARRAY.put(notes_sT)

                // COLLECTION_RECJOBJECT.put("notes", COLLECTION_RECJARRAY)
                COLLECTION_RECJOBJECT.put("email", user_email)



                Log.d("Activity Service", COLLECTION_RECJOBJECT.toString())
                jsObjRequest = JsonObjectRequest(Request.Method.POST, domain, COLLECTION_RECJOBJECT, Response.Listener { response ->
                    Log.i("volley", "response: $response")
                    Log.d("jV", "JV length" + response.length())
                    try {
                        var response_result = ""
                        response_result = if (response.has("result")) {
                            response.getString("result")
                        } else {
                            "data"
                        }

                        if (response_result.equals("Leave Applied Successfully", ignoreCase = true)) {
                            dialog!!.dismiss()

                            Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Leave_Created), "Yes")

                            val i = Intent(context, MainActivity::class.java)
                            startActivity(i)
                            finish()
                        } else if (response_result.equals("Leave Already Applied", ignoreCase = true)) {
                            dialog!!.dismiss()

                            Global_Data.Custom_Toast(ActivityTaskDetails@ this, "Leave Already Applied", "Yes")


                        } else {
                            dialog!!.dismiss()
                            Global_Data.Custom_Toast(context, response_result, "Yes")

                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                        dialog!!.dismiss()
                    }
                    dialog!!.dismiss()
                    dialog!!.dismiss()
                }, Response.ErrorListener { error ->
                    //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    if (error is TimeoutError || error is NoConnectionError) {
                        Global_Data.Custom_Toast(context,
                                "your internet connection is not working, saving locally. Please sync when Internet is available", "")
                        //                            Toast.makeText(Cash_Collect.this,
                        //                                    "your internet connection is not working, saving locally. Please sync when Internet is available",
                        //                                    Toast.LENGTH_LONG).show();
                    } else if (error is AuthFailureError) {
                        Global_Data.Custom_Toast(context,
                                "Server AuthFailureError  Error", "")
                        //                            Toast.makeText(Cash_Collect.this,
                        //                                    "Server AuthFailureError  Error",
                        //                                    Toast.LENGTH_LONG).show();
                    } else if (error is ServerError) {
                        Global_Data.Custom_Toast(context,
                                resources.getString(R.string.Server_Errors), "")
                        //                            Toast.makeText(Cash_Collect.this,
                        //                                    getResources().getString(R.string.Server_Errors),
                        //                                    Toast.LENGTH_LONG).show();
                    } else if (error is NetworkError) {
                        Global_Data.Custom_Toast(context,
                                "your internet connection is not working, saving locally. Please sync when Internet is available", "")
                        //                            Toast.makeText(Cash_Collect.this,
                        //                                    "your internet connection is not working, saving locally. Please sync when Internet is available",
                        //                                    Toast.LENGTH_LONG).show();
                    } else if (error is ParseError) {
                        Global_Data.Custom_Toast(context,
                                "ParseError   Error", "")
                        //                            Toast.makeText(Cash_Collect.this,
                        //                                    "ParseError   Error",
                        //                                    Toast.LENGTH_LONG).show();
                    } else {
                        Global_Data.Custom_Toast(context, error.message, "")
                        //Toast.makeText(Cash_Collect.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    dialog!!.dismiss()
                    // finish();
                })
                val requestQueue = Volley.newRequestQueue(context)
                val socketTimeout = 150000 //90 seconds - change to what you want
                val policy: RetryPolicy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                jsObjRequest.retryPolicy = policy
                // requestQueue.se
//requestQueue.add(jsObjRequest);
                jsObjRequest.setShouldCache(false)
                requestQueue.cache.clear()
                requestQueue.add(jsObjRequest)
            } catch (e: Exception) {
                e.printStackTrace()
                dialog!!.dismiss()
            }
        } catch (e: Exception) { // TODO: handle exception
            Log.e("DATA", e.message.toString())
        }
    }

}