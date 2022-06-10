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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msimplelogic.App.AppController;

import com.msimplelogic.activities.R;

import com.msimplelogic.adapter.Order_Delivery_Adapter;
import com.msimplelogic.model.Order_Delivery_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cpm.simplelogic.helper.DividerItemDecoration;
import cpm.simplelogic.helper.MyOrder_totalInterface;

public class Order_Delivery extends Activity implements MyOrder_totalInterface {
    Double pp = 0.0;
    private ArrayList<Order_Delivery_Model> order_delivery_models;
    String q_check = "";
    TextView dtxttotalPreview;
    private ProgressDialog pDialog;
    private Order_Delivery_Adapter mAdapter;
    private RecyclerView recyclerView;
    Button d_update, d_delivered, d_cancel;
    private ArrayList<String> p_id = new ArrayList<String>();
    private ArrayList<String> p_name = new ArrayList<String>();
    private ArrayList<String> o_name = new ArrayList<String>();
    private ArrayList<String> o_quantity = new ArrayList<String>();
    private ArrayList<String> stock_quantity = new ArrayList<String>();
    private ArrayList<String> d_quantity = new ArrayList<String>();
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    LoginDataBaseAdapter loginDataBaseAdapter;
    String ttl_price;
    String response_result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.order_delivery);

        d_update = (Button) findViewById(R.id.d_update);
        d_delivered = (Button) findViewById(R.id.d_delivered);
        d_cancel = (Button) findViewById(R.id.d_cancel);
        dtxttotalPreview = (TextView) findViewById(R.id.dtxttotalPreview);

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();


        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        ttl_price = spf1.getString("var_total_price", "");
        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            LayoutInflater mInflater = LayoutInflater.from(this);
            Intent i = getIntent();
            String name = i.getStringExtra("retialer");
            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText("Deliver Order");

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = Order_Delivery.this.getSharedPreferences("SimpleLogic", 0);

//	       if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//	       	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//			}

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
            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
