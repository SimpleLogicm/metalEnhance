package com.msimplelogic.activities.kotlinFiles


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.activities.*
import com.msimplelogic.activities.R
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval.Companion.Listresults
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval.Companion.ListresultsHashMap
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval.Companion.list_customers
import com.msimplelogic.adapter.PromotionalMeeetingAdapter
import com.msimplelogic.helper.MyLayoutOperation
import com.msimplelogic.model.UpdateStockModel
import com.msimplelogic.services.getServices
import cpm.simplelogic.helper.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_promotional_meeting_add.*
import kotlinx.android.synthetic.main.content_promotional_meeting_add.*
import kotlinx.android.synthetic.main.content_promotional_meetings.*
import kotlinx.android.synthetic.main.content_taskdetails.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Promotional_meeting_add : BaseActivity() {

    var parent_linear_layout: LinearLayout? = null
    var add: ImageView? = null
    var delete: ImageView? = null
    var delete_layout: ImageView? = null
    var rowView: View? = null
    var type_spin: Spinner? = null
    var et_topic: EditText? = null
    var et_date: EditText? = null
    var et_time: EditText? = null
    var attendees: EditText? = null
    var et_location: AutoCompleteTextView? = null
    var btn_save: Button? = null
    private var mAdapter: PromotionalMeeetingAdapter? = null
    var updateStockModel: MutableList<UpdateStockModel> = ArrayList()
    var adapter_state2: ArrayAdapter<String>? = null
    var dialog: ProgressDialog? = null
    var context: Context? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var time_date = "";
    var code = "";
    var bs_check = ""
    var ss = StringBuilder()
    var i = 0
    var dbvoc = DataBaseHelper(this)
    var list_customers_address = java.util.ArrayList<String>()
    var detailsList: ArrayList<String> = ArrayList<String>()
    var sp:SharedPreferences?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_promotional_meeting_add)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        context = ActivityTask@ this
        cd = ConnectionDetector(context)
        dialog = ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)

        parent_linear_layout = findViewById(com.msimplelogic.activities.R.id.parent_linear_layout)
        add = findViewById(com.msimplelogic.activities.R.id.add)
        type_spin = findViewById(com.msimplelogic.activities.R.id.type_spin)
        et_topic = findViewById(com.msimplelogic.activities.R.id.et_topic)
        et_date = findViewById(com.msimplelogic.activities.R.id.et_date)
        et_time = findViewById(com.msimplelogic.activities.R.id.et_time)
        attendees = findViewById(com.msimplelogic.activities.R.id.attendees)
        et_location = findViewById(com.msimplelogic.activities.R.id.et_location)
        btn_save = findViewById(com.msimplelogic.activities.R.id.btn_save)


//        Listresults.add(resources.getString(R.string.Select_Type))
//        Listresults.add(resources.getString(R.string.Standard))
//        Listresults.add(resources.getString(R.string.Executive))
        adapter_state2 = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Listresults)
        adapter_state2!!.setDropDownViewResource(R.layout.spinner_item)
        type_spin!!.setAdapter(adapter_state2)
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {

            hedder_theameh.setImageResource(R.drawable.dark_hedder)
        }

        list_customers.clear()
        val contacts3: List<Local_Data> = dbvoc.getAllCustomer()
        for (cn in contacts3) {
            list_customers.add(cn.get_stocks_product_name())
            list_customers_address.add(cn.getADDRESS())
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(context!!,
                R.layout.autocomplete,
                list_customers_address)
        et_location!!.setThreshold(1)
        et_location!!.setAdapter(adapter)

        if (Global_Data.PlannerUpdate.equals("Yes")) {
            val extras = intent.extras
            val i = this.intent
            if (extras != null) {

                try {

                    btn_save!!.setText(getResources().getString(R.string.Update))
                    code = extras.getString("code")!!
                    et_topic!!.setText(extras.getString("et_topic"))
                    et_date!!.setText(extras.getString("et_date"))
                    et_time!!.setText(extras.getString("et_time"))
                    et_location!!.setText(extras.getString("et_location"))

                    //spnProduct.setOnItemSelectedListener(NewOrderActivity.this);
                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(extras.getString("type_spin"))) {
                        val spinnerPosition: Int = adapter_state2!!.getPosition(extras.getString("type_spin"))
                        type_spin!!.setSelection(spinnerPosition)
                    }

                    try {
                        var array_value = JSONArray(extras.getString("arrays"))
                        //val json = JSONObject(JSONTokener(array_value))
                        //val atte = json.getJSONArray("attendees")

                        if(array_value.length() > 0)
                        {
                            for (i in 0 until array_value.length()) {
                                val jsonObject = array_value.getJSONObject(i)

                                updateStockModel!!.add(UpdateStockModel(jsonObject.getString("attendee_id"), jsonObject.getString("attendee"), "", "", "", "", ""))

                                Global_Data.Order_hashmap.put(jsonObject.getString("attendee_id"), jsonObject.getString("attendee"))
                            }
                        }
                        else
                        {
                            val random = Random()
                            val randomPIN = System.currentTimeMillis()
                            val PINString = randomPIN.toString()

                            val value: Int = 16 + random.nextInt(5)

                            updateStockModel!!.add(UpdateStockModel("YES" + PINString + value, "", "", "", "", "", ""))
                        }



                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            val random = Random()
            val randomPIN = System.currentTimeMillis()
            val PINString = randomPIN.toString()

            val value: Int = 16 + random.nextInt(5)

            updateStockModel!!.add(UpdateStockModel("YES" + PINString + value, "", "", "", "", "", ""))


        }




        mAdapter = PromotionalMeeetingAdapter(applicationContext, updateStockModel)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        m_recycler.setLayoutManager(mLayoutManager)
        m_recycler.setItemAnimator(DefaultItemAnimator())
        m_recycler.setAdapter(mAdapter)




        btn_save!!.setOnClickListener {

            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_topic!!.text.toString())) {
                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.please_task_name), "Yes")
            } else
                if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_date!!.text.toString())) {
                    Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_Date), "Yes")
                } else
                    if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_time!!.text.toString())) {
                        Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.select_time), "Yes")
                    } else
                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(et_location!!.text.toString())) {
                            Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.please_meeting_location), "Yes")
                        } else
                            if (type_spin!!.getSelectedItem().toString().equals(resources.getString(R.string.Select_Type), ignoreCase = true)) {
                                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.Select_Type), "Yes")
                            } else {

                                isInternetPresent = cd!!.isConnectingToInternet
                                if (isInternetPresent) {
                                    getServices.due_date = et_date!!.text.toString()
                                    SyncTask()
                                } else {
                                    Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")
                                }

                            }
        }

        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                var time = ""
                var hh = 0;
               // if (hourOfDay >= 0 && hourOfDay < 12) {
                    time = "$hourOfDay" + " : " + "$minute" ;
