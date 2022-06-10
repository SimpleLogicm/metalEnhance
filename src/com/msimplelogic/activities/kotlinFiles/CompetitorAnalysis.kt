package com.msimplelogic.activities.kotlinFiles

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ImageView
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Customer_Stock
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.webservice.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_beat_selection.*
import kotlinx.android.synthetic.main.content_competitor_analysis.*

class CompetitorAnalysis : BaseActivity(), OnItemSelectedListener {

    var new_launch: ImageView? = null
    var market_survey: ImageView? = null
    var advertisement: ImageView? = null
    var cd: ConnectionDetector? = null
    var isInternetPresent = false
    var sp:SharedPreferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_competitor_analysis)

        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")
        new_launch = findViewById<View>(R.id.new_launch) as ImageView
        advertisement = findViewById<View>(R.id.advertisement) as ImageView
        market_survey = findViewById<View>(R.id.market_survey) as ImageView
        cd = ConnectionDetector(applicationContext)

        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
            advertisement!!.setImageResource(R.drawable.ratings_dark)
            market_survey!!.setImageResource(R.drawable.marketing_material_dark)
            new_launch!!.setImageResource(R.drawable.product_comp_dark)
        }



        com_container_3!!.setOnClickListener {
            isInternetPresent = cd!!.isConnectingToInternet
            val launch = Intent(this@CompetitorAnalysis, CustomerRating::class.java)
            startActivity(launch)
        }

        com_container_2!!.setOnClickListener {
            isInternetPresent = cd!!.isConnectingToInternet
            val launch = Intent(this@CompetitorAnalysis, MarketingMaterial::class.java)
            startActivity(launch)
        }
        com_container_1!!.setOnClickListener {
            isInternetPresent = cd!!.isConnectingToInternet
//            val launch = Intent(this@CompetitorAnalysis, Product_comparison::class.java)
//            startActivity(launch)
            val launch = Intent(this@CompetitorAnalysis, Customer_Stock::class.java)
            startActivity(launch)

        }


    }

    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View, arg2: Int,
                                arg3: Long) {
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val i = Intent(this@CompetitorAnalysis, Neworderoptions::class.java)
                startActivity(i)
                super.onBackPressed()
            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp = this@CompetitorAnalysis.getSharedPreferences("SimpleLogic", 0)
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