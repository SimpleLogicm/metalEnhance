package com.msimplelogic.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.kotlinFiles.ActivityTarget;
import com.msimplelogic.activities.kotlinFiles.ActivityViewTarget;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import cpm.simplelogic.helper.ConnectionDetector;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class TargetAchievementActivity extends BaseActivity {
    Toolbar toolbar;
    String selectedItem="";
    String fromDate,toDate;
    Spinner targetAchievementSpinner,taMonthSpinner;
    protected static Typeface mTfLight;
    static String final_response = "";
    ProgressDialog dialog;
    String selectedYearly="";
    String response_result = "";
    //ArrayList <String> All_Order_Amount = new ArrayList<String>();
    ArrayList <String> All_Order_Targets = new ArrayList<String>();
    ArrayList <String> All_Order_Achieves = new ArrayList<String>();
    ArrayList <String> All_User_Name = new ArrayList<String>();
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    SharedPreferences sp;
    ImageView hedder_theame;
    private CoordinatorLayout coordinatorLayout;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    TextView btnDaycoverage,btnViewtarget;
    ArrayAdapter<String> targetAchievementAdapter,yearlyAdapter;

    private ArrayList<String> targetAchievementResults = new ArrayList<String>();
    private ArrayList<String> yearlyResults = new ArrayList<String>();

    private CombinedChart mChart;

    private static final String TAG = MainActivity.class.getSimpleName();

    static final String TAG_ORDERID = "order_id";
    static final String TAG_PRODUCTNM = "product_name";
    static final String TAG_TOTALQTY = "total_qty";
    static final String TAG_RETAILPRICE = "retail_price";
    static final String TAG_MRP = "mrp";
    static final String TAG_AMOUNT = "amount";
    static final String TAG_CUSTSHOPNAME = "shop_name";

    public TargetAchievementActivity() {
    }

    //DataBaseHelper dbvoc = new DataBaseHelper(this);
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_achievement);

        cd  = new ConnectionDetector(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnDaycoverage = findViewById(R.id.btn_daycoverage);
        btnViewtarget = findViewById(R.id.btn_viewtarget);
        hedder_theame = findViewById(R.id.hedder_theame);
        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1){

            hedder_theame.setImageResource(R.drawable.dark_hedder);
            // linearMian.setBackgroundColor(Color.BLACK);



//ll.setBackgroundResource(R.drawable.dark_theme_background);
            //      final int sdk = android.os.Build.VERSION.SDK_INT; if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) { ll.setBackgroundResource(R.drawable.dark_theme_background); } else { ll.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.dark_theme_background)); }
        }

        btnDaycoverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TargetAchievementActivity.this, ActivityTarget.class));
            }
        });

        btnViewtarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TargetAchievementActivity.this,ActivityViewTarget.class));
            }
        });

