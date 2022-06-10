package com.msimplelogic.activities;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jsibbold.zoomage.ZoomageView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.adapter.AutoCompleteAdapter;
import com.msimplelogic.adapter.ReturnOrderImageAdapter;
import com.msimplelogic.adapter.SpinnerListAdapter;
import com.msimplelogic.model.Category;
import com.msimplelogic.model.SpinerListModel;
import com.msimplelogic.services.getServices;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cpm.simplelogic.helper.ConnectionDetector;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class ReturnOrder1 extends BaseActivity {
    static int quantity = 0;
    static float rp, mrp, totalprice, totalprice1, totalprc_scheme, productprice;
    static String scheme = "";
    static String category, productScheme;
    int check = 0;
    Button list_ok, show_list_fromsearch;
    RelativeLayout searchLout;
    Boolean B_flag;
    MediaPlayer mpplayer;
    String categ_name, subcateg_name, varient_name;
    int check_product = 0;
    int check_ProductSpec = 0;
    AutoCompleteTextView Product_Variant,Product_Variant_search;
    String price = "";
    String str_variant, price_str;
    LoginDataBaseAdapter loginDataBaseAdapter;
    Spinner spnCategory, spnScheme, spnProduct;
    TextView editTextRP, editTextMRP, txtDeleiveryQuantity, txt_rp, textMRP;
    EditText editTextQuantity, txtPrice, editTextbatchno, refund_amount, return_order_remarks;
    Button buttonAddMOre, buttonPreviewOrder;
    ArrayAdapter<String> dataAdapterCategory, dataAdapterProductSpec, dataAdapterProduct;
    List<String> listProduct, listProductSpec;
    List<String> listScheme;
    LinearLayout info;
    Button btn_search,list_cancel;
    ArrayAdapter<String> dataAdapterScheme;
    ArrayAdapter<String> adapter_state1;
    ArrayAdapter<String> adapter_state2;
    ArrayAdapter<String> adapter_state3;
    SimpleCursorAdapter cursorAdapter;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    ArrayList<Category> dataCategories = new ArrayList<Category>();
    private ArrayList<String> results = new ArrayList<String>();
    private ArrayList<String> results1 = new ArrayList<String>();
    private ArrayList<String> results2 = new ArrayList<String>();
    private ArrayList<String> search_result = new ArrayList<String>();
    private ArrayList<String> result_product = new ArrayList<String>();
    Toolbar toolbar;
    ZoomageView imgdialog;
    ImageView camerabtn;
    //  FrameLayout img_layout;
    String image_check = "";
    private String pictureImagePath = "";
    private String pictureImagePath_new = "";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    LinearLayoutManager HorizontalLayout;
    ReturnOrderImageAdapter RecyclerViewHorizontalAdapter;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    HashMap<String, String> product_map = new HashMap<>();
    private String category_click = "";
    AutoCompleteAdapter AutoSearchAdapter;
    List<SpinerListModel> snlist = new ArrayList<>();
    RecyclerView searchSpinnerRecycleview;
    SpinnerListAdapter spinner_list_adapter;
    ImageView hedder_theame;
SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  setTitle(getResources().getString(R.string.Return_Order));

        Global_Data.image_counter = 0;

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        // crossimg=findViewById(R.id.crossimg);
        //img_layout= (FrameLayout) findViewById(R.id.img_layout);
        // img_show=(ImageView) findViewById(R.id.img_show);
        btn_search = (Button) findViewById(R.id.btn_search);
     //   show_list_fromsearch = findViewById(R.id.show_list_fromsearch);
        list_ok = findViewById(R.id.list_ok);
        list_cancel = findViewById(R.id.list_cancel);
        searchLout = findViewById(R.id.search_lout);
        searchSpinnerRecycleview = findViewById(R.id.search_spinner_recycleview);
        Product_Variant_search = findViewById(R.id.Product_Variant_search);
        info = (LinearLayout) findViewById(R.id.info);
        camerabtn = (ImageView) findViewById(R.id.camerabtn);
        textMRP = (TextView) findViewById(R.id.textMRP);
        txtPrice = (EditText) findViewById(R.id.txtPrice1);
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnProduct = (Spinner) findViewById(R.id.spnProduct);
        Product_Variant = (AutoCompleteTextView) findViewById(R.id.Product_Variant);
        //spnProductSpec = (Spinner) findViewById(R.id.spnProductSpec);
        spnScheme = (Spinner) findViewById(R.id.spnScheme1);
        //spnScheme.setVisibility(View.INVISIBLE);

        editTextRP = (TextView) findViewById(R.id.editTextRP);
        editTextMRP = (TextView) findViewById(R.id.editTextMRP);

        editTextbatchno = findViewById(R.id.editTextbatchno);
        refund_amount = findViewById(R.id.txtPrice1);
        return_order_remarks = findViewById(R.id.return_order_remarks);

        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity1);

        txt_rp = (TextView) findViewById(R.id.textRP);
        hedder_theame = findViewById(R.id.hedder_theame);

        recyclerView = findViewById(R.id.recyclerview1);

        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(RecyclerViewLayoutManager);

        cd = new ConnectionDetector(getApplicationContext());

        list_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                        Intent i = new Intent(getApplicationContext(), ReturnOrder1.class);
                        startActivity(i);

            }
        });

        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);

        }

        searchSpinnerRecycleview.setLayoutManager(new LinearLayoutManager(ReturnOrder1.this));
        AutoSearchAdapter = new AutoCompleteAdapter(this, R.layout.autocomplete);
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

        searchSpinnerRecycleview.setLayoutManager(new LinearLayoutManager(ReturnOrder1.this));
        spinner_list_adapter = new SpinnerListAdapter(ReturnOrder1.this, snlist);
        searchSpinnerRecycleview.setAdapter(spinner_list_adapter);


        list_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                searchLout.setVisibility(View.GONE);
