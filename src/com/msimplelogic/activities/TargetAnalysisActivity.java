package com.msimplelogic.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
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

import com.msimplelogic.adapter.AdapterTargetAnalysisOrderList;
import com.msimplelogic.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cpm.simplelogic.helper.ConnectionDetector;

public class TargetAnalysisActivity extends Activity {
    private ArrayList<Product> dataOrder;
    //private SwipeListView swipeListView;
    private ListView swipeListView;
    private static final int REQUEST_CODE_SETTINGS = 0;
    private AdapterTargetAnalysisOrderList adapter;
    ArrayList<HashMap<String, String>> SwipeList;
    protected static Typeface mTfLight;
    static String final_response = "";
    static String final_response1 = "";
    ProgressDialog dialog;
    String response_result = "";
    String response_result1 = "";
    ArrayList <String> All_Order_Amount = new ArrayList<String>();
    ArrayList <String> All_Order_Targets = new ArrayList<String>();
    ArrayList <String> All_Order_Achieves = new ArrayList<String>();
    ArrayList <String> All_User_Name = new ArrayList<String>();
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private CoordinatorLayout coordinatorLayout;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private GridLayoutManager lLayout;
    private CombinedChart mChart;
    private final int itemcount = 12;
    private FloatingActionButton fab;
    private RecyclerView recyclerviewOrderlist;
    //private AdapterTargetAnalysisOrderList adapterTargetAnalysisOrderList;
//    private ArrayList<Image> images;
    BottomNavigationView bottomNavigationView;
    String s[];
    ArrayList <String> abc = new ArrayList<String>();
    private static final String TAG = MainActivity.class.getSimpleName();

    static final String TAG_ORDERID = "order_id";
    static final String TAG_PRODUCTNM = "product_name";
    static final String TAG_TOTALQTY = "total_qty";
    static final String TAG_RETAILPRICE = "retail_price";
    static final String TAG_MRP = "mrp";
    static final String TAG_AMOUNT = "amount";
    static final String TAG_CUSTSHOPNAME = "shop_name";

    public TargetAnalysisActivity() {
    }

    //DataBaseHelper dbvoc = new DataBaseHelper(this);
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_target_analysis);
//        bottomNavigationView = (BottomNavigationView)
//                findViewById(R.id.bottom_navigation);
//        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
//        BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(1);
//        item.setEnabled(false);
        // item.setVisibility(View.GONE);

       // recyclerviewOrderlist = (RecyclerView) findViewById(R.id.recyclerview_orderlist);

        SwipeList=new ArrayList<HashMap<String, String>>();
        swipeListView = (ListView) findViewById(R.id.taorder_list);
        cd  = new ConnectionDetector(getApplicationContext());
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //initNavigationDrawer();

//        Global_Data.GLOVEL_PRODUCT_SERCH_VALUE = "";
//        Global_Data.GLOVEL_PRODUCT_SERCH_VALUE = "";
//        Global_Data.GLOVEL_PRODUCT_SERCH_Category = "";
//        Global_Data.GLOVEL_PRODUCT_SERCH_Sub_Category = "";



//        ArrayList<String> f_list = new ArrayList<String>();
//
//        f_list.add("Product Performance");
//        f_list.add("User Performance");
//        f_list.add("Customer Performance");
//        f_list.add("Beat Performance");
//        f_list.add("User Performance With Targets");
//
//        images = new ArrayList<>();
//        for(int i=0; i<f_list.size(); i++)
//        {
//            Image image = new Image();
//            image.setName(f_list.get(i));
//            images.add(image);
//        }
//
//        mAdapter = new Footer_Menu(getApplicationContext(), images);
//
//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recycler_view_menu.setLayoutManager(layoutManager);
//        recycler_view_menu.setItemAnimator(new DefaultItemAnimator());
//        recycler_view_menu.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();

//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar snackbar = Snackbar.make(coordinatorLayout, "click", Snackbar.LENGTH_LONG);
//                snackbar.show();
//            }
//        });

        String orderid="";

        SwipeList.clear();

        //List<Local_Data> contacts = dbvoc.getOrderIdsn(Global_Data.GLOvel_CUSTOMER_ID,"Secondary Sales / Retail Sales");
