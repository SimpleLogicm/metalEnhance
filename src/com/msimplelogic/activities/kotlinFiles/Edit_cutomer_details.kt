package com.msimplelogic.activities.kotlinFiles

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
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
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.msimplelogic.activities.*
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval
import com.msimplelogic.helper.OnSwipeTouchListener
import com.msimplelogic.services.getServices
import com.msimplelogic.webservice.ConnectionDetector
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_edit_cutomer_details.*
import kotlinx.android.synthetic.main.content_edit_cutomer_details.*
import kotlinx.android.synthetic.main.content_visit__schedule.*
import kotlinx.android.synthetic.main.content_visit__schedule.hedder_theame
import kotlinx.android.synthetic.main.fragment_add_retailer.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Edit_cutomer_details : BaseActivity() {
    internal var B_flag: Boolean? = null
    internal var image_check = ""
    var pictureImagePath = ""
    var pictureImagePath_visitingcard = ""
    var pictureImagePath_Inshop = ""
    var pictureImagePath_Signboard = ""

    var pictureImagePath_visitingcard_server = ""
    var pictureImagePath_Inshop_server = ""
    var pictureImagePath_Signboard_server = ""

    var buttonSave: LinearLayout? = null
    var buttonnext: LinearLayout? = null
    var cardAddretailerDone: CardView? = null
    var cardAddretailerContact: CardView? = null
    var cardAddretailerPlace: CardView? = null
    var cardAddretailerDetails: CardView? = null
    var swipe: ImageView? = null
    var newswipeAddretailer: NestedScrollView? = null
    internal var choice = 0
    var et_storename: EditText? = null
    var et_address: EditText? = null
    var et_street: EditText? = null
    var et_Landmark: EditText? = null
    var et_pin: EditText? = null
    var tv_googleaddress: TextView? = null
    var tv_pin_add: TextView? = null
    var editTextcontact1: EditText? = null
    var editTextcontact2: EditText? = null
    var editTextcontact3: EditText? = null
    var editTextEmial: EditText? = null
    var editTextgst: EditText? = null
    var editTextbanckaccname: EditText? = null
    var editTextaccountno: EditText? = null
    var editTextifsc: EditText? = null
    var editTextAdharcard: EditText? = null
    var editTextpancard: EditText? = null
    var editTextshopanniversary: EditText? = null
    var editTextcust_birth: EditText? = null
    var visiting_card_pic: ImageView? = null
    var visiting_card_btn: ImageView? = null
    var inshop_display_pic: ImageView? = null
    var inshop_display_btn: ImageView? = null
    var signboard_pic: ImageView? = null
    var signboard_btn: ImageView? = null
    private var pic1PhotoPath = ""
    private var pic2PhotoPath = ""
    private var pic3PhotoPath = ""
    internal val REQUEST_IMAGE_CAPTURE = 1
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var context: Context? = null
    var dbvoc = DataBaseHelper(this)
    var dialogcustom: Dialog? = null
    var sp:SharedPreferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cutomer_details)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        cd = ConnectionDetector(this@Edit_cutomer_details)
        context = this@Edit_cutomer_details;

        Global_Data.emailcustomer=""
        newswipeAddretailer = findViewById<NestedScrollView>(R.id.newswipe_addretailer)
        cardAddretailerDetails = findViewById<CardView>(R.id.card_addretailerdetails)
        cardAddretailerPlace = findViewById<CardView>(R.id.card_addretailerplace)
        cardAddretailerContact = findViewById<CardView>(R.id.card_addretailercontact)
        cardAddretailerDone = findViewById<CardView>(R.id.card_addretailerdone)
        swipe = findViewById(R.id.swipe)
        buttonSave = findViewById(R.id.buttonSave)
        buttonnext = findViewById(R.id.buttonnext)
        editTextshopanniversary = findViewById(R.id.editTextshopanniversary)
        visiting_card_pic = findViewById(R.id.visiting_card_pic)
        visiting_card_btn = findViewById(R.id.visiting_card_btn)
        inshop_display_pic = findViewById(R.id.inshop_display_pic)
        inshop_display_btn = findViewById(R.id.inshop_display_btn)
        signboard_pic = findViewById(R.id.signboard_pic)
        signboard_btn = findViewById(R.id.signboard_btn)
        editTextshopanniversary = findViewById(R.id.editTextshopanniversary);
        editTextcust_birth = findViewById(R.id.editTextcust_birth);
        et_storename = findViewById(R.id.et_storename);
        et_address = findViewById(R.id.et_address);
        et_street = findViewById(R.id.et_street);
        et_Landmark = findViewById(R.id.et_Landmark);
        et_pin = findViewById(R.id.et_pin);
        tv_googleaddress = findViewById(R.id.tv_googleaddress);
        tv_pin_add = findViewById(R.id.tv_pin_add);
        editTextcontact1 = findViewById(R.id.editTextcontact1);
        editTextcontact2 = findViewById(R.id.editTextcontact2);
        editTextcontact3 = findViewById(R.id.editTextcontact3);
        editTextEmial = findViewById(R.id.editTextEmial);
        editTextgst = findViewById(R.id.editTextgst);
        editTextifsc = findViewById(R.id.editTextifsc);
        editTextaccountno = findViewById(R.id.editTextaccountno);
        editTextbanckaccname = findViewById(R.id.editTextbanckaccname);
        editTextAdharcard = findViewById(R.id.editTextAdharcard);
        editTextpancard = findViewById(R.id.editTextpancard);
        editTextshopanniversary = findViewById(R.id.editTextshopanniversary);
        editTextcust_birth = findViewById(R.id.editTextcust_birth);
        sp = getSharedPreferences("SimpleLogic", 0)
        val current_theme = sp?.getInt("CurrentTheme", 0)

        if (current_theme == 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder)
            nextimg.setImageResource(R.drawable.viewordermenu_dark)
            nextimg2.setImageResource(R.drawable.viewordermenu_dark)

