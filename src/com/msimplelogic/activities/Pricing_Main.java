package com.msimplelogic.activities;

/**
 * Created by vinod on 04-10-2016.
 */

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cpm.simplelogic.helper.ContactInfo;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class Pricing_Main extends BaseActivity {
    String categ_name, subcateg_name;
    int check = 0;
    String str_variant;
    String product_code = "";
    int check_product = 0;
    int check_ProductSpec = 0;
    AutoCompleteTextView Product_Variant;
    Spinner spnCategory, spnProductSpec, spnScheme, spnProduct;

    ArrayAdapter<String> dataAdapterCategory, dataAdapterProductSpec, dataAdapterProduct;
    //ArrayList productList = new ArrayList();
    List<String> listProduct, listProductSpec;
    List<String> listScheme;
    SharedPreferences sp;

    private ArrayList<String> results = new ArrayList<String>();
    private ArrayList<String> results1 = new ArrayList<String>();
    private ArrayList<String> results2 = new ArrayList<String>();
    private ArrayList<String> result_product = new ArrayList<String>();

    ArrayAdapter<String> adapter_state1;
    ArrayAdapter<String> adapter_state2;
    ArrayAdapter<String> adapter_state3;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    RecyclerView recList;
    RelativeLayout rlout_price, rlout_stock;
    Toolbar toolbar;
    ImageView hedder_theame;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String selected_Product = "";
    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my);

        setContentView(R.layout.activity_pricestock);

        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnProduct = (Spinner) findViewById(R.id.spnProduct);
        Product_Variant = (AutoCompleteTextView) findViewById(R.id.Product_Variant);
        rlout_price = (RelativeLayout) findViewById(R.id.rlout_price);
        rlout_stock = (RelativeLayout) findViewById(R.id.rlout_stock);

        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        Global_Data.Stock_warehouse_flag = "";
        Global_Data.Stock_product_flag = "";
        Global_Data.Stock_product_flag_value_check = "";
        Global_Data.Stock_warehouse_flag_value_check = "";
        hedder_theame=findViewById(R.id.hedder_theame);
        // create a instance of SQLite Database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

