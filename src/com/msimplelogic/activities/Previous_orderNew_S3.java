package com.msimplelogic.activities;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.Settings;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
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
import java.util.List;
import java.util.Locale;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

//import com.simplelogic.database.DatabaseHandler;
//import com.simplelogic.webservice.GmailSender;

public class Previous_orderNew_S3 extends BaseActivity {
    //DataBaseHelper dbvoc;
    private String Signature_path = "";
    private String mCurrentPhotoPath = "";
    private String mCurrentPhotoPathnft = "";
    int choice=0;
    EditText dis_name;
    String capimg="";
    String hintsa="";
    Boolean isInternetPresent = false;
    TextView details1, details2;
    ConnectionDetector cd;
    LinearLayout mContent;
    CardView signatureCardFirst, signatureCardSecond;
    RelativeLayout signatureCardThird;
    signature mSignature;
    SignaturePad signature_pad;
    byte b5[];
    Calendar myCalendar;
    ImageView cam_btn;
    String strdetail1_mandate, strdetail2_mandate;
    Button  mGetSign, mCancel;
    TextView mClear,btnCash,btnCheque,btnNeft;
    int m_sign_flag = 0;
    ArrayList<Product> dataOrder = new ArrayList<Product>();
    public static String tempDir;
    LinearLayout order_de;
    public int count = 1;
    public String current = null;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    private Bitmap mBitmap;
    View mView;
    int userID, cityID, beatID, retailerID, distID;
    ImageView get_icon, swipe;
    Boolean B_flag;
    String detail1str, detail2str;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText yourName, order_detail1, order_detail2;
    Spinner order_type;
    DatePickerDialog.OnDateSetListener date, date1;
    ArrayAdapter<String> dataAdapter_order_type;
    List<String> list_order_type;
    private ArrayList<String> results_order_type = new ArrayList<String>();
    float totalPrice;
    public String order = "", retailer_mobile = "", retailer_emailID = "", dist_mobile = "", dist_emailID = "", retailer_code = "", ret_Name = "";
    Toolbar toolbar;
    CoordinatorLayout linearLayout_swipe;
    SharedPreferences sp;
    ImageView hedder_theame,nextimg2,nextimg1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_previous_order_s3);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // setTitle(getResources().getString(R.string.Previous_Order));
        cd = new ConnectionDetector(getApplicationContext());
        SharedPreferences sp = this.getSharedPreferences("SimpleLogic", 0);
        order = sp.getString("order", "");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        swipe = findViewById(R.id.swipe);
        signatureCardFirst = findViewById(R.id.signature_card1);
        hedder_theame = findViewById(R.id.hedder_theame);
        signatureCardSecond = findViewById(R.id.signature_card2);
        signatureCardThird = findViewById(R.id.signature_card3);
        btnCash =  findViewById(R.id.btn_cash);
        btnCheque =  findViewById(R.id.btn_cheque);
        btnNeft =  findViewById(R.id.btn_neft);
        dis_name = findViewById(R.id.username);
        nextimg1 = findViewById(R.id.nextimg1);
        nextimg2 = findViewById(R.id.nextimg2);
       // mContent = (LinearLayout) findViewById(R.id.linearLayout);
       // mSignature = new signature(this, null);
      //  mSignature.setBackgroundColor(Color.WHITE);
       // mContent.addView(mSignature, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        mClear = (TextView) findViewById(R.id.clear);
        signature_pad = findViewById(R.id.signature_pad);
       // mClear.setBackgroundColor(Color.parseColor("#414042"));
        mGetSign = (Button) findViewById(R.id.getsign);
       // mGetSign.setBackgroundColor(Color.parseColor("#414042"));
        cam_btn = findViewById(R.id.cam_btn);
        // mGetSign.setEnabled(false);
        mCancel = (Button) findViewById(R.id.cancel);
        linearLayout_swipe =  findViewById(R.id.linearLayout1);
      // mCancel.setBackgroundColor(Color.parseColor("#414042"));
      //  mView = mContent;

        yourName = (EditText) findViewById(R.id.yourName);
        order_detail1 = (EditText) findViewById(R.id.order_detail1);
        order_detail2 = (EditText) findViewById(R.id.order_detail2);
        order_de = findViewById(R.id.order_de);
        order_type = (Spinner) findViewById(R.id.order_type);
        get_icon = (ImageView) findViewById(R.id.get_icon);
        // details1 = (TextView) findViewById(R.id.details1);
        // details2 = (TextView) findViewById(R.id.details2);

