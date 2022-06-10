package com.msimplelogic.activities;

/**
 * Created by vinod on 04-10-2016.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msimplelogic.activities.R;

import com.msimplelogic.activities.kotlinFiles.AddCustomer;
import com.msimplelogic.webservice.ConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.simplelogic.helper.Customer_Info;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class Customer_info_main extends BaseActivity {
    HashMap<String, String> hm = new HashMap<String, String>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> badapter;
    Spinner sortby_beat;
    Spinner dcus_category;
    List<Customer_Info> Allresult = new ArrayList<Customer_Info>();
    //  List<Customer_Info> Allresult_Glo = new ArrayList<Customer_Info>();
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    ProgressDialog dialog;
    ImageView hedder_theame;
    ProgressDialog dialog1;
    RecyclerView recList;
    String Customer_id = "";
    String City_id = "";
    String Beat_id = "";
    private String[] arraySpinner;
    private SearchView mSearchView;
    Customer_info_main_adapter ca;
    String s[];
    SharedPreferences sp;
    ArrayList<String> All_customers = new ArrayList<String>();
    //Double amt_outstanding;
    AutoCompleteTextView autoCompleteTextView1;
    Button filter_submit, filter_clear;
    ImageView filter_btn, close_filter, cash_Reportmenu;
    LinearLayout filter_layout;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Toolbar toolbar;
    String inte="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        //setTitle(getResources().getString(R.string.CUSTOMER_PROFILE));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        inte = getIntent().getStringExtra("intent");

        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        recList = (RecyclerView) findViewById(R.id.c_info_card);
      //  filter_btn = (ImageView) findViewById(R.id.filter_btn);
        filter_btn = findViewById(R.id.filter_btn);
        filter_layout = (LinearLayout) findViewById(R.id.filter_layout);
        cash_Reportmenu = (ImageView) findViewById(R.id.cash_Reportmenu);
        hedder_theame = (ImageView) findViewById(R.id.hedder_theame);
        cd = new ConnectionDetector(getApplicationContext());
        // recList.addItemDecoration(new DividerItemDecoration(Customer_info_main.this, DividerItemDecoration.VERTICAL_LIST));

        Global_Data.image_counter = 0;
        recList.setHasFixedSize(true);


        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1){
            hedder_theame.setImageResource(R.drawable.dark_hedder);
            filter_btn.setImageResource(R.drawable.filterordertype_icon_dark);

        }
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        SharedPreferences spf = Customer_info_main.this.getSharedPreferences("SimpleLogic", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("order", "new");
        editor.commit();

        Global_Data.GlobeloPname = "";
        Global_Data.GlobeloPAmount = "";
        if (Global_Data.Globelo_OU_CUSTID.equalsIgnoreCase("") && Global_Data.Globelo_OU_CUST_name.equalsIgnoreCase("")) {
            Global_Data.G_BEAT_service_flag = "";

            if (Global_Data.G_RadioG_valueC.equalsIgnoreCase(getResources().getString(R.string.Show_All)) && !Global_Data.G_BEAT_VALUEC.equalsIgnoreCase(getResources().getString(R.string.Sort_by_Beat))) {
                //  Global_Data.G_RadioG_valueC = "Show Outstanding";
                Global_Data.G_BEAT_service_flag = "beat";
                dialog1 = new ProgressDialog(Customer_info_main.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                dialog1.setMessage(getResources().getString(R.string.customer_loading_dialog_message));
                dialog1.setTitle(getResources().getString(R.string.app_name));
                dialog1.setCancelable(false);
                dialog1.show();
                //dialog.dismiss();
                new CustomerASN().execute();

            } else if (!Global_Data.G_RadioG_valueC.equalsIgnoreCase(getResources().getString(R.string.Show_All)) && !Global_Data.G_RadioG_valueC.equalsIgnoreCase("")) {
                dialog1 = new ProgressDialog(Customer_info_main.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                dialog1.setMessage(getResources().getString(R.string.customer_loading_dialog_message));
                dialog1.setTitle(getResources().getString(R.string.app_name));
                dialog1.setCancelable(false);
                dialog1.show();
                //dialog.dismiss();
                new CustomerASN_Filter().execute();

            } else {
                Global_Data.G_BEAT_service_flag = "";
                dialog1 = new ProgressDialog(Customer_info_main.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                dialog1.setMessage(getResources().getString(R.string.customer_loading_dialog_message));
                dialog1.setTitle(getResources().getString(R.string.app_name));
                dialog1.setCancelable(false);
                dialog1.show();
                //dialog.dismiss();
                new CustomerASN().execute();
            }
        } else {
            if (!Global_Data.Globelo_OU_CUSTID.equalsIgnoreCase("")) {
                getcus_details(Global_Data.Globelo_OU_CUSTID);
            } else {
                getcus_detailsn(Global_Data.Globelo_OU_CUST_name);
            }
        }

        filter_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("List size", "L SIZE" + Allresult.size());
                FilterDialog();
            }
        });

        cash_Reportmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReporyListDialog();
            }
        });

        autoCompleteTextView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (autoCompleteTextView1.getRight() - autoCompleteTextView1.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        View view = Customer_info_main.this.getCurrentFocus();
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

                    dialog1 = new ProgressDialog(Customer_info_main.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    dialog1.setMessage(getResources().getString(R.string.customer_loading_dialog_message));
                    dialog1.setTitle(getResources().getString(R.string.app_name));
                    dialog1.setCancelable(false);
                    dialog1.show();


                    new CustomerASN().execute();
//                    ca = new Customer_info_main_adapter(Allresult,Customer_info_main.this);
//                    recList.setAdapter(ca);
//                    ca.notifyDataSetChanged();
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

                Global_Data.hideSoftKeyboard(Customer_info_main.this);

                String customer_name = "";
                String address_type = "";
                if (autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0) {
                    s = autoCompleteTextView1.getText().toString().trim().split(":");
                    customer_name = s[0].trim();
                    address_type = s[1].trim();
                } else {
                    customer_name = autoCompleteTextView1.getText().toString().trim();
                }

                Log.d("CUSTOMER", "CUSTOMER CALL");
//
//
//				Global_Data.credit_limit_amount = "";
//				Global_Data.outstandings_amount = "";

                List<Local_Data> contacts = dbvoc.getCustomerCode(customer_name);

                Customer_Info ci = new Customer_Info();
                List<Customer_Info> result = new ArrayList<Customer_Info>();
                if (contacts.size() <= 0) {
                    Global_Data.Custom_Toast(Customer_info_main.this, getResources().getString(R.string.Customer_Not_Found),"Yes");
//                    Toast toast = Toast.makeText(Customer_info_main.this,
//                            getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                } else {
                    for (Local_Data cn : contacts) {

                        recList.setVisibility(View.VISIBLE);
                        ci.name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.CName) + "</b>" + cn.getCUSTOMER_NAME()));
                        ci.mobile = cn.getMOBILE_NO();
                        ci.shop_name = cn.getCUSTOMER_SHOPNAME();
                        ci.address = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.CAddress) + "</b>" + cn.getAddress()));
                        ci.latlong = cn.getlatitude() + "," + cn.getlongitude();
                        Customer_id = cn.getCust_Code();
                        ci.cus_code = cn.getCust_Code();
                        City_id = cn.getCITY_ID();
                        Beat_id = cn.getBEAT_ID();

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getDcus_business_type())) {
                            ci.c_business_type = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Business_Type) + "</b>" + cn.getDcus_business_type()));
                        } else {
                            ci.c_business_type = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Business_Type) + "</b>" + ""));
                        }
                    }

                    List<Local_Data> cityi = dbvoc.getcityByState_idn(City_id);
                    if (cityi.size() > 0) {
                        for (Local_Data cnnn : cityi) {
                            // ci.city_name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.City_Name) + "</b>" + cnnn.getCityName()));
                            ci.city_name = String.valueOf(Html.fromHtml( "</b>" + cnnn.getCityName()));
                        }
                    }

                    List<Local_Data> beati = dbvoc.getbeatByCityIDn(Beat_id);
                    if (beati.size() > 0) {
                        for (Local_Data cnnnn : beati) {
                            //  ci.Beat_name = cnnnn.getCityName();
                            //  ci.Beat_name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Beat_Name) + "</b>" + cnnnn.getCityName()));
                            ci.Beat_name = String.valueOf(cnnnn.getCityName());
                        }
                    }

                    List<Local_Data> contactlimit = dbvoc.getCreditprofileData(Customer_id);
                    if (contactlimit.size() > 0) {
                        for (Local_Data cnn : contactlimit) {

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.get_credit_limit())) {
                                Double credit_limit = ((Double.valueOf(cnn.get_credit_limit())));
                                //  ci.credit_limit = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit) + "</b>" + String.format("%.2f", credit_limit)));
                                ci.credit_limit = String.valueOf(credit_limit);
                            } else {
                                //   ci.credit_limit = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit_Not_Found) + "</b>"));
                                ci.credit_limit = String.valueOf( "0.0");
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.get_shedule_outstanding_amount())) {
                                Global_Data.amt_outstanding = ((Double.valueOf(cnn.get_shedule_outstanding_amount())));
                                //  ci.amount1 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding) + "</b>" + String.format("%.2f", Global_Data.amt_outstanding)));
                                ci.amount1 = String.valueOf( Global_Data.amt_outstanding);


                            } else {

                                //   ci.amount1 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding_Not_Found) + "</b>"));
                                ci.amount1 = String.valueOf("0.0");
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.getAmmount_overdue())) {
                                Double amt_overdue = ((Double.valueOf(cnn.getAmmount_overdue())));
                                //  ci.amount2 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue) + "</b>" + String.format("%.2f", amt_overdue)));
                                ci.amount2 = String.valueOf( amt_overdue);


                            } else {
                                //  ci.amount2 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue_Not_Found) + "</b>"));
                                ci.amount2 = String.valueOf("0.0");
                            }
                        }
                    } else {
                        ci.credit_limit = String.valueOf( "0.0" );
                        ci.amount1 = String.valueOf("0.0");
                        ci.amount2 = String.valueOf( "0.0" );
                    }

                    result.add(ci);

                    ca = new Customer_info_main_adapter(result, Customer_info_main.this);
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
                SharedPreferences sp = Customer_info_main.this.getSharedPreferences("SimpleLogic", 0);
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
        //dialog.d

        if (Global_Data.addcustomerintent=="add_cust"&& Global_Data.addcustomerintent!=""){
            Intent i = new Intent(Customer_info_main.this, AddCustomer.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            Global_Data.addcustomerintent="";
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();

        }
        else if(Global_Data.customeroutstanding=="yes"&& Global_Data.customeroutstanding!="") {
            Intent i = new Intent(Customer_info_main.this, Customer_Outstanding.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            Global_Data.addcustomerintent="";
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();

        }

        else {
            Global_Data.G_BEAT_IDC = "";
            Global_Data.G_BEAT_VALUEC = "";
            Global_Data.G_BEAT_service_flag = "";
            Global_Data.G_RadioG_valueC = "";
            Global_Data.G_CBUSINESS_TYPE = "";

            Intent i = new Intent(Customer_info_main.this, Sales_Dash.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

    }

    private class CustomerASN extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... response) {

            List<Local_Data> contacts3 = dbvoc.getcustomer_pre_data();

            if (contacts3.size() <= 0) {
                // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

                Customer_info_main.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Global_Data.Custom_Toast(Customer_info_main.this, getResources().getString(R.string.Sorry_No_Record_Found), "yes");

                    }
                });

                dialog1.dismiss();
                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
            } else {
                Customer_info_main.this.runOnUiThread(new Runnable() {
                    public void run() {
                        recList.setVisibility(View.VISIBLE);
                    }
                });

                All_customers.clear();
                List<Customer_Info> result = new ArrayList<Customer_Info>();
                for (Local_Data cn : contacts3) {

                    Customer_Info ci = new Customer_Info();
                    ci.name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.CName) + "</b>" + cn.getCUSTOMER_NAME()));
                    ci.mobile = cn.getMOBILE_NO();
                    ci.shop_name = cn.getCUSTOMER_SHOPNAME();
                    ci.address = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.CAddress) + "</b>" + cn.getAddress()));
                    ci.latlong = cn.getlatitude() + "," + cn.getlongitude();
                    Customer_id = cn.getCust_Code();
                    City_id = cn.getCITY_ID();
                    ci.cus_code = cn.getCust_Code();

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getDcus_business_type())) {
                        ci.c_business_type = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Business_Type) + "</b>" + cn.getDcus_business_type()));
                    } else {
                        //ci.c_business_type = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Business_Type) + "</b>" + ""));
                        ci.c_business_type = "";
                    }

                    Beat_id = cn.getBEAT_ID();

                    All_customers.add(cn.getCUSTOMER_SHOPNAME());

                    //ci.city_name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.City_Name) + "</b>" + cn.getCityName()));
                    ci.city_name = String.valueOf( cn.getCityName());

                    // ci.Beat_name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Beat_Name) + "</b>" + cn.getBeatName()));
                    ci.Beat_name = String.valueOf( cn.getBeatName());

//                    List<Local_Data> cityi = dbvoc.getcityByState_idn(City_id);
//                    if(cityi.size() > 0)
//                    {
//                        for (Local_Data cnnn : cityi) {
//                            ci.city_name = String.valueOf(Html.fromHtml("<b>" +"City Name : "+ "</b>"+cnnn.getCityName()));
//                        }
//                    }

//                    List<Local_Data> beati = dbvoc.getbeatByCityIDn(Beat_id);
//                    if(beati.size() > 0)
//                    {
//                        for (Local_Data cnnnn : beati) {
//                            //  ci.Beat_name = cnnnn.getCityName();
//                            ci.Beat_name = String.valueOf(Html.fromHtml("<b>" +"Beat Name : "+ "</b>"+cnnnn.getCityName()));
//                        }
//                    }

                    List<Local_Data> contactlimit = dbvoc.getCreditprofileData(Customer_id);
                    if (contactlimit.size() > 0) {

                        for (Local_Data cnn : contactlimit) {

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.get_credit_limit())) {
                                Double credit_limit = ((Double.valueOf(cnn.get_credit_limit())));
                                //     ci.credit_limit = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit) + "</b>" + String.format("%.2f", credit_limit)));
                                ci.credit_limit = String.valueOf(credit_limit);
                            } else {
                                //  ci.credit_limit = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit_Not_Found) + "</b>"));
                                ci.credit_limit = String.valueOf( "0.0" );
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.get_shedule_outstanding_amount())) {
                                Global_Data.amt_outstanding = ((Double.valueOf(cnn.get_shedule_outstanding_amount())));
                                // ci.amount1 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding) + "</b>" + String.format("%.2f", Global_Data.amt_outstanding)));
                                ci.amount1 = String.valueOf( Global_Data.amt_outstanding);

                            } else {

                                //  ci.amount1 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding_Not_Found) + "</b>"));
                                ci.amount1 = String.valueOf("0.0" );
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.getAmmount_overdue())) {
                                Double amt_overdue = ((Double.valueOf(cnn.getAmmount_overdue())));
                                //          ci.amount2 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue) + "</b>" + String.format("%.2f", amt_overdue)));
                                ci.amount2 = String.valueOf( amt_overdue);

                            } else {
                                //     ci.amount2 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue_Not_Found) + "</b>"));
                                ci.amount2 = String.valueOf( "0.0" );
                            }
                        }
                    } else {
                        // ci.credit_limit = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit_Not_Found) + "</b>"));
                        ci.credit_limit = String.valueOf("0.0");
                        ci.amount1 = String.valueOf("0.0");
                        ci.amount2 = String.valueOf( "0.0" );
                    }

                    result.add(ci);
                    if (Allresult.isEmpty()) {
                        Allresult.add(ci);
                    }

                }
                ca = new Customer_info_main_adapter(result, Customer_info_main.this);
                Customer_info_main.this.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog1.dismiss();
                        recList.setAdapter(ca);
                        ca.notifyDataSetChanged();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Customer_info_main.this, R.layout.autocomplete,
                                All_customers);
                        autoCompleteTextView1.setThreshold(1);// will start working from
                        // first character
                        autoCompleteTextView1.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                    }
                });
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            //dialog.dismiss();
            Customer_info_main.this.runOnUiThread(new Runnable() {
                public void run() {
                    dialog1.dismiss();
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


    void FilterDialog() {
        final Dialog dialog = new Dialog(Customer_info_main.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.customer_filter);

        filter_submit = (Button) dialog.findViewById(R.id.filter_submit);
        filter_clear = (Button) dialog.findViewById(R.id.filter_clear);
        final RadioGroup cashradioGroup = (RadioGroup) dialog.findViewById(R.id.cashradioGroup);
        final RadioButton radio_showall = (RadioButton) dialog.findViewById(R.id.radio_showall);
        RadioButton radio_overdue = (RadioButton) dialog.findViewById(R.id.radio_overdue);
        RadioButton radio_outstanding = (RadioButton) dialog.findViewById(R.id.radio_outstanding);
        close_filter = (ImageView) dialog.findViewById(R.id.close_filter);
        sortby_beat = (Spinner) dialog.findViewById(R.id.sortby_beat);
        dcus_category = (Spinner) dialog.findViewById(R.id.dcus_category);

        cashradioGroup.check(radio_showall.getId());

        if (Global_Data.G_RadioG_valueC.equalsIgnoreCase(getResources().getString(R.string.Show_Overdue))) {
            cashradioGroup.check(radio_overdue.getId());
        } else if (Global_Data.G_RadioG_valueC.equalsIgnoreCase(getResources().getString(R.string.Show_Outstanding))) {
            cashradioGroup.check(radio_outstanding.getId());
        }

        ArrayList<String> beat_array = new ArrayList<String>();
        ArrayList<String> btype_array = new ArrayList<String>();
        hm.clear();
        btype_array.add(getResources().getString(R.string.FBusiness_Type));
        beat_array.add(getResources().getString(R.string.Sort_by_Beat));

        List<Local_Data> customer_cate = dbvoc.get_Castomer_btype();
        for (Local_Data cn : customer_cate) {

            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getDcus_business_type())) {
                btype_array.add(cn.getDcus_business_type());
            }
        }

        List<Local_Data> contacts2 = dbvoc.get_BeatsData_custFilter();
        for (Local_Data cn : contacts2) {

            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getBeatName())) {
                beat_array.add(cn.getBeatName());
                hm.put(cn.getBeatName(), cn.getBEAT_ID());
            }
        }

        badapter = new ArrayAdapter<String>(this, R.layout.spiiner_layout_theame, btype_array);
        badapter.setDropDownViewResource(R.layout.spinner_item);
        dcus_category.setAdapter(badapter);

        adapter = new ArrayAdapter<String>(this, R.layout.spiiner_layout_theame, beat_array);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        sortby_beat.setAdapter(adapter);

        if (!Global_Data.G_CBUSINESS_TYPE.equals(null) && !Global_Data.G_CBUSINESS_TYPE.equals("")) {
            int spinnerPosition = badapter.getPosition(Global_Data.G_CBUSINESS_TYPE);
            dcus_category.setSelection(spinnerPosition);
        }

        if (!Global_Data.G_BEAT_VALUEC.equals(null) && !Global_Data.G_BEAT_VALUEC.equals("")) {
            int spinnerPosition = adapter.getPosition(Global_Data.G_BEAT_VALUEC);
            sortby_beat.setSelection(spinnerPosition);
        }

        dcus_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item

                if (!selectedItem.equalsIgnoreCase(getResources().getString(R.string.FBusiness_Type))) {

                    Global_Data.G_CBUSINESS_TYPE = selectedItem.trim();
                } else {

                    Global_Data.G_CBUSINESS_TYPE = "";
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sortby_beat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item

                if (!selectedItem.equalsIgnoreCase(getResources().getString(R.string.Sort_by_Beat))) {
                    Object value = hm.get(selectedItem);
                    Global_Data.G_BEAT_IDC = value.toString();
                    Global_Data.G_BEAT_VALUEC = selectedItem.trim();
                } else {
                    Global_Data.G_BEAT_IDC = "";
                    Global_Data.G_BEAT_service_flag = "";
                    Global_Data.G_BEAT_VALUEC = selectedItem.trim();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filter_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioButtonID = cashradioGroup.getCheckedRadioButtonId();
                View radioButton = cashradioGroup.findViewById(radioButtonID);
                int idx = cashradioGroup.indexOfChild(radioButton);

                RadioButton r = (RadioButton) cashradioGroup.getChildAt(idx);
                String selectedtext = r.getText().toString();

                Log.d("Radio Value", "Radio value" + selectedtext);

                Global_Data.G_RadioG_valueC = selectedtext.trim();

                autoCompleteTextView1.setText("");


                if (Global_Data.G_RadioG_valueC.equalsIgnoreCase(getResources().getString(R.string.Show_Overdue))) {
                    Global_Data.G_RadioG_valueC = "Show Overdue";
                } else if (Global_Data.G_RadioG_valueC.equalsIgnoreCase(getResources().getString(R.string.Show_Outstanding))) {
                    Global_Data.G_RadioG_valueC = "Show Outstanding";
                }


                if (Global_Data.G_RadioG_valueC.equalsIgnoreCase(getResources().getString(R.string.Show_All)) && !Global_Data.G_BEAT_VALUEC.equalsIgnoreCase(getResources().getString(R.string.Sort_by_Beat)) && !Global_Data.G_BEAT_VALUEC.equalsIgnoreCase("")) {
                    Global_Data.G_RadioG_valueC = "Show All";
                    Global_Data.G_BEAT_service_flag = "beat";
                    dialog1.setMessage(getResources().getString(R.string.customer_loading_dialog_message));
                    dialog1.setTitle(getResources().getString(R.string.app_name));
                    dialog1.setCancelable(false);
                    dialog1.show();
                    dialog.dismiss();
                    new CustomerASN().execute();

                } else if (Global_Data.G_RadioG_valueC.equalsIgnoreCase(getResources().getString(R.string.Show_All)) && (Global_Data.G_BEAT_VALUEC.equalsIgnoreCase(getResources().getString(R.string.Sort_by_Beat)) || Global_Data.G_BEAT_VALUEC.equalsIgnoreCase(""))) {
                    Global_Data.G_RadioG_valueC = "Show All";
                    Log.d("No Filter", "No Filter");
                    Global_Data.G_BEAT_service_flag = "";
                    dialog1.setMessage(getResources().getString(R.string.customer_loading_dialog_message));
                    dialog1.setTitle(getResources().getString(R.string.app_name));
                    dialog1.setCancelable(false);
                    dialog1.show();
                    new CustomerASN().execute();
                    dialog.dismiss();
                } else {

                    if (Global_Data.G_RadioG_valueC.equalsIgnoreCase(getResources().getString(R.string.Show_Overdue))) {
                        Global_Data.G_RadioG_valueC = "Show Overdue";
                    } else if (Global_Data.G_RadioG_valueC.equalsIgnoreCase(getResources().getString(R.string.Show_Outstanding))) {
                        Global_Data.G_RadioG_valueC = "Show Outstanding";
                    }

                    List<Local_Data> contacts3 = dbvoc.getcustomer_pre_data_filter();

                    if (contacts3.size() <= 0) {

                        Global_Data.Custom_Toast(Customer_info_main.this, getResources().getString(R.string.Sorry_No_Record_Found), "yes");

                        dialog.dismiss();

//                        dialog1 = new ProgressDialog(Customer_info_main.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                        dialog1.setMessage("Please wait Customer Loading....");
//                        dialog1.setTitle("Metal App");
//                        dialog1.setCancelable(false);
//                        dialog1.show();
//                        dialog.dismiss();

                        // new CustomerASN().execute();

                    } else {
//                        dialog1 = new ProgressDialog(Customer_info_main.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//
//                        dialog1.setMessage("Please wait Customer Loading....");
//                        dialog1.setTitle("Metal App");
//                        dialog1.setCancelable(false);
//                        dialog1.show();
                        dialog.dismiss();
                        Log.d("List size", "L SIZE" + Allresult.size());
                        new CustomerASN_Filter().execute();
                    }


                }

            }
        });

        filter_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Global_Data.G_BEAT_IDC = "";
                Global_Data.G_BEAT_VALUEC = "";
                Global_Data.G_BEAT_service_flag = "";
                Global_Data.G_RadioG_valueC = "";

                Global_Data.G_CBUSINESS_TYPE = "";

                int spinnerPosition = adapter.getPosition(getResources().getString(R.string.Sort_by_Beat));
                sortby_beat.setSelection(spinnerPosition);

                int spinnerPosition2 = badapter.getPosition(getResources().getString(R.string.FBusiness_Type));
                dcus_category.setSelection(spinnerPosition2);

                cashradioGroup.check(radio_showall.getId());

                int radioButtonID = cashradioGroup.getCheckedRadioButtonId();
                View radioButton = cashradioGroup.findViewById(radioButtonID);
                int idx = cashradioGroup.indexOfChild(radioButton);
                RadioButton r = (RadioButton) cashradioGroup.getChildAt(idx);
                String selectedtext = r.getText().toString();

                if (!selectedtext.equalsIgnoreCase(getResources().getString(R.string.Show_All))) {
                    Global_Data.G_BEAT_service_flag = "";
                    dialog1.setMessage(getResources().getString(R.string.customer_loading_dialog_message));
                    dialog1.setTitle(getResources().getString(R.string.app_name));
                    dialog1.setCancelable(false);
                    dialog1.show();
                    new CustomerASN().execute();
                }


            }
        });

        close_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    void ReporyListDialog() {
        final Dialog dialog = new Dialog(Customer_info_main.this);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.LEFT | Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        //WindowManager.LayoutParams WMLP = dialog.getWindow().getAttributes();

//        WMLP.x = 100;   //x position
//        WMLP.y = 100;   //y position
//
//        dialog.getWindow().setAttributes(WMLP);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.profile_menu_dialog);
        ImageView close_menu = (ImageView) dialog.findViewById(R.id.close_menu);

        RelativeLayout collect_state = (RelativeLayout) dialog.findViewById(R.id.collect_state);
        RelativeLayout deposit_state = (RelativeLayout) dialog.findViewById(R.id.deposit_state);
        RelativeLayout pending_trans = (RelativeLayout) dialog.findViewById(R.id.pending_trans);
        RelativeLayout deposit_trans = (RelativeLayout) dialog.findViewById(R.id.deposit_trans);

        collect_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer_info_main.this, CollectionStatement.class);
                intent.putExtra("title", getResources().getString(R.string.Collection_Statement));
                intent.putExtra("text1", getResources().getString(R.string.Dates));
                intent.putExtra("text2", getResources().getString(R.string.Collection_Statement));
                startActivity(intent);
            }
        });

        deposit_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer_info_main.this, CollectionStatement.class);
                intent.putExtra("title", getResources().getString(R.string.Deposit_Statement));
                intent.putExtra("text1", getResources().getString(R.string.Deposit_Details));
                intent.putExtra("text2", getResources().getString(R.string.Collection_Statement));
                startActivity(intent);
            }
        });

        pending_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Customer_info_main.this, CollectionStatement.class);
                intent.putExtra("title", getResources().getString(R.string.Pending_Transactions));
                intent.putExtra("text1", getResources().getString(R.string.Time_Left));
                intent.putExtra("text2", getResources().getString(R.string.Deposit_Details));
                startActivity(intent);
            }
        });

        deposit_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    Intent csub = new Intent(Customer_info_main.this, Cash_Submit.class);
                    startActivity(csub);
                } else {

                    //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
                    Global_Data.Custom_Toast(Customer_info_main.this, getResources().getString(R.string.internet_connection_error),"Yes");
//                    Toast toast = Toast.makeText(Customer_info_main.this, getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                }
            }
        });

        close_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private class CustomerASN_Filter extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... response) {

            Log.d("List size", "L SIZE" + Allresult.size());

            List<Local_Data> contacts3 = dbvoc.getcustomer_pre_data_filter();

            if (contacts3.size() <= 0) {
                // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

                Customer_info_main.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog1.dismiss();
                        Global_Data.Custom_Toast(Customer_info_main.this, getResources().getString(R.string.Sorry_No_Record_Found), "yes");


                    }
                });


            } else {
                Customer_info_main.this.runOnUiThread(new Runnable() {
                    public void run() {
                        recList.setVisibility(View.VISIBLE);
                    }
                });


                All_customers.clear();
                List<Customer_Info> result = new ArrayList<Customer_Info>();
                for (Local_Data cn : contacts3) {

                    Customer_Info ci = new Customer_Info();
                    ci.name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.CName) + "</b>" + cn.getCUSTOMER_NAME()));
                    ci.mobile = cn.getMOBILE_NO();
                    ci.shop_name = cn.getCUSTOMER_SHOPNAME();
                    ci.address = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.CAddress) + "</b>" + cn.getAddress()));
                    ci.latlong = cn.getlatitude() + "," + cn.getlongitude();
                    Customer_id = cn.getCust_Code();
                    City_id = cn.getCITY_ID();
                    ci.cus_code = cn.getCust_Code();

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getDcus_business_type())) {
                        ci.c_business_type = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Business_Type) + "</b>" + cn.getDcus_business_type()));
                    } else {
                        ci.c_business_type = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Business_Type) + "</b>" + ""));
                    }
                    Beat_id = cn.getBEAT_ID();

                    All_customers.add(cn.getCUSTOMER_SHOPNAME());

                    //   ci.city_name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.City_Name) + "</b>" + cn.getCityName()));
                    ci.city_name = String.valueOf( cn.getCityName());

                    //   ci.Beat_name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Beat_Name) + "</b>" + cn.getBeatName()));
                    ci.Beat_name = String.valueOf( cn.getBeatName());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.get_credit_limit())) {
                        Double credit_limit = ((Double.valueOf(cn.get_credit_limit())));
                        // ci.credit_limit = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit) + "</b>" + String.format("%.2f", credit_limit)));
                        ci.credit_limit = String.valueOf( credit_limit);
                    } else {
                        //  ci.credit_limit = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit_Not_Found) + "</b>"));
                        ci.credit_limit = String.valueOf("0.0" );
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.get_shedule_outstanding_amount())) {
                        Global_Data.amt_outstanding = ((Double.valueOf(cn.get_shedule_outstanding_amount())));
                        // ci.amount1 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding) + "</b>" + String.format("%.2f", Global_Data.amt_outstanding)));
                        ci.amount1 = String.valueOf( Global_Data.amt_outstanding);


                    } else {

                        // ci.amount1 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding_Not_Found) + "</b>"));
                        ci.amount1 = String.valueOf("0.0");
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getAmmount_overdue())) {
                        Double amt_overdue = ((Double.valueOf(cn.getAmmount_overdue())));
                        //    ci.amount2 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue) + "</b>" + String.format("%.2f", amt_overdue)));
                        ci.amount2 = String.valueOf( amt_overdue);


                    } else {
                        //   ci.amount2 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue_Not_Found) + "</b>"));
                        ci.amount2 = String.valueOf("0.0" );

                    }

                    result.add(ci);
                    if (Allresult.isEmpty()) {
                        Allresult.add(ci);
                    }
                }
                ca = new Customer_info_main_adapter(result, Customer_info_main.this);
                Customer_info_main.this.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog1.dismiss();
                        recList.setAdapter(ca);
                        ca.notifyDataSetChanged();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Customer_info_main.this, R.layout.autocomplete,
                                All_customers);
                        autoCompleteTextView1.setThreshold(1);// will start working from
                        // first character
                        autoCompleteTextView1.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        dialog1.dismiss();
                    }
                });
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            //dialog.dismiss();
            Customer_info_main.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
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

    public void getcus_details(String Cust_ID) {
        List<Local_Data> contacts = dbvoc.getCustomerCodeOUS(Cust_ID);

        Global_Data.Globelo_OU_CUSTID = "";
        Customer_Info ci = new Customer_Info();
        List<Customer_Info> result = new ArrayList<Customer_Info>();
        if (contacts.size() <= 0) {
            Global_Data.Custom_Toast(Customer_info_main.this,
                    getResources().getString(R.string.customer_notfound_message),"Yes");
//            Toast toast = Toast.makeText(Customer_info_main.this,
//                    getResources().getString(R.string.customer_notfound_message), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        } else {
            for (Local_Data cn : contacts) {

                recList.setVisibility(View.VISIBLE);
                ci.name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.CName) + "</b>" + cn.getCUSTOMER_NAME()));
                ci.mobile = cn.getMOBILE_NO();
                ci.shop_name = cn.getCUSTOMER_SHOPNAME();
                autoCompleteTextView1.setText(ci.shop_name);
                ci.address = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.CAddress) + "</b>" + cn.getAddress()));
                ci.latlong = cn.getlatitude() + "," + cn.getlongitude();
                Customer_id = cn.getCust_Code();
                ci.cus_code = cn.getCust_Code();
                City_id = cn.getCITY_ID();
                Beat_id = cn.getBEAT_ID();

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getDcus_business_type())) {
                    ci.c_business_type = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Business_Type) + "</b>" + cn.getDcus_business_type()));
                } else {
                    ci.c_business_type = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Business_Type) + "</b>" + ""));
                }


            }

            List<Local_Data> cityi = dbvoc.getcityByState_idn(City_id);
            if (cityi.size() > 0) {
                for (Local_Data cnnn : cityi) {
                    //  ci.city_name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.City_Name) + "</b>" + cnnn.getCityName()));
                    ci.city_name = String.valueOf( cnnn.getCityName());
                }
            }

            List<Local_Data> beati = dbvoc.getbeatByCityIDn(Beat_id);
            if (beati.size() > 0) {
                for (Local_Data cnnnn : beati) {
                    //  ci.Beat_name = cnnnn.getCityName();
                    //  ci.Beat_name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Beat_Name) + "</b>" + cnnnn.getCityName()));
                    ci.Beat_name = String.valueOf( cnnnn.getCityName());
                }
            }

            List<Local_Data> contactlimit = dbvoc.getCreditprofileData(Customer_id);
            if (contactlimit.size() > 0) {

                for (Local_Data cnn : contactlimit) {

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.get_credit_limit())) {
                        Double credit_limit = ((Double.valueOf(cnn.get_credit_limit())));
                        //  ci.credit_limit = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit) + "</b>" + String.format("%.2f", credit_limit)));
                        ci.credit_limit = String.valueOf(credit_limit);
                    } else {
                        // ci.credit_limit = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit_Not_Found) + "</b>"));
                        ci.credit_limit = String.valueOf("0.0" );
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.get_shedule_outstanding_amount())) {
                        Global_Data.amt_outstanding = ((Double.valueOf(cnn.get_shedule_outstanding_amount())));
                        //  ci.amount1 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding) + "</b>" + String.format("%.2f", Global_Data.amt_outstanding)));
                        ci.amount1 = String.valueOf(Global_Data.amt_outstanding);


                    } else {

                        // ci.amount1 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding_Not_Found) + "</b>"));
                        ci.amount1 = String.valueOf( "0.0" );
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.getAmmount_overdue())) {
                        Double amt_overdue = ((Double.valueOf(cnn.getAmmount_overdue())));
                        // ci.amount2 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue) + "</b>" + String.format("%.2f", amt_overdue)));
                        ci.amount2 = String.valueOf( amt_overdue);


                    } else {
                        // ci.amount2 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue_Not_Found) + "</b>"));
                        ci.amount2 = String.valueOf( "0.0");

                    }


                }
            } else {
                ci.credit_limit = String.valueOf( "0.0" );
                ci.amount1 = String.valueOf("0.0");
                ci.amount2 = String.valueOf( "0.0" );
            }

            result.add(ci);

            ca = new Customer_info_main_adapter(result, Customer_info_main.this);
            recList.setAdapter(ca);
            ca.notifyDataSetChanged();
        }
    }

    public void getcus_detailsn(String Cust_shop_name) {
        List<Local_Data> contacts = dbvoc.getCustomerCode(Cust_shop_name);

        Global_Data.Globelo_OU_CUST_name = "";
        Customer_Info ci = new Customer_Info();
        List<Customer_Info> result = new ArrayList<Customer_Info>();
        if (contacts.size() <= 0) {
            Global_Data.Custom_Toast(Customer_info_main.this,
                    getResources().getString(R.string.customer_notfound_message),"Yes");
//            Toast toast = Toast.makeText(Customer_info_main.this,
//                    getResources().getString(R.string.customer_notfound_message), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        } else {
            for (Local_Data cn : contacts) {

                recList.setVisibility(View.VISIBLE);
                ci.name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.CName) + "</b>" + cn.getCUSTOMER_NAME()));
                ci.mobile = cn.getMOBILE_NO();
                ci.shop_name = cn.getCUSTOMER_SHOPNAME();
                autoCompleteTextView1.setText(ci.shop_name);
                ci.address = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.CAddress) + "</b>" + cn.getAddress()));
                ci.latlong = cn.getlatitude() + "," + cn.getlongitude();
                Customer_id = cn.getCust_Code();
                ci.cus_code = cn.getCust_Code();
                City_id = cn.getCITY_ID();
                Beat_id = cn.getBEAT_ID();

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getDcus_business_type())) {
                    ci.c_business_type = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Business_Type) + "</b>" + cn.getDcus_business_type()));
                } else {
                    ci.c_business_type = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Business_Type) + "</b>" + ""));
                }


            }

            List<Local_Data> cityi = dbvoc.getcityByState_idn(City_id);
            if (cityi.size() > 0) {
                for (Local_Data cnnn : cityi) {
                    //  ci.city_name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.City_Name) + "</b>" + cnnn.getCityName()));
                    ci.city_name = String.valueOf( cnnn.getCityName());
                }
            }

            List<Local_Data> beati = dbvoc.getbeatByCityIDn(Beat_id);
            if (beati.size() > 0) {
                for (Local_Data cnnnn : beati) {
                    //  ci.Beat_name = cnnnn.getCityName();
                    //   ci.Beat_name = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Beat_Name) + "</b>" + cnnnn.getCityName()));
                    ci.Beat_name = String.valueOf( cnnnn.getCityName());
                }
            }

            List<Local_Data> contactlimit = dbvoc.getCreditprofileData(Customer_id);
            if (contactlimit.size() > 0) {

                for (Local_Data cnn : contactlimit) {

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.get_credit_limit())) {
                        Double credit_limit = ((Double.valueOf(cnn.get_credit_limit())));
                        //  ci.credit_limit = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit) + "</b>" + String.format("%.2f", credit_limit)));
                        ci.credit_limit = String.valueOf( credit_limit);
                    } else {
                        //   ci.credit_limit = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Credit_Limit_Not_Found) + "</b>"));
                        ci.credit_limit = String.valueOf( "0.0" );
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.get_shedule_outstanding_amount())) {
                        Global_Data.amt_outstanding = ((Double.valueOf(cnn.get_shedule_outstanding_amount())));
                        //  ci.amount1 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding) + "</b>" + String.format("%.2f", Global_Data.amt_outstanding)));
                        ci.amount1 = String.valueOf( Global_Data.amt_outstanding);


                    } else {

//                        ci.amount1 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Outstanding_Not_Found) + "</b>"));
                        ci.amount1 = String.valueOf("0.0" );
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnn.getAmmount_overdue())) {
                        Double amt_overdue = ((Double.valueOf(cnn.getAmmount_overdue())));
//                        ci.amount2 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue) + "</b>" + String.format("%.2f", amt_overdue)));
                        ci.amount2 = String.valueOf( amt_overdue);


                    } else {
//                        ci.amount2 = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.Amount_Overdue_Not_Found) + "</b>"));
                        ci.amount2 = String.valueOf("0.0");

                    }


                }
            } else {
                ci.credit_limit = String.valueOf( "0.0");
                ci.amount1 = String.valueOf("0.0" );
                ci.amount2 = String.valueOf("0.0");
            }

            result.add(ci);

            ca = new Customer_info_main_adapter(result, Customer_info_main.this);
            recList.setAdapter(ca);
            ca.notifyDataSetChanged();
        }
    }

}
