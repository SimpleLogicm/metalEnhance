package com.msimplelogic.activities;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.helper.MultipartUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cpm.simplelogic.helper.ConnectionDetector;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class Customer_Stock extends BaseActivity {
    TextView tvCompstockPrice, tvCompstockproduct;
    Boolean B_flag;
    RelativeLayout container;
    EditText editCometitorName, editProductName;
    LinearLayout priceLout;
    LinearLayout cust_lout2;
    LinearLayout cust_lout1;
    RelativeLayout cust_lout3;
    cpm.simplelogic.helper.ConnectionDetector cd;
    Boolean isInternetPresent = false;
    String response_result;
    private String mCurrentPhotoPath = "";
    ZoomageView imgdialog;
    private ImageView img_show;
    ImageView crossimg;
    SharedPreferences sp;
    String strCompStockPrice = "";
    static String competitionStockCode = "";
    static String competitionStockPhotoPath = "";
    private String pictureImagePath = "";
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    LoginDataBaseAdapter loginDataBaseAdapter;
    Spinner feed_spinner, category_spinner, product_spinner, variant_spinner;
    Button claims_submit;
    ImageView btnCompstockPhoto;
    String image_check = "";
    int check = 0;
    ProgressBar progressBar_cyclic;
    int check_product = 0;
    int check_ProductSpec = 0;
    EditText edit_quantity, edit_details, etCompstockPrice;
    List<String> Stock;
    List<String> Categary;
    ImageView hedder_theame;
    List<String> product;
    List<String> variants;
    ArrayAdapter<String> adapter_state1;
    ArrayAdapter<String> adapter_state2;
    //ArrayAdapter<String> adapter_state4;
    ArrayAdapter<String> adapter_state3;
    ArrayAdapter<String> adapter_state4;
    Toolbar toolbar;
    String response_result_image = "";
    InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                //System.out.println("Type : " + type);
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    };
    private boolean isSpinnerInitial = true;
    private String Current_Date = "";
    private String CAT_ID = "";
    private String PRO_ID = "";
    private String VAR_ID = "";
    private String name, CP_NAME, RE_TEXT;
    private String filePath = null;

    EditText et_compstockprice;
    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerstock);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = Customer_Stock.this;
