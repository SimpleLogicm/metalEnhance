package com.msimplelogic.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.adapter.AutoCompleteAdapter;
import com.msimplelogic.adapter.SpinnerListAdapter;
import com.msimplelogic.animation.ActivitySwitcher;
import com.msimplelogic.model.Category;
import com.msimplelogic.model.Product;
import com.msimplelogic.model.Scheme;
import com.msimplelogic.model.SpinerListModel;
import com.msimplelogic.webservice.ConnectionDetector;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class NewOrderActivity extends BaseActivity {
    static int quantity = 0, deleiveryQuantity = 0;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ImageView hedder_theame;
    RecyclerView searchSpinnerRecycleview;
    Button list_ok, show_list_fromsearch;
    static float rp, mrp, totalprice, totalprice1, totalprc_scheme, productprice;
    static String scheme = "";
    static String category, productName, productSpec, productQuantity,
            productDeleiveryQuantity, productScheme, productrp, productmrp,
            producttotalPrice;
    static int categoryID, productID, schemeID;
    int check = 0;
    MediaPlayer mpplayer;
    int check_product = 0;
    int check_ProductSpec = 0;
    String CategoriesSpinner = "";
    String str_var1;
    TextView order_toolbar_title;
    AutoCompleteTextView Product_Variant, Product_Variant_search;
    String categ_name, subcateg_name, varient_name;
    String ProductSpinner = "";
    String price = "";
    String str_variant;
    String product_code = "";
    String scheme_code = "";
    String scheme_namen = "";
    LoginDataBaseAdapter loginDataBaseAdapter;
    Spinner spnCategory, spnProductSpec, spnScheme, spnProduct;
    TextView editTextRP, editTextMRP, txtPrice, txtDeleiveryQuantity, txt_rp;
    EditText txtDeleiveryQuantity1;
    EditText editTextQuantity;
    Button buttonAddMOre, buttonPreviewOrder, online_catalogue, local_catalogue, show_list, list_cancel;
    AutoCompleteTextView autoCompleteTextView1;
    //ArrayList<DatabaseModel> dataCategories,dataVarients;
    ArrayAdapter<String> dataAdapterCategory, dataAdapterProductSpec, dataAdapterProduct;
    //ArrayList productList = new ArrayList();
    List<String> listProduct, listProductSpec;
    List<String> listScheme;
    ArrayList<String> Discount_list = new ArrayList<String>();
    ArrayAdapter<String> dataAdapterScheme;
    HashMap<String, String> categoriesMap, productsMap;
    int dbschemeID;
    ArrayAdapter<String> adapter_state1;
    ArrayAdapter<String> adapter_state2;
    ArrayAdapter<String> adapter_state3;
    ArrayAdapter<String> Discount_Adapter;
    SimpleCursorAdapter cursorAdapter;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    ArrayList<Category> dataCategories = new ArrayList<Category>();
    ArrayList<Product> dataProducts = new ArrayList<Product>();
    ArrayList<Scheme> dataScheme = new ArrayList<Scheme>();
    private ArrayList<String> results = new ArrayList<String>();
    private ArrayList<String> results1 = new ArrayList<String>();
    private ArrayList<String> results2 = new ArrayList<String>();
    private ArrayList<String> result_product = new ArrayList<String>();
    private ArrayList<String> Scheme_array = new ArrayList<String>();
    AutoCompleteAdapter AutoSearchAdapter;
    RelativeLayout searchLout;
    SpinnerListAdapter spinner_list_adapter;
    List<SpinerListModel> snlist = new ArrayList<>();
    Toolbar toolbar;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.neworder_main);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        order_toolbar_title=findViewById(R.id.order_toolbar_title);
        if (Global_Data.q.equalsIgnoreCase("Yes")&&Global_Data.q!=""){
            order_toolbar_title.setText(getResources().getString(R.string.Quote));
        }else{
            order_toolbar_title.setText(getResources().getString(R.string.New_Order));

        }
        setTitle("");
        Global_Data.Search_Category_name = "";

        Global_Data.Search_Product_name = "";
        Global_Data.Order_hashmap.clear();
        Global_Data.catalogue_m.clear();
        Global_Data.Orderproduct_remarks.clear();
        cd = new ConnectionDetector(NewOrderActivity.this);
        Global_Data.Orderproduct_detail1.clear();
        Global_Data.Orderproduct_detail2.clear();
        Global_Data.Orderproduct_detail3.clear();
        Global_Data.Orderproduct_detail4.clear();
        Global_Data.Orderproduct_detail5.clear();

        Global_Data.spiner_list_modelList.clear();
        Global_Data.array_of_pVarient.clear();

        // create a instance of SQLite Database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        show_list_fromsearch = findViewById(R.id.show_list_fromsearch);
        list_ok = findViewById(R.id.list_ok);
        list_cancel = findViewById(R.id.list_cancel);

        txtPrice = (TextView) findViewById(R.id.txtPrice1);
        //txtDeleiveryQuantity = (TextView) findViewById(R.id.txtDeleiveryQuantity1);
        //txtDeleiveryQuantity1 = (EditText) findViewById(R.id.txtDeleiveryQuantity);
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnProduct = (Spinner) findViewById(R.id.spnProduct);
        //spnProductSpec = (Spinner) findViewById(R.id.spnProductSpec);
        Product_Variant = (AutoCompleteTextView) findViewById(R.id.Product_Variant);
        Product_Variant_search = findViewById(R.id.Product_Variant_search);
        spnScheme = (Spinner) findViewById(R.id.spnScheme1);

        editTextRP = (TextView) findViewById(R.id.editTextRP);
        editTextMRP = (TextView) findViewById(R.id.editTextMRP);
        txt_rp = (TextView) findViewById(R.id.textRP);
        hedder_theame=findViewById(R.id.hedder_theame);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity1);
        searchSpinnerRecycleview = findViewById(R.id.search_spinner_recycleview);
        searchLout = findViewById(R.id.search_lout);

        show_list =  findViewById(R.id.show_list);
        online_catalogue = findViewById(R.id.online_catalogue);


        local_catalogue = findViewById(R.id.local_catalogue);



        mpplayer = new MediaPlayer();
        // for label RP change
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        String rpstr = spf1.getString("var_rp", "");
        if (rpstr.length() > 0) {
            txt_rp.setText(rpstr);
        } else {
            txt_rp.setText(getResources().getString(R.string.RP));
        }


        SharedPreferences sound = NewOrderActivity.this.getSharedPreferences("SimpleLogic", 0);
        Global_Data.sound_file = sound.getString("var_addsound", "");

        SharedPreferences spf12 = NewOrderActivity.this.getSharedPreferences("SimpleLogic", 0);
        Global_Data.app_sound = spf12.getBoolean("var_addmore", false);

        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);

        }


            if(Global_Data.CUSTOMER_SERVICE_FLAG.equalsIgnoreCase("QUOTE"))
        {
            show_list.setVisibility(View.GONE);
            show_list_fromsearch.setVisibility(View.GONE);
        }else{
            show_list.setVisibility(View.VISIBLE);
            show_list_fromsearch.setVisibility(View.VISIBLE);
        }

        Scheme_array.clear();
        Scheme_array.add(getResources().getString(R.string.Select_Scheme));

        Discount_Adapter = new ArrayAdapter<String>(NewOrderActivity.this, R.layout.spinner_item, Scheme_array);
        Discount_Adapter.setDropDownViewResource(R.layout.spinner_item);
        spnScheme.setAdapter(Discount_Adapter);

        results2.clear();