//	       	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
                todaysTarget.setText("Today's Target Acheived");
            }

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        recyclerView = (RecyclerView) findViewById(R.id.drecycler_view);

        pDialog = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);


        order_delivery_models = new ArrayList<>();


        mAdapter = new Order_Delivery_Adapter(getApplicationContext(), order_delivery_models, Order_Delivery.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        Online_Order_Data();


        d_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {


            }
        });

        d_delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // Toast.makeText(Order_Delivery.this, "click", Toast.LENGTH_SHORT).show();
                new Order_Delivery.Varientsave().execute();
            }
        });

        d_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (Global_Data.Order_Delivery_hashmap.size() > 0) {

                    AlertDialog alertDialog = new AlertDialog.Builder(Order_Delivery.this).create(); //Read Update
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage("Are you sure you want to discard the entered quantity?");
                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            p_id.clear();
                            p_name.clear();
                            o_name.clear();
                            o_quantity.clear();
                            stock_quantity.clear();
                            d_quantity.clear();

                            Global_Data.Order_Delivery_hashmap.clear();

                            Intent i = new Intent(Order_Delivery.this, Order.class);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    });

                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });


                    alertDialog.show();


                } else {
                    p_id.clear();
                    p_name.clear();
                    o_name.clear();
                    o_quantity.clear();
                    stock_quantity.clear();
                    d_quantity.clear();
                    Global_Data.Order_Delivery_hashmap.clear();
                    Intent i = new Intent(Order_Delivery.this, Order.class);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }

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
        //Global_Data.order_delivery_models.clear();
        if (Global_Data.Order_Delivery_hashmap.size() > 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(Order_Delivery.this).create(); //Read Update
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Are you sure you want to discard the entered quantity?");
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    p_id.clear();
                    p_name.clear();
                    o_name.clear();
                    o_quantity.clear();
                    stock_quantity.clear();
                    d_quantity.clear();
                    Global_Data.Order_Delivery_hashmap.clear();

                    Intent i = new Intent(Order_Delivery.this, Order.class);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            });

            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });


            alertDialog.show();


        } else {

            p_id.clear();
            p_name.clear();
            o_name.clear();
            o_quantity.clear();
            stock_quantity.clear();
            d_quantity.clear();
            Global_Data.Order_Delivery_hashmap.clear();
            Intent i = new Intent(Order_Delivery.this, Order.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }

    public void Online_Order_Data() {

        //String device_id = "354733074410810";
        String domain = "";
        String domain1 = "";

        SharedPreferences spf = Order_Delivery.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        //dialog = new ProgressDialog(Video_Main_List.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pDialog.setMessage("Please wait....");
        pDialog.setTitle("Metal");
        pDialog.setCancelable(false);
        pDialog.show();

        String service_url = "";
        SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
        String Cust_domain = sp.getString("Cust_Service_Url", "");
        String service_url1 = Cust_domain + "metal/api/v1/";
        String device_id = sp.getString("devid", "");
        domain = service_url1;

        domain1 = Cust_domain;

        try {
            service_url = domain + "van_sales?imei_no=" + device_id + "&email=" + URLEncoder.encode(user_email, "UTF-8") + "&customer_code=" + Global_Data.GLOvel_CUSTOMER_ID;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //service_url = service_url.replaceAll(" ", "%20");


        Log.d("Server url", "van_sales Server url" + service_url);

        StringRequest stringRequest = null;
        stringRequest = new StringRequest(service_url,
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

//                                    Toast toast = Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(getApplicationContext(), response_result,"yes");

                                    Intent launch = new Intent(Order_Delivery.this, Order.class);
                                    startActivity(launch);
                                    finish();
                                } else {
                                    response_result = "data";


                                    JSONArray products = json.getJSONArray("products");
                                    // Global_Data.products = json.getJSONArray("products");

                                    Log.i("volley", "response reg products Length: " + products.length());

                                    Log.d("users", "products" + products.toString());

                                    order_delivery_models.clear();
                                    pp = 0.0;

                                    if (products.length() > 0) {
                                        for (int i = 0; i < products.length(); i++) {
                                            JSONObject object = products.getJSONObject(i);
                                            Order_Delivery_Model order_delivery_model_sub = new Order_Delivery_Model();
                                            order_delivery_model_sub.setBalance_stock_value(object.getString("balance_stock"));
                                            // order_delivery_model_sub.setDelivered_qty_value(object.getString("code"));
                                            order_delivery_model_sub.setDitem_id1(object.getString("product_id"));
                                            order_delivery_model_sub.setOrder_name_value(object.getString("order_number"));
                                            order_delivery_model_sub.setProduct_named(object.getString("product_name"));
                                            order_delivery_model_sub.setOrder_qty_value(object.getString("order_quntity"));


                                            order_delivery_models.add(order_delivery_model_sub);

                                        }


                                        pDialog.dismiss();
                                        mAdapter.notifyDataSetChanged();


//                                   if (ttl_price.length() > 0) {
//                                            dtxttotalPreview.setText(ttl_price + ":" + pp);
//                                        } else {
//
//                                            dtxttotalPreview.setText("Total : " + pp);
//                                        }

                                    } else {
                                        pDialog.dismiss();
                                        // Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG).show();

//                                        Toast toast = Toast.makeText(getApplicationContext(), "Order Not Found.", Toast.LENGTH_LONG);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();
Global_Data.Custom_Toast(getApplicationContext(), "Order Not Found.","yes");
                                        Intent launch = new Intent(Order_Delivery.this, Order.class);
                                        startActivity(launch);
                                        finish();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
//                                Toast toast = Toast.makeText(Order_Delivery.this,
//                                        "Service Error",
//                                        Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(Order_Delivery.this,
                                        "Service Error","yes");
                                Intent launch = new Intent(Order_Delivery.this, Order.class);
                                startActivity(launch);
                                finish();
                                pDialog.hide();
                            }
                            pDialog.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  finish();
                            pDialog.dismiss();
                        }
                        pDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(Image_Gellary.this,
//                                    "Network Error",
//                                    Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(Order_Delivery.this,
//                                    "Network Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(Order_Delivery.this,
                                    "Network Error","yes");
                        } else if (error instanceof AuthFailureError) {

//                            Toast toast = Toast.makeText(Order_Delivery.this,
//                                    "Server AuthFailureError  Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(Order_Delivery.this,
                                    "Server AuthFailureError  Error","yes");
                        } else if (error instanceof ServerError) {

//                            Toast toast = Toast.makeText(Order_Delivery.this,
//                                    getResources().getString(R.string.Server_Errors),
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(Order_Delivery.this,
                                    getResources().getString(R.string.Server_Errors),"yes");
                        } else if (error instanceof NetworkError) {

//                            Toast toast = Toast.makeText(Order_Delivery.this,
//                                    "Network   Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(Order_Delivery.this,
                                    "Network   Error","yes");
                        } else if (error instanceof ParseError) {


//                            Toast toast = Toast.makeText(Order_Delivery.this,
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                            Global_Data.Custom_Toast(Order_Delivery.this,
                                    "ParseError   Error","yes");
                        } else {
                            // Toast.makeText(Image_Gellary.this, error.getMessage(), Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(Order_Delivery.this, error.getMessage(), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Order_Delivery.this, error.getMessage(),"yes");
                        }
                        Intent launch = new Intent(Order_Delivery.this, Order.class);
                        startActivity(launch);
                        finish();
                        pDialog.dismiss();
                        // finish();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Order_Delivery.this);

        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        stringRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        //requestQueue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void onButtonClickListner(int position) {
        int pos = position;