//        rlout_stock.setBackgroundResource(R.drawable.single_wtab);
//        rlout_price.setBackgroundResource(R.drawable.single_btab);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);

        }


        rlout_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        Stock_Main.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

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
//            mTitleTextView.setText(getResources().getString(R.string.Pricing));
//
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = Pricing_Main.this.getSharedPreferences("SimpleLogic", 0);
//
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
//
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
//            mActionBar.setCustomView(mCustomView);
//            mActionBar.setDisplayShowCustomEnabled(true);
//            mActionBar.setHomeButtonEnabled(true);
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        recList.setVisibility(View.INVISIBLE);

        results2.clear();
        List<Local_Data> contacts2 = dbvoc.getAllVariant();
        for (Local_Data cn : contacts2) {
            results2.add(cn.getStateName());
            result_product.add(cn.getStateName());
            //loginDataBaseAdapter.insertvirtual_item(cn.getCode(),cn.getStateName());
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

                        View view = Pricing_Main.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        //autoCompleteTextView1.setText("");
                        Product_Variant.showDropDown();
                        return true;
                    } else if (event.getRawX() >= (Product_Variant.getLeft() - Product_Variant.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {

                        View view = Pricing_Main.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        //autoCompleteTextView1.setText("");
                        //Product_Variant.showDropDown();
                        promptSpeechInput();
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

                Global_Data.hideSoftKeyboard(Pricing_Main.this);
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

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Pricing_Main.this,
                                R.layout.autocomplete,
                                result_product);
                        Product_Variant.setThreshold(1);// will start working from
                        // first character
                        Product_Variant.setAdapter(adapter);// setting the adapter
                        // data into the
                        // AutoCompleteTextView
                        Product_Variant.setText("");


//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Select_Category), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Select_Category),"yes");

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
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Pricing_Main.this,
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

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Pricing_Main.this, R.layout.autocomplete, results2);
                            Product_Variant.setThreshold(1);// will start working from
                            // first character
                            Product_Variant.setAdapter(adapter);// setting the adapter
                            // data into the
                            // AutoCompleteTextView

                        }

                    //  List<Local_Data> contacts3 = dbvoc.getProductByCat(Product_Variant.getText().toString().trim());

                        List<Local_Data> contacts3 = dbvoc.getSearchProduct_with_name(Product_Variant.getText().toString());

                        if (contacts3.size() <= 0 && !Product_Variant.getText().toString().trim().equalsIgnoreCase("")) {
                            // Toast.makeText(Pricing_Main.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

//                            Toast toast = Toast.makeText(getApplicationContext(),  "Sorry No Record Found.", Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Product_Variant.setText("");


                            recList.setVisibility(View.INVISIBLE);
                        } else {
                            if (Product_Variant.length() > 0) {
                                recList.setVisibility(View.VISIBLE);
                                List<ContactInfo> result = new ArrayList<ContactInfo>();
                                for (Local_Data cn : contacts3) {

                                    ContactInfo ci = new ContactInfo();
                                    ci.name = cn.getProduct_nm();
                                    ci.rp = cn.getStateName();
                                    ci.mrp = cn.getMRP();

                                    result.add(ci);
                                }

                                ContactAdapter ca = new ContactAdapter(Pricing_Main.this, result);
                                recList.setAdapter(ca);
                                ca.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }


    private List<ContactInfo> createList(int size) {

        List<ContactInfo> result = new ArrayList<ContactInfo>();
        for (int i = 1; i <= size; i++) {
            ContactInfo ci = new ContactInfo();
            ci.name = "101" + " " + "abc";
            ci.rp = ContactInfo.SURNAME_PREFIX + i;
            ci.mrp = ContactInfo.EMAIL_PREFIX + i + "@test.com";

            result.add(ci);

        }

        return result;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//
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
//		return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add:
                String targetNew = "";
                SharedPreferences sp = Pricing_Main.this.getSharedPreferences("SimpleLogic", 0);
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

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
//            Toast.makeText(getApplicationContext(),
//                    getString(R.string.speech_not_supported),
//                    Toast.LENGTH_SHORT).show();
            Global_Data.Custom_Toast(getApplicationContext(),
                    getString(R.string.speech_not_supported),"yes");
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    // String s = "";
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    Intent i = new Intent(Pricing_Main.this, Chat_PriceDemo.class);
                    Global_Data.Voice_value = result.get(0).trim();
                    startActivity(i);


//                    for(int i=0; i<result.size(); i++)
//                    {
//                        if(i != result.size()-1)
//                        {
//                            s += results.get(i) + ",";
//                        }
//                        else
//                        {
//                            s += results.get(i);
//                        }
//                    }

                   // Toast.makeText(this, result.get(0), Toast.LENGTH_SHORT).show();

              //      List<Local_Data> cont = dbvoc.getProductByVoiceString(result.get(0).trim());

//                    //results2.add("Select Variant");
//                    if (cont.size() > 0) {
//
//                        Product_Variant.setText(result.get(0).trim());
//                        VoiceResultData(cont);
//                    } else {
//                       // Global_Data.Custom_Toast(Pricing_Main.this, getResources().getString(R.string.product_not_found_message), "Yes");
//
//                        showDialog(Pricing_Main.this, getResources().getString(R.string.Do_You_Mean_This), result,
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                        if (which == -1) {
//                                            Log.d("VV", "On button click ok");
//
//                                            List<Local_Data> cont = dbvoc.getProductByVoiceString(selected_Product);
//
//                                            //results2.add("Select Variant");
//                                            if (cont.size() > 0) {
//                                                Product_Variant.setText(selected_Product);
//                                                VoiceResultDataDialog(cont);
//                                            } else {
//                                                Global_Data.Custom_Toast(Pricing_Main.this, getResources().getString(R.string.product_not_found_message), "Yes");
//                                               // dialog.dismiss();
//
////
//                                            }
//
//                                        } else if (which == -2) {
//                                            Log.d("VV", "On button click cancel");
//                                            dialog.dismiss();
//                                        } else if (which == -3) {
//                                            Log.d("VV", "On button click try");
//                                            promptSpeechInput();
//                                        }
//
//                                    }
//                                });
//                    }
//
//
//                    adapter_state1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spnCategory.setAdapter(adapter_state1);
//                    //spnCategory.setOnItemSelectedListener(NewOrderActivity.this);
//
//                    adapter_state2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    spnProduct.setAdapter(adapter_state2);
//                    //spnProduct.setOnItemSelectedListener(NewOrderActivity.this);
//
//                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(categ_name)) {
//                        int spinnerPosition = adapter_state1.getPosition(categ_name);
//                        spnCategory.setSelection(spinnerPosition);
//                    }
//
//                    // txtSpeechInput.setText(result.get(0));
                }
                break;
            }

        }
    }


    public void showDialog(Context context, String title, ArrayList<String> voiceInputList,
                           DialogInterface.OnClickListener listener) {

        final CharSequence[] items = voiceInputList.toArray(new String[voiceInputList.size()]);

        if (listener == null)
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    paramDialogInterface.dismiss();
                }
            };
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setTitle(title);
       // builder.setMessage(getResources().getString(R.string.Do_You_Mean_This));

        int checkedItem = 1;
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selected_Product = (String) items[which];

            }
        });