//        List<Local_Data> contacts2 = dbvoc.getAllVariant();
//        //results2.add("Select All");
//        //result_product.add("Select All");
//        for (Local_Data cn : contacts2) {
//            results2.add(cn.getStateName());
//            result_product.add(cn.getStateName());
//        }

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_dropdown_item,
//                results2);
//        Product_Variant.setThreshold(1);// will start working from
//        // first character
//        Product_Variant.setAdapter(adapter);// setting the adapter
//        // data into the
//        // AutoCompleteTextView
//        Product_Variant.setTextColor(Color.BLACK);


        searchSpinnerRecycleview.setLayoutManager(new LinearLayoutManager(NewOrderActivity.this));
        AutoSearchAdapter = new AutoCompleteAdapter(this,  R.layout.autocomplete);
        Product_Variant_search.setAdapter(AutoSearchAdapter);

        List<SpinerListModel> cont1 = dbvoc.getAllProducta();
        if (cont1.size() > 0) {
            Global_Data.spiner_list_modelList.clear();
            for (SpinerListModel cnt1 : cont1) {
                SpinerListModel spiner_list_model = new SpinerListModel();
//				spiner_list_model.setBusiness_category("");
//				spiner_list_model.setBusiness_unit("");
                spiner_list_model.setPrimary_category("");
                spiner_list_model.setProduct_variant(cnt1.getProduct_variant());
                spiner_list_model.setSub_category("");
                spiner_list_model.setCode(cnt1.getCode());
                spiner_list_model.setSelected(false);

                snlist.add(spiner_list_model);
                Global_Data.spiner_list_modelList.add(spiner_list_model);
            }
        }

        searchSpinnerRecycleview.setLayoutManager(new LinearLayoutManager(NewOrderActivity.this));
        spinner_list_adapter = new SpinnerListAdapter(NewOrderActivity.this, snlist);
        searchSpinnerRecycleview.setAdapter(spinner_list_adapter);

        list_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "";
          //      Global_Data.array_of_pVarient.clear();
                for (int i = 0; i < Global_Data.spiner_list_modelList.size(); i++) {
                    SpinerListModel singleStudent = Global_Data.spiner_list_modelList.get(i);
                    if (singleStudent.isSelected() == true) {
                        data = singleStudent.getProduct_variant();
                        Log.d("Values", "Values" + data + " " + singleStudent.isSelected());
                        try {
                            data = URLEncoder.encode(data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Global_Data.array_of_pVarient.add(data);
                    }
                    Log.d("Values", "Values" + data + " " + singleStudent.isSelected());
                }

                if (Global_Data.array_of_pVarient.size() > 0) {
                    //Global_Data.Search_business_unit_name = "";
                    Global_Data.Search_Category_name = "";
                    //Global_Data.Search_BusinessCategory_name = "";
                    Intent i = new Intent(getApplicationContext(), OnlineCatalogue.class);
                    startActivity(i);
                } else {
                 //   Toast.makeText(getApplicationContext(), "Please select product variant.", Toast.LENGTH_SHORT).show();
                    Global_Data.Custom_Toast(getApplicationContext(), "Please select product variant.","");
                }
            }
        });

        list_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NewOrderActivity.class);
                startActivity(i);
            }
        });

        show_list_fromsearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String data = "";
                Global_Data.array_of_pVarient.clear();
                for (int i = 0; i < Global_Data.spiner_list_modelList.size(); i++) {
                    SpinerListModel singleStudent = Global_Data.spiner_list_modelList.get(i);
                    if (singleStudent.isSelected() == true) {
                        data = singleStudent.getCode();
                        Log.d("Values", "Values" + data + " " + singleStudent.isSelected());
                        Global_Data.array_of_pVarient.add(data);
                    }
                    Log.d("Values", "Values" + data + " " + singleStudent.isSelected());
                }

                if (Global_Data.array_of_pVarient.size() > 0) {
                    //Global_Data.Search_business_unit_name = "";
                    Global_Data.Search_Category_name = "";
                    //Global_Data.Search_BusinessCategory_name = "";
                    Intent i = new Intent(getApplicationContext(), ProductAll_Varients.class);
                    startActivity(i);
                } else {
                  //  Toast.makeText(getApplicationContext(), "Please select product variant.", Toast.LENGTH_SHORT).show();
                    Global_Data.Custom_Toast(getApplicationContext(), "Please select product variant.","");
                }
            }
        });

        Product_Variant.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (Product_Variant.getRight() - Product_Variant.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        View view = NewOrderActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        }

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

                Global_Data.hideSoftKeyboard(NewOrderActivity.this);

//				 Intent intent = new Intent(getApplicationContext(), ProductAll_Varients.class);
//				 intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//				 //startActivityForResult(intent,SIGNATURE_ACTIVITY);
//				 NewOrderActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//				 startActivity(intent);

                if (Product_Variant.getText().toString().trim().equalsIgnoreCase("Select All")) {

                    Global_Data.ProductVariant = Product_Variant.getText().toString().trim();
//					 Intent intent = new Intent(getApplicationContext(), ProductAll_Varients.class);
//					 intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                     //startActivityForResult(intent,SIGNATURE_ACTIVITY);
//                     NewOrderActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                     startActivity(intent);
                } else {
                    editTextQuantity.setEnabled(true);

                    List<Local_Data> cont = dbvoc.getProductByCat(Product_Variant.getText().toString().trim());
                    //results2.add("Select Variant");
                    for (Local_Data cn1 : cont) {
                        String str_var = "" + cn1.getStateName();
                        str_var1 = "" + cn1.getMRP();
                        String str_var2 = "" + cn1.get_Description();
                        String str_var3 = "" + cn1.get_Claims();
                        Global_Data.amnt = "" + cn1.get_Description();
                        Global_Data.amnt1 = "" + cn1.get_Claims();
                        product_code = cn1.getCode();

                        varient_name = Product_Variant.getText().toString().trim();
                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(categ_name)) {
                            categ_name = cn1.getCategory();
                        }

                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(subcateg_name)) {
                            subcateg_name = cn1.getSubcateg();
                        }

//                         editTextRP.setText(str_var);
//                         editTextMRP.setText(str_var1);
//                         txtPrice.setText("Total Price : " + "");

