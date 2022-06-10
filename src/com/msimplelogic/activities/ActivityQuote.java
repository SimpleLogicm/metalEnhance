package com.msimplelogic.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.msimplelogic.adapter.AdapterQuote;
import com.msimplelogic.adapter.OnlineSchemeAdapter;
import com.msimplelogic.animation.ActivitySwitcher;
import com.msimplelogic.model.Catalogue_model;
import com.msimplelogic.model.OnlineSchemeModel;
import com.msimplelogic.model.Product;
import com.msimplelogic.services.getServices;
import com.msimplelogic.swipelistview.BaseSwipeListViewListener;
import com.msimplelogic.swipelistview.SwipeListView;
import com.msimplelogic.swipelistview.sample.utils.SettingsManager;
import com.msimplelogic.webservice.ConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.simplelogic.helper.Catalogue_slider_caller;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class ActivityQuote extends BaseActivity implements Catalogue_slider_caller {
    String str;
    Double pp = 0.0;
    String ttl_price;
    private OnlineSchemeAdapter mAdapter;
    private ArrayList<OnlineSchemeModel> catalogue_m1;
    double expectAmt,totalAmt;
    Double sum = 0.0;
    private RecyclerView moreDealRecyclerView;
    //String orderListStatus;
    private ProgressDialog pDialog;
    private ArrayList<Catalogue_model> catalogue_m;
    HashMap<String, String> map;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ArrayList<HashMap<String, String>> SwipeList;
    ArrayList<String> Amount_tp = new ArrayList<String>();
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    private static final int REQUEST_CODE_SETTINGS = 0;
    private ArrayList<String> Distributer_list = new ArrayList<String>();
    private AdapterQuote adapter;
    private ArrayList<Product> dataOrder;
    LoginDataBaseAdapter loginDataBaseAdapter;
  //  private SwipeListView swipeListView;
    private ListView swipeListView;
    LinearLayout expectedLout;
    TextView textView1, tabletextview1, tabletextview2, tabletextview3;
    static TextView txttotalPreview,txttotalPreview1;
    static final String TAG_ITEMNAME = "product_name";
    static final String TAG_QTY = "total_qty";
    static final String TAG_PRICE = "MRP";
    static final String TAG_AMOUNT = "amount";
    static final String TAG_ITEM_NUMBER = "item_number";
    static final String TAG_PRODUCT_IMAGE = "product_image";
    static final String TAG_MIN_QTY = "product_min_qty";
    static final String TAG_PKG_QTY = "product_pkg_qty";
    static final String TAG_ITEM_SCHEME = "product_scheme";
    static final String TAG_ITEM_DETAIL1 = "detail1";
    static final String TAG_ITEM_DETAIL2 = "detail2";
    static final String TAG_ITEM_DETAIL3 = "detail3";
    static final String TAG_ITEM_DETAIL4 = "detail4";
    static final String TAG_ITEM_DETAIL5 = "detail5";
    static final String TAG_ITEM_DETAIL6 = "detail6";
    SharedPreferences sp;
    //Button buttonShowScheme;
    String expectedAmt="";
    ImageView imgView,buttonPreviewAddMOre1,buttonPreviewCheckout;
    TextView buttonPreviewAddMOre,buttonPreviewHome;
    static String price_str;
    static float totalPrice;
    String statusOrderActivity = "";
    Button buttonPreviewCheckout1, buttonPreviewHome1, btn_subapprove;
    ArrayAdapter<String> dataDistrubutorsAdapter;
    boolean firstLaunch = false;
    static String c_Total = "";
    EditText expectedAmount;
    Toolbar toolbar;
    TextView viewMoreDealBtn,checkoutBtn;
    ImageView hedder_theame;

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        cd = new ConnectionDetector(getApplicationContext());
        txttotalPreview = (TextView) findViewById(R.id.txttotalPreview);
        //txttotalPreview1 = (TextView) findViewById(R.id.txttotalPreview1);
        swipeListView = (ListView) findViewById(R.id.example_lv_list);
        hedder_theame =  findViewById(R.id.hedder_theame);

        map = new HashMap<String, String>();

        SwipeList = new ArrayList<HashMap<String, String>>();
        btn_subapprove = (Button) findViewById(R.id.btn_subapprove);

        //buttonShowScheme = (Button) findViewById(R.id.buttonShowScheme);
        //buttonPreviewAddMOre = (ImageView) findViewById(R.id.buttonPreviewAddMOre);
        buttonPreviewAddMOre = (TextView) findViewById(R.id.buttonPreviewAddMOre);
        //buttonPreviewHome = (Button) findViewById(R.id.buttonPreviewHome);
        buttonPreviewHome = (TextView) findViewById(R.id.buttonPreviewHome);
        expectedAmount = (EditText) findViewById(R.id.expected_amount);
        //expectedLout = (LinearLayout) findViewById(R.id.expected_lout);

        SharedPreferences spf2 = this.getSharedPreferences("SimpleLogic", 0);
        ttl_price = spf2.getString("var_total_price", "");
        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);

        }

        catalogue_m = new ArrayList<>();
        pDialog = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        // for label RP change
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        String rpstr = spf1.getString("var_rp", "");
        String mrpstr = spf1.getString("var_mrp", "");
        price_str = spf1.getString("var_total_price", "");
        c_Total = getResources().getString(R.string.CTotal);

        btn_subapprove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    if (expectedAmount.getText().toString().equalsIgnoreCase("")) {
                        Global_Data.Custom_Toast(ActivityQuote.this,"Please Enter Expected Amount","yes");
                    }

                    else if(TextUtils.isEmpty(expectedAmount.getText().toString()))
                    {
                        getServices.SYNCORDER_BYCustomerINSTI(ActivityQuote.this, expectedAmount.getText().toString().trim());
                    }
                    else{
                        expectAmt = new Double(expectedAmount.getText().toString());
                        totalAmt = new Double(txttotalPreview.getText().toString());
                        //totalAmt = new Double(txttotalPreview1.getText().toString());

                        if(expectAmt>totalAmt)
                        {
                            Global_Data.Custom_Toast(getApplicationContext(),"Expected Amount Should be less than Total amount","");
                         //   Toast.makeText(getApplicationContext(),"Expected Amount Should be less than Total amount",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Global_Data.expectedAmountAtCreate=expectedAmount.getText().toString().trim();
                            getServices.SYNCORDER_BYCustomerINSTI(ActivityQuote.this, expectedAmount.getText().toString().trim());
                        }
                    }

