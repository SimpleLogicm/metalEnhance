package com.msimplelogic.activities.kotlinFiles.kotGlobal;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

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
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.App.AppController;
import com.msimplelogic.activities.AppLocationManager;
import com.msimplelogic.activities.BaseActivity;
import com.msimplelogic.activities.BuildConfig;
import com.msimplelogic.activities.CaptureSignature;
import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.Local_Data;
import com.msimplelogic.activities.LocationAddress;
import com.msimplelogic.activities.LoginDataBaseAdapter;
import com.msimplelogic.activities.MainActivity;
import com.msimplelogic.activities.NewOrderActivity;
import com.msimplelogic.activities.OnlineSchemeActivity;
import com.msimplelogic.activities.PlayService_Location;
import com.msimplelogic.activities.PreviewOrderSwipeActivity;
import com.msimplelogic.activities.Previous_Order;
import com.msimplelogic.activities.QuotationCaptureSignature;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.ReturnOrder1;
import com.msimplelogic.activities.TargetAnalysisActivity;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.adapter.OnlineSchemeAdapter;
import com.msimplelogic.adapter.Quick_order_adaptor;
import com.msimplelogic.animation.ActivitySwitcher;
import com.msimplelogic.model.Catalogue_model;
import com.msimplelogic.model.OnlineSchemeModel;
import com.msimplelogic.model.Product;
import com.msimplelogic.services.getServices;
import com.msimplelogic.swipelistview.BaseSwipeListViewListener;
import com.msimplelogic.swipelistview.SwipeListView;
import com.msimplelogic.swipelistview.sample.adapters.PackageAdapter;
import com.msimplelogic.swipelistview.sample.utils.SettingsManager;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cpm.simplelogic.helper.Catalogue_slider_caller;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static com.github.mikephil.charting.utils.Utils.convertDpToPixel;

public class Checkout_quick_order extends BaseActivity implements Catalogue_slider_caller {
    public LinearLayout place_order_ll;
    public LinearLayout total_ll;
    public LinearLayout l_lout;
    public LinearLayout order_de;
    public EditText order_detail2, et_remark, Requiredby;
    public ImageView cam_btn;
    public SignaturePad signature_pad;
    private String Signature_path = "";
    private String mCurrentPhotoPath = "";
    private String mCurrentPhotoPathnft = "";
    public TextView clear;
    public TextView btn_cash, btn_cheque, btn_neft;
    public RecyclerView rv2;
    Boolean B_flag;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String capimg = "";
    String hintsa = "";
    private SwipeListView swipeListView;
    String str;
    Double pp = 0.0;
    String ttl_price;
    private OnlineSchemeAdapter mAdapter;
    private ArrayList<OnlineSchemeModel> catalogue_m1;
    double expectAmt, totalAmt;
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
    private PackageAdapter adapter;
    private ArrayList<Product> dataOrder;
    LoginDataBaseAdapter loginDataBaseAdapter;
    LinearLayout expectedLout;
    TextView textView1, tabletextview1, tabletextview2, tabletextview3;
    static TextView txttotalPreview, txttotalPreview1;
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
    //Button buttonShowScheme;
    String expectedAmt = "";
    TextView btnCash, btnCheque, btnNeft;
    ImageView imgView, buttonPreviewAddMOre1, buttonPreviewCheckout;
    TextView buttonPreviewAddMOre, buttonPreviewHome;
    static String price_str;
    String strdetail1_mandate, strdetail2_mandate;

    static float totalPrice;
    String statusOrderActivity = "";
    Button buttonPreviewCheckout1, buttonPreviewHome1, btn_subapprove;
    ArrayAdapter<String> dataDistrubutorsAdapter;
    boolean firstLaunch = false;
    static String c_Total = "";
    EditText expectedAmount;
    Toolbar toolbar;
    String detail1str, detail2str;
    EditText order_detail1;
    ImageView get_icon;
    DatePickerDialog.OnDateSetListener date, date1;
    Calendar myCalendar;
    TextView viewMoreDealBtn, checkoutBtn, save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_quick_order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        setTitle("");//null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cd = new ConnectionDetector(getApplicationContext());

        place_order_ll = findViewById(R.id.place_order_ll);
        save = findViewById(R.id.save);
        btnCash = findViewById(R.id.btn_cash);
        btnCheque = findViewById(R.id.btn_cheque);
        btnNeft = findViewById(R.id.btn_neft);
        txttotalPreview = (TextView) findViewById(R.id.txttotalPreview);
        txttotalPreview1 = (TextView) findViewById(R.id.txttotalPreview1);
        total_ll = findViewById(R.id.total_ll);
        l_lout = findViewById(R.id.l_lout);
        order_de = findViewById(R.id.order_de);
        order_detail2 = findViewById(R.id.order_detail2);
        et_remark = findViewById(R.id.et_remark);
        cam_btn = findViewById(R.id.cam_btn);
        signature_pad = findViewById(R.id.signature_pad);
        clear = findViewById(R.id.clear);
        get_icon = findViewById(R.id.get_icon);
        order_detail1 = findViewById(R.id.order_detail1);
        btn_cash = findViewById(R.id.btn_cash);
        btn_cheque = findViewById(R.id.btn_cheque);
        btn_neft = findViewById(R.id.btn_neft);
        swipeListView = findViewById(R.id.example_lv_list);
        swipeListView.setNestedScrollingEnabled(true);

        map = new HashMap<String, String>();

        SwipeList = new ArrayList<HashMap<String, String>>();
        //btn_subapprove = (Button) findViewById(R.id.btn_subapprove);

