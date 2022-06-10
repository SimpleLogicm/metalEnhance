package com.msimplelogic.activities.kotlinFiles

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.msimplelogic.activities.*
import com.msimplelogic.adapter.Invoice_adaptor
import com.msimplelogic.helper.FileUtils
import com.msimplelogic.helper.LogUtils.LOGE
import com.msimplelogic.helper.PermissionsChecker
import com.msimplelogic.model.Invoice_model
import com.msimplelogic.services.getServices
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_invoice_order.*
import kotlinx.android.synthetic.main.content_visit__schedule.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException


class Invoice_order : BaseActivity() {

    var tv_orderdate: TextView? = null
    var tv_deliverydate: TextView? = null
    var tv_status: TextView? = null
    var tv_cust_name: TextView? = null
    var tv_cust_email: TextView? = null
    var tv_cust_billing_address: TextView? = null
    var tv_cust_shipping_details: TextView? = null
    var rv: RecyclerView? = null
    var btn_ok: Button? = null
    var btn_fav: Button? = null
    var btn_share: Button? = null
    var tv_totalamount: TextView? = null
    var list: ArrayList<Invoice_model>? = null
    var adaptor: Invoice_adaptor? = null
    internal var dialog: ProgressDialog? = null

    var mContext: Context? = null
    var sp:SharedPreferences?=null

    var checker: PermissionsChecker? = null
    var o_date = "";
    var need_date = ""
    var o_amount: Double = 0.0;
    var user_email = ""
    var shopcode = ""
    var c_address = ""
    var c_name = ""
    var c_mobile = ""
    var c_email = ""
    var order_number = "";
    var dbvoc = DataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_order)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
      //  setTitle(resources.getString(R.string.Invoice))

        mContext = Invoice_order@ this;



        tv_orderdate = findViewById(R.id.tv_orderdate)
        tv_deliverydate = findViewById(R.id.tv_deliverydate)
        tv_status = findViewById(R.id.tv_status)
        tv_cust_name = findViewById(R.id.tv_cust_name)
        tv_cust_email = findViewById(R.id.tv_cust_email)
        tv_cust_billing_address = findViewById(R.id.tv_cust_billing_address)
        tv_cust_shipping_details = findViewById(R.id.tv_cust_shipping_details)
        rv = findViewById(R.id.rv)
        btn_ok = findViewById(R.id.btn_ok)
        btn_fav = findViewById(R.id.btn_fav)
        btn_share = findViewById(R.id.btn_share)
        tv_totalamount = findViewById(R.id.tv_totalamount)

        if(Global_Data.quat.equals("Yes") && Global_Data.quat!=""){
            btn_fav!!.visibility=View.GONE
        }else{
            btn_fav!!.visibility=View.VISIBLE

        }

        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
