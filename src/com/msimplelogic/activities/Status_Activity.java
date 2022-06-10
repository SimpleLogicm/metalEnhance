package com.msimplelogic.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msimplelogic.activities.R;

import com.msimplelogic.activities.Status_Adapter.customButtonListener;
import com.msimplelogic.services.getServices;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

//import android.support.v7.app.ActionBar;

public class Status_Activity extends BaseActivity implements customButtonListener {
    ArrayList<String> totalApproveAmount = new ArrayList<String>();
    Double sum = 0.0;
    double expectAmt, totalAmt;
    ListView list_statusclick;
    String ORDER_ID = "order_id";
    String original_id="";
    String MANAGER_REMARK = "manager_remarks";
    String ORDER_STATUS = "order_status";
    String ITEM_DESC = "item_desc";
    String ITEM_AMOUNT = "item_tamount";
    String ITEM_NUMBER = "item_number";
    String ITEM_QUANTITY = "Item_quantity";
    String QUOTATION_ID = "quotation_id";
    String MODIFY_VALUE = "modify_value";
    String APPROVED_BYADMIN = "approved_amount_by_admin";
    String ITEM_NO = "item_number";
    String quotationId = "";
    String modifyValue = "";
    String reasonvalues = "";
    String approveAmtByAdmin = "";
    String orderAmount = "";
    String itemNumber = "";
    String expectedAmountEnteredStatus = "FALSE";
    HttpPost http_post;
    HttpResponse http_resp;
    HttpClient http_client;
    List<NameValuePair> http_nmvalpair;
    HttpEntity http_entity;
    ArrayAdapter adapter;
    ListView status_list;
    //CustomListAdapter adapter1;
    JSONObject jsonobject;
    EditText expectedAmount, reason;
    TextView totalAmount;
    static TextView approvedAmount;
    Button but_listclick, butaprove_listclick, but_convert_order;
    ImageView but_addmore,hedder_theame;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    JSONArray jsonarray;
    String str, str1, cust_str;
    Spinner city_spinner, state_spinner, spinner_click, spinner_modify;
    String customer_address = "";
    LoginDataBaseAdapter loginDataBaseAdapter;
    ProgressDialog dialog;
    String device_id = "";
    String line;
    Button status_button, cancel_list;
    Status_Adapter customListAdapter;
    OrderStatus_Adapter OrderListAdapter;
    EditText searchbox;
    ArrayList<String> product_value = new ArrayList<String>();
    ArrayList<String> searchResults = new ArrayList<String>();
    BufferedReader in = null;
    ArrayList<HashMap<String, String>> arraylist1;
    ArrayList<HashMap<String, String>> arraylist2;
    private ArrayList<HashMap<String, String>> dataArrayList;
    private ArrayList<HashMap<String, String>> OrderDataList;
    String CUSTOMER_NAME = "Customer_name";
    String CUSTOMER_ADDRESS = "Customer_address";
    String CUSTOMER_DISTANCE = "Customer_distance";
    AutoCompleteTextView autoCompleteTextView1;
    LinearLayout totalAmountLout, expectedLout, approvedLout, reasonLout;
    Toolbar toolbar;
    SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotestatus);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        status_list = (ListView) findViewById(R.id.status_list);
        cancel_list = (Button) findViewById(R.id.cancel_list);
        hedder_theame = findViewById(R.id.hedder_theame);
        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);

        }

        Global_Data.fromQuotation="";
        SharedPreferences pref_devid = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
        device_id = pref_devid.getString("devid", "");

        dataArrayList = new ArrayList<HashMap<String, String>>();

        OrderDataList = new ArrayList<HashMap<String, String>>();

        if (Global_Data.GLOVEL_ORDER_REJECT_FLAG == "TRUE") {
            if (!Global_Data.GLOvel_REMARK.equalsIgnoreCase("rejected")) {
                getListViewg(Global_Data.GLObalOrder_id, Global_Data.GLOvel_REMARK);
            }
        } else {
            View_NearestCustomer(device_id, Global_Data.cust_str);
        }

        cancel_list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        status_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView contactId = (TextView) view.findViewById(R.id.ordername);
                String str = contactId.getText().toString();

                TextView contactstatus = (TextView) view.findViewById(R.id.remark);
                String strr = contactstatus.getText().toString();

                TextView quotation_id = (TextView) view.findViewById(R.id.quotation_id);
                TextView orgqotationid = (TextView) view.findViewById(R.id.ordername);
                original_id=orgqotationid.getText().toString();

                TextView modify_value = (TextView) view.findViewById(R.id.modify_value);
                TextView approved_byadmin = (TextView) view.findViewById(R.id.approved_byadmin);
                TextView status=(TextView)view.findViewById(R.id.status);
                //TextView item_no = (TextView) view.findViewById(R.id.item_no);
                reasonvalues=status.getText().toString().trim();
                quotationId = quotation_id.getText().toString().trim();
                modifyValue = modify_value.getText().toString().trim();
                approveAmtByAdmin = approved_byadmin.getText().toString().trim();
                //itemNumber= item_no.getText().toString().trim();

                Global_Data.GLOvel_REMARK = strr;
                // String value = (String)parent.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(),"fgd:"+str,Toast.LENGTH_LONG).show();
//                 if(Global_Data.GLOvel_REMARK.equalsIgnoreCase("ordered"))
//                 {
//                	 Toast.makeText(getApplicationContext(), "Quotation Already aproved", Toast.LENGTH_LONG).show();
//                 }
//                 else
//                 {

                if (!Global_Data.GLOvel_REMARK.equalsIgnoreCase("rejected")) {
                    getListViewg(str, strr);
                }

                // }

            }
        });

