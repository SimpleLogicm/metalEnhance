package com.msimplelogic.activities.kotlinFiles

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.MainActivity
import com.msimplelogic.activities.R
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_planner.*
import kotlinx.android.synthetic.main.content_planner.*

class ActivityPlanner : BaseActivity() {
    //var toolbar: Toolbar? = null
    var sp :SharedPreferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_planner)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Global_Data.Order_hashmap.clear()
        setTitle("")


    sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            task_btn.setImageResource(R.drawable.tasks_dark)
            leavem_btn.setImageResource(R.drawable.leave_managementdark)
            tourp_btn.setImageResource(R.drawable.tour_programme_dark)
            note_btn.setImageResource(R.drawable.notes_dark)
            meeting_btn.setImageResource(R.drawable.meeting_dark)
            timesheet_btn.setImageResource(R.drawable.time_sheet_dark)
            hedder_theame.setImageResource(R.drawable.dark_hedder)

//ll.setBackgroundResource(R.drawable.dark_theme_background);
        }
        bankcardId.setOnClickListener(View.OnClickListener {
            var intent  = Intent(this@ActivityPlanner, ActivityTask::class.java)
            intent.putExtra("activity_flag","Task")
            startActivity(intent)

        })

        leavem_btn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@ActivityPlanner, ActivityLeaveManagement::class.java))
            //Global_Data.PlannerName ="Task"
        })

        tourp_btn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@ActivityPlanner, ActivityTour::class.java))

        })

        note_btn.setOnClickListener(View.OnClickListener {
            Global_Data.PlannerName ="Note"
            startActivity(Intent(this@ActivityPlanner, ActivityTask::class.java))

        })

        meeting_btn.setOnClickListener(View.OnClickListener {
            val cona = Intent(this@ActivityPlanner, Promotional_meetings::class.java)
            startActivity(cona)

        })

        timesheet_btn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@ActivityPlanner, TimeSheetActivity::class.java))

        })


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
                val sp: SharedPreferences = this@ActivityPlanner.getSharedPreferences("SimpleLogic", 0)
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

    override fun onBackPressed() { // TODO Auto-generated method stub
//super.onBackPressed();
        val i = Intent(this@ActivityPlanner, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i)
        finish()
    }



}