//                         if (editTextQuantity.getText().toString().length() != 0) {
//                             if (!editTextQuantity.getText().toString().equalsIgnoreCase("") && !editTextQuantity.getText().toString().equalsIgnoreCase(null) && !editTextQuantity.getText().toString().equalsIgnoreCase("null") && !editTextQuantity.getText().toString().equalsIgnoreCase("0.0") && !editTextMRP.getText().toString().equalsIgnoreCase("0.0")) {
//                                 long final_mrp = (Long.valueOf(editTextMRP.getText().toString())) * (Long.valueOf(editTextQuantity.getText().toString().trim()));
//                                 txtPrice.setText("Total Price : " + final_mrp);
//                                 price = String.valueOf(final_mrp);
//
//                                 // txtDeleiveryQuantity.setText("Delivery Quantity:"+editTextQuantity.getText().toString());
//                             } else {
//                                 if (!editTextMRP.getText().toString().equalsIgnoreCase("") && !editTextMRP.getText().toString().equalsIgnoreCase(null) && !editTextMRP.getText().toString().equalsIgnoreCase("null") && !editTextMRP.getText().toString().equalsIgnoreCase("0.0")) {
//                                     // Float final_mrp = (Float.valueOf(editTextMRP.getText().toString()));
//                                     // txtPrice.setText("Total Price : "+final_mrp);
//                                     // price = String.valueOf(final_mrp);
//                                     //txtDeleiveryQuantity.setText("Delivery Quantity:"+editTextQuantity.getText().toString());
//                                 }
//                             }
//                         }
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

//                     Scheme_array.clear();
//                     Scheme_array.add("Select Scheme");
//
//                     List<Local_Data> scheme_name = dbvoc.getProductscheme_Name(product_code.trim());
//                     //results2.add("Select Variant");
//                     for (Local_Data s : scheme_name) {
//                         Scheme_array.add(s.getSche_disname());
//                         scheme_namen = s.getSche_disname().toString();
//                     }

//					Discount_Adapter = new ArrayAdapter<String>(NewOrderActivity.this, R.layout.spinner_item, Scheme_array);
//					Discount_Adapter.setDropDownViewResource(R.layout.spinner_item);
//					spnScheme.setAdapter(Discount_Adapter);
//
//				if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(scheme_namen)){
//					int spinnerPosition = Discount_Adapter.getPosition(scheme_namen);
//					spnScheme.setSelection(spinnerPosition);

                    editTextQuantity.setFocusableInTouchMode(true);
                    //}
                }
            }
        });

        Product_Variant.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

//				if(Product_Variant.getText().toString().trim().length() == 0)
//				{
//					Product_Variant.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_icon, 0);
//					searchSpinnerRecycleview.setVisibility(View.GONE);
//					list_ok.setVisibility(View.GONE);
//					//spnBusinessDiv.setVisibility(View.VISIBLE);
//					spnCategory.setVisibility(View.VISIBLE);
//					spnProduct.setVisibility(View.VISIBLE);
//					//spnBu.setVisibility(View.VISIBLE);
//				}
//				else
//				{
//					Product_Variant.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.close_filter, 0);
//				}
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //searchLout.setVisibility(View.GONE);
                searchSpinnerRecycleview.setVisibility(View.GONE);
                list_ok.setVisibility(View.GONE);
                list_cancel.setVisibility(View.GONE);
                show_list_fromsearch.setVisibility(View.GONE);
                //spnBusinessDiv.setVisibility(View.VISIBLE);
                spnCategory.setVisibility(View.VISIBLE);
                spnProduct.setVisibility(View.VISIBLE);
                //spnBu.setVisibility(View.VISIBLE);
                rp = 0.00f;
                mrp = 0.00f;
                productprice = rp;

                editTextRP.setText("" + rp);
                editTextMRP.setText("" + mrp);
                editTextQuantity.setFocusableInTouchMode(false);
                editTextQuantity.setEnabled(false);
                //txtPrice.setText("Total Price : ");

            }
        });

        Product_Variant_search.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (Product_Variant_search.getRight() - Product_Variant_search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        View view = NewOrderActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        if (!Product_Variant_search.getText().toString().equalsIgnoreCase("")) {
//                            Product_Variant_search.setText("");
//                            searchSpinnerRecycleview.setVisibility(View.GONE);
//                            list_ok.setVisibility(View.GONE);
//                            list_cancel.setVisibility(View.GONE);
//                            show_list_fromsearch.setVisibility(View.GONE);
//                            //spnBusinessDiv.setVisibility(View.VISIBLE);
//                            spnCategory.setVisibility(View.VISIBLE);
//                            spnProduct.setVisibility(View.VISIBLE);
                            //spnBu.setVisibility(View.VISIBLE);
                            Intent i = new Intent(getApplicationContext(), NewOrderActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            //searchLout.setVisibility(View.VISIBLE);
                            searchSpinnerRecycleview.setVisibility(View.VISIBLE);
                            list_ok.setVisibility(View.VISIBLE);
                            list_cancel.setVisibility(View.VISIBLE);
                            show_list_fromsearch.setVisibility(View.VISIBLE);
                            //spnBusinessDiv.setVisibility(View.GONE);
                            spnCategory.setVisibility(View.GONE);
                            spnProduct.setVisibility(View.GONE);
                            //spnBu.setVisibility(View.GONE);
                        }
                        searchLout.setVisibility(View.VISIBLE);

                        if(Global_Data.CUSTOMER_SERVICE_FLAG.equalsIgnoreCase("QUOTE"))
                        {
                            show_list_fromsearch.setVisibility(View.GONE);
                        }else{
                            show_list_fromsearch.setVisibility(View.VISIBLE);
                        }

                        //autoCompleteTextView1.setText("");
                        // Product_Variant.showDropDown();
                        return true;
                    }
                }
                return false;
            }
        });


        Product_Variant_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                //Toast.makeText(Order.this," selected", Toast.LENGTH_LONG).show();

                Global_Data.hideSoftKeyboard(NewOrderActivity.this);

                String customer_name = "";
                String address_type = "";
//                if(autoCompleteTextView1.getText().toString().trim().indexOf(":") > 0)
//                {
//                    s = autoCompleteTextView1.getText().toString().trim().split(":");
//                    customer_name = s[0].trim();
//                    address_type = s[1].trim();
//                }
//                else
//                {
//                    customer_name = autoCompleteTextView1.getText().toString().trim();
//                }


            }
        });

        Product_Variant_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Product_Variant_search.getText().toString().trim().length() == 0) {
//                    Product_Variant_search.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_icon, 0);
//                    searchSpinnerRecycleview.setVisibility(View.GONE);
//                    list_ok.setVisibility(View.GONE);
//                    list_cancel.setVisibility(View.GONE);
//                    show_list_fromsearch.setVisibility(View.GONE);
//                    //searchLout.setVisibility(View.GONE);
//                    //spnBusinessDiv.setVisibility(View.VISIBLE);
//                    spnCategory.setVisibility(View.VISIBLE);
//                    spnProduct.setVisibility(View.VISIBLE);
//                    //spnBu.setVisibility(View.VISIBLE);

                    Intent i = new Intent(getApplicationContext(), NewOrderActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    searchLout.setVisibility(View.VISIBLE);
                    if(Global_Data.CUSTOMER_SERVICE_FLAG.equalsIgnoreCase("QUOTE"))
                    {
                        show_list_fromsearch.setVisibility(View.GONE);
                    }else{
                        show_list_fromsearch.setVisibility(View.VISIBLE);
                    }
                    Product_Variant_search.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.close_product, 0);
                }

            }
        });