//        try {
//            android.app.ActionBar mActionBar = getActionBar();
//            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            // mActionBar.setDisplayShowHomeEnabled(false);
//            // mActionBar.setDisplayShowTitleEnabled(false);
//            LayoutInflater mInflater = LayoutInflater.from(this);
//            Intent i = getIntent();
//            String name = i.getStringExtra("retialer");
//            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
//            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
//			mTitleTextView.setText(getResources().getString(R.string.Quote_status));
//
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = Status_Activity.this.getSharedPreferences("SimpleLogic", 0);
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
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    }
//                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
//                }
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
////
////		       if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
////		       	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
////				}
//
//
////		       if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)<0) {
////		 //       	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
////		       	todaysTarget.setText("Today's Target Acheived");
////				}
//
//            mActionBar.setCustomView(mCustomView);
//            mActionBar.setDisplayShowCustomEnabled(true);
//            mActionBar.setHomeButtonEnabled(true);
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

    }

    public void View_NearestCustomer(String device_id, String cust_id) {

        SharedPreferences pref_devid = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
        device_id = pref_devid.getString("devid", "");

        loginDataBaseAdapter = new LoginDataBaseAdapter(Status_Activity.this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        SharedPreferences spf = Status_Activity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

        // dbvoc = new DataBaseHelper(context);

        //PreferencesHelper Prefs = new PreferencesHelper(context);
        //String URL = Prefs.GetPreferences("URL");
        String domain = "";

        dialog = new ProgressDialog(Status_Activity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(getResources().getString(R.string.Please_Wait));
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
        String Cust_domain = sp.getString("Cust_Service_Url", "");
        String service_url = Cust_domain + "metal/api/v1/";
        domain = service_url;

        Log.d("url", "url customer" + domain + "quotations/send_quotation_status?customer_code=" + cust_id + "&email=" + user_email);

        // Global_Val global_Val = new Global_Val();
//        if(URL.equalsIgnoreCase(null) || URL.equalsIgnoreCase("null") || URL.equalsIgnoreCase("") || URL.equalsIgnoreCase(" ")) {
//            domain = context.getResources().getString(R.string.service_domain);
//        }
//        else
//        {
//            domain = URL.toString();
//        }


        StringRequest stringRequest = null;
        stringRequest = new StringRequest(Method.GET, domain + "quotations/send_quotation_status?customer_code=" + cust_id + "&email=" + user_email,

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
                                if (json.has("result")) {
                                    response_result = json.getString("result");
                                } else {
                                    response_result = "data";
                                }

                                if (response_result.equalsIgnoreCase("product not found")) {
                                    //Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG).show();

                                    Global_Data.Custom_Toast(Status_Activity.this, response_result, "Yes");
//								  Toast toast = Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG);
//								  toast.setGravity(Gravity.CENTER, 0, 0);
//								  toast.show();

                                    Intent a = new Intent(Status_Activity.this, MainActivity.class);
                                    startActivity(a);
                                    finish();
                                } else if (response_result.equalsIgnoreCase("Device not registered")) {

                                    // Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG).show();

                                    Global_Data.Custom_Toast(Status_Activity.this, response_result, "Yes");
//								  Toast toast = Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG);
//								  toast.setGravity(Gravity.CENTER, 0, 0);
//								  toast.show();

                                } else if (json.getJSONArray("quotations").length() <= 0) {

                                    // Toast.makeText(Status_Activity.this, "Quotations Not Found for Customer", Toast.LENGTH_LONG).show();

                                    Global_Data.Custom_Toast(Status_Activity.this, getResources().getString(R.string.Quotations_Not_Found), "Yes");
//								  Toast toast = Toast.makeText(Status_Activity.this, getResources().getString(R.string.Quotations_Not_Found), Toast.LENGTH_LONG);
//								  toast.setGravity(Gravity.CENTER, 0, 0);
//								  toast.show();

                                    Intent a = new Intent(Status_Activity.this, MainActivity.class);
                                    startActivity(a);
                                    finish();

                                } else {

                                    dbvoc.getDeleteTable("status_master");

                                    JSONArray quotations = json.getJSONArray("quotations");
                                    Log.d("quotations", "quotations" + quotations.toString());

                                    Log.d("quotations", "quotations length" + quotations.length());

                                    JSONArray quotation_products = json.getJSONArray("quotation_products");
                                    Log.d("quotation_products", "quotation_products" + quotation_products.toString());

                                    Log.d("quotation_products", "quotation_products length" + quotation_products.length());

                                    // Log.d("customers", "customers" + customers.toString());
                                    // Log.d("devices", "devices" + devices.toString());


                                    dataArrayList.clear();
                                    for (int i = 0; i < quotations.length(); i++) {

                                        JSONObject jsonObject = quotations.getJSONObject(i);
                                        HashMap<String, String> map = new HashMap<String, String>();

//										 quotationId=jsonObject.getString("quotation_id");
//										 modifyValue=jsonObject.getString("modify_value");

                                        map.put(ORDER_ID, jsonObject.getString("original_quote_number"));
                                        map.put(QUOTATION_ID, jsonObject.getString("quotation_id"));
                                        map.put(MODIFY_VALUE, jsonObject.getString("modify_value"));
                                        map.put(APPROVED_BYADMIN, jsonObject.getString("approved_amount_by_admin"));

                                        if (jsonObject.getString("comments").equalsIgnoreCase(null) || jsonObject.getString("comments").equalsIgnoreCase("null")) {
                                            map.put(MANAGER_REMARK, getResources().getString(R.string.no_comment));
                                        } else {
                                            map.put(MANAGER_REMARK, jsonObject.getString("comments"));
                                        }

                                        map.put(ORDER_STATUS, jsonObject.getString("aasm_state"));

                                        if (jsonObject.getString("aasm_state").equalsIgnoreCase("ordered") || jsonObject.getString("aasm_state").equalsIgnoreCase("lost")) {
                                            dbvoc.getDeleteTableorder_bycustomer_INN("Institutional Sales", jsonObject.getString("original_quote_number"));
                                            dbvoc.getDeleteTableorderproduct_bycustomer_INN("Institutional Sales", jsonObject.getString("original_quote_number"));
                                        }
                                        // product_value.add(jsonObject.getString("code"));
                                        dataArrayList.add(map);
                                    }

                                    for (int i = 0; i < quotation_products.length(); i++) {

                                        JSONObject jsonObject = quotation_products.getJSONObject(i);
                                        //  HashMap<String, String> map = new HashMap<String, String>();
                                        // map.put(ORDER_ID, jsonObject.getString("order_number"));
                                        //quotationId=jsonObject.getString("quotation_id");


                                        itemNumber = jsonObject.getString("item_number");
                                        loginDataBaseAdapter.Order_Status(jsonObject.getString("original_quote_number"), jsonObject.getString("item_name"), jsonObject.getString("amount"), jsonObject.getString("item_number"),jsonObject.getString("total_quantity"));
//                                        Global_Data.p_code.add(jsonObject.getString("item_number"));
//                                        Global_Data.p_mrp.add(jsonObject.getString("amount"));
//                                        Global_Data.p_amount.add(jsonObject.getString("amount"));
//                                        Global_Data.p_qty.add(jsonObject.getString("total_quantity"));
//		                                  if(jsonObject.getString("aasm_state").equalsIgnoreCase("approved"))
//		                                  {
//		                                	  dbvoc.getDeleteTableorderproduct_bycustomer_INN("Institutional Sales",jsonObject.getString("order_number"));
//		                                  }
                                    }


//                                    searchResults=new ArrayList<String>(product_value);
//                            		adapter = new ArrayAdapter<String>(Nearest_Customer.this, R.layout.filtertxt, product_value);
//                            		//adapter.notifyDataSetChanged();
//                            		listnearcust.setAdapter(adapter);

                                    customListAdapter = new Status_Adapter(Status_Activity.this, dataArrayList);
                                    customListAdapter.setCustomButtonListner(Status_Activity.this);
                                    status_list.setAdapter(customListAdapter);

//	                        	String[] Cable1Array = {"ARIAL BUNCHED CABLE","CONTROL CABLE","EXTRA HIGH VOLTAGE","INSTRUMENTS CABLES","POWER CABLES","SERVICE CABLES","SOLAR CABLES","TELEPHONE CABLES(ARM)","THERMOCOUPLE/COMPENSATING"};
//	                            adapter = new ArrayAdapter<String>(Status_Activity.this, R.layout.filtertxt, Cable1Array);
////	                    		//adapter.notifyDataSetChanged();
//	                            status_list.setAdapter(adapter);

                                    //dbvoc.update_stockChecks(s_code,s_stock);
                                    // Toast.makeText(Status_Activity.this, "List Sync Successfully.", Toast.LENGTH_LONG).show();
                                //    Global_Data.Custom_Toast(Status_Activity.this, getResources().getString(R.string.List_Sync_Successfully), "Yes");
//								  Toast toast = Toast.makeText(Status_Activity.this, getResources().getString(R.string.List_Sync_Successfully), Toast.LENGTH_LONG);
//								  toast.setGravity(Gravity.CENTER, 0, 0);
//								  toast.show();


                                    // Global_Val.STOCK_SERVICE_FLAG = "";
                                    dialog.dismiss();
                                    //finish();
                                }

                                //  finish();
                                // }

                                // output.setText(data);
                            } catch (JSONException e) {

                                e.printStackTrace();
                                dialog.dismiss();
                                Intent a = new Intent(Status_Activity.this, MainActivity.class);
                                startActivity(a);
                                finish();
                            }


                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  finish();
                            dialog.dismiss();
                            Intent a = new Intent(Status_Activity.this, MainActivity.class);
                            startActivity(a);
                            finish();
                        }
                        dialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Network Error",
//	                              Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(Status_Activity.this, "Network Error", "Yes");
//						  Toast toast = Toast.makeText(Status_Activity.this,  "Network Error", Toast.LENGTH_LONG);
//						  toast.setGravity(Gravity.CENTER, 0, 0);
//						  toast.show();

                        } else if (error instanceof AuthFailureError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Server AuthFailureError  Error",
//	                              Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(Status_Activity.this, "Server AuthFailureError  Error", "Yes");
//						  Toast toast = Toast.makeText(Status_Activity.this,  "Server AuthFailureError  Error", Toast.LENGTH_LONG);
//						  toast.setGravity(Gravity.CENTER, 0, 0);
//						  toast.show();
                        } else if (error instanceof ServerError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Server   Error",
//	                              Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(Status_Activity.this,
                                    getResources().getString(R.string.Server_Errors), "Yes");
//						  Toast toast = Toast.makeText(Status_Activity.this,
//								  getResources().getString(R.string.Server_Errors),
//								  Toast.LENGTH_LONG);
//						  toast.setGravity(Gravity.CENTER, 0, 0);
//						  toast.show();
                        } else if (error instanceof NetworkError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Network   Error",
//	                              Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(Status_Activity.this,
                                    "Network   Error", "Yes");

