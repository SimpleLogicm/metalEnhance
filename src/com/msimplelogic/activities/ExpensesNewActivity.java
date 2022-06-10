package com.msimplelogic.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.msimplelogic.adapter.ExpensesHorizontalAdapter;
import com.msimplelogic.adapter.ExpensesSwipeAdapter;
import com.msimplelogic.model.ExpensesModel;
import com.msimplelogic.model.Product;
import com.msimplelogic.swipelistview.BaseSwipeListViewListener;
import com.msimplelogic.swipelistview.SwipeListView;
import com.msimplelogic.swipelistview.sample.utils.SettingsManager;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static com.github.mikephil.charting.utils.Utils.convertDpToPixel;

public class ExpensesNewActivity extends BaseActivity {
    String month = "";
    static String ExpenseType;
    private ExpensesSwipeAdapter adapter;
    private SwipeListView swipeListView;
    String selectedMonth = "";
    String currentYear;
    String selectedYear = "";
    Date dateselect;
    RelativeLayout hor_rlout1, hor_rlout2, hor_rlout3, hor_rlout4;
    static String final_response = "";
    String response_result = "";
    ProgressDialog dialog;
    SharedPreferences sp;
    ImageView iv_horizontal1, iv_horizontal2, iv_horizontal3, iv_horizontal4,hedder_theame;
    ConnectionDetector cd;
    static final String TAG_ITEMNAME = "amount";
    static final String TAG_QTY = "status";
    static final String TAG_PRICE = "date";
    Boolean isInternetPresent = false;
    TextView schedule_txt, textView1sf, txtClaimAmount, txtApproved, txtPending, tvConvey, tvFood, tvHotel, tvMisc, tv_con_amount, tv_foodamount, tv_hotelamount, tv_miscamount;
    EditText etExpenseDate;
    DatePickerDialog.OnDateSetListener date;
    Calendar myCalendar;
    ImageView plusToggle;
    Spinner expenseSpinner, expensesMonthSpinner;
    ArrayAdapter<String> expensesSpnAdapter;
    ArrayAdapter<String> expensesMonthSpnAdapter;
    private ArrayList<String> expensesResults = new ArrayList<String>();
    private ArrayList<String> expensesMonthResults = new ArrayList<String>();
    private ArrayList<String> expensesMonthResultsHide = new ArrayList<String>();
    ShimmerRecyclerView listOfficelistInfo;
    private List<ExpensesModel> officelistinfo_list = new ArrayList<>();
    private ExpensesHorizontalAdapter expensesHorizontalAdapter;
    ArrayList<HashMap<String, String>> SwipeList;
    private ArrayList<Product> dataOrder;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newexpenses);

        etExpenseDate = (EditText) findViewById(R.id.et_expensedate);
        expenseSpinner = (Spinner) findViewById(R.id.sp_expenses_types);
        expensesMonthSpinner = findViewById(R.id.sp_expenses_months);
        plusToggle = (ImageView) findViewById(R.id.plus_toggle);
        hedder_theame = (ImageView) findViewById(R.id.hedder_theameexp);
        swipeListView = (SwipeListView) findViewById(R.id.expense_swipelistn);
        SwipeList = new ArrayList<HashMap<String, String>>();
        txtClaimAmount = (TextView) findViewById(R.id.tv_total_claimamt);
        txtApproved = (TextView) findViewById(R.id.tv_approved);
        txtPending = (TextView) findViewById(R.id.tv_pending);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        hor_rlout1 = findViewById(R.id.hor_rlout1);
        hor_rlout2 = findViewById(R.id.hor_rlout2);
        hor_rlout3 = findViewById(R.id.hor_rlout3);
        hor_rlout4 = findViewById(R.id.hor_rlout4);

        iv_horizontal1 = findViewById(R.id.iv_horizontal1);
        iv_horizontal2 = findViewById(R.id.iv_horizontal2);
        iv_horizontal3 = findViewById(R.id.iv_horizontal3);
        iv_horizontal4 = findViewById(R.id.iv_horizontal4);

        tv_con_amount = findViewById(R.id.tv_con_amount);
        tv_foodamount = findViewById(R.id.tv_foodamount);
        tv_hotelamount = findViewById(R.id.tv_hotelamount);
        tv_miscamount = findViewById(R.id.tv_miscamount);

        tvConvey = findViewById(R.id.tv_convey);
        tvFood = findViewById(R.id.tv_food);
        tvHotel = findViewById(R.id.tv_hotel);
        tvMisc = findViewById(R.id.tv_misc);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        cd = new ConnectionDetector(getApplicationContext());

        listOfficelistInfo = findViewById(R.id.expense_swipelist);

//        expenseList.add(new ExpensesModel("Conveyance","image","24000","","",""));
//        expenseList.add(new ExpensesModel("Conveyance","image","24000","","",""));

        // add a divider after each item for more clarity
        //groceryRecyclerView1.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.HORIZONTAL));
        expensesHorizontalAdapter = new ExpensesHorizontalAdapter(getApplicationContext(), officelistinfo_list);
        LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(ExpensesNewActivity.this, LinearLayoutManager.VERTICAL, false);
        listOfficelistInfo.setLayoutManager(horizontalLayoutManager2);
        listOfficelistInfo.setAdapter(expensesHorizontalAdapter);
        listOfficelistInfo.hideShimmerAdapter();

        // for label change
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        String schedulestr = spf1.getString("var_schedule", "");

//        if(schedulestr.length()>0)
//        {
//            Log.d("App Language", "App Language " + Locale.getDefault().getDisplayLanguage());
//            String locale = Locale.getDefault().getDisplayLanguage();
//            if (locale.equalsIgnoreCase("English")) {
//                schedule_txt.setText(schedulestr.toUpperCase());
//            } else {
//                schedule_txt.setText(getResources().getString(R.string.SCHEDULE));
//            }
//
//        }else{
//            schedule_txt.setText(getResources().getString(R.string.SCHEDULE));
//        }

        Intent i = this.getIntent();
        dataOrder = i.getParcelableArrayListExtra("productsList");
        adapter = new ExpensesSwipeAdapter(ExpensesNewActivity.this, SwipeList);

