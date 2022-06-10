
package com.msimplelogic.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.msimplelogic.activities.kotlinFiles.About_Metal;
import com.msimplelogic.activities.kotlinFiles.ActivityPlanner;
import com.msimplelogic.activities.kotlinFiles.ActivitySummary;
import com.msimplelogic.activities.kotlinFiles.ActivityTarget;
import com.msimplelogic.activities.kotlinFiles.AddCustomer;
import com.msimplelogic.activities.kotlinFiles.AllOrders_Sync;
import com.msimplelogic.activities.kotlinFiles.BeatSelection;
import com.msimplelogic.activities.kotlinFiles.ChangePasswordActivity;
import com.msimplelogic.activities.kotlinFiles.ChatBActivity;
import com.msimplelogic.activities.kotlinFiles.Contact_Us;
import com.msimplelogic.activities.kotlinFiles.Day_sheduler;
import com.msimplelogic.activities.kotlinFiles.Invoice_order;
import com.msimplelogic.activities.kotlinFiles.Marketing;
import com.msimplelogic.activities.kotlinFiles.Myprofile;
import com.msimplelogic.activities.kotlinFiles.SettingsActivity;
import com.msimplelogic.activities.kotlinFiles.Smart_Order;
import com.msimplelogic.activities.kotlinFiles.Visitlog;
import com.msimplelogic.activities.kotlinFiles.kotGlobal.QuickOrder;
import com.msimplelogic.animation.ActivitySwitcher;
import com.msimplelogic.helper.ThemeUtil;
import com.msimplelogic.imageadapters.Image;
import com.msimplelogic.service.LocationServices;
import com.msimplelogic.services.BJobService;
import com.msimplelogic.services.TestJobServiceOrder;
import com.msimplelogic.slidingmenu.adapter.NavDrawerListAdapter;
import com.msimplelogic.slidingmenu.model.NavDrawerItem;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static com.msimplelogic.activities.R.drawable.dark_theme_background;
import static com.msimplelogic.activities.kotlinFiles.SettingsActivityKt.KEY_CURRENT_THEME;
import static com.msimplelogic.activities.kotlinFiles.SettingsActivityKt.LILAC_THEME;
import static com.msimplelogic.activities.kotlinFiles.SettingsActivityKt.MAINTHEME;
import static com.msimplelogic.helper.ThemeUtil.THEME_DARK;


public class MainActivity extends BaseActivity {
    public static int mTheme = THEME_DARK;
    public static boolean mIsNightMode = false;
    String currentTheme = "";
    private ArrayList<String> Language_List = new ArrayList<String>();
    ArrayAdapter<String> LanguageAdapter;
    DrawerLayout drawer;
    NavigationView navigationView;
    ImageView distributorvisit;
    FrameLayout frameLayout;
    ActionBarDrawerToggle toggle;
    ImageView imageView;
    Toolbar toolbar;
    LinearLayout Addcust;
    View header;
    int count = 0;
    static String final_response = "";
    private Uri picUri;
    String imagePath = "";
    Uri file;
    final int PIC_CROP = 3;
    String device_id;
    double lat, lon;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    LoginDataBaseAdapter loginDataBaseAdapter;
    ProgressDialog dialog;
    String response_result = "";
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    TextView screenname;
    boolean firstLaunch;
    private String pictureImagePath = "";
    private String pictureImagePath_new = "";
    Boolean B_flag;
    View v;
    TextView screen_title;
    TextView todaysTarget;
    int fragmentPoistion;
    PlayService_Location PlayServiceManager;
    private RequestQueue requestQueue;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Calendar calendarn;
    private float t_total = 0;
    private float achived_total = 0;
    private int year, month, day;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private FirebaseAnalytics mFirebaseAnalytics;
    SharedPreferences pic;
    SharedPreferences.Editor editor;
    CardView Visit_retailer_card, Visit_distributor_card, day_sceduler_card, target_card, calender_card, expenses_card, pricing_card, marketing_data_card,Visit_log_card;
    private CircleImageView img_header;
    RelativeLayout ll;
    LinearLayout linearMian;
    private TextView tv_name, tv_number, tv_change, textViewVersion, textView1, tv_designation;
    ImageView hedder_theame, order, calendar, custom_serve, expenses, target, schedule, logout, pricing, add_retailernew, marketing_data, syncmdata, myActivityBtn, moreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.activity_menu);
        setTitle("");
        firstLaunch = false;
        screenname = (TextView) findViewById(R.id.screenname);

        cd = new ConnectionDetector(getApplicationContext());

        Global_Data.selected= "";
        Global_Data.catalogue_m.clear();
        Global_Data.quikeditdel = "";
        Global_Data.Qorder_item_list.clear();
        Global_Data.spiner_list_modelList.clear();
        Global_Data.array_of_pVarient.clear();
        Global_Data.q = "";
        Global_Data.ExpenseName = "";
        Global_Data.p_code.clear();
        Global_Data.p_mrp.clear();
        Global_Data.p_amount.clear();
        Global_Data.p_qty.clear();
        Global_Data.quat = "";
        Global_Data.quickorder_maponback = "";


        dialog = new ProgressDialog(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        SharedPreferences sp = MainActivity.this.getSharedPreferences("SimpleLogic", 0);
        // for label RP change
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        Global_Data.rsstr = spf1.getString("var_rs", "");

        /* Theme Code */
        //  currentTheme = sp.getString(KEY_CURRENT_THEME, LILAC_THEME);
        setAppTheme(currentTheme);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        alaramM();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);

//        toolbar.setNavigationIcon(R.drawable.ic_bell);
//        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);
//        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_bell));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Visit_distributor_card = (CardView) findViewById(R.id.Visit_distributor_card);
        day_sceduler_card = (CardView) findViewById(R.id.day_sceduler_card);
        target_card = (CardView) findViewById(R.id.target_card);
        calender_card = (CardView) findViewById(R.id.calender_card);
        expenses_card = (CardView) findViewById(R.id.expenses_card);
        marketing_data_card = (CardView) findViewById(R.id.marketing_data_card);
        Visit_log_card = (CardView) findViewById(R.id.Visit_log_card);
        pricing_card = (CardView) findViewById(R.id.pricing_card);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        Visit_retailer_card = findViewById(R.id.Visit_retailer_card);
        order = (ImageView) findViewById(R.id.order);
        hedder_theame = (ImageView) findViewById(R.id.hedder_theame);
        ll = (RelativeLayout) findViewById(R.id.mainlayout);
        linearMian = (LinearLayout) findViewById(R.id.linearMian);
        //   ll.setBackgroundResource(R.drawable.dark_theme_background);
        calendar = (ImageView) findViewById(R.id.calendar);
        //custom_serve=(ImageView)rootView.findViewById(R.id.custom_serve);
        expenses = (ImageView) findViewById(R.id.expenses);
        target = (ImageView) findViewById(R.id.target);
        pricing = (ImageView) findViewById(R.id.pricing);
        add_retailernew = (ImageView) findViewById(R.id.add_retailernew);
        marketing_data = (ImageView) findViewById(R.id.marketing_data);
        syncmdata = (ImageView) findViewById(R.id.syncmdata);
        myActivityBtn = (ImageView) findViewById(R.id.img_myact);
        moreBtn = (ImageView) findViewById(R.id.img_more);
        Addcust = findViewById(R.id.Addcust);
        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme", 0);

        if (current_theme == 1) {
            order.setImageResource(R.drawable.visitretailer_home_dark);
            syncmdata.setImageResource(R.drawable.visitdistributor_home_dark);
            add_retailernew.setImageResource(R.drawable.dayscheduler_home_dark);
            target.setImageResource(R.drawable.target_home_dark);
            calendar.setImageResource(R.drawable.activityplanner_home_dark);
            expenses.setImageResource(R.drawable.expenses_home_dark);
            pricing.setImageResource(R.drawable.pricestock_home_dark);
            marketing_data.setImageResource(R.drawable.marketing_home_dark);
            hedder_theame.setImageResource(R.drawable.dark_hedder);
            // linearMian.setBackgroundColor(Color.BLACK);


//ll.setBackgroundResource(R.drawable.dark_theme_background);
            //      final int sdk = android.os.Build.VERSION.SDK_INT; if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) { ll.setBackgroundResource(R.drawable.dark_theme_background); } else { ll.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.dark_theme_background)); }
        }


        Addcust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCustomer.class);
                startActivity(intent);
            }
        });

        Visit_log_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Visitlog.class);
                startActivity(intent);
            }
        });


        header = navigationView.getHeaderView(0);
