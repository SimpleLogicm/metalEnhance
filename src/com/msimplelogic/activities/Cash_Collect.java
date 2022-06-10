package com.msimplelogic.activities;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.adapter.Collection_images_adapter;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

/**
 * Created by vv on 9/6/2017.
 */

public class Cash_Collect extends BaseActivity {
    private File output = null;
    HashMap<String, String> rhm = new HashMap<String, String>();
    String val = "" + ((int) (Math.random() * 9000) + 1000);

    String amount_out, amount_overd, customer_code;
    int serverResponseCode;
    File file;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    ProgressDialog dialog;
    String encr_iamgecode = "";
    LoginDataBaseAdapter loginDataBaseAdapter;
    byte b5[];
    String type, reason;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 200;
    Spinner instrument_type, reason_list;
    private String[] arraySpinner, arraySpinner1;
    LinearLayout mContent;
    signature mSignature;
    Button mClear, mGetSign, mCancel;
    int m_sign_flag = 0;
    private Bitmap mBitmap;
    View mView;
    private ArrayList<String> reason_name = new ArrayList<String>();
    EditText collect_amount, cust_name, collect_remark, details1, details2, details3, details4, details5;
    ImageView cam_btn;
    Boolean B_flag;
    private String pictureImagePath = "";
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    RecyclerView recyclerView;
    ArrayList<String> Number = new ArrayList<>();
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    Collection_images_adapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout;
    View ChildView;
    int RecyclerViewItemPosition;
    private SignaturePad signature_pad;
    private  Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//        );

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //   this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_cashcollect);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getResources().getString(R.string.Cash_Deposit));
        instrument_type = (Spinner) findViewById(R.id.instrument_type);
        reason_list = (Spinner) findViewById(R.id.reason_list);
        collect_amount = (EditText) findViewById(R.id.collect_amount);
        cust_name = (EditText) findViewById(R.id.cust_name);
        collect_remark = (EditText) findViewById(R.id.collect_remark);
        details1 = (EditText) findViewById(R.id.details1);
        details2 = (EditText) findViewById(R.id.details2);
        details3 = (EditText) findViewById(R.id.details3);
        details4 = (EditText) findViewById(R.id.details4);
        details5 = (EditText) findViewById(R.id.details5);
        cam_btn = (ImageView) findViewById(R.id.cam_btn);

        signature_pad=findViewById(R.id.signature_pad);
//        mContent = (LinearLayout) findViewById(R.id.linearLayout);
//        mSignature = new signature(this, null);
//        mSignature.setBackgroundColor(Color.WHITE);
//        mContent.addView(mSignature, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        mClear = (Button) findViewById(R.id.collect_clear);
        //   mClear.setBackgroundColor(Color.parseColor("#414042"));
        mGetSign = (Button) findViewById(R.id.collect_submit);
        //  mGetSign.setBackgroundColor(Color.parseColor("#414042"));
        // mGetSign.setEnabled(false);
        //     mView = mContent;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String c_name = extras.getString("CUS_NAME", null);
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(c_name)) {
                cust_name.setText(c_name);
            } else {
                cust_name.setText("");
            }

            String amount_outn = extras.getString("OUT_STANDING", null);
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(c_name)) {
                amount_out = amount_outn;
            } else {
                amount_out = "0";
            }

            String amount_overdn = extras.getString("OVER_DUE", null);
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(c_name)) {
                amount_overd = amount_overdn;
            } else {
                amount_overd = "0";
            }

            String customer_coden = extras.getString("CUS_CODE", null);
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(c_name)) {
                customer_code = customer_coden;
            } else {
                customer_code = "";
            }
        }

        // create a instance of SQLite Database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        cd = new ConnectionDetector(getApplicationContext());


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);

        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(RecyclerViewLayoutManager);

        // Adding items to RecyclerView.
        // AddItemsToRecyclerViewArrayList();
        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            //    mTitleTextView.setText("Cash Collect");

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = this.getSharedPreferences("SimpleLogic", 0);

            // create a instance of SQLite Database
            loginDataBaseAdapter = new LoginDataBaseAdapter(this);
            loginDataBaseAdapter = loginDataBaseAdapter.open();


//        loginDataBaseAdapter.insertReason("1", "wwe", "Reason Name1", "r", "df");
//        loginDataBaseAdapter.insertReason("2", "f", "Reason Name2", "we", "tt");
//        loginDataBaseAdapter.insertReason("3", "rt", "Reason Name3 ", "e", "ff");