//        String distri_id = "";
//        List<Local_Data> distru = dbvoc.getDistributors();
//        for (Local_Data cnn : distru) {
//            if (!cnn.getCUSTOMER_SHOPNAME().equalsIgnoreCase("") && !cnn.getCUSTOMER_SHOPNAME().equalsIgnoreCase(" ")) {
//                String str_dist = "" + cnn.getCUSTOMER_SHOPNAME();
//                dis_name.setText(str_dist);
//                distri_id = cnn.getCode();
//
//            }
//        }
        String dis_id = "";
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

        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);
            nextimg2.setImageResource(R.drawable.viewordermenu_dark);
            nextimg1.setImageResource(R.drawable.viewordermenu_dark);
        }

        btnCash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCash.setBackgroundResource(R.drawable.card_border_black);
                btnNeft.setBackgroundResource(android.R.color.transparent);
                btnCheque.setBackgroundResource(android.R.color.transparent);
                order_de.setVisibility(View.GONE);
                capimg="Cash";
            }
        });

        btnCheque.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCash.setBackgroundResource(android.R.color.transparent);
                btnNeft.setBackgroundResource(android.R.color.transparent);
                btnCheque.setBackgroundResource(R.drawable.card_border_black);
                order_de.setVisibility(View.VISIBLE);
                capimg="Cheque";
                order_detail2.setHint("PDC No");
                order_detail2.setText("");
            }
        });

        btnNeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCash.setBackgroundResource(android.R.color.transparent);
                btnNeft.setBackgroundResource(R.drawable.card_border_black);
                btnCheque.setBackgroundResource(android.R.color.transparent);
                order_de.setVisibility(View.VISIBLE);
                capimg="NEFT";
                order_detail2.setHint("Reference No");
                order_detail2.setText("");
            }
        });


        linearLayout_swipe.setOnTouchListener(new OnSwipeTouchListener(Previous_orderNew_S3.this) {
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


        Intent i = getIntent();
        dataOrder = i.getParcelableArrayListExtra("productsList");

        SharedPreferences sp1 = Previous_orderNew_S3.this
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

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cc_code)) {

            List<Local_Data> cont = dbvoc.get_order_category_name(cc_code);

            for (Local_Data cn : cont) {
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getOrder_type_name().trim())) {
                    int spinnerPosition = dataAdapter_order_type.getPosition(cn.getOrder_type_name().trim());
                    order_type.setSelection(spinnerPosition);
                }
            }
        }

        // for label change
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        detail1str = spf1.getString("var_detail1", "");

//        if (detail1str.length() > 0) {
//            order_detail1.setHint(detail1str);
//        } else {
//            order_detail1.setHint(getResources().getString(R.string.Detail1));
//        }


//        myCalendar = Calendar.getInstance();
//
//        date = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel();
//            }
//        };

