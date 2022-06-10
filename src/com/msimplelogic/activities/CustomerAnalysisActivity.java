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

        import com.google.android.material.bottomnavigation.BottomNavigationView;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.coordinatorlayout.widget.CoordinatorLayout;
        import com.google.android.material.snackbar.Snackbar;
        import androidx.drawerlayout.widget.DrawerLayout;
        import androidx.appcompat.widget.Toolbar;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;

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

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import java.io.UnsupportedEncodingException;
        import java.net.URLEncoder;
        import java.util.ArrayList;
        import cpm.simplelogic.helper.ConnectionDetector;
        import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
//        import static com.v.simplelogic.myapplication.DemoBase.mTfLight;

public class CustomerAnalysisActivity extends BaseActivity implements OnChartValueSelectedListener {
    private String xAxis_Flag = "";
    private String xAxis_Flag_n = "";
    static String final_response = "";
    String response_result = "";
    ArrayList<String> All_CUSTOMERS = new ArrayList<String>();
    ArrayList <String> All_Quantity = new ArrayList<String>();
    Button piechartBtn;
    LinearLayout filterBtn;
    ProgressDialog dialog;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private CoordinatorLayout coordinatorLayout;
    protected BarChart mChart;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    String s[];
    ArrayList <String> abc = new ArrayList<String>();

    private static final String TAG = CustomerAnalysisActivity.class.getSimpleName();

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customar_analysis_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
       //setTitle(getResources().getString(R.string.CUSTOMER_ANALYSIS));
//        bottomNavigationView = (BottomNavigationView)
//                findViewById(R.id.bottom_navigation);
//        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        Global_Data.GLOVEL_PRODUCT_SERCH_VALUE = "";

//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
//        BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(3);
//        item.setEnabled(false);
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
//                .coordinatorLayout);
//
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
//        }

        piechartBtn = (Button) findViewById(R.id.piechart_btn);
        filterBtn = (LinearLayout) findViewById(R.id.filter_btn);
        mChart = (BarChart) findViewById(R.id.chart1);
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

//        mChart.setXAxisRenderer(new CustomXAxisRenderer(mChart.getViewPortHandler(), mChart.getXAxis(), mChart.getTransformer(YAxis.AxisDependency.LEFT)));
        // mChart.setDrawYLabels(false);

        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

//        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart

        //setData(4, 4);

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
//            mTitleTextView.setText(getResources().getString(R.string.CUSTOMER_ANALYSIS));
//
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = CustomerAnalysisActivity.this.getSharedPreferences("SimpleLogic", 0);
//
////	       if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
////	       	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
////			}
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
//
//                } else {
//                    int age = (int) Math.round(age_float);
//
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    }
//
//                }
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
////	       	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
//                todaysTarget.setText("Today's Target Acheived");
//            }
//
//            mActionBar.setCustomView(mCustomView);
//            mActionBar.setDisplayShowCustomEnabled(true);
//            mActionBar.setHomeButtonEnabled(true);
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        cd = new ConnectionDetector(getApplicationContext());

        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
            dialog = new ProgressDialog(CustomerAnalysisActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Please wait Customers Loading....");
            dialog.setTitle("Metal App");
            dialog.setCancelable(false);
            dialog.show();

            customers_Result();
        }
        else
        {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "You don't have internet connection.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }


        piechartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerAnalysisActivity.this, CustomerAnalysisPieActivity.class);
                startActivity(intent);
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerAnalysisActivity.this, FilterCustomerSearchActivity.class);
                startActivity(intent);
            }
        });

//        bottomNavigationView.setOnNavigationItemSelectedListener(
//                new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.product_p:
////                                List<Local_Data> contacts2 = dbvoc.getProductPERFORMANCEDATA();
////
////                                if(contacts2.size() > 0) {
//                                Intent pbc = new Intent(getApplicationContext(),CustomerAnalysisActivity.class);
//                                pbc.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(pbc);
//                                finish();
////                                }
////                                else
////                                {
////                                    Toast.makeText(getApplicationContext(),"Product not found in database, Please sync..",Toast.LENGTH_LONG).show();
////                                }
//                                break;
//
//                            case R.id.user_p:
//                                Intent ubc = new Intent(getApplicationContext(),User_Performance.class);
//                                ubc.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(ubc);
//                                finish();
//                                break;
//                            case R.id.customert_p:
//                                Intent cbc = new Intent(getApplicationContext(),Customer_Performance.class);
//                                cbc.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(cbc);
//                                finish();
//
//                                break;
//
//                            case R.id.beat_p:
//                                Intent abc = new Intent(getApplicationContext(),Beat_Performance.class);
//                                abc.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(abc);
//                                finish();
//
//                                break;
//
//                            case R.id.target_p:
//                                Intent tbc = new Intent(getApplicationContext(),MainActivity.class);
//                                tbc.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(tbc);
//                                finish();
//
//                                break;
//                        }
//                        return true;
//                    }
//                });


    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        MenuItem chartMenuItem = menu.findItem(R.id.action_action_Sync);
