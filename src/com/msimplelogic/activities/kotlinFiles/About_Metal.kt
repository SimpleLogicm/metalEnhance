package com.msimplelogic.activities.kotlinFiles

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.msimplelogic.activities.*
import com.msimplelogic.webservice.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.about_metal.*
import kotlinx.android.synthetic.main.activity_beat_selection.*

class About_Metal : BaseActivity(), OnItemSelectedListener {

    var blob_data_logo: Bitmap? = null
    var cd: ConnectionDetector? = null
    var isInternetPresent = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_metal_main)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //setTitle("")

        cd = ConnectionDetector(applicationContext)
        phonenext!!.setOnClickListener { requestPhoneCallPermission(phonenext!!.text.toString().trim { it <= ' ' }) }
        webtext!!.setOnClickListener {
            val i = Intent(this@About_Metal, Webview_Activity::class.java)
            Global_Data.Web_view_back = resources.getString(R.string.About)
            Global_Data.Web_view_url = "http://www.simplelogic.in"
            Global_Data.Web_view_Title = resources.getString(R.string.Simple_Logic)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            startActivity(i)
            finish()
        }

        phonenext2!!.setOnClickListener { requestPhoneCallPermission(phonenext2!!.text.toString().trim { it <= ' ' }) }
        email1!!.setOnClickListener {
            try {
                try {
                    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", email1!!.text.toString().trim { it <= ' ' }, null))
                    // emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text");
                    startActivity(Intent.createChooser(emailIntent, null))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        email2!!.setOnClickListener {
            try {
                try {
                    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", email2!!.text.toString().trim { it <= ' ' }, null))
                    // emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text");
                    startActivity(Intent.createChooser(emailIntent, null))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    override fun onItemSelected(arg0: AdapterView<*>?, arg1: View, arg2: Int,
                                arg3: Long) { // TODO Auto-generated method stub
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) { // TODO Auto-generated method stub
    }

    override fun onBackPressed() { // TODO Auto-generated method stub
        val i = Intent(this@About_Metal, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        finish()
    }

    private fun requestPhoneCallPermission(mobile_number: String) {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:$mobile_number")
                        startActivity(callIntent)
                        return
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) { // check for permanent denial of permission
                        if (response.isPermanentlyDenied) {
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@About_Metal)
        builder.setTitle(resources.getString(R.string.need_permission_message))
        builder.setCancelable(false)
        builder.setMessage(resources.getString(R.string.need_permission_setting_message))
        builder.setPositiveButton(resources.getString(R.string.GOTO_SETTINGS)) { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(resources.getString(R.string.Cancel)) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.msimplelogic.activities.R.menu.main, menu)
        return true
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
            com.msimplelogic.activities.R.id.add -> {
                var targetNew = ""
                val sp = this@About_Metal.getSharedPreferences("SimpleLogic", 0)
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

                val view: View = findViewById(com.msimplelogic.activities.R.id.add)
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

