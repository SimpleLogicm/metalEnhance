package com.msimplelogic.activities.kotlinFiles

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.github.gcacace.signaturepad.views.SignaturePad
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
import com.msimplelogic.helper.OnSwipeTouchListener
import com.msimplelogic.services.getServices
import de.hdodenhof.circleimageview.CircleImageView
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_new__feedback.*
import kotlinx.android.synthetic.main.content_new__feedback.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class New_Feedback : BaseActivity() {
    internal var image_check = ""
    var pictureImagePath = ""
    var pictureImagePath_dealer = ""
    var pictureImagePath_new_Tech = ""
    var pictureImagePath_new_dealer = ""
    var Signature_path_tech = ""
    var Signature_path_deal = ""
    var Signature_path_dealfile=""
    var Signature_path_techfile = ""
    var signatureCardFirst: CardView? = null
    var signatureCardSecond: CardView? = null
    var signatureCardThird: CardView? = null
    var swipe: ImageView? = null
    var Next1: LinearLayout? = null
    var Next2: LinearLayout? = null
    var Next3: LinearLayout? = null
    var swipelayout: LinearLayout? = null
    var swipescroll: ScrollView? = null
    internal var choice = 0
    var CirculerImage: CircleImageView? = null
    var tv_shopname: TextView? = null
    var tv_shopadd: TextView? = null
    var card1radiogroup: RadioGroup? = null
    var R_btn_yes1: RadioButton? = null
    var R_btn_No1: RadioButton? = null
    var exellent: LinearLayout? = null
    var Good: LinearLayout? = null
    var average: LinearLayout? = null
    var poor: LinearLayout? = null
    var k_excellent: LinearLayout? = null
    var k_good: LinearLayout? = null
    var k_avg: LinearLayout? = null
    var k_poor: LinearLayout? = null
    var minus_btn: ImageView? = null
    var plus_btn: ImageView? = null
    var et_num: EditText? = null
    var et_datepick: EditText? = null
    var card_2_radiogroup: RadioGroup? = null
    var R_btn_yes_2: RadioButton? = null
    var R_btn_No_2: RadioButton? = null
    var et_pending_issues: EditText? = null
    var et_suggetions: EditText? = null
    var As_Technician: TextView? = null
    var As_dealer: TextView? = null
    var card_view_Image1: CardView? = null
    var img_show1: ImageView? = null
    var crossimg1: ImageView? = null
    var btn_takephoto1: Button? = null
    var next4: LinearLayout? = null
    var dealer_photo_image: ImageView? = null
    var dealer_sign_image: ImageView? = null
    var Tech_sign_image: ImageView? = null
    var Tech_photo_image: ImageView? = null
    var img_show_dealer: ImageView? = null
    var crossimg_dealer: ImageView? = null
    var next6_submit: Button? = null
    var btn_takephoto_dealer: Button? = null
    var next5: Button? = null
    var pathname: String? = null
    var bitmaptech: Bitmap? = null
    var bitmapdeal: Bitmap? = null
    var imgdialog: ZoomageView? = null
    var As_Technician_last: TextView? = null
    var clear_dealer: TextView? = null
    var clear_tech: TextView? = null
    var card_view_Image_dealer: CardView? = null
    var signature_cardn_dealer: CardView? = null
    var signature_cardn_tech: CardView? = null
    var imgframe_dealer: FrameLayout? = null
    var signature_pad_dealer: SignaturePad? = null
    var signature_pad_tech: SignaturePad? = null
    internal var B_flag: Boolean? = null
    var cardSignature4: RelativeLayout? = null
    var cardSignature5: RelativeLayout? = null
    var cardSignature6: RelativeLayout? = null
    var save_sign_dealer: TextView? = null
    var save_sign_techni: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new__feedback)
        setSupportActionBar(toolbar)
        assert(supportActionBar != null) //null check
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("")
        save_sign_techni = findViewById(R.id.save_sign_techni);
        dealer_sign_image = findViewById(R.id.dealer_sign_image);
        save_sign_dealer = findViewById(R.id.save_sign_dealer);
        Tech_sign_image = findViewById(R.id.Tech_sign_image);
        Tech_photo_image = findViewById(R.id.Tech_photo_image)
        clear_tech = findViewById(R.id.clear_tech);
        signature_pad_tech = findViewById(R.id.signature_pad_tech);
        //next4 = findViewById(R.id.next4)
        btn_takephoto1 = findViewById(R.id.btn_takephoto1)
        img_show1 = findViewById(R.id.img_show1)
        crossimg1 = findViewById(R.id.crossimg1)
        card_view_Image1 = findViewById(R.id.card_view_Image1)
        As_dealer = findViewById(R.id.As_dealer)
        As_Technician = findViewById(R.id.As_Technician)
        signatureCardFirst = findViewById<CardView>(R.id.signature_card1)
        signatureCardSecond = findViewById<CardView>(R.id.signature_card2)
        signatureCardThird = findViewById<CardView>(R.id.signature_card3)
        swipe = findViewById(R.id.swipe)
        Next1 = findViewById(R.id.next1)
        //Next2 = findViewById(R.id.next2)
        Next3 = findViewById(R.id.next3)
        swipelayout = findViewById(R.id.swipelayout)
        exellent = findViewById(R.id.exellent)
        Good = findViewById(R.id.Good)
        average = findViewById(R.id.average)
        poor = findViewById(R.id.poor)
        k_excellent = findViewById(R.id.k_excellent)
        k_good = findViewById(R.id.k_good)
        k_avg = findViewById(R.id.k_avg)
        k_poor = findViewById(R.id.k_poor)
        minus_btn = findViewById(R.id.minus_btn)
        plus_btn = findViewById(R.id.plus_btn)
        et_num = findViewById(R.id.et_num)
        et_datepick = findViewById(R.id.et_datepick)
        card_2_radiogroup = findViewById(R.id.card_2_radiogroup)
        R_btn_yes_2 = findViewById(R.id.R_btn_yes_2)
        R_btn_No_2 = findViewById(R.id.R_btn_No_2)
        et_pending_issues = findViewById(R.id.et_pending_issues)
        et_suggetions = findViewById(R.id.et_suggetions)
        CirculerImage = findViewById(R.id.CirculerImage)
        tv_shopname = findViewById(R.id.tv_shopname)
        tv_shopadd = findViewById(R.id.tv_shopadd)
        card1radiogroup = findViewById(R.id.card1radiogroup)
        R_btn_yes1 = findViewById(R.id.R_btn_yes1)
        R_btn_No1 = findViewById(R.id.R_btn_No1)
        btn_takephoto_dealer = findViewById(R.id.btn_takephoto_dealer)
        imgframe_dealer = findViewById(R.id.imgframe_dealer)
        img_show_dealer = findViewById(R.id.img_show_dealer)
        crossimg_dealer = findViewById(R.id.crossimg_dealer)
        signature_pad_dealer = findViewById(R.id.signature_pad_dealer)
        clear_dealer = findViewById(R.id.clear_dealer)
        signature_cardn_dealer = findViewById(R.id.signature_cardn_dealer)
        imgframe_dealer = findViewById(R.id.imgframe_dealer)
        imgframe_dealer = findViewById(R.id.imgframe_dealer)
        imgframe_dealer = findViewById(R.id.imgframe_dealer)

        cardSignature4 = findViewById(R.id.signature_card4)
        cardSignature5 = findViewById(R.id.signature_card5)
        cardSignature6 = findViewById(R.id.signature_card6)
        dealer_photo_image = findViewById(R.id.dealer_photo_image)

        swipescroll = findViewById(R.id.swipescroll)


        val spref: SharedPreferences = getSharedPreferences("SimpleLogic", 0)
        val shopname: String? = spref.getString("shopname", "")
        val shopadd: String? = spref.getString("shopadd", "")

        tv_shopname!!.text=shopname
        tv_shopadd!!.text=shopadd

        save_sign_dealer!!.setOnClickListener {

            if (signature_pad_dealer!!.isEmpty()) run {
                Global_Data.Custom_Toast(this@New_Feedback, "Please Sign", "Yes")
            }
            else {
                try {
                    bitmapdeal = signature_pad_dealer!!.getSignatureBitmap()

                    SaveImagedealer(bitmapdeal, "dealer_sign")
//

                    dealer_sign_image!!.setImageBitmap(bitmapdeal)
//                    Glide.with(this@New_Feedback)
//                            .load(Signature_path_deal)
//                            .into(dealer_sign_image!!)
//                    val picasso = Picasso.get()
//                    picasso.load(Signature_path_deal)
//                            .into(dealer_sign_image)
                    // Picasso.get().load(Signature_path_deal).into(dealer_sign_image);
                    Global_Data.Custom_Toast(this@New_Feedback, "Dealer Sign Save successfully", "Yes")

                    //mSignature.clear();
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        save_sign_techni!!.setOnClickListener {
            if (signature_pad_tech!!.isEmpty()) run {
                Global_Data.Custom_Toast(this@New_Feedback, "Please Sign", "Yes")
            }
            else {
                try {
                    bitmaptech = signature_pad_tech!!.getSignatureBitmap()
                    SaveImage(bitmaptech, "tech_sign")

                    Tech_sign_image!!.setImageBitmap(bitmaptech)

                    Global_Data.Custom_Toast(this@New_Feedback, "Technician Sign Save successfully", "Yes")

                    //mSignature.clear();
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        clear_dealer!!.setOnClickListener {
            signature_pad_dealer!!.clear()
        }
        clear_tech!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        //Do Something
                        signature_pad_tech!!.clear()
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        CirculerImage!!.setOnClickListener {
            //ppleae Enter path of scirculer image insted of pictureImagePath_new_dealer
            if (pictureImagePath_new_dealer !== "") {
                val builder = AlertDialog.Builder(this@New_Feedback)
                val inflater = layoutInflater
                val dialogLayout = inflater.inflate(R.layout.dialogeimage, null)
                imgdialog = dialogLayout.findViewById<ZoomageView>(R.id.imageView)
                Glide.with(this@New_Feedback).load(pictureImagePath_new_dealer).into(imgdialog!!)
                builder.setPositiveButton("OK", null)
                builder.setView(dialogLayout)
                builder.show()
            }
        }

        btn_takephoto1!!.setOnClickListener {

            requestpermistion()
        }

        btn_takephoto_dealer!!.setOnClickListener {
            requestpermistion2()
        }

        crossimg1!!.setOnClickListener {
            deletedialog()
        }

        crossimg_dealer!!.setOnClickListener { deletedialog2() }

        exellent!!.setOnClickListener {
            exellent!!.setBackgroundResource(R.drawable.card_border_black)
            Good!!.setBackgroundResource(R.drawable.background_transparent)
            average!!.setBackgroundResource(R.drawable.background_transparent)
            poor!!.setBackgroundResource(R.drawable.background_transparent)
        }

        Good!!.setOnClickListener {
            exellent!!.setBackgroundResource(R.drawable.background_transparent)
            Good!!.setBackgroundResource(R.drawable.card_border_black)
            average!!.setBackgroundResource(R.drawable.background_transparent)
            poor!!.setBackgroundResource(R.drawable.background_transparent)
        }

        average!!.setOnClickListener {
            exellent!!.setBackgroundResource(R.drawable.background_transparent)
            Good!!.setBackgroundResource(R.drawable.background_transparent)
            average!!.setBackgroundResource(R.drawable.card_border_black)
            poor!!.setBackgroundResource(R.drawable.background_transparent)
        }

        poor!!.setOnClickListener {
            exellent!!.setBackgroundResource(R.drawable.background_transparent)
            Good!!.setBackgroundResource(R.drawable.background_transparent)
            average!!.setBackgroundResource(R.drawable.background_transparent)
            poor!!.setBackgroundResource(R.drawable.card_border_black)
        }

        k_excellent!!.setOnClickListener {
            k_excellent!!.setBackgroundResource(R.drawable.card_border_black)
            k_good!!.setBackgroundResource(R.drawable.background_transparent)
            k_avg!!.setBackgroundResource(R.drawable.background_transparent)
            k_poor!!.setBackgroundResource(R.drawable.background_transparent)
        }

        k_good!!.setOnClickListener {
            k_excellent!!.setBackgroundResource(R.drawable.background_transparent)
            k_good!!.setBackgroundResource(R.drawable.card_border_black)
            k_avg!!.setBackgroundResource(R.drawable.background_transparent)
            k_poor!!.setBackgroundResource(R.drawable.background_transparent)
        }

        k_avg!!.setOnClickListener {
            k_excellent!!.setBackgroundResource(R.drawable.background_transparent)
            k_good!!.setBackgroundResource(R.drawable.background_transparent)
            k_avg!!.setBackgroundResource(R.drawable.card_border_black)
            k_poor!!.setBackgroundResource(R.drawable.background_transparent)
        }

        k_poor!!.setOnClickListener {
            k_excellent!!.setBackgroundResource(R.drawable.background_transparent)
            k_good!!.setBackgroundResource(R.drawable.background_transparent)
            k_avg!!.setBackgroundResource(R.drawable.background_transparent)
            k_poor!!.setBackgroundResource(R.drawable.card_border_black)
        }

        btn_feedbacksubmit!!.setOnClickListener {
            getServices.SyncFeedbackDynamic(this@New_Feedback,  pictureImagePath,pictureImagePath_dealer,Signature_path_techfile,Signature_path_dealfile)
            getServices.SyncMarketingSurveyRecord(this@New_Feedback)
        }

//        Next1!!.setOnClickListener {
//            swipe!!.setImageResource(R.drawable.timeliner2)
//            signatureCardFirst!!.setVisibility(View.GONE)
//            signatureCardSecond!!.setVisibility(View.VISIBLE)
//            signatureCardThird!!.setVisibility(View.GONE)
//            Next1!!.setVisibility(View.GONE)
//            Next2!!.setVisibility(View.VISIBLE)
//            Next3!!.setVisibility(View.GONE)
//            choice--
//        }
//        Next2!!.setOnClickListener {
//            swipe!!.setImageResource(R.drawable.timeliner3)
//            signatureCardFirst!!.setVisibility(View.GONE)
//            signatureCardSecond!!.setVisibility(View.GONE)
//            signatureCardThird!!.setVisibility(View.VISIBLE)
//            Next1!!.setVisibility(View.GONE)
//            Next2!!.setVisibility(View.GONE)
//            Next3!!.setVisibility(View.VISIBLE)
//            choice++
//
//        }

        swipescroll!!.setOnTouchListener(object : OnSwipeTouchListener(this@New_Feedback) {
            override fun onSwipeRight() {
                when (choice) {
//                    5 -> {
//                        swipe!!.setImageResource(R.drawable.sixtimeline5)
//                        signatureCardFirst!!.setVisibility(View.GONE)
//                        signatureCardSecond!!.setVisibility(View.GONE)
//                        signatureCardThird!!.setVisibility(View.GONE)
//                        cardSignature4!!.setVisibility(View.GONE)
//                        cardSignature5!!.setVisibility(View.VISIBLE)
//                        cardSignature6!!.setVisibility(View.GONE)
//                        Next1!!.setVisibility(View.GONE)
//                        Next2!!.setVisibility(View.VISIBLE)
//                        Next3!!.setVisibility(View.GONE)
//                        choice--
//                    }
//                    4 -> {
//                        swipe!!.setImageResource(R.drawable.sixtimeline4)
//                        signatureCardFirst!!.setVisibility(View.GONE)
//                        signatureCardSecond!!.setVisibility(View.GONE)
//                        signatureCardThird!!.setVisibility(View.GONE)
//                        cardSignature4!!.setVisibility(View.VISIBLE)
//                        cardSignature5!!.setVisibility(View.GONE)
//                        cardSignature6!!.setVisibility(View.GONE)
//                        Next1!!.setVisibility(View.VISIBLE)
//                        Next2!!.setVisibility(View.GONE)
//                        Next3!!.setVisibility(View.GONE)
//                        choice--
//                    }
//                    else -> {
//                    }
                    3 -> {
                        swipe!!.setImageResource(R.drawable.timeliner3)
//                        signatureCardFirst!!.setVisibility(View.GONE)
//                        signatureCardSecond!!.setVisibility(View.GONE)
//                        signatureCardThird!!.setVisibility(View.VISIBLE)
                        cardSignature4!!.setVisibility(View.GONE)
                        cardSignature5!!.setVisibility(View.GONE)
                        cardSignature6!!.setVisibility(View.VISIBLE)

                        choice--
                    }
                    2 -> {
                        swipe!!.setImageResource(R.drawable.timeliner2)
//                        signatureCardFirst!!.setVisibility(View.GONE)
//                        signatureCardSecond!!.setVisibility(View.VISIBLE)
//                        signatureCardThird!!.setVisibility(View.GONE)
                        cardSignature4!!.setVisibility(View.GONE)
                        cardSignature5!!.setVisibility(View.VISIBLE)
                        cardSignature6!!.setVisibility(View.GONE)

                        choice--
                    }

                    1 -> {
                        swipe!!.setImageResource(R.drawable.timeliner)
//                        signatureCardFirst!!.setVisibility(View.VISIBLE)
//                        signatureCardSecond!!.setVisibility(View.GONE)
//                        signatureCardThird!!.setVisibility(View.GONE)
                        cardSignature4!!.setVisibility(View.VISIBLE)
                        cardSignature5!!.setVisibility(View.GONE)
                        cardSignature6!!.setVisibility(View.GONE)

                        choice--
                    }

                }//Toast.makeText(CaptureSignature.this, "3", Toast.LENGTH_SHORT).show();
                //choice=1;
            }

            override fun onSwipeLeft() {
                when (choice) {
                    0 -> {
                        swipe!!.setImageResource(R.drawable.timeliner2)
//                        signatureCardFirst!!.setVisibility(View.GONE)
//                        signatureCardSecond!!.setVisibility(View.VISIBLE)
//                        signatureCardThird!!.setVisibility(View.GONE)
                        cardSignature4!!.setVisibility(View.GONE)
                        cardSignature5!!.setVisibility(View.VISIBLE)
                        cardSignature6!!.setVisibility(View.GONE)

                        choice++

                    }
                    1 -> {
                        swipe!!.setImageResource(R.drawable.timeliner3)
//                        signatureCardFirst!!.setVisibility(View.GONE)
//                        signatureCardSecond!!.setVisibility(View.GONE)
//                        signatureCardThird!!.setVisibility(View.VISIBLE)
                        cardSignature4!!.setVisibility(View.GONE)
                        cardSignature5!!.setVisibility(View.GONE)
                        cardSignature6!!.setVisibility(View.VISIBLE)

                        choice++
                    }

//                    2 -> {
//                        swipe!!.setImageResource(R.drawable.sixtimeline4)
////                        signatureCardFirst!!.setVisibility(View.GONE)
////                        signatureCardSecond!!.setVisibility(View.GONE)
////                        signatureCardThird!!.setVisibility(View.GONE)
//                        cardSignature4!!.setVisibility(View.VISIBLE)
//                        cardSignature5!!.setVisibility(View.GONE)
//                        cardSignature6!!.setVisibility(View.GONE)
//
//                        choice++
//                    }

//                    3 -> {
//                        swipe!!.setImageResource(R.drawable.sixtimeline5)
//                        signatureCardFirst!!.setVisibility(View.GONE)
//                        signatureCardSecond!!.setVisibility(View.GONE)
//                        signatureCardThird!!.setVisibility(View.GONE)
//                        cardSignature4!!.setVisibility(View.GONE)
//                        cardSignature5!!.setVisibility(View.VISIBLE)
//                        cardSignature6!!.setVisibility(View.GONE)
//                        Next1!!.setVisibility(View.GONE)
//                        Next2!!.setVisibility(View.GONE)
//                        Next3!!.setVisibility(View.VISIBLE)
//                        choice++
//
//                    }
//
//                    4 -> {
//                        swipe!!.setImageResource(R.drawable.sixtimeline6)
//                        signatureCardFirst!!.setVisibility(View.GONE)
//                        signatureCardSecond!!.setVisibility(View.GONE)
//                        signatureCardThird!!.setVisibility(View.GONE)
//                        cardSignature4!!.setVisibility(View.GONE)
//                        cardSignature5!!.setVisibility(View.GONE)
//                        cardSignature6!!.setVisibility(View.VISIBLE)
//                        Next1!!.setVisibility(View.GONE)
//                        Next2!!.setVisibility(View.GONE)
//                        Next3!!.setVisibility(View.VISIBLE)
//                        choice++
//                    }


                }//Toast.makeText(CaptureSignature.this, "3", Toast.LENGTH_SHORT).show();
                //choice=1;
                // swipe.setImageResource(R.drawable.timeliner2);
            }
        })

    }

    private fun SaveImagedealer(bitmap: Bitmap?, s: String) {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val myDir = File(path, "Metal_Signature")
        myDir.mkdirs()

        val fname = s + ".jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
            Signature_path_deal = file.absolutePath
            Signature_path_dealfile = "file:" + file.absolutePath
            // Signature_path_tech = "file:" + file.absolutePath

            out.flush()
            out.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun SaveImage(bitmap: Bitmap?, pathname: String) {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val myDir = File(path, "Metal_Signature")
        myDir.mkdirs()

        val fname = pathname + ".jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
            Signature_path_tech = file.absolutePath
            Signature_path_techfile = "file:" + file.absolutePath
            //Signature_path_tech = "file:" + file.absolutePath

            out.flush()
            out.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun requestpermistion() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            B_flag = isDeviceSupportCamera()

                            if (B_flag == true) {

                                //final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Choose_from_Gallery), getResources().getString(R.string.Cancel)};
                                val options = arrayOf<CharSequence>(resources.getString(R.string.Take_Photo), resources.getString(R.string.Cancel))

                                val builder = AlertDialog.Builder(this@New_Feedback)

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

                                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                                        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                        val photoURI = FileProvider.getUriForFile(this@New_Feedback,
                                                BuildConfig.APPLICATION_ID + ".provider",
                                                photoFile!!)
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                        startActivityForResult(cameraIntent, 1)

                                    } else if (options[item] == resources.getString(R.string.Cancel)) {

                                        dialog.dismiss()

                                    }//                               else if (options[item].equals(getResources().getString(R.string.Choose_from_Gallery)))
                                    //
                                    //                            {
                                    //
                                    //                               image_check = "gallery";
                                    //                               Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    //
                                    //                               startActivityForResult(intent, 2);
                                    //
                                    //
                                    //                            }
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

    private fun requestpermistion2() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            B_flag = isDeviceSupportCamera()

                            if (B_flag == true) {

                                //final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Choose_from_Gallery), getResources().getString(R.string.Cancel)};
                                val options = arrayOf<CharSequence>(resources.getString(R.string.Take_Photo), resources.getString(R.string.Cancel))

                                val builder = AlertDialog.Builder(this@New_Feedback)

                                builder.setTitle(resources.getString(R.string.Add_Photo))

                                builder.setItems(options) { dialog, item ->
                                    if (options[item] == resources.getString(R.string.Take_Photo)) {
                                        image_check = "photo"

                                        var photoFile2: File? = null
                                        try {
                                            photoFile2 = createImageFile2()
                                        } catch (ex: IOException) {
                                            // Error occurred while creating the File
                                            Log.i("Image TAG", "IOException")
                                            pictureImagePath_dealer = ""
                                        }

                                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                        //  cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile2))
                                        val photoURI = FileProvider.getUriForFile(this@New_Feedback,
                                                BuildConfig.APPLICATION_ID + ".provider",
                                                photoFile2!!)
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                        startActivityForResult(cameraIntent, 2)

                                    } else if (options[item] == resources.getString(R.string.Cancel)) {

                                        dialog.dismiss()

                                    }//                               else if (options[item].equals(getResources().getString(R.string.Choose_from_Gallery)))
                                    //
                                    //                            {
                                    //
                                    //                               image_check = "gallery";
                                    //                               Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    //
                                    //                               startActivityForResult(intent, 2);
                                    //
                                    //
                                    //                            }
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
        val imageFileName = "custserv_tech"
        var storageDir: File? = null

        storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "feedback")


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
        pictureImagePath_new_Tech = image.absolutePath
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image
    }

    private fun createImageFile2(): File {
        // Create an image file name
        val imageFileName = "custserv_dealer"
        var storageDir: File? = null

        storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "feedback")

        if (!storageDir.exists()) {
            storageDir.mkdir()
        }

        val image = File.createTempFile(
                imageFileName, // prefix
                ".jpg", // suffix
                storageDir      // directory
        )

        // Save a file: path for use with ACTION_VIEW intents
        pictureImagePath_dealer = "file:" + image.absolutePath
        pictureImagePath_new_dealer = image.absolutePath
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image
    }


    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@New_Feedback)
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

    private fun deletedialog() {
        val alertDialog = AlertDialog.Builder(this@New_Feedback).create() //Read Update
        alertDialog.setTitle(resources.getString(R.string.Warning))
        alertDialog.setMessage(resources.getString(R.string.image_dialog_warning_message))
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, resources.getString(R.string.Yes)) { dialog, which ->
            val file = File(pictureImagePath_new_Tech)
            val delete = file.delete()

            val file1 = File(pictureImagePath)
            val delete1 = file1.delete()

            img_show1!!.setVisibility(View.GONE)
            crossimg1!!.setVisibility(View.GONE)

            Global_Data.Custom_Toast(this@New_Feedback, resources.getString(R.string.image_dialog_delete_success), "Yes")
            //          Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.image_dialog_delete_success),
            //                Toast.LENGTH_LONG);
            //          toast.setGravity(Gravity.CENTER, 0, 0);
            //          toast.show();

            //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, resources.getString(R.string.No_Button_label)) { dialog, which -> dialog.cancel() }
        alertDialog.show()

    }

    private fun deletedialog2() {
        val alertDialog = AlertDialog.Builder(this@New_Feedback).create() //Read Update
        alertDialog.setTitle(resources.getString(R.string.Warning))
        alertDialog.setMessage(resources.getString(R.string.image_dialog_warning_message))
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, resources.getString(R.string.Yes)) { dialog, which ->
            val file = File(pictureImagePath_new_dealer)
            val delete = file.delete()

            val file1 = File(pictureImagePath_dealer)
            val delete1 = file1.delete()

            imgframe_dealer!!.setVisibility(View.GONE)

            Global_Data.Custom_Toast(this@New_Feedback, resources.getString(R.string.image_dialog_delete_success), "Yes")
            //          Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.image_dialog_delete_success),
            //                Toast.LENGTH_LONG);
            //          toast.setGravity(Gravity.CENTER, 0, 0);
            //          toast.show();

            //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, resources.getString(R.string.No_Button_label)) { dialog, which -> dialog.cancel() }
        alertDialog.show()

    }


    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // if the result is capturing Image
        //    try {

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val imgFils = File(pictureImagePath_new_Tech)
            if (imgFils.exists()) {

                //img_show = findViewById(R.id.img_show);
                crossimg1!!.setVisibility(View.VISIBLE)
                img_show1!!.setVisibility(View.VISIBLE)
                //img_show.setRotation((float) 90.0);

                Glide.with(this@New_Feedback).load(pictureImagePath_new_Tech).into(img_show1!!)
                Glide.with(this@New_Feedback).load(pictureImagePath_new_Tech).into(Tech_photo_image!!)

                //img_show.setImageURI(Uri.fromFile(imgFils));
            }


        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            val imgFils = File(pictureImagePath_new_dealer)
            if (imgFils.exists()) {

                //img_show = findViewById(R.id.img_show);
                imgframe_dealer!!.setVisibility(View.VISIBLE)
                //img_show.setRotation((float) 90.0);

                Glide.with(this@New_Feedback).load(pictureImagePath_new_dealer).into(img_show_dealer!!)
                Glide.with(this@New_Feedback).load(pictureImagePath_new_dealer).into(dealer_photo_image!!)

                //img_show.setImageURI(Uri.fromFile(imgFils));
            }


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
                super.onBackPressed()
            }
        }
        //    return super.onOptionsItemSelected(item);

        when (item.itemId) {
            R.id.add -> {
                var targetNew = ""
                val sp = this@New_Feedback.getSharedPreferences("SimpleLogic", 0)
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