//		editTextQuantity.addTextChangedListener(new TextWatcher() {
//
//			public void afterTextChanged(Editable s) {
//
//			}
//
//			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//				if(!String.valueOf(s).equalsIgnoreCase(""))
//				{
//					if(Integer.parseInt(String.valueOf(s))<=0)
//					{
//						editTextQuantity.setText("");
//						txtPrice.setText("Total Price : "+"");
//						price = String.valueOf("");
//					}
//				}
//				else
//				{
//					txtPrice.setText("Total Price : "+"");
//					//price = String.valueOf(" ");
//				}
//			}
//		});
//

        Global_Data.GLOVEL_SubCategory_Button = "";

//		//Reading all
//	         List<Local_Data> contacts = dbvoc.getAllMain();
//	          for (Local_Data cn : contacts) {
//	        	Global_Data.local_user = ""+cn.getUser();
//	        	Global_Data.local_pwd = ""+cn.getPwd();
//	        	System.out.println("Local Values:-"+Global_Data.local_user+","+Global_Data.local_pwd);
//	        	//Toast.makeText(LoginActivity.this, "Login:"+Global_Data.local_user,Toast.LENGTH_SHORT).show();
//	        	                             }

        SharedPreferences spf = NewOrderActivity.this.getSharedPreferences("SimpleLogic", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("order", "new");
        editor.commit();

        //Reading all
        List<Local_Data> contacts1 = dbvoc.HSS_DescriptionITEM();
        results1.add(getResources().getString(R.string.Select_Categories));
        for (Local_Data cn : contacts1) {
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

//		final List<String> listCategory = new ArrayList<String>();
//		listCategory.add("Select Category");

        //adapter_state1 = new ArrayAdapter<String>(this,R.layout.spinner_item, results1);

        spnProduct.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
//					// TODO Auto-generated method stub

                //editTextQuantity.setText("");
                if (!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(subcateg_name))) {
                    txtPrice.setText("Total Price : " + "");
                }

                check_product = check_product + 1;
                if (check_product > 1) {


                    if (spnCategory.getSelectedItem().toString().equalsIgnoreCase("Select Category")) {

                        //Toast.makeText(getApplicationContext(), "Please Select Category", Toast.LENGTH_LONG).show();

//						categ_name = "";
//						subcateg_name = "";
//                        Toast toast = Toast.makeText(getApplicationContext(), "Please Select Category", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                        Global_Data.Custom_Toast(getApplicationContext(), "Please Select Category","yes");
                    } else if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase(getResources().getString(R.string.Select_Product))) {

                        //categ_name = "";
                        subcateg_name = "";
                        varient_name = "";
                        results2.add("");
                        results2.clear();

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewOrderActivity.this,
                                R.layout.autocomplete,
                                results2);

                        Product_Variant.setThreshold(1);// will start working from
                        // first character
                        Product_Variant.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView

                        Product_Variant.setText("");
                    } else {
                        results2.clear();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewOrderActivity.this,
                                R.layout.autocomplete,
                                results2);
                        Product_Variant.setThreshold(1);// will start working from
                        // first character
                        Product_Variant.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        //Product_Variant.setText("");

                        if (!subcateg_name.equalsIgnoreCase(parent.getItemAtPosition(pos).toString().trim())) {
                            subcateg_name = "";
                            varient_name = "";

                            subcateg_name = parent.getItemAtPosition(pos).toString().trim();
                            Product_Variant.setText("");
                        }

                        Global_Data.NewProduct = parent.getItemAtPosition(pos).toString().trim();
                        List<Local_Data> contacts33 = dbvoc.HSS_DescriptionITEM1_IDD(parent.getItemAtPosition(pos).toString().trim());
                        for (Local_Data cn : contacts33) {
                            Global_Data.GLOVEL_PRODUCT_ID = cn.getCust_Code();
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.GLOVEL_PRODUCT_ID)) {

                            //List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_ID(Global_Data.GLOVEL_PRODUCT_ID);
                            List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_BYNAME(spnCategory.getSelectedItem().toString().trim(), parent.getItemAtPosition(pos).toString().trim());
                            results2.add("Select All");
                            for (Local_Data cn : contacts3) {
                                str_variant = "" + cn.getStateName();
                                results2.add(str_variant);
                            }

                            Global_Data.Search_Category_name = spnCategory.getSelectedItem().toString().trim();

                            Global_Data.Search_Product_name = parent.getItemAtPosition(pos).toString().trim();

                            ArrayAdapter<String> adaptern = new ArrayAdapter<String>(NewOrderActivity.this,  R.layout.autocomplete, results2);
                            Product_Variant.setThreshold(1);// will start working from
                            // first character
                            Product_Variant.setAdapter(adaptern);// setting the adapter
                            // data into the
                            // AutoCompleteTextView

                            //Product_Variant.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(varient_name));


                        }


                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub


            }
        });

        Intent i = getIntent();
        String name = i.getStringExtra("retialer");
        if (i.hasExtra("data")) {
            Global_Data.productList = i.getParcelableArrayListExtra("productsList");
        }
        buttonAddMOre =  findViewById(R.id.buttonAddMOre);
        buttonAddMOre.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {

                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mpplayer = new MediaPlayer();
					/*if (spnCategory.getSelectedItem().toString()
							.equalsIgnoreCase("Select Category")
							|| spnProduct.getSelectedItem().toString()
									.equalsIgnoreCase("Select Product")
							|| spnCategory.getSelectedItem().toString()
									.equalsIgnoreCase("Select Variant")
							|| editTextQuantity.getText().toString().length() == 0) {

						//Toast.makeText(getApplicationContext(),"Please Fill details ", Toast.LENGTH_SHORT).setGravity(Gravity.CENTER, 0, 0).show();
						Toast toast = Toast.makeText(getApplicationContext(),"Please Fill details ", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
					}
					*/
                    if (spnCategory.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Categories))) {
//                        Toast toast = Toast.makeText(NewOrderActivity.this, getResources().getString(R.string.Please_Select_Category), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(NewOrderActivity.this, getResources().getString(R.string.Please_Select_Category),"yes");
                    } else if (spnProduct.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Product))) {
//                        Toast toast = Toast.makeText(NewOrderActivity.this, getResources().getString(R.string.Please_Select_Product), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(NewOrderActivity.this, getResources().getString(R.string.Please_Select_Product),"yes");
                    } else if (Product_Variant.getText().toString().trim().equalsIgnoreCase("")) {
//                        Toast toast = Toast.makeText(NewOrderActivity.this, getResources().getString(R.string.Please_Select_Variant), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(NewOrderActivity.this, getResources().getString(R.string.Please_Select_Variant), "yes");
                    } else if (editTextQuantity.getText().toString().length() == 0) {
//                        Toast toast = Toast.makeText(NewOrderActivity.this, getResources().getString(R.string.Please_enter_Quantity), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(NewOrderActivity.this, getResources().getString(R.string.Please_enter_Quantity), "yes");
                    }

