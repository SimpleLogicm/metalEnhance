package com.msimplelogic.activities;

/**
 * Created by vinod on 04-10-2016.
 */

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.msimplelogic.activities.R;

import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cpm.simplelogic.helper.TargetValue_info;

public class Target_Summary1 extends BaseActivity {
    int check=0;
    int check_product=0;
    int check_ProductSpec=0;

    ArrayAdapter<String> dataAdapterCategory,dataAdapterProductSpec,dataAdapterProduct;
    //ArrayList productList = new ArrayList();
    List<String> listProduct,listProductSpec;
    List<String> listScheme;

    private ArrayList<String> results = new ArrayList<String>();
    private ArrayList<String> results1 = new ArrayList<String>();
    private ArrayList<String> results2 = new ArrayList<String>();
    List<TargetValue_info> result = new ArrayList<TargetValue_info>();

    String Target_From_Month = "";
    String Target_To_Month = "";
    String Target_Product_Category = "";
    String Target_Year = "";
    String Target_TO_YEAR = "";
    String Target_TO_MONTHNEW = "";
    String Target_TO_YEAR_ARRAY[];

    Double t_total = 0.0;
    Double achived_total = 0.0;
    Double agen_total = 0.0;
    String age_value = "";
    String target_value = "";
    String achieved_value = "";
    String product_category = "";
    String product_subcategory = "";

    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    ArrayAdapter<String> adapter_state1;
    ArrayAdapter<String> adapter_state2;
    ArrayAdapter<String> adapter_state3;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    RecyclerView recList;
    TextView t_total_value,a_total_value,age_total_value,T_from_date,T_to_date,TS1month;
    ProgressDialog dialog;
    LoginDataBaseAdapter loginDataBaseAdapter;
    Button ts1back, ts1more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my);

        setContentView(R.layout.target_sumary1);

        t_total_value = (TextView) findViewById(R.id.target_value_final);
        a_total_value = (TextView) findViewById(R.id.achieved_value_final);
        age_total_value = (TextView) findViewById(R.id.age_value_final);
        T_from_date = (TextView) findViewById(R.id.T_from_date);
        T_to_date = (TextView) findViewById(R.id.T_to_date);
        TS1month = (TextView) findViewById(R.id.TS1month);
        ts1back = (Button) findViewById(R.id.ts1back);
        ts1more = (Button) findViewById(R.id.ts1more);

        cd  = new ConnectionDetector(getApplicationContext());


        if (Global_Data.target_grpby.equalsIgnoreCase(getResources().getString(R.string.By_Product)))
        {
            TS1month.setText(getResources().getString(R.string.PRODUCT_CATEGORY));
        }

        ts1back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Target_Summary1.this, Target_REYC_Main.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

        ts1more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent)
                {
                    Intent i=new Intent(Target_Summary1.this, Target_Summary2.class);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else
                {
//                    Toast toast = Toast.makeText(Target_Summary1.this, getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(Target_Summary1.this, getResources().getString(R.string.internet_connection_error), "yes");
                }
            }
        });



        recList = (RecyclerView) findViewById(R.id.cardList);


        loginDataBaseAdapter=new LoginDataBaseAdapter(Target_Summary1.this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        try
        {
//            Bundle extras = getIntent().getExtras();
//            if (extras != null) {
            Target_From_Month = Global_Data.Target_From_Month;
            Target_To_Month = Global_Data.Target_To_Month   ;
            Target_Product_Category = Global_Data.Target_Product_Category;
            Target_Year = Global_Data.Target_Year;

            if(Target_To_Month.contains(" "))
            {
                Target_TO_YEAR_ARRAY = Target_To_Month.split(" ");

                Target_TO_MONTHNEW = Target_TO_YEAR_ARRAY[0];
                Target_TO_YEAR = Target_TO_YEAR_ARRAY[1];
            }
            //}
        }catch(Exception ex){ex.printStackTrace();}


        T_from_date.setText(Target_From_Month+" " + Target_Year);
        T_to_date.setText(Target_To_Month);

        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
            getTargetDataSummary1();
        }
        else
        {
//            Toast toast = Toast.makeText(Target_Summary1.this, getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
            Global_Data.Custom_Toast(Target_Summary1.this, getResources().getString(R.string.internet_connection_error),"yes");

                    Intent i=new Intent(Target_Summary1.this, Target_REYC_Main.class);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
        }



        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


        SharedPreferences spf = Target_Summary1.this.getSharedPreferences("SimpleLogic", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("order", "new");
        editor.commit();
        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText(getResources().getString(R.string.Targett));

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = Target_Summary1.this.getSharedPreferences("SimpleLogic", 0);

//        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//        	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//		}
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


            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


//        List<TargetValue_info> result = new ArrayList<TargetValue_info>();
//        TargetValue_info ci = new TargetValue_info();
//        ci.product_value = "Product 1";
//        ci.product_Sub_value = "Product 1 sub";
//        ci.target_value = "1000";
//        ci.achieved_value = "500";
//        ci.age_value = "50%";
//
//
//        result.add(ci);
//
//        TargetCard_Adapter ca = new TargetCard_Adapter(result);
//        recList.setAdapter(ca);
//        ca.notifyDataSetChanged();

//        List<Local_Data> contacts3 = dbvoc.getSchedule_ListAll();
//
//            if (contacts3.size() <= 0) {
//                // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();
//
//                Toast toast = Toast.makeText(Target_REYCLE.this, "Sorry No Record Found.", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//
//                Intent intent = new Intent(getApplicationContext(),
//                        MainActivity.class);
//                startActivity(intent);
//            } else {
//                recList.setVisibility(View.VISIBLE);
//                List<TargetValue_info> result = new ArrayList<TargetValue_info>();
//                for (Local_Data cn : contacts3) {
//
//
//                    TargetValue_info ci = new TargetValue_info();
//                    ci.product_value = cn.get_shedule_delivery_date();
//                    ci.product_Sub_value = cn.get_shedule_order_id();
//                    ci.target_value = cn.getADDRESS();
//                    ci.achieved_value = cn.getADDRESS();
//                    ci.age_value = cn.getADDRESS();
//
//                    result.add(ci);
//                }
//                TargetCard_Adapter ca = new TargetCard_Adapter(result);
//                recList.setAdapter(ca);
//                ca.notifyDataSetChanged();
//
//
//            }



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


        Intent i=new Intent(Target_Summary1.this, Target_REYC_Main.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        //finish();

    }

    public void getTargetDataSummary1()
    {
        result.clear();
        loginDataBaseAdapter=new LoginDataBaseAdapter(Target_Summary1.this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        dialog = new ProgressDialog(Target_Summary1.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(getResources().getString(R.string.Target_dialog_message));
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        try
        {
            SharedPreferences spf = Target_Summary1.this.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL",null);

            SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url1 = Cust_domain + "metal/api/v1/";
            String domain = service_url1;
            String device_id = sp.getString("devid", "");
            Log.i("volley", "domain: " + domain);
            Log.i("volley", "email: " + user_email);

            String product_category_final = " ";
            if (Target_Product_Category.equalsIgnoreCase(getResources().getString(R.string.All_Product_Category)))
            {
                product_category_final = " ";
            }
            else
            {
                product_category_final = Target_Product_Category;
            }

            Date date = new SimpleDateFormat("MMMM").parse(Target_From_Month);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String T_FROM_MONTH = String.valueOf(cal.get(Calendar.MONTH)+1);

            Date date2 = new SimpleDateFormat("MMMM").parse(Target_TO_MONTHNEW);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            String T_TO_MONTH = String.valueOf(cal2.get(Calendar.MONTH)+1);

            String date_flag = "";
            String service_url = "";
            if (Global_Data.target_grpby.equalsIgnoreCase(getResources().getString(R.string.By_Product)))
            {
                date_flag = "false";
                service_url = domain+"targets/get_targets_by_product?imei_no="+device_id+"&email="+user_email+"&from_year="+Target_Year+"&from_month="+T_FROM_MONTH+"&to_year="+Target_TO_YEAR+"&to_month="+T_TO_MONTH+"&primary_category="+URLEncoder.encode(product_category_final, "UTF-8");
            }
            else
            {
                date_flag = "true";

                service_url = domain+"targets/get_targets?imei_no="+device_id+"&email="+user_email+"&from_year="+Target_Year+"&from_month="+T_FROM_MONTH+"&to_year="+Target_TO_YEAR+"&to_month="+T_TO_MONTH+"&primary_category="+URLEncoder.encode(product_category_final, "UTF-8");
            }


            Log.i("target url", "target url " + service_url);

            JsonObjectRequest jsObjRequest = new JsonObjectRequest(service_url,null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);
                    //  Log.i("volley", "response reg Length: " + response.length());


                    try{

                        String response_result = "";
                        if(response.has("result"))
                        {
                            response_result = response.getString("result");
                        }
                        else
                        {
                            response_result = "data";
                        }


                        if(response_result.equalsIgnoreCase("User doesn't exist")) {

                            // Toast.makeText(getActivity(), response_result, Toast.LENGTH_LONG).show();

                            dialog.dismiss();
//                            Toast toast = Toast.makeText(Target_Summary1.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Global_Data.Custom_Toast(Target_Summary1.this, response_result,"yes");

                            Intent i=new Intent(Target_Summary1.this, Target_REYC_Main.class);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();

                        }
                        else
                        if(response_result.equalsIgnoreCase("Please provide all the data")) {

                            // Toast.makeText(getActivity(), response_result, Toast.LENGTH_LONG).show();

                            dialog.dismiss();
//                            Toast toast = Toast.makeText(Target_Summary1.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Target_Summary1.this, response_result,"yes");

                            Intent i=new Intent(Target_Summary1.this, Target_REYC_Main.class);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();

                        }
                        else {

                            dbvoc.getDeleteTable("targets");

                            JSONArray targets = response.getJSONArray("targets");


                            Log.i("volley", "response reg targets Length: " + targets.length());

                            Log.d("States", "targets" + targets.toString());

                            if(targets.length() <= 0)
                            {
                                dialog.dismiss();
                                //Toast.makeText(getActivity(), "Target not found.", Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(Target_Summary1.this, getResources().getString(R.string.Target_not_found), Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
Global_Data.Custom_Toast(Target_Summary1.this, getResources().getString(R.string.Target_not_found),"yes");

                                Intent i=new Intent(Target_Summary1.this, Target_REYC_Main.class);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                for (int i = 0; i < targets.length(); i++) {

                                    JSONObject jsonObject = targets.getJSONObject(i);
                                    TargetValue_info ci = new TargetValue_info();

                                    if (Global_Data.target_grpby.equalsIgnoreCase(getResources().getString(R.string.By_Product)))
                                    {
                                        if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("products_primary_category").toString()))
                                        {
                                            if(!jsonObject.getString("products_primary_category").equalsIgnoreCase("null") && !jsonObject.getString("products_primary_category").equalsIgnoreCase(null) & !jsonObject.getString("products_primary_category").equalsIgnoreCase("") & !jsonObject.getString("products_primary_category").equalsIgnoreCase(" "))
                                            {
                                                product_category = jsonObject.getString("products_primary_category").toString();
                                            }
                                            else
                                            {

                                                product_category = "";
                                            }

                                            ci.monthgrp_str = product_category;
                                        }
                                    }
                                    else
                                    {
                                        if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("year").toString()))
                                        {
                                            ci.monthgrp_str = jsonObject.getString("month").toString() +" "+jsonObject.getString("year").toString();
                                        }
                                        else
                                        {
                                            ci.monthgrp_str = jsonObject.getString("month").toString();
                                        }
                                    }

//                                    if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("products_sub_category").toString()))
//                                    {
//                                        if(!jsonObject.getString("products_sub_category").equalsIgnoreCase("null") && !jsonObject.getString("products_sub_category").equalsIgnoreCase(null) & !jsonObject.getString("products_sub_category").equalsIgnoreCase("") & !jsonObject.getString("products_sub_category").equalsIgnoreCase(" "))
//                                        {
//                                            product_subcategory = jsonObject.getString("products_sub_category").toString();
//                                        }
//                                        else
//                                        {
//
//                                            product_subcategory = "";
//                                        }
//
//                                        ci.product_Sub_value = product_subcategory;
//                                    }


                                    if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("target").toString()))
                                    {
                                        if(!jsonObject.getString("target").equalsIgnoreCase("null") && !jsonObject.getString("target").equalsIgnoreCase(null) & !jsonObject.getString("target").equalsIgnoreCase("") & !jsonObject.getString("target").equalsIgnoreCase(" "))
                                        {
                                            if(Global_Data.target_amount.equalsIgnoreCase("In Crores"))
                                            {
                                                t_total +=(Double.valueOf(jsonObject.getString("target").toString())/10000000);
                                                target_value = String.format("%.2f",Double.valueOf(jsonObject.getString("target").toString())/10000000);
                                            }else if(Global_Data.target_amount.equalsIgnoreCase("In Lakhs"))
                                            {
                                                t_total +=(Double.valueOf(jsonObject.getString("target").toString())/100000);
                                                target_value = String.format("%.2f",Double.valueOf(jsonObject.getString("target").toString())/100000);
                                            }else if(Global_Data.target_amount.equalsIgnoreCase("In Thousands"))
                                            {
                                                t_total +=(Double.valueOf(jsonObject.getString("target").toString())/1000);
                                                target_value = String.format("%.2f",Double.valueOf(jsonObject.getString("target").toString())/1000);
                                            }
                                            else if(Global_Data.target_amount.equalsIgnoreCase("In Ruppes"))
                                            {
                                                t_total +=(Double.valueOf(jsonObject.getString("target").toString()));
                                                target_value = String.format("%.2f",Double.valueOf(jsonObject.getString("target").toString()));
                                            }

//                                                t_total +=Double.valueOf(jsonObject.getString("target").toString());
//                                               // target_value = String.valueOf(Double.valueOf(jsonObject.getString("target").toString()));
//                                                target_value = String.format("%.2f",Double.valueOf(jsonObject.getString("target").toString())/1000);
                                        }
                                        else
                                        {
                                            t_total +=Double.valueOf("0.00");
                                            target_value = String.valueOf(Double.valueOf("0.00"));
                                        }

                                        ci.targetgrp_str = target_value;
                                    }
                                    else
                                    {
                                        t_total +=Double.valueOf("0.00");
                                        target_value = String.valueOf(Double.valueOf("0.00"));

                                        ci.targetgrp_str = target_value;
                                    }

                                    if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("achieved").toString()))
                                    {
                                        if(!jsonObject.getString("achieved").equalsIgnoreCase("null") && !jsonObject.getString("achieved").equalsIgnoreCase(null) & !jsonObject.getString("achieved").equalsIgnoreCase("") & !jsonObject.getString("achieved").equalsIgnoreCase(" "))
                                        {
                                            if (Global_Data.target_amount.equalsIgnoreCase(getResources().getString(R.string.In_Crores)))
                                            {
                                                achived_total +=(Double.valueOf(jsonObject.getString("achieved").toString())/10000000);
                                                achieved_value = String.format("%.2f",Double.valueOf(jsonObject.getString("achieved").toString())/10000000);

                                            } else if (Global_Data.target_amount.equalsIgnoreCase(getResources().getString(R.string.In_Lakhs)))
                                            {
                                                achived_total +=(Double.valueOf(jsonObject.getString("achieved").toString())/100000);
                                                achieved_value = String.format("%.2f",Double.valueOf(jsonObject.getString("achieved").toString())/100000);

                                            } else if (Global_Data.target_amount.equalsIgnoreCase(getResources().getString(R.string.In_Thousands)))
                                            {
                                                achived_total +=(Double.valueOf(jsonObject.getString("achieved").toString())/1000);
                                                achieved_value = String.format("%.2f",Double.valueOf(jsonObject.getString("achieved").toString())/1000);
                                            } else if (Global_Data.target_amount.equalsIgnoreCase(getResources().getString(R.string.In_Ruppes)))
                                            {
                                                achived_total +=(Double.valueOf(jsonObject.getString("achieved").toString()));
                                                achieved_value = String.format("%.2f",Double.valueOf(jsonObject.getString("achieved").toString()));
                                            }
//                                                achived_total +=Double.valueOf(jsonObject.getString("achieved").toString());
//                                                //achieved_value = String.valueOf(Double.valueOf(jsonObject.getString("achieved").toString()));
//                                                achieved_value = String.format("%.2f",Double.valueOf(jsonObject.getString("achieved").toString()));
                                        }
                                        else
                                        {
                                            achived_total +=Double.valueOf("0.00");
                                            achieved_value = String.valueOf(Double.valueOf("0.00"));
                                        }
                                        ci.achievedgrp_str = achieved_value;
                                    }
                                    else
                                    {
                                        achived_total +=Double.valueOf("0.00");
                                        achieved_value = String.valueOf(Double.valueOf("0.00"));
                                        ci.achievedgrp_str = achieved_value;
                                    }

                                    if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("achieved").toString()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("target").toString()))
                                    {
                                        if(!jsonObject.getString("achieved").equalsIgnoreCase("null") && !jsonObject.getString("achieved").equalsIgnoreCase(null) & !jsonObject.getString("achieved").equalsIgnoreCase("") & !jsonObject.getString("achieved").equalsIgnoreCase(" ") && !jsonObject.getString("target").equalsIgnoreCase("null") && !jsonObject.getString("target").equalsIgnoreCase(null) & !jsonObject.getString("target").equalsIgnoreCase("") & !jsonObject.getString("target").equalsIgnoreCase(" "))
                                        {
                                            Double age_n = ((Double.valueOf(jsonObject.getString("achieved").toString()))/(Double.valueOf(jsonObject.getString("target").toString())))*100;

                                            age_value = String.format("%.2f",age_n);
//                                            agen_total +=  (Double.valueOf(jsonObject.getString("achieved").toString()))/(Double.valueOf(jsonObject.getString("target").toString()));
                                        }
                                        else
                                        {
                                            age_value =  String.valueOf("0.0");
                                            agen_total +=  Double.valueOf("0.0");
                                        }
                                        ci.agegrp_str = age_value+"%";
                                    }
                                    else
                                    {
                                        age_value =  String.valueOf("0.0");
                                        agen_total +=  Double.valueOf("0.0");
                                        ci.agegrp_str = age_value+"%";
                                    }
                                    result.add(ci);
                                }

                                Groupby_Adapter ca = new Groupby_Adapter(result);
                                recList.setAdapter(ca);
                                ca.notifyDataSetChanged();

                                t_total_value.setText(String.format("%.2f",t_total));
                                a_total_value.setText(String.format("%.2f",achived_total));

                                Double agen_total_final =  ((Double.valueOf(achived_total))/(Double.valueOf(t_total)))*100;

                                age_total_value.setText(String.format("%.2f",agen_total_final)+"%");

                                dialog.dismiss();
                            }

                            dialog.dismiss();

                            //finish();

                        }


                        // }

                        // output.setText(data);
                    }catch(JSONException e){e.printStackTrace(); dialog.dismiss(); }


                    dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volley", "error: " + error);
//
//                    Toast toast = Toast.makeText(Target_Summary1.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(Target_Summary1.this, getResources().getString(R.string.Server_Error),"yyes");

                    dialog.dismiss();

                    Intent i=new Intent(Target_Summary1.this, Target_REYC_Main.class);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(Target_Summary1.this);
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
}