//        imageView = (ImageView) header.findViewById(R.id.imageView);
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawer.closeDrawer(GravityCompat.START);
//            }
//        });

        toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //toggle.setDrawerIndicatorEnabled(true);
        //  toggle.setHomeAsUpIndicator(R.drawable.ic_home);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


//        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//        	todaysTarget.setText("Target/Acheived : Rs "+String.format(sp.getFloat("Target",0)+"/"+sp.getFloat("Achived", 0)));
//		}

        if (sp.getFloat("Target", 0) == 0.0) {
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                getTargetDatamain();
            }
        } else {
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
                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                } else {
                    int age = (int) Math.round(age_float);
                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    }
                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
                }

                //	todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        myActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, New_Feedback.class);ActivitySummary
//                startActivity(intent);
                Intent intent = new Intent(MainActivity.this, ActivitySummary.class);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.nav_gallery) {
                    //loadFragment(new galleryFragment());
                } else if (id == R.id.nav_dashboard) {
                    //dashboard
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                    finish();
                    //loadFragment(new Fragment());
                } else if (id == R.id.nav_attendance) {
                    //Attendence
                    Intent map = new Intent(MainActivity.this, BasicMapDemoActivity.class);
                    startActivity(map);
                    finish();

                } else if (id == R.id.quick_order) {
                    Global_Data.order_pushflag = "";
                    Global_Data.catalogue_m.clear();
                    Global_Data.Qorder_item_list.clear();
                    Global_Data.spiner_list_modelList.clear();
                    Global_Data.array_of_pVarient.clear();
                    Intent intent = new Intent(MainActivity.this, QuickOrder.class);
                    startActivity(intent);
                    // finish();

                } else if (id == R.id.Smart_order) {
                    Global_Data.order_pushflag = "";
                    Intent intent = new Intent(MainActivity.this, Smart_Order.class);
                    startActivity(intent);
                    // finish();

                } else if (id == R.id.nav_sync) {
                    Intent intent = new Intent(MainActivity.this, AllOrders_Sync.class);
                    startActivity(intent);
                    // finish();

                } else if (id == R.id.nav_setting) {


                    if (count == 0) {
                        Menu menu = navigationView.getMenu();
                        menu.findItem(R.id.nav_appurl).setVisible(false);
                        menu.findItem(R.id.nav_language).setVisible(true);
                        menu.findItem(R.id.nav_skin).setVisible(false);
                        menu.findItem(R.id.nav_aboutmetal).setVisible(true);
                        menu.findItem(R.id.nav_changepass).setVisible(true);


                        count = 1;
                        return false;
                    }
                    if (count == 1) {
                        Menu menu = navigationView.getMenu();
                        menu.findItem(R.id.nav_appurl).setVisible(false);
                        menu.findItem(R.id.nav_language).setVisible(false);
                        menu.findItem(R.id.nav_skin).setVisible(false);
                        menu.findItem(R.id.nav_aboutmetal).setVisible(false);
                        menu.findItem(R.id.nav_changepass).setVisible(false);

                        count = 0;
                        return false;
                    }
                    // id==R.id.nav_appurl
                } else if (id == R.id.nav_language) {
                    Language_Select_Dialog();
//                    Intent conaa1 = new Intent(MainActivity.this, Sound_Setting.class);
//                    startActivity(conaa1);
//                    finish();

                } else if (id == R.id.nav_aboutmetal) {
                    Intent cona = new Intent(MainActivity.this, About_Metal.class);
                    startActivity(cona);
                    finish();

                } else if (id == R.id.nav_changepass) {
                    Intent cona1 = new Intent(MainActivity.this, ChangePasswordActivity.class);
                    startActivity(cona1);
                    finish();
                } else if (id == R.id.nav_logout) {
                    onBackPressed();
                } else if (id == R.id.nav_appurl) {
//                    Intent cona = new Intent(MainActivity.this, Invoice_order.class);
//                    startActivity(cona);
//                    Intent cona = new Intent(MainActivity.this, Promotional_meetings.class);
//                    startActivity(cona);
                    //Toast.makeText(MainActivity.this, "App url", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_skin) {

                    //  setTheme(R.style.AppThemeDark);
                    Intent cona = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(cona);

                    //Toast.makeText(MainActivity.this, "Skin Clicked", Toast.LENGTH_SHORT).show();

                }
                Menu menu = navigationView.getMenu();
                menu.findItem(R.id.nav_appurl).setVisible(false);
                menu.findItem(R.id.nav_language).setVisible(false);
                menu.findItem(R.id.nav_skin).setVisible(false);
                menu.findItem(R.id.nav_aboutmetal).setVisible(false);
                menu.findItem(R.id.nav_changepass).setVisible(false);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        Global_Data.GLOVEL_LONG_DESC = "";
        Global_Data.GLOVEL_CATEGORY_SELECTION = "";
        Global_Data.Target_From_Month = "";
        Global_Data.Target_To_Month = "";
        Global_Data.Target_Product_Category = "";
        Global_Data.target_quarter = "";
        Global_Data.Target_Year = "";
        Global_Data.target_amount = "";
        Global_Data.target_grpby = "";
        Global_Data.G_BEAT_IDC = "";
        Global_Data.G_BEAT_VALUEC = "";
        Global_Data.G_BEAT_service_flag = "";
        Global_Data.G_RadioG_valueC = "";
        Global_Data.GlobeloPname = "";
        Global_Data.GlobeloPAmount = "";
        Global_Data.G_CBUSINESS_TYPE = "";

        String user_name = "";
        if (!Global_Data.USER_FIRST_NAME.equalsIgnoreCase("null")) {
            user_name = Global_Data.USER_FIRST_NAME.trim();
            if (!Global_Data.USER_LAST_NAME.equalsIgnoreCase("null")) {
                user_name += " " + Global_Data.USER_LAST_NAME.trim();
            }
        }

        Calendar c1 = Calendar.getInstance();
        //System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SSS");
        String formattedDate = df.format(c1.getTime());
        // Obtain the FirebaseAnalytics instance.
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Global_Data.GLOvel_USER_EMAIL);
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Global_Data.USER_FIRST_NAME + " " + Global_Data.USER_LAST_NAME);
//        bundle.putString(FirebaseAnalytics.Param.START_DATE, formattedDate);
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        /* Firebase Code End */

        loginDataBaseAdapter = new LoginDataBaseAdapter(MainActivity.this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        //scheduleNotify();
        SharedPreferences pref_devid = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);

        final String Device_id = pref_devid.getString("devid", "");
        Global_Data.device_id = Device_id;

        requestGPSPermission();

        SharedPreferences img = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);

        Global_Data.PREVIOUS_ORDER_BACK_FLAG = "";


        Visit_retailer_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Local_Data> contacts = dbvoc.checkCustomer();
                if (contacts.size() <= 0) {

//                    Toast toast = Toast.makeText(MainActivity.this,
//                            getResources().getString(R.string.customer_notfound_message), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(MainActivity.this,
                            getResources().getString(R.string.customer_notfound_message), "yes");

                } else {
                    Global_Data.CUSTOMER_SERVICE_FLAG = "";
                    Global_Data.sales_btnstring = "Secondary Sales / Retail Sales";
                    Intent intent = new Intent(getApplicationContext(), BeatSelection.class);
                    startActivity(intent);

//                    Global_Data.CUSTOMER_SERVICE_FLAG = "";
//                    Intent intent = new Intent(MainActivity.this, Sales_Dash.class);
//                    startActivity(intent);
                }
            }

        });
        calender_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, ActivityPlanner.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        expenses_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExpensesNewActivity.class);
                startActivity(intent);
//                Intent intent = new Intent(MainActivity.this, Expenses.class);
//                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        pricing_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Pricing_Main.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //getActivity().finish();
            }
        });

        day_sceduler_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Day_sheduler.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                //requestStoragePermissionLog();
            }
        });


        marketing_data_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Marketing.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //getActivity().finish();


            }
        });

        Visit_distributor_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent mIntent = getPackageManager().getLaunchIntentForPackage("com.simplelogic.stockmanagementsystem");