//					else if (spnScheme.getSelectedItem().toString().equalsIgnoreCase("Select Discount Type")) {
//						Toast toast = Toast.makeText(NewOrderActivity.this,"Please Select Discount Type", Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
//					}

                    else {


                        List<Local_Data> scheme_name = dbvoc.getProductscheme_code(spnScheme.getSelectedItem().toString().trim());
                        //results2.add("Select Variant");
                        if (scheme_name.size() > 0) {
                            for (Local_Data s : scheme_name) {
                                scheme_code = s.getCode();
                            }
                        } else {
                            scheme_code = "";
                        }


                        Global_Data.order_category = spnCategory.getSelectedItem().toString();
                        //Global_Data.order_product = spnProduct.getSelectedItem().toString();
                        // Global_Data.order_variant = spnProductSpec.getSelectedItem().toString();
                        //Global_Data.order_variant = spnProduct.getText().toString().trim();

                        loginDataBaseAdapter = loginDataBaseAdapter.open();

                        String item_name = "";
                        List<Local_Data> contacts1 = dbvoc.getItemCode(spnCategory.getSelectedItem().toString().trim(), spnProduct.getSelectedItem().toString().trim(), Product_Variant.getText().toString().trim());

                        if (contacts1.size() <= 0) {
//                            Toast toast = Toast.makeText(NewOrderActivity.this,
//                                    getResources().getString(R.string.Variant_Not_Found), Toast.LENGTH_SHORT);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(NewOrderActivity.this,
                                    getResources().getString(R.string.Variant_Not_Found),"yes");
                        } else {


                            for (Local_Data cn1 : contacts1) {

                                Global_Data.item_code = cn1.getItem_Code();
                                Global_Data.GLOvel_ITEM_NUMBER = cn1.getItem_Code();
                                item_name = cn1.getProdname();

                            }

//				   	          if(Global_Data.sales_btnstring.equalsIgnoreCase("Secondary Sales / Retail Sales"))
//							  {
//					   	       dbvoc.getDeleteTableorder_bycustomer_PRE(Global_Data.order_retailer.trim(),"Secondary Sales / Retail Sales");
//							   dbvoc.getDeleteTableorderproduct_bycustomer_PRE(Global_Data.order_retailer.trim(),"Secondary Sales / Retail Sales");
//							  }

                            if (Global_Data.GLOVEL_ORDER_REJECT_FLAG.equalsIgnoreCase("TRUE")) {
                                List<Local_Data> contactsn = dbvoc.GetOrders_BY_ORDER_ID(Global_Data.GLObalOrder_id, Global_Data.GLOvel_ITEM_NUMBER);

                                if (contactsn.size() > 0) {
                                    for (Local_Data cn : contactsn) {

                                        int quantity = Integer.parseInt(cn.get_delivery_product_order_quantity()) + Integer.parseInt(editTextQuantity.getText().toString());
                                        Double amount = Double.valueOf(cn.getAmount()) + Double.valueOf(price);
                                        dbvoc.update_itemamountandquantity(String.valueOf(quantity), String.valueOf(amount), Global_Data.GLOvel_ITEM_NUMBER, Global_Data.GLObalOrder_id);
                                    }
//                                    Toast toast = Toast.makeText(NewOrderActivity.this, getResources().getString(R.string.Item_updated_successfully), Toast.LENGTH_SHORT);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(NewOrderActivity.this, getResources().getString(R.string.Item_updated_successfully),"yes");
                                } else {
                                    //Toast.makeText(getApplicationContext(), "Item add successfully", Toast.LENGTH_LONG).show();
                                    if (Global_Data.app_sound == true) {
                                        mpplayer = MediaPlayer.create(NewOrderActivity.this, R.raw.cheer_8k);
                                        mpplayer.stop();
                                    } else {
                                        if (Global_Data.sound_file.length() > 0) {
                                            //Toast.makeText(NewOrderActivity.this, "sndfile:"+Global_Data.sound_file, Toast.LENGTH_SHORT).show();
                                            //mpplayer = new MediaPlayer();
                                            try {
                                                if (mpplayer.isPlaying()) {
                                                    mpplayer.reset();
                                                }
                                                mpplayer = MediaPlayer.create(NewOrderActivity.this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Audio/" + Global_Data.sound_file));
////                                    mp.start();
                                                //mMediaPlayer.setDataSource(filename);
                                                mpplayer.start();
                                                mpplayer.prepare();
                                            } catch (Exception e) {

                                            }

                                        } else {
                                            mpplayer = MediaPlayer.create(NewOrderActivity.this, R.raw.cheer_8k);
                                            mpplayer.start();
                                        }
                                    }

//                                    Toast toast = Toast.makeText(NewOrderActivity.this, getResources().getString(R.string.Item_added_successfully), Toast.LENGTH_SHORT);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(NewOrderActivity.this, getResources().getString(R.string.Item_added_successfully),"yes");
//									  loginDataBaseAdapter.insertOrderProducts("", "",  Global_Data.GLObalOrder_id, "", spnCategory.getSelectedItem().toString() , spnProduct.getText().toString() , Global_Data.order_variant , " " , spnScheme.getSelectedItem().toString() , " ", "", editTextQuantity.getText().toString() , Global_Data.variant_rr,editTextMRP.getText().toString(), price, "", "",Global_Data.order_retailer,discount_amount,Global_Data.GLOvel_ITEM_NUMBER,discount_type);//Reading all

                                    loginDataBaseAdapter.insertOrderProducts(" ", " ", Global_Data.GLObalOrder_id, "", spnCategory.getSelectedItem().toString(), spnProduct.getSelectedItem().toString(), Global_Data.order_variant, " ", scheme_code, " ", "", editTextQuantity.getText().toString(), editTextRP.getText().toString().trim(), Global_Data.variant_mrp, price, "", "", Global_Data.order_retailer, " ", Global_Data.GLOvel_ITEM_NUMBER, " ", item_name, "", "", "", "", "", "", "");
                                }
                            } else {

                                Long randomPIN = System.currentTimeMillis();
                                String PINString = String.valueOf(randomPIN);
                                Global_Data.variant_rr = "";
                                Global_Data.variant_mrp = editTextMRP.getText().toString();
                                Global_Data.order_qty = editTextQuantity.getText().toString();
                                String strAmount = String.valueOf(Global_Data.order_amount);

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String formattedDate = df.format(c.getTime());

                                SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
                                String formattedDatef = dff.format(c.getTime());

                                SharedPreferences spf = NewOrderActivity.this.getSharedPreferences("SimpleLogic", 0);
                                String user_email = spf.getString("USER_EMAIL", null);

                                if (Global_Data.GLOvel_GORDER_ID.equalsIgnoreCase("")) {
                                    if (Global_Data.sales_btnstring.equalsIgnoreCase("Secondary Sales / Retail Sales")) {
                                        Global_Data.GLObalOrder_id = "Ord" + PINString;
                                        Global_Data.GLOvel_GORDER_ID = "Ord" + PINString;
                                    } else {
                                        Global_Data.GLObalOrder_id = "QNO" + PINString;
                                        Global_Data.GLOvel_GORDER_ID = "QNO" + PINString;
                                    }

                                    try {
                                        AppLocationManager appLocationManager = new AppLocationManager(NewOrderActivity.this);
                                        Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                                        Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                        PlayService_Location PlayServiceManager = new PlayService_Location(NewOrderActivity.this);

                                        if (PlayServiceManager.checkPlayServices(NewOrderActivity.this)) {
                                            Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                                        } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                            Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                            Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                        }

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    loginDataBaseAdapter.insertOrders("", Global_Data.GLOvel_GORDER_ID, Global_Data.GLOvel_CUSTOMER_ID, Global_Data.order_retailer, user_email, Global_Data.order_city, Global_Data.order_beat, "", "", "", "", "", "", "", "", Global_Data.order_retailer, Global_Data.order_state, Global_Data.order_city, Global_Data.sales_btnstring, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Glovel_BEAT_ID, "", "", "", "", "", "", "");

                                    if (Global_Data.sales_btnstring.equalsIgnoreCase("Secondary Sales / Retail Sales")) {
                                        loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "NEW ORDER", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);
                                    } else {
                                        loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "NEW QUOTE", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);
                                    }

                                }

                                //loginDataBaseAdapter.insertOrders("", "", Global_Data.order_retailer, "", Global_Data.order_city, Global_Data.order_beat, "", "", "", "", "", "", "", "");

                                //loginDataBaseAdapter.insertOrderProducts("", "", "0"+PINString, "", Global_Data.order_category , Global_Data.order_product , Global_Data.order_variant , " " , spnScheme.getSelectedItem().toString() , " ", "", Global_Data.order_qty , Global_Data.variant_rr, Global_Data.variant_mrp, strAmount, "", "",Global_Data.order_retailer);//Reading all

                                List<Local_Data> contactsn = dbvoc.GetOrders_BY_ORDER_ID(Global_Data.GLObalOrder_id, Global_Data.GLOvel_ITEM_NUMBER);

                                if (contactsn.size() > 0) {
                                    for (Local_Data cn : contactsn) {

                                        int quantity = Integer.parseInt(cn.get_delivery_product_order_quantity()) + Integer.parseInt(editTextQuantity.getText().toString());
                                        Double amount = Double.valueOf(cn.getAmount()) + Double.valueOf(price);
                                        dbvoc.update_itemamountandquantity(String.valueOf(quantity), String.valueOf(amount), Global_Data.GLOvel_ITEM_NUMBER, Global_Data.GLObalOrder_id);
                                    }
//                                    Toast toast = Toast.makeText(NewOrderActivity.this, getResources().getString(R.string.Item_updated_successfully), Toast.LENGTH_SHORT);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(NewOrderActivity.this, getResources().getString(R.string.Item_updated_successfully),"yes");
                                } else {
                                    loginDataBaseAdapter.insertOrderProducts(" ", " ", Global_Data.GLOvel_GORDER_ID, "", spnCategory.getSelectedItem().toString(), spnProduct.getSelectedItem().toString(), Global_Data.order_variant, " ", scheme_code, " ", "", editTextQuantity.getText().toString(), editTextRP.getText().toString().trim(), Global_Data.variant_mrp, price, "", "", Global_Data.order_retailer, " ", Global_Data.item_code, " ", item_name, "", "", "", "", "", "", "");//Reading all

                                    //Toast.makeText(getApplicationContext(), "Item add successfully", Toast.LENGTH_LONG).show();

                                    if (Global_Data.app_sound == true) {
                                        mpplayer = MediaPlayer.create(NewOrderActivity.this, R.raw.cheer_8k);
                                        mpplayer.stop();
                                    } else {
                                        if (Global_Data.sound_file.length() > 0) {
                                            //Toast.makeText(NewOrderActivity.this, "sndfile:"+Global_Data.sound_file, Toast.LENGTH_SHORT).show();
                                            //mpplayer = new MediaPlayer();
                                            try {
                                                if (mpplayer.isPlaying()) {
                                                    mpplayer.reset();
                                                }
                                                mpplayer = MediaPlayer.create(NewOrderActivity.this, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/Audio/" + Global_Data.sound_file));
////                                    mp.start();
                                                //mMediaPlayer.setDataSource(filename);
                                                mpplayer.start();
                                                mpplayer.prepare();
                                            } catch (Exception e) {

                                            }

                                        } else {
                                            mpplayer = MediaPlayer.create(NewOrderActivity.this, R.raw.cheer_8k);
                                            mpplayer.start();
                                        }
                                    }
                                    //mpplayer.start();
//                                    Toast toast = Toast.makeText(NewOrderActivity.this, getResources().getString(R.string.Item_added_successfully), Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(NewOrderActivity.this, getResources().getString(R.string.Item_added_successfully),"yes");
                                    //loginDataBaseAdapter.insertOrderProducts("", "",  Global_Data.GLOvel_GORDER_ID, "", spnCategory.getSelectedItem().toString() , spnProduct.getText().toString() , Global_Data.order_variant , " " , spnScheme.getSelectedItem().toString() , " ", "", editTextQuantity.getText().toString() , Global_Data.variant_rr,editTextMRP.getText().toString(), price, "", "",Global_Data.order_retailer,discount_amount,Global_Data.GLOvel_ITEM_NUMBER,discount_type);//Reading all
                                }
                            }

                            check = 0;
                            check_product = 0;
                            check_ProductSpec = 0;
                            editTextQuantity.setText("");
                            editTextQuantity.setEnabled(false);
                            spnCategory.setSelection(adapter_state1.getPosition(getResources().getString(R.string.Select_Categories)));
                            // spnProductSpec.setSelection(adapter_state3.getPosition("Select Variant"));
                            Product_Variant.setText("");
                            spnProduct.setSelection(adapter_state2.getPosition(getResources().getString(R.string.Select_Product)));
                            spnScheme.setSelection(Discount_Adapter.getPosition(getResources().getString(R.string.Select_Scheme)));
//									spnScheme.setEnabled(false);
                            editTextMRP.setText("");
                            txtPrice.setText("");
                            editTextRP.setText("");

                        }
                        // spnProduct.setText("");