//        try {
//            ActionBar mActionBar = getActionBar();
//            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            // mActionBar.setDisplayShowHomeEnabled(false);
//            // mActionBar.setDisplayShowTitleEnabled(false);
//            LayoutInflater mInflater = LayoutInflater.from(this);
//            Intent i = getIntent();
//            String name = i.getStringExtra("retialer");
//            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
//            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
//            mTitleTextView.setText(getResources().getString(R.string.TARGET_ACHIEVEMENT));
//
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = TargetAchievementActivity.this.getSharedPreferences("SimpleLogic", 0);
//
////            ImageView H_LOGO = (ImageView) mCustomView.findViewById(R.id.Header_logo);
////            H_LOGO.setImageResource(R.drawable.cal);
////            H_LOGO.setVisibility(View.VISIBLE);
//
//            try {
//                int target = (int) Math.round(sp.getFloat("Target", 0));
//                int achieved = (int) Math.round(sp.getFloat("Achived", 0));
//                Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
//                if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
//                    int age = (int) Math.round(age_float);
//
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
//                    }
//
//                } else {
//                    int age = (int) Math.round(age_float);
//
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    }
//                }
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
////        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
////        	//todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
////			todaysTarget.setText("T/A : Rs "+String.format(sp.getFloat("Target",0)+"/"+sp.getFloat("Achived", 0)));
////		}
////        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)<0) {
//////        	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
////        	todaysTarget.setText("Today's Target Acheived");
////		}
//
//            mActionBar.setCustomView(mCustomView);
//            mActionBar.setDisplayShowCustomEnabled(true);
//            mActionBar.setHomeButtonEnabled(true);
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        targetAchievementSpinner = (Spinner) findViewById(R.id.achievement_spinner);
        taMonthSpinner = (Spinner) findViewById(R.id.achievement_monthly_spinner);
        mChart = (CombinedChart) findViewById(R.id.combinechart);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);

        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisLeft().setDrawGridLines(false);
        //mChart.getAxisLeft().setLabelCount(5, true);
        mChart.getAxisRight().setDrawGridLines(false);

        //mChart.zoom(2f,0f,0f,0f);

        // draw bars behind lines
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(new LargeValueFormatter());

        //for yearly
        yearlyResults.clear();
       // yearlyResults.add("Select Year");
        int start_year = 2015;
        int current_year = Calendar.getInstance().get(Calendar.YEAR);

        while (start_year <= current_year) {
            int yy = start_year++;
            int zz=yy+1;

            yearlyResults.add(String.valueOf(yy)+"-"+String.valueOf(zz));
//            String last_element= yearlyResults.get(yearlyResults.size() - 1);
//            int index = yearlyResults.size() - 1;
//            // Delete last element by passing index
//            yearlyResults.remove(index);
//            yearlyResults.add(last_element);
            Collections.reverse(yearlyResults);
        }

        //for selection
        targetAchievementResults.clear();
        //targetAchievementResults.add("Select Graph Type");
        targetAchievementResults.add("Monthly");
        targetAchievementResults.add("Yearly");

        targetAchievementAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, targetAchievementResults);
        targetAchievementAdapter.setDropDownViewResource(R.layout.spinner_item);
        targetAchievementSpinner.setAdapter(targetAchievementAdapter);

//        targetAchievementSpinner.setPrompt("monthly");
//        taMonthSpinner.setPrompt("2020-2021");
//
//        String[] separated = selectedYearly.split("-");
//        fromDate="01-04-"+separated[0];
//        toDate="31-03-"+separated[1];
//
//        isInternetPresent = cd.isConnectingToInternet();
//        if (isInternetPresent)
//        {
//            dialog = new ProgressDialog(TargetAchievementActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//            dialog.setMessage("Please wait Loading....");
//            dialog.setTitle("Metal App");
//            dialog.setCancelable(false);
//            dialog.show();
//            AllusersTargetResultu("monthly");
//            //AllusersResultu(selectedYearly);
//        }
//        else
//        {
//            //  Toast.makeText(TargetAchievementActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
//            Global_Data.Custom_Toast(TargetAchievementActivity.this, "You don't have internet connection.", "yes");
//        }

        targetAchievementSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Monthly"))
                {
                    selectedItem="monthly";
                    taMonthSpinner.setVisibility(View.VISIBLE);
                    //targetAchievementSpinner.setVisibility(View.GONE);

                    All_Order_Targets.clear();
                    All_Order_Achieves.clear();
                    All_User_Name.clear();
                    mChart.invalidate();
                    mChart.clear();

                    yearlyAdapter = new ArrayAdapter<String>(TargetAchievementActivity.this,
                            R.layout.spinner_item, yearlyResults);
                    yearlyAdapter.setDropDownViewResource(	R.layout.spinner_item);
                    taMonthSpinner.setAdapter(yearlyAdapter);
                    yearlyAdapter.notifyDataSetChanged();


                    // do your stuff
                }else if(selectedItem.equals("Yearly"))
                {
                    Global_Data.BarGraphStatus="yearly";
                    taMonthSpinner.setVisibility(View.GONE);
                    selectedItem="yearly";

//                    yearlyAdapter = new ArrayAdapter<String>(TargetAchievementActivity.this,
//                            android.R.layout.simple_spinner_item, yearlyResults);
//                    yearlyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    taMonthSpinner.setAdapter(yearlyAdapter);
//                    yearlyAdapter.notifyDataSetChanged();

                }else{
                       taMonthSpinner.setVisibility(View.GONE);
                     }