//        HashMap<String, String> mapp = new HashMap<String, String>();
//        mapp.put(TAG_ITEMNAME, "Maggi");
//        mapp.put(TAG_QTY, i.getStringExtra("5"));
//        mapp.put(TAG_PRICE, i.getStringExtra("2000"));
//
//        SwipeList.add(mapp);

//        if (Build.VERSION.SDK_INT >= 11) {
//            swipeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            swipeListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                      long id, boolean checked) {
                    mode.setTitle("Selected (" + swipeListView.getCountSelected() + ")");
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.menu_delete) {
                        swipeListView.dismissSelected();
                        return true;
                    }
                    return false;
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.menu_choice_items, menu);
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    swipeListView.unselectedChoiceStates();
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }
            });
        }

        swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {

            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));
            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    dataOrder.remove(position);
                }
                adapter.notifyDataSetChanged();
            }
        });

        swipeListView.setAdapter(adapter);

        reload();

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

//        etExpenseDate.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                etExpenseDate.setEnabled(false);
////                etExpenseDate.setFocusableInTouchMode(false);
//
//                etExpenseDate.setEnabled(true);
//                etExpenseDate.setFocusableInTouchMode(false);
//
//
//                new DatePickerDialog(ExpensesNewActivity.this, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//
//                //  Toast.makeText(CalendarPlannerActivity.this, "adsfsdg", Toast.LENGTH_SHORT).show();
//            }
//        });
        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1){

            hedder_theame.setImageResource(R.drawable.dark_hedder);
            plusToggle.setColorFilter(ContextCompat.getColor(ExpensesNewActivity.this, R.color.secondaryDarkColorDarkTheme), android.graphics.PorterDuff.Mode.MULTIPLY);


            Glide.with(ExpensesNewActivity.this)
                    //.load(horizontalGrocderyList.get(position).getProductImage())
                    .load(R.drawable.conveyance_dark)
                    .thumbnail(0.5f)
                    //.centerCrop()
                    .placeholder(R.drawable.not_found)
                    .error(R.drawable.not_found)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_horizontal1);

            Glide.with(ExpensesNewActivity.this)
                    //.load(horizontalGrocderyList.get(position).getProductImage())
                    .load(R.drawable.food_dark)
                    .thumbnail(0.5f)
                    //.centerCrop()
                    .placeholder(R.drawable.not_found)
                    .error(R.drawable.not_found)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_horizontal2);

            Glide.with(ExpensesNewActivity.this)
                    //.load(horizontalGrocderyList.get(position).getProductImage())
                    .load(R.drawable.hotel_dark)
                    .thumbnail(0.5f)
                    //.centerCrop()
                    .placeholder(R.drawable.not_found)
                    .error(R.drawable.not_found)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_horizontal3);

            Glide.with(ExpensesNewActivity.this)
                    //.load(horizontalGrocderyList.get(position).getProductImage())
                    .load(R.drawable.miscllaneous_dark)
                    .thumbnail(0.5f)
                    //.centerCrop()
                    .placeholder(R.drawable.not_found)
                    .error(R.drawable.not_found)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_horizontal4);

//ll.setBackgroundResource(R.drawable.dark_theme_background);
        }
        hor_rlout1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expenseSpinner.getSelectedItem().toString().equalsIgnoreCase("Travel")||expenseSpinner.getSelectedItem().toString().equalsIgnoreCase("All")) {
                    Global_Data.ExpenseName = tvConvey.getText().toString();
                    hor_rlout1.setBackgroundResource(R.drawable.card_redbg);
                    tvConvey.setTextColor(Color.parseColor("#ffffff"));
                    //holder.imageView.setImageResource(R.drawable.miscllaneous_white);
                    hor_rlout2.setBackgroundResource(R.drawable.leavebal_bg);
                    tvFood.setTextColor(Color.parseColor("#4C4C4C"));

                    hor_rlout3.setBackgroundResource(R.drawable.leavebal_bg);
                    tvHotel.setTextColor(Color.parseColor("#4C4C4C"));

                    hor_rlout4.setBackgroundResource(R.drawable.leavebal_bg);
                    tvMisc.setTextColor(Color.parseColor("#4C4C4C"));

                    sp = getSharedPreferences("SimpleLogic", 0);
                    int current_theme = sp.getInt("CurrentTheme",0);

                    if (current_theme== 1){


                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.conveyance_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal1);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.food_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal2);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.hotel_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal3);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.miscllaneous_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal4);

//ll.setBackgroundResource(R.drawable.dark_theme_background);
                    }else{

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.conveyance)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal1);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.food)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal2);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.hotel)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal3);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.miscllaneous)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal4);
                    }




                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();

                        ExpenseResult();


                        //AllusersResultu();
                    } else {
                        Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.","");
                       // Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Global_Data.Custom_Toast(ExpensesNewActivity.this, "Please Select Type Travel", "");
                }


            }
        });

        hor_rlout2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expenseSpinner.getSelectedItem().toString().equalsIgnoreCase("Food")||expenseSpinner.getSelectedItem().toString().equalsIgnoreCase("All")) {


                Global_Data.ExpenseName = tvFood.getText().toString();
                hor_rlout2.setBackgroundResource(R.drawable.card_redbg);
                tvFood.setTextColor(Color.parseColor("#ffffff"));
                //holder.imageView.setImageResource(R.drawable.miscllaneous_white);

                hor_rlout1.setBackgroundResource(R.drawable.leavebal_bg);
                tvConvey.setTextColor(Color.parseColor("#4C4C4C"));

                hor_rlout3.setBackgroundResource(R.drawable.leavebal_bg);
                tvHotel.setTextColor(Color.parseColor("#4C4C4C"));

                hor_rlout4.setBackgroundResource(R.drawable.leavebal_bg);
                tvMisc.setTextColor(Color.parseColor("#4C4C4C"));

                    sp = getSharedPreferences("SimpleLogic", 0);
                    int current_theme = sp.getInt("CurrentTheme",0);

                    if (current_theme== 1){

                        hedder_theame.setImageResource(R.drawable.dark_hedder);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.conveyance_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal1);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.food_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal2);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.hotel_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal3);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.miscllaneous_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal4);