//                } else {
//                    if (hourOfDay == 12) {
//                        time = "$hourOfDay" + " : " + "$minute" + " PM";
//                    } else {
//                        hh = hourOfDay - 12;
//                        time = "$hh" + " : " + "$minute" + " PM";
//                    }
//                }
                et_time!!.setText(time)
            }
        }, hour, minute, true)

        et_time!!.setOnClickListener {

           // mTimePicker.show()
            var pickerfrom: TimePickerDialog


            val cldr = Calendar.getInstance()
            val hour = cldr[Calendar.HOUR_OF_DAY]
            val minutes = cldr[Calendar.MINUTE]
            // time picker dialog
            pickerfrom = TimePickerDialog(this@Promotional_meeting_add,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(tp: TimePicker?, sHour: Int, sMinute: Int) {
                            et_time!!.setText("$sHour:$sMinute")

                            MyLayoutOperation.dsf = "$sHour:$sMinute"
                        }
                    }, hour, minutes, true)
            pickerfrom.show()



        }

        et_date!!.setOnClickListener {

            val now = Calendar.getInstance()
            val dt =  DatePickerDialog(
                    Promotional_meeting_add@ this,
                    DatePickerDialog.OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        var mo = month
                        ++mo
                        Log.d("Orignal", mo.toString())
                        var date_from = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;
                        et_date!!.setText(date_from)

                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            )
            dt.datePicker.setMinDate(System.currentTimeMillis() - 1000)

            dt.show()
        }

