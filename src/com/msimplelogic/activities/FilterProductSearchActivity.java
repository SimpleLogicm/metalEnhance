package com.msimplelogic.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msimplelogic.activities.R;
//import com.v.database.DataBaseHelper;
//import com.v.database.LoginDataBaseAdapter;
//import com.v.helper.Check_Null_Value;
//import com.v.helper.ConnectionDetector;
//import com.v.helper.Global_Data;
//import com.v.helper.Local_Data;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cpm.simplelogic.helper.ConnectionDetector;

public class FilterProductSearchActivity extends Activity implements DatePickerDialog.OnDateSetListener{
    private CoordinatorLayout coordinatorLayout;
    Boolean isInternetPresent = false;
    static String final_response = "";
    String response_result = "";
    ConnectionDetector cd;
    ProgressDialog dialog;
    int check=0;
    int check_product=0;
    int check_ProductSpec=0;
    String CategoriesSpinner = "";
    String str_var1;
    DatePickerDialog.OnDateSetListener date, date1;
    //AutoCompleteTextView Product_Variant;
    Calendar myCalendar;
    String categ_name,subcateg_name;
    String ProductSpinner = "";
    String price = "";
    String str_variant;
    String product_code = "";
    String scheme_code = "";
    String scheme_namen = "";
    LoginDataBaseAdapter loginDataBaseAdapter;
    String click_detect_flag = "";
    DatePickerDialog datePickerDialog ;
    EditText fromdate;
    EditText todate;

    static Calendar calendar ;

    static int Year, Month, Day ;

    Button btn_product_search;

    Spinner spnCategory, spnProductSpec, spnScheme, spnProduct;
    AutoCompleteTextView Product_Variant;
    private Toolbar toolbar;
    private ArrayList<String> result_product = new ArrayList<String>();
    private ArrayList<String> results = new ArrayList<String>();
    private ArrayList<String> results1 = new ArrayList<String>();
    private ArrayList<String> results2 = new ArrayList<String>();


    ArrayAdapter<String> adapter_category;
    ArrayAdapter<String> adapter_subcategory;
    ArrayAdapter<String> adapter_varient;
    TextView txt_data;


    SimpleCursorAdapter cursorAdapter;
    DataBaseHelper dbvoc = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_filterproductsearch);

        calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        fromdate = (EditText) findViewById(R.id.fromdate);
        todate = (EditText)findViewById(R.id.todate);

//        setSupportActionBar(toolbar);
//
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
//        }