//                if (mIntent != null) {
//                    try {
//                        startActivity(mIntent);
//                        finish();
//                    } catch (ActivityNotFoundException err) {
//                        err.printStackTrace();
//                        Toast.makeText(MainActivity.this, "Please install bar", Toast.LENGTH_SHORT).show();
//                        try {
//                            final String appPackageName = "com.simplelogic.stockmanagementsystem"; // Can also use getPackageName(), as below
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                            finish();
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                            Toast.makeText(MainActivity.this, "App Not available in google play.", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                } else {
//                    try {
//                        final String appPackageName = "com.simplelogic.stockmanagementsystem"; // Can also use getPackageName(), as below
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                        finish();
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                        Toast.makeText(MainActivity.this, "App Not available in google play.", Toast.LENGTH_SHORT).show();
//                    }
//                }

                Global_Data.Custom_Toast(MainActivity.this, "Not available", "yes");
            }
        });

        target_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectingToInternet();
                Intent i = new Intent(MainActivity.this, ActivityTarget.class);
                //Intent i = new Intent(MainActivity.this, Target_REYC_Main.class);
                startActivity(i);
                finish();
            }
        });

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectingToInternet();
                SharedPreferences sp;
                sp = getSharedPreferences("SimpleLogic", 0);
                int current_theme = sp.getInt("CurrentTheme", 0);

                if (current_theme == 1) {
                    new BottomSheet.Builder(MainActivity.this, R.style.BottomSheet_StyleDialog_dark).sheet(R.menu.grid_sheet).listener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogn, int which) {
                            switch (which) {
                                case R.id.nearest_customer:
                                    Intent a = new Intent(getApplicationContext(), Nearest_Customer.class);
                                    startActivity(a);
                                    break;
                                case R.id.beat_on_map:
                                    requestGPSPermissionsignamap();
//                                Intent invoices_i =  new Intent(MainActivity.this, MapMultiUser.class);
//                                startActivity(invoices_i);
                                    break;
                                case R.id.order_list:
                                    List<Local_Data> contcustomer = dbvoc.getOrderAll("Secondary Sales / Retail Sales");

                                    if (contcustomer.size() <= 0) {

                                        Global_Data.Custom_Toast(MainActivity.this, getResources().getString(R.string.Order_Not_Found), "Yes");
                                    } else {
                                        Intent ord = new Intent(MainActivity.this, Previous_Order.class);
                                        startActivity(ord);
                                        finish();
                                    }
                                    break;
                                case R.id.schedule_list:
                                    isInternetPresent = cd.isConnectingToInternet();

                                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
                                    alertDialog.setTitle(MainActivity.this.getResources().getString(R.string.Scheduleo));
                                    alertDialog.setMessage(MainActivity.this.getResources().getString(R.string.schedule_offline_message));
                                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, MainActivity.this.getResources().getString(R.string.Online), new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog1, int which) {
                                            dialog1.cancel();

                                            if (isInternetPresent) {
                                                //getScheduleDataFORALLCUSTOMERS();

                                                //calendarn = Calendar.getInstance();
                                                //year = calendarn.get(Calendar.YEAR);
                                                loginDataBaseAdapter = new LoginDataBaseAdapter(MainActivity.this);
                                                loginDataBaseAdapter = loginDataBaseAdapter.open();
                                                dialog = new ProgressDialog(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                                dialog.setMessage(MainActivity.this.getResources().getString(R.string.Please_Wait));
                                                dialog.setTitle(MainActivity.this.getResources().getString(R.string.app_name));
                                                dialog.setCancelable(false);
                                                dialog.show();

                                                SharedPreferences sp = MainActivity.this.getSharedPreferences("SimpleLogic", 0);
                                                String Cust_domain = sp.getString("Cust_Service_Url", "");
                                                String service_url = Cust_domain + "metal/api/v1/";
                                                String device_id = sp.getString("devid", "");
                                                String domain = service_url;

                                                SharedPreferences spf = MainActivity.this.getSharedPreferences("SimpleLogic", 0);
                                                String user_email = spf.getString("USER_EMAIL", null);


                                                Log.i("volley", "domain: " + domain);
                                                Log.i("volley", "email: " + user_email);
                                                Log.i("target url", "target url " + domain + "delivery_schedules/send_all_schedules?email=" + user_email);

                                                StringRequest jsObjRequest = null;

//					 jsObjRequest = new StringRequest(domain+"delivery_schedules/send_all_schedules?imei_no="+"358187071078870"+"&email="+"vinod.raghuwanshi@simplelogic.in", new Response.Listener<String>() {


                                                jsObjRequest = new StringRequest(domain + "delivery_schedules/send_all_schedules?email=" + user_email, new Response.Listener<String>() {

                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.i("volley", "response: " + response);
                                                        final_response = response;

                                                        new scheduleoperation().execute(response);

                                                    }
                                                },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                dialog.dismiss();
                                                                //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                                                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                                                                Toast.makeText(MainActivity.this,
//                                                                        "Network Error",
//                                                                        Toast.LENGTH_LONG).show();
                                                                    Global_Data.Custom_Toast(MainActivity.this,
                                                                            "Network Error", "");
                                                                } else if (error instanceof AuthFailureError) {
//                                                                Toast.makeText(MainActivity.this,
//                                                                        "Server AuthFailureError  Error",
//                                                                        Toast.LENGTH_LONG).show();
                                                                    Global_Data.Custom_Toast(MainActivity.this,
                                                                            "Server AuthFailureError  Error", "");
                                                                } else if (error instanceof ServerError) {
//                                                                Toast.makeText(MainActivity.this,
//                                                                        MainActivity.this.getResources().getString(R.string.Server_Errors),
//                                                                        Toast.LENGTH_LONG).show();
                                                                    Global_Data.Custom_Toast(MainActivity.this,
                                                                            MainActivity.this.getResources().getString(R.string.Server_Errors), "");
                                                                } else if (error instanceof NetworkError) {
//                                                                Toast.makeText(MainActivity.this,
//                                                                        "Network   Error",
//                                                                        Toast.LENGTH_LONG).show();
                                                                    Global_Data.Custom_Toast(MainActivity.this,
                                                                            "Network   Error", "");
                                                                } else if (error instanceof ParseError) {
//                                                                Toast.makeText(MainActivity.this,
//                                                                        "ParseError   Error",
//                                                                        Toast.LENGTH_LONG).show();
                                                                    Global_Data.Custom_Toast(MainActivity.this,
                                                                            "ParseError   Error", "");
                                                                } else {
                                                                    //Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                                                    Global_Data.Custom_Toast(MainActivity.this, error.getMessage(), "");
                                                                }
                                                                dialog.dismiss();
                                                                // finish();
                                                            }
                                                        });

                                                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                                                int socketTimeout = 300000;//30 seconds - change to what you want
                                                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                                jsObjRequest.setRetryPolicy(policy);
                                                // requestQueue.se
                                                //requestQueue.add(jsObjRequest);
                                                jsObjRequest.setShouldCache(false);
                                                requestQueue.getCache().clear();
                                                requestQueue.add(jsObjRequest);

                                            } else {

                                                //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//                                            Toast toast = Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                                            toast.setGravity(Gravity.CENTER, 0, 0);
//                                            toast.show();
                                                Global_Data.Custom_Toast(MainActivity.this, MainActivity.this.getResources().getString(R.string.internet_connection_error), "yes");
                                            }
                                        }
                                    });

                                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, MainActivity.this.getResources().getString(R.string.Offline), new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog1, int which) {

                                            List<Local_Data> contacts3 = dbvoc.getSchedule_ListAll();

                                            if (contacts3.size() <= 0) {
                                                //Toast.makeText(Order.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

//                                            Toast toast = Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.Sorry_No_Record_Found), Toast.LENGTH_LONG);
//                                            toast.setGravity(Gravity.CENTER, 0, 0);
//                                            toast.show();
                                                Global_Data.Custom_Toast(MainActivity.this, MainActivity.this.getResources().getString(R.string.Sorry_No_Record_Found), "yes");

                                            } else {
                                                Global_Data.Schedule_FLAG = "ALLCUSTOMER";
                                                Intent intent = new Intent(MainActivity.this,
                                                        Schedule_List.class);
                                                MainActivity.this.startActivity(intent);
                                                //itemView.getContext().finish();
                                                //itemView.getContext().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            }

                                            dialog1.cancel();

                                        }
                                    });
                                    alertDialog.show();
                                    break;

                                case R.id.cust_outstanding:
                                    Intent invoices_iiii = new Intent(MainActivity.this, Customer_Outstanding.class);
                                    startActivity(invoices_iiii);
                                    break;
                                case R.id.cust_Chat:
                                    isInternetPresent = cd.isConnectingToInternet();
                                    if (isInternetPresent) {
                                        Intent chat = new Intent(MainActivity.this, ChatBActivity.class);
                                        startActivity(chat);
                                    } else {

                                        //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//                                    Toast toast = Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                        Global_Data.Custom_Toast(MainActivity.this, MainActivity.this.getResources().getString(R.string.internet_connection_error), "yes");
                                    }
                                    break;


                            }

                        }
                    }).grid().show();

                } else {
                    new BottomSheet.Builder(MainActivity.this, R.style.BottomSheet_StyleDialog).sheet(R.menu.grid_sheet).listener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogn, int which) {
                            switch (which) {
                                case R.id.nearest_customer:
                                    Intent a = new Intent(getApplicationContext(), Nearest_Customer.class);
                                    startActivity(a);
                                    break;
                                case R.id.beat_on_map:
                                    requestGPSPermissionsignamap();
//                                Intent invoices_i =  new Intent(MainActivity.this, MapMultiUser.class);
//                                startActivity(invoices_i);
                                    break;
                                case R.id.order_list:
                                    List<Local_Data> contcustomer = dbvoc.getOrderAll("Secondary Sales / Retail Sales");

                                    if (contcustomer.size() <= 0) {

                                        Global_Data.Custom_Toast(MainActivity.this, getResources().getString(R.string.Order_Not_Found), "Yes");
                                    } else {
                                        Intent ord = new Intent(MainActivity.this, Previous_Order.class);
                                        startActivity(ord);
                                        finish();
                                    }
                                    break;
                                case R.id.schedule_list:
                                    isInternetPresent = cd.isConnectingToInternet();

                                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
                                    alertDialog.setTitle(MainActivity.this.getResources().getString(R.string.Scheduleo));
                                    alertDialog.setMessage(MainActivity.this.getResources().getString(R.string.schedule_offline_message));
                                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, MainActivity.this.getResources().getString(R.string.Online), new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog1, int which) {
                                            dialog1.cancel();

                                            if (isInternetPresent) {
                                                //getScheduleDataFORALLCUSTOMERS();

                                                //calendarn = Calendar.getInstance();
                                                //year = calendarn.get(Calendar.YEAR);
                                                loginDataBaseAdapter = new LoginDataBaseAdapter(MainActivity.this);
                                                loginDataBaseAdapter = loginDataBaseAdapter.open();
                                                dialog = new ProgressDialog(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                                dialog.setMessage(MainActivity.this.getResources().getString(R.string.Please_Wait));
                                                dialog.setTitle(MainActivity.this.getResources().getString(R.string.app_name));
                                                dialog.setCancelable(false);
                                                dialog.show();

                                                SharedPreferences sp = MainActivity.this.getSharedPreferences("SimpleLogic", 0);
                                                String Cust_domain = sp.getString("Cust_Service_Url", "");
                                                String service_url = Cust_domain + "metal/api/v1/";
                                                String device_id = sp.getString("devid", "");
                                                String domain = service_url;

                                                SharedPreferences spf = MainActivity.this.getSharedPreferences("SimpleLogic", 0);
                                                String user_email = spf.getString("USER_EMAIL", null);


                                                Log.i("volley", "domain: " + domain);
                                                Log.i("volley", "email: " + user_email);
                                                Log.i("target url", "target url " + domain + "delivery_schedules/send_all_schedules?email=" + user_email);

                                                StringRequest jsObjRequest = null;

//					 jsObjRequest = new StringRequest(domain+"delivery_schedules/send_all_schedules?imei_no="+"358187071078870"+"&email="+"vinod.raghuwanshi@simplelogic.in", new Response.Listener<String>() {


                                                jsObjRequest = new StringRequest(domain + "delivery_schedules/send_all_schedules?email=" + user_email, new Response.Listener<String>() {

                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.i("volley", "response: " + response);
                                                        final_response = response;

                                                        new scheduleoperation().execute(response);

                                                    }
                                                },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                dialog.dismiss();
                                                                //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                                                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                                                                Toast.makeText(MainActivity.this,
//                                                                        "Network Error",
//                                                                        Toast.LENGTH_LONG).show();
                                                                    Global_Data.Custom_Toast(MainActivity.this,
                                                                            "Network Error", "");
                                                                } else if (error instanceof AuthFailureError) {
//                                                                Toast.makeText(MainActivity.this,
//                                                                        "Server AuthFailureError  Error",
//                                                                        Toast.LENGTH_LONG).show();
                                                                    Global_Data.Custom_Toast(MainActivity.this,
                                                                            "Server AuthFailureError  Error", "");
                                                                } else if (error instanceof ServerError) {
//                                                                Toast.makeText(MainActivity.this,
//                                                                        MainActivity.this.getResources().getString(R.string.Server_Errors),
//                                                                        Toast.LENGTH_LONG).show();
                                                                    Global_Data.Custom_Toast(MainActivity.this,
                                                                            MainActivity.this.getResources().getString(R.string.Server_Errors), "");
                                                                } else if (error instanceof NetworkError) {
//                                                                Toast.makeText(MainActivity.this,
//                                                                        "Network   Error",
//                                                                        Toast.LENGTH_LONG).show();
                                                                    Global_Data.Custom_Toast(MainActivity.this,
                                                                            "Network   Error", "");
                                                                } else if (error instanceof ParseError) {
//                                                                Toast.makeText(MainActivity.this,
//                                                                        "ParseError   Error",
//                                                                        Toast.LENGTH_LONG).show();
                                                                    Global_Data.Custom_Toast(MainActivity.this,
                                                                            "ParseError   Error", "");
                                                                } else {
                                                                    //Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                                                    Global_Data.Custom_Toast(MainActivity.this, error.getMessage(), "");
                                                                }
                                                                dialog.dismiss();
                                                                // finish();
                                                            }
                                                        });

                                                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                                                int socketTimeout = 300000;//30 seconds - change to what you want
                                                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                                jsObjRequest.setRetryPolicy(policy);
                                                // requestQueue.se
                                                //requestQueue.add(jsObjRequest);
                                                jsObjRequest.setShouldCache(false);
                                                requestQueue.getCache().clear();
                                                requestQueue.add(jsObjRequest);

                                            } else {

                                                //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//                                            Toast toast = Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                                            toast.setGravity(Gravity.CENTER, 0, 0);
//                                            toast.show();
                                                Global_Data.Custom_Toast(MainActivity.this, MainActivity.this.getResources().getString(R.string.internet_connection_error), "yes");
                                            }
                                        }
                                    });

                                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, MainActivity.this.getResources().getString(R.string.Offline), new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog1, int which) {

                                            List<Local_Data> contacts3 = dbvoc.getSchedule_ListAll();

                                            if (contacts3.size() <= 0) {
                                                //Toast.makeText(Order.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

//                                            Toast toast = Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.Sorry_No_Record_Found), Toast.LENGTH_LONG);
//                                            toast.setGravity(Gravity.CENTER, 0, 0);
//                                            toast.show();
                                                Global_Data.Custom_Toast(MainActivity.this, MainActivity.this.getResources().getString(R.string.Sorry_No_Record_Found), "yes");

                                            } else {
                                                Global_Data.Schedule_FLAG = "ALLCUSTOMER";
                                                Intent intent = new Intent(MainActivity.this,
                                                        Schedule_List.class);
                                                MainActivity.this.startActivity(intent);
                                                //itemView.getContext().finish();
                                                //itemView.getContext().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            }

                                            dialog1.cancel();

                                        }
                                    });
                                    alertDialog.show();
                                    break;

                                case R.id.cust_outstanding:
                                    Intent invoices_iiii = new Intent(MainActivity.this, Customer_Outstanding.class);
                                    startActivity(invoices_iiii);
                                    break;
                                case R.id.cust_Chat:
                                    isInternetPresent = cd.isConnectingToInternet();
                                    if (isInternetPresent) {
                                        Intent chat = new Intent(MainActivity.this, ChatBActivity.class);
                                        startActivity(chat);
                                    } else {

                                        //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//                                    Toast toast = Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                        Global_Data.Custom_Toast(MainActivity.this, MainActivity.this.getResources().getString(R.string.internet_connection_error), "yes");
                                    }
                                    break;


                            }

                        }
                    }).grid().show();

                }


