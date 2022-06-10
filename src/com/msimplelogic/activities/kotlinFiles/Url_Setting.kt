package com.msimplelogic.activities.kotlinFiles

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.URLUtil
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.LocationServices
import com.msimplelogic.activities.*
import com.msimplelogic.webservice.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_beat_selection.*
import kotlinx.android.synthetic.main.url_setting.*
import org.json.JSONException
import java.io.File
import java.net.URLEncoder
import java.util.*

class Url_Setting : BaseActivity() {
    var devid: String? = null
    var str_email: String? = null
    var dialog: ProgressDialog? = null
    var dialogClear: ProgressDialog? = null
    var dbvoc = DataBaseHelper(this)
    var loginDataBaseAdapter: LoginDataBaseAdapter? = null
    var url = ""
    var progress: ProgressDialog? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var url_hints = ArrayList<String>()
    var sp: SharedPreferences? = null
    private val Language_List = ArrayList<String>()
    var LanguageAdapter: ArrayAdapter<String>? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.url_setting_main)

        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")


        edit_url!!.setSelection(edit_url!!.getText().length)
        loginDataBaseAdapter = LoginDataBaseAdapter(this)
        loginDataBaseAdapter = loginDataBaseAdapter!!.open()
        dialog = ProgressDialog(this@Url_Setting, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        dialogClear = ProgressDialog(this@Url_Setting, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        cd = ConnectionDetector(applicationContext)
        sp = getSharedPreferences("SimpleLogic", 0)
        val hasURL = sp?.contains("Cust_Service_Url")
        if (hasURL == true) {
            val Cust_Service_Url = sp?.getString("Cust_Service_Url", "")
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Cust_Service_Url)) {
                edit_url?.setText(Cust_Service_Url)
                edit_url?.setFocusable(false)
                edit_url?.setCursorVisible(false)
                // edit_url.setClickable(false);
                urls_btn!!.setVisibility(View.GONE)
            }
        }
        url_hints.clear()
        url_hints.add("http://")
        url_hints.add("https://")
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                url_hints)
        edit_url!!.setThreshold(1) // will start working from
        // first character
        edit_url!!.setAdapter(adapter) // setting the adapter
        // data into the
