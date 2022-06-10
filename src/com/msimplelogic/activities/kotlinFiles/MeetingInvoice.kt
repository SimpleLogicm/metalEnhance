package com.msimplelogic.activities.kotlinFiles

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.helper.FileUtils
import com.msimplelogic.helper.LogUtils.LOGE
import com.msimplelogic.helper.PermissionsChecker
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_meeting_invoice.*
import kotlinx.android.synthetic.main.content_meeting_invoice.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MeetingInvoice : BaseActivity() {

    var mContext: Context? = null

    var checker: PermissionsChecker? = null
    var o_date = "";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_invoice)


        setSupportActionBar(mmtoolbar)
        setTitle("Promotional Meeting")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        setTitle(resources.getString(R.string.Invoice))

        mContext = MeetingInvoice@ this;


        val spf = getSharedPreferences("SimpleLogic", 0)

        Action_Item_Value.setText(spf.getString("Action_Item_Value", null))
        Assigned_To_Value.setText(spf.getString("Assigned_To_Value", null))
        Due_Date_Value.setText(spf.getString("Due_Date_Value", null))
        Status_value.setText(spf.getString("Status_value", null))
        Description_value.setText(spf.getString("Description_value", null))



        m_btn_share!!.setOnClickListener {
            requestStoragePermissionsave();
        }

        m_btn_close!!.setOnClickListener {
            // getTargetDataservice(context);
            val intentn = Intent(applicationContext, ActivityPlanner::class.java)

            startActivity(intentn)
            finish()
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
                val sp = this@MeetingInvoice.getSharedPreferences("SimpleLogic", 0)
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
           //Toast.makeText(applicationContext, "No application found which can open the file", Toast.LENGTH_SHORT).show()
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
            val mValueFontSize = 26.0f
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
            val mOrderDetailsTitleChunk = Chunk("Agenda", mOrderDetailsTitleFont)
            val mOrderDetailsTitleParagraph = Paragraph(mOrderDetailsTitleChunk)
            mOrderDetailsTitleParagraph.alignment = Element.ALIGN_CENTER
            document.add(mOrderDetailsTitleParagraph)
            // Fields of Order Details...
// Adding Chunks for Title and value
            val mOrderIdFont = Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent)
            val mOrderIdChunk = Chunk("Action Item :", mOrderIdFont)
            val mOrderIdParagraph = Paragraph(mOrderIdChunk)
            document.add(mOrderIdParagraph)
            val mOrderIdValueFont = Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK)
            val mOrderIdValueChunk = Chunk(Action_Item_Value.text.toString(), mOrderIdValueFont)
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
            val mOrderDateChunk = Chunk("Assigned To :", mOrderDateFont)
            val mOrderDateParagraph = Paragraph(mOrderDateChunk)
            document.add(mOrderDateParagraph)
            val mOrderDateValueFont = Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK)
            val mOrderDateValueChunk = Chunk(Assigned_To_Value.text.toString(), mOrderDateValueFont)
            val mOrderDateValueParagraph = Paragraph(mOrderDateValueChunk)
            document.add(mOrderDateValueParagraph)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))
            // Fields of Order Details...
            val mOrderAcNameFont = Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent)
            val mOrderAcNameChunk = Chunk("Due Date ", mOrderAcNameFont)
            val mOrderAcNameParagraph = Paragraph(mOrderAcNameChunk)
            document.add(mOrderAcNameParagraph)
            val mOrderAcNameValueFont = Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK)
            val mOrderAcNameValueChunk = Chunk(Due_Date_Value.text.toString(), mOrderAcNameValueFont)
            val mOrderAcNameValueParagraph = Paragraph(mOrderAcNameValueChunk)
            document.add(mOrderAcNameValueParagraph)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))


            val name = Chunk("Status value :", mOrderAcNameFont)
            val mOrderAcNameParagraphdd = Paragraph(name)
            document.add(mOrderAcNameParagraphdd)
            val mOrderAcNameValueChunkss = Chunk(Status_value!!.text.toString(), mOrderAcNameValueFont)
            val mOrderAcNameValueParagraphss = Paragraph(mOrderAcNameValueChunkss)
            document.add(mOrderAcNameValueParagraphss)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            val details = Chunk("Description :", mOrderAcNameFont)
            val mOrderAcNameParagraphd = Paragraph(details)
            document.add(mOrderAcNameParagraphd)
            val mOrderAcNameValueChunks = Chunk(Description_value!!.text.toString(), mOrderAcNameValueFont)
            val mOrderAcNameValueParagraphs = Paragraph(mOrderAcNameValueChunks)
            document.add(mOrderAcNameValueParagraphs)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))


            document.close()
           // Toast.makeText(mContext, "Created... :)", Toast.LENGTH_SHORT).show()
            Global_Data.Custom_Toast(mContext, "Created... :)","")
            FileUtils.openFile(mContext, File(dest))
        } catch (ie: IOException) {
            LOGE("createPdf: Error " + ie.localizedMessage)
        } catch (ie: DocumentException) {
            LOGE("createPdf: Error " + ie.localizedMessage)
        } catch (ae: ActivityNotFoundException) {
           // Toast.makeText(mContext, "No application found to open this file.", Toast.LENGTH_SHORT).show()
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
                   // Toast.makeText(applicationContext, resources.getString(R.string.Error_occurredd).toString() + error.toString(), Toast.LENGTH_SHORT).show()
                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Error_occurredd).toString() + error.toString(), "")
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
        val builder = AlertDialog.Builder(this@MeetingInvoice)
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
        val intentn = Intent(applicationContext, Promotional_meetings::class.java)

        startActivity(intentn)
        finish()
    }

}
