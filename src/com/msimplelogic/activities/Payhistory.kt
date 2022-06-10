package com.msimplelogic.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.adapter.PayhistoryAdaptor
import com.msimplelogic.adapter.Product_demo_adaptor
import com.msimplelogic.model.PayhistoryModel
import com.msimplelogic.model.Productdemo_model
import cpm.simplelogic.helper.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip

import kotlinx.android.synthetic.main.activity_payhistory.*
import kotlinx.android.synthetic.main.activity_payhistory.toolbar
import kotlinx.android.synthetic.main.activity_productdemo.*
import kotlinx.android.synthetic.main.content_neworderoptions.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

class Payhistory :  BaseActivity() {
    var list: ArrayList<PayhistoryModel>? = null
    var rv: RecyclerView? = null
    var adaptor: PayhistoryAdaptor? = null
     var cd: ConnectionDetector?=null
    var isInternetPresent = false
    private var pDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payhistory)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        cd = ConnectionDetector(applicationContext)
        list = ArrayList<PayhistoryModel>()
        rv=findViewById(R.id.rv)
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
        }

        isInternetPresent = cd!!.isConnectingToInternet
        if (isInternetPresent) {
            pDialog = ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
            History()
        } else {
//            val toast = Toast.makeText(this@Payhistory, "You don't have internet connection.", Toast.LENGTH_LONG)
//            toast.setGravity(Gravity.CENTER, 0, 0)
//            toast.show()
            Global_Data.Custom_Toast(this@Payhistory, "You don't have internet connection.","yes")

            val order_home = Intent(applicationContext, OutstandingOverdueActivity::class.java)
            startActivity(order_home)
            finish()
        }



    }

    private fun History() {
        pDialog!!.setMessage(resources.getString(R.string.Please_Wait))
        pDialog!!.setTitle(resources.getString(R.string.app_name))
        pDialog!!.setCancelable(false)
        pDialog!!.show()
        var domain = ""

        val spf: SharedPreferences = this@Payhistory.getSharedPreferences("SimpleLogic", 0)
        val user_email = spf.getString("USER_EMAIL", null)


        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val Cust_domain = sp.getString("Cust_Service_Url", "")
        val service_url = Cust_domain + "metal/api/v1/"

        domain = service_url + "customers/order_outstanding_amount_history?customer_code="+Global_Data.GLOvel_CUSTOMER_ID +"&email="+ user_email

        var stringRequest: StringRequest? = null
        stringRequest = StringRequest(domain,
                Response.Listener { response ->
                    //showJSON(response);
// Log.d("jV", "JV" + response);
                    Log.d("jV", "JV length" + response.length)
                    // JSONObject person = (JSONObject) (response);
                    try {
                        val json = JSONObject(JSONTokener(response))
                        try {
                            var response_result = ""
                            response_result = if (json.has("result")) {
                                json.getString("result")
                            } else {
                                "data"
                            }
                            if (response_result.equals("Order History Not Found", ignoreCase = true)) {
                                pDialog!!.hide()
                           //     val toast = Toast.makeText(applicationContext, resources.getString(R.string.Sorry_No_Record_Found), Toast.LENGTH_LONG)
//                                toast.setGravity(Gravity.CENTER, 0, 0)
//                                toast.show()
                                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Sorry_No_Record_Found), "yes")
                                finish()
                            } else if (response_result.equals("Device not registered", ignoreCase = true)) {
                                pDialog!!.hide()
                                // Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG).show();
//                                val toast = Toast.makeText(applicationContext, response_result, Toast.LENGTH_LONG)
//                                toast.setGravity(Gravity.CENTER, 0, 0)
//                                toast.show()
                                Global_Data.Custom_Toast(applicationContext, response_result,"yes")
                                finish()
                            } else {
                                val launches = json.getJSONArray("order_history")
                                //
                                Log.i("volley", "response reg launches Length: " + launches.length())
                                //
                                Log.d("users", "launches$launches")
                                //
//
                                // images!!.clear()
                                for (i in 0 until launches.length()) {
                                    val `object` = launches.getJSONObject(i)
//
                                    list!!.add(PayhistoryModel(`object`.getString("paid_amount"), `object`.getString("order_date"), `object`.getString("order_no")))

                                  //  list_product_name!!.add(`object`.getString("name"))
                                }
                                pDialog!!.hide()
                                adaptor = PayhistoryAdaptor(this, list as java.util.ArrayList<PayhistoryModel>)

                                val mLayoutManager = LinearLayoutManager(this)
                                rv!!.layoutManager = mLayoutManager
                                //viewtarget_list.addItemDecoration(DividerItemDecoration(this@ActivityViewTarget, LinearLayoutManager.VERTICAL))
                               // rv!!.itemAnimator = DefaultItemAnimator()
                                rv!!.adapter = adaptor
                            }
                            //  finish();
// }
// output.setText(data);
                        } catch (e: JSONException) {
                            e.printStackTrace()
//                            val toast = Toast.makeText(this@Payhistory,
//                                    "Service Error",
//                                    Toast.LENGTH_LONG)
//                            toast.setGravity(Gravity.CENTER, 0, 0)
//                            toast.show()
                            Global_Data.Custom_Toast(this@Payhistory,
                                    "Service Error","yes")
                            val launch = Intent(this@Payhistory, OutstandingOverdueActivity::class.java)
                            startActivity(launch)
                            finish()
                            pDialog!!.hide()
                        }
                        pDialog!!.hide()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        //  finish();
                        pDialog!!.dismiss()
                    }
                    pDialog!!.dismiss()
                },
                Response.ErrorListener { error ->
                    //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    if (error is TimeoutError || error is NoConnectionError) { //                            Toast.makeText(Image_Gellary.this,
//                                    "Network Error",
//                                    Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(this@Payhistory,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@Payhistory,
                                "Network Error","")
                    } else if (error is AuthFailureError) {
//                        val toast = Toast.makeText(this@Payhistory,
//                                "Server AuthFailureError  Error",
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@Payhistory,
                                "Server AuthFailureError  Error","")
                    } else if (error is ServerError) {
//                        val toast = Toast.makeText(this@Payhistory,
//                                resources.getString(R.string.Server_Errors),
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@Payhistory,
                                resources.getString(R.string.Server_Errors),"")
                    } else if (error is NetworkError) {
//                        val toast = Toast.makeText(this@Payhistory,
//                                "Network   Error",
//                                Toast.LENGTH_LONG)
//                        toast.show()

                        Global_Data.Custom_Toast(this@Payhistory,
                                "Network Error","")
                    } else if (error is ParseError) {
//                        val toast = Toast.makeText(this@Payhistory,
//                                "ParseError   Error",
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@Payhistory,
                                "ParseError   Error","")
                    } else { // Toast.makeText(Image_Gellary.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        val toast = Toast.makeText(this@Payhistory, error.message, Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                    }
                    val launch = Intent(this@Payhistory, OutstandingOverdueActivity::class.java)
                    startActivity(launch)
                    finish()
                    pDialog!!.dismiss()
                    // finish();
                })
        val requestQueue = Volley.newRequestQueue(this@Payhistory)
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
                val sp = this@Payhistory.getSharedPreferences("SimpleLogic", 0)
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
}
