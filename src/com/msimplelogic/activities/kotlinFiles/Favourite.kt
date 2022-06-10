package com.msimplelogic.activities.kotlinFiles

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.msimplelogic.activities.R
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.*
import com.msimplelogic.adapter.Smart_order_fav_adaptor
import com.msimplelogic.model.Customer_Model
import com.msimplelogic.model.Download_Order_Model
import com.msimplelogic.model.Smart_order_model
import com.msimplelogic.webservice.ConnectionDetector
import com.opencsv.CSVWriter
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip

import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.android.synthetic.main.content_smart__order.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*

class Favourite : BaseActivity() {
    var repeat_order:Button?=null
    var dialog1: ProgressDialog? = null
    var adaptor: Smart_order_fav_adaptor? = null
    var list: ArrayList<Smart_order_model>? = null
    var rv: RecyclerView? = null
    var s = arrayOf<String>()
    val testingList: ArrayList<Customer_Model> = ArrayList();
    var privious: ImageView? = null
    var next: ImageView? = null
    var sp:SharedPreferences?=null
    var count = 1
    var final_response = ""
    var response_result = ""
    var download_order_models = ArrayList<Download_Order_Model>()
    internal var dbvoc = DataBaseHelper(this)
    var loginDataBaseAdapter: LoginDataBaseAdapter? = null
    var downlodell:LinearLayout?=null
    var fav: Button? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var service_url = ""
    var dialog: ProgressDialog? = null
    var repeatorder:Button?=null
    var edit_order:LinearLayout?=null
    var delete:LinearLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        cd = ConnectionDetector(Smart_Order@ this)

        list = ArrayList<Smart_order_model>()
        loginDataBaseAdapter = LoginDataBaseAdapter(this)
        loginDataBaseAdapter = loginDataBaseAdapter!!.open()

        rv = findViewById(R.id.rv)
        delete = findViewById(R.id.delete)
        repeatorder = findViewById(R.id.repeat_order)
        next = findViewById(R.id.next)
        privious = findViewById(R.id.privious)
        downlodell = findViewById(R.id.downlodell)

        edit_order=findViewById(R.id.edit_order)

        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {

            orderstatusimg.setBackgroundResource(R.drawable.order_status_dark)
            editorderimg.setBackgroundResource(R.drawable.edit_order_dark)
            deleteimg.setBackgroundResource(R.drawable.delete_dark)
            downloadeimg.setBackgroundResource(R.drawable.download_dark)
        }

        edit_order!!.setOnClickListener {
             if (Global_Data.GLOvel_GORDER_ID == "") {
                Global_Data.Custom_Toast(this@Favourite, "Please Select Order", "Yes")


            } else {
                isInternetPresent = cd!!.isConnectingToInternet
                if (isInternetPresent) {

                    val contacts = dbvoc.getCustomerCode(Global_Data.custname)

                    for (cn in contacts) {
                        // All_customers!!.add(cn.customeR_SHOPNAME.trim())
                        var customer_id = cn.cust_Code.trim()

                        var s_name = cn.getCUSTOMER_SHOPNAME()
                        var s_address = cn.getAddress()
                        var c_mobile_number = cn.getMOBILE_NO()
                        var customer_landline = cn.landlinE_NO

                        val sp: SharedPreferences = this@Favourite.getApplicationContext().getSharedPreferences("SimpleLogic", 0)
                        val spreedit: SharedPreferences.Editor = sp.edit()
                        spreedit.putString("shopname", s_name)
                        spreedit.putString("c_address", s_address)
                        spreedit.putString("shopcode", customer_id)
                        spreedit.putString("c_mobile_number", c_mobile_number)
                        spreedit.putString("customer_landline", customer_landline)
//                        spreedit.putString("c_credit_profile",item.credit_profile.toString())
//                        spreedit.putString("c_outstanding",item.outstanding.toString())
//                        spreedit.putString("c_overdue",item.overdue.toString())
//                        spreedit.putString("c_address",item.address.toString())
                        spreedit.commit()
                    }
                    getPrevious_OrderData("Edit")                } else {

                    Global_Data.Custom_Toast(CustomerServicesTraits@ this, resources.getString(R.string.internet_connection_error), "Yes")

                }

            }
        }