//        progressBar_cyclic.setClickable(false);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // Receiving the data from previous activity
        cd = new cpm.simplelogic.helper.ConnectionDetector(getApplicationContext());
        //  Intent i = getIntent();

        // image or video path that is captured in previous activity

        isSpinnerInitial = true;
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = sdf.format(c.getTime());
        Current_Date = sdf.format(c.getTime());

        Intent i = getIntent();
        name = i.getStringExtra("retialer");
        CP_NAME = i.getStringExtra("CP_NAME");
        RE_TEXT = i.getStringExtra("RE_TEXT");

        filePath = i.getStringExtra("filePath");

        if (filePath != null) {
            // Displaying the image or video on the screen
            //previewMedia(isImage);
            //Toast.makeText(getApplicationContext(),filePath, Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(getApplicationContext(),"Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }

        progressBar_cyclic = findViewById(R.id.progressBar_cyclic);
        container=findViewById(R.id.container);

        priceLout = (LinearLayout) findViewById(R.id.price_lout);
        cust_lout2 = (LinearLayout) findViewById(R.id.cust_lout2);
        cust_lout1 = (LinearLayout) findViewById(R.id.cust_lout1);
        cust_lout3 = (RelativeLayout) findViewById(R.id.cust_lout3);
        claims_submit = (Button) findViewById(R.id.claims_submit);
        edit_quantity = (EditText) findViewById(R.id.edit_quantity);
        edit_details = (EditText) findViewById(R.id.edit_details);
        etCompstockPrice = (EditText) findViewById(R.id.et_compstockprice);
        btnCompstockPhoto = (ImageView) findViewById(R.id.btn_compstockphoto);
        hedder_theame = (ImageView) findViewById(R.id.hedder_theame);
//        edit_cometitorname = (EditText) findViewById(R.id.edit_cometitorname);
//        edit_productname = (EditText) findViewById(R.id.edit_cometitorname);

        edit_details.setFilters(new InputFilter[]{filter});

        //feed_spinner=(Spinner)findViewById(R.id.comp_stock);
        category_spinner = (Spinner) findViewById(R.id.category);
        product_spinner = (Spinner) findViewById(R.id.product);
        variant_spinner = (Spinner) findViewById(R.id.variant);
        tvCompstockPrice = (TextView) findViewById(R.id.tv_compstockprice);
        tvCompstockproduct = (TextView) findViewById(R.id.tv_compstockproduct);
        editCometitorName = (EditText) findViewById(R.id.edit_cometitorname);
        editProductName = (EditText) findViewById(R.id.edit_productname);


        img_show = findViewById(R.id.img_show);
        crossimg = findViewById(R.id.crossimg);
        crossimg.setVisibility(View.INVISIBLE);

        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1){

            hedder_theame.setImageResource(R.drawable.dark_hedder);
            // linearMian.setBackgroundColor(Color.BLACK);



//ll.setBackgroundResource(R.drawable.dark_theme_background);
            //      final int sdk = android.os.Build.VERSION.SDK_INT; if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) { ll.setBackgroundResource(R.drawable.dark_theme_background); } else { ll.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.dark_theme_background)); }
        }

        crossimg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                deletedialog();
            }
        });

        img_show.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentPhotoPath != "") {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Stock.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.dialogeimage, null);
                    imgdialog = dialogLayout.findViewById(R.id.imageView);
                    Glide.with(Customer_Stock.this).load(mCurrentPhotoPath).into(imgdialog);
                    builder.setPositiveButton("OK", null);
                    builder.setView(dialogLayout);
                    builder.show();
                }
            }
        });

        Categary = new ArrayList<String>();
        product = new ArrayList<String>();
        variants = new ArrayList<String>();
        Stock = new ArrayList<String>();

        Categary.add(getResources().getString(R.string.Select_Categories));
        product.add(getResources().getString(R.string.Select_Product));
        variants.add(getResources().getString(R.string.Select_Variant));
        Stock.add(getResources().getString(R.string.Select_Competitors_Stock));

        List<Local_Data> contacts2 = dbvoc.getAllProductPackSizes();
        for (Local_Data cn : contacts2) {
            Stock.add(cn.get_PackSizes_desc());
        }

        List<Local_Data> contacts1 = dbvoc.HSS_DescriptionITEM();
        for (Local_Data cn : contacts1) {
            if (!cn.getStateName().equalsIgnoreCase("") && !cn.getStateName().equalsIgnoreCase(" ")) {
                String str_categ = "" + cn.getStateName();
                Categary.add(str_categ);
            }
        }

        adapter_state2 = new ArrayAdapter<String>(this, R.layout.spinner_item, Categary);
        adapter_state2.setDropDownViewResource(R.layout.spinner_item);
        category_spinner.setAdapter(adapter_state2);

        adapter_state3 = new ArrayAdapter<String>(Customer_Stock.this, R.layout.spinner_item, product);
        adapter_state3.setDropDownViewResource(R.layout.spinner_item);
        product_spinner.setAdapter(adapter_state3);

        adapter_state4 = new ArrayAdapter<String>(Customer_Stock.this, R.layout.spinner_item, variants);
        adapter_state4.setDropDownViewResource(R.layout.spinner_item);
        variant_spinner.setAdapter(adapter_state4);
        // category_spinner.setOnItemSelectedListener(this);
//
//		  adapter_state3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		  product_spinner.setAdapter(adapter_state3);
//		  product_spinner.setOnItemSelectedListener(this);
//
//
//		  adapter_state3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		  variant_spinner.setAdapter(adapter_state4);
//		  variant_spinner.setOnItemSelectedListener(this);


        //variant_spinner.setOnItemSelectedListener(this);

        btnCompstockPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();

            }
        });

        claims_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (category_spinner.getSelectedItem().toString() == getResources().getString(R.string.Select_Categories)) {

                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Select_Category), "Yes");
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Select_Category), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();

                } else if (product_spinner.getSelectedItem().toString() == getResources().getString(R.string.Select_Product)) {
                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Select_Product), "Yes");

