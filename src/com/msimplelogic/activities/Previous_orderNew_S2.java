package com.msimplelogic.activities;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.msimplelogic.activities.R;
import com.msimplelogic.activities.kotlinFiles.Favourite;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.activities.kotlinFiles.Smart_Order;
import com.msimplelogic.animation.ActivitySwitcher;
import com.msimplelogic.model.Product;
import com.msimplelogic.services.getServices;
import com.msimplelogic.swipelistview.BaseSwipeListViewListener;
import com.msimplelogic.swipelistview.SwipeListView;
import com.msimplelogic.swipelistview.sample.adapters.Previous_orderEdit_Adapter;
import com.msimplelogic.swipelistview.sample.utils.SettingsManager;
import com.msimplelogic.webservice.ConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class Previous_orderNew_S2 extends BaseActivity {
    String str;
    static String price_str;
    HashMap<String, String> map;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    static final String TAG_MIN_QTY = "product_min_qty";
    static final String TAG_PKG_QTY = "product_pkg_qty";
    static final String TAG_ITEM_SCHEME = "product_scheme";
    static final String TAG_PRODUCT_IMAGE = "product_image";
    ArrayList<HashMap<String, String>> SwipeList;
    ArrayList<String> Amount_tp = new ArrayList<String>();
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    private static final int REQUEST_CODE_SETTINGS = 0;
    private ArrayList<String> Distributer_list = new ArrayList<String>();
    private Previous_orderEdit_Adapter adapter;
    private ArrayList<Product> dataOrder;
    LoginDataBaseAdapter loginDataBaseAdapter;
    private SwipeListView swipeListView;
    TextView textView1, tabletextview1, tabletextview2, tabletextview3, txt_checkout;
    static TextView txttotalPreview;
    static final String TAG_ITEMNAME = "product_name";
    static final String TAG_QTY = "total_qty";
    static final String TAG_PRICE = "MRP";
    static final String TAG_AMOUNT = "amount";
    static final String TAG_ITEM_NUMBER = "item_number";
    ImageView imgView, buttonPreviewCheckout;
    static float totalPrice;
    String statusOrderActivity = "";
    Button buttonPreviewCheckout1, btn_subapprove;
    ArrayAdapter<String> dataDistrubutorsAdapter;
    TextView buttonPreviewAddMOre, buttonPreviewHome;
    boolean firstLaunch = false;
    static String c_Total = "";
    Toolbar toolbar;
    TextView order_toolbar_title;
    String intentval = "";
    SharedPreferences sp;
    ImageView hedder_theame;

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_order);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cd = new ConnectionDetector(getApplicationContext());
        txttotalPreview = (TextView) findViewById(R.id.txttotalPreview);
        swipeListView = (SwipeListView) findViewById(R.id.example_lv_list);
        swipeListView.setNestedScrollingEnabled(true);
        map = new HashMap<String, String>();
        txt_checkout = findViewById(R.id.txt_checkout);
        hedder_theame = findViewById(R.id.hedder_theame);
        order_toolbar_title = findViewById(R.id.order_toolbar_title);


        // for label RP change

        // = getIntent().getExtras().get("int");
        if (Global_Data.intentvalue=="smart"&& Global_Data.intentvalue!=""){
            order_toolbar_title.setText(getResources().getString(R.string.Smart_Order));
            setTitle("");
            //txttotalPreview.setText("" + Global_Data.totalsumsmartorder);
            //  txttotalPreview.setText(Global_Data.amount);
        }
        else {
            order_toolbar_title.setText(getResources().getString(R.string.Previous_Order));
        }



        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);
        }


        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        String rpstr = spf1.getString("var_rp", "");
        String mrpstr = spf1.getString("var_mrp", "");
        price_str = spf1.getString("var_total_price", "");

        c_Total = getResources().getString(R.string.CTotal);

        SwipeList = new ArrayList<HashMap<String, String>>();
        btn_subapprove = (Button) findViewById(R.id.btn_subapprove);

        btn_subapprove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    getServices.SYNCORDER_BYCustomerINSTI(Previous_orderNew_S2.this, "");
                } else {
                    // Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();

//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.internet_connection_error),"yes");
                }
            }
        });

        buttonPreviewCheckout = (ImageView) findViewById(R.id.buttonPreviewCheckout);
        buttonPreviewCheckout1 = (Button) findViewById(R.id.buttonPreviewCheckout1);

        if (Global_Data.sales_btnstring.equalsIgnoreCase("Institutional Sales")) {
            btn_subapprove.setVisibility(View.VISIBLE);
            buttonPreviewCheckout.setVisibility(View.GONE);
            buttonPreviewCheckout1.setVisibility(View.VISIBLE);
            //buttonPreviewCheckout.setEnabled(false);
        } else {

        }

        Intent i = this.getIntent();

        String name = i.getStringExtra("retialer");

        if (i.hasExtra("return")) {
            statusOrderActivity = "return";
        } else if (i.hasExtra("new")) {
            statusOrderActivity = "new";
        } else if (i.hasExtra("previous")) {
            statusOrderActivity = "previous";
        }


        if (Global_Data.Previous_Order_ServiceOrder_ID.equalsIgnoreCase(Global_Data.Previous_Order_UpdateOrder_ID)) {
            List<Local_Data> cont1 = dbvoc.getItemNamePrevious_Order(Global_Data.Previous_Order_ServiceOrder_ID);
            for (Local_Data cnt1 : cont1) {
                HashMap<String, String> mapp = new HashMap<String, String>();
                mapp.put(TAG_ITEMNAME, cnt1.getProduct_nm());
                mapp.put(TAG_QTY, cnt1.getQty());
                mapp.put(TAG_PRICE, cnt1.getPrice());
                mapp.put(TAG_ITEM_NUMBER, cnt1.get_category_ids());
                Log.d("ITEM_NUMBER N", "ITEM_NUMBER N" + cnt1.get_category_ids());
                str += cnt1.getAmount();
                Amount_tp.add(cnt1.getAmount());

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
                //txttotalPreview.setText("Total		:		"+cnt1.getAmount());
                //Toast.makeText(NewOrderActivity.this, "Login:"+Global_Data.order_id,Toast.LENGTH_SHORT).show();
                SwipeList.add(mapp);
            }
            Double sum = 0.0;
            for (int m = 0; m < Amount_tp.size(); m++) {
                sum += Double.valueOf(Amount_tp.get(m));
            }

            updateSum(sum);





        } else {
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
            Double sum = 0.0;
            for (int m = 0; m < Amount_tp.size(); m++) {
                sum += Double.valueOf(Amount_tp.get(m));
            }
            updateSum(sum);


        }

        Global_Data.GLOVEL_LONG_DESC = "";
        Global_Data.GLOVEL_CATEGORY_SELECTION = "";
        Global_Data.GLOVEL_ITEM_MRP = "";

