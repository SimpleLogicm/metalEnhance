package com.msimplelogic.activities.kotlinFiles

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.StrictMode
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.MainActivity
import com.msimplelogic.activities.R
import com.msimplelogic.adapter.Product_demo_adaptor
import com.msimplelogic.model.Productdemo_model
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_productdemo.*
import kotlinx.android.synthetic.main.content_productdemo.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URLEncoder

class Productdemo : BaseActivity() {

    var produvr_varient: AutoCompleteTextView? = null
    var rv: RecyclerView? = null
    var list: ArrayList<Productdemo_model>? = null
    var adaptor: Product_demo_adaptor? = null
    private var pDialog: ProgressDialog? = null
    var list_product_name: ArrayList<String>? = null

var sp:SharedPreferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productdemo)
        var builder = StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        produvr_varient = findViewById(R.id.produvr_varient)
        rv = findViewById(R.id.rv)

        list = ArrayList<Productdemo_model>()
        list_product_name = ArrayList<String>()
        pDialog = ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theamep.setImageResource(R.drawable.dark_hedder)
        }
        getproductdemo()

//        list!!.add(Productdemo_model("","Abc","giudgiuugdiui","26-78"))
//        list!!.add(Productdemo_model("","Abc","giudgiuugdiui","26-78"))
//        list!!.add(Productdemo_model("","Abc","giudgiuugdiui","26-78"))
//        list!!.add(Productdemo_model("","Abc","giudgiuugdiui","26-78"))
//        list!!.add(Productdemo_model("","Abc","giudgiuugdiui","26-78"))
//
//
//        adaptor = Product_demo_adaptor(this, list as java.util.ArrayList<Productdemo_model>)
//
//        val mLayoutManager = LinearLayoutManager(this)
//        rv!!.layoutManager = mLayoutManager
//        //viewtarget_list.addItemDecoration(DividerItemDecoration(this@ActivityViewTarget, LinearLayoutManager.VERTICAL))
//        rv!!.itemAnimator = DefaultItemAnimator()
//        rv!!.adapter = adaptor

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val adapter = ArrayAdapter<String>(this,
                R.layout.autocomplete, list_product_name!!)
        produvr_varient!!.setThreshold(1)// will start working from
        // first character
        produvr_varient!!.setAdapter(adapter)// setting the adapter
        // data into the
        // AutoCompleteTextView


        produvr_varient!!.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= produvr_varient!!.getRight() - produvr_varient!!.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()) {

                    val view = this@Productdemo.getCurrentFocus()
                    if (view != null) {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
                    }
                    //autoCompleteTextView1.setText("");
                    produvr_varient!!.showDropDown()
                    return@OnTouchListener true
                }
            }
            false
        })

        produvr_varient!!.setOnItemClickListener(AdapterView.OnItemClickListener { parent, arg1, pos, id ->

            // list!!.get()


            var text = parent.getItemAtPosition(pos).toString()

            var filterlist: ArrayList<Productdemo_model>? = null
            filterlist = ArrayList<Productdemo_model>()

            for (i in 0 until list!!.size) {
                if (list!!.get(i).Productname.equals(text)) {
                    filterlist!!.add( Productdemo_model(list!!.get(i).image,list!!.get(i).productname,list!!.get(i).proddiscrip,list!!.get(i).lastsync))
                 //   list!!.get(i).
                }

            }

            adaptor = Product_demo_adaptor(this, filterlist as java.util.ArrayList<Productdemo_model>)

            val mLayoutManager = LinearLayoutManager(this)
            rv!!.layoutManager = mLayoutManager
            //viewtarget_list.addItemDecoration(DividerItemDecoration(this@ActivityViewTarget, LinearLayoutManager.VERTICAL))
            rv!!.itemAnimator = DefaultItemAnimator()
            rv!!.adapter = adaptor
            adapter.notifyDataSetChanged()


//            if (text.equals("")){
//                adaptor = Product_demo_adaptor(this, list as java.util.ArrayList<Productdemo_model>)
//
//                val mLayoutManager = LinearLayoutManager(this)
//                rv!!.layoutManager = mLayoutManager
//                //viewtarget_list.addItemDecoration(DividerItemDecoration(this@ActivityViewTarget, LinearLayoutManager.VERTICAL))
//                rv!!.itemAnimator = DefaultItemAnimator()
//                rv!!.adapter = adaptor
//                adapter.notifyDataSetChanged()
//            }


        })

        //produvr_varient!!.addTextChangedListener()
        produvr_varient!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (produvr_varient!!.getText().toString().trim({ it <= ' ' }).length == 0) {

                    adaptor = Product_demo_adaptor(this@Productdemo, list as java.util.ArrayList<Productdemo_model>)

                    val mLayoutManager = LinearLayoutManager(this@Productdemo)
                    rv!!.layoutManager = mLayoutManager
                    //viewtarget_list.addItemDecoration(DividerItemDecoration(this@ActivityViewTarget, LinearLayoutManager.VERTICAL))
                    rv!!.itemAnimator = DefaultItemAnimator()
                    rv!!.adapter = adaptor
                    adapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

    }

    private fun getproductdemo() {

        pDialog!!.setMessage(resources.getString(R.string.Please_Wait))
        pDialog!!.setTitle(resources.getString(R.string.app_name))
        pDialog!!.setCancelable(false)
        pDialog!!.show()
        var domain = ""

        val spf: SharedPreferences = this@Productdemo.getSharedPreferences("SimpleLogic", 0)
        val user_email = spf.getString("USER_EMAIL", null)


        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val Cust_domain = sp.getString("Cust_Service_Url", "")
        val service_url = Cust_domain + "metal/api/v1/"

        domain = service_url + "product_demos/get_product_demo_detail?email=" + user_email
        // Log.i("volley", "Sim_Number: " + Global_Val.Sim_Number);
        Log.i("volley", "Service url: " + domain )


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
                            if (response_result.equals("Product Demo Not Found", ignoreCase = true)) {
                                pDialog!!.hide()

                                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Sorry_No_Record_Found), "yes")
                                finish()
                            } else if (response_result.equals("Device not registered", ignoreCase = true)) {
                                pDialog!!.hide()
                                // Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG).show();

                                Global_Data.Custom_Toast(applicationContext, response_result,"yes")
                                finish()
                            } else {
                                val launches = json.getJSONArray("product_demos")
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
                                    list!!.add(Productdemo_model(`object`.getString("attachment"), `object`.getString("name"), `object`.getString("description"), `object`.getString("status")))

                                    list_product_name!!.add(`object`.getString("name"))
                                }
                                pDialog!!.hide()
                                adaptor = Product_demo_adaptor(this, list as java.util.ArrayList<Productdemo_model>)

                                val mLayoutManager = LinearLayoutManager(this)
                                rv!!.layoutManager = mLayoutManager
                                //viewtarget_list.addItemDecoration(DividerItemDecoration(this@ActivityViewTarget, LinearLayoutManager.VERTICAL))
                                rv!!.itemAnimator = DefaultItemAnimator()
                                rv!!.adapter = adaptor
                            }
                            //  finish();
