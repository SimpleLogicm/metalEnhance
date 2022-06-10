package com.msimplelogic.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.msimplelogic.activities.R;
import com.msimplelogic.webservice.ConnectionDetector;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;
import java.util.List;

public class Stock_Product_Options extends Activity  {
    private String Re_Text = "";
    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    int check=0;
    int check_product=0;
    int check_ProductSpec=0;

    Button  adr_button;
    Spinner spnCategory,spnProduct,Product_Variant;
    TextView selVersion;
    HttpGet httppst;
    String s[];
    int state_flag = 0;
    ProgressDialog dialog;
    ArrayAdapter<String> adapter_state1;

    ArrayAdapter<String> adapter_state2;
    ArrayAdapter<String> adapter_beat;
    ArrayAdapter<String> adapter_state3;
    LinearLayout order_view, custserve_view, schedule_view;
    RelativeLayout rlout_order, rlout_custserve, rlout_schedule;
    HttpClient httpclint;
    List<NameValuePair> nameValuePars;
    HttpPost httppost;
    HttpResponse response;
    LoginDataBaseAdapter adapter_ob;
    DataBaseHelper helper_ob;
    SQLiteDatabase db_ob;
    Cursor cursor;
    String[] from;
    ListView lv;
    SimpleCursorAdapter cursorAdapter;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    String C_ID = "";
    String S_ID = "";
    String B_ID = "";
    String CUS_ID = "";
    String CUS_NAME = "";
    SharedPreferences sp;
    ArrayList<String> list_cities = new ArrayList<String>();
    String[] customer_array;
    Button customer_submit;
    private ArrayList<String> results = new ArrayList<String>();
    private ArrayList<String> results1 = new ArrayList<String>();
    private ArrayList<String> results2 = new ArrayList<String>();


    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;

    private Button btnGetLocation = null;
    private EditText editLocation = null;
    private ProgressBar pb = null;