        //buttonShowScheme = (Button) findViewById(R.id.buttonShowScheme);
        //buttonPreviewAddMOre = (ImageView) findViewById(R.id.buttonPreviewAddMOre);
        buttonPreviewAddMOre = (TextView) findViewById(R.id.buttonPreviewAddMOre);
        //buttonPreviewHome = (Button) findViewById(R.id.buttonPreviewHome);
        buttonPreviewHome = (TextView) findViewById(R.id.buttonPreviewHome);
        //expectedAmount = (EditText) findViewById(R.id.expected_amount);
        //expectedLout = (LinearLayout) findViewById(R.id.expected_lout);
        moreDealRecyclerView = (RecyclerView) findViewById(R.id.knowmoredeal_listview);

        SharedPreferences spf2 = this.getSharedPreferences("SimpleLogic", 0);
        ttl_price = spf2.getString("var_total_price", "");

        catalogue_m = new ArrayList<>();
        pDialog = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (signature_pad.isEmpty()) {
                    Global_Data.Custom_Toast(Checkout_quick_order.this, "Please Sign", "Yes");
//                                Toast toast = Toast.makeText(CaptureSignature.this, "Please Sign", Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 105, 50);
//                                toast.show();
                } else {
                    Global_Data.quicksign = signature_pad.getTransparentSignatureBitmap();
                    Global_Data.quikeditdel = "Yes";
                    Global_Data.Custom_Toast(Checkout_quick_order.this, "Sign Save Successfully", "Yes");


                }
            }
        });

        if (Global_Data.btnquick.equalsIgnoreCase("1") & Global_Data.btnquick != "") {
            btnCash.setBackgroundResource(R.drawable.card_border_black);
            btnNeft.setBackgroundResource(android.R.color.transparent);
            btnCheque.setBackgroundResource(android.R.color.transparent);
            //order_detail2.setVisibility(View.GONE);
            order_de.setVisibility(View.GONE);
            capimg = "Cash";
            et_remark.setText(Global_Data.quickremark);
            order_detail1.setText(Global_Data.quickdate);
            signature_pad.setSignatureBitmap(Global_Data.quicksign);
//            Global_Data.quickremark = et_remark.getText().toString();
//            Global_Data.quickdate= order_detail1.getText().toString();
//            Global_Data.quicksign= signature_pad.getTransparentSignatureBitmap();
        } else if (Global_Data.btnquick.equalsIgnoreCase("2") & Global_Data.btnquick != "") {
            btnCash.setBackgroundResource(android.R.color.transparent);
            btnNeft.setBackgroundResource(android.R.color.transparent);
            btnCheque.setBackgroundResource(R.drawable.card_border_black);
            //  order_detail2.setVisibility(View.VISIBLE);
            capimg = "Cheque";
            order_de.setVisibility(View.VISIBLE);
            order_detail2.setHint("PDC No");
            et_remark.setText(Global_Data.quickremark);
            order_detail1.setText(Global_Data.quickdate);
            signature_pad.setSignatureBitmap(Global_Data.quicksign);
            order_detail2.setText(Global_Data.quickorder_detail2);

        } else if (Global_Data.btnquick.equalsIgnoreCase("3") & Global_Data.btnquick != "") {
            btnCash.setBackgroundResource(android.R.color.transparent);
            btnNeft.setBackgroundResource(R.drawable.card_border_black);
            btnCheque.setBackgroundResource(android.R.color.transparent);
            //   order_detail2.setVisibility(View.VISIBLE);
            order_de.setVisibility(View.VISIBLE);
            order_detail2.setHint("Reference No");
            capimg = "NEFT";
            et_remark.setText(Global_Data.quickremark);
            order_detail1.setText(Global_Data.quickdate);
            signature_pad.setSignatureBitmap(Global_Data.quicksign);
            order_detail2.setText(Global_Data.quickorder_detail2nft);
        }

        // for label RP change
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        String rpstr = spf1.getString("var_rp", "");
        String mrpstr = spf1.getString("var_mrp", "");
        price_str = spf1.getString("var_total_price", "");


        detail2str = spf2.getString("var_detail2", "");

        SharedPreferences spf3 = this.getSharedPreferences("SimpleLogic", 0);
        String strdetail1_edit = spf3.getString("var_detail1_edit", "");

        SharedPreferences spf4 = this.getSharedPreferences("SimpleLogic", 0);
        strdetail1_mandate = spf4.getString("var_detail1_mandate", "");

        SharedPreferences spf5 = this.getSharedPreferences("SimpleLogic", 0);
        String strdetail1_allow = spf5.getString("var_detail1_allow", "");

        SharedPreferences spf23 = this.getSharedPreferences("SimpleLogic", 0);
        String strdetail2_edit = spf23.getString("var_detail2_edit", "");

        SharedPreferences spf24 = this.getSharedPreferences("SimpleLogic", 0);
        strdetail2_mandate = spf24.getString("var_detail2_mandate", "");

        SharedPreferences spf25 = this.getSharedPreferences("SimpleLogic", 0);
        String strdetail2_allow = spf25.getString("var_detail2_allow", "");


        // for label change
        detail1str = spf1.getString("var_detail1", "");

