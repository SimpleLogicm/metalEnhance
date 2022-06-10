package com.msimplelogic.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.msimplelogic.App.AppController;
import com.msimplelogic.adapter.CatalogueAdapter;
import com.msimplelogic.model.Catalogue_model;
import com.msimplelogic.model.SwipeImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpm.simplelogic.helper.Catalogue_slider_caller;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class OnlineCatalogue extends BaseActivity implements CatalogueAdapter.customButtonListener, Catalogue_slider_caller {
    Double pp = 0.0;
    private ArrayList<Catalogue_model> catalogue_m;
    private ArrayList<SwipeImage> swipeimg_catalogue_m;
    ProgressDialog dialog;
    String q_check = "";
    TextView txttotalPreview;
    private ProgressDialog pDialog;
    private CatalogueAdapter mAdapter;
    private RecyclerView recyclerView;
    Button online_catalog_save, online_catalog_Previewv, online_catalog_addmore;
    private ArrayList<String> scheme_id = new ArrayList<String>();
    private ArrayList<String> p_id = new ArrayList<String>();
    private ArrayList<String> p_name = new ArrayList<String>();
    private ArrayList<String> p_mrp = new ArrayList<String>();
    private ArrayList<String> p_rp = new ArrayList<String>();
    private ArrayList<String> p_q = new ArrayList<String>();
    private ArrayList<String> p_price = new ArrayList<String>();
    private ArrayList<String> p_remarks = new ArrayList<String>();
    private ArrayList<String> p_detail1 = new ArrayList<String>();
    private ArrayList<String> p_detail2 = new ArrayList<String>();
    private ArrayList<String> p_detail3 = new ArrayList<String>();
    private ArrayList<String> p_detail4 = new ArrayList<String>();
    private ArrayList<String> p_detail5 = new ArrayList<String>();
    private ArrayList<String> p_url = new ArrayList<String>();
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    LoginDataBaseAdapter loginDataBaseAdapter;
    String ttl_price;
    static final String TAG_ITEMNAME = "product_name";
    static final String TAG_QTY = "total_qty";
    static final String TAG_PRICE = "MRP";
    static final String TAG_RP = "RP";
    static final String TAG_AMOUNT = "amount";
    static final String TAG_QUANTITY = "amount";
    static final String TAG_ITEM_NUMBER = "item_number";
    Toolbar toolbar;
    ImageView hedder_theame;
SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.onlinecatalogue_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        online_catalog_save = (Button) findViewById(R.id.online_catalog_save);
        online_catalog_Previewv = (Button) findViewById(R.id.online_catalog_Previewv);
        online_catalog_addmore = (Button) findViewById(R.id.online_catalog_addmore);
        txttotalPreview = (TextView) findViewById(R.id.txttotalPreview);
        hedder_theame = (ImageView) findViewById(R.id.hedder_theame);
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        swipeimg_catalogue_m = new ArrayList<>();



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);
        }

        pDialog = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        if (Global_Data.catalogue_m.size() > 0) {
            catalogue_m = new ArrayList<>();
            catalogue_m = (ArrayList<Catalogue_model>) Global_Data.catalogue_m;
            mAdapter = new CatalogueAdapter(getApplicationContext(), Global_Data.catalogue_m, OnlineCatalogue.this);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

        } else {
            catalogue_m = new ArrayList<>();
            mAdapter = new CatalogueAdapter(getApplicationContext(), catalogue_m, OnlineCatalogue.this);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            Online_Catalogue();
        }


        online_catalog_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                String buttonText = ((Button) arg0).getText().toString();

                if (buttonText.equalsIgnoreCase("Add More")) {
                    p_id.clear();
                    p_url.clear();
                    p_name.clear();
                    scheme_id.clear();
                    p_mrp.clear();
                    p_q.clear();
                    p_price.clear();
                    p_rp.clear();
                    Global_Data.Order_hashmap.clear();
                    Global_Data.Orderproduct_remarks.clear();
                    p_remarks.clear();
                    p_detail1.clear();
                    p_detail2.clear();
                    p_detail3.clear();
                    p_detail4.clear();
                    p_detail5.clear();
                    Global_Data.Orderproduct_detail1.clear();
                    Global_Data.Orderproduct_detail2.clear();
                    Global_Data.Orderproduct_detail3.clear();
                    Global_Data.Orderproduct_detail4.clear();
                    Global_Data.Orderproduct_detail5.clear();
                    Global_Data.GLOVEL_LONG_DESC = "";
                    Global_Data.GLOVEL_CATEGORY_SELECTION = "";
                    Global_Data.GLOVEL_ITEM_MRP = "";
                    // Global_Data.Search_business_unit_name = "";
                    Global_Data.Search_Category_name = "";

                    Intent i = new Intent(OnlineCatalogue.this, NewOrderActivity.class);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();

                } else {
                    p_id.clear();
                    p_url.clear();
                    p_name.clear();
                    scheme_id.clear();
                    p_mrp.clear();
                    p_q.clear();
                    p_price.clear();
                    p_rp.clear();
                    q_check = "";

                    p_remarks.clear();
                    p_detail1.clear();
                    p_detail2.clear();
                    p_detail3.clear();
                    p_detail4.clear();
                    p_detail5.clear();

//                    dialog = new ProgressDialog(OnlineCatalogue.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//                    dialog.setMessage("Please wait Customer Loading....");
//                    dialog.setTitle("Metal App");
//                    dialog.setCancelable(false);
//                    dialog.show();

                    new OnlineCatalogue.Varientsave().execute();

                }
            }
        });

        online_catalog_Previewv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(Global_Data.sales_btnstring.equals("Institutional Sales"))
                {
                    if (!Global_Data.GLOvel_GORDER_ID.equalsIgnoreCase("")) {
                        Global_Data.GLOVEL_LONG_DESC = "";
                        Global_Data.GLOVEL_CATEGORY_SELECTION = "";
                        Global_Data.GLOVEL_ITEM_MRP = "";
                        Global_Data.Search_Category_name = "";
                        Global_Data.Order_hashmap.clear();
                        Global_Data.Search_Product_name = "";

                        Global_Data.Orderproduct_remarks.clear();
                        p_remarks.clear();
                        p_detail1.clear();
                        p_detail2.clear();
                        p_detail3.clear();
                        p_detail4.clear();
                        p_detail5.clear();
                        Global_Data.Orderproduct_detail1.clear();
                        Global_Data.Orderproduct_detail2.clear();
                        Global_Data.Orderproduct_detail3.clear();
                        Global_Data.Orderproduct_detail4.clear();
                        Global_Data.Orderproduct_detail5.clear();

                        Intent i = new Intent(OnlineCatalogue.this, ActivityQuote.class);
                        //Intent i = new Intent(OnlineCatalogue.this, ActivityQuote.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        startActivity(i);
                        finish();
                        //   }
                        //NewOrderFragment.this.startActivity(i);
                    }
                    else {
//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Preview_order_validation_message), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Preview_order_validation_message),"yes");
                    }

                }else{
                    if (!Global_Data.GLOvel_GORDER_ID.equalsIgnoreCase("")) {
                        Global_Data.GLOVEL_LONG_DESC = "";
                        Global_Data.GLOVEL_CATEGORY_SELECTION = "";
                        Global_Data.GLOVEL_ITEM_MRP = "";
                        Global_Data.Search_Category_name = "";
                        Global_Data.Order_hashmap.clear();
                        Global_Data.Search_Product_name = "";

                        Global_Data.Orderproduct_remarks.clear();
                        p_remarks.clear();
                        p_detail1.clear();
                        p_detail2.clear();
                        p_detail3.clear();
                        p_detail4.clear();
                        p_detail5.clear();
                        Global_Data.Orderproduct_detail1.clear();
                        Global_Data.Orderproduct_detail2.clear();
                        Global_Data.Orderproduct_detail3.clear();
                        Global_Data.Orderproduct_detail4.clear();
                        Global_Data.Orderproduct_detail5.clear();

                        Intent i = new Intent(OnlineCatalogue.this, PreviewOrderSwipeActivity.class);
                        //Intent i = new Intent(OnlineCatalogue.this, ActivityQuote.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        startActivity(i);
                        finish();
                        //   }
                        //NewOrderFragment.this.startActivity(i);
                    }
                    else {
//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Preview_order_validation_message), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Preview_order_validation_message),"yes");
                    }
                }
            }
        });

        online_catalog_addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (!Global_Data.GLOvel_GORDER_ID.equalsIgnoreCase("")) {

                    if (Global_Data.Order_hashmap.size() > 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(OnlineCatalogue.this).create(); //Read Update
                        alertDialog.setTitle(getResources().getString(R.string.Warning));
                        alertDialog.setMessage(getResources().getString(R.string.item_add_dialog_message));
                        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Global_Data.Order_hashmap.clear();

                                Global_Data.GLOVEL_LONG_DESC = "";
                                Global_Data.GLOVEL_CATEGORY_SELECTION = "";
                                Global_Data.GLOVEL_ITEM_MRP = "";
                                Global_Data.Search_Category_name = "";

                                Global_Data.Search_Product_name = "";
                                p_id.clear();
                                p_url.clear();
                                p_name.clear();
                                scheme_id.clear();
                                p_mrp.clear();
                                p_q.clear();
                                p_price.clear();
                                p_rp.clear();
                                Global_Data.Order_hashmap.clear();
                                Global_Data.Orderproduct_remarks.clear();
                                p_remarks.clear();
                                p_detail1.clear();
                                p_detail2.clear();
                                p_detail3.clear();
                                p_detail4.clear();
                                p_detail5.clear();
                                Global_Data.Orderproduct_detail1.clear();
                                Global_Data.Orderproduct_detail2.clear();
                                Global_Data.Orderproduct_detail3.clear();
                                Global_Data.Orderproduct_detail4.clear();
                                Global_Data.Orderproduct_detail5.clear();
                                Intent i = new Intent(OnlineCatalogue.this, NewOrderActivity.class);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                startActivity(i);
                                finish();
                            }
                        });

                        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                        alertDialog.show();
                    } else {
                        Global_Data.Order_hashmap.clear();

                        Global_Data.GLOVEL_LONG_DESC = "";
                        Global_Data.GLOVEL_CATEGORY_SELECTION = "";
                        Global_Data.GLOVEL_ITEM_MRP = "";
                        Global_Data.Search_Category_name = "";

                        Global_Data.Search_Product_name = "";
                        p_id.clear();
                        p_url.clear();
                        p_name.clear();
                        scheme_id.clear();
                        p_mrp.clear();
                        p_q.clear();
                        p_price.clear();
                        p_rp.clear();
                        Global_Data.Order_hashmap.clear();
                        Global_Data.Orderproduct_remarks.clear();
                        p_remarks.clear();
                        p_detail1.clear();
                        p_detail2.clear();
                        p_detail3.clear();
                        p_detail4.clear();
                        p_detail5.clear();
                        Global_Data.Orderproduct_detail1.clear();
                        Global_Data.Orderproduct_detail2.clear();
                        Global_Data.Orderproduct_detail3.clear();
                        Global_Data.Orderproduct_detail4.clear();
                        Global_Data.Orderproduct_detail5.clear();
                        Intent i = new Intent(OnlineCatalogue.this, NewOrderActivity.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        startActivity(i);
                        finish();
                    }

                    //NewOrderFragment.this.startActivity(i);
                } else {

                    if (Global_Data.Order_hashmap.size() > 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(OnlineCatalogue.this).create(); //Read Update
                        alertDialog.setTitle(getResources().getString(R.string.Warning));
                        alertDialog.setMessage(getResources().getString(R.string.item_add_dialog_message));
                        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Global_Data.Order_hashmap.clear();

                                Global_Data.GLOVEL_LONG_DESC = "";
                                Global_Data.GLOVEL_CATEGORY_SELECTION = "";
                                Global_Data.GLOVEL_ITEM_MRP = "";
                                Global_Data.Search_Category_name = "";

                                Global_Data.Search_Product_name = "";
                                p_id.clear();
                                p_url.clear();
                                p_name.clear();
                                scheme_id.clear();
                                p_mrp.clear();
                                p_q.clear();
                                p_price.clear();
                                p_rp.clear();
                                Global_Data.Order_hashmap.clear();
                                Global_Data.Orderproduct_remarks.clear();
                                p_remarks.clear();
                                p_detail1.clear();
                                p_detail2.clear();
                                p_detail3.clear();
                                p_detail4.clear();
                                p_detail5.clear();
                                Global_Data.Orderproduct_detail1.clear();
                                Global_Data.Orderproduct_detail2.clear();
                                Global_Data.Orderproduct_detail3.clear();
                                Global_Data.Orderproduct_detail4.clear();
                                Global_Data.Orderproduct_detail5.clear();

                                Intent i = new Intent(OnlineCatalogue.this, NewOrderActivity.class);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                startActivity(i);
                                finish();

                            }
                        });

                        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        alertDialog.show();
                    } else {
                        Global_Data.Order_hashmap.clear();

                        Global_Data.GLOVEL_LONG_DESC = "";
                        Global_Data.GLOVEL_CATEGORY_SELECTION = "";
                        Global_Data.GLOVEL_ITEM_MRP = "";
                        Global_Data.Search_Category_name = "";

                        Global_Data.Search_Product_name = "";
                        p_id.clear();
                        p_url.clear();
                        p_name.clear();
                        scheme_id.clear();
                        p_mrp.clear();
                        p_q.clear();
                        p_price.clear();
                        p_rp.clear();
                        Global_Data.Order_hashmap.clear();
                        Global_Data.Orderproduct_remarks.clear();
                        p_remarks.clear();
                        p_detail1.clear();
                        p_detail2.clear();
                        p_detail3.clear();
                        p_detail4.clear();
                        p_detail5.clear();
                        Global_Data.Orderproduct_detail1.clear();
                        Global_Data.Orderproduct_detail2.clear();
                        Global_Data.Orderproduct_detail3.clear();
                        Global_Data.Orderproduct_detail4.clear();
                        Global_Data.Orderproduct_detail5.clear();
                        Intent i = new Intent(OnlineCatalogue.this, NewOrderActivity.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        startActivity(i);
                        finish();
                    }
                }
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
        //return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add:
                String targetNew="";
                SharedPreferences sp = OnlineCatalogue.this.getSharedPreferences("SimpleLogic", 0);
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
        Global_Data.catalogue_m.clear();
        if (Global_Data.Order_hashmap.size() > 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(OnlineCatalogue.this).create(); //Read Update
            alertDialog.setTitle(getResources().getString(R.string.Warning));
            alertDialog.setMessage(getResources().getString(R.string.ITEM_DISCART_DIALOG_MESSAGE));
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Global_Data.GLOVEL_LONG_DESC = "";
                    Global_Data.GLOVEL_CATEGORY_SELECTION = "";
                    Global_Data.GLOVEL_ITEM_MRP = "";
                    Global_Data.Search_Category_name = "";
                    Global_Data.Search_Product_name = "";
                    p_id.clear();
                    p_url.clear();
                    p_name.clear();
                    scheme_id.clear();
                    p_mrp.clear();
                    p_q.clear();
                    p_price.clear();
                    p_rp.clear();
                    Global_Data.Order_hashmap.clear();
                    Global_Data.Orderproduct_remarks.clear();
                    p_remarks.clear();
                    p_detail1.clear();
                    p_detail2.clear();
                    p_detail3.clear();
                    p_detail4.clear();
                    p_detail5.clear();
                    Global_Data.Orderproduct_detail1.clear();
                    Global_Data.Orderproduct_detail2.clear();
                    Global_Data.Orderproduct_detail3.clear();
                    Global_Data.Orderproduct_detail4.clear();
                    Global_Data.Orderproduct_detail5.clear();
                    Intent i = new Intent(OnlineCatalogue.this, NewOrderActivity.class);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });

            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });


            alertDialog.show();


        } else {
            Global_Data.GLOVEL_LONG_DESC = "";
            Global_Data.GLOVEL_CATEGORY_SELECTION = "";
            Global_Data.GLOVEL_ITEM_MRP = "";
            Global_Data.Search_Category_name = "";
            Global_Data.Search_Product_name = "";
            p_id.clear();
            p_url.clear();
            p_name.clear();
            scheme_id.clear();
            p_mrp.clear();
            p_q.clear();
            p_price.clear();
            p_rp.clear();
            Global_Data.Order_hashmap.clear();
            Global_Data.Orderproduct_remarks.clear();
            p_remarks.clear();
            p_detail1.clear();
            p_detail2.clear();
            p_detail3.clear();
            p_detail4.clear();
            p_detail5.clear();
            Global_Data.Orderproduct_detail1.clear();
            Global_Data.Orderproduct_detail2.clear();
            Global_Data.Orderproduct_detail3.clear();
            Global_Data.Orderproduct_detail4.clear();
            Global_Data.Orderproduct_detail5.clear();
            Intent i = new Intent(OnlineCatalogue.this, NewOrderActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }

    public void Online_Catalogue() {

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
        String device_id = sp.getString("devid", "");
        domain = service_url1;

        domain1 = Cust_domain;

        SharedPreferences spf = OnlineCatalogue.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        Log.d("email_id", "email_id" + user_email);


        if (Global_Data.ProductVariant.trim().equalsIgnoreCase("Select All")) {

            try {
                service_url = domain + "products/get_product_catalogues?email=" +
                        URLEncoder.encode(user_email, "UTF-8") + "&primary_category=" + URLEncoder.encode(Global_Data.Search_Category_name, "UTF-8") + "&sub_category=" + URLEncoder.encode(Global_Data.Search_Product_name, "UTF-8") + "&product_variant=" + "";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //service_url = service_url.replaceAll(" ", "%20");

        } else {

            if (Global_Data.array_of_pVarient.size() > 0) {
            //if (Global_Data.multiSelectVariant.size() > 0) {
                StringBuilder ss = new StringBuilder();
                String[] mStringArray = new String[Global_Data.array_of_pVarient.size()];
                mStringArray = Global_Data.array_of_pVarient.toArray(mStringArray);
                for (int i = 0; i < Global_Data.array_of_pVarient.size(); i++) {

                    ss.append(Global_Data.array_of_pVarient.get(i));
                    if ((Global_Data.array_of_pVarient.size() - 1) != i) {
                        ss.append(",");
                    }
                }

                Global_Data.ProductVariant=ss.toString();
                try {
                    service_url = domain + "products/get_product_catalogues?email=" + URLEncoder.encode(user_email, "UTF-8") + "&primary_category=" + URLEncoder.encode(Global_Data.Search_Category_name, "UTF-8") + "&sub_category=" + URLEncoder.encode(Global_Data.Search_Product_name, "UTF-8") + "&product_variant=" + Global_Data.ProductVariant;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
//            if(Global_Data.multiSelectVariant.size()>0)
//            {
//                try {
//                    service_url = domain + "products/get_product_catalogues?imei_no=" + device_id + "&email=" + URLEncoder.encode(user_email, "UTF-8") + "&primary_category=" + URLEncoder.encode(Global_Data.Search_Category_name, "UTF-8") + "&sub_category=" + URLEncoder.encode(Global_Data.Search_Product_name, "UTF-8") + "&product_variant=" + URLEncoder.encode(Global_Data.ProductVariant, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }

            else{
                try {
                    service_url = domain + "products/get_product_catalogues?email=" + URLEncoder.encode(user_email, "UTF-8") + "&primary_category=" + URLEncoder.encode(Global_Data.Search_Category_name, "UTF-8") + "&sub_category=" + URLEncoder.encode(Global_Data.Search_Product_name, "UTF-8") + "&product_variant=" + URLEncoder.encode(Global_Data.ProductVariant, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
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
                                if (json.has("message")) {
                                    response_result = json.getString("message");

//                                    Toast toast = Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(getApplicationContext(), response_result,"yes");

                                    Intent launch = new Intent(OnlineCatalogue.this, NewOrderActivity.class);
                                    startActivity(launch);
                                    finish();
                                } else {

                                    JSONArray products = json.getJSONArray("products");
                                    Global_Data.products = json.getJSONArray("products");
//
                                    Log.i("volley", "response reg products Length: " + products.length());
//
                                    Log.d("users", "products" + products.toString());
//
                                    catalogue_m.clear();
                                    //Global_Data.imageUrlArr1.clear();
                                    pp = 0.0;

                                    if (products.length() > 0) {
                                        for (int i = 0; i < products.length(); i++) {
                                            JSONObject object = products.getJSONObject(i);
                                            Catalogue_model catalogue_model = new Catalogue_model();
                                            SwipeImage swipeImageModel = new SwipeImage();

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
                                            catalogue_model.setItem_description(object.getString("description"));

                                            JSONArray product_catalogues = object.getJSONArray("product_catalogues");

                                            //catalogue_model.setImageUrlArr11((List<String>) product_catalogues);
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

                                           // String[] tempArray = new String[product_catalogues.length()];
//                                            for (int j = 0; j < product_catalogues.length(); j++) {
//                                                JSONObject image_object = product_catalogues.getJSONObject(j);
//                                                //swipeImageModel.setImage(finalDomain + image_object.getString("thumb_url"));
//                                                swipeImageModel.setImage(finalDomain + image_object.getString("thumb_url"));
//                                                Global_Data.imageUrlArr1.add((finalDomain + image_object.getString("thumb_url")));
//                                                //tempArray[i] = myArray [i];
//                                                // swipeImageModel.setImage("https://demonuts.com/Demonuts/SampleImages/W-03.JPG");
//                                                //catalogue_model.setItem_image_url("http://f59c3827.ngrok.io"+"/"+image_object.getString("thumb_url"));
//                                                //catalogue_model.setItem_image_url(image_object.getString("thumb_url"));
//                                            }

                                            List<Local_Data> contactsn = dbvoc.GetOrder_Product_BY_ORDER_ID(Global_Data.GLObalOrder_id, object.getString("code"));
                                            if (contactsn.size() > 0) {
                                                for (Local_Data cn : contactsn) {
                                                    catalogue_model.setItem_quantity(cn.get_delivery_product_order_quantity());
                                                  //  catalogue_model.setItem_amount(getResources().getString(R.string.PRICE) + cn.getAmount());
                                                    catalogue_model.setItem_amount("₹"  + cn.getAmount());


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
                                                       // Global_Data.GrandTotal+= Double.valueOf(cn.getAmount());
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

                                            //swipeimg_catalogue_m.add(swipeImageModel);
                                            catalogue_m.add(catalogue_model);

                                        }

                                        //Intent launch = new Intent(context,Youtube_Player_Activity.class);
                                        //startActivity(launch);

                                        Global_Data.catalogue_m = catalogue_m;
                                        //Global_Data.imageUrlArr = swipeimg_catalogue_m;
                                        pDialog.hide();
                                        mAdapter.notifyDataSetChanged();

                                        if (ttl_price.length() > 0) {
                                            Global_Data.GrandTotal=pp;
                                            txttotalPreview.setText(ttl_price + ":"+ " ₹" + pp);
                                        } else {
                                            Global_Data.GrandTotal=pp;
                                            txttotalPreview.setText(getResources().getString(R.string.Total) + " ₹"+pp);
                                        }

                                    } else {
                                        pDialog.hide();
                                        // Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG).show();

//                                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.product_not_found_message), Toast.LENGTH_LONG);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();
                                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.product_not_found_message),"yes");

                                        Intent launch = new Intent(OnlineCatalogue.this, NewOrderActivity.class);
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


//                                Toast toast = Toast.makeText(OnlineCatalogue.this,
//                                        "Service Error",
//                                        Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(OnlineCatalogue.this,
                                        "Service Error","yes");
                                Intent launch = new Intent(OnlineCatalogue.this, MainActivity.class);
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

//                            Toast toast = Toast.makeText(OnlineCatalogue.this,
//                                    "Network Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(OnlineCatalogue.this,
                                    "Network Error","");
                        } else if (error instanceof AuthFailureError) {

//                            Toast toast = Toast.makeText(OnlineCatalogue.this,
//                                    "Server AuthFailureError  Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(OnlineCatalogue.this,
                                    "Server AuthFailureError  Error","");
                        } else if (error instanceof ServerError) {

//                            Toast toast = Toast.makeText(OnlineCatalogue.this,
//                                    getResources().getString(R.string.Server_Errors),
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(OnlineCatalogue.this,
                                    getResources().getString(R.string.Server_Errors),"");
                        } else if (error instanceof NetworkError) {

//                            Toast toast = Toast.makeText(OnlineCatalogue.this,
//                                    "Network   Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(OnlineCatalogue.this,
                                    "Network   Error","");
                        } else if (error instanceof ParseError) {


//                            Toast toast = Toast.makeText(OnlineCatalogue.this,
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(OnlineCatalogue.this,
                                    "ParseError   Error","");
                        } else {
                            // Toast.makeText(Image_Gellary.this, error.getMessage(), Toast.LENGTH_LONG).show();
//
//                            Toast toast = Toast.makeText(OnlineCatalogue.this, error.getMessage(), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(OnlineCatalogue.this, error.getMessage(),"yes");
                        }
                        Intent launch = new Intent(OnlineCatalogue.this, MainActivity.class);
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

        RequestQueue requestQueue = Volley.newRequestQueue(OnlineCatalogue.this);

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

    public void onButtonClickListner(int position) {
        int pos = position;
//        String value = value1;
//        String valuen = value2;
//        String name = value3;

        try {
            getMethod();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getMethod() {
        final Dialog openDialog = new Dialog(OnlineCatalogue.this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setCancelable(false);
        openDialog.setContentView(R.layout.catalogue_dialog);

        ImageView dialogClose = (ImageView) openDialog.findViewById(R.id.close_btn);

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                openDialog.dismiss();
            }
        });

        openDialog.show();
    }

    private class Varientsave extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... response) {

            try {

                OnlineCatalogue.this.runOnUiThread(new Runnable() {
                    public void run() {
                        View parentView = null;

                        if (!(Global_Data.Order_hashmap.isEmpty())) {

                            try {
                                for (Object name : Global_Data.Order_hashmap.keySet()) {

                                    Object key = name.toString();
                                    Object value = Global_Data.Order_hashmap.get(name);
                                    Object value_remarks = Global_Data.Orderproduct_remarks.get(name);
                                    Object value_detail1 = Global_Data.Orderproduct_detail1.get(name);
                                    Object value_detail2 = Global_Data.Orderproduct_detail2.get(name);
                                    Object value_detail3 = Global_Data.Orderproduct_detail3.get(name);
                                    Object value_detail4 = Global_Data.Orderproduct_detail4.get(name);
                                    Object value_detail5 = Global_Data.Orderproduct_detail5.get(name);
                                    //System.out.println(key + " " + value);
                                    Log.d("KEY", "Key: " + key + " Value: " + value);
                                    JSONObject item = new JSONObject();

                                    String key_array[] = String.valueOf(key).split("&");
                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value))) {
                                        String key_value_array[] = String.valueOf(value).split("pq");
                                        String key_value_price_array[] = key_value_array[1].split("pprice");
                                        String key_value_pname_array[] = key_value_price_array[1].split("pmrp");
                                        String key_value_pmrp_array[] = key_value_pname_array[1].split("prp");
                                        String key_value_schemeid_array[] = key_value_pmrp_array[1].split("sid");
                                        String key_value_url_array[] = key_value_schemeid_array[1].split("url");

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(key_value_price_array[0])) {
                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_remarks))) {
                                                p_remarks.add(String.valueOf(value_remarks));
                                            } else {
                                                p_remarks.add("");
                                            }

                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail1))) {
                                                p_detail1.add(String.valueOf(value_detail1));
                                            } else {
                                                p_detail1.add("");
                                            }

                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail2))) {
                                                p_detail2.add(String.valueOf(value_detail2));
                                            } else {
                                                p_detail2.add("");
                                            }

                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail3))) {
                                                p_detail3.add(String.valueOf(value_detail3));
                                            } else {
                                                p_detail3.add("");
                                            }

                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail4))) {
                                                p_detail4.add(String.valueOf(value_detail4));
                                            } else {
                                                p_detail4.add("");
                                            }

                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail5))) {
                                                p_detail5.add(String.valueOf(value_detail5));
                                            } else {
                                                p_detail5.add("");
                                            }

                                            q_check = "yes";
                                            p_id.add(key_array[1]);
                                            p_q.add(key_value_array[0]);
                                            p_price.add(key_value_price_array[0]);
                                            p_name.add(key_value_pname_array[0]);
                                            scheme_id.add(key_value_url_array[0]);
                                            p_mrp.add(key_value_pmrp_array[0]);
                                            p_rp.add(key_value_schemeid_array[0]);
                                            p_url.add(key_value_url_array[1]);

                                            Log.d("quantity", "quantity" + key_value_array[0]);
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                // dialog.dismiss();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            SharedPreferences spf = OnlineCatalogue.this.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL",null);

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            //dialog.dismiss();

            if (!p_id.isEmpty() && q_check.equalsIgnoreCase("yes")) {

                Long randomPIN = System.currentTimeMillis();
                String PINString = String.valueOf(randomPIN);
                if (Global_Data.GLOvel_GORDER_ID.equalsIgnoreCase("")) {
                    if (Global_Data.sales_btnstring.equalsIgnoreCase("Secondary Sales / Retail Sales")) {
                        Global_Data.GLObalOrder_id = "Ord" + PINString;
                        Global_Data.GLOvel_GORDER_ID = "Ord" + PINString;
                    } else {
                        Global_Data.GLObalOrder_id = "QNO" + PINString;
                        Global_Data.GLOvel_GORDER_ID = "QNO" + PINString;
                    }

                    try {
                        AppLocationManager appLocationManager = new AppLocationManager(OnlineCatalogue.this);
                        Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                        Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                        PlayService_Location PlayServiceManager = new PlayService_Location(OnlineCatalogue.this);

                        if (PlayServiceManager.checkPlayServices(OnlineCatalogue.this)) {
                            Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                        } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                            Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                            Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    List<Local_Data> checkq = dbvoc.checkOrderExist(Global_Data.GLOvel_CUSTOMER_ID, Global_Data.GLObalOrder_id);

                    if (checkq.size() <= 0) {
                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time =&gt; " + c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        final String formattedDaten = df.format(c.getTime());

                        SharedPreferences spf1 = getSharedPreferences("SimpleLogic", 0);
                        String  shopname = spf1.getString("shopname", "");


                        try {

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LATITUDE) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LONGITUDE)) {
                                LocationAddress locationAddress = new LocationAddress();
                                LocationAddress.getAddressFromLocation(Double.valueOf(Global_Data.GLOvel_LATITUDE), Double.valueOf(Global_Data.GLOvel_LONGITUDE),
                                        getApplicationContext(), new Global_Data.GeocoderHandler());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();

                        }

                        loginDataBaseAdapter.insertOrders("", Global_Data.GLOvel_GORDER_ID, Global_Data.GLOvel_CUSTOMER_ID, Global_Data.order_retailer, user_email, Global_Data.order_city, Global_Data.order_beat, Global_Data.address, "", "", "", "", "", formattedDaten, "", shopname, Global_Data.order_state, Global_Data.order_city, Global_Data.sales_btnstring, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Glovel_BEAT_ID, "", "", "", "", "", "", "");
                    }
                }


                Double pp = 0.0;
                try {
                    for (int k = 0; k < p_id.size(); k++) {

                        List<Local_Data> contactsn = dbvoc.GetOrders_BY_ORDER_ID(Global_Data.GLObalOrder_id, p_id.get(k));

                        if (contactsn.size() > 0) {

                            for (Local_Data cn : contactsn) {

//                                if(Global_Data.Varient_value_add_flag.equalsIgnoreCase("yes"))
//                                {

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                                    dbvoc.update_itemamountandquantity_withremarks(String.valueOf(p_q.get(k)), String.valueOf(p_price.get(k)), p_id.get(k), Global_Data.GLObalOrder_id, p_remarks.get(k), p_detail1.get(k), p_url.get(k), p_detail2.get(k), p_detail3.get(k), p_detail4.get(k), p_detail5.get(k));
                                } else if (!(p_q.get(k).equalsIgnoreCase(cn.get_delivery_product_order_quantity())) && !(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k)))) {
                                    dbvoc.getDeleteTableorderproduct_byITEM_NUMBER(p_id.get(k), Global_Data.GLObalOrder_id);
                                }

//                                }
//                                else
//                                {
//                                    int quantity = Integer.parseInt(cn.get_delivery_product_order_quantity()) + Integer.parseInt(p_q.get(k));
//                                    Double amount = Double.valueOf(cn.getAmount()) + Double.valueOf(p_price.get(k));
//                                    dbvoc.update_itemamountandquantity(String.valueOf(quantity), String.valueOf(amount), p_id.get(k), Global_Data.GLObalOrder_id);
//                                }

                            }
                        } else {

                            String schemeid= scheme_id.get(k);

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                                loginDataBaseAdapter.insertOrderProducts(" ", " ", Global_Data.GLOvel_GORDER_ID, "", Global_Data.Search_Category_name, Global_Data.Search_Product_name, p_name.get(k), " ", schemeid, " ", "", p_q.get(k), p_rp.get(k), p_mrp.get(k), p_price.get(k), "", "", Global_Data.order_retailer, " ", p_id.get(k), " ", p_name.get(k), p_remarks.get(k), p_detail1.get(k), p_url.get(k), p_detail2.get(k), p_detail3.get(k), p_detail4.get(k), p_detail5.get(k));//Reading all

                                // Log.d("pPRIZE","Pprize"+ p_price.get(k));
                            }


                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                            pp += Double.valueOf(p_price.get(k));
                            //Global_Data.GrandTotal+= Double.valueOf(p_price.get(k));
                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                online_catalog_save.setEnabled(true);
                online_catalog_save.setText(getResources().getString(R.string.Save));

                if (ttl_price.length() > 0) {
                    Global_Data.GrandTotal=pp;
                    txttotalPreview.setText(ttl_price + ":" + " ₹"+ pp);
                } else {
                    Global_Data.GrandTotal=pp;
                    txttotalPreview.setText(getResources().getString(R.string.CTotal) +  " ₹"+pp);
                }

                Global_Data.Varient_value_add_flag = "yes";

                List<Local_Data> checkq = dbvoc.getItemName(Global_Data.GLObalOrder_id);

                if (checkq.size() <= 0) {
                    q_check = "";
                    Global_Data.Order_hashmap.clear();
                    p_id.clear();
                    p_url.clear();
                    p_q.clear();
                    p_price.clear();
                    p_name.clear();
                    scheme_id.clear();
                    p_mrp.clear();
                    p_rp.clear();
                    Global_Data.Orderproduct_remarks.clear();
                    p_remarks.clear();
                    p_detail1.clear();
                    p_detail2.clear();
                    p_detail3.clear();
                    p_detail4.clear();
                    p_detail5.clear();
                    // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();
                    Global_Data.Orderproduct_detail1.clear();
                    Global_Data.Orderproduct_detail2.clear();
                    Global_Data.Orderproduct_detail3.clear();
                    Global_Data.Orderproduct_detail4.clear();
                    Global_Data.Orderproduct_detail5.clear();
                    OnlineCatalogue.this.runOnUiThread(new Runnable() {
                        public void run() {
//                            Toast toast = Toast.makeText(OnlineCatalogue.this, getResources().getString(R.string.All_item_delete), Toast.LENGTH_SHORT);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(OnlineCatalogue.this, getResources().getString(R.string.All_item_delete),"yes");
                            // Product_Variant.setText("");

                            Intent i = new Intent(OnlineCatalogue.this, NewOrderActivity.class);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    });

                } else {
                    pp = 0.0;
                    for (Local_Data qtr : checkq) {
                        pp += Double.valueOf(qtr.getAmount());
                        //Global_Data.GrandTotal += Double.valueOf(qtr.getAmount());
                    }

                    if (ttl_price.length() > 0) {
                        Global_Data.GrandTotal=pp;
                        txttotalPreview.setText(ttl_price + ":" + " ₹"+ pp);
                    } else {
                        Global_Data.GrandTotal=pp;
                        txttotalPreview.setText(getResources().getString(R.string.CTotal) + " ₹"+ pp);
                    }

                    q_check = "";
                    Global_Data.Order_hashmap.clear();

//                    if(!Product_Variant.getText().toString().equalsIgnoreCase(""))
//                    {
//                        Product_Variant.setText("");
//                    }

                    p_id.clear();
                    p_url.clear();
                    p_q.clear();
                    p_price.clear();
                    p_name.clear();
                    scheme_id.clear();
                    p_mrp.clear();
                    p_rp.clear();
                    Global_Data.Orderproduct_remarks.clear();
                    p_remarks.clear();
                    p_detail1.clear();
                    p_detail2.clear();
                    p_detail3.clear();
                    p_detail4.clear();
                    p_detail5.clear();
//                    Toast toast = Toast.makeText(OnlineCatalogue.this, getResources().getString(R.string.Item_added_successfully), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(OnlineCatalogue.this, getResources().getString(R.string.Item_added_successfully),"yes");
                    Global_Data.Orderproduct_detail1.clear();
                    Global_Data.Orderproduct_detail2.clear();
                    Global_Data.Orderproduct_detail3.clear();
                    Global_Data.Orderproduct_detail4.clear();
                    Global_Data.Orderproduct_detail5.clear();
//                    catalogue_m = new ArrayList<>();
//                    mAdapter = new CatalogueAdapter(getApplicationContext(), catalogue_m, OnlineCatalogue.this);
//
//                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
//                    recyclerView.setLayoutManager(mLayoutManager);
//                    recyclerView.setItemAnimator(new DefaultItemAnimator());
//                    recyclerView.setAdapter(mAdapter);
//                    Online_Catalogue();

                    int a = catalogue_m.size();

                    for (int i = 0; i < a; i++) {
                        Catalogue_model catalogue_mm = catalogue_m.get(i);
                        List<Local_Data> contactsn = dbvoc.GetOrder_Product_BY_ORDER_ID(Global_Data.GLObalOrder_id, catalogue_mm.getItem_number());
                        if (contactsn.size() > 0) {
                            for (Local_Data cn : contactsn) {
                                catalogue_mm.setItem_quantity(cn.get_delivery_product_order_quantity());
                               // catalogue_mm.setItem_amount(getResources().getString(R.string.PRICE) + cn.getAmount());
                                catalogue_mm.setItem_amount("₹" + cn.getAmount());

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getRemarks())) {
                                    catalogue_mm.setItem_remarks(cn.getRemarks());
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail1())) {
                                    catalogue_mm.setDetail1(cn.getDetail1());
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail2())) {
                                    catalogue_mm.setDetail2(cn.getDetail2());
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail3())) {
                                    catalogue_mm.setDetail3(cn.getDetail3());
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail4())) {
                                    catalogue_mm.setDetail4(cn.getDetail4());
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail5())) {
                                    catalogue_mm.setDetail5(cn.getDetail5());
                                }

                            }
                        } else {
                            catalogue_mm.setItem_quantity("");
                            catalogue_mm.setItem_amount("");
                            catalogue_mm.setItem_remarks("");
                            catalogue_mm.setDetail1("");
                            catalogue_mm.setDetail2("");
                            catalogue_mm.setDetail3("");
                            catalogue_mm.setDetail4("");
                            catalogue_mm.setDetail5("");
                        }

                        catalogue_m.set(i, catalogue_mm);
                    }

                    Global_Data.catalogue_m = catalogue_m;