//						  Toast toast = Toast.makeText(Status_Activity.this,
//								  "Network   Error",
//								  Toast.LENGTH_LONG);
//						  toast.setGravity(Gravity.CENTER, 0, 0);
//						  toast.show();
                        } else if (error instanceof ParseError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "ParseError   Error",
//	                              Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(Status_Activity.this,
                                    "ParseError   Error", "Yes");

//						  Toast toast = Toast.makeText(Status_Activity.this,
//								  "ParseError   Error",
//								  Toast.LENGTH_LONG);
//						  toast.setGravity(Gravity.CENTER, 0, 0);
//						  toast.show();
                        } else {
                            //Toast.makeText(Status_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();

                            Global_Data.Custom_Toast(Status_Activity.this,
                                    error.getMessage(), "Yes");
//						  Toast toast = Toast.makeText(Status_Activity.this,
//								  error.getMessage(),
//								  Toast.LENGTH_LONG);
//						  toast.setGravity(Gravity.CENTER, 0, 0);
//						  toast.show();
                        }

                        dialog.dismiss();
                        Intent a = new Intent(Status_Activity.this, MainActivity.class);
                        startActivity(a);
                        finish();
                        // finish();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        stringRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void getListViewg(String order_id, String remark) {
        Global_Data.GLOVEL_ORDER_REJECT_FLAG = "";
        Global_Data.GLObalOrder_id = order_id;






        final Dialog dialognew = new Dialog(Status_Activity.this);
        dialognew.setCancelable(true);
        dialognew.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialognew.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //tell the Dialog to use the dialog.xml as it's layout description
        dialognew.setContentView(R.layout.status_click);

        list_statusclick = (ListView) dialognew
                .findViewById(R.id.list_statusclick);

        but_listclick = (Button) dialognew
                .findViewById(R.id.butback_listclick);

        butaprove_listclick = (Button) dialognew
                .findViewById(R.id.butapprove_listclick);

        spinner_click = (Spinner) dialognew
                .findViewById(R.id.spinner_addmore);

        spinner_modify = (Spinner) dialognew
                .findViewById(R.id.spinner_modify);