//        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//        	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//		}

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
                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
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

        this.arraySpinner = new String[]{
                "Instrument Type", "Cash", "Cheque", "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arraySpinner);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        instrument_type.setAdapter(adapter);

//        this.arraySpinner1 = new String[]{
//                "Reason List", "Reason1", "Reason2", "Other"
//        };


        //for reason
        rhm.clear();
        List<Local_Data> contacts1 = dbvoc.getAllReasons();
        reason_name.add("Select Reason");
        for (Local_Data cn : contacts1) {
            String str_state = "" + cn.getreason_name();
            reason_name.add(str_state);
            rhm.put(cn.getreason_name(), cn.getreason_code());
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, reason_name);
        adapter1.setDropDownViewResource(R.layout.spinner_item);
        reason_list.setAdapter(adapter1);

//        instrument_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            /**
//             * Called when a new item is selected (in the Spinner)
//             */
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                // An spinnerItem was selected. You can retrieve the selected item using
//                // parent.getItemAtPosition(pos)
//
//               type = parent.getItemAtPosition(pos).toString();
//               Toast.makeText(Cash_Collect.this, "typelist:"+type, Toast.LENGTH_SHORT).show();
//
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//
//                // Do nothing, just another required interface callback
//            }
//        });

        instrument_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An spinnerItem was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                reason = parent.getItemAtPosition(pos).toString();
                //Toast.makeText(Cash_Collect.this, "list:"+reason, Toast.LENGTH_SHORT).show();
                //  String type = item.trim();
//                if (selected.equalsIgnoreCase("Cheque")) {
//                    cam_btn.setVisibility(View.VISIBLE);
//
//                } else {
//                    cam_btn.setVisibility(View.GONE);
//                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

                // Do nothing, just another required interface callback
            }
        });

        reason_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                reason = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

                // Do nothing, just another required interface callback
            }
        });

        collect_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                if (s.toString() != null && !s.toString().equals("")) {
                    //  String doubleval= s.toString();
                    Double d = Double.valueOf(s.toString());
                    //Double d = Double.parseDouble(collect_amount.getText().toString());
                    if (d <= Double.valueOf(amount_out)) {
                        String str = collect_amount.getText().toString();
                    } else {
                        collect_amount.setText("");
                        Global_Data.Custom_Toast(Cash_Collect.this, "Enter Amount should be less than or equal outstanding amount.","");
                        // Toast.makeText(Cash_Collect.this, "Enter Amount should be less than or equal outstanding amount.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

//        collect_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    Double d = Double.parseDouble(collect_amount.getText().toString());
//                    if(d<=Global_Data.amt_outstanding){
//                        // code to execute when EditText loses focus
//
//
//                        } else {
//                            Toast toast = Toast.makeText(getApplicationContext(), "Not Accepted", Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
//                        }
//
//                }
//            }
//        });

        mClear.setOnTouchListener(new View.OnTouchListener() {

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
                    Log.v("log_tag", "Panel Cleared");
                    //   mSignature.clear();
                    signature_pad.clear();
                    m_sign_flag = 0;
                    //  mGetSign.setEnabled(false);

                }
                return false;
            }
        });

        mGetSign.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {

                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    //   b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //down event
                    //   b.setBackgroundColor(Color.parseColor("#910505"));


//                    mView.setDrawingCacheEnabled(true);
//                    LinearLayout content = (LinearLayout) findViewById(R.id.linearLayout);
//                    content.setDrawingCacheEnabled(true);
//                    final Bitmap bitmap = content.getDrawingCache();
                    final Bitmap bitmap  = signature_pad.getTransparentSignatureBitmap();

//                    if (signature_pad.isEmpty()) {
//                        Global_Data.Custom_Toast(Cash_Collect.this, "Please Sign","Yes");
                    //finish();

                    // TODO Auto-generated method stub
                    //v.setBackgroundColor(Color.parseColor("#910505"));


                    if (instrument_type.getSelectedItem().toString().equalsIgnoreCase("Instrument Type")) {

                        Global_Data.Custom_Toast(Cash_Collect.this, "Please Select Instrument Type","Yes");
//                        Toast toast = Toast.makeText(Cash_Collect.this, "Please Select Instrument Type", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 50, 50);
//                        toast.show();
                    } else if (reason_list.getSelectedItem().toString().equalsIgnoreCase("Select Reason")) {
                        Global_Data.Custom_Toast(Cash_Collect.this, "Please Select Reason","Yes");
//                        Toast toast = Toast.makeText(Cash_Collect.this, "Please Select Reason", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 50, 50);
//                        toast.show();
                    } else if (collect_amount.getText().length() == 0) {
                        Global_Data.Custom_Toast(Cash_Collect.this, "Please Enter Amount", "Yes");
//                        Toast toast = Toast.makeText(Cash_Collect.this, "Please Enter Amount", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 50, 50);
//                        toast.show();
                    } else if ((details1.getText().length() == 0) && (details2.getText().length() == 0) && (details3.getText().length() == 0)
                            && (details4.getText().length() == 0) && (details5.getText().length() == 0)) {
                        Global_Data.Custom_Toast(Cash_Collect.this, "Please Enter Detail","Yes");
//                        Toast toast = Toast.makeText(Cash_Collect.this, "Please Enter Detail", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 50, 50);
//                        toast.show();
                    } else if (signature_pad.isEmpty()) {
                        // uploadVideo("");
//                        SendfeedbackJob job = new SendfeedbackJob();
//                        job.execute();
                        Global_Data.Custom_Toast(Cash_Collect.this, "Please Sign.... ","Yes");
//                        Toast toast = Toast.makeText(Cash_Collect.this, "Please Sign.... ", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 50, 50);
//                        toast.show();
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(Cash_Collect.this).create(); //Read Update
                        alertDialog.setTitle("Confirmation");
                        alertDialog.setMessage(" Are you sure you want to continue?");
                        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                } else
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                File storagePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_DIRECTORY_NAME + "/" + Global_Data.GLOvel_CUSTOMER_ID);
                                storagePath.mkdirs();

                                File myImage = new File(storagePath, Long.toString(System.currentTimeMillis()) + ".jpg");


                                String uploadImage = "";

                                try {

                                    FileOutputStream out = new FileOutputStream(myImage);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 10, out);
                                    out.flush();
                                    out.close();
                                    uploadImage = getStringImage(bitmap);
                                    //  dbvoc.updateORDER_SIGNATURENEW(uploadImage, Global_Data.GLObalOrder_id, order_detail1_text, order_detail2_text, order_type_name, order_type_code);
                                    //  mSignature.clear();
                                    signature_pad.clear();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    //delete(mediaStorageDir);
                                    if (storagePath.isDirectory()) {
                                        String[] children = storagePath.list();
                                        for (int i = 0; i < children.length; i++) {
                                            new File(storagePath, children[i]).delete();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                isInternetPresent = cd.isConnectingToInternet();

                                SharedPreferences spf = Cash_Collect.this.getSharedPreferences("SimpleLogic", 0);
                                String user_email = spf.getString("USER_EMAIL",null);

                                if (isInternetPresent) {

                                    try {
                                        LocationAddress locationAddress = new LocationAddress();
                                        LocationAddress.getAddressFromLocation(Double.valueOf(Global_Data.GLOvel_LATITUDE), Double.valueOf(Global_Data.GLOvel_LONGITUDE),
                                                Cash_Collect.this, new GeocoderHandler());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        // String address = addressn(latitude, longitude);
                                    }

                                    String address = Global_Data.address;

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String formattedDate = df.format(c.getTime());
                                    System.out.println("dv"+formattedDate);
                                    String final_outstanding = "";
                                    String final_overdue = "";
                                    String code = "";
                                    try {
                                        float fover_due = Float.valueOf(amount_overd);
                                        float famount_out = Float.valueOf(amount_out);
                                        float f_amount = Float.valueOf(collect_amount.getText().toString().trim());
                                        if (f_amount >= fover_due) {
                                            final_overdue = "0";
                                        } else if (f_amount <= fover_due) {
                                            final_overdue = String.valueOf(fover_due - f_amount);
                                        }

                                        final_outstanding = String.valueOf(famount_out - f_amount);

                                        code = firstThree(user_email) + firstThree(customer_code) + formattedDate;

                                        dbvoc.updateCREDIT_PROFILE_bycuid(final_outstanding, final_overdue, customer_code);

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        Object value = rhm.get(reason);
                                        SyncCashrecieptData(value.toString(), collect_amount.getText().toString(), details1.getText().toString(), details2.getText().toString(), details3.getText().toString(), details4.getText().toString(), details5.getText().toString(), formattedDate, customer_code, cust_name.getText().toString(), address, uploadImage, collect_remark.getText().toString(), val, final_overdue, final_outstanding, code);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();

                                        SyncCashrecieptData("", collect_amount.getText().toString(), details1.getText().toString(), details2.getText().toString(), details3.getText().toString(), details4.getText().toString(), details5.getText().toString(), formattedDate, customer_code, cust_name.getText().toString(), address, uploadImage, collect_remark.getText().toString(), val, final_overdue, final_outstanding, code);
                                    }
                                } else {


                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String formattedDate = df.format(c.getTime());
                                    System.out.println("dv" + formattedDate);
                                    //getServices.SYNCORDER_BYCustomer(Cash_Collect.this, Global_Data.GLOvel_GORDER_ID);
                                    //Toast.makeText(Cash_Collect.this, "Captured..", Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(Cash_Collect.this, Cash_Submit.class));

                                    String final_outstanding = "";
                                    String final_overdue = "";
                                    String code = "";
                                    try {
                                        float fover_due = Float.valueOf(amount_overd);
                                        float famount_out = Float.valueOf(amount_out);
                                        float f_amount = Float.valueOf(collect_amount.getText().toString().trim());
                                        if (f_amount >= fover_due) {
                                            final_overdue = "0";
                                        } else if (f_amount <= fover_due) {
                                            final_overdue = String.valueOf(fover_due - f_amount);
                                        }

                                        final_outstanding = String.valueOf(famount_out - f_amount);

                                        code = firstThree(user_email) + firstThree(customer_code) + formattedDate;

                                        dbvoc.updateCREDIT_PROFILE_bycuid(final_outstanding, final_overdue, customer_code);

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        Object value = rhm.get(reason);
                                        loginDataBaseAdapter.insertReceipt("", user_email, "", value.toString(), collect_amount.getText().toString(), details1.getText().toString(), details2.getText().toString(), details3.getText().toString(), details4.getText().toString(), details5.getText().toString(), "", "", "", "", "", "", "", "", "", "", formattedDate, customer_code, cust_name.getText().toString(),
                                                (Global_Data.GLOvel_LATITUDE + "," + Global_Data.GLOvel_LONGITUDE), "", uploadImage, collect_remark.getText().toString(), "", "",
                                                "", "", val, final_overdue, final_outstanding, code);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();

                                        loginDataBaseAdapter.insertReceipt("", user_email, "", "", collect_amount.getText().toString(), details1.getText().toString(), details2.getText().toString(), details3.getText().toString(), details4.getText().toString(), details5.getText().toString(), "", "", "", "", "", "", "", "", "", "", formattedDate, customer_code, cust_name.getText().toString(),
                                                (Global_Data.GLOvel_LATITUDE + "," + Global_Data.GLOvel_LONGITUDE), "", uploadImage, collect_remark.getText().toString(), "", "",
                                                "", "", val, final_overdue, final_outstanding, code);
                                    }

                                    Global_Data.Custom_Toast(Cash_Collect.this, "Submit Successfully","");
                                    // Toast.makeText(Cash_Collect.this, "Submit Successfully", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(Cash_Collect.this, Customer_info_main.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });

                        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        });

                        alertDialog.setCancelable(false);
                        alertDialog.show();

                    }
                    // }


                }
                return false;
            }
        });


        cam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestStoragePermission();

            }
        });

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
                String url = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "title", null);
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



    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Cash_Collect.this, Customer_info_main.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N){
//
//            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
//                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
//                b5 = bos5.toByteArray();
//
//                encr_iamgecode= Base64.encodeToString(b5,Base64.DEFAULT);
//
//                try {
//
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                //get_icon.setImageBitmap(imageBitmap);
//            }
//        }
//        else
//        {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {


            Number.add(pictureImagePath);

            RecyclerViewHorizontalAdapter = new Collection_images_adapter(Cash_Collect.this, Number);

            HorizontalLayout = new LinearLayoutManager(Cash_Collect.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(HorizontalLayout);

            recyclerView.setAdapter(RecyclerViewHorizontalAdapter);

            ++Global_Data.image_counter;

//            Intent i=new Intent(Intent.ACTION_VIEW);
//
//            i.setDataAndType(Uri.fromFile(file), "image/jpeg");
//            startActivity(i);
//            finish();


            // new Cash_Collect.CameraOperation().execute();

        }

    }

    private class CameraOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap bm400 = getScaledBitmap(myBitmap, 800, 800);
                Bitmap bm405 = rotateImage(bm400, 90);
                ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
                bm405.compress(Bitmap.CompressFormat.PNG, 100, bos5);
                b5 = bos5.toByteArray();

                encr_iamgecode = Base64.encodeToString(b5, Base64.DEFAULT);
                // dialog.dismiss();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            if (dialog != null) {
                dialog.dismiss();
                Global_Data.Custom_Toast(Cash_Collect.this, "Image Captured..","");
                //Toast.makeText(Cash_Collect.this, "Image Captured..", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {

            //dialog.dismiss();

