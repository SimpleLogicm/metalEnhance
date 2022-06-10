package com.msimplelogic.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.msimplelogic.activities.R;

import com.msimplelogic.model.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Previous_Item_Edit_Activity extends BaseActivity {
    static int quantity = 0;
    static float rp, mrp, totalprice;
    static String scheme = "";
    static String category, productScheme;
    static int categoryID;
    int check=0;
    String scheme_code = "";
    String scheme_namen = "";
    String CategoriesSpinner = "";
    String price = "";
    LoginDataBaseAdapter loginDataBaseAdapter;
    Spinner spnProductSpec, spnScheme;
    TextView editTextRP, editTextMRP, txtPrice, txtDeleiveryQuantity, txt_rp, txt_mrp;
    EditText txtDeleiveryQuantity1;
    EditText editTextQuantity, spnProduct, eremarks, edetail1, edetail2, edetail3, edetail4, edetail5;
    Button buttonAddMOre,buttonEditBack;
    ArrayAdapter<String> dataAdapterCategory,dataAdapterProductSpec,dataAdapterProduct;

    List<String> listProduct,listProductSpec;
    List<String> listScheme;
    ArrayAdapter<String> dataAdapterScheme;
    ArrayAdapter<String> adapter_state1;
    ArrayAdapter<String> adapter_state2;
    ArrayAdapter<String> adapter_state3;
    ArrayAdapter<String> Discount_Adapter;
    SimpleCursorAdapter cursorAdapter;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    ArrayList<Product> dataProducts = new ArrayList<Product>();
    private ArrayList<String> Scheme_array = new ArrayList<String>();
    private ArrayList<String> results = new ArrayList<String>();
    private ArrayList<String> results1 = new ArrayList<String>();
    private ArrayList<String> results2 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previous_edit_product_item);

        // create a instance of SQLite Database
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        SharedPreferences spf=Previous_Item_Edit_Activity.this.getSharedPreferences("SimpleLogic",0);
        SharedPreferences.Editor editor=spf.edit();
        editor.putString("order", "new");
        editor.commit();

        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtDeleiveryQuantity1 = (EditText) findViewById(R.id.txtDeleiveryQuantity);

        txt_rp = (TextView) findViewById(R.id.textRP);
        txt_mrp = (TextView) findViewById(R.id.textMRP);

        // for label RP change
        SharedPreferences spf1=this.getSharedPreferences("SimpleLogic",0);
        String rpstr=spf1.getString("var_rp", "");
        String mrpstr = spf1.getString("var_mrp", "");
        final String ttlpricestr = spf1.getString("var_total_price", "");

        if (rpstr.length() > 0) {
            txt_rp.setText(rpstr + " : ");
        } else {
            txt_rp.setText(getResources().getString(R.string.RP));
        }

        if (mrpstr.length() > 0) {
            txt_mrp.setText(mrpstr + " : ");
        } else {
            txt_mrp.setText(getResources().getString(R.string.MRP));
        }

        txtDeleiveryQuantity1.setVisibility(View.INVISIBLE);
        spnProduct = (EditText) findViewById(R.id.spnProduct);
        spnProductSpec = (Spinner) findViewById(R.id.spnProductSpec);
        spnScheme = (Spinner) findViewById(R.id.spnScheme);

        editTextRP = (TextView) findViewById(R.id.editTextRP);
        editTextMRP = (TextView) findViewById(R.id.editTextMRP);

        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        eremarks = findViewById(R.id.eremarks);
        edetail1 = findViewById(R.id.edetail1);
        edetail2 = findViewById(R.id.edetail2);
        edetail3 = findViewById(R.id.edetail3);
        edetail4 = findViewById(R.id.edetail4);
        edetail5 = findViewById(R.id.edetail5);

        editTextQuantity.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!String.valueOf(s).equalsIgnoreCase(""))
                {
                    if(Integer.parseInt(String.valueOf(s))<=0)
                    {
                        editTextQuantity.setText("");
                        txtPrice.setText(getResources().getString(R.string.Total_Price) + "");

                    }
                }
                else
                {
                    txtPrice.setText(getResources().getString(R.string.Total_Price) + "");

                }

            }
        });

        listProduct = new ArrayList<String>();
        dataAdapterProduct = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listProduct);

        listProductSpec = new ArrayList<String>();
        dataAdapterProductSpec = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listProductSpec);

        listScheme = new ArrayList<String>();
        dataAdapterScheme = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listScheme);

        final List<String> listCategory = new ArrayList<String>();
        listCategory.add(getResources().getString(R.string.Select_Categories));

        // adapter_state1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, results1);
        adapter_state2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, results);
        adapter_state3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, results2);