//                Intent i = new Intent(MainActivity.this, MenuOptimization.class);
//                startActivity(i);
//                finish();

//                Intent i = new Intent(MainActivity.this, Nearest_Customer.class);
//                startActivity(i);
//                finish();
            }
        });

//        mTitle = mDrawerTitle = getTitle();
//        v = getActionBar().getCustomView();
//        screen_title = (TextView) v.findViewById(R.id.screenname);
//        // load slide menu items
//        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
//
//        // nav drawer icons from resources
////		navMenuIcons = getResources()
////				.obtainTypedArray(R.array.nav_drawer_icons);
//
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
//        LayoutInflater inflater = getLayoutInflater();
//        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header, null, false);
//        mDrawerList.addHeaderView(header);
//
//        LayoutInflater inflaterf = getLayoutInflater();
//        ViewGroup footer = (ViewGroup) inflaterf.inflate(R.layout.footer, null, false);
//        mDrawerList.addFooterView(footer);
//
//        textView1 = footer.findViewById(R.id.textView1);
//        textViewVersion = footer.findViewById(R.id.textViewVersion);
//        img_header = header.findViewById(R.id.img_header);
//        img_header.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, Myprofile.class);
//                startActivity(intent);
//            }
//        });

        img_header = header.findViewById(R.id.img_header);
        img_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Myprofile.class);
                startActivity(intent);
            }
        });
        tv_name = header.findViewById(R.id.tv_name);
        tv_designation = header.findViewById(R.id.tv_designation);
        tv_name.setText(user_name);

        SharedPreferences myPrefs1 = getSharedPreferences("SimpleLogic", MODE_PRIVATE);
        String designation = myPrefs1.getString("designation", "");

        if (designation.length() > 0) {
            tv_designation.setText(designation);
        } else {
            tv_designation.setVisibility(View.INVISIBLE);
        }