//							spnCategory.setAdapter(adapter_state1);
//
//							listProduct.clear();
//							listProduct.add("Select Product");
//
//							dataAdapterProduct.notifyDataSetChanged();
//							dataAdapterProduct
//									.setDropDownViewResource(android.R.layout.simple_spinner_item);
//							//spnProduct.setAdapter(adapter_state2);
//
//							listProductSpec.clear();
//							listProductSpec.add("Select Variant");
//							dataAdapterProductSpec.notifyDataSetChanged();
//							dataAdapterProductSpec
//									.setDropDownViewResource(android.R.layout.simple_spinner_item);
//							spnProductSpec.setAdapter(adapter_state3);
//
//							listScheme.clear();
//							listScheme.add("Select Scheme");
//
//							dataAdapterScheme.notifyDataSetChanged();
//							dataAdapterScheme
//									.setDropDownViewResource(android.R.layout.simple_spinner_item);
//							spnScheme.setAdapter(dataAdapterScheme);
//
//							editTextRP.setText("" + rp);
//							editTextMRP.setText("" + mrp);
//							txtPrice.setText("Total Price : ");
//							rp = 0.00f;
//							mrp = 0.00f;
//							totalprice = 0.00f;
//							deleiveryQuantity = 0;
//							editTextQuantity.setText("");
                        //txtDeleiveryQuantity.setText("Delivery Quantity :");
                        return true;
                        //}
                    }

                    return true;
                }
                return false;
            }
        });

        buttonPreviewOrder =  findViewById(R.id.buttonPreviewOrder);
        show_list.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mpplayer.stop();
                    if (Product_Variant.getText().toString().length() > 0) {

                        Global_Data.Order_hashmap.clear();
                        Global_Data.ProductVariant = Product_Variant.getText().toString().trim();
                        Intent intent = new Intent(getApplicationContext(), ProductAll_Varients.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        //startActivityForResult(intent,SIGNATURE_ACTIVITY);
                        NewOrderActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        startActivity(intent);
                    } else {
//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Select_Variant), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Select_Variant),"yes");
                    }

                    return true;
                }
                return false;
            }
        });

        online_catalogue.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mpplayer.stop();
                    //down event

                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        if (Product_Variant.getText().toString().length() > 0) {
                            Global_Data.CatlogueStatus = "online";
                            Global_Data.ProductVariant = Product_Variant.getText().toString().trim();
                            Global_Data.Order_hashmap.clear();
                            Intent intent = new Intent(getApplicationContext(), OnlineCatalogue.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            //startActivityForResult(intent,SIGNATURE_ACTIVITY);
                            NewOrderActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            startActivity(intent);
                        } else {
//                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Select_Variant), Toast.LENGTH_SHORT);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Select_Variant),"yes");
                        }
                    } else {
                        //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();

//                        Toast toast = Toast.makeText(NewOrderActivity.this, getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(NewOrderActivity.this, getResources().getString(R.string.internet_connection_error),"yes");

                    }


                    return true;
                }
                return false;
            }
        });

        buttonPreviewOrder.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mpplayer.stop();
                    if (Global_Data.GLOVEL_ORDER_REJECT_FLAG.equalsIgnoreCase("TRUE")) {
                        Intent i = new Intent(NewOrderActivity.this, Status_Activity.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        startActivity(i);
                        finish();
                    } else {
                        if(Global_Data.CUSTOMER_SERVICE_FLAG.equalsIgnoreCase("QUOTE"))
                        {
                            if (!Global_Data.GLOvel_GORDER_ID.equalsIgnoreCase("")) {
                                Global_Data.Order_hashmap.clear();
                                Global_Data.ProductVariant = Product_Variant.getText().toString().trim();
                                Intent intent = new Intent(getApplicationContext(), ActivityQuote.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                //startActivityForResult(intent,SIGNATURE_ACTIVITY);
                                NewOrderActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                startActivity(intent);
                            }else{
//                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.No_Items_Added), Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.No_Items_Added),"yes");
                            }

                        }else{
                            if (!Global_Data.GLOvel_GORDER_ID.equalsIgnoreCase("")) {
									/*final Intent i = new Intent(getApplicationContext(),
											PreviewOrderActivity.class);*/
                                final Intent i = new Intent(getApplicationContext(),
                                        PreviewOrderSwipeActivity.class);
                                i.putParcelableArrayListExtra("productsList", Global_Data.productList);
                                i.putExtra("new", "new");
                                SharedPreferences sp = NewOrderActivity.this
                                        .getSharedPreferences("SimpleLogic", 0);

                                i.putExtra("retialer",
                                        "" + sp.getString("RetailerName", ""));
                                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Global_Data.GLOVEL_LONG_DESC = "";
                                Global_Data.GLOVEL_CATEGORY_SELECTION = "";
                                Global_Data.GLOVEL_ITEM_MRP = "";

                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                ActivitySwitcher.animationOut(findViewById(R.id.containerNewOrder), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
                                    @Override
                                    public void onAnimationFinished() {
                                        startActivity(i);
                                        finish();
                                    }
                                });
                                //NewOrderFragment.this.startActivity(i);
                            } else {
                                //Toast.makeText(getBaseContext(), "No Items Added", Toast.LENGTH_SHORT).show();

//                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.No_Items_Added), Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.No_Items_Added),"yes");
                            }

//                            if (!Global_Data.GLOvel_GORDER_ID.equalsIgnoreCase("")) {
//									/*final Intent i = new Intent(getApplicationContext(),
//											PreviewOrderActivity.class);*/
//                                final Intent i = new Intent(getApplicationContext(),
//                                        PreviewOrderSwipeActivity.class);
//                                i.putParcelableArrayListExtra("productsList", Global_Data.productList);
//                                i.putExtra("new", "new");
//                                SharedPreferences sp = NewOrderActivity.this
//                                        .getSharedPreferences("SimpleLogic", 0);
//
//                                i.putExtra("retialer",
//                                        "" + sp.getString("RetailerName", ""));
//                                //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                Global_Data.GLOVEL_LONG_DESC = "";
//                                Global_Data.GLOVEL_CATEGORY_SELECTION = "";
//                                Global_Data.GLOVEL_ITEM_MRP = "";
//
//                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                ActivitySwitcher.animationOut(findViewById(R.id.containerNewOrder), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
//                                    @Override
//                                    public void onAnimationFinished() {
//                                        startActivity(i);
//                                        finish();
//                                    }
//                                });
//                                //NewOrderFragment.this.startActivity(i);
//                            } else {
//                                //Toast.makeText(getBaseContext(), "No Items Added", Toast.LENGTH_SHORT).show();
//
//                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.No_Items_Added), Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//                            }
                        }


                    }

                    return true;
                }
                return false;
            }
        });

        spnCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
