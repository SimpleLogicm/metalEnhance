package com.msimplelogic.activities.kotlinFiles

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msimplelogic.DataClass.BeatDataModel
import com.msimplelogic.activities.*
import com.msimplelogic.adapter.BeatSelectionAdapter
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_beat_selection.*
import kotlinx.android.synthetic.main.content_beat_selection.*

class BeatSelection : BaseActivity() {
    private var imageModelArrayList: ArrayList<BeatDataModel>? = null
    private var adapter: BeatSelectionAdapter? = null
    var dbvoc = DataBaseHelper(this)
    var beatcount : Int = 0;
    var customerCount : Int = 0;
    var sp:SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beat_selection)

        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)   //show back button
        setTitle("")
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
            sortIcon.setImageResource(R.drawable.sortby_ordertype_dark)
            fitericon.setImageResource(R.drawable.filterordertype_icon_dark)

        }

        imageModelArrayList = populateList()
        adapter = BeatSelectionAdapter(this, imageModelArrayList!!)
        beat_recycler!!.adapter = adapter
        beat_recycler!!.layoutManager = GridLayoutManager(this, 2) as RecyclerView.LayoutManager?;

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
    }

    private fun populateList(): ArrayList<BeatDataModel> {

        val list = ArrayList<BeatDataModel>()

        val contacts2: List<Local_Data> = dbvoc.getAllBeats()
        //results.add("Select Beat");
        //results.add("Select Beat");
        for (cn in contacts2) {
            val str_beat = "" + cn.stateName
            val imageModel = BeatDataModel(cn.stateName, "", cn.code);
            list.add(imageModel)
            beatcount++
        }

        val contacts3 = dbvoc.allCustomer
        for (cn in contacts3) {
            customerCount++
        }

        main_tv.text = this@BeatSelection.resources.getString(R.string.Beat)+" : $beatcount      " + this@BeatSelection.resources.getString(R.string.Customer)+ ": $customerCount"
        dbvoc.close()

//        for (i in 0..7) {
//            val imageModel = BeatDataModel(myImageNameList[i],"");
//            list.add(imageModel)
//        }

        return list
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp = this@BeatSelection.getSharedPreferences("SimpleLogic", 0)
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

                //val yourView = findViewById(R.id.add)
                val yourView = findViewById<View>(R.id.add)
                SimpleTooltip.Builder(this)
                        .anchorView(yourView)
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
        // TODO Auto-generated method stub
        //super.onBackPressed();

        val i = Intent(this@BeatSelection, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

}