//        if (detail1str.length() > 0) {
//            order_detail1.setHint(detail1str);
//        } else {
//            order_detail1.setHint(getResources().getString(R.string.Detail1));
//        }


        String cc_code = "";

        List<Local_Data> contacts = dbvoc.GetOrders_details("Secondary Sales / Retail Sales", Global_Data.GLOvel_GORDER_ID);

        if (Global_Data.quickedit.equalsIgnoreCase("Yes") & Global_Data.btnquick != "") {
            order_detail1.setText(Global_Data.quickdate);
        } else if (contacts.size() > 0) {
            for (Local_Data cn : contacts) {
                // yourName.setText(cn.getOrder_detail3());
                order_detail1.setText(cn.getOrder_detail1());
                order_detail2.setText(cn.getOrder_detail2());
                cc_code = cn.getOrder_category_type().trim();
            }
        }

        myCalendar = Calendar.getInstance();
        date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }
        };
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

        if (strdetail1_edit.equalsIgnoreCase("true")) {
            // order_detail1.setEnabled(true);

            if (strdetail1_allow.equalsIgnoreCase("Text")) {
                order_detail1.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (strdetail1_allow.equalsIgnoreCase("Integer")) {
                order_detail1.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                order_detail1.setFocusableInTouchMode(false);
                order_detail1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Calendar c = Calendar.getInstance();
                        DatePickerDialog dialog = new DatePickerDialog(Checkout_quick_order.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String _year = String.valueOf(year);
                                String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                                String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                                //     String _pickedDate = year + "-" + _month + "-" + _date;
                                String _pickedDate = _date + "/" + _month + "/" + year;
                                Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12

                                order_detail1.setText(_pickedDate);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        dialog.show();
                    }
                });

                //order_detail1.setInputType(InputType.TYPE_CLASS_DATETIME);
            }
        }
        c_Total = getResources().getString(R.string.CTotal);

        get_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestStoragePermission();
            }
        });

        cam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermissionnft();
            }
        });

        place_order_ll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    Log.v("log_tag", "Panel Saved");
                    boolean error = captureSignature();
                    if (!error) {
                        if (capimg.equalsIgnoreCase("")) {
                            Global_Data.Custom_Toast(Checkout_quick_order.this, "Please Select Payment Method", "yes");
                        } else if (capimg.equalsIgnoreCase("NEFT")) {
                            if (order_detail2.getText().toString().equalsIgnoreCase("")) {
                                hintsa = "Reference No";
                                Global_Data.Custom_Toast(Checkout_quick_order.this, "Please Enter " + hintsa, "yes");
                            }else {
                                requestStoragePermissionsave();
                            }

                        } else if (capimg.equalsIgnoreCase("Cheque")) {
                            if (order_detail2.getText().toString().equalsIgnoreCase("")) {
                                hintsa = "PDC No";
                                Global_Data.Custom_Toast(Checkout_quick_order.this, "Please Enter " + hintsa, "yes");
                            }
                            else {
                                requestStoragePermissionsave();
                            }
                        } else {



                            requestStoragePermissionsave();
                        }
                    }
                }
                return false;
            }
        });

        clear.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    signature_pad.clear();
                }
                return false;
            }
        });

//        viewMoreDealBtn.setOnClickListener(new View.OnClickListener(
//        ) {
//            @Override
//            public void onClick(View v) {
//
//                isInternetPresent = cd.isConnectingToInternet();
//                if (isInternetPresent) {
//                    startActivity(new Intent(Checkout_quick_order.this, OnlineSchemeActivity.class));
//                } else {
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                }
//            }
//        });

