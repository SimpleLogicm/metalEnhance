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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.msimplelogic.activities.R;

import com.msimplelogic.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ReturnEditItem extends BaseActivity {
    int check = 0;
    int activity_load_flag = 0;
    String CategoriesSpinner = "";
    String ProductSpinner = "";
    String price = "";
    LoginDataBaseAdapter loginDataBaseAdapter;
    Spinner spnProductSpec, spnScheme;
    TextView editTextRP, editTextMRP, txtPrice, txt_rp;
    EditText txtDeleiveryQuantity1;
    static int quantity = 0;
    static float rp, mrp, totalprice;
    static String scheme = "";
    EditText editTextQuantity, spnProduct;
    static String category, productScheme;
    Button buttonAddMOre, buttonEditBack;
    ArrayAdapter<String> dataAdapterCategory, dataAdapterProductSpec, dataAdapterProduct;

    List<String> listProduct, listProductSpec;
    List<String> listScheme;
    ArrayList<String> Discount_list = new ArrayList<String>();
    ArrayAdapter<String> dataAdapterScheme;
    static int categoryID;
    private ArrayList<String> results = new ArrayList<String>();
    private ArrayList<String> results2 = new ArrayList<String>();

    ArrayAdapter<String> adapter_state1;
    ArrayAdapter<String> adapter_state2;
    ArrayAdapter<String> adapter_state3;
    ArrayAdapter<String> Discount_Adapter;

    SimpleCursorAdapter cursorAdapter;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    ArrayList<Product> dataProducts = new ArrayList<Product>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);

        // create a instance of SQLite Database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();


        SharedPreferences spf = ReturnEditItem.this.getSharedPreferences("SimpleLogic", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("order", "new");
        editor.commit();


        txtPrice = (TextView) findViewById(R.id.txtPrice);
        //txtDeleiveryQuantity = (TextView) findViewById(R.id.txtDeleiveryQuantity);
        txtDeleiveryQuantity1 = (EditText) findViewById(R.id.txtDeleiveryQuantity);
        //spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnProduct = (EditText) findViewById(R.id.spnProduct);
        spnProductSpec = (Spinner) findViewById(R.id.spnProductSpec);
        spnScheme = (Spinner) findViewById(R.id.spnScheme);

        editTextRP = (TextView) findViewById(R.id.editTextRP);
        editTextMRP = (TextView) findViewById(R.id.editTextMRP);

        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);


        txt_rp = (TextView) findViewById(R.id.textRP);
        // for label RP change
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        String rpstr = spf1.getString("var_rp", "");
        txt_rp.setText(rpstr);

        editTextQuantity.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!String.valueOf(s).equalsIgnoreCase("")) {
                    if (Integer.parseInt(String.valueOf(s)) <= 0) {
                        editTextQuantity.setText("");
                    }
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
        listCategory.add("Select Category");

        // adapter_state1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, results1);
        adapter_state2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, results);
        adapter_state3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, results2);

        Discount_list.add("Select Discount Type");
        Discount_list.add("Rupees");
        Discount_list.add("Percentage");

        Discount_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Discount_list);
        Discount_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnScheme.setAdapter(Discount_Adapter);


        if (Global_Data.amount != "") {
            spnProduct.setText(Global_Data.product_dec);
            editTextQuantity.setText(Global_Data.total_qty);
            editTextMRP.setText(Global_Data.MRP);
            editTextRP.setText(Global_Data.RP);
            // spnScheme.setEnabled(true);
            txtPrice.setText(getResources().getString(R.string.Total_Price) + Global_Data.amount);
            //  txtDeleiveryQuantity1.setVisibility(View.VISIBLE);

            if (!Global_Data.actual_discount.equalsIgnoreCase("")) {
                Log.d("Globel categary", "in");
                spnScheme.setSelection(Discount_Adapter.getPosition(Global_Data.actual_discount));
                //Global_Data.GLOVEL_CATEGORY_SELECTION = "";

                txtDeleiveryQuantity1.setText(Global_Data.scheme_amount);

            }

        } else {
            //editTextQuantity.setFocusable(false) ;
            editTextMRP.setText("");
            editTextRP.setText("");
            // spnScheme.setEnabled(false);
            txtDeleiveryQuantity1.setVisibility(View.INVISIBLE);
        }

        Intent i = getIntent();
        String name = i.getStringExtra("retialer");
        if (i.hasExtra("data")) {
            //Log.e("data", "***********productList**********");
            Global_Data.productList = i.getParcelableArrayListExtra("productsList");
        }


        buttonAddMOre = (Button) findViewById(R.id.buttonAddMOre);
        buttonAddMOre.setBackgroundColor(Color.parseColor("#414042"));

        buttonEditBack = (Button) findViewById(R.id.buttonEditBack);
        buttonEditBack.setBackgroundColor(Color.parseColor("#414042"));

