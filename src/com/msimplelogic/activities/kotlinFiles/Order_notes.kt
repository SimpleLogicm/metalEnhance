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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import com.msimplelogic.activities.R
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import com.msimplelogic.adapter.Order_notes_adaptor
import com.msimplelogic.model.Order_notes_model
import com.msimplelogic.webservice.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_order_notes.*
import kotlinx.android.synthetic.main.content_visit__schedule.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Order_notes : BaseActivity() {
    var rv_nots: RecyclerView? = null
    var tv_view_all: TextView? = null
    var et_customer_name: EditText? = null
    var et_title: EditText? = null
    var et_discription: EditText? = null
    var btn_save: Button? = null
    var card: CardView? = null
    var list: ArrayList<Order_notes_model>? = null
    var adaptor: Order_notes_adaptor? = null
    var dialog: ProgressDialog? = null
    var context: Context? = null
    var total_count:TextView?=null
    var dbvoc = DataBaseHelper(this)
    var loginDataBaseAdapter: LoginDataBaseAdapter? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var user_email : String? = null
var sp:SharedPreferences?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_notes)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
     //   setTitle(resources.getString(R.string.notes))
        Kot_Gloval.listfullnotes = ArrayList<Order_notes_model>()



        tv_view_all=findViewById(R.id.tv_view_all)
        total_count=findViewById(R.id.total_count)

        et_customer_name=findViewById(R.id.et_customer_name)
        et_title=findViewById(R.id.et_title)
        et_discription=findViewById(R.id.et_discription)
        btn_save=findViewById(R.id.btn_save)
        card=findViewById(R.id.card)
        rv_nots=findViewById(R.id.rv_nots)

        rv_nots?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        val sps = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val shopname: String = sps.getString("shopname", null)!!
        et_customer_name!!.setText(shopname)

        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
