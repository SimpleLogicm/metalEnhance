package com.msimplelogic.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.msimplelogic.adapter.Customer_Outstanding_Adapter;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cpm.simplelogic.helper.Customer_Info;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

/**
 * Created by vinod on 13-09-2017.
 */


public class Customer_Outstanding extends BaseActivity {

    static String final_response = "";
    String response_result = "";

    LoginDataBaseAdapter loginDataBaseAdapter;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    List<Customer_Info> Allresult = new ArrayList<Customer_Info>();
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    ProgressDialog dialog;
    RecyclerView recList;
    String Customer_id = "";
    String City_id = "";
    String Beat_id = "";
    private SearchView mSearchView;
    Customer_Outstanding_Adapter ca;
    String s[];
    ArrayList<String> All_customers = new ArrayList<String>();
    Toolbar toolbar;
    AutoCompleteTextView autoCompleteTextView1;
    TextView c_total_0us;
    Double total_outstanding = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_customer_outstanding);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        recList = (RecyclerView) findViewById(R.id.c_info_card);
        // recList.addItemDecoration(new DividerItemDecoration(Customer_Outstanding.this, DividerItemDecoration.VERTICAL_LIST));

        c_total_0us = (TextView) findViewById(R.id.c_total_0us);

        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        cd = new ConnectionDetector(getApplicationContext());

        loginDataBaseAdapter = new LoginDataBaseAdapter(Customer_Outstanding.this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
//        try {
//            ActionBar mActionBar = getActionBar();
//            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            // mActionBar.setDisplayShowHomeEnabled(false);
//            // mActionBar.setDisplayShowTitleEnabled(false);
//            LayoutInflater mInflater = LayoutInflater.from(this);
//
//            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
//            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
//            mTitleTextView.setText(getResources().getString(R.string.Customer_Outstanding));
//
//            Global_Data.Globelo_OU_CUSTID = "";
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = Customer_Outstanding.this.getSharedPreferences("SimpleLogic", 0);
//
//            try {
//                int target = (int) Math.round(sp.getFloat("Target", 0));
//                int achieved = (int) Math.round(sp.getFloat("Achived", 0));
//                Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
//                if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
//                    int age = (int) Math.round(age_float);
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
//                    }
//                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
//                } else {
//                    int age = (int) Math.round(age_float);
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    }
//                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                }
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//
//            mActionBar.setCustomView(mCustomView);
//            mActionBar.setDisplayShowCustomEnabled(true);
//            mActionBar.setHomeButtonEnabled(true);
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


        // setupSearchView();

        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            online_OutstandingList();
        } else {

            dialog = new ProgressDialog(Customer_Outstanding.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage(getResources().getString(R.string.Please_Wait));
            dialog.setTitle(getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();

            new CustomerASN().execute();
        }


        autoCompleteTextView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (autoCompleteTextView1.getRight() - autoCompleteTextView1.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        View view = Customer_Outstanding.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        //autoCompleteTextView1.setText("");
                        autoCompleteTextView1.showDropDown();
                        return true;
                    }
                }
                return false;
            }
        });

        autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (autoCompleteTextView1.getText().toString().trim().length() == 0) {

                    ca = new Customer_Outstanding_Adapter(Allresult, Customer_Outstanding.this);
                    recList.setAdapter(ca);
                    ca.notifyDataSetChanged();


                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                //Toast.makeText(Order.this," selected", Toast.LENGTH_LONG).show();

                Global_Data.hideSoftKeyboard(Customer_Outstanding.this);

                String customer_name = "";
                String address_type = "";
                if (autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0) {
                    s = autoCompleteTextView1.getText().toString().trim().split(":");
                    customer_name = s[0].trim();
                    address_type = s[1].trim();
                } else {
                    customer_name = autoCompleteTextView1.getText().toString().trim();
                }
//
//
//				Global_Data.credit_limit_amount = "";
//				Global_Data.outstandings_amount = "";

                List<Local_Data> contacts = dbvoc.getCustomerCodenew(customer_name);

                Customer_Info ci = new Customer_Info();
                List<Customer_Info> result = new ArrayList<Customer_Info>();
                if (contacts.size() <= 0) {
                    Global_Data.Custom_Toast(Customer_Outstanding.this,
                            getResources().getString(R.string.Customer_Not_Found),"Yes");
//                    Toast toast = Toast.makeText(Customer_Outstanding.this,
//                            getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                } else {
                    for (Local_Data cn : contacts) {

                        recList.setVisibility(View.VISIBLE);


                        Double amt_outstanding = ((Double.valueOf(cn.get_shedule_outstanding_amount())));
                        Double amt_overdue = ((Double.valueOf(cn.getAmmount_overdue())));
                        ci.amount1 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding) + "</b>" + String.format("%.2f", amt_outstanding)));

                        ci.amount2 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue) + "</b>" + String.format("%.2f", amt_overdue)));

                        ci.name = String.valueOf(Html.fromHtml(cn.getCUSTOMER_SHOPNAME()));
                        Customer_id = cn.getCust_Code();
                        ci.cus_code = cn.getCust_Code();
