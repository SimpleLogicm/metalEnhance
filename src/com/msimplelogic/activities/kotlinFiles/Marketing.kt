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
import android.widget.Toast
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.activities.Survey_Home
import com.msimplelogic.webservice.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_beat_selection.*
import kotlinx.android.synthetic.main.activity_marketing.*

class Marketing : BaseActivity(), OnItemSelectedListener {
    var sp: SharedPreferences?=null

    var cd: ConnectionDetector? = null
    var isInternetPresent = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.marketting_main)

        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")

        cd = ConnectionDetector(applicationContext)

        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
            new_launch.setImageResource(R.drawable.new_launch_dark)
           // productdemoeimg.setBackgroundResource(R.drawable.marketing_material_dark)
            market_survey!!.setImageResource(R.drawable.marketing_survey_dark)
            advertisement!!.setImageResource(R.drawable.ad_dark)
        }

        product_demo.setOnClickListener {
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                val launch = Intent(this@Marketing, Productdemo::class.java)
                startActivity(launch)

            } else {
//                val toast = Toast.makeText(applicationContext, resources.getString(R.string.internet_connection_error), Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.CENTER, 0, 0)
//                toast.show()
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error),"yes")
            }
        }
        m_card_view.setOnClickListener {
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                val launch = Intent(this@Marketing, Image_Gellary::class.java)
                startActivity(launch)

            } else {
//                val toast = Toast.makeText(applicationContext, resources.getString(R.string.internet_connection_error), Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.CENTER, 0, 0)
//                toast.show()
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error),"yey")
            }
        }
        m_card_view3.setOnClickListener {
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                val launch = Intent(this@Marketing, Video_Main_List::class.java)
                startActivity(launch)

            } else {
//                val toast = Toast.makeText(applicationContext, resources.getString(R.string.internet_connection_error), Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.CENTER, 0, 0)
//                toast.show()
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error),"yes")
            }
        }
        m_card_view2.setOnClickListener {
            val map = Intent(this@Marketing, Survey_Home::class.java)
            startActivity(map)
            finish()

        }

    }

    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View, arg2: Int,
                                arg3: Long) {
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                onBackPressed()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onBackPressed() {
//
//        val i = Intent(this@Marketing, MainActivity::class.java)
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        startActivity(i)
//        finish()
//    }
override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.main, menu)
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
            R.id.add -> {
                var targetNew = ""
                val sp = this@Marketing.getSharedPreferences("SimpleLogic", 0)
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