//        List<Local_Data> contacts = dbvoc.getOrderIdsnAll("Secondary Sales / Retail Sales");
//        for (Local_Data cn : contacts)
//        {
//            orderid = cn.getCust_Code();
//
//            HashMap<String, String> mapp = new HashMap<String, String>();
//            mapp.put(TAG_ORDERID, orderid);
//            mapp.put(TAG_PRODUCTNM, cn.getc_name());
//
//
//            //txttotalPreview.setText("Total		:		"+cnt1.getAmount());
//            //Toast.makeText(NewOrderActivity.this, "Login:"+Global_Data.order_id,Toast.LENGTH_SHORT).show();
//            SwipeList.add(mapp);
//
//        }

        adapter = new AdapterTargetAnalysisOrderList(TargetAnalysisActivity.this, SwipeList);

        if (cd.isConnectingToInternet())
        {
//            dialog = new ProgressDialog(TargetAnalysisActivity.this,  android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//            dialog.setMessage("Please wait Target Loading....");
//            dialog.setTitle("Metal App");
//            dialog.setCancelable(false);
//            dialog.show();

            TargetOrderResult();
        }
        else
        {
//            Toast toast = Toast.makeText(TargetAnalysisActivity.this,"You don't have internet connection.", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
            Global_Data.Custom_Toast(TargetAnalysisActivity.this,"You don't have internet connection.","yes");

            Intent order_home = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(order_home);
            finish();
        }

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
            mTitleTextView.setText(getResources().getString(R.string.TARGET_ANALYSIS));

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = TargetAnalysisActivity.this.getSharedPreferences("SimpleLogic", 0);

//            ImageView H_LOGO = (ImageView) mCustomView.findViewById(R.id.Header_logo);
//            H_LOGO.setImageResource(R.drawable.cal);
//            H_LOGO.setVisibility(View.VISIBLE);

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

//        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//        	//todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//			todaysTarget.setText("T/A : Rs "+String.format(sp.getFloat("Target",0)+"/"+sp.getFloat("Achived", 0)));
//		}
//        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)<0) {
////        	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
//        	todaysTarget.setText("Today's Target Acheived");
//		}

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        if (Build.VERSION.SDK_INT >= 11) {
//            swipeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            swipeListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//
//                @Override
//                public void onItemCheckedStateChanged(ActionMode mode, int position,
//                                                      long id, boolean checked) {
//                    mode.setTitle("Selected (" + swipeListView.getCountSelected() + ")");
//                }
//
//                @Override
//                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                    int id = item.getItemId();
//                    if (id == R.id.menu_delete) {
//                        swipeListView.dismissSelected();
//                        return true;
//                    }
//                    return false;
//                }
//
//                @Override
//                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                    MenuInflater inflater = mode.getMenuInflater();
//                    inflater.inflate(R.menu.menu_choice_items, menu);
//                    return true;
//                }
//
//                @Override
//                public void onDestroyActionMode(ActionMode mode) {
//                    swipeListView.unselectedChoiceStates();
//                }
//
//                @Override
//                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                    return false;
//                }
//            });
//        }
//
//        swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
//            @Override
//            public void onOpened(int position, boolean toRight) {
//
//            }
//
//            @Override
//            public void onClosed(int position, boolean fromRight) {
//            }
//
//            @Override
//            public void onListChanged() {
//            }
//
//            @Override
//            public void onMove(int position, float x) {
//            }
//
//            @Override
//            public void onStartOpen(int position, int action, boolean right) {
//                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
//            }
//
//            @Override
//            public void onStartClose(int position, boolean right) {
//                Log.d("swipe", String.format("onStartClose %d", position));
//            }
//
//            @Override
//            public void onClickFrontView(int position) {
//                Log.d("swipe", String.format("onClickFrontView %d", position));
//            }
//
//            @Override
//            public void onClickBackView(int position) {
//                Log.d("swipe", String.format("onClickBackView %d", position));
//            }
//
//            @Override
//            public void onDismiss(int[] reverseSortedPositions) {
//                for (int position : reverseSortedPositions) {
//                    dataOrder.remove(position);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//        });

        swipeListView.setAdapter(adapter);

       // reload();


        lLayout = new GridLayoutManager(TargetAnalysisActivity.this, 2);


        //coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

//        List<Local_Data> contacts2 = dbvoc.getTarget();
//
//
//        if(contacts2.size() > 0) {
//            for (Local_Data cn : contacts2) {
//
//                //entries.add(new Entry(a+ 0.5f, Float.valueOf(cn.get_Target_Achieved())));
//                // entries1.add(new BarEntry(0, Float.valueOf(cn.get_Target_Text())));
//                //abc.add(cn.get_Target_Month());
//                abc.add("January");
//            }
//        }

        mChart = (CombinedChart) findViewById(R.id.combinechart);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
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

        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
            //dialog = new ProgressDialog(TargetAnalysisActivity.this,  R.style.AppTheme_Dark_Dialog);
            dialog = new ProgressDialog(TargetAnalysisActivity.this,  android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Please wait Target Loading....");
            dialog.setTitle("Metal App");
            dialog.setCancelable(false);
            dialog.show();
            //dialog.getWindow().setGravity(Gravity.CENTER);

//
//            dialog_profile_update = new ProgressDialog(EditProfile.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//            dialog_profile_update.setMessage("Please Wait....");
//            dialog_profile_update.setTitle("Intranet App");
//            dialog_profile_update.setCancelable(false);
//            dialog_profile_update.show();
//

            //dialog.show(this,"Metal App","Please wait Target Loading...", true,false);
            AllusersTargetResultu();
        }
        else
        {

//            Snackbar snackbar = Snackbar.make(coordinatorLayout, "You don't have internet connection.", Snackbar.LENGTH_LONG);
//            snackbar.show();
        }
//it will be executed whether exception is hadle or not
//        bottomNavigationView.setOnNavigationItemSelectedListener(
//                new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.product_p:
////                                List<Local_Data> contacts2 = dbvoc.getProductPERFORMANCEDATA();
////
////                                if(contacts2.size() > 0) {
//                                Intent pbc = new Intent(getApplicationContext(),Product_Performance.class);
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
//                                // finish();
//                                break;
//                            case R.id.customert_p:
//                                Intent cbc = new Intent(getApplicationContext(),Customer_Performance.class);
//                                cbc.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(cbc);
//                                //finish();
//
//                                break;
//
//                            case R.id.beat_p:
//                                Intent abc = new Intent(getApplicationContext(),Beat_Performance.class);
//                                abc.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(abc);
//                                // finish();
//
//                                break;
//                        }
//                        return true;
//                    }
//                });


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

        float groupSpace = 0.08f;
        float barSpace = 0.0f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1,set2);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        //Global_Data.Target_user_flag = "";

        return d;
    }