//	        but_addmore = (Button) dialognew
//                    .findViewById(R.id.but_addmore_click);

        but_addmore = (ImageView) dialognew.findViewById(R.id.btn_addmorenew);

        but_convert_order = (Button) dialognew
                .findViewById(R.id.but_convert_order);

        totalAmountLout = (LinearLayout) dialognew.findViewById(R.id.total_amount_lout);
        expectedLout = (LinearLayout) dialognew.findViewById(R.id.expected_lout);
        reasonLout = (LinearLayout) dialognew.findViewById(R.id.reason_lout);
        approvedLout = (LinearLayout) dialognew.findViewById(R.id.approved_lout);

        totalAmount = (TextView) dialognew.findViewById(R.id.total_amount);
        expectedAmount = (EditText) dialognew.findViewById(R.id.expected_amount);
        reason = (EditText) dialognew.findViewById(R.id.edit_reason);
        approvedAmount = (TextView) dialognew.findViewById(R.id.approved_amount);
        TextView txt_reason = (TextView) dialognew.findViewById(R.id.txt_reason);
        String sda = getResources().getString(R.string.Reason)+" :                           ";
        String reasonaa = "<font color='#111111'>" + sda + "</font>" + "<font color='#FF0000'>*</font>" + "<font color='#000000'></font>";
        txt_reason.setText(Html.fromHtml(reasonaa));

        if (modifyValue.length() > 0) {
            expectedAmount.setText(modifyValue);
        }
        if ( reasonvalues.equalsIgnoreCase("No Remark")){
            reason.setText("");
        }else {
            reason.setText(reasonvalues);
        }

//		else{
//			expectedAmount.setText(Global_Data.expectedAmountAtCreate);
//		}


        String[] customer_array = {getResources().getString(R.string.Select_Quotation_status), "ordered", "lost", "pending"};

//		if(expectedAmountEnteredStatus.equalsIgnoreCase("TRUE") && Global_Data.GLOvel_REMARK.equalsIgnoreCase("modified"))
//		{
//			Toast.makeText(Status_Activity.this, "You Can't Convert to Order", Toast.LENGTH_SHORT).show();
//		}else{
//
//		}
        String[] customer_array1 = {getResources().getString(R.string.Select_status), "Convert to Order", "Submit for Approval", "Reject"};

        //Global_Data.GLOvel_REMARK="approved";

        if (Global_Data.GLOvel_REMARK.equals("ordered") || Global_Data.GLOvel_REMARK.equals("lost")) {
            butaprove_listclick.setEnabled(false);
            butaprove_listclick.setBackgroundResource(android.R.drawable.btn_default);
            but_addmore.setEnabled(false);
            spinner_click.setEnabled(false);
            but_addmore.setBackgroundResource(android.R.drawable.btn_default);
            spinner_click.setBackgroundResource(android.R.drawable.btn_default);
        } else if (Global_Data.GLOvel_REMARK.equals("approved")) {
            but_addmore.setVisibility(View.INVISIBLE);
            butaprove_listclick.setText("CONVERT TO ORDER");
            spinner_click.setVisibility(View.INVISIBLE);
            //butaprove_listclick.setText(getResources().getString(R.string.convert_to_order));
        } else if (Global_Data.GLOvel_REMARK.equals("pending")) {
            //totalAmountLout.setVisibility(View.VISIBLE);
            expectedLout.setVisibility(View.VISIBLE);
            approvedLout.setVisibility(View.VISIBLE);
            reasonLout.setVisibility(View.VISIBLE);
            spinner_click.setVisibility(View.INVISIBLE);
//			   but_addmore.setVisibility(View.INVISIBLE);
//			   butaprove_listclick.setText(getResources().getString(R.string.convert_to_order));

//				expectedAmount.addTextChangedListener(new TextWatcher() {
//
//					@Override
//					public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//						//adapter.getFilter().filter(cs);
//						if(cs.length()>0)
//						{
//							butaprove_listclick.setText("SUBMIT FOR APPROVAL");
//							//Toast.makeText(getApplicationContext(),cs,Toast.LENGTH_LONG).show();
//						}else{
//							butaprove_listclick.setText("SUBMIT FOR APPROVAL");
//						}
//					}
//
//					@Override
//					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//						//Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
//					}
//
//					@Override
//					public void afterTextChanged(Editable arg0) {
//						//Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();
//					}
//				});
        } else if (Global_Data.GLOvel_REMARK.equals("modified")) {
            expectedLout.setVisibility(View.VISIBLE);
            approvedLout.setVisibility(View.VISIBLE);
            reasonLout.setVisibility(View.VISIBLE);
            spinner_click.setVisibility(View.INVISIBLE);
            butaprove_listclick.setText("SUBMIT");
            spinner_modify.setVisibility(View.VISIBLE);
        } else {
            but_addmore.setEnabled(true);
            but_addmore.setEnabled(true);
            butaprove_listclick.setEnabled(true);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                customer_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_click.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                customer_array1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_modify.setAdapter(adapter1);

        OrderDataList.clear();
        totalApproveAmount.clear();
        //description
        List<Local_Data> contcheck = dbvoc.getStatusOrderId_BYORDERPRODUCT(order_id);
//        if (contcheck.size() <= 0) {

        List<Local_Data> cont1 = dbvoc.getStatusOrderId(order_id);
        for (Local_Data cnt1 : cont1) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(ITEM_DESC, cnt1.getOrderStatus());
            orderAmount = cnt1.getOrderAmount();
            totalApproveAmount.add(cnt1.getOrderAmount());
            //itemNumber=cnt1.getorder_number();
            map.put(ITEM_AMOUNT, cnt1.getOrderAmount());
            map.put(ITEM_NUMBER, cnt1.getorder_number());
            //    map.put(ITEM_QUANTITY, cnt1.getItem_quantity());
            OrderDataList.add(map);


        }


//        } else {
//
//            List<Local_Data> cont1 = dbvoc.getStatusOrderId_BYORDERPRODUCT(order_id);
//            for (Local_Data cnt1 : cont1) {
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put(ITEM_DESC, cnt1.getOrderStatus());
//                orderAmount = cnt1.getOrderAmount();
//                totalApproveAmount.add(cnt1.getOrderAmount());
//                map.put(ITEM_AMOUNT, cnt1.getOrderAmount());
//                map.put(ITEM_NUMBER, cnt1.getorder_number());
//                OrderDataList.add(map);
//            }
//////
//////
//        }
        Double sum = 0.0;
        for (int m = 0; m < totalApproveAmount.size(); m++) {
            sum += Double.valueOf(totalApproveAmount.get(m));
        }

        //totalAmount.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(orderAmount));
        if (approveAmtByAdmin.equalsIgnoreCase("null") || approveAmtByAdmin.equalsIgnoreCase(null) || approveAmtByAdmin.equalsIgnoreCase("")) {
            updateSum(sum);
            //approvedAmount.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(orderAmount));
        } else {
            updateSum(Double.valueOf(approveAmtByAdmin));
            //approvedAmount.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(approveAmtByAdmin));
        }