//				// TODO Auto-generated method stub

//				// Toast.makeText(parent.getContext(),
//				// "OnItemSelectedListener : " +
//				// parent.getItemAtPosition(pos).toString(),Toast.LENGTH_SHORT).show();
//				editTextQuantity.setFocusableInTouchMode(false);
//				editTextQuantity.setEnabled(false);
//				txtPrice.setText("Total Price : ");
//
                Global_Data.GLOVEL_ITEM_MRP = "";

//				category = parent.getItemAtPosition(pos).toString();
                Log.d("Globel categary", Global_Data.GLOVEL_CATEGORY_SELECTION);

                check = check + 1;
                if (check > 1) {

                    // editTextQuantity.setText("");

//					if(!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(subcateg_name))){
//						txtPrice.setText("Total Price : "+"");
//					}


//				if (!Global_Data.GLOVEL_CATEGORY_SELECTION.equalsIgnoreCase(""))
//				{
//					Log.d("Globel categary selection", "in");
//					spnCategory.setSelection(adapter_state1.getPosition(Global_Data.GLOVEL_CATEGORY_SELECTION));
//
//
//				}
//				else
//				{
//                 if (Global_Data.GLOVEL_CATEGORY_SELECTION.equalsIgnoreCase(""))
//                 {
                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase(getResources().getString(R.string.Select_Categories))) {

                        categ_name = "";
                        subcateg_name = "";
                        varient_name = "";

                        results.clear();
                        results.add(getResources().getString(R.string.Select_Product));
                        adapter_state2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results);
                        adapter_state2.setDropDownViewResource(R.layout.spinner_item);
                        spnProduct.setAdapter(adapter_state2);
                        //spnProduct.setOnItemSelectedListener(NewOrderActivity.this);