//ll.setBackgroundResource(R.drawable.dark_theme_background);
                    }else{
                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.food)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal2);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.conveyance)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal1);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.hotel)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal3);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.miscllaneous)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal4);
                    }


                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    dialog.setMessage("Please wait Loading....");
                    dialog.setTitle("Metal App");
                    dialog.setCancelable(false);
                    dialog.show();
                    ExpenseResult();
                    //AllusersResultu();
                } else {
                  Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.", "");
                  //  Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                }
                }else
                {
                    Global_Data.Custom_Toast(ExpensesNewActivity.this, "Please Select Type Food", "");
                }
            }
        });

        hor_rlout3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expenseSpinner.getSelectedItem().toString().equalsIgnoreCase("Hotel")||expenseSpinner.getSelectedItem().toString().equalsIgnoreCase("All")) {


                Global_Data.ExpenseName = tvHotel.getText().toString();
                hor_rlout3.setBackgroundResource(R.drawable.card_redbg);
                tvHotel.setTextColor(Color.parseColor("#ffffff"));
                //holder.imageView.setImageResource(R.drawable.miscllaneous_white);

                hor_rlout2.setBackgroundResource(R.drawable.leavebal_bg);
                tvFood.setTextColor(Color.parseColor("#4C4C4C"));

                hor_rlout1.setBackgroundResource(R.drawable.leavebal_bg);
                tvConvey.setTextColor(Color.parseColor("#4C4C4C"));

                hor_rlout4.setBackgroundResource(R.drawable.leavebal_bg);
                tvMisc.setTextColor(Color.parseColor("#4C4C4C"));



                    sp = getSharedPreferences("SimpleLogic", 0);
                    int current_theme = sp.getInt("CurrentTheme",0);

                    if (current_theme== 1){

                        hedder_theame.setImageResource(R.drawable.dark_hedder);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.conveyance_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal1);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.food_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal2);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.hotel_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal3);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.miscllaneous_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal4);

//ll.setBackgroundResource(R.drawable.dark_theme_background);
                    }else{
                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.food)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal2);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.conveyance)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal1);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.hotel)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal3);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.miscllaneous)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal4);
                    }

                    isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    dialog.setMessage("Please wait Loading....");
                    dialog.setTitle("Metal App");
                    dialog.setCancelable(false);
                    dialog.show();
                    ExpenseResult();
                    //AllusersResultu();
                } else {
                   Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.","");
                  //  Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                }
                }else
                {
                    Global_Data.Custom_Toast(ExpensesNewActivity.this, "Please Select Type Hotel", "");
                }
            }
        });

        hor_rlout4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expenseSpinner.getSelectedItem().toString().equalsIgnoreCase("Miscellaneous")||expenseSpinner.getSelectedItem().toString().equalsIgnoreCase("All")) {


                Global_Data.ExpenseName = tvMisc.getText().toString();
                hor_rlout4.setBackgroundResource(R.drawable.card_redbg);
                tvMisc.setTextColor(Color.parseColor("#ffffff"));
                //holder.imageView.setImageResource(R.drawable.miscllaneous_white);

                hor_rlout2.setBackgroundResource(R.drawable.leavebal_bg);
                tvFood.setTextColor(Color.parseColor("#4C4C4C"));

                hor_rlout3.setBackgroundResource(R.drawable.leavebal_bg);
                tvHotel.setTextColor(Color.parseColor("#4C4C4C"));

                hor_rlout1.setBackgroundResource(R.drawable.leavebal_bg);
                tvConvey.setTextColor(Color.parseColor("#4C4C4C"));

                    sp = getSharedPreferences("SimpleLogic", 0);
                    int current_theme = sp.getInt("CurrentTheme",0);

                    if (current_theme== 1){

                        hedder_theame.setImageResource(R.drawable.dark_hedder);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.conveyance_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal1);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.food_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal2);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.hotel_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal3);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.miscllaneous_dark)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal4);

