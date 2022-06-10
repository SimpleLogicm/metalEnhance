package com.msimplelogic.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.msimplelogic.activities.R;
import com.msimplelogic.model.Distributor_mine;
import com.msimplelogic.model.Product;
import com.msimplelogic.swipelistview.sample.adapters.Product_AllVarient_P_Adapter;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Product_All_P_Varients extends Activity {

    int pp = 0;
    String q_check = "";
    ProgressDialog dialog;
    String str;
    HashMap<String, String> map;
    ArrayList<String> list1 = new ArrayList<String>();
    ArrayList<String> list2 = new ArrayList<String>();
    ArrayList<String> list3 = new ArrayList<String>();
    ArrayList<String> list4 = new ArrayList<String>();
    ArrayList<String> list5 = new ArrayList<String>();
    ArrayList<String> list6 = new ArrayList<String>();
    ArrayList<String> list7 = new ArrayList<String>();
    ArrayList<String> list8 = new ArrayList<String>();
    ArrayList<String> list9 = new ArrayList<String>();
    Boolean isInternetPresent = false;

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
    ConnectionDetector cd;
    ArrayList<HashMap<String, String>> SwipeList;
    ArrayList<String> Amount_tp = new ArrayList<String>();
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    private static final int REQUEST_CODE_SETTINGS = 0;
    private ArrayList<String> Distributer_list = new ArrayList<String>();
    private Product_AllVarient_P_Adapter adapter;
    private ArrayList<Product> dataOrder;
    LoginDataBaseAdapter loginDataBaseAdapter;
    private ListView swipeListView;
    TextView textView1, tabletextview1, tabletextview2, tabletextview3;
    public static TextView txttotalPreview;
    static final String TAG_ITEMNAME = "product_name";
    static final String TAG_QTY = "total_qty";
    static final String TAG_PRICE = "MRP";
    static final String TAG_RP = "RP";
    static final String TAG_AMOUNT = "amount";
    static final String TAG_ITEM_NUMBER = "item_number";
    static final String TAG_MINQTY = "product_minqty";
    static final String TAG_PKGQTY = "product_pkgqty";
    static final String TAG_REMARKS = "product_remarks";

    ImageView imgView;
    static float totalPrice;
    String statusOrderActivity = "";
    Button buttonPreviewCheckout, buttonPreviewCheckout1, buttonPreviewAddMOre, buttonPreviewHome, addmorenews;
    public static final int SIGNATURE_ACTIVITY = 1;

    ArrayList<Distributor_mine> dataDistrubutors = new ArrayList<Distributor_mine>();
    List<String> listDistrubutor;
    HashMap<String, String> distrubutorsMap;
    ArrayAdapter<String> dataDistrubutorsAdapter;
    boolean firstLaunch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_all_varients);

        cd = new ConnectionDetector(getApplicationContext());

        Global_Data.Varient_value_add_flag = "";

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        txttotalPreview = (TextView) findViewById(R.id.txttotalPreviewv);

        buttonPreviewAddMOre = (Button) findViewById(R.id.buttonPreviewAddMOrev);
        buttonPreviewHome = (Button) findViewById(R.id.buttonPreviewv);
        addmorenews = (Button) findViewById(R.id.addmorenews);
        swipeListView = (ListView) findViewById(R.id.example_lv_list);
        swipeListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        map = new HashMap<String, String>();

        //txttotalPreview.setText("Total		:		"+"");

        SwipeList = new ArrayList<HashMap<String, String>>();

        SharedPreferences spf = Product_All_P_Varients.this.getSharedPreferences("SimpleLogic", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("order", "new");
        editor.commit();
        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText(getResources().getString(R.string.Product_List));

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = Product_All_P_Varients.this.getSharedPreferences("SimpleLogic", 0);

//        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//        	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//		}
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


            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }



        dialog = new ProgressDialog(Product_All_P_Varients.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(getResources().getString(R.string.customer_loading_dialog_message));
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        new VarientASN().execute();

        buttonPreviewAddMOre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                String buttonText = ((Button) arg0).getText().toString();

                if (buttonText.equalsIgnoreCase("Add More")) {
                    p_id.clear();
                    p_name.clear();
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


                    Intent i = new Intent(Product_All_P_Varients.this, Previous_orderNew_S1.class);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();

                } else {
                    p_id.clear();
                    p_name.clear();
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

                    new Varientsave().execute();

                }


            }
        });


        addmorenews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (!Global_Data.GLOvel_GORDER_ID.equalsIgnoreCase("")) {

                    if (Global_Data.Order_hashmap.size() > 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(Product_All_P_Varients.this).create(); //Read Update
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
                                p_name.clear();
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
                                Intent i = new Intent(Product_All_P_Varients.this, Previous_orderNew_S1.class);
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
                        p_name.clear();
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
                        Intent i = new Intent(Product_All_P_Varients.this, Previous_orderNew_S1.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        startActivity(i);
                        finish();
                    }


                    //NewOrderFragment.this.startActivity(i);
                } else {

                    if (Global_Data.Order_hashmap.size() > 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(Product_All_P_Varients.this).create(); //Read Update
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
                                p_name.clear();
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

                                Intent i = new Intent(Product_All_P_Varients.this, Previous_orderNew_S1.class);
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
                        Global_Data.Orderproduct_remarks.clear();
                        p_remarks.clear();
                        p_detail1.clear();
                        p_detail2.clear();
                        p_detail3.clear();
                        p_detail4.clear();
                        p_detail5.clear();
                        p_name.clear();
                        p_mrp.clear();
                        p_q.clear();
                        p_price.clear();
                        p_rp.clear();
                        Global_Data.Order_hashmap.clear();
                        Global_Data.Orderproduct_detail1.clear();
                        Global_Data.Orderproduct_detail2.clear();
                        Global_Data.Orderproduct_detail3.clear();
                        Global_Data.Orderproduct_detail4.clear();
                        Global_Data.Orderproduct_detail5.clear();

                        Intent i = new Intent(Product_All_P_Varients.this, Previous_orderNew_S1.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        startActivity(i);
                        finish();
                    }


                }

            }
        });

        buttonPreviewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (!Global_Data.GLOvel_GORDER_ID.equalsIgnoreCase("")) {

//                    if (p_id.isEmpty()) {
//                        AlertDialog alertDialog = new AlertDialog.Builder(Product_All_P_Varients.this).create(); //Read Update
//                        alertDialog.setTitle("Warning");
//                        alertDialog.setMessage("Are you sure you want to preview order without saving current list items?");
//                        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                Global_Data.GLOVEL_LONG_DESC = "";
//                                Global_Data.GLOVEL_CATEGORY_SELECTION = "";
//                                Global_Data.GLOVEL_ITEM_MRP = "";
//                                Global_Data.Search_Category_name = "";
//
//                                Global_Data.Search_Product_name = "";
//
//                                Intent i = new Intent(Product_All_P_Varients.this, Previous_orderNew_S2.class);
//                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                                startActivity(i);
//                                finish();
//                            }
//                        });
//
//                        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//
//
//                        alertDialog.show();
//                    } else {
                    Global_Data.GLOVEL_LONG_DESC = "";
                    Global_Data.GLOVEL_CATEGORY_SELECTION = "";
                    Global_Data.GLOVEL_ITEM_MRP = "";
                    Global_Data.Search_Category_name = "";
                    Global_Data.Order_hashmap.clear();
                    Global_Data.Search_Product_name = "";
                    p_remarks.clear();
                    p_detail1.clear();
                    p_detail2.clear();
                    p_detail3.clear();
                    p_detail4.clear();
                    p_detail5.clear();
                    p_name.clear();
                    p_mrp.clear();
                    p_q.clear();
                    p_price.clear();
                    p_rp.clear();


                    Global_Data.Orderproduct_detail1.clear();
                    Global_Data.Orderproduct_detail2.clear();
                    Global_Data.Orderproduct_detail3.clear();
                    Global_Data.Orderproduct_detail4.clear();
                    Global_Data.Orderproduct_detail5.clear();
                    Global_Data.Orderproduct_remarks.clear();

                    Intent i = new Intent(Product_All_P_Varients.this, Previous_orderNew_S2.class);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    startActivity(i);
                    finish();
                    //   }


                    //NewOrderFragment.this.startActivity(i);
                } else {

//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Preview_order_validation_message), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Preview_order_validation_message),"yes");
                }

            }
        });


    }


    private class VarientASN extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... response) {

            try {


                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.Search_Category_name) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.Search_Product_name) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.ProductVariant)) {

                    List<Local_Data> cont1;

                    if (Global_Data.ProductVariant.trim().equalsIgnoreCase("Select All")) {
                        cont1 = dbvoc.getProductvarientbycategoryandproduct(Global_Data.Search_Category_name, Global_Data.Search_Product_name);

                    } else {
                        cont1 = dbvoc.getProductvarientbycategoryandproductandvarant(Global_Data.Search_Category_name, Global_Data.Search_Product_name, Global_Data.ProductVariant);

                    }

                    if (cont1.size() <= 0) {
                        // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

                        Product_All_P_Varients.this.runOnUiThread(new Runnable() {
                            public void run() {
//                                Toast toast = Toast.makeText(Product_All_P_Varients.this, getResources().getString(R.string.Sorry_No_Record_Found), Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(Product_All_P_Varients.this, getResources().getString(R.string.Sorry_No_Record_Found),"yes");

                                Intent i = new Intent(Product_All_P_Varients.this, Previous_orderNew_S1.class);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                        });

                    } else {
                        // resultsvarient.clear();
                        SwipeList.clear();
                        list1.clear();
                        list2.clear();
                        list4.clear();
                        list3.clear();
                        list5.clear();
                        list6.clear();
                        list7.clear();
                        list8.clear();
                        list9.clear();
                        pp = 0;
                        for (Local_Data cnt1 : cont1) {
                            HashMap<String, String> mapp = new HashMap<String, String>();
                            mapp.put(TAG_ITEMNAME, cnt1.getProduct_nm());
                            mapp.put(TAG_QTY, "");
                            mapp.put(TAG_PRICE, cnt1.getMRP());
                            mapp.put(TAG_RP, cnt1.getStateName());
                            mapp.put(TAG_ITEM_NUMBER, cnt1.getCode());
                            mapp.put(TAG_MINQTY, cnt1.getMinqty());
                            mapp.put(TAG_PKGQTY, cnt1.getPkgqty());
                            Log.d("ITEM_NUMBER N", "ITEM_NUMBER N" + cnt1.getCode());

                            List<Local_Data> contactsn = dbvoc.GetOrder_Product_BY_ORDER_ID(Global_Data.GLObalOrder_id, cnt1.getCode());


                            if (contactsn.size() > 0) {
                                for (Local_Data cn : contactsn) {

                                    list1.add(cn.get_delivery_product_order_quantity());
                                    list2.add(getResources().getString(R.string.PRICE) + cn.getAmount());
                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getRemarks())) {

                                        list4.add(cn.getRemarks());
                                    } else {
                                        list4.add("");
                                    }
                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail1())) {
                                        list5.add(cn.getDetail1());
                                    }
                                    {
                                        list5.add("");
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail2())) {
                                        list6.add(cn.getDetail2());
                                    }
                                    {
                                        list6.add("");
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail3())) {
                                        list7.add(cn.getDetail3());
                                    }
                                    {
                                        list7.add("");
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail4())) {
                                        list8.add(cn.getDetail4());
                                    }
                                    {
                                        list8.add("");
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail5())) {
                                        list9.add(cn.getDetail5());
                                    }
                                    {
                                        list9.add("");
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getAmount())) {
                                        pp += Double.valueOf(cn.getAmount());
                                    }
                                }
                            } else {
                                list1.add("");
                                list2.add("");
                                list4.add("");
                                list5.add("");
                                list6.add("");
                                list7.add("");
                                list8.add("");
                                list9.add("");
                                mapp.put(TAG_REMARKS, "");
                            }

                            List<Local_Data> contactsn1 = dbvoc.GetSchemeByCode(cnt1.getCode());
                            if (contactsn1.size() > 0) {
                                for (Local_Data cn : contactsn1) {

                                    list3.add(getResources().getString(R.string.OScheme) + cn.getDisplayName());

                                }
                            } else {
                                list3.add("");

                            }

                            SwipeList.add(mapp);
                        }

                        Product_All_P_Varients.this.runOnUiThread(new Runnable() {
                            public void run() {
                                swipeListView.setItemsCanFocus(true);

                                adapter = new Product_AllVarient_P_Adapter(Product_All_P_Varients.this, SwipeList, list1, list2, list3, list4, list5, list6, list7, list8, list9);

                                txttotalPreview.setText(getResources().getString(R.string.CTotal) + pp);
                                swipeListView.setAdapter(adapter);


                            }
                        });



                    }
                } else {

                    List<Local_Data> cont1 = dbvoc.getProductvarient();

                    if (cont1.size() <= 0) {
                        // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

                        Product_All_P_Varients.this.runOnUiThread(new Runnable() {
                            public void run() {
//                                Toast toast = Toast.makeText(Product_All_P_Varients.this, getResources().getString(R.string.Sorry_No_Record_Found), Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(Product_All_P_Varients.this, getResources().getString(R.string.Sorry_No_Record_Found),"yes");

                                Intent i = new Intent(Product_All_P_Varients.this, Previous_orderNew_S1.class);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                        });

                    } else {

                        SwipeList.clear();
                        list1.clear();
                        list2.clear();
                        list3.clear();
                        list4.clear();
                        list5.clear();
                        list6.clear();
                        list7.clear();
                        list8.clear();
                        list9.clear();
                        pp = 0;
                        for (Local_Data cnt1 : cont1) {
                            HashMap<String, String> mapp = new HashMap<String, String>();
                            mapp.put(TAG_ITEMNAME, cnt1.getProduct_nm() + " RP : " + cnt1.getStateName() + " MRP : " + cnt1.getMRP());
                            mapp.put(TAG_QTY, "");
                            mapp.put(TAG_PRICE, cnt1.getMRP());
                            mapp.put(TAG_ITEM_NUMBER, cnt1.getCode());
                            mapp.put(TAG_MINQTY, cnt1.getMinqty());
                            mapp.put(TAG_PKGQTY, cnt1.getPkgqty());
                            Log.d("ITEM_NUMBER N", "ITEM_NUMBER N" + cnt1.getCode());

                            List<Local_Data> contactsn = dbvoc.GetOrder_Product_BY_ORDER_ID(Global_Data.GLObalOrder_id, cnt1.getCode());

                            if (contactsn.size() > 0) {
                                for (Local_Data cn : contactsn) {

                                    list1.add(cn.get_delivery_product_order_quantity());
                                    list2.add(getResources().getString(R.string.PRICE) + cn.getAmount());
                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getRemarks())) {

                                        list4.add(cn.getRemarks());
                                    } else {
                                        list4.add("");
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail1())) {
                                        list5.add(cn.getDetail1());
                                    }
                                    {
                                        list5.add("");
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail2())) {
                                        list6.add(cn.getDetail2());
                                    }
                                    {
                                        list6.add("");
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail3())) {
                                        list7.add(cn.getDetail3());
                                    }
                                    {
                                        list7.add("");
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail4())) {
                                        list8.add(cn.getDetail4());
                                    }
                                    {
                                        list8.add("");
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getDetail5())) {
                                        list9.add(cn.getDetail5());
                                    }
                                    {
                                        list9.add("");
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getAmount())) {
                                        pp += Double.valueOf(cn.getAmount());
                                    }
                                }
                            } else {
                                list1.add("");
                                list2.add("");
                                list4.add("");
                                list5.add("");
                                list6.add("");
                                list7.add("");
                                list8.add("");
                                list9.add("");
                            }

                            List<Local_Data> contactsn1 = dbvoc.GetSchemeByCode(cnt1.getCode());
                            if (contactsn1.size() > 0) {
                                for (Local_Data cn : contactsn1) {

                                    list3.add(getResources().getString(R.string.OScheme) + cn.getDisplayName());

                                }
                            } else {
                                list3.add("");

                            }

                            SwipeList.add(mapp);
                        }

                        Product_All_P_Varients.this.runOnUiThread(new Runnable() {
                            public void run() {
                                swipeListView.setItemsCanFocus(true);

                                adapter = new Product_AllVarient_P_Adapter(Product_All_P_Varients.this, SwipeList, list1, list2, list3, list4, list5, list6, list7, list8, list9);

                                swipeListView.setAdapter(adapter);
                                txttotalPreview.setText(getResources().getString(R.string.CTotal) + pp);


                            }
                        });



                    }

                }


                dialog.dismiss();

            } catch (Exception ex) {
                ex.printStackTrace();
                dialog.dismiss();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            //dialog.dismiss();
            Product_All_P_Varients.this.runOnUiThread(new Runnable() {
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

    private class Varientsave extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... response) {

            try {

                Product_All_P_Varients.this.runOnUiThread(new Runnable() {
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
                                            p_mrp.add(key_value_pmrp_array[0]);
                                            p_rp.add(key_value_pmrp_array[1]);
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
                dialog.dismiss();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            //dialog.dismiss();

            if (!p_id.isEmpty() && q_check.equalsIgnoreCase("yes")) {

                SharedPreferences spf = Product_All_P_Varients.this.getSharedPreferences("SimpleLogic", 0);
                String user_email = spf.getString("USER_EMAIL",null);

                Long randomPIN = System.currentTimeMillis();
                String PINString = String.valueOf(randomPIN);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());

                SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDatef = dff.format(c.getTime());
                if (Global_Data.Previous_Order_ServiceOrder_ID.equalsIgnoreCase(Global_Data.Previous_Order_UpdateOrder_ID)) {
                    Global_Data.Previous_Order_UpdateOrder_ID = "Ord" + PINString;

                    Global_Data.GLObalOrder_id = "Ord" + PINString;
                    Global_Data.GLOvel_GORDER_ID = "Ord" + PINString;
                    // Global_Data.GLOvel_GORDER_ID = "Ord"+PINString;

                    dbvoc.getDeleteTable("previous_orders");

                    try {
                        AppLocationManager appLocationManager = new AppLocationManager(Product_All_P_Varients.this);
                        Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                        Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                        PlayService_Location PlayServiceManager = new PlayService_Location(Product_All_P_Varients.this);

                        if (PlayServiceManager.checkPlayServices(Product_All_P_Varients.this)) {
                            Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                        } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                            Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                            Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    Calendar c1 = Calendar.getInstance();
                    System.out.println("Current time =&gt; " + c1.getTime());

                    SimpleDateFormat fdff = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    final String formattedDaten = fdff.format(c1.getTime());

                    loginDataBaseAdapter.insertOrders("", Global_Data.Previous_Order_UpdateOrder_ID, Global_Data.GLOvel_CUSTOMER_ID, Global_Data.order_retailer, user_email, Global_Data.order_city, Global_Data.order_beat, "", "", "", "", "", "", formattedDaten, "", Global_Data.order_retailer, Global_Data.order_state, Global_Data.order_city, Global_Data.sales_btnstring, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Glovel_BEAT_ID, "", "", "", "", "", "", "");

                    loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "NEW ORDER", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                    List<Local_Data> cont1 = dbvoc.getItemNamePrevious_OrderChecknew(Global_Data.Previous_Order_ServiceOrder_ID);
                    for (Local_Data cnt1 : cont1) {

                        loginDataBaseAdapter.insertOrderProducts("", "", Global_Data.GLOvel_GORDER_ID, "", "", "", "", "", cnt1.getSche_code().trim(), " ", "", cnt1.getQty().trim(), cnt1.getRP().trim(), cnt1.getPrice().trim(), cnt1.getAmount().trim(), "", "", Global_Data.order_retailer, " ", cnt1.get_category_ids(), " ", cnt1.getProduct_nm(), "", "", "", "", "", "", "");
                    }

                    dbvoc.getDeleteTable("previous_order_products");
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
                                    dbvoc.update_itemamountandquantity_withremarksonly(String.valueOf(p_q.get(k)), String.valueOf(p_price.get(k)), p_id.get(k), Global_Data.GLObalOrder_id, p_remarks.get(k), p_detail1.get(k), p_detail2.get(k), p_detail3.get(k), p_detail4.get(k), p_detail5.get(k));
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

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                                loginDataBaseAdapter.insertOrderProducts(" ", " ", Global_Data.GLOvel_GORDER_ID, "", Global_Data.Search_Category_name, Global_Data.Search_Product_name, p_name.get(k), " ", "", " ", "", p_q.get(k), p_rp.get(k), p_mrp.get(k), p_price.get(k), "", "", Global_Data.order_retailer, " ", p_id.get(k), " ", p_name.get(k), p_remarks.get(k), p_detail2.get(k), "", p_detail2.get(k), p_detail3.get(k), p_detail4.get(k), p_detail5.get(k));//Reading all

                                // Log.d("pPRIZE","Pprize"+ p_price.get(k));
                            }


                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                            pp += Double.valueOf(p_price.get(k));
                        }



                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                buttonPreviewAddMOre.setEnabled(true);
                buttonPreviewAddMOre.setText(getResources().getString(R.string.Save));
                txttotalPreview.setText(getResources().getString(R.string.CTotal) + pp);
                Global_Data.Varient_value_add_flag = "yes";

                List<Local_Data> checkq = dbvoc.getItemName(Global_Data.GLObalOrder_id);

                if (checkq.size() <= 0) {
                    q_check = "";
                    Global_Data.Order_hashmap.clear();
                    p_id.clear();
                    p_q.clear();
                    p_price.clear();
                    p_name.clear();
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
                    Product_All_P_Varients.this.runOnUiThread(new Runnable() {
                        public void run() {
//                            Toast toast = Toast.makeText(Product_All_P_Varients.this, getResources().getString(R.string.All_item_delete), Toast.LENGTH_SHORT);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Product_All_P_Varients.this, getResources().getString(R.string.All_item_delete),"yes");
                            // Product_Variant.setText("");

                            Intent i = new Intent(Product_All_P_Varients.this, Previous_orderNew_S1.class);
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
                    }
                    txttotalPreview.setText(getResources().getString(R.string.CTotal) + pp);
                    q_check = "";
                    Global_Data.Order_hashmap.clear();

//                    if(!Product_Variant.getText().toString().equalsIgnoreCase(""))
//                    {
//                        Product_Variant.setText("");
//                    }
                    Global_Data.Orderproduct_detail1.clear();
                    Global_Data.Orderproduct_detail2.clear();
                    Global_Data.Orderproduct_detail3.clear();
                    Global_Data.Orderproduct_detail4.clear();
                    Global_Data.Orderproduct_detail5.clear();
                    p_id.clear();
                    p_q.clear();
                    p_price.clear();
                    p_name.clear();
                    p_mrp.clear();
                    p_rp.clear();
                    Global_Data.Orderproduct_remarks.clear();
                    p_remarks.clear();
                    p_detail1.clear();
                    p_detail2.clear();
                    p_detail3.clear();
                    p_detail4.clear();
                    p_detail5.clear();
//                    Toast toast = Toast.makeText(Product_All_P_Varients.this, getResources().getString(R.string.Item_added_successfully), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(Product_All_P_Varients.this, getResources().getString(R.string.Item_added_successfully),"yes");
                }



            } else {
                Global_Data.Orderproduct_detail1.clear();
                Global_Data.Orderproduct_detail2.clear();
                Global_Data.Orderproduct_detail3.clear();
                Global_Data.Orderproduct_detail4.clear();
                Global_Data.Orderproduct_detail5.clear();
                q_check = "";
                Global_Data.Order_hashmap.clear();
                Global_Data.Orderproduct_remarks.clear();
                p_remarks.clear();
                p_detail1.clear();
                p_detail2.clear();
                p_detail3.clear();
                p_detail4.clear();
                p_detail5.clear();
                p_id.clear();
                p_q.clear();
                p_price.clear();
                p_name.clear();
                p_mrp.clear();
                p_rp.clear();
                buttonPreviewAddMOre.setEnabled(true);
                buttonPreviewAddMOre.setText(getResources().getString(R.string.Save));
//                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_enter_quantity), Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_enter_quantity),"yre");
            }


            Product_All_P_Varients.this.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });

        }

        @Override
        protected void onPreExecute() {


            Product_All_P_Varients.this.runOnUiThread(new Runnable() {
                public void run() {


//          dialog.setMessage("Please wait....");
//          dialog.setTitle("Siyaram App");
//          dialog.setCancelable(false);
//          dialog.show();

                    buttonPreviewAddMOre.setEnabled(false);
                    buttonPreviewAddMOre.setText(getResources().getString(R.string.Please_Wait));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void updateSum(Double sum) {
        //txttotalPreview.setText("Total		:		"+sum);
    }


    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition
                + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }


    @Override
    public void onBackPressed() {

        if (Global_Data.Order_hashmap.size() > 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(Product_All_P_Varients.this).create(); //Read Update
            alertDialog.setTitle(getResources().getString(R.string.Warning));
            alertDialog.setMessage(getResources().getString(R.string.ITEM_DISCART_DIALOG_MESSAGE));
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Global_Data.GLOVEL_LONG_DESC = "";
                    Global_Data.GLOVEL_CATEGORY_SELECTION = "";
                    Global_Data.GLOVEL_ITEM_MRP = "";
                    Global_Data.Search_Category_name = "";
                    Global_Data.Orderproduct_detail1.clear();
                    Global_Data.Orderproduct_detail2.clear();
                    Global_Data.Orderproduct_detail3.clear();
                    Global_Data.Orderproduct_detail4.clear();
                    Global_Data.Orderproduct_detail5.clear();
                    Global_Data.Search_Product_name = "";
                    p_id.clear();
                    Global_Data.Orderproduct_remarks.clear();
                    p_remarks.clear();
                    p_name.clear();
                    p_mrp.clear();
                    p_q.clear();
                    p_price.clear();
                    p_rp.clear();
                    Global_Data.Order_hashmap.clear();

                    Intent i = new Intent(Product_All_P_Varients.this, Previous_orderNew_S1.class);
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
            p_name.clear();
            p_mrp.clear();
            p_q.clear();
            p_price.clear();
            p_rp.clear();
            Global_Data.Order_hashmap.clear();
            Global_Data.Orderproduct_remarks.clear();
            p_remarks.clear();
            Global_Data.Orderproduct_detail1.clear();
            Global_Data.Orderproduct_detail2.clear();
            Global_Data.Orderproduct_detail3.clear();
            Global_Data.Orderproduct_detail4.clear();
            Global_Data.Orderproduct_detail5.clear();
            Intent i = new Intent(Product_All_P_Varients.this, Previous_orderNew_S1.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

    }


}