//                searchSpinnerRecycleview.setVisibility(View.GONE);
//                list_ok.setVisibility(View.GONE);
//                list_cancel.setVisibility(View.GONE);
                //    show_list_fromsearch.setVisibility(View.VISIBLE);
                //spnBusinessDiv.setVisibility(View.GONE);
//                spnCategory.setVisibility(View.VISIBLE);
//                spnProduct.setVisibility(View.VISIBLE);
//                btn_search.setVisibility(View.GONE);
//                //spnBu.setVisibility(View.GONE);
//                info.setVisibility(View.VISIBLE);
//                buttonPreviewOrder.setVisibility(View.VISIBLE);
//                camerabtn.setVisibility(View.VISIBLE);

                String data = "";
                //      Global_Data.array_of_pVarient.clear();
                for (int i = 0; i < Global_Data.spiner_list_modelList.size(); i++) {
                    SpinerListModel singleStudent = Global_Data.spiner_list_modelList.get(i);
                    if (singleStudent.isSelected() == true) {
                        data = singleStudent.getCode();
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
                    Global_Data.return_oredr.clear();
                    Global_Data.GLObalOrder_id_return = "";
                    Global_Data.GLOvel_GORDER_ID_RETURN = "";
                    Global_Data.Orderproduct_remarks.clear();
                    Global_Data.Orderproduct_detail1.clear();
                    Global_Data.ProductVariant = "";
                    Global_Data.GLOVEL_CATEGORY_SELECTION = "";
                    Global_Data.Search_Product_name = "";
                    Intent intent = new Intent(ReturnOrder1.this,return_order2.class);
                    startActivity(intent);
                    //finish();
                    //Global_Data.Search_BusinessCategory_name = "";

                } else {
                   // Toast.makeText(getApplicationContext(), "Please select product variant.", Toast.LENGTH_SHORT).show();
                    Global_Data.Custom_Toast(getApplicationContext(), "Please select product variant.","");
                }

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

                        View view = ReturnOrder1.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        if (!Product_Variant_search.getText().toString().equalsIgnoreCase("")) {
//                            Product_Variant_search.setText("");
//                            searchSpinnerRecycleview.setVisibility(View.GONE);
//                            list_ok.setVisibility(View.GONE);
//                            list_cancel.setVisibility(View.GONE);
//                      //      show_list_fromsearch.setVisibility(View.GONE);
//                            //spnBusinessDiv.setVisibility(View.VISIBLE);
//                            spnCategory.setVisibility(View.VISIBLE);
//                            spnProduct.setVisibility(View.VISIBLE);

                            Intent i = new Intent(getApplicationContext(), ReturnOrder1.class);
                            startActivity(i);
                            finish();

                            //spnBu.setVisibility(View.VISIBLE);
                        } else {
                            searchLout.setVisibility(View.VISIBLE);
                            searchSpinnerRecycleview.setVisibility(View.VISIBLE);
                            list_ok.setVisibility(View.VISIBLE);
                            list_cancel.setVisibility(View.VISIBLE);
                        //    show_list_fromsearch.setVisibility(View.VISIBLE);
                            //spnBusinessDiv.setVisibility(View.GONE);
                            spnCategory.setVisibility(View.GONE);
                            spnProduct.setVisibility(View.GONE);
                            btn_search.setVisibility(View.GONE);
                            //spnBu.setVisibility(View.GONE);
                            info.setVisibility(View.GONE);
                            buttonPreviewOrder.setVisibility(View.GONE);
                            camerabtn.setVisibility(View.GONE);
                        }
                       searchLout.setVisibility(View.VISIBLE);



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

                Global_Data.hideSoftKeyboard(ReturnOrder1.this);

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
//               //     show_list_fromsearch.setVisibility(View.GONE);
//                    //searchLout.setVisibility(View.GONE);
//                    //spnBusinessDiv.setVisibility(View.VISIBLE);
//                    spnCategory.setVisibility(View.VISIBLE);
//                    spnProduct.setVisibility(View.VISIBLE);
//                    btn_search.setVisibility(View.VISIBLE);
//                    //spnBu.setVisibility(View.GONE);
//                    info.setVisibility(View.VISIBLE);
//                    buttonPreviewOrder.setVisibility(View.VISIBLE);
//                    camerabtn.setVisibility(View.VISIBLE);
                    //spnBu.setVisibility(View.VISIBLE);

                    Intent i = new Intent(getApplicationContext(), ReturnOrder1.class);
                    startActivity(i);
                    finish();

                } else {
                    searchLout.setVisibility(View.VISIBLE);
                    if(Global_Data.CUSTOMER_SERVICE_FLAG.equalsIgnoreCase("QUOTE"))
                    {
                       // show_list_fromsearch.setVisibility(View.GONE);
                    }else{
                      //  show_list_fromsearch.setVisibility(View.VISIBLE);
                    }
                    Product_Variant_search.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.close_product, 0);
                }

            }
        });
        // for label RP change
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        String rpstr = spf1.getString("var_rp", "");
        String mrpstr = spf1.getString("var_mrp", "");
        price_str = spf1.getString("var_total_price", "");

        if (rpstr.length() > 0) {
            txt_rp.setText(rpstr);
        } else {
            txt_rp.setText(getResources().getString(R.string.RP));
        }

        if (mrpstr.length() > 0) {
            textMRP.setText(mrpstr);
        } else {
            textMRP.setText(getResources().getString(R.string.MRP));
        }


        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission2();
            }
        });