//            val img: Drawable =   context!!.resources.getDrawable(R.drawable.ic_baseline_date_range_24_dark)
//
//            et_date!!.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        val cont1: List<Local_Data> = dbvoc.getCustomername(Global_Data.GLOvel_CUSTOMER_ID)
        if (cont1.size > 0) {

            for (cnt1 in cont1) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getADDRESS())) {
                    tv_googleaddress!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getADDRESS()))
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getCUSTOMER_SHOPNAME())) {
                    et_storename!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getCUSTOMER_SHOPNAME()))
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getAddress())) {
                    et_address!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getAddress()))
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getSTREET())) {
                    et_street!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getSTREET()))
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getLANDMARK())) {
                    et_Landmark!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getLANDMARK()))
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getPIN_CODE())) {
                    et_pin!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getPIN_CODE()))
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getMOBILE_NO())) {
                    editTextcontact1!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getMOBILE_NO()))
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getMobile2())) {
                    editTextcontact2!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getMobile2()))
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getLANDLINE_NO())) {
                    editTextcontact3!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getLANDLINE_NO()))
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getEMAIL_ADDRESS())) {
                    editTextEmial!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getEMAIL_ADDRESS()))
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getvatin())) {
                    editTextgst!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getvatin()))
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getBank_account_name())) {
                    editTextbanckaccname!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getBank_account_name()))
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getBank_account_no())) {
                    editTextaccountno!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getBank_account_no()))
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getBank_account_ifsc())) {
                    editTextifsc!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getBank_account_ifsc()))
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getAdhar_no())) {
                    editTextAdharcard!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getAdhar_no()))
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getPan_card_no())) {
                    editTextpancard!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getPan_card_no()))
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getShop_anniversary_date())) {
                    editTextshopanniversary!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getShop_anniversary_date()))
                }
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getDate_of_birthday())) {
                    editTextcust_birth!!.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(cnt1.getDate_of_birthday()))
                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getVisitc_img())) {

                    if (cnt1.getVisitc_img().indexOf("http:") != -1) {
                        pictureImagePath_visitingcard_server = "yes";
                        pictureImagePath_visitingcard = cnt1.getVisitc_img()
                        Glide.with(this@Edit_cutomer_details).load(pictureImagePath_visitingcard).into(visiting_card_pic!!)
                    } else {
                        pictureImagePath_visitingcard = cnt1.getVisitc_img()
                        Glide.with(this@Edit_cutomer_details).load(pictureImagePath_visitingcard).into(visiting_card_pic!!)
                    }

                }


                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getInshop_img())) {
                    if (cnt1.getVisitc_img().indexOf("http:") != -1) {
                        pictureImagePath_Inshop_server = "yes";
                        pictureImagePath_Inshop = cnt1.getInshop_img()
                        Glide.with(this@Edit_cutomer_details).load(pictureImagePath_Inshop).into(inshop_display_pic!!)
                    } else {
                        pictureImagePath_Inshop = cnt1.getInshop_img()
                        Glide.with(this@Edit_cutomer_details).load(pictureImagePath_Inshop).into(inshop_display_pic!!)
                    }

                }

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cnt1.getSignboard_img())) {

                    if (cnt1.getVisitc_img().indexOf("http:") != -1) {
                        pictureImagePath_Signboard_server = "yes";
                        pictureImagePath_Signboard = cnt1.getSignboard_img()
                        Glide.with(this@Edit_cutomer_details).load(pictureImagePath_Signboard).into(signboard_pic!!)
                    } else {
                        pictureImagePath_Signboard = cnt1.getSignboard_img()
                        Glide.with(this@Edit_cutomer_details).load(pictureImagePath_Signboard).into(signboard_pic!!)
                    }

                }


            }
        }


        tv_pin_add!!.setOnClickListener {

            var str: java.lang.StringBuilder;
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {
                try {
                    val geo = Geocoder(this@Edit_cutomer_details.getApplicationContext(), Locale.getDefault())
                    val addresses = geo.getFromLocation(Global_Data.GLOvel_LATITUDE.toDouble(), Global_Data.GLOvel_LONGITUDE.toDouble(), 1)
                    if (addresses.isEmpty()) { //yourtextfieldname.setText("Waiting for Location");

                    } else {
                        if (addresses.size > 0) {
                            str = StringBuilder()
                            str.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses[0].getAddressLine(0)) + " ")
                            if (str.indexOf(addresses[0].locality) <= 0) {
                                str.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses[0].locality) + " ")
                            }
                            if (str.indexOf(addresses[0].adminArea) <= 0) {
                                str.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses[0].adminArea) + " ")
                            }
                            if (str.indexOf(addresses[0].countryName) <= 0) {
                                str.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses[0].countryName) + " ")
                            }
                            if (str.indexOf(addresses[0].postalCode) <= 0) {
                                str.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses[0].postalCode) + " ")
                            }
                            str.append("\n")
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(str.toString())) {
                                Global_Data.address = str.toString()
                            }
                            tv_googleaddress!!.setText(str)
                        } else {
                            Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            } else {
                Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")
            }
        }
        visiting_card_btn!!.setOnClickListener {
            requestpermistion()
        }
        inshop_display_btn!!.setOnClickListener {
            requestpermistion2()

        }
        signboard_btn!!.setOnClickListener {
            requestpermistion3()

        }

        visiting_card_pic!!.setOnClickListener {

            if (!pictureImagePath_visitingcard.equals("")) {
                image_zoom_dialog(pictureImagePath_visitingcard, "pictureImagePath_visitingcard")
            }

        }

        inshop_display_pic!!.setOnClickListener {

            if (!pictureImagePath_Inshop.equals("")) {
                image_zoom_dialog(pictureImagePath_Inshop, "pictureImagePath_Inshop")
            }

        }

        signboard_pic!!.setOnClickListener {

            if (!pictureImagePath_Signboard.equals("")) {
                image_zoom_dialog(pictureImagePath_Signboard, "pictureImagePath_Signboard")
            }

        }



        editTextshopanniversary!!.setOnClickListener {

            val now = Calendar.getInstance()
            DatePickerDialog(
                    Edit_cutomer_details@ this,
                    DatePickerDialog.OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        var mo = month
                        ++mo
                        Log.d("Orignal", mo.toString())
                        var date_from = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;
                        editTextshopanniversary!!.setText(date_from)

                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        editTextcust_birth!!.setOnClickListener {

            val now = Calendar.getInstance()
            DatePickerDialog(
                    Edit_cutomer_details@ this,
                    DatePickerDialog.OnDateSetListener { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        var mo = month
                        ++mo
                        Log.d("Orignal", mo.toString())
                        var date_from = "" + Kot_Gloval.getPaddedNumber(dayOfMonth) + "-" + (Kot_Gloval.getPaddedNumber(mo)) + "-" + year;
                        editTextcust_birth!!.setText(date_from)

                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        buttonSave!!.setOnClickListener {
            val sdf: SimpleDateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            var cutmob = ""
            var customername = ""
            var customernamem2 = ""
            var customernamel = ""
            var mob2 = ""
            var land = ""
            dbvoc = DataBaseHelper(this@Edit_cutomer_details)


            val cont1 = dbvoc.getmob_byshop_mobileval(editTextcontact1!!.getText().toString())
            val cont2 = dbvoc.getmob_byshop_mobile2(editTextcontact2!!.getText().toString())
            val cont3 = dbvoc.getmob_byshop_land(editTextcontact3!!.getText().toString())

            if (cont1.size > 1 && editTextcontact1!!.text.toString() != "") {
                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Mobile No 1 Already Exist.", "yes")
            } else {


                for (cnt1 in cont1) {

                    customername = cnt1.getCUSTOMER_SHOPNAME()
                    cutmob = cnt1.getMOBILE_NO()
//                        mob2 = cnt1.getMobile2()
//                        land = cnt1.getLANDLINE_NO()


                }
            }

            //}

            if (cont2.size > 1 && editTextcontact2!!.text.toString() != "") {
                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Mobile No 2 Already Exist.", "yes")
            } else {
//                    if (cont2.size > 0) {

                for (cnt1 in cont2) {

                    customernamem2 = cnt1.getCUSTOMER_SHOPNAME()
                    //     cutmob = cnt1.getMOBILE_NO()
                    mob2 = cnt1.getMobile2()
                    //     land = cnt1.getLANDLINE_NO()


                }


            }

            if (cont3.size > 1 && editTextcontact3!!.text.toString() != "") {
                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Landline No Already Exist.", "yes")
            } else {
//                    if (cont3.size > 0) {

                for (cnt1 in cont3) {

                    customernamel = cnt1.getCUSTOMER_SHOPNAME()
                    //   cutmob = cnt1.getMOBILE_NO()
                    //  mob2 = cnt1.getMobile2()
                    land = cnt1.getLANDLINE_NO()


                }
            }



            if (et_storename!!.text.toString().trim().equals("")) {
                Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.Enter_Store_Name), "yes")
            } else
                if (et_address!!.text.toString().trim().equals("")) {
                    Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.Enter_Address), "yes")
                } else
                    if (et_pin!!.text.toString().trim().equals("")) {
                        Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.Enter_Pin), "yes")
                    } else
                        if (et_pin!!.text.length < 6) {
                            Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.Enter_Valid_Pin), "yes")
                        } else
                            if (editTextcontact1!!.text.toString().trim().equals("")) {
                                Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.EnterMobile1), "yes")
                            } else
                                if (editTextcontact1!!.text.toString().trim().equals("")) {
                                    Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.VEnterMobile1), "yes")
                                } else
                                    if (editTextcontact1!!.text.length < 10) {
                                        Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.VEnterMobile1), "yes")
                                    } else if (editTextcontact2!!.text.toString().trim().equals("") && editTextcontact2!!.text.length != 0) {
                                        Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.EnterMobile2), "yes")

                                    } else if (editTextcontact2!!.text.length < 10 && editTextcontact2!!.text.length != 0) {
                                        Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.VEnterMobile2), "yes")


                                    } else if (editTextcontact1!!.text.toString() == editTextcontact2!!.text.toString() && editTextcontact1!!.text.toString() != "" && editTextcontact2!!.text.toString() != "") {
                                        Global_Data.Custom_Toast(UpdateStockScreen3@ this, "Mobile No1 and Mobile No.2 Should not be same", "yes")


                                    }
