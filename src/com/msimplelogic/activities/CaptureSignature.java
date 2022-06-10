package com.msimplelogic.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Checkout_quick_order;
import com.msimplelogic.helper.OnSwipeTouchListener;
import com.msimplelogic.model.Product;
import com.msimplelogic.services.getServices;
import com.msimplelogic.webservice.ConnectionDetector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

//import com.simplelogic.database.DatabaseHandler;
//import com.simplelogic.webservice.GmailSender;

public class CaptureSignature extends BaseActivity {
    //DataBaseHelper dbvoc;
    CardView signatureCardFirst, signatureCardSecond;
    RelativeLayout signatureCardThird;
    private String Signature_path = "";
    private String mCurrentPhotoPath = "";
    private String mCurrentPhotoPathnft = "";
    Boolean isInternetPresent = false;
    ImageView get_icon, swipe;
    Boolean B_flag;
    String strdetail1_mandate, strdetail2_mandate;
    Bitmap bitmap1;
    byte b5[];
    int choice = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_FROM_CAMERA = 1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    ConnectionDetector cd;
    String str_uri;
    private Uri fileUri;
    LinearLayout mContent;
    Button mClear1, mGetSign, mCancel;
    TextView mClear;
    int m_sign_flag = 0;
    DatePickerDialog.OnDateSetListener date, date1;
    ArrayList<Product> dataOrder = new ArrayList<Product>();
    public static String tempDir;
    public int count = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public String current = null;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    private Bitmap mBitmap;
    ImageView cam_btn;
    View mView;
    String detail1str, detail2str;
    int userID, cityID, beatID, retailerID, distID;
    TextView details1, details2;
    Calendar myCalendar;
    //private String uniqueId;
    LinearLayout order_de;
    private EditText yourName, order_detail1, order_detail2;
    Spinner order_type, s_payment_mode;
    ArrayAdapter<String> dataAdapter_order_type;
    List<String> list_order_type;
    private ArrayList<String> results_order_type = new ArrayList<String>();
    float totalPrice;
    TextView btnCash, btnCheque, btnNeft;
    public String order = "", retailer_code = "";
    Toolbar toolbar;
    SignaturePad signature_pad;
    private String[] arraySpinner;
    ArrayAdapter<String> adapter;
    CoordinatorLayout linearLayout_swipe;
    TextView dis_name;
    String capimg ="";
    String hintsa = "";
    ImageView hedder_theame,imgnext1,nextimg2;
    // ImageView cam_btn;

    SharedPreferences sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_main);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        setTitle("");//null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        cd = new ConnectionDetector(getApplicationContext());

        SharedPreferences sp = this.getSharedPreferences("SimpleLogic", 0);
        order = sp.getString("order", "");

        mClear = findViewById(R.id.clear);
        hedder_theame = findViewById(R.id.hedder_theame);
        imgnext1 = findViewById(R.id.imgnext1);
        nextimg2 = findViewById(R.id.nextimg2);
        mGetSign = findViewById(R.id.getsign);
        mCancel = findViewById(R.id.cancel);
        signature_pad = findViewById(R.id.signature_pad);
        // cam_btn = findViewById(R.id.cam_btn);
        yourName = findViewById(R.id.yourName);
        dis_name = findViewById(R.id.username);
        order_detail1 = findViewById(R.id.order_detail1);
        order_detail2 = findViewById(R.id.order_detail2);
        get_icon = findViewById(R.id.get_icon);
        order_type = findViewById(R.id.order_type);
        cam_btn = findViewById(R.id.cam_btn);
        s_payment_mode = findViewById(R.id.s_payment_mode);
        signatureCardFirst = findViewById(R.id.signature_card1);
        signatureCardSecond = findViewById(R.id.signature_card2);
        signatureCardThird = findViewById(R.id.signature_card3);
        btnCash = findViewById(R.id.btn_cash);
        btnCheque = findViewById(R.id.btn_cheque);
        btnNeft = findViewById(R.id.btn_neft);
        order_de = findViewById(R.id.order_de);
        swipe = findViewById(R.id.swipe);
        linearLayout_swipe = findViewById(R.id.linearLayout1);
        //etPdcNo =  findViewById(R.id.order_detail2);

        Intent i = getIntent();
        dataOrder = i.getParcelableArrayListExtra("productsList");

        String dis_id = "";
        String beat_id = "";
        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);
            imgnext1.setImageResource(R.drawable.viewordermenu_dark);
            nextimg2.setImageResource(R.drawable.viewordermenu_dark);
        }

