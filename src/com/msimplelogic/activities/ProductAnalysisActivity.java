package com.msimplelogic.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.msimplelogic.activities.R;

import com.msimplelogic.helper.DayAxisValueFormatter;
import com.msimplelogic.helper.MyAxisValueFormatter_Doller;
import com.msimplelogic.webservice.ConnectionDetector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

//how to give voice for icon when swipe in android
public class ProductAnalysisActivity extends BaseActivity implements OnChartValueSelectedListener {
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    protected BarChart mChart;
    static String final_response = "";
    String response_result = "";
    ProgressDialog dialog;
    Dialog device_dialog;
    Button piechartBtn;
    LinearLayout filterBtn;
    private String xAxis_Flag = "";
    ArrayList <String> abc = new ArrayList<String>();
    ArrayList <String> All_Category = new ArrayList<String>();
    ArrayList <String> All_SubCategory = new ArrayList<String>();
    ArrayList <String> All_Varient = new ArrayList<String>();
    ArrayList <String> All_Dates = new ArrayList<String>();
    ArrayList <String> All_Quantity = new ArrayList<String>();
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_analysis_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        //setTitle(getResources().getString(R.string.PRODUCT_ANALYSIS));


        cd = new ConnectionDetector(getApplicationContext());

        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);
            Intent i = getIntent();
            String name = i.getStringExtra("retialer");
            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText(getResources().getString(R.string.PRODUCT_ANALYSIS));

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = ProductAnalysisActivity.this.getSharedPreferences("SimpleLogic", 0);

//	       if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//	       	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//			}

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

                } else {
                    int age = (int) Math.round(age_float);

                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    }

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
//	       	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
                todaysTarget.setText("Today's Target Acheived");
            }

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        Global_Data.Selected_USER_EMAIL = "";

      //  dialog = new ProgressDialog(ProductAnalysisActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        mChart = (BarChart) findViewById(R.id.chart1);
        piechartBtn = (Button) findViewById(R.id.piechart_btn);
        filterBtn = (LinearLayout) findViewById(R.id.filter_btn);

        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        //for horizontal lines remove betn graph
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getAxisRight().setDrawGridLines(false);

        mChart.setDrawGridBackground(false);


        cd = new ConnectionDetector(getApplicationContext());

        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
//            dialog = new ProgressDialog(ProductAnalysisActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
////            dialog.setMessage("Please wait Products Loading....");
////            dialog.setTitle("Metal App");
////            dialog.setCancelable(false);
////            dialog.show();
            try {
                if (device_dialog != null && device_dialog.isShowing()) {
                    device_dialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            device_dialog = new Dialog(ProductAnalysisActivity.this);
            device_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            device_dialog.setCancelable(false);
            device_dialog.setContentView(R.layout.dialog_sync);
          //  tablename = (TextView) device_dialog.findViewById(R.id.tablename);
            device_dialog.show();

            ProductP_Result();
        }
        else
        {
           // Toast.makeText(this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
            Global_Data.Custom_Toast(this, "You don't have internet connection.","yes");

        }

        piechartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductAnalysisActivity.this, ProductAnalysisPieActivity.class);
                startActivity(intent);
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductAnalysisActivity.this, FilterProductSearchActivity.class);
                startActivity(intent);
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
                return true;
        }