//            if(dialog != null) {
//                dialog.dismiss();
//                dialog = null;
            dialog = new ProgressDialog(Cash_Collect.this);
            dialog.setMessage("Please Wait...");
            dialog.setTitle("Metal");
            dialog.setCancelable(false);
            dialog.show();
            // }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight) {
        Matrix m = new Matrix();

        m.setRectToRect(new RectF(0, 0, b.getWidth(), b.getHeight()), new RectF(0, 0, reqWidth, reqHeight), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        // String encodedImage =imageBytes.toString();

        return encodedImage;
    }






    private class SendfeedbackJob extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            String fileName = pictureImagePath;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;

            File sourceFile = new File(pictureImagePath);
            if (!sourceFile.isFile()) {
                Log.e("Huzza", "Source File Does not exist");
                return null;
            }

            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL("http://192.168.101.21/uploads/upload.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                //conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("myFile", fileName);
                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                Log.i("Huzza", "Initial .available : " + bytesAvailable);

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = conn.getResponseCode();

                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (serverResponseCode == 200) {
                StringBuilder sb = new StringBuilder();
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn
                            .getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                    rd.close();
                } catch (IOException ioex) {
                }
                return sb.toString();
            } else {
                return "Could not upload";
            }

        }

        @Override
        protected void onPostExecute(String message) {
            //process message
        }
    }

    public String firstThree(String str) {
        return str.length() < 3 ? str : str.substring(0, 3);
    }

    public void SyncCashrecieptData(String reason, String collect_amount, String details1, String details2, String details3, String details4, String details5, String formattedDate, String customer_code, String cust_name, String full_address, String signature, String collect_remark, String val, String final_overdue, String final_outstanding, final String code) {
        System.gc();
        String reason_code = "";
        try {

            dialog = new ProgressDialog(Cash_Collect.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Please wait....");
            dialog.setTitle("Metal");
            dialog.setCancelable(false);
            dialog.show();

            String domain = "";
            String device_id = "";


            SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";

            SharedPreferences spf = Cash_Collect.this.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL",null);

            device_id = sp.getString("devid", "");

            domain = service_url;

            JsonObjectRequest jsObjRequest = null;
            try {

                Log.d("Server url", "Server url" + domain + "cash_receipts/save_receipts");

                JSONArray COLLECTION_RECJARRAY = new JSONArray();
                //JSONObject product_value = new JSONObject();
                JSONObject COLLECTION_RECJOBJECT = new JSONObject();
                JSONArray product_imei = new JSONArray();

                JSONObject INNER_CASH_JOB = new JSONObject();
                INNER_CASH_JOB.put("code", code);
                INNER_CASH_JOB.put("reason_code", reason);
                INNER_CASH_JOB.put("amount", collect_amount);
                INNER_CASH_JOB.put("detail1", details1);
                INNER_CASH_JOB.put("detail2", details2);
                INNER_CASH_JOB.put("detail3", details3);
                INNER_CASH_JOB.put("detail4", details4);
                INNER_CASH_JOB.put("detail5", details5);
                INNER_CASH_JOB.put("received_at", formattedDate);
                INNER_CASH_JOB.put("customer_code", customer_code);
                //INNER_CASH_JOB.put("received_from_name", cust_name);
                INNER_CASH_JOB.put("latitude", Global_Data.GLOvel_LATITUDE);
                INNER_CASH_JOB.put("longitude", Global_Data.GLOvel_LONGITUDE);
                INNER_CASH_JOB.put("address", full_address);
                INNER_CASH_JOB.put("remarks", collect_remark);
                // INNER_CASH_JOB.put("random_value", val);
                INNER_CASH_JOB.put("amount_overdue", final_overdue);
                INNER_CASH_JOB.put("amount_outstanding", final_outstanding);
                INNER_CASH_JOB.put("signature", signature);
                COLLECTION_RECJARRAY.put(INNER_CASH_JOB);

                COLLECTION_RECJOBJECT.put("receipts", COLLECTION_RECJARRAY);
                COLLECTION_RECJOBJECT.put("imei_no", Global_Data.device_id);
                COLLECTION_RECJOBJECT.put("email", user_email);
                Log.d("customers Service", COLLECTION_RECJOBJECT.toString());

                jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "cash_receipts/save_receipts", COLLECTION_RECJOBJECT, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("volley", "response: " + response);

                        Log.d("jV", "JV length" + response.length());
                        try {

                            String response_result = "";
                            if (response.has("result")) {
                                response_result = response.getString("result");
                            } else {
                                response_result = "data";
                            }


                            if (response_result.equalsIgnoreCase("Receipts created successfully.")) {

                                dialog.dismiss();

                                Global_Data.Custom_Toast(Cash_Collect.this, response_result,"Yes");
//                                Toast toast = Toast.makeText(Cash_Collect.this, response_result, Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

                                dbvoc.getDeleteCelectionRe(code);

                                Intent i = new Intent(Cash_Collect.this, Customer_info_main.class);
                                startActivity(i);
                                finish();


                            } else {
                                dialog.dismiss();
                                Global_Data.Custom_Toast(Cash_Collect.this, response_result,"Yes");
//                                Toast toast = Toast.makeText(Cash_Collect.this, response_result, Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }


                        dialog.dismiss();
                        dialog.dismiss();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Global_Data.Custom_Toast(Cash_Collect.this,
                                    "your internet connection is not working, saving locally. Please sync when Internet is available","");
//                            Toast.makeText(Cash_Collect.this,
//                                    "your internet connection is not working, saving locally. Please sync when Internet is available",
//                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Global_Data.Custom_Toast(Cash_Collect.this,
                                    "Server AuthFailureError  Error","");
//                            Toast.makeText(Cash_Collect.this,
//                                    "Server AuthFailureError  Error",
//                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Global_Data.Custom_Toast(Cash_Collect.this,
                                    getResources().getString(R.string.Server_Errors),"");
//                            Toast.makeText(Cash_Collect.this,
//                                    getResources().getString(R.string.Server_Errors),
//                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Global_Data.Custom_Toast(Cash_Collect.this,
                                    "your internet connection is not working, saving locally. Please sync when Internet is available","");
//                            Toast.makeText(Cash_Collect.this,
//                                    "your internet connection is not working, saving locally. Please sync when Internet is available",
//                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Global_Data.Custom_Toast(Cash_Collect.this,
                                    "ParseError   Error","");
//                            Toast.makeText(Cash_Collect.this,
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG).show();
                        } else {
                            Global_Data.Custom_Toast(Cash_Collect.this, error.getMessage(),"");
                            //Toast.makeText(Cash_Collect.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                        // finish();
                    }
                });


                RequestQueue requestQueue = Volley.newRequestQueue(Cash_Collect.this);

                int socketTimeout = 150000;//90 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsObjRequest.setRetryPolicy(policy);
                // requestQueue.se
                //requestQueue.add(jsObjRequest);
                jsObjRequest.setShouldCache(false);
                requestQueue.getCache().clear();
                requestQueue.add(jsObjRequest);

            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("DATA", e.getMessage());
        }
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

                                if (Global_Data.image_counter <= 4) {


                                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                    String imageFileName = timeStamp + ".png";
                                    File storageDir = Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES);
                                    pictureImagePath = storageDir.getAbsolutePath() + "/" + "MCollection" + "/" + cust_name.getText().toString().trim() + "/" + val + "/" + imageFileName;
                                    file = new File(pictureImagePath);
                                    // Uri outputFileUri = Uri.fromFile(file);
                                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                                    Uri photoURI = FileProvider.getUriForFile(Cash_Collect.this,
                                            BuildConfig.APPLICATION_ID + ".provider",
                                            file);
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivityForResult(cameraIntent, 1);
                                } else {
                                    Global_Data.Custom_Toast(Cash_Collect.this, "You can not take more than 5 picture.","");
                                    //   Toast.makeText(Cash_Collect.this, "You can not take more than 5 picture.", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Global_Data.Custom_Toast(getApplicationContext(), "no camera on this device","");
                                // Toast.makeText(getApplicationContext(), "no camera on this device", Toast.LENGTH_LONG).show();
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
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(),"");
                        // Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Cash_Collect.this);
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
                SharedPreferences sp = Cash_Collect.this.getSharedPreferences("SimpleLogic", 0);
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


}