//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Select_Product), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();

                } else if (variant_spinner.getSelectedItem().toString() == getResources().getString(R.string.Select_Variant)) {
                    // Toast.makeText(getApplicationContext(),"Please Variant", Toast.LENGTH_LONG).show();

                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Select_Variant), "Yes");
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Select_Variant), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(editCometitorName.getText().toString())) {
                    Global_Data.Custom_Toast(getApplicationContext(), "Please enter Competitor name", "Yes");
                } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(editProductName.getText().toString())) {
                    Global_Data.Custom_Toast(getApplicationContext(), "Please enter Product name", "Yes");


                } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etCompstockPrice.getText().toString())) {
                    Global_Data.Custom_Toast(getApplicationContext(), "Please enter price", "Yes");

                } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(edit_quantity.getText().toString())) {
                    //Toast.makeText(getApplicationContext(),"Please Enter Quantity", Toast.LENGTH_LONG).show();

                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_enter_Quantity), "Yes");
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_enter_Quantity), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(edit_details.getText().toString())) {
                    // Toast.makeText(getApplicationContext(),"Please Enter Details", Toast.LENGTH_LONG).show();
                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Enter_Details), "Yes");
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Enter_Details), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                } else {
//					 String RE_ID = "";
                    List<Local_Data> contacts = dbvoc.VARIENT_ID(variant_spinner.getSelectedItem().toString());
                    for (Local_Data cn : contacts) {

                        VAR_ID = cn.getItem_Code();
                        PRO_ID = cn.getItem_Code();
                        CAT_ID = cn.getItem_Code();
                    }

                    String gaddress = "";
                    try {
                        if (Global_Data.address.equalsIgnoreCase("null")) {
                            gaddress = "";
                        } else {
                            gaddress = Global_Data.address;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    String sms_body = "";
                    sms_body = "Dear " + Global_Data.USER_MANAGER_NAME + " ," + "\n" + " There is competitive information from  " + Global_Data.CUSTOMER_NAME_NEW + "  that I have uploaded." + " This is to keep you updated." + "\n\n" + " Thank you." + "\n" + " " + Global_Data.USER_FIRST_NAME + " " + Global_Data.USER_LAST_NAME + "\n" + " " + gaddress;

                    if (!Global_Data.cus_MAnager_mobile.equalsIgnoreCase(null) && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("null") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase(" ")) {
                        //	 Global_Data.sendSMS(Global_Data.cus_MAnager_mobile,sms_body, Customer_Stock.this);
                        // mobile_numbers.add(Global_Data.cus_MAnager_mobile);
                    }

                    try {
                        AppLocationManager appLocationManager = new AppLocationManager(Customer_Stock.this);
                        Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                        Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                        PlayService_Location PlayServiceManager = new PlayService_Location(Customer_Stock.this);

                        if (PlayServiceManager.checkPlayServices(Customer_Stock.this)) {
                            Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                        } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                            Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                            Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    Long randomPIN = System.currentTimeMillis();
                    String PINString = String.valueOf(randomPIN);

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDatef = dff.format(c.getTime());

                    SharedPreferences spf = Customer_Stock.this.getSharedPreferences("SimpleLogic", 0);
                    String user_email = spf.getString("USER_EMAIL", null);

                    cd = new ConnectionDetector(getApplicationContext());

                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        progressBar_cyclic.setVisibility(View.VISIBLE);
                        priceLout.setVisibility(View.GONE);
                        cust_lout1.setVisibility(View.GONE);
                        cust_lout2.setVisibility(View.GONE);
                        cust_lout3.setVisibility(View.GONE);
                        claims_submit.setVisibility(View.GONE);

                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        stockservice(PINString, user_email, VAR_ID);

                    } else {
                        progressBar_cyclic.setVisibility(View.VISIBLE);
                        priceLout.setVisibility(View.GONE);
                        cust_lout1.setVisibility(View.GONE);
                        cust_lout2.setVisibility(View.GONE);
                        cust_lout3.setVisibility(View.GONE);
                        claims_submit.setVisibility(View.GONE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        loginDataBaseAdapter.insertCustomerServiceCompetitionStock("", Global_Data.GLOvel_CUSTOMER_ID, Global_Data.GLOvel_CUSTOMER_ID, user_email,
                                CAT_ID, PRO_ID, VAR_ID, "PSIZE_ID", "PNAME", edit_quantity.getText().toString(), edit_details.getText().toString(), Current_Date, Current_Date, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, PINString, etCompstockPrice.getText().toString(), pictureImagePath, editCometitorName.getText().toString(), editProductName.getText().toString());
                        loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "C STOCK", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Your_Data_Saved_Successfully), "Yes");
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Your_Data_Saved_Successfully), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();


                        Intent intent = new Intent(Customer_Stock.this, Neworderoptions.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        progressBar_cyclic.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        priceLout.setVisibility(View.VISIBLE);
                        cust_lout1.setVisibility(View.VISIBLE);
                        cust_lout2.setVisibility(View.VISIBLE);
                        cust_lout3.setVisibility(View.VISIBLE);
                        claims_submit.setVisibility(View.VISIBLE);



                    }


                }
            }
        });

        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));

            LayoutInflater mInflater = LayoutInflater.from(this);
            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);

            mTitleTextView.setText(getResources().getString(R.string.Competition_Stock));
            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            ImageView H_LOGO = (ImageView) mCustomView.findViewById(R.id.Header_logo);
            H_LOGO.setImageResource(R.drawable.list);
            H_LOGO.setVisibility(View.VISIBLE);
            SharedPreferences sp = Customer_Stock.this.getSharedPreferences("SimpleLogic", 0);
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
                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                } else {
                    int age = (int) Math.round(age_float);
                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    }
                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