//        Discount_list.add("Select Discount Type");
//        Discount_list.add("Rupees");
//        Discount_list.add("Percentage");
//
//        Discount_Adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Discount_list);
//        Discount_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//        spnScheme.setAdapter(Discount_Adapter);

//		  adapter_state1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		  city_spinner.setAdapter(adapter_state1);
//		  city_spinner.setOnItemSelectedListener(this);
//
//		  adapter_state2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		  beat_spinner.setAdapter(adapter_state2);
//		  beat_spinner.setOnItemSelectedListener(this);
//
//		  adapter_state3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		  retail_spinner.setAdapter(adapter_state3);
//		  retail_spinner.setOnItemSelectedListener(this);


        if(Global_Data.amount != "")
        {
            spnProduct.setText(Global_Data.product_dec);
            editTextQuantity.setText(Global_Data.total_qty);
            editTextMRP.setText(Global_Data.MRP);
            editTextRP.setText(Global_Data.RP);
            eremarks.setText(Global_Data.e_remarks);
            edetail1.setText(Global_Data.e_detail1);
            edetail2.setText(Global_Data.e_detail2);
            edetail3.setText(Global_Data.e_detail3);
            edetail4.setText(Global_Data.e_detail4);
            edetail5.setText(Global_Data.e_detail5);
            // spnScheme.setEnabled(true);

            if (ttlpricestr.length() > 0) {
                txtPrice.setText(ttlpricestr + " : " + Global_Data.amount);
            } else {
                txtPrice.setText(getResources().getString(R.string.Total_Price) + Global_Data.amount);
            }

            txtDeleiveryQuantity1.setVisibility(View.INVISIBLE);

            if (!Global_Data.actual_discount.equalsIgnoreCase(""))
            {
                Log.d("Globel ", "in");

            }

        }
        else
        {
            //editTextQuantity.setFocusable(false) ;
            editTextMRP.setText("");
            editTextRP.setText("");
            // spnScheme.setEnabled(false);
            txtDeleiveryQuantity1.setVisibility(View.INVISIBLE);
        }

        Scheme_array.clear();
        Scheme_array.add(getResources().getString(R.string.Select_Scheme));

        List<Local_Data> scheme_name = dbvoc.getProductscheme_Name(Global_Data.item_no);
//results2.add("Select Variant");
        for (Local_Data s : scheme_name) {
            Scheme_array.add(s.getSche_disname());
            scheme_namen = s.getSche_disname().toString();


        }


        Discount_Adapter = new ArrayAdapter<String>(Previous_Item_Edit_Activity.this, R.layout.spinner_item, Scheme_array);
        Discount_Adapter.setDropDownViewResource(R.layout.spinner_item);
        spnScheme.setAdapter(Discount_Adapter);

        if(!(Global_Data.pre_schecode.equalsIgnoreCase("")))
        {
            if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(scheme_namen)){
                int spinnerPosition = Discount_Adapter.getPosition(scheme_namen);
                spnScheme.setSelection(spinnerPosition);
            }
        }

        Intent i = getIntent();
        String name = i.getStringExtra("retialer");
        if (i.hasExtra("data")) {
            //Log.e("data", "***********productList**********");
            Global_Data.productList=i.getParcelableArrayListExtra("productsList");
        }


        buttonAddMOre = (Button) findViewById(R.id.buttonAddMOre);
        //buttonAddMOre.setBackgroundColor(Color.parseColor("#414042"));

        buttonEditBack = (Button) findViewById(R.id.buttonEditBack);
        //buttonEditBack.setBackgroundColor(Color.parseColor("#414042"));