//    public void initNavigationDrawer() {
//        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//
//                int id = menuItem.getItemId();
//                isInternetPresent = cd.isConnectingToInternet();
//
//                switch (id){
//                    case R.id.csync_guide:
////                        new FinestWebView.Builder(MainActivity.this).titleDefault("Google Codelabs")
////                                .show("https://codelabs.developers.google.com/");
//
//                        if (isInternetPresent)
//                        {
//                            sendRequestnew(MainActivity.this);
//                        }
//                        else
//                        {
//                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
//                            snackbar.show();
//                        }
//
//                        drawerLayout.closeDrawers();
//                        break;
//
//                    case R.id.collection_status:
//
//                        if (isInternetPresent)
//                        {
//                            Intent cbc = new Intent(getApplicationContext(),Collection_MainList.class);
//                            startActivity(cbc);
//                            drawerLayout.closeDrawers();
//                        }
//                        else
//                        {
//                            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG);
//                            snackbar.show();
//                        }
//
//                        drawerLayout.closeDrawers();
//                        break;
//                    case R.id.about_guide:
//
//                        Intent cbc = new Intent(getApplicationContext(),Contact_Us.class);
//                        startActivity(cbc);
//
//                        drawerLayout.closeDrawers();
//                        break;
//
//                    case R.id.Ametal_lib:
//
//                        Intent ubc = new Intent(getApplicationContext(),About_Metal.class);
//                        startActivity(ubc);
//                        drawerLayout.closeDrawers();
//                        break;
//
//                    case R.id.logout:
//
//                        onBackPressed();
//
//                }
//                return true;
//            }
//        });
//        View header = navigationView.getHeaderView(0);
//        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
//        tv_email.setText("Metal");
//        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
//
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
//
//            @Override
//            public void onDrawerClosed(View v){
//                super.onDrawerClosed(v);
//            }
//
//            @Override
//            public void onDrawerOpened(View v) {
//                super.onDrawerOpened(v);
//            }
//        };
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//
//        MenuItem chartMenuItem = menu.findItem(R.id.action_action_Sync);
//        chartMenuItem.setVisible(false);
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_action_Sync) {
//
//            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Check updates.", Snackbar.LENGTH_LONG);
//            snackbar.show();
//
//            return true;
//        }
//        else
//        if (id == R.id.action_action_Filter) {
//
//            Global_Data.Target_user_flag = "true";
//            Intent i=new Intent(getApplicationContext(), User_Search_Main.class);
//
//            startActivity(i);
//
//            return true;
//        }

//        return super.onOptionsItemSelected(item);
//    }


//    @Override
//    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();

//        AlertDialog alertDialog = new AlertDialog.Builder(TargetAnalysisActivity.this,
//                R.style.AppTheme_Dark_Dialog).create(); //Read Update
//        alertDialog.setTitle("Confirmation");
//        alertDialog.setMessage(" Are you sure you want to logout?");
//        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes",new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO Auto-generated method stub
//
//                Intent i=new Intent(getApplicationContext(), LoginActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
//                finishAffinity();
//                finish();
//            }
//        });
//
//        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No",new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO Auto-generated method stub
//                dialog.cancel();
//            }
//        });
//
//        alertDialog.setCancelable(false);
//        alertDialog.show();
    //}


//    private void reload() {
//        SettingsManager settings = SettingsManager.getInstance();
//        swipeListView.setSwipeMode(settings.getSwipeMode());
//        swipeListView.setSwipeActionLeft(settings.getSwipeActionLeft());
//        swipeListView.setSwipeActionRight(settings.getSwipeActionRight());
//        swipeListView.setOffsetLeft(convertDpToPixel(settings.getSwipeOffsetLeft()));
//        swipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
//        swipeListView.setAnimationTime(settings.getSwipeAnimationTime());
//        swipeListView.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
//    }

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
        //super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        this.finish();
    }

    public void AllusersTargetResultu()
    {
        String domain = getResources().getString(R.string.service_domain);
        //String domain = "http://150.242.140.105:8005/metal/api/performance/v1/";

        SharedPreferences spf = TargetAnalysisActivity.this.getSharedPreferences("SimpleLogic", 0);
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
            service_url = domain + "users/user_target_performance?imei_no=" + Global_Data.imei_no + "&email=" + user_email;
        //}

        Log.i("volley", "service_url: " + service_url);

        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;

                new TargetAnalysisActivity.GetAllUserTargetListn().execute(response);

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

                    TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
//                            Toast toast = Toast.makeText(TargetAnalysisActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Global_Data.Custom_Toast(TargetAnalysisActivity.this, response_result,"yes");
//                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    });


                }
                else
                if(response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(TargetAnalysisActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(TargetAnalysisActivity.this, response_result,"yes");

//                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    });



                }
                else
                if(response_result.equalsIgnoreCase("Device not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                        public void run() {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(TargetAnalysisActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
//
                            Global_Data.Custom_Toast(TargetAnalysisActivity.this, response_result,"yes");

                            Intent intent = new Intent(TargetAnalysisActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });



                }
                else {

                    //dbvoc.getDeleteTable("delivery_products");

                    JSONArray result = response.getJSONArray("result");
                    Log.i("volley", "response orders Length: " + result.length());
                    Log.d("volley", "orders" + result.toString());


                    //
                    if (result.length() <= 0) {

                        TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                                //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(TargetAnalysisActivity.this, " Users Targets doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

                                Global_Data.Custom_Toast(TargetAnalysisActivity.this, " Users Targets doesn't exist", "yes");

//
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
                                All_User_Name.add(jsonObject.getString("month"));
                            //}


                        }

//                        Global_Data.Selected_USER_EMAIL = "";
//                        Global_Data.Globel_fromDate = "";
//                        Global_Data.Globel_toDate = "";



                        TargetAnalysisActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {



                                dialog.dismiss();
                            }
                        });



                    }

                    TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
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

                TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });
            }

            TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //dialog.dismiss();

                    if(All_User_Name.size() > 0) {

                        XAxis xAxis = mChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                        xAxis.setAxisMinimum(0f);
                        xAxis.setGranularity(1f);
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

    public void TargetOrderResult()
    {
        String domain = getResources().getString(R.string.service_domain);

        Log.i("volley", "domain: " + domain);

        StringRequest jsObjRequest = null;
        String service_url = "";

        SharedPreferences spf = TargetAnalysisActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        //service_url = domain + "beats/beats_performance?imei_no=" + Global_Data.CityName + "&email=" + Global_Data.CityName;
//        service_url = domain + "primary_category/"+item_cat+"/show_item_list";

        service_url = domain + "users/order_list?email=" + user_email + "&imei_no=" + Global_Data.imei_no;


        Log.i("volley", "service_url: " + service_url);

        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response1 = response;

                new TargetOrderTask().execute(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Intent intent = new Intent(TargetAnalysisActivity.this, MainActivity.class);
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
                          //  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(),"");
                        }
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

    private class TargetOrderTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {

            try {
                JSONObject response = new JSONObject(final_response1);
                if (response.has("message")) {
                    response_result1 = response.getString("message");
                } else {
                    response_result1 = "data";
                }

                if (response_result1.equalsIgnoreCase("No order found")) {

                    //Toast.makeText(Order.this, response_result, Toast.LENGTH_LONG).show();

                    TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            //adapter.notifyDataSetChanged();
                            //recyclerView.hideShimmerAdapter();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();
                            //dialog.dismiss();
//                            Toast toast = Toast.makeText(TargetAnalysisActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Global_Data.Custom_Toast(TargetAnalysisActivity.this, response_result,"yes");

//                            Intent intent = new Intent(AnchorFanActivity.this, MainActivity.class);
//                            startActivity(intent);
                            finish();
                        }
                    });
                }
                else
                if(response_result1.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            //adapter.notifyDataSetChanged();
                            //recyclerView.hideShimmerAdapter();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();
                            //dialog.dismiss();
                            Toast toast = Toast.makeText(TargetAnalysisActivity.this, response_result, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(TargetAnalysisActivity.this, response_result, "yes");

                            Intent intent = new Intent(TargetAnalysisActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
                else {

                    JSONArray regions = response.getJSONArray("order_products");
                    Log.i("volley", "response regions Length: " + regions.length());
                    Log.d("volley", "regions" + regions.toString());

                    if (regions.length() <= 0) {

                        TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                //adapter.notifyDataSetChanged();
                                //recyclerView.hideShimmerAdapter();
//                                Toast toast = Toast.makeText(TargetAnalysisActivity.this, "Record not exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(TargetAnalysisActivity.this, "Record not exist","yes");
                                //dialog.dismiss();
                                Intent intent = new Intent(TargetAnalysisActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {

                        //list_Offices.clear();
                        SwipeList.clear();
                        for (int i = 0; i < regions.length(); i++)
                        {

                            JSONObject jsonObject = regions.getJSONObject(i);

                            HashMap<String, String> mapp = new HashMap<String, String>();
                            mapp.put(TAG_ORDERID, jsonObject.getString("order_number"));
                            mapp.put(TAG_PRODUCTNM, jsonObject.getString("product_name"));
                            mapp.put(TAG_TOTALQTY, jsonObject.getString("total_qty"));
                            mapp.put(TAG_RETAILPRICE, jsonObject.getString("retail_price"));
                            mapp.put(TAG_MRP, jsonObject.getString("mrp"));
                            mapp.put(TAG_AMOUNT, jsonObject.getString("amount"));
                            mapp.put(TAG_CUSTSHOPNAME, jsonObject.getString("customer_shop_name"));

            //txttotalPreview.setText("Total		:		"+cnt1.getAmount());
            //Toast.makeText(NewOrderActivity.this, "Login:"+Global_Data.order_id,Toast.LENGTH_SHORT).show();
                               SwipeList.add(mapp);

                            //SwipeList.add(new TargetOrderListModel(jsonObject.getString("order_number"), jsonObject.getString("product_name"), jsonObject.getString("total_qty"), jsonObject.getString("retail_price"), jsonObject.getString("mrp"), jsonObject.getString("amount"), jsonObject.getString("customer_shop_name")));
                            //list_Offices.add(jsonObject.getString("name"));

                        }
//                        office_list.add(new office_List_Model("More", "", ""));
//                        list_Offices.add("More");

                        TargetAnalysisActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {

                               // adapter = new AdapterTargetAnalysisOrderList(TargetAnalysisActivity.this, SwipeList);
                                //dialog.dismiss();
//                                recyclerView.hideShimmerAdapter();
//                                office_list_adapter.notifyDataSetChanged();
//
//                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Office_List.this,
//                                        android.R.layout.simple_spinner_dropdown_item,list_Offices);
//                                autoCompleteTextView.setThreshold(1);// will start working from
//// first character
//                                autoCompleteTextView.setAdapter(adapter);// setting the adapter
//// data into the
//// AutoCompleteTextView
//                                autoCompleteTextView.setTextColor(Color.BLACK);
                            }
                        });
                    }

                    TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            //dialog.dismiss();
                            //recyclerView.hideShimmerAdapter();
                        }
                    });
                    //	dialog.dismiss();
                    //finish();
                }
                // }

                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();
                //dialog.dismiss();
                Intent intent = new Intent(TargetAnalysisActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        //adapter.notifyDataSetChanged();
                        //recyclerView.hideShimmerAdapter();
                    }
                });
            }

            TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //dialog.dismiss();
                    //adapter.notifyDataSetChanged();
                   // recyclerView.hideShimmerAdapter();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            TargetAnalysisActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //dialog.dismiss();
                    //adapter.notifyDataSetChanged();
                    //recyclerView.hideShimmerAdapter();
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
}