//        chartMenuItem.setVisible(false);
//        MenuItem bedMenuItem = menu.findItem(R.id.action_action_Filter);
//
//        bedMenuItem.setTitle("Filter Customer");
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_action_Sync) {
//
//            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Check updates.", Snackbar.LENGTH_LONG);
//            snackbar.show();
//
//            return true;
//        }
//        else
//        if (item.getItemId() == android.R.id.home) {
//            //  finish(); // close this activity and return to preview activity (if there is any)
//            //dialog.dismiss();
//
//            onBackPressed();
//
//
//        }
//        else
//        if (id == R.id.action_action_Filter) {
//
//            Intent i=new Intent(getApplicationContext(), Customer_Search.class);
//
//            startActivity(i);
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onBackPressed() {
        Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE = "";
        Global_Data.GLOVEL_CUSTOMER_SERCH_State = "";
        Global_Data.GLOVEL_CUSTOMER_SERCH_City = "";
        Global_Data.GLOVEL_CUSTOMER_SERCH_Beat = "";

        Intent intent = new Intent(CustomerAnalysisActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();

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
                SharedPreferences sp = CustomerAnalysisActivity.this.getSharedPreferences("SimpleLogic", 0);
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


    private void setData(int count, float range) {

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(TargetAchievementActivity.mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setLabelRotationAngle(10);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);



//        LimitLine llXAxis = new LimitLine(10f, "Index 10");
//        llXAxis.setLineWidth(4f);
//        llXAxis.enableDashedLine(10f, 10f, 0f);
//        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        llXAxis.setTextSize(10f);
//
//        xAxis.addLimitLine(llXAxis);
        //mChart.setViewPortOffsets(60, 0, 50, 60);
        //  xAxis.setValueFormatter(new LargeValueFormatter());

        // mChart.setExtraBottomOffset(100);
        // xAxis.setCenterAxisLabels(trufe);
        //xAxis.setAxisLineWidth(3);

        //  xAxis.setMultiLineLabel(true);
        // xAxis.setCenterAxisLabels(true);
        //xAxis.setAxisLineWidth(5);
        //xAxis.setSpaceMin(20);


        // xAxis.setm
        //xAxis.set();
        // xAxis.setValueFormatter(xAxisFormatter);


        Global_Data.FINAL_PI_Y = All_Quantity;

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                // return DemoBase.Categories[(int) value % DemoBase.Categories.length];
                return All_CUSTOMERS.get((int) value % All_CUSTOMERS.size());
            }
        });

        Global_Data.FINAL_PI_X = All_CUSTOMERS;

        IAxisValueFormatter custom = new MyAxisValueFormatter_Doller();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(TargetAchievementActivity.mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(new LargeValueFormatter());

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(TargetAchievementActivity.mTfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setDrawLabels(false);
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
            // yVals1.add(new BarEntry(i, Float.valueOf(All_Quantity.get(i))));
            yVals1.add(new BarEntry(i, Float.valueOf(All_Quantity.get(i))));
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            set1.setValueFormatter(new LargeValueFormatter());

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {

            try
            {
                if(xAxis_Flag_n.indexOf("@") > 0)
                {
                    String s[] = xAxis_Flag_n.split("@");

                    // Global_Data.F_PRODUCT_NAME = s[1];
                    set1 = new BarDataSet(yVals1, s[1]+" Performance");
                    set1.setColors(ColorTemplate.JOYFUL_COLORS);
                    set1.setValueFormatter(new LargeValueFormatter());

                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                    dataSets.add(set1);

                    BarData data = new BarData(dataSets);
                    data.setValueTextSize(10f);
                    data.setValueTypeface(TargetAchievementActivity.mTfLight);

                    if(All_CUSTOMERS.size() ==1)
                    {
                        data.setBarWidth(0.4f);
                    }
                    else
                    {
                        data.setBarWidth(0.9f);
                    }


                    mChart.setData(data);
                    mChart.invalidate();


                }
                else
                {

                    // Global_Data.F_PRODUCT_NAME = "Categories";
                    set1 = new BarDataSet(yVals1, "States Performance");
                    set1.setColors(ColorTemplate.JOYFUL_COLORS);

                    ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                    dataSets.add(set1);
                    set1.setValueFormatter(new LargeValueFormatter());

                    BarData data = new BarData(dataSets);
                    data.setValueTextSize(10f);
                    data.setValueTypeface(TargetAchievementActivity.mTfLight);

                    if(All_CUSTOMERS.size() ==1)
                    {
                        data.setBarWidth(0.4f);
                    }
                    else
                    {
                        data.setBarWidth(0.9f);
                    }

                    //data.setBarWidth(0.9f); // set custom bar width

                    // mChart.invalidate();
                    mChart.getLegend().setWordWrapEnabled(true);
                    mChart.setData(data);
                    //  mChart.setScaleMinima(8f, 1f);
                    // mChart.setFitBars(true);
                    mChart.invalidate();
                }

            }catch(Exception ex){ex.printStackTrace();}

            Global_Data.Globel_fromDate = "";
            Global_Data.Globel_toDate = "";
            Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE = "";
            Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE_NEW = "";
            Global_Data.GLOVEL_CUSTOMER_SERCH_State = "";
            Global_Data.GLOVEL_CUSTOMER_SERCH_City = "";
            Global_Data.GLOVEL_CUSTOMER_SERCH_Beat = "";

            //  Global_Data.GLOVEL_PRODUCT_SERCH_VALUE = "";


            //TODO Prevent zoom factor
            //mChart.fitScreen();
            // mChart.setScaleEnabled(false);
        }
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


    public void customers_Result()
    {
        SharedPreferences spf = CustomerAnalysisActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        String domain = getResources().getString(R.string.service_domain1);

        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);
        Log.i("user list url", "user list url " + domain + "metal/api/performance/v1/customers?email=" + user_email+"&type="+"customer_performance");

        StringRequest jsObjRequest = null;

        String service_url = "";

        if(Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE.equalsIgnoreCase(""))
        {
            xAxis_Flag = "States";
            xAxis_Flag_n = "States";
            service_url = domain + "metal/api/performance/v1/customers?email=" + user_email +"&type="+"customer_performance";
        }
        else
        {
            String Search_val[] = Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE.split("@");
            String Search_valn[] = Global_Data.GLOVEL_CUSTOMER_SERCH_VALUE_NEW.split("@");

            if(Search_valn[0].length()>0 && Search_valn[1].equalsIgnoreCase("CUSTOMER"))
            {
                try {
                    service_url = domain + "metal/api/performance/v1/customers?email=" + user_email+"&state="+URLEncoder.encode(Global_Data.GLOVEL_CUSTOMER_State, "UTF-8")+"&city="+URLEncoder.encode(Global_Data.GLOVEL_CUSTOMER_City, "UTF-8")+"&beat="+URLEncoder.encode(Global_Data.GLOVEL_CUSTOMER_Beat, "UTF-8")+"&customer="+URLEncoder.encode(Search_valn[0], "UTF-8")+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate+"&type="+"customer_performance";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }else
            if(Search_val[1].equalsIgnoreCase("ALL STATES"))
            {
                xAxis_Flag = "States";
                xAxis_Flag_n = "States";
                service_url = domain + "metal/api/performance/v1/customers?email=" + user_email+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate+"&type="+"customer_performance";
            }
            else
            if(Search_val[1].equalsIgnoreCase("ALL CITIES"))
            {

                xAxis_Flag = "States"+"@"+Search_val[0];
                xAxis_Flag_n =  "States"+"@"+Search_valn[0];
                try {
                    service_url = domain + "metal/api/performance/v1/customers?email=" + user_email+"&state="+ URLEncoder.encode(Search_val[0], "UTF-8")+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate+"&type="+"customer_performance";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            else
            if(Search_val[1].equalsIgnoreCase("ALL BEATS"))
            {

                xAxis_Flag = "Cities"+"@"+Search_val[0];
                xAxis_Flag_n =  "Cities"+"@"+Search_valn[0];
                try {
                    service_url = domain + "metal/api/performance/v1/customers?email="+ user_email+"&state="+ URLEncoder.encode(Global_Data.GLOVEL_CUSTOMER_SERCH_State, "UTF-8")+"&city="+ URLEncoder.encode(Search_val[0], "UTF-8")+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate+"&type="+"customer_performance";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            else
            if(Search_val[1].equalsIgnoreCase("All CUSTOMERS"))
            {
                xAxis_Flag = "Beats"+"@"+Search_val[0];
                xAxis_Flag_n =  "Beats"+"@"+Search_valn[0];
                try {
                    service_url = domain + "metal/api/performance/v1/customers?email=" + user_email+"&state="+ URLEncoder.encode(Global_Data.GLOVEL_CUSTOMER_SERCH_State, "UTF-8")+"&city="+ URLEncoder.encode(Global_Data.GLOVEL_CUSTOMER_SERCH_City, "UTF-8")+"&beat="+URLEncoder.encode(Search_val[0], "UTF-8")+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate+"&type="+"customer_performance";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            else
            if(Search_val[1].equalsIgnoreCase("CUSTOMER"))
            {
                xAxis_Flag = "Customer"+"@"+Search_val[0];
                xAxis_Flag_n =  "Customer"+"@"+Search_valn[0];
                try {
                    service_url = domain + "metal/api/performance/v1/customers?email=" + user_email+"&state="+URLEncoder.encode(Global_Data.GLOVEL_CUSTOMER_SERCH_State, "UTF-8")+"&city="+URLEncoder.encode(Global_Data.GLOVEL_CUSTOMER_SERCH_City, "UTF-8")+"&beat="+URLEncoder.encode(Global_Data.GLOVEL_CUSTOMER_SERCH_Beat, "UTF-8")+"&customer="+URLEncoder.encode(Search_val[0], "UTF-8")+"&from_date="+Global_Data.Globel_fromDate+ "&to_date="+ Global_Data.Globel_toDate+"&type="+"customer_performance";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("volley", "service_url: " + service_url);

        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;

                new CustomerAnalysisActivity.CustomersPerformance_Task().execute(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();

                        Intent intent = new Intent(CustomerAnalysisActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
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
                                    "Server   Error","");
//                            Toast.makeText(getApplicationContext(),
//                                    "Server   Error",
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
                           // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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

    private class CustomersPerformance_Task extends AsyncTask<String, Void, String> {
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

                    CustomerAnalysisActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

                            Global_Data.Custom_Toast(CustomerAnalysisActivity.this, response_result,"Yes");
//                            Toast toast = Toast.makeText(CustomerAnalysisActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

//                            Intent intent = new Intent(CustomerAnalysisActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    });

                }
                else
                if(response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    CustomerAnalysisActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

                            Global_Data.Custom_Toast(CustomerAnalysisActivity.this, response_result,"Yes");
//                            Toast toast = Toast.makeText(CustomerAnalysisActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

//                            Intent intent = new Intent(CustomerAnalysisActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    });
                }
                else {

                    //dbvoc.getDeleteTable("delivery_products");

                    JSONArray orders = response.getJSONArray("orders");
                    Log.i("volley", "response orders Length: " + orders.length());
                    Log.d("volley", "orders" + orders.toString());


                    //
                    if (orders.length() <= 0) {

                        CustomerAnalysisActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                                Global_Data.Custom_Toast(CustomerAnalysisActivity.this, "Customers Orders Record doesn't exist","Yes");
//                                Toast toast = Toast.makeText(CustomerAnalysisActivity.this, "Customers Orders Record doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

//                                Intent intent = new Intent(CustomerAnalysisActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();
                            }
                        });
                    } else {


                        All_CUSTOMERS.clear();
                        All_Quantity.clear();
                        for (int i = 0; i < orders.length(); i++) {

                            JSONObject jsonObject = orders.getJSONObject(i);

//                            if (!(All_User_Name.contains(jsonObject.getString("user")))) {
//                                single_user_check = 1;
//                            }

                            All_CUSTOMERS.add(jsonObject.getString("x_axis_data"));
                            // All_CUSTOMERS.add(StringOp.CutString(jsonObject.getString("x_axis_data"),5));
                            All_Quantity.add(jsonObject.getString("total_order_amount"));



                        }
                        CustomerAnalysisActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {
                                if(Global_Data.PieStatus.length()>0)
                                {
                                    Intent intent = new Intent(CustomerAnalysisActivity.this, CustomerAnalysisPieActivity.class);
                                    startActivity(intent);
                                    Global_Data.PieStatus="";
                                }

                                //  dialog.dismiss();
                                dialog.dismiss();
                            }
                        });

                        CustomerAnalysisActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {



                                dialog.dismiss();
                            }
                        });



                    }

                    CustomerAnalysisActivity.this.runOnUiThread(new Runnable() {
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

                CustomerAnalysisActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

                Intent intent = new Intent(CustomerAnalysisActivity.this, MainActivity.class);
                startActivity(intent);
                finish();



            }


            CustomerAnalysisActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            CustomerAnalysisActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    setData(4, 4);
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