//        List<Local_Data> getbeatid = dbvoc.GetOrders_beatID(Global_Data.GLOvel_GORDER_ID);
//        for (Local_Data cn : getbeatid) {
//            beat_id = cn.getBEAT_ID();
//        }

        String beatCode="";
        if(Global_Data.DistributorStatus.length()>0)
        {
            beatCode=sp.getString("beatid", "");
            Global_Data.DistributorStatus="";
        }else{
            beatCode=sp.getString("Beat_Code", "");
        }



        List<Local_Data> getdistri_code = dbvoc.getDistributors_code(beatCode);
        for (Local_Data cn : getdistri_code) {
            dis_id = cn.getDISTRIBUTER_ID();
        }

//        List<Local_Data> getdistri_name = dbvoc.getDistributorByBeat(shopName);
//        for (Local_Data cn1 : getdistri_name) {
//            dis_id = cn1.getImei();
//        }

        String distri_id = "";
        List<Local_Data> distru = dbvoc.getDistributors_BYID(dis_id);
        for (Local_Data cnn : distru) {
            if (!cnn.getStateName().equalsIgnoreCase("") && !cnn.getStateName().equalsIgnoreCase(" ")) {
                String str_dist = "" + cnn.getStateName();
                dis_name.setText(str_dist);
                distri_id = cnn.getCode();
            }
        }

        dbvoc.updateORDER_DISTRIBUTER(dis_id, Global_Data.GLOvel_GORDER_ID);
        // }

        btnCash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCash.setBackgroundResource(R.drawable.card_border_black);
                btnNeft.setBackgroundResource(android.R.color.transparent);
                btnCheque.setBackgroundResource(android.R.color.transparent);
                //order_detail2.setVisibility(View.GONE);
                capimg="Cash";
                order_de.setVisibility(View.GONE);
            }
        });

        btnCheque.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCash.setBackgroundResource(android.R.color.transparent);
                btnNeft.setBackgroundResource(android.R.color.transparent);
                btnCheque.setBackgroundResource(R.drawable.card_border_black);
                //  order_detail2.setVisibility(View.VISIBLE);
                order_de.setVisibility(View.VISIBLE);
                capimg="Cheque";
                order_detail2.setHint("PDC No");
            }
        });

        btnNeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCash.setBackgroundResource(android.R.color.transparent);
                btnNeft.setBackgroundResource(R.drawable.card_border_black);
                btnCheque.setBackgroundResource(android.R.color.transparent);
                //   order_detail2.setVisibility(View.VISIBLE);
                capimg="NEFT";
                order_de.setVisibility(View.VISIBLE);
                order_detail2.setHint("Reference No");
            }
        });

        linearLayout_swipe.setOnTouchListener(new OnSwipeTouchListener(CaptureSignature.this) {
            public void onSwipeRight() {
                switch (choice) {
                    case 2:
                        swipe.setImageResource(R.drawable.timeliner2);
                        signatureCardFirst.setVisibility(View.GONE);
                        signatureCardSecond.setVisibility(View.VISIBLE);
                        signatureCardThird.setVisibility(View.GONE);
                        choice--;
                        break;
                    case 1:
                        swipe.setImageResource(R.drawable.timeliner);
                        signatureCardFirst.setVisibility(View.VISIBLE);
                        signatureCardSecond.setVisibility(View.GONE);
                        signatureCardThird.setVisibility(View.GONE);
                        choice--;
                        break;
                    default:
                        //Toast.makeText(CaptureSignature.this, "3", Toast.LENGTH_SHORT).show();
                        //choice=1;
                        break;
                }
            }

            public void onSwipeLeft() {
                switch (choice) {
                    case 0:
                        swipe.setImageResource(R.drawable.timeliner2);
                        signatureCardFirst.setVisibility(View.GONE);
                        signatureCardSecond.setVisibility(View.VISIBLE);
                        signatureCardThird.setVisibility(View.GONE);
                        choice++;
                        break;
                    case 1:
                        swipe.setImageResource(R.drawable.timeliner3);
                        signatureCardFirst.setVisibility(View.GONE);
                        signatureCardSecond.setVisibility(View.GONE);
                        signatureCardThird.setVisibility(View.VISIBLE);
                        choice++;
                        break;
                    default:
                        //Toast.makeText(CaptureSignature.this, "3", Toast.LENGTH_SHORT).show();
                        //choice=1;
                        break;
                }
                // swipe.setImageResource(R.drawable.timeliner2);
            }
        });

        // for label change
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        detail1str = spf1.getString("var_detail1", "");