//                    if(expectedAmount.getText().toString().length()>0)
//                    {
//                        w = new Double(expectedAmount.getText().toString());
//                        if(w>sum)
//                        {
//                            getServices.SYNCORDER_BYCustomerINSTI(PreviewOrderSwipeActivity.this, expectedAmount.getText().toString().trim());
//                        }else{
//                            Toast.makeText(getApplicationContext(),"Expected Amount Should greater than Total amount",Toast.LENGTH_LONG).show();
//                        }
//
//                    }else{
//                        getServices.SYNCORDER_BYCustomerINSTI(PreviewOrderSwipeActivity.this, expectedAmount.getText().toString().trim());
//                    }

                } else {
                    // Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();

                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.internet_connection_error),"yes");
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                }
            }
        });

//        buttonPreviewCheckout = (ImageView) findViewById(R.id.buttonPreviewCheckout);
//        buttonPreviewCheckout1 = (Button) findViewById(R.id.buttonPreviewCheckout1);

        if (Global_Data.sales_btnstring.equalsIgnoreCase("Institutional Sales")) {
            buttonPreviewHome.getLayoutParams().width= ViewGroup.LayoutParams.WRAP_CONTENT;
            btn_subapprove.setVisibility(View.VISIBLE);
//            buttonPreviewCheckout.setVisibility(View.GONE);
//            buttonPreviewCheckout1.setVisibility(View.GONE);
            //buttonShowScheme.setVisibility(View.GONE);
            //expectedLout.setVisibility(View.VISIBLE);
            //buttonPreviewCheckout.setEnabled(false);

            expectedAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    //adapter.getFilter().filter(cs);

//                    try {
//
//                    if((Double.parseDouble(String.valueOf(cs)))>sum)
//                    {
//                        w = new Double(expectedAmount.getText().toString());
//                    }
//                    else{
//                        Toast.makeText(getApplicationContext(),"Expected Amount Should greater than Total amount",Toast.LENGTH_LONG).show();
//                    }
//
//                    } catch (NumberFormatException e) {
//                        w = 0; // your default value
//                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    //Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
                }
