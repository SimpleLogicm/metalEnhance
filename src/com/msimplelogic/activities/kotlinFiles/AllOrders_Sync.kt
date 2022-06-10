package com.msimplelogic.activities.kotlinFiles

import android.app.ActivityManager
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import com.msimplelogic.activities.*
import com.msimplelogic.services.getServices
import com.msimplelogic.webservice.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_beat_selection.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by admin on 06-09-2016.
 */
class AllOrders_Sync : BaseActivity() {
    var dialogClear: ProgressDialog? = null
    var isInternetPresent = false
    var loginDataBaseAdapter: LoginDataBaseAdapter? = null
    var cd: ConnectionDetector? = null
    var angry_btn1: Button? = null
    var angry_btn2: Button? = null
    var angry_btn3: Button? = null
    var angry_btn4: Button? = null
    var clearData: Button? = null
    var lastsyncon: TextView? = null
    var Totalsosync: TextView? = null
    var dbvoc = DataBaseHelper(this)
    private var Current_Date = ""
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sync_all_main)

        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        lastsyncon = findViewById<View>(R.id.lastsyncon) as TextView
        Totalsosync = findViewById<View>(R.id.Totalsosync) as TextView
        angry_btn1 = findViewById<View>(R.id.angry_btn1) as Button
        angry_btn2 = findViewById<View>(R.id.angry_btn2) as Button
        angry_btn3 = findViewById<View>(R.id.angry_btn3) as Button
        angry_btn4 = findViewById<View>(R.id.angry_btn4) as Button
        clearData = findViewById<View>(R.id.clear_data) as Button
        cd = ConnectionDetector(this@AllOrders_Sync)
        loginDataBaseAdapter = LoginDataBaseAdapter(this)
        loginDataBaseAdapter = loginDataBaseAdapter!!.open()
        dialogClear = ProgressDialog(this@AllOrders_Sync, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd MMMM yyyy")
        val strDate = sdf.format(c.time)
        Current_Date = sdf.format(c.time)
        val contacts1 = dbvoc.order_details
        for (cn in contacts1) {
            lastsyncon!!.text = ""+ cn.getsync_time()
            Totalsosync!!.text = ""+ cn.gettotal_sync()
        }

       if(Global_Data.order_pushflag.equals("yes",ignoreCase = false))
       {
           isInternetPresent = cd!!.isConnectingToInternet
           if (isInternetPresent) {
               getServices.SYNCORDER_AllOrders(this@AllOrders_Sync)
           } else {
//               val toast = Toast.makeText(applicationContext, resources.getString(R.string.internet_connection_error), Toast.LENGTH_LONG)
//               toast.setGravity(Gravity.CENTER, 0, 0)
//               toast.show()
               Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error),"yes")
           }
       }
        angry_btn1!!.setOnClickListener {
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                getServices.SYNCORDER_AllOrders(this@AllOrders_Sync)
            } else {
//                val toast = Toast.makeText(applicationContext, resources.getString(R.string.internet_connection_error), Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.CENTER, 0, 0)
//                toast.show()
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error),"yes")
            }
        }
        angry_btn2!!.setOnClickListener {
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                Global_Data.sync="yes"
                getServices.sendRequestnew(this@AllOrders_Sync, resources.getString(R.string.please_wait_dialog_messagen))
            } else {
//                val toast = Toast.makeText(applicationContext, resources.getString(R.string.internet_connection_error), Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.CENTER, 0, 0)
//                toast.show()
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error),"yes")
            }
        }
        angry_btn3!!.setOnClickListener {
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                getServices.SyncDataToServercommon(this@AllOrders_Sync)
            } else {
//                val toast = Toast.makeText(applicationContext, resources.getString(R.string.internet_connection_error), Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.CENTER, 0, 0)
//                toast.show()
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error),"yes")
            }
        }
        angry_btn4!!.setOnClickListener {
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                getServices.SyncDataToServerCustomer(this@AllOrders_Sync)
            } else {
//                val toast = Toast.makeText(applicationContext, resources.getString(R.string.internet_connection_error), Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.CENTER, 0, 0)
//                toast.show()
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.internet_connection_error),"yes")
            }
        }
        clearData!!.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this@AllOrders_Sync).create() //Read Update
            alertDialog.setTitle(resources.getString(R.string.Confirmation))
            alertDialog.setMessage(resources.getString(R.string.Clear_data_warning_message))
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, resources.getString(R.string.Yes)) { dialog, which ->
                // TODO Auto-generated method stub
                dialogClear!!.setMessage(resources.getString(R.string.url_athentication_check_messagen))
                dialogClear!!.setTitle(resources.getString(R.string.app_name))
                dialogClear!!.setCancelable(false)
                dialogClear!!.show()
                clearapplicationdata()
            }
            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, resources.getString(R.string.No_Button_label)) { dialog, which ->
                // TODO Auto-generated method stub
                dialog.cancel()
            }
            alertDialog.setCancelable(false)
            alertDialog.show()
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
                val sp = this@AllOrders_Sync.getSharedPreferences("SimpleLogic", 0)
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
//        val i = Intent(this@AllOrders_Sync, MainActivity::class.java)
//        startActivity(i)

        val mngr = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val taskList = mngr.getRunningTasks(10)

        if (taskList[0].numActivities == 1 && taskList[0].topActivity!!.className == this.javaClass.name) {
            Log.i("Last Activity", "This is last activity in the stack")
            val i = Intent(this@AllOrders_Sync, SplashScreenActivity::class.java)
            startActivity(i)
            finish()
        }
        else
        {
            finish()
        }

    }

    private fun clearapplicationdata() {
        val cacheDirectory = cacheDir
        val applicationDirectory = File(cacheDirectory.parent)
        if (applicationDirectory.exists()) {
            val fileNames = applicationDirectory.list()
            for (fileName in fileNames) {
                if (fileName != "lib") {
                    deleteFile(File(applicationDirectory, fileName))
                    val intent = Intent(this@AllOrders_Sync, SplashScreenActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    startActivity(intent)
                    stopService(Intent(this@AllOrders_Sync, LocationServices::class.java))
                    finishAffinity()
                    finish()
                    dialogClear!!.dismiss()
                }
            }
        }
    }

    private fun deleteFile(file: File?): Boolean {
        var deletedAll = true
        if (file != null) {
            if (file.isDirectory) {
                val children = file.list()
                for (i in children.indices) {
                    deletedAll = deleteFile(File(file, children[i])) && deletedAll
                }
            } else {
                deletedAll = file.delete()
            }
        }
        return deletedAll
    }
}