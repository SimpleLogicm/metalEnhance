package com.msimplelogic.activities.kotlinFiles

import android.app.AlertDialog
import android.app.ProgressDialog
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
import kotlinx.android.synthetic.main.activity_quoteaddretailertrait.*
import kotlinx.android.synthetic.main.content_quoteaddretailer.*
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
import com.msimplelogic.adapter.AdapterQuoteAddRetailerTrait
import com.msimplelogic.model.CustomerTraitModel
import com.msimplelogic.webservice.ConnectionDetector
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class QuoteAddRetailerTrait : BaseActivity() {
    var listOrderCustTrait: ArrayList<CustomerTraitModel>? = null
    var adapterOrderCustTrait: AdapterQuoteAddRetailerTrait? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var user_email : String? = null
    var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quoteaddretailertrait)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        quoteaddret_rv?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        //context = Visit_Schedule@ this
        dialog = ProgressDialog(Customer_Traits@this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)

        cd = ConnectionDetector(Customer_Traits@this)
        listOrderCustTrait = ArrayList<CustomerTraitModel>()

//            listOrderCustTrait?.add(CustomerTraitModel("No98674r5", "2020/03/02","",""))
//            listOrderCustTrait?.add(CustomerTraitModel("No98674r5", "2020/03/02","",""))
        adapterOrderCustTrait = AdapterQuoteAddRetailerTrait(this, listOrderCustTrait!!)
        quoteaddret_rv?.adapter = adapterOrderCustTrait
        dialog?.dismiss()

        val type=intent.getStringExtra("TYPE")


        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {

            type?.let { OrderCustomerTrait(it) }
        }
        else
        {
            Global_Data.Custom_Toast(CustomerServicesTraits@this, resources.getString(R.string.internet_connection_error), "Yes")
        }


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
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
                super.onBackPressed()            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            com.msimplelogic.activities.R.id.add -> {
                var targetNew = ""
                val sp = this@QuoteAddRetailerTrait.getSharedPreferences("SimpleLogic", 0)
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
            service_url = service_url + "customer_traits/customer_traits_order?email=" + (URLEncoder.encode(user_email, "UTF-8"))+"&customer_code="+shop_code+"&type="+type
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
                               finish()
                            } else {

                                val visits = json.getJSONArray("data")
                                Log.i("volley", "response visits Length: " + visits.length())
                                Log.d("users", "customer_stocks$visits")

                                if(visits.length()>0) {
                                    if (type.equals("quotation")) {

                                        listOrderCustTrait = ArrayList<CustomerTraitModel>()
                                        for (i in 0 until visits.length()) {
                                            val jsonObject = visits.getJSONObject(i)
                                            listOrderCustTrait!!.add(CustomerTraitModel("Quote No : " + jsonObject.getString("quote_number"), jsonObject.getString("create_date"), "Status : " + jsonObject.getString("status"),""))
                                        }
                                        adapterOrderCustTrait = AdapterQuoteAddRetailerTrait(this, listOrderCustTrait!!)
                                        quoteaddret_rv?.adapter = adapterOrderCustTrait
                                        dialog!!.dismiss()

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