//        if (detail1str.length() > 0) {
//            order_detail1.setHint(detail1str);
//        } else {
//            order_detail1.setHint(getResources().getString(R.string.Detail1));
//        }

        InputFilter[] Textfilters = new InputFilter[1];
        Textfilters[0] = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {

                    char[] acceptedChars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.'};

                    for (int index = start; index < end; index++) {
                        if (!new String(acceptedChars).contains(String.valueOf(source.charAt(index)))) {
                            return "";
                        }
                    }
                }
                return null;
            }


        };

//        myCalendar = Calendar.getInstance();
//
//        date = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//           //    date.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//
//                updateLabel();
//            }
//
//
//        };
//
//        date1 = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel1();
//            }
//
//        };

        // for label change
        SharedPreferences spf2 = this.getSharedPreferences("SimpleLogic", 0);
        detail2str = spf2.getString("var_detail2", "");

        SharedPreferences spf3 = this.getSharedPreferences("SimpleLogic", 0);
        String strdetail1_edit = spf3.getString("var_detail1_edit", "");

        SharedPreferences spf4 = this.getSharedPreferences("SimpleLogic", 0);
        strdetail1_mandate = spf4.getString("var_detail1_mandate", "");

        SharedPreferences spf5 = this.getSharedPreferences("SimpleLogic", 0);
        String strdetail1_allow = spf5.getString("var_detail1_allow", "");

        SharedPreferences spf23 = this.getSharedPreferences("SimpleLogic", 0);
        String strdetail2_edit = spf23.getString("var_detail2_edit", "");

        SharedPreferences spf24 = this.getSharedPreferences("SimpleLogic", 0);
        strdetail2_mandate = spf24.getString("var_detail2_mandate", "");

        SharedPreferences spf25 = this.getSharedPreferences("SimpleLogic", 0);
        String strdetail2_allow = spf25.getString("var_detail2_allow", "");

        if (strdetail1_edit.equalsIgnoreCase("true")) {
            order_detail1.setEnabled(true);

            if (strdetail1_allow.equalsIgnoreCase("Text")) {
                order_detail1.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (strdetail1_allow.equalsIgnoreCase("Integer")) {
                order_detail1.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                order_detail1.setFocusableInTouchMode(false);
                order_detail1.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Calendar c = Calendar.getInstance();
                        DatePickerDialog dialog = new DatePickerDialog(CaptureSignature.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String _year = String.valueOf(year);
                                String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                                String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                                //     String _pickedDate = year + "-" + _month + "-" + _date;
                                String _pickedDate = _date + "/" + _month + "/" + year;
                                Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12

                                order_detail1.setText(_pickedDate);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        dialog.show();

                    }
                });

                //order_detail1.setInputType(InputType.TYPE_CLASS_DATETIME);
            }
        }
        if (strdetail2_edit.equalsIgnoreCase("true")) {
            order_detail2.setEnabled(true);

            if (strdetail2_allow.equalsIgnoreCase("Text")) {
                order_detail2.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (strdetail2_allow.equalsIgnoreCase("Integer")) {
                order_detail2.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                order_detail2.setFocusableInTouchMode(false);
                order_detail2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Calendar c = Calendar.getInstance();
                        DatePickerDialog dialog = new DatePickerDialog(CaptureSignature.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String _year = String.valueOf(year);
                                String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                                String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                                //     String _pickedDate = year + "-" + _month + "-" + _date;
                                String _pickedDate = _date + "/" + _month + "/" + year;
                                Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12

                                order_detail2.setText(_pickedDate);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        dialog.show();
                    }
                });
            }
        }

        if (detail2str.length() > 0) {
            order_detail2.setHint(detail2str);
        } else {
            order_detail2.setHint(getResources().getString(R.string.Detail2));
        }

        SharedPreferences sp1 = CaptureSignature.this
                .getSharedPreferences("SimpleLogic", 0);

        results_order_type.clear();
        List<Local_Data> contacts1 = dbvoc.getorder_category();
        results_order_type.add(getResources().getString(R.string.Select_Order_Type));
        for (Local_Data cn : contacts1) {
            if (!cn.getOrder_type_name().equalsIgnoreCase("") && !cn.getOrder_type_name().equalsIgnoreCase(" ")) {
                String str_categ = "" + cn.getOrder_type_name();
                results_order_type.add(str_categ);
            }
        }

        dataAdapter_order_type = new ArrayAdapter<String>(this, R.layout.spinner_item, results_order_type);
        dataAdapter_order_type.setDropDownViewResource(R.layout.spinner_item);
        order_type.setAdapter(dataAdapter_order_type);

        this.arraySpinner = new String[]{
                getResources().getString(R.string.Instrument_Type), getResources().getString(R.string.Cash), getResources().getString(R.string.Cheque), getResources().getString(R.string.Other)
        };

        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arraySpinner);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        s_payment_mode.setAdapter(adapter);

        String cc_code = "";

        List<Local_Data> contacts = dbvoc.GetOrders_details("Secondary Sales / Retail Sales", Global_Data.GLOvel_GORDER_ID);

        if (contacts.size() > 0) {
            for (Local_Data cn : contacts) {
                yourName.setText(cn.getOrder_detail3());
                order_detail1.setText(cn.getOrder_detail1());
                order_detail2.setText(cn.getOrder_detail2());
                cc_code = cn.getOrder_category_type().trim();
            }
        }

        //  yourName.setText(Global_Data.customernaaaame);

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cc_code)) {
            List<Local_Data> cont = dbvoc.get_order_category_name(cc_code);

            for (Local_Data cn : cont) {
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getOrder_type_name().trim())) {
                    int spinnerPosition = dataAdapter_order_type.getPosition(cn.getOrder_type_name().trim());
                    order_type.setSelection(spinnerPosition);
                }
            }
        }

        get_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                requestStoragePermission();
            }
        });

        cam_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermissionnft();
            }
        });

        mClear.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    signature_pad.clear();
                }
                return false;
            }
        });

        mGetSign.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.v("log_tag", "Panel Saved");
                    boolean error = captureSignature();
                    if (!error) {
                        if (capimg.equalsIgnoreCase("")) {
                            Global_Data.Custom_Toast(CaptureSignature.this, "Please Select Payment Method", "yes");
                        } else if (capimg.equalsIgnoreCase("NEFT")) {
                            if (order_detail2.getText().toString().equalsIgnoreCase("")) {
                                hintsa = "Reference No";
                                Global_Data.Custom_Toast(CaptureSignature.this, "Please Enter " + hintsa, "yes");
                            }else {
                                requestStoragePermissionsave();
                            }

                        } else if (capimg.equalsIgnoreCase("Cheque")) {
                            if (order_detail2.getText().toString().equalsIgnoreCase("")) {
                                hintsa = "PDC No";
                                Global_Data.Custom_Toast(CaptureSignature.this, "Please Enter " + hintsa, "yes");
                            }
                            else {
                                requestStoragePermissionsave();
                            }
                        } else {


                            requestStoragePermissionsave();
                        }

                    }
                }
                return false;
            }
        });


        mCancel.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    Log.v("log_tag", "Panel Canceled");
                    Bundle b1 = new Bundle();
                    b1.putString("status", "cancel");
                    Intent intent = new Intent();
                    intent.putExtras(b1);
                    setResult(RESULT_OK, intent);
                    finish();

                }
                return false;
            }
        });


    }

    private void requestStoragePermissionnft() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            B_flag = isDeviceSupportCamera();

                            if (B_flag == true) {
//                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                   // fileUri = getOutputMediaFileUrinew(MEDIA_TYPE_IMAGE);
//                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    //startActivityForResult(intent1, MEDIA_TYPE_IMAGE);
//                    startActivityForResult(intent1, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                    }

//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                    }

                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                    // Create the File where the photo should go
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFilenft();
                                    } catch (Exception ex) {
                                        // Error occurred while creating the File
                                        Log.i("Image TAG", "IOException");
                                        mCurrentPhotoPathnft = "";
                                    }
                                    // Continue only if the File was successfully created
                                    if (photoFile != null) {
                                        Uri photoURI = FileProvider.getUriForFile(CaptureSignature.this,
                                                BuildConfig.APPLICATION_ID + ".provider",
                                                photoFile);
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        //  cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                        startActivityForResult(cameraIntent, 6);
                                    }
                                }

                            } else {
                                Global_Data.Custom_Toast(getApplicationContext(), "no camera on this device", "");
                                //  Toast.makeText(getApplicationContext(), "no camera on this device", Toast.LENGTH_LONG).show();
                                //
                            }


                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                        // Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    protected void onDestroy() {
        Log.w("GetSignature", "onDestory");
        super.onDestroy();
    }

    private boolean captureSignature() {
        boolean error = false;
        String errorMessage = "";

        if (yourName.getText().toString().equalsIgnoreCase("")) {
            errorMessage = errorMessage + getResources().getString(R.string.Please_Enter_customer_Name);
            error = true;
        } else if ((strdetail1_mandate.equalsIgnoreCase("true")) || (strdetail2_mandate.equalsIgnoreCase("true"))) {

            if (strdetail1_mandate.equalsIgnoreCase("true")) {
                if (order_detail1.getText().toString().equalsIgnoreCase("")) {
                    errorMessage = errorMessage + getResources().getString(R.string.Please_Enter) + detail1str;
                    error = true;
                }
            }

            if (strdetail2_mandate.equalsIgnoreCase("true")) {
                if (order_detail2.getText().toString().equalsIgnoreCase("")) {
                    errorMessage = errorMessage + getResources().getString(R.string.Please_Enter) + detail2str;
                    error = true;
                }
            }
        }

//        }else if((strdetail1_mandate.equalsIgnoreCase("true")) || (strdetail2_mandate.equalsIgnoreCase("true"))){
//
//            if(strdetail1_mandate.equalsIgnoreCase("true")) {
//                if (order_detail1.getText().toString().equalsIgnoreCase("")) {
//                    errorMessage = errorMessage + "Please Enter " + detail1str;
//                    error = true;
//                }
//            }
//
//            if(strdetail2_mandate.equalsIgnoreCase("true"))
//            {
//                if(order_detail2.getText().toString().equalsIgnoreCase("")){
//                    errorMessage = errorMessage + "Please Enter "+detail2str;
//                    error = true;
//                }
//            }
//        }

        if (error) {
            Global_Data.Custom_Toast(this, errorMessage, "Yes");

//            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        }

        return error;
    }


    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                Global_Data.CatlogueStatus = "";
                return true;
        }