//	        	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
                todaysTarget.setText("Today's Target Acheived");
            }

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        category_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
//				// TODO Auto-generated method stub


                check = check + 1;
                if (check > 1) {
                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase(getResources().getString(R.string.Select_Categories))) {

                        product.clear();
                        product.add(getResources().getString(R.string.Select_Product));
                        adapter_state3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, product);
                        adapter_state3.setDropDownViewResource(R.layout.spinner_item);
                        product_spinner.setAdapter(adapter_state3);

                        variants.clear();
                        variants.add(getResources().getString(R.string.Select_Variant));
                        adapter_state4 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, variants);
                        adapter_state4.setDropDownViewResource(R.layout.spinner_item);
                        variant_spinner.setAdapter(adapter_state4);

                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Select_Category), "Yes");
//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Select_Category), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                    } else {


                        product.clear();
                        //List<Local_Data> contacts22 = dbvoc.HSS_DescriptionITEM1_ID(Global_Data.GLOVEL_CATEGORY_ID);
                        List<Local_Data> contacts22 = dbvoc.HSS_DescriptionITEM1_category_name(parent.getItemAtPosition(pos).toString().trim());
                        product.add(getResources().getString(R.string.Select_Product));
                        for (Local_Data cn : contacts22) {
                            String str_product = "" + cn.getStateName();
                            //Global_Data.local_pwd = ""+cn.getPwd();

                            product.add(str_product);
                            System.out.println("Local Values:-" + Global_Data.local_user);
                            //Toast.makeText(LoginActivity.this, "Login Invalid"+Global_Data.local_user,Toast.LENGTH_SHORT).show();
                        }

                        adapter_state3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, product);
                        adapter_state3.setDropDownViewResource(R.layout.spinner_item);
                        product_spinner.setAdapter(adapter_state3);
                        //startActivity(intent);
                        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }
//
                }
//
//

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        product_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
//					// TODO Auto-generated method stub

                check_product = check_product + 1;
                if (check_product > 1) {

                    if (category_spinner.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Categories))) {

                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Select_Category), "Yes");
//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Select_Category), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                    } else if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase(getResources().getString(R.string.Select_Product))) {
                        variants.clear();
                        variants.add(getResources().getString(R.string.Select_Variant));
                        adapter_state4 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, variants);
                        adapter_state4.setDropDownViewResource(R.layout.spinner_item);
                        variant_spinner.setAdapter(adapter_state4);
                    } else {
                        List<Local_Data> contacts33 = dbvoc.HSS_DescriptionITEM1_IDD(parent.getItemAtPosition(pos).toString().trim());
                        for (Local_Data cn : contacts33) {
                            Global_Data.GLOVEL_PRODUCT_ID = cn.getCust_Code();
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.GLOVEL_PRODUCT_ID)) {
                            variants.clear();
                            //List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_ID(Global_Data.GLOVEL_PRODUCT_ID);
                            List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_BYNAME(category_spinner.getSelectedItem().toString().trim(), parent.getItemAtPosition(pos).toString().trim());
                            variants.add(getResources().getString(R.string.Select_Variant));
                            for (Local_Data cn : contacts3) {
                                variants.add(cn.getStateName().trim());
                            }
                            adapter_state4 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, variants);
                            adapter_state4.setDropDownViewResource(R.layout.spinner_item);
                            variant_spinner.setAdapter(adapter_state4);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        variant_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