//ll.setBackgroundResource(R.drawable.dark_theme_background);
                    }else{
                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.food)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal2);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.conveyance)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal1);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.hotel)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal3);

                        Glide.with(ExpensesNewActivity.this)
                                //.load(horizontalGrocderyList.get(position).getProductImage())
                                .load(R.drawable.miscllaneous)
                                .thumbnail(0.5f)
                                //.centerCrop()
                                .placeholder(R.drawable.not_found)
                                .error(R.drawable.not_found)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_horizontal4);
                    }


                    isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    dialog.setMessage("Please wait Loading....");
                    dialog.setTitle("Metal App");
                    dialog.setCancelable(false);
                    dialog.show();
                    ExpenseResult();
                    //AllusersResultu();
                } else {
                   Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.","");
                  //  Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                }
                }else
                {
                    Global_Data.Custom_Toast(ExpensesNewActivity.this, "Please Select Type Miscellaneous", "");
                }
            }
        });

        plusToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global_Data.ExpenseName.length() > 0) {
                    startActivity(new Intent(ExpensesNewActivity.this, ExpenseDetailsActivity.class));
                    Global_Data.cam = "";
                } else {
                    Global_Data.Custom_Toast(ExpensesNewActivity.this, "Please Select Type", "");

//                    Toast.makeText(ExpensesNewActivity.this, "Please Select Type", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //for selection
        expensesResults.clear();
        expensesResults.add("All");
        expensesResults.add("Travel");
        expensesResults.add("Food");
        expensesResults.add("Hotel");
        expensesResults.add("Miscellaneous");

        expensesSpnAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, expensesResults);
        expensesSpnAdapter.setDropDownViewResource(R.layout.spinner_item);
        expenseSpinner.setAdapter(expensesSpnAdapter);

        expenseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                // TODO Auto-generated method stub
                ExpenseType = adapterView.getItemAtPosition(i).toString();


                if (ExpenseType.equals("Food")) {
                    ExpenseType = "food";
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
//                        hor_rlout2.setBackgroundResource(R.drawable.card_redbg);
//                        tvFood.setTextColor(Color.parseColor("#ffffff"));
//                        //holder.imageView.setImageResource(R.drawable.miscllaneous_white);
//
//                        hor_rlout1.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvConvey.setTextColor(Color.parseColor("#4C4C4C"));
//
//                        hor_rlout3.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvHotel.setTextColor(Color.parseColor("#4C4C4C"));
//
//                        hor_rlout4.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvMisc.setTextColor(Color.parseColor("#4C4C4C"));
                        AllusersResultu(ExpenseType);
                      //  ExpenseResult();
                    } else {
                        Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.","");
                      //  Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } else if (ExpenseType.equals("Travel")) {
                    ExpenseType = "travel";
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
//                        hor_rlout2.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvFood.setTextColor(Color.parseColor("#4C4C4C"));
//                        //holder.imageView.setImageResource(R.drawable.miscllaneous_white);
//
//                        hor_rlout1.setBackgroundResource(R.drawable.card_redbg);
//                        tvConvey.setTextColor(Color.parseColor("#ffffff"));
//
//                        hor_rlout3.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvHotel.setTextColor(Color.parseColor("#4C4C4C"));
//
//                        hor_rlout4.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvMisc.setTextColor(Color.parseColor("#4C4C4C"));
                        AllusersResultu(ExpenseType);
                      //  ExpenseResult();
                    } else {
                        Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.","");
                    //    Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                    }


                } else if (ExpenseType.equals("Hotel")) {
                    ExpenseType = "hotel";
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
//                        hor_rlout2.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvFood.setTextColor(Color.parseColor("#4C4C4C"));
//                        //holder.imageView.setImageResource(R.drawable.miscllaneous_white);
//
//                        hor_rlout1.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvConvey.setTextColor(Color.parseColor("#4C4C4C"));
//
//                        hor_rlout3.setBackgroundResource(R.drawable.card_redbg);
//                        tvHotel.setTextColor(Color.parseColor("#ffffff"));
//
//                        hor_rlout4.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvMisc.setTextColor(Color.parseColor("#4C4C4C"));
                        AllusersResultu(ExpenseType);
                       // ExpenseResult();
                    } else {
                        Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.","");
                      //  Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } else if (ExpenseType.equals("Miscellaneous")) {
                    ExpenseType = "miscs";
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
//                        hor_rlout2.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvFood.setTextColor(Color.parseColor("#4C4C4C"));
//                        //holder.imageView.setImageResource(R.drawable.miscllaneous_white);
//
//                        hor_rlout1.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvConvey.setTextColor(Color.parseColor("leavebal_bg"));
//
//                        hor_rlout3.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvHotel.setTextColor(Color.parseColor("#4C4C4C"));
//
//                        hor_rlout4.setBackgroundResource(R.drawable.card_redbg);
//                        tvMisc.setTextColor(Color.parseColor("#ffffff"));
                        AllusersResultu(ExpenseType);
                       // ExpenseResult();
                    } else {
                        Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.", "");
                        //Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ExpenseType = "all";
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
                        AllusersResultu(ExpenseType);
                    } else {
                        Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.","");
                       // Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }
//                else if(ExpenseType.equals("all"))
//                {
//                    isInternetPresent = cd.isConnectingToInternet();
//                    if (isInternetPresent)
//                    {
//                        dialog = new ProgressDialog(ExpensesNewActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                        dialog.setMessage("Please wait Loading....");
//                        dialog.setTitle("Metal App");
//                        dialog.setCancelable(false);
//                        dialog.show();
//                        AllusersResultu();
//                    }
//                    else
//                    {
//                        Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
//                    }
//                }

            }

            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        expensesMonthResults.clear();

