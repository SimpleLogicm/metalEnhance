package com.msimplelogic.activities.kotlinFiles

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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import com.msimplelogic.helper.ItemOffsetDecoration
import com.msimplelogic.imageadapters.GalleryAdapter
import com.msimplelogic.imageadapters.Image
import com.msimplelogic.activities.R

import kotlinx.android.synthetic.main.activity_beat_selection.*
import kotlinx.android.synthetic.main.content_beat_selection.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*

class Image_Gellary : BaseActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private var images: ArrayList<Image>? = null
    private var pDialog: ProgressDialog? = null
    private var mAdapter: GalleryAdapter? = null
    private var recyclerView: RecyclerView? = null
    var dbvoc = DataBaseHelper(this)
    var sp:SharedPreferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imagegellary_main)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //String domain = "http://150.242.140.105:8005/metal/api/performance/v1/";


        recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        pDialog = ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        images = ArrayList()
        mAdapter = GalleryAdapter(applicationContext, images)
        val mLayoutManager: LayoutManager = GridLayoutManager(applicationContext, 2)
        recyclerView!!.layoutManager = mLayoutManager
        val itemDecoration =  ItemOffsetDecoration(applicationContext, R.dimen.item_offset);
        recyclerView!!.addItemDecoration(itemDecoration);
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter

        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
        }

        val contacts = dbvoc.allMain
        if (contacts.size > 0) {
            GetNewLaunch_Datann()
        } else {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
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
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    fun GetNewLaunch_Datann() {
        var domain = ""

        val spf: SharedPreferences = this@Image_Gellary.getSharedPreferences("SimpleLogic", 0)
        val user_email = spf.getString("USER_EMAIL", null)

        pDialog!!.setMessage(resources.getString(R.string.Please_Wait))
        pDialog!!.setTitle(resources.getString(R.string.app_name))
        pDialog!!.setCancelable(false)
        pDialog!!.show()
        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val Cust_domain = sp.getString("Cust_Service_Url", "")
        val service_url = Cust_domain + "metal/api/v1/"
        val device_id = sp.getString("devid", "")
        domain = service_url
        Log.d("Server url", "Server url" + domain + "new_launches?email=" + user_email)
        var stringRequest: StringRequest? = null
        stringRequest = StringRequest(domain + "new_launches?email=" + user_email,
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
                            if (response_result.equals("No Data Found", ignoreCase = true)) {
                                pDialog!!.hide()
//                                val toast = Toast.makeText(applicationContext, resources.getString(R.string.Sorry_No_Record_Found), Toast.LENGTH_LONG)
//                                toast.setGravity(Gravity.CENTER, 0, 0)
//                                toast.show()
                                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Sorry_No_Record_Found),"yes")
                                finish()
                            } else if (response_result.equals("Device not registered", ignoreCase = true)) {
                                pDialog!!.hide()
                                // Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG).show();
//                                val toast = Toast.makeText(applicationContext, response_result, Toast.LENGTH_LONG)
//                                toast.setGravity(Gravity.CENTER, 0, 0)
//                                toast.show()
                                Global_Data.Custom_Toast(applicationContext, response_result, "yes")
                                finish()
                            } else {
                                val launches = json.getJSONArray("launches")
                                //
                                Log.i("volley", "response reg launches Length: " + launches.length())
                                //
                                Log.d("users", "launches$launches")
                                //
//
                                images!!.clear()
                                for (i in 0 until launches.length()) {
                                    val `object` = launches.getJSONObject(i)
                                    val image = Image()
                                    image.name = `object`.getString("name")
                                    // JSONObject url = object.getJSONObject("url");
                                    image.small = `object`.getString("small")
                                    image.medium = `object`.getString("small")
                                    image.large = `object`.getString("medium")
                                    image.timestamp = `object`.getString("date")
                                    images!!.add(image)
                                }
                                pDialog!!.hide()
                                mAdapter!!.notifyDataSetChanged()
                                //finish();
                            }
                            //  finish();
// }
// output.setText(data);
                        } catch (e: JSONException) {
                            e.printStackTrace()
//                            val toast = Toast.makeText(this@Image_Gellary,
//                                    "Service Error",
//                                    Toast.LENGTH_LONG)
//                            toast.setGravity(Gravity.CENTER, 0, 0)
//                            toast.show()
                            Global_Data.Custom_Toast(this@Image_Gellary,
                                    "Service Error","yes")
                            val launch = Intent(this@Image_Gellary, MainActivity::class.java)
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
//                        val toast = Toast.makeText(this@Image_Gellary,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@Image_Gellary,
                                "Network Error","")
                    } else if (error is AuthFailureError) {
//                        val toast = Toast.makeText(this@Image_Gellary,
//                                "Server AuthFailureError  Error",
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@Image_Gellary,
                                "Server AuthFailureError  Error","")
                    } else if (error is ServerError) {
//                        val toast = Toast.makeText(this@Image_Gellary,
//                                resources.getString(R.string.Server_Errors),
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@Image_Gellary,
                                resources.getString(R.string.Server_Errors),"")
                    } else if (error is NetworkError) {
//                        val toast = Toast.makeText(this@Image_Gellary,
//                                "Network   Error",
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@Image_Gellary,
                                "Network   Error","")
                    } else if (error is ParseError) {
//                        val toast = Toast.makeText(this@Image_Gellary,
//                                "ParseError   Error",
//                                Toast.LENGTH_LONG)
//                        toast.show()
                        Global_Data.Custom_Toast(this@Image_Gellary,
                                "ParseError   Error","")
                    } else { // Toast.makeText(Image_Gellary.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(this@Image_Gellary, error.message, Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(this@Image_Gellary, error.message,"yes")
                    }
                    val launch = Intent(this@Image_Gellary, MainActivity::class.java)
                    startActivity(launch)
                    finish()
                    pDialog!!.dismiss()
                    // finish();
                })
        val requestQueue = Volley.newRequestQueue(this@Image_Gellary)
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