//
//                ArrayAdapter myAdap = (ArrayAdapter) targetAchievementSpinner.getAdapter(); //cast to an ArrayAdapter
//
//                int spinnerPosition = myAdap.getPosition("Monthly");
//
////set the default according to value
//                targetAchievementSpinner.setSelection(spinnerPosition);
//
//                ArrayAdapter myAdap2 = (ArrayAdapter) taMonthSpinner.getAdapter(); //cast to an ArrayAdapter
//
//                int spinnerPosition2 = myAdap2.getPosition("2021-2022");
//
////set the default according to value
//                taMonthSpinner.setSelection(spinnerPosition2);
//
//                String[] separated = selectedYearly.split("-");
//        fromDate="01-04-2021";
//        toDate="31-03-2022";
////
//
//
//                isInternetPresent = cd.isConnectingToInternet();
//                if (isInternetPresent)
//                {
//                    selectedItem="monthly";
//                    dialog = new ProgressDialog(TargetAchievementActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                    dialog.setMessage("Please wait Loading....");
//                    dialog.setTitle("Metal App");
//                    dialog.setCancelable(false);
//                    dialog.show();
//                    AllusersTargetResultu(selectedItem);
//                    dialog.dismiss();
//                    //AllusersResultu(selectedItem);
//                }
//                else
//                {
//                    //  Toast.makeText(TargetAchievementActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
//                    Global_Data.Custom_Toast(TargetAchievementActivity.this, "You don't have internet connection.","yes");
//                }


                if(!selectedItem.equalsIgnoreCase("Select Graph Type"))
                {
                    if(selectedItem.equalsIgnoreCase("yearly"))
                    {
                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent)
                        {
                            dialog = new ProgressDialog(TargetAchievementActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                            dialog.setMessage("Please wait Loading....");
                            dialog.setTitle("Metal App");
                            dialog.setCancelable(false);
                            dialog.show();
                            AllusersTargetResultu(selectedItem);
                            //AllusersResultu(selectedItem);
                        }
                        else
                        {
                          //  Toast.makeText(TargetAchievementActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                            Global_Data.Custom_Toast(TargetAchievementActivity.this, "You don't have internet connection.","yes");
                        }
                    }
                }else {
                    All_Order_Targets.clear();
                    All_Order_Achieves.clear();
                    All_User_Name.clear();
                    mChart.invalidate();
                    mChart.clear();
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        yearlyAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, yearlyResults);
        yearlyAdapter.setDropDownViewResource(	R.layout.spinner_item);
        taMonthSpinner.setAdapter(yearlyAdapter);

        taMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedYearly = parent.getItemAtPosition(position).toString();

                if(!selectedYearly.equalsIgnoreCase("Select Year"))
                {

                    String[] separated = selectedYearly.split("-");
                    fromDate="01-04-"+separated[0];
                    toDate="31-03-"+separated[1];

                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent)
                    {
                        dialog = new ProgressDialog(TargetAchievementActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
                        AllusersTargetResultu(selectedItem);
                        //AllusersResultu(selectedYearly);
                    }
                    else
                    {
                      //  Toast.makeText(TargetAchievementActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(TargetAchievementActivity.this, "You don't have internet connection.", "yes");
                    }
                }

                //Toast.makeText(OrderAnalysisActivity.this, selectedYearly, Toast.LENGTH_SHORT).show();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