//        btn_subapprove.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isInternetPresent = cd.isConnectingToInternet();
//                if (isInternetPresent) {
//
//                    if(TextUtils.isEmpty(expectedAmount.getText().toString()))
//                    {
//                        getServices.SYNCORDER_BYCustomerINSTI(PreviewOrderSwipeActivity.this, expectedAmount.getText().toString().trim());
//                    }else{
//                        expectAmt = new Double(expectedAmount.getText().toString());
//                        totalAmt = new Double(txttotalPreview1.getText().toString());
//
//                        if(expectAmt>totalAmt)
//                        {
//                            Toast.makeText(getApplicationContext(),"Expected Amount Should be less than Total amount",Toast.LENGTH_LONG).show();
//                        }else{
//                            Global_Data.expectedAmountAtCreate=expectedAmount.getText().toString().trim();
//                            getServices.SYNCORDER_BYCustomerINSTI(PreviewOrderSwipeActivity.this, expectedAmount.getText().toString().trim());
//                        }
//                    }
//
////                    if(expectedAmount.getText().toString().length()>0)
////                    {
////                        w = new Double(expectedAmount.getText().toString());
////                        if(w>sum)
////                        {
////                            getServices.SYNCORDER_BYCustomerINSTI(PreviewOrderSwipeActivity.this, expectedAmount.getText().toString().trim());
////                        }else{
////                            Toast.makeText(getApplicationContext(),"Expected Amount Should greater than Total amount",Toast.LENGTH_LONG).show();
////                        }
////
////                    }else{
////                        getServices.SYNCORDER_BYCustomerINSTI(PreviewOrderSwipeActivity.this, expectedAmount.getText().toString().trim());
////                    }
//
//                } else {
//                    // Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                }
//            }
//        });

        buttonPreviewCheckout = (ImageView) findViewById(R.id.buttonPreviewCheckout);
        // buttonPreviewCheckout1 = (Button) findViewById(R.id.buttonPreviewCheckout1);

        if (Global_Data.sales_btnstring.equalsIgnoreCase("Institutional Sales")) {
            buttonPreviewHome.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            //btn_subapprove.setVisibility(View.VISIBLE);
            buttonPreviewCheckout.setVisibility(View.GONE);
            //buttonPreviewCheckout1.setVisibility(View.GONE);
            //buttonShowScheme.setVisibility(View.GONE);
            // expectedLout.setVisibility(View.VISIBLE);
            //buttonPreviewCheckout.setEnabled(false);

//            expectedAmount.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                    //adapter.getFilter().filter(cs);
//
////                    try {
////
////                    if((Double.parseDouble(String.valueOf(cs)))>sum)
////                    {
////                        w = new Double(expectedAmount.getText().toString());
////                    }
////                    else{
////                        Toast.makeText(getApplicationContext(),"Expected Amount Should greater than Total amount",Toast.LENGTH_LONG).show();
////                    }
////
////                    } catch (NumberFormatException e) {
////                        w = 0; // your default value
////                    }
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//                    //Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void afterTextChanged(Editable arg0) {
//                    //Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();
//                }
//            });

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

        if ((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(Global_Data.orderListStatus)).equalsIgnoreCase("orderlist_status")) {
            Global_Data.PREVIOUS_ORDER_BACK_FLAG = Global_Data.orderListStatus;
            buttonPreviewAddMOre.setVisibility(View.INVISIBLE);
            buttonPreviewCheckout.setVisibility(View.GONE);
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
        } else if ((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(Global_Data.orderListStatus)).equalsIgnoreCase("orderlist_status")) {

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
//        try {
//            ActionBar mActionBar = getActionBar();
//            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            // mActionBar.setDisplayShowHomeEnabled(false);
//            // mActionBar.setDisplayShowTitleEnabled(false);
//            LayoutInflater mInflater = LayoutInflater.from(this);
//            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
//            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
//            mTitleTextView.setText(Global_Data.order_retailer);
//            //mTitleTextView.setText(Global_Data.order_retailer + " " + "(" + Global_Data.AmountOutstanding + "/" + Global_Data.AmountOverdue + ")");
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = PreviewOrderSwipeActivity.this.getSharedPreferences("SimpleLogic", 0);
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
//
//            mActionBar.setCustomView(mCustomView);
//            mActionBar.setDisplayShowCustomEnabled(true);
//            mActionBar.setHomeButtonEnabled(true);
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        dataOrder = i.getParcelableArrayListExtra("productsList");
        adapter = new PackageAdapter(Checkout_quick_order.this, SwipeList);
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

        catalogue_m1 = new ArrayList<>();
        mAdapter = new OnlineSchemeAdapter(Checkout_quick_order.this,getApplicationContext(), catalogue_m1, Checkout_quick_order.this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
//        moreDealRecyclerView.setLayoutManager(mLayoutManager);
//        moreDealRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        moreDealRecyclerView.setAdapter(mAdapter);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            OnlineSchemeCatalogue();
        } else {
            Global_Data.Custom_Toast(this, "" + getResources().getString(R.string.internet_connection_error), "Yes");
        }


        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCash.setBackgroundResource(R.drawable.card_border_black);
                btnNeft.setBackgroundResource(android.R.color.transparent);
                btnCheque.setBackgroundResource(android.R.color.transparent);
                //order_detail2.setVisibility(View.GONE);
                order_de.setVisibility(View.GONE);
                Global_Data.btnquick = "1";
                capimg = "Cash";
                Global_Data.quickremark = et_remark.getText().toString();
                Global_Data.quickedit = "Yes";

                //  Global_Data.quickdate= order_detail1.getText().toString();
                //   Global_Data.quicksign= signature_pad.getTransparentSignatureBitmap();


            }
        });

        btnCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCash.setBackgroundResource(android.R.color.transparent);
                btnNeft.setBackgroundResource(android.R.color.transparent);
                btnCheque.setBackgroundResource(R.drawable.card_border_black);
                //  order_detail2.setVisibility(View.VISIBLE);
                order_de.setVisibility(View.VISIBLE);
                order_detail2.setHint("PDC No");
                Global_Data.btnquick = "2";
                Global_Data.quickedit = "Yes";
                Global_Data.quickremark = et_remark.getText().toString();
                capimg = "Cheque";
                order_detail2.setText("");
                // Global_Data.quickdate= order_detail1.getText().toString();
                //  Global_Data.quicksign= signature_pad.getTransparentSignatureBitmap();
                Global_Data.quickorder_detail2 = order_detail2.getText().toString();
            }
        });

        btnNeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCash.setBackgroundResource(android.R.color.transparent);
                btnNeft.setBackgroundResource(R.drawable.card_border_black);
                btnCheque.setBackgroundResource(android.R.color.transparent);
                //   order_detail2.setVisibility(View.VISIBLE);
                order_de.setVisibility(View.VISIBLE);
                order_detail2.setHint("Reference No");
                Global_Data.btnquick = "3";
                Global_Data.quickedit = "Yes";
                Global_Data.quickremark = et_remark.getText().toString();
                capimg = "NEFT";
                order_detail2.setText("");
                // Global_Data.quickdate= order_detail1.getText().toString();
                // Global_Data.quicksign= signature_pad.getTransparentSignatureBitmap();
                Global_Data.quickorder_detail2nft = order_detail2.getText().toString();

            }
        });


    }

    private void requestStoragePermissionnft() {
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
//                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                   // fileUri = getOutputMediaFileUrinew(MEDIA_TYPE_IMAGE);
//                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    //startActivityForResult(intent1, MEDIA_TYPE_IMAGE);
//                    startActivityForResult(intent1, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                    }

//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                    }

                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                    // Create the File where the photo should go
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFilenft();
                                    } catch (Exception ex) {
                                        // Error occurred while creating the File
                                        Log.i("Image TAG", "IOException");
                                        mCurrentPhotoPathnft = "";
                                    }
                                    // Continue only if the File was successfully created
                                    if (photoFile != null) {
                                        Uri photoURI = FileProvider.getUriForFile(Checkout_quick_order.this,
                                                BuildConfig.APPLICATION_ID + ".provider",
                                                photoFile);
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        //  cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                        startActivityForResult(cameraIntent, 6);
                                    }
                                }

                            } else {
                                Global_Data.Custom_Toast(getApplicationContext(), "no camera on this device", "");
                                //  Toast.makeText(getApplicationContext(), "no camera on this device", Toast.LENGTH_LONG).show();
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
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                        // Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        order_detail1.setText(sdf.format(myCalendar.getTime()));
        Global_Data.orderDetail1 = order_detail1.getText().toString();
        Global_Data.quickdate = order_detail1.getText().toString();
    }

    private void updateLabel1() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        order_detail1.setText(sdf.format(myCalendar.getTime()));
        Global_Data.orderDetail1 = order_detail1.getText().toString();
        Global_Data.quickdate = order_detail1.getText().toString();

    }

    private void requestStoragePermissionsave() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (signature_pad.isEmpty()) {
                                Global_Data.Custom_Toast(Checkout_quick_order.this, "Please Sign", "Yes");
//                                Toast toast = Toast.makeText(CaptureSignature.this, "Please Sign", Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 105, 50);
//                                toast.show();
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(Checkout_quick_order.this).create(); //Read Update
                                alertDialog.setTitle(getResources().getString(R.string.Confirmation));
                                alertDialog.setMessage(getResources().getString(R.string.Continue_dialog));
                                alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub

                                        String order_detail1_text = "";
                                        String order_detail2_text = "";
                                        String order_type_text = "";
                                        String order_type_name = "";
                                        String order_type_code = "";
                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(order_detail1.getText().toString().trim())) {

                                            order_detail1_text = order_detail1.getText().toString().trim();
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(order_detail2.getText().toString().trim())) {

                                            order_detail2_text = order_detail2.getText().toString().trim();
                                        }