//        String value = value1;
//        String valuen = value2;
//        String name = value3;

        try {
            getMethod();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getMethod() {
        final Dialog openDialog = new Dialog(Order_Delivery.this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setCancelable(false);
        openDialog.setContentView(R.layout.catalogue_dialog);

        ImageView dialogClose = (ImageView) openDialog.findViewById(R.id.close_btn);

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                openDialog.dismiss();
            }
        });

        openDialog.show();
    }

    @Override
    public void Calculate_total_forone(String flag) {

    }

    private class Varientsave extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... response) {

            try {

                Order_Delivery.this.runOnUiThread(new Runnable() {
                    public void run() {
                        View parentView = null;

                        if (!(Global_Data.Order_Delivery_hashmap.isEmpty())) {

                            try {
                                for (Object name : Global_Data.Order_Delivery_hashmap.keySet()) {

                                    Object key = name.toString();
                                    Object value = Global_Data.Order_Delivery_hashmap.get(name);
                                    Log.d("KEY", "Key: " + key + " Value: " + value);
                                    JSONObject item = new JSONObject();

                                    String key_array[] = String.valueOf(key).split("&");
                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value))) {

                                        String key_value_array[] = String.valueOf(value).split("%");

                                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(key_value_array[0])) {


                                            q_check = "yes";
                                            p_id.add(key_array[1]);
                                            d_quantity.add(key_value_array[0]);
                                            o_name.add(key_value_array[1]);

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
                // dialog.dismiss();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            //dialog.dismiss();

            if (!p_id.isEmpty() && q_check.equalsIgnoreCase("yes")) {

                System.gc();
                String reason_code = "";
                try {

                    JsonObjectRequest jsObjRequest = null;
                    try {


                        //String device_id = "354733074410810";
                        String domain = "";
                        String domain1 = "";
                        Order_Delivery.this.runOnUiThread(new Runnable() {
                            public void run() {

                                pDialog.setMessage("Please wait....");
                                pDialog.setTitle("Metal");
                                pDialog.setCancelable(false);
                                pDialog.show();
                            }
                        });


                        String service_url = "";
                        SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
                        String Cust_domain = sp.getString("Cust_Service_Url", "");
                        String service_url1 = Cust_domain + "metal/api/v1/";
                        String device_id = sp.getString("devid", "");
                        domain = service_url1;

                        SharedPreferences spf = Order_Delivery.this.getSharedPreferences("SimpleLogic", 0);
                        String user_email = spf.getString("USER_EMAIL",null);

                        service_url = domain + "van_sales/update_van_products_stocks?imei_no=" + device_id + "&email=" + URLEncoder.encode(user_email, "UTF-8") + "&customer_code=" + Global_Data.GLOvel_CUSTOMER_ID;

                        Log.d("order_delivery ", service_url);

                        JSONObject SINOBJECT = new JSONObject();

                        for (int i = 0; i < p_id.size(); i++) {
                            JSONObject SINARR = new JSONObject();
                            SINARR.put("delivered_qty", d_quantity.get(i));
                            SINARR.put("order_number", o_name.get(i));
                            SINARR.put("product_id", p_id.get(i));

                            SINOBJECT.put("order_delivery", SINARR);
                        }


                        Log.d("order_delivery ", SINOBJECT.toString());

                        jsObjRequest = new JsonObjectRequest(Request.Method.POST, service_url, SINOBJECT, new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("volley", "response: " + response);

                                Log.d("jV", "JV length" + response.length());
                                try {

                                    response_result = "";
                                    if (response.has("result")) {
                                        response_result = response.getString("result");
                                    } else {
                                        response_result = "data";
                                    }


                                    if (response_result.equalsIgnoreCase("updated van sales successfully.")) {


                                        Order_Delivery.this.runOnUiThread(new Runnable() {
                                            public void run() {

                                                pDialog.dismiss();

                                                Global_Data.Order_Delivery_hashmap.clear();

//                                                Toast toast = Toast.makeText(Order_Delivery.this, response_result, Toast.LENGTH_LONG);
//                                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                                toast.show();
Global_Data.Custom_Toast(Order_Delivery.this, response_result,"yes");
                                                Intent i = new Intent(Order_Delivery.this, Order.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });


                                    } else if (response_result.equalsIgnoreCase("Something went wrong.")) {


                                        Order_Delivery.this.runOnUiThread(new Runnable() {
                                            public void run() {

                                                pDialog.dismiss();
//                                                Toast toast = Toast.makeText(Order_Delivery.this, response_result, Toast.LENGTH_SHORT);
//                                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                                toast.show();
                                                Global_Data.Custom_Toast(Order_Delivery.this, response_result,"yes");
                                            }
                                        });

                                    } else {
                                        Order_Delivery.this.runOnUiThread(new Runnable() {
                                            public void run() {

                                                pDialog.dismiss();
//                                                Toast toast = Toast.makeText(Order_Delivery.this, response_result, Toast.LENGTH_SHORT);
//                                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                                toast.show();
                                                Global_Data.Custom_Toast(Order_Delivery.this, response_result, "yes");
                                            }
                                        });

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    Order_Delivery.this.runOnUiThread(new Runnable() {
                                        public void run() {

                                            pDialog.dismiss();
                                        }
                                    });

                                }


                                Order_Delivery.this.runOnUiThread(new Runnable() {
                                    public void run() {

                                        pDialog.dismiss();
                                    }
                                });


                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(final VolleyError error) {
                                //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                                    Order_Delivery.this.runOnUiThread(new Runnable() {
                                        public void run() {

//                                            Toast.makeText(Order_Delivery.this,
//                                                    "your internet connection is not working, saving locally. Please sync when Internet is available",
//                                                    Toast.LENGTH_LONG).show();
                                            Global_Data.Custom_Toast(Order_Delivery.this,
                                                    "your internet connection is not working, saving locally. Please sync when Internet is available","");
                                        }
                                    });

                                } else if (error instanceof AuthFailureError) {

                                    Order_Delivery.this.runOnUiThread(new Runnable() {
                                        public void run() {

//                                            Toast.makeText(Order_Delivery.this,
//                                                    "Server AuthFailureError  Error",
//                                                    Toast.LENGTH_LONG).show();
                                            Global_Data.Custom_Toast(Order_Delivery.this,
                                                    "Server AuthFailureError  Error","yes");
                                        }
                                    });

                                } else if (error instanceof ServerError) {

                                    Order_Delivery.this.runOnUiThread(new Runnable() {
                                        public void run() {

//                                            Toast.makeText(Order_Delivery.this,
//                                                    "Server   Error",
//                                                    Toast.LENGTH_LONG).show();
                                            Global_Data.Custom_Toast(Order_Delivery.this,
                                                    "Server   Error","");
                                        }
                                    });

                                } else if (error instanceof NetworkError) {

                                    Order_Delivery.this.runOnUiThread(new Runnable() {
                                        public void run() {

//                                            Toast.makeText(Order_Delivery.this,
//                                                    "your internet connection is not working, saving locally. Please sync when Internet is available",
//                                                    Toast.LENGTH_LONG).show();
                                            Global_Data.Custom_Toast(Order_Delivery.this,
                                                    "your internet connection is not working, saving locally. Please sync when Internet is available","yes");
                                        }
                                    });

                                } else if (error instanceof ParseError) {

                                    Order_Delivery.this.runOnUiThread(new Runnable() {
                                        public void run() {

//                                            Toast.makeText(Order_Delivery.this,
//                                                    "ParseError   Error",
//                                                    Toast.LENGTH_LONG).show();
                                            Global_Data.Custom_Toast(Order_Delivery.this,
                                                    "ParseError   Error","");
                                        }
                                    });

                                } else {

                                    Order_Delivery.this.runOnUiThread(new Runnable() {
                                        public void run() {

                                         //   Toast.makeText(Order_Delivery.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                            Global_Data.Custom_Toast(Order_Delivery.this, error.getMessage(), "yes");
                                        }
                                    });

                                }

                                Order_Delivery.this.runOnUiThread(new Runnable() {
                                    public void run() {

                                        pDialog.dismiss();
                                    }
                                });

                                // finish();
                            }
                        });


                        RequestQueue requestQueue = Volley.newRequestQueue(Order_Delivery.this);

                        int socketTimeout = 150000;//90 seconds - change to what you want
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        jsObjRequest.setRetryPolicy(policy);
                        // requestQueue.se
                        //requestQueue.add(jsObjRequest);
                        jsObjRequest.setShouldCache(false);
                        requestQueue.getCache().clear();
                        requestQueue.add(jsObjRequest);

                    } catch (Exception e) {
                        e.printStackTrace();

                        Order_Delivery.this.runOnUiThread(new Runnable() {
                            public void run() {

                                pDialog.dismiss();
                            }
                        });

                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("DATA", e.getMessage());

                    Order_Delivery.this.runOnUiThread(new Runnable() {
                        public void run() {

                            pDialog.dismiss();
                        }
                    });
                }


            } else {
               // Toast.makeText(Order_Delivery.this, "Please Enter Quantity.", Toast.LENGTH_SHORT).show();
                Global_Data.Custom_Toast(Order_Delivery.this, "Please Enter Quantity.","");
            }


            Order_Delivery.this.runOnUiThread(new Runnable() {
                public void run() {
                    //dialog.dismiss();
                }
            });

        }

        @Override
        protected void onPreExecute() {


            Order_Delivery.this.runOnUiThread(new Runnable() {
                public void run() {


                    d_update.setEnabled(false);
                    d_update.setText("Wait...");

                }
            });
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }
}