// AutoCompleteTextView
        edit_url!!.setTextColor(Color.BLACK)
        TmLanguage!!.setOnClickListener(View.OnClickListener { Language_Select_Dialog() })
        edit_url!!.setOnClickListener(View.OnClickListener {
            val be = edit_url!!.isFocusable()
            if (be == false) {
                val builder = AlertDialog.Builder(this@Url_Setting)
                builder.setMessage(resources.getString(R.string.server_url_dilog_title))
                        .setCancelable(false)
                        .setPositiveButton(resources.getString(R.string.OK_Button_label)) { dialog, id ->
                            dialogClear!!.setMessage(resources.getString(R.string.url_athentication_check_messagen))
                            dialogClear!!.setTitle(resources.getString(R.string.app_name))
                            dialogClear!!.setCancelable(false)
                            dialogClear!!.show()
                            clearapplicationdata()
                        }
                        .setNegativeButton(resources.getString(R.string.No_Button_label)) { dialog, id -> dialog.cancel() }
                //Creating dialog box
                val alert = builder.create()
                //Setting the title manually
                alert.setTitle(resources.getString(R.string.app_name))
                alert.show()
            }
        })
        urls_btn!!.setOnClickListener(View.OnClickListener {
            url = edit_url?.getText().toString().trim { it <= ' ' }
            edit_url?.setText(url.trim { it <= ' ' })
            edit_url?.setSelection(edit_url!!.getText().length)
            val url_check = URLUtil.isValidUrl(url)
            Log.d("ULEN", "ULEN " + edit_url!!.length())
            if (url_check == false) {
                Global_Data.Custom_Toast(this@Url_Setting, resources.getString(R.string.url_validation_message),"yes")
            } else {
                isInternetPresent = cd!!.isConnectingToInternet
                if (isInternetPresent) {
                    urls_btn!!.setEnabled(false)
                    urls_btn!!.setClickable(false)
                    dialog!!.setMessage(resources.getString(R.string.url_athentication_check_messagen))
                    dialog!!.setTitle(resources.getString(R.string.app_name))
                    dialog!!.setCancelable(false)
                    dialog!!.show()
                    checkurl_Tast().execute()
                } else {

                    Global_Data.Custom_Toast(this@Url_Setting, resources.getString(R.string.internet_connection_error),"yes")
                }
            }
        })
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                onBackPressed()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//           case android.R.id.home:
//                //this.finish();
//                onBackPressed();
//                Global_Data.CatlogueStatus = "";
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//           case android.R.id.home:
//                //this.finish();
//                onBackPressed();
//                Global_Data.CatlogueStatus = "";
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
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
                val sp: SharedPreferences = this@Url_Setting.getSharedPreferences("SimpleLogic", 0)
                try {
                    val target = Math.round(sp.getFloat("Target", 0f))
                    val achieved = Math.round(sp.getFloat("Achived", 0f))
                    val age_float = sp.getFloat("Achived", 0f) / sp.getFloat("Target", 0f) * 100
                    targetNew = if (age_float.toString().equals("infinity", ignoreCase = true)) {
                        val age = Math.round(age_float)
                        if (Global_Data.rsstr.length > 0) {
                            "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [infinity") + "%" + "]"
                            //todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        } else {
                            "T/A : Rs " + String.format("$target/$achieved [infinity") + "%" + "]"
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                    } else {
                        val age = Math.round(age_float)
                        if (Global_Data.rsstr.length > 0) {
                            "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [$age") + "%" + "]"
                            // todaysTarget.setText();
                        } else {
                            "T/A : Rs " + String.format("$target/$achieved [$age") + "%" + "]"
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
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
        val i = Intent(this@Url_Setting, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        finish()
    }

    private inner class checkurl_Tast : AsyncTask<String?, String?, String>() {
         override fun doInBackground(vararg urls: String?): String? {
            try {
                val pref_devid = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE)
                devid = pref_devid.getString("devid", "")
                val Device_id = pref_devid.getString("devid", "")
                Log.i("volley", "domain: $url")
                Log.i("volley", "Device_id: $Device_id")
                // Log.i("volley", "Sim_Number: " + Global_Val.Sim_Number);
                Log.i("volley", "Service url: " + url + "/metal/api/v1/menus/registration?imei_no=" + URLEncoder.encode(Device_id, "UTF-8"))
                val jsObjRequest = JsonObjectRequest(url + "/metal/api/v1/menus/registration?imei_no=" + URLEncoder.encode(Device_id, "UTF-8"), null, Response.Listener { response ->
                    // JsonObjectRequest jsObjRequest = new JsonObjectRequest(domain+"/menus/registration?imei_no="+ URLEncoder.encode("911305401754123", "UTF-8"),null, new Response.Listener<JSONObject>() {
                    Log.i("volley", "response: $response")
                    //  Log.i("volley", "response reg Length: " + response.length());
                    try {
                        var response_result: String? = ""
                        response_result = if (response.has("result")) {
                            response.getString("result")
                        } else {
                            "data"
                        }
                        runOnUiThread {
                            urls_btn!!.isEnabled = true
                            urls_btn!!.isClickable = true
                            dialog!!.dismiss()
                            if (!url.endsWith("/")) {
                                url = "$url/"
                            }
                            val editor = sp!!.edit()
                            editor.putString("Cust_Service_Url", url)
                            editor.commit()
                            displayExceptionMessage(resources.getString(R.string.url_athentication_success_message))
                            val i = Intent(this@Url_Setting, LoginActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            startActivity(i)
                            finish()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        runOnUiThread {
                            displayExceptionMessage(e.message)
                            urls_btn!!.isEnabled = true
                            urls_btn!!.isClickable = true
                            dialog!!.dismiss()
                        }
                    }
                }, Response.ErrorListener { error ->
                    runOnUiThread {
                        Log.i("volley", "error: $error")
                        urls_btn!!.isEnabled = true
                        urls_btn!!.isClickable = true
                        displayExceptionMessage(error.message)
                        dialog!!.dismiss()
                    }
                })
                val requestQueue = Volley.newRequestQueue(this@Url_Setting)
                // queue.add(jsObjRequest);
                jsObjRequest.setShouldCache(false)
                val socketTimeout = 30000 //30 seconds - change to what you want
                val policy: RetryPolicy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                jsObjRequest.retryPolicy = policy
                requestQueue.add(jsObjRequest)
            } catch (e: Exception) {
                runOnUiThread {
                    e.printStackTrace()
                    displayExceptionMessage(e.message)
                    dialog!!.dismiss()
                }
            }
            return ""
        }
    }

    protected fun onPostExecute(result: String?) {}
    fun displayExceptionMessage(msg: String?) {

        Global_Data.Custom_Toast(this, msg,"")
    }

    fun Language_Select_Dialog() {
        val dialog = Dialog(this@Url_Setting)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.language_dialog)
        // dialog.setTitle(getResources().getString(R.string.PDistributors));
        Language_List.clear()
        Language_List.add(resources.getString(R.string.Select_Language))
        Language_List.add(resources.getString(R.string.English))
        Language_List.add(resources.getString(R.string.Hindi))
        val spnLanguage = dialog.findViewById<Spinner>(R.id.spnLanguage)
        LanguageAdapter = ArrayAdapter(this@Url_Setting, android.R.layout.simple_spinner_item, Language_List)
        LanguageAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnLanguage.adapter = LanguageAdapter
        val spff = getSharedPreferences("SimpleLogic", 0)
        val Language = spff.getString("Language", "")
        if (!Language.equals("", ignoreCase = true) && !Language.equals(null, ignoreCase = true)) {
            val spinnerPosition = LanguageAdapter!!.getPosition(Language)
            spnLanguage.setSelection(spinnerPosition)
        }
        spnLanguage.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, arg1: View,
                                        pos: Int, arg3: Long) {
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
        val dialogButtonOk = dialog.findViewById<Button>(R.id.dialogButtonOk)
        dialogButtonOk.setOnClickListener {
            if (spnLanguage.selectedItem.toString()
                            .equals(resources.getString(R.string.Select_Language), ignoreCase = true)) {
                Global_Data.Custom_Toast(this@Url_Setting,
                        resources.getString(R.string.Please_Select_Language),"yes")
            } else if (spnLanguage.selectedItem.toString()
                            .equals(resources.getString(R.string.Hindi), ignoreCase = true)) {
                val spf = getSharedPreferences("SimpleLogic", 0)
                val editor = spf.edit()
                editor.putString("Language", "hi")
                editor.commit()
                val myLocale = Locale("hi")
                val res = resources
                val dm = res.displayMetrics
                val conf = res.configuration
                conf.locale = myLocale
                res.updateConfiguration(conf, dm)
                recreate()
                dialog.dismiss()
            } else if (spnLanguage.selectedItem.toString()
                            .equals(resources.getString(R.string.English), ignoreCase = true)) {
                val spf = getSharedPreferences("SimpleLogic", 0)
                val editor = spf.edit()
                editor.putString("Language", "en")
                editor.commit()
                val myLocale = Locale("en_US")
                val res = resources
                val dm = res.displayMetrics
                val conf = res.configuration
                conf.locale = myLocale
                res.updateConfiguration(conf, dm)
                recreate()
                dialog.dismiss()
            }
        }
        val dialogButtonCancel = dialog.findViewById<View>(R.id.dialogButtonCancel) as Button
        dialogButtonCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun clearapplicationdata() {
        val cacheDirectory = cacheDir
        val applicationDirectory = File(cacheDirectory.parent)
        if (applicationDirectory.exists()) {
            val fileNames = applicationDirectory.list()
            for (fileName in fileNames) {
                if (fileName != "lib") {
                    val intent = Intent(this@Url_Setting, SplashScreenActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    startActivity(intent)
                    stopService(Intent(this@Url_Setting, LocationServices::class.java))
                    finishAffinity()
                    finish()
                    dialogClear!!.dismiss()
                    deleteFile(File(applicationDirectory, fileName))
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