//            val img: Drawable =   context!!.resources.getDrawable(R.drawable.ic_baseline_date_range_24_dark)
//
//            et_date!!.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        context = Visit_Schedule@ this
        dialog = ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        loginDataBaseAdapter = LoginDataBaseAdapter(context)
        loginDataBaseAdapter = loginDataBaseAdapter!!.open()
        cd = ConnectionDetector(context)
        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        user_email = sp.getString("USER_EMAIL", null)

        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {

            GetNotesData()
        }
        else
        {
            val cont1 : List<Order_notes_model> = dbvoc.getnotes_ByUseremails(user_email);
            if (cont1.size > 0) {
                for (cnt1 in cont1) {

                    et_customer_name!!.setText(cnt1.name)
                    et_title!!.setText(cnt1.name)
                    et_discription!!.setText(cnt1.name)
                }
            }

            Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")

        }




        btn_save!!.setOnClickListener {
            validation()
        }

        tv_view_all!!.setOnClickListener {

            if(Kot_Gloval.listfullnotes!!.size > 3)
            {
                Kot_Gloval.activity_flag = "Notes"
                var intent = Intent(context,ListDetailsView::class.java)
                startActivity(intent)
            }

        }
    }

    private fun validation() {
        if (et_customer_name!!.getText().length == 0) run {

            Global_Data.Custom_Toast(this@Order_notes, resources.getString(R.string.Enter_customer_name), "yes")

        }else
        if (et_title!!.getText().length == 0) run {

        Global_Data.Custom_Toast(this@Order_notes, resources.getString(R.string.Enter_Title), "yes")

        }else
        if (et_discription!!.getText().length == 0) run {

            Global_Data.Custom_Toast(this@Order_notes, resources.getString(R.string.Enter_Description), "yes")

        }
        else
        {
            val c = Calendar.getInstance()
            val df = SimpleDateFormat("dd-MM-yyyy- HH:mm:ss")
            val formattedDate = df.format(c.time)
            val randomPIN = System.currentTimeMillis()
            val PINString = randomPIN.toString()

            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {

                dialog!!.setMessage(resources.getString(R.string.Please_Wait))
                dialog!!.setTitle(resources.getString(R.string.app_name))
                dialog!!.setCancelable(false)
                dialog!!.show()



                loginDataBaseAdapter!!.insert_notes(PINString,user_email, et_customer_name!!.text.toString(), et_title!!.text.toString(), et_discription!!.text.toString(),formattedDate);
                SyncNotes(PINString)
            }
            else
            {
                loginDataBaseAdapter!!.insert_notes(PINString,user_email, et_customer_name!!.text.toString(), et_title!!.text.toString(), et_discription!!.text.toString(),formattedDate);

                Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")
                Global_Data.Custom_Toast(context, resources.getString(R.string.Notes_Save), "Yes")
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
                onBackPressed()
            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp = this@Order_notes.getSharedPreferences("SimpleLogic", 0)
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


    fun GetNotesData() {

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
            service_url = service_url + "notes/get_notes_details?email=" + URLEncoder.encode(user_email, "UTF-8")+"&type=customer"+"&customer_code="+shop_code;
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
                                val visits = json.getJSONArray("notes")

                                Log.i("volley", "response visits Length: " + visits.length())

                                Log.d("users", "customer_stocks$visits")

                                list = ArrayList<Order_notes_model>()
                                Kot_Gloval.listfullnotes = ArrayList<Order_notes_model>()

                                for (i in 0 until visits.length()) {
                                    val jsonObject = visits.getJSONObject(i)

                                    if(i < 3)
                                    {
                                        list!!.add(Order_notes_model("",jsonObject.getString("customer_name"), jsonObject.getString("name"), jsonObject.getString("description"), jsonObject.getString("created_date")))
                                    }

                                    if(i > 2){
                                        tv_view_all!!.visibility=View.VISIBLE

                                    }else{
                                        tv_view_all!!.visibility=View.GONE

                                    }

                                    Kot_Gloval.listfullnotes!!.add(Order_notes_model("",jsonObject.getString("customer_name"), jsonObject.getString("name"), jsonObject.getString("description"),jsonObject.getString("created_date")))

                                }
                                var sixe =visits.length().toString()
                                total_count!!.setText("("+sixe+")")
                                adaptor = Order_notes_adaptor(this, list!!)
                                rv_nots?.adapter = adaptor

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
                                error.message,"yes")
                    }

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

    fun SyncNotes(code:String) {
        System.gc()
        val reason_code = ""
        try {
            var domain = ""
            var device_id = ""
            val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
            val Cust_domain = sp.getString("Cust_Service_Url", "")
            val service_url = Cust_domain + "metal/api/v1/"
            val spf: SharedPreferences = context!!.getSharedPreferences("SimpleLogic", 0)
            val user_email = spf.getString("USER_EMAIL", null)
            val shopcode = spf.getString("shopcode", null)
            val fcm_token = spf.getString("fcm_token", null)

            domain = service_url
            var jsObjRequest: JsonObjectRequest? = null
            try {
                Log.d("Server url", "Server url" + domain + "notes/create_notes ")
                val COLLECTION_RECJARRAY = JSONArray()
                val COLLECTION_RECJOBJECT = JSONObject()
                val notes_JOB = JSONObject()

                val cont1 : List<Order_notes_model> = dbvoc.getnotes_ByUseremail(user_email,code)
                if (cont1.size > 0) {
                    for (cnt1 in cont1) {

                        val notes_sT = JSONObject()

                        notes_sT.put("customer_name", cnt1.name)
                        notes_sT.put("name", cnt1.notestitle)
                        notes_sT.put("description", cnt1.datenotes)
                        notes_sT.put("created_date", cnt1.created_date)
                        notes_sT.put("latitude",  Global_Data.GLOvel_LATITUDE)
                        notes_sT.put("longitude",  Global_Data.GLOvel_LONGITUDE)
                        notes_sT.put("customer_code",shopcode)
                        notes_sT.put("type ","customer")

                        COLLECTION_RECJARRAY.put(notes_sT)

                    }
                }

                COLLECTION_RECJOBJECT.put("notes", COLLECTION_RECJARRAY)
                COLLECTION_RECJOBJECT.put("email", user_email)


                Log.d("Notes Service", COLLECTION_RECJOBJECT.toString())
                jsObjRequest = JsonObjectRequest(Request.Method.POST, domain + "notes/create_notes", COLLECTION_RECJOBJECT, Response.Listener { response ->
                    Log.i("volley", "response: $response")
                    Log.d("jV", "JV length" + response.length())
                    try {
                        var response_result = ""
                        response_result = if (response.has("result")) {
                            response.getString("result")
                        } else {
                            "data"
                        }
                        if (response_result.equals("Note Saved Successfully", ignoreCase = true)) {
                            dialog!!.dismiss()
                            Global_Data.Custom_Toast(context, response_result, "Yes")
                            val i = Intent(context, Sales_Dash::class.java)
                            startActivity(i)
                            finish()
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

        // val i = Intent(UpdateStockScreen2@ this, UpdateStockScreen1::class.java)
        // startActivity(i)
        finish()
    }

}