//        img_show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(pictureImagePath_new!="") {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(ReturnOrder1.this);
//                    LayoutInflater inflater = getLayoutInflater();
//                    View dialogLayout = inflater.inflate(R.layout.dialogeimage, null);
//                    imgdialog = dialogLayout.findViewById(R.id.imageView);
//                    Glide.with(ReturnOrder1.this).load(pictureImagePath_new).into(imgdialog);
//                    builder.setPositiveButton("OK", null);
//                    builder.setView(dialogLayout);
//                    builder.show();
//                }
//
//            }
//        });
//        crossimg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                deletedialog();
//            }
//        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Product_Variant.getText().toString().trim().equalsIgnoreCase("")) {
//                    Toast toast = Toast.makeText(ReturnOrder1.this, getResources().getString(R.string.Please_Select_Variant), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(ReturnOrder1.this, getResources().getString(R.string.Please_Select_Variant),"yes");

                } else {
                    btn_search.setVisibility(View.GONE);
                    info.setVisibility(View.VISIBLE);
                    buttonPreviewOrder.setVisibility(View.VISIBLE);
                    camerabtn.setVisibility(View.VISIBLE);

                }

            }
        });


        SharedPreferences spf11 = ReturnOrder1.this.getSharedPreferences("SimpleLogic", 0);
        Global_Data.app_sound = spf11.getBoolean("var_addmore", false);

        SharedPreferences sound = ReturnOrder1.this.getSharedPreferences("SimpleLogic", 0);
        Global_Data.sound_file = sound.getString("var_addsound", "");

        results2.clear();
        List<Local_Data> contacts2 = dbvoc.getAllVariant();
        for (Local_Data cn : contacts2) {
            results2.add(cn.getStateName());
            result_product.add(cn.getStateName());
            product_map.put(cn.getStateName(), cn.getCode());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.autocomplete,
                results2);
        Product_Variant.setThreshold(1);// will start working from
        // first character
        Product_Variant.setAdapter(adapter);// setting the adapter
        // data into the
        // AutoCompleteTextView

        Product_Variant.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (Product_Variant.getRight() - Product_Variant.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        View view = ReturnOrder1.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        //autoCompleteTextView1.setText("");
                        Product_Variant.showDropDown();
                        btn_search.setVisibility(View.VISIBLE);
                        info.setVisibility(View.GONE);
                        buttonPreviewOrder.setVisibility(View.GONE);
                        camerabtn.setVisibility(View.GONE);
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

                Global_Data.hideSoftKeyboard(ReturnOrder1.this);

              //  editTextQuantity.setFocusableInTouchMode(true);
               // editTextQuantity.setEnabled(true);

                varient_name = Product_Variant.getText().toString().trim();
                Global_Data.ProductVariant = Product_Variant.getText().toString().trim();


                Global_Data.return_oredr.clear();
                Global_Data.GLObalOrder_id_return = "";
                Global_Data.GLOvel_GORDER_ID_RETURN = "";
                Global_Data.Orderproduct_remarks.clear();
                Global_Data.Orderproduct_detail1.clear();
                Global_Data.array_of_pVarient.clear();

                Intent intent = new Intent(ReturnOrder1.this,return_order2.class);
                startActivity(intent);

                List<Local_Data> cont = dbvoc.getProductByCat(Product_Variant.getText().toString().trim());
                //results2.add("Select Variant");
                for (Local_Data cn1 : cont) {
                    String str_var = "" + cn1.getStateName();
                    String str_var1 = "" + cn1.getMRP();
                    String str_var2 = "" + cn1.get_Description();
                    String str_var3 = "" + cn1.get_Claims();
                    Global_Data.amnt = "" + cn1.get_Description();
                    Global_Data.amnt1 = "" + cn1.get_Claims();

                    varient_name = Product_Variant.getText().toString().trim();
                   // if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(categ_name)) {
                        categ_name = cn1.getCategory();
                  //  }

                  //  if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(subcateg_name)) {
                        subcateg_name = cn1.getSubcateg();
                   // }

                    editTextRP.setText(str_var);
                    editTextMRP.setText(str_var1);

                    txtPrice.setText("");
                    // txtPrice.setText(getResources().getString(R.string.Total_Price) + "");

                    if (editTextQuantity.getText().toString().length() != 0) {
                        if (!editTextQuantity.getText().toString().equalsIgnoreCase("") && !editTextQuantity.getText().toString().equalsIgnoreCase(null) && !editTextQuantity.getText().toString().equalsIgnoreCase("null") && !editTextQuantity.getText().toString().equalsIgnoreCase("0.0") && !editTextMRP.getText().toString().equalsIgnoreCase("0.0")) {
                            long final_mrp = (Long.valueOf(editTextMRP.getText().toString())) * (Long.valueOf(editTextQuantity.getText().toString().trim()));

                            if (price_str.length() > 0) {
                                // txtPrice.setText(price_str + " : " + final_mrp);
                                txtPrice.setText(price_str + " : " + final_mrp);
                            } else {
                                //   txtPrice.setText(getResources().getString(R.string.Total_Price) + final_mrp);
                                txtPrice.setText("" + final_mrp);

                            }


                            //price = String.valueOf(final_mrp);

                            // txtDeleiveryQuantity.setText("Delivery Quantity:"+editTextQuantity.getText().toString());
                        } else {
                            if (!editTextMRP.getText().toString().equalsIgnoreCase("") && !editTextMRP.getText().toString().equalsIgnoreCase(null) && !editTextMRP.getText().toString().equalsIgnoreCase("null") && !editTextMRP.getText().toString().equalsIgnoreCase("0.0")) {
                                // Float final_mrp = (Float.valueOf(editTextMRP.getText().toString()));
                                // txtPrice.setText("Total Price : "+final_mrp);
                                // price = String.valueOf(final_mrp);
                                //txtDeleiveryQuantity.setText("Delivery Quantity:"+editTextQuantity.getText().toString());
                            }
                        }
                    }
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

        Product_Variant.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                listScheme.clear();
//                listScheme.add(getResources().getString(R.string.Select_Scheme));
//
//                dataAdapterScheme.notifyDataSetChanged();
//                dataAdapterScheme.setDropDownViewResource(R.layout.spinner_item);
//                spnScheme.setAdapter(dataAdapterScheme);
//                rp = 0.00f;
//                mrp = 0.00f;
//                productprice = rp;
//
//                editTextRP.setText("" + rp);
//                editTextMRP.setText("" + mrp);
////                editTextQuantity.setFocusableInTouchMode(false);
////                editTextQuantity.setEnabled(false);
//                //txtPrice.setText("Total Price : ");
//
//                if (Product_Variant.getText().toString().trim().length() == 0 && category_click.equalsIgnoreCase("")) {
//
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReturnOrder1.this,
//                            android.R.layout.simple_spinner_dropdown_item,
//                            results2);
//                    Product_Variant.setThreshold(1);// will start working from
//                    // first character
//                    Product_Variant.setAdapter(adapter);// setting the adapter
//                    // data into the
//                    // AutoCompleteTextView
//                    Product_Variant.setTextColor(Color.BLACK);
//
//
//                    category_click = "";
//
////                    dataAdapterCategory = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results1);
////                    dataAdapterCategory.setDropDownViewResource(R.layout.spinner_item);
////                    spnCategory.setAdapter(adapter_state1);
////
////                   // int spinnerPosition = adapter_state1.getPosition(subcateg_name);
////                    spnProduct.setSelection(0);
//
////                    results.clear();
////                    results.add(getResources().getString(R.string.Select_Product));
////                    adapter_state2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results);
////                    adapter_state2.setDropDownViewResource(R.layout.spinner_item);
////                    spnProduct.setAdapter(adapter_state2);
//                }

            }
        });

        editTextQuantity.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!String.valueOf(s).equalsIgnoreCase("")) {
                    if (Integer.parseInt(String.valueOf(s)) <= 0) {
                        editTextQuantity.setText("");
                        txtPrice.setText("");
                        //txtPrice.setText(getResources().getString(R.string.Total_Price) + "");
                        price = String.valueOf("");
                    }
                } else {
                    txtPrice.setText("");
                    // txtPrice.setText(getResources().getString(R.string.Total_Price) + "");
                    //price = String.valueOf(" ");
                }

            }
        });


        Global_Data.GLOVEL_SubCategory_Button = "";
        SharedPreferences spf = ReturnOrder1.this.getSharedPreferences("SimpleLogic", 0);
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

        results.add(getResources().getString(R.string.Select_Product));