//        try {
//            ActionBar mActionBar = getActionBar();
//            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            // mActionBar.setDisplayShowHomeEnabled(false);
//            // mActionBar.setDisplayShowTitleEnabled(false);
//            LayoutInflater mInflater = LayoutInflater.from(this);
//
//            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
//            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
//            //mTitleTextView.setText(Global_Data.order_retailer + " " + "(" + Global_Data.AmountOutstanding + "/" + Global_Data.AmountOverdue + ")");
//            mTitleTextView.setText(Global_Data.order_retailer);
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = Previous_orderNew_S2.this.getSharedPreferences("SimpleLogic", 0);
//            //Log.e("DATA", "PreviewOrderSwipeActivity-"+sp.getString("order", ""));
//
//
////        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
////        	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
////		}
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
//                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
//                } else {
//                    int age = (int) Math.round(age_float);
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    }
//                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
//                }
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
////        	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
//                todaysTarget.setText("Today's Target Acheived");
//            }
//            mActionBar.setCustomView(mCustomView);
//            mActionBar.setDisplayShowCustomEnabled(true);
//            mActionBar.setHomeButtonEnabled(true);
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        dataOrder = i.getParcelableArrayListExtra("productsList");
        adapter = new Previous_orderEdit_Adapter(Previous_orderNew_S2.this, SwipeList);
        totalPrice = 0.00f;

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

        buttonPreviewCheckout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    //b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    List<Local_Data> cont1 = dbvoc.getItemNamePrevious_Order(Global_Data.Previous_Order_ServiceOrder_ID);
                    if(cont1.size() > 0)
                    {
// Toast.makeText(Previous_orderNew_S2.this, "Please update previous order or add more items if you want to sync this order.", Toast.LENGTH_SHORT).show();

//                        Toast toast = Toast.makeText(Previous_orderNew_S2.this, "Please update previous order or add more items if you want to sync this order.", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(Previous_orderNew_S2.this, "Please update previous order or add more items if you want to sync this order.","yes");
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), Previous_orderNew_S3.class);
                        startActivity(intent);
                        return true;
                    }



                }
                return false;
            }
        });
        txt_checkout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    //b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    List<Local_Data> cont1 = dbvoc.getItemNamePrevious_Order(Global_Data.Previous_Order_ServiceOrder_ID);
                    if(cont1.size() > 0)
                    {
// Toast.makeText(Previous_orderNew_S2.this, "Please update previous order or add more items if you want to sync this order.", Toast.LENGTH_SHORT).show();

//                        Toast toast = Toast.makeText(Previous_orderNew_S2.this, "Please update previous order or add more items if you want to sync this order.", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(Previous_orderNew_S2.this, "Please update previous order or add more items if you want to sync this order.","yes");
                    }
                    else
                    {
                        Intent intent = new Intent(getApplicationContext(), Previous_orderNew_S3.class);
                        startActivity(intent);
                        return true;
                    }

                }
                return false;
            }
        });


        buttonPreviewHome = (TextView) findViewById(R.id.buttonPreviewHome);
        buttonPreviewHome.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event

                    // b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //down event
                    //b.setBackgroundColor(Color.parseColor("#910505"));
                    AlertDialog alertDialog = new AlertDialog.Builder(Previous_orderNew_S2.this).create(); //Read Update
                    alertDialog.setTitle(getResources().getString(R.string.Warning));
                    alertDialog.setMessage(getResources().getString(R.string.Dialog_cancel_alert_message));
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // create a instance of SQLite Database
                            dbvoc = new DataBaseHelper(Previous_orderNew_S2.this);
                            dbvoc.getDeleteTableorder_byOID(Global_Data.GLObalOrder_id);
                            dbvoc.getDeleteTableorderproduct_byOID(Global_Data.GLObalOrder_id);
                            Global_Data.GLOvel_GORDER_ID = "";
                            Intent order_home = new Intent(getApplicationContext(), Neworderoptions.class);
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

        buttonPreviewAddMOre = (TextView) findViewById(R.id.buttonPreviewAddMOre);
        //buttonPreviewCheckout.setBackgroundColor(Color.parseColor("#414042"));
        //buttonPreviewHome.setBackgroundColor(Color.parseColor("#414042"));
        //buttonPreviewAddMOre.setBackgroundColor(Color.parseColor("#414042"));

        buttonPreviewAddMOre.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //v.setBackgroundColor(Color.parseColor("#910505"));