//        tv_number = header.findViewById(R.id.tv_number);
//        tv_change = header.findViewById(R.id.tv_change);
//
        SharedPreferences myPrefs = getSharedPreferences("SimpleLogic", MODE_PRIVATE);
        String ipAdrs = myPrefs.getString("img_path", "");

//        if(file != null)
//        {
        Glide.with(getApplicationContext())
                .load(ipAdrs)
                .thumbnail(0.5f)
                //.centerCrop()
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_header);
        //}

//        tv_name = header.findViewById(R.id.tv_name);
//        tv_number = header.findViewById(R.id.tv_number);
//        tv_change = header.findViewById(R.id.tv_change);
//
//        SharedPreferences myPrefs = getSharedPreferences("SimpleLogic", MODE_PRIVATE);
//        String ipAdrs=myPrefs.getString("img_path", "");
//
////        if(file != null)
////        {
//            Glide.with(getApplicationContext())
//                    .load(ipAdrs)
//                    .thumbnail(0.5f)
//                    //.centerCrop()
//                    .placeholder(R.drawable.img_not_found)
//                    .error(R.drawable.img_not_found)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(img_header);
//        //}


        //   navDrawerItems.add(new NavDrawerItem(navMenuTitles[6]));
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7]));
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8]));
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[9]));
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[10]));
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[11]));
        //	navDrawerItems.add(new NavDrawerItem(navMenuTitles[12]));
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6]));
//
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7]));
//
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8]));
//
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[9]));
//
//		navDrawerItems.add(new NavDrawerItem(navMenuTitles[10]));

        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));

		/*navDrawerItems.add(new NavDrawerItem(navMenuTitles[5]));

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6]));*/

        // What's hot, We  will add a counter here

        // Recycle the typed array
        //navMenuIcons.recycle();

        //   mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

//        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//              clickonitam(i);
//
//            }
//        });

        // setting the nav drawer list adapter
//        adapter = new NavDrawerListAdapter(getApplicationContext(),
//                navDrawerItems);
//        mDrawerList.setAdapter(adapter);
//
//        // enabling action bar app icon and behaving it as toggle button
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);
//
//        if (Global_Data.SYNC_SERVICE_FLAG.equalsIgnoreCase("TRUE")) {
//            Global_Data.SYNC_SERVICE_FLAG = "";
//            getServices.sendRequest(MainActivity.this);
//        }
//
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//                R.drawable.ic_drawer, //nav menu toggle icon
//                R.string.app_name, // nav drawer open - description for accessibility
//                R.string.app_name // nav drawer close - description for accessibility
//        ) {
//            public void onDrawerClosed(View view) {
//                //getActionBar().setTitle(mTitle);
//                // calling onPrepareOptionsMenu() to show action bar icons
//
//                screen_title.setText(mTitle);
//                invalidateOptionsMenu();
//            }
//
//            public void onDrawerOpened(View drawerView) {
//                //getActionBar().setTitle(mDrawerTitle);
//                // calling onPrepareOptionsMenu() to hide action bar icons
//
//                screen_title.setText(getResources().getString(R.string.app_name));
//                invalidateOptionsMenu();
//            }
//        };
//
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//
//        if (savedInstanceState == null) {
//            // on first time display view for first nav item
//            displayView(1);
//        }
    }