//		          for (Local_Data cn : contacts2) {
//		        	String str_product = ""+cn.getStateName();
//		        	//Global_Data.local_pwd = ""+cn.getPwd();
//		        	
//		        	results.add(str_product);
//		        	System.out.println("Local Values:-"+Global_Data.local_user);
//		        	//Toast.makeText(LoginActivity.this, "Login Invalid"+Global_Data.local_user,Toast.LENGTH_SHORT).show();
//		        	                             }
//		          
        adapter_state2 = new ArrayAdapter<String>(this, R.layout.spinner_item, results);
        adapter_state2.setDropDownViewResource(R.layout.spinner_item);
        spnProduct.setAdapter(adapter_state2);

        listProduct = new ArrayList<String>();
        dataAdapterProduct = new ArrayAdapter<String>(
                this, R.layout.spinner_item, listProduct);

        listProductSpec = new ArrayList<String>();
        dataAdapterProductSpec = new ArrayAdapter<String>(
                this, R.layout.spinner_item, listProductSpec);

        listScheme = new ArrayList<String>();
        dataAdapterScheme = new ArrayAdapter<String>(
                this, R.layout.spinner_item, listScheme);

        final List<String> listCategory = new ArrayList<String>();
        listCategory.add(getResources().getString(R.string.Select_Categories));

        adapter_state1 = new ArrayAdapter<String>(this, R.layout.spinner_item, results1);

        spnProduct.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
//					// TODO Auto-generated method stub
                Product_Variant.dismissDropDown();
                //editTextQuantity.setText("");
                if (!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(subcateg_name))) {
                    txtPrice.setText("");
                    // txtPrice.setText(getResources().getString(R.string.Total_Price) + "");
                }

                check_product = check_product + 1;
                if (check_product > 1) {

                    if (spnCategory.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Categories))) {

                        categ_name = "";
                        subcateg_name = "";
                        varient_name = "";
//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Select_Category), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Select_Category),"yes");

                    } else if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase(getResources().getString(R.string.Select_Product))) {
                        subcateg_name = "";
                        varient_name = "";
                       // results2.add("");
                        //results2.clear();

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReturnOrder1.this,
                                R.layout.autocomplete,
                                search_result);
                        Product_Variant.setThreshold(1);// will start working from
                        // first character
                        Product_Variant.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        Product_Variant.setText("");


                    } else {

                        search_result.clear();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReturnOrder1.this,
                                R.layout.autocomplete,
                                search_result);
                        Product_Variant.setThreshold(1);// will start working from
                        // first character
                        Product_Variant.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        //Product_Variant.setText("");

                        if (!subcateg_name.equalsIgnoreCase(parent.getItemAtPosition(pos).toString().trim())) {
                            subcateg_name = "";
                           // varient_name = "";

                            subcateg_name = parent.getItemAtPosition(pos).toString().trim();
                            Product_Variant.setText("");
                        }

                        Global_Data.Search_Product_name = parent.getItemAtPosition(pos).toString().trim();

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
                                //Global_Data.local_pwd = ""+cn.getPwd();

                                search_result.add(str_variant);
                                //System.out.println("Local Values:-"+Global_Data.local_user);
                                //Toast.makeText(LoginActivity.this, "Login Invalid"+Global_Data.local_user,Toast.LENGTH_SHORT).show();


                            }