//        isInternetPresent = cd.isConnectingToInternet();
//        if (isInternetPresent)
//        {
//            //dialog = new ProgressDialog(TargetAnalysisActivity.this,  R.style.AppTheme_Dark_Dialog);
//            dialog = new ProgressDialog(TargetAchievementActivity.this,  android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//            dialog.setMessage("Please wait Loading....");
//            dialog.setTitle("Metal App");
//            dialog.setCancelable(false);
//            dialog.show();
//            //dialog.getWindow().setGravity(Gravity.CENTER);
//
////
////            dialog_profile_update = new ProgressDialog(EditProfile.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
////            dialog_profile_update.setMessage("Please Wait....");
////            dialog_profile_update.setTitle("Intranet App");
////            dialog_profile_update.setCancelable(false);
////            dialog_profile_update.show();
////
//
//            //dialog.show(this,"Metal App","Please wait Target Loading...", true,false);
//            AllusersTargetResultu();
//        }
//        else
//        {
//
////            Snackbar snackbar = Snackbar.make(coordinatorLayout, "You don't have internet connection.", Snackbar.LENGTH_LONG);
////            snackbar.show();
//        }
    }

    private LineData generateLineData() {
        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();

        List<Local_Data> contacts2 = dbvoc.getTarget();
        int a = 0;
        Float fx = 0.0f;

        if(contacts2.size() > 0) {
            for (Local_Data cn : contacts2) {
                // entries1.add(new BarEntry(0, Float.valueOf(cn.get_Target_Text())));
                fx += Float.valueOf(cn.get_Target_Achieved());
            }
        }

        //  entries.add(new Entry(0.26f, fx));
//
//        for (int index = 0; index < itemcount; index++)
//            entries.add(new Entry(index + 0.26f, getRandom(15, 5)));

//        entries.add(new Entry(0+0.26f, Float.valueOf(22457)));
//        entries.add(new Entry(1+0.26f, Float.valueOf(42457)));
//        entries.add(new Entry(2+0.26f, Float.valueOf(12457)));
//        entries.add(new Entry(3+0.26f, Float.valueOf(32457)));
//        entries.add(new Entry(4+0.26f, Float.valueOf(72457)));
//        entries.add(new Entry(5+0.26f, Float.valueOf(52457)));
        for (int i = 0; i < All_Order_Achieves.size() ; i++) {

            entries.add(new Entry(i+0.26f, Float.valueOf(All_Order_Achieves.get(i))));
        }

        LineDataSet set = new LineDataSet(entries, "Achieved");
        set.setColor(Color.rgb(131, 131, 131));
        set.setLineWidth(3f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(220, 80, 78));
        set.setValueFormatter(new com.github.mikephil.charting.formatter.LargeValueFormatter());

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();

//        List<Local_Data> contacts2 = dbvoc.getTarget();
//        int a = 0;
//        Float fx = 0.0f;
//
//        if(contacts2.size() > 0) {
//            for (Local_Data cn : contacts2) {
//
//               // entries.add(new Entry(a+ 0.5f, Float.valueOf(cn.get_Target_Achieved())));
//               //  entries1.add(new BarEntry(0, Float.valueOf(cn.get_Target_Text())));
//                fx += Float.valueOf(cn.get_Target_Text());
//            }
//        }
//
//        fx += 100000.0f;
        //   entries1.add(new BarEntry(0, fx));

//        for (int index = 0; index < itemcount; index++) {
//            entries1.add(new BarEntry(0, getRandom(25, 25)));}

        for (int i = 0; i < All_Order_Targets.size() ; i++) {

            entries1.add(new BarEntry(0, Float.valueOf(All_Order_Targets.get(i))));
            //entries.add(new Entry(index + 0.26f, getRandom(15, 5)));
        }

        // entries2.add(new BarEntry(0, getRandom(13, 12)));

        // stacked
        //entries2.add(new BarEntry(0, new float[]{getRandom(13, 12)}));
        //  }

        BarDataSet set1 = new BarDataSet(entries1, "Targets");
        set1.setColor(Color.rgb(61, 165, 255));
        set1.setValueTextColor(Color.BLACK);
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setValueFormatter(new com.github.mikephil.charting.formatter.LargeValueFormatter());

//        BarDataSet set2 = new BarDataSet(entries1, "Bar 2");
//        set2.setColor(Color.rgb(61, 165, 255));
//        set2.setValueTextColor(Color.rgb(61, 165, 255));
//        set2.setValueTextSize(10f);
//        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        // set1.set

        BarDataSet set2 = new BarDataSet(entries2, "");
        // set2.setStackLabels(new String[]{"Stack 1"});
        set2.setColors(Color.rgb(61, 165, 255));
        set2.setValueTextColor(Color.rgb(61, 165, 255));
        set2.setValueTextSize(0f);
        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);


        BarData d = new BarData(set1,set2);


        if(All_Order_Targets.size() ==1)
        {
            float groupSpace = 0.08f;
            float barSpace = 0.2f; // x2 dataset
            float barWidth = 0.2f; // x2 dataset
            // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

            d.setBarWidth(barWidth);
            d.groupBars(0, groupSpace, barSpace); // start at x = 0
        }
        else
        {
            float groupSpace = 0.08f;
            float barSpace = 0.0f; // x2 dataset
            float barWidth = 0.45f; // x2 dataset
            // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

            //BarData d = new BarData(set1,set2);
            d.setBarWidth(barWidth);
            d.groupBars(0, groupSpace, barSpace); // start at x = 0
        }


        // make this BarData object grouped


        //Global_Data.Target_user_flag = "";

        return d;
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
                SharedPreferences sp = TargetAchievementActivity.this.getSharedPreferences("SimpleLogic", 0);
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        this.finish();
    }

    public void AllusersTargetResultu(String selectedItem)
    {
        String domain = getResources().getString(R.string.service_domain);
        //String domain = "http://150.242.140.105:8005/metal/api/performance/v1/";

        SharedPreferences spf = TargetAchievementActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);
        //Log.i("user list url", "user list url " + domain + "users/user_target_performance?imei_no=" + Global_Data.imei_no + "&email=" + Global_Data.GLOvel_USER_EMAIL+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate);

        StringRequest jsObjRequest = null;

        String service_url = "";

