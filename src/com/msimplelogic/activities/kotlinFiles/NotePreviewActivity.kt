package com.msimplelogic.activities.kotlinFiles

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.R
import kotlinx.android.synthetic.main.activity_note_preview.*
import kotlinx.android.synthetic.main.content_note_preview.*
import java.text.DateFormat
import java.text.SimpleDateFormat


class NotePreviewActivity : BaseActivity() {
    var order_toolbar_title: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_preview)
        setSupportActionBar(toolbar)

        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        order_toolbar_title = findViewById(R.id.order_toolbar_title)

        val intent = intent
        var bundle: Bundle? = intent.extras
        var dates = bundle!!.getString("dates") // 1
        var notes = bundle!!.getString("notes") // 2

//        supportActionBar!!.setTitle(getFormattedDate(myEventDay.getCalendar().getTime()))
        val string = dates

        val parser = SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy")
        val date = parser.parse(dates)
        val format: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val formattedDate = format.format(date)

//        supportActionBar!!.setTitle(formattedDate)
        order_toolbar_title!!.setText(formattedDate)
        note.setText(notes)


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

        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {

        val i = Intent(applicationContext, ShiftRoaster::class.java)
        startActivity(i)
        super.onBackPressed()

    }


}