//        expensesMonthResults.add("January "+currentYear);
//        expensesMonthResults.add("February "+currentYear);
//        expensesMonthResults.add("March "+currentYear);
//        expensesMonthResults.add("April "+currentYear);
//        expensesMonthResults.add("May "+currentYear);
//        expensesMonthResults.add("June "+currentYear);
//        expensesMonthResults.add("July "+currentYear);
//        expensesMonthResults.add("August "+currentYear);
//        expensesMonthResults.add("September "+currentYear);
//        expensesMonthResults.add("October "+currentYear);
//        expensesMonthResults.add("November "+currentYear);
//        expensesMonthResults.add("December "+currentYear);

        int start_year = 2019;
        int current_year = Calendar.getInstance().get(Calendar.YEAR);
        int current_month = Calendar.getInstance().get(Calendar.MONTH);

        String[] monthName = {"January ", "February ",
                "March ", "April ", "May ", "June ", "July ",
                "August ", "September ", "October ", "November ",
                "December "};

        Calendar cal = Calendar.getInstance();
        String month = monthName[cal.get(Calendar.MONTH)];

        while (start_year <= current_year) {
            int yy = start_year++;
            //int zz=yy+1;
            if (month.equalsIgnoreCase("January ")) {
                if (month.equalsIgnoreCase("January ") && yy == current_year) {
                    expensesMonthResults.add("January " + String.valueOf(yy));

                } else {
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                    expensesMonthResults.add("November " + String.valueOf(yy));
                    expensesMonthResults.add("December " + String.valueOf(yy));
                }
            } else if (month.equalsIgnoreCase("February ")) {

                if (month.equalsIgnoreCase("February ") && yy == current_year) {
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));

                } else {

                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                    expensesMonthResults.add("November " + String.valueOf(yy));
                    expensesMonthResults.add("December " + String.valueOf(yy));
                    expensesMonthResults.add("January " + String.valueOf(yy));
                }
            } else if (month.equalsIgnoreCase("March ")) {

                if (month.equalsIgnoreCase("March ") && yy == current_year) {
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                } else {

                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                    expensesMonthResults.add("November " + String.valueOf(yy));
                    expensesMonthResults.add("December " + String.valueOf(yy));
                }

            } else if (month.equalsIgnoreCase("April ")) {
                if (month.equalsIgnoreCase("April ") && yy == current_year) {
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                } else {
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                    expensesMonthResults.add("November " + String.valueOf(yy));
                    expensesMonthResults.add("December " + String.valueOf(yy));
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                }
            } else if (month.equalsIgnoreCase("May ")) {
                if (month.equalsIgnoreCase("May ") && yy == current_year) {
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                } else {
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                    expensesMonthResults.add("November " + String.valueOf(yy));
                    expensesMonthResults.add("December " + String.valueOf(yy));
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                }
            } else if (month.equalsIgnoreCase("June ")) {
                if (month.equalsIgnoreCase("June ") && yy == current_year) {
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                } else {
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                    expensesMonthResults.add("November " + String.valueOf(yy));
                    expensesMonthResults.add("December " + String.valueOf(yy));
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                }
            } else if (month.equalsIgnoreCase("July ")) {
                if (month.equalsIgnoreCase("July ") && yy == current_year) {
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                } else {
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                    expensesMonthResults.add("November " + String.valueOf(yy));
                    expensesMonthResults.add("December " + String.valueOf(yy));
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                }
            } else if (month.equalsIgnoreCase("August ")) {
                if (month.equalsIgnoreCase("August ") && yy == current_year) {
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                } else {
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                    expensesMonthResults.add("November " + String.valueOf(yy));
                    expensesMonthResults.add("December " + String.valueOf(yy));
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                }
            } else if (month.equalsIgnoreCase("September ")) {
                if (month.equalsIgnoreCase("September ") && yy == current_year) {
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                } else {
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                    expensesMonthResults.add("November " + String.valueOf(yy));
                    expensesMonthResults.add("December " + String.valueOf(yy));
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                }
            } else if (month.equalsIgnoreCase("October ")) {
                if (month.equalsIgnoreCase("October ") && yy == current_year) {
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                } else {
                    expensesMonthResults.add("October " + String.valueOf(yy));
                    expensesMonthResults.add("November " + String.valueOf(yy));
                    expensesMonthResults.add("December " + String.valueOf(yy));
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                }
            } else if (month.equalsIgnoreCase("November ")) {
                if (month.equalsIgnoreCase("November ") && yy == current_year) {
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                    expensesMonthResults.add("November " + String.valueOf(yy));
                } else {
                    expensesMonthResults.add("November " + String.valueOf(yy));
                    expensesMonthResults.add("December " + String.valueOf(yy));
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                }
            } else if (month.equalsIgnoreCase("December ")) {
                if (month.equalsIgnoreCase("December ") && yy == current_year) {
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                    expensesMonthResults.add("November " + String.valueOf(yy));
                    expensesMonthResults.add("December " + String.valueOf(yy));
                } else {
                    expensesMonthResults.add("December " + String.valueOf(yy));
                    expensesMonthResults.add("January " + String.valueOf(yy));
                    expensesMonthResults.add("February " + String.valueOf(yy));
                    expensesMonthResults.add("March " + String.valueOf(yy));
                    expensesMonthResults.add("April " + String.valueOf(yy));
                    expensesMonthResults.add("May " + String.valueOf(yy));
                    expensesMonthResults.add("June " + String.valueOf(yy));
                    expensesMonthResults.add("July " + String.valueOf(yy));
                    expensesMonthResults.add("August " + String.valueOf(yy));
                    expensesMonthResults.add("September " + String.valueOf(yy));
                    expensesMonthResults.add("October " + String.valueOf(yy));
                    expensesMonthResults.add("November " + String.valueOf(yy));
                }
            }
            Collections.reverse(expensesMonthResults);
            //yearlyResults.add(String.valueOf(yy));
        }

        expensesMonthSpnAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, expensesMonthResults);
        expensesMonthSpnAdapter.setDropDownViewResource(R.layout.spinner_item);
        expensesMonthSpinner.setAdapter(expensesMonthSpnAdapter);

        expensesMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = parent.getItemAtPosition(position).toString();

                ExpenseType = "all";


                if (ExpenseType.equals("Food")) {
                    ExpenseType = "food";
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
//                        hor_rlout2.setBackgroundResource(R.drawable.card_redbg);
//                        tvFood.setTextColor(Color.parseColor("#ffffff"));
//                        //holder.imageView.setImageResource(R.drawable.miscllaneous_white);
//
//                        hor_rlout1.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvConvey.setTextColor(Color.parseColor("#4C4C4C"));
//
//                        hor_rlout3.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvHotel.setTextColor(Color.parseColor("#4C4C4C"));
//
//                        hor_rlout4.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvMisc.setTextColor(Color.parseColor("#4C4C4C"));
                        AllusersResultu(ExpenseType);
                        //  ExpenseResult();
                    } else {
                        Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.","");
                        //  Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } else if (ExpenseType.equals("Travel")) {
                    ExpenseType = "travel";
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
//                        hor_rlout2.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvFood.setTextColor(Color.parseColor("#4C4C4C"));
//                        //holder.imageView.setImageResource(R.drawable.miscllaneous_white);
//
//                        hor_rlout1.setBackgroundResource(R.drawable.card_redbg);
//                        tvConvey.setTextColor(Color.parseColor("#ffffff"));
//
//                        hor_rlout3.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvHotel.setTextColor(Color.parseColor("#4C4C4C"));
//
//                        hor_rlout4.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvMisc.setTextColor(Color.parseColor("#4C4C4C"));
                        AllusersResultu(ExpenseType);
                        //  ExpenseResult();
                    } else {
                        Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.","");
                        //    Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                    }


                } else if (ExpenseType.equals("Hotel")) {
                    ExpenseType = "hotel";
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
//                        hor_rlout2.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvFood.setTextColor(Color.parseColor("#4C4C4C"));
//                        //holder.imageView.setImageResource(R.drawable.miscllaneous_white);
//
//                        hor_rlout1.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvConvey.setTextColor(Color.parseColor("#4C4C4C"));
//
//                        hor_rlout3.setBackgroundResource(R.drawable.card_redbg);
//                        tvHotel.setTextColor(Color.parseColor("#ffffff"));
//
//                        hor_rlout4.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvMisc.setTextColor(Color.parseColor("#4C4C4C"));
                        AllusersResultu(ExpenseType);
                        // ExpenseResult();
                    } else {
                        Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.","");
                        //  Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } else if (ExpenseType.equals("Miscellaneous")) {
                    ExpenseType = "miscs";
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
//                        hor_rlout2.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvFood.setTextColor(Color.parseColor("#4C4C4C"));
//                        //holder.imageView.setImageResource(R.drawable.miscllaneous_white);
//
//                        hor_rlout1.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvConvey.setTextColor(Color.parseColor("leavebal_bg"));
//
//                        hor_rlout3.setBackgroundResource(R.drawable.leavebal_bg);
//                        tvHotel.setTextColor(Color.parseColor("#4C4C4C"));
//
//                        hor_rlout4.setBackgroundResource(R.drawable.card_redbg);
//                        tvMisc.setTextColor(Color.parseColor("#ffffff"));
                        AllusersResultu(ExpenseType);
                        // ExpenseResult();
                    } else {
                        Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.", "");
                        //Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ExpenseType = "all";
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        dialog = new ProgressDialog(ExpensesNewActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage("Please wait Loading....");
                        dialog.setTitle("Metal App");
                        dialog.setCancelable(false);
                        dialog.show();
                        AllusersResultu(ExpenseType);
                        dialog.dismiss();
                    } else {
                        Global_Data.Custom_Toast(ExpensesNewActivity.this, "You don't have internet connection.","");
                        // Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }

                //Toast.makeText(ExpensesNewActivity.this, selectedMonth, Toast.LENGTH_SHORT).show();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {
            //    Toast.makeText(ExpensesNewActivity.this, "no select", Toast.LENGTH_SHORT).show();
                Global_Data.Custom_Toast(ExpensesNewActivity.this, "no select","");
            }
        });

