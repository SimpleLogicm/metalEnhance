package com.msimplelogic.activities.kotlinFiles

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.msimplelogic.activities.*
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import com.msimplelogic.adapter.ReturnOrderImageAdapter
import com.msimplelogic.services.getServices
import cpm.simplelogic.helper.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_update_stock_screen3.*
import kotlinx.android.synthetic.main.content_update_stock_screen2.*
import kotlinx.android.synthetic.main.content_update_stock_screen3.*
import kotlinx.android.synthetic.main.content_update_stock_screen3.hedder_theame
import java.io.File
import java.io.IOException


class UpdateStockScreen3 : BaseActivity() {

    var radio_selected = ""
    var context: Context? = null
    var B_flag: Boolean? = null
    var image_check = ""
    private var pictureImagePath = ""
    var RecyclerViewLayoutManager: LayoutManager? = null
    var HorizontalLayout: LinearLayoutManager? = null
    var RecyclerViewHorizontalAdapter: ReturnOrderImageAdapter? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var sp:SharedPreferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_update_stock_screen3)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")

        Global_Data.image_counter = 0
        context = UpdateStockScreen3@ this
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)


        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)

        }

        RecyclerViewLayoutManager = LinearLayoutManager(applicationContext)

        up_recyclerview.setLayoutManager(RecyclerViewLayoutManager)

        cd = ConnectionDetector(applicationContext)

        ups_img_camera.setOnClickListener {

            requestStoragePermission2()
        }

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Kot_Gloval.stock_status)) {
            if (Kot_Gloval.stock_status != "") {
                if (Kot_Gloval.stock_status == "low") {
                    radio_selected = "low"
                    stockradioGroup.check(R.id.radio_low)
                } else
                    if (Kot_Gloval.stock_status == "moderate") {
                        radio_selected = "moderate"
                        stockradioGroup.check(R.id.radio_Moderate)
                    } else
                        if (Kot_Gloval.stock_status == "high") {
                            radio_selected = "high"
                            stockradioGroup.check(R.id.radio_High)
                        }

                stockradioGroup.checkedRadioButtonId
            }
        }


        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Kot_Gloval.stock_details)) {
            st_details.setText(Kot_Gloval.stock_details);
        }

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Kot_Gloval.stock_pick1) || Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Kot_Gloval.stock__pick2) || Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Kot_Gloval.stock__pick3)) {
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Kot_Gloval.stock_pick1)) {
                Global_Data.Number.add(Kot_Gloval.stock_pick1)
                ++Global_Data.image_counter
                // Kot_Gloval.stock_pick1 = "";
            }

            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Kot_Gloval.stock__pick2)) {
                Global_Data.Number.add(Kot_Gloval.stock__pick2)
                ++Global_Data.image_counter
                // Kot_Gloval.stock__pick2 = "";
            }

            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Kot_Gloval.stock__pick3)) {
                Global_Data.Number.add(Kot_Gloval.stock__pick3)
                ++Global_Data.image_counter
                //Kot_Gloval.stock__pick3 = "";
            }


            up_recyclerview.setVisibility(View.VISIBLE)

            RecyclerViewHorizontalAdapter = ReturnOrderImageAdapter(context, Global_Data.Number)
            HorizontalLayout = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            up_recyclerview.setLayoutManager(HorizontalLayout)
            up_recyclerview.setAdapter(RecyclerViewHorizontalAdapter)

        }


        stockradioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_low -> {
                    radio_selected = "low"
                    Kot_Gloval.stock_status = "low"
                }
                R.id.radio_Moderate -> {
                    radio_selected = "moderate"
                    Kot_Gloval.stock_status = "moderate"
                }
                R.id.radio_High -> {
                    radio_selected = "high"
                    Kot_Gloval.stock_status = "high"
                }
            }
        })


        up_stock_update.setOnClickListener {

            if (radio_selected.equals("")) {
                Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.Please_Stock_status), "yes")
            } else
                if (st_details.text.toString().trim().equals("")) {
                    Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.Enter_Detail), "yes")
                } else {
                    for (i in Global_Data.Number.indices) {
                        if (i == 0) {
                            Kot_Gloval.stock_pick1 = Global_Data.Number[0]
                        }
                        if (i == 1) {
                            Kot_Gloval.stock__pick2 = Global_Data.Number[1]
                        }
                        if (i == 2) {
                            Kot_Gloval.stock__pick3 = Global_Data.Number[2]
                        }

                    }
                    cd = ConnectionDetector(applicationContext)
                    isInternetPresent = cd!!.isConnectingToInternet
                    if (isInternetPresent) {
                        getServices.SYNStock(this@UpdateStockScreen3, radio_selected, st_details.text.toString().trim(), Kot_Gloval.stock_pick1, Kot_Gloval.stock__pick2, Kot_Gloval.stock__pick3)

                        Kot_Gloval.stock_pick1 = "";
                        Kot_Gloval.stock__pick2 = "";
                        Kot_Gloval.stock__pick3 = ";"
                    } else {
                        Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")
                    }

                }

        }
    }

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
                val sp: SharedPreferences = this@UpdateStockScreen3.getSharedPreferences("SimpleLogic", 0)
                try {
                    val target = Math.round(sp.getFloat("Target", 0f))
                    val achieved = Math.round(sp.getFloat("Achived", 0f))
                    val age_float = sp.getFloat("Achived", 0f) / sp.getFloat("Target", 0f) * 100
                    targetNew = if (age_float.toString().equals("infinity", ignoreCase = true)) {
                        val age = Math.round(age_float)
                        if (Global_Data.rsstr.length > 0) {
                            "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [infinity") + "%" + "]"
                        } else {
                            "T/A : Rs " + String.format("$target/$achieved [infinity") + "%" + "]"
                        }
                    } else {
                        val age = Math.round(age_float)
                        if (Global_Data.rsstr.length > 0) {
                            "T/A : " + Global_Data.rsstr + String.format("$target/$achieved [$age") + "%" + "]"
                        } else {
                            "T/A : Rs " + String.format("$target/$achieved [$age") + "%" + "]"
                        }
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

    private fun isDeviceSupportCamera(): Boolean {
        return if (applicationContext.packageManager.hasSystemFeature(
                        PackageManager.FEATURE_CAMERA_ANY)) { // this device has a camera
            true
        } else { // no camera on this device
            false
        }
    }

    private fun requestStoragePermission2() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            B_flag = isDeviceSupportCamera()
                            if (B_flag == true) {
                                if (Global_Data.image_counter <= 2) { //final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Choose_from_Gallery), getResources().getString(R.string.Cancel)};
                                    val options = arrayOf<CharSequence>(resources.getString(R.string.Take_Photo), resources.getString(R.string.Cancel))
                                    val builder = AlertDialog.Builder(context)
                                    builder.setTitle(resources.getString(R.string.Add_Photo))
                                    builder.setItems(options) { dialog, item ->
                                        if (options[item] == resources.getString(R.string.Take_Photo)) {
                                            image_check = "photo"
                                            var photoFile: File? = null
                                            try {
                                                photoFile = createImageFile()
                                            } catch (ex: IOException) { // Error occurred while creating the File
                                                Log.i("Image TAG", "IOException")
                                                pictureImagePath = ""
                                            }
                                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                            val photoURI = FileProvider.getUriForFile(this@UpdateStockScreen3,
                                                    BuildConfig.APPLICATION_ID + ".provider",
                                                    photoFile!!)
                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            startActivityForResult(cameraIntent, 1)
                                        } else if (options[item] == resources.getString(R.string.Cancel)) {
                                            dialog.dismiss()
                                        }
                                    }
                                    builder.show()
                                } else {
                                    Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.pick_validation3), "")
                                }
                            } else {
                                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.no_camera), "")
                                //Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_camera), Toast.LENGTH_LONG).show();
                            }
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
                    Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Error_occurredd).toString() + error.toString(), "")
                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                }
                .onSameThread()
                .check()
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(context)
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

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? { // Create an image file name
        val imageFileName = "Return"
        var storageDir: File? = null
        storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AnchorUpdateStock")
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        val image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",  // suffix
                storageDir // directory
        )
        pictureImagePath = "file:" + image.absolutePath

        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            up_recyclerview.setVisibility(View.VISIBLE)
            Global_Data.Number.add(pictureImagePath)
            RecyclerViewHorizontalAdapter = ReturnOrderImageAdapter(context, Global_Data.Number)
            HorizontalLayout = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            up_recyclerview.setLayoutManager(HorizontalLayout)
            up_recyclerview.setAdapter(RecyclerViewHorizontalAdapter)
            ++Global_Data.image_counter
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            try {
                val selectedImage = data!!.data
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val c = contentResolver.query(selectedImage!!, filePath, null, null, null)
                c!!.moveToFirst()
                val columnIndex = c.getColumnIndex(filePath[0])
                pictureImagePath = "file:" + c.getString(columnIndex)
                c.close()
                //new Expenses.LongOperation().execute();
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }


    override fun onBackPressed() {
        Kot_Gloval.stock_details = st_details.text.toString()
        Kot_Gloval.stock_local_image_flag = "yes"

        if (Global_Data.Number.size > 0) {
            for (i in 0 until Global_Data.Number.size) {

                if (i == 0) {
                    Kot_Gloval.stock_pick1 = Global_Data.Number.get(i)
                }

                if (i == 1) {
                    Kot_Gloval.stock__pick2 = Global_Data.Number.get(i)
                }

                if (i == 2) {
                    Kot_Gloval.stock__pick3 = Global_Data.Number.get(i)
                }


            }
        }
        val i = Intent(UpdateStockScreen3@ this, UpdateStockScreen2::class.java)
        startActivity(i)
        finish()
    }

}
