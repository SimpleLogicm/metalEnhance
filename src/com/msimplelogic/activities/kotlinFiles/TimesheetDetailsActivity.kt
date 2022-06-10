package com.msimplelogic.activities.kotlinFiles

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.msimplelogic.activities.R
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jakewharton.threetenabp.AndroidThreeTen
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.yearMonth
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import com.msimplelogic.adapter.AdapterTimesheet
import com.msimplelogic.helper.MyLayoutOperation
import com.msimplelogic.model.TimeSheetModel
import com.msimplelogic.model.TimeSheetShowModel
import com.msimplelogic.webservice.ConnectionDetector
import com.view.calender.horizontal.umar.horizontalcalendarview.DayDateMonthYearModel
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarListener
import com.view.calender.horizontal.umar.horizontalcalendarview.HorizontalCalendarView
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_timesheetdetails.*
import kotlinx.android.synthetic.main.content_timesheet.*
import kotlinx.android.synthetic.main.content_timesheetdetails.*
import kotlinx.android.synthetic.main.copytimesheet_dialog.view.*
import kotlinx.android.synthetic.main.example_1_calendar_day.view.*
import kotlinx.android.synthetic.main.timesheetrowdetails.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

class TimesheetDetailsActivity : BaseActivity(), HorizontalCalendarListener {
    private var adapter: AdapterTimesheet? = null
    //var list: ArrayList<TimeSheetModel>? = null
    var dialog: ProgressDialog? = null
    var dateStr: String? = null
    //var copyTimesheetRadioValue: String?=null
    var dsf: String? = null
    var dsf1: String? = null
    var dsf2: String? = null
    var dsf3: String? = null
    var dsf4: String? = null
    var dsf5: String? = null
    val from_arr = ArrayList<String>()
    val favourite_arr = ArrayList<String>()
    var myCalendar: Calendar? = null
    var copySelectDate: TextView? = null
    var date: DatePickerDialog.OnDateSetListener? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var legendLayout: LinearLayout? = null
    var dbvoc = DataBaseHelper(this)
    var loginDataBaseAdapter: LoginDataBaseAdapter? = null
    lateinit var currentMonthTextView: TextView
    var tmdetails_from: EditText? = null
var sp:SharedPreferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContentView(R.layout.activity_timesheetdetails)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        cd = ConnectionDetector(TimesheetDetailsActivity@ this)

        currentMonthTextView = findViewById(R.id.month)
        val hcv = findViewById<HorizontalCalendarView>(R.id.horizontalcalendarview)
        hcv.setContext(this@TimesheetDetailsActivity)
        hcv.setBackgroundColor(resources.getColor(R.color.white))
        hcv.showControls(true)
        hcv.setControlTint(R.color.light_grey)
        hcv.changeAccent(R.color.black)
        loginDataBaseAdapter = LoginDataBaseAdapter(this)
        loginDataBaseAdapter = loginDataBaseAdapter!!.open()

        //MyLayoutOperation.addd(this)

        val linearLayoutForm = this.findViewById<View>(R.id.linearLayoutForm) as LinearLayout
        val newView = this.layoutInflater.inflate(R.layout.timesheetrowdetails, null) as LinearLayout
        newView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val tmdetails_from = newView.findViewById<View>(R.id.tmdetails_from) as EditText
        val tmdetails_to = newView.findViewById<View>(R.id.tmdetails_to) as EditText
        var et_task = newView.findViewById<View>(R.id.et_task) as EditText
        var et_details1 = newView.findViewById<View>(R.id.et_details1) as EditText
        var et_details2 = newView.findViewById<View>(R.id.et_details2) as EditText
        var et_remark = newView.findViewById<View>(R.id.et_remark) as EditText
        var favourite = newView.findViewById<View>(R.id.fav_img) as ImageView
        var favourite_red = newView.findViewById<View>(R.id.fav_img_red) as ImageView
        var favourite_status = newView.findViewById<View>(R.id.fav_status) as TextView
        var delete_btn = newView.findViewById<View>(R.id.btnRemove) as ImageButton

