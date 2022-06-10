
package com.msimplelogic.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.msimplelogic.activities.R;
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Checkout_quick_order;

import java.util.ArrayList;
import java.util.List;

public class Item_Edit_Activity extends BaseActivity {
    String scheme_type_new = "";
    String scheme_amount_new = "";
    String scheme_buyqty_new = "";
    String scheme_minqty_new = "";
    String newprise = "";
    int check = 0;
    String scheme_code = "";
    String scheme_namen = "";
    String schemeType, schemeAmouont, schemeGetQty, schemeBuyQty, schemeMinQty;
    private ArrayList<String> Scheme_array = new ArrayList<String>();
    String price = "";
    int qtySchemeValue;
    LoginDataBaseAdapter loginDataBaseAdapter;
    Spinner spnProductSpec, spnScheme;
    TextView editTextRP, editTextMRP, txtPrice, txt_rp, txt_mrp, txtDiscountPrice;
    EditText txtDeleiveryQuantity1;
    static int quantity = 0;
    static float rp, mrp, totalprice;
    static String scheme = "";
    EditText editTextQuantity, spnProduct, eremarks, edetail1, edetail2, edetail3, edetail4, edetail5;
    static String category, productScheme;
    Button buttonAddMOre, buttonEditBack;

    ArrayAdapter<String> dataAdapterCategory, dataAdapterProductSpec, dataAdapterProduct;

    List<String> listProduct, listProductSpec;
    List<String> listScheme;
    ArrayAdapter<String> dataAdapterScheme;
    private ArrayList<String> results = new ArrayList<String>();
    private ArrayList<String> results2 = new ArrayList<String>();

    ArrayAdapter<String> adapter_state1;
    ArrayAdapter<String> adapter_state2;
    ArrayAdapter<String> adapter_state3;
    ArrayAdapter<String> Discount_Adapter;

    SimpleCursorAdapter cursorAdapter;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    SharedPreferences spv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product_item);

        spv = getApplicationContext().getSharedPreferences("valuesset", 0);

        final String SCHEME_NAME = spv.getString("SCHEME_NAME", "");
        final String SCHEME_TYPE = spv.getString("SCHEME_TYPE", "");
        final String SCHEME_AMOUNT = spv.getString("SCHEME_AMOUNT", "");
        final String SCHEME_BUYQTY = spv.getString("SCHEME_BUYQTY", "");
        final String SCHEME_DESCRIPTION = spv.getString("SCHEME_DESCRIPTION", "");
        final String SCHEME_GETQTY = spv.getString("SCHEME_GETQTY", "");
        final String SCHEME_MINQTY = spv.getString("SCHEME_MINQTY", "");

        // create a instance of SQLite Database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        SharedPreferences spf = Item_Edit_Activity.this.getSharedPreferences("SimpleLogic", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("order", "new");
        editor.commit();

        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtDeleiveryQuantity1 = (EditText) findViewById(R.id.txtDeleiveryQuantity);

        txt_rp = (TextView) findViewById(R.id.textRP);
        txt_mrp = (TextView) findViewById(R.id.textMRP);
        txtDiscountPrice = (TextView) findViewById(R.id.txtDiscountPrice);

        // for label RP change
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        String rpstr = spf1.getString("var_rp", "");
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

        txtDeleiveryQuantity1.setVisibility(View.GONE);
        spnProduct = (EditText) findViewById(R.id.spnProduct);
        spnProductSpec = (Spinner) findViewById(R.id.spnProductSpec);
        spnScheme = (Spinner) findViewById(R.id.spnScheme);

        editTextRP = (TextView) findViewById(R.id.editTextRP);
        editTextMRP = (TextView) findViewById(R.id.editTextMRP);

        editTextQuantity = findViewById(R.id.editTextQuantity);
        eremarks = findViewById(R.id.eremarks);
        edetail1 = findViewById(R.id.edetail1);
        edetail2 = findViewById(R.id.edetail2);
        edetail3 = findViewById(R.id.edetail3);
        edetail4 = findViewById(R.id.edetail4);
        edetail5 = findViewById(R.id.edetail5);

//        editTextQuantity.addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable s) {
//
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if (!String.valueOf(s).equalsIgnoreCase("")) {
//                    if (Integer.parseInt(String.valueOf(s)) <= 0) {
//                        editTextQuantity.setText("");
//                        txtPrice.setText(getResources().getString(R.string.Total_Price) + "");
//                    }
//                } else {
//                    txtPrice.setText(getResources().getString(R.string.Total_Price) + "");
//
//                }
//
//            }
//        });

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
        adapter_state2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, results);
        adapter_state3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, results2);