    private static final String TAG = "Debug";
    private Boolean flag = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_product_options);
        // lv=(ListView)findViewById(R.id.listView1);
        // customer_submit = (Button) findViewById(R.id.customer_submit);

        locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        adr_button = (Button) findViewById(R.id.but_neworder);
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnProduct = (Spinner) findViewById(R.id.spnProduct);
        Product_Variant = (Spinner) findViewById(R.id.Product_Variant);

        spnCategory.setPopupBackgroundResource(R.drawable.spinner);
        spnProduct.setPopupBackgroundResource(R.drawable.spinner);
        Product_Variant.setPopupBackgroundResource(R.drawable.spinner);

        rlout_order = (RelativeLayout) findViewById(R.id.rlout_order);
        rlout_custserve = (RelativeLayout) findViewById(R.id.rlout_customer);
        rlout_schedule = (RelativeLayout) findViewById(R.id.rlout_schedule);


        Global_Data.GLObalOrder_id_return = "";

        cd  = new ConnectionDetector(getApplicationContext());

        Global_Data.GLOvel_GORDER_ID = "";
        Global_Data.GLOVEL_LONG_DESC = "";
        Global_Data.GLOVEL_CATEGORY_SELECTION = "";
        Global_Data.GLOVEL_ITEM_MRP = "";


        // current_locationcheck();

        adr_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spnCategory.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.All_Category)))
                {
                    Intent intent = new Intent(getApplicationContext(),
                            Stock_Main.class);
                    startActivity(intent);
                    Global_Data.Stock_product_flag = "All Category";
                    Global_Data.Stock_product_flag_value_check = "Category";

                } else if (spnProduct.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.All_Sub_Category)))
                {
                    Global_Data.Stock_product_flag = spnCategory.getSelectedItem().toString()+"-"+"All Sub Category";
                    Global_Data.Stock_product_flag_value_check = "All Sub Category";
                    Intent intent = new Intent(getApplicationContext(),
                            Stock_Main.class);
                    startActivity(intent);


                } else if (!(spnCategory.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Categories))) && !(spnProduct.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Sub_Category))) && Product_Variant.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Variant_s)))
                {
                    Global_Data.Stock_product_flag = spnCategory.getSelectedItem().toString()+"-"+spnProduct.getSelectedItem().toString();
                    Global_Data.Stock_product_flag_value_check = "Category and All Sub Category";
                    Intent intent = new Intent(getApplicationContext(),
                            Stock_Main.class);
                    startActivity(intent);


                }
                else if (spnCategory.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Categories))) {
//                    Toast toast = Toast.makeText(Stock_Product_Options.this, getResources().getString(R.string.Please_Select_Category), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(Stock_Product_Options.this, getResources().getString(R.string.Please_Select_Category), "yes");
                } else if (spnProduct.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Sub_Category))) {
//                    Toast toast = Toast.makeText(Stock_Product_Options.this, getResources().getString(R.string.Please_Select_Sub_Category), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(Stock_Product_Options.this, getResources().getString(R.string.Please_Select_Sub_Category),"yes");
                } else if (Product_Variant.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Variant_s))) {
//                    Toast toast = Toast.makeText(Stock_Product_Options.this, getResources().getString(R.string.Please_Select_Variant_s), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(Stock_Product_Options.this, getResources().getString(R.string.Please_Select_Variant_s),"yes");
                }
                else
                {
                    //Global_Data.Stock_product_flag = Product_Variant.getSelectedItem().toString();

                    List<Local_Data> contacts1 = dbvoc.getItemCode(spnCategory.getSelectedItem().toString().trim(),spnProduct.getSelectedItem().toString().trim(),Product_Variant.getSelectedItem().toString().trim());

                    if(contacts1.size() <= 0)
                    {
//                        Toast toast = Toast.makeText(Stock_Product_Options.this,
//                                getResources().getString(R.string.Variant_Not_Found), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(Stock_Product_Options.this,
                                getResources().getString(R.string.Variant_Not_Found),"yes");
                    }
                    else {


                        for (Local_Data cn1 : contacts1) {

                            Global_Data.Stock_product_flag = cn1.getProdname();

                        }
                    }
                    Global_Data.Stock_product_flag_value_check = "product";
                    Intent intent = new Intent(getApplicationContext(),
                            Stock_Main.class);
                    startActivity(intent);

                }
            }
        });








        // Reading all
        List<Local_Data> contacts1 = dbvoc.HSS_DescriptionITEM();
        results1.add(getResources().getString(R.string.Select_Categories));
        for (Local_Data cn : contacts1)
        {
            if(!cn.getStateName().equalsIgnoreCase("") && !cn.getStateName().equalsIgnoreCase(" "))
            {
                String str_categ = ""+cn.getStateName();
                results1.add(str_categ);
            }
        }