        delete_btn.visibility=View.GONE

        var pickerfrom: TimePickerDialog
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {

            hedder_theame.setImageResource(R.drawable.dark_hedder)

//ll.setBackgroundResource(R.drawable.dark_theme_background);
        }


        tmdetails_from.setOnClickListener(View.OnClickListener {
            tmdetails_from.setText("")
            dsf=""
            val cldr = Calendar.getInstance()
            val hour = cldr[Calendar.HOUR_OF_DAY]
            val minutes = cldr[Calendar.MINUTE]
            // time picker dialog
            pickerfrom = TimePickerDialog(this,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(tp: TimePicker?, sHour: Int, sMinute: Int) {
                            tmdetails_from.setText("$sHour:$sMinute")

                        }
                    }, hour, minutes, true)
            pickerfrom.show()
        })

        var pickerto: TimePickerDialog
        tmdetails_to.setOnClickListener(View.OnClickListener {
            val cldr = Calendar.getInstance()
            val hour = cldr[Calendar.HOUR_OF_DAY]
            val minutes = cldr[Calendar.MINUTE]
            // time picker dialog
            pickerto = TimePickerDialog(this,
                    object : TimePickerDialog.OnTimeSetListener {
                        override fun onTimeSet(tp: TimePicker?, sHour: Int, sMinute: Int) {
                            if(tmdetails_from.length()>0)
                            {
                            val date = Date()
                            val dateFormat = SimpleDateFormat("HH:mm")
                            dateFormat.format(date)

                            val fromtime=dateFormat.parse(tmdetails_from.text.toString())
                            val totime=dateFormat.parse("$sHour:$sMinute")

                            if ((fromtime).after(totime)) {
                                Global_Data.Custom_Toast(applicationContext, "To Time should be Greater than From Time", "Yes")
                            } else {
                                tmdetails_to.setText("$sHour:$sMinute")
                            }
                        }else{
                            Global_Data.Custom_Toast(applicationContext, "Please Select From Time", "Yes")
                        }

                        }
                    }, hour, minutes, true)
            pickerto.show()
        })

        favourite.setOnClickListener {
            if (favourite.getVisibility() == View.VISIBLE) {
                // Its visible
                favourite.visibility = View.GONE
                favourite_red.visibility = View.VISIBLE
                favourite_status.setText("true")

            } else {
                favourite.visibility = View.VISIBLE
                favourite_red.visibility = View.GONE
                favourite_status.setText("false")

//                favourite_arr.add("false")
                // Either gone or invisible
                //favourite.setImageResource(R.drawable.ic_favorite_red_24dp)
            }
            //linearLayoutForm.removeView(newView)
        }

        favourite_red.setOnClickListener {
            if (favourite.getVisibility() == View.VISIBLE) {
                // Its visible
                favourite.visibility = View.GONE
                favourite_red.visibility = View.VISIBLE
                favourite_status.setText("true")
            } else {
                favourite.visibility = View.VISIBLE
                favourite_red.visibility = View.GONE
                favourite_status.setText("false")
                // Either gone or invisible
                //favourite.setImageResource(R.drawable.ic_favorite_red_24dp)
            }
            //linearLayoutForm.removeView(newView)
        }

        if (Global_Data.ShowTimesheetArr.size > 0) {
            MyLayoutOperation.addd(this)
            btn_addtask.visibility = View.INVISIBLE
        } else {
            linearLayoutForm.addView(newView)
            btn_addtask.visibility = View.VISIBLE
        }

//        if(dsf!!.length>0 && dsf1!!.length>0 && dsf2!!.length>0 && dsf3!!.length>0 && dsf4!!.length>0 && dsf5!!.length>0)
//        {
//            MyLayoutOperation.add(this, btn_addtask)
//        }

        //if(tmdetails_from.text.toString().length>0 && tmdetails_to.text.toString().length>0 && et_task.text.toString().length>0 && et_details1.text.toString().length>0 && et_details2.text.toString().length>0 && et_remark.text.toString().length>0)