//	        String[] Cable1Array = {"ARIAL BUNCHED CABLE","CONTROL CABLE","EXTRA HIGH VOLTAGE","INSTRUMENTS CABLES","POWER CABLES","SERVICE CABLES","SOLAR CABLES","TELEPHONE CABLES(ARM)","THERMOCOUPLE/COMPENSATING"};
//            adapter = new ArrayAdapter<String>(Status_Activity.this, R.layout.filtertxt, Cable1Array);
////    		//adapter.notifyDataSetChanged();
//            list_statusclick.setAdapter(adapter);

        OrderListAdapter = new OrderStatus_Adapter(Status_Activity.this, OrderDataList);
        //OrderListAdapter.setCustomButtonListner(Status_Activity.this);
        list_statusclick.setAdapter(OrderListAdapter);

//	        final Button cancel = (Button) dialognew
//	                .findViewById(R.id.cancel_list);

        //View_NearestCustomer(device_id, cust_str);

        but_addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global_Data.fromQuotation="";
                Global_Data.GLOVEL_ORDER_REJECT_FLAG = "TRUE";
                Intent i = new Intent(Status_Activity.this, NewOrderActivity.class);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                startActivity(i);
                Status_Activity.this.finish();
                dialognew.dismiss();
            }
        });

//		if(butaprove_listclick.getText().toString().equalsIgnoreCase("SUBMIT"))
//		{
//			if(spinner_modify.getSelectedItem().toString().trim().equalsIgnoreCase("Reject"))
//			{
//				expectedLout.setVisibility(View.GONE);
//				approvedLout.setVisibility(View.GONE);
//			}
//		}

        spinner_modify.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Reject")) {
                    but_addmore.setVisibility(View.INVISIBLE);
                    expectedLout.setVisibility(View.INVISIBLE);
                    approvedLout.setVisibility(View.INVISIBLE);
                    // do your stuff
                } else {
                    //but_addmore.setVisibility(View.VISIBLE);
                    but_addmore.setVisibility(View.INVISIBLE);
                    expectedLout.setVisibility(View.VISIBLE);
                    approvedLout.setVisibility(View.VISIBLE);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        butaprove_listclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global_Data.fromQuotation="";
                SharedPreferences spf = Status_Activity.this.getSharedPreferences("SimpleLogic", 0);
                String user_email = spf.getString("USER_EMAIL", null);

//					if(butaprove_listclick.getText().toString().equalsIgnoreCase("CONVERT TO ORDER"))
//					{
//
//						Toast.makeText(Status_Activity.this, "CONVERT", Toast.LENGTH_SHORT).show();
//					}else{
                //if (reason.length()>0 && expectedAmount.length()>0) {

//							Log.d("flag new", "flag new"+spinner_click.getSelectedItem().toString().trim());
//							ModifyService(device_id,Global_Data.GLOvel_USER_EMAIL,quotationId,reason.getText().toString().trim(),expectedAmount.getText().toString().trim());

                if (butaprove_listclick.getText().toString().equalsIgnoreCase("SUBMIT")) {
                    if (spinner_modify.getSelectedItem().toString().trim().equalsIgnoreCase("Convert to Order")) {
//							if(expectedAmountEnteredStatus.equalsIgnoreCase("TRUE") && Global_Data.GLOvel_REMARK.equalsIgnoreCase("modified"))
//							{
//								Toast.makeText(Status_Activity.this, "You Can't Convert to Order", Toast.LENGTH_SHORT).show();
//							}else{
//
//							}
                        AlertDialog alertDialog = new AlertDialog.Builder(Status_Activity.this).create(); //Read Update
                        alertDialog.setTitle(getResources().getString(R.string.Confirmation));
                        alertDialog.setMessage("Are you Sure for approved Amount?");
                        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Global_Data.p_code.clear();
                                Global_Data.p_mrp.clear();
                                Global_Data.p_amount.clear();
                                Global_Data.p_qty.clear();
                                List<Local_Data> cont5 = dbvoc.getStatusOrderId(original_id);
                                Global_Data.p_code.clear();
                                Global_Data.p_mrp.clear();
                                Global_Data.p_amount.clear();
                                Global_Data.p_qty.clear();
                                for (Local_Data cnt1 : cont5) {
//
                                    Global_Data.p_code.add(cnt1.getorder_number());
                                    Global_Data.p_mrp.add(cnt1.getOrderAmount());
                                    Global_Data.p_amount.add(cnt1.getOrderAmount());
                                    Global_Data.p_qty.add(cnt1.getItem_quantity());
                                }

                                Intent intent = new Intent(getApplicationContext(), QuotationCaptureSignature.class);
                                //intent.putParcelableArrayListExtra("productsList", dataOrder);
                                intent.putExtra("QUOTATION_ID", quotationId);
                                intent.putExtra("ORDER_AMOUNT", approvedAmount.getText().toString().trim());
                                intent.putExtra("ITEM_NO", itemNumber);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                //startActivityForResult(intent,SIGNATURE_ACTIVITY);
                                Status_Activity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                startActivity(intent);
                            }
                        });

                        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        });

                        alertDialog.setCancelable(false);
                        alertDialog.show();

                    } else if (spinner_modify.getSelectedItem().toString().trim().equalsIgnoreCase("Submit for Approval")) {
                        if (reason.getText().toString().length() > 0) {
                            if (TextUtils.isEmpty(expectedAmount.getText().toString())) {
                                Global_Data.Custom_Toast(Status_Activity.this, getResources().getString(R.string.please_enter_expected_amount), "Yes");
//									Toast toast = Toast.makeText(Status_Activity.this, getResources().getString(R.string.please_enter_expected_amount), Toast.LENGTH_SHORT);
//									toast.setGravity(Gravity.CENTER, 0, 0);
//									toast.show();
                                //ModifyService(device_id,Global_Data.GLOvel_USER_EMAIL,quotationId,reason.getText().toString().trim(),expectedAmount.getText().toString().trim(),dialognew);
                            } else {
                                expectAmt = new Double(expectedAmount.getText().toString());
                                totalAmt = new Double(approvedAmount.getText().toString());

                                if (expectAmt > totalAmt) {
                                 //   Toast.makeText(getApplicationContext(), "Expected Amount Should be less than Amount", Toast.LENGTH_LONG).show();
                                    Global_Data.Custom_Toast(getApplicationContext(), "Expected Amount Should be less than Amount","");
                                } else {
                                    //expectedAmountEnteredStatus="TRUE";
                                    ModifyService(device_id, user_email, quotationId, reason.getText().toString().trim(), expectedAmount.getText().toString().trim(), dialognew);
                                }
                            }
                        } else {
//                            Toast toast = Toast.makeText(Status_Activity.this, getResources().getString(R.string.please_enter_reason), Toast.LENGTH_SHORT);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Status_Activity.this, getResources().getString(R.string.please_enter_reason), "yes");
                        }
                    } else if (spinner_modify.getSelectedItem().toString().trim().equalsIgnoreCase("Reject")) {
                        if (reason.getText().toString().length() > 0) {
                            Global_Data.reject = "yes";
                            getServices.SYNCORDER_BYCustomerINSTI_NEW(Status_Activity.this, "lost");
                        } else {
//                            Toast toast = Toast.makeText(Status_Activity.this, getResources().getString(R.string.please_enter_reason), Toast.LENGTH_SHORT);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Status_Activity.this, getResources().getString(R.string.please_enter_reason),"yes");
                        }

                    } else {
//                        Toast toast = Toast.makeText(Status_Activity.this, "Please Select Status", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(Status_Activity.this, "Please Select Status", "yes");
                    }

                } else {
                    if (Global_Data.GLOvel_REMARK.equals("modified") || Global_Data.GLOvel_REMARK.equals("pending")) {

                        if (reason.getText().toString().length() > 0) {
                            if (TextUtils.isEmpty(expectedAmount.getText().toString())) {
                                ModifyService(device_id, user_email, quotationId, reason.getText().toString().trim(), expectedAmount.getText().toString().trim(), dialognew);
                            } else {
                                expectAmt = new Double(expectedAmount.getText().toString());
                                totalAmt = new Double(approvedAmount.getText().toString());

                                if (expectAmt > totalAmt) {
                                  //  Toast.makeText(getApplicationContext(), "Expected Amount Should be less than Amount", Toast.LENGTH_LONG).show();
                                    Global_Data.Custom_Toast(getApplicationContext(), "Expected Amount Should be less than Amount","");
                                } else {
                                    ModifyService(device_id, user_email, quotationId, reason.getText().toString().trim(), expectedAmount.getText().toString().trim(), dialognew);
                                }
                            }
                        } else {
//                            Toast toast = Toast.makeText(Status_Activity.this, getResources().getString(R.string.please_enter_reason), Toast.LENGTH_SHORT);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Status_Activity.this, getResources().getString(R.string.please_enter_reason),"yes");
                        }

//						if(reason.getText().toString().length()>0)
//						{
//							ModifyService(device_id,Global_Data.GLOvel_USER_EMAIL,quotationId,reason.getText().toString().trim(),expectedAmount.getText().toString().trim());
//
//						}else{
//							Toast toast = Toast.makeText(Status_Activity.this, getResources().getString(R.string.please_enter_reason), Toast.LENGTH_SHORT);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
//						}
                    } else {

                        if (Global_Data.GLOvel_REMARK.equals("approved")) {
                            Global_Data.p_code.clear();
                            Global_Data.p_mrp.clear();
                            Global_Data.p_amount.clear();
                            Global_Data.p_qty.clear();
                            List<Local_Data> cont5 = dbvoc.getStatusOrderId(original_id);
                            Global_Data.p_code.clear();
                            Global_Data.p_mrp.clear();
                            Global_Data.p_amount.clear();
                            Global_Data.p_qty.clear();
                            for (Local_Data cnt1 : cont5) {
//
                                Global_Data.p_code.add(cnt1.getorder_number());
                                Global_Data.p_mrp.add(cnt1.getOrderAmount());
                                Global_Data.p_amount.add(cnt1.getOrderAmount());
                                Global_Data.p_qty.add(cnt1.getItem_quantity());
                            }

                            Intent intent = new Intent(getApplicationContext(), QuotationCaptureSignature.class);
                            //intent.putParcelableArrayListExtra("productsList", dataOrder);
                            intent.putExtra("QUOTATION_ID", quotationId);
                            intent.putExtra("ORDER_AMOUNT", expectedAmount.getText().toString().trim());
                            intent.putExtra("ITEM_NO", itemNumber);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            //startActivityForResult(intent,SIGNATURE_ACTIVITY);
                            Status_Activity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            startActivity(intent);
                        }

//						if(spinner_click.getSelectedItem().toString().trim().equalsIgnoreCase("Select Quotation status"))
//						{
//							getServices.SYNCORDER_BYCustomerINSTI_NEW(Status_Activity.this,spinner_click.getSelectedItem().toString().trim());
//							dialognew.dismiss();
//
//						}else if(spinner_click.getSelectedItem().toString().trim().equalsIgnoreCase("approved")){
//							OrderService(Global_Data.GLOvel_USER_EMAIL,quotationId,spinner_click.getSelectedItem().toString().trim());
//							dialognew.dismiss();
//						}
                        //View_NearestCustomer(device_id, Global_Data.cust_str);
                    }
                }


