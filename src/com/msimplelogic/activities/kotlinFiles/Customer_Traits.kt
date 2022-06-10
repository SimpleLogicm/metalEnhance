package com.msimplelogic.activities.kotlinFiles

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_customer__traits.*
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.LoginDataBaseAdapter
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import com.msimplelogic.adapter.AdapterCustomerTrait
import com.msimplelogic.adapter.Order_notes_adaptor
import com.msimplelogic.adapter.TaskSwipeAdapter
import com.msimplelogic.model.CustomerTraitModel
import com.msimplelogic.model.Order_notes_model
import com.msimplelogic.webservice.ConnectionDetector
import kotlinx.android.synthetic.main.cell_content_layout.*
import kotlinx.android.synthetic.main.content_customer__traits.*
import kotlinx.android.synthetic.main.content_visit__schedule.*
import kotlinx.android.synthetic.main.content_visit__schedule.hedder_theame
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class Customer_Traits : BaseActivity() {
    var listOrderCustTrait: ArrayList<CustomerTraitModel>? = null
    var adapterOrderCustTrait: AdapterCustomerTrait? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var user_email: String? = null
    var dialog: ProgressDialog? = null
    var order_card: CardView? = null
    var Return_order_card: CardView? = null
    var no_order_card: CardView? = null
    var feedback_card: CardView? = null
    var complaints_card: CardView? = null
    var claims_card: CardView? = null
    var claim_rv: RecyclerView? = null
    var CompetitorAnalysis_card: CardView? = null
    var picture_vid_card: CardView? = null
    var ORDER_btn: ImageView? = null
    var Return_order_img: ImageView? = null
    var feedback_img: ImageView? = null
    var Complaints_img: ImageView? = null
    var Claims_img: ImageView? = null
    var CompetitorAnalysis_img: ImageView? = null
    var pic_vid_img: ImageView? = null
    var order_rv: RecyclerView? = null
    var return_order_rv: RecyclerView? = null
    var no_order_rv: RecyclerView? = null
    var feedback_rv: RecyclerView? = null
    var complaints_rv: RecyclerView? = null
    var compatitor_analysis_rv: RecyclerView? = null
    var picture_vid_rv: RecyclerView? = null
    var ordercard = "0"
    var noordercard = "0"
    var returnordercard = "0"
    var sp:SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.msimplelogic.activities.R.layout.activity_customer__traits)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        order_card = findViewById(R.id.order_card)
        ORDER_btn = findViewById(R.id.ORDER_btn_img)
        order_rv = findViewById(R.id.order_rv)
        Return_order_card = findViewById(R.id.Return_order_card)
        Return_order_img = findViewById(R.id.Return_order_img)
        return_order_rv = findViewById(R.id.return_order_rv)
        no_order_card = findViewById(R.id.no_order_card)
        //No_order_img=findViewById(R.id.no_order_card)
        no_order_rv = findViewById(R.id.no_order_rv)
        feedback_card = findViewById(R.id.feedback_card)
        feedback_img = findViewById(R.id.feedback_img)
        feedback_rv = findViewById(R.id.feedback_rv)
        complaints_card = findViewById(R.id.complaints_card)
        Complaints_img = findViewById(R.id.Complaints_btn)
        complaints_rv = findViewById(R.id.complaints_rv)
        claims_card = findViewById(R.id.claims_card)
        Claims_img = findViewById(R.id.Claims_img)
        claim_rv = findViewById(R.id.claim_rv)
        //claim_rv=findViewById(R.id.claim_rv)
        CompetitorAnalysis_card = findViewById(R.id.CompetitorAnalysis_card)
        CompetitorAnalysis_img = findViewById(R.id.CompetitorAnalysis_img)
        compatitor_analysis_rv = findViewById(R.id.compatitor_analysis_rv)
        picture_vid_card = findViewById(R.id.picture_vid_card)
        pic_vid_img = findViewById(R.id.pic_vid_img)
        picture_vid_rv = findViewById(R.id.picture_vid_rv)
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
            ORDER_btn_img.setImageResource(R.drawable.viewordermenu_dark)
            Return_order_img!!.setImageResource(R.drawable.viewordermenu_dark)
            No_order_img.setImageResource(R.drawable.viewordermenu_dark)