// }
// output.setText(data);
                        } catch (e: JSONException) {
                            e.printStackTrace()

                            Global_Data.Custom_Toast(this@Productdemo,
                                    "Service Error","yes")
                            val launch = Intent(this@Productdemo, MainActivity::class.java)
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
                    if (error is TimeoutError || error is NoConnectionError) {

                        Global_Data.Custom_Toast(this@Productdemo,
                                "Network Error","")
                    } else if (error is AuthFailureError) {

                        Global_Data.Custom_Toast(this@Productdemo,
                                "Server AuthFailureError  Error","")
                    } else if (error is ServerError) {

                        Global_Data.Custom_Toast(this@Productdemo,
                                resources.getString(R.string.Server_Errors),"")
                    } else if (error is NetworkError) {

                        Global_Data.Custom_Toast(this@Productdemo,
                                "Network   Error","yes")
                    } else if (error is ParseError) {

                        Global_Data.Custom_Toast(this@Productdemo,
                                "ParseError   Error","")
                    } else {

                        Global_Data.Custom_Toast(this@Productdemo, error.message,"yes")
                    }
                    val launch = Intent(this@Productdemo, MainActivity::class.java)
                    startActivity(launch)
                    finish()
                    pDialog!!.dismiss()
                    // finish();
                })
        val requestQueue = Volley.newRequestQueue(this@Productdemo)
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
                val sp = this@Productdemo.getSharedPreferences("SimpleLogic", 0)
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
