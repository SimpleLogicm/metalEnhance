package com.msimplelogic.activities.kotlinFiles

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Check_Null_Value
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.helper.OnSwipeTouchListener
import com.msimplelogic.services.getServices
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_myprofile.*
import kotlinx.android.synthetic.main.content_myprofile.*
import kotlinx.android.synthetic.main.content_order__customer_list.*
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Myprofile : BaseActivity() {
    var choice = 0
    private var dialog: ProgressDialog? = null
    var B_flag: Boolean? = null
    private var picUri: Uri? = null
    private var pictureImagePath = ""
    private var pictureImagePath_new = ""
    val PIC_CROP = 3
    var filePath: String? = null
    var myCalendar: Calendar? = null
    var date: DatePickerDialog.OnDateSetListener? = null
    var sp:SharedPreferences?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myprofile)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        dialog = ProgressDialog(this@Myprofile, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        dialog!!.setMessage("Please Wait....")
        dialog!!.setTitle("Metal App")
        dialog!!.setCancelable(false)
        dialog!!.show()
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)

        if (current_theme == 1) {

            hedder_theamemy.setImageResource(R.drawable.dark_hedder)
        }
        getProfileData()

        btn_edit.setOnClickListener { view ->
            requestStoragePermission()
        }

        btn_profilenext.setOnClickListener { view ->
            if(btn_profilenext.text.equals("Done >>"))
            {
                if(et_firstname.text.toString().length>0 && et_lastname.text.toString().length>0 && et_profilecontact.text.toString().length>0)
                {
                    getServices.SyncEditProfile(this@Myprofile,  pictureImagePath,et_firstname.text.toString(),et_lastname.text.toString(),et_designation.text.toString(),et_profilecontact.text.toString(),et_profileaddress.text.toString(),et_panno.text.toString(),et_adhaarno.text.toString(),et_vehicleno.text.toString(),et_birthday.text.toString())

                }else{
                    Global_Data.Custom_Toast(applicationContext, "Please Enter Personal Details", "Yes")
                }

            }else{
                   Global_Data.Custom_Toast(applicationContext, "Swipe to next", "Yes")
                 }
        }

        myCalendar = Calendar.getInstance()
        date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar?.set(Calendar.YEAR, year)
            myCalendar?.set(Calendar.MONTH, monthOfYear)
            myCalendar?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

        et_birthday.setOnClickListener(View.OnClickListener {
            DatePickerDialog(this@Myprofile, date, myCalendar!!.get(Calendar.YEAR), myCalendar!!.get(Calendar.MONTH),
                    myCalendar!!.get(Calendar.DAY_OF_MONTH)).show()
        })

        swipe_scroll.setOnTouchListener(object : OnSwipeTouchListener(this@Myprofile) {
            override fun onSwipeRight() {
                when (choice) {
                    2 -> {
                        personal_details_card.setVisibility(View.GONE)
                        other_details_card.setVisibility(View.VISIBLE)
                        btn_profilenext.setText("Done >>")
                        choice--
                    }
                    1 -> {
                        personal_details_card.setVisibility(View.VISIBLE)
                        other_details_card.setVisibility(View.GONE)
                        btn_profilenext.setText("Swipe to Next >>")
                        choice--
                    }
//                    else -> {
//                    }
                }
            }

            override fun onSwipeLeft() {
                when (choice) {
                    0 -> {
                        personal_details_card.setVisibility(View.GONE)
                        other_details_card.setVisibility(View.VISIBLE)
                        btn_profilenext.setText("Done >>")
                        choice++
                    }
//                    1 -> {
//
//                        personal_details_card.setVisibility(View.VISIBLE)
//                        other_details_card.setVisibility(View.GONE)
//                        btn_profilenext.setText("Swipe to Next >>")
//                        choice++
//                    }
//                    else -> {
//                    }
                }
                // swipe.setImageResource(R.drawable.timeliner2);
            }
        })

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
                val sp = this@Myprofile.getSharedPreferences("SimpleLogic", 0)
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

    fun getProfileData() {
        try {
            val spf: SharedPreferences = this@Myprofile.getSharedPreferences("SimpleLogic", 0)
            val user_email = spf.getString("USER_EMAIL", null)

            var jsObjRequest: JsonObjectRequest? = null
            try {
                val domain = resources.getString(R.string.service_domain)
                Log.d("getUserData url", "getUserData url" + domain + "users/display_user_profile?email=" + user_email)
                jsObjRequest = object : JsonObjectRequest(domain + "users/display_user_profile?email=" + user_email, null, Response.Listener { response ->
                    Log.i("volley", "response: $response")
                    Log.d("jV", "JV length" + response.length())
                    try {
                        var response_result = ""
                        if (response.has("result")) {
                            response_result = response.getString("result")
                          //  Toast.makeText(this@Myprofile, "" + response_result, Toast.LENGTH_SHORT).show()
                            Global_Data.Custom_Toast(this@Myprofile, "" + response_result,"")
                        } else {

                            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(response.getString("first_name"))) {
                                et_firstname.setText("")
                            } else {
                                et_firstname.setText(response.getString("first_name"))
                            }

                            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(response.getString("last_name"))) {
                                et_lastname.setText("")
                            } else {
                                et_lastname.setText(response.getString("last_name"))
                                tv_fullname.setText(response.getString("first_name")+" "+response.getString("last_name"))
                            }

                                    if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(response.getString("email"))) {
                                        et_emailid.setText("")
                                    } else {
                                        et_emailid.setText(response.getString("email"))
                                    }

                            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(response.getString("aadhar_no"))) {
                                et_adhaarno.setText("")
                            } else {
                                et_adhaarno.setText(response.getString("aadhar_no"))
                            }

                            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(response.getString("designation"))) {
                                et_designation.setText("")
                            } else {

                                val pref = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE)
                                val edit = pref.edit()
                                edit.putString("designation", response.getString("designation"))
                                edit.commit()

                                et_designation.setText(response.getString("designation"))


                            }

                            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(response.getString("mob_no"))) {
                                et_profilecontact.setText("")
                            } else {
                                et_profilecontact.setText(response.getString("mob_no"))
                            }

                            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(response.getString("address"))) {
                                et_profileaddress.setText("")
                            } else {
                                et_profileaddress.setText(response.getString("address"))
                            }

                            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(response.getString("pan_card_no"))) {
                                et_panno.setText("")
                            } else {
                                et_panno.setText(response.getString("pan_card_no"))
                            }

                            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(response.getString("vehicle_no"))) {
                                et_vehicleno.setText("")
                            } else {
                                et_vehicleno.setText(response.getString("vehicle_no"))
                            }

                            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(response.getString("birth_date"))) {
                                et_birthday.setText("")
                            } else {
                                et_birthday.setText(response.getString("birth_date"))
                            }

                            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(response.getString("profile_image"))) {
                                Glide.with(getApplicationContext())
                                        .load(response.getString("profile_image"))
                                        //.thumbnail(Glide.with(this).load("file:///android_asset/loading.gif"))
                                        //.centerCrop()
                                        .placeholder(R.drawable.user)
                                        .error(R.drawable.user)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(user_image)
                            } else {

                                val pref = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE)
                                val edit = pref.edit()
                                edit.putString("img_path", response.getString("profile_image"))
                                edit.commit()

                                Glide.with(getApplicationContext())
                                        .load(response.getString("profile_image"))
                                        .thumbnail(Glide.with(this).load("file:///android_asset/loading.gif"))
                                        //.centerCrop()
                                        .placeholder(R.drawable.user)
                                        .error(R.drawable.user)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(user_image)
                            }

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        dialog?.dismiss()
                    }
                    dialog?.dismiss()
                }, Response.ErrorListener { error ->
                    //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    if (error is TimeoutError || error is NoConnectionError) {

                        Global_Data.Custom_Toast(this@Myprofile,
                                "your internet connection is not working, saving locally. Please sync when Internet is available","")
                    } else if (error is AuthFailureError) {

                        Global_Data.Custom_Toast(this@Myprofile,
                                "Server AuthFailureError  Error","yes")
                    } else if (error is ServerError) {

                        Global_Data.Custom_Toast(this@Myprofile,
                                "Server   Error","")
                    } else if (error is NetworkError) {

                        Global_Data.Custom_Toast(this@Myprofile,
                                "your internet connection is not working, saving locally. Please sync when Internet is available","")
                    } else if (error is ParseError) {

                        Global_Data.Custom_Toast(this@Myprofile,
                                "ParseError   Error","")
                    } else {
                    //    Toast.makeText(this@Myprofile, error.message, Toast.LENGTH_LONG).show()
                        Global_Data.Custom_Toast(this@Myprofile, error.message,"")
                    }
                    dialog?.dismiss()
                    // finish();
                }) { //                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String>  params = new HashMap<String, String>();
//                        params.put("PASSWORD", password_text.getText().toString().trim());
//
//                        return params;
//                    }
                }
                val requestQueue = Volley.newRequestQueue(this@Myprofile)
                val socketTimeout = 150000 //90 seconds - change to what you want
                val policy: RetryPolicy = DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                jsObjRequest.setRetryPolicy(policy)
                // requestQueue.se