//                    mAdapter.notifyDataSetChanged();
                }
            }
            else {
                Global_Data.Orderproduct_detail1.clear();
                Global_Data.Orderproduct_detail2.clear();
                Global_Data.Orderproduct_detail3.clear();
                Global_Data.Orderproduct_detail4.clear();
                Global_Data.Orderproduct_detail5.clear();
                q_check = "";
                Global_Data.Order_hashmap.clear();
                p_id.clear();
                p_url.clear();
                p_q.clear();
                p_price.clear();
                p_name.clear();
                scheme_id.clear();
                p_mrp.clear();
                p_rp.clear();
                Global_Data.Orderproduct_remarks.clear();
                p_remarks.clear();
                p_detail1.clear();
                p_detail2.clear();
                p_detail3.clear();
                p_detail4.clear();
                p_detail5.clear();
                online_catalog_save.setEnabled(true);
                online_catalog_save.setText(getResources().getString(R.string.Save));
//                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_enter_quantity), Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_enter_quantity),"yes");
            }


            OnlineCatalogue.this.runOnUiThread(new Runnable() {
                public void run() {
                    //dialog.dismiss();
                }
            });

        }

        @Override
        protected void onPreExecute() {


            OnlineCatalogue.this.runOnUiThread(new Runnable() {
                public void run() {


//          dialog.setMessage("Please wait....");
//          dialog.setTitle("Siyaram App");
//          dialog.setCancelable(false);
//          dialog.show();

                    online_catalog_save.setEnabled(false);
                    online_catalog_save.setText(getResources().getString(R.string.Please_Wait));
                    //int pic = R.drawable.round_btngray;
                    // buttonPreviewAddMOre.setBackgroundResource(pic);
                }
            });
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        ttl_price = spf1.getString("var_total_price", "");

        if (ttl_price.length() > 0) {
            txttotalPreview.setText(ttl_price + ":" + " ₹"+ Global_Data.GrandTotal);
        } else {
            txttotalPreview.setText(getResources().getString(R.string.CTotal) + " ₹"+ Global_Data.GrandTotal);
        }
    }

    public void slideCall(int position) {
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("images", catalogue_m);
//        bundle.putInt("position", position);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        OnlineCatalogue_Slides newFragment = OnlineCatalogue_Slides.newInstance();
//        newFragment.setArguments(bundle);
//        newFragment.show(ft, "slideshow");
    }


}