//					if(spinner_click.getSelectedItem().toString().trim().equalsIgnoreCase("Select Quotation status"))
//					{
//						getServices.SYNCORDER_BYCustomerINSTI_NEW(Status_Activity.this,spinner_click.getSelectedItem().toString().trim());
//						//View_NearestCustomer(device_id, Global_Data.cust_str);
//						dialognew.dismiss();
//					}else{
//						getServices.SYNCORDER_BYCustomerINSTI_NEW(Status_Activity.this,"");
//						//View_NearestCustomer(device_id, Global_Data.cust_str);
//						dialognew.dismiss();
//					}


//						}
//						else
//						{
//							Toast toast = Toast.makeText(Status_Activity.this, getResources().getString(R.string.please_enter_reason), Toast.LENGTH_SHORT);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
//						}
                //}
            }
        });

        but_listclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global_Data.fromQuotation="";
                View_NearestCustomer(device_id, Global_Data.cust_str);
                dialognew.dismiss();

            }
        });

        dialognew.show();
    }

    public void getListViewnew(final String value1, String value2) {
        final Dialog dialognew = new Dialog(Status_Activity.this);
        dialognew.setCancelable(false);
        dialognew.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //tell the Dialog to use the dialog.xml as it's layout description
        dialognew.setContentView(R.layout.customer_address_dialog);

        final EditText userInput = (EditText) dialognew
                .findViewById(R.id.update_textdialog);

        final EditText distance = (EditText) dialognew
                .findViewById(R.id.cu_distance);


        // item_description.setText("Address");


        final Button Submit = (Button) dialognew
                .findViewById(R.id.update_textdialogclick);

        final Button update_cancel = (Button) dialognew
                .findViewById(R.id.update_cancel);
        update_cancel.setVisibility(View.GONE);

        Submit.setText(getResources().getString(R.string.OK_Button_label));

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(value1)) {
            userInput.setText(getResources().getString(R.string.Address) + value1);
        }

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(value2)) {
            distance.setText(getResources().getString(R.string.Distance_s) + value2);
        }