//		return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add:
                String targetNew="";
                SharedPreferences sp = ProductAnalysisActivity.this.getSharedPreferences("SimpleLogic", 0);
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
        //super.onBackPressed();

        Intent i = new Intent(ProductAnalysisActivity.this,DashboardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i);
        finish();
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() { }


    public void ProductP_Result()
    {
        String domain = getResources().getString(R.string.service_domain1);

        SharedPreferences spf = ProductAnalysisActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);
        Log.i("user list url", "user list url " + domain + "users?email=" + user_email);

        StringRequest jsObjRequest = null;
        String service_url = "";



        if(Global_Data.GLOVEL_PRODUCT_SERCH_VALUE.equalsIgnoreCase(""))
        {
            xAxis_Flag = "Categories";
            service_url = domain + "metal/api/performance/v1/products?email=" + user_email+"&type="+"product_performance";

        }
        else
        {
            String Search_val[] = Global_Data.GLOVEL_PRODUCT_SERCH_VALUE.split("@");

            if(Search_val[1].equalsIgnoreCase("ALL CATEGORY"))
            {
                if(Global_Data.GLOVEL_PRODUCT_SERCH_Category.length()>0 && Global_Data.GLOVEL_PRODUCT_SERCH_Sub_Category.length()>0 && Global_Data.GLOVEL_PRODUCT_SERCH_Variant.length()>0)
                {
                    try {
                        service_url = domain + "metal/api/performance/v1/products?email=" + user_email+"&primary_category="+URLEncoder.encode(Global_Data.GLOVEL_PRODUCT_SERCH_Category, "UTF-8")+"&sub_category="+URLEncoder.encode(Global_Data.GLOVEL_PRODUCT_SERCH_Sub_Category, "UTF-8")+"&product_variant="+URLEncoder.encode(Global_Data.GLOVEL_PRODUCT_SERCH_Variant, "UTF-8")+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate+"&type="+"product_performance";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }else {
                    xAxis_Flag = "Categories";
                    service_url = domain + "metal/api/performance/v1/products?email=" + user_email+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate+"&type="+"product_performance";

                }


            }
            else
            if(Search_val[1].equalsIgnoreCase("ALL SUB CATEGORY"))
            {

                xAxis_Flag = "Categories"+"@"+Search_val[0];
                try {
                    service_url = domain + "metal/api/performance/v1/products?email=" + user_email+"&primary_category="+ URLEncoder.encode(Search_val[0], "UTF-8")+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate+"&type="+"product_performance";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            else
            if(Search_val[1].equalsIgnoreCase("ALL PRODUCT"))
            {
                xAxis_Flag = "SubCategories"+"@"+Search_val[0];
                try {
                    service_url = domain + "metal/api/performance/v1/products?email=" + user_email+"&primary_category="+URLEncoder.encode(Global_Data.GLOVEL_PRODUCT_SERCH_Category, "UTF-8")+"&sub_category="+URLEncoder.encode(Search_val[0], "UTF-8")+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate+"&type="+"product_performance";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            else
            if(Search_val[1].equalsIgnoreCase("VARIANT"))
            {
                xAxis_Flag = "Varients"+"@"+Search_val[0];
                try {
                    service_url = domain + "metal/api/performance/v1/products?email=" + user_email+"&primary_category="+URLEncoder.encode(Global_Data.GLOVEL_PRODUCT_SERCH_Category, "UTF-8")+"&sub_category="+URLEncoder.encode(Global_Data.GLOVEL_PRODUCT_SERCH_Sub_Category, "UTF-8")+"&product_variant="+URLEncoder.encode(Search_val[0], "UTF-8")+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate+"&type="+"product_performance";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else if(!Global_Data.GLOVEL_PRODUCT_SERCH_Category.equalsIgnoreCase("Select Category") || !Global_Data.GLOVEL_PRODUCT_SERCH_Category.equalsIgnoreCase("Select All"))
            {
              //  Toast.makeText(this, "fhgfh", Toast.LENGTH_SHORT).show();
                Global_Data.Custom_Toast(this, "fhgfh", "");
            }
        }


        Log.i("volley", "service_url: " + service_url);

        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;

                new ProductAnalysisActivity.ProductPerformance_Task().execute(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  dialog.dismiss();
                        device_dialog.dismiss();

                        Intent intent = new Intent(ProductAnalysisActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
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
                                    "Network   Error","Yes");
                        } else if (error instanceof ParseError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "ParseError   Error","");
                        } else {
                       //     Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(),"");
                        }
                     //   dialog.dismiss();
                        device_dialog.dismiss();
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

    private class ProductPerformance_Task extends AsyncTask<String, Void, String> {
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

                    ProductAnalysisActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                        //    dialog.dismiss();
                            device_dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(ProductAnalysisActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Global_Data.Custom_Toast(ProductAnalysisActivity.this, response_result,"yes");

//                            Intent intent = new Intent(ProductAnalysisActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    });

                }
                else
                if(response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    ProductAnalysisActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                          //  dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();
                            device_dialog.dismiss();
//                            Toast toast = Toast.makeText(ProductAnalysisActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ProductAnalysisActivity.this, response_result,"yes");

//                            Intent intent = new Intent(ProductAnalysisActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    });
                }
                else {
                    //dbvoc.getDeleteTable("delivery_products");

                    JSONArray order_products = response.getJSONArray("graph_data");
                    Log.i("volley", "response orders Length: " + order_products.length());
                    Log.d("volley", "orders" + order_products.toString());

                    if (order_products.length() <= 0) {

                        ProductAnalysisActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                               // dialog.dismiss();
                                device_dialog.dismiss();
//                                Toast toast = Toast.makeText(ProductAnalysisActivity.this, "Products doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

                                Global_Data.Custom_Toast(ProductAnalysisActivity.this, "Products doesn't exist","yes");

//                                Intent intent = new Intent(ProductAnalysisActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();
                            }
                        });
                    } else {

                        for (int i = 0; i < order_products.length(); i++) {

                            JSONObject jsonObject = order_products.getJSONObject(i);

//                            if (!(All_User_Name.contains(jsonObject.getString("user")))) {
//                                single_user_check = 1;
//                            }

                            All_Category.add(jsonObject.getString("x_axis_data"));
                            All_Dates.add(jsonObject.getString("date"));
                            All_Quantity.add(jsonObject.getString("total_order_amount"));

                        }

                        ProductAnalysisActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {
                                if(Global_Data.PieStatus.length()>0)
                                {
                                    Intent intent = new Intent(ProductAnalysisActivity.this, ProductAnalysisPieActivity.class);
                                    startActivity(intent);
                                    Global_Data.PieStatus="";
                                }

                              //  dialog.dismiss();
                                device_dialog.dismiss();
                            }
                        });
                    }

                    ProductAnalysisActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                           // dialog.dismiss();
                            device_dialog.dismiss();
                        }
                    });
                    //	dialog.dismiss();

                    //finish();

                }


                // }

                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();

                Intent intent = new Intent(ProductAnalysisActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                ProductAnalysisActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                       // dialog.dismiss();
                        device_dialog.dismiss();
                    }
                });

            }


            ProductAnalysisActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                   // dialog.dismiss();
                    device_dialog.dismiss();

                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            ProductAnalysisActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                  //  dialog.dismiss();
                    device_dialog.dismiss();
                }
            });

            setData(4, 4);


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void setData(int count, float range) {

//        if(Global_Data.GLOVEL_PRODUCT_SERCH_VALUE.equalsIgnoreCase(""))
//        {
//            List<Local_Data> contacts2 = dbvoc.getProductPERFORMANCEDATA();
//            All_Category.clear();
//            if(contacts2.size() > 0) {
//                for (Local_Data cn : contacts2) {
//
//                    All_Category.add(cn.getCategory());
//                    All_MRP.add(cn.getMRP());
//                }
//            }
//        }
//        else
//        {
//            String Search_val[] = Global_Data.GLOVEL_PRODUCT_SERCH_VALUE.split("@");
//
//            if(Search_val[1].equalsIgnoreCase("ALL CATEGORY"))
//            {
//                List<Local_Data> contacts2 = dbvoc.getProductPERFORMANCEDATA();
//                All_Category.clear();
//                if(contacts2.size() > 0) {
//                    for (Local_Data cn : contacts2) {
//
//                        All_Category.add(cn.getCategory());
//                        All_MRP.add(cn.getMRP());
//                    }
//                }
//            }
//            else
//            if(Search_val[1].equalsIgnoreCase("ALL SUB CATEGORY"))
//            {
//                List<Local_Data> contacts2 = dbvoc.getProductByCategory(Search_val[0]);
//                All_Category.clear();
//                if(contacts2.size() > 0) {
//                    for (Local_Data cn : contacts2) {
//
//                        All_Category.add(cn.getSubcateg());
//                        All_MRP.add(cn.getMRP());
//                    }
//                }
//            }
//            else
//            if(Search_val[1].equalsIgnoreCase("ALL PRODUCT"))
//            {
//                List<Local_Data> contacts2 = dbvoc.getItemCodevariantAll(Global_Data.GLOVEL_PRODUCT_SERCH_Category,Search_val[0]);
//                All_Category.clear();
//                if(contacts2.size() > 0) {
//                    for (Local_Data cn : contacts2) {
//
//                        All_Category.add(cn.getProduct_nm());
//                        All_MRP.add(cn.getMRP());
//                    }
//                }
//            }
//            else
//            if(Search_val[1].equalsIgnoreCase("VARIANT"))
//            {
//                List<Local_Data> contacts2 = dbvoc.getItemCodevarient(Global_Data.GLOVEL_PRODUCT_SERCH_Category,Global_Data.GLOVEL_PRODUCT_SERCH_Sub_Category,Search_val[0]);
//                All_Category.clear();
//                if(contacts2.size() > 0) {
//                    for (Local_Data cn : contacts2) {
//
//                        All_Category.add(cn.getProduct_nm());
//                        All_MRP.add(cn.getMRP());
//                    }
//                }
//            }
//        }



        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(TargetAnalysisActivity.mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setLabelRotationAngle(10);
        //xAxis.set();
        // xAxis.setValueFormatter(xAxisFormatter);

        Global_Data.FINAL_PI_Y = All_Quantity;

        if(!(Global_Data.GLOVEL_PRODUCT_SERCH_VALUE.equalsIgnoreCase("")))
        {

            if(Global_Data.GLOVEL_PRODUCT_SERCH_VALUE.indexOf("@") >0)
            {
                String Search_val[] = Global_Data.GLOVEL_PRODUCT_SERCH_VALUE.split("@");
                if(Search_val[1].equalsIgnoreCase("VARIANT"))
                {
                    xAxis.setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            // return DemoBase.Categories[(int) value % DemoBase.Categories.length];
                            return All_Dates.get((int) value % All_Dates.size());
                        }
                    });

                    Global_Data.FINAL_PI_X = All_Dates;
                }
                else
                {
                    xAxis.setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            // return DemoBase.Categories[(int) value % DemoBase.Categories.length];
                            return All_Category.get((int) value % All_Category.size());
                        }
                    });

                    Global_Data.FINAL_PI_X = All_Category;
                }
            }
            else
            {
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        // return DemoBase.Categories[(int) value % DemoBase.Categories.length];
                        return All_Category.get((int) value % All_Category.size());
                    }
                });

                Global_Data.FINAL_PI_X = All_Category;
            }

        }
        else
        {
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    // return DemoBase.Categories[(int) value % DemoBase.Categories.length];
                    return All_Category.get((int) value % All_Category.size());
                }
            });

            Global_Data.FINAL_PI_X = All_Category;
        }


        IAxisValueFormatter custom = new MyAxisValueFormatter_Doller();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(TargetAnalysisActivity.mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(new LargeValueFormatter());

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(TargetAnalysisActivity.mTfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setEnabled(false);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < All_Quantity.size() ; i++) {
//            float mult = (range + 1);
//            float val = (float) (i);
            yVals1.add(new BarEntry(i, Float.valueOf(All_Quantity.get(i))));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
            set1.setValueFormatter(new LargeValueFormatter());
        } else {

            try
            {
                if(xAxis_Flag.indexOf("@") > 0)
                {
                    String s[] = xAxis_Flag.split("@");

                    Global_Data.F_PRODUCT_NAME = s[1];
                    set1 = new BarDataSet(yVals1, s[1]+" Performance");
                    set1.setColors(ColorTemplate.PASTEL_COLORS);
                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                    dataSets.add(set1);
                    BarData data = new BarData(dataSets);
                    data.setValueTextSize(10f);
                    data.setValueTypeface(TargetAnalysisActivity.mTfLight);


                    if(All_Quantity.size() ==1)
                    {
                        data.setBarWidth(0.4f);
                    }
                    else
                    {
                        data.setBarWidth(0.9f);
                    }


                    //data.setBarWidth(0.9f);
                    mChart.setData(data);
                    mChart.invalidate();
                    set1.setValueFormatter(new LargeValueFormatter());

                }
                else
                {
                    if(xAxis_Flag.equalsIgnoreCase("Categories"))
                    {
                        Global_Data.F_PRODUCT_NAME = "Categories";
                        set1 = new BarDataSet(yVals1, "Product Categories Performance");

                        set1.setColors(ColorTemplate.PASTEL_COLORS);
                        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                        dataSets.add(set1);
                        BarData data = new BarData(dataSets);
                        data.setValueTextSize(10f);
                        data.setValueTypeface(TargetAnalysisActivity.mTfLight);
                        //data.setBarWidth(0.9f);

                        if(All_Quantity.size() ==1)
                        {
                            data.setBarWidth(0.4f);
                        }
                        else
                        {
                            data.setBarWidth(0.9f);
                        }


                        set1.setValueFormatter(new LargeValueFormatter());

                        mChart.setData(data);
                        mChart.invalidate();
                    }
                }

            }catch(Exception ex){ex.printStackTrace();}

            Global_Data.Globel_fromDate = "";
            Global_Data.Globel_toDate = "";
            Global_Data.GLOVEL_PRODUCT_SERCH_VALUE = "";
            Global_Data.GLOVEL_PRODUCT_SERCH_Category = "";
            Global_Data.GLOVEL_PRODUCT_SERCH_Sub_Category = "";

            //  Global_Data.GLOVEL_PRODUCT_SERCH_VALUE = "";


            //TODO Prevent zoom factor
            //mChart.fitScreen();
            // mChart.setScaleEnabled(false);
        }


    }


}

