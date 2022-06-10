package com.msimplelogic.activities;

/**
 * Created by sujit on 2/6/2017.
 */

        import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import androidx.coordinatorlayout.widget.CoordinatorLayout;
        import com.google.android.material.snackbar.Snackbar;

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
        import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.UnsupportedEncodingException;
        import java.lang.reflect.InvocationTargetException;
        import java.net.URLEncoder;
        import java.text.DecimalFormat;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.HashMap;
        import java.util.Locale;

        import cpm.simplelogic.helper.ConnectionDetector;

//@SuppressLint("DefaultLocale")
public class FilterCustomerSearchActivity extends Activity implements DatePickerDialog.OnDateSetListener{
    private CoordinatorLayout coordinatorLayout;
    EditText fromdate, todate;
    int check_state = 0;
    int check_city = 0;
    int check_beat = 0;
    static String final_response = "";
    String response_result = "";
    android.app.DatePickerDialog.OnDateSetListener date, date1;
    //Calendar myCalendar;
    HashMap<Integer,String> statespinnerMap = new HashMap<Integer, String>();
    HashMap<Integer,String> cityspinnerMap = new HashMap<Integer, String>();
    HashMap<Integer,String> beatspinnerMap = new HashMap<Integer, String>();
    HashMap<String,String> custumer_map = new HashMap<String, String>();

    private Toolbar toolbar;
    private String Re_Text = "";
    String items,item_beat,item_city,item_state;
    String state_name = "";
    String city_name = "";
    TextView schedule_txt;
    TextView mTitleTextView;
    String beat_name = "";
    private int drop_value = 0;
    Boolean isInternetPresent = false;
    private String c_mobile_number = "";
    ConnectionDetector cd;
    Spinner city_spinner, state_spinner, beat_spinner;
    TextView txt_data;
    String s[];
    int state_flag = 0;
    ProgressDialog dialog;
    ArrayAdapter<String> adapter_state1;
    ArrayAdapter<String> adapter_state2;
    ArrayAdapter<String> adapter_beat;
    AutoCompleteTextView autoCompleteTextView1;
    AutoCompleteTextView cust_variant;
    ArrayList<String> list_AllCustomers = new ArrayList<String>();
    ArrayList<String> list_AllCustomers_check = new ArrayList<String>();
    ArrayList<String> list_AllCustomersmain = new ArrayList<String>();

    DataBaseHelper dbvoc = new DataBaseHelper(this);
    String state_idn = "";
    String city_idn = "";
    String beat_idn = "";
    String customer_idn = "";
    String C_ID = "";
    String S_ID = "";
    String B_ID = "";
    ArrayList<String> list_cities = new ArrayList<String>();
    private ArrayList<String> results = new ArrayList<String>();
    private ArrayList<String> results1 = new ArrayList<String>();
    private ArrayList<String> results2 = new ArrayList<String>();
    private ArrayList<String> state_id = new ArrayList<String>();
    private ArrayList<String> city_id = new ArrayList<String>();
    private ArrayList<String> beat_id = new ArrayList<String>();
    private ArrayList<String> customer_id = new ArrayList<String>();
    String click_detect_flag = "";
    DatePickerDialog datePickerDialog ;
    static Calendar calendar ;
    static int Year, Month, Day ;
    private ArrayList<String> results_beat = new ArrayList<String>();
    JSONObject jsonObject;
    Button btn_custsearch;
    LoginDataBaseAdapter loginDataBaseAdapter;
    private static final String TAG = "Debug";
    Boolean userIsInteracting = false;

    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_filtercustomer_search);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
//        }
//        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);



        Global_Data.GLObalOrder_id = "";
        Global_Data.Schedule_FLAG = "";
        Global_Data.Glovel_BEAT_ID = "";
        Global_Data.Selected_USER_EMAIL = "";

        String[] arr = { "Paries,France", "PA,United States","Parana,Brazil",
                "Padua,Italy", "Pasadena,CA,United States"};


        city_spinner = (Spinner) findViewById(R.id.spnCity);
        state_spinner = (Spinner) findViewById(R.id.spnState);
        beat_spinner = (Spinner) findViewById(R.id.spnBeat);
        txt_data = (TextView) findViewById(R.id.txt_data);
        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewcustsearch);
        //cust_variant = (AutoCompleteTextView) findViewById(R.id.cust_variant);
        fromdate = (EditText) findViewById(R.id.fromdate);
        todate = (EditText)findViewById(R.id.todate);

        cd  = new ConnectionDetector(getApplicationContext());

        btn_custsearch=(Button)findViewById(R.id.button_prdsearch);

        list_AllCustomers.clear();
        list_AllCustomersmain.clear();

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                (this, R.layout.autocomplete, list_AllCustomersmain);
//
//        cust_variant.setThreshold(1);
//        cust_variant.setAdapter(adapter);

        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Global_Data.hideSoftKeyboard(FilterCustomerSearchActivity.this);

                //click_detect_flag = "from_date";

                new android.app.DatePickerDialog(FilterCustomerSearchActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

                //updateLabel();

//                datePickerDialog = DatePickerDialog.newInstance(FilterCustomerSearchActivity.this, Year, Month, Day);
//
//                datePickerDialog.setThemeDark(false);
//
//                datePickerDialog.showYearPickerFirst(false);
//
//                datePickerDialog.setAccentColor(Color.parseColor("#E43F3F"));
//
//                datePickerDialog.setTitle("Select Date");
//
//                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

            }
        });

        // Your android custom dialog ok action
        // Action for custom dialog ok button click
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Global_Data.hideSoftKeyboard(FilterCustomerSearchActivity.this);

                new android.app.DatePickerDialog(FilterCustomerSearchActivity.this, date1, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

//                click_detect_flag = "to_date";
//
//                datePickerDialog = DatePickerDialog.newInstance(FilterCustomerSearchActivity.this, Year, Month, Day);
//
//                datePickerDialog.setThemeDark(false);
//
//                datePickerDialog.showYearPickerFirst(false);
//
//                datePickerDialog.setAccentColor(Color.parseColor("#E43F3F"));
//
//                datePickerDialog.setTitle("Select Date");
//
//                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

            }
        });

        date = new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        date1 = new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }
        };

       // list_AllCustomers.clear();
        ArrayAdapter<String> adapters = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.autocomplete,
                list_AllCustomersmain);
        autoCompleteTextView1.setThreshold(1);// will start working from
        // first character
        autoCompleteTextView1.setAdapter(adapters);// setting the adapter
        // data into the
        // AutoCompleteTextView
        autoCompleteTextView1.setTextColor(Color.BLACK);


