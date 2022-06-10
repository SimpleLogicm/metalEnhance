package com.msimplelogic.activities;

/**
 * Created by vinod on 04-10-2016.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.msimplelogic.activities.R;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cpm.simplelogic.helper.ContactInfo;
import cpm.simplelogic.helper.Stock_Info;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class Stock_Main extends BaseActivity {
    String str_variant;
    Spinner warehouseDistributor;
    int warehouse_flagnew = 0;
    int product_flagnew = 0;
    int check = 0;
    int check_product = 0;
    ImageView hedder_theame;
    SharedPreferences sp;
    int check_ProductSpec = 0;
    Toolbar toolbar;
    String wareDist;
    String categ_name, subcateg_name;
    private ArrayList<String> result_product = new ArrayList<String>();
    ArrayAdapter<String> dataAdapterCategory, dataAdapterProductSpec, dataAdapterProduct;
    //ArrayList productList = new ArrayList();
    List<String> listProduct, listProductSpec;
    List<String> listScheme;
    ProgressDialog dialog;
    ArrayList<String> All_Product = new ArrayList<String>();
    ArrayList<String> All_Warehouse = new ArrayList<String>();
    ArrayList<String> All_ProductDefault = new ArrayList<String>();
    String product_code = "";
    private ArrayList<String> results = new ArrayList<String>();
    private ArrayList<String> results1 = new ArrayList<String>();
    private ArrayList<String> results2 = new ArrayList<String>();

    ArrayAdapter<String> adapter_state1;
    ArrayAdapter<String> adapter_state2;
    ArrayAdapter<String> adapter_state3;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    RecyclerView recList;
    RelativeLayout rlout_price, rlout_stock;
    AutoCompleteTextView warehouse_list1, product_list;
    Spinner warehouse_list;
    List<Stock_Info> result = new ArrayList<Stock_Info>();
    Button ware_result_button;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Spinner spnCategory, spnProductSpec, spnScheme, spnProduct;
    AutoCompleteTextView Product_Variant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my);

        setContentView(R.layout.activity_stock);

        cd = new ConnectionDetector(getApplicationContext());
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnProduct = (Spinner) findViewById(R.id.spnProduct);
        Product_Variant = (AutoCompleteTextView) findViewById(R.id.Product_Variant);
        rlout_price = (RelativeLayout) findViewById(R.id.rlout_price);
        rlout_stock = (RelativeLayout) findViewById(R.id.rlout_stock);
        hedder_theame=findViewById(R.id.hedder_theame);
        //warehouse_list = (AutoCompleteTextView) findViewById(R.id.warehouse_list);
        warehouse_list = (Spinner) findViewById(R.id.warehouse_list);
        product_list = (AutoCompleteTextView) findViewById(R.id.product_list);

        ware_result_button = (Button) findViewById(R.id.ware_result_button);
        //warehouseDistributor = (Spinner) findViewById(R.id.warehouse_distributor);

        recList = (RecyclerView) findViewById(R.id.cardListn);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        rlout_stock.setBackgroundResource(R.drawable.single_btab);
//        rlout_price.setBackgroundResource(R.drawable.single_wtab);
        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);

        }


        rlout_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),
                        Pricing_Main.class);
                startActivity(intent);
                finish();
                //overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
            }
        });

        // Spinner DropDown Distributor
        List<String> warehDistributor = new ArrayList<String>();
        warehDistributor.add("Select Warehouse/Distributor");
        warehDistributor.add("Item 2");
        warehDistributor.add("Item 3");

//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, warehDistributor);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        warehouseDistributor.setAdapter(dataAdapter);

        // Spinner DropDown Distributor
//        List<String> wareHouse = new ArrayList<String>();
//       List<Local_Data> warehouselist = dbvoc.getAllwarehouseList();
//
//        if (warehouselist.size() <= 0) {
//            // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();
//
//            Stock_Main.this.runOnUiThread(new Runnable() {
//                public void run() {
//
//                    // dialog.dismiss();
//                  //  ++a;
//                    Toast toast = Toast.makeText(Stock_Main.this, getResources().getString(R.string.product_not_found_message), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                }
//            });
//
////                Intent intent = new Intent(getApplicationContext(),
////                        MainActivity.class);
////                startActivity(intent);
//        } else {
//
//            for (Local_Data cn : warehouselist) {
//
//                wareHouse.add(cn.getWare_name());
//
//            }
//
//            Stock_Main.this.runOnUiThread(new Runnable() {
//                public void run() {
//                    // dialog.dismiss();
//                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(Stock_Main.this, android.R.layout.simple_spinner_item, wareHouse);
//
//                    // Drop down layout style - list view with radio button
//                    dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//                    // attaching data adapter to spinner
//                    warehouse_list.setAdapter(dataAdapter1);
//                }
//            });
//
//
//
//        }


//        wareHouse.add("Select Warehouse");
//        wareHouse.add("Andheri");
//        wareHouse.add("Thane");

        // Creating adapter for spinner


        results2.clear();
        List<Local_Data> contacts2 = dbvoc.getAllVariant();
        for (Local_Data cn : contacts2) {
            results2.add(cn.getStateName());
            result_product.add(cn.getStateName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.autocomplete,
                results2);
        Product_Variant.setThreshold(1);// will start working from
        // first character
        Product_Variant.setAdapter(adapter);// setting the adapter
        // data into the
        // AutoCompleteTextView

        Product_Variant.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (Product_Variant.getRight() - Product_Variant.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        View view = Stock_Main.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        //autoCompleteTextView1.setText("");
                        Product_Variant.showDropDown();
                        return true;
                    }
                }
                return false;
            }
        });

        Product_Variant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                //Toast.makeText(Order.this," selected", Toast.LENGTH_LONG).show();

                Global_Data.hideSoftKeyboard(Stock_Main.this);
//                editTextQuantity.setFocusableInTouchMode(true);
//                editTextQuantity.setEnabled(true);

                List<Local_Data> cont = dbvoc.getProductByCat(Product_Variant.getText().toString().trim());
                //results2.add("Select Variant");
                for (Local_Data cn1 : cont) {
                    String str_var = "" + cn1.getStateName();
                    //str_var1 = ""+cn1.getMRP();
                    String str_var2 = "" + cn1.get_Description();
                    String str_var3 = "" + cn1.get_Claims();
                    Global_Data.amnt = "" + cn1.get_Description();
                    Global_Data.amnt1 = "" + cn1.get_Claims();
                    product_code = cn1.getCode();

                    categ_name = cn1.getCategory();
                    subcateg_name = cn1.getSubcateg();


                }

                adapter_state1.setDropDownViewResource(R.layout.spinner_item);
                spnCategory.setAdapter(adapter_state1);
                //spnCategory.setOnItemSelectedListener(NewOrderActivity.this);

                adapter_state2.setDropDownViewResource(R.layout.spinner_item);
                spnProduct.setAdapter(adapter_state2);
                //spnProduct.setOnItemSelectedListener(NewOrderActivity.this);

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(categ_name)) {
                    int spinnerPosition = adapter_state1.getPosition(categ_name);
                    spnCategory.setSelection(spinnerPosition);
                }
            }
        });

        Global_Data.GLOVEL_SubCategory_Button = "";


        //Reading all
        List<Local_Data> contacts12 = dbvoc.HSS_DescriptionITEM();
        results1.add(getResources().getString(R.string.Select_Categories));
        for (Local_Data cn : contacts12) {
            if (!cn.getStateName().equalsIgnoreCase("") && !cn.getStateName().equalsIgnoreCase(" ")) {
                String str_categ = "" + cn.getStateName();
                results1.add(str_categ);
            }
        }

        adapter_state1 = new ArrayAdapter<String>(this, R.layout.spinner_item, results1);
        adapter_state1.setDropDownViewResource(R.layout.spinner_item);
        spnCategory.setAdapter(adapter_state1);
        //	spnCategory.setOnItemSelectedListener(NewOrderActivity.this);

        if (!Global_Data.GLOVEL_CATEGORY_SELECTION.equalsIgnoreCase("")) {
            Log.d("Globel cat", "in");
            spnCategory.setSelection(adapter_state1.getPosition(Global_Data.GLOVEL_CATEGORY_SELECTION));
            Global_Data.GLOVEL_CATEGORY_SELECTION = "";

        }


        results.add(getResources().getString(R.string.Select_Product));
        adapter_state2 = new ArrayAdapter<String>(this, R.layout.spinner_item, results);
        adapter_state2.setDropDownViewResource(R.layout.spinner_item);
        spnProduct.setAdapter(adapter_state2);


        listProduct = new ArrayList<String>();
        dataAdapterProduct = new ArrayAdapter<String>(
                this, R.layout.spinner_item, listProduct);

        listProductSpec = new ArrayList<String>();
        dataAdapterProductSpec = new ArrayAdapter<String>(
                this, R.layout.spinner_item, listProductSpec);

//		listScheme = new ArrayList<String>();
//		dataAdapterScheme = new ArrayAdapter<String>(
//				this, R.layout.spinner_item, listScheme);

        final List<String> listCategory = new ArrayList<String>();
        listCategory.add(getResources().getString(R.string.Select_Categories));

        adapter_state1 = new ArrayAdapter<String>(this, R.layout.spinner_item, results1);


        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
//				// TODO Auto-generated method stub

                check = check + 1;
                if (check > 1) {
                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase(getResources().getString(R.string.Select_Categories))) {
                        categ_name = "";
                        subcateg_name = "";
                        results.clear();
                        results.add(getResources().getString(R.string.Select_Product));
                        adapter_state2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results);
                        adapter_state2.setDropDownViewResource(R.layout.spinner_item);
                        spnProduct.setAdapter(adapter_state2);
                        recList.setVisibility(View.INVISIBLE);

                        results2.clear();

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Stock_Main.this,
                                R.layout.autocomplete,
                                result_product);
                        Product_Variant.setThreshold(1);// will start working from
                        // first character
                        Product_Variant.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        Product_Variant.setText("");

//
//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Select_Category), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Select_Category), "yes");

                    } else {
                        Global_Data.GLOVEL_CATEGORY_SELECTION = parent.getItemAtPosition(pos).toString();
                        //Intent intent = new Intent(getApplicationContext(), Filter_List.class);
                        Global_Data.GLOVEL_CATEGORY_NAME = parent.getItemAtPosition(pos).toString();

                        List<Local_Data> contacts2 = dbvoc.HSS_DescriptionITEM_ID(parent.getItemAtPosition(pos).toString().trim());

                        for (Local_Data cn : contacts2) {
                            Global_Data.GLOVEL_CATEGORY_ID = cn.getCust_Code();


                            //Global_Data.local_pwd = ""+cn.getPwd();

                            //results.add(str_product);
                            //System.out.println("Local Values:-"+Global_Data.local_user);
                            //Toast.makeText(LoginActivity.this, "Login Invalid"+Global_Data.local_user,Toast.LENGTH_SHORT).show();
                        }

                        results.clear();
                        //List<Local_Data> contacts22 = dbvoc.HSS_DescriptionITEM1_ID(Global_Data.GLOVEL_CATEGORY_ID);
                        List<Local_Data> contacts22 = dbvoc.HSS_DescriptionITEM1_category_name(parent.getItemAtPosition(pos).toString().trim());
                        results.add(getResources().getString(R.string.Select_Product));
                        for (Local_Data cn : contacts22) {
                            String str_product = "" + cn.getStateName();
                            //Global_Data.local_pwd = ""+cn.getPwd();

                            results.add(str_product);
                            System.out.println("Local Values:-" + Global_Data.local_user);
                            //Toast.makeText(LoginActivity.this, "Login Invalid"+Global_Data.local_user,Toast.LENGTH_SHORT).show();
                        }

                        adapter_state2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results);
                        adapter_state2.setDropDownViewResource(R.layout.spinner_item);
                        spnProduct.setAdapter(adapter_state2);
                        // spnProduct.setOnItemSelectedListener(NewOrderActivity.this);

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(subcateg_name)) {
                            int spinnerPosition = adapter_state2.getPosition(subcateg_name);
                            spnProduct.setSelection(spinnerPosition);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spnProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
//					// TODO Auto-generated method stub
                check_product = check_product + 1;
                if (check_product > 1) {

                    if (spnCategory.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Categories))) {

//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Select_Category), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Select_Category),"yes");

                    } else if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase(getResources().getString(R.string.Select_Product))) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Stock_Main.this,
                                R.layout.autocomplete,
                                result_product);
                        Product_Variant.setThreshold(1);// will start working from
                        // first character
                        Product_Variant.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        Product_Variant.setText("");
                        recList.setVisibility(View.INVISIBLE);

                    } else {

                        results2.clear();

                        List<Local_Data> contacts33 = dbvoc.HSS_DescriptionITEM1_IDD(parent.getItemAtPosition(pos).toString().trim());
                        for (Local_Data cn : contacts33) {
                            Global_Data.GLOVEL_PRODUCT_ID = cn.getCust_Code();
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.GLOVEL_PRODUCT_ID)) {

                            //List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_ID(Global_Data.GLOVEL_PRODUCT_ID);
                            List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_BYNAME(spnCategory.getSelectedItem().toString().trim(), parent.getItemAtPosition(pos).toString().trim());
                            // results2.add("Select Variant");
                            for (Local_Data cn : contacts3) {
                                str_variant = "" + cn.getStateName();

                                results2.add(str_variant);

                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Stock_Main.this, R.layout.autocomplete, results2);
                            Product_Variant.setThreshold(1);// will start working from
                            // first character
                            Product_Variant.setAdapter(adapter);// setting the adapter
                            // data into the
                            // AutoCompleteTextView

                        }

//                        List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_BYNAMENEW(spnCategory.getSelectedItem().toString().trim(), parent.getItemAtPosition(pos).toString().trim(), Product_Variant.getText().toString().trim());
//                        if (contacts3.size() <= 0 && !Product_Variant.getText().toString().trim().equalsIgnoreCase(""))
//                        {
//                            // Toast.makeText(Pricing_Main.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();
//
////                            Toast toast = Toast.makeText(getApplicationContext(),  "Sorry No Record Found.", Toast.LENGTH_LONG);
////                            toast.setGravity(Gravity.CENTER, 0, 0);
////                            toast.show();
//
//                            Product_Variant.setTextColor(Color.BLACK);
//                            Product_Variant.setText("");
//
//
//                            recList.setVisibility(View.INVISIBLE);
//                        }
//                        else
//                        {
//                            if (Product_Variant.length() > 0) {
//                                recList.setVisibility(View.VISIBLE);
//                                List<ContactInfo> result = new ArrayList<ContactInfo>();
//                                for (Local_Data cn : contacts3) {
//
//                                    ContactInfo ci = new ContactInfo();
//                                    ci.name = cn.get_product_desc();
//                                    ci.rp = cn.getRP();
//                                    ci.mrp = cn.getMRP();
//
//                                    result.add(ci);
//                                }
//                                ContactAdapter ca = new ContactAdapter(Stock_Main.this, result);
//                                recList.setAdapter(ca);
//                                ca.notifyDataSetChanged();
//                            }
//                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(warehouse_list.getSelectedItem().toString()) || Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(product_list.getText().toString())) {
                            isInternetPresent = cd.isConnectingToInternet();
                            if (isInternetPresent) {
                                getWarehouseDetails();
                            } else {
//                                Toast toast = Toast.makeText(Stock_Main.this, getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(Stock_Main.this, getResources().getString(R.string.internet_connection_error), "yes");
                            }
                        } else {
//                            Toast toast = Toast.makeText(Stock_Main.this, getResources().getString(R.string.Select_warehouse_message), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Stock_Main.this, getResources().getString(R.string.Select_warehouse_message), "yes");
                        }


                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


//        warehouseDistributor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showAlertDialog();
//            }
//        });

        ware_result_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(warehouse_list.getSelectedItem().toString()) || Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(product_list.getText().toString())) {
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        getWarehouseDetails();
                    } else {
//                        Toast toast = Toast.makeText(Stock_Main.this, getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                        Global_Data.Custom_Toast(Stock_Main.this, getResources().getString(R.string.internet_connection_error),"yes");

                    }
                } else {
//                    Toast toast = Toast.makeText(Stock_Main.this, getResources().getString(R.string.Select_warehouse_message), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(Stock_Main.this, getResources().getString(R.string.Select_warehouse_message),"yes");
                }

            }
        });

        SharedPreferences spf = Stock_Main.this.getSharedPreferences("SimpleLogic", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("order", "new");
        editor.commit();

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
//            mTitleTextView.setText(getResources().getString(R.string.STOCK));
//
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = Stock_Main.this.getSharedPreferences("SimpleLogic", 0);
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
//                    // todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
//                } else {
//                    int age = (int) Math.round(age_float);
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    }
//                    // todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
//                }
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//
//            mActionBar.setCustomView(mCustomView);
//            mActionBar.setDisplayShowCustomEnabled(true);
//            mActionBar.setHomeButtonEnabled(true);
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


        recList.setVisibility(View.INVISIBLE);

        dialog = new ProgressDialog(Stock_Main.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(getResources().getString(R.string.customer_loading_dialog_message));
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        new Stock_Main.StockASN().execute();

        if ((!Global_Data.Stock_warehouse_flag.equalsIgnoreCase(""))) {
            //warehouse_list.setText(Global_Data.Stock_warehouse_flag);
        }

        if ((!Global_Data.Stock_product_flag.equalsIgnoreCase(""))) {
            product_list.setText(Global_Data.Stock_product_flag);
        }

        product_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                ware_result_button.setVisibility(View.VISIBLE);

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (product_list.getRight() - product_list.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        View view = Stock_Main.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        //autoCompleteTextView1.setText("");
                        // product_list.showDropDown();

                        Global_Data.Stock_product_flag = "";
                        Global_Data.Stock_product_flag_value_check = "";
                        Intent i = new Intent(Stock_Main.this, Stock_Product_Options.class);
                        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        startActivity(i);

                        return true;
                    }
                }
                return false;
            }
        });

        product_list.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (product_list.getText().toString().trim().length() == 0) {

                    //ca = new Customer_info_main_adapter(Allresult,Stock_Main.this);
                    //recList.setAdapter(ca);
                    //ca.notifyDataSetChanged();
                    //ware_result_button.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        product_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                //Toast.makeText(Order.this," selected", Toast.LENGTH_LONG).show();

                ware_result_button.setVisibility(View.VISIBLE);
                Global_Data.hideSoftKeyboard(Stock_Main.this);
                Global_Data.Stock_product_flag_value_check = "";
                Global_Data.Stock_warehouse_flag_value_check = "";
                Global_Data.Stock_product_flag = "";
                Global_Data.Stock_warehouse_flag = "";


            }
        });

        warehouse_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(warehouse_list.getSelectedItem().toString()) || Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(product_list.getText().toString())) {
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        getWarehouseDetails();
                    } else {
//                        Toast toast = Toast.makeText(Stock_Main.this, getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                        Global_Data.Custom_Toast(Stock_Main.this, getResources().getString(R.string.internet_connection_error),"yes");

                    }
                } else {
//                    Toast toast = Toast.makeText(Stock_Main.this, getResources().getString(R.string.Select_warehouse_message), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(Stock_Main.this, getResources().getString(R.string.Select_warehouse_message), "yes");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        warehouse_list.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                final int DRAWABLE_LEFT = 0;
////                final int DRAWABLE_TOP = 1;
////                final int DRAWABLE_RIGHT = 2;
////                final int DRAWABLE_BOTTOM = 3;
//                ware_result_button.setVisibility(View.VISIBLE);
//
////                if(event.getAction() == MotionEvent.ACTION_UP) {
////                    if(event.getRawX() >= (warehouse_list.getRight() - warehouse_list.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
////
////                        View view = Stock_Main.this.getCurrentFocus();
////                        if (view != null) {
////                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
////                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
////                        }
//                        //autoCompleteTextView1.setText("");
//                       // warehouse_list.showDropDown();
//                        Global_Data.Stock_warehouse_flag = "";
//                        Global_Data.Stock_warehouse_flag_value_check = "";
//                        Intent i=new Intent(Stock_Main.this, Stock_options.class);
//                        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                        startActivity(i);
//                        //finish();
//                        return false;
////                    }
////                }
//                //return false;
//            }
//        });

//        warehouse_list.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if(warehouse_list.getText().toString().trim().length() == 0) {
//
//                    //ca = new Customer_info_main_adapter(Allresult,Stock_Main.this);
//                    //recList.setAdapter(ca);
//                    //ca.notifyDataSetChanged();
//
//
//
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//
//        warehouse_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
//                                    long id) {
//                //Toast.makeText(Order.this," selected", Toast.LENGTH_LONG).show();
//
//                ware_result_button.setVisibility(View.VISIBLE);
//                Global_Data.hideSoftKeyboard(Stock_Main.this);
//                Global_Data.Stock_product_flag_value_check = "";
//                Global_Data.Stock_warehouse_flag_value_check = "";
//                Global_Data.Stock_product_flag = "";
//                Global_Data.Stock_warehouse_flag = "";
//            }
//        });
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
                SharedPreferences sp = Stock_Main.this.getSharedPreferences("SimpleLogic", 0);
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

    private class StockASN extends AsyncTask<String, Void, String> {

        int a = 0;

        @Override
        protected String doInBackground(String... response) {

            List<Local_Data> contacts3 = dbvoc.getAllitemList();

            if (contacts3.size() <= 0) {
                // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

                Stock_Main.this.runOnUiThread(new Runnable() {
                    public void run() {

                        // dialog.dismiss();
                        ++a;
//                        Toast toast = Toast.makeText(Stock_Main.this, getResources().getString(R.string.product_not_found_message), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                        Global_Data.Custom_Toast(Stock_Main.this, getResources().getString(R.string.product_not_found_message),"yes");
                    }
                });

//                Intent intent = new Intent(getApplicationContext(),
//                        MainActivity.class);
//                startActivity(intent);
            } else {

                for (Local_Data cn : contacts3) {

                    All_Product.add(cn.getProdname());

                }

                Stock_Main.this.runOnUiThread(new Runnable() {
                    public void run() {
                        // dialog.dismiss();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Stock_Main.this, android.R.layout.simple_spinner_dropdown_item,
                                All_Product);
                        product_list.setThreshold(1);// will start working from
                        product_list.setAdapter(adapter);// setting the adapter
                        product_list.setTextColor(Color.BLACK);
                    }
                });


            }
            List<String> wareHouse = new ArrayList<String>();
            List<Local_Data> warehouselist = dbvoc.getAllwarehouseList();

            if (warehouselist.size() <= 0) {
                // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

                Stock_Main.this.runOnUiThread(new Runnable() {
                    public void run() {

                        // dialog.dismiss();
//                  //  ++a;
//                        Toast toast = Toast.makeText(Stock_Main.this, getResources().getString(R.string.product_not_found_message), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(Stock_Main.this, getResources().getString(R.string.product_not_found_message),"yes");
                    }
                });
//
//                Intent intent = new Intent(getApplicationContext(),
//                        MainActivity.class);
//                startActivity(intent);
            } else {

                wareHouse.add("Select Warehouse");
                for (Local_Data cn : warehouselist) {

                    wareHouse.add(cn.getWare_name());

                }
//
                Stock_Main.this.runOnUiThread(new Runnable() {
                    public void run() {
                        // dialog.dismiss();
                        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(Stock_Main.this,R.layout.spinner_item, wareHouse);

                        // Drop down layout style - list view with radio button
                        dataAdapter1.setDropDownViewResource(R.layout.spinner_item);

                        // attaching data adapter to spinner
                        warehouse_list.setAdapter(dataAdapter1);
                    }
                });
//
//
//
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            //dialog.dismiss();
            Stock_Main.this.runOnUiThread(new Runnable() {
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Stock_Main.this, Pricing_Main.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void getWarehouseDetails() {
        recList.setVisibility(View.INVISIBLE);
        result.clear();
        // loginDataBaseAdapter=new LoginDataBaseAdapter(Target_Summary2.this);
        //  loginDataBaseAdapter=loginDataBaseAdapter.open();
        dialog = new ProgressDialog(Stock_Main.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(getResources().getString(R.string.warehouse_loading_message));
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        warehouse_flagnew = 0;
        product_flagnew = 0;
        String warehouse_flag = "";
        String product_flag = "";
        String city_state[];
        String sub_caty[];
        String state_name = "";
        String city_name = "";
        String state_code = "";
        String city_code = "";
        String warehouse_code = "";

        String caategory_name = "";
        String sub_caategory_name = "";
        String caategory_code = "";
        String sub_caategory_code = "";
        String product_code = "";


        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(warehouse_list.getSelectedItem().toString())) {
            warehouse_flag = "true";

            if (warehouse_list.getSelectedItem().toString().equalsIgnoreCase("All States")) {
                state_code = "";
                city_code = "";
                warehouse_code = "";

            } else if (warehouse_list.getSelectedItem().toString().contains("All Cities")) {
                city_state = warehouse_list.getSelectedItem().toString().split("-");
                state_name = city_state[0];

                List<Local_Data> state_namen = dbvoc.getState_id(state_name.trim());

                for (Local_Data nm : state_namen) {

                    state_code = nm.getSTATE_ID();
                }

                city_code = "";
                warehouse_code = "";

            } else if (Global_Data.Stock_warehouse_flag_value_check.equalsIgnoreCase("All States AND All Cities")) {
                city_state = warehouse_list.getSelectedItem().toString().split("-");
                state_name = city_state[0];
                city_name = city_state[1];

                List<Local_Data> state_namen = dbvoc.getState_id(state_name.trim());

                for (Local_Data nm : state_namen) {

                    state_code = nm.getSTATE_ID();
                }

                List<Local_Data> city_namen = dbvoc.getCity_id(city_name.trim());

                for (Local_Data cnm : city_namen) {

                    city_code = cnm.getCITY_ID();
                }

                warehouse_code = "";

            } else {
                List<Local_Data> w_namen = dbvoc.getwarehouseByname(warehouse_list.getSelectedItem().toString().trim());

                if (w_namen.size() > 0) {
                    warehouse_flagnew = 0;
                    for (Local_Data wn : w_namen) {

                        warehouse_code = wn.getWare_code();
                    }
                } else {
                    warehouse_flagnew = 1;
                }
                state_code = "";
                city_code = "";
            }
        } else {
            state_code = "";
            city_code = "";
            warehouse_code = "";
        }

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(product_list.getText().toString())) {
            product_flag = "true";

            if (product_list.getText().toString().equalsIgnoreCase("All Category")) {
                caategory_code = "";
                sub_caategory_code = "";
                product_code = "";

            } else if (product_list.getText().toString().contains("All Sub Category")) {
                sub_caty = product_list.getText().toString().split("-");
                caategory_name = sub_caty[0];

//                List<Local_Data> c_namen = dbvoc.HSS_DescriptionITEM_ID(caategory_name.trim());
//
//                for (Local_Data cn : c_namen)
//                {

                caategory_code = caategory_name;
                //  }

                sub_caategory_code = "";
                product_code = "";

            } else if (Global_Data.Stock_product_flag_value_check.equalsIgnoreCase("Category and All Sub Category")) {
                sub_caty = product_list.getText().toString().split("-");
                caategory_name = sub_caty[0];
                sub_caategory_name = sub_caty[1];

//                List<Local_Data> c_namen = dbvoc.HSS_DescriptionITEM_ID(caategory_name.trim());
//
//                for (Local_Data cn : c_namen)
//                {

                caategory_code = caategory_name;
                //  }

                sub_caategory_code = sub_caategory_name;
                product_code = "";
            } else {
                List<Local_Data> c_namen = dbvoc.PRODUCT_ID(product_list.getText().toString().trim());

                if (c_namen.size() > 0) {
                    product_flagnew = 0;
                    for (Local_Data cn : c_namen) {

                        product_code = cn.getItem_Code();
                    }
                } else {
                    product_flagnew = 1;
                }

                caategory_code = "";
                sub_caategory_code = "";

            }

        } else {
            caategory_code = "";
            sub_caategory_code = "";
            product_code = "";
        }

        if (warehouse_flagnew == 0 && product_flagnew == 0) {
            try {

                SharedPreferences spf = Stock_Main.this.getSharedPreferences("SimpleLogic", 0);
                String user_email = spf.getString("USER_EMAIL", null);

                SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
                String Cust_domain = sp.getString("Cust_Service_Url", "");
                String service_url1 = Cust_domain + "metal/api/v1/";
                String device_id = sp.getString("devid", "");
                String domain = service_url1;

                Log.i("volley", "domain: " + domain);
                Log.i("volley", "email: " + user_email);
                String service_url = "";

                service_url = domain + "wh_stocks/send_stocks?email=" + user_email + "&warehouse_code=" + URLEncoder.encode(warehouse_code, "UTF-8") + "&product_code=" + URLEncoder.encode(product_code, "UTF-8") + "&state_code=" + state_code + "&city_code=" + city_code + "&primary_category=" + URLEncoder.encode(caategory_code, "UTF-8") + "&sub_category=" + URLEncoder.encode(sub_caategory_code, "UTF-8");


                Log.i("target url", "target url " + service_url);

                JsonObjectRequest jsObjRequest = new JsonObjectRequest(service_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("volley", "response: " + response);
                        //  Log.i("volley", "response reg Length: " + response.length());

                        try {
                            String response_result = "";
                            if (response.has("result")) {
                                response_result = response.getString("result");
                            } else {
                                response_result = "data";
                            }

                            if (response_result.equalsIgnoreCase("User doesn't exist")) {
                                // Toast.makeText(getActivity(), response_result, Toast.LENGTH_LONG).show();
                                Stock_Main.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                });
//                                Toast toast = Toast.makeText(Stock_Main.this, response_result, Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                result.clear();
                                Global_Data.Custom_Toast(Stock_Main.this, response_result, "yes");

                                Intent i = new Intent(Stock_Main.this, Pricing_Main.class);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();

                            } else if (response_result.equalsIgnoreCase("Please provide all the data")) {

                                // Toast.makeText(getActivity(), response_result, Toast.LENGTH_LONG).show();

                                Stock_Main.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                });
//                                Toast toast = Toast.makeText(Stock_Main.this, response_result, Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                result.clear();
                                Global_Data.Custom_Toast(Stock_Main.this, response_result,"yes");

                                Intent i = new Intent(Stock_Main.this, Pricing_Main.class);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();

                            } else {
                                JSONArray stocks = response.getJSONArray("stocks");

                                Log.i("volley", "response stocks Length: " + stocks.length());

                                Log.d("stocks", "stocks" + stocks.toString());

                                if (stocks.length() <= 0) {
                                    Stock_Main.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                        }
                                    });
                                    //Toast.makeText(getActivity(), "Target not found.", Toast.LENGTH_LONG).show();

//                                    Toast toast = Toast.makeText(Stock_Main.this, getResources().getString(R.string.Warehouse_Data_Not_Found), Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    result.clear();
                                    Global_Data.Custom_Toast(Stock_Main.this, getResources().getString(R.string.Warehouse_Data_Not_Found),"yes");
                                    //                                Intent i=new Intent(Stock_Main.this, Pricing_Main.class);
                                    //                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    //                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    //                                startActivity(i);
                                    //
                                    //                                finish();
                                } else {
                                    result.clear();
                                    View view = Stock_Main.this.getCurrentFocus();
                                    if (view != null) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                    }

                                    ware_result_button.setVisibility(View.GONE);
                                    recList.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < stocks.length(); i++) {

                                        JSONObject jsonObject = stocks.getJSONObject(i);


                                        Stock_Info ci = new Stock_Info();

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("product_primary_category").toString())) {
                                            String product_category = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.SPRODUCT) + "</b>") + jsonObject.getString("product_primary_category").toString());
                                            if (!jsonObject.getString("product_sub_category").equalsIgnoreCase("null") && !jsonObject.getString("product_sub_category").equalsIgnoreCase(null) & !jsonObject.getString("product_sub_category").equalsIgnoreCase("") & !jsonObject.getString("product_sub_category").equalsIgnoreCase(" ")) {
                                                product_category += " - " + jsonObject.getString("product_sub_category").toString();
                                                ;
                                            }

                                            if (!jsonObject.getString("product_variant").equalsIgnoreCase("null") && !jsonObject.getString("product_variant").equalsIgnoreCase(null) & !jsonObject.getString("product_variant").equalsIgnoreCase("") & !jsonObject.getString("product_variant").equalsIgnoreCase(" ")) {
                                                product_category += " - " + jsonObject.getString("product_variant").toString();
                                                ;
                                            }

                                            if (!jsonObject.getString("product_name").equalsIgnoreCase("null") && !jsonObject.getString("product_name").equalsIgnoreCase(null) & !jsonObject.getString("product_name").equalsIgnoreCase("") & !jsonObject.getString("product_name").equalsIgnoreCase(" ")) {
                                                product_category += " - " + jsonObject.getString("product_name").toString();
                                                ;
                                            }


                                            ci.ss_product = product_category;
                                        } else {
                                            ci.ss_product = String.valueOf("");
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("product_rp").toString())) {
                                            ci.ss_RP = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.SRP) + "</b>") + jsonObject.getString("product_rp").toString());
                                        } else {
                                            ci.ss_RP = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.SRP) + "</b>") + "null");
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("product_mrp").toString())) {
                                            ci.ss_MRP = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.SMRP) + "</b>") + jsonObject.getString("product_mrp").toString());
                                        } else {
                                            ci.ss_MRP = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.SMRP) + "</b>") + "null");
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("wh_name").toString())) {
                                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("wh_type").toString())) {
                                                ci.ss_name = jsonObject.getString("wh_type").toString() + " - " + jsonObject.getString("wh_name").toString();
                                            } else {
                                                ci.ss_name = jsonObject.getString("wh_name").toString();
                                            }

                                        } else {
                                            ci.ss_name = "";
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("wh_address").toString())) {

                                            ci.ss_address = String.valueOf(jsonObject.getString("wh_address").toString());

                                        } else {
                                            ci.ss_address = String.valueOf("");
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("gross_stock").toString())) {

                                            ci.ss_grossstock = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.GROSS_STOCK) + "</b>") + jsonObject.getString("gross_stock").toString());

                                        } else {
                                            ci.ss_grossstock = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.GROSS_STOCK) + "</b>") + "");
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("sellable_stock").toString())) {

                                            ci.ss_sellable = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.SELLABLE) + "</b>") + jsonObject.getString("sellable_stock").toString());

                                        } else {
                                            ci.ss_sellable = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.SELLABLE) + "</b>") + "");
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("updated_at").toString())) {

                                            ci.updated_at = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.SUPDATED) + "</b>") + jsonObject.getString("updated_at").toString());

                                        } else {
                                            ci.updated_at = String.valueOf(Html.fromHtml("<b>" + getResources().getString(R.string.SUPDATED) + "</b>") + "");
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("wh_city_name").toString())) {

                                            ci.city = String.valueOf(jsonObject.getString("wh_city_name").toString());

                                        } else {
                                            ci.city = String.valueOf("");
                                        }

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("wh_state_name").toString())) {

                                            ci.state = String.valueOf(jsonObject.getString("wh_state_name").toString());

                                        } else {
                                            ci.state = String.valueOf("");
                                        }

                                        result.add(ci);

                                    }

                                    Stock_Adapter ca = new Stock_Adapter(result, Stock_Main.this);
                                    recList.setAdapter(ca);
                                    // ca.notifyDataSetChanged();
                                    //recList.setRecycledViewPool();

                                    Stock_Main.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                        }
                                    });

                                }

                                Stock_Main.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                });

                                //finish();

                            }


                            // }

                            // output.setText(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Stock_Main.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            });
                        }


                        Stock_Main.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("volley", "error: " + error);

//                        Toast toast = Toast.makeText(Stock_Main.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                        Global_Data.Custom_Toast(Stock_Main.this, getResources().getString(R.string.Server_Error),"yes");

                        Stock_Main.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        });

                        Intent i = new Intent(Stock_Main.this, Pricing_Main.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();

                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(Stock_Main.this);
                // queue.add(jsObjRequest);
                jsObjRequest.setShouldCache(false);
                int socketTimeout = 200000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsObjRequest.setRetryPolicy(policy);
                requestQueue.add(jsObjRequest);

            } catch (Exception e) {
                e.printStackTrace();
                Stock_Main.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });


            }

        } else {
            Stock_Main.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            });
            if (warehouse_flagnew == 1) {

                //warehouse_list.setText("");
//                Toast toast = Toast.makeText(Stock_Main.this, getResources().getString(R.string.valid_warehouse_name), Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
                //Global_Data.Custom_Toast(Stock_Main.this, getResources().getString(R.string.valid_warehouse_name), "yes");
            } else if (product_flagnew == 1) {

                product_list.setText("");
//                Toast toast = Toast.makeText(Stock_Main.this, getResources().getString(R.string.valid_product_name), Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();

                Global_Data.Custom_Toast(Stock_Main.this, getResources().getString(R.string.valid_product_name),"yes");

            }
        }
        Stock_Main.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Stock_Main.this);
        alertDialog.setTitle("Select");
        String[] items = {"Distributor", "Warehouse"};
        int checkedItem = 0;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        wareDist = "Distributor";
                        //warehouseDistributor.setText(wareDist);
                        Stock_Main.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                        break;
                    case 1:
                        wareDist = "Warehouse";
                        //warehouseDistributor.setText(wareDist);
                        Stock_Main.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}