//            val img: Drawable =   context!!.resources.getDrawable(R.drawable.ic_baseline_date_range_24_dark)
//
//            et_date!!.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        btn_fav!!.setOnClickListener {
            Favouritefun()
        }

        rv?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            o_date = bundle.getString("order_date")!!
            need_date = bundle.getString("need_bydate")!!
            order_number = bundle.getString("order_number")!!

        }

        val spf = getSharedPreferences("SimpleLogic", 0)
        user_email = spf.getString("USER_EMAIL", null)!!
        shopcode = spf.getString("shopcode", null)!!
        c_address = spf.getString("c_address", null)!!
        c_name = spf.getString("shopname", null)!!
        c_mobile = spf.getString("c_mobile_number", null)!!
        // c_email = spf.getString("c_address", null)

        tv_orderdate!!.setText(" " + o_date);
        tv_deliverydate!!.setText(" " + need_date);
        tv_cust_name!!.setText(c_name);
        tv_cust_email!!.setText(c_mobile);
        tv_cust_billing_address!!.setText(c_address);
        tv_cust_shipping_details!!.setText(c_address);

        list = ArrayList<Invoice_model>()

        var product_name = "";
        for (i in 0..Global_Data.p_code.size - 1) {
            val cont1: List<Local_Data> = dbvoc.getItemName_bycode(Global_Data.p_code.get(i))
            if (cont1.size > 0) {
                for (cn in cont1) {
                    product_name = cn.getProduct_nm()
                }
            }

            o_amount = o_amount + Global_Data.p_amount.get(i).toDouble();
            list!!.add(Invoice_model(product_name, Global_Data.p_code.get(i), Global_Data.p_qty.get(i), Global_Data.p_mrp.get(i), Global_Data.p_amount.get(i)))
        }

        tv_totalamount!!.setText("₹"+o_amount.toString());

        adaptor = Invoice_adaptor(this, list!!)
        rv?.adapter = adaptor

        btn_share!!.setOnClickListener {
            requestStoragePermissionsave();
        }

        btn_ok!!.setOnClickListener {
            // getTargetDataservice(context);
            val intentn = Intent(applicationContext, MainActivity::class.java)

            startActivity(intentn)
            finish()
        }
    }

    private fun Favouritefun() {
        dialog = ProgressDialog(this@Invoice_order, ProgressDialog.THEME_HOLO_LIGHT)
        dialog!!.setMessage(this@Invoice_order.getResources().getString(R.string.Please_Wait))
        dialog!!.setTitle(this@Invoice_order.getResources().getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()

        val product_valuenew = JSONObject()


        val sp = this@Invoice_order.getSharedPreferences("SimpleLogic", 0)
        val user_email = sp.getString("USER_EMAIL", null)

        try {
            product_valuenew.put("cust_code", Global_Data.GLOvel_CUSTOMER_ID)
            product_valuenew.put("order_number", Global_Data.orderid)
            product_valuenew.put("is_favourits", "true")
            product_valuenew.put("email", user_email)
        } catch (e: JSONException) {
            e.printStackTrace()
        }


        val domain = this@Invoice_order.getResources().getString(R.string.service_domain)
        val url = domain + "orders/create_order_favourite"


        var jsObjRequest: JsonObjectRequest? = null
        try {


            Log.d("Server url", "Server url$url")


            jsObjRequest = JsonObjectRequest(Request.Method.POST, url, product_valuenew, Response.Listener { response ->
                Log.i("volley", "response: $response")

                Log.d("jV", "JV length" + response.length())
                //JSONObject json = new JSONObject(new JSONTokener(response));
                try {

                    var response_result = ""
                    if (response.has("result")) {
                        response_result = response.getString("result")
                    } else {
                        response_result = "data"
                    }


                    if (response_result.equals("success", ignoreCase = true)) {


                        //Toast.makeText(Customer_Feed.this, "Media Upload Successfully.", Toast.LENGTH_LONG).show();

                        val `val` = ""



                        (this@Invoice_order as Activity).runOnUiThread { dialog!!.dismiss() }

                     //   Global_Data.Custom_Toast(this@Invoice_order, "Fav", "Yes")

                        //								Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Media_Upload_Successfully), Toast.LENGTH_LONG);
                        //								toast.setGravity(Gravity.CENTER, 0, 0);
                        //								toast.show();

                        //                            Intent a = new Intent(Customer_Feed.this, Neworderoptions.class);
                        //                            startActivity(a);
                        //                            finish();


                    } else {

                        //                            dialog.dismiss();
                        //                            button1.setClickable(true);
                        //                            button1.setEnabled(true);
                        (this@Invoice_order as Activity).runOnUiThread { dialog!!.dismiss() }

                        Global_Data.Custom_Toast(this@Invoice_order, response_result, "Yes")
                        //								Toast toast = Toast.makeText(Customer_Feed.this, response_result, Toast.LENGTH_SHORT);
                        //								toast.setGravity(Gravity.CENTER, 0, 0);
                        //								toast.show();
                        //                                    Intent a = new Intent(context,Order.class);
                        //                                    context.startActivity(a);
                        //                                    ((Activity)context).finish();
                    }

                    //  finish();
                    // }

                    // output.setText(data);
                } catch (e: JSONException) {
                    e.printStackTrace()
                    //  dialog.dismiss();
                }


                // dialog.dismiss();
                //   dialog.dismiss();
            }, Response.ErrorListener { error ->
                Log.i("volley", "error: $error")
                //Toast.makeText(Customer_Feed.this, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();
                (this@Invoice_order as Activity).runOnUiThread { dialog!!.dismiss() }
                Global_Data.Custom_Toast(this@Invoice_order, this@Invoice_order.getResources().getString(R.string.Server_Error), "Yes")
                //                        Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
                //						toast.setGravity(Gravity.CENTER, 0, 0);
                //						toast.show();


                try {
//                    val responseBody = String(error.networkResponse.data, "utf-8")
//                    val jsonObject = JSONObject(responseBody)
                } catch (e: JSONException) {
                    //Handle a malformed json response
                } catch (errorn: UnsupportedEncodingException) {

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

                // dialog.dismiss();
                //                    button1.setClickable(true);
                //                    button1.setEnabled(true);
            })


            val requestQueue = Volley.newRequestQueue(this@Invoice_order)

            val socketTimeout = 2000000//90 seconds - change to what you want
            val policy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            jsObjRequest.retryPolicy = policy
            // requestQueue.se
            //requestQueue.add(jsObjRequest);
            jsObjRequest.setShouldCache(false)
            requestQueue.cache.clear()
            requestQueue.add(jsObjRequest)

        } catch (e: Exception) {
            e.printStackTrace()
            //  dialog.dismiss();
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
                val sp = this@Invoice_order.getSharedPreferences("SimpleLogic", 0)
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


    private fun openFile(url: File) {
        try {
            val uri = Uri.fromFile(url)
            val intent = Intent(Intent.ACTION_VIEW)
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) { // Word document
                intent.setDataAndType(uri, "application/msword")
            } else if (url.toString().contains(".pdf")) { // PDF file
                intent.setDataAndType(uri, "application/pdf")
            } else if (url.toString().contains(".jpg")) { // PDF file
                intent.setDataAndType(uri, "image/*")
            } else if (url.toString().contains(".png")) { // PDF file
                intent.setDataAndType(uri, "image/*")
            } else if (url.toString().contains(".jpeg")) { // PDF file
                intent.setDataAndType(uri, "image/*")
            } else if (url.toString().contains(".gif")) { // PDF file
                intent.setDataAndType(uri, "image/*")
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) { // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) { // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel")
            } else if (url.toString().contains(".txt")) { // Text file
                intent.setDataAndType(uri, "text/plain")
            } else if (url.toString().contains(".mp4") || url.toString().contains(".AVI") || url.toString().contains(".FLV") || url.toString().contains(".WMV") || url.toString().contains(".MOV")) { // Text file
                intent.setDataAndType(uri, "video/mp4")
            } else {
                intent.setDataAndType(uri, "text/csv")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
           // Toast.makeText(applicationContext, "No application found which can open the file", Toast.LENGTH_SHORT).show()
            Global_Data.Custom_Toast(applicationContext, "No application found which can open the file","")
        }
    }

    fun createPdf(dest: String?) {
        if (File(dest).exists()) {
            File(dest).delete()
        }
        try {
            /**
             * Creating Document
             */
            val document = Document()
            // Location to save
            PdfWriter.getInstance(document, FileOutputStream(dest))
            // Open to write
            document.open()
            // Document Settings
            document.setPageSize(PageSize.A4)
            document.addCreationDate()
            document.addAuthor("Metal")
            document.addCreator("Simple Logic")
            /***
             * Variables for further use....
             */
            val mColorAccent = BaseColor(0, 153, 204, 255)
            val mHeadingFontSize = 20.0f
            val mValueFontSize = 24.0f
            /**
             * How to USE FONT....
             */
            val urName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED)
            // LINE SEPARATOR
            val lineSeparator = LineSeparator()
            lineSeparator.lineColor = BaseColor(0, 0, 0, 68)
            // Title Order Details...
// Adding Title....
            val mOrderDetailsTitleFont = Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK)
            val mOrderDetailsTitleChunk = Chunk("Order Details", mOrderDetailsTitleFont)
            val mOrderDetailsTitleParagraph = Paragraph(mOrderDetailsTitleChunk)
            mOrderDetailsTitleParagraph.alignment = Element.ALIGN_CENTER
            document.add(mOrderDetailsTitleParagraph)
            // Fields of Order Details...
// Adding Chunks for Title and value
            val mOrderIdFont = Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent)
            val mOrderIdChunk = Chunk("Order Date:", mOrderIdFont)
            val mOrderIdParagraph = Paragraph(mOrderIdChunk)
            document.add(mOrderIdParagraph)
            val mOrderIdValueFont = Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK)
            val mOrderIdValueChunk = Chunk(o_date, mOrderIdValueFont)
            val mOrderIdValueParagraph = Paragraph(mOrderIdValueChunk)
            document.add(mOrderIdValueParagraph)
            // Adding Line Breakable Space....
            document.add(Paragraph(""))
            // Adding Horizontal Line...
            document.add(Chunk(lineSeparator))
            // Adding Line Breakable Space....
            document.add(Paragraph(""))
            // Fields of Order Details...
            val mOrderDateFont = Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent)
            val mOrderDateChunk = Chunk("Delivery Date:", mOrderDateFont)
            val mOrderDateParagraph = Paragraph(mOrderDateChunk)
            document.add(mOrderDateParagraph)
            val mOrderDateValueFont = Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK)
            val mOrderDateValueChunk = Chunk(need_date, mOrderDateValueFont)
            val mOrderDateValueParagraph = Paragraph(mOrderDateValueChunk)
            document.add(mOrderDateValueParagraph)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))
            // Fields of Order Details...
            val mOrderAcNameFont = Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent)
            val mOrderAcNameChunk = Chunk("Status:", mOrderAcNameFont)
            val mOrderAcNameParagraph = Paragraph(mOrderAcNameChunk)
            document.add(mOrderAcNameParagraph)
            val mOrderAcNameValueFont = Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK)
            val mOrderAcNameValueChunk = Chunk("Processing", mOrderAcNameValueFont)
            val mOrderAcNameValueParagraph = Paragraph(mOrderAcNameValueChunk)
            document.add(mOrderAcNameValueParagraph)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            val name = Chunk("Customer Name :", mOrderAcNameFont)
            val mOrderAcNameParagraphdd = Paragraph(name)
            document.add(mOrderAcNameParagraphdd)
            val mOrderAcNameValueChunkss = Chunk(tv_cust_name!!.text.toString(), mOrderAcNameValueFont)
            val mOrderAcNameValueParagraphss = Paragraph(mOrderAcNameValueChunkss)
            document.add(mOrderAcNameValueParagraphss)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            val details = Chunk("Customer Mobile :", mOrderAcNameFont)
            val mOrderAcNameParagraphd = Paragraph(details)
            document.add(mOrderAcNameParagraphd)
            val mOrderAcNameValueChunks = Chunk(tv_cust_email!!.text.toString(), mOrderAcNameValueFont)
            val mOrderAcNameValueParagraphs = Paragraph(mOrderAcNameValueChunks)
            document.add(mOrderAcNameValueParagraphs)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            val b_details = Chunk("Billing Details : ", mOrderAcNameFont)
            val mOrderAcNameParagraphdds = Paragraph(b_details)
            document.add(mOrderAcNameParagraphdds)
            val mOrderAcNameValueChunkssdfsd = Chunk(tv_cust_billing_address!!.text.toString(), mOrderAcNameValueFont)
            val mOrderAcNameValueParagraphsdsfsdf = Paragraph(mOrderAcNameValueChunkssdfsd)
            document.add(mOrderAcNameValueParagraphsdsfsdf)