//        if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.Selected_USER_EMAIL)){
//
//            if(Global_Data.Selected_USER_EMAIL.equalsIgnoreCase("All Users"))
//            {
//                service_url = domain + "users/user_target_performance?imei_no=" + Global_Data.imei_no + "&email=" + Global_Data.GLOvel_USER_EMAIL+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate;
//            }
//            else
//            {
//                service_url = domain + "users/user_target_performance?imei_no=" + Global_Data.imei_no + "&email=" + Global_Data.GLOvel_USER_EMAIL+ "&user_name=" + Global_Data.Selected_USER_EMAIL+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate;
//            }
//
//        }
//        else
//        {

        if(selectedItem.equalsIgnoreCase("monthly"))
        {
            service_url = domain + "users/user_monthly_yearly_target_performance?email=" + user_email+"&type="+selectedItem+"&from_date="+fromDate+"&to_date="+toDate;

        }else if(selectedItem.equalsIgnoreCase("yearly"))
        {
            service_url = domain + "users/user_monthly_yearly_target_performance?email=" + user_email+"&type="+selectedItem;

        }
               //}

        Log.i("volley", "service_url: " + service_url);

        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;

                new TargetAchievementActivity.GetAllUserTargetListn().execute(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();

//                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Network Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Network Error","yes");
                        } else if (error instanceof AuthFailureError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Server AuthFailureError  Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Server AuthFailureError  Error","yes");
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
                                    "Network   Error","yes");
                        } else if (error instanceof ParseError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "ParseError   Error","");
                        } else {
                        //   Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(), "yes");
                        }
                        //dialog.dismiss();
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

    private class GetAllUserTargetListn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {
            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("message")) {
                    response_result = response.getString("message");
                } else {
                    response_result = "data";
                }

                if (response_result.equalsIgnoreCase("User doesn't exist")) {
                    TargetAchievementActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
//                            Toast toast = Toast.makeText(TargetAchievementActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Global_Data.Custom_Toast(TargetAchievementActivity.this, response_result,"yes");

//                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    });
                }
                else
                if(response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    TargetAchievementActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();
//
//                            Toast toast = Toast.makeText(TargetAchievementActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
//
                            Global_Data.Custom_Toast(TargetAchievementActivity.this, response_result,"yes");

//                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    });
                }
                else
                if(response_result.equalsIgnoreCase("Device not registered")) {
                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    TargetAchievementActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(TargetAchievementActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Global_Data.Custom_Toast(TargetAchievementActivity.this, response_result, "yes");

                            Intent intent = new Intent(TargetAchievementActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                else {
                    JSONArray result = response.getJSONArray("result");
                    Log.i("volley", "response orders Length: " + result.length());
                    Log.d("volley", "orders" + result.toString());

                    if (result.length() <= 0) {
                        TargetAchievementActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
                                All_Order_Targets.clear();
                                All_Order_Achieves.clear();
                                All_User_Name.clear();
                                mChart.invalidate();
                                mChart.clear();
//                                Toast toast = Toast.makeText(TargetAchievementActivity.this, "Targets doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

                                Global_Data.Custom_Toast(TargetAchievementActivity.this, "Targets doesn't exist", "yes");
                            }
                        });
                    } else {

                        All_Order_Targets.clear();
                        All_Order_Achieves.clear();
                        All_User_Name.clear();

                        for (int i = 0; i < result.length(); i++) {
                            JSONObject jsonObject = result.getJSONObject(i);

//                            if (!(All_User_Name.contains(jsonObject.getString("user")))) {
//                                single_user_check = 1;
//                            }
                            All_Order_Targets.add(jsonObject.getString("target"));
                            All_Order_Achieves.add(jsonObject.getString("achieved"));

//                            if(Global_Data.Selected_USER_EMAIL.equalsIgnoreCase("All Users") || Global_Data.Selected_USER_EMAIL.equalsIgnoreCase(""))
//                            {
//                                All_User_Name.add(jsonObject.getString("user_name"));
//                            }
//                            else
//                            {

                            All_User_Name.add(jsonObject.getString("date"));
//                            Arrays.sort(new ArrayList[]{All_User_Name});
//
//                            // Print out array item after ording.
//                            for(String codingLang : All_User_Name)
//                            {
//                                All_User_Name.add(codingLang);
//                            }

                            //}
                        }
//                        Global_Data.Selected_USER_EMAIL = "";
//                        Global_Data.Globel_fromDate = "";
//                        Global_Data.Globel_toDate = "";

                        TargetAchievementActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                    }

                    TargetAchievementActivity.this.runOnUiThread(new Runnable() {
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

//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();

                TargetAchievementActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });
            }

            TargetAchievementActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            TargetAchievementActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //dialog.dismiss();

                    if(All_User_Name.size() > 0) {
                        XAxis xAxis = mChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        //xAxis.setAxisMinimum(0f);
                        xAxis.setGranularity(1f);
                        xAxis.setLabelCount(10);
                        xAxis.setValueFormatter(new IAxisValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                // return abc.get((int) value % abc.size());
                                return All_User_Name.get((int) value % All_User_Name.size());
                            }
                        });

                        CombinedData data = new CombinedData();
                        data.setData(generateLineData());
                        data.setData(generateBarData());
//        data.setData(generateBubbleData());
//        data.setData(generateScatterData());
//        data.setData(generateCandleData());
                        data.setValueTypeface(mTfLight);
                        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                        xAxis.setLabelRotationAngle(10);
                        //xAxis.setLabelCount(4,true);
                        mChart.setData(data);
                        mChart.invalidate();
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

    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQUEST_CODE_SETTINGS:
//                reload();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
