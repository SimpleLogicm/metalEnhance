package com.msimplelogic.activities.kotlinFiles

import  com.msimplelogic.activities.R
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import com.msimplelogic.adapter.UpdateStockAdapter
import com.msimplelogic.model.UpdateStockModel
import com.msimplelogic.services.getServices
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_update_stock_screen2.*
import kotlinx.android.synthetic.main.content_beat_selection.*
import kotlinx.android.synthetic.main.content_update_stock_screen1.*
import kotlinx.android.synthetic.main.content_update_stock_screen2.*
import kotlinx.android.synthetic.main.content_update_stock_screen2.hedder_theame
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.DecimalFormat
import java.util.*

class UpdateStockScreen2 : BaseActivity() {

    private var mAdapter: UpdateStockAdapter? = null
    var updateStockModel: MutableList<UpdateStockModel> = ArrayList()
    var dialog: ProgressDialog? = null
    var context: Context? = null
    var sp:SharedPreferences?=null
    var sortIcon:ImageView?=null
    var fitericon:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_update_stock_screen2)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")

        context = UpdateStockScreen2@ this
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)

        fitericon=findViewById(R.id.fitericon)
        sortIcon=findViewById(R.id.sortIcon)
        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
            sortIcon?.setImageResource(R.drawable.sortby_ordertype_dark)
            fitericon?.setImageResource(R.drawable.filterordertype_icon_dark)
            nextimg.setImageResource(R.drawable.viewordermenu_dark)
        }
        dialog = ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        // initData()
        GetNewLaunch_Datan()



        mAdapter = UpdateStockAdapter(applicationContext, updateStockModel)

        val mLayoutManager: LayoutManager = LinearLayoutManager(applicationContext)
        updates_recycler.addItemDecoration(DividerItemDecoration(updates_recycler.context, DividerItemDecoration.VERTICAL));
        updates_recycler.setLayoutManager(mLayoutManager)
        updates_recycler.setItemAnimator(DefaultItemAnimator())
        updates_recycler.setAdapter(mAdapter)

        ustock_next_container.setOnClickListener {
            Global_Data.Number.clear()
            Global_Data.image_counter = 0;
            val i = Intent(UpdateStockScreen2@ this, UpdateStockScreen3::class.java)
            startActivity(i)
        }

        filter_lout.setOnClickListener {
            Global_Data.updateStockStatus=""
            val i = Intent(UpdateStockScreen2@ this, UpdateStockScreen1::class.java)
            startActivity(i)
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
                val sp: SharedPreferences = this@UpdateStockScreen2.getSharedPreferences("SimpleLogic", 0)
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


    override fun onBackPressed() {

        Kot_Gloval.stock_status = ""
        Kot_Gloval.stock_details = ""
        Kot_Gloval.stock_pick1 = ""
        Kot_Gloval.stock__pick2 = ""
        Kot_Gloval.stock__pick3 = ""
        Global_Data.Number.clear()
        Global_Data.image_counter = 0;

        val i = Intent(UpdateStockScreen2@ this, Sales_Dash::class.java)
        startActivity(i)
        finish()
    }

    fun GetNewLaunch_Datan() {

        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()

        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null)!!
        val shop_code: String = sp.getString("shopcode", null)!!

        val Cust_domain = sp.getString("Cust_Service_Url", "")
        var service_url = Cust_domain + "metal/api/v1/"

if(Global_Data.updateStockStatus.equals("yes"))
{
    try {
        service_url = service_url + "customer_stocks/get_stocks_details?email=" + URLEncoder.encode(user_email, "UTF-8") + "&customer_code=" + shop_code+"&page="+"1"
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }
}else{
    if (Global_Data.array_of_pVarient.size > 0) { //if (Global_Data.multiSelectVariant.size() > 0) {
        val ss = StringBuilder()
        var mStringArray: Array<String?>? = arrayOfNulls(Global_Data.array_of_pVarient.size)
        mStringArray = Global_Data.array_of_pVarient.toTypedArray()
        for (i in Global_Data.array_of_pVarient.indices) {

            if (i == 0) {
                ss.append("[");
            }
            ss.append(Global_Data.array_of_pVarient[i])
            if (Global_Data.array_of_pVarient.size - 1 != i) {
                ss.append(",")
            }
            else {
                ss.append("]");
            }
        }
        Global_Data.ProductVariant = ss.toString()
        try {
            service_url = service_url + "customer_stocks/get_stocks_details?email=" + URLEncoder.encode(user_email, "UTF-8") + "&product_category=" + URLEncoder.encode(Global_Data.Search_Category_name, "UTF-8") + "&sub_category=" + URLEncoder.encode(Global_Data.Search_Product_name, "UTF-8") + "&product_code=" + Global_Data.ProductVariant + "&customer_code=" + shop_code+"&page="+"1"
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    } else {
        try {
            service_url = service_url + "customer_stocks/get_stocks_details?email=" + URLEncoder.encode(user_email, "UTF-8") + "&product_category=" + URLEncoder.encode(Global_Data.Search_Category_name, "UTF-8") + "&sub_category=" + URLEncoder.encode(Global_Data.Search_Product_name, "UTF-8") + "&product_code=" + URLEncoder.encode(Global_Data.ProductVariant, "UTF-8") + "&customer_code=" + shop_code+"&page="+"1"
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
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
                            response_result = if (json.has("result")) {
                                json.getString("result")
                            } else {
                                "data"
                            }
                            if (response_result.equals("CustomerStock Not Found", ignoreCase = true)) {
                                dialog!!.hide()
                                Global_Data.Custom_Toast(context, response_result, "Yes")
                                finish()
                            }
                            else
                            if (response_result.equals("Products Not Found", ignoreCase = true)) {
                                dialog!!.hide()
                                Global_Data.Custom_Toast(context,response_result, "Yes")
                                finish()
                            }
                            else {
                                val customer_stocks = json.getJSONArray("customer_stocks")
                                val pictures = json.getJSONArray("pictures")

                                Log.i("volley", "response reg ads Length: " + customer_stocks.length())

                                Log.d("users", "customer_stocks$customer_stocks")

                                if (json.has("details") && Kot_Gloval.stock_details.equals("")) {
                                    Kot_Gloval.stock_details = json.getString("details")
                                }
                                if (json.has("stock_status") && Kot_Gloval.stock_status.equals("")) {
                                    Kot_Gloval.stock_status = json.getString("stock_status")
                                }

                                for (i in 0 until customer_stocks.length()) {
                                    val jsonObject = customer_stocks.getJSONObject(i)

                                    val nf = DecimalFormat("0.#");
                                    var mrp = "";
                                    var rp = "";

                                    try {
                                        val mm = jsonObject.getString("item_mrp").toDouble()
                                        val rr = jsonObject.getString("retail_price").toDouble()
                                        mrp = nf.format(mm);
                                        rp = nf.format(rr);
                                    } catch (e: Exception) {
                                        mrp = jsonObject.getString("item_mrp");
                                        rp = jsonObject.getString("retail_price");
                                        e.printStackTrace()
                                    }

                                    var qt_value:String? = "";
                                    try
                                    {
                                         qt_value = Global_Data.Order_hashmap.get(i.toString()+"&"+jsonObject.getString("product_code")+"@"+jsonObject.getString("product_code"))

                                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(qt_value)) {
                                            qt_value  = "";
                                        }
                                    }catch(e:Exception)
                                    {
                                        qt_value  = "";
                                        e.printStackTrace()
                                    }



                                    updateStockModel!!.add(UpdateStockModel(jsonObject.getString("product_code"), jsonObject.getString("item_name"), mrp, rp, jsonObject.getString("product_code"), jsonObject.getString("stock_qty"), qt_value!!))

                                }

                                for (i in 0 until pictures.length()) {
                                    val jsonObject = pictures.getJSONObject(i)

                                    if (i == 0 && Kot_Gloval.stock_pick1.equals("") && Kot_Gloval.stock_local_image_flag.equals("")) {
                                        Kot_Gloval.stock_pick1 = jsonObject.getString("picture");
                                    }

                                    if (i == 1 && Kot_Gloval.stock__pick2.equals("") && Kot_Gloval.stock_local_image_flag.equals("")) {
                                        Kot_Gloval.stock__pick2 = jsonObject.getString("picture");
                                    }

                                    if (i == 2 && Kot_Gloval.stock__pick3.equals("") && Kot_Gloval.stock_local_image_flag.equals("")) {
                                        Kot_Gloval.stock__pick3 = jsonObject.getString("picture");
                                    }


                                }

                                mAdapter!!.notifyDataSetChanged()

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

}