//        if(contacts1.size() > 1)
//        {
        results1.add(getResources().getString(R.string.All_Category));
        // }





        adapter_state1 = new ArrayAdapter<String>(this,R.layout.spinner_item, results1);
        adapter_state1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter_state1);
      //  spnCategory.setOnItemSelectedListener(this);

        results.add(getResources().getString(R.string.Select_Sub_Category));
        adapter_state2 = new ArrayAdapter<String>(this, R.layout.spinner_item, results);
        adapter_state2.setDropDownViewResource(R.layout.spinner_item);
        spnProduct.setAdapter(adapter_state2);


        results2.add(getResources().getString(R.string.Select_Variant_s));
        adapter_state3 = new ArrayAdapter<String>(this, R.layout.spinner_item, results2);
        adapter_state3.setDropDownViewResource(R.layout.spinner_item);
        Product_Variant.setAdapter(adapter_state3);

        spnProduct.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                check_product=check_product+1;
                if(check_product>1)
                {

                    if (spnCategory.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Categories)))
                    {

//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Select_Category), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Select_Category),"yes");

                    }
                    else
                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase(getResources().getString(R.string.Select_Sub_Category)))
                    {
                        results2.add("");
                        results2.clear();

                        results2.add(getResources().getString(R.string.Select_Variant_s));
                        adapter_state3 = new ArrayAdapter<String>(Stock_Product_Options.this, R.layout.spinner_item, results2);
                        adapter_state3.setDropDownViewResource(R.layout.spinner_item);
                        Product_Variant.setAdapter(adapter_state3);
                    }
                    else
                    {

                        results2.clear();
                        results2.add(getResources().getString(R.string.Select_Variant_s));

                        List<Local_Data> contacts33 = dbvoc.HSS_DescriptionITEM1_IDD(parent.getItemAtPosition(pos).toString().trim());
                        for (Local_Data cn : contacts33)
                        {
                            Global_Data.GLOVEL_PRODUCT_ID = cn.getCust_Code();
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.GLOVEL_PRODUCT_ID)) {

                            //List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_ID(Global_Data.GLOVEL_PRODUCT_ID);
                            List<Local_Data> contacts3 = dbvoc.HSS_DescriptionITEM2_BYNAME(spnCategory.getSelectedItem().toString().trim(),parent.getItemAtPosition(pos).toString().trim());
                            for (Local_Data cn : contacts3)
                            {

                                results2.add(cn.getStateName());
                            }

                            adapter_state3 = new ArrayAdapter<String>(Stock_Product_Options.this, R.layout.spinner_item, results2);
                            adapter_state3.setDropDownViewResource(R.layout.spinner_item);
                            Product_Variant.setAdapter(adapter_state3);
                        }

                    }

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        spnCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {

                Log.d("Globel categary", Global_Data.GLOVEL_CATEGORY_SELECTION);

                check=check+1;
                if(check>1)
                {
                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase(getResources().getString(R.string.Select_Categories))) {

                        results.clear();
                        results.add(getResources().getString(R.string.Select_Sub_Category));
                        adapter_state2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results);
                        adapter_state2.setDropDownViewResource(R.layout.spinner_item);
                        spnProduct.setAdapter(adapter_state2);

						results2.clear();
                        results2.add(getResources().getString(R.string.Select_Variant_s));
						adapter_state3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results2);
				        adapter_state3.setDropDownViewResource(R.layout.spinner_item);

//                        Toast toast = Toast.makeText(Stock_Product_Options.this, getResources().getString(R.string.Please_Select_Category), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                        Global_Data.Custom_Toast(Stock_Product_Options.this, getResources().getString(R.string.Please_Select_Category),"yes");

                    }
                    else
                    {
                        List<Local_Data> contacts2 = dbvoc.HSS_DescriptionITEM_ID(parent.getItemAtPosition(pos).toString().trim());
                        //results.add("Select Product");
                        for (Local_Data cn : contacts2)
                        {
                            //Global_Data.GLOVEL_CATEGORY_ID = cn.getCust_Code();

                        }

                        results.clear();
                        //List<Local_Data> contacts22 = dbvoc.HSS_DescriptionITEM1_ID(Global_Data.GLOVEL_CATEGORY_ID);
                        List<Local_Data> contacts22 = dbvoc.HSS_DescriptionITEM1_category_name(parent.getItemAtPosition(pos).toString().trim());
                        results.add(getResources().getString(R.string.Select_Sub_Category));
                        for (Local_Data cn : contacts22) {
                            String str_product = ""+cn.getStateName();
                            //Global_Data.local_pwd = ""+cn.getPwd();

                            results.add(str_product);
                            System.out.println("Local Values:-"+Global_Data.local_user);
                            //Toast.makeText(LoginActivity.this, "Login Invalid"+Global_Data.local_user,Toast.LENGTH_SHORT).show();
                        }

                        results.add(getResources().getString(R.string.All_Sub_Category));
                        adapter_state2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, results);
                        adapter_state2.setDropDownViewResource(R.layout.spinner_item);
                        spnProduct.setAdapter(adapter_state2);


                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();

        Intent i=new Intent(Stock_Product_Options.this, Stock_Main.class);
        startActivity(i);
        finish();
    }


}

