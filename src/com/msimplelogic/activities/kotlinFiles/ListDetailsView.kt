package com.msimplelogic.activities.kotlinFiles

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import com.msimplelogic.adapter.Order_notes_adaptor
import com.msimplelogic.adapter.Visit_schedule_adaptor
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_list_details_view.*
import kotlinx.android.synthetic.main.content_list_details_view.*
import kotlinx.android.synthetic.main.content_list_details_view.hedder_theame
import kotlinx.android.synthetic.main.content_visit__schedule.*

class ListDetailsView : BaseActivity() {

    var adaptor: Visit_schedule_adaptor? = null
    var adaptor2: Order_notes_adaptor? = null
    var order_toolbar_title: TextView? = null
    var ssf_titletext:TextView?=null
    var total_count:TextView?=null
    var sp: SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_details_view)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
      //  setTitle(resources.getString(R.string.Visit_schedule))
        order_toolbar_title=findViewById(R.id.order_toolbar_title)
        ssf_titletext=findViewById(R.id.ssf_titletext)
        total_count=findViewById(R.id.total_count)
       // Kot_Gloval.activity_flag = "Notes"
        if (Kot_Gloval.activity_flag == "Notes") {
            order_toolbar_title!!.setText(resources.getString(R.string.notes))
            ssf_titletext!!.setText("All Notes")
            total_count!!.visibility=View.VISIBLE
        } else {
            order_toolbar_title!!.setText(resources.getString(R.string.Visit_schedule))
            ssf_titletext!!.setText(resources.getString(R.string.Upcomimg_visit_schedule))

        }
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
//            val img: Drawable =   context!!.resources.getDrawable(R.drawable.ic_baseline_date_range_24_dark)
//
//            et_date!!.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        rv_visit_shedulef?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        if (Kot_Gloval.activity_flag.equals("schedule")) {
            adaptor = Visit_schedule_adaptor(this, Kot_Gloval.listfull!!)
            rv_visit_shedulef?.adapter = adaptor
        } else
            if (Kot_Gloval.activity_flag.equals("Notes")) {
                var leng=  Kot_Gloval.listfullnotes!!.size.toString()
                total_count!!.setText("("+leng+")")
                adaptor2 = Order_notes_adaptor(this, Kot_Gloval.listfullnotes!!)
                rv_visit_shedulef?.adapter = adaptor2
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
                val sp = this@ListDetailsView.getSharedPreferences("SimpleLogic", 0)
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

        // val i = Intent(UpdateStockScreen2@ this, UpdateStockScreen1::class.java)
        // startActivity(i)
        finish()
    }

}
