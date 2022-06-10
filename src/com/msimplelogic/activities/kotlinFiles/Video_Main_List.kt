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
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.*
import com.msimplelogic.activities.R
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import cpm.simplelogic.helper.Config
import cpm.simplelogic.helper.CustomListAdapter
import cpm.simplelogic.helper.Movie
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_marketing.*
import kotlinx.android.synthetic.main.video_main.*
import kotlinx.android.synthetic.main.activity_video_main.*
import kotlinx.android.synthetic.main.activity_video_main.hedder_theame
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*

class Video_Main_List : BaseActivity() {
    var dialog: ProgressDialog? = null
    private val pDialog: ProgressDialog? = null
    private val movieList: MutableList<Movie> = ArrayList()
    var dbvoc = DataBaseHelper(this)
    private var listView: ListView? = null
    private var adapter: CustomListAdapter? = null
    var sp:SharedPreferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_main)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")
        listView = findViewById<View>(R.id.list) as ListView

        adapter = CustomListAdapter(this, movieList)
        sp = getSharedPreferences("SimpleLogic", 0)

        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
        }

        listView!!.adapter = adapter
        listView!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val c = view.findViewById<View>(R.id.rating) as TextView
            Config.YOUTUBE_VIDEO_CODE = c.text.toString()
            val d = view.findViewById<View>(R.id.title) as TextView
            Config.YOUTUBE_VIDEO_DISCRIPTION = d.text.toString()
            val e = view.findViewById<View>(R.id.releaseYear) as TextView
            Config.YOUTUBE_VIDEO_DATE = e.text.toString()
            //Toast.makeText(Video_Main_List.this,Config.YOUTUBE_VIDEO_CODE, Toast.LENGTH_SHORT).show();
            val launch = Intent(this@Video_Main_List, Youtube_Player_Activity::class.java)
            startActivity(launch)
        }
        dialog = ProgressDialog(this@Video_Main_List, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        val contacts = dbvoc.allMain
        if (contacts.size > 0) {
            GetNewLaunch_Datan()
        } else {
            finish()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }



    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                super.onBackPressed()            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp = this@Video_Main_List.getSharedPreferences("SimpleLogic", 0)
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


    fun GetNewLaunch_Datan() { //context = contextn;
//loginDataBaseAdapter=new LoginDataBaseAdapter(Video_Main_List.this);
//loginDataBaseAdapter=loginDataBaseAdapter.open();
//PreferencesHelper Prefs = new PreferencesHelper(context);
//String URL = Prefs.GetPreferences("URL");
//dialog = new ProgressDialog(Video_Main_List.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()

        val sp1 = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp1.getString("USER_EMAIL", null)!!

        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val Cust_domain = sp.getString("Cust_Service_Url", "")
        val service_url = Cust_domain + "metal/api/v1/"
        //val device_id = sp.getString("devid", "");
        Log.d("Server url", "Server url" + service_url + "advertisements/send_advertisements?email=" + user_email)
        var stringRequest: StringRequest? = null
        stringRequest = StringRequest(service_url + "advertisements/send_advertisements?email=" + user_email,
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
                                dialog!!.hide()

                                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Sorry_No_Record_Found),"yes")
                                finish()
                            } else if (response_result.equals("Device not registered", ignoreCase = true)) {
                                dialog!!.hide()

                                Global_Data.Custom_Toast(applicationContext, response_result,"yes")
                                finish()
                            } else {
                                val ads = json.getJSONArray("ads")
                                //
                                Log.i("volley", "response reg ads Length: " + ads.length())
                                //
                                Log.d("users", "ads$ads")
                                //
//
                                for (i in 0 until ads.length()) {
                                    val jsonObject = ads.getJSONObject(i)
                                    //Config.YOUTUBE_VIDEO_CODE = jsonObject.getString("user_name");
//Config.YOUTUBE_VIDEO_DATE = jsonObject.getString("user_name");
//Config.YOUTUBE_VIDEO_DISCRIPTION = jsonObject.getString("user_name");
                                    val movie = Movie()
                                    movie.title = jsonObject.getString("description")
                                    movie.thumbnailUrl = "http://img.youtube.com/vi/" + jsonObject.getString("youtube_id") + "/0.jpg"
                                    movie.rating = jsonObject.getString("youtube_id")
                                    movie.year = jsonObject.getString("date")
                                    movieList.add(movie)
                                    //
                                }
                                //Intent launch = new Intent(context,Youtube_Player_Activity.class);
//startActivity(launch);
                                dialog!!.dismiss()
                                adapter!!.notifyDataSetChanged()
                                //finish();
                            }
                            //  finish();
// }
// output.setText(data);
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
                        Global_Data.Custom_Toast(this@Video_Main_List,
                                "Network Error","yes")
                    } else if (error is AuthFailureError) {
                        Global_Data.Custom_Toast(this@Video_Main_List,
                                "Server AuthFailureError  Error","yes")
                    } else if (error is ServerError) {
                        Global_Data.Custom_Toast(this@Video_Main_List,
                                resources.getString(R.string.Server_Errors),"yes")
                    } else if (error is NetworkError) {
                        Global_Data.Custom_Toast(this@Video_Main_List,
                                "Network Error","yes")
                    } else if (error is ParseError) {
                        Global_Data.Custom_Toast(this@Video_Main_List,
                                "ParseError   Error","yes")
                    } else {
                        Global_Data.Custom_Toast(this@Video_Main_List,
                                error.message,"yes")
                    }
                    //						Intent launch = new Intent(Video_Main_List.this,MainActivity.class);
//						startActivity(launch);
                    finish()
                    dialog!!.dismiss()
                    // finish();
                })
        val requestQueue = Volley.newRequestQueue(this@Video_Main_List)
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