//requestQueue.add(jsObjRequest);
                jsObjRequest.setShouldCache(false)
                requestQueue.cache.clear()
                requestQueue.add(jsObjRequest)
            } catch (e: Exception) {
                e.printStackTrace()
                dialog?.dismiss()
            }
        } catch (e: Exception) { // TODO: handle exception
            Log.e("DATA", e.message.toString())
        }
    }

    private fun requestStoragePermission() {
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
                                val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
                                val builder = AlertDialog.Builder(this@Myprofile)
                                builder.setTitle("Add Photo!")
                                builder.setItems(options) { dialog, item ->
                                    if (options[item] == "Take Photo") { // image_check = "photo";
                                        var photoFile: File? = null
                                        try {
                                            photoFile = createImageFile()
                                            picUri = Uri.fromFile(photoFile)
                                        } catch (ex: IOException) { // Error occurred while creating the File
                                            Log.i("Image TAG", "IOException")
                                            //pictureImagePath = "";
                                        }
                                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                                        startActivityForResult(cameraIntent, 1)
                                    } else if (options[item] == "Choose from Gallery") {
                                        val pictureIntent = Intent(Intent.ACTION_GET_CONTENT)
                                        pictureIntent.type = "image/*"
                                        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE)
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                            val mimeTypes = arrayOf("image/jpeg", "image/png")
                                            pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                                        }
                                        startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), 2)
                                        // image_check = "gallery";
                                        //                                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        //
                                        //                                            startActivityForResult(intent, 2);
                                    } else if (options[item] == "Cancel") {
                                        dialog.dismiss()
                                    }
                                }
                                builder.show()
                            } else {
                            //    Toast.makeText(applicationContext, "no camera on this device", Toast.LENGTH_LONG).show()
                                Global_Data.Custom_Toast(applicationContext, "no camera on this device","")
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
                    //Toast.makeText(applicationContext, "Error occurred! $error", Toast.LENGTH_SHORT).show()
                Global_Data.Custom_Toast(applicationContext, "Error occurred! $error","")
                }
                .onSameThread()
                .check()
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@Myprofile)
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

    private fun isDeviceSupportCamera(): Boolean { // this device has a camera
// no camera on this device
        return applicationContext.packageManager.hasSystemFeature(
                PackageManager.FEATURE_CAMERA)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? { // Create an image file name
        val imageFileName = "prifle_pic"
        val storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "D_CUstomer_Profile")
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        //        else
//        {
//            storageDir.delete();
//            storageDir.mkdir();
//        }
        val image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",  // suffix
                storageDir // directory
        )
        // Save a file: path for use with ACTION_VIEW intents
        pictureImagePath = "file:" + image.absolutePath
        pictureImagePath_new = image.absolutePath
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    val uri: Uri? = picUri
                    //                file = new File(pictureImagePath);