//            document.add(Paragraph(""))
//            document.add(Chunk(lineSeparator))
//            document.add(Paragraph(""))

            val table = PdfPTable(5)

            val b_detailsanchor = Chunk("Product Details : ", mOrderAcNameFont)

            val anchor = Anchor("Product Details")
            val catPart = Chapter(Paragraph(b_detailsanchor), 1)

            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            table.spacingBefore=15f

            var c1 = PdfPCell(Phrase("Name"))
            c1.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(c1)

            c1 = PdfPCell(Phrase("Code"))
            c1.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(c1)

            c1 = PdfPCell(Phrase("Quantity"))
            c1.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(c1)

            c1 = PdfPCell(Phrase("Price"))
            c1.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(c1)

            c1 = PdfPCell(Phrase("Amount"))
            c1.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(c1)

            table.headerRows = 1

            if (list!!.size > 0) {
                for (i in list!!.indices) { //PdfPTable table1 = new PdfPTable(7);
                    table.addCell(list!!.get(i).name)
                    table.addCell(list!!.get(i).code)
                    table.addCell(list!!.get(i).quantity)
                    table.addCell(list!!.get(i).price)
                    table.addCell(list!!.get(i).amount)
            }
                catPart.add(table)
                document.add(catPart)

            }

            val amountss = Chunk("Total Amount : " + tv_totalamount!!.text.toString(), mOrderAcNameFont)
            val mOrderAdsd = Paragraph(amountss)
            document.add(mOrderAdsd)


            document.close()
         //   Toast.makeText(mContext, "Created... :)", Toast.LENGTH_SHORT).show()
            FileUtils.openFile(mContext, File(dest))
        } catch (ie: IOException) {
            LOGE("createPdf: Error " + ie.localizedMessage)
        } catch (ie: DocumentException) {
            LOGE("createPdf: Error " + ie.localizedMessage)
        } catch (ae: ActivityNotFoundException) {
         //   Toast.makeText(mContext, "No application found to open this file.", Toast.LENGTH_SHORT).show()
            Global_Data.Custom_Toast(mContext, "No application found to open this file.","")
        }


    }


    /**
     * Requesting multiple permissions (storage and location) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private fun requestStoragePermissionsave() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            createPdf(FileUtils.getAppPath(mContext) + "order_invoice.pdf");
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) { // show alert dialog navigating to Settings
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).withErrorListener { error ->
                //    Toast.makeText(applicationContext, resources.getString(R.string.Error_occurredd).toString() + error.toString(), Toast.LENGTH_SHORT).show()
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Error_occurredd).toString() + error.toString(),"")
                }
                .onSameThread()
                .check()
    }


    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@Invoice_order)
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

    override fun onBackPressed() {
        super.onBackPressed()
        val intentn = Intent(applicationContext, MainActivity::class.java)

        startActivity(intentn)
        finish()
    }

}