//            else
//                if(editTextcontact3!!.text.toString().trim().equals("")){
//
//                    Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.EnterMobile3), "yes")
//
//                }
                                    else
                                        if (editTextcontact3!!.text.length < 6 && editTextcontact3!!.text.length != 0) {

                                            Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.VEnterMobile3), "yes")

                                        } else
                                            if (editTextcontact1!!.text.toString() == editTextcontact3!!.text.toString() && editTextcontact1!!.text.toString() != "" && editTextcontact3!!.text.toString() != "") {

                                                Global_Data.Custom_Toast(UpdateStockScreen3@ this, "Mobile No 1 and Landline No Should not be same", "yes")

                                            } else
                                                if (editTextcontact2!!.text.toString() == editTextcontact3!!.text.toString() && editTextcontact2!!.text.toString() != "" && editTextcontact3!!.text.toString() != "") {
                                                    Global_Data.Custom_Toast(UpdateStockScreen3@ this, "Mobile No 2 and Landline No Should not be same", "yes")

                                                } else

                                                    if (editTextcontact1!!.text.toString() == editTextcontact2!!.text.toString() && editTextcontact1!!.text.toString() != "" && editTextcontact2!!.text.toString() != "") {
                                                        Global_Data.Custom_Toast(UpdateStockScreen3@ this, "Mobile No 1 and Mobile No 2 No Should not be same", "yes")

                                                    } else


                                                        if (cont1.size > 1 && editTextcontact1!!.text.toString() != "") {
                                                            Global_Data.Custom_Toast(this@Edit_cutomer_details, "Mobile No 1 Already Exist.", "yes")

                                                        } else
                                                            if (customername != et_storename!!.text.toString() && cutmob == editTextcontact1!!.text.toString()) {
                                                                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Mobile No 1 Already Exist.", "yes")
                                                            } else if (customername != et_storename!!.text.toString() && cutmob != editTextcontact1!!.text.toString() && cutmob == editTextcontact2!!.text.toString()) {
                                                                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Mobile No 2 Already Exist.", "yes")

                                                            } else if (customername != et_storename!!.text.toString() && cutmob != editTextcontact1!!.text.toString() && cutmob != editTextcontact2!!.text.toString() && cutmob == editTextcontact3!!.text.toString()) {
                                                                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Landline No Already Exist.", "yes")

                                                            } else if (cont2.size > 1 && editTextcontact2!!.text.toString() != "") {
                                                                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Mobile No 2 Already Exist.", "yes")

                                                            } else if (customernamem2 != et_storename!!.text.toString() && mob2 == editTextcontact2!!.text.toString() && editTextcontact2!!.text.length != 0) {
                                                                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Mobile No 2 Already Exist.", "yes")

                                                            } else if (customernamem2 != et_storename!!.text.toString() && mob2 != editTextcontact2!!.text.toString() && editTextcontact2!!.text.length != 0 && mob2 == editTextcontact1!!.text.toString()) {
                                                                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Mobile No 1 Already Exist.", "yes")

                                                            } else if (customernamem2 != et_storename!!.text.toString() && mob2 != editTextcontact2!!.text.toString() && editTextcontact3!!.text.length != 0 && mob2 == editTextcontact3!!.text.toString()) {
                                                                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Landline No Already Exist.", "yes")

                                                            } else if (cont3.size > 1 && editTextcontact3!!.text.toString() != "") {
                                                                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Landline No Already Exist.", "yes")

                                                            } else if (customernamel != et_storename!!.text.toString() && land == editTextcontact3!!.text.toString() && editTextcontact3!!.text.length != 0) {
                                                                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Landline No Already Exist.", "yes")

                                                            } else if (customernamel != et_storename!!.text.toString() && land != editTextcontact3!!.text.toString() && editTextcontact3!!.text.length != 0 && land == editTextcontact1!!.text.toString()) {
                                                                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Mobile No 1 Already Exist.", "yes")

                                                            } else if (customernamel != et_storename!!.text.toString() && land != editTextcontact3!!.text.toString() && editTextcontact3!!.text.length != 0 && land == editTextcontact2!!.text.toString()) {
                                                                Global_Data.Custom_Toast(this@Edit_cutomer_details, "Mobile No 2 Already Exist.", "yes")

                                                            } else


                                                                if (editTextEmial!!.text.toString().trim().equals("")) {
                                                                    Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.Enter_Email_Address), "yes")
                                                                } else
                                                                    if (!Global_Data.isValidEmail(editTextEmial!!.text)) {
                                                                        Global_Data.Custom_Toast(UpdateStockScreen3@ this, resources.getString(R.string.Enter_Valid_Email_Address), "yes")
                                                                    } else