//                Uri imageUri = Uri.fromFile(file);
                    //performCrop(uri)
                    // new doFileUpload().execute(pictureImagePath_new);

                Glide.with(getApplicationContext())
                        .load(pictureImagePath)
                        .thumbnail(Glide.with(this).load("file:///android_asset/loading.gif"))
                        //.centerCrop()
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(user_image)

                    val imageFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "D_CUstomer_Profile")
                    val list = imageFile.listFiles()
                    for (i in list.indices) {
                        if (!pictureImagePath_new.equals(list[i].toString(), ignoreCase = true)) {
                            list[i].delete()
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } else if (requestCode == 2) {
                try {
//                    val selectedImage = data!!.data
//                    var filePath = arrayOf(MediaStore.Images.Media.DATA)
//                    val c = contentResolver.query(selectedImage, filePath, null, null, null)
//                    c.moveToFirst()
//                    val columnIndex = c.getColumnIndex(filePath[0])
//                    pictureImagePath = "file:" + c.getString(columnIndex)
//                    pictureImagePath_new = c.getString(columnIndex)

                    if (data != null)
                    {
                        val contentURI = data!!.data
                        try
                        {
                            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                            pictureImagePath = saveImage(bitmap)


                            //Toast.makeText(this@Myprofile, "Image Saved!"+path, Toast.LENGTH_SHORT).show()
                            user_image!!.setImageBitmap(bitmap)

                        }
                        catch (e: IOException) {
                            e.printStackTrace()
                            //Toast.makeText(this@Myprofile, "Failed!", Toast.LENGTH_SHORT).show()
                        }

                    }




                    //performCrop(selectedImage);
//Uri sourceUri = data.getData();
                    //val file: File = getImageFile()
                    //val destinationUri = Uri.fromFile(file)
                    //openCropActivity(selectedImage, destinationUri)
                    //new doFileUpload().execute(pictureImagePath_new);
//                Glide.with(getApplicationContext())
//                        .load(pictureImagePath_new)
//                        .thumbnail(0.5f)
//                        //.centerCrop()
//                        .placeholder(R.drawable.user)
//                        .error(R.drawable.user)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(user_image)
//                    c.close()
//                    val imageFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "D_CUstomer_Profile")
//                    val list = imageFile.listFiles()
//                    for (i in list.indices) {
//                        if (!pictureImagePath_new.equals(list[i].toString(), ignoreCase = true)) {
//                            list[i].delete()
//                        }
//                    }
                    val imageFileName = "prifle_pic"
                    val storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "D_CUstomer_Profile")
                    if (!storageDir.exists()) {
                        storageDir.mkdir()
                    }
                    //copyFileOrDirectory(pictureImagePath_new,storageDir.toString());
//new Expenses.LongOperation().execute();
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
//            else if (requestCode == PIC_CROP) { //get the returned data
//                var imagePath: String? = ""
//                if (data!!.extras == null) { //  bitmap = (Bitmap)data.getExtras().get("data");
////Bundle extras = data.getExtras().get("data");;
////            //get the cropped bitmap
//                    var thePic: Bitmap? = null
//                    try {
//                        thePic = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                    profile_image.setImageBitmap(thePic)
//                    val bytes = ByteArrayOutputStream()
//                    thePic!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//                    val path = MediaStore.Images.Media.insertImage(this.contentResolver, thePic, "", null)
//                    //picUri = (Uri) extras.get("data");
//                    val imageuri: Uri = getImageUri(this@Myprofile, thePic)
//                    imagePath = getRealPathFromURI(imageuri)
//                }
//                else {
//                    val extras = data.extras
//                    //            //get the cropped bitmap
//                    val thePic = extras["data"] as Bitmap
//                    profile_image.setImageBitmap(thePic)
//                    val bytes = ByteArrayOutputStream()
//                    thePic.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//                    val path = MediaStore.Images.Media.insertImage(this.contentResolver, thePic, "", null)
//                    //picUri = (Uri) extras.get("data");
//                    val imageuri: Uri = getImageUri(this@Myprofile, thePic)
//                    imagePath = getRealPathFromURI(imageuri)
//                }
//                //
////            Uri selectedImage = data.getData();
////display the returned cropped image
////Uri imageFileUri = Uri.parse(path);
////BitMapToString(thePic);
////getRealPathFromURI_API19(this,imageFileUri);
////Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
//// new doFileUpload().execute(pictureImagePath_new);
////            picUri = data.getData();
////
////            GlideApp.with(getApplicationContext())
////                        .load(picUri.toString())
////                        .thumbnail(0.5f)
////                        //.centerCrop()
////                        .placeholder(R.drawable.my_profile)
////                        .error(R.drawable.my_profile)
////                        .diskCacheStrategy(DiskCacheStrategy.ALL)
////                        .into(profile_image);
//                val imageFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "D_CUstomer_Profile")
//                val list = imageFile.listFiles()
//                for (i in list.indices) {
//                    if (!pictureImagePath_new.equals(list[i].toString(), ignoreCase = true)) {
//                        list[i].delete()
//                    }
//                }
//                //doFileUpload().execute(imagePath)
//            }
//            else if (requestCode == UCrop.REQUEST_CROP) {
//                if (data != null) {
//                    val uri: Uri = UCrop.getOutput(data)
//                    showImage(uri)
//                }
//            }
        }
        //        if (requestCode == GALLERY) {
//            Log.d("what","gale");
//            if (data != null) {
//                Uri contentURI = data.getData();
//
//                String selectedVideoPath = getPath(contentURI);
//                Log.d("path",selectedVideoPath);
//                saveVideoToInternalStorage(selectedVideoPath);
//                videoAttach.setVideoURI(contentURI);
//                videoAttach.requestFocus();
//                videoAttach.start();
//            }
//
//        } else if (requestCode == CAMERA) {
//            Uri contentURI = data.getData();
//            String recordedVideoPath = getPath(contentURI);
//            Log.d("frrr",recordedVideoPath);
//            saveVideoToInternalStorage(recordedVideoPath);
//            videoAttach.setVideoURI(contentURI);
//            videoAttach.requestFocus();
//            videoAttach.start();
//        }
    }

    private fun performCrop(aaa: Uri) {
        try { //call the standard crop action intent (the user device may not support it)
            val cropIntent = Intent("com.android.camera.action.CROP")
            //cropIntent.setClassName("com.android.camera", "com.android.camera.CropImage");
//indicate image type and Uri
            cropIntent.setDataAndType(aaa, "image/*")
            //cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, aaa);
//set crop properties
            cropIntent.putExtra("crop", "true")
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256)
            cropIntent.putExtra("outputY", 256)
            //retrieve data on return
            cropIntent.putExtra("return-data", true)
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, aaa)
            cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP)
        } catch (anfe: ActivityNotFoundException) { //display an error message
            val errorMessage = "Whoops - your device doesn't support the crop action!"

            Global_Data.Custom_Toast(this, errorMessage, "")
        }
    }

    @Throws(IOException::class)
    private fun getImageFile(): File? {
        val imageFileName = "JPEG_" + System.currentTimeMillis() + "_"
        val storageDir = File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        )
        println(storageDir.absolutePath)
        if (storageDir.exists()) println("File exists") else println("File not exists")
        val file = File.createTempFile(
                imageFileName, ".jpg", storageDir
        )
        pictureImagePath_new = "file:" + file.absolutePath
        return file
    }


    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                    arrayOf(f.getPath()),
                    arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }
    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
    }

    private fun updateLabel() { //String myFormat = "yyyy/MM/dd"; //In which you need put here
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        et_birthday.setText(sdf.format(myCalendar!!.time))
    }
}