//					// TODO Auto-generated method stub

                List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_BYNAMENEW(category_spinner.getSelectedItem().toString().trim(), product_spinner.getSelectedItem().toString().trim(), parent.getItemAtPosition(pos).toString().trim());

                if (contacts3.size() <= 0) {
                    // Toast.makeText(Pricing_Main.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();
                    priceLout.setVisibility(View.GONE);
                } else {
                    for (Local_Data cn : contacts3) {
                        strCompStockPrice = cn.getMRP();
                        priceLout.setVisibility(View.VISIBLE);
                        tvCompstockPrice.setText((getResources().getString(R.string.RP) + " " + cn.getRP() + " : " + getResources().getString(R.string.MRP)) + " " + strCompStockPrice);
                        tvCompstockproduct.setText(cn.get_product_desc());
                    }

//                    if (Product_Variant.length() > 0) {
//                        recList.setVisibility(View.VISIBLE);
//                        List<ContactInfo> result = new ArrayList<ContactInfo>();
//                        for (Local_Data cn : contacts3) {
//
//                            ContactInfo ci = new ContactInfo();
//                            ci.name = cn.get_product_desc();
//                            ci.rp = cn.getRP();
//                            ci.mrp = cn.getMRP();
//
//                            result.add(ci);
//                        }
//
//                        ContactAdapter ca = new ContactAdapter(Pricing_Main.this, result);
//                        recList.setAdapter(ca);
//                        ca.notifyDataSetChanged();
//                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void stockservice(String PINString, String user_email, String VAR_ID) {

        JSONArray COMPS = new JSONArray();
        JSONObject product_value_n = new JSONObject();

        JSONObject cll = new JSONObject();
        try {
            cll.put("code", PINString);
            cll.put("customer_code", Global_Data.GLOvel_CUSTOMER_ID);
            cll.put("user_email", user_email);
            //cll.put("category_id", cn.get_category_id());
            // cll.put("product_id", cn.get_product_code());
            cll.put("product_code", VAR_ID);
            cll.put("competition_product_text", edit_details.getText().toString());
            cll.put("competition_product_quantity", edit_quantity.getText().toString());
            cll.put("latitude", Global_Data.GLOvel_LATITUDE);
            cll.put("longitude", Global_Data.GLOvel_LONGITUDE);
            cll.put("competitor_product_price", etCompstockPrice.getText().toString());

            String ssss = editProductName.getText().toString();
            String sss = editCometitorName.getText().toString();
            cll.put("competition_product_name", ssss);
            cll.put("competitor_name", sss);


            competitionStockCode = PINString;
            competitionStockPhotoPath = pictureImagePath;


        } catch (JSONException e) {
            e.printStackTrace();
        }


        COMPS.put(cll);


        try {
            product_value_n.put("stocks", COMPS);
            product_value_n.put("email", user_email);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {

            String domain = Customer_Stock.this.getResources().getString(R.string.service_domain);
            String service_url = "";
            service_url = domain + "customer_stocks/create_customer_service_competition_stock";
//            product_valuenew.put("no_orders", no_order);
//            product_valuenew.put("email", user_email);
//            Log.d("No Order", no_order.toString());

            Log.i("volley", "domain: " + service_url+product_value_n);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, service_url, product_value_n, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);


                    response_result = "";
                    //if (response.has("result")) {
                    try {
                        response_result = response.getString("result");
                    } catch (JSONException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            public void run() {

//                                dialog.dismiss();
//                                buttonnoOrderSave.setEnabled(true);
//                                buttonnoOrderSave.setText("Submit");
//                                buttonnoOrdercancel.setEnabled(true);
                            }
                        });
                    }

                    if (response_result.equalsIgnoreCase("Device not found.")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Global_Data.Custom_Toast(getApplicationContext(), response_result, "yes");
//
//                                        Toast toast = Toast.makeText(NoOrderActivity.this, "Device Not Found", Toast.LENGTH_LONG);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();
////                                        dialog.dismiss();
//                                buttonnoOrderSave.setEnabled(true);
//                                buttonnoOrderSave.setText("Submit");
//                                buttonnoOrdercancel.setEnabled(true);
//

                            }
                        });

                    } else {

                        runOnUiThread(new Runnable() {
                            public void run() {
//                                buttonnoOrderSave.setEnabled(true);
//                                buttonnoOrderSave.setText("Submit");
//                                buttonnoOrdercancel.setEnabled(true);

                                if (competitionStockPhotoPath.length() > 0) {

                                    new doFileUpload3().execute(competitionStockCode, competitionStockPhotoPath);
                                    //  Global_Data.Custom_Toast(getApplicationContext(), response_result, "yes");
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                } else {
                                    Global_Data.Custom_Toast(getApplicationContext(), response_result, "yes");

                                    Customer_Stock.this.finish();
                                    progressBar_cyclic.setVisibility(View.GONE);
                                    priceLout.setVisibility(View.VISIBLE);
                                    cust_lout1.setVisibility(View.VISIBLE);
                                    cust_lout2.setVisibility(View.VISIBLE);
                                    cust_lout3.setVisibility(View.VISIBLE);
                                    claims_submit.setVisibility(View.VISIBLE);

                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    finish();
                                }


                                // dialog.dismiss();


                            }
                        });


                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    // dialog.dismiss();
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar_cyclic.setVisibility(View.GONE);
                                priceLout.setVisibility(View.VISIBLE);
                                cust_lout1.setVisibility(View.VISIBLE);
                                cust_lout2.setVisibility(View.VISIBLE);
                                cust_lout3.setVisibility(View.VISIBLE);
                                claims_submit.setVisibility(View.VISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                Global_Data.Custom_Toast(getApplicationContext(), "your internet connection is not working, saving locally. Please sync when Internet is available", "");
//
//                                        Toast.makeText(NoOrderActivity.this,
//                                                "your internet connection is not working, saving locally. Please sync when Internet is available",
//                                                Toast.LENGTH_LONG).show();
                            }
                        });

                    } else if (error instanceof AuthFailureError) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar_cyclic.setVisibility(View.GONE);
                                priceLout.setVisibility(View.VISIBLE);
                                cust_lout1.setVisibility(View.VISIBLE);
                                cust_lout2.setVisibility(View.VISIBLE);
                                cust_lout3.setVisibility(View.VISIBLE);
                                claims_submit.setVisibility(View.VISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                Global_Data.Custom_Toast(getApplicationContext(), "Server AuthFailureError  Error", "");
//
//                                        Toast.makeText(NoOrderActivity.this,
//                                                "Server AuthFailureError  Error",
//                                                Toast.LENGTH_LONG).show();
                            }
                        });

                    } else if (error instanceof ServerError) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar_cyclic.setVisibility(View.GONE);
                                priceLout.setVisibility(View.VISIBLE);
                                cust_lout1.setVisibility(View.VISIBLE);
                                cust_lout2.setVisibility(View.VISIBLE);
                                cust_lout3.setVisibility(View.VISIBLE);
                                claims_submit.setVisibility(View.VISIBLE);
                                Global_Data.Custom_Toast(getApplicationContext(), "Server   Error", "");

