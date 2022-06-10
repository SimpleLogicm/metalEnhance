package com.msimplelogic.activities.kotlinFiles

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.msimplelogic.activities.*
import com.msimplelogic.webservice.ConnectionDetector
import kotlinx.android.synthetic.main.activity_beat_selection.*

class Contact_Us : BaseActivity(), OnItemSelectedListener {
    //Button retail_sales, institute_sales;
    var dbvoc = DataBaseHelper(this)
    var url = ""
    var phonenext: TextView? = null
    var phonenext2: TextView? = null
    var email1: TextView? = null
    var email2: TextView? = null
    var webtext: TextView? = null
    var title: TextView? = null
    var header: TextView? = null
    var sub_heading: TextView? = null
    var address: TextView? = null
    var blob_data_logo: Bitmap? = null
    var cd: ConnectionDetector? = null
    var isInternetPresent = false
    var thumbnail: ImageView? = null
    private val mWebview: WebView? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_us_main)

        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")

        title = findViewById<View>(R.id.Metal_Header) as TextView
        header = findViewById<View>(R.id.Metal_Header_descri) as TextView
        sub_heading = findViewById<View>(R.id.title) as TextView
        address = findViewById<View>(R.id.discriptionnew) as TextView
        phonenext = findViewById<View>(R.id.phonenext) as TextView
        phonenext2 = findViewById<View>(R.id.phonenext2) as TextView
        email1 = findViewById<View>(R.id.email1) as TextView
        email2 = findViewById<View>(R.id.email2) as TextView
        webtext = findViewById<View>(R.id.webtext) as TextView
        thumbnail = findViewById<View>(R.id.thumbnail) as ImageView
        val spf1 = getSharedPreferences("SimpleLogic", 0)
        val logostr = spf1.getString("logo_data", "")
        if (logostr!!.length > 0) {
            val decodedString = Base64.decode(logostr, Base64.DEFAULT)
            blob_data_logo = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            thumbnail!!.setImageBitmap(blob_data_logo)
        }
        val contacts1 = dbvoc.l1_Contact
        if (contacts1.size > 0) {
            for (cn in contacts1) {
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.title)) {
                    title!!.text = cn.title
                } else {
                    title!!.visibility = View.GONE
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.heading)) {
                    header!!.text = cn.heading
                } else {
                    header!!.visibility = View.GONE
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.sub_Heading)) {
                    sub_heading!!.text = cn.sub_Heading
                } else {
                    sub_heading!!.visibility = View.GONE
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.l1_Address)) {
                    val addressn = "<b>" + resources.getString(R.string.Address) + "</b> " + cn.l1_Address
                    address!!.text = Html.fromHtml(addressn)
                } else {
                    address!!.visibility = View.GONE
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.l1_phone1)) {
                    phonenext!!.text = cn.l1_phone1
                } else {
                    phonenext!!.text = ""
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.l1_phone2)) {
                    phonenext2!!.text = cn.l1_phone2
                } else {
                    phonenext2!!.text = ""
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.l1_email_id1)) {
                    email1!!.text = cn.l1_email_id1
                } else {
                    email1!!.text = ""
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.l1_email_id2)) {
                    email2!!.text = cn.l1_email_id2
                } else {
                    email2!!.text = ""
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.l1_website)) { //webtext.setText(cn.getL1_website());
                    url = cn.l1_website
                } else {
                    webtext!!.visibility = View.GONE
                    url = ""
                }
            }
        } else {
          //  Toast.makeText(this, resources.getString(R.string.Contact_not_found), Toast.LENGTH_SHORT).show()
            Global_Data.Custom_Toast(this, resources.getString(R.string.Contact_not_found),"yes")
            val i = Intent(this@Contact_Us, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            startActivity(i)
            finish()
        }
        cd = ConnectionDetector(applicationContext)
        phonenext!!.setOnClickListener { requestPhoneCallPermission(phonenext!!.text.toString().trim { it <= ' ' }) }
        webtext!!.setOnClickListener {
            Global_Data.Web_view_back = resources.getString(R.string.Contact)
            if (url.trim { it <= ' ' }.indexOf(":") > 0) {
                Global_Data.Web_view_url = url.trim { it <= ' ' }
            } else { //  myWebView.loadUrl("http://"+Global_Data.Web_view_url);
                Global_Data.Web_view_url = "http://" + url.trim { it <= ' ' } + "/"
            }
            Global_Data.Web_view_Title = title!!.text.toString().trim { it <= ' ' }
            val i = Intent(this@Contact_Us, Webview_Activity::class.java)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() { // TODO Auto-generated method stub
//super.onBackPressed();
        val i = Intent(this@Contact_Us, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@Contact_Us)
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
}