//        date1 = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel1();
//            }
//        };
//
//        // for label change
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
                        DatePickerDialog dialog = new DatePickerDialog(Previous_orderNew_S3.this, new DatePickerDialog.OnDateSetListener() {
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
                        new DatePickerDialog(Previous_orderNew_S3.this, date1, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
            }
        }

        if (detail2str.length() > 0) {
            order_detail2.setHint(detail2str);
        } else {
            order_detail2.setHint(getResources().getString(R.string.Detail2));
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

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //down event
                   // b.setBackgroundColor(Color.parseColor("#910505"));
                    Log.v("log_tag", "Panel Cleared");
                   // mSignature.clear();
                    signature_pad.clear();
                    // mGetSign.setEnabled(false);
                    m_sign_flag = 0;
                }
                return false;
            }
        });

        mGetSign.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    //b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //down event
                    //b.setBackgroundColor(Color.parseColor("#910505"));

                    Log.v("log_tag", "Panel Saved");
                    boolean error = captureSignature();
                    if (!error) {
                        if (capimg.equalsIgnoreCase("")) {
                            Global_Data.Custom_Toast(Previous_orderNew_S3.this, "Please Select Payment Method", "yes");
                        } else if (capimg.equalsIgnoreCase("NEFT")) {
                            if (order_detail2.getText().toString().equalsIgnoreCase("")) {
                                hintsa = "Reference No";
                                Global_Data.Custom_Toast(Previous_orderNew_S3.this, "Please Enter " + hintsa, "yes");
                            }else {
                                requestStoragePermissionsave();
                            }

                        } else if (capimg.equalsIgnoreCase("Cheque")) {
                            if (order_detail2.getText().toString().equalsIgnoreCase("")) {
                                hintsa = "PDC No";
                                Global_Data.Custom_Toast(Previous_orderNew_S3.this, "Please Enter " + hintsa, "yes");
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
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //down event
                    b.setBackgroundColor(Color.parseColor("#910505"));

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
        try {

            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText(Global_Data.order_retailer);
            //mTitleTextView.setText(Global_Data.order_retailer + " " + "(" + Global_Data.AmountOutstanding + "/" + Global_Data.AmountOverdue + ")");

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);

            try {
                int target = (int) Math.round(sp.getFloat("Target", 0));
                int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
                if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                    int age = (int) Math.round(age_float);

                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                    }
                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                } else {
                    int age = (int) Math.round(age_float);

                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    }
                    // todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
//        	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
                todaysTarget.setText("Today's Target Acheived");
            }

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


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
                                        Uri photoURI = FileProvider.getUriForFile(Previous_orderNew_S3.this,
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
            errorMessage = errorMessage + getResources().getString(R.string.Please_Enter_your_Name);
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

        if (error) {
//            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
            Global_Data.Custom_Toast(this, errorMessage,"yes");
        }

        return error;
    }


    private boolean makedirs() {
        File tempdir = new File(tempDir);
        if (!tempdir.exists())
            tempdir.mkdirs();

        if (tempdir.isDirectory()) {/*
            File[] files = tempdir.listFiles();
            for (File file : files)
            {
                if (!file.delete())
                {
                    System.out.println("Failed to delete " + file);
                }
            }
        */
        }
        return (tempdir.isDirectory());
    }

    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v, String mypath) {
            Log.e("log_tag", "Width: " + v.getWidth());
            Log.e("log_tag", "Height: " + v.getHeight());
            if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
                ;
            }
            Canvas canvas = new Canvas(mBitmap);
            try {
                FileOutputStream mFileOutStream = new FileOutputStream(mypath);

                v.draw(canvas);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();
                String url = Images.Media.insertImage(getContentResolver(), mBitmap, "title", null);
                Log.e("log_tag", "mypath: " + mypath);

                Log.e("log_tag", "url: " + url);
                //In case you want to delete the file
                //boolean deleted = mypath.delete();
                //Log.v("log_tag","deleted: " + mypath.toString() + deleted);
                //If you want to convert the image to string use base64 converter

            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            m_sign_flag = 1;
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
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
                return true;
        }