//                                        Toast.makeText(NoOrderActivity.this,
//                                                "Server   Error",
//                                                Toast.LENGTH_LONG).show();
                            }
                        });

                    } else if (error instanceof NetworkError) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar_cyclic.setVisibility(View.GONE);
                                priceLout.setVisibility(View.VISIBLE);
                                cust_lout1.setVisibility(View.VISIBLE);
                                cust_lout2.setVisibility(View.VISIBLE);
                                cust_lout3.setVisibility(View.VISIBLE);
                                claims_submit.setVisibility(View.VISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                Global_Data.Custom_Toast(getApplicationContext(), "your internet connection is not working, saving locally. Please sync when Internet is available", "");

//                                        Toast.makeText(NoOrderActivity.this,
//                                                "your internet connection is not working, saving locally. Please sync when Internet is available",
//                                                Toast.LENGTH_LONG).show();
                            }
                        });

                    } else if (error instanceof ParseError) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar_cyclic.setVisibility(View.GONE);
                                priceLout.setVisibility(View.VISIBLE);
                                cust_lout1.setVisibility(View.VISIBLE);
                                cust_lout2.setVisibility(View.VISIBLE);
                                cust_lout3.setVisibility(View.VISIBLE);
                                claims_submit.setVisibility(View.VISIBLE);
                                Global_Data.Custom_Toast(getApplicationContext(), "ParseError   Error", "");