//                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(yourName.getText().toString().trim())) {
//
//                                            order_type_name = yourName.getText().toString().trim();
//                                        }
//
//                                        if (!(order_type.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Order_Type)))) {
//
//                                            order_type_text = order_type.getSelectedItem().toString();
//                                            List<Local_Data> contacts1 = dbvoc.get_order_category_code(order_type_text);
//
//                                            for (Local_Data cn : contacts1) {
//
//                                                order_type_code = cn.getOrder_type_code();
//                                            }
//
//                                        }

                                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                        } else
                                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                        try {
                                            AppLocationManager appLocationManager = new AppLocationManager(Checkout_quick_order.this);
                                            Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                                            Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                            PlayService_Location PlayServiceManager = new PlayService_Location(Checkout_quick_order.this);

                                            if (PlayServiceManager.checkPlayServices(Checkout_quick_order.this)) {
                                                Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                                            } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                                Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                                Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                            }

                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                        try {
                                            final Bitmap bitmap = signature_pad.getTransparentSignatureBitmap();

                                            SaveImage(bitmap, "MSI" + Global_Data.GLObalOrder_id);
                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LATITUDE) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LONGITUDE)) {

                                                dbvoc.updateORDER_SIGNATURENEW_WITHLATLONG(Signature_path, Global_Data.GLObalOrder_id, order_detail1_text, order_detail2_text, order_type_name, order_type_code, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE);
                                            } else {
                                                dbvoc.updateORDER_SIGNATURENEW(Signature_path, Global_Data.GLObalOrder_id, order_detail1_text, order_detail2_text, order_type_name, order_type_code);
                                            }
                                            //mSignature.clear();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        isInternetPresent = cd.isConnectingToInternet();

                                        if (isInternetPresent) {

                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(String.valueOf(Global_Data.GLOvel_LATITUDE)) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(String.valueOf(Global_Data.GLOvel_LONGITUDE))) {

                                                try {
                                                    LocationAddress locationAddress = new LocationAddress();
                                                    LocationAddress.getAddressFromLocation(Double.valueOf(Global_Data.GLOvel_LATITUDE), Double.valueOf(Global_Data.GLOvel_LONGITUDE),
                                                            getApplicationContext(), new Checkout_quick_order.GeocoderHandler());

                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                    addressn(Double.valueOf(Global_Data.GLOvel_LATITUDE), Double.valueOf(Global_Data.GLOvel_LONGITUDE));
                                                }
                                            }


                                            Log.d("GLOVADD", "GLOVADD" + Global_Data.address);
                                            getServices.SYNCORDER_BYCustomer(Checkout_quick_order.this, Global_Data.GLOvel_GORDER_ID);
                                        } else {
                                            //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();

                                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.internet_connection_error), "");
//                                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                                            //toast.setGravity(Gravity.CENTER, 0, 0);
//                                            toast.show();

                                            get_dialog();
                                        }
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
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                        //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public void get_dialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(Checkout_quick_order.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Confirmation));
        alertDialog.setMessage(getResources().getString(R.string.Dialog_order_offline_message));
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Save), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub


                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//								   InsertOrderAsyncTask insertOrderAsyncTask =new InsertOrderAsyncTask(CaptureSignature.this);
//								   insertOrderAsyncTask.execute();

                //  Toast.makeText(getApplicationContext(),"Order generate successfully.",Toast.LENGTH_LONG).show();

                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Order_generate_successfully), "Yes");
