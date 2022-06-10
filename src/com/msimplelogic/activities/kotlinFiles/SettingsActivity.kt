package com.msimplelogic.activities.kotlinFiles

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.MainActivity
import com.msimplelogic.activities.R
import com.msimplelogic.adapter.ThemeAdapter
import com.msimplelogic.helper.ThemeUtil
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_beat_selection.*
import kotlinx.android.synthetic.main.activity_settings.*

const val KEY_CURRENT_THEME = "current_theme"
const val LILAC_THEME = "lilac"
const val MAINTHEME = "main"

class SettingsActivity : BaseActivity() {
    private var mAdapter: ThemeAdapter? = null
    var context : Context? = null;
    var mTheme = ThemeUtil.THEME_BLUE
    var mIsNightMode = false
    var sp: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_activity_main)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)   //show back button
        setTitle("")

        context = SettingsActivity@this

        val spref: SharedPreferences = getSharedPreferences("SimpleLogic", 0)
        val currentTheme = spref.getString(KEY_CURRENT_THEME, LILAC_THEME)


        mAdapter = ThemeAdapter(Global_Data.mThemeList,context)

        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 4)
        themerecyclerView!!.layoutManager = mLayoutManager
        themerecyclerView.itemAnimator = DefaultItemAnimator()
        themerecyclerView.adapter = mAdapter

        prepareThemeData()

        try {
            sp = getSharedPreferences("SimpleLogic", 0)
            val current_theme = sp!!.getInt("CurrentTheme", 0)
            theme_selected.setTheme(Global_Data.mThemeList.get(current_theme));
        } catch (ex: Exception) {
            theme_selected.setTheme(Global_Data.mThemeList.get(0));
            ex.printStackTrace()
        }




    }

    private fun prepareThemeData() {
        Global_Data.mThemeList.clear()
        Global_Data.mThemeList.addAll(ThemeUtil.getThemeList())
        mAdapter!!.notifyDataSetChanged()
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
                val sp = this@SettingsActivity.getSharedPreferences("SimpleLogic", 0)
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

        val i = Intent(this@SettingsActivity, MainActivity::class.java)
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