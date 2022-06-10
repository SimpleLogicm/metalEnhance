package com.msimplelogic.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.msimplelogic.activities.R;

import com.msimplelogic.adapter.CollectionStatementAdapter;
import com.msimplelogic.adapter.PendingTransactionAdapter;
import com.msimplelogic.model.CollectionItem;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cpm.simplelogic.helper.CashCollection_Data;

/**
 * Created by sujit on 11/15/2017.
 */

public class CollectionStatement extends Activity {
    RecyclerView collection_recview;
    private DatePickerDialog fromDatePickerDialog, fromDatePickerDialog1;
    private SimpleDateFormat dateFormatter;
    EditText from_date, to_date;
    CollectionStatementAdapter itemArrayAdapter;
    PendingTransactionAdapter pendingtransactionAdapter;
    ProgressDialog dialog;
    ArrayList<CollectionItem> itemList;
    TextView collect_total;
    AutoCompleteTextView auto_searchco;
    Button sta_submit;
    ConnectionDetector cd;
    TextView sheader1, sheader2, sheader3;
    Boolean isInternetPresent = false;
    HashMap<String, String> customers_map = new HashMap<String, String>();
    ArrayList<String> list_customers = new ArrayList<String>();

    String Atitle = "";
    LinearLayout ldate1, ldate2;
    DataBaseHelper dbvoc = new DataBaseHelper(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.collect_statement);

        collection_recview = (RecyclerView) findViewById(R.id.collection_card);
        from_date = (EditText) findViewById(R.id.from_date);
        to_date = (EditText) findViewById(R.id.to_date);
        collect_total = (TextView) findViewById(R.id.collect_total);
        auto_searchco = (AutoCompleteTextView) findViewById(R.id.auto_searchco);
        sta_submit = (Button) findViewById(R.id.sta_submit);
        sheader1 = (TextView) findViewById(R.id.sheader1);
        sheader2 = (TextView) findViewById(R.id.sheader2);
        sheader3 = (TextView) findViewById(R.id.sheader3);

        Global_Data.GlobeloPname = "";
        Global_Data.GlobeloPAmount = "";

        ldate1 = (LinearLayout) findViewById(R.id.ldate1);
        ldate2 = (LinearLayout) findViewById(R.id.ldate2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String text11 = bundle.getString("text1");
            String text22 = bundle.getString("text2");
            sheader1.setText(text11);
            sheader2.setText(text22);
            Atitle = bundle.getString("title");
        }

        cd = new ConnectionDetector(getApplicationContext());