//        add!!.setOnClickListener {
//            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//
//            rowView = inflater.inflate(R.layout.attendees, null);
//            parent_linear_layout!!.addView(rowView, parent_linear_layout!!.getChildCount() - 1);
//            delete_layout = rowView!!.findViewById(R.id.deletel)
//
//
//        }
//
//        delete_layout?.setOnClickListener {
//
//            parent_linear_layout!!.removeView(it.getParent() as View)
//        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.msimplelogic.activities.R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this@Promotional_meeting_add, Promotional_meetings::class.java))
                finish()
                return true
            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            com.msimplelogic.activities.R.id.add -> {
                var targetNew = ""
                val sp = this@Promotional_meeting_add.getSharedPreferences("SimpleLogic", 0)
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

                val view: View = findViewById(com.msimplelogic.activities.R.id.add)
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

    fun SyncTask() {
        System.gc()
        //detailsList.clear()
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
                val ATTE_RECJARRAY = JSONArray()
                val COLLECTION_RECJOBJECT = JSONObject()

                if (Global_Data.PlannerUpdate.equals("Yes", ignoreCase = false)) {
                    domain = service_url + "promotional_meetings/add_new_meeting"
                    COLLECTION_RECJOBJECT.put("meeting_id", code)
                } else {
                    domain = service_url + "promotional_meetings/add_new_meeting"

                }

                try {
                    val lati = Global_Data.getLocationFromAddress(context, et_tasklocation!!.text.toString())
                    Log.d("LATI", "LATI" + lati)
                    if (!lati.equals("")) {
                        val s = lati.split(",")
                        COLLECTION_RECJOBJECT.put("location_latitude", s[0])
                        COLLECTION_RECJOBJECT.put("location_longitude", s[1])
                    } else {
                        COLLECTION_RECJOBJECT.put("location_latitude", "")
                        COLLECTION_RECJOBJECT.put("location_longitude", "")
                    }
                } catch (e: Exception) {
                    // Log.e("DATA", e.message)
                    COLLECTION_RECJOBJECT.put("location_latitude", "")
                    COLLECTION_RECJOBJECT.put("location_longitude", "")
                }


                var id = "";
                if (!type_spin!!.getSelectedItem().toString().equals(resources.getString(R.string.Select_Type), ignoreCase = true)) {
                    id = ListresultsHashMap.get(type_spin!!.getSelectedItem().toString())!!

                    // id =  type_spin!!.getSelectedItem().toString()
                }

                if (!Global_Data.Order_hashmap.isEmpty()) {
                    try {
                        for (name in Global_Data.Order_hashmap.keys) {
                            val item = JSONObject()
                            val key = name.toString()
                            val key_array = key.toString().split("&".toRegex()).toTypedArray()
                            val value: Any? = Global_Data.Order_hashmap[key]
                            //detailsList.add(value.toString());


                            val attende = JSONObject()
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(value.toString()) && key.contains("YES")) {
                                attende.put("attendee", value.toString());

//                                if (key.contains("YES")) {
//
//                                    // attende.put("attendee_id", key);
//                                } else {
//                                    attende.put("attendee", value.toString());
//                                    attende.put("attendee_id", key);
//                                }

                                ATTE_RECJARRAY.put(attende);
                            }
                            else
                            {
                                if(!key.toString().contains("YES"))
                                {
                                    attende.put("attendee", value.toString());
                                    attende.put("attendee_id", key);
                                    ATTE_RECJARRAY.put(attende);
                                }

                            }

                        }

                        bs_check = ss.toString();
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                } else {
                    bs_check = "";
                }

                COLLECTION_RECJOBJECT.put("latitude", Global_Data.GLOvel_LATITUDE)
                COLLECTION_RECJOBJECT.put("longitude", Global_Data.GLOvel_LONGITUDE)
                COLLECTION_RECJOBJECT.put("name", et_topic!!.text)
                COLLECTION_RECJOBJECT.put("meeting_date", et_date!!.text)
                COLLECTION_RECJOBJECT.put("meeting_time", et_time!!.text)


                // COLLECTION_RECJOBJECT.put("attendees", bs_check)
                COLLECTION_RECJOBJECT.put("location", et_location!!.text)
                COLLECTION_RECJOBJECT.put("meeting_type_id", id)
                COLLECTION_RECJOBJECT.put("device_fcm_key", fcm_token)
                COLLECTION_RECJOBJECT.put("attendees", ATTE_RECJARRAY)


                // COLLECTION_RECJOBJECT.put("reminder_type", et_tasktype!!.selectedItem.toString().toLowerCase())


                // COLLECTION_RECJARRAY.put(Schedule_JOB)

                // COLLECTION_RECJOBJECT.put("visit_schedules", COLLECTION_RECJARRAY)
                // COLLECTION_RECJOBJECT.put("customer_code", shopcode)
                COLLECTION_RECJOBJECT.put("email", user_email)
                COLLECTION_RECJOBJECT.put("device_fcm_key", fcm_token)




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

                        if (response.has("meeting_id")) {
                            getServices.id = response.getString("meeting_id")
                        }
                        if (response_result.equals("PromotionalMeeting Saved Successfully", ignoreCase = true) || response_result.equals("PromotionalMeeting Saved Successfully", ignoreCase = true)) {
                            dialog!!.dismiss()
                            // Global_Data.Custom_Toast(context, response_result, "Yes")
                            if (!Global_Data.PlannerUpdate.equals("Yes")) {
                                Global_Data.Custom_Toast(ActivityTaskDetails@ this, "PromotionalMeeting Saved Successfully", "Yes")
                            } else {
                                Global_Data.Custom_Toast(ActivityTaskDetails@ this, "PromotionalMeeting Updated Successfully", "Yes")
                            }

                            if (type_spin!!.getSelectedItem().toString().equals(resources.getString(R.string.Executive), ignoreCase = true)) {
                                val i = Intent(context, Promotional_save::class.java)
                                startActivity(i)
                                finish()
                            } else {
                                val i = Intent(context, MeetingSaveNew::class.java)
                                startActivity(i)
                                finish()
                            }


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

        val i = Intent(context, Promotional_meetings::class.java)
        startActivity(i)
        finish()
    }

}