//			buttonAddMOre.setOnTouchListener(new OnTouchListener() {
        buttonEditBack.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View b, MotionEvent event) {

                // TODO Auto-generated method stub
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    //up event
                    //b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    //down event
                   // b.setBackgroundColor(Color.parseColor("#910505"));

                    Global_Data.GLOVEL_ORDER_REJECT_FLAG = "";

                    Intent intent = new Intent(Previous_Item_Edit_Activity.this, Previous_orderNew_S2.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    startActivity(intent);
                    finish();

                    return true;
                }
                return false;
            }
        });

        buttonAddMOre.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {

                // TODO Auto-generated method stub
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    //up event
                    //b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    //down event
                    //b.setBackgroundColor(Color.parseColor("#910505"));

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
//					if (spnCategory.getSelectedItem().toString().equalsIgnoreCase("Select Category")) {
//						Toast toast = Toast.makeText(Item_Edit_Activity.this,"Please Select Category", Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
//					}

//					else if (spnProduct.getSelectedItem().toString().equalsIgnoreCase("Select Product")) {
//							Toast toast = Toast.makeText(Item_Edit_Activity.this,"Please Select Product", Toast.LENGTH_SHORT);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
//						}

//					else if (spnProductSpec.getSelectedItem().toString().equalsIgnoreCase("Select Variant")) {
//						Toast toast = Toast.makeText(Item_Edit_Activity.this,"Please Select Variant", Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
//					}

                    if (editTextQuantity.getText().toString().length() == 0) {
//                        Toast toast = Toast.makeText(Previous_Item_Edit_Activity.this, getResources().getString(R.string.Please_enter_Quantity), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();

                        Global_Data.Custom_Toast(Previous_Item_Edit_Activity.this, getResources().getString(R.string.Please_enter_Quantity),"yes");
                    }

//					else if (spnScheme.getSelectedItem().toString().equalsIgnoreCase("Select Discount Type")) {
//						Toast toast = Toast.makeText(Item_Edit_Activity.this,"Please Select Discount Type", Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
//					}

                    else{

                        // TODO Auto-generated method stub
                        // v.setBackgroundColor(Color.parseColor("#910505"));

                        // create a instance of SQLite Database

//						if (!spnScheme.getSelectedItem().toString().equalsIgnoreCase("Select Discount Type")&& txtDeleiveryQuantity1.getText().toString().length() == 0) {
//							Toast toast = Toast.makeText(Item_Edit_Activity.this,"Please enter Discount Amount", Toast.LENGTH_SHORT);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
//						}
//						else
//						{
                        //Global_Data.order_category = spnCategory.getSelectedItem().toString();
                        //Global_Data.order_product = spnProduct.getSelectedItem().toString();
                        // Global_Data.order_variant = spnProductSpec.getSelectedItem().toString();


                        loginDataBaseAdapter=loginDataBaseAdapter.open();

                        String s_price[] = txtPrice.getText().toString().split(":");

//	//					     //Reading all
//				   	         List<Local_Data> contacts = dbvoc.getRR();
//				   	          for (Local_Data cn : contacts) {
//				   	        	String str = ""+cn.getRR();
//				   	        	//Global_Data.local_pwd = ""+cn.getPwd();
//				   	        	System.out.println("Local Values:-"+str);
//				   	        	//Toast.makeText(LoginActivity.this, "Login:"+Global_Data.local_user,Toast.LENGTH_SHORT).show();
//				   	        	                             }
                        String discount_type = "";
                        String discount_amount = "";

                        List<Local_Data> scheme_name = dbvoc.getProductscheme_code(spnScheme.getSelectedItem().toString().trim());
                        if(scheme_name.size() > 0)
                        {
                            for (Local_Data s : scheme_name) {
                                scheme_code= s.getCode();
                            }
                        }
                        else
                        {
                            scheme_code = "";
                        }

                        Long randomPIN = System.currentTimeMillis();
                        String PINString = String.valueOf(randomPIN);

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());

                        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDatef = dff.format(c.getTime());

                        if(Global_Data.Previous_Order_ServiceOrder_ID.equalsIgnoreCase(Global_Data.Previous_Order_UpdateOrder_ID))
                        {
                            Global_Data.Previous_Order_UpdateOrder_ID = "Ord"+PINString;

                            Global_Data.GLObalOrder_id = "Ord"+PINString;
                            Global_Data.GLOvel_GORDER_ID = "Ord"+PINString;
                            // Global_Data.GLOvel_GORDER_ID = "Ord"+PINString;

                            dbvoc.getDeleteTable("previous_orders");

                            try
                            {
                                AppLocationManager appLocationManager = new AppLocationManager(Previous_Item_Edit_Activity.this);
                                Log.d("Class LAT LOG","Class LAT LOG"+appLocationManager.getLatitude()+" "+ appLocationManager.getLongitude());
                                Log.d("Service LAT LOG","Service LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);
                                PlayService_Location PlayServiceManager = new PlayService_Location(Previous_Item_Edit_Activity.this);

                                if(PlayServiceManager.checkPlayServices(Previous_Item_Edit_Activity.this))
                                {
                                    Log.d("Play LAT LOG","Play LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);

                                }
                                else
                                if(!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)  && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null))
                                {
                                    Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                    Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                }

                            }catch(Exception ex){ex.printStackTrace();}

                            Calendar c1 = Calendar.getInstance();
                            System.out.println("Current time =&gt; " + c1.getTime());

                            SimpleDateFormat fdff = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            final String formattedDaten = fdff.format(c1.getTime());

                            SharedPreferences spf = Previous_Item_Edit_Activity.this.getSharedPreferences("SimpleLogic", 0);
                            String user_email = spf.getString("USER_EMAIL",null);

                            loginDataBaseAdapter.insertOrders("", Global_Data.Previous_Order_UpdateOrder_ID, Global_Data.GLOvel_CUSTOMER_ID, Global_Data.order_retailer, user_email, Global_Data.order_city, Global_Data.order_beat, "", "", "", "", "", "", formattedDaten, "", Global_Data.order_retailer, Global_Data.order_state, Global_Data.order_city, Global_Data.sales_btnstring, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Glovel_BEAT_ID, "", "", "", "", "", "", "");

                            loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "NEW ORDER", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                            List<Local_Data> cont1 = dbvoc.getItemNamePrevious_OrderCheck(Global_Data.Previous_Order_ServiceOrder_ID,Global_Data.item_no);
                            for (Local_Data cnt1 : cont1) {

                                loginDataBaseAdapter.insertOrderProducts("", "", Global_Data.GLOvel_GORDER_ID, "", "", "", "", "", cnt1.getSche_code().trim(), " ", "", cnt1.getQty().trim(), cnt1.getRP().trim(), cnt1.getPrice().trim(), cnt1.getAmount().trim(), "", "", Global_Data.order_retailer, " ", cnt1.get_category_ids(), " ", cnt1.getProduct_nm(), "", "", "", "", "", "", "");
                            }

                            dbvoc.getDeleteTable("previous_order_products");

//                            List<Local_Data> check_order_product = dbvoc.GetOrders_BY_ORDER_ID(Global_Data.GLOvel_GORDER_ID,Global_Data.item_no);
//
//                            if(check_order_product.size() < 0)
//                            {
                            loginDataBaseAdapter.insertOrderProducts(" ", " ", Global_Data.GLOvel_GORDER_ID, "", "", "", "", " ", scheme_code, " ", "", editTextQuantity.getText().toString(), editTextRP.getText().toString().trim(), editTextMRP.getText().toString().trim(), s_price[1].trim(), "", "", Global_Data.order_retailer, " ", Global_Data.item_no, " ", spnProduct.getText().toString().trim(), eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), "", edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());
                           // }

                        }
                        else
                        {
                            dbvoc.update_item(editTextQuantity.getText().toString().trim(), editTextMRP.getText().toString().trim(), s_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());
                        }


                       // Toast.makeText(Previous_Item_Edit_Activity.this, "Item Update Successfully",Toast.LENGTH_LONG).show();