//        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
//                .coordinatorLayout);

        cd = new ConnectionDetector(getApplicationContext());

        // create a instance of SQLite Database
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnProduct = (Spinner) findViewById(R.id.spnProduct);
        Product_Variant = (AutoCompleteTextView) findViewById(R.id.Product_Variant);
        txt_data = (TextView) findViewById(R.id.txt_data);

        btn_product_search=(Button)findViewById(R.id.button_prdsearch);

        results1.add("Select Category");
        adapter_category = new ArrayAdapter<String>(FilterProductSearchActivity.this, R.layout.spinner_item, results1);
        adapter_category.setDropDownViewResource(R.layout.spinner_item);
        spnCategory.setAdapter(adapter_category);

        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
            dialog = new ProgressDialog(FilterProductSearchActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Please wait Categories Loading....");
            dialog.setTitle("Metal App");
            dialog.setCancelable(false);
            dialog.show();

            category_details();
        }
        else
        {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "You don't have internet connection.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        myCalendar = Calendar.getInstance();

        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Global_Data.hideSoftKeyboard(FilterProductSearchActivity.this);

                click_detect_flag = "from_date";

                new DatePickerDialog(FilterProductSearchActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                //updateLabel();

//                datePickerDialog = DatePickerDialog.newInstance(FilterProductSearchActivity.this, Year, Month, Day);
//
//                datePickerDialog.setThemeDark(false);
//
//                datePickerDialog.showYearPickerFirst(false);
//
//                datePickerDialog.setAccentColor(Color.parseColor("#E43F3F"));
//
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
//
//                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

            }
        });

        // Action for custom dialog ok button click
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   Global_Data.hideSoftKeyboard(FilterProductSearchActivity.this);

                click_detect_flag = "to_date";

                new DatePickerDialog(FilterProductSearchActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

//                datePickerDialog = DatePickerDialog.newInstance(FilterProductSearchActivity.this, Year, Month, Day);
//
//                datePickerDialog.setThemeDark(false);
//
//                datePickerDialog.showYearPickerFirst(false);
//
//                datePickerDialog.setAccentColor(Color.parseColor("#E43F3F"));
//
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
//
//                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

            }
        });

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }
        };

        btn_product_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spnCategory.getSelectedItem().toString().equalsIgnoreCase("Select All"))
                {
                    Global_Data.Act_Performance="product_search";
                    Global_Data.GLOVEL_PRODUCT_SERCH_VALUE = "CATEGORY"+"@"+"ALL CATEGORY";

                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(fromdate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterProductSearchActivity.this,
//                                "Please Select From Date.", Toast.LENGTH_SHORT);
//                        toast.show();
                        Global_Data.Custom_Toast(FilterProductSearchActivity.this,
                                "Please Select From Date.","");
                    }
                    else
                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(todate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterProductSearchActivity.this,
//                                "Please Select To Date.", Toast.LENGTH_SHORT);
//
//                        toast.show();
                        Global_Data.Custom_Toast(FilterProductSearchActivity.this,
                                "Please Select To Date.","");
                    }
                    else
                    {
                        Global_Data.Globel_fromDate = fromdate.getText().toString().trim();
                        Global_Data.Globel_toDate = todate.getText().toString().trim();

                        if(Global_Data.Act_Performance.equalsIgnoreCase("product_search"))
                        {
                            Intent ss = new Intent(FilterProductSearchActivity.this,ProductAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("beat_search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,Beat_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }else if(Global_Data.Act_Performance.equalsIgnoreCase("customer_search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,Customer_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("User_Search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,User_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("Target_Search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,MainActivity.class);
//                            startActivity(ss);
//                            finish();
//                        }
                    }

//                    Intent intent = new Intent(Product_Search.this, Customer_DatePIcker.class);
//                    startActivity(intent);

                }
                else if (spnProduct.getSelectedItem().toString().equalsIgnoreCase("Select All"))
                {
                    Global_Data.Act_Performance="product_search";
                    Global_Data.GLOVEL_PRODUCT_SERCH_VALUE = spnCategory.getSelectedItem().toString() +"@" +"ALL SUB CATEGORY";

                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(fromdate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterProductSearchActivity.this,
//                                "Please Select From Date.", Toast.LENGTH_SHORT);
//                        toast.show();
                        Global_Data.Custom_Toast(FilterProductSearchActivity.this,
                                "Please Select From Date.","");
                    }
                    else
                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(todate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterProductSearchActivity.this,
//                                "Please Select To Date.", Toast.LENGTH_SHORT);
//
//                        toast.show();
                        Global_Data.Custom_Toast(FilterProductSearchActivity.this,
                                "Please Select To Date.","");
                    }
                    else
                    {
                        Global_Data.Globel_fromDate = fromdate.getText().toString().trim();
                        Global_Data.Globel_toDate = todate.getText().toString().trim();

                        if(Global_Data.Act_Performance.equalsIgnoreCase("product_search"))
                        {
                            Intent ss = new Intent(FilterProductSearchActivity.this,ProductAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("beat_search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,Beat_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }else if(Global_Data.Act_Performance.equalsIgnoreCase("customer_search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,Customer_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("User_Search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,User_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("Target_Search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,MainActivity.class);
//                            startActivity(ss);
//                            finish();
//                        }
                    }

//                    Intent intent = new Intent(Product_Search.this, Customer_DatePIcker.class);
//                    startActivity(intent);
                }else if((fromdate.getText().toString().length()>0) || (todate.getText().toString().length()>0))
                {
                    Global_Data.Act_Performance="product_search";
                    Global_Data.GLOVEL_PRODUCT_SERCH_VALUE = "CATEGORY"+"@"+"ALL CATEGORY";

                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(fromdate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterProductSearchActivity.this,
//                                "Please Select From Date.", Toast.LENGTH_SHORT);
//                        toast.show();
                        Global_Data.Custom_Toast(FilterProductSearchActivity.this,
                                "Please Select From Date.","");
                    }
                    else
                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(todate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterProductSearchActivity.this,
//                                "Please Select To Date.", Toast.LENGTH_SHORT);
//
//                        toast.show();
                        Global_Data.Custom_Toast(FilterProductSearchActivity.this,
                                "Please Select To Date.","");
                    }
                    else
                    {
                        Global_Data.Globel_fromDate = fromdate.getText().toString().trim();
                        Global_Data.Globel_toDate = todate.getText().toString().trim();

                        Global_Data.GLOVEL_PRODUCT_SERCH_Category = spnCategory.getSelectedItem().toString();
                        Global_Data.GLOVEL_PRODUCT_SERCH_Sub_Category = spnProduct.getSelectedItem().toString();
                        Global_Data.GLOVEL_PRODUCT_SERCH_Variant = Product_Variant.getText().toString().trim();

                        if(Global_Data.Act_Performance.equalsIgnoreCase("product_search"))
                        {
                            Intent ss = new Intent(FilterProductSearchActivity.this,ProductAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("beat_search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,Beat_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }else if(Global_Data.Act_Performance.equalsIgnoreCase("customer_search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,Customer_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("User_Search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,User_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("Target_Search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,MainActivity.class);
//                            startActivity(ss);
//                            finish();
//                        }
                    }
                }

                else
                if(spnCategory.getSelectedItem().toString().equalsIgnoreCase("Select Category")) {
//                    Toast toast = Toast.makeText(FilterProductSearchActivity.this,"Please Select Category", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(FilterProductSearchActivity.this,"Please Select Category", "yes");
                }
                else if(spnProduct.getSelectedItem().toString().equalsIgnoreCase("Select Sub Category")) {
//                    Toast toast = Toast.makeText(FilterProductSearchActivity.this,"Please Select Sub Category", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(FilterProductSearchActivity.this,"Please Select Sub Category","yes");
                }
                else if(Product_Variant.getText().toString().trim().equalsIgnoreCase("")) {
//                    Toast toast = Toast.makeText(FilterProductSearchActivity.this,"Please Select Variant", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(FilterProductSearchActivity.this,"Please Select Variant","yes");
                }
                else if(Product_Variant.getText().toString().trim().equalsIgnoreCase("All PRODUCT")) {
                    Global_Data.Act_Performance="product_search";
                    Global_Data.GLOVEL_PRODUCT_SERCH_VALUE = spnProduct.getSelectedItem().toString() +"@" +"ALL PRODUCT";
                    Global_Data.GLOVEL_PRODUCT_SERCH_Category = spnCategory.getSelectedItem().toString();
                    Global_Data.GLOVEL_PRODUCT_SERCH_Sub_Category = "";

                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(fromdate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterProductSearchActivity.this,
//                                "Please Select From Date.", Toast.LENGTH_SHORT);
//                        toast.show();
                        Global_Data.Custom_Toast(FilterProductSearchActivity.this,
                                "Please Select From Date.", "");
                    }
                    else
                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(todate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterProductSearchActivity.this,
//                                "Please Select To Date.", Toast.LENGTH_SHORT);
//
//                        toast.show();
                        Global_Data.Custom_Toast(FilterProductSearchActivity.this,
                                "Please Select To Date.","");
                    }
                    else
                    {
                        Global_Data.Globel_fromDate = fromdate.getText().toString().trim();
                        Global_Data.Globel_toDate = todate.getText().toString().trim();

                        if(Global_Data.Act_Performance.equalsIgnoreCase("product_search"))
                        {
                            Intent ss = new Intent(FilterProductSearchActivity.this,ProductAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("beat_search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,Beat_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }else if(Global_Data.Act_Performance.equalsIgnoreCase("customer_search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,Customer_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("User_Search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,User_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("Target_Search"))
//                        {
//                            Intent ss = new Intent(Product_Search.this,MainActivity.class);
//                            startActivity(ss);
//                            finish();
//                        }
                    }

//                    Intent intent = new Intent(Product_Search.this, Customer_DatePIcker.class);
//                    startActivity(intent);
                }
                else
                {
                    Global_Data.GLOVEL_PRODUCT_SERCH_VALUE =Product_Variant.getText().toString().trim() +"@" +"VARIANT";

                    Global_Data.GLOVEL_PRODUCT_SERCH_Category = spnCategory.getSelectedItem().toString();
                    Global_Data.GLOVEL_PRODUCT_SERCH_Sub_Category = spnProduct.getSelectedItem().toString();
                    Global_Data.GLOVEL_PRODUCT_SERCH_Variant = Product_Variant.getText().toString().trim();

                    Global_Data.Act_Performance="product_search";

//                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(fromdate.getText().toString().trim()))){
//
//                        Toast toast = Toast.makeText(Product_Search.this,
//                                "Please Select From Date.", Toast.LENGTH_SHORT);
//                        toast.show();
//                    }
//                    else
//                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(todate.getText().toString().trim()))){
//
//                        Toast toast = Toast.makeText(Product_Search.this,
//                                "Please Select To Date.", Toast.LENGTH_SHORT);
//
//                        toast.show();
//                    }
//                    else
//                    {
//                        Global_Data.Globel_fromDate = fromdate.getText().toString().trim();
//                        Global_Data.Globel_toDate = todate.getText().toString().trim();

                    if(Global_Data.Act_Performance.equalsIgnoreCase("product_search"))
                    {
                        Intent ss = new Intent(FilterProductSearchActivity.this,ProductAnalysisActivity.class);
                        startActivity(ss);
                        finish();
                    }
//                    else if(Global_Data.Act_Performance.equalsIgnoreCase("beat_search"))
//                    {
//                        Intent ss = new Intent(Product_Search.this,Beat_Performance.class);
//                        startActivity(ss);
//                        finish();
//                    }else if(Global_Data.Act_Performance.equalsIgnoreCase("customer_search"))
//                    {
//                        Intent ss = new Intent(Product_Search.this,Customer_Performance.class);
//                        startActivity(ss);
//                        finish();
//                    }
//                    else if(Global_Data.Act_Performance.equalsIgnoreCase("User_Search"))
//                    {
//                        Intent ss = new Intent(Product_Search.this,User_Performance.class);
//                        startActivity(ss);
//                        finish();
//                    }
//                    else if(Global_Data.Act_Performance.equalsIgnoreCase("Target_Search"))
//                    {
//                        Intent ss = new Intent(Product_Search.this,MainActivity.class);
//                        startActivity(ss);
//                        finish();
//                    }
                    // }

//                    Intent intent = new Intent(Product_Search.this, Customer_DatePIcker.class);
//                    startActivity(intent);

                }
            }
        });

        //Reading all
        //	spnCategory.setOnItemSelectedListener(NewOrderActivity.this);

        results.add("Select Product");
        adapter_subcategory = new ArrayAdapter<String>(this, R.layout.spinner_item, results);
        adapter_subcategory.setDropDownViewResource(R.layout.spinner_item);
        spnProduct.setAdapter(adapter_subcategory);

//

        results2.clear();
        // results2.add("ALL PRODUCT");
        //result_product.add("ALL PRODUCT");
        List<Local_Data> contacts2 = dbvoc.getAllVariant();
        for (Local_Data cn : contacts2) {
            results2.add(cn.getStateName());
           // results2.sort(String::compareToIgnoreCase);
         //   Collections.sort(results2);

            result_product.add(cn.getStateName());
          //  Collections.sort(result_product);

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                results2);
        Product_Variant.setThreshold(1);// will start working from
        // first character
        Product_Variant.setAdapter(adapter);// setting the adapter
        // data into the
        // AutoCompleteTextView
        //Product_Variant.setTextColor(Color.BLACK);

        Product_Variant.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (Product_Variant.getRight() - Product_Variant.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        View view = FilterProductSearchActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        //autoCompleteTextView1.setText("");
                        Product_Variant.showDropDown();
                        return true;
                    }
                }
                return false;
            }
        });

        Product_Variant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {

//                List<Local_Data> cont = dbvoc.getProductByCat(Product_Variant.getText().toString().trim());
//                //results2.add("Select Variant");
//                for (Local_Data cn1 : cont) {
//                    String str_var = ""+cn1.getStateName();
//                    str_var1 = ""+cn1.getMRP();
//                    String str_var2 = ""+cn1.get_Description();
//                    String str_var3 = ""+cn1.get_Claims();
//                  //  Global_Data.amnt= ""+cn1.get_Description();
//                   // Global_Data.amnt1= ""+cn1.get_Claims();
//
//                    categ_name = cn1.getCategory();
//                    subcateg_name = cn1.getSubcateg();
//
//
//
//                }

                txt_data.setText(spnCategory.getSelectedItem().toString()+", "+spnCategory.getSelectedItem().toString()+", "+Product_Variant.getText().toString().trim());

//                adapter_category.setDropDownViewResource(R.layout.spinner_item);
//                spnCategory.setAdapter(adapter_category);
//                //spnCategory.setOnItemSelectedListener(NewOrderActivity.this);
//
//                adapter_subcategory.setDropDownViewResource(R.layout.spinner_item);
//                spnProduct.setAdapter(adapter_subcategory);
//                //spnProduct.setOnItemSelectedListener(NewOrderActivity.this);
//
//
//
//                if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(categ_name)){
//                    int spinnerPosition = adapter_category.getPosition(categ_name);
//                    spnCategory.setSelection(spinnerPosition);
//                }
            }
        });

        spnProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                check_product=check_product+1;
                if(check_product>1)
                {

                    if (spnCategory.getSelectedItem().toString().equalsIgnoreCase("Select Category") && !(parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase("Select All")))
                    {

                        //Toast.makeText(getApplicationContext(), "Please Select Category", Toast.LENGTH_LONG).show();

//                        Toast toast = Toast.makeText(getApplicationContext(),"Please Select Category",Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(getApplicationContext(),"Please Select Category","yes");
                        spnCategory.setClickable(true);
                        spnCategory.setEnabled(true);
                        //Product_Variant.setText("");
                        Product_Variant.setClickable(true);
                        Product_Variant.setEnabled(true);

                    }
                    else
                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase("Select Sub Category"))
                    {
                        categ_name = "";
                        subcateg_name = "";
                        results2.add("");
                        results2.clear();
                        txt_data.setText("");

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FilterProductSearchActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                result_product);
                        Product_Variant.setThreshold(1);// will start working from
                        // first character
                        Product_Variant.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        //Product_Variant.setTextColor(Color.WHITE);
                        Product_Variant.setText("");
                    }
                    else
                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase("Select All")) {

                        txt_data.setText("");
                        // int spinnerPosition = adapter_category.getPosition("Select Category");
                        //spnCategory.setSelection(spinnerPosition);
                        // spnCategory.setClickable(false);
                        //spnCategory.setEnabled(false);
                        Product_Variant.setText("");
                        Product_Variant.setClickable(false);
                        Product_Variant.setEnabled(false);

                    }
                    else
                    {

                        spnCategory.setClickable(true);
                        spnCategory.setEnabled(true);
                        //Product_Variant.setText("");
                        Product_Variant.setClickable(true);
                        Product_Variant.setEnabled(true);
                        results2.clear();

                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent)
                        {
                            dialog = new ProgressDialog(FilterProductSearchActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                            dialog.setMessage("Please wait variants Loading....");
                            dialog.setTitle("Metal App");
                            dialog.setCancelable(false);
                            dialog.show();

                            Variant_details(parent.getItemAtPosition(pos).toString().trim());
                        }
                        else
                        {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "You don't have internet connection.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
//                        results2.add("ALL PRODUCT");
//                        String c_id = "";
//
//                        List<Local_Data> contacts33 = dbvoc.HSS_DescriptionITEM1_IDD(parent.getItemAtPosition(pos).toString().trim());
//                        for (Local_Data cn : contacts33)
//                        {
//                            c_id = cn.getCust_Code();
//                        }
//
//                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(c_id)) {
//
//                            //List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_ID(Global_Data.GLOVEL_PRODUCT_ID);
//                            List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_BYNAME(spnCategory.getSelectedItem().toString().trim(),parent.getItemAtPosition(pos).toString().trim());
//                            // results2.add("Select Variant");
//                            for (Local_Data cn : contacts3)
//                            {
//                                str_variant = ""+cn.getStateName();
//
//
//                                results2.add(str_variant);
//
//
//
//                            }
//
//                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Product_Search.this,android.R.layout.simple_spinner_dropdown_item,results2);
//                            Product_Variant.setThreshold(1);// will start working from
//                            // first character
//                            Product_Variant.setAdapter(adapter);// setting the adapter
//                            // data into the
//                            // AutoCompleteTextView
//                            Product_Variant.setTextColor(Color.WHITE);
//
//
//                        }

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {

                check=check+1;
                if(check>2)
                {
                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase("Select Category")) {

                        categ_name = "";
                        subcateg_name = "";
                        txt_data.setText("");

                        results.clear();
                        results.add("Select Sub Category");
                        adapter_subcategory = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results);
                        adapter_subcategory.setDropDownViewResource(R.layout.spinner_item);
                        spnProduct.setAdapter(adapter_subcategory);

                        results2.clear();
                        results2.add("Select Variant");
                        adapter_varient = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results2);
                        adapter_varient.setDropDownViewResource(R.layout.spinner_item);
                        //spnProductSpec.setAdapter(adapter_state3);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FilterProductSearchActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                result_product);
                        Product_Variant.setThreshold(1);// will start working from
                        // first character
                        Product_Variant.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        //Product_Variant.setTextColor(Color.WHITE);
                        Product_Variant.setText("");



                        //Toast.makeText(getApplicationContext(), "Please Select Category", Toast.LENGTH_LONG).show();

//                        Toast toast = Toast.makeText(FilterProductSearchActivity.this, "Please Select Category", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(FilterProductSearchActivity.this, "Please Select Category","yes");

                        // txt_data.setText("");
                        // int spinnerPosition = adapter_subcategory.getPosition("Select Sub Category");
                        // spnProduct.setSelection(spinnerPosition);
                        spnProduct.setClickable(true);
                        spnProduct.setEnabled(true);
                        //Product_Variant.setText("");
                        Product_Variant.setClickable(true);
                        Product_Variant.setEnabled(true);

                    }
                    else
                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase("Select All")) {

                        txt_data.setText("");
                        int spinnerPosition = adapter_subcategory.getPosition("Select Sub Category");
                        spnProduct.setSelection(spinnerPosition);
                        spnProduct.setClickable(false);
                        spnProduct.setEnabled(false);
                        Product_Variant.setText("");
                        Product_Variant.setClickable(false);
                        Product_Variant.setEnabled(false);

                    }
                    else
                    {
                        spnProduct.setClickable(true);
                        spnProduct.setEnabled(true);
                        //Product_Variant.setText("");
                        Product_Variant.setClickable(true);
                        Product_Variant.setEnabled(true);
//                        List<Local_Data> contacts2 = dbvoc.HSS_DescriptionITEM_ID(parent.getItemAtPosition(pos).toString().trim());
//                        //results.add("Select Product");
//                        for (Local_Data cn : contacts2)
//                        {
//                            //Global_Data.GLOVEL_CATEGORY_ID = cn.getCust_Code();
//
//                        }

                        results.clear();

                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent)
                        {
                            dialog = new ProgressDialog(FilterProductSearchActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                            dialog.setMessage("Please wait Sub Categories Loading....");
                            dialog.setTitle("Metal App");
                            dialog.setCancelable(false);
                            dialog.show();

                            Subcategory_details(parent.getItemAtPosition(pos).toString().trim());
                        }
                        else
                        {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "You don't have internet connection.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        //List<Local_Data> contacts22 = dbvoc.HSS_DescriptionITEM1_ID(Global_Data.GLOVEL_CATEGORY_ID);


                        if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(subcateg_name)){
                            int spinnerPosition = adapter_subcategory.getPosition(subcateg_name);
                            spnProduct.setSelection(spinnerPosition);
                        }
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        finish();


    }

    //    @Override
    public void onResume() {
        // TODO Auto-generated method stubs
        super.onResume();


    }


    public void category_details()
    {
        SharedPreferences spf = FilterProductSearchActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        String domain = getResources().getString(R.string.service_domain1);

        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);
        Log.i("user list url", "user list url " + domain + "metal/api/performance/v1/products/populate_primary_category?imei_no=" + Global_Data.imei_no + "&email=" + user_email+"&type="+"product_performance");

        StringRequest jsObjRequest = null;

        jsObjRequest = new StringRequest(domain + "metal/api/performance/v1/products/populate_primary_category?imei_no=" + Global_Data.imei_no + "&email=" + user_email+"&type="+"product_performance", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;

                new FilterProductSearchActivity.getAllCategories().execute(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Network Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Network Error","");
                        } else if (error instanceof AuthFailureError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Server AuthFailureError  Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Server AuthFailureError  Error","");
                        } else if (error instanceof ServerError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Server   Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Server   Error","");
                        } else if (error instanceof NetworkError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Network   Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Network   Error","");
                        } else if (error instanceof ParseError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "ParseError   Error","");
                        } else {
                           // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(),"");
                        }
                        dialog.dismiss();
                        // finish();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        jsObjRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        requestQueue.add(jsObjRequest);
    }

//    @Override
//    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {
//        DecimalFormat mFormat= new DecimalFormat("00");
//        String date = mFormat.format(Double.valueOf(Day)) + "-" + mFormat.format(Double.valueOf(Month+1)) + "-" + Year;
//
//        // Toast.makeText(Customer_DatePIcker.this, date, Toast.LENGTH_LONG).show();
//
//        if(click_detect_flag.equalsIgnoreCase("from_date"))
//        {
//            // fromdate.setVisibility(View.VISIBLE);
//            fromdate.setText(date);
//        }
//        else
//        if(click_detect_flag.equalsIgnoreCase("to_date"))
//        {
//            // todatevalue.setVisibility(View.VISIBLE);
//            todate.setText(date);
//        }
//    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    private class getAllCategories extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {


            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("result")) {
                    response_result = response.getString("result");
                } else {
                    response_result = "data";
                }


                if (response_result.equalsIgnoreCase("User doesn't exist")) {


                    //Toast.makeText(Order.this, response_result, Toast.LENGTH_LONG).show();

                    FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterProductSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterProductSearchActivity.this, response_result,"yes");
                        }
                    });


                }
                else
                if(response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterProductSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterProductSearchActivity.this, response_result,"yes");
                        }
                    });



                }
                else {

                    //dbvoc.getDeleteTable("delivery_products");

                    JSONArray primary_categories = response.getJSONArray("primary_categories");
                    Log.i("volley", "response primary_categories Length: " + primary_categories.length());
                    Log.d("volley", "primary_categories" + primary_categories.toString());


                    //
                    if (primary_categories.length() <= 0) {

                        FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                                //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(FilterProductSearchActivity.this, "primary categories doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(FilterProductSearchActivity.this, "primary categories doesn't exist","yes");
                            }
                        });
                    } else {

                        results1.clear();
                        results1.add("Select Category");
                        if (primary_categories.length() > 1)
                        {
                            results1.add("Select All");
                        }

                        for (int i = 0; i < primary_categories.length(); i++) {

                            try {
                                if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(primary_categories.getString(i))){
                                    {
                                        results1.add(primary_categories.getString(i));
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                        }

                        FilterProductSearchActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {

                                adapter_category = new ArrayAdapter<String>(FilterProductSearchActivity.this, R.layout.spinner_item, results1);
                                adapter_category.setDropDownViewResource(R.layout.spinner_item);
                                spnCategory.setAdapter(adapter_category);
                                dialog.dismiss();
                            }
                        });

                        FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                            }
                        });
                        //	dialog.dismiss();

                        //finish();

                    }


                    // }

                    // output.setText(data);
                } }catch (JSONException e) {
                e.printStackTrace();


                FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

            }


            FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void Subcategory_details(String primary_category)
    {
        String domain = getResources().getString(R.string.service_domain1);
        StringRequest jsObjRequest = null;

        SharedPreferences spf = FilterProductSearchActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);


        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);
        try {
            Log.i("user list url", "user list url " + domain + "metal/api/performance/v1/products/populate_sub_category?imei_no=" + Global_Data.imei_no +"&email=" + user_email+"&primary_category=" + URLEncoder.encode(primary_category, "UTF-8")+"&type="+"product_performance");




            jsObjRequest = new StringRequest(domain + "metal/api/performance/v1/products/populate_sub_category?imei_no=" + Global_Data.imei_no + "&email=" + user_email+"&primary_category=" + URLEncoder.encode(primary_category, "UTF-8")+"&type="+"product_performance", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.i("volley", "response: " + response);
                    final_response = response;

                    new getAllSubCategories().execute(response);

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Network Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "Network Error","");
                            } else if (error instanceof AuthFailureError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Server AuthFailureError  Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "Server AuthFailureError  Error","");
                            } else if (error instanceof ServerError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Server   Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "Server   Error","");
                            } else if (error instanceof NetworkError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Network   Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "Network   Error","");
                            } else if (error instanceof ParseError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "ParseError   Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "ParseError   Error","");
                            } else {
                               // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(),"");
                            }
                            dialog.dismiss();
                            // finish();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        jsObjRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        requestQueue.add(jsObjRequest);
    }


    private class getAllSubCategories extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {


            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("result")) {
                    response_result = response.getString("result");
                } else {
                    response_result = "data";
                }


                if (response_result.equalsIgnoreCase("User doesn't exist")) {


                    //Toast.makeText(Order.this, response_result, Toast.LENGTH_LONG).show();

                    FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterProductSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterProductSearchActivity.this, response_result,"yes");
                        }
                    });


                }
                else
                if(response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterProductSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterProductSearchActivity.this, response_result,"");
                        }
                    });



                }
                else {

                    //dbvoc.getDeleteTable("delivery_products");

                    JSONArray sub_categories = response.getJSONArray("sub_categories");
                    Log.i("volley", "response sub_categories Length: " + sub_categories.length());
                    Log.d("volley", "sub_categories" + sub_categories.toString());


                    //
                    if (sub_categories.length() <= 0) {

                        FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                                //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(FilterProductSearchActivity.this, "Sub Categories doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(FilterProductSearchActivity.this, "Sub Categories doesn't exist","yes");
                            }
                        });
                    } else {

                        results.add("Select Sub Category");
                        if (sub_categories.length() > 1)
                        {
                            results.add("Select All");
                        }

                        for (int i = 0; i < sub_categories.length(); i++) {

                            try {
                                if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(sub_categories.getString(i))){
                                    {
                                        results.add(sub_categories.getString(i));
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        FilterProductSearchActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {

                                adapter_subcategory = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results);
                                adapter_subcategory.setDropDownViewResource(R.layout.spinner_item);
                                spnProduct.setAdapter(adapter_subcategory);
                                dialog.dismiss();
                            }
                        });

                        FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                            }
                        });
                        //	dialog.dismiss();

                        //finish();

                    }


                    // }

                    // output.setText(data);
                } }catch (JSONException e) {
                e.printStackTrace();

                FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });
            }

            FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    public void Variant_details(String sub_category)
    {
        String domain = getResources().getString(R.string.service_domain1);

        SharedPreferences spf = FilterProductSearchActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        StringRequest jsObjRequest = null;
        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);

        try {
            Log.i("user list url", "user list url " + domain + "metal/api/performance/v1/products/populate_variant?imei_no=" + Global_Data.imei_no +"&email=" + user_email+"&sub_category=" + URLEncoder.encode(sub_category, "UTF-8")+"&primary_category=" + URLEncoder.encode(spnCategory.getSelectedItem().toString(), "UTF-8")+"&type="+"product_performance");

            jsObjRequest = new StringRequest(domain + "metal/api/performance/v1/products/populate_variant?imei_no=" + Global_Data.imei_no + "&email=" + user_email+"&sub_category=" + URLEncoder.encode(sub_category, "UTF-8")+"&primary_category=" + URLEncoder.encode(spnCategory.getSelectedItem().toString(), "UTF-8")+"&type="+"product_performance", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.i("volley", "response: " + response);
                    final_response = response;

                    new getAllVarients().execute(response);

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Network Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "Network Error","");
                            } else if (error instanceof AuthFailureError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Server AuthFailureError  Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "Server AuthFailureError  Error","");
                            } else if (error instanceof ServerError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Server   Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "Server   Error","");
                            } else if (error instanceof NetworkError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Network   Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "Network   Error","");
                            } else if (error instanceof ParseError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "ParseError   Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "ParseError   Error","");
                            } else {
                              //  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(),"");
                            }
                            dialog.dismiss();
                            // finish();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        jsObjRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        requestQueue.add(jsObjRequest);
    }

    private class getAllVarients extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {


            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("result")) {
                    response_result = response.getString("result");
                } else {
                    response_result = "data";
                }


                if (response_result.equalsIgnoreCase("User doesn't exist")) {


                    //Toast.makeText(Order.this, response_result, Toast.LENGTH_LONG).show();

                    FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterProductSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterProductSearchActivity.this, response_result,"yes");
                        }
                    });

                }
                else
                if(response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterProductSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterProductSearchActivity.this, response_result,"yes");
                        }
                    });



                }
                else {

                    //dbvoc.getDeleteTable("delivery_products");

                    JSONArray variants = response.getJSONArray("variants");
                    Log.i("volley", "response variants Length: " + variants.length());
                    Log.d("volley", "variants" + variants.toString());


                    //
                    if (variants.length() <= 0) {

                        FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                                //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(FilterProductSearchActivity.this, "variants doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(FilterProductSearchActivity.this, "variants doesn't exist","yes");
                            }
                        });
                    } else {

                        results2.clear();
                        for (int i = 0; i < variants.length(); i++) {

                            try {
                                if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(variants.getString(i))){
                                    {
                                        results2.add(variants.getString(i));
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        FilterProductSearchActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(FilterProductSearchActivity.this,android.R.layout.simple_spinner_dropdown_item,results2);
                                Product_Variant.setThreshold(1);// will start working from
                                // first character
                                Product_Variant.setAdapter(adapter);// setting the adapter
                                // data into the
                                // AutoCompleteTextView
                                //Product_Variant.setTextColor(Color.WHITE);
                                dialog.dismiss();
                            }
                        });

                        FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                            }
                        });
                        //	dialog.dismiss();

                        //finish();

                    }


                    // }

                    // output.setText(data);
                } }catch (JSONException e) {
                e.printStackTrace();


                FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

            }


            FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            FilterProductSearchActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void updateLabel() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        fromdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        todate.setText(sdf.format(myCalendar.getTime()));
    }

}