//			buttonAddMOre.setOnTouchListener(new OnTouchListener() {
        buttonEditBack.setOnTouchListener(new OnTouchListener() {
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

                    Global_Data.GLOVEL_ORDER_REJECT_FLAG = "";
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
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //down event
                    b.setBackgroundColor(Color.parseColor("#910505"));
			        
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
//                        Toast toast = Toast.makeText(ReturnEditItem.this, getResources().getString(R.string.Please_enter_Quantity), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(ReturnEditItem.this, getResources().getString(R.string.Please_enter_Quantity),"yes");
                    }

//					else if (spnScheme.getSelectedItem().toString().equalsIgnoreCase("Select Discount Type")) {
//						Toast toast = Toast.makeText(Item_Edit_Activity.this,"Please Select Discount Type", Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
//					}

                    else {

                        loginDataBaseAdapter = loginDataBaseAdapter.open();

                        String s_price[] = txtPrice.getText().toString().split(":");
                        String discount_type = "";
                        String discount_amount = "";
//						     if (spnScheme.getSelectedItem().toString().equalsIgnoreCase("Select Discount Type"))
//						     {
//						    	 discount_type = "";
//						    	 discount_amount = "";
//						     }
//						     else
//						     {
//						    	 discount_type = spnScheme.getSelectedItem().toString().trim();
//						    	 discount_amount = txtDeleiveryQuantity1.getText().toString().trim();
//						     }
                        dbvoc.update_item_return(editTextQuantity.getText().toString().trim(), editTextMRP.getText().toString().trim(), s_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id_return);
                        //Toast.makeText(ReturnEditItem.this, "Item Update Successfully",Toast.LENGTH_LONG).show();

//                        Toast toast = Toast.makeText(ReturnEditItem.this, getResources().getString(R.string.Item_Update_Successfully), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(ReturnEditItem.this, getResources().getString(R.string.Item_Update_Successfully),"yes");

                        if (Global_Data.GLOVEL_ORDER_REJECT_FLAG == "") {
                            Intent intent = new Intent(ReturnEditItem.this, ReturnOrder2.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(ReturnEditItem.this, Status_Activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            startActivity(intent);
                            finish();
                        }

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
                    if (!editTextQuantity.getText().toString().equalsIgnoreCase("") && !editTextQuantity.getText().toString().equalsIgnoreCase(null) && !editTextQuantity.getText().toString().equalsIgnoreCase("null") && !editTextMRP.getText().toString().equalsIgnoreCase("") && !editTextMRP.getText().toString().equalsIgnoreCase("null") && !editTextMRP.getText().toString().equalsIgnoreCase(null)) {
                        try {
                            Double final_mrp = Double.valueOf(editTextMRP.getText().toString()) * Double.valueOf(editTextQuantity.getText().toString().trim());
                            txtPrice.setText(getResources().getString(R.string.Total_Price) + final_mrp);
                            price = String.valueOf(final_mrp);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        //}
                    } else {
                        txtPrice.setText(getResources().getString(R.string.Total_Price) + "");
                        price = String.valueOf("0");
                    }

                }
                //Field2.setText("");
            }
        });

        txtDeleiveryQuantity1.addTextChangedListener(new TextWatcher() {

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

                    if (!editTextQuantity.getText().toString().equalsIgnoreCase("") && !editTextQuantity.getText().toString().equalsIgnoreCase(null) && !editTextQuantity.getText().toString().equalsIgnoreCase("null")) {
                        if (spnScheme.getSelectedItem().toString().equalsIgnoreCase("Rupees")) {
                            long final_mrp = (Long.valueOf(editTextMRP.getText().toString())) * (Long.valueOf(editTextQuantity.getText().toString().trim()));
                            long final_discount = Long.parseLong(String.valueOf(s));
                            //int final_discount = (Integer.parseInt(String.valueOf(s)))*(Integer.parseInt(editTextQuantity.getText().toString()));
                            // int price1 = (Integer.parseInt(editTextMRP.getText().toString()))-(Integer.parseInt((String.valueOf(s))));
                            long price1 = (final_mrp) - (final_discount);
                            txtPrice.setText(getResources().getString(R.string.Total_Price) + price1);
                            price = String.valueOf(price1);
                        } else if (spnScheme.getSelectedItem().toString().equalsIgnoreCase("Percentage")) {
                            Float final_mrp = (Float.valueOf(editTextMRP.getText().toString())) * (Float.valueOf(editTextQuantity.getText().toString()));
                            int final_discount = Integer.parseInt(String.valueOf(s));
                            //Float final_discount = (Float.valueOf((String.valueOf(s))))*(Float.valueOf(editTextQuantity.getText().toString()));
                            //		    			   Float per_value = (Float.valueOf((editTextMRP.getText().toString())))*(Float.valueOf((String.valueOf(s))))/(100);
                            //		    			   Float price1 = (Float.valueOf(editTextMRP.getText().toString()))-(per_value);

                            Float per_value = (final_mrp) * (Float.valueOf((final_discount))) / (100);
                            Float price1 = (final_mrp) - (per_value);

                            txtPrice.setText(getResources().getString(R.string.Total_Price) + price1);
                            price = String.valueOf(price1);
                        }

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
                if (parent.getItemAtPosition(pos).toString()
                        .equalsIgnoreCase("Select Discount Type")) {

                    txtDeleiveryQuantity1.setVisibility(View.INVISIBLE);

                    if (!editTextQuantity.getText().toString().equalsIgnoreCase("") && !editTextQuantity.getText().toString().equalsIgnoreCase(null) && !editTextQuantity.getText().toString().equalsIgnoreCase("null")) {
                        Float final_mrp = (Float.valueOf(editTextMRP.getText().toString())) * (Float.valueOf(editTextQuantity.getText().toString().trim()));
                        txtPrice.setText(getResources().getString(R.string.Total_Price) + final_mrp);
                        price = String.valueOf(final_mrp);
                    } else {
                        if (!editTextMRP.getText().toString().equalsIgnoreCase("") && !editTextMRP.getText().toString().equalsIgnoreCase(null) && !editTextMRP.getText().toString().equalsIgnoreCase("null")) {
                            Float final_mrp = (Float.valueOf(editTextMRP.getText().toString()));
                            txtPrice.setText(getResources().getString(R.string.Total_Price) + final_mrp);
                            price = String.valueOf(final_mrp);
                        }
                    }

                } else if (activity_load_flag != 0) {
                    activity_load_flag++;
                    if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase("Rupees")) {
//						txtDeleiveryQuantity1.setText("");
//						txtDeleiveryQuantity1.setVisibility(View.VISIBLE);
//						txtDeleiveryQuantity1.setHint("Rs.");
//						txtPrice.setText("Total Price : ");
                    } else if (parent.getItemAtPosition(pos).toString()
                            .equalsIgnoreCase("Percentage")) {
//						txtDeleiveryQuantity1.setText("");
//						txtDeleiveryQuantity1.setVisibility(View.VISIBLE);
//						txtDeleiveryQuantity1.setHint("%");
//						txtPrice.setText("Total Price : ");
                    }
                }

                activity_load_flag++;
//				else {
//					schemeID=dbschemeID;
//					scheme=parent.getSelectedItem().toString();
//					
//					String [] aray=scheme.split("and");
//					int buy=Integer.parseInt(aray[0].replaceAll("[\\D]", ""));
//					int get=Integer.parseInt(aray[1].replaceAll("[\\D]", ""));
//					if (editTextQuantity.getText().toString().length()!=0) {
//						quantity=Integer.parseInt(editTextQuantity.getText().toString());
//						int extra = quantity / buy;
//						deleiveryQuantity = extra*get + quantity;
//						txtDeleiveryQuantity.setText("Delivery Quantity : "
//								+ deleiveryQuantity);  
//    				}
//    			}


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
        //super.onBackPressed();

        this.finish();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stubs
        super.onResume();
        buttonAddMOre.setBackgroundColor(Color.parseColor("#414042"));
        //buttonPreviewOrder.setBackgroundColor(Color.parseColor("#414042"));
    }





}