//        return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add:
                String targetNew = "";
                SharedPreferences sp = CaptureSignature.this.getSharedPreferences("SimpleLogic", 0);
                try {
                    int target = (int) Math.round(sp.getFloat("Target", 0));
                    int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                    Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
                    if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                        int age = (int) Math.round(age_float);
                        if (Global_Data.rsstr.length() > 0) {
                            targetNew = "T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
                            //todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        } else {
                            targetNew = "T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                    } else {
                        int age = (int) Math.round(age_float);
                        if (Global_Data.rsstr.length() > 0) {
                            targetNew = "T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
                            // todaysTarget.setText();
                        } else {
                            targetNew = "T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                View yourView = findViewById(R.id.add);
                new SimpleTooltip.Builder(this)
                        .anchorView(yourView)
                        .text(targetNew)
                        .gravity(Gravity.START)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    public void get_dialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(CaptureSignature.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Confirmation));
        alertDialog.setMessage(getResources().getString(R.string.Dialog_order_offline_message));
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Save), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub


                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//								   InsertOrderAsyncTask insertOrderAsyncTask =new InsertOrderAsyncTask(CaptureSignature.this);
//								   insertOrderAsyncTask.execute();

                //  Toast.makeText(getApplicationContext(),"Order generate successfully.",Toast.LENGTH_LONG).show();

                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Order_generate_successfully), "Yes");