//
//		  Discount_list.add("Select Discount Type");
//		  Discount_list.add("Rupees");
//		  Discount_list.add("Percentage");
//
//		  Discount_Adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Discount_list);
//		  Discount_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
//		  spnScheme.setAdapter(Discount_Adapter);

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

        Scheme_array.clear();
        Scheme_array.add(getResources().getString(R.string.Select_Scheme));

        List<Local_Data> scheme_name = dbvoc.getProductscheme_Name(Global_Data.item_no);
        //results2.add("Select Variant");
        for (Local_Data s : scheme_name) {
            Scheme_array.add(s.getSche_disname());
            scheme_namen = s.getSche_disname().toString();
            scheme_type_new = s.getSche_type().toString();
            scheme_amount_new = s.getSche_amount().toString();
            scheme_buyqty_new = s.getSche_qualifying_qty().toString();
            scheme_minqty_new = s.getSche_min_qty().toString();
        }

        Discount_Adapter = new ArrayAdapter<String>(Item_Edit_Activity.this, R.layout.spinner_item, Scheme_array);
        Discount_Adapter.setDropDownViewResource(R.layout.spinner_item);
        spnScheme.setAdapter(Discount_Adapter);

//        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//        SharedPreferences.Editor editor1 = pref.edit();

        List<Local_Data> schemeitem_name = dbvoc.Get_OrderProducts_scheme(Global_Data.GLObalOrder_id);
        for (Local_Data ss : schemeitem_name) {
            //editor1.putString("key_name", "string value");
//            String scheme_id = ss.getSche_code();
            if (!ss.getSche_code().equalsIgnoreCase("")) {
                Global_Data.SchemeCodeEdit = ss.getSche_code();
                //Global_Data.SchemeCodeEdit;
            }
//            else{
//                Global_Data.SchemeCodeEdit = ss.getSche_code();
//            }

            String scheam = (Global_Data.SchemeCodeEdit);


            List<Local_Data> scheme_type = dbvoc.getProductSchemeType(Global_Data.SchemeCodeEdit);
            for (Local_Data s : scheme_type) {
                schemeType = s.getSche_type();
                schemeAmouont = s.getSche_discription();
                schemeGetQty = s.getSche_disname();
                schemeBuyQty = s.getSche_name();
                schemeMinQty = s.getMinqty();
            }
//            if (!(aaa.equalsIgnoreCase(""))) {
//                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(scheme_namen)) {
//                    int spinnerPosition = Discount_Adapter.getPosition(scheme_namen);
//                    spnScheme.setSelection(spinnerPosition);
//                }
//            }
        }

        if (Global_Data.amount != "") {
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

            //txtDeleiveryQuantity1.setVisibility(View.VISIBLE);

            //TODO FOR SCHEME ERROR
            txtDeleiveryQuantity1.setVisibility(View.GONE);

            if (!Global_Data.actual_discount.equalsIgnoreCase("")) {
                Log.d("Globel ", "in");


            }

        } else {

            editTextMRP.setText("");
            editTextRP.setText("");

            txtDeleiveryQuantity1.setVisibility(View.GONE);
        }

        buttonAddMOre =  findViewById(R.id.buttonAddMOre);
        buttonEditBack =  findViewById(R.id.buttonEditBack);
        buttonEditBack.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View b, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if(Global_Data.fromQuotation.equalsIgnoreCase("YES"))
                    {
                        Global_Data.fromQuotation="";
                        finish();
                    }else{
                        if (Global_Data.quickorderback=="yes"&& Global_Data.quickorderback!=""){
                            Global_Data.GLOVEL_ORDER_REJECT_FLAG = "";
                            Intent intent = new Intent(Item_Edit_Activity.this, Checkout_quick_order.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            startActivity(intent);
                            finish();

                        }else {
                            Global_Data.GLOVEL_ORDER_REJECT_FLAG = "";
                            Intent intent = new Intent(Item_Edit_Activity.this, PreviewOrderSwipeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            startActivity(intent);
                            finish();
                        }
                    }

                    return true;
                }
                return false;
            }
        });

        buttonAddMOre.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

			        
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
//                        Toast toast = Toast.makeText(Item_Edit_Activity.this, getResources().getString(R.string.Please_enter_Quantity), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(Item_Edit_Activity.this, getResources().getString(R.string.Please_enter_Quantity),"yes");
                    }