//                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Order_generate_successfully), Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();

                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);

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


    public String addressn(Double lat, Double longi) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        StringBuilder sb = new StringBuilder();
        sb.append("");

        try {
            addresses = geocoder.getFromLocation(lat, longi, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getAddressLine(0)) + " ");
            if (!(sb.indexOf(addresses.get(0).getLocality()) > 0)) {
                sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getLocality()) + " ");
            }

            if (!(sb.indexOf(addresses.get(0).getAdminArea()) > 0)) {
                sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getAdminArea()) + " ");
            }

            if (!(sb.indexOf(addresses.get(0).getCountryName()) > 0)) {
                sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getCountryName()) + " ");
            }

            if (!(sb.indexOf(addresses.get(0).getPostalCode()) > 0)) {
                sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getPostalCode()) + " ");
            }
            //String knownName = addresses.get(0).getFeatureName();

            Global_Data.address = sb.toString();
            //sb.append(knownName).append(" ");
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress = "";
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    //locationAddress = " ";
            }
            //  LOCATION.setText(locationAddress);


            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(locationAddress)) {
                Global_Data.address = locationAddress;
                Log.d("GLOBEL ADDRESS G", "V" + locationAddress);

            } else {
                Global_Data.address = "";
                Log.d("GLOBEL ADDRESS G", "address not found.");
            }


        }
    }

    private void SaveImage(Bitmap finalBitmap, String name) {

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File myDir = new File(path, "Metal_Signature");
        myDir.mkdirs();

        String fname = name + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Signature_path = "file:" + file.getAbsolutePath();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private boolean captureSignature() {
        boolean error = false;
        String errorMessage = "";

        if (et_remark.getText().toString().equalsIgnoreCase("")) {
            errorMessage = errorMessage + getResources().getString(R.string.Please_Enter_remark);
            error = true;
        } else if ((strdetail1_mandate.equalsIgnoreCase("true")) || (strdetail2_mandate.equalsIgnoreCase("true"))) {

            if (strdetail1_mandate.equalsIgnoreCase("true")) {
                if (order_detail1.getText().toString().equalsIgnoreCase("")) {
                    errorMessage = errorMessage + getResources().getString(R.string.Please_Enter) + detail1str;
                    error = true;
                }
            }

            if (strdetail2_mandate.equalsIgnoreCase("true")) {
                if (order_detail2.getText().toString().equalsIgnoreCase("")) {
                    errorMessage = errorMessage + getResources().getString(R.string.Please_Enter) + detail2str;
                    error = true;
                }
            }
        }

//        }else if((strdetail1_mandate.equalsIgnoreCase("true")) || (strdetail2_mandate.equalsIgnoreCase("true"))){
//
//            if(strdetail1_mandate.equalsIgnoreCase("true")) {
//                if (order_detail1.getText().toString().equalsIgnoreCase("")) {
//                    errorMessage = errorMessage + "Please Enter " + detail1str;
//                    error = true;
//                }
//            }
//
//            if(strdetail2_mandate.equalsIgnoreCase("true"))
//            {
//                if(order_detail2.getText().toString().equalsIgnoreCase("")){
//                    errorMessage = errorMessage + "Please Enter "+detail2str;
//                    error = true;
//                }
//            }
//        }

        if (error) {
            Global_Data.Custom_Toast(this, errorMessage, "Yes");

//            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        }

        return error;
    }

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
//                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                   // fileUri = getOutputMediaFileUrinew(MEDIA_TYPE_IMAGE);
//                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    //startActivityForResult(intent1, MEDIA_TYPE_IMAGE);
//                    startActivityForResult(intent1, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                    }

//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                    }

                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                    // Create the File where the photo should go
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (Exception ex) {
                                        // Error occurred while creating the File
                                        Log.i("Image TAG", "IOException");
                                        mCurrentPhotoPath = "";
                                    }
                                    // Continue only if the File was successfully created
                                    if (photoFile != null) {
                                        Uri photoURI = FileProvider.getUriForFile(Checkout_quick_order.this,
                                                BuildConfig.APPLICATION_ID + ".provider",
                                                photoFile);
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                        // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                                    }
                                }

                            } else {
                                Global_Data.Custom_Toast(getApplicationContext(), "no camera on this device", "");
                                //  Toast.makeText(getApplicationContext(), "no camera on this device", Toast.LENGTH_LONG).show();
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
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                        // Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Checkout_quick_order.this);
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

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private File createImageFilenft() throws IOException {
        // Create an image file name
        String imageFileName = "Metal";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Metal");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPathnft = "file:" + image.getAbsolutePath();
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "Metal";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Metal");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
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

    public void OnlineSchemeCatalogue() {

        String domain = "";
        String domain1 = "";

        //dialog = new ProgressDialog(Video_Main_List.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pDialog.setMessage(getResources().getString(R.string.Please_Wait));
        pDialog.setTitle(getResources().getString(R.string.app_name));
        pDialog.setCancelable(false);
        pDialog.show();

        String service_url = "";
        SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
        String Cust_domain = sp.getString("Cust_Service_Url", "");
        String service_url1 = Cust_domain + "metal/api/v1/";
        //String device_id = sp.getString("devid", "");
        domain = service_url1;

        domain1 = Cust_domain;

        SharedPreferences spf = Checkout_quick_order.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

//        Global_Data.Search_Category_name = Global_Data.Search_Category_name.replaceAll("&", "%26");
//        Global_Data.Search_Product_name = Global_Data.Search_Product_name.replaceAll("&", "%26");
//        Global_Data.ProductVariant = Global_Data.ProductVariant.replaceAll("&", "%26");

        if (Global_Data.ProductVariant.trim().equalsIgnoreCase("Select All")) {

            try {
                service_url = domain + "products/get_products_of_active_schemes?email=" +
                        URLEncoder.encode(user_email, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //service_url = service_url.replaceAll(" ", "%20");

        } else {

            try {
                service_url = domain + "products/get_products_of_active_schemes?email=" + URLEncoder.encode(user_email, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //service_url = service_url.replaceAll(" ", "%20");

        }

        Log.d("Server url", "Server url" + service_url);

        StringRequest stringRequest = null;
        final String finalDomain = domain1;
        stringRequest = new StringRequest(service_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //showJSON(response);
                        // Log.d("jV", "JV" + response);
                        Log.d("jV", "JV length" + response.length());
                        // JSONObject person = (JSONObject) (response);
                        try {
                            JSONObject json = new JSONObject(new JSONTokener(response));
                            try {
                                String response_result = "";
                                if (json.has("result")) {
                                    response_result = json.getString("result");

//                                    Toast toast = Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();

//                                    Global_Data.Custom_Toast(getApplicationContext(), response_result, "yes");
//                                    Intent launch = new Intent(Checkout_quick_order.this, QuickOrder.class);
//                                    startActivity(launch);
//                                    finish();
                                } else {

                                    JSONArray products = json.getJSONArray("products");
                                    Global_Data.products = json.getJSONArray("products");
//
                                    Log.i("volley", "response reg products Length: " + products.length());
//
                                    Log.d("users", "products" + products.toString());
//
                                    catalogue_m.clear();
                                    pp = 0.0;

                                    if (products.length() > 0) {
                                        for (int i = 0; i < products.length(); i++) {
                                            JSONObject object = products.getJSONObject(i);
                                            OnlineSchemeModel catalogue_model = new OnlineSchemeModel();
                                            catalogue_model.setItem_name(object.getString("name"));
                                            catalogue_model.setItem_number(object.getString("code"));
                                            catalogue_model.setItem_rp(object.getString("retail_price"));
                                            catalogue_model.setItem_mrp(object.getString("mrp"));
                                            catalogue_model.setItem_min_qty(object.getString("min_order_qty"));
                                            catalogue_model.setItem_max_qty(object.getString("max_order_qty"));
                                            catalogue_model.setItem_pkg_qty1(object.getString("pkg_qty1"));
                                            catalogue_model.setItem_pkg_qty2(object.getString("pkg_qty2"));

                                            catalogue_model.setScheme_id(object.getString("scheme_id"));
                                            catalogue_model.setScheme_name(object.getString("scheme_name"));
                                            catalogue_model.setScheme_type(object.getString("scheme_type"));
                                            catalogue_model.setScheme_amount(object.getString("scheme_amount"));
                                            catalogue_model.setScheme_description(object.getString("scheme_description"));
                                            catalogue_model.setScheme_buy_qty(object.getString("scheme_buy_qty"));
                                            catalogue_model.setScheme_get_qty(object.getString("scheme_get_qty"));
                                            catalogue_model.setScheme_topsellingproduct(object.getString("is_top_selling_product"));
                                            catalogue_model.setScheme_min_qty(object.getString("scheme_min_qty"));

                                            JSONArray product_catalogues = object.getJSONArray("product_catalogues");
                                            // Global_Data.products = object.getJSONArray("product_catalogues");

                                            List<Local_Data> contactsn1 = dbvoc.GetSchemeByCode(object.getString("code"));
                                            if (contactsn1.size() > 0) {
                                                for (Local_Data cn : contactsn1) {
                                                    catalogue_model.setItem_scheme(cn.getDisplayName());
                                                    // list3.add("SCHEME : " + cn.getDisplayName());
                                                }
                                            }

                                            for (int j = 0; j < product_catalogues.length(); j++) {
                                                JSONObject image_object = product_catalogues.getJSONObject(j);
                                                catalogue_model.setItem_image_url(finalDomain + image_object.getString("thumb_url"));
                                                //catalogue_model.setItem_image_url("http://f59c3827.ngrok.io"+"/"+image_object.getString("thumb_url"));
                                                //catalogue_model.setItem_image_url(image_object.getString("thumb_url"));
                                            }

                                            List<Local_Data> contactsn = dbvoc.GetOrder_Product_BY_ORDER_ID(Global_Data.GLObalOrder_id, object.getString("code"));
                                            if (contactsn.size() > 0) {
                                                for (Local_Data cn : contactsn) {
                                                    catalogue_model.setItem_quantity(cn.get_delivery_product_order_quantity());
                                                    catalogue_model.setItem_amount(getResources().getString(R.string.PRICE) + cn.getAmount());


                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getRemarks())) {
                                                        catalogue_model.setItem_remarks(cn.getRemarks());
                                                    } else {
                                                        catalogue_model.setItem_remarks("");
                                                    }

                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail1())) {
                                                        catalogue_model.setDetail1(cn.getDetail1());
                                                    }

                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail2())) {
                                                        catalogue_model.setDetail2(cn.getDetail2());
                                                    }

                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail3())) {
                                                        catalogue_model.setDetail3(cn.getDetail3());
                                                    }

                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail4())) {
                                                        catalogue_model.setDetail4(cn.getDetail4());
                                                    }

                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail5())) {
                                                        catalogue_model.setDetail5(cn.getDetail5());
                                                    }


                                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getAmount())) {
                                                        pp += Double.valueOf(cn.getAmount());
                                                    }
                                                }
                                            } else {
                                                catalogue_model.setItem_quantity("");
                                                catalogue_model.setItem_amount("");
                                                catalogue_model.setItem_remarks("");
                                                catalogue_model.setDetail1("");
                                                catalogue_model.setDetail2("");
                                                catalogue_model.setDetail3("");
                                                catalogue_model.setDetail4("");
                                                catalogue_model.setDetail5("");
                                            }

                                            catalogue_m1.add(catalogue_model);

                                        }

                                        //Intent launch = new Intent(context,Youtube_Player_Activity.class);
                                        //startActivity(launch);

                                        //Global_Data.catalogue_m = catalogue_m;
                                        pDialog.hide();
                                        mAdapter.notifyDataSetChanged();

                                        if (ttl_price.length() > 0) {
                                            txttotalPreview.setText(ttl_price + ":" + pp);
                                        } else {

                                            txttotalPreview.setText(getResources().getString(R.string.Total) + pp);
                                        }


                                    } else {
                                        pDialog.hide();
                                        // Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG).show();

//                                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.product_not_found_message), Toast.LENGTH_LONG);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();

                                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.product_not_found_message), "yes");
                                        Intent launch = new Intent(Checkout_quick_order.this, QuickOrder.class);
                                        startActivity(launch);
                                        finish();
                                    }

                                    //finish();

                                }

                                //  finish();
                                // }

                                // output.setText(data);
                            } catch (JSONException e) {
                                e.printStackTrace();


                                Log.i("tag", "tag " + e.toString());
//                                Toast toast = Toast.makeText(Checkout_quick_order.this,
//                                        "Service Error",
//                                        Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(Checkout_quick_order.this,
                                        "Service Error", "yes");
                                Intent launch = new Intent(Checkout_quick_order.this, MainActivity.class);
                                startActivity(launch);
                                finish();

                                pDialog.hide();
                            }
                            pDialog.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  finish();
                            pDialog.dismiss();
                        }
                        pDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(Image_Gellary.this,
//                                    "Network Error",
//                                    Toast.LENGTH_LONG).show();
//
//                            Toast toast = Toast.makeText(Checkout_quick_order.this,
//                                    "Network Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(Checkout_quick_order.this,
                                    "Network Error", "");
                        } else if (error instanceof AuthFailureError) {

//                            Toast toast = Toast.makeText(Checkout_quick_order.this,
//                                    "Server AuthFailureError  Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(Checkout_quick_order.this,
                                    "Server AuthFailureError  Error", "");

                        } else if (error instanceof ServerError) {
//
//                            Toast toast = Toast.makeText(Checkout_quick_order.this,
//                                    getResources().getString(R.string.Server_Errors),
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(Checkout_quick_order.this,
                                    getResources().getString(R.string.Server_Errors), "");
                        } else if (error instanceof NetworkError) {

//                            Toast toast = Toast.makeText(Checkout_quick_order.this,
//                                    "Network   Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(Checkout_quick_order.this,
                                    "Network   Error", "");
                        } else if (error instanceof ParseError) {


//                            Toast toast = Toast.makeText(Checkout_quick_order.this,
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(Checkout_quick_order.this,
                                    "ParseError   Error", "");
                        } else {
                            // Toast.makeText(Image_Gellary.this, error.getMessage(), Toast.LENGTH_LONG).show();
//
//                            Toast toast = Toast.makeText(Checkout_quick_order.this, error.getMessage(), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Checkout_quick_order.this, error.getMessage(), "");
                        }
                        Intent launch = new Intent(Checkout_quick_order.this, MainActivity.class);
                        startActivity(launch);
                        finish();
                        pDialog.dismiss();
                        // finish();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Checkout_quick_order.this);

        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        stringRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        //requestQueue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();


        AlertDialog alertDialog = new AlertDialog.Builder(Checkout_quick_order.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Warning));
        alertDialog.setMessage(getResources().getString(R.string.Dialog_cancel_alert_message));
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Global_Data.CatlogueStatus = "";
                Intent intent = new Intent(Checkout_quick_order.this, QuickOrder.class);
                startActivity(intent);
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

    }

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
                String targetNew = "";
                SharedPreferences sp = Checkout_quick_order.this.getSharedPreferences("SimpleLogic", 0);
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
    public void slideCall(int po) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static void updateSum(Double sum) {
        if (price_str.length() > 0) {
            txttotalPreview.setText(price_str + " : " + "₹" + sum);
            txttotalPreview1.setText("₹" + sum);
        } else {
            txttotalPreview.setText(c_Total + "₹" + sum);
            txttotalPreview1.setText("₹" + sum);
        }
        //txttotalPreview.setText("Total		:		"+sum);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {

                dbvoc.updateORDER_order_image(mCurrentPhotoPath, Global_Data.GLObalOrder_id);
                Global_Data.Custom_Toast(Checkout_quick_order.this, "Image Captured and Save successfully", "yes");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //get_icon.setImageBitmap(imageBitmap);
        }

        if (requestCode == 6 && resultCode == RESULT_OK) {
            if (capimg.equalsIgnoreCase("NEFT")) {
                Global_Data.Custom_Toast(Checkout_quick_order.this, "NEFT Image Captured and Save successfully", "yes");

            } else {
                Global_Data.Custom_Toast(Checkout_quick_order.this, "Cheque Image Captured and Save successfully", "yes");

            }


        }

    }
}