        GetOrder(Global_Data.GLOvel_CUSTOMER_ID)
        privious!!.setOnClickListener {

            count = count - 1
            GetOrder(Global_Data.GLOvel_CUSTOMER_ID)
        }

        next!!.setOnClickListener {
            count = count + 1
            privious!!.visibility = View.VISIBLE
            GetOrder(Global_Data.GLOvel_CUSTOMER_ID)
        }


        delete!!.setOnClickListener {

            if(Global_Data.smart_order_adaptor==""){

                Global_Data.Custom_Toast(this@Favourite,"Please Select Order","Yes")
            }else{

                //  var removeindex:Int?=null
                //  removeindex==Global_Data.smart_order_adaptor
                adaptor!!.detete(Global_Data.smart_order_adaptor.toInt())
                Global_Data.smart_order_adaptor="";
                //  adaptor!!.notifyDataSetChanged()
            }
        }

        repeatorder!!.setOnClickListener {

            try {
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LATITUDE) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LONGITUDE)) {
                    val locationAddress = LocationAddress()
                    LocationAddress.getAddressFromLocation(java.lang.Double.valueOf(Global_Data.GLOvel_LATITUDE), java.lang.Double.valueOf(Global_Data.GLOvel_LONGITUDE),
                            applicationContext, Global_Data.GeocoderHandler())
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

           if (Global_Data.GLOvel_GORDER_ID == "") {
                Global_Data.Custom_Toast(this@Favourite, "Please Select Order", "Yes")


            } else {
                isInternetPresent = cd!!.isConnectingToInternet
                if (isInternetPresent) {
                    val contacts = dbvoc.getCustomerCode(Global_Data.custname)

                    for (cn in contacts) {
                        // All_customers!!.add(cn.customeR_SHOPNAME.trim())
                        var customer_id = cn.cust_Code.trim()

                        var s_name = cn.getCUSTOMER_SHOPNAME()
                        var s_address = cn.getAddress()
                        var c_mobile_number = cn.getMOBILE_NO()
                        var customer_landline = cn.landlinE_NO

                        val sp: SharedPreferences = this@Favourite.getApplicationContext().getSharedPreferences("SimpleLogic", 0)
                        val spreedit: SharedPreferences.Editor = sp.edit()
                        spreedit.putString("shopname", s_name)
                        spreedit.putString("c_address", s_address)
                        spreedit.putString("shopcode", customer_id)
                        spreedit.putString("c_mobile_number", c_mobile_number)
                        spreedit.putString("customer_landline", customer_landline)
//                        spreedit.putString("c_credit_profile",item.credit_profile.toString())
//                        spreedit.putString("c_outstanding",item.outstanding.toString())
//                        spreedit.putString("c_overdue",item.overdue.toString())
//                        spreedit.putString("c_address",item.address.toString())
                        spreedit.commit()
                    }
                    getPrevious_OrderData("Order")
                } else {
                    Global_Data.Custom_Toast(CustomerServicesTraits@ this, resources.getString(R.string.internet_connection_error), "Yes")
                }
            }
        }

        downlodell!!.setOnClickListener {

                isInternetPresent = cd!!.isConnectingToInternet
                if (isInternetPresent) {

                    downlode()
                } else {
                    Global_Data.Custom_Toast(CustomerServicesTraits@ this, resources.getString(R.string.internet_connection_error), "Yes")
                }
        }





    }

    private fun downlode() {


        dialog1!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog1!!.setTitle(resources.getString(R.string.app_name))
        dialog1!!.setCancelable(false)
        dialog1!!.show()
        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null)!!

        val domain = resources.getString(R.string.service_domain)

        try {
            //  service_url = domain + "orders/order_list?email=" + user_email + "&page=" + count + "&cust_code=" + customerId
            service_url = domain + "orders/download_cutomer_order_list?cust_code=" + Global_Data.GLOvel_CUSTOMER_ID + "&email=" + user_email+"&isfavourite=true"

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        Log.i("volley", "service " + service_url)
        var jsObjRequest: StringRequest? = null

        jsObjRequest = StringRequest(service_url, Response.Listener { response ->
            Log.i("volley", "response: $response")
            final_response = response

            OrderList_List_Task_download().execute(response)

            dialog1!!.dismiss()
        },
                Response.ErrorListener { error ->
                    downlodell!!.setEnabled(true)
                    downlodell!!.setClickable(true)
                    if (error is TimeoutError || error is NoConnectionError) {
//                        Toast.makeText(applicationContext,
//                                "Network Error",
//                                Toast.LENGTH_LONG).show()
                        Global_Data.Custom_Toast(applicationContext,
                                "Network Error","")
                    } else if (error is AuthFailureError) {
//                        Toast.makeText(applicationContext,
//                                "Server AuthFailureError  Error",
//                                Toast.LENGTH_LONG).show()
                        Global_Data.Custom_Toast(applicationContext,
                                "Server AuthFailureError  Error","")
                    } else if (error is ServerError) {
//                        Toast.makeText(applicationContext,
//                                "Server   Error",
//                                Toast.LENGTH_LONG).show()
                        Global_Data.Custom_Toast(applicationContext,
                                "Server   Error","")
                    } else if (error is NetworkError) {
//                        Toast.makeText(applicationContext,
//                                "Network   Error",
//                                Toast.LENGTH_LONG).show()
                        Global_Data.Custom_Toast(applicationContext,
                                "Network   Error","")
                    } else if (error is ParseError) {
//                        Toast.makeText(applicationContext,
//                                "ParseError   Error",
//                                Toast.LENGTH_LONG).show()
                        Global_Data.Custom_Toast(applicationContext,
                                "ParseError   Error","")
                    } else {
                        //Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
                        Global_Data.Custom_Toast(applicationContext, error.message,"")
                    }
                });

        val requestQueue = Volley.newRequestQueue(applicationContext)
        val socketTimeout = 300000//30 seconds - change to what you want
        val policy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        jsObjRequest.setRetryPolicy(policy)
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        jsObjRequest.setShouldCache(false)
        requestQueue.cache.clear()
        requestQueue.add<String>(jsObjRequest)

    }

    private inner class OrderList_List_Task_download : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg responsenew: String): String {


            try {
                val response = JSONObject(final_response)
                if (response.has("result")) {
                    response_result = response.getString("result")
                    this@Favourite.runOnUiThread(Runnable {
                        downlodell!!.setEnabled(true)
                        downlodell!!.setClickable(true)
                        dialog!!.dismiss()

//                        val toast = Toast.makeText(this@Favourite, response_result, Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(this@Favourite, response_result,"")
                    })
                } else  {

                    //dbvoc.getDeleteTable("delivery_products");

                    val order_lists = response.getJSONArray("order_lists")
                    Log.i("volley", "response order_lists Length: " + order_lists.length())
                    Log.d("volley", "order_lists$order_lists")

                    if (order_lists.length() <= 0) {

                        this@Favourite.runOnUiThread(Runnable {
                            downlodell!!.setEnabled(true)
                            downlodell!!.setClickable(true)
                            //  dialog!!.dismiss()
//                            val toast = Toast.makeText(this@Favourite, "Record not exist", Toast.LENGTH_LONG)
//                            toast.setGravity(Gravity.CENTER, 0, 0)
//                            toast.show()
                            Global_Data.Custom_Toast(this@Favourite, "Record not exist","yes")
                        })
                    } else {


                        download_order_models.clear()

                        for (i in 0 until order_lists.length()) {

                            val jsonObject = order_lists.getJSONObject(i)
                            download_order_models.add(Download_Order_Model(jsonObject.getString("order_number"), jsonObject.getString("order_date"), jsonObject.getString("division"), jsonObject.getString("brand"), jsonObject.getString("item_code"), jsonObject.getString("qty"), jsonObject.getString("amt")))

                        }

                        //  dialog!!.dismiss()
                        this@Favourite.runOnUiThread(Runnable {
                            downlodell!!.setEnabled(true)
                            downlodell!!.setClickable(true)
                            //    dialog!!.dismiss()
                            requestStoragePermission()
                        })


                    }

                    this@Favourite.runOnUiThread(Runnable { })
                    //	dialog.dismiss();

                    //finish();

                }


                // }

                // output.setText(data);
            } catch (e: JSONException) {
                e.printStackTrace()

                val intent = Intent(this@Favourite, MainActivity::class.java)
                startActivity(intent)
                finish()

                this@Favourite.runOnUiThread(Runnable {
                    downlodell!!.setEnabled(true)
                    downlodell!!.setClickable(true)
                    //  dialog!!.dismiss()
                })

            }


//            this@Smart_Order.runOnUiThread(Runnable {
//
//            })


            return "Executed"
        }

        override fun onPostExecute(result: String) {

            this@Favourite.runOnUiThread(Runnable {
                downlodell!!.setEnabled(true)
                downlodell!!.setClickable(true)
                //   dialog!!.dismiss()
            })


        }


        override fun onProgressUpdate(vararg values: Void) {}
    }

    private fun requestStoragePermission() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {


                            try {
                                exportEmailInCSV()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).withErrorListener { error ->
                  //  Toast.makeText(applicationContext, "Error occurred! $error", Toast.LENGTH_SHORT).show()
                    Global_Data.Custom_Toast(applicationContext, "Error occurred! $error", "")
                }
                .onSameThread()
                .check()
    }

    @Throws(IOException::class)
    fun exportEmailInCSV() {
        run {

            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val folder = File(path, "DealerApp")

            if (!folder.exists()) {
                folder.mkdir()
            }

            val `var` = false


            println("" + `var`)


            val filename = folder.toString() + "/" + Global_Data.GLOvel_CUSTOMER_ID+"Favourite" + ".csv"

            // show waiting screen
            val contentTitle = getString(R.string.app_name)
            val progDailog = ProgressDialog.show(
                    this@Favourite, contentTitle, "Generated...",
                    true)//please wait
            val handler = object : Handler() {
                override fun handleMessage(msg: Message) {

                    val yourFile = File(folder, Global_Data.GLOvel_CUSTOMER_ID + "Favourite"+ ".csv")

                    try {
                        openFile(yourFile)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }
            }

            object : Thread() {
                override fun run() {
                    try {


                        var writer: CSVWriter? = null
                        // writer = CSVWriter(FileWriter(filename), ',')
                        writer = CSVWriter(FileWriter(filename), ',')
                        val entries = "Order Number#Order Date#Division#Brand#Item Code#Qty#Amt".split("#") // array of your values
                        writer!!.writeNext(entries.toTypedArray())


                        if (download_order_models.size > 0) {
                            for (i in download_order_models.indices) {
                                val list_items = download_order_models[i]

                                val s = list_items.order_Number + "#" + list_items.order_Date + "#" + list_items.division + "#" + list_items.brand + "#" + list_items.item_Code + "#" + list_items.qty + "#" + list_items.amt
                                val entriesdata  = s.split("#") // array of your values
                                writer!!.writeNext(entriesdata.toTypedArray())

                            }
                        }


                        writer!!.close()

                    } catch (e: Exception) {
                    }

                    handler.sendEmptyMessage(0)
                    progDailog.dismiss()
                }
            }.start()

        }

    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@Favourite)
        builder.setTitle("Need Permissions")
        builder.setCancelable(false)
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
            openSettings()
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()

    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun openFile(url: File) {

        try {

            val uri = Uri.fromFile(url)

            val intent = Intent(Intent.ACTION_VIEW)
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword")
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf")
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel")

            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain")
            } else if (url.toString().contains(".mp4") || url.toString().contains(".AVI") || url.toString().contains(".FLV") || url.toString().contains(".WMV") || url.toString().contains(".MOV")) {
                // Text file
                intent.setDataAndType(uri, "video/mp4")

            } else {
                intent.setDataAndType(uri, "text/csv")
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
          //  Toast.makeText(applicationContext, "No application found which can open the file", Toast.LENGTH_SHORT).show()
            Global_Data.Custom_Toast(applicationContext, "No application found which can open the file","")
        }

    }
    fun getPrevious_OrderData(editflag:String) { //calendarn = Calendar.getInstance();
//year = calendarn.get(Calendar.YEAR);

        dialog = ProgressDialog(this@Favourite, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()
        try { //TODO USER EMAIL
            val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
            val domain = resources.getString(R.string.service_domain)
            val device_id = sp.getString("devid", "")
            val spf: SharedPreferences = this@Favourite.getSharedPreferences("SimpleLogic", 0)
            val user_email = spf.getString("USER_EMAIL", null)
            val service_url = domain + "orders/view_order?email=" + user_email + "&order_id=" + Global_Data.GLOvel_GORDER_ID

            Log.i("volley", "domain: $service_url")
            Log.i("volley", "email: $user_email")
            Log.i("volley", "serviuce" + service_url)
            // Log.i("previous_order url", "previous_order url " + service_url + "customers/previous_order?customer_code=" + Global_Data.GLOvel_CUSTOMER_ID + "&email=" + user_email)
            val jsObjRequest = JsonObjectRequest(domain + "orders/view_order?email=" + user_email + "&order_id=" + Global_Data.GLOvel_GORDER_ID, null, Response.Listener { response ->
                Log.i("volley", "response: $response")
                //  Log.i("volley", "response reg Length: " + response.length());
                try {
                    var response_result: String? = ""
                    if (response.has("result")) {
                        response_result = response.getString("result")
//                        val toast = Toast.makeText(applicationContext, response_result, Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(applicationContext, response_result,"")
                    } else {
//                        if(editflag.equals("Edit",ignoreCase = false))
//                        {
//                            dbvoc.getDeleteTable("previous_order_products")
//                            dbvoc.getDeleteTable("previous_orders")
//                        }
//                        else
//                        {

                            val randomPIN = System.currentTimeMillis()
                            val PINString = randomPIN.toString()
                            val c = Calendar.getInstance()
                            println("Current time =&gt; " + c.time)

                            val df = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                            val formattedDaten = df.format(c.time)

                            val spf1 = getSharedPreferences("SimpleLogic", 0)
                            val shopname = spf1.getString("shopname", "")

                            Global_Data.GLObalOrder_id = "Ord$PINString"
                            Global_Data.GLOvel_GORDER_ID = "Ord$PINString"

                            loginDataBaseAdapter!!.insertOrders("", Global_Data.GLOvel_GORDER_ID, Global_Data.GLOvel_CUSTOMER_ID, "", user_email, "", "", Global_Data.address, "", "", "", "", "", formattedDaten, "", shopname, "", "", "Secondary Sales / Retail Sales", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, "", "", "", "", "", "", "", "")
                       // }


                        //JSONArray previous_orders = response.getJSONArray("previous_orders");
                        val previous_order_products = response.getJSONArray("order_products")
                        //Log.i("volley", "response reg previous_orders Length: " + previous_orders.length());
                        Log.i("volley", "response reg previous_order_products Length: " + previous_order_products.length())
                        //	Log.d("States", "previous_orders" + previous_orders.toString());
                        Log.d("States", "previous_order_products$previous_order_products")
                        //
                        if (previous_order_products.length() <= 0) {
                            dialog!!.dismiss()
                            //Toast.makeText(Order.this, "Previous order not found.", Toast.LENGTH_LONG).show();
//                            val toast = Toast.makeText(applicationContext, resources.getString(R.string.Previous_order_not_found), Toast.LENGTH_LONG)
//                            toast.setGravity(Gravity.CENTER, 0, 0)
//                            toast.show()
                            Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Previous_order_not_found),"yes")
                        } else { //								for (int i = 0; i < previous_orders.length(); i++) {
                            //
                            //									JSONObject jsonObject = previous_orders.getJSONObject(i);
                            //
                            //								}
                            for (i in 0 until previous_order_products.length()) {
                                val jsonObject = previous_order_products.getJSONObject(i)

//                                if(editflag.equals("Edit",ignoreCase = false))
//                                {
//                                    Global_Data.Previous_Order_UpdateOrder_ID = jsonObject.getString("order_number").trim { it <= ' ' }
//                                    Global_Data.Previous_Order_ServiceOrder_ID = jsonObject.getString("order_number").trim { it <= ' ' }
//                                    loginDataBaseAdapter?.insertPreviousOrderProducts("", "", jsonObject.getString("order_number"), "", "", "", "", "", jsonObject.getString("scheme_code"), " ", "", jsonObject.getString("total_qty"), jsonObject.getString("retail_price"), jsonObject.getString("mrp"), jsonObject.getString("amount"), "", "", "", " ", jsonObject.getString("product_code"), " ", jsonObject.getString("product_name"))
//                                }
//                                else{

                                    loginDataBaseAdapter!!.insertOrderProducts(" ", " ", Global_Data.GLOvel_GORDER_ID, "","", "",jsonObject.getString("product_code"), " ", jsonObject.getString("scheme_code"), " ", "", jsonObject.getString("total_qty"), jsonObject.getString("retail_price"), jsonObject.getString("mrp"), jsonObject.getString("amount"), "", "", Global_Data.GLOvel_CUSTOMER_ID, " ",  jsonObject.getString("product_code"), " ",  jsonObject.getString("product_name"), "", "","", "", "", "", "")
                               //}

                            }

//                            if(editflag.equals("Edit",ignoreCase = false))
//                            {
//                                val intent = Intent(this@Favourite, Previous_orderNew_S2::class.java)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
//                                //  intent.putExtra("int","smart")
//                                Global_Data.intentvalue="favourite"
//                                startActivity(intent)
//                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                                finish()
//                                dialog!!.dismiss()
//                            }
//                            else{

                                val intent = Intent(this@Favourite, PreviewOrderSwipeActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                //  intent.putExtra("int","smart")
                                Global_Data.intentvalue="favourite"
                                startActivity(intent)
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                finish()
                                dialog!!.dismiss()
                           // }

                        }
                        dialog!!.dismiss()
                        //finish();
                    }
                    // }
                    // output.setText(data);
                } catch (e: JSONException) {
                    e.printStackTrace()
                    dialog!!.dismiss()
                }
                dialog!!.dismiss()
            }, Response.ErrorListener { error ->
                Log.i("volley", "error: $error")
                //Toast.makeText(Order.this, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();
//                val toast = Toast.makeText(applicationContext, resources.getString(R.string.Server_Error), Toast.LENGTH_LONG)
//                toast.setGravity(Gravity.CENTER, 0, 0)
//                toast.show()
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Server_Error),"yes")
                dialog!!.dismiss()
            })
            val requestQueue = Volley.newRequestQueue(this@Favourite)
            // queue.add(jsObjRequest);
            jsObjRequest.setShouldCache(false)
            val socketTimeout = 200000 //30 seconds - change to what you want
            val policy: RetryPolicy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            jsObjRequest.retryPolicy = policy
            requestQueue.add(jsObjRequest)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            dialog!!.dismiss()
        }
    }



    private fun GetOrder(glovelCustomerId: String?) {

        list!!.clear()

        dialog1 = ProgressDialog(this@Favourite, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        dialog1!!.setMessage(resources.getString(R.string.customer_loading_dialog_message))
        dialog1!!.setTitle(resources.getString(R.string.app_name))
        dialog1!!.setCancelable(false)
        dialog1!!.show()
        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null)!!

        val domain = resources.getString(R.string.service_domain)

        try {
            //  service_url = domain + "orders/order_list?email=" + user_email + "&page=" + count + "&cust_code=" + customerId
            service_url = domain + "orders/order_list?email=" + user_email + "&page=" + count + "&cust_code=" + glovelCustomerId+ "&type=favourite"

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

                                dialog1!!.hide()
                                Global_Data.Custom_Toast(this, "" + response_result, "Yes")
                                //finish().toString()
                                //finish()
                                list!!.clear()

//                                if(list!!.size<0){
//                                    adaptor!!.notifyDataSetChanged()
//                                }

                                next!!.visibility = View.GONE
                                dialog1!!.hide()
                                dialog1!!.dismiss()

                                onBackPressed()

                            } else {
                                Global_Data.image_check.clear()
                                Global_Data.Order_Select_check.clear()

                                dialog1!!.dismiss()


                                val obj = JSONObject(response)


                                val heroArray = obj.getJSONArray("orders")
                                for (i in 0 until heroArray.length()) {
                                    val jsonObject = heroArray.getJSONObject(i)


                                    list!!.add(Smart_order_model("", jsonObject.getString("total_order_amount"), jsonObject.getString("created_at"), jsonObject.getString("is_favourite"), jsonObject.getString("id"), jsonObject.getString("order_number")))

                                    Global_Data.Order_Select_check[i] = ""
                                    if (jsonObject.getString("is_favourite").equals("true", ignoreCase = true)) {
                                        Global_Data.image_check[i] = "yes"
                                    } else {
                                        Global_Data.image_check[i] = "no"
                                    }
                                    // Kot_Gloval.listfullnotes!!.add(Order_notes_model("",jsonObject.getString("customer_name"), jsonObject.getString("name"), jsonObject.getString("description"),jsonObject.getString("created_date")))

                                }
                                this@Favourite.runOnUiThread(Runnable {
                                    if (list!!.size > 9) {

                                        next!!.visibility = View.VISIBLE
                                    }
                                    if (count == 1) {
                                        privious!!.visibility = View.GONE
                                    }
                                    dialog1!!.dismiss()
                                    dialog1!!.hide()

                                })



                                adaptor = Smart_order_fav_adaptor(this, list)
                                val mLayoutManager = LinearLayoutManager(this)
                                rv!!.layoutManager = mLayoutManager
                                rv!!.adapter = adaptor
                                adaptor!!.notifyDataSetChanged()

                                dialog1!!.dismiss()
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                            finish()
                            dialog1!!.dismiss()
                        }
                        //  dialog1!!.dismiss()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        finish();
                        dialog1!!.dismiss()
                    }
                    dialog1!!.dismiss()
                },
                Response.ErrorListener { error ->
                    //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    if (error is TimeoutError || error is NoConnectionError) { //							Toast.makeText(Video_Main_List.this,
//									"Network Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@ this,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                "Network Error","yes")
                    } else if (error is AuthFailureError) { //							Toast.makeText(Video_Main_List.this,
//									"Server AuthFailureError  Error",
//									Toast.LENGTH_LONG).show();
                      /*  val toast = Toast.makeText(QuoteAddRetailerTrait@ this,
                                "Server AuthFailureError  Error",
                                Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()*/
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                "Server AuthFailureError  Error","yes")
                    } else if (error is ServerError) { //							Toast.makeText(Video_Main_List.this,
//									"Server   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@ this,
//                                resources.getString(R.string.Server_Errors),
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                resources.getString(R.string.Server_Errors),"yes")
                    } else if (error is NetworkError) { //							Toast.makeText(Video_Main_List.this,
//									"Network   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@ this,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                "Network Error","yes")
                    } else if (error is ParseError) { //							Toast.makeText(Video_Main_List.this,
//									"ParseError   Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@ this,
//                                "ParseError   Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QQuoteAddRetailerTrait@ this,
                               "ParseError   Error","yes")
                    } else { //Toast.makeText(Video_Main_List.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(QuoteAddRetailerTrait@ this,
//                                error.message,
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(QuoteAddRetailerTrait@ this,
                                error.message,"yes")
                    }
                    //						Intent launch = new Intent(Video_Main_List.this,MainActivity.class);
//						startActivity(launch);
                    finish()
                    dialog1!!.dismiss()
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


    override fun onBackPressed() {

        Global_Data.image_check.clear()
        Global_Data.Order_Select_check.clear()
        Global_Data.intentvalue=""

        val m = Intent(applicationContext, Smart_Order::class.java)
        startActivity(m)

        finish()
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
                val sp = this@Favourite.getSharedPreferences("SimpleLogic", 0)
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
}