//                                                                    if(editTextAdharcard!!.text.toString().trim().equals("")){
//                                                                        Global_Data.Custom_Toast(UpdateStockScreen3@ this, "Please enter Adahar Card Number", "yes")
//
//                                                                    }else if(editTextpancard!!.text.toString().trim().equals("")){
//                                                                        Global_Data.Custom_Toast(UpdateStockScreen3@ this, "Please enter Pan Card Number", "yes")
//
//                                                                    }else

                                                                    {
                                                                        cd = ConnectionDetector(applicationContext)
                                                                        isInternetPresent = cd!!.isConnectingToInternet
                                                                        if (isInternetPresent) {

                                                                            dbvoc.updateshop_details_Did(Global_Data.GLOvel_CUSTOMER_ID, et_address!!.text.toString().trim(), editTextcontact1!!.text.toString().trim(), et_storename!!.text.toString().trim(), et_Landmark!!.text.toString().trim(), et_street!!.text.toString().trim(), et_pin!!.text.toString().trim(), editTextcontact2!!.text.toString().trim(), editTextcontact3!!.text.toString().trim(), editTextEmial!!.text.toString().trim(), editTextgst!!.text.toString().trim(), editTextbanckaccname!!.text.toString().trim(), editTextaccountno!!.text.toString().trim(), editTextifsc!!.text.toString().trim(), editTextAdharcard!!.text.toString().trim(), editTextpancard!!.text.toString().trim(), editTextshopanniversary!!.text.toString().trim(), editTextcust_birth!!.text.toString().trim(), pictureImagePath_visitingcard, pictureImagePath_Inshop, pictureImagePath_Signboard, tv_googleaddress!!.text.toString().trim(), currentDate);

                                                                            getServices.SYNCustomer(this@Edit_cutomer_details, "customer", "")
//                                                                            val i = Intent(UpdateStockScreen3@ this, Sales_Dash::class.java)
//                                                                            startActivity(i)
//                                                                            finish()
                                                                        } else {
                                                                            Global_Data.emailcustomer=editTextEmial!!.text.toString()

                                                                            dbvoc.updateshop_details_Did(Global_Data.GLOvel_CUSTOMER_ID, et_address!!.text.toString().trim(), editTextcontact1!!.text.toString().trim(), et_storename!!.text.toString().trim(), et_Landmark!!.text.toString().trim(), et_street!!.text.toString().trim(), et_pin!!.text.toString().trim(), editTextcontact2!!.text.toString().trim(), editTextcontact3!!.text.toString().trim(), editTextEmial!!.text.toString().trim(), editTextgst!!.text.toString().trim(), editTextbanckaccname!!.text.toString().trim(), editTextaccountno!!.text.toString().trim(), editTextifsc!!.text.toString().trim(), editTextAdharcard!!.text.toString().trim(), editTextpancard!!.text.toString().trim(), editTextshopanniversary!!.text.toString().trim(), editTextcust_birth!!.text.toString().trim(), pictureImagePath_visitingcard, pictureImagePath_Inshop, pictureImagePath_Signboard, tv_googleaddress!!.text.toString().trim(), currentDate);

                                                                            val i = Intent(UpdateStockScreen3@ this, Sales_Dash::class.java)
                                                                            startActivity(i)
                                                                            finish()

                                                                            Global_Data.Custom_Toast(context, resources.getString(R.string.CEDIT), "Yes")

                                                                            Global_Data.Custom_Toast(context, resources.getString(R.string.internet_connection_error), "Yes")
                                                                        }
                                                                    }

        }

        newswipeAddretailer!!.setOnTouchListener(object : OnSwipeTouchListener(this@Edit_cutomer_details) {

        override fun onSwipeRight() {
                when (choice) {
                    3 -> {
                        swipe!!.setImageResource(R.drawable.fourtimeline3)
                        cardAddretailerDetails!!.setVisibility(View.GONE)
                        cardAddretailerPlace!!.setVisibility(View.GONE)
                        cardAddretailerContact!!.setVisibility(View.VISIBLE)
                        cardAddretailerDone!!.setVisibility(View.GONE)
                        buttonSave!!.setVisibility(View.GONE)
                        buttonnext!!.setVisibility(View.VISIBLE)
                        choice--
                    }
                    2 -> {
                        swipe!!.setImageResource(R.drawable.fourtimeline2)
                        cardAddretailerDetails!!.setVisibility(View.GONE)
                        cardAddretailerPlace!!.setVisibility(View.VISIBLE)
                        cardAddretailerContact!!.setVisibility(View.GONE)
                        cardAddretailerDone!!.setVisibility(View.GONE)
                        buttonSave!!.setVisibility(View.GONE)
                        buttonnext!!.setVisibility(View.VISIBLE)
                        choice--
                    }
                    1 -> {
                        swipe!!.setImageResource(R.drawable.fourtimeline1)
                        cardAddretailerDetails!!.setVisibility(View.VISIBLE)
                        cardAddretailerPlace!!.setVisibility(View.GONE)
                        cardAddretailerContact!!.setVisibility(View.GONE)
                        cardAddretailerDone!!.setVisibility(View.GONE)
                        buttonSave!!.setVisibility(View.GONE)
                        buttonnext!!.setVisibility(View.VISIBLE)
                        choice--
                    }
                    else -> {
                    }
                }
            }

            override fun onSwipeLeft() {
                when (choice) {
                    0 -> {
                        swipe!!.setImageResource(R.drawable.fourtimeline2)
                        cardAddretailerDetails!!.setVisibility(View.GONE)
                        cardAddretailerPlace!!.setVisibility(View.VISIBLE)
                        cardAddretailerContact!!.setVisibility(View.GONE)
                        cardAddretailerDone!!.setVisibility(View.GONE)
                        buttonSave!!.setVisibility(View.GONE)
                        buttonnext!!.setVisibility(View.VISIBLE)
                        choice++
                    }
                    1 -> {
                        swipe!!.setImageResource(R.drawable.fourtimeline3)
                        cardAddretailerDetails!!.setVisibility(View.GONE)
                        cardAddretailerPlace!!.setVisibility(View.GONE)
                        cardAddretailerContact!!.setVisibility(View.VISIBLE)
                        cardAddretailerDone!!.setVisibility(View.GONE)
                        buttonSave!!.setVisibility(View.GONE)
                        buttonnext!!.setVisibility(View.VISIBLE)
                        choice++
                    }
                    2 -> {
                        swipe!!.setImageResource(R.drawable.fourtimeline4)
                        cardAddretailerDetails!!.setVisibility(View.GONE)
                        cardAddretailerPlace!!.setVisibility(View.GONE)
                        cardAddretailerContact!!.setVisibility(View.GONE)
                        cardAddretailerDone!!.setVisibility(View.VISIBLE)
                        buttonSave!!.setVisibility(View.VISIBLE)
                        buttonnext!!.setVisibility(View.GONE)

                        choice++
                    }
                    else -> {
                    }
                }
            }
        })