//        if(Global_Data.date_arr.size>0)
//        {
        MyLayoutOperation.display(this, btn_submit)
//        }else{
//            Global_Data.Custom_Toast(Day_sheduler@this, "Please Enter Timesheet", "Yes")
//        }

//        if (tmdetails_from.text.toString().length > 0 && tmdetails_to.text.toString().length > 0 && et_task.text.toString().length > 0 && et_details1.text.toString().length > 0 && et_details2.text.toString().length > 0 && et_remark.text.toString().length > 0) {
//            val btnRemove = newView.findViewById<View>(R.id.btnRemove) as ImageButton
//            btnRemove.visibility = View.GONE
//        } else {
//            btnRemove.visibility = View.VISIBLE
//
//        }
        btn_addtask.setOnClickListener(View.OnClickListener {
            //favourite_arr.add(favStatus.toString())
            var fromdate =""
            var todate =""
            var taskname = ""
            var detail1 = ""
            var detail2 = ""
            var remarkd = ""
            val scrollViewlinerLayout = this@TimesheetDetailsActivity.findViewById<View>(com.msimplelogic.activities.R.id.linearLayoutForm) as LinearLayout
            for (i in 0 until scrollViewlinerLayout.childCount) {
                val innerLayout = scrollViewlinerLayout.getChildAt(i) as LinearLayout
                val from = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.tmdetails_from) as EditText
                val to = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.tmdetails_to) as EditText

                val task = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.et_task) as EditText
                val details1 = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.et_details1) as EditText

                val details2 = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.et_details2) as EditText
                val remark = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.et_remark) as EditText
                val fav_status = innerLayout.findViewById<View>(com.msimplelogic.activities.R.id.fav_status) as TextView