//				if(statusOrderActivity.equalsIgnoreCase("previous")){
                final Intent i = new Intent(Previous_orderNew_S2.this, Previous_orderNew_S1.class);
                i.putExtra("data", "data");
                i.putParcelableArrayListExtra("productsList", dataOrder);

                // Log.e("DATA", "Starting out NewOrderFragment...");
                SharedPreferences sp = Previous_orderNew_S2.this.getSharedPreferences("SimpleLogic", 0);

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
        swipeListView.setSwipeMode(settings.getSwipeMode());
        swipeListView.setSwipeActionLeft(settings.getSwipeActionLeft());
        swipeListView.setSwipeActionRight(settings.getSwipeActionRight());
        swipeListView.setOffsetLeft(convertDpToPixel(settings.getSwipeOffsetLeft()));
        swipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
        swipeListView.setAnimationTime(settings.getSwipeAnimationTime());
        swipeListView.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
    }

    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }


//    public boolean onOptionsItemSelected(int featureId, MenuItem item) {
//        final int id = item.getItemId();
//        if (id == android.R.id.home) {
//            //finish();
//            onBackPressed();
//        }
//        return true;
//    }

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
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        SharedPreferences spf = Previous_orderNew_S2.this.getSharedPreferences("SimpleLogic", 0);
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
                String targetNew = "";
                SharedPreferences sp = Previous_orderNew_S2.this.getSharedPreferences("SimpleLogic", 0);
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


    @Override
    public void onBackPressed() {

        AlertDialog alertDialog = new AlertDialog.Builder(Previous_orderNew_S2.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Warning));
        alertDialog.setMessage(getResources().getString(R.string.pre_page_back_message));
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!Global_Data.Previous_Order_ServiceOrder_ID.equalsIgnoreCase(Global_Data.Previous_Order_UpdateOrder_ID)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(Previous_orderNew_S2.this).create(); //Read Update

                    alertDialog.setTitle(getResources().getString(R.string.Warning));
                    alertDialog.setMessage(getResources().getString(R.string.pre_cancel_order_message));
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.Cancel_Order), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                            dbvoc = new DataBaseHelper(Previous_orderNew_S2.this);
                            dbvoc.getDeleteTableorder_byOID(Global_Data.GLObalOrder_id);
                            dbvoc.getDeleteTableorderproduct_byOID(Global_Data.GLObalOrder_id);
                            Global_Data.GLOvel_GORDER_ID = "";
                            Global_Data.GLObalOrder_id = "";

                           // Toast.makeText(Previous_orderNew_S2.this, getResources().getString(R.string.Order_Canceled_Successfully), Toast.LENGTH_SHORT).show();
                            Global_Data.Custom_Toast(Previous_orderNew_S2.this, getResources().getString(R.string.Order_Canceled_Successfully),"yes");
                            if (Global_Data.intentvalue=="smart"&& Global_Data.intentvalue!=""){
                                Global_Data.intentvalue="";
                                Intent order_home = new Intent(getApplicationContext(), Smart_Order.class);
                                startActivity(order_home);
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                            }else if(Global_Data.intentvalue=="favourite"&& Global_Data.intentvalue!=""){
                                Global_Data.intentvalue="";
                                Intent order_home = new Intent(getApplicationContext(), Favourite.class);
                                startActivity(order_home);
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                            }

                            else {
                                Global_Data.intentvalue="";
                                Intent order_home = new Intent(getApplicationContext(), Neworderoptions.class);
                                startActivity(order_home);
                                finish();
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }



                        }
                    });

                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
                } else {
                    if (Global_Data.intentvalue=="smart"&& Global_Data.intentvalue!=""){
                        Global_Data.intentvalue="";
                        Intent order_home = new Intent(getApplicationContext(), Smart_Order.class);
                        startActivity(order_home);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                    }else if(Global_Data.intentvalue=="favourite"&& Global_Data.intentvalue!=""){
                        Global_Data.intentvalue="";
                        Intent order_home = new Intent(getApplicationContext(), Favourite.class);
                        startActivity(order_home);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                    }


                    else {
                        Global_Data.intentvalue="";
                        Intent order_home = new Intent(getApplicationContext(), Neworderoptions.class);
                        startActivity(order_home);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }

                //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });
        alertDialog.show();
    }

    public static void updateSum(Double sum) {

        if (price_str.length() > 0) {
            txttotalPreview.setText("₹" + sum);
        } else {
            txttotalPreview.setText("₹" + sum);
        }
    }
}