//	        final Button cancel = (Button) dialognew
//	                .findViewById(R.id.cancel);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialognew.dismiss();

            }
        });

//	        update_cancel.setOnClickListener(new View.OnClickListener() {
//	            @Override
//	            public void onClick(View v) {
//
////
//
//	                    dialognew.dismiss();
//
//
//	            }
        // });

//	        cancel.setOnClickListener(new View.OnClickListener() {

//	            @Override
//	            public void onClick(View v) {
//	                dialognew.dismiss();
        //
        //
//	            }
//	        });

        dialognew.show();
    }


    public void ModifyService(String device_id1, String email, String quotation_id, String comment, String modify_value, final Dialog dialognew) {

        SharedPreferences pref_devid = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
        device_id = pref_devid.getString("devid", "");

        String domain = "";

        dialog = new ProgressDialog(Status_Activity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(getResources().getString(R.string.Please_Wait));
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
        String Cust_domain = sp.getString("Cust_Service_Url", "");
        String service_url = Cust_domain + "metal/api/v1/";
        domain = service_url;

        //Log.d("url", "url customer"+domain+"quotations/send_quotation_status?customer_code="+cust_id+"&imei_no="+device_id+"&email="+Global_Data.GLOvel_USER_EMAIL);

        // Global_Val global_Val = new Global_Val();
//        if(URL.equalsIgnoreCase(null) || URL.equalsIgnoreCase("null") || URL.equalsIgnoreCase("") || URL.equalsIgnoreCase(" ")) {
//            domain = context.getResources().getString(R.string.service_domain);
//        }
//        else
//        {
//            domain = URL.toString();
//        }


        StringRequest stringRequest = null;

        //String url="http://2d28006e.ngrok.io/metal/api/v1/quotations/modify_quotation?imei_no=359988060274084&email=jagadalerupchand5399@gmail.com&quotation_id=50&comment=ffffss&modify_value=4520";

        //stringRequest = new StringRequest(Method.POST,url,


        try {

            stringRequest = new StringRequest(Method.POST, domain + "quotations/modify_quotation?email=" + email + "&quotation_id=" + quotation_id + "&comment=" + URLEncoder.encode(comment, "UTF-8") + "&modify_value=" + modify_value,

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
                                    } else {
                                        response_result = "data";
                                    }

                                    if (response_result.equalsIgnoreCase("product not found")) {
                                        //Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG).show();

//                                        Toast toast = Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();
Global_Data.Custom_Toast(Status_Activity.this, response_result,"yes");

                                        Intent a = new Intent(Status_Activity.this, MainActivity.class);
                                        startActivity(a);
                                        finish();
                                    } else if (response_result.equalsIgnoreCase("Device not registered")) {

                                        // Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG).show();

//                                        Toast toast = Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();
                                        Global_Data.Custom_Toast(Status_Activity.this, response_result,"yes");

                                    } else {
//                                        Toast toast = Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();
                                        Global_Data.Custom_Toast(Status_Activity.this, response_result,"yes");

                                        dialog.dismiss();

                                        View_NearestCustomer(device_id, Global_Data.cust_str);
                                        dialognew.dismiss();
                                    }
//								else
//								if(json.getJSONArray("quotations").length()<= 0) {
//
//									// Toast.makeText(Status_Activity.this, "Quotations Not Found for Customer", Toast.LENGTH_LONG).show();
//
//									Toast toast = Toast.makeText(Status_Activity.this, getResources().getString(R.string.Quotations_Not_Found), Toast.LENGTH_LONG);
//									toast.setGravity(Gravity.CENTER, 0, 0);
//									toast.show();
//
//									Intent a = new Intent(Status_Activity.this,MainActivity.class);
//									startActivity(a);
//									finish();
//
//								}
//								else {
//
//
//
//									// Log.d("customers", "customers" + customers.toString());
//									// Log.d("devices", "devices" + devices.toString());
//
//
//									dataArrayList.clear();
//
//
//
//
//									Toast toast = Toast.makeText(Status_Activity.this, getResources().getString(R.string.List_Sync_Successfully), Toast.LENGTH_LONG);
//									toast.setGravity(Gravity.CENTER, 0, 0);
//									toast.show();
//
//
//
//									// Global_Val.STOCK_SERVICE_FLAG = "";
//									dialog.dismiss();
//									//finish();
//								}

                                    //  finish();
                                    // }

                                    // output.setText(data);
                                } catch (JSONException e) {

                                    e.printStackTrace();
                                    dialog.dismiss();
                                    Intent a = new Intent(Status_Activity.this, MainActivity.class);
                                    startActivity(a);
                                    finish();
                                }


                                dialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //  finish();
                                dialog.dismiss();
                                Intent a = new Intent(Status_Activity.this, MainActivity.class);
                                startActivity(a);
                                finish();
                            }
                            dialog.dismiss();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Network Error",
//	                              Toast.LENGTH_LONG).show();
//                                Toast toast = Toast.makeText(Status_Activity.this, "Network Error", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

                                Global_Data.Custom_Toast(Status_Activity.this, "Network Error","yes");

                            } else if (error instanceof AuthFailureError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Server AuthFailureError  Error",
//	                              Toast.LENGTH_LONG).show();
//                                Toast toast = Toast.makeText(Status_Activity.this, "Server AuthFailureError  Error", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(Status_Activity.this, "Server AuthFailureError  Error","yes");
                            } else if (error instanceof ServerError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Server   Error",
//	                              Toast.LENGTH_LONG).show();
//                                Toast toast = Toast.makeText(Status_Activity.this,
//                                        getResources().getString(R.string.Server_Errors),
//                                        Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(Status_Activity.this,
                                        getResources().getString(R.string.Server_Errors),"yes");
                            } else if (error instanceof NetworkError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Network   Error",
//	                              Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(Status_Activity.this,
//                                        "Network   Error",
//                                        Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(Status_Activity.this,
                                        "Network   Error","yes");
                            } else if (error instanceof ParseError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "ParseError   Error",
//	                              Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(Status_Activity.this,
//                                        "ParseError   Error",
//                                        Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(Status_Activity.this,
                                        "ParseError   Error","yes");
                            } else {
                                //Toast.makeText(Status_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(Status_Activity.this,
//                                        error.getMessage(),
//                                        Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(Status_Activity.this,
                                        error.getMessage(),"yes");
                            }

                            dialog.dismiss();
                            Intent a = new Intent(Status_Activity.this, MainActivity.class);
                            startActivity(a);
                            finish();
                            // finish();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        stringRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void OrderService(String email, String quotation_id, String aasm_state) {

        SharedPreferences pref_devid = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
        device_id = pref_devid.getString("devid", "");

        String domain = "";

        dialog = new ProgressDialog(Status_Activity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(getResources().getString(R.string.Please_Wait));
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
        String Cust_domain = sp.getString("Cust_Service_Url", "");
        String service_url = Cust_domain + "metal/api/v1/";
        domain = service_url;

        StringRequest stringRequest = null;

        stringRequest = new StringRequest(Method.POST, domain + "quotations/after_approve_action_on_quotation?email=" + email + "&quotation_id=" + quotation_id + "&aasm_state=" + aasm_state,

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
                                } else {
                                    response_result = "data";
                                }

                                if (response_result.equalsIgnoreCase("product not found")) {
                                    //Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG).show();

//                                    Toast toast = Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
Global_Data.Custom_Toast(Status_Activity.this, response_result, "yes");

                                    Intent a = new Intent(Status_Activity.this, MainActivity.class);
                                    startActivity(a);
                                    finish();
                                } else if (response_result.equalsIgnoreCase("Device not registered")) {

                                    // Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG).show();

//                                    Toast toast = Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(Status_Activity.this, response_result,"yes");

                                } else {
//                                    Toast toast = Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(Status_Activity.this, response_result,"yes");

                                    dialog.dismiss();
                                }
//								else
//								if(json.getJSONArray("quotations").length()<= 0) {
//
//									// Toast.makeText(Status_Activity.this, "Quotations Not Found for Customer", Toast.LENGTH_LONG).show();
//
//									Toast toast = Toast.makeText(Status_Activity.this, getResources().getString(R.string.Quotations_Not_Found), Toast.LENGTH_LONG);
//									toast.setGravity(Gravity.CENTER, 0, 0);
//									toast.show();
//
//									Intent a = new Intent(Status_Activity.this,MainActivity.class);
//									startActivity(a);
//									finish();
//
//								}
//								else {
//
//
//
//									// Log.d("customers", "customers" + customers.toString());
//									// Log.d("devices", "devices" + devices.toString());
//
//
//									dataArrayList.clear();
//
//
//
//
//									Toast toast = Toast.makeText(Status_Activity.this, getResources().getString(R.string.List_Sync_Successfully), Toast.LENGTH_LONG);
//									toast.setGravity(Gravity.CENTER, 0, 0);
//									toast.show();
//
//
//
//									// Global_Val.STOCK_SERVICE_FLAG = "";
//									dialog.dismiss();
//									//finish();
//								}

                                //  finish();
                                // }

                                // output.setText(data);
                            } catch (JSONException e) {

                                e.printStackTrace();
                                dialog.dismiss();
                                Intent a = new Intent(Status_Activity.this, MainActivity.class);
                                startActivity(a);
                                finish();
                            }


                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  finish();
                            dialog.dismiss();
                            Intent a = new Intent(Status_Activity.this, MainActivity.class);
                            startActivity(a);
                            finish();
                        }
                        dialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Network Error",