//                        ci.mobile = cn.getMOBILE_NO();
//                        ci.shop_name = cn.getCUSTOMER_SHOPNAME();
                        // ci.address = String.valueOf(Html.fromHtml("<b>" + "Address : " + "</b>" + cn.getAddress()));
                        //  ci.latlong = cn.getlatitude() + "," + cn.getlongitude();
                        Customer_id = cn.getCust_Code();



                    }



                    result.add(ci);

                    ca = new Customer_Outstanding_Adapter(result, Customer_Outstanding.this);
                    recList.setAdapter(ca);
                    ca.notifyDataSetChanged();
                }
            }
        });
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
                String targetNew="";
                SharedPreferences sp = Customer_Outstanding.this.getSharedPreferences("SimpleLogic", 0);
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
        Intent i = new Intent(Customer_Outstanding.this, MainActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private class CustomerASN extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... response) {

            int cout_check = 0;
            List<Local_Data> contacts3 = dbvoc.getcustomer_pre_data_c_outstanding();

            if (contacts3.size() <= 0) {
                // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

                Customer_Outstanding.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Global_Data.Custom_Toast(Customer_Outstanding.this, getResources().getString(R.string.Sorry_No_Record_Found),"Yes");
//                        Toast toast = Toast.makeText(Customer_Outstanding.this, getResources().getString(R.string.Sorry_No_Record_Found), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Intent i = new Intent(Customer_Outstanding.this, MainActivity.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                });

                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
            } else {
                Customer_Outstanding.this.runOnUiThread(new Runnable() {
                    public void run() {
                        recList.setVisibility(View.VISIBLE);
                    }
                });


                total_outstanding = 0.0;
                All_customers.clear();
                try {
                    List<Customer_Info> result = new ArrayList<Customer_Info>();
                    for (Local_Data cn : contacts3) {

                        Customer_Info ci = new Customer_Info();

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpoin(cn.get_shedule_outstanding_amount())) {
                            cout_check = 1;
                            Double amt_outstanding = ((Double.valueOf(cn.get_shedule_outstanding_amount())));
                            Double amt_overdue = ((Double.valueOf(cn.getAmmount_overdue())));

                            ci.amount1 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding) + "</b>" + String.format("%.2f", amt_outstanding)));

                            ci.amount2 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue) + "</b>" + String.format("%.2f", amt_overdue)));

                            total_outstanding += amt_outstanding;

                            ci.name = String.valueOf(Html.fromHtml(cn.getCUSTOMER_SHOPNAME()));
                            Customer_id = cn.getCust_Code();
                            ci.cus_code = cn.getCust_Code();


                            if (!All_customers.contains(cn.getCUSTOMER_SHOPNAME())) {
                                All_customers.add(cn.getCUSTOMER_SHOPNAME());
                            }


                            result.add(ci);
                            Allresult.add(ci);


                        }