//        buttonSave!!.setOnClickListener {
//            validations()
//        }


    }


    private fun validations() {
        if (et_storename!!.getText().length == 0) run {

            Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_Store_Name), "yes")

        } else
            if (et_address!!.getText().length == 0) run {

                Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_Address), "yes")

            } else
                if (et_street!!.getText().length == 0) run {

                    Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_Street), "yes")

                } else
                    if (et_Landmark!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_LandMark), "yes")

                    } else if (et_pin!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_Pin), "yes")

                    } else if (editTextcontact1!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_Contact_Number1), "yes")

                    } else if (editTextcontact2!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_Contact_Number2), "yes")

                    } else if (editTextcontact3!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_Contact_Number3), "yes")
                    } else if (editTextEmial!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_Email_Address), "yes")

                    } else if (editTextgst!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_GSTIN_Number), "yes")

                    } else if (editTextbanckaccname!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_Bank_Account_name), "yes")

                    } else if (editTextaccountno!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_Bank_Account_no), "yes")

                    } else if (editTextifsc!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_Bank_Account_ifsc), "yes")

                    } else if (editTextAdharcard!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_adhar_no), "yes")

                    } else if (editTextpancard!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.Enter_pan_no), "yes")

                    } else if (editTextshopanniversary!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string.select_Shop_Anniversary), "yes")

                    } else if (editTextcust_birth!!.getText().length == 0) run {

                        Global_Data.Custom_Toast(this@Edit_cutomer_details, resources.getString(R.string._select_Customer_bith_date), "yes")

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

                                val builder = AlertDialog.Builder(this@Edit_cutomer_details)

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
                                        // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                                        val photoURI = FileProvider.getUriForFile(this@Edit_cutomer_details,
                                                BuildConfig.APPLICATION_ID + ".provider",
                                                photoFile!!)
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                        startActivityForResult(cameraIntent, 1)

                                    } else if (options[item] == resources.getString(R.string.Cancel)) {

                                        dialog.dismiss()

                                    }//										else if (options[item].equals(getResources().getString(R.string.Choose_from_Gallery)))
                                    //
                                    //										{
                                    //
                                    //											image_check = "gallery";
                                    //											Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    //
                                    //											startActivityForResult(intent, 2);
                                    //
                                    //
                                    //										}
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

                                val builder = AlertDialog.Builder(this@Edit_cutomer_details)

                                builder.setTitle(resources.getString(R.string.Add_Photo))

                                builder.setItems(options) { dialog, item ->
                                    if (options[item] == resources.getString(R.string.Take_Photo)) {
                                        image_check = "photo"

                                        var photoFile: File? = null
                                        try {
                                            photoFile = createImageFile2()
                                        } catch (ex: IOException) {
                                            // Error occurred while creating the File
                                            Log.i("Image TAG", "IOException")
                                            pictureImagePath = ""
                                        }

                                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                        // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                                        val photoURI = FileProvider.getUriForFile(this@Edit_cutomer_details,
                                                BuildConfig.APPLICATION_ID + ".provider",
                                                photoFile!!)
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                        startActivityForResult(cameraIntent, 2)

                                    } else if (options[item] == resources.getString(R.string.Cancel)) {

                                        dialog.dismiss()

                                    }//										else if (options[item].equals(getResources().getString(R.string.Choose_from_Gallery)))
                                    //
                                    //										{
                                    //
                                    //											image_check = "gallery";
                                    //											Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    //
                                    //											startActivityForResult(intent, 2);
                                    //
                                    //
                                    //										}
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

    private fun requestpermistion3() {
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

                                val builder = AlertDialog.Builder(this@Edit_cutomer_details)

                                builder.setTitle(resources.getString(R.string.Add_Photo))

                                builder.setItems(options) { dialog, item ->
                                    if (options[item] == resources.getString(R.string.Take_Photo)) {
                                        image_check = "photo"

                                        var photoFile: File? = null
                                        try {
                                            photoFile = createImageFile3()
                                        } catch (ex: IOException) {
                                            // Error occurred while creating the File
                                            Log.i("Image TAG", "IOException")
                                            pictureImagePath = ""
                                        }

                                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                        // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                                        val photoURI = FileProvider.getUriForFile(this@Edit_cutomer_details,
                                                BuildConfig.APPLICATION_ID + ".provider",
                                                photoFile!!)
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                        startActivityForResult(cameraIntent, 3)

                                    } else if (options[item] == resources.getString(R.string.Cancel)) {

                                        dialog.dismiss()

                                    }//										else if (options[item].equals(getResources().getString(R.string.Choose_from_Gallery)))
                                    //
                                    //										{
                                    //
                                    //											image_check = "gallery";
                                    //											Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    //
                                    //											startActivityForResult(intent, 2);
                                    //
                                    //
                                    //										}
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


    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@Edit_cutomer_details)
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

    private fun createImageFile(): File {
        // Create an image file name
        val imageFileName = "cust_visiting_card"
        var storageDir: File? = null

        storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Customer Details")


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
        pictureImagePath_visitingcard = image.absolutePath
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image
    }

    private fun createImageFile2(): File {
        // Create an image file name
        val imageFileName = "cust_Inshop"
        var storageDir: File? = null

        storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Customer Details")


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
        pictureImagePath_Inshop = image.absolutePath
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image
    }

    private fun createImageFile3(): File {
        // Create an image file name
        val imageFileName = "cust_sign_board"
        var storageDir: File? = null

        storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Customer Details")


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
        pictureImagePath_Signboard = image.absolutePath
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image
    }


    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // if the result is capturing Image
        //		try {

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val imgFils = File(pictureImagePath_visitingcard)
            if (imgFils.exists()) {

                //img_show = findViewById(R.id.img_show);
//                crossimg1!!.setVisibility(View.VISIBLE)
//                img_show1!!.setVisibility(View.VISIBLE)
                //img_show.setRotation((float) 90.0);

                Glide.with(this@Edit_cutomer_details).load(pictureImagePath_visitingcard).into(visiting_card_pic!!)

                //img_show.setImageURI(Uri.fromFile(imgFils));
            }


        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            val imgFils = File(pictureImagePath_Inshop)
            if (imgFils.exists()) {

                //img_show = findViewById(R.id.img_show);
//                crossimg1!!.setVisibility(View.VISIBLE)
//                img_show1!!.setVisibility(View.VISIBLE)
                //img_show.setRotation((float) 90.0);

                Glide.with(this@Edit_cutomer_details).load(pictureImagePath_Inshop).into(inshop_display_pic!!)

                //img_show.setImageURI(Uri.fromFile(imgFils));
            }


        }
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            val imgFils = File(pictureImagePath_Signboard)
            if (imgFils.exists()) {

                //img_show = findViewById(R.id.img_show);
//                crossimg1!!.setVisibility(View.VISIBLE)
//                img_show1!!.setVisibility(View.VISIBLE)
                //img_show.setRotation((float) 90.0);

                Glide.with(this@Edit_cutomer_details).load(pictureImagePath_Signboard).into(signboard_pic!!)

                //img_show.setImageURI(Uri.fromFile(imgFils));
            }


        }