//							 adapter_state3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results2);
//							 adapter_state3.setDropDownViewResource(R.layout.spinner_item);
//							 spnProductSpec.setAdapter(adapter_state3);

                            ArrayAdapter<String> adaptern = new ArrayAdapter<String>(ReturnOrder1.this, R.layout.autocomplete, search_result);
                            Product_Variant.setThreshold(1);// will start working from
                            // first character
                            Product_Variant.setAdapter(adaptern);// setting the adapter
                            // data into the
                            // AutoCompleteTextView


                        }


                    }
//
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        for (int i = 0; i < dataCategories.size(); i++) {
            listCategory.add(dataCategories.get(i).getDesc());
        }

        dataAdapterCategory = new ArrayAdapter<String>(this, R.layout.spinner_item, results1);
        dataAdapterCategory.setDropDownViewResource(R.layout.spinner_item);
        spnCategory.setAdapter(adapter_state1);

        if (!Global_Data.GLOVEL_CATEGORY_SELECTION.equalsIgnoreCase("")) {
            Log.d("Globel ", "in");
            spnCategory.setSelection(adapter_state1.getPosition(Global_Data.GLOVEL_CATEGORY_SELECTION));
            Global_Data.GLOVEL_CATEGORY_SELECTION = "";

        }

        Intent i = getIntent();
        String name = i.getStringExtra("retialer");
        if (i.hasExtra("data")) {
            //Log.e("data", "***********productList**********");
            Global_Data.productList = i.getParcelableArrayListExtra("productsList");
        }

        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText(Global_Data.order_retailer);
            //mTitleTextView.setText(Global_Data.order_retailer + " " + "(" + Global_Data.AmountOutstanding + "/" + Global_Data.AmountOverdue + ")");

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = ReturnOrder1.this.getSharedPreferences("SimpleLogic", 0);

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

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
//        	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
                todaysTarget.setText("Today's Target Acheived");
            }

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
       /* mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        buttonAddMOre = (Button) findViewById(R.id.buttonAddMOre);
        // buttonAddMOre.setBackgroundColor(Color.parseColor("#414042"));

        buttonAddMOre.setOnTouchListener(new OnTouchListener() {


            @Override
            public boolean onTouch(View b, MotionEvent event) {

                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //down event
                    b.setBackgroundColor(Color.parseColor("#910505"));
                    return true;
                }
                return false;
            }
        });

        buttonPreviewOrder = (Button) findViewById(R.id.buttonPreviewOrder);
        //     buttonPreviewOrder.setBackgroundColor(Color.parseColor("#414042"));

        buttonPreviewOrder.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    // b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (Global_Data.GLOVEL_ORDER_REJECT_FLAG.equalsIgnoreCase("TRUE")) {
                        Intent i = new Intent(ReturnOrder1.this, Status_Activity.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        startActivity(i);
                        finish();
                    } else {
                        addbtnfun();

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

                Product_Variant.dismissDropDown();
                Global_Data.GLOVEL_ITEM_MRP = "";
                category = parent.getItemAtPosition(pos).toString();
                Log.d("Globel categary", Global_Data.GLOVEL_CATEGORY_SELECTION);

                check = check + 1;
                if (check > 1) {

                    // editTextQuantity.setText("");

                    if (!(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(subcateg_name))) {
                        txtPrice.setText("");
                        // txtPrice.setText(getResources().getString(R.string.Total_Price) + "");
                    }


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
                       // results2.clear();

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReturnOrder1.this,
                                R.layout.autocomplete,
                                result_product);
                        Product_Variant.setThreshold(1);// will start working from
                        // first character
                        Product_Variant.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        Product_Variant.setText("");
//
//                        Toast toast = Toast.makeText(ReturnOrder1.this, getResources().getString(R.string.Please_Select_Category), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(ReturnOrder1.this, getResources().getString(R.string.Please_Select_Category),"yes");

                    } else {


                        search_result.clear();
//                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ReturnOrder1.this,
//                                android.R.layout.simple_spinner_dropdown_item,
//                                search_result);
//                        Product_Variant.setThreshold(1);// will start working from
//                        // first character
//                        Product_Variant.setAdapter(adapter2);// setting the adapter
//                        // data into the
//                        // AutoCompleteTextView
//                        Product_Variant.setTextColor(Color.BLACK);



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

                        search_result.clear();
                        List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_BYcateNAME(parent.getItemAtPosition(pos).toString().trim());
                        // results2.add("Select Variant");
                        for (Local_Data cn : contacts3) {
                            str_variant = "" + cn.getStateName();
                            //Global_Data.local_pwd = ""+cn.getPwd();

                            search_result.add(str_variant);
                            //System.out.println("Local Values:-"+Global_Data.local_user);
                            //Toast.makeText(LoginActivity.this, "Login Invalid"+Global_Data.local_user,Toast.LENGTH_SHORT).show();


                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReturnOrder1.this,
                                R.layout.autocomplete,
                                search_result);
                        Product_Variant.setThreshold(1);// will start working from
                        // first character
                        Product_Variant.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView

                        category_click = "yes";

                        adapter_state2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results);
                        adapter_state2.setDropDownViewResource(R.layout.spinner_item);
                        spnProduct.setAdapter(adapter_state2);
                        // spnProduct.setOnItemSelectedListener(NewOrderActivity.this);

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(subcateg_name)) {
                            int spinnerPosition = adapter_state2.getPosition(subcateg_name);
                            spnProduct.setSelection(spinnerPosition);
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(varient_name)) {
                            Product_Variant.setText(varient_name.trim());
                            varient_name = "";
                        }
                        else
                        {
                            Product_Variant.setText("");
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

                                if (price_str.length() > 0) {
                                    txtPrice.setText(price_str + " : " + final_mrp);
                                } else {
                                    // txtPrice.setText(getResources().getString(R.string.Total_Price) + final_mrp);
                                    txtPrice.setText("" + final_mrp);

                                }
                                price = String.valueOf(final_mrp);


                            } else {
                                if (!editTextMRP.getText().toString().equalsIgnoreCase("") && !editTextMRP.getText().toString().equalsIgnoreCase(null) && !editTextMRP.getText().toString().equalsIgnoreCase("null") && !editTextMRP.getText().toString().equalsIgnoreCase("0.0")) {

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
                //editTextQuantity.setText(" ");
                //txtPrice.setText("Total Price : "+"");
                //txtPrice.setText("Total Price : "+"");
                if (parent.getItemAtPosition(pos).toString()
                        .equalsIgnoreCase(getResources().getString(R.string.Select_Scheme))) {

                    if (!editTextQuantity.getText().toString().equalsIgnoreCase("") && !editTextQuantity.getText().toString().equalsIgnoreCase(null) && !editTextQuantity.getText().toString().equalsIgnoreCase("null") && !editTextQuantity.getText().toString().equalsIgnoreCase("0.0") && !editTextMRP.getText().toString().equalsIgnoreCase("0.0")) {
                        long final_mrp = (Long.valueOf(editTextMRP.getText().toString())) * (Long.valueOf(editTextQuantity.getText().toString().trim()));

                        if (price_str.length() > 0) {
                            txtPrice.setText(price_str + " : " + final_mrp);
                        } else {
                            txtPrice.setText("" + final_mrp);
                            //  txtPrice.setText(getResources().getString(R.string.Total_Price) + final_mrp);
                        }

                    } else {
                        if (!editTextMRP.getText().toString().equalsIgnoreCase("") && !editTextMRP.getText().toString().equalsIgnoreCase(null) && !editTextMRP.getText().toString().equalsIgnoreCase("null") && !editTextMRP.getText().toString().equalsIgnoreCase("0.0")) {

                        }

                    }

                } else {

                    productprice = Float.valueOf(editTextMRP.getText().toString());

                    totalprice = productprice * quantity;

                    Global_Data.order_amount = totalprice;
                    //Toast.makeText(NewOrderActivity.this, ""+Global_Data.order_amount ,Toast.LENGTH_SHORT).show();

                    int aaa = Integer.parseInt(Global_Data.amnt);

                    totalprc_scheme = ((quantity / productprice) * aaa);

                    totalprice1 = (quantity + ((quantity / productprice) * aaa));

//					String vvv=String.valueOf(totalprice1);
//
//					int delivery= (quantity+Integer.valueOf(vvv));

                    if (price_str.length() > 0) {
                        txtPrice.setText(price_str + " : " + String.format("%.2f", totalprc_scheme));
                    } else {
                        txtPrice.setText(getResources().getString(R.string.Total_Price) + String.format("%.2f", totalprc_scheme));
                        txtPrice.setText("" + String.format("%.2f", totalprc_scheme));
                    }


                    txtDeleiveryQuantity.setText(getResources().getString(R.string.Delivered_Quantity) + " : " + String.format("%.2f", totalprice1));


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });
    }

    private void deletedialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(ReturnOrder1.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Warning));
        alertDialog.setMessage(getResources().getString(R.string.image_dialog_warning_message));
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                File file = new File(pictureImagePath_new);
                boolean delete = file.delete();

                File file1 = new File(pictureImagePath);
                boolean delete1 = file1.delete();

                // img_show.setVisibility(View.GONE);
                // crossimg.setVisibility(View.GONE);
                //   img_layout.setVisibility(View.GONE);

                Global_Data.Custom_Toast(ReturnOrder1.this, getResources().getString(R.string.image_dialog_delete_success), "Yes");
//				Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.image_dialog_delete_success),
//						Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.CENTER, 0, 0);
//				toast.show();

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

    private void requestStoragePermission2() {

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

                                if (Global_Data.image_counter <= 4) {

                                    //final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Choose_from_Gallery), getResources().getString(R.string.Cancel)};
                                    final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Cancel)};

                                    AlertDialog.Builder builder = new AlertDialog.Builder(ReturnOrder1.this);

                                    builder.setTitle(getResources().getString(R.string.Add_Photo));

                                    builder.setItems(options, new DialogInterface.OnClickListener() {

                                        @Override

                                        public void onClick(DialogInterface dialog, int item) {

                                            if (options[item].equals(getResources().getString(R.string.Take_Photo))) {
                                                image_check = "photo";

                                                File photoFile = null;
                                                try {
                                                    photoFile = createImageFile();
                                                } catch (IOException ex) {
                                                    // Error occurred while creating the File
                                                    Log.i("Image TAG", "IOException");
                                                    pictureImagePath = "";
                                                }
                                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                                // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                                Uri photoURI = FileProvider.getUriForFile(ReturnOrder1.this,
                                                        BuildConfig.APPLICATION_ID + ".provider",
                                                        photoFile);
                                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                startActivityForResult(cameraIntent, 1);

                                            }
//										else if (options[item].equals(getResources().getString(R.string.Choose_from_Gallery)))
//
//										{
//
//											image_check = "gallery";
//											Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//											startActivityForResult(intent, 2);
//
//
//										}
                                            else if (options[item].equals(getResources().getString(R.string.Cancel))) {

                                                dialog.dismiss();

                                            }

                                        }

                                    });

                                    builder.show();


                                } else {
                                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.pick_validation), "");
                                    //   Toast.makeText(Cash_Collect.this, "You can not take more than 5 picture.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.no_camera), "");
                                //Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_camera), Toast.LENGTH_LONG).show();
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
                        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReturnOrder1.this);
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "Return";
        File storageDir = null;

        storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Return Order");


        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

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


    private void addbtnfun() {
        if (spnCategory.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Categories))) {
            Global_Data.Custom_Toast(ReturnOrder1.this, getResources().getString(R.string.Please_Select_Category), "Yes");
        } else if (spnProduct.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Product))) {
            Global_Data.Custom_Toast(ReturnOrder1.this, getResources().getString(R.string.Please_Select_Product), "Yes");
        } else if (Product_Variant.getText().toString().trim().equalsIgnoreCase("")) {
            Global_Data.Custom_Toast(ReturnOrder1.this, getResources().getString(R.string.Please_Select_Variant), "Yes");
        } else if (editTextQuantity.getText().toString().length() == 0) {
            Global_Data.Custom_Toast(ReturnOrder1.this, getResources().getString(R.string.Please_enter_Quantity), "Yes");
        } else if (editTextbatchno.getText().toString().length() == 0) {
            Global_Data.Custom_Toast(ReturnOrder1.this, getResources().getString(R.string.PLEASEEBAC_NO), "Yes");
        } else if (refund_amount.getText().toString().length() == 0) {
            Global_Data.Custom_Toast(ReturnOrder1.this, getResources().getString(R.string.PL_REFUNDAMOUNT), "Yes");
        } else if (return_order_remarks.getText().toString().length() == 0) {
            Global_Data.Custom_Toast(ReturnOrder1.this, getResources().getString(R.string.PL_Remarks), "Yes");
        } else {

            SharedPreferences spf = ReturnOrder1.this.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL", null);


            Long randomPIN = System.currentTimeMillis();
            String PINString = String.valueOf(randomPIN);
            Global_Data.variant_rr = editTextRP.getText().toString();
            Global_Data.variant_mrp = editTextMRP.getText().toString();
            Global_Data.order_qty = editTextQuantity.getText().toString();
            String strAmount = String.valueOf(Global_Data.order_amount);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDatef = dff.format(c.getTime());


            Global_Data.GLObalOrder_id_return = "Ord" + PINString;
            Global_Data.GLOvel_GORDER_ID_RETURN = "Ord" + PINString;


            try {
                AppLocationManager appLocationManager = new AppLocationManager(ReturnOrder1.this);
                Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                PlayService_Location PlayServiceManager = new PlayService_Location(ReturnOrder1.this);

                if (PlayServiceManager.checkPlayServices(ReturnOrder1.this)) {
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

            SimpleDateFormat fdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            final String formattedDaten = fdf.format(c1.getTime());

            AlertDialog alertDialog = new AlertDialog.Builder(ReturnOrder1.this).create(); //Read Update
            alertDialog.setTitle("Confirmation");
            alertDialog.setMessage(" Are you sure you want to continue?");
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cd = new ConnectionDetector(getApplicationContext());
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {

                        String urls1 = "";
                        String urls2 = "";
                        String urls3 = "";
                        String urls4 = "";
                        String urls5 = "";
                        for (int i = 0; i < Global_Data.Number.size(); i++) {
                            if (i == 0) {
                                urls1 = Global_Data.Number.get(0);
                            }
                            if (i == 1) {
                                urls2 = Global_Data.Number.get(1);
                            }
                            if (i == 2) {
                                urls3 = Global_Data.Number.get(2);
                            }

                            if (i == 3) {
                                urls4 = Global_Data.Number.get(3);
                            }
                            if (i == 4) {
                                urls5 = Global_Data.Number.get(4);
                            }
                        }

                        try {
                            String product_id = product_map.get(Product_Variant.getText().toString());
                            loginDataBaseAdapter.insertReturnOrders("", Global_Data.GLOvel_GORDER_ID_RETURN, Global_Data.GLOvel_CUSTOMER_ID, Global_Data.order_retailer, user_email, Global_Data.order_city, Global_Data.order_beat, "", "", "", "", "", "", formattedDaten, "", Global_Data.order_retailer, Global_Data.order_state, Global_Data.order_city, Global_Data.sales_btnstring, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Glovel_BEAT_ID, urls1, urls2, urls3, urls4, urls5);

                            loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "RETURN ORDER", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                            loginDataBaseAdapter.insertReturnOrderProducts(" ", " ", Global_Data.GLOvel_GORDER_ID_RETURN, "", spnCategory.getSelectedItem().toString(), product_id, Global_Data.order_variant, " ", spnScheme.getSelectedItem().toString(), " ", "", editTextQuantity.getText().toString(), Global_Data.variant_rr, Global_Data.variant_mrp, price, "", "", Global_Data.order_retailer, " ", Global_Data.item_code_return, " ", "", editTextbatchno.getText().toString(), refund_amount.getText().toString(), return_order_remarks.getText().toString());//Reading all
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        getServices.SYNReturnOrder(ReturnOrder1.this, Global_Data.GLOvel_GORDER_ID_RETURN);

                    } else {

                        String urls1 = "";
                        String urls2 = "";
                        String urls3 = "";
                        String urls4 = "";
                        String urls5 = "";
                        for (int i = 0; i < Global_Data.Number.size(); i++) {
                            if (i == 0) {
                                urls1 = Global_Data.Number.get(0);
                            }
                            if (i == 1) {
                                urls2 = Global_Data.Number.get(1);
                            }
                            if (i == 2) {
                                urls3 = Global_Data.Number.get(2);
                            }

                            if (i == 3) {
                                urls4 = Global_Data.Number.get(3);
                            }
                            if (i == 4) {
                                urls5 = Global_Data.Number.get(4);
                            }
                        }

                        try {
                            String product_id = product_map.get(Product_Variant.getText().toString());
                            loginDataBaseAdapter.insertReturnOrders("", Global_Data.GLOvel_GORDER_ID_RETURN, Global_Data.GLOvel_CUSTOMER_ID, Global_Data.order_retailer, user_email, Global_Data.order_city, Global_Data.order_beat, "", "", "", "", "", "", formattedDaten, "", Global_Data.order_retailer, Global_Data.order_state, Global_Data.order_city, Global_Data.sales_btnstring, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Glovel_BEAT_ID, urls1, urls2, urls3, urls4, urls5);

                            loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "RETURN ORDER", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                            loginDataBaseAdapter.insertReturnOrderProducts(" ", " ", Global_Data.GLOvel_GORDER_ID_RETURN, "", spnCategory.getSelectedItem().toString(), product_id, Global_Data.order_variant, " ", spnScheme.getSelectedItem().toString(), " ", "", editTextQuantity.getText().toString(), Global_Data.variant_rr, Global_Data.variant_mrp, price, "", "", Global_Data.order_retailer, " ", Global_Data.item_code_return, " ", "", editTextbatchno.getText().toString(), refund_amount.getText().toString(), return_order_remarks.getText().toString());//Reading all
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        Global_Data.Custom_Toast(ReturnOrder1.this, getResources().getString(R.string.PNO_Sucess), "Yes");

                        Intent i = new Intent(ReturnOrder1.this, Neworderoptions.class);
                        startActivity(i);
                        finish();

                        // Global_Data.Custom_Toast(ReturnOrder1.this,getResources().getString(R.string.internet_connection_error),"Yes");


                    }


                }
            });

            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            alertDialog.setCancelable(false);
            alertDialog.show();


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
                SharedPreferences sp = ReturnOrder1.this.getSharedPreferences("SimpleLogic", 0);
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
        Global_Data.GLOVEL_CATEGORY_SELECTION="";
        Intent i = new Intent(ReturnOrder1.this, Neworderoptions.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stubs
        super.onResume();
//        buttonAddMOre.setBackgroundColor(Color.parseColor("#414042"));
//        buttonPreviewOrder.setBackgroundColor(Color.parseColor("#414042"));
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
//		try {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            recyclerView.setVisibility(View.VISIBLE);
            Global_Data.Number.add(pictureImagePath_new);

            RecyclerViewHorizontalAdapter = new ReturnOrderImageAdapter(ReturnOrder1.this, Global_Data.Number);

            HorizontalLayout = new LinearLayoutManager(ReturnOrder1.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(HorizontalLayout);

            recyclerView.setAdapter(RecyclerViewHorizontalAdapter);

            ++Global_Data.image_counter;

//            File imgFils = new File(pictureImagePath_new);
//            if (imgFils.exists()) {
//
//                img_show = findViewById(R.id.img_show);
//                 crossimg.setVisibility(View.VISIBLE);
//                  img_show.setVisibility(View.VISIBLE);
//                 img_layout.setVisibility(View.VISIBLE);
//                img_show.setRotation((float) 90.0);
//
//                Glide.with(ReturnOrder1.this).load(pictureImagePath_new).into(img_show);
//
//                img_show.setImageURI(Uri.fromFile(imgFils));
//            }


        }
//		else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
//			beginCrop(data.getData());
//		} else if (requestCode == Crop.REQUEST_CROP) {
//
//			try {
//				if (data == null) {
//					imageview.setVisibility(View.VISIBLE);
//					imageview.setImageDrawable(Glovel_Drawable);
//					// imageview.setTag(Glovel_bitmap);
//					//imageviewr.setImageBitmap(Glovel_bitmap);
//				} else {
//					handleCrop(resultCode, data, data.getData());
//				}
//			} catch (Exception ex) {
//				ex.printStackTrace();
//				imageview.setVisibility(View.VISIBLE);
//				imageview.setImageDrawable(Glovel_Drawable);
//				// imageview.setTag(Glovel_bitmap);
//				// imageviewr.setImageBitmap(Glovel_bitmap);
//			}
//		}


        //fileUri = getOutputMediaFileUri(selectedPath);
        //textView.setText(selectedPath);
        if (requestCode == 2 && resultCode == RESULT_OK) {

            try {
                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                pictureImagePath = "file:" + c.getString(columnIndex);


                c.close();

                //new Expenses.LongOperation().execute();
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }


//		}catch (Exception ex){ex.printStackTrace();
//			Toast.makeText(Customer_Feed.this, "Please Select Media File.", Toast.LENGTH_LONG).show();
//		}
    }


}