//
//                @Override
//                public void afterTextChanged(Editable arg0) {
//                    //Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();
//                }
            });

        } else {

        }

        Intent i = this.getIntent();
        Global_Data.orderListStatus = i.getStringExtra("TA_ORDERLIST_STATUS");
        String name = i.getStringExtra("retialer");

        if (i.hasExtra("return")) {
            statusOrderActivity = "return";
        } else if (i.hasExtra("new")) {
            statusOrderActivity = "new";
        } else if (i.hasExtra("previous")) {
            statusOrderActivity = "previous";
        }

        if((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(Global_Data.orderListStatus)).equalsIgnoreCase("orderlist_status"))
        {
            Global_Data.PREVIOUS_ORDER_BACK_FLAG=Global_Data.orderListStatus;
            buttonPreviewAddMOre.setVisibility(View.INVISIBLE);
           // buttonPreviewCheckout.setVisibility(View.GONE);
            //buttonShowScheme.setVisibility(View.INVISIBLE);
            buttonPreviewHome.setVisibility(View.INVISIBLE);

            HashMap<String, String> mapp = new HashMap<String, String>();
            mapp.put(TAG_ITEMNAME, i.getStringExtra("PRODUCT_NAME"));
            mapp.put(TAG_QTY, i.getStringExtra("TOTAL_QTY"));
            mapp.put(TAG_PRICE, i.getStringExtra("MRP"));
            //mapp.put(TAG_ITEM_NUMBER, i.getStringExtra("TA_ORDERLIST_STATUS"));

            //Log.d("ITEM_NUMBER N", "ITEM_NUMBER N" + cnt1.get_category_ids());
            //str += i.getStringExtra("MRP");
            Amount_tp.add(i.getStringExtra("AMOUNT"));
            //txttotalPreview.setText("Total		:		"+cnt1.getAmount());
            //Toast.makeText(NewOrderActivity.this, "Login:"+Global_Data.order_id,Toast.LENGTH_SHORT).show();
            SwipeList.add(mapp);


            for (int m = 0; m < Amount_tp.size(); m++) {
                sum += Double.valueOf(Amount_tp.get(m));
            }
            updateSum(sum);
        }else if((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(Global_Data.orderListStatus)).equalsIgnoreCase("orderlist_status"))
        {

        } else {
            try {
                List<Local_Data> cont1 = dbvoc.getItemName(Global_Data.GLObalOrder_id);
                for (Local_Data cnt1 : cont1) {
                    HashMap<String, String> mapp = new HashMap<String, String>();
                    mapp.put(TAG_ITEMNAME, cnt1.getProduct_nm());
                    mapp.put(TAG_QTY, cnt1.getQty());
                    mapp.put(TAG_PRICE, cnt1.getPrice());
                    mapp.put(TAG_ITEM_NUMBER, cnt1.get_category_ids());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnt1.get_mediaUrl()) && !cnt1.get_mediaUrl().equalsIgnoreCase("not found")) {
                        mapp.put(TAG_PRODUCT_IMAGE, cnt1.get_mediaUrl());
                    } else {
                        mapp.put(TAG_PRODUCT_IMAGE, "");
                    }

                    try {
                        List<Local_Data> contactsn1 = dbvoc.GetSchemeByCode(cnt1.get_category_ids());
                        if (contactsn1.size() > 0) {
                            for (Local_Data cn : contactsn1) {
                                mapp.put(TAG_ITEM_SCHEME, cn.getDisplayName());
                                // list3.add("SCHEME : " + cn.getDisplayName());
                            }
                        } else {
                            mapp.put(TAG_ITEM_SCHEME, "");
                        }

                        List<Local_Data> contactsn2 = dbvoc.getProductBymin_cat(cnt1.get_category_ids());
                        if (contactsn2.size() > 0) {
                            for (Local_Data cn : contactsn1) {
                                mapp.put(TAG_MIN_QTY, (cn.getMinqty()));
                                mapp.put(TAG_PKG_QTY, (cn.getPkgqty()));
                                // list3.add("SCHEME : " + cn.getDisplayName());
                            }
                        } else {
                            mapp.put(TAG_MIN_QTY, (""));
                            mapp.put(TAG_PKG_QTY, (""));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.d("ITEM_NUMBER N", "ITEM_NUMBER N" + cnt1.get_category_ids());
                    str += cnt1.getAmount();
                    Amount_tp.add(cnt1.getAmount());
                    //txttotalPreview.setText("Total		:		"+cnt1.getAmount());
                    //Toast.makeText(NewOrderActivity.this, "Login:"+Global_Data.order_id,Toast.LENGTH_SHORT).show();
                    SwipeList.add(mapp);
                }

                // }
                Double sum = 0.0;
                for (int m = 0; m < Amount_tp.size(); m++) {
                    sum += Double.valueOf(Amount_tp.get(m));
                }
                updateSum(sum);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

//		List<Local_Data> swipedata = dbvoc.getAportunityBy_opid(cn.geopportunity_id());
//        for (Local_Data swipe : swipedata) {
//            map.put(TAG_OPPORTUNITY, swipe.getdescription());
//            map.put(TAG_PRODUCTID, swipe.getPRODUCT_ID());
//            map.put(TAG_VALUE, swipe.getexpected_value());
//           // total =total+Integer.parseInt(swipe.getexpected_value());
//        }

		/*ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#8A0808")));
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle(name);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		*/
        Global_Data.GLOVEL_LONG_DESC = "";
        Global_Data.GLOVEL_CATEGORY_SELECTION = "";
        Global_Data.GLOVEL_ITEM_MRP = "";
        //Global_Data.productList.clear();

        dataOrder = i.getParcelableArrayListExtra("productsList");
        adapter = new AdapterQuote(ActivityQuote.this, SwipeList);
        totalPrice = 0.00f;

//        if (Build.VERSION.SDK_INT >= 11) {
//            swipeListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        }

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

        reload();

//        buttonPreviewCheckout.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View b, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    //up event
//                    //b.setBackgroundColor(Color.parseColor("#414042"));
//                    return true;
//                }
//
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    firstLaunch = true;
//
//                    Intent intent = new Intent(getApplicationContext(), CaptureSignature.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    startActivity(intent);
//
//                    return true;
//                }
//                return false;
//            }
//        });


        buttonPreviewHome.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View b, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event

                    // b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    //b.setBackgroundColor(Color.parseColor("#910505"));
                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityQuote.this).create(); //Read Update
                    alertDialog.setTitle(getResources().getString(R.string.Warning));
                    alertDialog.setMessage(getResources().getString(R.string.Dialog_cancel_alert_message));
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // create a instance of SQLite Database
                            dbvoc = new DataBaseHelper(ActivityQuote.this);
                            dbvoc.getDeleteTableorder_byOID(Global_Data.GLObalOrder_id);
                            dbvoc.getDeleteTableorderproduct_byOID(Global_Data.GLObalOrder_id);
                            Global_Data.GLOvel_GORDER_ID = "";
                            //Intent order_home = new Intent(getApplicationContext(), Order.class);
                            Intent order_home = new Intent(getApplicationContext(), Sales_Dash.class);
                            startActivity(order_home);
                            finish();
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }
                    });

                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();

                        }
                    });

                    alertDialog.show();

                    return true;
                }
                return false;
            }
        });

        //buttonPreviewCheckout.setBackgroundColor(Color.parseColor("#414042"));
        //buttonPreviewHome.setBackgroundColor(Color.parseColor("#414042"));
        //buttonPreviewAddMOre.setBackgroundColor(Color.parseColor("#414042"));
        buttonPreviewAddMOre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.setBackgroundColor(Color.parseColor("#910505"));