//                if(from_arr.contains(from.text.toString()))
//                {
//                    System.out.println("no")
//                }else{
//                    from_arr.add(from.text.toString())
//                }

                fromdate = from.text.toString()
                todate = to.text.toString()
                taskname = task.text.toString()
                detail1 = details1.text.toString()
                detail2 = details2.text.toString()
                remarkd = remark.text.toString()

            }
            dsf = tmdetails_from.text.toString()
            dsf1 = tmdetails_to.text.toString()
            dsf2 = et_task.text.toString()
            dsf3 = et_details1.text.toString()
            dsf4 = et_details2.text.toString()
            dsf5 = et_remark.text.toString()

            if (dsf!!.length > 0 && dsf1!!.length > 0 && dsf2!!.length > 0 && dsf3!!.length > 0 && dsf4!!.length > 0 && dsf5!!.length > 0 ||fromdate!!.length>0 && todate!!.length>0 && taskname!!.length>0 && detail1!!.length>0 && detail2!!.length>0 && remarkd!!.length>0  ) {

                if(dsf2!!.length > 0){



                if (from_arr.contains(dsf!!)) {
                    Global_Data.Custom_Toast(TimeSheetActivity@ this, "From time record already exist!", "Yes")

                } else {
                    from_arr.add(dsf!!)
                    val linearLayoutForm = this.findViewById<View>(R.id.linearLayoutForm) as LinearLayout
                    val newView = this.layoutInflater.inflate(R.layout.timesheetrowdetails, null) as LinearLayout
                    newView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    val btnRemove = newView.findViewById<View>(R.id.btnRemove) as ImageButton
                    btnRemove.setOnClickListener { linearLayoutForm.removeView(newView) }

                    val tmdetails_from = newView.findViewById<View>(R.id.tmdetails_from) as EditText
                    val tmdetails_to = newView.findViewById<View>(R.id.tmdetails_to) as EditText
                    et_task = newView.findViewById<View>(R.id.et_task) as EditText
                    et_details1 = newView.findViewById<View>(R.id.et_details1) as EditText
                    et_details2 = newView.findViewById<View>(R.id.et_details2) as EditText
                    et_remark = newView.findViewById<View>(R.id.et_remark) as EditText
                    favourite = newView.findViewById<View>(R.id.fav_img) as ImageView
                    favourite_red = newView.findViewById<View>(R.id.fav_img_red) as ImageView

                    var pickerfrom: TimePickerDialog

                    tmdetails_from.setOnClickListener(View.OnClickListener {
                        val cldr = Calendar.getInstance()
                        val hour = cldr[Calendar.HOUR_OF_DAY]
                        val minutes = cldr[Calendar.MINUTE]
                        // time picker dialog
                        pickerfrom = TimePickerDialog(this,
                                object : TimePickerDialog.OnTimeSetListener {
                                    override fun onTimeSet(tp: TimePicker?, sHour: Int, sMinute: Int) {

                                        tmdetails_from.setText("$sHour:$sMinute")
                                        dsf = "$sHour:$sMinute"

                                    }
                                }, hour, minutes, true)
                        pickerfrom.show()
                    })

                    var pickerto: TimePickerDialog
                    tmdetails_to.setOnClickListener(View.OnClickListener {
                        val cldr = Calendar.getInstance()
                        val hour = cldr[Calendar.HOUR_OF_DAY]
                        val minutes = cldr[Calendar.MINUTE]
                        // time picker dialog
                        pickerto = TimePickerDialog(this,
                                object : TimePickerDialog.OnTimeSetListener {
                                    override fun onTimeSet(tp: TimePicker?, sHour: Int, sMinute: Int) {
                                        if(tmdetails_from.length()>0)
                                        {
                                           // tmdetails_to.setText("$sHour:$sMinute")
                                            val date = Date()
                                            val dateFormat = SimpleDateFormat("HH:mm")
                                            dateFormat.format(date)

                                            val fromtime=dateFormat.parse(tmdetails_from.text.toString())
                                            val totime=dateFormat.parse("$sHour:$sMinute")

                                            if ((fromtime).after(totime)) {
                                                Global_Data.Custom_Toast(applicationContext, "To Time should be Greater than From Time", "Yes")
                                            } else {
                                                tmdetails_to.setText("$sHour:$sMinute")
                                            }


                                        }else{
                                            Global_Data.Custom_Toast(applicationContext, "Please Select From Time", "Yes")
                                        }


                                    }
                                }, hour, minutes, true)
                        pickerto.show()
                    })

                    favourite.setOnClickListener {
                        if (favourite.getVisibility() == View.VISIBLE) {
                            // Its visible
                            favourite.visibility = View.GONE
                            favourite_red.visibility = View.VISIBLE
                            favourite_status.setText("true")
                        } else {
                            favourite.visibility = View.VISIBLE
                            favourite_red.visibility = View.GONE
                            favourite_status.setText("false")
                            // Either gone or invisible
                            //favourite.setImageResource(R.drawable.ic_favorite_red_24dp)
                        }
                        //linearLayoutForm.removeView(newView)
                    }

                    favourite_red.setOnClickListener {
                        if (favourite.getVisibility() == View.VISIBLE) {
                            // Its visible
                            favourite.visibility = View.GONE
                            favourite_red.visibility = View.VISIBLE
                            favourite_status.setText("true")
                        } else {
                            favourite.visibility = View.VISIBLE
                            favourite_red.visibility = View.GONE
                            favourite_status.setText("false")
                            // Either gone or invisible
                            //favourite.setImageResource(R.drawable.ic_favorite_red_24dp)
                        }
                        //linearLayoutForm.removeView(newView)
                    }
                    linearLayoutForm.addView(newView)
                    dsf = ""
                    dsf1 = ""
                    dsf2 = ""
                    dsf3 = ""
                    dsf4 = ""
                    dsf5 = ""
                }

                }else{
                    Global_Data.Custom_Toast(applicationContext@ this, "Please Enter Task", "Yes")

                }
            } else {
                Global_Data.Custom_Toast(applicationContext@ this, "Please Enter Timesheet", "Yes")
            }
        })

        val selectedDates = mutableSetOf<LocalDate>()
        val today = LocalDate.now()
        val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
        legendLayout = findViewById(R.id.legendLayout)
        val daysOfWeek = daysOfWeekFromLocale()
        legendLayout?.children?.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase(Locale.ENGLISH)
                setTextColorRes(R.color.example_1_white_light)
            }
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(0)
        val endMonth = currentMonth.plusMonths(10)
        exOneCalendar.setup(startMonth, endMonth, daysOfWeek.first())
        exOneCalendar.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay

            val textView = view.exOneDayText

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDates.contains(day.date)) {
                            selectedDates.remove(day.date)
                        } else {
                            selectedDates.add(day.date)
                        }
                        exOneCalendar.notifyDayChanged(day)
                    }
                }
            }
        }

        exOneCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    when {
                        selectedDates.contains(day.date) -> {
                            textView.setTextColorRes(R.color.example_1_bg)
                            textView.setBackgroundResource(R.drawable.example_1_selected_bg)

                            Global_Data.date_arr.add(day.date.dayOfMonth.toString() + "-" + day.date.monthValue.toString() + "-" + day.date.year.toString())
                            //Toast.makeText(this@TimesheetDetailsActivity, "Date:" +Global_Data.date_arr, Toast.LENGTH_SHORT).show()

                        }
                        today == day.date -> {
                            textView.setTextColorRes(R.color.black)
                            textView.setBackgroundResource(R.drawable.example_1_today_bg)
                        }
                        else -> {
                            textView.setTextColorRes(R.color.example_1_white)
                            textView.background = null
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.black)
                    textView.background = null
                }
            }
        }

        exOneCalendar.monthScrollListener = {
            if (exOneCalendar.maxRowCount == 6) {
                exOneYearText.text = it.yearMonth.year.toString()
                exOneMonthText.text = monthTitleFormatter.format(it.yearMonth)
            } else {
                // In week mode, we show the header a bit differently.
                // We show indices with dates from different months since
                // dates overflow and cells in one index can belong to different
                // months/years.
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    exOneYearText.text = firstDate.yearMonth.year.toString()
                    exOneMonthText.text = monthTitleFormatter.format(firstDate)
                } else {
                    exOneMonthText.text =
                            "${monthTitleFormatter.format(firstDate)} - ${monthTitleFormatter.format(lastDate)}"
                    if (firstDate.year == lastDate.year) {
                        exOneYearText.text = firstDate.yearMonth.year.toString()
                    } else {
                        exOneYearText.text = "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                    }
                }
            }
        }

        weekModeCheckBox.setOnCheckedChangeListener { _, monthToWeek ->
            val firstDate = exOneCalendar.findFirstVisibleDay()?.date
                    ?: return@setOnCheckedChangeListener
            val lastDate = exOneCalendar.findLastVisibleDay()?.date
                    ?: return@setOnCheckedChangeListener

            val oneWeekHeight = exOneCalendar.dayHeight
            val oneMonthHeight = oneWeekHeight * 6

            val oldHeight = if (monthToWeek) oneMonthHeight else oneWeekHeight
            val newHeight = if (monthToWeek) oneWeekHeight else oneMonthHeight

            // Animate calendar height changes.
            val animator = ValueAnimator.ofInt(oldHeight, newHeight)
            animator.addUpdateListener { animator ->
                exOneCalendar.layoutParams = exOneCalendar.layoutParams.apply {
                    height = animator.animatedValue as Int
                }
            }
            // When changing from month to week mode, we change the calendar's
            // config at the end of the animation(doOnEnd) but when changing
            // from week to month mode, we change the calendar's config at
            // the start of the animation(doOnStart). This is so that the change
            // in height is visible. You can do this whichever way you prefer.

            animator.doOnStart {
                if (!monthToWeek) {
                    exOneCalendar.inDateStyle = InDateStyle.ALL_MONTHS
                    exOneCalendar.maxRowCount = 6
                    exOneCalendar.hasBoundaries = true
                }
            }
            animator.doOnEnd {
                if (monthToWeek) {
                    exOneCalendar.inDateStyle = InDateStyle.FIRST_MONTH
                    exOneCalendar.maxRowCount = 1
                    exOneCalendar.hasBoundaries = false
                }

                if (monthToWeek) {
                    // We want the first visible day to remain
                    // visible when we change to week mode.
                    exOneCalendar.scrollToDate(firstDate)
                } else {
                    // When changing to month mode, we choose current
                    // month if it is the only one in the current frame.
                    // if we have multiple months in one frame, we prefer
                    // the second one unless it's an outDate in the last index.
                    if (firstDate.yearMonth == lastDate.yearMonth) {
                        exOneCalendar.scrollToMonth(firstDate.yearMonth)
                    } else {
                        exOneCalendar.scrollToMonth(minOf(firstDate.yearMonth.next, endMonth))
                    }
                }
            }
            animator.duration = 250
            animator.start()
        }

        btn_copytimesheet.setOnClickListener(View.OnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.copytimesheet_dialog, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                    .setTitle("TimeSheet")
            //show dialog
            val mAlertDialog = mBuilder.show()

            myCalendar = Calendar.getInstance()
            date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar?.set(Calendar.YEAR, year)
                myCalendar?.set(Calendar.MONTH, monthOfYear)
                myCalendar?.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd-MM-yyyy" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.US)

                mDialogView.copy_select_date?.setText(sdf.format(myCalendar!!.time))

                //updateLabel()
            }

            mDialogView.copy_select_date.setOnClickListener {
                DatePickerDialog(this@TimesheetDetailsActivity, date, myCalendar!!.get(Calendar.YEAR), myCalendar!!.get(Calendar.MONTH),
                        myCalendar!!.get(Calendar.DAY_OF_MONTH)).show()
            }

            //login button click of custom layout
            mDialogView.btn_copyok.setOnClickListener {
                //dismiss dialog
                //mAlertDialog.dismiss()

                dialog = ProgressDialog(Day_sheduler@ this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                isInternetPresent = cd!!.isConnectingToInternet

                if (isInternetPresent) {
                    if (mDialogView.copy_select_date.text.toString().length > 0 && Global_Data.copyTimesheetRadioValue.length > 0) {
                        GetTimesheetResult(mDialogView.copy_select_date.text.toString(), Global_Data.copyTimesheetRadioValue)
                    } else {
                        Global_Data.Custom_Toast(TimeSheetActivity@ this, "Please Select date and type", "Yes")
                    }
                } else {
                    mAlertDialog.dismiss()
                    Global_Data.Custom_Toast(TimeSheetActivity@ this, resources.getString(R.string.internet_connection_error), "Yes")
                }
            }
        })
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked
            val checked = view.isChecked
            //   val radio: RadioButton = findViewById(checkedId)
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_fromtimesheet ->
                    if (checked) {
                        Global_Data.copyTimesheetRadioValue = "copy_from_existing"
                        //Global_Data.Custom_Toast(this@TimesheetDetailsActivity,"Copy from existing timesheet","")
                    }
                R.id.radio_fromfavourite ->
                    if (checked) {
                        Global_Data.copyTimesheetRadioValue = "copy_from_favourite"
                        //Global_Data.Custom_Toast(this@TimesheetDetailsActivity,"Copy from favourite","")
                    }
            }
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
                val sp: SharedPreferences = this@TimesheetDetailsActivity.getSharedPreferences("SimpleLogic", 0)
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
//super.onBackPressed();
        Global_Data.ShowTimesheetArr.clear()
        Global_Data.date_arr.clear()
        val i = Intent(this@TimesheetDetailsActivity, TimeSheetActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i)
        finish()
    }

    override fun updateMonthOnScroll(selectedDate: DayDateMonthYearModel?) {
        currentMonthTextView.text = "" + selectedDate?.month + " " + selectedDate?.year

    }

    override fun newDateSelected(selectedDate: DayDateMonthYearModel?) {
        //Toast.makeText(this@TimesheetDetailsActivity ,selectedDate?.date +""+ selectedDate?.month + " " + selectedDate?.year , Toast.LENGTH_LONG).show()
    }

    fun GetTimesheetResult(date1: String, copy_from: String) {
        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()

        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null)!!

        val Cust_domain = sp.getString("Cust_Service_Url", "")
        var service_url = Cust_domain + "metal/api/v1/"

        try {
            service_url = service_url + "time_sheets/get_timesheet_for_copy?email=" + (URLEncoder.encode(user_email, "UTF-8")) + "&copy_date=" + date1 + "&type=" + copy_from

            //service_url = "http://827bdc44.ngrok.io/metal/api/v1/time_sheets/get_timesheet_for_copy?email=sujitkumar.giradkar@simplelogic.in&copy_date=30-01-2020&type=copy_from_existing"

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
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
                            if (json.has("result")) {
                                response_result = json.getString("result")

                                dialog!!.hide()
                                Global_Data.Custom_Toast(this, resources.getString(R.string.Sorry_No_Record_Found), "Yes")
                                //finish().toString()
                                //finish()
                            } else {

                                val summaryData = json.getJSONArray("time_sheet")
                                Log.i("volley", "response visits Length: " + summaryData.length())
                                Log.d("users", "visite_schedules$summaryData")
                                Global_Data.ShowTimesheetArr?.clear()
                                if (summaryData.length() > 0) {

                                    Global_Data.ShowTimesheetArr = ArrayList<TimeSheetShowModel>()
                                    for (i in 0 until summaryData.length()) {
                                        val jsonObject = summaryData.getJSONObject(i)
                                        Global_Data.ShowTimesheetArr!!.add(TimeSheetShowModel(jsonObject.getString("from_time"), jsonObject.getString("to_time"), jsonObject.getString("task_title"), jsonObject.getString("details1"), jsonObject.getString("details2"), jsonObject.getString("remark"), jsonObject.getString("is_favourite")))
                                    }
                                    adapter = AdapterTimesheet(this, (Global_Data.ShowTimesheetArr as ArrayList<TimeSheetModel>)!!)
                                    timesheet_list?.adapter = adapter
                                    dialog!!.dismiss()

                                    Global_Data.copyTimesheetRadioValue = ""
                                    //finish()

                                    startActivity(Intent(this, TimesheetDetailsActivity::class.java))

                                } else {
                                    Global_Data.Custom_Toast(this, resources.getString(R.string.Sorry_No_Record_Found), "Yes")
                                    adapter!!.notifyDataSetChanged()
                                    dialog!!.dismiss()
                                }
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
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
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                "Network Error","yes")
                    } else if (error is AuthFailureError) {
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                "Server AuthFailureError  Error","yes")
                    } else if (error is ServerError) {
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                resources.getString(R.string.Server_Errors),"yes")
                    } else if (error is NetworkError) {
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                "Network Error","yes")
                    } else if (error is ParseError) {
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                "ParseError   Error","yes")
                    } else {
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                error.message,"yes")
                    }
                    //						Intent launch = new Intent(Video_Main_List.this,MainActivity.class);
//						startActivity(launch);
                    finish()
                    dialog!!.dismiss()
                    // finish();
                })
        val requestQueue = Volley.newRequestQueue(QuoteAddRetailerTrait@ this)
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