//    private void clickonitam(int i) {
//        Fragment fragment = null;
//        if (i==1){
//
//            fragment = new Home();
//            fragmentPoistion = 0;
//        }else if(i==2){
//            Intent conaa = new Intent(MainActivity.this, Sound_Setting.class);
//            startActivity(conaa);
//            fragment = new Home();
//
//        }else if(i==3){
//            Intent con = new Intent(MainActivity.this, Contact_Us.class);
//            startActivity(con);
//
//            fragment = new Home();
//            fragmentPoistion = 2;
//
//
//        }else if(i==4){
//            Intent cona = new Intent(MainActivity.this, About_Metal.class);
//            startActivity(cona);
//            fragment = new Home();
//
//        }else if(i==5){
//            fragment = new Home();
//
//        }else if(i==6){
//
//            onBackPressed();
//            fragment = new Home();
//
//        }
//
//        if (fragment != null) {
//            FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.frame_container, fragment).commit();
//
//            // update selected item and title, then close the drawer
//            mDrawerList.setItemChecked(i, true);
//            mDrawerList.setSelection(i);
//            mDrawerLayout.closeDrawer(mDrawerList);
//        } else {
//            // error in creating fragment
//            Log.e("MainActivity", "Error in creating fragment");
//        }
//    }

    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            B_flag = isDeviceSupportCamera();

                            if (B_flag == true) {

                                //final CharSequence[] options = {"Take Photo", "Choose from Gallery", "View Image", "Cancel"};
                                final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);

                                builder.setTitle("Add Photo!");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {

                                        if (options[item].equals("Take Photo")) {
                                            // image_check = "photo";

                                            File photoFile = null;
                                            try {
                                                photoFile = createImageFile();
                                                picUri = Uri.fromFile(photoFile);
                                            } catch (IOException ex) {
                                                // Error occurred while creating the File
                                                Log.i("Image TAG", "IOException");
                                                //pictureImagePath = "";
                                            }
                                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                            Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                                                    BuildConfig.APPLICATION_ID + ".provider",
                                                    photoFile);
                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                            startActivityForResult(cameraIntent, 123);

                                        } else if (options[item].equals("Choose from Gallery")) {


                                            Intent i = new Intent(
                                                    Intent.ACTION_PICK,
                                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                            startActivityForResult(i, 1);
//                                            Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                                            pictureIntent.setType("image/*");
//                                            pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                                                String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
//                                                pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//                                            }
//                                            startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), 2);

                                            // image_check = "gallery";
//                                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                                            startActivityForResult(intent, 2);


                                        } else if (options[item].equals("View Image")) {
                                            Intent intent = new Intent();
                                            intent.setAction(android.content.Intent.ACTION_VIEW);
                                            intent.setDataAndType(Uri.fromFile(new File(pictureImagePath_new)), "image/png");
                                            startActivity(intent);


                                            //call the standard crop action intent

                                        } else if (options[item].equals("Cancel")) {

                                            dialog.dismiss();

                                        }

                                    }

                                });

                                builder.show();
                            } else {
                                //   Toast.makeText(getApplicationContext(), "no camera on this device", Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), "no camera on this device", "");
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        //    Toast.makeText(getApplicationContext(), "Error occurred! " + error.toString(), Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(getApplicationContext(), "Error occurred! " + error.toString(), "");

                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            //get the cropped bitmap from extras
            Bitmap thePic = extras.getParcelable("data");

            //set image bitmap to image view
            img_header.setImageBitmap(thePic);

            Uri imageuri = getImageUri(MainActivity.this, thePic);

            imagePath = getRealPathFromURI(imageuri);

            SharedPreferences pref = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString("img_path", imagePath);
            edit.commit();

        } else if (requestCode == 123 && resultCode == RESULT_OK) {
            File imgFile = new File(pictureImagePath_new);
            if (imgFile.exists()) {


                img_header.setImageURI(Uri.fromFile(imgFile));
                cropCapturedImage(Uri.fromFile(imgFile));


            }
        } else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            // img_header.setImageBitmap(picturePath);
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);

            Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setShader(shader);
            paint.setAntiAlias(true);
            Canvas c = new Canvas(circleBitmap);
            c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
            img_header.setImageBitmap(circleBitmap);
            cropCapturedImage(Uri.fromFile(new File(picturePath)));
            //gallery image
            pictureImagePath_new = picturePath;
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = "prifle_pic";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "D_CUstomer_Profile");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
//        else
//        {
//            storageDir.delete();
//            storageDir.mkdir();
//        }

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        pictureImagePath = "file:" + image.getAbsolutePath();
        pictureImagePath_new = image.getAbsolutePath();
        // mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    public void cropCapturedImage(Uri picUri) {
        //call the standard crop action intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        cropIntent.setDataAndType(picUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, 2);
    }

    private Boolean isDeviceSupportCamera() {
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.add:
                String targetNew = "";
                SharedPreferences sp = MainActivity.this.getSharedPreferences("SimpleLogic", 0);
                try {
                    int target = (int) Math.round(sp.getFloat("Target", 0));
                    int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                    Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
                    if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                        int age = (int) Math.round(age_float);
                        if (Global_Data.rsstr.length() > 0) {
                            targetNew = "T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
                            //todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        } else {
                            targetNew = "T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                    } else {
                        int age = (int) Math.round(age_float);
                        if (Global_Data.rsstr.length() > 0) {
                            targetNew = "T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
                            // todaysTarget.setText();
                        } else {
                            targetNew = "T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
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

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */

    public void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;

        switch (position) {

            case 1:
                fragment = new Home();
                fragmentPoistion = 0;
                break;

            case 2:
                Intent map = new Intent(MainActivity.this, BasicMapDemoActivity.class);
                startActivity(map);

//                Intent conaa = new Intent(MainActivity.this, DashboardActivity.class);
//                startActivity(conaa);
                fragment = new Home();
                break;

            case 3:
                Intent conaaa = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(conaaa);
                fragment = new Home();
                break;

            case 4:
                Intent conaa1 = new Intent(MainActivity.this, Sound_Setting.class);
                startActivity(conaa1);
                fragment = new Home();
                break;

            case 5:

                Intent con = new Intent(MainActivity.this, Contact_Us.class);
                startActivity(con);

                fragment = new Home();
                fragmentPoistion = 2;
                break;


//			case 4:
//
//                // onBackPressed();
//
//                List<Local_Data> contcustomer = dbvoc.getOrderAll("Secondary Sales / Retail Sales");
//
//                if (contcustomer.size() <= 0) {
//                    //Toast.makeText(getApplicationContext(), "NO order found", Toast.LENGTH_LONG).show();
//
//                    Toast toast = Toast.makeText(getApplicationContext(), "NO order found", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                } else {
//                    Intent a = new Intent(MainActivity.this, Previous_Order.class);
//                    startActivity(a);
//                    finish();
//                }
//
//			fragment = new Home();
//
//    		break;
//
//            case 5:
//
//                List<Local_Data> contcust = dbvoc.getOrderAllReturn("Secondary Sales / Retail Sales");
//
//                if (contcust.size() <= 0) {
//                    //Toast.makeText(getApplicationContext(), "NO Returnorder found", Toast.LENGTH_LONG).show();
//
//                    Toast toast = Toast.makeText(getApplicationContext(), "NO Return order found", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                } else {
//                    Intent b = new Intent(MainActivity.this, Previous_returnOrder.class);
//                    startActivity(b);
//                    finish();
//                }
//
//                fragment = new Home();
//
//                break;

            case 6:
                Intent cona = new Intent(MainActivity.this, About_Metal.class);
                startActivity(cona);
                fragment = new Home();
                break;
//                Intent targetintent = new Intent(MainActivity.this, TargetAnalysisActivity.class);
//                startActivity(targetintent);

            case 7:
                Intent cona1 = new Intent(MainActivity.this, ChangePasswordActivity.class);
                startActivity(cona1);
                fragment = new Home();
                break;


            case 8:

                onBackPressed();
                fragment = new Home();
                break;
//

//			case 7:
//                Intent conaa = new Intent(MainActivity.this, Sound_Setting.class);
//                startActivity(conaa);
//                //finish();
//                fragment = new Home();
//                break;
//            case 8:
//                Intent con = new Intent(MainActivity.this, Contact_Us.class);
//                startActivity(con);
//                //finish();
//                fragment = new Home();
//                break;
//            case 9:
//                Intent cona = new Intent(MainActivity.this, About_Metal.class);
//                startActivity(cona);
//                //finish();
//                fragment = new Home();
//                break;
//			case 10:
//				Intent out = new Intent(MainActivity.this, Customer_Outstanding.class);
//				startActivity(out);
//				//finish();
//				fragment = new Home();
//				break;
//
//			case 11:
//				onBackPressed();
//                fragment = new Home();
//                break;

            default:
                break;
        }

//        if (fragment != null) {
//            FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.frame_container, fragment).commit();
//
//            // update selected item and title, then close the drawer
//            mDrawerList.setItemChecked(position, true);
//            mDrawerList.setSelection(position);
//            //    setTitle(navMenuTitles[position]);
//            mDrawerLayout.closeDrawer(mDrawerList);
//        } else {
//            // error in creating fragment
//            Log.e("MainActivity", "Error in creating fragment");
//        }
    }

//    @Override
//    public void setTitle(CharSequence title) {
//        mTitle = title;
//
//
////		if (title.equals("Home") || title.equals("Sync Now") || title.equals("View Nearest Customer") || title.equals("View Institutional Sales Status")) {
//        mTitle = getResources().getString(R.string.Dashboard);
//        //screenname.setText(mTitle);
//        screen_title.setText(mTitle);
//        //}
//        getActionBar().setTitle(mTitle);
//    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        // mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        // animateIn this activity

//        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
//
//
//        /* Theme Code */
//       String  selectedTheme = spf1.getString(KEY_CURRENT_THEME, LILAC_THEME);
//        if(currentTheme != selectedTheme)
        //  recreate();

        if (firstLaunch) {
            // Your onResume Code Here
            //ViewGroup vg = (ViewGroup) findViewById (R.id.drawer_layout);
            //vg.invalidate();
            //getWindow().getDecorView().findViewById(android.R.id.content).invalidate();

            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            try {
                ActivitySwitcher.animationIn(findViewById(R.id.container), getWindowManager());
            } catch (Exception e) {
                // TODO: handle exception
            }
            //screen_title.setText("Order Booking");
            SharedPreferences sp = MainActivity.this.getSharedPreferences("SimpleLogic", 0);

//		        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//		        	//todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//					todaysTarget.setText("Target/Acheived : Rs "+String.format(sp.getFloat("Target",0)+"/"+sp.getFloat("Achived", 0)));
//				}


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
                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                } else {
                    int age = (int) Math.round(age_float);
                    try {
                        if (Global_Data.rsstr.length() > 0) {
                            todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                        } else {
                            todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
//		        	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
                todaysTarget.setText("Today's Target Acheived");
            }

        } else {
            firstLaunch = true;
        }
        super.onResume();
    }

//	public void newOrder(View v)
//	{
//		Intent i=new Intent(getApplicationContext(), NewOrderFragment.class);
//		SharedPreferences  sp=this.getSharedPreferences("SimpleLogic", 0);
//
//		i.putExtra("retialer", ""+sp.getString("RetailerName", ""));
//		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(i);
//		//Log.e("TEST", "NEW OREDER CLICKED");
//
//		//NewOrderFragment newFragment = new NewOrderFragment();
//		//FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//		// Replace whatever is in the fragment_container view with this fragment,
//		// and add the transaction to the back stack
//		//transaction.replace(R.id.fragment_container, newFragment);
//		//transaction.addToBackStack(null);
//
//		// Commit the transaction
//		//transaction.commit();
//	}

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();

        if (fragmentPoistion == 0 || fragmentPoistion == 3) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
            alertDialog.setTitle(getResources().getString(R.string.Confirmation));
            alertDialog.setMessage(getResources().getString(R.string.Logout_warning_message));
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    startActivity(i);
                    stopService(new Intent(MainActivity.this, LocationServices.class));
                    finishAffinity();
                    finish();
                }
            });

            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                }
            });

            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    private void scheduleNotify() {
        Intent notificationIntent = new Intent(this, SendLocation.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 1000 * 60 * 5, pendingIntent);
        SharedPreferences pref_devid = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);

        final String Device_id = pref_devid.getString("devid", "");
        Global_Data.device_id = Device_id;
    }

    public void getTargetDatamain() {
        t_total = 0;
        achived_total = 0;
        SharedPreferences sp = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);

        //device_id = sp.getString("devid", "");
        calendarn = Calendar.getInstance();
        year = calendarn.get(Calendar.YEAR);