//					else if (spnScheme.getSelectedItem().toString().equalsIgnoreCase("Select Discount Type")) {
//						Toast toast = Toast.makeText(Item_Edit_Activity.this,"Please Select Discount Type", Toast.LENGTH_SHORT);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
//					}

                    else {

                        // TODO Auto-generated method stub

                        loginDataBaseAdapter = loginDataBaseAdapter.open();

                        String s_price[] = txtPrice.getText().toString().split(":");
                        String dis_price[] = txtDiscountPrice.getText().toString().split(":");
                        // String dis_price = txtDiscountPrice.getText().toString().substring(16)

                        String discount_type = "";
                        String discount_amount = "";

                        List<Local_Data> scheme_name = dbvoc.getProductscheme_code(scheme_namen);
//results2.add("Select Variant");
                        if (scheme_name.size() > 0) {
                            for (Local_Data s : scheme_name) {
                                scheme_code = s.getCode();
                            }
                        } else {
                            scheme_code = "";
                        }
//                        if(editTextQuantity.equals(editTextQuantity)){
//
//                            dbvoc.update_item(editTextQuantity.getText().toString().trim(), editTextMRP.getText().toString().trim(), s_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());
//
//                        }
//                       else
                        if ((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(scheme_type_new)).equalsIgnoreCase("quantity_scheme")) {
//                            dbvoc.update_item(String.valueOf(qtySchemeValue), editTextMRP.getText().toString().trim(), dis_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());

                            if(qtySchemeValue == 0)
                            {
                                dbvoc.update_item(editTextQuantity.getText().toString().trim(), editTextMRP.getText().toString().trim(), s_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());

                            }else{
                                dbvoc.update_item(String.valueOf(qtySchemeValue), editTextMRP.getText().toString().trim(), s_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());

                            }


                            //dbvoc.update_item(String.valueOf(qtySchemeValue), editTextMRP.getText().toString().trim(), s_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());


                        } else if ((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(scheme_type_new)).equalsIgnoreCase("discount_scheme")) {
                            //String sss=Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(dis_price[0].trim());
//                            System.out.println(sss);Discount Price
                            if(dis_price.length==1)
                            {
                                dbvoc.update_item(editTextQuantity.getText().toString().trim(), editTextMRP.getText().toString().trim(), s_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());

                            }else{
                                dbvoc.update_item(editTextQuantity.getText().toString().trim(), editTextMRP.getText().toString().trim(), dis_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());

                            }


                        } else if ((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(scheme_type_new)).equalsIgnoreCase("value_scheme")) {

                            if(dis_price.length==1)
                            {
                                dbvoc.update_item(editTextQuantity.getText().toString().trim(), editTextMRP.getText().toString().trim(), s_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());

                            }else {
                                dbvoc.update_item(editTextQuantity.getText().toString().trim(), editTextMRP.getText().toString().trim(), dis_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());

                            }



                            //      dbvoc.update_item(editTextQuantity.getText().toString().trim(), editTextMRP.getText().toString().trim(), dis_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());


//                            String minSchemeQty=(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(schemeMinQty));
//
//                            if(minSchemeQty.length()>0)
//                            {
//                                if(Double.valueOf(holder.edit_value.getText().toString())>=Double.valueOf(minSchemeQty))
//                                {
//                                    dbvoc.update_item(editTextQuantity.getText().toString().trim(), editTextMRP.getText().toString().trim(), dis_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());
//
////                                    holder.discount_price.setVisibility(View.VISIBLE);
////                                    holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount()))));
////                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());
//
////                                    holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))));
////                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());
//
//                                }else{
//                                    //holder.discount_price.setVisibility(View.GONE);
//                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());
//                                }
//
//                            }else{
//                                dbvoc.update_item(editTextQuantity.getText().toString().trim(), editTextMRP.getText().toString().trim(), dis_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());
//                            }


                        } else {

                            dbvoc.update_item(editTextQuantity.getText().toString().trim(), editTextMRP.getText().toString().trim(), s_price[1].trim(), discount_amount, discount_type, Global_Data.item_no, Global_Data.GLObalOrder_id, scheme_code, eremarks.getText().toString().trim(), edetail1.getText().toString().trim(), edetail2.getText().toString().trim(), edetail3.getText().toString().trim(), edetail4.getText().toString().trim(), edetail5.getText().toString().trim());

                        }

                        // Toast.makeText(Item_Edit_Activity.this, "Item Update Successfully",Toast.LENGTH_LONG).show();

//                        Toast toast = Toast.makeText(Item_Edit_Activity.this, getResources().getString(R.string.Item_updated_successfully), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(Item_Edit_Activity.this, getResources().getString(R.string.Item_updated_successfully),"yes");

                        if (Global_Data.GLOVEL_ORDER_REJECT_FLAG == "") {

                            if (Global_Data.quickorderback=="yes"&& Global_Data.quickorderback!=""){
                                Global_Data.GLOVEL_ORDER_REJECT_FLAG = "";
                                Intent intent = new Intent(Item_Edit_Activity.this, Checkout_quick_order.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                startActivity(intent);
                                finish();

                            }else {
                                Global_Data.GLOVEL_ORDER_REJECT_FLAG = "";
                                Intent intent = new Intent(Item_Edit_Activity.this, PreviewOrderSwipeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(Item_Edit_Activity.this, Status_Activity.class);
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

                    if (!editTextQuantity.getText().toString().equalsIgnoreCase("") && !editTextQuantity.getText().toString().equalsIgnoreCase(null) && !editTextQuantity.getText().toString().equalsIgnoreCase("null") && !editTextMRP.getText().toString().equalsIgnoreCase("") && !editTextMRP.getText().toString().equalsIgnoreCase("null") && !editTextMRP.getText().toString().equalsIgnoreCase(null)) {
                        try {

                            Double final_mrp = Double.valueOf(editTextMRP.getText().toString()) * Double.valueOf(editTextQuantity.getText().toString().trim());


                            if (ttlpricestr.length() > 0) {
                                txtPrice.setText(ttlpricestr + " : " + final_mrp);
                            } else {
                                txtPrice.setText(getResources().getString(R.string.Total_Price) + final_mrp);
                            }

                            //  txtPrice.setText("Total Price : " + final_mrp);
                            price = String.valueOf(final_mrp);

                            Double value = Double.valueOf(editTextQuantity.getText().toString()) * Double.valueOf(editTextMRP.getText().toString());

                            if ((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(scheme_type_new)).equalsIgnoreCase("value_scheme")) {
//                                txtDiscountPrice.setVisibility(View.VISIBLE);
//                                txtDiscountPrice.setText(Item_Edit_Activity.this.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(schemeAmouont)*Double.valueOf(editTextQuantity.getText().toString()))));

                                String minSchemeQty = (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(scheme_minqty_new));

                                if (minSchemeQty.length() > 0) {
                                    if (Double.valueOf(editTextQuantity.getText().toString()) >= Double.valueOf(minSchemeQty)) {
                                        txtDiscountPrice.setVisibility(View.VISIBLE);
                                        txtDiscountPrice.setText(Item_Edit_Activity.this.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value - (Double.valueOf(scheme_amount_new))));
                                        newprise = String.valueOf(value - (Double.valueOf(scheme_amount_new)));
//                                        txtDiscountPrice.setVisibility(View.VISIBLE);
//                                        txtDiscountPrice.setText(Item_Edit_Activity.this.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount()))));
//                                        Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

//                                    holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))));
//                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                                    } else {
                                        txtDiscountPrice.setVisibility(View.GONE);
                                        //Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());
                                    }

                                } else {
                                    txtDiscountPrice.setVisibility(View.VISIBLE);
                                    txtDiscountPrice.setText(Item_Edit_Activity.this.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value - (Double.valueOf(scheme_amount_new) * Double.valueOf(editTextQuantity.getText().toString()))));
                                    newprise = String.valueOf(value - (Double.valueOf(scheme_amount_new) * Double.valueOf(editTextQuantity.getText().toString())));

                                }

                                //Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(editTextQuantity.getText().toString()))) + "pprice" + editTextQuantity.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + vimg_url.getText().toString());

                            } else if ((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(scheme_type_new)).equalsIgnoreCase("discount_scheme")) {
//                                txtDiscountPrice.setVisibility(View.VISIBLE);
//                                //double res = (value / 100.0f) * (Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()));
//                                double res = (value-((value / 100.0f) * (Double.valueOf(schemeAmouont))));
//                                txtDiscountPrice.setText(Item_Edit_Activity.this.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(res));

                                String minSchemeQty = (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(scheme_minqty_new));

                                if (minSchemeQty.length() > 0) {
                                    if (Double.valueOf(editTextQuantity.getText().toString()) >= Double.valueOf(minSchemeQty)) {
                                        txtDiscountPrice.setVisibility(View.VISIBLE);
                                        //double res = (value / 100.0f) * (Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()));
                                        double res = (value - ((value / 100.0f) * (Double.valueOf(scheme_amount_new))));
                                        txtDiscountPrice.setText(Item_Edit_Activity.this.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(res));
//                                        txtDiscountPrice.setVisibility(View.VISIBLE);
//                                        txtDiscountPrice.setText(Item_Edit_Activity.this.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount()))));
//                                        Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

//                                    holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))));
//                                    Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());

                                    } else {
                                        txtDiscountPrice.setVisibility(View.GONE);
                                        //Global_Data.Order_hashmap.put(position + "&" + holder.cat_pid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + holder.t_title.getText().toString() + "pmrp" + holder.mrpv.getText().toString() + "prp" + holder.rpv.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + holder.vimg_url.getText().toString());
                                    }

                                } else {
                                    txtDiscountPrice.setVisibility(View.VISIBLE);
                                    //double res = (value / 100.0f) * (Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()));
                                    double res = (value - ((value / 100.0f) * (Double.valueOf(scheme_amount_new))));
                                    txtDiscountPrice.setText(Item_Edit_Activity.this.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(res));
                                }


                                // Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(res) + "pprice" + editTextQuantity.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + vimg_url.getText().toString());

                            } else if ((Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(scheme_type_new)).equalsIgnoreCase("quantity_scheme")) {

//                         Toast.makeText(mContext, ""+(((Double.valueOf(catalogue_mm.getScheme_buy_qty()))+(Double.valueOf(catalogue_mm.getScheme_get_qty())))*Double.valueOf(holder.edit_value.getText().toString())), Toast.LENGTH_SHORT).show();
                                double aaa = (Double.valueOf(editTextQuantity.getText().toString()) / Double.valueOf(scheme_buyqty_new)) + Double.valueOf(editTextQuantity.getText().toString());
                                qtySchemeValue = (int) aaa;


                                //Toast.makeText(Item_Edit_Activity.this, "" + qtySchemeValue, Toast.LENGTH_SHORT).show();


                                //  Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), y + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + vimg_url.getText().toString());

//                            holder.discount_price.setVisibility(View.VISIBLE);
//                            holder.discount_price.setText(mContext.getResources().getString(R.string.DISCOUNT_PRICE) + String.valueOf(value-(Double.valueOf(catalogue_mm.getScheme_amount())*Double.valueOf(holder.edit_value.getText().toString()))));
                            } else {
                                //  Global_Data.Order_hashmap.put(position + "&" + dialogPid.getText().toString(), s.toString() + "pq" + String.valueOf(value) + "pprice" + dialogText.getText().toString() + "pmrp" + emrp_dialog.getText().toString() + "prp" + erp_dialog.getText().toString() + "sid" + catalogue_mm.getScheme_id()+ "url" + vimg_url.getText().toString());

                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        txtPrice.setText(getResources().getString(R.string.Total_Price) + "");
                        price = String.valueOf("0");
                    }


                }

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

                            if (ttlpricestr.length() > 0) {
                                txtPrice.setText(ttlpricestr + " : " + price1);
                            } else {
                                txtPrice.setText(getResources().getString(R.string.Total_Price) + price1);
                            }

                            // txtPrice.setText("Total Price : "+price1);
                            price = String.valueOf(price1);
                        } else if (spnScheme.getSelectedItem().toString().equalsIgnoreCase("Percentage")) {
                            Float final_mrp = (Float.valueOf(editTextMRP.getText().toString())) * (Float.valueOf(editTextQuantity.getText().toString()));
                            int final_discount = Integer.parseInt(String.valueOf(s));
                            //Float final_discount = (Float.valueOf((String.valueOf(s))))*(Float.valueOf(editTextQuantity.getText().toString()));
                            //		    			   Float per_value = (Float.valueOf((editTextMRP.getText().toString())))*(Float.valueOf((String.valueOf(s))))/(100);
                            //		    			   Float price1 = (Float.valueOf(editTextMRP.getText().toString()))-(per_value);

                            Float per_value = (final_mrp) * (Float.valueOf((final_discount))) / (100);
                            Float price1 = (final_mrp) - (per_value);

                            if (ttlpricestr.length() > 0) {
                                txtPrice.setText(ttlpricestr + " : " + price1);
                            } else {
                                txtPrice.setText(getResources().getString(R.string.Total_Price) + price1);
                            }

                            //txtPrice.setText("Total Price : "+price1);
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
        super.onResume();
    }

}
