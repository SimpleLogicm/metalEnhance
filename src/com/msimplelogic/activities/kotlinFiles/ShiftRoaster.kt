package com.msimplelogic.activities.kotlinFiles

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.EventDay
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.BasicMapDemoActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.helper.MyEventDay
import com.whiteelephant.monthpicker.MonthPickerDialog
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_shift_roaster.*
import kotlinx.android.synthetic.main.content_shift_roaster.*
import java.text.DateFormatSymbols
import java.util.*

class ShiftRoaster : BaseActivity() {
    val RESULT = "result"
    val EVENT = "event"
    private val ADD_NOTE = 44
    private val mEventDays: List<EventDay> = ArrayList()
    var events: MutableList<EventDay> = ArrayList()
    var selected_month : Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shift_roaster)

        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")

        // val myEventDay = MyEventDay(datePicker.getSelectedDate(),
        //     R.drawable.ic_message_black_48dp, noteEditText.getText().toString())

        val calendar = Calendar.getInstance()
        events.add(MyEventDay(calendar, R.drawable.cal_icon, "fghgfhgfh"))
        calendarView.setEvents(events);

        calendar.set(2019, 7, 5)
        calendarView.setDate(calendar)

        calendarView!!.setOnDayClickListener({

            val clickedDayCalendar: Calendar = it.getCalendar()
            previewNote(it);
        })

        Roast_date.setOnClickListener {
            val builder = MonthPickerDialog.Builder(this@ShiftRoaster, MonthPickerDialog.OnDateSetListener { selectedMonth, selectedYear ->
               Log.d("RDATE","RDATE"+selectedMonth+" "+selectedYear);
                val years = Calendar.getInstance().get(Calendar.YEAR);
                selected_month = selectedMonth;
                val months = DateFormatSymbols().getMonths()[selectedMonth]
                tv_date.setText("$months $years")

            },  /* activated number in year */Calendar.YEAR, Calendar.MONTH)
            .setActivatedMonth(selected_month)
            .setActivatedYear(Calendar.YEAR)
            builder.showMonthOnly()
                    .build()
                    .show()
        }

        att_mapat.setOnClickListener {
            val i = Intent(this@ShiftRoaster, BasicMapDemoActivity::class.java)
            startActivity(i)
        }

        att_log.setOnClickListener {
            val i = Intent(this@ShiftRoaster, AttendanceLog::class.java)
            startActivity(i)
        }

    }

    private fun previewNote(eventDay: EventDay) {
        if (eventDay is MyEventDay) {
            val myEventDay: MyEventDay = eventDay
            val dates = myEventDay.getCalendar().getTime();
            val notes = myEventDay.note
            val intent = Intent(this, NotePreviewActivity::class.java)
            intent.putExtra("dates", dates.toString())
            intent.putExtra("notes", notes)
            startActivity(intent)
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
                val sp = this@ShiftRoaster.getSharedPreferences("SimpleLogic", 0)
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

        val i = Intent(this@ShiftRoaster, BasicMapDemoActivity::class.java)
        startActivity(i)
        super.onBackPressed()

    }

}
