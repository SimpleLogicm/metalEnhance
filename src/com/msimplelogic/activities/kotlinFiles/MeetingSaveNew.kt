package com.msimplelogic.activities.kotlinFiles

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder
import cafe.adriel.androidaudiorecorder.model.AudioChannel
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate
import cafe.adriel.androidaudiorecorder.model.AudioSource
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.msimplelogic.App.AppController
import com.msimplelogic.activities.BaseActivity
import com.msimplelogic.activities.Check_Null_Value
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.adapter.Promotional_meeting_adaptor
import com.msimplelogic.adapter.StatndardMeetingCheckingAdapter
import com.msimplelogic.helper.PathUtil
import com.msimplelogic.model.Promotional_Model
import com.msimplelogic.model.UpdateStockModel
import com.msimplelogic.services.getServices
import cpm.simplelogic.helper.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_meeting_save_new.*
import kotlinx.android.synthetic.main.content_meeting_save_new.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.*
import java.net.URISyntaxException
import java.net.URL
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList

private var outputFile: File? = null
private var url = "";

class MeetingSaveNew : BaseActivity() {

    var adaptor: Promotional_meeting_adaptor? = null
    var list: ArrayList<Promotional_Model>? = null
    var context: Context? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var dialog: ProgressDialog? = null
    private var mAdapter: StatndardMeetingCheckingAdapter? = null
    var updateStockModel: MutableList<UpdateStockModel> = java.util.ArrayList()
    var count = 0;
    var checkin_out_flag = ""
    var code = "";
    val randomPIN = System.currentTimeMillis()
    val PINString = randomPIN.toString()
    var pdfPath: String? = null
    var mCurentPhotoPath: String? = null
    private var fileName: String = ""
    private val PICK_PDF_REQUEST = 1
    var urifgh: Uri? = null
    var myFile: File? = null
    var selectedImage: Uri? = null
    private var pictureImagePath = ""
    private var filePath1: Uri? = null
    private var detailsList: ArrayList<String> = ArrayList<String>()
    var id = "";
    var requestCode = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(R.layout.activity_meeting_save_new)
        var builder = StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        context = MeetingSaveNew@ this
        cd = ConnectionDetector(context)
        dialog = ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)

        // fileName = "${externalCacheDir.absolutePath}/audiorecordtest.mp3"

        fileName = "${Environment.getExternalStorageDirectory()}/recorded_audio.wav";

        code = getServices.id;
        if (Global_Data.PlannerUpdate.equals("Yes")) {
            try {

                btn_saves!!.setText(getResources().getString(R.string.Update))
                code = getServices.id;

                isInternetPresent = cd!!.isConnectingToInternet
                if (isInternetPresent) {
                    GetMeetingExcutiveListData(code)
                } else {

                    Global_Data.Custom_Toast(context, "You don't have internet connection.","")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            val random = Random()
            var a = 0
            for (i in 1..3) {
                val value: Int = 16 + random.nextInt(5)
                count++
                updateStockModel!!.add(UpdateStockModel("YES" + PINString + value + count, "", "", "gfhgfh", "gfhfgh", "fghh", ""))

                ++count
            }
        }



        mAdapter = StatndardMeetingCheckingAdapter(applicationContext, updateStockModel)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        msave_recycler_view.setLayoutManager(mLayoutManager)
        msave_recycler_view.setItemAnimator(DefaultItemAnimator())
        msave_recycler_view.setAdapter(mAdapter)


        btn_add.setOnClickListener {

            val randomPIN = System.currentTimeMillis()
            val PINString = randomPIN.toString()
            val random = Random()
            val value: Int = 16 + random.nextInt(5)
            count++
            updateStockModel.add(updateStockModel.size - 1, UpdateStockModel("YES" + PINString + value + count, "", "", "", "", "", ""))
            mAdapter!!.notifyItemInserted(updateStockModel.size - 1);
            mAdapter!!.notifyDataSetChanged()
        }

        checkin_out_flag = "check_in"
        btn_Check_In?.setOnClickListener {
            btn_Check_In!!.setBackgroundColor(Color.parseColor("#075b97"))
            btn_Check_In!!.setTextColor(Color.WHITE)
            btn_Check_Out!!.setBackgroundColor(Color.WHITE)
            btn_Check_Out!!.setTextColor(Color.BLACK)

            checkin_out_flag = "check_in"
        }

        btn_Check_Out?.setOnClickListener {
            btn_Check_Out!!.setBackgroundColor(Color.parseColor("#075b97"))
            btn_Check_Out!!.setTextColor(Color.WHITE)
            btn_Check_In!!.setBackgroundColor(Color.WHITE)
            btn_Check_In!!.setTextColor(Color.BLACK)

            checkin_out_flag = "check_out"
        }

        s_ms_att_music.setOnClickListener {

            requestAudioPermission()

        }

        s_ms_att_pdf.setOnClickListener({
            var photoFile: File? = null
            try {
                photoFile = createImageFile1()
            } catch (ex: IOException) { // Error occurred while creating the File
                Log.i("Image TAG", "IOException")
                pdfPath = ""
            }

            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "application/pdf"
            // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            val photoURI = FileProvider.getUriForFile(this@MeetingSaveNew,
                    com.msimplelogic.activities.BuildConfig.APPLICATION_ID + ".provider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, 1)

//            val intent = Intent()
//            intent.type = "application/pdf"
//            intent.action = Intent.ACTION_GET_CONTENT
//            startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST)

        })

        btn_saves.setOnClickListener {

            detailsList.clear()
            if (!Global_Data.Order_hashmap.isEmpty()) {
                try {
                    for (name in Global_Data.Order_hashmap.keys) {
                        val item = JSONObject()
                        val key: Any = name.toString()
                        val key_array = key.toString().split("&".toRegex()).toTypedArray()
                        val value: Any? = Global_Data.Order_hashmap[key]
                        //detailsList.add(value.toString());


                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(checkin_out_flag)) {
                Global_Data.Custom_Toast(ActivityTaskDetails@ this, getResources().getString(R.string.checkino), "Yes")
            }
//            else
//            if(detailsList.size > 0)
//            {
//
//            }
            else {
                isInternetPresent = cd!!.isConnectingToInternet
                if (isInternetPresent) {

                    getServices.PMeeting_Data(this@MeetingSaveNew, checkin_out_flag, "", "", " ", "", pdfPath, fileName, code, "standard", id)


                } else {
                    Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")
                }
            }


        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.msimplelogic.activities.R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(context, Promotional_meetings::class.java))
                finish()
                return true
            }
        }
        //		return super.onOptionsItemSelected(item);

        when (item.itemId) {
            com.msimplelogic.activities.R.id.add -> {
                var targetNew = ""
                val sp = getSharedPreferences("SimpleLogic", 0)
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


    public override fun onDestroy() {
        super.onDestroy()
        hidePDialog()

    }

    private fun hidePDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
            dialog = null
        }
    }

    override fun onBackPressed() {
        val i = Intent(context, Promotional_meetings::class.java)
        startActivity(i)
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) { // Get the Uri of the selected file
            urifgh = data!!.data
            val uriString: String = urifgh.toString()
            myFile = File(uriString)
            pdfPath = myFile!!.getAbsolutePath()
            var displayName: String? = null
            val fileuri = data.data
            //   val mm = ExpenseDetailsActivity.getMimeType(context, fileuri!!)
            //  val r_path: String = getRealPathFromURI(context, fileuri)
            //  val file_name: String = getFileName(fileuri)
            try {

                pdfPath = "file:" + PathUtil.getPath(context, fileuri)


            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            //            File folder = new File(Environment.getExternalStorageDirectory() + "/EmailClient/");
//
//            folder.mkdirs();
//            File file = new File(folder, String.valueOf(myFile));
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            if (uriString.startsWith("content://")) {
                var cursor: Cursor? = null
                try {
                    cursor = this.contentResolver.query(urifgh!!, null, null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        Log.d("nameeeee>>>>  ", displayName)
                        //saveTextToFile(ExpenseDetailsActivity.this,displayName,uriString);
//uploadMultipart();
// uploadPDF(displayName,urifgh);
                        val finalDisplayName = displayName
                        this@MeetingSaveNew.runOnUiThread(Runnable {
                            //new doFileUpload().execute(feedPhotoCode,feedPhotoPath);
                            //new doFileUploadPdf.execute();
                            //new doFileUploadPdf().execute(pdfPath,pictureImagePath);
                        })
                    }
                } finally {
                    cursor!!.close()
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile!!.getName()
                Log.d("nameeeee>>>>  ", displayName)
            }
            //new Expenses.LongOperation().execute();
//			try {
//
//				//dbvoc.updateORDER_order_image(pictureImagePath, Global_Data.GLObalOrder_id);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            try {
                selectedImage = data!!.data
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
        } else if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath1 = data.data
            //new File(filePath1.getPath());
        } else
            if (requestCode == 0) {
                if (resultCode == RESULT_OK) {
                    // Great! User has recorded and saved the audio file
                    Global_Data.Custom_Toast(context, "Great! You have recorded and saved the audio file", "Yes")
                } else if (resultCode == RESULT_CANCELED) {
                    try {
                        val fdelete = File(fileName)
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                println("file Deleted :" + fileName)
                            } else {
                                println("file not Deleted :" + fileName)
                            }
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                    Global_Data.Custom_Toast(context, "Oops! You Have canceled the recording", "Yes")
                    // Oops! User has canceled the recording
                }
            } else {
                try {
                    val fdelet = File(pdfPath)
                    if (fdelet.exists()) {
                        if (fdelet.delete()) {
                            println("file Deleted :" + pdfPath)
                        } else {
                            println("file not Deleted :" + pdfPath)
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                try {
                    val fdele = File(mCurentPhotoPath)
                    if (fdele.exists()) {
                        if (fdele.delete()) {
                            println("file Deleted :" + mCurentPhotoPath)
                        } else {
                            println("file not Deleted :" + mCurentPhotoPath)
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
    }

    @Throws(IOException::class)
    private fun createImageFile1(): File? { // Create an image file name
        val imageFileName = "exp"
        val storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_Expenses")
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        val image = File.createTempFile(
                imageFileName,  // prefix
                ".pdf",  // suffix
                storageDir // directory
        )
        // Save a file: path for use with ACTION_VIEW intents
        pdfPath = "file:" + image.absolutePath
        mCurentPhotoPath = image.getAbsolutePath();
        return image
    }

    fun GetMeetingExcutiveListData(code: String?) {

        dialog!!.setMessage(resources.getString(R.string.Please_Wait))
        dialog!!.setTitle(resources.getString(R.string.app_name))
        dialog!!.setCancelable(false)
        dialog!!.show()

        val sp = applicationContext.getSharedPreferences("SimpleLogic", 0)
        val user_email: String = sp.getString("USER_EMAIL", null)!!
        // val shop_code: String = sp.getString("shopcode", null)

        val Cust_domain = sp.getString("Cust_Service_Url", "")
        var service_url = Cust_domain + "metal/api/v1/"

        try {

            service_url = service_url + "promotional_meetings/standard_meeting_details?email=" + URLEncoder.encode(user_email, "UTF-8") + "&meeting_id=" + code

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

                                dialog!!.hide()

                            } else {


                                val meetings = json.getJSONArray("standard_meetings")

                                Log.i("volley", "response activity_planner Length: " + meetings.length())

                                Log.d("users", "holidays$meetings")



                                for (i in 0 until meetings.length()) {
                                    val jsonObject = meetings.getJSONObject(i)

                                    try {


                                        id = jsonObject.getString("id")

                                        if (jsonObject.getString("checked").equals("check_in", ignoreCase = false)) {
                                            btn_Check_In!!.setBackgroundColor(Color.parseColor("#075b97"))
                                            btn_Check_In!!.setTextColor(Color.WHITE)
                                            btn_Check_Out!!.setBackgroundColor(Color.WHITE)
                                            btn_Check_Out!!.setTextColor(Color.BLACK)

                                            checkin_out_flag = "check_in"
                                        } else {
                                            btn_Check_Out!!.setBackgroundColor(Color.parseColor("#075b97"))
                                            btn_Check_Out!!.setTextColor(Color.WHITE)
                                            btn_Check_In!!.setBackgroundColor(Color.WHITE)
                                            btn_Check_In!!.setTextColor(Color.BLACK)

                                            checkin_out_flag = "check_out"
                                        }

                                        val promotional_descriptions = JSONArray(jsonObject.getString("promotional_descriptions"))

                                        if (promotional_descriptions.length() > 0) {
                                            for (i in 0 until promotional_descriptions.length()) {
                                                val jsonObject = promotional_descriptions.getJSONObject(i)

                                                updateStockModel!!.add(UpdateStockModel(jsonObject.getString("description_id"), jsonObject.getString("name"), "", "", "", "", ""))
                                            }
                                        } else {
                                            val random = Random()
                                            for (i in 1..3) {
                                                val value: Int = 16 + random.nextInt(5)
                                                count++
                                                updateStockModel!!.add(UpdateStockModel("YES" + PINString + value + count, "", "", "gfhgfh", "gfhfgh", "fghh", ""))

                                                ++count
                                            }
                                        }



                                        outputFile = File(fileName)
                                        url = jsonObject.getString("file_audio")


                                        someTask().execute();
                                        // downloadFile(jsonObject.getString("file_audio"),outputFile)


                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }


                                }



                                mAdapter = StatndardMeetingCheckingAdapter(applicationContext, updateStockModel)
                                msave_recycler_view.setAdapter(mAdapter)
                                mAdapter!!.notifyDataSetChanged()

                                dialog!!.dismiss()

                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                            finish()
                            dialog!!.dismiss()
                        }
                        dialog!!.dismiss()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        //  finish();
                        dialog!!.dismiss()
                    }
                    dialog!!.dismiss()
                },
                Response.ErrorListener { error ->
                    //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    if (error is TimeoutError || error is NoConnectionError) { //							Toast.makeText(Video_Main_List.this,
//									"Network Error",
//									Toast.LENGTH_LONG).show();
//                        val toast = Toast.makeText(context,
//                                "Network Error",
//                                Toast.LENGTH_LONG)
//                        toast.setGravity(Gravity.CENTER, 0, 0)
//                        toast.show()
                        Global_Data.Custom_Toast(context,
                                "Network Error","yes")
                    } else if (error is AuthFailureError) {
//
                        Global_Data.Custom_Toast(context,
                                "Server AuthFailureError  Error","yes")
                    } else if (error is ServerError) {

                        Global_Data.Custom_Toast(context,
                                resources.getString(R.string.Server_Errors),"yes")
                    } else if (error is NetworkError) {

                        Global_Data.Custom_Toast(context,
                                "Network Error","yes")
                    } else if (error is ParseError) {
                        Global_Data.Custom_Toast(context,
                                "ParseError   Error","yes")
                    } else {

                        Global_Data.Custom_Toast(context,
                                error.message,"yes")
                    }

                    finish()
                    dialog!!.dismiss()

                })
        val requestQueue = Volley.newRequestQueue(context)
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


    private fun requestAudioPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.RECORD_AUDIO)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {

                        try {
                            val dir = File(fileName)
                            if (dir.exists()) {

                                val alertDialog = AlertDialog.Builder(this@MeetingSaveNew).create() //Read Update

                                alertDialog.setTitle(resources.getString(R.string.app_name))
                                alertDialog.setMessage(resources.getString(R.string.Audio_message))
                                alertDialog.setButton(Dialog.BUTTON_POSITIVE, resources.getString(R.string.Play)) { dialog, which ->
                                    var playAudioIntent = Intent(Intent.ACTION_VIEW);
                                    playAudioIntent.setDataAndType(Uri.parse("file://" + fileName), "video/*");
                                    startActivity(playAudioIntent);

                                }

                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, resources.getString(R.string.Record)) { dialog, id ->

                                    try {
                                        // val dir = File(fileName)
//                                        if (dir.exists) {
//                                            val children = dir.list()
//                                            for (i in children.indices) {
//
//                                                File(dir, children[i]).delete()
//
//                                            }

                                        val fdelete = File(fileName)
                                        if (fdelete.exists()) {
                                            if (fdelete.delete()) {
                                                println("file Deleted :" + fileName)
                                            } else {
                                                println("file not Deleted :" + fileName)
                                            }
                                        }
                                    } catch (ex: Exception) {
                                        ex.printStackTrace()
                                    }

                                    var color = getResources().getColor(R.color.primarycolor);
                                    AndroidAudioRecorder.with(this@MeetingSaveNew)
                                            // Required
                                            .setFilePath(fileName)
                                            .setColor(color)
                                            .setRequestCode(requestCode)

                                            // Optional
                                            .setSource(AudioSource.MIC)
                                            .setChannel(AudioChannel.STEREO)
                                            .setSampleRate(AudioSampleRate.HZ_48000)
                                            .setAutoStart(true)
                                            .setKeepDisplayOn(true)
                                            // Start recording
                                            .record();

                                }

                                alertDialog.show()
                            } else {
                                var color = getResources().getColor(R.color.primarycolor);
                                AndroidAudioRecorder.with(this@MeetingSaveNew)
                                        // Required
                                        .setFilePath(fileName)
                                        .setColor(color)
                                        .setRequestCode(requestCode)

                                        // Optional
                                        .setSource(AudioSource.MIC)
                                        .setChannel(AudioChannel.STEREO)
                                        .setSampleRate(AudioSampleRate.HZ_48000)
                                        .setAutoStart(true)
                                        .setKeepDisplayOn(true)
                                        // Start recording
                                        .record();
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }


//                        val i = Intent(context, AudioRecordTest::class.java)
//                        startActivity(i)

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

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    class someTask() : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String? {
            try {
                val u = URL(url)
                val conn = u.openConnection()
                val contentLength = conn.contentLength
                val stream = DataInputStream(u.openStream())
                val buffer = ByteArray(contentLength)
                stream.readFully(buffer)
                stream.close()
                val fos = DataOutputStream(FileOutputStream(outputFile))
                fos.write(buffer)
                fos.flush()
                fos.close()
            } catch (e: FileNotFoundException) {
                return null // swallow a 404
            } catch (e: IOException) {
                return null // swallow a 404
            }

            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
            // ...
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // ...
        }
    }

}