//        dialog = new ProgressDialog(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//        dialog.setMessage(getResources().getString(R.string.Target_dialog_message));
//        dialog.setTitle(getResources().getString(R.string.app_name));
//        dialog.setCancelable(false);
//        //dialog.setCancelable(false);
//        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();

        String user_email = "";

        try {
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(String.valueOf(sp.getString("USER_EMAIL", "")))) {
                user_email = sp.getString("USER_EMAIL", "");
            } else {
                user_email = Global_Data.GLOvel_USER_EMAIL;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";
            String domain = service_url;

            Log.i("volley", "domain: " + domain);
            Log.i("volley", "email: " + user_email);
            Log.i("target url", "target url " + domain + "targets?email=" + user_email);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(domain + "targets?email=" + user_email, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);

                    try {
                        String response_result = "";
                        if (response.has("result")) {
                            response_result = response.getString("result");
                        } else {
                            response_result = "data";
                        }

                        if (response_result.equalsIgnoreCase("User doesn't exist")) {
//                            Toast toast = Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(MainActivity.this, response_result, "yes");
                        } else if (response_result.equalsIgnoreCase("User not registered")) {
//                            Toast toast = Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(MainActivity.this, response_result, "yes");
                            dbvoc.getDeleteTable("targets");
                            JSONArray targets = response.getJSONArray("targets");
                            Log.i("volley", "response reg targets Length: " + targets.length());
                            Log.d("States", "targets" + targets.toString());

                            if (targets.length() > 0) {
                                for (int i = 0; i < targets.length(); i++) {
                                    JSONObject jsonObject = targets.getJSONObject(i);
//	                                loginDataBaseAdapter.insertTargets(jsonObject.getString("code"),"", jsonObject.getString("user_id"),
//	                                		 jsonObject.getString("year"), jsonObject.getString("month"), jsonObject.getString("target"),
//	                                		 jsonObject.getString("achieved"), jsonObject.getString("created_at"), jsonObject.getString("update_at"));
                                    loginDataBaseAdapter.insertTargets("", "", "",
                                            jsonObject.getString("year"), jsonObject.getString("month"), jsonObject.getString("target"),
                                            jsonObject.getString("achieved"), "", "");

                                    if (jsonObject.getString("year").equalsIgnoreCase(String.valueOf(year))) {
                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("target").toString())) {
                                            if (!jsonObject.getString("target").equalsIgnoreCase("null") && !jsonObject.getString("target").equalsIgnoreCase(null) & !jsonObject.getString("target").equalsIgnoreCase("") & !jsonObject.getString("target").equalsIgnoreCase(" ")) {
                                                t_total += Float.valueOf(jsonObject.getString("target").toString());
                                            } else {
                                                t_total += Float.valueOf("0.0");
                                            }
                                        }
                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("achieved").toString())) {
                                            if (!jsonObject.getString("achieved").equalsIgnoreCase("null") && !jsonObject.getString("achieved").equalsIgnoreCase(null) & !jsonObject.getString("achieved").equalsIgnoreCase("") & !jsonObject.getString("achieved").equalsIgnoreCase(" ")) {
                                                achived_total += Float.valueOf(jsonObject.getString("achieved").toString());
                                            } else {
                                                achived_total += Float.valueOf("0.0");
                                            }
                                        }
                                    }
                                }
                                SharedPreferences spf = MainActivity.this.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                //editor.putString("UserID", "admin");
                                //editor.putString("pwd", "test");
                                editor.putFloat("Target", t_total);
                                editor.putFloat("Achived", achived_total);
                                //editor.putString("SimID", simSerial);
                                editor.commit();
                                try {
                                    int target = (int) Math.round(t_total);
                                    int achieved = (int) Math.round(achived_total);
                                    Float age_float = (achived_total / t_total) * 100;
                                    //int age = (int) Math.round(age_float);
                                    //	todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
                                    if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                                        int age = (int) Math.round(age_float);

                                        if (Global_Data.rsstr.length() > 0) {
                                            todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                                        } else {
                                            todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                                        }

                                        //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                                    } else {
                                        int age = (int) Math.round(age_float);
                                        if (Global_Data.rsstr.length() > 0) {
                                            todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                                        } else {
                                            todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                                        }
                                        //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                try {
                                    SharedPreferences spf = MainActivity.this.getSharedPreferences("SimpleLogic", 0);
                                    SharedPreferences.Editor editor = spf.edit();
                                    //editor.putString("UserID", "admin");
                                    //editor.putString("pwd", "test");
                                    editor.putFloat("Target", 0);
                                    editor.putFloat("Achived", 0);
                                    //editor.putString("SimID", simSerial);
                                    editor.commit();
                                    //todaysTarget.setText("T/A : Rs " + "0");
                                    if (Global_Data.rsstr.length() > 0) {
                                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format("0" + "/" + "0" + " [" + "0") + "%" + "]");
                                    } else {
                                        todaysTarget.setText("T/A : Rs " + String.format("0" + "/" + "0" + " [" + "0") + "%" + "]");
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            //todaysTarget.setText("T/A : Rs "+t_total+"/"+achived_total);

                            // dialog.dismiss();
                            //finish();
                        }
                        // }

                        // output.setText(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //dialog.dismiss();
                    }
                    // dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volley", "error: " + error);
                    //Toast.makeText(MainActivity.this, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();
//                    Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(MainActivity.this, getResources().getString(R.string.Server_Error), "yes");
                    //   dialog.dismiss();
                }
            });
            // requestQueue = Volley.newRequestQueue(MainActivity.this);

            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(MainActivity.this);
                Log.d("new error", "Setting a new request queue");
            }

            // queue.add(jsObjRequest);
            jsObjRequest.setShouldCache(false);
            int socketTimeout = 200000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);

        } catch (Exception e) {
            e.printStackTrace();
            //  dialog.dismiss();
        }
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item

            displayView(position);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
            //dialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
            //dialog.dismiss();
        }
    }

    /**
     * Requesting GPS permission
     * This uses single permission model from dexter
     * Once the permission granted, opens the camera
     * On permanent denial opens settings dialog
     */
    private void requestGPSPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        PlayServiceManager = new PlayService_Location(MainActivity.this);

                        if (Global_Data.LOCATION_SERVICE_HIT.equalsIgnoreCase("TRUE")) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                // startForegroundService(new Intent(this, MyService.class));
                                // startService(new Intent(this, MyService.class));
//                ComponentName jobService =
//                        new ComponentName(getPackageName(), TestJobService.class.getName());
                                //  startService(new Intent(this, MyService.class));

                                JobScheduler jobScheduler =
                                        (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                                jobScheduler.schedule(new JobInfo.Builder(1,
                                        new ComponentName(MainActivity.this, BJobService.class))
                                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                        .build());

                            } else {
                                startService(new Intent(MainActivity.this, MyService.class));
                            }
                            // startService(new Intent(this, MyService.class));
                            Global_Data.LOCATION_SERVICE_HIT = "";
                        }

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.need_permission_message));
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.need_permission_setting_message));
        builder.setPositiveButton(getResources().getString(R.string.GOTO_SETTINGS), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    public void alaramM() {
        try {
//            Intent notifyIntent = new Intent(this, OrderCheckReceiver.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast
//                    (MainActivity.this, 8, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(),
//                    60000, pendingIntent);

            JobScheduler jobScheduler =
                    (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(new JobInfo.Builder(1,
                    new ComponentName(this, TestJobServiceOrder.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build());
        } catch (Exception ex) {

        }

    }


    private class scheduleoperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {

            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("result")) {
                    response_result = response.getString("result");
                } else {
                    response_result = "data";
                }

                if (response_result.equalsIgnoreCase("Schedule doesn't exist")) {


                    //Toast.makeText(Order.this, response_result, Toast.LENGTH_LONG).show();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            // do something
                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(MainActivity.this, response_result, "yes");
                        }
                    });

//                    runOnUiThread(new Runnable() {
//                        public void run() {
//
//
//                            dialog.dismiss();
//                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();
//
//                            Toast toast = Toast.makeText(itemView.getContext(), response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
//                        }
//                    });

                } else {

                    //dbvoc.getDeleteTable("delivery_products");
                    //dbvoc.getDeleteTable("delivery_schedules");
                    //dbvoc.getDeleteTable("credit_profile");
                    dbvoc.getDeletedelivery_schedulesAll();
                    dbvoc.getDeletedelivery_productsAll();
                    dbvoc.getDeletecredit_limitsAll();

                    JSONArray delivery_products = response.getJSONArray("delivery_products");
                    JSONArray delivery_schedules = response.getJSONArray("delivery_schedules");
                    JSONArray credit_profile = response.getJSONArray("credit_profiles");

                    Log.i("volley", "response reg delivery_products Length: " + delivery_products.length());
                    Log.i("volley", "response reg delivery_schedules Length: " + delivery_schedules.length());
                    Log.i("volley", "response reg credit_profile Length: " + credit_profile.length());

                    Log.d("volley", "delivery_products" + delivery_products.toString());
                    Log.d("volley", "delivery_schedules" + delivery_schedules.toString());
                    Log.d("volley", "credit_profile" + credit_profile.toString());

                    if (delivery_schedules.length() <= 0) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                // do something
                                dialog.dismiss();
                                //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();
//
//                                Toast toast = Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.Delivery_Schedule_Not_Found), Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(MainActivity.this, MainActivity.this.getResources().getString(R.string.Delivery_Schedule_Not_Found), "yes");
                            }
                        });