//				if(statusOrderActivity.equalsIgnoreCase("previous")){
                final Intent i = new Intent(ActivityQuote.this, NewOrderActivity.class);
                i.putExtra("data", "data");
                i.putParcelableArrayListExtra("productsList", dataOrder);

                // Log.e("DATA", "Starting out NewOrderFragment...");
                SharedPreferences sp = ActivityQuote.this.getSharedPreferences("SimpleLogic", 0);

                i.putExtra("retialer", "" + sp.getString("RetailerName", ""));
                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //finish();
                //this.startActivity(i);

                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                ActivitySwitcher.animationOut(findViewById(R.id.containerPreview), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
                    @Override
                    public void onAnimationFinished() {
                        //Log.e("DATA", "Starting in NewOrderFragment...");
                        startActivity(i);
                        finish();
                    }
                });
//				}
//				else {
//					onBackPressed();
//				}
            }
        });
    }

    private void reload() {
        SettingsManager settings = SettingsManager.getInstance();
//        swipeListView.setSwipeMode(settings.getSwipeMode());
//        swipeListView.setSwipeActionLeft(settings.getSwipeActionLeft());
//        swipeListView.setSwipeActionRight(settings.getSwipeActionRight());
//        swipeListView.setOffsetLeft(convertDpToPixel(settings.getSwipeOffsetLeft()));
//        swipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
//        swipeListView.setAnimationTime(settings.getSwipeAnimationTime());
        //swipeListView.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
    }

    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    public boolean onOptionsItemSelected (int featureId, MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            //finish();
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SETTINGS:
                reload();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstLaunch) {

        }
        SharedPreferences spf = ActivityQuote.this.getSharedPreferences("SimpleLogic", 0);
        if (spf.getInt("Capture", 0) == 1) {
            SharedPreferences.Editor editor = spf.edit();
            editor.putInt("Capture", 2);
            editor.commit();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//           case android.R.id.home:
//                //this.finish();
//                onBackPressed();
//                Global_Data.CatlogueStatus = "";
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
                SharedPreferences sp = ActivityQuote.this.getSharedPreferences("SimpleLogic", 0);
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
        //super.onBackPressed();

        if (Global_Data.PREVIOUS_ORDER_BACK_FLAG.equalsIgnoreCase("TRUE")) {
            Global_Data.CatlogueStatus = "";
            final Intent i = new Intent(getApplicationContext(), Previous_Order.class);
            i.putExtra("data", "data");
            // i.putParcelableArrayListExtra("productsList", dataOrder);

            //SharedPreferences  sp=PreviewOrderSwipeActivity.this.getSharedPreferences("SimpleLogic", 0);

            //i.putExtra("retialer", ""+sp.getString("RetailerName", ""));
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //finish();
            //this.startActivity(i);

            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            ActivitySwitcher.animationOut(findViewById(R.id.containerPreview), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    startActivity(i);
                    finish();
                }
            });
        } else if (Global_Data.PREVIOUS_ORDER_BACK_FLAG.equalsIgnoreCase("")) {
            Global_Data.CatlogueStatus = "";

            final Intent i = new Intent(ActivityQuote.this, NewOrderActivity.class);
            i.putExtra("data", "data");
            i.putParcelableArrayListExtra("productsList", dataOrder);
            // Log.e("DATA", "Starting out NewOrderFragment...");
            SharedPreferences sp = ActivityQuote.this.getSharedPreferences("SimpleLogic", 0);
            i.putExtra("retialer", "" + sp.getString("RetailerName", ""));
            //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //finish();
            //this.startActivity(i);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            ActivitySwitcher.animationOut(findViewById(R.id.containerPreview), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    //Log.e("DATA", "Starting in NewOrderFragment...");
                    startActivity(i);
                    finish();
                }
            });
        } else if (statusOrderActivity.equalsIgnoreCase("previous")) {
            AlertDialog alertDialog = new AlertDialog.Builder(ActivityQuote.this).create(); //Read Update
            alertDialog.setTitle(getResources().getString(R.string.Warning));
            alertDialog.setMessage(getResources().getString(R.string.Dialog_cancel_alert_message));
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Global_Data.CatlogueStatus = "";
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                }
            });

            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                }
            });
            alertDialog.show();
        }else if(Global_Data.PREVIOUS_ORDER_BACK_FLAG.equalsIgnoreCase("orderlist_status")){
            Global_Data.PREVIOUS_ORDER_BACK_FLAG = "";
            final Intent i = new Intent(getApplicationContext(), TargetAnalysisActivity.class);
            i.putExtra("data", "data");

            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            ActivitySwitcher.animationOut(findViewById(R.id.containerPreview), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    startActivity(i);
                    finish();
                }
            });
        }
    }

    public static void updateSum(Double sum) {
        if (price_str.length() > 0) {
            txttotalPreview.setText(""+sum);
            //txttotalPreview.setText(price_str + "           " + sum);
            //txttotalPreview1.setText(""+sum);
        } else {
            txttotalPreview.setText(""+sum);
            //txttotalPreview.setText(c_Total + "           " + sum);
            txttotalPreview1.setText(""+sum);
        }
        //txttotalPreview.setText("Total		:		"+sum);
    }


    @Override
    public void slideCall(int po) {

    }
}