//                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Order_generate_successfully), Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();

                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);

            }
        });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        // String encodedImage =imageBytes.toString();

        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

//            try {
//                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            // Bundle extras = data.getExtras();
            //  Bitmap imageBitmap = (Bitmap) extras.get("data");

            try {

                dbvoc.updateORDER_order_image(mCurrentPhotoPath, Global_Data.GLObalOrder_id);
                Global_Data.Custom_Toast(CaptureSignature.this, "Image Captured and Save successfully", "yes");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //get_icon.setImageBitmap(imageBitmap);
        }
        if (requestCode == 6 && resultCode == RESULT_OK){
            if (capimg.equalsIgnoreCase("NEFT")){
                Global_Data.Custom_Toast(CaptureSignature.this, "NEFT Image Captured and Save successfully", "yes");
            }else {
                Global_Data.Custom_Toast(CaptureSignature.this, "Cheque Image Captured and Save successfully", "yes");

            }


        }
    }

    // Call this whn the user has chosen the date and set the Date in the EditText in format that you wish
    private void updateLabel() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        order_detail1.setText(sdf.format(myCalendar.getTime()));

    }

    private void updateLabel1() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        order_detail2.setText(sdf.format(myCalendar.getTime()));
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "Metal";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Metal");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createImageFilenft() throws IOException {
        // Create an image file name
        String imageFileName = "Metal";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Metal");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPathnft = "file:" + image.getAbsolutePath();
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void SaveImage(Bitmap finalBitmap, String name) {

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File myDir = new File(path, "Metal_Signature");
        myDir.mkdirs();

        String fname = name + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Signature_path = "file:" + file.getAbsolutePath();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Requesting multiple permissions (storage and location) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            B_flag = isDeviceSupportCamera();

                            if (B_flag == true) {
//                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                   // fileUri = getOutputMediaFileUrinew(MEDIA_TYPE_IMAGE);
//                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    //startActivityForResult(intent1, MEDIA_TYPE_IMAGE);
//                    startActivityForResult(intent1, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                    }

//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                    }

                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                    // Create the File where the photo should go
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (Exception ex) {
                                        // Error occurred while creating the File
                                        Log.i("Image TAG", "IOException");
                                        mCurrentPhotoPath = "";
                                    }
                                    // Continue only if the File was successfully created
                                    if (photoFile != null) {
                                        Uri photoURI = FileProvider.getUriForFile(CaptureSignature.this,
                                                BuildConfig.APPLICATION_ID + ".provider",
                                                photoFile);
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        //  cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                                    }
                                }

                            } else {
                                Global_Data.Custom_Toast(getApplicationContext(), "no camera on this device", "");
                                //  Toast.makeText(getApplicationContext(), "no camera on this device", Toast.LENGTH_LONG).show();
                            }


                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                        // Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    /**
     * Requesting multiple permissions (storage and location) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermissionsave() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (signature_pad.isEmpty()) {
                                Global_Data.Custom_Toast(CaptureSignature.this, "Please Sign", "Yes");
//                                Toast toast = Toast.makeText(CaptureSignature.this, "Please Sign", Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 105, 50);
//                                toast.show();
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(CaptureSignature.this).create(); //Read Update
                                alertDialog.setTitle(getResources().getString(R.string.Confirmation));
                                alertDialog.setMessage(getResources().getString(R.string.Continue_dialog));
                                alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub

                                        String order_detail1_text = "";
                                        String order_detail2_text = "";
                                        String order_type_text = "";
                                        String order_type_name = "";
                                        String order_type_code = "";
                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(order_detail1.getText().toString().trim())) {

                                            order_detail1_text = order_detail1.getText().toString().trim();
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(order_detail2.getText().toString().trim())) {

                                            order_detail2_text = order_detail2.getText().toString().trim();
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(yourName.getText().toString().trim())) {

                                            order_type_name = yourName.getText().toString().trim();
                                        }

                                        if (!(order_type.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Order_Type)))) {

                                            order_type_text = order_type.getSelectedItem().toString();
                                            List<Local_Data> contacts1 = dbvoc.get_order_category_code(order_type_text);

                                            for (Local_Data cn : contacts1) {

                                                order_type_code = cn.getOrder_type_code();
                                            }

                                        }

                                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                        } else
                                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                        try {
                                            AppLocationManager appLocationManager = new AppLocationManager(CaptureSignature.this);
                                            Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                                            Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                            PlayService_Location PlayServiceManager = new PlayService_Location(CaptureSignature.this);

                                            if (PlayServiceManager.checkPlayServices(CaptureSignature.this)) {
                                                Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                                            } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                                Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                                Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                            }

                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                        try {
                                            final Bitmap bitmap = signature_pad.getTransparentSignatureBitmap();

                                            SaveImage(bitmap, "MSI" + Global_Data.GLObalOrder_id);
                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LATITUDE) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LONGITUDE)) {

                                                dbvoc.updateORDER_SIGNATURENEW_WITHLATLONG(Signature_path, Global_Data.GLObalOrder_id, order_detail1_text, order_detail2_text, order_type_name, order_type_code, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE);
                                            } else {
                                                dbvoc.updateORDER_SIGNATURENEW(Signature_path, Global_Data.GLObalOrder_id, order_detail1_text, order_detail2_text, order_type_name, order_type_code);
                                            }
                                            //mSignature.clear();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        isInternetPresent = cd.isConnectingToInternet();

                                        if (isInternetPresent) {

                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(String.valueOf(Global_Data.GLOvel_LATITUDE)) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(String.valueOf(Global_Data.GLOvel_LONGITUDE))) {

                                                try {
                                                    LocationAddress locationAddress = new LocationAddress();
                                                    LocationAddress.getAddressFromLocation(Double.valueOf(Global_Data.GLOvel_LATITUDE), Double.valueOf(Global_Data.GLOvel_LONGITUDE),
                                                            getApplicationContext(), new CaptureSignature.GeocoderHandler());

                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                    addressn(Double.valueOf(Global_Data.GLOvel_LATITUDE), Double.valueOf(Global_Data.GLOvel_LONGITUDE));
                                                }
                                            }


                                            Log.d("GLOVADD", "GLOVADD" + Global_Data.address);
                                            getServices.SYNCORDER_BYCustomer(CaptureSignature.this, Global_Data.GLOvel_GORDER_ID);
                                        } else {
                                            //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();

                                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.internet_connection_error), "");