//        builder.setSingleChoiceItems(items, -1,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int item) {
//                        selected_Product = voiceInputList.get(item);
//                    }
//                });
        builder.setPositiveButton(getResources().getString(R.string.OK_Button_label), listener);
        builder.setNegativeButton(getResources().getString(R.string.Cancel), listener);
        builder.setNeutralButton(getResources().getString(R.string.Try_Again), listener);

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
       // builder.show();
    }

    public void VoiceResultData(List<Local_Data> cont)
    {
        List<ContactInfo> voiceResults = new ArrayList<ContactInfo>();


//        if(cont.size() > 1)
//        {
//            ArrayList<String> result = new ArrayList<>();
//
//            for (Local_Data cn1 : cont) {
//
//                result.add(cn1.getProdname());
//            }
//            showDialog(Pricing_Main.this, getResources().getString(R.string.Do_You_Mean_This), result,
//                    new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            if (which == -1) {
//                                Log.d("VV", "On button click ok");
//
//                                List<Local_Data> cont = dbvoc.getProductByVoiceString(selected_Product);
//
//                                //results2.add("Select Variant");
//                                if (cont.size() > 0) {
//                                    Product_Variant.setText(selected_Product);
//                                    VoiceResultData(cont);
//                                } else {
//                                    Global_Data.Custom_Toast(Pricing_Main.this, getResources().getString(R.string.product_not_found_message), "Yes");
//                                    //dialog.dismiss();
//
////
//                                }
//
//                            } else if (which == -2) {
//                                Log.d("VV", "On button click cancel");
//                                dialog.dismiss();
//                            } else if (which == -3) {
//                                Log.d("VV", "On button click try");
//                                promptSpeechInput();
//                            }
//
//                        }
//                    });
//        }
//        else
//        {
            for (Local_Data cn1 : cont) {

                Global_Data.amnt = "" + cn1.get_Description();
                Global_Data.amnt1 = "" + cn1.get_Claims();
                product_code = cn1.getCode();

                categ_name = cn1.getCategory();
                subcateg_name = cn1.getSubcateg();

//                if(cont.size() == 1)
//                {
//                    ContactInfo ci = new ContactInfo();
//                    ci.name = cn1.getProdname() ;
//                    ci.rp = cn1.getRP();
//                    ci.mrp = cn1.getMRP();
//
//                    voiceResults.add(ci);
//                }


           // }

            adapter_state1.setDropDownViewResource(	R.layout.spinner_item);
            spnCategory.setAdapter(adapter_state1);
            //spnCategory.setOnItemSelectedListener(NewOrderActivity.this);

            adapter_state2.setDropDownViewResource(	R.layout.spinner_item);
            spnProduct.setAdapter(adapter_state2);
            //spnProduct.setOnItemSelectedListener(NewOrderActivity.this);

            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(categ_name)) {
                int spinnerPosition = adapter_state1.getPosition(categ_name);
                spnCategory.setSelection(spinnerPosition);
            }

//            if(cont.size() == 1)
//            {
//
//                recList.setVisibility(View.VISIBLE);
//                ContactAdapter ca = new ContactAdapter(Pricing_Main.this, voiceResults);
//                recList.setAdapter(ca);
//                ca.notifyDataSetChanged();
//            }
        }

    }



    public void VoiceResultDataDialog(List<Local_Data> cont)
    {
        List<ContactInfo> voiceResults = new ArrayList<ContactInfo>();

        for (Local_Data cn1 : cont) {

                Global_Data.amnt = "" + cn1.get_Description();
                Global_Data.amnt1 = "" + cn1.get_Claims();
                product_code = cn1.getCode();

                categ_name = cn1.getCategory();
                subcateg_name = cn1.getSubcateg();


//                    ContactInfo ci = new ContactInfo();
//                    ci.name = cn1.getVariant() ;
//                    ci.rp = cn1.getRP();
//                    ci.mrp = cn1.getMRP();
//
//                    voiceResults.add(ci);



            }


        adapter_state1.setDropDownViewResource(	R.layout.spinner_item);
        spnCategory.setAdapter(adapter_state1);
        //spnCategory.setOnItemSelectedListener(NewOrderActivity.this);

        adapter_state2.setDropDownViewResource(	R.layout.spinner_item);
        spnProduct.setAdapter(adapter_state2);
        //spnProduct.setOnItemSelectedListener(NewOrderActivity.this);

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(categ_name)) {
            int spinnerPosition = adapter_state1.getPosition(categ_name);
            spnCategory.setSelection(spinnerPosition);
        }

//                recList.setVisibility(View.VISIBLE);
//                ContactAdapter ca = new ContactAdapter(Pricing_Main.this, voiceResults);
//                recList.setAdapter(ca);
//                ca.notifyDataSetChanged();



    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
        Intent i = new Intent(Pricing_Main.this, MainActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

//    @Override
//    public void onBackPressed() {
//
//        Intent i=new Intent(Pricing_Main.this, MainActivity.class);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
//        finish();
//
//        }

}
