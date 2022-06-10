package com.msimplelogic.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
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
import com.msimplelogic.webservice.ConnectionDetector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

//how to give voice for icon when swipe in android
public class OrderAnalysisActivity extends BaseActivity implements OnItemSelectedListener, OnChartValueSelectedListener {
    Spinner orderAnalysisSpinner,monthlySpinner,quarterlySpinner,halfYearlySpinner,yearlySpinner;
    ConnectionDetector cd;
    String selectedItem="";
    String selectedMonth="";
    String selectedQuarter="";
    String selectedYearly="";
    Boolean isInternetPresent = false;
    ArrayAdapter<String> orderAnalysisAdapter,monthlyAdapter,quarterlyAdapter,halfYearlyAdapter,yearlyAdapter;
    private ArrayList<String> orderAnalysisResults = new ArrayList<String>();
    private ArrayList<String> monthlyResults = new ArrayList<String>();
    private ArrayList<String> quarterlyResults = new ArrayList<String>();
    private ArrayList<String> halfYearlyResults = new ArrayList<String>();
    private ArrayList<String> yearlyResults = new ArrayList<String>();
    protected BarChart mChart;
    TextView txtYaxisTitle;
    static String final_response = "";
    String response_result = "";
    ProgressDialog dialog;
    ArrayList <String> All_Order_Amount = new ArrayList<String>();
    ArrayList <String> All_Order_Date = new ArrayList<String>();
    ArrayList <String> All_User_Name = new ArrayList<String>();
    Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_analisys_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setTitle(getResources().getString(R.string.ORDER_ANALYSIS));
        setTitle("");

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
            mTitleTextView.setText(getResources().getString(R.string.ORDER_ANALYSIS));

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = OrderAnalysisActivity.this.getSharedPreferences("SimpleLogic", 0);

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