//                                        Toast.makeText(NoOrderActivity.this,
//                                                "ParseError   Error",
//                                                Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar_cyclic.setVisibility(View.GONE);
                                priceLout.setVisibility(View.VISIBLE);
                                cust_lout1.setVisibility(View.VISIBLE);
                                cust_lout2.setVisibility(View.VISIBLE);
                                cust_lout3.setVisibility(View.VISIBLE);
                                claims_submit.setVisibility(View.VISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(), "");


//                                        Toast.makeText(NoOrderActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
//                            buttonnoOrderSave.setEnabled(true);
//                            buttonnoOrderSave.setText("Submit");
//                            buttonnoOrdercancel.setEnabled(true);
//
//                            dialog.dismiss();
                        }
                    });

                    // finish();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(Customer_Stock.this);
            // queue.add(jsObjRequest);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);

        } catch (Exception ex) {
            ex.printStackTrace();

            runOnUiThread(new Runnable() {
                public void run() {
//                    buttonnoOrderSave.setEnabled(true);
//                    buttonnoOrderSave.setText("Submit");
//                    buttonnoOrdercancel.setEnabled(true);
//                    dialog.dismiss();

                }
            });
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
                String targetNew = "";
                SharedPreferences sp = Customer_Stock.this.getSharedPreferences("SimpleLogic", 0);
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
        //super.onBackPressed();

        Categary.clear();
        product.clear();
        variants.clear();
        Stock.clear();

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        this.finish();
    }

    public void getproductA(String ID) {
        List<Local_Data> contacts2 = dbvoc.getProduct(ID);
        for (Local_Data cn : contacts2) {

            product.add(cn.get_product_desc());
        }
        adapter_state3 = new ArrayAdapter<String>(Customer_Stock.this,R.layout.spinner_item, product);
        adapter_state3.setDropDownViewResource(R.layout.spinner_item);
        product_spinner.setAdapter(adapter_state3);
    }

    public void getvariantsA(String ID) {
        List<Local_Data> contacts2 = dbvoc.getVariants(ID);
        for (Local_Data cn : contacts2) {

            variants.add(cn.get_variants_desc());
        }

        adapter_state4 = new ArrayAdapter<String>(Customer_Stock.this, R.layout.spinner_item, variants);
        adapter_state4.setDropDownViewResource(R.layout.spinner_item);
        variant_spinner.setAdapter(adapter_state4);
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


                                //final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Choose_from_Gallery), getResources().getString(R.string.Cancel)};

                                final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Cancel)};


                                AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Stock.this);

                                builder.setTitle(getResources().getString(R.string.Add_Photo));

                                builder.setItems(options, new DialogInterface.OnClickListener() {

                                    @Override

                                    public void onClick(DialogInterface dialog, int item) {
                                        if (options[item].equals(getResources().getString(R.string.Take_Photo))) {
//                                            image_check = "photo";
//                                            File photoFile = null;
//                                            try {
//                                                photoFile = createImageFile();
//                                            } catch (IOException ex) {
//                                                // Error occurred while creating the File
//                                                Log.i("Image TAG", "IOException");
//                                                pictureImagePath = "";
//                                            }
//                                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                                            startActivityForResult(cameraIntent, 1);
//
                                            image_check = "photo";

                                            File photoFile = null;
                                            try {
                                                photoFile = createImageFile();
                                            } catch (IOException ex) {
                                                // Error occurred while creating the File
                                                Log.i("Image TAG", "IOException");
                                                pictureImagePath = "";
                                            }
                                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                            startActivityForResult(cameraIntent, 1);

                                        }
//                                        else if (options[item].equals(getResources().getString(R.string.Choose_from_Gallery)))
//
//                                        {
//
//                                            image_check = "gallery";
//                                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                                            startActivityForResult(intent, 2);
//
//
//                                        }

                                        else if (options[item].equals(getResources().getString(R.string.Cancel))) {

                                            dialog.dismiss();

                                        }

                                    }

                                });

                                builder.show();


                            } else {
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.no_camera), "Yes");
                                //   Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_camera), Toast.LENGTH_LONG).show();
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

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Stock.this);
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

    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "comp";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Comp_Stock");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        pictureImagePath = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = image.getAbsolutePath();
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            File imgFils = new File(mCurrentPhotoPath);
            if (imgFils.exists()) {

                //img_show = findViewById(R.id.img_show);
                crossimg.setVisibility(View.VISIBLE);
                img_show.setVisibility(View.VISIBLE);
                //img_show.setRotation((float) 90.0);
                //img_show.setImageURI(Uri.fromFile(imgFils));
                Glide.with(Customer_Stock.this).load(mCurrentPhotoPath).into(img_show);
            }

            //new Expenses.LongOperation().execute();