//                        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Item_Update_Successfully), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Item_Update_Successfully),"yes");


                            Intent intent = new Intent(Previous_Item_Edit_Activity.this, Previous_orderNew_S2.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            startActivity(intent);
                            finish();


//
                        return true;
                        //}
                    }

                    return true;
                }
                return false;
            }
        });



        editTextQuantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                {

//			    	  if(!txtDeleiveryQuantity1.getText().toString().equalsIgnoreCase("") && !txtDeleiveryQuantity1.getText().toString().equalsIgnoreCase(null) && !txtDeleiveryQuantity1.getText().toString().equalsIgnoreCase("null"))
//			    	  {
//
//
//				    	  if(spnScheme.getSelectedItem().toString().equalsIgnoreCase("Rupees"))
//				    	  {
//				    		  int final_mrp = (Integer.parseInt(editTextMRP.getText().toString()))*(Integer.parseInt(String.valueOf(s)));
//				    		  int final_discount = Integer.parseInt(String.valueOf(s));
//				    		  // int final_discount = (Integer.parseInt(txtDeleiveryQuantity1.getText().toString()))*(Integer.parseInt(String.valueOf(s)));
//				    		 // int price1 = (Integer.parseInt(editTextMRP.getText().toString()))-(Integer.parseInt((String.valueOf(s))));
//				    		  int price1 = (final_mrp)-(final_discount);
//				    		  txtPrice.setText("Total Price : "+price1);
//				    		  price = String.valueOf(price1);
//				    	  }
//				    	  else
//			    		  if(spnScheme.getSelectedItem().toString().equalsIgnoreCase("Percentage"))
//				    	  {
//			    			   Float final_mrp = (Float.valueOf(editTextMRP.getText().toString()))*(Float.valueOf(String.valueOf(s)));
//			    			   int final_discount = Integer.parseInt(String.valueOf(s));
//			    			   //Float final_discount = (Float.valueOf((txtDeleiveryQuantity1.getText().toString())))*(Float.valueOf(String.valueOf(s)));
//			//		    			   Float per_value = (Float.valueOf((editTextMRP.getText().toString())))*(Float.valueOf((String.valueOf(s))))/(100);
//			//		    			   Float price1 = (Float.valueOf(editTextMRP.getText().toString()))-(per_value);
//
//			    			   Float per_value = (final_mrp)*(Float.valueOf((final_discount)))/(100);
//			    			   Float price1 = (final_mrp)-(per_value);
//
//				    		  txtPrice.setText("Total Price : "+price1);
//				    		  price = String.valueOf(price1);
//				    	  }
//
//			    	  }
//			    	  else
//			    	  {
                    if(!editTextQuantity.getText().toString().equalsIgnoreCase("") && !editTextQuantity.getText().toString().equalsIgnoreCase(null) && !editTextQuantity.getText().toString().equalsIgnoreCase("null") &&  !editTextMRP.getText().toString().equalsIgnoreCase("") && !editTextMRP.getText().toString().equalsIgnoreCase("null") && !editTextMRP.getText().toString().equalsIgnoreCase(null))
                    {
                        try {
                            Double final_mrp = Double.valueOf(editTextMRP.getText().toString()) * Double.valueOf(editTextQuantity.getText().toString().trim());

                            if (ttlpricestr.length() > 0) {
                                txtPrice.setText(ttlpricestr + " : " + final_mrp);
                            } else {
                                txtPrice.setText(getResources().getString(R.string.Total_Price) + final_mrp);
                            }

                            price = String.valueOf(final_mrp);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    else
                    {
                        txtPrice.setText(getResources().getString(R.string.Total_Price) + "");
                        price = String.valueOf("0");
                    }

                }

            }
        });

        txtDeleiveryQuantity1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                {

                    if(!editTextQuantity.getText().toString().equalsIgnoreCase("") && !editTextQuantity.getText().toString().equalsIgnoreCase(null) && !editTextQuantity.getText().toString().equalsIgnoreCase("null"))
                    {
                        if(spnScheme.getSelectedItem().toString().equalsIgnoreCase("Rupees"))
                        {
                            long final_mrp = (Long.valueOf(editTextMRP.getText().toString()))*(Long.valueOf(editTextQuantity.getText().toString().trim()));
                            long final_discount = Long.parseLong(String.valueOf(s));
                            //int final_discount = (Integer.parseInt(String.valueOf(s)))*(Integer.parseInt(editTextQuantity.getText().toString()));
                            // int price1 = (Integer.parseInt(editTextMRP.getText().toString()))-(Integer.parseInt((String.valueOf(s))));
                            long price1 = (final_mrp)-(final_discount);

                            if (ttlpricestr.length() > 0) {
                                txtPrice.setText(ttlpricestr + " : " + price1);
                            } else {
                                txtPrice.setText(getResources().getString(R.string.Total_Price) + price1);
                            }

                            //txtPrice.setText("Total Price : "+price1);
                            price = String.valueOf(price1);
                        }
                        else
                        if(spnScheme.getSelectedItem().toString().equalsIgnoreCase("Percentage"))
                        {
                            Float final_mrp = (Float.valueOf(editTextMRP.getText().toString()))*(Float.valueOf(editTextQuantity.getText().toString()));
                            int final_discount = Integer.parseInt(String.valueOf(s));
                            //Float final_discount = (Float.valueOf((String.valueOf(s))))*(Float.valueOf(editTextQuantity.getText().toString()));
                            //		    			   Float per_value = (Float.valueOf((editTextMRP.getText().toString())))*(Float.valueOf((String.valueOf(s))))/(100);
                            //		    			   Float price1 = (Float.valueOf(editTextMRP.getText().toString()))-(per_value);

                            Float per_value = (final_mrp)*(Float.valueOf((final_discount)))/(100);
                            Float price1 = (final_mrp)-(per_value);

                            if (ttlpricestr.length() > 0) {
                                txtPrice.setText(ttlpricestr + " : " + price1);
                            } else {
                                txtPrice.setText(getResources().getString(R.string.Total_Price) + price1);
                            }

                            // txtPrice.setText("Total Price : "+price1);
                            price = String.valueOf(price1);
                        }

                    }


                }
                //Field2.setText("");
            }
        });

        spnScheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                // TODO Auto-generated method stub
                productScheme = parent.getItemAtPosition(pos).toString();
                //Toast.makeText(NewOrderActivity.this, "click appear.", Toast.LENGTH_SHORT).show();
                if (parent.getItemAtPosition(pos).toString()
                        .equalsIgnoreCase(getResources().getString(R.string.Select_Scheme))) {



                }
                else{




                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

//        spnScheme.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View arg1,
//                                       int pos, long arg3) {
//                // TODO Auto-generated method stub
//                productScheme = parent.getItemAtPosition(pos).toString();
//                if (parent.getItemAtPosition(pos).toString()
//                        .equalsIgnoreCase("Select Discount Type")) {
//
//                    txtDeleiveryQuantity1.setVisibility(View.INVISIBLE);
//
//                    if(!editTextQuantity.getText().toString().equalsIgnoreCase("") && !editTextQuantity.getText().toString().equalsIgnoreCase(null) && !editTextQuantity.getText().toString().equalsIgnoreCase("null"))
//                    {
//                        Float final_mrp = (Float.valueOf(editTextMRP.getText().toString()))*(Float.valueOf(editTextQuantity.getText().toString().trim()));
//                        txtPrice.setText("Total Price : "+final_mrp);
//                        price = String.valueOf(final_mrp);
//                    }
//                    else
//                    {
//                        if(!editTextMRP.getText().toString().equalsIgnoreCase("") && !editTextMRP.getText().toString().equalsIgnoreCase(null) && !editTextMRP.getText().toString().equalsIgnoreCase("null"))
//                        {
//                            Float final_mrp = (Float.valueOf(editTextMRP.getText().toString()));
//                            txtPrice.setText("Total Price : "+final_mrp);
//                            price = String.valueOf(final_mrp);
//                        }
//                    }
//
//                }
//                else
//                if(activity_load_flag != 0)
//                {
//                    activity_load_flag++;
//                    if(parent.getItemAtPosition(pos).toString()
//                            .equalsIgnoreCase("Rupees"))
//                    {
//                        txtDeleiveryQuantity1.setText("");
//                        txtDeleiveryQuantity1.setVisibility(View.VISIBLE);
//                        txtDeleiveryQuantity1.setHint("Rs.");
//                        txtPrice.setText("Total Price : ");
//                    }
//                    else
//                    if(parent.getItemAtPosition(pos).toString()
//                            .equalsIgnoreCase("Percentage"))
//                    {
//                        txtDeleiveryQuantity1.setText("");
//                        txtDeleiveryQuantity1.setVisibility(View.VISIBLE);
//                        txtDeleiveryQuantity1.setHint("%");
//                        txtPrice.setText("Total Price : ");
//                    }
//                }
//
//                activity_load_flag++;
////				else {
////					schemeID=dbschemeID;
////					scheme=parent.getSelectedItem().toString();
////
////					String [] aray=scheme.split("and");
////					int buy=Integer.parseInt(aray[0].replaceAll("[\\D]", ""));
////					int get=Integer.parseInt(aray[1].replaceAll("[\\D]", ""));
////					if (editTextQuantity.getText().toString().length()!=0) {
////						quantity=Integer.parseInt(editTextQuantity.getText().toString());
////						int extra = quantity / buy;
////						deleiveryQuantity = extra*get + quantity;
////						txtDeleiveryQuantity.setText("Delivery Quantity : "
////								+ deleiveryQuantity);
////    				}
////    			}
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//
//            }
//
//        });

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
        //super.onBackPressed();

        this.finish();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stubs
        super.onResume();
        //buttonAddMOre.setBackgroundColor(Color.parseColor("#414042"));
        //buttonPreviewOrder.setBackgroundColor(Color.parseColor("#414042"));
    }


}