//                                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                                            //toast.setGravity(Gravity.CENTER, 0, 0);
//                                            toast.show();

                                            get_dialog();
                                        }
                                    }
                                });

                                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        dialog.cancel();
                                    }
                                });

                                alertDialog.setCancelable(false);
                                alertDialog.show();

                            }

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                        //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CaptureSignature.this);
        builder.setTitle(getResources().getString(R.string.need_permission_message));
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.need_permission_setting_message));
        builder.setPositiveButton(getResources().getString(R.string.GOTO_SETTINGS), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress = "";
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    //locationAddress = " ";
            }
            //  LOCATION.setText(locationAddress);


            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(locationAddress)) {
                Global_Data.address = locationAddress;
                Log.d("GLOBEL ADDRESS G", "V" + locationAddress);

            } else {
                Global_Data.address = "";
                Log.d("GLOBEL ADDRESS G", "address not found.");
            }


        }
    }

    public String addressn(Double lat, Double longi) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        StringBuilder sb = new StringBuilder();
        sb.append("");

        try {
            addresses = geocoder.getFromLocation(lat, longi, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getAddressLine(0)) + " ");
            if (!(sb.indexOf(addresses.get(0).getLocality()) > 0)) {
                sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getLocality()) + " ");
            }

            if (!(sb.indexOf(addresses.get(0).getAdminArea()) > 0)) {
                sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getAdminArea()) + " ");
            }

            if (!(sb.indexOf(addresses.get(0).getCountryName()) > 0)) {
                sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getCountryName()) + " ");
            }

            if (!(sb.indexOf(addresses.get(0).getPostalCode()) > 0)) {
                sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getPostalCode()) + " ");
            }
            //String knownName = addresses.get(0).getFeatureName();

            Global_Data.address = sb.toString();
            //sb.append(knownName).append(" ");
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


}