//                  List<Local_Data> contactlimit = dbvoc.getCreditprofileData(Customer_id);
//                    if(contactlimit.size() > 0) {
//
//                        for (Local_Data cnn : contactlimit) {
//
//                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.get_credit_limit())) {
//                                Double credit_limit =  ((Double.valueOf(cnn.get_credit_limit())));
//                                ci.credit_limit = String.valueOf(Html.fromHtml("<b>" +"Credit Limit : "+ "</b>"+String.format("%.2f",credit_limit)));
//                            } else {
//                                ci.credit_limit = String.valueOf(Html.fromHtml("<b>" +"Credit Limit Not Found"+ "</b>"));
//                            }
//
//                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.get_shedule_outstanding_amount())) {
//                                Double amt_outstanding =  ((Double.valueOf(cnn.get_shedule_outstanding_amount())));
//                                ci.amount1 = String.valueOf(Html.fromHtml("<b>" +"Amount Outstanding : "+ "</b>"+String.format("%.2f",amt_outstanding)));
//
//
//                            } else {
//
//                                ci.amount1  = String.valueOf(Html.fromHtml("<b>" +"Amount Outstanding Not Found"+ "</b>"));
//                            }
//
//                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.getAmmount_overdue())) {
//                                Double amt_overdue =  ((Double.valueOf(cnn.getAmmount_overdue())));
//                                ci.amount2 = String.valueOf(Html.fromHtml("<b>" +"Amount Overdue : "+ "</b>"+String.format("%.2f",amt_overdue)));
//
//
//                            } else {
//                                ci.amount2  = String.valueOf(Html.fromHtml("<b>" +"Amount Overdue Not Found"+ "</b>"));
//
//                            }
//
//
//                        }
//                    }
//                    else
//                    {
//                        ci.credit_limit = String.valueOf(Html.fromHtml("<b>" +"Credit Limit Not Found"+ "</b>"));
//                        ci.amount1  = String.valueOf(Html.fromHtml("<b>" +"Amount Outstanding Not Found"+ "</b>"));
//                        ci.amount2  = String.valueOf(Html.fromHtml("<b>" +"Amount Overdue Not Found"+ "</b>"));
//                    }


                    }

                    if (cout_check == 1) {
                        ca = new Customer_Outstanding_Adapter(result, Customer_Outstanding.this);
                        Customer_Outstanding.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
                                recList.setAdapter(ca);
                                ca.notifyDataSetChanged();
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Customer_Outstanding.this, R.layout.autocomplete,
                                        All_customers);
                                autoCompleteTextView1.setThreshold(1);// will start working from
                                // first character
                                autoCompleteTextView1.setAdapter(adapter);// setting the adapter
                                // data into the
                                // AutoCompleteTextView

                                String s = String.format("%.2f", total_outstanding);
                                c_total_0us.setText(getResources().getString(R.string.Total_Outstanding) + s);


                            }
                        });
                    } else {
                        Customer_Outstanding.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Global_Data.Custom_Toast(Customer_Outstanding.this, getResources().getString(R.string.Sorry_No_Record_Found), "Yes");
//                                Toast toast = Toast.makeText(Customer_Outstanding.this, getResources().getString(R.string.Sorry_No_Record_Found), Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

                                Intent i = new Intent(Customer_Outstanding.this, MainActivity.class);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                        });
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            //dialog.dismiss();
            Customer_Outstanding.this.runOnUiThread(new Runnable() {
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

    public void online_OutstandingList() {

        SharedPreferences spf = Customer_Outstanding.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        //calendarn = Calendar.getInstance();
        //year = calendarn.get(Calendar.YEAR);
        loginDataBaseAdapter = new LoginDataBaseAdapter(Customer_Outstanding.this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        dialog = new ProgressDialog(Customer_Outstanding.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(getResources().getString(R.string.Please_Wait));
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
        String Cust_domain = sp.getString("Cust_Service_Url", "");
        String service_url = Cust_domain + "metal/api/v1/";
        String domain = service_url;
        String device_id = sp.getString("devid", "");
        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);
        Log.i("target url", "target url " + domain + "delivery_schedules/send_all_schedules?email=" + user_email);

        StringRequest jsObjRequest = null;
        jsObjRequest = new StringRequest(domain + "delivery_schedules/send_all_schedules?email=" + user_email, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;

                new Customer_Outstanding.outstanding_data().execute(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  dialog.dismiss();

                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Network Error","");
//                            Toast.makeText(getApplicationContext(),
//                                    "Network Error",
//                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Server AuthFailureError  Error","");
//                            Toast.makeText(getApplicationContext(),
//                                    "Server AuthFailureError  Error",
//                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    getResources().getString(R.string.Server_Errors),"");
//                            Toast.makeText(getApplicationContext(),
//                                    getResources().getString(R.string.Server_Errors),
//                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Network   Error","");
//                            Toast.makeText(getApplicationContext(),
//                                    "Network   Error",
//                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "ParseError   Error","");
//                            Toast.makeText(getApplicationContext(),
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG).show();
                        } else {
                            Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(),"");
                         //   Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        //  dialog.dismiss();
                        new CustomerASN().execute();
                        // finish();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 110000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        jsObjRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        requestQueue.add(jsObjRequest);
    }

    private class outstanding_data extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {


            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("result")) {
                    response_result = response.getString("result");
                } else {
                    response_result = "data";
                }


                if (response_result.equalsIgnoreCase("Record doesn't exist")) {


                    //Toast.makeText(Order.this, response_result, Toast.LENGTH_LONG).show();

                    Customer_Outstanding.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(Customer_Outstanding.this, response_result,"Yes");

//                            Toast toast = Toast.makeText(Customer_Outstanding.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            new CustomerASN().execute();
                        }
                    });


                } else {

                    JSONArray credit_profile = response.getJSONArray("credit_profiles");

                    Log.i("volley", "response reg credit_profile Length: " + credit_profile.length());
                    Log.d("volley", "credit_profile" + credit_profile.toString());

                    //
                    if (credit_profile.length() <= 0) {

                        Customer_Outstanding.this.runOnUiThread(new Runnable() {
                            public void run() {

                                new CustomerASN().execute();
//                                dialog.dismiss();
//                                //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();
//
//                                Toast toast = Toast.makeText(Customer_Outstanding.this, "Record Not Found.", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                            }
                        });
                    } else {

                        dbvoc.getDeletecredit_limitsAll();
                        int cout_check = 0;

                        total_outstanding = 0.0;
                        All_customers.clear();
                        Allresult.clear();

                        List<Customer_Info> result = new ArrayList<Customer_Info>();
                        for (int i = 0; i < credit_profile.length(); i++) {

                            JSONObject jsonObject = credit_profile.getJSONObject(i);

                            loginDataBaseAdapter.insert_credit_profile("", jsonObject.getString("customer_code"), jsonObject.getString("customer_code"), "", "", "", "", jsonObject.getString("credit_limit"), jsonObject.getString("amount_outstanding"), jsonObject.getString("amount_overdue"));

//                            Customer_Info ci = new Customer_Info();
//
//                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("amount_outstanding"))) {
//                                cout_check = 1;
//                                Double amt_outstanding = ((Double.valueOf(jsonObject.getString("amount_outstanding"))));
//
//                                Double amt_overdue = ((Double.valueOf(jsonObject.getString("amount_overdue"))));
//
//                                ci.amount1 = String.valueOf(Html.fromHtml("<b>" + "Amount Outstanding : " + "</b>" + String.format("%.2f", amt_outstanding)));
//
//                                ci.amount2 = String.valueOf(Html.fromHtml("<b>" + "Amount Overdue : " + "</b>" + String.format("%.2f", amt_overdue)));
//
//                                total_outstanding += amt_outstanding;
//
//                                ci.name = String.valueOf(Html.fromHtml("<b>" + "Name : " + "</b>" + ""));
//
//                                ci.name = String.valueOf(Html.fromHtml(jsonObject.getString("customer_code")));
//                               // Customer_id = jsonObject.getString("customer_code");
//                                ci.cus_code = jsonObject.getString("customer_code");
//
//
//                                All_customers.add(jsonObject.getString("customer_code"));
//
//                               // ci.shop_name = "";
//
//                                result.add(ci);
//                                Allresult.add(ci);
//
//
//                            }

                            Customer_Outstanding.this.runOnUiThread(new Runnable() {
                                public void run() {

//                                    dialog.dismiss();
//                                    dialog = new ProgressDialog(Customer_Outstanding.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                                    dialog.setMessage("Please wait Customer Loading....");
//                                    dialog.setTitle("Metal App");
//                                    dialog.setCancelable(false);
//                                    dialog.show();

                                    new CustomerASN().execute();
                                }
                            });


                        }

//                        if (cout_check == 1) {
//                            ca = new Customer_Outstanding_Adapter(result, Customer_Outstanding.this);
//                            Customer_Outstanding.this.runOnUiThread(new Runnable() {
//                                public void run() {
//                                    dialog.dismiss();
//                                    Toast.makeText(Customer_Outstanding.this, "Outstanding record load successfully", Toast.LENGTH_SHORT).show();
//                                    recList.setAdapter(ca);
//                                    ca.notifyDataSetChanged();
//                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Customer_Outstanding.this, android.R.layout.simple_spinner_dropdown_item,
//                                            All_customers);
//                                    autoCompleteTextView1.setThreshold(1);// will start working from
//                                    // first character
//                                    autoCompleteTextView1.setAdapter(adapter);// setting the adapter
//                                    // data into the
//                                    // AutoCompleteTextView
//                                    autoCompleteTextView1.setTextColor(Color.BLACK);
//
//                                    String s = String.format("%.2f", total_outstanding);
//                                    c_total_0us.setText("Total Outstanding : " + s);
//
//
//                                }
//                            });
//                        } else {
//                            Customer_Outstanding.this.runOnUiThread(new Runnable() {
//                                public void run() {
//                                    Toast toast = Toast.makeText(Customer_Outstanding.this, "Sorry No Record Found.", Toast.LENGTH_SHORT);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//
//                                    Intent i = new Intent(Customer_Outstanding.this, MainActivity.class);
//                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(i);
//                                    finish();
//                                }
//                            });
//                        }

                    }

                    Customer_Outstanding.this.runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
                        }
                    });
                    //	dialog.dismiss();

                    //finish();

                }


                // }

                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();


                Customer_Outstanding.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                        dialog = new ProgressDialog(Customer_Outstanding.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage(getResources().getString(R.string.Please_Wait));
                        dialog.setTitle(getResources().getString(R.string.app_name));
                        dialog.setCancelable(false);
                        dialog.show();

                        new CustomerASN().execute();
                    }
                });

            }


            Customer_Outstanding.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            Customer_Outstanding.this.runOnUiThread(new Runnable() {
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

}