        mChart = (BarChart) findViewById(R.id.chart1);
        txtYaxisTitle = (TextView) findViewById(R.id.txt_yaxis_title);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);
        //mChart.getDescription().setText("Y-Axis : No of Orders");
        //mChart.setDescription(null);

        //mChart.getDescription().setPosition(1f,1f);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);
        //mChart.getGridLabelRenderer().setVerticalAxisTitle("Word Count");
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        //for horizontal lines remove betn graph
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getAxisRight().setDrawGridLines(false);

        mChart.setDrawGridBackground(false);


        orderAnalysisSpinner = (Spinner) findViewById(R.id.analysis_spinner);
        monthlySpinner = (Spinner) findViewById(R.id.monthly_spinner);
        quarterlySpinner = (Spinner) findViewById(R.id.quarterly_spinner);
        halfYearlySpinner = (Spinner) findViewById(R.id.halfyearly_spinner);
        yearlySpinner = (Spinner) findViewById(R.id.yearly_spinner);

        //for selection
        orderAnalysisResults.clear();
        orderAnalysisResults.add("Select Graph Type");
        orderAnalysisResults.add("Last 7 Days");
        orderAnalysisResults.add("Monthly");
        orderAnalysisResults.add("Quarterly");
        orderAnalysisResults.add("Half Yearly");
        orderAnalysisResults.add("Yearly");

        orderAnalysisAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, orderAnalysisResults);
        orderAnalysisAdapter.setDropDownViewResource(R.layout.spinner_item);
        orderAnalysisSpinner.setAdapter(orderAnalysisAdapter);

        orderAnalysisSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Last 7 Days"))
                {
                    monthlySpinner.setVisibility(View.GONE);
                    quarterlySpinner.setVisibility(View.GONE);
                    halfYearlySpinner.setVisibility(View.GONE);
                    yearlySpinner.setVisibility(View.GONE);
                    selectedItem="last_7_days";
                    Global_Data.BarGraphStatus="Last 7 Days";

                    // do your stuff
                }else if(selectedItem.equals("Monthly"))
                {
                    Global_Data.BarGraphStatus="Monthly";
                    quarterlySpinner.setVisibility(View.GONE);
                    monthlySpinner.setVisibility(View.VISIBLE);
                    halfYearlySpinner.setVisibility(View.GONE);
                    yearlySpinner.setVisibility(View.GONE);

//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();
//                    txtYaxisTitle.setVisibility(View.GONE);
                    mChart.setVisibility(View.GONE);

                    monthlyAdapter = new ArrayAdapter<String>(OrderAnalysisActivity.this,
                            R.layout.spinner_item, monthlyResults);
                    monthlyAdapter.setDropDownViewResource(R.layout.spinner_item);
                    monthlySpinner.setAdapter(monthlyAdapter);
                    monthlyAdapter.notifyDataSetChanged();

                }else if(selectedItem.equals("Quarterly")){
                    Global_Data.BarGraphStatus="Quarterly";
                    monthlySpinner.setVisibility(View.GONE);
                    quarterlySpinner.setVisibility(View.VISIBLE);
                    halfYearlySpinner.setVisibility(View.GONE);
                    yearlySpinner.setVisibility(View.GONE);

//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();
//                    txtYaxisTitle.setVisibility(View.GONE);

                    mChart.setVisibility(View.GONE);

                    quarterlyAdapter = new ArrayAdapter<String>(OrderAnalysisActivity.this,
                            R.layout.spinner_item, quarterlyResults);
                    quarterlyAdapter.setDropDownViewResource(R.layout.spinner_item);
                    quarterlySpinner.setAdapter(quarterlyAdapter);
                    quarterlyAdapter.notifyDataSetChanged();

                }else if(selectedItem.equals("Half Yearly")){
                    Global_Data.BarGraphStatus="Half Yearly";
                    monthlySpinner.setVisibility(View.GONE);
                    quarterlySpinner.setVisibility(View.GONE);
                    halfYearlySpinner.setVisibility(View.GONE);
                    yearlySpinner.setVisibility(View.GONE);
                    selectedItem="half_year";

                }else if(selectedItem.equals("Yearly")){
                    Global_Data.BarGraphStatus="Yearly";
                    monthlySpinner.setVisibility(View.GONE);
                    quarterlySpinner.setVisibility(View.GONE);
                    halfYearlySpinner.setVisibility(View.GONE);
                    yearlySpinner.setVisibility(View.VISIBLE);

//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();
//                    txtYaxisTitle.setVisibility(View.GONE);

                    mChart.setVisibility(View.GONE);

                    yearlyAdapter = new ArrayAdapter<String>(OrderAnalysisActivity.this,
                            R.layout.spinner_item, yearlyResults);
                    yearlyAdapter.setDropDownViewResource(R.layout.spinner_item);
                    yearlySpinner.setAdapter(yearlyAdapter);
                    yearlyAdapter.notifyDataSetChanged();

                }
                else{
                    quarterlySpinner.setVisibility(View.GONE);
                    monthlySpinner.setVisibility(View.GONE);
                    halfYearlySpinner.setVisibility(View.GONE);
                    yearlySpinner.setVisibility(View.GONE);
                }

                if(!selectedItem.equalsIgnoreCase("Select Graph Type"))
                {
                    if(selectedItem.equalsIgnoreCase("last_7_days"))
                    {
//                        All_User_Name.clear();
//                        All_Order_Amount.clear();
//                        mChart.invalidate();
//                        mChart.clear();

                        mChart.setVisibility(View.GONE);

                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent)
                        {
                            dialog = new ProgressDialog(OrderAnalysisActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                            dialog.setMessage("Please wait Loading....");
                            dialog.setTitle("Metal App");
                            dialog.setCancelable(false);
                            dialog.show();
                            AllusersResultu(selectedItem);
                        }
                        else
                        {
                           // Toast.makeText(OrderAnalysisActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                            Global_Data.Custom_Toast(OrderAnalysisActivity.this, "You don't have internet connection.","");

                        }
                    }else if(selectedItem.equalsIgnoreCase("half_year"))
                    {
//                        All_User_Name.clear();
//                        All_Order_Amount.clear();
//                        mChart.invalidate();
//                        mChart.clear();

                        mChart.setVisibility(View.GONE);

                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent)
                        {
                            dialog = new ProgressDialog(OrderAnalysisActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                            dialog.setMessage("Please wait Loading....");
                            dialog.setTitle("Metal App");
                            dialog.setCancelable(false);
                            dialog.show();
                            AllusersResultu(selectedItem);
                        }
                        else
                        {
                           // Toast.makeText(OrderAnalysisActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                            Global_Data.Custom_Toast(OrderAnalysisActivity.this, "You don't have internet connection.", "");

                        }
                    }
                }else {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();
//                    txtYaxisTitle.setVisibility(View.GONE);

                    mChart.setVisibility(View.GONE);
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        //for monthly
        monthlyResults.clear();
        monthlyResults.add("Select Month");
        monthlyResults.add("January");
        monthlyResults.add("February");
        monthlyResults.add("March");
        monthlyResults.add("April");
        monthlyResults.add("May");
        monthlyResults.add("June");
        monthlyResults.add("July");
        monthlyResults.add("August");
        monthlyResults.add("September");
        monthlyResults.add("October");
        monthlyResults.add("November");
        monthlyResults.add("December");

        monthlyAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, monthlyResults);
        monthlyAdapter.setDropDownViewResource(R.layout.spinner_item);
        monthlySpinner.setAdapter(monthlyAdapter);

        monthlySpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedMonth = parent.getItemAtPosition(position).toString();
                if(selectedMonth.equals("January"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedMonth="1";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
                }else if(selectedMonth.equals("February"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedMonth="2";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
                }else if(selectedMonth.equals("March"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);
                    selectedMonth="3";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
                }else if(selectedMonth.equals("April"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedMonth="4";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
                }else if(selectedMonth.equals("May"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedMonth="5";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
                }else if(selectedMonth.equals("June"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedMonth="6";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
                }else if(selectedMonth.equals("July"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedMonth="7";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
                }else if(selectedMonth.equals("August"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedMonth="8";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
                }else if(selectedMonth.equals("September"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedMonth="9";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
                }else if(selectedMonth.equals("October"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedMonth="10";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
                }else if(selectedMonth.equals("November"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedMonth="11";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
                }else if(selectedMonth.equals("December"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedMonth="12";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
                }

                if(!selectedMonth.equalsIgnoreCase("Select Month"))
                {
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent)
                    {
                        dialog = new ProgressDialog(OrderAnalysisActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
                        AllusersResultu(selectedMonth);
                    }
                    else
                    {
                       // Toast.makeText(OrderAnalysisActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(OrderAnalysisActivity.this, "You don't have internet connection.","");
                    }
                }else {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    txtYaxisTitle.setVisibility(View.GONE);
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {
               // Toast.makeText(OrderAnalysisActivity.this, "no select", Toast.LENGTH_SHORT).show();
                Global_Data.Custom_Toast(OrderAnalysisActivity.this, "no select", "");
            }
        });

//for quarterly
        quarterlyResults.clear();
        quarterlyResults.add("Select Quarter");
        quarterlyResults.add("Q1 (April - June)");
        quarterlyResults.add("Q2 (July- September)");
        quarterlyResults.add("Q3 (October - December)");
        quarterlyResults.add("Q4 (Jan - March)");

        quarterlyAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, quarterlyResults);
        quarterlyAdapter.setDropDownViewResource(R.layout.spinner_item);
        quarterlySpinner.setAdapter(quarterlyAdapter);

        quarterlySpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedQuarter = parent.getItemAtPosition(position).toString();
                if(selectedQuarter.equals("Q1 (April - June)"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();
                    mChart.setVisibility(View.GONE);

                    selectedQuarter="q1";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedQuarter, Toast.LENGTH_SHORT).show();
                }else if(selectedQuarter.equals("Q2 (July- September)"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedQuarter="q2";
                   // Toast.makeText(OrderAnalysisActivity.this, selectedQuarter, Toast.LENGTH_SHORT).show();
                }else if(selectedQuarter.equals("Q3 (October - December)"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedQuarter="q3";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedQuarter, Toast.LENGTH_SHORT).show();
                }else if(selectedQuarter.equals("Q4 (Jan - March)"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    selectedQuarter="q4";
                    //Toast.makeText(OrderAnalysisActivity.this, selectedQuarter, Toast.LENGTH_SHORT).show();
                }

                if(!selectedQuarter.equalsIgnoreCase("Select Quarter"))
                {
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent)
                    {
                        dialog = new ProgressDialog(OrderAnalysisActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
                        AllusersResultu(selectedQuarter);
                    }
                    else
                    {
                       // Toast.makeText(OrderAnalysisActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(OrderAnalysisActivity.this, "You don't have internet connection.","");
                    }
                }
                else {

                    txtYaxisTitle.setVisibility(View.GONE);
                    mChart.setVisibility(View.GONE);
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

//        //for half yearly
//        halfYearlyResults.clear();
//        halfYearlyResults.add("First Half Year");
//        halfYearlyResults.add("Second Half Year");
//
//        halfYearlyAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, halfYearlyResults);
//        halfYearlyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        halfYearlySpinner.setAdapter(halfYearlyAdapter);

        //for yearly
        yearlyResults.clear();
        yearlyResults.add("Select Year");
        int start_year = 2015;
        int current_year = Calendar.getInstance().get(Calendar.YEAR);

        while (start_year <= current_year) {
            int yy = start_year++;
            int zz=yy+1;
            yearlyResults.add(String.valueOf(yy)+"-"+String.valueOf(zz));
            //yearlyResults.add(String.valueOf(yy));
        }

        //for yearly
//        yearlyResults.clear();
//        yearlyResults.add("Select Year");
//        yearlyResults.add("2015");
//        yearlyResults.add("2016");
//        yearlyResults.add("2017");
//        yearlyResults.add("2018");
//        yearlyResults.add("2019");
//        yearlyResults.add("2020");

        yearlyAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, yearlyResults);
        yearlyAdapter.setDropDownViewResource(R.layout.spinner_item);
        yearlySpinner.setAdapter(yearlyAdapter);

        yearlySpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedYearly = parent.getItemAtPosition(position).toString();


                if(!selectedYearly.equalsIgnoreCase("Select Year"))
                {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();

                    mChart.setVisibility(View.GONE);

                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent)
                    {
                        dialog = new ProgressDialog(OrderAnalysisActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
                        AllusersResultu(selectedYearly);
                    }
                    else
                    {
                        //Toast.makeText(OrderAnalysisActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(OrderAnalysisActivity.this, "You don't have internet connection.","");

                    }
                }else {
//                    All_User_Name.clear();
//                    All_Order_Amount.clear();
//                    mChart.invalidate();
//                    mChart.clear();
                    txtYaxisTitle.setVisibility(View.GONE);
                    mChart.setVisibility(View.GONE);
                }


                //Toast.makeText(OrderAnalysisActivity.this, selectedYearly, Toast.LENGTH_SHORT).show();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

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
                SharedPreferences sp = OrderAnalysisActivity.this.getSharedPreferences("SimpleLogic", 0);
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

        Intent i = new Intent(OrderAnalysisActivity.this,DashboardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i);
        finish();
    }

    public void AllusersResultu(String type)
    {
        String domain = getResources().getString(R.string.service_domain);

        SharedPreferences spf = OrderAnalysisActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);
        Log.i("user list url", "user list url " + domain + "orders/get_order_counts_of_filter?email=" + user_email + "&quarter_filter=" + type);

        StringRequest jsObjRequest = null;

        String service_url = "";

        if(Global_Data.BarGraphStatus.equalsIgnoreCase("Last 7 Days")){

            service_url = domain + "orders/get_order_counts_of_filter?email=" + user_email + "&day_filter=" + type;
        }else if(Global_Data.BarGraphStatus.equalsIgnoreCase("Monthly")){
            service_url = domain + "orders/get_order_counts_of_filter?email=" + user_email + "&month_filter=" + type;
        }else if(Global_Data.BarGraphStatus.equalsIgnoreCase("Quarterly")){
            service_url = domain + "orders/get_order_counts_of_filter?email=" + user_email + "&quarter_filter=" + type;
        }else if(Global_Data.BarGraphStatus.equalsIgnoreCase("Half Yearly")){
            service_url = domain + "orders/get_order_counts_of_filter?email=" + user_email + "&half_yearlly_filter=" + type;
        }
        else
        {
            service_url = domain + "orders/get_order_counts_of_filter?email=" + user_email + "&yearlly_filter=" + type;
        }

        Log.i("volley", "service_url: " + service_url);

        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;
                new OrderAnalysisActivity.GetAllUserListn().execute(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Intent intent = new Intent(OrderAnalysisActivity.this, MainActivity.class);
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
                                    "Network   Error","");
                        } else if (error instanceof ParseError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "ParseError   Error","");
                        } else {
                       //     Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(), "");
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

    private class GetAllUserListn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {
            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("result")) {
                    response_result = response.getString("result");
                } else {
                    response_result = "data";
                }

                if (response_result.equalsIgnoreCase("Device not found.")) {
                    OrderAnalysisActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
//                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();
//                            Toast toast = Toast.makeText(OrderAnalysisActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(OrderAnalysisActivity.this, response_result,"yes");

                            Intent intent = new Intent(OrderAnalysisActivity.this, OrderAnalysisActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                else {
                    JSONArray dateArray = response.getJSONArray("date_array");

                    JSONArray orderCountArray = response.getJSONArray("order_count_array");
                    Log.d("volley", "orders" + dateArray.toString());
                    Log.d("volley", "orders" + orderCountArray.toString());

                    if (dateArray.length() <= 0) {

//                        All_User_Name.clear();
//                        All_Order_Amount.clear();

                        OrderAnalysisActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
//                                All_User_Name.clear();
//                                All_Order_Amount.clear();
//                                mChart.invalidate();
//                                mChart.clear();


//                                XAxis xAxis = mChart.getXAxis();
//                                xAxis.setAxisLineColor(getResources().getColor(R.color.demo_light_transparent));
//                                mChart.getAxisLeft().setDrawAxisLine(false);
                                //mChart.getAxisRight().setDrawAxisLine(false);

                               // mChart.notifyDataSetChanged();
                                txtYaxisTitle.setVisibility(View.GONE);

                                mChart.setVisibility(View.GONE);

//                                Toast toast = Toast.makeText(OrderAnalysisActivity.this, "Record doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(OrderAnalysisActivity.this, "Record doesn't exist","yes");
//                                Intent intent = new Intent(OrderAnalysisActivity.this, OrderAnalysisActivity.class);
//                                startActivity(intent);
//                                finish();
                            }
                        });
                    } else {

                        All_User_Name.clear();
                        All_Order_Amount.clear();
//                        XAxis xAxis = mChart.getXAxis();
//                        xAxis.setAxisLineColor(getResources().getColor(R.color.lightgray));
//                        mChart.getAxisLeft().setDrawAxisLine(true);
//                        OrderAnalysisActivity.this.runOnUiThread(new Runnable()
//                        {
//                            public void run() {
//                                mChart.invalidate();
//                                mChart.clear();
//                            }
//                        });

                        if(Global_Data.BarGraphStatus.equalsIgnoreCase("Quarterly") || Global_Data.BarGraphStatus.equalsIgnoreCase("Yearly") || Global_Data.BarGraphStatus.equalsIgnoreCase("Monthly") || Global_Data.BarGraphStatus.equalsIgnoreCase("Half Yearly") || Global_Data.BarGraphStatus.equalsIgnoreCase("Last 7 Days")){
                            for (int i = dateArray.length()-1; i>=0; i--) {
                           // for (int i = 0; i < dateArray.length(); i++) {
                                //JSONObject jsonObject = dateArray.getJSONObject(i);
                                //String aaa= dateArray[i];
                                //All_User_Name.add(dateArray.getString((i)));
                                String currentString = dateArray.getString((i));
                                String[] separated = currentString.split("T");

                                if(Global_Data.BarGraphStatus.equalsIgnoreCase("Quarterly") || Global_Data.BarGraphStatus.equalsIgnoreCase("Half Yearly") || Global_Data.BarGraphStatus.equalsIgnoreCase("Yearly"))
                                {
                                    String dob = separated[0];
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        Date d = sdf.parse(dob);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(d);
                                        String monthString  = (String) DateFormat.format("MMMM",  d);
                                        All_User_Name.add(monthString);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    All_User_Name.add(separated[0]);
                                }
                                All_Order_Amount.add(orderCountArray.getString((i)));
                            }
                            OrderAnalysisActivity.this.runOnUiThread(new Runnable()
                            {
                                public void run() {
                                    dialog.dismiss();
                                    txtYaxisTitle.setVisibility(View.VISIBLE);
                                    mChart.setVisibility(View.VISIBLE);
                                }
                            });
                        }else{
                            for (int i = 0; i < dateArray.length(); i++) {
                                //JSONObject jsonObject = dateArray.getJSONObject(i);
                                //String aaa= dateArray[i];
                                //All_User_Name.add(dateArray.getString((i)));
                                String currentString = dateArray.getString((i));
                                String[] separated = currentString.split("T");

                                if(Global_Data.BarGraphStatus.equalsIgnoreCase("Quarterly") || Global_Data.BarGraphStatus.equalsIgnoreCase("Half Yearly") || Global_Data.BarGraphStatus.equalsIgnoreCase("Yearly"))
                                {
                                    String dob = separated[0];
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        Date d = sdf.parse(dob);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(d);
                                        String monthString  = (String) DateFormat.format("MMMM",  d);
                                        All_User_Name.add(monthString);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    All_User_Name.add(separated[0]);
                                }
                                All_Order_Amount.add(orderCountArray.getString((i)));
                            }
                            OrderAnalysisActivity.this.runOnUiThread(new Runnable()
                            {
                                public void run() {
                                    dialog.dismiss();

                                    txtYaxisTitle.setVisibility(View.VISIBLE);
                                    mChart.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                }
                // }
                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("tagmsg","tagmsg"+e.toString());

                Intent intent = new Intent(OrderAnalysisActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                OrderAnalysisActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }
            OrderAnalysisActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            OrderAnalysisActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                    setData(4, 4);

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

    private void setData(int count, float range) {

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(TargetAnalysisActivity.mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setLabelRotationAngle(10);

        //  xAxis.setSpaceMin(8.7f);
        //xAxis.set();
        // xAxis.setValueFormatter(xAxisFormatter);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                // return DemoBase.Categories[(int) value % DemoBase.Categories.length];
//                if(single_user_check == 1)
//                {
//                    return All_Order_Date.get((int) value % All_Order_Date.size());
//                }
//                else
//                {
                return All_User_Name.get((int) value % All_User_Name.size());
                //}
            }
        });

        //IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        // leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        //leftAxis.setValueFormatter(new LargeValueFormatter());

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(TargetAnalysisActivity.mTfLight);
        rightAxis.setLabelCount(8, false);
        // rightAxis.setValueFormatter(custom);
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

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < All_Order_Amount.size() ; i++) {
            float mult = (range + 1);
            float val = (float) (i);
            yVals1.add(new BarEntry(i, Float.valueOf(All_Order_Amount.get(i))));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            set1.setValueFormatter(new LargeValueFormatter());
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        }
        else {

//            if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.Selected_USER_EMAIL)){
//
//                if(Global_Data.Selected_USER_EMAIL.equalsIgnoreCase("All Users"))
//                {
//
////                    Global_Data.Globel_fromDate
////                    Global_Data.Globel_toDate
//
//                    set1 = new BarDataSet(yVals1, "User Performance");
//                    set1.setColors(ColorTemplate.PASTEL_COLORS);
//                    set1.setValueFormatter(new LargeValueFormatter());
//
//                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//                    dataSets.add(set1);
//
//                    BarData data = new BarData(dataSets);
//                    data.setValueTextSize(10f);
//                    data.setValueTypeface(mTfLight);
//                    data.setBarWidth(0.9f);
//
//                    mChart.setData(data);
//                    mChart.invalidate();
//                }
//                else
//                {
//                    // Global_Data.Selected_USER_EMAIL
//
//                    set1 = new BarDataSet(yVals1, Global_Data.Selected_USER_EMAIL+" Performance");
//                    set1.setColors(ColorTemplate.PASTEL_COLORS);
//
//                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//                    dataSets.add(set1);
//                    set1.setValueFormatter(new LargeValueFormatter());
//
//                    BarData data = new BarData(dataSets);
//                    data.setValueTextSize(10f);
//                    data.setValueTypeface(mTfLight);
//                    data.setBarWidth(0.9f);
//
//                    mChart.setData(data);
//                    mChart.invalidate();
//                }
//
//            }
//            else
//            {
                set1 = new BarDataSet(yVals1, "Order Analysis");
                set1.setColors(ColorTemplate.PASTEL_COLORS);
                set1.setValueFormatter(new LargeValueFormatter());

                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                data.setValueTextSize(10f);
                data.setValueTypeface(TargetAnalysisActivity.mTfLight);

            if(All_User_Name.size() ==1)
            {
                data.setBarWidth(0.4f);
            }
            else
            {
                data.setBarWidth(0.9f);
            }


                mChart.setData(data);
                mChart.invalidate();


            //}

//            Global_Data.Selected_USER_EMAIL = "";
//            Global_Data.Globel_fromDate = "";
//            Global_Data.Globel_toDate = "";

            //  mChart.notifyAll();

            //  Global_Data.GLOVEL_PRODUCT_SERCH_VALUE = "";


            //TODO Prevent zoom factor
            //mChart.fitScreen();
            // mChart.setScaleEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if ((dialog != null) && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }
}

