package com.msimplelogic.activities.kotlinFiles

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.jsibbold.zoomage.ZoomageView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.BuildConfig
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.services.getServices
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_marketing_material.*
import kotlinx.android.synthetic.main.content_competitor_analysis.*
import kotlinx.android.synthetic.main.content_marketing_material.*
import kotlinx.android.synthetic.main.content_marketing_material.hedder_theame
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class MarketingMaterial : BaseActivity() {

    var mget_icon: ImageView? = null
    var marketingm_click: Button? = null
    internal var B_flag: Boolean? = null
    internal var image_check = ""
    internal var pictureImagePath = ""
    internal var  pictureImagePath_new = ""
    var img_show:ImageView?=null
    var crossimg:ImageView?=null
    var imgdialog: ZoomageView?=null
    val arrayListautocomplete = ArrayList<String>()
    var sp:SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketing_material)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null)   //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //editText1 = findViewById(R.id.editText1)
//        POSMText = findViewById(R.id.POSMText)
//        ChillersText = findViewById(R.id.ChillersText)
//        HangersText = findViewById(R.id.HangersText)
//        OthersText = findViewById(R.id.OthersText)
//        txtRemark = findViewById(R.id.txtRemark)
        mget_icon = findViewById(R.id.customer_services_picture)
        marketingm_click = findViewById(R.id.marketingm_click)
        img_show = findViewById(R.id.img_show)
        crossimg = findViewById(R.id.crossimg)

        img_show!!.setVisibility(View.INVISIBLE)
        crossimg!!.setVisibility(View.INVISIBLE)
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp!!.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)

        }

        img_show!!.setOnClickListener(View.OnClickListener {
            if (pictureImagePath_new !== "") {
                val builder = AlertDialog.Builder(this@MarketingMaterial)
                val inflater = layoutInflater
                val dialogLayout = inflater.inflate(R.layout.dialogeimage, null)
                imgdialog = dialogLayout.findViewById<ZoomageView>(R.id.imageView)
                Glide.with(this@MarketingMaterial).load(pictureImagePath_new).into(imgdialog!!)
                builder.setPositiveButton("OK", null)
                builder.setView(dialogLayout)
                builder.show()
            }
        })

        editText1?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (editText1!!.length() == 1) {
                    autocompletetextdatafetch(s)
                }
               // autocompletetextdatafetch(s)
            }
        })

        marketingm_click!!.setOnClickListener(View.OnClickListener {
            if(editText1.text.toString().length>0)
            {
                getServices.SyncMarketingMaterial(this@MarketingMaterial,  pictureImagePath,editText1.text.toString(),POSMText.text.toString(),ChillersText.text.toString(),HangersText.text.toString(),OthersText.text.toString(),txtRemark.text.toString())
            }else{
                Global_Data.Custom_Toast(this@MarketingMaterial, "Please Enter Competitor Name", "Yes")
            }
        })

        crossimg!!.setOnClickListener(View.OnClickListener { deletedialog() })
        mget_icon!!.setOnClickListener {
            requestStoragePermission2()
        }

    }

    private fun requestStoragePermission2() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granfted
                        if (report.areAllPermissionsGranted()) {

                            B_flag = isDeviceSupportCamera()

                            if (B_flag == true) {

                                //final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Choose_from_Gallery), getResources().getString(R.string.Cancel)};
                                val options = arrayOf<CharSequence>(resources.getString(R.string.Take_Photo), resources.getString(R.string.Cancel))

                                val builder = AlertDialog.Builder(this@MarketingMaterial)

                                builder.setTitle(resources.getString(R.string.Add_Photo))

                                builder.setItems(options) { dialog, item ->
                                    if (options[item] == resources.getString(R.string.Take_Photo)) {
                                        image_check = "photo"

                                        var photoFile: File? = null
                                        try {
                                            photoFile = createImageFile()
                                        } catch (ex: IOException) {
                                            // Error occurred while creating the File
                                            Log.i("Image TAG", "IOException")
                                            pictureImagePath = ""
                                        }

                                        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                                        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                        val photoURI = FileProvider.getUriForFile(this@MarketingMaterial,
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
                                Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.no_camera), "")
                                //Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_camera), Toast.LENGTH_LONG).show();
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
                    Global_Data.Custom_Toast(applicationContext, resources.getString(R.string.Error_occurredd) + error.toString(), "")
                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                }
                .onSameThread()
                .check()
    }


    private fun isDeviceSupportCamera(): Boolean {
        return if (applicationContext.packageManager.hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            true
        } else {
            // no camera on this device
            false
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val imageFileName = "Marketing"
        var storageDir: File? = null

            storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Marketingmaterial")

        if (!storageDir.exists()) {
            storageDir.mkdir()
        }

        val image = File.createTempFile(
                imageFileName, // prefix
                ".jpg", // suffix
                storageDir      // directory
        )

        // Save a file: path for use with ACTION_VIEW intents
        pictureImagePath = "file:" + image.absolutePath
        pictureImagePath_new = image.absolutePath
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image
    }
    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@MarketingMaterial)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // if the result is capturing Image
        //		try {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val imgFils = File(pictureImagePath_new)
            if (imgFils.exists()) {


                crossimg!!.setVisibility(View.VISIBLE)
                img_show!!.setVisibility(View.VISIBLE)

                Glide.with(this@MarketingMaterial).load(pictureImagePath_new).into(img_show!!)

            }


        }
    }

    override fun onBackPressed() {

        val i = Intent(applicationContext, CompetitorAnalysis::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        finish()
    }

    private fun deletedialog() {
        val alertDialog = AlertDialog.Builder(this@MarketingMaterial).create() //Read Update
        alertDialog.setTitle(resources.getString(R.string.Warning))
        alertDialog.setMessage(resources.getString(R.string.image_dialog_warning_message))
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, resources.getString(R.string.Yes)) { dialog, which ->
            val file = File(pictureImagePath_new)
            val delete = file.delete()

            val file1 = File(pictureImagePath)
            val delete1 = file1.delete()

            img_show!!.setVisibility(View.INVISIBLE)
            crossimg!!.setVisibility(View.INVISIBLE)

            Global_Data.Custom_Toast(this@MarketingMaterial, resources.getString(R.string.image_dialog_delete_success), "Yes")
            //				Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.image_dialog_delete_success),
            //						Toast.LENGTH_LONG);
            //				toast.setGravity(Gravity.CENTER, 0, 0);
            //				toast.show();

            //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, resources.getString(R.string.No_Button_label)) { dialog, which -> dialog.cancel() }
        alertDialog.show()

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
                val sp = this@MarketingMaterial.getSharedPreferences("SimpleLogic", 0)
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

    private fun autocompletetextdatafetch(s: CharSequence) {
        //val url: String = "http://95aeb3c8.ngrok.io/metal/api/v1/marketing_materials/get_competitor_names_on_search?email=sujitkumar.giradkar@simplelogic.in&string=" + s + "&customer_code=" + "249"

        val domain = resources.getString(R.string.service_domain)

        val spf: SharedPreferences = this@MarketingMaterial.getSharedPreferences("SimpleLogic", 0)
        val user_email = spf.getString("USER_EMAIL", null)

        val shop_code: String = spf.getString("shopcode", null)!!

        Log.i("volley", "domain: $domain")

        var service_url = ""
        service_url = domain + "marketing_materials/get_competitor_names_on_search?email=" + user_email + "&string=" + s + "&customer_code=" + shop_code

        Log.i("volley", "service_url: $service_url")

        val stringRequest = StringRequest(Request.Method.GET, service_url,
                Response.Listener { response ->

                    arrayListautocomplete.clear()

                    try {

                        val obj = JSONObject(response)


                        val heroArray = obj.getJSONArray("orders")
                        


                        for (i in 0 until heroArray.length()) {

                            val hero = heroArray.getString(i)

                            arrayListautocomplete.add(hero)
                        }

                        //  runOnUiThread()
                        this@MarketingMaterial.runOnUiThread(Runnable {
                            val adapter = ArrayAdapter(this,  R.layout.autocomplete, arrayListautocomplete)

                            editText1!!.setAdapter(adapter)
                        })


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                   // Toast.makeText(getApplicationContext(), error.message, Toast.LENGTH_SHORT).show()
                    Global_Data.Custom_Toast(getApplicationContext(), error.message,"")
                })

        val requestQueue = Volley.newRequestQueue(getApplicationContext())

        requestQueue.add<String>(stringRequest)
    }


}