//            val img: Drawable =   context!!.resources.getDrawable(R.drawable.ic_baseline_date_range_24_dark)
//
//            et_date!!.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }
//        val recyclerView = findViewById<View>(com.msimplelogic.activities.R.id.rv_order) as RecyclerView
//        val layoutManager = LinearLayoutManager(this)
        // RecyclerView has some built in animations to it, using the DefaultItemAnimator.
        // Specifically when you call notifyItemChanged() it does a fade animation for the changing
        // of the data in the ViewHolder. If you would like to disable this you can use the following:

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        order_card?.setOnClickListener { view ->
            order_rv?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            //context = Visit_Schedule@ this
            dialog = ProgressDialog(Customer_Traits@ this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)

            cd = ConnectionDetector(Customer_Traits@ this)
            listOrderCustTrait = ArrayList<CustomerTraitModel>()
            //order_rv?.visibility = View.VISIBLE
//            listOrderCustTrait?.add(CustomerTraitModel("No98674r5", "2020/03/02", "45", "45365"))
//            listOrderCustTrait?.add(CustomerTraitModel("No98674r5", "2020/03/02", "45", "45365"))
            adapterOrderCustTrait = AdapterCustomerTrait(this, listOrderCustTrait!!)
            order_rv?.adapter = adapterOrderCustTrait
            dialog?.dismiss()

            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {

                if (ordercard == "0") {
                    ordercard = "1"
                    OrderCustomerTrait("orders")

                } else {
                    ordercard = "0"
                    order_rv!!.visibility = View.GONE

                }

                //   OrderCustomerTrait("orders")
            } else {
                Global_Data.Custom_Toast(Customer_Traits@ this, resources.getString(R.string.internet_connection_error), "Yes")
            }
        }

        Return_order_card?.setOnClickListener { view ->
            return_order_rv?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            dialog = ProgressDialog(Customer_Traits@ this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
            cd = ConnectionDetector(Customer_Traits@ this)
            listOrderCustTrait = ArrayList<CustomerTraitModel>()
            adapterOrderCustTrait = AdapterCustomerTrait(this, listOrderCustTrait!!)
            return_order_rv?.adapter = adapterOrderCustTrait
            dialog?.dismiss()

//            listOrderCustTrait?.add(CustomerTraitModel("No98674r5", "2020/03/02", "45", "45365"))
//            listOrderCustTrait?.add(CustomerTraitModel("No98674r5", "2020/03/02", "45", "45365"))
//            adapterOrderCustTrait = AdapterCustomerTrait(this, listOrderCustTrait!!)
//            return_order_rv?.adapter = adapterOrderCustTrait
//            dialog?.dismiss()

            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                if (returnordercard == "0") {
                    returnordercard = "1"
                    OrderCustomerTrait("return_orders")

                } else {
                    returnordercard = "0"
                    return_order_rv!!.visibility = View.GONE

                }

            } else {
                Global_Data.Custom_Toast(Customer_Traits@ this, resources.getString(R.string.internet_connection_error), "Yes")
            }
        }

        no_order_card?.setOnClickListener { view ->
            no_order_rv?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
            dialog = ProgressDialog(Customer_Traits@ this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
            cd = ConnectionDetector(Customer_Traits@ this)
            listOrderCustTrait = ArrayList<CustomerTraitModel>()

//            listOrderCustTrait?.add(CustomerTraitModel("No98674r5", "2020/03/02", "45", "45365"))
//            listOrderCustTrait?.add(CustomerTraitModel("No98674r5", "2020/03/02", "45", "45365"))
            adapterOrderCustTrait = AdapterCustomerTrait(this, listOrderCustTrait!!)
            no_order_rv?.adapter = adapterOrderCustTrait
            dialog?.dismiss()

            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                if (noordercard == "0") {
                    noordercard="1"
                    OrderCustomerTrait("no_orders")

                } else {
                    noordercard="0"
                    no_order_rv!!.visibility = View.GONE
                }
            } else {
                Global_Data.Custom_Toast(Customer_Traits@ this, resources.getString(R.string.internet_connection_error), "Yes")
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.msimplelogic.activities.R.menu.main, menu)
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
            com.msimplelogic.activities.R.id.add -> {
                var targetNew = ""
                val sp = this@Customer_Traits.getSharedPreferences("SimpleLogic", 0)
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

    fun OrderCustomerTrait(type: String) {
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
            service_url = service_url + "customer_traits/customer_traits_order?email=" + (URLEncoder.encode(user_email, "UTF-8")) + "&customer_code=" + shop_code + "&type=" + type
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
                                Global_Data.Custom_Toast(this, resources.getString(R.string.Sorry_No_Record_Found), "Yes")
                                //finish().toString()
                                if (type.equals("orders")) {
                                    order_rv?.visibility = View.GONE
                                } else if (type.equals("return_orders")) {
                                    return_order_rv?.visibility = View.GONE
                                } else {
                                    no_order_rv?.visibility = View.GONE
                                }

                            } else {

                                val visits = json.getJSONArray("data")
                                Log.i("volley", "response visits Length: " + visits.length())
                                Log.d("users", "customer_stocks$visits")

                                if (visits.length() > 0) {
                                    if (type.equals("orders")) {
                                        no_order_rv?.visibility = View.GONE
                                        return_order_rv?.visibility = View.GONE
                                        order_rv?.visibility = View.VISIBLE

                                        listOrderCustTrait = ArrayList<CustomerTraitModel>()
                                        for (i in 0 until visits.length()) {
                                            val jsonObject = visits.getJSONObject(i)
                                            listOrderCustTrait!!.add(CustomerTraitModel("Order No : " + jsonObject.getString("order_number"), "Date : " + jsonObject.getString("order_date"), "Quantity : " + jsonObject.getString("quantity"), "Price : " + jsonObject.getString("order_amount")))
                                        }
                                        adapterOrderCustTrait = AdapterCustomerTrait(this, listOrderCustTrait!!)
                                        order_rv?.adapter = adapterOrderCustTrait
                                        dialog!!.dismiss()

                                    } else if (type.equals("return_orders")) {
                                        no_order_rv?.visibility = View.GONE
                                        return_order_rv?.visibility = View.VISIBLE
                                        order_rv?.visibility = View.GONE

//                                    val visits = json.getJSONArray("data")
//                                    Log.i("volley", "response return_orders Length: " + visits.length())
//                                    Log.d("users", "customer_stocks$visits")
                                        listOrderCustTrait = ArrayList<CustomerTraitModel>()
                                        for (i in 0 until visits.length()) {
                                            val jsonObject = visits.getJSONObject(i)
                                            listOrderCustTrait!!.add(CustomerTraitModel("Batch No : " + jsonObject.getString("batch_no"), "Date : " + jsonObject.getString("date"), "Quantity : " + jsonObject.getString("quantity"), "Refund Amount : " + jsonObject.getString("refund_amount")))
                                        }
                                        adapterOrderCustTrait = AdapterCustomerTrait(this, listOrderCustTrait!!)
                                        return_order_rv?.adapter = adapterOrderCustTrait
                                        dialog!!.dismiss()
                                    } else if (type.equals("no_orders")) {
                                        no_order_rv?.visibility = View.VISIBLE
                                        return_order_rv?.visibility = View.GONE
                                        order_rv?.visibility = View.GONE

//                                    val visits = json.getJSONArray("data")
//                                    Log.i("volley", "response return_orders Length: " + visits.length())
//                                    Log.d("users", "customer_stocks$visits")
                                        listOrderCustTrait = ArrayList<CustomerTraitModel>()
                                        for (i in 0 until visits.length()) {
                                            val jsonObject = visits.getJSONObject(i)
                                            listOrderCustTrait!!.add(CustomerTraitModel("Order No : " + jsonObject.getString("order_number"), "Date : " + jsonObject.getString("date"), "Reason : " + jsonObject.getString("reason_desc"), ""))
                                        }
                                        adapterOrderCustTrait = AdapterCustomerTrait(this, listOrderCustTrait!!)
                                        no_order_rv?.adapter = adapterOrderCustTrait
                                        dialog!!.dismiss()
                                    }

                                } else {
                                    dialog!!.hide()
                                    Global_Data.Custom_Toast(this, resources.getString(R.string.Sorry_No_Record_Found), "Yes")
                                    //finish().toString()
                                    if (type.equals("orders")) {
                                        order_rv?.visibility = View.GONE
                                    } else if (type.equals("return_orders")) {
                                        return_order_rv?.visibility = View.GONE
                                    } else {
                                        no_order_rv?.visibility = View.GONE
                                    }
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
//                        val toast = Toast.makeText(Customer_Traits@ this,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(Customer_Traits@ this,
                                "Network Error","yes")
                    } else if (error is AuthFailureError) { //							Toast.makeText(Video_Main_List.this,
//									"Server AuthFailureError  Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(Customer_Traits@ this,
//                                "Server AuthFailureError  Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(Customer_Traits@ this,
                                "Server AuthFailureError  Error","yes")
                    } else if (error is ServerError) { //							Toast.makeText(Video_Main_List.this,
//									"Server   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(Customer_Traits@ this,
//                                resources.getString(R.string.Server_Errors),
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(Customer_Traits@ this,
                                resources.getString(R.string.Server_Errors),"yes")
                    } else if (error is NetworkError) { //							Toast.makeText(Video_Main_List.this,
//									"Network   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(Customer_Traits@ this,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(Customer_Traits@ this,
                                "Network Error","yes")
                    } else if (error is ParseError) { //							Toast.makeText(Video_Main_List.this,
//									"ParseError   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(Customer_Traits@ this,
//                                "ParseError   Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(Customer_Traits@ this,
                                "ParseError   Error","yes")
                    } else { //Toast.makeText(Video_Main_List.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(Customer_Traits@ this,
//                                error.message,
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(Customer_Traits@ this,
                                error.message,"yes")
                    }
                    //						Intent launch = new Intent(Video_Main_List.this,MainActivity.class);
//						startActivity(launch);
                    finish()
                    dialog!!.dismiss()
                    // finish();
                })
        val requestQueue = Volley.newRequestQueue(Customer_Traits@ this)
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