//			try {
//
//				//dbvoc.updateORDER_order_image(pictureImagePath, Global_Data.GLObalOrder_id);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

        } else if (requestCode == 2 && resultCode == RESULT_OK) {

            try {
                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                pictureImagePath = "file:" + c.getString(columnIndex);

                c.close();

                //new Expenses.LongOperation().execute();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deletedialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(Customer_Stock.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Warning));
        alertDialog.setMessage(getResources().getString(R.string.image_dialog_warning_message));
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                File file = new File(mCurrentPhotoPath);
                boolean delete = file.delete();

                File file1 = new File(pictureImagePath);
                boolean delete1 = file1.delete();

                img_show.setVisibility(View.INVISIBLE);
                crossimg.setVisibility(View.INVISIBLE);

                Global_Data.Custom_Toast(Customer_Stock.this, getResources().getString(R.string.image_dialog_delete_success), "Yes");
//                Toast toast = Toast.makeText(Customer_Stock.this, getResources().getString(R.string.image_dialog_delete_success),
//                        Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();

                //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });
        alertDialog.show();
    }

    public class doFileUpload3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... response) {

            String domain = getResources().getString(R.string.service_domain) + "customer_service_feedbacks/customer_service_upload_images";
            Log.i("volley", "domain: " + domain);


            Uri uri4 = Uri.parse(response[1]);
            final File file4 = new File(uri4.getPath());

            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(domain, charset);

                //for comp stock
                if (!response[0].equalsIgnoreCase("")) {
                    multipart.addFormField("competition_stock_code", response[0]);
                    multipart.addFormField("competition_stock_type", "competition_stock");
                }

                if (!response[1].equalsIgnoreCase("")) {
                    multipart.addFilePart("competition_stock_image", file4);
                }


                List<String> response1 = multipart.finish();

                Log.v("rht", "SERVER REPLIED:");

                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        // dialog.dismiss();
                    }
                });

                for (String line : response1) {
                    Log.v("rht", "Line : " + line);
                    response_result_image = line;
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            try {

                                JSONObject obj = new JSONObject(response_result_image);

                                if (obj.getString("message").equalsIgnoreCase("Customer Services uploaded images successfully.")) {
                                    Global_Data.Custom_Toast(getApplicationContext(), response_result, "yes");
                                    if (file4.exists()) {
                                        if (file4.delete()) {
                                            System.out.println("file Deleted :" + competitionStockPhotoPath);
                                        } else {
                                            System.out.println("file not Deleted :" + competitionStockPhotoPath);
                                        }
                                    }

                                    competitionStockPhotoPath = "";
                                    finish();

                                }

                                Log.d("My App", obj.toString());

                            } catch (Throwable t) {
                                Log.e("My App", "Could not parse malformed JSON: \"" + response_result_image + "\"");

                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        //  dialog.dismiss();

                                        // Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                                    }
                                });
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        //  dialog.dismiss();
                        //Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                    }
                });
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            ((Activity) context).runOnUiThread(new Runnable() {
                public void run() {

                    // dialog.dismiss();
                    // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                }
            });

        }

        @Override
        protected void onPreExecute() {

//            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//            dialog.setMessage("Image Sync in Progress, Please Wait");
//            dialog.setTitle("Metal");
//            dialog.setCancelable(false);
//            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