//						results2.clear();
//						results2.add("Select Variant");
//						adapter_state3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results2);
//				        adapter_state3.setDropDownViewResource(R.layout.spinner_item);
//						spnProductSpec.setAdapter(adapter_state3);

                        results2.clear();

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewOrderActivity.this,
                                R.layout.autocomplete,
                                result_product);
                        Product_Variant.setThreshold(1);// will start working from
                        // first character
                        Product_Variant.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView

                        Product_Variant.setText("");

//						Toast toast = Toast.makeText(NewOrderActivity.this, "Please Select Category", Toast.LENGTH_LONG);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();

                    } else {
                        Global_Data.NewCategory = parent.getItemAtPosition(pos).toString();
                        Global_Data.GLOVEL_CATEGORY_SELECTION = parent.getItemAtPosition(pos).toString();
                        //Intent intent = new Intent(getApplicationContext(), Filter_List.class);
                        Global_Data.GLOVEL_CATEGORY_NAME = parent.getItemAtPosition(pos).toString();

                        categ_name = parent.getItemAtPosition(pos).toString();

                        List<Local_Data> contacts2 = dbvoc.HSS_DescriptionITEM_ID(parent.getItemAtPosition(pos).toString().trim());
                        //results.add("Select Product");
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
                        //startActivity(intent);
                        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    }
//                 }
//                 else
//                 {
//                	 Global_Data.GLOVEL_CATEGORY_SELECTION = "";
//                 }


                    //}
                }
//
//				else {
//
//					//categoryID = Integer.parseInt(categoriesMap.get(""+parent.getSelectedItemId()));
//					categoryID =dataCategories.get(pos-1).getId();
//					CategoriesSpinner = parent.getItemAtPosition(pos).toString();
//					LoadProductsAsyncTask loadProductsAsyncTask=new LoadProductsAsyncTask(NewOrderActivity.this);
//					loadProductsAsyncTask.execute();
//
//				}

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    try {
                        if (editTextQuantity.getText().toString().length() != 0) {

                            if (!editTextQuantity.getText().toString().equalsIgnoreCase("") && !editTextQuantity.getText().toString().equalsIgnoreCase(null) && !editTextQuantity.getText().toString().equalsIgnoreCase("null") && !editTextQuantity.getText().toString().equalsIgnoreCase("0.0") && !editTextMRP.getText().toString().equalsIgnoreCase("0.0")) {
                                double final_mrp = (Double.valueOf(editTextMRP.getText().toString())) * (Double.valueOf(editTextQuantity.getText().toString().trim()));
                                txtPrice.setText("Total Price : " + final_mrp);
                                price = String.valueOf(final_mrp);

                                // txtDeleiveryQuantity.setText("Delivery Quantity:"+editTextQuantity.getText().toString());
                            } else {
                                if (!editTextMRP.getText().toString().equalsIgnoreCase("") && !editTextMRP.getText().toString().equalsIgnoreCase(null) && !editTextMRP.getText().toString().equalsIgnoreCase("null") && !editTextMRP.getText().toString().equalsIgnoreCase("0.0")) {
                                    // Float final_mrp = (Float.valueOf(editTextMRP.getText().toString()));
                                    // txtPrice.setText("Total Price : "+final_mrp);
                                    // price = String.valueOf(final_mrp);
                                    //txtDeleiveryQuantity.setText("Delivery Quantity:"+editTextQuantity.getText().toString());
                                }
                            }
//
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //Field2.setText("");
            }
        });

        spnScheme.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                // TODO Auto-generated method stub
                productScheme = parent.getItemAtPosition(pos).toString();
                //Toast.makeText(NewOrderActivity.this, "click appear.", Toast.LENGTH_SHORT).show();
                if (parent.getItemAtPosition(pos).toString()
                        .equalsIgnoreCase(getResources().getString(R.string.Select_Scheme))) {

                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
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
                mpplayer.stop();
                return true;
        }
//        return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add:
                String targetNew="";
                SharedPreferences sp = NewOrderActivity.this.getSharedPreferences("SimpleLogic", 0);
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
        // TODO Auto-generated method stub
        //super.onBackPressed();
        Global_Data.spiner_list_modelList.clear();
        Global_Data.array_of_pVarient.clear();
        Global_Data.GLOVEL_LONG_DESC = "";
        Global_Data.GLOVEL_CATEGORY_SELECTION = "";
        Global_Data.GLOVEL_ITEM_MRP = "";
        Global_Data.Search_Category_name = "";

        Global_Data.Search_Product_name = "";
        // Global_Data.productList.clear();

        if (Global_Data.PREVIOUS_ORDER_BACK_FLAG.equalsIgnoreCase("TRUE")) {
            Intent i = new Intent(NewOrderActivity.this, PreviewOrderSwipeActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            startActivity(i);
            finish();
        } else {
            if (!Global_Data.GLOvel_GORDER_ID.equalsIgnoreCase("")) {

                AlertDialog alertDialog = new AlertDialog.Builder(NewOrderActivity.this).create(); //Read Update
                alertDialog.setTitle(getResources().getString(R.string.Warning));
                alertDialog.setMessage(getResources().getString(R.string.Order_dialog_message));
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(NewOrderActivity.this, PreviewOrderSwipeActivity.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        startActivity(i);
                        finish();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.Cancel_Order), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbvoc = new DataBaseHelper(NewOrderActivity.this);
                        dbvoc.getDeleteTableorder_byOID(Global_Data.GLObalOrder_id);
                        dbvoc.getDeleteTableorderproduct_byOID(Global_Data.GLObalOrder_id);
                        Global_Data.GLOvel_GORDER_ID = "";
                        Global_Data.GLObalOrder_id = "";

                     //   Toast.makeText(NewOrderActivity.this, getResources().getString(R.string.Order_Canceled_Successfully), Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(NewOrderActivity.this, getResources().getString(R.string.Order_Canceled_Successfully),"");
                        Intent order_home = new Intent(getApplicationContext(), Neworderoptions.class);
                        startActivity(order_home);

                        finish();
                    }
                });

                alertDialog.show();
            } else {

                if (Global_Data.PREVIOUS_ORDER_BACK_FLAG.equalsIgnoreCase("TRUE")) {
                    if (!Global_Data.GLObalOrder_id.equalsIgnoreCase("")) {
//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Order_save_successfully), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Order_save_successfully),"yres");
                    }
                    Intent i = new Intent(NewOrderActivity.this, PreviewOrderSwipeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    if (!Global_Data.GLObalOrder_id.equalsIgnoreCase("")) {

//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Order_save_successfully), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Order_save_successfully),"yes");
                    }
//                    Intent i = new Intent(NewOrderActivity.this, Neworderoptions.class);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                    startActivity(i);
                    finish();
                }
            }
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stubs
        super.onResume();
        Global_Data.array_of_pVarient.clear();

    }

}