        dateFormatter = new SimpleDateFormat("MMMM-yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText(Atitle);

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = this.getSharedPreferences("SimpleLogic", 0);

            try {
                int target = (int) Math.round(sp.getFloat("Target", 0));
                int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
                if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                    int age = (int) Math.round(age_float);

                    todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                } else {
                    int age = (int) Math.round(age_float);

                    todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
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

        SharedPreferences spf = CollectionStatement.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        // Initializing list view with the custom adapter
        itemList = new ArrayList<CollectionItem>();


        if (Atitle.equalsIgnoreCase(getResources().getString(R.string.Collection_Statement))) {
            itemArrayAdapter = new CollectionStatementAdapter(R.layout.list_item, itemList);
            collection_recview = (RecyclerView) findViewById(R.id.collection_card);
            collection_recview.setLayoutManager(new LinearLayoutManager(this));
            collection_recview.setItemAnimator(new DefaultItemAnimator());
//        collection_recview.addItemDecoration(new DividerItemDecoration(this,android.R.drawable.divider_horizontal_bright));
            collection_recview.setAdapter(itemArrayAdapter);

            list_customers.clear();
            customers_map.clear();
            List<Local_Data> contacts2 = dbvoc.getAllCustomerCollection();
            for (Local_Data cn : contacts2) {
                if (!list_customers.contains(cn.getCUSTOMER_SHOPNAME())) {
                    list_customers.add(cn.getCUSTOMER_SHOPNAME());
                    customers_map.put(cn.getCUSTOMER_SHOPNAME(), cn.getLEGACY_CUSTOMER_CODE());
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionStatement.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    list_customers);
            auto_searchco.setThreshold(1);// will start working from
            // first character
            auto_searchco.setAdapter(adapter);// setting the adapter
            // data into the
            // AutoCompleteTextView
            auto_searchco.setTextColor(Color.BLACK);

            List<CashCollection_Data> cash_reci_Data = dbvoc.getAllCash_Reciept();

            if (cash_reci_Data.size() > 0) {

                dialog = new ProgressDialog(CollectionStatement.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                dialog.setMessage(getResources().getString(R.string.Please_Wait));
                dialog.setTitle(getResources().getString(R.string.app_name));
                dialog.setCancelable(false);
                dialog.show();



                SyncCashrecieptDatan(CollectionStatement.this, Global_Data.device_id, user_email);

            } else {
                dialog = new ProgressDialog(CollectionStatement.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                dialog.setMessage(getResources().getString(R.string.Please_Wait));
                dialog.setTitle(getResources().getString(R.string.app_name));
                dialog.setCancelable(false);
                dialog.show();

                getStatementData(Global_Data.device_id, user_email, "", "", "");
            }

        } else if (Atitle.equalsIgnoreCase(getResources().getString(R.string.Deposit_Statement))) {
            itemArrayAdapter = new CollectionStatementAdapter(R.layout.list_item, itemList);
            collection_recview = (RecyclerView) findViewById(R.id.collection_card);
            collection_recview.setLayoutManager(new LinearLayoutManager(this));
            collection_recview.setItemAnimator(new DefaultItemAnimator());
//        collection_recview.addItemDecoration(new DividerItemDecoration(this,android.R.drawable.divider_horizontal_bright));
            collection_recview.setAdapter(itemArrayAdapter);

            auto_searchco.setVisibility(View.GONE);
            dialog = new ProgressDialog(CollectionStatement.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage(getResources().getString(R.string.Please_Wait));
            dialog.setTitle(getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();

            getStatementDataWi_CUS(Global_Data.device_id, user_email, "", "");
        } else if (Atitle.equalsIgnoreCase(getResources().getString(R.string.Pending_Transactions))) {
            pendingtransactionAdapter = new PendingTransactionAdapter(CollectionStatement.this, R.layout.list_item, itemList);
            collection_recview = (RecyclerView) findViewById(R.id.collection_card);
            collection_recview.setLayoutManager(new LinearLayoutManager(this));
            collection_recview.setItemAnimator(new DefaultItemAnimator());
//        collection_recview.addItemDecoration(new DividerItemDecoration(this,android.R.drawable.divider_horizontal_bright));
            collection_recview.setAdapter(pendingtransactionAdapter);

            auto_searchco.setVisibility(View.GONE);
            from_date.setVisibility(View.GONE);
            to_date.setVisibility(View.GONE);
            ldate1.setVisibility(View.GONE);
            ldate2.setVisibility(View.GONE);
            dialog = new ProgressDialog(CollectionStatement.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage(getResources().getString(R.string.Please_Wait));
            dialog.setTitle(getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();

            getStatementDataWi_CUS(Global_Data.device_id, user_email, "", "");

//            for(int i=0;i<5; i++)
//            {
//            String input = "2007-11-11 12:13:14";
//            java.sql.Timestamp ts = java.sql.Timestamp.valueOf(input);
//
//            String s = "11/21/2017 15:59:60";
//            String s1 = "11/21/2017 16:01:60";
//            String s2 = "11/21/2017 16:01:60";
//            String s3 = "11/21/2017 16:02:60";
//            String s4 = "11/21/2017 16:02:60";
//            String s5 = "11/21/2017 17:14:60";
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//            try {
//                Date date = simpleDateFormat.parse(s);
//                Date date1 = simpleDateFormat.parse(s1);
//                Date date2 = simpleDateFormat.parse(s2);
//                Date date3 = simpleDateFormat.parse(s3);
//                Date date4 = simpleDateFormat.parse(s4);
//                Date date5 = simpleDateFormat.parse(s5);
//
//                System.out.println("date : " + simpleDateFormat.format(date));
//                final long currentTime = date.getTime();
//                final long currentTime1 = date1.getTime();
//                final long currentTime2 = date2.getTime();
//                final long currentTime3 = date3.getTime();
//                final long currentTime4 = date4.getTime();
//                final long currentTime5 = date5.getTime();
//                itemList.add(new CollectionItem(String.valueOf(currentTime), "name1", "8000"));
//                itemList.add(new CollectionItem(String.valueOf(currentTime1), "name1", "8000"));
//                itemList.add(new CollectionItem(String.valueOf(currentTime2), "name1", "8000"));
//                itemList.add(new CollectionItem(String.valueOf(currentTime3), "name1", "8000"));
//                itemList.add(new CollectionItem(String.valueOf(currentTime4), "name1", "8000"));
//                itemList.add(new CollectionItem(String.valueOf(currentTime5), "name1", "8000"));
//            } catch (ParseException ex) {
//                System.out.println("Exception " + ex);
//            }


//                 itemList.add(new CollectionItem("80000", "name1", "8000"));
//            itemList.add(new CollectionItem("80000", "name1", "8000"));
//            itemList.add(new CollectionItem("80000", "name1", "8000"));
//            itemList.add(new CollectionItem("80000", "name1", "8000"));
//            itemList.add(new CollectionItem("80000", "name1", "8000"));
//            itemList.add(new CollectionItem("80000", "name1", "8000"));
//            itemList.add(new CollectionItem("80000", "name1", "8000"));
            //}
            //  pendingtransactionAdapter.notifyDataSetChanged();
        }


        // Populating list items
//        for (int i = 0; i < 100; i++) {
//            itemList.add(new CollectionItem("Item " + i));
//        }

        sta_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CollectionStatement.this, Customer_info_main.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(i);
                // finish();
            }

        });

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String yr_reg = Integer.toString(year);
                String mnth_reg = Integer.toString(monthOfYear + 1);
                String date_reg = Integer.toString(dayOfMonth);

                SharedPreferences spf = CollectionStatement.this.getSharedPreferences("SimpleLogic", 0);
                String user_email = spf.getString("USER_EMAIL",null);

                from_date.setText(date_reg + "-" + (dateFormatter.format(newDate.getTime())));

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(from_date.getText().toString().trim()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(to_date.getText().toString().trim())) {

                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {



                        Date date1 = new Date(from_date.getText().toString());
                        Date date2 = new Date(to_date.getText().toString());
                        Calendar cal1 = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();
                        cal1.setTime(date1);
                        cal2.setTime(date1);

                        if (date1.compareTo(date2) > 0) {
                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.From_date_validations),"Yes");
//                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.From_date_validations), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            from_date.setText("");
                        } else {
                            if (Atitle.equalsIgnoreCase(getResources().getString(R.string.Collection_Statement))) {
                                dialog = new ProgressDialog(CollectionStatement.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                dialog.setMessage(getResources().getString(R.string.Please_Wait));
                                dialog.setTitle(getResources().getString(R.string.app_name));
                                dialog.setCancelable(false);
                                dialog.show();

                                getStatementData(Global_Data.device_id, user_email, from_date.getText().toString().trim(), to_date.getText().toString().trim(), auto_searchco.getText().toString().trim());
                            } else if (Atitle.equalsIgnoreCase(getResources().getString(R.string.Deposit_Statement))) {
                                auto_searchco.setVisibility(View.GONE);
                                dialog = new ProgressDialog(CollectionStatement.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                dialog.setMessage(getResources().getString(R.string.Please_Wait));
                                dialog.setTitle(getResources().getString(R.string.app_name));
                                dialog.setCancelable(false);
                                dialog.show();

                                getStatementDataWi_CUS(Global_Data.device_id, user_email, from_date.getText().toString().trim(), to_date.getText().toString().trim());
                            }
                        }

                    } else {
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.internet_connection_error),"Yes");
//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                    }

                } else {
                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Select_To_Date),"Yes");
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Select_To_Date), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                }

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.getWindow().getAttributes();
                fromDatePickerDialog.show();
            }
        });

        fromDatePickerDialog1 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String yr_reg = Integer.toString(year);
                String mnth_reg = Integer.toString(monthOfYear + 1);
                String date_reg = Integer.toString(dayOfMonth);

                to_date.setText(date_reg + "-" + (dateFormatter.format(newDate.getTime())));


                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(from_date.getText().toString().trim()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(to_date.getText().toString().trim())) {

                    Date date1 = new Date(from_date.getText().toString());
                    Date date2 = new Date(to_date.getText().toString());
                    Calendar cal1 = Calendar.getInstance();
                    Calendar cal2 = Calendar.getInstance();
                    cal1.setTime(date1);
                    cal2.setTime(date1);

                    if (date1.compareTo(date2) > 0) {
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.To_date_validation_message),"Yes");
//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.To_date_validation_message), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        to_date.setText("");
                    } else {

                        SharedPreferences spf = CollectionStatement.this.getSharedPreferences("SimpleLogic", 0);
                        String user_email = spf.getString("USER_EMAIL",null);

                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent) {

                            if (Atitle.equalsIgnoreCase(getResources().getString(R.string.Collection_Statement))) {
                                dialog = new ProgressDialog(CollectionStatement.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                dialog.setMessage(getResources().getString(R.string.Please_Wait));
                                dialog.setTitle(getResources().getString(R.string.app_name));
                                dialog.setCancelable(false);
                                dialog.show();

                                getStatementData(Global_Data.device_id, user_email, from_date.getText().toString().trim(), to_date.getText().toString().trim(), auto_searchco.getText().toString().trim());
                            } else if (Atitle.equalsIgnoreCase(getResources().getString(R.string.Deposit_Statement))) {
                                auto_searchco.setVisibility(View.GONE);
                                dialog = new ProgressDialog(CollectionStatement.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                dialog.setMessage(getResources().getString(R.string.Please_Wait));
                                dialog.setTitle(getResources().getString(R.string.app_name));
                                dialog.setCancelable(false);
                                dialog.show();

                                getStatementDataWi_CUS(Global_Data.device_id, user_email, from_date.getText().toString().trim(), to_date.getText().toString().trim());
                            }


                        } else {
                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.internet_connection_error),"Yes");
//                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                        }
                    }



                } else {
                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Select_From_Date),"Yes");
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Select_From_Date), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog1.getWindow().getAttributes();
                fromDatePickerDialog1.show();
            }
        });

        auto_searchco.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                SharedPreferences spf = CollectionStatement.this.getSharedPreferences("SimpleLogic", 0);
                String user_email = spf.getString("USER_EMAIL",null);

                Log.d("IN CHECK", "IN CHECK" + s.toString());
                if (s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(" ")) {
                    Global_Data.hideSoftKeyboard(CollectionStatement.this);
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {

                        Log.d(" CUSTOMER NAme", "NAme" + auto_searchco.getText().toString());


                        dialog = new ProgressDialog(CollectionStatement.this);
                        dialog.setMessage(getResources().getString(R.string.Please_Wait));
                        dialog.setTitle(getResources().getString(R.string.app_name));
                        dialog.setCancelable(false);
                        dialog.show();
                        getStatementData(Global_Data.device_id, user_email, from_date.getText().toString().trim(), to_date.getText().toString().trim(), "");


                    } else {
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.internet_connection_error), "Yes");
//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                    }
                }

            }
        });

        auto_searchco.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (auto_searchco.getRight() - auto_searchco.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        View view = CollectionStatement.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        //autoCompleteTextView1.setText("");
                        auto_searchco.showDropDown();
                        return true;
                    }
                }
                return false;
            }
        });

        auto_searchco.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                //Toast.makeText(Order.this," selected", Toast.LENGTH_LONG).show();

                SharedPreferences spf = CollectionStatement.this.getSharedPreferences("SimpleLogic", 0);
                String user_email = spf.getString("USER_EMAIL",null);

                auto_searchco.dismissDropDown();
                String custname = auto_searchco.getText().toString();

                Global_Data.hideSoftKeyboard(CollectionStatement.this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    Log.d(" CUSTOMER NAme", "NAme" + auto_searchco.getText().toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(auto_searchco.getText().toString())) {
                        dialog = new ProgressDialog(CollectionStatement.this);
                        dialog.setMessage(getResources().getString(R.string.Please_Wait));
                        dialog.setTitle(getResources().getString(R.string.app_name));
                        dialog.setCancelable(false);
                        dialog.show();
                        getStatementData(Global_Data.device_id, user_email, from_date.getText().toString().trim(), to_date.getText().toString().trim(), auto_searchco.getText().toString().trim());
                    }

                } else {
                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.internet_connection_error),"Yes");
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent i = new Intent(CollectionStatement.this, Customer_info_main.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        startActivity(i);
        // finish();
    }

    public void getStatementData(String device_id, String email, String from_date, String to_date, String cus_name) {
        try {

            String customer_code = "";
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(cus_name)) {

                customer_code = customers_map.get(auto_searchco.getText().toString().trim());
                Log.d(" CUSTOMER ID", "ID" + customer_code);
            } else {
                customer_code = "";
                Log.d(" CUSTOMER ID", "ID" + customer_code);
            }
            SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";

            String domain = service_url;
            Log.i("volley", "domain: " + domain);
            Log.i("volley", "email: " + email);
            Log.i("cash_receipts url", "cash_receipts report url " + domain + "cash_receipts?imei_no=" + device_id + "&email=" + email + "&from_date=" + from_date + "&to_date=" + to_date + "&customer_code=" + customer_code);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(domain + "cash_receipts?imei_no=" + device_id + "&email=" + email + "&from_date=" + from_date + "&to_date=" + to_date + "&customer_code=" + customer_code, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);
                    //  Log.i("volley", "response reg Length: " + response.length());


                    try {

                        String response_result = "";
                        if (response.has("result")) {
                            response_result = response.getString("result");
                        } else {
                            response_result = "data";
                        }
                        if (response_result.equalsIgnoreCase("User doesn't found.")) {

                            dialog.dismiss();

                            Global_Data.Custom_Toast(CollectionStatement.this, response_result,"Yes");
//                            Toast toast = Toast.makeText(CollectionStatement.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Intent intent = new Intent(CollectionStatement.this, Customer_info_main.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            // finish();

                        } else if (response_result.equalsIgnoreCase("User doesn't exist")) {

                            //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                            dialog.dismiss();
                            Global_Data.Custom_Toast(CollectionStatement.this, response_result,"Yes");
//                            Toast toast = Toast.makeText(CollectionStatement.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Intent intent = new Intent(CollectionStatement.this, Customer_info_main.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            //finish();

                        } else {

                            JSONArray receipts = response.getJSONArray("receipts");
                            //  String cash_inh = response.getString("wallet_balance");


                            Log.i("volley", "response receipts Length: " + receipts.length());

                            Log.d("Cash_listData", "Cash_listData" + receipts.toString());
                            if (receipts.length() <= 0) {
                                dialog.dismiss();

                                Global_Data.Custom_Toast(CollectionStatement.this, getResources().getString(R.string.Sorry_No_Record_Found),"Yes");
//                                Toast toast = Toast.makeText(CollectionStatement.this, getResources().getString(R.string.Sorry_No_Record_Found), Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

                                Intent intent = new Intent(CollectionStatement.this, Customer_info_main.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                //  finish();


                            } else {

                                itemList.clear();
                                // customers_map.clear();
                                //  list_customers.clear();
                                Float amount = 0.0f;
                                for (int i = 0; i < receipts.length(); i++) {

                                    JSONObject jsonObject = receipts.getJSONObject(i);
                                    //submit_toArray.add(jsonObject.getString("user_name"));

                                    itemList.add(new CollectionItem(jsonObject.getString("creation"), jsonObject.getString("customer_shop_name") + "\n" + jsonObject.getString("customer_address"), jsonObject.getString("amount")));

//                                    customers_map.put(jsonObject.getString("customer_name"), jsonObject.getString("customer_code"));
//
//                                    if (!list_customers.contains(jsonObject.getString("customer_name"))) {
//                                        list_customers.add(jsonObject.getString("customer_name"));
//                                    }


                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(jsonObject.getString("amount"))) {
                                        amount += Float.valueOf(jsonObject.getString("amount"));

                                    }

                                }


                                collect_total.setText(getResources().getString(R.string.Total) + String.valueOf(amount));
                                itemArrayAdapter = new CollectionStatementAdapter(R.layout.list_item, itemList);
                                // collection_recview = (RecyclerView) findViewById(R.id.collection_card);
                                // collection_recview.setLayoutManager(new LinearLayoutManager(this));
                                //  collection_recview.setItemAnimator(new DefaultItemAnimator());
//        collection_recview.addItemDecoration(new DividerItemDecoration(this,android.R.drawable.divider_horizontal_bright));
                                collection_recview.setAdapter(itemArrayAdapter);

                                itemArrayAdapter.notifyDataSetChanged();
//                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionStatement.this,
//                                        android.R.layout.simple_spinner_dropdown_item,
//                                        list_customers);
//                                auto_searchco.setThreshold(1);// will start working from
//                                // first character
//                                auto_searchco.setAdapter(adapter);// setting the adapter
//                                // data into the
//                                // AutoCompleteTextView
//                                auto_searchco.setTextColor(Color.BLACK);


                                dialog.dismiss();
                            }
                            dialog.dismiss();


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                    }


                    dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volley", "error: " + error);
                    //Toast.makeText(Order.this, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();
Global_Data.Custom_Toast(CollectionStatement.this, getResources().getString(R.string.Server_Error),"Yes");
//                    Toast toast = Toast.makeText(CollectionStatement.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    dialog.dismiss();

                    Intent intent = new Intent(CollectionStatement.this, Customer_info_main.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    // finish();

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(CollectionStatement.this);
            // queue.add(jsObjRequest);
            jsObjRequest.setShouldCache(false);
            int socketTimeout = 200000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);

        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();


        }
    }

    public void getStatementDataWi_CUS(String device_id, String email, String from_date, String to_date) {
        try {

            SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";

            String domain = service_url;
            Log.i("volley", "domain: " + domain);
            Log.i("volley", "email: " + email);

            String url = "";
            if (Atitle.equalsIgnoreCase("Pending Transaction")) {
                url = domain + "cash_deposits/get_pending_txns?imei_no=" + device_id + "&email=" + email;
            } else {
                url = domain + "cash_deposits?imei_no=" + device_id + "&email=" + email + "&from_date=" + from_date + "&to_date=" + to_date;
            }

            Log.i("cash_deposits url", "cash_deposits report url " + url);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);
                    //  Log.i("volley", "response reg Length: " + response.length());


                    try {

                        String response_result = "";
                        if (response.has("result")) {
                            response_result = response.getString("result");
                        } else {
                            response_result = "data";
                        }


                        if (response_result.equalsIgnoreCase("User doesn't found.")) {


                            dialog.dismiss();
                            Global_Data.Custom_Toast(CollectionStatement.this, response_result,"Yes");
//
//                            Toast toast = Toast.makeText(CollectionStatement.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Intent intent = new Intent(CollectionStatement.this, Customer_info_main.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            // finish();

                        } else if (response_result.equalsIgnoreCase("User doesn't exist")) {

                            //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                            dialog.dismiss();
                            Global_Data.Custom_Toast(CollectionStatement.this, response_result,"Yes");
//                            Toast toast = Toast.makeText(CollectionStatement.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Intent intent = new Intent(CollectionStatement.this, Customer_info_main.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            // finish();

                        } else {

                            JSONArray deposits = response.getJSONArray("deposits");
                            //  String cash_inh = response.getString("wallet_balance");


                            Log.i("volley", "response reg deposits Length: " + deposits.length());

                            Log.d("deposits", "deposits" + deposits.toString());
                            if (deposits.length() <= 0) {
                                dialog.dismiss();

                                Global_Data.Custom_Toast(CollectionStatement.this, getResources().getString(R.string.Sorry_No_Record_Found),"Yes");
//                                Toast toast = Toast.makeText(CollectionStatement.this, getResources().getString(R.string.Sorry_No_Record_Found), Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

                                Intent intent = new Intent(CollectionStatement.this, Customer_info_main.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                //  finish();


                            } else {

                                itemList.clear();
                                //customers_map.clear();
                                // list_customers.clear();
                                Float amount = 0.0f;
                                for (int i = 0; i < deposits.length(); i++) {

                                    JSONObject jsonObject = deposits.getJSONObject(i);
                                    //submit_toArray.add(jsonObject.getString("user_name"));

                                    if (Atitle.equalsIgnoreCase("Pending Transaction")) {

                                        try {
                                            String s = jsonObject.getString("creation");

                                            itemList.add(new CollectionItem(s, jsonObject.getString("user_name"), jsonObject.getString("amount")));
                                        } catch (Exception ex) {
                                            System.out.println("Exception " + ex);
                                        }

                                    } else {
                                        itemList.add(new CollectionItem(jsonObject.getString("creation"), jsonObject.getString("user_name") + "\n" + jsonObject.getString("address"), jsonObject.getString("amount")));
                                    }



//                                    customers_map.put(jsonObject.getString("user_name"), jsonObject.getString("user_name"));
//
//                                    list_customers.add(jsonObject.getString("user_name"));

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(jsonObject.getString("amount"))) {
                                        amount += Float.valueOf(jsonObject.getString("amount"));

                                    }

                                }

                                collect_total.setText(getResources().getString(R.string.Total) + String.valueOf(amount));

                                if (Atitle.equalsIgnoreCase(getResources().getString(R.string.Pending_Transactions))) {
                                    pendingtransactionAdapter.notifyDataSetChanged();
                                } else {
                                    itemArrayAdapter.notifyDataSetChanged();
                                }

//                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionStatement.this,
//                                        android.R.layout.simple_spinner_dropdown_item,
//                                        list_customers);
//                                auto_searchco.setThreshold(1);// will start working from
//                                // first character
//                                auto_searchco.setAdapter(adapter);// setting the adapter
//                                // data into the
//                                // AutoCompleteTextView
//                                auto_searchco.setTextColor(Color.BLACK);


                                dialog.dismiss();
                            }
                            dialog.dismiss();


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                    }


                    dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volley", "error: " + error);
                    //Toast.makeText(Order.this, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();

                    Global_Data.Custom_Toast(CollectionStatement.this, getResources().getString(R.string.Server_Error),"Yes");
//                    Toast toast = Toast.makeText(CollectionStatement.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    dialog.dismiss();

                    Intent intent = new Intent(CollectionStatement.this, Customer_info_main.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    // finish();

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(CollectionStatement.this);
            // queue.add(jsObjRequest);
            jsObjRequest.setShouldCache(false);
            int socketTimeout = 200000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);

        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();


        }
    }

    public void SyncCashrecieptDatan(Context context, final String device_id, final String user_email) {
        System.gc();
        String reason_code = "";
        try {

            String domain = "";

            SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";

            domain = service_url;

            JsonObjectRequest jsObjRequest = null;
            try {

                Log.d("Server url", "Server url" + domain + "cash_receipts/save_receipts");

                JSONArray COLLECTION_RECJARRAY = new JSONArray();
                //JSONObject product_value = new JSONObject();
                JSONObject COLLECTION_RECJOBJECT = new JSONObject();
                JSONArray product_imei = new JSONArray();

                List<CashCollection_Data> cash_reci_Data = dbvoc.getAllCash_Reciept();

                if (cash_reci_Data.size() > 0) {
                    for (CashCollection_Data c : cash_reci_Data) {

                        JSONObject INNER_CASH_JOB = new JSONObject();
                        INNER_CASH_JOB.put("code", c.getCode());
                        INNER_CASH_JOB.put("reason_code", c.getReason_name());
                        INNER_CASH_JOB.put("amount", c.getAmount());
                        INNER_CASH_JOB.put("detail1", c.getCash_detail1());
                        INNER_CASH_JOB.put("detail2", c.getCash_detail2());
                        INNER_CASH_JOB.put("detail3", c.getCash_detail3());
                        INNER_CASH_JOB.put("detail4", c.getCash_detail4());
                        INNER_CASH_JOB.put("detail5", c.getCash_detail5());
                        INNER_CASH_JOB.put("received_at", c.getReceived_at());
                        INNER_CASH_JOB.put("customer_code", c.getReceived_from());
                        //INNER_CASH_JOB.put("received_from_name", c.getReceived_from_name());
                        if (c.getReceived_loc_latlong().indexOf(",") > 0) {
                            String latlong[] = c.getReceived_loc_latlong().split(",");
                            INNER_CASH_JOB.put("latitude", latlong[0]);
                            INNER_CASH_JOB.put("longitude", latlong[1]);
                        } else {
                            INNER_CASH_JOB.put("latitude", c.getReceived_loc_latlong());
                            INNER_CASH_JOB.put("longitude", c.getReceived_loc_latlong());
                        }
                        INNER_CASH_JOB.put("address", c.getReceived_loc_address());
                        INNER_CASH_JOB.put("signature", c.getReceived_signature());
                        INNER_CASH_JOB.put("remarks", c.getReceived_remarks());
                        //INNER_CASH_JOB.put("random_value", c.getPunched_on());
                        INNER_CASH_JOB.put("amount_overdue", c.getColle_overdue());
                        INNER_CASH_JOB.put("amount_outstanding", c.getColle_outstanding());
                        COLLECTION_RECJARRAY.put(INNER_CASH_JOB);

                    }
                }

                COLLECTION_RECJOBJECT.put("receipts", COLLECTION_RECJARRAY);
                COLLECTION_RECJOBJECT.put("imei_no", device_id);
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

                                Log.d("CASHR Response", "CASHR Response " + response_result);

                                dbvoc.getDeleteTable("cash_receipt");

                                SharedPreferences spf = CollectionStatement.this.getSharedPreferences("SimpleLogic", 0);
                                String user_email1 = spf.getString("USER_EMAIL",null);

                                getStatementData(Global_Data.device_id, user_email1, "", "", "");
                                //  dialog.dismiss();

                            } else {
                                SharedPreferences spf = CollectionStatement.this.getSharedPreferences("SimpleLogic", 0);
                                String user_email1 = spf.getString("USER_EMAIL",null);

                                Log.d("CASHR Response", "CASHR Response " + response_result);
                                getStatementData(Global_Data.device_id, user_email1, "", "", "");
                                // dialog.dismiss();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            SharedPreferences spf = CollectionStatement.this.getSharedPreferences("SimpleLogic", 0);
                            String user_email1 = spf.getString("USER_EMAIL",null);
                            getStatementData(Global_Data.device_id, user_email1, "", "", "");
                            //dialog.dismiss();

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        SharedPreferences spf = CollectionStatement.this.getSharedPreferences("SimpleLogic", 0);
                        String user_email1 = spf.getString("USER_EMAIL",null);
                        // dialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Log.d("ErrorC", "ErrorC your internet connection is not working, saving locally. Please sync when Internet is available");
                        } else if (error instanceof AuthFailureError) {
                            Log.d("ErrorC", "ErrorC Server AuthFailureError  Error");
                        } else if (error instanceof ServerError) {
                            Log.d("ErrorC", "ErrorC Server Error");
                        } else if (error instanceof NetworkError) {
                            Log.d("ErrorC", "ErrorC your internet connection is not working, saving locally. Please sync when Internet is available");
                        } else if (error instanceof ParseError) {
                            Log.d("ErrorC", "ErrorC arseError   Error");
                        } else {
                            Log.d("ErrorC", "ErrorC " + error.getMessage());
                        }
                        getStatementData(Global_Data.device_id, user_email1, "", "", "");

                    }
                });


                RequestQueue requestQueue = Volley.newRequestQueue(context);

                int socketTimeout = 120000;//90 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsObjRequest.setRetryPolicy(policy);
                // requestQueue.se
                //requestQueue.add(jsObjRequest);
                jsObjRequest.setShouldCache(false);
                requestQueue.getCache().clear();
                requestQueue.add(jsObjRequest);

            } catch (Exception e) {
                SharedPreferences spf = CollectionStatement.this.getSharedPreferences("SimpleLogic", 0);
                String user_email1 = spf.getString("USER_EMAIL",null);
                e.printStackTrace();
                getStatementData(Global_Data.device_id, user_email1, "", "", "");

            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("DATA", e.getMessage());
            dialog.dismiss();

            SharedPreferences spf = CollectionStatement.this.getSharedPreferences("SimpleLogic", 0);
            String user_email1 = spf.getString("USER_EMAIL",null);
            getStatementData(Global_Data.device_id, user_email1, "", "", "");
        }
    }
}