//        isInternetPresent = cd.isConnectingToInternet();
//        if (isInternetPresent)
//        {
//            dialog = new ProgressDialog(ExpensesNewActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//            dialog.setMessage("Please wait Loading....");
//            dialog.setTitle("Metal App");
//            dialog.setCancelable(false);
//            dialog.show();
//            AllusersResultu();
//        }
//        else
//        {
//            Toast.makeText(ExpensesNewActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
//
//        }

    }

    public void AllusersResultu(String type) {
        String domain = getResources().getString(R.string.service_domain);

        SharedPreferences spf = ExpensesNewActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);
        //Log.i("user list url", "user list url " + domain + "expenses/get_expenses_index?email=" + user_email + "&type=" + type+ "&year=" + currentYear+ "&month=" + selectedMonth) ;
        StringRequest jsObjRequest = null;
        String service_url = "";

        //String type=expenseSpinner.getSelectedItem().toString();
        String month1 = expensesMonthSpinner.getSelectedItem().toString();

        String splitData[] = month1.split("\\s", 2);
        selectedMonth = splitData[0];
        selectedYear = splitData[1];

        if (selectedMonth.equalsIgnoreCase("January")) {
            month = "01";
        } else if (selectedMonth.equalsIgnoreCase("February")) {
            month = "02";
        } else if (selectedMonth.equalsIgnoreCase("March")) {
            month = "03";
        } else if (selectedMonth.equalsIgnoreCase("April")) {
            month = "04";
        } else if (selectedMonth.equalsIgnoreCase("May")) {
            month = "05";
        } else if (selectedMonth.equalsIgnoreCase("June")) {
            month = "06";
        } else if (selectedMonth.equalsIgnoreCase("July")) {
            month = "07";
        } else if (selectedMonth.equalsIgnoreCase("August")) {
            month = "08";
        } else if (selectedMonth.equalsIgnoreCase("September")) {
            month = "09";
        } else if (selectedMonth.equalsIgnoreCase("October")) {
            month = "10";
        } else if (selectedMonth.equalsIgnoreCase("November")) {
            month = "11";
        } else if (selectedMonth.equalsIgnoreCase("December")) {
            month = "12";
        }

        service_url = domain + "expenses/get_expenses_index?email=" + user_email + "&type=" + type + "&year=" + selectedYear + "&month=" + month;

        Log.i("volley", "service_url: " + service_url);

        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;
                new ExpensesNewActivity.GetAllUserListn().execute(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Intent intent = new Intent(ExpensesNewActivity.this, MainActivity.class);
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

                if (response_result.equalsIgnoreCase("Expenses Record Not Found.")) {
                    ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();

                            Global_Data.Custom_Toast(ExpensesNewActivity.this, response_result, "Yes");

                            txtClaimAmount.setText("₹ " + "0.0");
                            txtApproved.setText("₹ " + "0.0");
                            txtPending.setText("₹ " + "0.0");

                            tv_con_amount.setText("₹ " + "0.0");
                            tv_foodamount.setText("₹ " + "0.0");
                            tv_hotelamount.setText("₹ " + "0.0");
                            tv_miscamount.setText("₹ " + "0.0");

//                            Intent intent = new Intent(ExpensesNewActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();

                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(final_response);

                                txtClaimAmount.setText("₹ " + jsonObject.getString("total_claimed_amount"));
                                txtApproved.setText("₹ " + jsonObject.getString("approved_amount"));
                                txtPending.setText("₹ " + jsonObject.getString("pending_amount"));

                                tv_con_amount.setText("₹ " + jsonObject.getString("expenses_travels_amount"));
                                tv_foodamount.setText("₹ " + jsonObject.getString("expenses_foods_amount"));
                                tv_hotelamount.setText("₹ " + jsonObject.getString("expenses_hotels_amount"));
                                tv_miscamount.setText("₹ " + jsonObject.getString("expenses_miscs_amount"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

//                    if (jsonObject.length() <= 0) {
//                        ExpensesNewActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//                                dialog.dismiss();
//
//                                Toast toast = Toast.makeText(ExpensesNewActivity.this, "Record doesn't exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
////                                Intent intent = new Intent(OrderAnalysisActivity.this, OrderAnalysisActivity.class);
////                                startActivity(intent);
////                                finish();
//                            }
//                        });
//                    }
//                    else {
//
//                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Intent intent = new Intent(ExpensesNewActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }
            ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });
            return "Executed";

        }

        @Override
        protected void onPostExecute(String result) {
            ExpensesNewActivity.this.runOnUiThread(new Runnable() {
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


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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

        // toggle nav drawer on selecting action bar app icon/title
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.add:
                String targetNew = "";
                SharedPreferences sp = ExpensesNewActivity.this.getSharedPreferences("SimpleLogic", 0);
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();

        Intent i = new Intent(ExpensesNewActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i);
        finish();
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etExpenseDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void reload() {
        SettingsManager settings = SettingsManager.getInstance();
        swipeListView.setSwipeMode(settings.getSwipeMode());
        swipeListView.setSwipeActionLeft(settings.getSwipeActionLeft());
        swipeListView.setSwipeActionRight(settings.getSwipeActionRight());
        swipeListView.setOffsetLeft(convertDpToPixel(settings.getSwipeOffsetLeft()));
        swipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
        swipeListView.setAnimationTime(settings.getSwipeAnimationTime());
        swipeListView.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
    }


    public void ExpenseResult() {
        String domain = getResources().getString(R.string.service_domain);

        SharedPreferences spf = ExpensesNewActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);
        Log.i("volley", "domain: " + domain);

        StringRequest jsObjRequest = null;
        String service_url = "";
        String type = expenseSpinner.getSelectedItem().toString();
        if (type.equalsIgnoreCase("All")) {
            type = "all";
        } else if (type.equalsIgnoreCase("Travel")) {
            type = "travel";
        } else if (type.equalsIgnoreCase("Food")) {
            type = "food";
        } else if (type.equalsIgnoreCase("Hotel")) {
            type = "hotel";
        } else if (type.equalsIgnoreCase("Miscellaneous")) {
            type = "miscs";
        }

        String month1 = expensesMonthSpinner.getSelectedItem().toString();

        String splitData[] = month1.split("\\s", 2);
        selectedMonth = splitData[0];
        selectedYear = splitData[1];

        if (selectedMonth.equalsIgnoreCase("January")) {
            month = "01";
        } else if (selectedMonth.equalsIgnoreCase("February")) {
            month = "02";
        } else if (selectedMonth.equalsIgnoreCase("March")) {
            month = "03";
        } else if (selectedMonth.equalsIgnoreCase("April")) {
            month = "04";
        } else if (selectedMonth.equalsIgnoreCase("May")) {
            month = "05";
        } else if (selectedMonth.equalsIgnoreCase("June")) {
            month = "06";
        } else if (selectedMonth.equalsIgnoreCase("July")) {
            month = "07";
        } else if (selectedMonth.equalsIgnoreCase("August")) {
            month = "08";
        } else if (selectedMonth.equalsIgnoreCase("September")) {
            month = "09";
        } else if (selectedMonth.equalsIgnoreCase("October")) {
            month = "10";
        } else if (selectedMonth.equalsIgnoreCase("November")) {
            month = "11";
        } else if (selectedMonth.equalsIgnoreCase("December")) {
            month = "12";
        }

        service_url = domain + "expenses/get_expenses_index?email=" + user_email + "&type=" + type + "&year=" + selectedYear + "&month=" + month;

        //service_url = domain + "expenses/get_expenses_index?email=" + user_email + "&type=" + type+ "&year=" + "2020"+ "&month=" + "01";

        Log.i("volley", "service_url: " + service_url);

        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;

                new ExpenseTask().execute();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Intent intent = new Intent(ExpensesNewActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Network Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data. Custom_Toast(getApplicationContext(),
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
                            Global_Data.Custom_Toast(getApplicationContext(),"Network   Error","");
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


    private class ExpenseTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {
            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("result")) {
                    response_result = response.getString("result");

                } else {
                    response_result = "data";
                }

                if (response_result.equalsIgnoreCase("Expenses Record Not Found.")) {

                    ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            listOfficelistInfo.hideShimmerAdapter();

//                            Toast toast = Toast.makeText(ExpensesNewActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            dialog.dismiss();
                            Global_Data.Custom_Toast(ExpensesNewActivity.this, response_result, "yes");
//                            Intent intent = new Intent(ExpensesNewActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    });

                } else if (response_result.equalsIgnoreCase("User Not Found")) {
                    ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            listOfficelistInfo.hideShimmerAdapter();
                            dialog.dismiss();
//                            Toast toast = Toast.makeText(ExpensesNewActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpensesNewActivity.this, response_result,"yes");

//                            Intent intent = new Intent(ExpensesNewActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    });
                } else {
                    JSONArray expenses_foods = response.getJSONArray("expenses_foods");
                    JSONArray expenses_hotels = response.getJSONArray("expenses_hotels");
                    JSONArray expenses_miscs = response.getJSONArray("expenses_miscs");
                    JSONArray expenses_travels = response.getJSONArray("expenses_travels");

                    Log.i("volley", "response orders Length: " + expenses_foods.length());
                    Log.d("volley", "orders" + expenses_foods.toString());

//                    if (expenses_foods.length() <= 0) {
//
//                        ExpensesNewActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//
//                                listOfficelistInfo.hideShimmerAdapter();
//                                Toast toast = Toast.makeText(ExpensesNewActivity.this, "Record not exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//
//                                Intent intent = new Intent(ExpensesNewActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        });
//                    } else {
                    officelistinfo_list.clear();
                    if (Global_Data.ExpenseName.equalsIgnoreCase("Food")) {
                        if (expenses_foods.length() == 0) {
                            ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                                public void run() {

                                    listOfficelistInfo.hideShimmerAdapter();
//                                    Toast toast = Toast.makeText(ExpensesNewActivity.this, "Record not exist", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(ExpensesNewActivity.this, "Record not exist","yes");
                                    dialog.dismiss();
//                                        Intent intent = new Intent(ExpensesNewActivity.this, MainActivity.class);
//                                        startActivity(intent);
//                                        finish();
                                }
                            });
                        } else {
                            for (int i = 0; i < expenses_foods.length(); i++) {
                                JSONObject jsonObject = expenses_foods.getJSONObject(i);
                                String create= jsonObject.getString("created_date");

                                officelistinfo_list.add(new ExpensesModel(jsonObject.getString("id").trim(), jsonObject.getString("amount").trim(), jsonObject.getString("approved_amount").trim(), jsonObject.getString("hotel_name").trim(), jsonObject.getString("status").trim(), create.toString().trim()));

                            }
                            ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();
//                                    Intent intent = new Intent(ExpensesNewActivity.this, OrderAnalysisActivity.class);
//                                    startActivity(intent);
//                                    finish();
                                }
                            });
                        }
                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Conveyance")) {
                        if (expenses_travels.length() == 0) {
                            ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                                public void run() {

                                    listOfficelistInfo.hideShimmerAdapter();
//                                    Toast toast = Toast.makeText(ExpensesNewActivity.this, "Record not exist", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(ExpensesNewActivity.this, "Record not exist","yes");
                                    dialog.dismiss();
//                                        Intent intent = new Intent(ExpensesNewActivity.this, MainActivity.class);
//                                        startActivity(intent);
//                                        finish();
                                }
                            });
                        } else {

                            for (int i = 0; i < expenses_travels.length(); i++) {
                                JSONObject jsonObject = expenses_travels.getJSONObject(i);
                                String create= jsonObject.getString("created_date");

                                officelistinfo_list.add(new ExpensesModel(jsonObject.getString("id"), jsonObject.getString("amount"), jsonObject.getString("approved_amount"), jsonObject.getString("travel_mode"), jsonObject.getString("status"), create.toString()));
                            }

                            ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();

                                }
                            });
                        }

                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Hotel")) {
                        if (expenses_hotels.length() == 0) {
                            ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    listOfficelistInfo.hideShimmerAdapter();
//                                    Toast toast = Toast.makeText(ExpensesNewActivity.this, "Record not exist", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(ExpensesNewActivity.this, "Record not exist","yes");
                                    dialog.dismiss();
//
//                                        Intent intent = new Intent(ExpensesNewActivity.this, MainActivity.class);
//                                        startActivity(intent);
//                                        finish();
                                }
                            });
                        } else {

                            for (int i = 0; i < expenses_hotels.length(); i++) {
                                JSONObject jsonObject = expenses_hotels.getJSONObject(i);
                                String create= jsonObject.getString("created_date");

                                officelistinfo_list.add(new ExpensesModel(jsonObject.getString("id"), jsonObject.getString("amount"), jsonObject.getString("approved_amount"), jsonObject.getString("hotel_name"), jsonObject.getString("status"), create.toString()));

                            }

                            ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();

                                }
                            });
                        }

                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Miscellaneous")) {

                        if (expenses_miscs.length() == 0) {
                            ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                                public void run() {

                                    listOfficelistInfo.hideShimmerAdapter();
//                                    Toast toast = Toast.makeText(ExpensesNewActivity.this, "Record not exist", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(ExpensesNewActivity.this, "Record not exist","yes");
                                    dialog.dismiss();
//                                        Intent intent = new Intent(ExpensesNewActivity.this, MainActivity.class);
//                                        startActivity(intent);
//                                        finish();
                                }
                            });
                        } else {

                            for (int i = 0; i < expenses_miscs.length(); i++) {
                                JSONObject jsonObject = expenses_miscs.getJSONObject(i);
                                String create= jsonObject.getString("created_date");

                                officelistinfo_list.add(new ExpensesModel(jsonObject.getString("id"), jsonObject.getString("amount"), jsonObject.getString("approved_amount"), jsonObject.getString("description"), jsonObject.getString("status"), create.toString()));
                            }

                            ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                    ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            listOfficelistInfo.hideShimmerAdapter();
                            expensesHorizontalAdapter.notifyDataSetChanged();
                        }
                    });

                    ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            listOfficelistInfo.hideShimmerAdapter();
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Intent intent = new Intent(ExpensesNewActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        listOfficelistInfo.hideShimmerAdapter();
                    }
                });
            }

            ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    listOfficelistInfo.hideShimmerAdapter();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            ExpensesNewActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    listOfficelistInfo.hideShimmerAdapter();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