//	                              Toast.LENGTH_LONG).show();
//                            Toast toast = Toast.makeText(Status_Activity.this, "Network Error", Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Status_Activity.this, "Network Error","yes");

                        } else if (error instanceof AuthFailureError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Server AuthFailureError  Error",
//	                              Toast.LENGTH_LONG).show();
//                            Toast toast = Toast.makeText(Status_Activity.this, "Server AuthFailureError  Error", Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Status_Activity.this, "Server AuthFailureError  Error","yes");
                        } else if (error instanceof ServerError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Server   Error",
//	                              Toast.LENGTH_LONG).show();
//                            Toast toast = Toast.makeText(Status_Activity.this,
//                                    getResources().getString(R.string.Server_Errors),
//                                    Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Status_Activity.this,
                                    getResources().getString(R.string.Server_Errors),"yes");
                        } else if (error instanceof NetworkError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Network   Error",
//	                              Toast.LENGTH_LONG).show();
//
//                            Toast toast = Toast.makeText(Status_Activity.this,
//                                    "Network   Error",
//                                    Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Status_Activity.this,
                                    "Network   Error","yes");
                        } else if (error instanceof ParseError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "ParseError   Error",
//	                              Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(Status_Activity.this,
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Status_Activity.this,
                                    "ParseError   Error","yes");
                        } else {
                            //Toast.makeText(Status_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(Status_Activity.this,
//                                    error.getMessage(),
//                                    Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Status_Activity.this,
                                    error.getMessage(),"yes");
                        }

                        dialog.dismiss();
                        Intent a = new Intent(Status_Activity.this, MainActivity.class);
                        startActivity(a);
                        finish();
                        // finish();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        stringRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }


    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub

    }


    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			onBackPressed();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

    public void onButtonClickListner(int position, String value1, String value2, View v) {
//        Toast.makeText(DeleteOrder.this, "Button click " + value,
//                Toast.LENGTH_SHORT).show();

        int pos = position;

        String value = value1;
        String valuen = value2;

        getListViewnew(value, valuen);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        this.finish();
    }

    public static void updateSum(Double sum) {
        approvedAmount.setText("" + sum);

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
                String targetNew = "";
                SharedPreferences sp = Status_Activity.this.getSharedPreferences("SimpleLogic", 0);
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
}