package com.msimplelogic.activities.kotlinFiles

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.msimplelogic.activities.*
import com.msimplelogic.services.getServices
import com.willy.ratingbar.BaseRatingBar
import com.willy.ratingbar.BaseRatingBar.OnRatingChangeListener
import cpm.simplelogic.helper.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_customer_rating.*
import kotlinx.android.synthetic.main.content_competitor_analysis.*
import kotlinx.android.synthetic.main.content_customer_rating.*
import kotlinx.android.synthetic.main.content_customer_rating.hedder_theame
import org.json.JSONException
import org.json.JSONObject

class CustomerRating : BaseActivity() {
    var custHappinessRating: String? = null
    var custProductQuality: String? = null
    val arrayListautocomplete = ArrayList<String>()
    var sp: SharedPreferences? = null
    var cd: ConnectionDetector? = null
    var isInternetPresent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_rating)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        cd = ConnectionDetector(applicationContext)

        //setTitle(resources.getString(R.string.Ratings))

//        val ratingBar = ScaleRatingBar(this)
//        ratingBar.numStars = 5
//        ratingBar.setMinimumStars(1f)
//        ratingBar.rating = 3f
//        ratingBar.starPadding = 10
//        ratingBar.stepSize = 0.5f
//        ratingBar.setWidth(105)
//        ratingBar.setHeight(105)
//        ratingbar.setIsIndicator(false)
//        ratingbar.setClickable(true)
//        ratingbar.setScrollable(true)
//        ratingbar.setClearRatingEnabled(true)
//        ratingBar.setEmptyDrawableRes(R.drawable.start_empty)
//        ratingBar.setFilledDrawableRes(R.drawable.start_empty)
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
        }

        editText1?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (editText1!!.length() == 1) {
                    autocompletetextdatafetch(s)
                }
                // autocompletetextdatafetch(s)
            }
        })

        simpleRatingBar1.setOnRatingChangeListener(object : OnRatingChangeListener {

            override fun onRatingChange(ratingBar: BaseRatingBar?, rating: Float, fromUser: Boolean) {
                Log.e("", "onRatingChange: $rating")
                custHappinessRating = rating.toString()
                Toast.makeText(applicationContext,"onRatingChange1: $rating", Toast.LENGTH_LONG).show()
            }
        })

        simpleRatingBar2.setOnRatingChangeListener(object : OnRatingChangeListener {

            override fun onRatingChange(ratingBar: BaseRatingBar?, rating: Float, fromUser: Boolean) {
                Log.e("", "onRatingChange: $rating")
                custProductQuality = rating.toString()
                //Toast.makeText(applicationContext,"onRatingChange2: $rating", Toast.LENGTH_LONG).show()
            }
        })

        rating_click!!.setOnClickListener(View.OnClickListener {
            if (editText1.text.toString().length > 0) {
                getServices.SyncRating(this@CustomerRating, editText1.text.toString(), custHappinessRating, custProductQuality)
            } else {
                Global_Data.Custom_Toast(this@CustomerRating, "Please Enter Competitor Name", "Yes")
            }
        })

        tv_history.setOnClickListener {
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                startActivity(Intent(this@CustomerRating, Ratingview::class.java))

            } else {
                Global_Data.Custom_Toast(this@CustomerRating, "You don't have internet connection.", "yes")
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
                val sp = this@CustomerRating.getSharedPreferences("SimpleLogic", 0)
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

    override fun onBackPressed() {
        val i = Intent(applicationContext, CompetitorAnalysis::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        finish()
    }

    private fun autocompletetextdatafetch(s: CharSequence) {

        val domain = resources.getString(R.string.service_domain)

        val spf: SharedPreferences = this@CustomerRating.getSharedPreferences("SimpleLogic", 0)
        val user_email = spf.getString("USER_EMAIL", null)

        val shop_code: String = spf.getString("shopcode", null)!!

        Log.i("volley", "domain: $domain")

        var service_url = ""
        service_url = domain + "marketing_materials/get_competitor_names_on_search?email=" + user_email + "&string=" + s + "&customer_code=" + shop_code

        Log.i("volley", "service_url: $service_url")

        val stringRequest = StringRequest(Request.Method.GET, service_url,
                Response.Listener { response ->

                    arrayListautocomplete.clear()

                    try {

                        val obj = JSONObject(response)


                        val heroArray = obj.getJSONArray("result")


                        for (i in 0 until heroArray.length()) {

                            val hero = heroArray.getString(i)

                            arrayListautocomplete.add(hero)
                        }

                        //  runOnUiThread()
                        this@CustomerRating.runOnUiThread(Runnable {
                            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListautocomplete)

                            editText1!!.setAdapter(adapter)
                        })


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    //    Toast.makeText(getApplicationContext(), error.message, Toast.LENGTH_SHORT).show()
                    Global_Data.Custom_Toast(getApplicationContext(), error.message, "yes")
                })

        val requestQueue = Volley.newRequestQueue(getApplicationContext())

        requestQueue.add<String>(stringRequest)
    }


}