//                        MainActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//
//                                dialog.dismiss();
//                                //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();
//
//                                Toast toast = Toast.makeText(MainActivity.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//                            }
//                        });
                    } else {

                        Global_Data.GLOvel_CUSTOMER_ID = "";
                        for (int i = 0; i < delivery_products.length(); i++) {

                            JSONObject jsonObject = delivery_products.getJSONObject(i);

                            loginDataBaseAdapter.insertDeliveryProducts("", jsonObject.getString("customer_code"), jsonObject.getString("order_number"), "", "", "", "", "", jsonObject.getString("order_quantity"), jsonObject.getString("delivered_quantity"), jsonObject.getString("truck_number"), jsonObject.getString("transporter_details"), "", "", jsonObject.getString("product_name") + "" + "" + "");


                        }

                        for (int i = 0; i < delivery_schedules.length(); i++) {

                            JSONObject jsonObject = delivery_schedules.getJSONObject(i);

                            loginDataBaseAdapter.insertDeliverySchedule("", jsonObject.getString("customer_code"), jsonObject.getString("customer_code"), jsonObject.getString("order_number"), "", jsonObject.getString("user_email"), jsonObject.getString("dispatch_date"), jsonObject.getString("delivery_date"), jsonObject.getString("order_amount"), jsonObject.getString("accepted_payment_mode"), "", jsonObject.getString("collected_amount"), jsonObject.getString("outstanding_amount"), "", "", jsonObject.getString("customer_address"));


                        }


                        for (int i = 0; i < credit_profile.length(); i++) {

                            JSONObject jsonObject = credit_profile.getJSONObject(i);

                            loginDataBaseAdapter.insert_credit_profile("", jsonObject.getString("customer_code"), jsonObject.getString("customer_code"), "", "", "", "", jsonObject.getString("credit_limit"), jsonObject.getString("amount_outstanding"), jsonObject.getString("amount_overdue"));

                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                // do something
                                Global_Data.Schedule_FLAG = "ALLCUSTOMER";
                                Intent intent = new Intent(MainActivity.this, Schedule_List.class);
                                MainActivity.this.startActivity(intent);
//                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                                finish();
                                dialog.dismiss();
                            }
                        });

//                        MainActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//
//                                Global_Data.Schedule_FLAG = "ALLCUSTOMER";
//                                Intent intent = new Intent(itemView.getContext(), Schedule_List.class);
//                                itemView.getContext().startActivity(intent);
////                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
////                                finish();
//                                dialog.dismiss();
//                            }
//                        });
                    }

//                    MainActivity.this.runOnUiThread(new Runnable() {
//                        public void run() {
//
//                            dialog.dismiss();
//                        }
//                    });
                    //dialog.dismiss();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            // do something
                            dialog.dismiss();
                        }
                    });
                    //finish();

                }


                // }

                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        // do something
                        dialog.dismiss();
                    }
                });

//                MainActivity.this.runOnUiThread(new Runnable() {
//                    public void run() {
//
//                        dialog.dismiss();
//                    }
//                });

            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    // do something
                    dialog.dismiss();
                }
            });

//            MainActivity.this.runOnUiThread(new Runnable() {
//                public void run() {
//
//                    dialog.dismiss();
//                }
//            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    // do something
                    dialog.dismiss();
                }
            });

//            MainActivity.this.runOnUiThread(new Runnable() {
//                public void run() {
//                    dialog.dismiss();
//                }
//            });


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void Language_Select_Dialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(null);
        dialog.setContentView(R.layout.language_dialog);
        // dialog.setTitle(getResources().getString(R.string.PDistributors));

        Language_List.clear();
        Language_List.add(getResources().getString(R.string.Select_Language));
        Language_List.add(getResources().getString(R.string.English));
        Language_List.add(getResources().getString(R.string.Hindi));

        final Spinner spnLanguage = dialog.findViewById(R.id.spnLanguage);
        LanguageAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, Language_List);
        LanguageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        LanguageAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.spinner_item, Language_List);
//        LanguageAdapter.setDropDownViewResource(R.layout.spinner_item);
        spnLanguage.setAdapter(LanguageAdapter);

        SharedPreferences spff = MainActivity.this.getSharedPreferences("SimpleLogic", 0);
        String Language = spff.getString("Language", "");

        if (!Language.equalsIgnoreCase("") && !Language.equalsIgnoreCase(null)) {
            int spinnerPosition = LanguageAdapter.getPosition(Language);
            spnLanguage.setSelection(spinnerPosition);
        }

        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        Button dialogButtonOk = dialog.findViewById(R.id.dialogButtonOk);
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spnLanguage.getSelectedItem().toString()
                        .equalsIgnoreCase(getResources().getString(R.string.Select_Language))) {
//                    Toast toast = Toast.makeText(MainActivity.this,
//                            getResources().getString(R.string.Please_Select_Language), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(MainActivity.this,
                            getResources().getString(R.string.Please_Select_Language), "yes");
                } else if (spnLanguage.getSelectedItem().toString()
                        .equalsIgnoreCase(getResources().getString(R.string.Hindi))) {

                    SharedPreferences spf = MainActivity.this.getSharedPreferences("SimpleLogic", 0);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("Language", "hi");
                    editor.commit();

                    Locale myLocale = new Locale("hi");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);

                    recreate();
                    dialog.dismiss();
                } else if (spnLanguage.getSelectedItem().toString()
                        .equalsIgnoreCase(getResources().getString(R.string.English))) {

                    SharedPreferences spf = MainActivity.this.getSharedPreferences("SimpleLogic", 0);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("Language", "en");
                    editor.commit();

                    Locale myLocale = new Locale("en_US");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);

                    recreate();

                    dialog.dismiss();
                }

            }
        });

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void requestGPSPermissionsignamap() {
        Dexter.withActivity((Activity) MainActivity.this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            isInternetPresent = cd.isConnectingToInternet();
                            if (isInternetPresent) {
                                dialog = new ProgressDialog(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                dialog.setMessage(MainActivity.this.getResources().getString(R.string.Please_Wait));
                                dialog.setTitle(MainActivity.this.getResources().getString(R.string.app_name));
                                dialog.setCancelable(false);
                                dialog.show();
                                List<Local_Data> contacts2 = dbvoc.get_BeatsData();
                                for (Local_Data cn : contacts2) {
                                    //results_beat.add(cn.getNamemap());
                                    String beat_address = "";

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getBeatName())) {
                                        beat_address = cn.getBeatName();

                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getCityName())) {
                                        beat_address += ", " + cn.getCityName();
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getStateName())) {
                                        beat_address += ", " + cn.getStateName();
                                    }

                                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                    List<Address> addresses = null;
                                    try {
                                        addresses = geocoder.getFromLocationName(beat_address, 1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //Address address = addresses.get(0);
                                    //if (addresses != null) {

                                    if (addresses.size() > 0) {
                                        lat = addresses.get(0).getLatitude();
                                        lon = addresses.get(0).getLongitude();
                                        //LatLng latLng = new LatLng(latitude, longitude);
//            mMap.addMarker(new MarkerOptions().position(latLng));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                                    }
                                    //}
                                    cn.setTitle(beat_address);
                                    //cn.setSnippet("Hello" +","+ cn.getNamemap());
                                    cn.setLatitude(String.valueOf(lat));
                                    cn.setLongitude(String.valueOf(lon));
                                    //arrayList.add(bean);
                                    Global_Data.list_mapdata.add(cn);
                                }
                                Intent con = new Intent(MainActivity.this, MapMultiUser.class);
                                startActivity(con);
                                // dialog.dismiss();
                            } else {
//                                Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(MainActivity.this, getResources().getString(R.string.internet_connection_error), "yes");
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        //Toast.makeText(MainActivity.this, getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(MainActivity.this, getResources().getString(R.string.Error_occurredd) + error.toString(), "yes");
                    }
                })
                .onSameThread()
                .check();
    }


    private void setAppTheme(String currentTheme) {
        setTheme(ThemeUtil.getThemeId(mTheme));

//        switch (currentTheme) {
//            case MAINTHEME :
//                this.setTheme(R.style.Theme_App_blue);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                    getWindow().setStatusBarColor(getResources().getColor(R.color.list_background_pressed));
//                }
//                break;
//            case LILAC_THEME :
//                this.setTheme(R.style.Theme_App_Lilac);
//                break;
//
//        }
    }


}