//		return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add:
                String targetNew="";
                SharedPreferences sp = Previous_orderNew_S3.this.getSharedPreferences("SimpleLogic", 0);
                try {
                    int target = (int) Math.round(sp.getFloat("Target", 0));
                    int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                    Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
                    if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                        int age = (int) Math.round(age_float);
                        if (Global_Data.rsstr.length() > 0) {
                            targetNew="T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
                            //todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        } else {
                            targetNew="T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                    } else {
                        int age = (int) Math.round(age_float);
                        if (Global_Data.rsstr.length() > 0) {
                            targetNew="T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
                            // todaysTarget.setText();
                        } else {
                            targetNew="T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
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
        Intent order_home = new Intent(getApplicationContext(), Previous_orderNew_S2.class);
        startActivity(order_home);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    public void get_dialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(Previous_orderNew_S3.this).create(); //Read Update
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

                //Toast.makeText(getApplicationContext(),"Order generate successfully.",Toast.LENGTH_LONG).show();

//                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Order_generate_successfully), Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Order_generate_successfully),"yes");

                Intent intent = new Intent(getApplicationContext(),
                        Neworderoptions.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//       if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                previewCapturedImage();
//
//            } else if (resultCode == RESULT_CANCELED) {
//                // user cancelled Image capture
//                Toast.makeText(getApplicationContext(),
//                        "User cancelled image capture", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                // failed to capture image
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }

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

            } catch (Exception e) {
                e.printStackTrace();
            }

            //get_icon.setImageBitmap(imageBitmap);
        }

        if (requestCode == 6 && resultCode == RESULT_OK) {
            if (capimg.equalsIgnoreCase("NEFT")) {
                Global_Data.Custom_Toast(Previous_orderNew_S3.this, "NEFT Image Captured and Save successfully", "yes");
            } else {
                Global_Data.Custom_Toast(Previous_orderNew_S3.this, "Cheque Image Captured and Save successfully", "yes");

            }


        }

    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        // String encodedImage =imageBytes.toString();

        return encodedImage;
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
    private void requestStoragePermissionsave() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

//                            mView.setDrawingCacheEnabled(true);
//
//                            LinearLayout content = (LinearLayout) findViewById(R.id.linearLayout);
//                            content.setDrawingCacheEnabled(true);
//                            final Bitmap bitmap = content.getDrawingCache();

                            //finish();

                            // TODO Auto-generated method stub
                            //v.setBackgroundColor(Color.parseColor("#910505"));
//                            if (m_sign_flag == 0) {
//                                Toast toast = Toast.makeText(Previous_orderNew_S3.this, getResources().getString(R.string.Please_Sign), Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 105, 50);
//                                toast.show();
                            if (signature_pad.isEmpty()) {
                                Global_Data.Custom_Toast(Previous_orderNew_S3.this, "Please Sign","Yes");

                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(Previous_orderNew_S3.this).create(); //Read Update
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
//								   InsertOrderAsyncTask insertOrderAsyncTask =new InsertOrderAsyncTask(CaptureSignature.this);
//								   insertOrderAsyncTask.execute();


                                        try {
                                            AppLocationManager appLocationManager = new AppLocationManager(Previous_orderNew_S3.this);
                                            Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                                            Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                            PlayService_Location PlayServiceManager = new PlayService_Location(Previous_orderNew_S3.this);

                                            if (PlayServiceManager.checkPlayServices(Previous_orderNew_S3.this)) {
                                                Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                                            } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                                Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                                Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                            }

                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }


                                        try {
                                            final Bitmap bitmap  = signature_pad.getTransparentSignatureBitmap();

//                                        FileOutputStream out = new FileOutputStream(myImage);
//                                        bitmap.compress(Bitmap.CompressFormat.PNG, 10, out);
//                                        out.flush();
//                                        out.close();
//                                        uploadImage =  getStringImage(bitmap);
                                            SaveImage(bitmap, "MSI" + Global_Data.GLObalOrder_id);
                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LATITUDE) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LONGITUDE)) {

                                                dbvoc.updateORDER_SIGNATURENEW_WITHLATLONG(Signature_path, Global_Data.GLObalOrder_id, order_detail1_text, order_detail2_text, order_type_name, order_type_code, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE);
                                            } else {
                                                dbvoc.updateORDER_SIGNATURENEW(Signature_path, Global_Data.GLObalOrder_id, order_detail1_text, order_detail2_text, order_type_name, order_type_code);
                                            }
                                          //  mSignature.clear();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        isInternetPresent = cd.isConnectingToInternet();

                                        if (isInternetPresent) {
                                            getServices.SYNCORDER_BYCustomer(Previous_orderNew_S3.this, Global_Data.GLOvel_GORDER_ID);
                                        } else {
                                            //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();

//                                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                                            //toast.setGravity(Gravity.CENTER, 0, 0);
//                                            toast.show();
                                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.internet_connection_error),"yes");

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
                      //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(),"yes");
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
                                        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                        Uri photoURI = FileProvider.getUriForFile(Previous_orderNew_S3.this,
                                                BuildConfig.APPLICATION_ID + ".provider",
                                                photoFile);
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                                    }
                                }

                            } else {
                             //  Toast.makeText(getApplicationContext(), "no camera on this device", Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), "no camera on this device", "Yes");
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
                      //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(),"yres");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Previous_orderNew_S3.this);
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
}