//        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
//            val imgFils = File(pictureImagePath_new_dealer)
//            if (imgFils.exists()) {
//
//                //img_show = findViewById(R.id.img_show);
//                imgframe_dealer!!.setVisibility(View.VISIBLE)
//                //img_show.setRotation((float) 90.0);
//
//                Glide.with(this@New_Feedback).load(pictureImagePath_new_dealer).into(img_show_dealer!!)
//
//                //img_show.setImageURI(Uri.fromFile(imgFils));
//            }
//
//
//        }

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
                val sp = this@Edit_cutomer_details.getSharedPreferences("SimpleLogic", 0)
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

    fun image_zoom_dialog(url: String?, image_flag: String) {
        dialogcustom = Dialog(context!!)
        dialogcustom!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogcustom!!.getWindow()!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        dialogcustom!!.setContentView(R.layout.collection_image_dialog)
        val Collection_zoom_image = dialogcustom!!.findViewById(R.id.Collection_zoom_image) as ImageView
        //        Glide.with(_context).load(hm_url)
//                .placeholder(R.drawable.loa)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(Collection_zoom_image);
        Glide.with(Edit_cutomer_details@ this).load(url)
                .thumbnail(Glide.with(Edit_cutomer_details@ this).load("file:///android_asset/loading.gif"))
                .fitCenter() // .crossFade()
                .into(Collection_zoom_image)
        val collection_zoom_delete = dialogcustom!!.findViewById(R.id.collection_zoom_delete) as Button
        val collection_zoom_ok = dialogcustom!!.findViewById(R.id.collection_zoom_ok) as Button
        collection_zoom_delete.setOnClickListener {
            val file = File(url)
            if (file.exists()) {
                file.delete()

                if (image_flag.equals("pictureImagePath_visitingcard")) {
                    pictureImagePath_visitingcard = "";
                    Glide.with(this@Edit_cutomer_details).load(R.drawable.img_not_found).into(visiting_card_pic!!)
                } else
                    if (image_flag.equals("pictureImagePath_Inshop")) {
                        pictureImagePath_Inshop = "";
                        Glide.with(this@Edit_cutomer_details).load(R.drawable.img_not_found).into(inshop_display_pic!!)
                    } else
                        if (image_flag.equals("pictureImagePath_Signboard")) {
                            pictureImagePath_Signboard = "";
                            Glide.with(this@Edit_cutomer_details).load(R.drawable.img_not_found).into(signboard_pic!!)
                        }
            } else {
                if (image_flag.equals("pictureImagePath_visitingcard")) {
                    pictureImagePath_visitingcard = "";
                    Glide.with(this@Edit_cutomer_details).load(R.drawable.img_not_found).into(visiting_card_pic!!)
                } else
                    if (image_flag.equals("pictureImagePath_Inshop")) {
                        pictureImagePath_Inshop = "";
                        Glide.with(this@Edit_cutomer_details).load(R.drawable.img_not_found).into(inshop_display_pic!!)
                    } else
                        if (image_flag.equals("pictureImagePath_Signboard")) {
                            pictureImagePath_Signboard = "";
                            Glide.with(this@Edit_cutomer_details).load(R.drawable.img_not_found).into(signboard_pic!!)
                        }
            }
            dialogcustom!!.dismiss()

        }
        collection_zoom_ok.setOnClickListener { dialogcustom!!.dismiss() }
        dialogcustom!!.setCancelable(false)
        dialogcustom!!.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        //val i = Intent(UpdateStockScreen3@ this, Sales_Dash::class.java)
        // startActivity(i)
        finish()
    }
}