//        list_AllCustomers.clear();
//        ArrayAdapter<String> adaptersn = new ArrayAdapter<String>(getApplicationContext(),
//                R.layout.autocomplete,
//                list_AllCustomers);
//        cust_variant.setThreshold(1);// will start working from
//        // first character
//        cust_variant.setAdapter(adaptersn);// setting the adapter
//        // data into the
//        // AutoCompleteTextView
//        cust_variant.setTextColor(Color.BLACK);

        autoCompleteTextView1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (autoCompleteTextView1.getRight() - autoCompleteTextView1.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        View view = FilterCustomerSearchActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        //autoCompleteTextView1.setText("");

                        try {

                            // try code

                            autoCompleteTextView1.showDropDown();

                            return true;

                        } catch (Exception e) {

                            // generic exception handling
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });


        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                //Toast.makeText(Order.this," selected", Toast.LENGTH_LONG).show();

                // Global_Data.hideSoftKeyboard(Customer_Search.this);

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.  toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                if(autoCompleteTextView1.getText().toString().trim().equalsIgnoreCase("Select All"))
                {
                    txt_data.setText("");
                }
                else
                {
                    customer_data(autoCompleteTextView1.getText().toString().trim());
                }
            }
        });

        btn_custsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state_spinner.getSelectedItem().toString().equalsIgnoreCase("Select All")) //|| ((fromdate.getText().toString().length()>0) || (todate.getText().toString().length()>0)))
                {
                    Global_Data.Act_Performance="customer_search";
                    Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE = "STATES"+"@"+"ALL STATES";
                    Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE_NEW = "STATES"+"@"+"ALL STATES";

                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(fromdate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,
//                                "Please Select From Date.", Toast.LENGTH_SHORT);
//                        toast.show();
                        Global_Data.Custom_Toast(FilterCustomerSearchActivity.this,
                                "Please Select From Date.","");
                    }
                    else
                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(todate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,
//                                "Please Select To Date.", Toast.LENGTH_SHORT);
//
//                        toast.show();

                        Global_Data.Custom_Toast(FilterCustomerSearchActivity.this,
                                "Please Select To Date.","");
                    }
                    else
                    {
                        Global_Data.Globel_fromDate = fromdate.getText().toString().trim();
                        Global_Data.Globel_toDate = todate.getText().toString().trim();


                        if(Global_Data.Act_Performance.equalsIgnoreCase("product_search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,ProductAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("beat_search"))
//                        {
//                            Intent ss = new Intent(FilterCustomerSearchActivity.this,Beat_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
                        else if(Global_Data.Act_Performance.equalsIgnoreCase("customer_search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,CustomerAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("User_Search"))
//                        {
//                            Intent ss = new Intent(FilterCustomerSearchActivity.this,User_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
                        else if(Global_Data.Act_Performance.equalsIgnoreCase("Target_Search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,MainActivity.class);
                            startActivity(ss);
                            finish();
                        }
                    }

//                    Intent intent = new Intent(Customer_Search.this, Customer_DatePIcker.class);
//                    startActivity(intent);

                }
                else if (city_spinner.getSelectedItem().toString().equalsIgnoreCase("Select All"))
                {
                    Global_Data.Act_Performance="customer_search";
                    Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE = state_idn +"@" +"ALL CITIES";
                    Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE_NEW = state_spinner.getSelectedItem().toString()+"@"+"ALL CITIES";

                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(fromdate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,
//                                "Please Select From Date.", Toast.LENGTH_SHORT);
//                        toast.show();
                        Global_Data.Custom_Toast(FilterCustomerSearchActivity.this,
                                "Please Select From Date.","");
                    }
                    else
                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(todate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,
//                                "Please Select To Date.", Toast.LENGTH_SHORT);
//
//                        toast.show();
                        Global_Data.Custom_Toast(FilterCustomerSearchActivity.this,
                                "Please Select To Date.","");
                    }
                    else
                    {
                        Global_Data.Globel_fromDate = fromdate.getText().toString().trim();
                        Global_Data.Globel_toDate = todate.getText().toString().trim();

                        if(Global_Data.Act_Performance.equalsIgnoreCase("product_search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,ProductAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("beat_search"))
//                        {
//                            Intent ss = new Intent(FilterCustomerSearchActivity.this,Beat_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
                        else if(Global_Data.Act_Performance.equalsIgnoreCase("customer_search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,CustomerAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("User_Search"))
//                        {
//                            Intent ss = new Intent(FilterCustomerSearchActivity.this,User_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
                        else if(Global_Data.Act_Performance.equalsIgnoreCase("Target_Search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,MainActivity.class);
                            startActivity(ss);
                            finish();
                        }
                    }

//                    Intent intent = new Intent(Customer_Search.this, Customer_DatePIcker.class);
//                    startActivity(intent);
                }
                else if(beat_spinner.getSelectedItem().toString().equalsIgnoreCase("Select All"))
                {
                    Global_Data.Act_Performance="customer_search";
                    Global_Data.GLOVEL_CUSTOMER_SERCH_State = state_idn;
                    Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE = city_idn +"@" +"ALL BEATS";
                    Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE_NEW = city_spinner.getSelectedItem().toString()+"@"+"ALL BEATS";

                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(fromdate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,
//                                "Please Select From Date.", Toast.LENGTH_SHORT);
//                        toast.show();
                        Global_Data.Custom_Toast(FilterCustomerSearchActivity.this,
                                "Please Select From Date.","");
                    }
                    else
                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(todate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,
//                                "Please Select To Date.", Toast.LENGTH_SHORT);
//
//                        toast.show();
                        Global_Data.Custom_Toast(FilterCustomerSearchActivity.this,
                                "Please Select To Date.", "");
                    }
                    else
                    {
                        Global_Data.Globel_fromDate = fromdate.getText().toString().trim();
                        Global_Data.Globel_toDate = todate.getText().toString().trim();

                        if(Global_Data.Act_Performance.equalsIgnoreCase("product_search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,ProductAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("beat_search"))
//                        {
//                            Intent ss = new Intent(FilterCustomerSearchActivity.this,Beat_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
                        else if(Global_Data.Act_Performance.equalsIgnoreCase("customer_search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,CustomerAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("User_Search"))
//                        {
//                            Intent ss = new Intent(FilterCustomerSearchActivity.this,User_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
                        else if(Global_Data.Act_Performance.equalsIgnoreCase("Target_Search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,MainActivity.class);
                            startActivity(ss);
                            finish();
                        }
                    }

//                    Intent intent = new Intent(Customer_Search.this, Customer_DatePIcker.class);
//                    startActivity(intent);
                }else if((fromdate.getText().toString().length()>0) || (todate.getText().toString().length()>0))
                {
                    Global_Data.Act_Performance="customer_search";
                    Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE = "STATES"+"@"+"ALL STATES";
                    Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE_NEW = "STATES"+"@"+"ALL STATES";

                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(fromdate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,
//                                "Please Select From Date.", Toast.LENGTH_SHORT);
//                        toast.show();
                        Global_Data.Custom_Toast(FilterCustomerSearchActivity.this,
                                "Please Select From Date.","");
                    }
                    else
                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(todate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,
//                                "Please Select To Date.", Toast.LENGTH_SHORT);
//
//                        toast.show();
                        Global_Data.Custom_Toast(FilterCustomerSearchActivity.this,
                                "Please Select To Date.","");
                    }
                    else
                    {
                        Global_Data.Globel_fromDate = fromdate.getText().toString().trim();
                        Global_Data.Globel_toDate = todate.getText().toString().trim();

                        Global_Data.GLOVEL_CUSTOMER_State = state_spinner.getSelectedItem().toString();
                        Global_Data.GLOVEL_CUSTOMER_City = city_spinner.getSelectedItem().toString();
                        Global_Data.GLOVEL_CUSTOMER_Beat = beat_spinner.getSelectedItem().toString();

                        Global_Data.GLOVEL_CUSTOMER_SERCH_City = city_idn;
                        Global_Data.GLOVEL_CUSTOMER_SERCH_State = state_idn;
                        Global_Data.GLOVEL_CUSTOMER_SERCH_Beat = beat_idn;

                        //Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE_NEW = autoCompleteTextView1.getText().toString().trim();
                        Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE_NEW = autoCompleteTextView1.getText().toString().trim()+"@"+"CUSTOMER";

                        if(Global_Data.Act_Performance.equalsIgnoreCase("product_search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,ProductAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("beat_search"))
//                        {
//                            Intent ss = new Intent(FilterCustomerSearchActivity.this,Beat_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
                        else if(Global_Data.Act_Performance.equalsIgnoreCase("customer_search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,CustomerAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("User_Search"))
//                        {
//                            Intent ss = new Intent(FilterCustomerSearchActivity.this,User_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
                        else if(Global_Data.Act_Performance.equalsIgnoreCase("Target_Search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,MainActivity.class);
                            startActivity(ss);
                            finish();
                        }
                    }
                }
                else
                if(state_spinner.getSelectedItem().toString().equalsIgnoreCase("Select State")) {
//                    Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,"Please Select State", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(FilterCustomerSearchActivity.this,"Please Select State","yes");
                }
                else if(city_spinner.getSelectedItem().toString().equalsIgnoreCase("Select City")) {
//                    Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,"Please Select Sub City", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(FilterCustomerSearchActivity.this,"Please Select Sub City","yes");
                }
                else if(beat_spinner.getSelectedItem().toString().equalsIgnoreCase("Select Beat")) {
//                    Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,"Please Select Beat", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(FilterCustomerSearchActivity.this,"Please Select Beat","yes");
                }
                else if(autoCompleteTextView1.getText().toString().trim().equalsIgnoreCase("")) {
//                    Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,"Please Select Customer", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(FilterCustomerSearchActivity.this,"Please Select Customer", "yes");
                }
                else if(autoCompleteTextView1.getText().toString().trim().equalsIgnoreCase("Select All")) {
                    Global_Data.Act_Performance="customer_search";
                    Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE = beat_idn +"@" +"ALL CUSTOMERS";
                    Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE_NEW = beat_spinner.getSelectedItem().toString()+"@"+"ALL CUSTOMERS";
                    Global_Data.GLOVEL_CUSTOMER_SERCH_City = city_idn;
                    Global_Data.GLOVEL_CUSTOMER_SERCH_State = state_idn;

                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(fromdate.getText().toString().trim()))){

                        Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,
                                "Please Select From Date.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(todate.getText().toString().trim()))){

//                        Toast toast = Toast.makeText(FilterCustomerSearchActivity.this,
//                                "Please Select To Date.", Toast.LENGTH_SHORT);
//                        toast.show();
                        Global_Data.Custom_Toast(FilterCustomerSearchActivity.this,
                                "Please Select To Date.", "");
                    }
                    else
                    {
                        Global_Data.Globel_fromDate = fromdate.getText().toString().trim();
                        Global_Data.Globel_toDate = todate.getText().toString().trim();

                        if(Global_Data.Act_Performance.equalsIgnoreCase("product_search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,ProductAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("beat_search"))
//                        {
//                            Intent ss = new Intent(FilterCustomerSearchActivity.this,Beat_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
                        else if(Global_Data.Act_Performance.equalsIgnoreCase("customer_search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,CustomerAnalysisActivity.class);
                            startActivity(ss);
                            finish();
                        }
//                        else if(Global_Data.Act_Performance.equalsIgnoreCase("User_Search"))
//                        {
//                            Intent ss = new Intent(FilterCustomerSearchActivity.this,User_Performance.class);
//                            startActivity(ss);
//                            finish();
//                        }
                        else if(Global_Data.Act_Performance.equalsIgnoreCase("Target_Search"))
                        {
                            Intent ss = new Intent(FilterCustomerSearchActivity.this,MainActivity.class);
                            startActivity(ss);
                            finish();
                        }
                    }
//                    Intent intent = new Intent(Customer_Search.this, Customer_DatePIcker.class);
//                    startActivity(intent);
                }
                else
                {
                    Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE =customer_idn +"@" +"CUSTOMER";
                    Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE_NEW = autoCompleteTextView1.getText().toString().trim()+"@"+"CUSTOMER";

                    Global_Data.GLOVEL_CUSTOMER_SERCH_City = city_idn;
                    Global_Data.GLOVEL_CUSTOMER_SERCH_State = state_idn;
                    Global_Data.GLOVEL_CUSTOMER_SERCH_Beat = beat_idn;

                    Global_Data.Act_Performance="customer_search";

//                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(fromdate.getText().toString().trim()))){
//
//                        Toast toast = Toast.makeText(Customer_Search.this,
//                                "Please Select From Date.", Toast.LENGTH_SHORT);
//                        toast.show();
//                    }
//                    else
//                    if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(todate.getText().toString().trim()))){
//
//                        Toast toast = Toast.makeText(Customer_Search.this,
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
                        Intent ss = new Intent(FilterCustomerSearchActivity.this,ProductAnalysisActivity.class);
                        startActivity(ss);
                        finish();
                    }
//                    else if(Global_Data.Act_Performance.equalsIgnoreCase("beat_search"))
//                    {
//                        Intent ss = new Intent(FilterCustomerSearchActivity.this,Beat_Performance.class);
//                        startActivity(ss);
//                        finish();
//                    }
                    else if(Global_Data.Act_Performance.equalsIgnoreCase("customer_search"))
                    {
                        Intent ss = new Intent(FilterCustomerSearchActivity.this,CustomerAnalysisActivity.class);
                        startActivity(ss);
                        finish();
                    }
//                    else if(Global_Data.Act_Performance.equalsIgnoreCase("User_Search"))
//                    {
//                        Intent ss = new Intent(FilterCustomerSearchActivity.this,User_Performance.class);
//                        startActivity(ss);
//                        finish();
//                    }
                    else if(Global_Data.Act_Performance.equalsIgnoreCase("Target_Search"))
                    {
                        Intent ss = new Intent(FilterCustomerSearchActivity.this,MainActivity.class);
                        startActivity(ss);
                        finish();
                    }
                    // }

//                    Intent intent = new Intent(Customer_Search.this, Customer_DatePIcker.class);
//                    startActivity(intent);
                }
            }
        });

        Global_Data.GLOVEL_PREVIOUS_ORDER_FLAG = "";

        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        Global_Data.GLOvel_GORDER_ID = "";
        Global_Data.GLOVEL_LONG_DESC = "";
        Global_Data.GLOVEL_CATEGORY_SELECTION = "";

        results.clear();
        results.add("Select City");
        results1.add("Select State");
        adapter_state1 = new ArrayAdapter<String>(FilterCustomerSearchActivity.this,
                android.R.layout.simple_spinner_item, results1);
        adapter_state1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_spinner.setAdapter(adapter_state1);

        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
            dialog = new ProgressDialog(FilterCustomerSearchActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Please wait States Loading....");
            dialog.setTitle("Metal App");
            dialog.setCancelable(false);
            dialog.show();

            states_details();
        }
        else
        {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "You don't have internet connection.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        results.add("Select City");

        adapter_state2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, results);
        adapter_state2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_spinner.setAdapter(adapter_state2);


        results_beat.add("Select Beat");
        adapter_beat = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, results_beat);
        adapter_beat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        beat_spinner.setAdapter(adapter_beat);

        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                check_state=check_state+1;
                if(check_state>2)
                {

                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase("Select State")) {

                        txt_data.setText("");

                        results.clear();
                        results.add("Select City");
                        adapter_state2 = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_item, results);
                        adapter_state2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        city_spinner.setAdapter(adapter_state2);


                        results_beat.clear();
                        results_beat.add("Select Beat");
                        adapter_beat = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_item, results_beat);
                        adapter_beat.setDropDownViewResource( R.layout.spinner_item);
                        beat_spinner.setAdapter(adapter_beat);
                        //spnProductSpec.setAdapter(adapter_state3);

                        autoCompleteTextView1.setText("");
                        list_AllCustomers.clear();
                        ArrayAdapter<String> adapters = new ArrayAdapter<String>(getApplicationContext(),
                                R.layout.autocomplete,
                                list_AllCustomersmain);
                        autoCompleteTextView1.setThreshold(1);// will start working from
                        // first character
                        autoCompleteTextView1.setAdapter(adapters);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        autoCompleteTextView1.setTextColor(Color.BLACK);
                        txt_data.setText("");

                        //Toast.makeText(getApplicationContext(), "Please Select Category", Toast.LENGTH_LONG).show();

//                        Toast toast = Toast.makeText(getApplicationContext(), "Please Select State", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                        Global_Data.Custom_Toast(getApplicationContext(), "Please Select State","yes");
                        // txt_data.setText("");
                        // int spinnerPosition = adapter_subcategory.getPosition("Select Sub Category");
                        // spnProduct.setSelection(spinnerPosition);
                        city_spinner.setClickable(true);
                        city_spinner.setEnabled(true);
                        //Product_Variant.setText("");
                        beat_spinner.setClickable(true);
                        beat_spinner.setEnabled(true);

                    }
                    else
                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase("Select All")) {

                        txt_data.setText("");
                        int spinnerPosition = adapter_state2.getPosition("Select Sub Category");
                        city_spinner.setSelection(spinnerPosition);
                        city_spinner.setClickable(false);
                        city_spinner.setEnabled(false);
                        autoCompleteTextView1.setText("");
                        beat_spinner.setClickable(false);
                        beat_spinner.setEnabled(false);
                        autoCompleteTextView1.setClickable(false);
                        autoCompleteTextView1.setEnabled(false);
                        txt_data.setText("");

                    }
                    else
                    {
                        txt_data.setText("");
                        city_spinner.setClickable(true);
                        city_spinner.setEnabled(true);
                        //Product_Variant.setText("");
                        autoCompleteTextView1.setClickable(true);
                        autoCompleteTextView1.setEnabled(true);
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
                            dialog = new ProgressDialog(FilterCustomerSearchActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                            dialog.setMessage("Please wait Cities Loading....");
                            dialog.setTitle("Metal App");
                            dialog.setCancelable(false);
                            dialog.show();

                            city_details(parent.getItemAtPosition(pos).toString().trim());
                        }
                        else
                        {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "You don't have internet connection.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        //List<Local_Data> contacts22 = dbvoc.HSS_DescriptionITEM1_ID(Global_Data.GLOVEL_CATEGORY_ID);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                check_city=check_city+1;
                if(check_city>2)
                {

                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase("Select City")) {

                        txt_data.setText("");

                        results_beat.clear();
                        results_beat.add("Select Beat");
                        adapter_beat = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_item, results_beat);
                        adapter_beat.setDropDownViewResource(R.layout.spinner_item);
                        beat_spinner.setAdapter(adapter_beat);
                        //spnProductSpec.setAdapter(adapter_state3);

                        autoCompleteTextView1.setText("");
                        list_AllCustomers.clear();
                        ArrayAdapter<String> adapters = new ArrayAdapter<String>(getApplicationContext(),
                                R.layout.autocomplete,
                                list_AllCustomersmain);
                        autoCompleteTextView1.setThreshold(1);// will start working from
                        // first character
                        autoCompleteTextView1.setAdapter(adapters);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        autoCompleteTextView1.setTextColor(Color.BLACK);



                        //Toast.makeText(getApplicationContext(), "Please Select Category", Toast.LENGTH_LONG).show();

//                        Toast toast = Toast.makeText(getApplicationContext(), "Please Select City", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                        Global_Data.Custom_Toast(getApplicationContext(), "Please Select City","yes");
                        // txt_data.setText("");
                        // int spinnerPosition = adapter_subcategory.getPosition("Select Sub Category");
                        // spnProduct.setSelection(spinnerPosition);
                        city_spinner.setClickable(true);
                        city_spinner.setEnabled(true);
                        //Product_Variant.setText("");
                        beat_spinner.setClickable(true);
                        beat_spinner.setEnabled(true);

                    }
                    else
                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase("Select All")) {

                        txt_data.setText("");
                        int spinnerPosition = adapter_beat.getPosition("Select Beat");
                        beat_spinner.setSelection(spinnerPosition);
                        beat_spinner.setClickable(false);
                        beat_spinner.setEnabled(false);
                        autoCompleteTextView1.setText("");
                        autoCompleteTextView1.setClickable(false);
                        autoCompleteTextView1.setEnabled(false);

                    }
                    else
                    {
                        txt_data.setText("");
                        beat_spinner.setClickable(true);
                        beat_spinner.setEnabled(true);
                        //Product_Variant.setText("");
                        autoCompleteTextView1.setClickable(true);
                        autoCompleteTextView1.setEnabled(true);
//                        List<Local_Data> contacts2 = dbvoc.HSS_DescriptionITEM_ID(parent.getItemAtPosition(pos).toString().trim());
//                        //results.add("Select Product");
//                        for (Local_Data cn : contacts2)
//                        {
//                            //Global_Data.GLOVEL_CATEGORY_ID = cn.getCust_Code();
//
//                        }

                        // results.clear();

                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent)
                        {
                            dialog = new ProgressDialog(FilterCustomerSearchActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                            dialog.setMessage("Please wait Beats Loading....");
                            dialog.setTitle("Metal App");
                            dialog.setCancelable(false);
                            dialog.show();

                            beat_details(parent.getItemAtPosition(pos).toString().trim());
                        }
                        else
                        {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "You don't have internet connection.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        //List<Local_Data> contacts22 = dbvoc.HSS_DescriptionITEM1_ID(Global_Data.GLOVEL_CATEGORY_ID);


                    }

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        beat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                check_beat=check_beat+1;
                if(check_beat>2)
                {

                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase("Select Beat")) {

                        txt_data.setText("");

                        autoCompleteTextView1.setText("");
                        list_AllCustomers.clear();
                        ArrayAdapter<String> adapters = new ArrayAdapter<String>(getApplicationContext(),
                                R.layout.autocomplete,
                                list_AllCustomersmain);
                        autoCompleteTextView1.setThreshold(1);// will start working from
                        // first character
                        autoCompleteTextView1.setAdapter(adapters);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        autoCompleteTextView1.setTextColor(Color.BLACK);



                        //Toast.makeText(getApplicationContext(), "Please Select Category", Toast.LENGTH_LONG).show();

//                        Toast toast = Toast.makeText(getApplicationContext(), "Please Select Beat", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                        Global_Data.Custom_Toast(getApplicationContext(), "Please Select Beat", "yes");
                        // txt_data.setText("");
                        // int spinnerPosition = adapter_subcategory.getPosition("Select Sub Category");
                        // spnProduct.setSelection(spinnerPosition);
                        city_spinner.setClickable(true);
                        city_spinner.setEnabled(true);
                        //Product_Variant.setText("");
                        beat_spinner.setClickable(true);
                        beat_spinner.setEnabled(true);

                    }
                    else
                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase("Select All")) {

                        txt_data.setText("");
                        autoCompleteTextView1.setText("");
                        autoCompleteTextView1.setClickable(false);
                        autoCompleteTextView1.setEnabled(false);

                    }
                    else
                    {
                        beat_spinner.setClickable(true);
                        beat_spinner.setEnabled(true);
                        //Product_Variant.setText("");
                        autoCompleteTextView1.setClickable(true);
                        autoCompleteTextView1.setEnabled(true);
//                        List<Local_Data> contacts2 = dbvoc.HSS_DescriptionITEM_ID(parent.getItemAtPosition(pos).toString().trim());
//                        //results.add("Select Product");
//                        for (Local_Data cn : contacts2)
//                        {
//                            //Global_Data.GLOVEL_CATEGORY_ID = cn.getCust_Code();
//
//                        }

                        //results_beat.clear();

                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent)
                        {
                            dialog = new ProgressDialog(FilterCustomerSearchActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                            dialog.setMessage("Please wait Customers Loading....");
                            dialog.setTitle("Metal App");
                            dialog.setCancelable(false);
                            dialog.show();

                            customer_details(parent.getItemAtPosition(pos).toString().trim());
                        }
                        else
                        {
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "You don't have internet connection.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        //List<Local_Data> contacts22 = dbvoc.HSS_DescriptionITEM1_ID(Global_Data.GLOVEL_CATEGORY_ID);


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

    public void states_details()
    {
        SharedPreferences spf = FilterCustomerSearchActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        String domain = getResources().getString(R.string.service_domain1);

        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);
        Log.i("user list url", "user list url " + domain + "metal/api/performance/v1/states?email=" + user_email);

        StringRequest jsObjRequest = null;

        jsObjRequest = new StringRequest(domain + "metal/api/performance/v1/states?email=" + user_email, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;

                new FilterCustomerSearchActivity.getAllSates().execute(response);

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
                         //  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {

        DecimalFormat mFormat= new DecimalFormat("00");
        String date = mFormat.format(Double.valueOf(Day)) + "-" + mFormat.format(Double.valueOf(Month+1)) + "-" + Year;

        // Toast.makeText(Customer_DatePIcker.this, date, Toast.LENGTH_LONG).show();

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
    }

    private class getAllSates extends AsyncTask<String, Void, String> {
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

                    FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, response_result,"yes");
                        }
                    });

                }
                else
                if(response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, response_result,"yes");
                        }
                    });

                }
                else {

                    //dbvoc.getDeleteTable("delivery_products");

                    JSONArray states = response.getJSONArray("states");
                    Log.i("volley", "response states Length: " + states.length());
                    Log.d("volley", "states" + states.toString());


                    //
                    if (states.length() <= 0) {

                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                                //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, "States doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, "States doesn't exist","yes");
                            }
                        });
                    } else {

                        results1.clear();
                        state_id.clear();
                        results1.add("Select State");
                        if (states.length() > 1)
                        {
                            results1.add("Select All");
                        }

                        for (int i = 0; i < states.length(); i++) {

                            JSONObject jsonObject = states.getJSONObject(i);

                            try {
                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(states.getString(i))) {
                                    {
                                        results1.add(jsonObject.getString("name"));
                                        state_id.add(jsonObject.getString("id"));
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        statespinnerMap.clear();

                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {

                                String[] spinnerArray = new String[state_id.size()];
                                for (int i = 0; i < state_id.size(); i++)
                                {
                                    statespinnerMap.put(i,state_id.get(i));
                                    //spinnerArray[i] = results1.get(i);
                                }

                                adapter_state1 = new ArrayAdapter<String>(FilterCustomerSearchActivity.this,
                                        android.R.layout.simple_spinner_item, results1);
                                adapter_state1.setDropDownViewResource(R.layout.spinner_item);
                                state_spinner.setAdapter(adapter_state1);

                                dialog.dismiss();
                            }
                        });



                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
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


                FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

            }


            FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
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

    public void city_details(String state_name)
    {
        SharedPreferences spf = FilterCustomerSearchActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        String name = state_name.trim();

        int position = state_spinner.getSelectedItemPosition();

        if(results1.contains("Select All"))
        {
            state_idn = statespinnerMap.get(position-2);
        }
        else
        {
            state_idn = statespinnerMap.get(position-1);
        }


        String domain = getResources().getString(R.string.service_domain1);
        StringRequest jsObjRequest = null;

        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);
        try {
            Log.i("user list url", "user list url " + domain + "metal/api/performance/v1/cities?email=" + user_email+"&state=" + URLEncoder.encode(state_idn, "UTF-8"));




            jsObjRequest = new StringRequest(domain + "metal/api/performance/v1/cities?email=" + user_email+"&state=" + URLEncoder.encode(state_idn, "UTF-8"), new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.i("volley", "response: " + response);
                    final_response = response;

                    new FilterCustomerSearchActivity.getAllCities().execute(response);

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
                             //   Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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


    private class getAllCities extends AsyncTask<String, Void, String> {

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

                    FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, response_result,"yes");
                        }
                    });


                }
                else
                if(response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, response_result,"yes");
                        }
                    });



                }
                else {

                    //dbvoc.getDeleteTable("delivery_products");

                    JSONArray cities = response.getJSONArray("cities");
                    Log.i("volley", "response cities Length: " + cities.length());
                    Log.d("volley", "cities" + cities.toString());


                    //
                    if (cities.length() <= 0) {

                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                                //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, "Cities doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, "Cities doesn't exist","yes");
                            }
                        });
                    } else {

                        results.clear();
                        city_id.clear();
                        results.add("Select City");
                        if (cities.length() > 1)
                        {
                            results.add("Select All");
                        }

                        for (int i = 0; i < cities.length(); i++) {

                            JSONObject jsonObject = cities.getJSONObject(i);

                            try {
                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cities.getString(i))) {
                                    {
                                        results.add(jsonObject.getString("name"));
                                        city_id.add(jsonObject.getString("id"));
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {

                                cityspinnerMap.clear();

                                String[] spinnerArray = new String[city_id.size()];
                                for (int i = 0; i < city_id.size(); i++)
                                {
                                    cityspinnerMap.put(i,city_id.get(i));
                                    //spinnerArray[i] = results1.get(i);
                                }

                                adapter_state2 = new ArrayAdapter<String>(FilterCustomerSearchActivity.this,
                                        android.R.layout.simple_spinner_item, results);
                                adapter_state2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                city_spinner.setAdapter(adapter_state2);

                                dialog.dismiss();
                            }
                        });

                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
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


                FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

            }


            FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
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


    public void beat_details(String city_name)
    {
        SharedPreferences spf = FilterCustomerSearchActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        String domain = getResources().getString(R.string.service_domain1);


        int position = city_spinner.getSelectedItemPosition();

        if(results.contains("Select All"))
        {
            city_idn = cityspinnerMap.get(position-2);
        }
        else
        {
            city_idn = cityspinnerMap.get(position-1);
        }

        StringRequest jsObjRequest = null;
        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);

        try {
            Log.i("user list url", "user list url " + domain + "metal/api/performance/v1/beats?email=" + user_email+"&city=" + URLEncoder.encode(city_idn, "UTF-8"));




            jsObjRequest = new StringRequest(domain + "metal/api/performance/v1/beats?email=" + user_email+"&city=" + URLEncoder.encode(city_idn, "UTF-8"), new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.i("volley", "response: " + response);
                    final_response = response;

                    new FilterCustomerSearchActivity.getAllbeats().execute(response);

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

    private class getAllbeats extends AsyncTask<String, Void, String> {
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

                    FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, response_result,"yes");
                        }
                    });


                }
                else
                if(response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, response_result,"yes");
                        }
                    });



                }
                else {

                    //dbvoc.getDeleteTable("delivery_products");

                    JSONArray beats = response.getJSONArray("beats");
                    Log.i("volley", "response beats Length: " + beats.length());
                    Log.d("volley", "beats" + beats.toString());


                    //
                    if (beats.length() <= 0) {

                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                                //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, "Beats doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, "Beats doesn't exist","yes");
                            }
                        });
                    } else {

                        results_beat.clear();
                        beat_id.clear();

                        results_beat.add("Select Beat");
                        if (beats.length() > 1)
                        {
                            results_beat.add("Select All");
                        }

                        for (int i = 0; i < beats.length(); i++) {

                            JSONObject jsonObject = beats.getJSONObject(i);

                            try {
                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(beats.getString(i))) {
                                    {
                                        results_beat.add(jsonObject.getString("name"));
                                        beat_id.add(jsonObject.getString("id"));
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {

                                beatspinnerMap.clear();

                                String[] spinnerArray = new String[beat_id.size()];
                                for (int i = 0; i < beat_id.size(); i++)
                                {
                                    beatspinnerMap.put(i,beat_id.get(i));
                                    //spinnerArray[i] = results1.get(i);
                                }

                                adapter_beat = new ArrayAdapter<String>(FilterCustomerSearchActivity.this,
                                        android.R.layout.simple_spinner_item, results_beat);
                                adapter_beat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                beat_spinner.setAdapter(adapter_beat);
                                // beat_spinner.setOnItemSelectedListener(Customer_Search.this);
                                dialog.dismiss();
                            }
                        });



                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
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


                FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

            }


            FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
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

    public void customer_details(String beat_name)
    {
        SharedPreferences spf = FilterCustomerSearchActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        String domain = getResources().getString(R.string.service_domain1);

        int position = beat_spinner.getSelectedItemPosition();

        if(results_beat.contains("Select All"))
        {
            beat_idn = beatspinnerMap.get(position-2);
        }
        else
        {
            beat_idn = beatspinnerMap.get(position-1);
        }

        StringRequest jsObjRequest = null;
        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);

        try {
//            Log.i("user list url", "user list url " + domain + "products/populate_variant?imei_no=" + Global_Data.imei_no +"&email=" + user_email+"&state=" + URLEncoder.encode(state_spinner.getSelectedItem().toString().trim(), "UTF-8")+"&city=" + URLEncoder.encode(city_spinner.getSelectedItem().toString(), "UTF-8")+"&beat=" + URLEncoder.encode(beat_name, "UTF-8"));

            Log.i("user list url", "user list url " + domain + "metal/api/performance/v1/customers/populate_customers_dropdown?email=" + user_email+"&beat=" + URLEncoder.encode(beat_idn, "UTF-8"));




            jsObjRequest = new StringRequest(domain + "metal/api/performance/v1/customers/populate_customers_dropdown?email=" + user_email+"&beat=" + URLEncoder.encode(beat_idn, "UTF-8"), new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.i("volley", "response: " + response);
                    final_response = response;

                    new FilterCustomerSearchActivity.getAllCustomers().execute(response);

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

    private class getAllCustomers extends AsyncTask<String, Void, String> {
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

                    FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, response_result,"Yes");
                        }
                    });


                }
                else
                if(response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, response_result,"yes");
                        }
                    });



                }
                else {

                    //dbvoc.getDeleteTable("delivery_products");

                    JSONArray customers = response.getJSONArray("customers");
                    Log.i("volley", "response customers Length: " + customers.length());
                    Log.d("volley", "variants" + customers.toString());


                    //
                    if (customers.length() <= 0) {

                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                                //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, "Customers doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, "Customers doesn't exist","yes");
                            }
                        });
                    } else {

                        list_AllCustomers.clear();
                        list_AllCustomers_check.clear();
                        customer_id.clear();

                        if (customers.length() > 1)
                        {
                            list_AllCustomers.add("Select All");
                        }

                        for (int i = 0; i < customers.length(); i++) {

                            JSONObject jsonObject = customers.getJSONObject(i);

                            try {
                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(customers.getString(i))) {
                                    {
                                        list_AllCustomers.add(jsonObject.getString("shop_name").trim());
                                        customer_id.add(jsonObject.getString("id"));
                                        list_AllCustomers_check.add(jsonObject.getString("shop_name").trim());
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {

                                String[] spinnerArray = new String[customer_id.size()];
                                // custumer_map.put("","");
                                for (int i = 0; i < customer_id.size(); i++)
                                {
                                    custumer_map.put(list_AllCustomers_check.get(i),customer_id.get(i));
                                    //spinnerArray[i] = results1.get(i);
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(FilterCustomerSearchActivity.this,
                                        R.layout.autocomplete,
                                        list_AllCustomers);
                                autoCompleteTextView1.setThreshold(1);// will start working from
                                // first character
                                autoCompleteTextView1.setAdapter(adapter);// setting the adapter
                                // data into the
                                // AutoCompleteTextView
                                autoCompleteTextView1.setTextColor(Color.BLACK);
                                dialog.dismiss();
                            }
                        });

                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
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


                FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

            }


            FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
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


    public void customer_data(String cust_name)
    {

        SharedPreferences spf = FilterCustomerSearchActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        String domain = getResources().getString(R.string.service_domain1);


//        if(list_AllCustomers.contains(""))
//        {
        customer_idn = custumer_map.get(cust_name);
//        }
//        else
//        {
//
//        }


        StringRequest jsObjRequest = null;
        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);

        try {
            Log.i("user list url", "user list url " + domain + "metal/api/performance/v1/customers/get_customer_details?email=" + user_email+"&customer=" + URLEncoder.encode(customer_idn, "UTF-8"));




            jsObjRequest = new StringRequest(domain + "metal/api/performance/v1/customers/get_customer_details?email=" + user_email+"&customer=" + URLEncoder.encode(customer_idn, "UTF-8"), new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.i("volley", "response: " + response);
                    final_response = response;

                    new FilterCustomerSearchActivity.getCustomersData().execute(response);

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
                                Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(), "yes");
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

    private class getCustomersData extends AsyncTask<String, Void, String> {
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

                    FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, response_result,"yes");
                        }
                    });


                }
                else
                if(response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, response_result,"yes");
                        }
                    });



                }
                else {

                    //dbvoc.getDeleteTable("delivery_products");

                    JSONArray customer = response.getJSONArray("customer");
                    Log.i("volley", "response customer Length: " + customer.length());
                    Log.d("volley", "customer" + customer.toString());


                    //
                    if (customer.length() <= 0) {

                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                                //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(FilterCustomerSearchActivity.this, "variants doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(FilterCustomerSearchActivity.this, "variants doesn't exist","yes");
                            }
                        });
                    } else {

                        //list_AllCustomers.clear();

                        for (int i = 0; i < customer.length(); i++) {

                            jsonObject = customer.getJSONObject(i);

                            try {
                                if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(customer.getString(i))){
                                    {
                                        //list_AllCustomers.add(variants.getString(i));

                                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                try {
                                                    txt_data.setText("Name : "+jsonObject.getString("shop_name")+"\n"+"Address : "+jsonObject.getString("address"));
                                                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });


                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                        }

                        FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
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


                FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

            }

            FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            FilterCustomerSearchActivity.this.runOnUiThread(new Runnable() {
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

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    private void updateLabel() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        fromdate.setText(sdf.format(calendar.getTime()));
    }

    private void updateLabel1() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        todate.setText(sdf.format(calendar.getTime()));
    }

}

