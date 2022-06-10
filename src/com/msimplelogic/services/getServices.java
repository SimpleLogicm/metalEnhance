package com.msimplelogic.services;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;


import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
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
import com.msimplelogic.activities.AppLocationManager;
import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.Local_Data;
import com.msimplelogic.activities.LocationAddress;
import com.msimplelogic.activities.LoginActivity;
import com.msimplelogic.activities.LoginDataBaseAdapter;
import com.msimplelogic.activities.MainActivity;
import com.msimplelogic.activities.MyService;
import com.msimplelogic.activities.PlayService_Location;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.Sales_Dash;
import com.msimplelogic.activities.SplashScreenActivity;
import com.msimplelogic.activities.ViewpagerActivity;
import com.msimplelogic.activities.kotlinFiles.ActivityPlanner;
import com.msimplelogic.activities.kotlinFiles.AllOrders_Sync;
import com.msimplelogic.activities.kotlinFiles.Invoice_order;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.activities.kotlinFiles.TimeSheetActivity;
import com.msimplelogic.activities.kotlinFiles.kotGlobal.Kot_Gloval;
import com.msimplelogic.helper.MultipartUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by vinod on 21-03-2016.
 */

public class getServices {
    private static ArrayList<String> hashkey = new ArrayList<String>();
    private static ArrayList<String> hashvalue = new ArrayList<String>();
    static String needByDate = "";
    static String return_order_id = "";
    static String image_url1 = "";
    static String image_url2 = "";
    static String image_url3 = "";
    static String image_url4 = "";
    static String image_url5 = "";
    static String response_result = "";
    static String order_image_url = "";
    static String order_image_url2 = "";
    static String response_result_image = "";
    static String feedPhotoCode = "";
    static String claimPhotoCode = "";
    static String complaintCode = "";
    static String competitionStockCode = "";
    static String feedPhotoPath = "";
    static String claimPhotoPath = "";
    static String complaintPhotoPath = "";
    static String competitionStockPhotoPath = "";
    static Bitmap blob_data_logo;
    static byte[] imgBytesData;
    static String str;
    static String strr = "";
    static int items_count = 0;
    static String signature_path = "";
    static String other_image_path = "";
    static ArrayList<String> PRODUCTOrder_ids = new ArrayList<String>();
    private static float t_total = 0;
    private static float achived_total = 0;
    private static Calendar calendarn;
    private static Double total_ammount = 0.0;
    private static int year;
    private static ArrayList<String> mobile_numbers = new ArrayList<String>();
    private int month;
    private int day;

    static String final_response = "";
    static Activity activity;
    // PreferencesHelper Prefs;
    TextView USERNAME, LOCATION, TARGETS, ACHIVE;
    static LoginDataBaseAdapter loginDataBaseAdapter;
    Location location;
    static String email_adress = "";

    // static String Account_Flag = "";
    static String customer_id = "";
    Boolean isInternetPresent = false;
    //ConnectionDetector cd;
    static ProgressDialog dialog;
    static Dialog customdialog;

    static JSONObject ParentJson;
    Context ctx;
    static DataBaseHelper dbvoc;
    // static LoginDataBaseAdapter loginDataBaseAdapter;

    static String Retailer_Flag = "";
    static String Retailer_Flag_create = "";
    static String FEED_Flag = "";
    static String COMP_Flag = "";
    static String CLAIM_Flag = "";
    static String COMPS_Flag = "";
    static String PICTURE_Flag = "";
    static String Stock_status = "";
    static String Details = "";
    static String pic1 = "";
    static String pic2 = "";
    static String pic3 = "";

    static String order_Flag = "";
    static String return_order_Flag = "";
    static String no_order_Flag = "";
    static String market_survey_Flag = "";
    static String travel_expenses_Flag = "";
    static String misc_expenses_Flag = "";
    static String calenderdata_Flag = "";

    static String Final_Flag_N = "";
    static String Final_Flag_ORDER_N = "";
    static int simState;
    static String Sim_Number = "";
    static Context context;
    static String device_id = "";
    static String Order_number = "";
    static String customer_flag = "";
    static String retailer_code = "";
    static ArrayList<String> s_code = new ArrayList<String>();
    static ArrayList<String> s_stock = new ArrayList<String>();
    static nl.dionsegijn.konfetti.KonfettiView viewKonfetti;
    static final Handler handler = new Handler();
    static public String check_flag = "";
    static public String action_item = "";
    static public String assign_to = "";
    static public String status = "";
    static public String description = "";
    static public String pdf_path = "";
    static public String mp3_path = "";
    static public String id = "";
    static public String m_type = "";
    static public String due_date = "";
    static public String s_id = "";
    static public ArrayList<String> detailsLists = new ArrayList<String>();
    static Dialog device_dialog;
    static TextView tablename;

    public static void sendRequestnew(Context contextn, String wait) {

        context = contextn;

        loginDataBaseAdapter = new LoginDataBaseAdapter(context);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        dbvoc = new DataBaseHelper(context);

        //PreferencesHelper Prefs = new PreferencesHelper(context);
        //String URL = Prefs.GetPreferences("URL");
        String domain = "";

        try {
            if (device_dialog != null && device_dialog.isShowing()) {
                device_dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        device_dialog = new Dialog(context);
        device_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        device_dialog.setCancelable(false);
        device_dialog.setContentView(R.layout.dialog_sync);
        tablename = (TextView) device_dialog.findViewById(R.id.tablename);
        device_dialog.show();


        SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
        String Cust_domain = sp.getString("Cust_Service_Url", "");
        String service_url = Cust_domain + "metal/api/v1/";
        //  device_id = sp.getString("devid", "");
        domain = service_url;

        // Global_Val global_Val = new Global_Val();
//        if(URL.equalsIgnoreCase(null) || URL.equalsIgnoreCase("null") || URL.equalsIgnoreCase("") || URL.equalsIgnoreCase(" ")) {
//            domain = context.getResources().getString(R.string.service_domain);
//        }
//        else
//        {
//            domain = URL.toString();
//        }F

        SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);


        Log.d("Server url", "Server url" + domain + "menus/sync_masters?email=" + user_email);

        StringRequest stringRequest = null;
        stringRequest = new StringRequest(domain + "menus/sync_masters?email=" + user_email,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //showJSON(response);
                        // Log.d("jV", "JV" + response);
                        Log.d("jV", "JV length" + response.length());
                        final_response = response;
                        Context mActivity = context;
                        new LongOperation().execute(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        device_dialog.dismiss();
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Global_Data.Custom_Toast(context,
                                    "Network Error", "");
                        } else if (error instanceof AuthFailureError) {

                            Global_Data.Custom_Toast(context,
                                    "Server AuthFailureError  Error", "");
                        } else if (error instanceof ServerError) {

                            Global_Data.Custom_Toast(context,
                                    "Server   Error", "");
                        } else if (error instanceof NetworkError) {
                            Global_Data.Custom_Toast(context,
                                    "Network   Error", "");
                        } else if (error instanceof ParseError) {

                            Global_Data.Custom_Toast(context,
                                    "ParseError   Error", "");
                        } else {

                            Global_Data.Custom_Toast(context, error.getMessage(), "");
                        }
                        device_dialog.dismiss();
                        // finish();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        stringRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public static void View_NearestCustomer(Context contextn, String address, String latitude, String longitude) {
        SharedPreferences pref_devid = contextn.getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
        device_id = pref_devid.getString("devid", "");
        context = contextn;
        loginDataBaseAdapter = new LoginDataBaseAdapter(context);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        Log.d("device_id ", "device_id" + device_id);

        // dbvoc = new DataBaseHelper(context);

        //PreferencesHelper Prefs = new PreferencesHelper(context);
        //String URL = Prefs.GetPreferences("URL");
        String domain = "";

        dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(context.getResources().getString(R.string.Please_Wait));
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        // Global_Val global_Val = new Global_Val();
//        if(URL.equalsIgnoreCase(null) || URL.equalsIgnoreCase("null") || URL.equalsIgnoreCase("") || URL.equalsIgnoreCase(" ")) {
//            domain = context.getResources().getString(R.string.service_domain);
//        }
//        else
//        {
//            domain = URL.toString();
//        }

        SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

        try {
            Log.d("Server url", "Server url" + domain + "menus/sync_stocks?email=" + user_email + "&latitude="
                    + URLEncoder.encode(latitude, "UTF-8") + "&longitude="
                    + URLEncoder.encode(longitude, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = null;
        try {
            stringRequest = new StringRequest(domain + "menus/sync_stocks?email=" + user_email + "&latitude="
                    + URLEncoder.encode(latitude, "UTF-8") + "&longitude="
                    + URLEncoder.encode(longitude, "UTF-8"),
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

                                    if (response_result.equalsIgnoreCase(context.getResources().getString(R.string.Data_is_up_to_date))) {


                                        Global_Data.Custom_Toast(context.getApplicationContext(), context.getResources().getString(R.string.app_name), "");

                                    } else if (json.getJSONArray("stocks").length() <= 0) {


                                        Global_Data.Custom_Toast(context.getApplicationContext(), "Stock is up to date.", "");

                                    } else {


                                        JSONArray stocks = json.getJSONArray("stocks");
                                        Log.d("stocks", "stocks" + stocks.toString());

                                        Log.d("stocks", "stocks length" + stocks.length());
                                        // Log.d("customers", "customers" + customers.toString());
                                        // Log.d("devices", "devices" + devices.toString());

                                        s_code.clear();
                                        s_stock.clear();

                                        for (int i = 0; i < stocks.length(); i++) {

                                            JSONObject jsonObject = stocks.getJSONObject(i);
                                            s_code.add(jsonObject.getString("code"));
                                            s_stock.add(jsonObject.getString("current_stock"));
                                            //dbvoc.update_stockCheck(jsonObject.getString("code"),jsonObject.getString("current_stock"));

                                        }

                                        //dbvoc.update_stockChecks(s_code,s_stock);

                                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Stock_Sync_Successfully), "");
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
                                }


                                dialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //  finish();
                                dialog.dismiss();
                            }
                            dialog.dismiss();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                                Global_Data.Custom_Toast(context,
                                        "Network Error", "");
                            } else if (error instanceof AuthFailureError) {

                                Global_Data.Custom_Toast(context,
                                        "Server AuthFailureError  Error", "");
                            } else if (error instanceof ServerError) {
                                Global_Data.Custom_Toast(context,
                                        context.getResources().getString(R.string.Server_Errors), "");
                            } else if (error instanceof NetworkError) {

                                Global_Data.Custom_Toast(context,
                                        "Network   Error", "");
                            } else if (error instanceof ParseError) {

                                Global_Data.Custom_Toast(context,
                                        "ParseError   Error", "");
                            } else {

                                Global_Data.Custom_Toast(context, error.getMessage(), "");
                            }
                            dialog.dismiss();
                            // finish();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        stringRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public static void SYNCORDER_BYORDER_ID(Context contextn) {

        context = contextn;
        final ProgressDialog dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        String uploadImage = "";
        dbvoc = new DataBaseHelper(contextn);

        SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

        JSONObject jsonBody = new JSONObject();
        try {

            JSONArray product = new JSONArray();
            JSONArray order = new JSONArray();
            JSONObject product_valuenew = new JSONObject();

            int a = 0;

            List<Local_Data> contacts = dbvoc.GetOrders("", "");
            //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");
            for (Local_Data cn : contacts) {
                JSONObject product_value = new JSONObject();
                product_value.put("order_number", cn.get_category_code());
                // product_value.put("order_date", cn.getCUSTOMER_ORDER_DATE());
                product_value.put("order_take_by", cn.get_custadr());
                // product_value.put("customer_account_code", cn.getCUSTOMER_ID());
                // product_value.put("remarks", cn.getCUSTOMER_REMARKS());
                //product_value.put("signature_image_name", uploadImage);
                product_value.put("email", user_email);
                product_value.put("order_type", cn.get_shedule_payment_mode());
                // product_value.put("conference_code", cn.getconference_id());
                order.put(product_value);
                Log.d("count", "a" + ++a);
                //delete_order_no = cn.getORDER_NUMBER();
                List<Local_Data> contactsproduct = dbvoc.Get_OrderProducts(cn.get_category_code());
                for (Local_Data cnp : contactsproduct) {

                    JSONObject item = new JSONObject();
                    item.put("order_number", cnp.get_category_code());
                    item.put("customer_name", cnp.get_custadr());
                    item.put("total_qty", cnp.get_stocks_product_quantity());
                    item.put("MRP", cnp.getMRP());
                    item.put("amount", cnp.get_Claims_amount());
                    item.put("scheme_amount", cnp.get_Target_Text());
                    product.put(item);
                    //Log.d("quantity","quantity"+cnp.getquantity());
                }
            }

//          for (int i = 0; i < 10; i++)
//          {
//
//
//
//          }


            product_valuenew.put("orders", order);
            product_valuenew.put("order_products", product);
            product_valuenew.put("email", user_email);
            Log.d("Orders", order.toString());

            Log.d("order_products", product.toString());

            // HashMap<String, String> params = new HashMap<String, String>();
            //params.put("token", json.toString());

            dialog.setMessage(context.getResources().getString(R.string.Order_Sync_in_Progress));
            dialog.setTitle(context.getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();

            // RequestQueue queue = Volley.newRequestQueue(getBaseContext());
            // PreferencesHelper Prefs = new PreferencesHelper(MasterSyncData.this);

            //String URL = Prefs.GetPreferences("URL");
            SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";

            String domain = service_url;

            Log.i("volley", "domain: " + domain);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "api/orders", product_valuenew, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);

                    String response_result = "";
                    //if (response.has("result")) {
                    try {
                        response_result = response.getString("result");

//                          if (response_result.equalsIgnoreCase("Device not found.")) {
//                              Toast toast = Toast.makeText(context, "Device Not Found", Toast.LENGTH_LONG);
//                              toast.setGravity(Gravity.CENTER, 0, 0);
//                              toast.show();
//                              dialog.dismiss();
//                          }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (response_result.equalsIgnoreCase(context.getResources().getString(R.string.Device_not_found))) {

                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Device_not_found), "yes");
                        dialog.dismiss();


                    } else {
//                  else
//                  {
//                      response_result = "data";
//                  }


                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Order_Sync_Successfully), "");

                        dialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volley", "error: " + error);
                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Server_Error), "");
                    dialog.dismiss();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            // queue.add(jsObjRequest);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void SYNCORDER_BYCustomer(Context contextn, String order_id) {
        context = contextn;
        dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(context.getResources().getString(R.string.Order_Sync_in_Progress));
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        new OrderAyncTask().execute();

    }

    public static void SYNCORDER_BYQuote(Context contextn) {
        context = contextn;

        dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(context.getResources().getString(R.string.Order_Sync_in_Progress));
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        new OrderQuoteAyncTask().execute();
    }

    public static void SYNCORDER_AllOrders(Context contextn) {
        context = contextn;
        new AllOrderAyncTask().execute();
    }

    public static void SyncTimesheetRecord(Context contextn) {
        context = contextn;
        new TimesheetAyncTask().execute();
    }

    public static void SyncMarketingSurveyRecord(Context contextn) {
        context = contextn;
        new MarketingSurveyAyncTask().execute();
    }

    public static void SYNCORDER_BYCustomer_Return(Context contextn) {
        context = contextn;
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; " + c.getTime());

        SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        final String formattedDate = df.format(c.getTime());

        final ProgressDialog dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        String uploadImage = "";
        dbvoc = new DataBaseHelper(contextn);

        JSONObject jsonBody = new JSONObject();

        try {

            JSONArray customer = new JSONArray();
            JSONArray product = new JSONArray();
            JSONArray order = new JSONArray();
            JSONObject product_valuenew = new JSONObject();

            int a = 0;
            String s = "";

//            List<Local_Data> customers_contacts = dbvoc.getAllRetailer_cre();
//            if (customers_contacts.size() > 0) {
//                // Retailer_Flag = "true";
//            } else {
//                // Retailer_Flag = "false";
//            }
//
//            for (Local_Data cn : customers_contacts) {
//                JSONObject product_value = new JSONObject();
//                product_value.put("user_email", cn.getemail());
//                product_value.put("code", cn.getLEGACY_CUSTOMER_CODE());
//                product_value.put("name", cn.getCUSTOMER_NAME());
//                product_value.put("shop_name", cn.getCUSTOMER_SHOPNAME());
//                product_value.put("address", cn.getADDRESS());
//                product_value.put("street", cn.getSTREET());
//                product_value.put("landmark", cn.getLANDMARK());
//                product_value.put("pincode", cn.getPIN_CODE());
//                product_value.put("mobile_no", cn.getMOBILE_NO());
//                product_value.put("email", cn.getEMAIL_ADDRESS());
//                product_value.put("status", cn.getSTATUS());
//                product_value.put("state_code", cn.getSTATE_ID());
//                product_value.put("city_code", cn.getCITY_ID());
//                product_value.put("beat_code", cn.getBEAT_ID());
//                product_value.put("vatin", cn.getvatin());
//                product_value.put("latitude", cn.getlatitude());
//                product_value.put("longitude", cn.getlongitude());
//                customer.put(product_value);
//
//            }

            List<Local_Data> contacts = dbvoc.GetOrders_return("Secondary Sales / Retail Sales", Global_Data.GLOvel_GORDER_ID_RETURN);
            //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");
            for (Local_Data cn : contacts) {
                JSONObject product_value = new JSONObject();
                product_value.put("order_number", cn.get_category_code());

                Order_number = cn.get_category_code();
                // product_value.put("order_date", cn.getCUSTOMER_ORDER_DATE());
                // product_value.put("order_take_by", "");
                product_value.put("customer_code", cn.get_category_id());

                product_value.put("email", user_email);

                customer_id = cn.get_category_id();
                product_value.put("latitude", cn.getlatitude());
                product_value.put("longitude", cn.getlongitude());
                product_value.put("signature_path", cn.getSignature_image());
                product_value.put("distributor_code", cn.getDISTRIBUTER_ID());
                // product_value.put("customer_account_code", cn.getCUSTOMER_ID());
                // product_value.put("remarks", cn.getCUSTOMER_REMARKS());
                //product_value.put("signature_image_name", uploadImage);
                product_value.put("email", user_email);
                product_value.put("offline_date", cn.getCreated_at());


                if (cn.get_shedule_payment_mode().equalsIgnoreCase("Secondary Sales / Retail Sales")) {
                    s = "Retail Sales";
                } else {
                    s = cn.get_shedule_payment_mode();
                }
                product_value.put("order_type", s);
                // product_value.put("conference_code", cn.getconference_id());
                order.put(product_value);
                Log.d("count", "a" + ++a);
                //delete_order_no = cn.getORDER_NUMBER();
                List<Local_Data> contactsproduct = dbvoc.Get_OrderProducts_return(cn.get_category_code());
                for (Local_Data cnp : contactsproduct) {
                    JSONObject item = new JSONObject();
                    item.put("order_number", cnp.get_category_code());
                    item.put("item_number", cnp.get_delivery_product_id());
                    item.put("total_return_qty", cnp.get_stocks_product_quantity());
                    item.put("MRP", cnp.getMRP());
                    item.put("amount", cnp.get_Claims_amount());

                    ++items_count;
                    //item.put("scheme_amount", cnp.get_Target_Text());
                    //item.put("item_number", cnp.get_delivery_product_id());
                    //item.put("discount_type", cnp.get_stocks_product_text());
                    product.put(item);
                    //Log.d("quantity","quantity"+cnp.getquantity());
                }
            }

//            for (int i = 0; i < 10; i++)
//            {
            //
            //
            //
//            }


            product_valuenew.put("return_orders", order);
            product_valuenew.put("return_order_products", product);
            product_valuenew.put("customers", customer);
            product_valuenew.put("email", user_email);

            Log.d("customers", customer.toString());

            Log.d("return_orders", order.toString());

            Log.d("return_order_products", product.toString());

            dialog.setMessage(context.getResources().getString(R.string.Order_Sync_in_Progress));
            dialog.setTitle(context.getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();


            SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";

            String domain = service_url;

            Log.i("volley", "domain: " + domain);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "return_orders/save_return_orders", product_valuenew, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);

                    String response_result = "";
                    //if (response.has("result")) {
                    try {
                        response_result = response.getString("result");

//                            if (response_result.equalsIgnoreCase("Device not found.")) {
//                                Toast toast = Toast.makeText(context, "Device Not Found", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//                                dialog.dismiss();
//                            }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (response_result.equalsIgnoreCase(context.getResources().getString(R.string.Device_not_registered))) {

                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Device_not_registered), "yes");
                        dialog.dismiss();
                    } else {


                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Return_Order_Sync_Successfully), "");

                        mobile_numbers.clear();

                        if (!Global_Data.customer_MobileNumber.equalsIgnoreCase(null) && !Global_Data.customer_MobileNumber.equalsIgnoreCase("null") && !Global_Data.customer_MobileNumber.equalsIgnoreCase("") && !Global_Data.customer_MobileNumber.equalsIgnoreCase(" ")) {
                            mobile_numbers.add(Global_Data.customer_MobileNumber);
                        }

                        if (!Global_Data.cus_MAnager_mobile.equalsIgnoreCase(null) && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("null") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase(" ")) {
                            mobile_numbers.add(Global_Data.cus_MAnager_mobile);
                        }

                        String gaddress = "";
                        try {
                            if (Global_Data.address.equalsIgnoreCase("null")) {
                                gaddress = "";
                            } else {
                                gaddress = Global_Data.address;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        String sms_body = "Dear " + Global_Data.CUSTOMER_NAME_NEW + " ," + "\n" + " " + Global_Data.order_retailer + " at " + Global_Data.CUSTOMER_ADDRESS_NEW + " at " + formattedDate + " has to return " + String.valueOf(items_count) + " items." + "\n\n" + " Thank you." + "\n" + " " + Global_Data.USER_FIRST_NAME + " " + Global_Data.USER_LAST_NAME + "\n" + " " + gaddress;

                        // dbvoc.getDeleteTable("order_products");
                        //dbvoc.getDeleteTable("orders");

                        if (!mobile_numbers.isEmpty() && mobile_numbers.size() > 0) {


                            for (int i = 0; i < mobile_numbers.size(); i++) {
                                String message = sms_body;
                                String tempMobileNumber = mobile_numbers.get(i).toString();
                                //String tempMobileNumber = "8454858739";
                                //Global_Data.sendSMS("8454858739",sms_body,context);

                                //  Global_Data.sendSMS(tempMobileNumber, message,context);
                            }
                        }

                        Global_Data.GLOvel_GORDER_ID_RETURN = "";
                        List<Local_Data> contactsn = dbvoc.Getcustomer_email(customer_id);
                        //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");
                        for (Local_Data cn : contactsn) {
                            email_adress = cn.get_Description();
                        }

                        String val = "";
                        dbvoc.updateCustomerby_CreateAt(val);
                        dbvoc.getDeleteTableorder_bycustomer_return(Global_Data.order_retailer.trim(), "Secondary Sales / Retail Sales", Order_number);
                        dbvoc.getDeleteTableorderproduct_bycustomer_return(Global_Data.order_retailer.trim(), "Secondary Sales / Retail Sales", Order_number);
                        dialog.dismiss();
                        final Dialog dialog = new Dialog(context);
                        dialog.setCancelable(false);

                        //tell the Dialog to use the dialog.xml as it's layout description
                        dialog.setContentView(R.layout.dialog);
                        dialog.setTitle(context.getResources().getString(R.string.Order_Status));

                        TextView txt = (TextView) dialog.findViewById(R.id.txtOrderID);

                        txt.setText(context.getResources().getString(R.string.Order_is_generated));
                        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
                        TextView txtEmail = (TextView) dialog.findViewById(R.id.txtEmail);

                        txtEmail.setText(context.getResources().getString(R.string.Mail_has_been_sent_to) + email_adress);
                        if (!mobile_numbers.isEmpty() && mobile_numbers.size() > 0) {
                            txtMessage.setText(context.getResources().getString(R.string.Sms_Send_Successfully));
                        }

                        ImageView dialogButton = (ImageView) dialog.findViewById(R.id.dialogButton);

                        dialogButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                Intent intentn = new Intent(context, MainActivity.class);
                                context.startActivity(intentn);
                                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                ((Activity) context).finish();

                            }
                        });

                        dialog.show();


                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                        Global_Data.Custom_Toast(context,
                                context.getResources().getString(R.string.internet_connection_error), "");
                    } else if (error instanceof AuthFailureError) {

                        Global_Data.Custom_Toast(context,
                                "Server AuthFailureError  Error", "");
                    } else if (error instanceof ServerError) {

                        Global_Data.Custom_Toast(context,
                                "Server   Error", "");
                    } else if (error instanceof NetworkError) {

                        Global_Data.Custom_Toast(context,
                                context.getResources().getString(R.string.internet_connection_error), "");
                    } else if (error instanceof ParseError) {

                        Global_Data.Custom_Toast(context,
                                "ParseError   Error", "");
                    } else {

                        Global_Data.Custom_Toast(context, error.getMessage(), "");
                    }
                    dialog.dismiss();
                    // finish();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            // queue.add(jsObjRequest);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);

        } catch (JSONException e) {
            e.printStackTrace();


        }


    }

    public static void SYNCORDER_BYCustomerINSTI(Context contextn, String Quote_status) {

        context = contextn;
        final ProgressDialog dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        String uploadImage = "";
        dbvoc = new DataBaseHelper(contextn);

        SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

        JSONObject jsonBody = new JSONObject();

        try {

            JSONArray CUSTOMERSN = new JSONArray();
            JSONArray product = new JSONArray();
            JSONArray order = new JSONArray();
            JSONObject product_valuenew = new JSONObject();

            int a = 0;
            String s = "";

//            List<Local_Data> contacts_customer = dbvoc.getAllRetailer_cre();
//
//            for (Local_Data cn : contacts_customer) {
//                JSONObject product_value = new JSONObject();
//                product_value.put("user_email", cn.getemail());
//                product_value.put("code", cn.getLEGACY_CUSTOMER_CODE());
//                product_value.put("name", cn.getCUSTOMER_NAME());
//                product_value.put("shop_name", cn.getCUSTOMER_SHOPNAME());
//                product_value.put("address", cn.getADDRESS());
//                product_value.put("street", cn.getSTREET());
//                product_value.put("landmark", cn.getLANDMARK());
//                product_value.put("pincode", cn.getPIN_CODE());
//                product_value.put("mobile_no", cn.getMOBILE_NO());
//                product_value.put("email", cn.getEMAIL_ADDRESS());
//                product_value.put("status", cn.getSTATUS());
//                product_value.put("state_code", cn.getSTATE_ID());
//                product_value.put("city_code", cn.getCITY_ID());
//                product_value.put("beat_code", cn.getBEAT_ID());
//                product_value.put("vatin", cn.getvatin());
//                product_value.put("latitude", cn.getlatitude());
//                product_value.put("longitude", cn.getlongitude());
//                CUSTOMERSN.put(product_value);
//            }

            List<Local_Data> contacts = dbvoc.GetOrdersInSI(Global_Data.order_retailer.trim(), Global_Data.GLObalOrder_id, "Institutional Sales");
            //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");
            for (Local_Data cn : contacts) {
                JSONObject product_value = new JSONObject();
                product_value.put("quote_number", cn.get_category_code());
                product_value.put("original_quote_number", cn.get_category_code());
                //product_value.put("aasm_state", "");
                // product_value.put("order_date", cn.getCUSTOMER_ORDER_DATE());
                // product_value.put("order_take_by", "");
                product_value.put("customer_code", cn.get_category_id());
                product_value.put("email", user_email);
                product_value.put("latitude", cn.getlatitude());
                product_value.put("longitude", cn.getlongitude());
                product_value.put("modify_value", Quote_status);

                Order_number = cn.get_category_code();
                customer_id = cn.get_category_id();
                // product_value.put("customer_account_code", cn.getCUSTOMER_ID());
                // product_value.put("remarks", cn.getCUSTOMER_REMARKS());
                //product_value.put("signature_image_name", uploadImage);
                //product_value.put("device_code", Global_Data.device_id);

                if (cn.get_shedule_payment_mode().equalsIgnoreCase("Secondary Sales / Retail Sales")) {
                    s = "Retail Sales";
                } else {
                    s = cn.get_shedule_payment_mode();
                }
                // product_value.put("order_type", s);
                // product_value.put("conference_code", cn.getconference_id());
                order.put(product_value);
                Log.d("count", "a" + ++a);
                //delete_order_no = cn.getORDER_NUMBER();
                List<Local_Data> contactsproduct = dbvoc.Get_OrderProducts(cn.get_category_code());
                for (Local_Data cnp : contactsproduct) {

                    JSONObject item = new JSONObject();
                    item.put("quote_number", cnp.get_category_code());
                    //item.put("customer_name", cnp.get_custadr());
                    item.put("total_qty", cnp.get_stocks_product_quantity());
                    item.put("MRP", cnp.getMRP());
                    item.put("amount", cnp.get_Claims_amount());
                    String sss=cnp.get_Target_Text();
                    String ssas=cnp.get_delivery_product_id();
                    String ssasa=cnp.get_stocks_product_text();
                    item.put("scheme_amount", sss);
                    item.put("item_number", ssas);
                    item.put("discount_type", ssasa);
                    product.put(item);
                    //Log.d("quantity","quantity"+cnp.getquantity());

                }

            }

//            for (int i = 0; i < 10; i++)
//            {
            //
            //
            //
//            }

            product_valuenew.put("customers", CUSTOMERSN);
            product_valuenew.put("quotations", order);
            product_valuenew.put("quotation_products", product);
            product_valuenew.put("email", user_email);

            Log.d("customers", CUSTOMERSN.toString());
            Log.d("quotations", order.toString());

            Log.d("quotation_products", product.toString());

            // HashMap<String, String> params = new HashMap<String, String>();
            //params.put("token", json.toString());

            dialog.setMessage(context.getResources().getString(R.string.Order_Sync_in_Progress));
            dialog.setTitle(context.getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();


            // RequestQueue queue = Volley.newRequestQueue(getBaseContext());
            // PreferencesHelper Prefs = new PreferencesHelper(MasterSyncData.this);


            //String URL = Prefs.GetPreferences("URL");
            SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";

            String domain = service_url;

            Log.i("volley", "domain: " + domain);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "quotations/save_quotations", product_valuenew, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);

                    String response_result = "";
                    //if (response.has("result")) {
                    try {
                        response_result = response.getString("result");

//                            if (response_result.equalsIgnoreCase("Device not found.")) {
//                                Toast toast = Toast.makeText(context, "Device Not Found", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//                                dialog.dismiss();
//                            }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (response_result.equalsIgnoreCase(context.getResources().getString(R.string.Device_not_found))) {

                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Device_not_found), "yes");
                        dialog.dismiss();
                    }
                    if (response_result.equalsIgnoreCase("No quotations received.")) {

                        Global_Data.Custom_Toast(context, "No quotations received.", "yes");
                        dialog.dismiss();
                    } else {

                        String val = "";
                        dbvoc.updateCustomerby_CreateAt(val);

                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Order_send_for_approval_successfully), "");


                        // dbvoc.getDeleteTable("order_products");
                        //dbvoc.getDeleteTable("orders");
//                    List<Local_Data> contactsn = dbvoc.Getcustomer_email(customer_id);
//                    //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");
//                    for (Local_Data cn : contactsn)
//                    {
//                   	 email_adress = cn.get_Description();
//                    }

                        //dbvoc.getDeleteTableorder_bycustomer_IN(Global_Data.order_retailer.trim(),"Institutional Sales",Order_number);
                        //dbvoc.getDeleteTableorderproduct_bycustomer_IN(Global_Data.order_retailer.trim(),"Institutional Sales",Order_number);
                        dialog.dismiss();
                        Intent intentn = new Intent(context, MainActivity.class);
                        context.startActivity(intentn);
                        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        ((Activity) context).finish();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volley", "error: " + error);

                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Server_Error), "");
                    dialog.dismiss();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            // queue.add(jsObjRequest);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);

        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    public static void SYNCORDER_BYCustomerINSTI_NEW(Context contextn, final String Quote_status) {

        context = contextn;
        final ProgressDialog dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        String uploadImage = "";
        dbvoc = new DataBaseHelper(contextn);

        SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

        try {
            AppLocationManager appLocationManager = new AppLocationManager(context);
            Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
            Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

            PlayService_Location PlayServiceManager = new PlayService_Location(context);

            if (PlayServiceManager.checkPlayServices(context)) {
                Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

            } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        JSONObject jsonBody = new JSONObject();

        try {

            Long randomPIN = System.currentTimeMillis();
            String PINString = String.valueOf(randomPIN);

            JSONArray product = new JSONArray();
            JSONArray order = new JSONArray();
            JSONObject product_valuenew = new JSONObject();

            int a = 0;
            String s = "";

            List<Local_Data> contacts = dbvoc.GetOrdersInSI(Global_Data.order_retailer.trim(), Global_Data.GLObalOrder_id, "Institutional Sales");
            //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");
            for (Local_Data cn : contacts) {
                JSONObject product_value = new JSONObject();
                product_value.put("quote_number", "QNO" + PINString);
                product_value.put("original_quote_number", cn.get_category_code());
                product_value.put("aasm_state", Quote_status);
                product_value.put("customer_code", cn.get_category_id());
                product_value.put("email", user_email);
                product_value.put("latitude", Global_Data.GLOvel_LATITUDE);
                product_value.put("longitude", Global_Data.GLOvel_LONGITUDE);
                Order_number = cn.get_category_code();
                customer_id = cn.get_category_id();
                // product_value.put("customer_account_code", cn.getCUSTOMER_ID());
                // product_value.put("remarks", cn.getCUSTOMER_REMARKS());
                //product_value.put("signature_image_name", uploadImage);
                product_value.put("email", user_email);

                if (cn.get_shedule_payment_mode().equalsIgnoreCase("Secondary Sales / Retail Sales")) {
                    s = "Retail Sales";
                } else {
                    s = cn.get_shedule_payment_mode();
                }
                // product_value.put("order_type", s);
                // product_value.put("conference_code", cn.getconference_id());
                order.put(product_value);
                Log.d("count", "a" + ++a);
                //delete_order_no = cn.getORDER_NUMBER();
                List<Local_Data> contactsproduct = dbvoc.Get_OrderProducts(cn.get_category_code());
                for (Local_Data cnp : contactsproduct) {

                    JSONObject item = new JSONObject();
                    item.put("quote_number", "QNO" + PINString);
                    //item.put("customer_name", cnp.get_custadr());
                    item.put("total_qty", cnp.get_stocks_product_quantity());
                    String dsf=cnp.getMRP();
                    item.put("MRP", dsf);
                    item.put("amount", cnp.get_Claims_amount());
                    // item.put("scheme_amount", cnp.get_Target_Text());
                    item.put("item_number", cnp.get_delivery_product_id());
                    // item.put("discount_type", cnp.get_stocks_product_text());
                    product.put(item);
                    //Log.d("quantity","quantity"+cnp.getquantity());

                }

            }

//           for (int i = 0; i < 10; i++)
//           {
            //
            //
            //
//           }

            product_valuenew.put("quotations", order);
            product_valuenew.put("quotation_products", product);
            product_valuenew.put("email", user_email);

            Log.d("quotations", order.toString());

            Log.d("quotation_products", product.toString());

            // HashMap<String, String> params = new HashMap<String, String>();
            //params.put("token", json.toString());

            dialog.setMessage(context.getResources().getString(R.string.Order_Sync_in_Progress));
            dialog.setTitle(context.getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();


            // RequestQueue queue = Volley.newRequestQueue(getBaseContext());
            // PreferencesHelper Prefs = new PreferencesHelper(MasterSyncData.this);


            //String URL = Prefs.GetPreferences("URL");
            SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";

            String domain = service_url;

            Log.i("volley", "domain: " + domain);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "quotations/save_quotations", product_valuenew, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);


                    String response_result = "";
                    // if (response.has("result")) {
                    try {
                        response_result = response.getString("result");

//                           if (response_result.equalsIgnoreCase("Device not found.")) {
//                               Toast toast = Toast.makeText(context, "Device Not Found", Toast.LENGTH_LONG);
//                               toast.setGravity(Gravity.CENTER, 0, 0);
//                               toast.show();
//                               dialog.dismiss();
//                           }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (response_result.equalsIgnoreCase(context.getResources().getString(R.string.Device_not_found))) {

                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Device_not_found), "yes");
                        dialog.dismiss();
                    } else {
//                   else
//                   {
//                       response_result = "data";
//                   }


                        //String response_result = "";
                        if (response.has("message")) {
                            try {
                                response_result = response.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            response_result = context.getResources().getString(R.string.Order_send_for_approval_successfully);
                        }

                        if (Global_Data.reject == "yes" && Global_Data.reject != "") {

                            Global_Data.Custom_Toast(context, "Quotation rejected successfully", "");

                        } else {

                            Global_Data.Custom_Toast(context, response_result, "");
                        }

                        Global_Data.reject = "";

                        // dbvoc.getDeleteTable("order_products");
                        //dbvoc.getDeleteTable("orders");
//                   List<Local_Data> contactsn = dbvoc.Getcustomer_email(customer_id);
//                   //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");
//                   for (Local_Data cn : contactsn)
//                   {
//                  	 email_adress = cn.get_Description();
//                   }

                        //dbvoc.getDeleteTableorder_bycustomer_IN(Global_Data.order_retailer.trim(),"Institutional Sales",Order_number);
                        //dbvoc.getDeleteTableorderproduct_bycustomer_IN(Global_Data.order_retailer.trim(),"Institutional Sales",Order_number);
                        if (Quote_status.equalsIgnoreCase("lost") || Quote_status.equalsIgnoreCase("ordered")) {
                            dbvoc.getDeleteTableorder_bycustomer_INN("Institutional Sales", Global_Data.GLObalOrder_id);
                            dbvoc.getDeleteTableorderproduct_bycustomer_INN("Institutional Sales", Global_Data.GLObalOrder_id);
                        }


                        dialog.dismiss();
                        Intent intentn = new Intent(context, MainActivity.class);
                        context.startActivity(intentn);
                        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        //((Activity) context).finish();
//  					final Dialog dialog = new Dialog(context);
//
//  					//tell the Dialog to use the dialog.xml as it's layout description
//  					dialog.setContentView(R.layout.dialog);
//  					dialog.setTitle("Order Status :");
//
//  					TextView txt = (TextView) dialog.findViewById(R.id.txtOrderID);
//
//  					txt.setText("Order is generated.");
//  					TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
//  					TextView txtEmail = (TextView) dialog.findViewById(R.id.txtEmail);
//
//  					txtEmail.setText("Mail has been sent to " + email_adress );
//
//
//  					ImageView dialogButton = (ImageView) dialog.findViewById(R.id.dialogButton);
//
//  					dialogButton.setOnClickListener(new OnClickListener() {
//  						@Override
//  						public void onClick(View v) {
//  							dialog.dismiss();
//
//
//
//  							//overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//  							Intent intentn = new Intent(context, MainActivity.class);
//  							context.startActivity(intentn);
//  							((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//  							((Activity) context).finish();
//
//  						}
//  					});
//
//  					dialog.show();

                        //Intent intentn = new Intent(context, MainActivity.class);
                        //context.startActivity(intentn);
                        //context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        //((Activity) context).finish();
//                   List<Local_Data> contacts = dbvoc.GetOrders(Global_Val.customer_id);
//                   for (Local_Data cn : contacts)
//                   {
//                       // JSONObject product_value = new JSONObject();
//                       //product_value.put("order_number", cn.getORDER_NUMBER());
                        //
//                       dbvoc.deleteOrderproductByOCID(cn.getORDER_NUMBER());
//                       dbvoc.deleteOrderTABLE_QuantityValue(cn.getORDER_NUMBER());
//                       dbvoc.deleteBarcode_ByOrder(cn.getORDER_NUMBER());
//                       dbvoc.deleteORDERSNEW(cn.getORDER_NUMBER());
                        //
//                   }


                        //dbvoc.deleteOrderByOCID(Global_Val.customer_id);
                        //dbvoc.getDeleteTable("DESIGN_CHECK");

//                   Intent i = new Intent(MasterSyncData.this, MyAndroidAppActivity.class);
//                   //				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                   i.putExtra("user_name", user_name);
//                   i.putExtra("confrence_name", confrence_name);
//                   i.putExtra("BackFlag", "nothing");
//                   Global_Val.STOCK_SERVICE_FLAG = "TRUE";
                        //				i.putExtra("Barcode_Number", userInput.getText().toString());
                        //				i.putExtra("BackFlag","Barcode");
//                   startActivity(i);
//                   MasterSyncData.this.finish();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volley", "error: " + error);

                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Server_Error), "");
                    dialog.dismiss();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            // queue.add(jsObjRequest);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//    public static void SYNCORDER_BYCustomerINSTI_NEW1(Context contextn, final String device_id, final String email_adress) {
//
//        context = contextn;
//        final ProgressDialog dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//        String uploadImage = "";
//        dbvoc = new DataBaseHelper(contextn);
//
//        try {
//            AppLocationManager appLocationManager = new AppLocationManager(context);
//            Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
//            Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
//
//            PlayService_Location PlayServiceManager = new PlayService_Location(context);
//
//            if (PlayServiceManager.checkPlayServices(context)) {
//                Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
//
//            } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
//                Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
//                Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//
//        JSONObject jsonBody = new JSONObject();
//
//        try {
//
////            Long randomPIN = System.currentTimeMillis();
////            String PINString = String.valueOf(randomPIN);
////
////            JSONArray product = new JSONArray();
////            JSONArray order = new JSONArray();
////            JSONObject product_valuenew = new JSONObject();
////
////            int a = 0;
////            String s = "";
////
////            List<Local_Data> contacts = dbvoc.GetOrdersInSI(Global_Data.order_retailer.trim(), Global_Data.GLObalOrder_id, "Institutional Sales");
////            //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");
////            for (Local_Data cn : contacts) {
////                JSONObject product_value = new JSONObject();
////                product_value.put("quote_number", "QNO" + PINString);
////                product_value.put("original_quote_number", cn.get_category_code());
////                product_value.put("aasm_state", Quote_status);
////                product_value.put("customer_code", cn.get_category_id());
////                product_value.put("email", Global_Data.GLOvel_USER_EMAIL);
////                product_value.put("latitude", Global_Data.GLOvel_LATITUDE);
////                product_value.put("longitude", Global_Data.GLOvel_LONGITUDE);
////                Order_number = cn.get_category_code();
////                customer_id = cn.get_category_id();
////                // product_value.put("customer_account_code", cn.getCUSTOMER_ID());
////                // product_value.put("remarks", cn.getCUSTOMER_REMARKS());
////                //product_value.put("signature_image_name", uploadImage);
////                product_value.put("device_code", Global_Data.device_id);
////
////                if (cn.get_shedule_payment_mode().equalsIgnoreCase("Secondary Sales / Retail Sales")) {
////                    s = "Retail Sales";
////                } else {
////                    s = cn.get_shedule_payment_mode();
////                }
////                // product_value.put("order_type", s);
////                // product_value.put("conference_code", cn.getconference_id());
////                order.put(product_value);
////                Log.d("count", "a" + ++a);
////                //delete_order_no = cn.getORDER_NUMBER();
////                List<Local_Data> contactsproduct = dbvoc.Get_OrderProducts(cn.get_category_code());
////                for (Local_Data cnp : contactsproduct) {
////
////                    JSONObject item = new JSONObject();
////                    item.put("quote_number", "QNO" + PINString);
////                    //item.put("customer_name", cnp.get_custadr());
////                    item.put("total_qty", cnp.get_stocks_product_quantity());
////                    item.put("MRP", cnp.getMRP());
////                    item.put("amount", cnp.get_Claims_amount());
////                    // item.put("scheme_amount", cnp.get_Target_Text());
////                    item.put("item_number", cnp.get_delivery_product_id());
////                    // item.put("discount_type", cnp.get_stocks_product_text());
////                    product.put(item);
////                    //Log.d("quantity","quantity"+cnp.getquantity());
////
////                }
////
////            }
//
////           for (int i = 0; i < 10; i++)
////           {
//            //
//            //
//            //
////           }
////
////            product_valuenew.put("quotations", order);
////            product_valuenew.put("quotation_products", product);
////            product_valuenew.put("imei_no", Global_Data.device_id);
////
////            Log.d("quotations", order.toString());
////
////            Log.d("quotation_products", product.toString());
//
//            // HashMap<String, String> params = new HashMap<String, String>();
//            //params.put("token", json.toString());
//
//            dialog.setMessage(context.getResources().getString(R.string.Order_Sync_in_Progress));
//            dialog.setTitle(context.getResources().getString(R.string.app_name));
//            dialog.setCancelable(false);
//            dialog.show();
//
//
//            //String URL = Prefs.GetPreferences("URL");
//            SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
//            String Cust_domain = sp.getString("Cust_Service_Url", "");
//            String service_url = Cust_domain + "metal/api/v1/";
//
//            String domain = service_url;
//
//            Log.i("volley", "domain: " + domain);
//            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "quotations/modify_quotation?imei_no="+device_id+"&email="+email_adress,null,  new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    Log.i("volley", "response: " + response);
//
//
//                    String response_result = "";
//                    // if (response.has("result")) {
//                    try {
//                        response_result = response.getString("result");
//
////                           if (response_result.equalsIgnoreCase("Device not found.")) {
////                               Toast toast = Toast.makeText(context, "Device Not Found", Toast.LENGTH_LONG);
////                               toast.setGravity(Gravity.CENTER, 0, 0);
////                               toast.show();
////                               dialog.dismiss();
////                           }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    if (response_result.equalsIgnoreCase(context.getResources().getString(R.string.Device_not_found))) {
//                        Toast toast = Toast.makeText(context, context.getResources().getString(R.string.Device_not_found), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//                        dialog.dismiss();
//                    } else {
////                   else
////                   {
////                       response_result = "data";
////                   }
//
//
//                        //String response_result = "";
//                        if (response.has("message")) {
//                            try {
//                                response_result = response.getString("message");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            response_result = context.getResources().getString(R.string.Order_send_for_approval_successfully);
//                        }
//
//                        Toast.makeText(context, response_result, Toast.LENGTH_LONG).show();
//
//
//                        dialog.dismiss();
//                        Intent intentn = new Intent(context, MainActivity.class);
//                        context.startActivity(intentn);
//                        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.i("volley", "error: " + error);
//                    Toast.makeText(context, context.getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG).show();
//                    dialog.dismiss();
//                }
//            });
//
//            RequestQueue requestQueue = Volley.newRequestQueue(context);
//            // queue.add(jsObjRequest);
//            int socketTimeout = 30000;//30 seconds - change to what you want
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            jsObjRequest.setRetryPolicy(policy);
//            requestQueue.add(jsObjRequest);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    public static void SyncDataToServercommon(final Context context) {
        System.gc();
        String reason_code = "";
        try {

//			DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//			DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
//			Date date1 = originalFormat.parse(getDateTime());
//			String formattedDate = targetFormat.format(date1);


            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage(context.getResources().getString(R.string.Please_Wait));
            dialog.setTitle(context.getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();

            String domain = "";
            String device_id = "";


            SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";
            device_id = sp.getString("devid", "");
            domain = service_url;


            JsonObjectRequest jsObjRequest = null;
            try {


                Log.d("Server url", "Server url" + domain + "expenses_travles/save_travel_expenses");


                JSONArray order = new JSONArray();
                JSONArray fR = new JSONArray();
                JSONArray COMP = new JSONArray();
                JSONArray CLAIM = new JSONArray();
                JSONArray COMPS = new JSONArray();
                JSONArray PICTURE = new JSONArray();
                //JSONObject product_value = new JSONObject();
                JSONObject product_value_n = new JSONObject();
                JSONArray product_imei = new JSONArray();

                final DataBaseHelper dbvoc = new DataBaseHelper(context);

//                List<Local_Data> contacts = dbvoc.getAllRetailer_cre();
//                if (contacts.size() > 0) {
//                    Retailer_Flag = "true";
//                } else {
//                    Retailer_Flag = "false";
//                }
//
//                for (Local_Data cn : contacts) {
//                    JSONObject product_value = new JSONObject();
//                    product_value.put("user_email", cn.getemail());
//                    product_value.put("code", cn.getLEGACY_CUSTOMER_CODE());
//                    product_value.put("name", cn.getCUSTOMER_NAME());
//                    product_value.put("shop_name", cn.getCUSTOMER_SHOPNAME());
//                    product_value.put("address", cn.getADDRESS());
//                    product_value.put("street", cn.getSTREET());
//                    product_value.put("landmark", cn.getLANDMARK());
//                    product_value.put("pincode", cn.getPIN_CODE());
//                    product_value.put("mobile_no", cn.getMOBILE_NO());
//                    product_value.put("email", cn.getEMAIL_ADDRESS());
//                    product_value.put("status", cn.getSTATUS());
//                    product_value.put("state_code", cn.getSTATE_ID());
//                    product_value.put("city_code", cn.getCITY_ID());
//                    product_value.put("beat_code", cn.getBEAT_ID());
//                    product_value.put("vatin", cn.getvatin());
//                    product_value.put("latitude", cn.getlatitude());
//                    product_value.put("longitude", cn.getlongitude());
//                    order.put(product_value);
//
//                }


                if (Retailer_Flag.equalsIgnoreCase("true")) {
                    Final_Flag_N += " " + "Retailer";
                } else {
                    Final_Flag_N = "";
                }

                if (Final_Flag_N.equalsIgnoreCase("")) {

                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.No_record_found_for_sync), "");
                    dialog.dismiss();
                } else {
                    SharedPreferences spf1 = context.getSharedPreferences("SimpleLogic", 0);
                    String user_email = spf1.getString("USER_EMAIL", null);

                    // product_imei.put(Global_Data.device_id);
                    product_value_n.put("customers", order);
                    // product_value_n.put("customer_service_feedbacks", fR);
                    // product_value_n.put("customer_service_complaints", COMP);
                    // product_value_n.put("customer_service_claims", CLAIM);
                    // product_value_n.put("customer_service_competition_stocks", COMPS);
                    // product_value_n.put("customer_service_media", PICTURE);
                    // product_value_n.put("imei_no", Global_Data.device_id);
                    product_value_n.put("email", user_email);

                    Log.d("customers", product_value_n.toString());
                    //Log.d("expenses_travels",product_value_n.toString());

//
//
//				 //product_value.put("email", Global_Data.GLOvel_USER_EMAIL);
//				// product_value.put("email", Global_Data.GLOvel_USER_EMAIL);
//
                    jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "customers/create_customer", product_value_n, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("volley", "response: " + response);

                            Log.d("jV", "JV length" + response.length());
                            //JSONObject json = new JSONObject(new JSONTokener(response));
                            try {

                                String response_result = "";
                                if (response.has("message")) {
                                    response_result = response.getString("message");
                                } else {
                                    response_result = "data";
                                }


                                if (response_result.equalsIgnoreCase("Customer created successfully.")) {

                                    dialog.dismiss();
                                    List<Local_Data> contacts = dbvoc.getAllRetailer_cre();
                                    for (Local_Data cn : contacts) {
                                        //dbvoc.updateCustomerby_Createid(cn.getLEGACY_CUSTOMER_CODE());
                                        dbvoc.deletesalesupdatebyID(cn.getCUSTOMER_NAME(), cn.getCUSTOMER_SHOPNAME());
                                    }

                                    Global_Data.Custom_Toast(context, response_result, "");

                                    Global_Data.SYNC_SERVICE_FLAG = "TRUE";
                                    Toast.makeText(context, response_result, Toast.LENGTH_LONG).show();
                                    Intent a = new Intent(context, MainActivity.class);
                                    context.startActivity(a);
                                    ((Activity) context).finish();


                                } else {

                                    dialog.dismiss();
                                    // Toast.makeText(context.getApplicationContext(), response_result, Toast.LENGTH_LONG).show();

                                    Global_Data.Custom_Toast(context, response_result, "");
                                    Intent a = new Intent(context, MainActivity.class);
                                    context.startActivity(a);
                                    ((Activity) context).finish();


                                }

                                //  finish();
                                // }

                                // output.setText(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                dialog.dismiss();
                            }


                            dialog.dismiss();
                            dialog.dismiss();


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("volley", "error: " + error);
                            Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Server_Error), "");
                            dialog.dismiss();
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(context);

                    int socketTimeout = 300000;//30 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    jsObjRequest.setRetryPolicy(policy);
                    // requestQueue.se
                    //requestQueue.add(jsObjRequest);
                    jsObjRequest.setShouldCache(false);
                    requestQueue.getCache().clear();
                    requestQueue.add(jsObjRequest);
                }
            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }


            //createdID=myDbHelper.generateNoOrder(userID,cityID,beatID,retailerID,retailer_code,reasonID,reasonOther,formattedDate);
            //createdID=1;
			/*if (!mobile.equalsIgnoreCase("NA")) {
				SmsManager smsManager=SmsManager.getDefault();
				smsManager.sendTextMessage("mobile", null, "Order ID : "+createdID+" is generated", null, null);
			}



			  if (cd.isConnectingToInternet()) {
                    // Internet Connection is Present
                    // make HTTP requests

                }
			 */

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("DATA", e.getMessage());
        }
    }

    public static void SyncDataToServerCustomer(Context contextm) {
        context = contextm;
        System.gc();
        new AllDataAyncTask().execute();
    }

    private static class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... response) {
            try {
                final Calendar c = Calendar.getInstance();
                JSONObject json = new JSONObject(final_response);
                try {

                    String response_result = "";
                    if (json.has("result")) {
                        response_result = json.getString("result");
                        final String finalResponse_result = response_result;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                Global_Data.Custom_Toast(context.getApplicationContext(), finalResponse_result, "");
                                device_dialog.dismiss();
                                //getTargetDataservice(context);
                            }
                        });

                    } else {

                        String starttime="";
                        if (json.has("location_schedule_start_time")) {
                            starttime=json.getString("location_schedule_start_time");
                            try {
                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("StartTime", starttime);
                                editor.commit();

                                Log.i("interval","Interval"+starttime);
                            }catch (Exception ex){
                                ex.printStackTrace();

                            }


                        }
                        String endtime="";
                        if (json.has("location_schedule_end_time")) {
                            endtime=json.getString("location_schedule_end_time");
                            try{
                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("Endtime", endtime);

                                editor.commit();
                                Log.i("interval","Interval"+endtime);
                            }catch (Exception ex){
                                ex.printStackTrace();

                            }


                        }
                        String intervaltime="";
                        if (json.has("location_schedule_interval_time")) {
                            intervaltime=json.getString("location_schedule_interval_time");
                            try {
                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("Interval", intervaltime);
                                editor.commit();

                                Log.i("interval","Interval"+intervaltime);
                            }catch (Exception ex){
                                ex.printStackTrace();

                            }


                        }


                        JSONArray items = json.getJSONArray("products");
                        JSONArray customers = json.getJSONArray("customers");
                        //TODO DONE for distributor problem
                        JSONArray distributors = json.getJSONArray("distributors");

                        JSONArray reasons = json.getJSONArray("reasons");

                        JSONArray states = json.getJSONArray("states");
                        JSONArray cities = json.getJSONArray("cities");
                        JSONArray beats = json.getJSONArray("beats");
                        JSONArray schemes = json.getJSONArray("schemes");
                        JSONArray Survey_Questions = json.getJSONArray("survey_questions");

                        JSONArray distributor_beats = json.getJSONArray("distributor_beats");

                        JSONArray credit_profile = json.getJSONArray("credit_profiles");

                        JSONArray logo_img = json.getJSONArray("customer_logo_splash");

                        JSONArray l1_contacts = json.getJSONArray("l1_contacts");

                        JSONArray label_acc = json.getJSONArray("label_changes");

                        JSONArray warehouse = json.getJSONArray("warehouses");

                        JSONArray Order_Category = json.getJSONArray("order_categories");

                        JSONArray txn_reasons = json.getJSONArray("txn_reasons");
                        JSONArray customer_types = json.getJSONArray("customer_types");
                        JSONArray customer_categories = json.getJSONArray("customer_categories");

                        dbvoc.getDeleteTable("cities");
                        dbvoc.getDeleteTable("states");
                        dbvoc.getDeleteTable("beats");
                        dbvoc.getDeleteTable("customer_master");
                        dbvoc.getDeleteTable("cust_master_child");
                        dbvoc.getDeleteTable("distributors");
                        dbvoc.getDeleteTable("Survey_Questions");
                        dbvoc.getDeleteTable("distributor_beats");
                        dbvoc.getDeleteTable("txn_reason");
                        dbvoc.getDeleteTable("customer_types");
                        dbvoc.getDeleteTable("customer_categories");
                        dbvoc.getDeletecredit_limitsAll();

                        // JSONArray scheme = json.getJSONArray("scheme");

                        //JSONArray credit_limits = json.getJSONArray("cr_limits");
                        //JSONArray outstandings = json.getJSONArray("outstandings");

                        Log.d("txn_reasons", "txn_reasons" + txn_reasons.toString());

                        Log.d("items", "items" + items.toString());
                        //Log.d("customers", "customers" + customers.toString());
                        // TODO DONE for distributor problem
                        Log.d("distributors", "distributors" + customers.toString());

                        Log.d("reasons", "reasons" + reasons.toString());

                        Log.d("items", "items length" + items.length());
                        Log.d("customers", "customers length" + customers.length());

                        Log.d("states", "states length" + states.length());
                        Log.d("cities", "cities length" + cities.length());
                        Log.d("beats", "beats length" + beats.length());

                        Log.d("states", "states length" + states.toString());
                        Log.d("cities", "cities length" + cities.toString());
                        Log.d("beats", "beats length" + beats.toString());
                        Log.d("distributor_beats", "distributor_beats length" + distributor_beats.toString());
                        Log.d("credit_profile", "credit_profile" + credit_profile.toString());
                        Log.d("l1_contacts", "l1_contacts" + l1_contacts.toString());
                        Log.d("schemes", "schemes" + schemes.toString());
                        Log.d("warehouse", "warehouse" + warehouse.toString());
                        Log.d("Order_Category", "Order_Category" + Order_Category.toString());

                        // Log.d("Survey_Questions", "Survey_Questions length" + Survey_Questions.toString());
                        // Log.d("credit_limits", "credit_limits length" + credit_limits.toString());
                        // Log.d("scheme", "scheme length" + scheme.toString());
                        // Log.d("outstandings", "outstandings length" + outstandings.toString());


                        // Log.d("customers", "customers" + customers.toString());
                        // Log.d("devices", "devices" + devices.toString());

                        for (int i = 0; i < label_acc.length(); i++) {

                            JSONObject jsonObject = label_acc.getJSONObject(i);
                            loginDataBaseAdapter.insertLABEL_CHANGES(jsonObject.getString("variable_name"), jsonObject.getString("new_label"), jsonObject.getString("editable"), jsonObject.getString("mandatory"), jsonObject.getString("allow"));

                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table Label is syncing");
                                }
                            });


                            Global_Data.Var_Label = jsonObject.getString("variable_name");
                            Global_Data.editable = jsonObject.getString("editable");
                            Global_Data.mandatory = jsonObject.getString("mandatory");
                            Global_Data.allow = jsonObject.getString("allow");


                            if (Global_Data.Var_Label.equalsIgnoreCase("no_order")) {
                                // Global_Data.New_Label =jsonObject.getString("new_label");
                                // Global_Data.editable =jsonObject.getString("editable");
                                // Prefs.SavePreferences("VAR_NOOREDER", cn.getVarLabel_account());

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("var_norder", jsonObject.getString("new_label"));
                                editor.commit();
                            }

                            if (Global_Data.Var_Label.equalsIgnoreCase("return_order")) {
                                //Global_Data.New_Label =jsonObject.getString("new_label");
                                // Global_Data.editable =jsonObject.getString("editable");

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("var_retorder", jsonObject.getString("new_label"));
                                editor.commit();
                            }

                            if (Global_Data.Var_Label.equalsIgnoreCase("rp")) {
                                // Global_Data.New_Label = jsonObject.getString("new_label");
                                // Prefs.SavePreferences("VAR_NOOREDER", cn.getVarLabel_account());

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("var_rp", jsonObject.getString("new_label"));
                                Global_Data.rpquickorder = jsonObject.getString("new_label");
                                editor.commit();
                            }

                            if (Global_Data.Var_Label.equalsIgnoreCase("mrp")) {
                                // Global_Data.New_Label = jsonObject.getString("new_label");
                                // Prefs.SavePreferences("VAR_NOOREDER", cn.getVarLabel_account());

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("var_mrp", jsonObject.getString("new_label"));
                                Global_Data.mrpquickorder = jsonObject.getString("new_label");
                                editor.commit();
                            }

                            if (Global_Data.Var_Label.equalsIgnoreCase("order_product_detail2")) {
                                // Global_Data.New_Label = jsonObject.getString("new_label");
                                // Prefs.SavePreferences("VAR_NOOREDER", cn.getVarLabel_account());

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("order_product_detail2", jsonObject.getString("new_label"));
                                editor.commit();
                            }

                            if (Global_Data.Var_Label.equalsIgnoreCase("order_product_detail3")) {
                                // Global_Data.New_Label = jsonObject.getString("new_label");
                                // Prefs.SavePreferences("VAR_NOOREDER", cn.getVarLabel_account());

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("order_product_detail3", jsonObject.getString("new_label"));
                                editor.commit();
                            }

                            if (Global_Data.Var_Label.equalsIgnoreCase("order_product_detail4")) {
                                // Global_Data.New_Label = jsonObject.getString("new_label");
                                // Prefs.SavePreferences("VAR_NOOREDER", cn.getVarLabel_account());

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("order_product_detail4", jsonObject.getString("new_label"));
                                editor.commit();
                            }

                            if (Global_Data.Var_Label.equalsIgnoreCase("order_product_detail5")) {
                                // Global_Data.New_Label = jsonObject.getString("new_label");
                                // Prefs.SavePreferences("VAR_NOOREDER", cn.getVarLabel_account());

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("order_product_detail5", jsonObject.getString("new_label"));
                                editor.commit();
                            }

                            if (Global_Data.Var_Label.equalsIgnoreCase("order_product_detail6")) {
                                // Global_Data.New_Label = jsonObject.getString("new_label");
                                // Prefs.SavePreferences("VAR_NOOREDER", cn.getVarLabel_account());

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("order_product_detail6", jsonObject.getString("new_label"));
                                editor.commit();
                            }

                            if (Global_Data.Var_Label.equalsIgnoreCase("total_amount")) {
                                //  Global_Data.New_Label = jsonObject.getString("new_label");
                                // Prefs.SavePreferences("VAR_NOOREDER", cn.getVarLabel_account());

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("var_total_price", jsonObject.getString("new_label"));
                                editor.commit();
                            }

                            if (Global_Data.Var_Label.equalsIgnoreCase("rs")) {
                                //Global_Data.New_Label = jsonObject.getString("new_label");
                                // Prefs.SavePreferences("VAR_NOOREDER", cn.getVarLabel_account());

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("var_rs", jsonObject.getString("new_label"));
                                editor.commit();
                            }


                            if (Global_Data.Var_Label.equalsIgnoreCase("schedule")) {
                                // Global_Data.New_Label = jsonObject.getString("new_label");
                                // Prefs.SavePreferences("VAR_NOOREDER", cn.getVarLabel_account());

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("var_schedule", jsonObject.getString("new_label"));
                                editor.commit();
                            }

                            if (Global_Data.Var_Label.equalsIgnoreCase("detail1")) {
                                //   Global_Data.New_Label = jsonObject.getString("new_label");
                                //    Global_Data.editable = jsonObject.getString("editable");

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("var_detail1", jsonObject.getString("new_label"));
                                editor.commit();

                                SharedPreferences spf1 = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor1 = spf1.edit();
                                editor1.putString("var_detail1_edit", jsonObject.getString("editable"));
                                editor1.commit();

                                SharedPreferences spf2 = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor2 = spf2.edit();
                                editor2.putString("var_detail1_mandate", jsonObject.getString("mandatory"));
                                editor2.commit();

                                SharedPreferences spf3 = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor3 = spf3.edit();
                                editor3.putString("var_detail1_allow", jsonObject.getString("allow"));
                                editor3.commit();
                            }

                            if (Global_Data.Var_Label.equalsIgnoreCase("detail2")) {
                                // Global_Data.New_Label = jsonObject.getString("new_label");
                                // Prefs.SavePreferences("VAR_NOOREDER", cn.getVarLabel_account());

                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("var_detail2", jsonObject.getString("new_label"));
                                editor.commit();

                                SharedPreferences spf1 = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor1 = spf1.edit();
                                editor1.putString("var_detail2_edit", jsonObject.getString("editable"));
                                editor1.commit();

                                SharedPreferences spf2 = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor2 = spf2.edit();
                                editor2.putString("var_detail2_mandate", jsonObject.getString("mandatory"));
                                editor2.commit();

                                SharedPreferences spf3 = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor3 = spf3.edit();
                                editor3.putString("var_detail2_allow", jsonObject.getString("allow"));
                                editor3.commit();
                            }
                        }

                        for (int z = 0; z < logo_img.length(); z++) {
                            JSONObject logo_imgjobj = logo_img.getJSONObject(z);
                            str = logo_imgjobj.getString("name");
                            strr = logo_imgjobj.getString("data_image_string");
                            if (str.equalsIgnoreCase("logo")) {
                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("logo_data", strr);
                                editor.commit();
                            }
                            if (str.equalsIgnoreCase("splash")) {
                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("splash_data", strr);
                                editor.commit();
                            }
                        }


                        for (int i = 0; i < customer_types.length(); i++) {

                            JSONObject jsonObject = customer_types.getJSONObject(i);

                            loginDataBaseAdapter.insertCustomerTypes(jsonObject.getString("id"), jsonObject.getString("name"), "");


                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table Customer Type is syncing");
                                }
                            });
                        }

                        for (int i = 0; i < customer_categories.length(); i++) {

                            JSONObject jsonObject = customer_categories.getJSONObject(i);

                            loginDataBaseAdapter.insertCustomerCategories(jsonObject.getString("id"), jsonObject.getString("name"), "", jsonObject.getString("duration"));
                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table Customer Categories is syncing");
                                }
                            });

                        }

                        for (int i = 0; i < l1_contacts.length(); i++) {
                            dbvoc.getDeleteTable("L1_Contact");

                            JSONObject jsonObject = l1_contacts.getJSONObject(i);
                            loginDataBaseAdapter.insert_L1_CONTACT("", jsonObject.getString("title"), jsonObject.getString("heading"), jsonObject.getString("sub_heading"), jsonObject.getString("address"), jsonObject.getString("contact_no1"), jsonObject.getString("contact_no2"), jsonObject.getString("email_id1"), jsonObject.getString("email_id2"), jsonObject.getString("website"), "", "", "");
                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table Contact is syncing");
                                }
                            });


                        }

                        for (int i = 0; i < Order_Category.length(); i++) {
                            //dbvoc.getDeleteTable("scheme_new");

                            JSONObject jsonObject = Order_Category.getJSONObject(i);
                            loginDataBaseAdapter.insert_ORDER_CATEGORY(jsonObject.getString("code"), jsonObject.getString("name"), "", "", "", "");
                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table Order Category is syncing");
                                }
                            });
                        }

                        for (int i = 0; i < schemes.length(); i++) {
                            //dbvoc.getDeleteTable("scheme_new");

                            JSONObject jsonObject = schemes.getJSONObject(i);
                            loginDataBaseAdapter.insert_itemSchemenew(jsonObject.getString("scheme_id"), jsonObject.getString("name"), jsonObject.getString("scheme_type"), jsonObject.getString("description"), jsonObject.getString("display_name"), jsonObject.getString("product_code"), jsonObject.getString("qualifying_quantity"), jsonObject.getString("amount"), jsonObject.getString("foc_product_code"), jsonObject.getString("free_quantity"), "", "", "", "", "", jsonObject.getString("minimum_quantity"));

                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table Scheme is syncing");
                                }
                            });
                        }

                        for (int i = 0; i < warehouse.length(); i++) {
                            //dbvoc.getDeleteTable("scheme_new");

                            JSONObject jsonObject = warehouse.getJSONObject(i);
                            loginDataBaseAdapter.insert_Warehouse("", jsonObject.getString("code"), "", jsonObject.getString("name"), "", jsonObject.getString("city_code"), jsonObject.getString("state_code"), "", "", "", "", "");

                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table Warehouse is syncing");
                                }
                            });
                        }

                        List<Local_Data> checkproducts = dbvoc.HSS_DescriptionITEM();

                        if (checkproducts.size() <= 0) {
                            Log.d("FIRST SYNC", "FIRST SYNC");
                            for (int i = 0; i < items.length(); i++) {

                                JSONObject jsonObject = items.getJSONObject(i);

                                loginDataBaseAdapter.insertEntryITEM_MASTER(jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getString("primary_category"),
                                        jsonObject.getString("sub_category"), jsonObject.getString("product_variant"), jsonObject.getString("retail_price"),
                                        jsonObject.getString("mrp"), jsonObject.getString("qualifying_qty"),
                                        jsonObject.getString("free_qty"), jsonObject.getString("status"), jsonObject.getString("min_order_qty"),
                                        jsonObject.getString("max_order_qty"), jsonObject.getString("pkg_qty1"), jsonObject.getString("pkg_qty2"));
                                ((Activity) context).runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        tablename.setText("Table Item Master is syncing");

                                    }
                                });
                            }
                        } else {
                            for (int i = 0; i < items.length(); i++) {

                                JSONObject jsonObject = items.getJSONObject(i);

                                dbvoc.getDeletePRODUCT(jsonObject.getString("code"));

                                if (jsonObject.getString("status").equalsIgnoreCase("active")) {
                                    loginDataBaseAdapter.insertEntryITEM_MASTER(jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getString("primary_category"),
                                            jsonObject.getString("sub_category"), jsonObject.getString("product_variant"), jsonObject.getString("retail_price"),
                                            jsonObject.getString("mrp"), jsonObject.getString("qualifying_qty"),
                                            jsonObject.getString("free_qty"), jsonObject.getString("status"), jsonObject.getString("min_order_qty"),
                                            jsonObject.getString("max_order_qty"), jsonObject.getString("pkg_qty1"), jsonObject.getString("pkg_qty2"));

                                    ((Activity) context).runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {

                                            tablename.setText("Table Item Master is syncing");
                                        }
                                    });
                                }
                            }
                        }

                        for (int i = 0; i < customers.length(); i++) {

                            JSONObject jsonObject = customers.getJSONObject(i);
                            List<Local_Data> contactsr = dbvoc.getCustomer_BYID(jsonObject.getString("name").trim(), jsonObject.getString("shop_name").trim());

                            if (contactsr.size() <= 0) {
                                loginDataBaseAdapter.insert_CUST_MASTER_CHILD(jsonObject.getString("code"), jsonObject.getString("gstin"), jsonObject.getString("business_type"), jsonObject.getString("store_type"), jsonObject.getString("primary_category"), jsonObject.getString("sub_category"), jsonObject.getString("category"));
                                loginDataBaseAdapter.insertCustMaster(jsonObject.getString("code"), jsonObject.getString("name").trim(), jsonObject.getString("shop_name").trim(), jsonObject.getString("address"), jsonObject.getString("street"), jsonObject.getString("landmark"),
                                        jsonObject.getString("pincode"), jsonObject.getString("landline_no"), jsonObject.getString("mobile_no"), jsonObject.getString("email"), jsonObject.getString("status"), jsonObject.getString("state_code"), jsonObject.getString("city_code"), jsonObject.getString("beat_code"), jsonObject.getString("gstin"), "", "", "", "", jsonObject.getString("visiting_card"), jsonObject.getString("inshop_display"), jsonObject.getString("signboard"), jsonObject.getString("aadhar_no"), jsonObject.getString("mobile1"), jsonObject.getString("mobile2"), jsonObject.getString("bank_account_name"), jsonObject.getString("bank_account_ifsc"), jsonObject.getString("bank_account_no"), jsonObject.getString("pan_card_no"), jsonObject.getString("shop_anniversary_date"), jsonObject.getString("date_of_birthday"), jsonObject.getString("customer_flag"), jsonObject.getString("customer_type_id"), jsonObject.getString("customer_category_id"), jsonObject.getString("google_address"));
                                ((Activity) context).runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        tablename.setText("Table Master Child is syncing");
                                    }
                                });

                            } else {
                                loginDataBaseAdapter.insert_CUST_MASTER_CHILD(jsonObject.getString("code"), jsonObject.getString("gstin"), jsonObject.getString("business_type"), jsonObject.getString("store_type"), jsonObject.getString("primary_category"), jsonObject.getString("sub_category"), jsonObject.getString("category"));
                                dbvoc.deletesalesupdatebyID(jsonObject.getString("name").trim(), jsonObject.getString("shop_name").trim());
                                loginDataBaseAdapter.insertCustMaster(jsonObject.getString("code"), jsonObject.getString("name").trim(), jsonObject.getString("shop_name").trim(), jsonObject.getString("address"), jsonObject.getString("street"), jsonObject.getString("landmark"),
                                        jsonObject.getString("pincode"), jsonObject.getString("landline_no"), jsonObject.getString("mobile_no"), jsonObject.getString("email"), jsonObject.getString("status"), jsonObject.getString("state_code"), jsonObject.getString("city_code"), jsonObject.getString("beat_code"), jsonObject.getString("gstin"), "", "", "", "", jsonObject.getString("visiting_card"), jsonObject.getString("inshop_display"), jsonObject.getString("signboard"), jsonObject.getString("aadhar_no"), jsonObject.getString("mobile1"), jsonObject.getString("mobile2"), jsonObject.getString("bank_account_name"), jsonObject.getString("bank_account_ifsc"), jsonObject.getString("bank_account_no"), jsonObject.getString("pan_card_no"), jsonObject.getString("shop_anniversary_date"), jsonObject.getString("date_of_birthday"), jsonObject.getString("customer_flag"), jsonObject.getString("customer_type_id"), jsonObject.getString("customer_category_id"), jsonObject.getString("google_address"));

                                ((Activity) context).runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        tablename.setText("Table Master Child is syncing");
                                    }
                                });
                            }

                        }


                        for (int i = 0; i < distributors.length(); i++) {

                            JSONObject jsonObject = distributors.getJSONObject(i);


                            loginDataBaseAdapter.insertDistributors(jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getString("shop_name"),
                                    jsonObject.getString("address1"), jsonObject.getString("address2"), jsonObject.getString("street"),
                                    jsonObject.getString("landmark"), jsonObject.getString("state_code"),
                                    jsonObject.getString("city_code"), jsonObject.getString("pincode"),
                                    jsonObject.getString("landline_no"), jsonObject.getString("mobile_no"),
                                    jsonObject.getString("email"), jsonObject.getString("status"));


                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table Distributors is syncing");
                                }
                            });
                        }


                        for (int i = 0; i < reasons.length(); i++) {

                            JSONObject jsonObject = reasons.getJSONObject(i);

                            if (!jsonObject.getString("status").equalsIgnoreCase("active")) {
                                dbvoc.getDeleteNOOrder(jsonObject.getString("code"));
                            }

                            if (jsonObject.getString("status").equalsIgnoreCase("active")) {
                                loginDataBaseAdapter.insertno_orderReason(jsonObject.getString("code"), jsonObject.getString("desc"));
                            }


                        }

                        for (int i = 0; i < states.length(); i++) {

                            JSONObject jsonObject = states.getJSONObject(i);

                            loginDataBaseAdapter.insertStates("", "", jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getString("status"), "",
                                    "", "", jsonObject.getString("code"));


                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table State is syncing");
                                }
                            });
                        }

                        for (int i = 0; i < cities.length(); i++) {

                            JSONObject jsonObject = cities.getJSONObject(i);

                            loginDataBaseAdapter.insertCities("", "", jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getString("state_code"), jsonObject.getString("status"), "",
                                    "", "", "", jsonObject.getString("code"));

                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table City is syncing");
                                }
                            });

                        }

                        for (int i = 0; i < beats.length(); i++) {

                            JSONObject jsonObject = beats.getJSONObject(i);
                            loginDataBaseAdapter.insertBeats("", "", jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getString("state_code"), jsonObject.getString("city_code"), jsonObject.getString("status"), "",
                                    "", jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("code"), jsonObject.getString("distributor_code"), jsonObject.getString("warehouse_code"));


                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table Beat is syncing");
                                }
                            });

                        }

                        for (int i = 0; i < Survey_Questions.length(); i++) {

                            JSONObject jsonObject = Survey_Questions.getJSONObject(i);

                            loginDataBaseAdapter.insert_Survey_Questions(jsonObject.getString("survey_code"), jsonObject.getString("question_code"), jsonObject.getString("active_from"), jsonObject.getString("active_to"), jsonObject.getString("question"), jsonObject.getString("option1"), jsonObject.getString("option2"), jsonObject.getString("option3"), jsonObject.getString("option4"), jsonObject.getString("option5"), "", "");

                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table Servey is syncing");
                                }
                            });

                        }

                        for (int i = 0; i < distributor_beats.length(); i++) {

                            JSONObject jsonObject = distributor_beats.getJSONObject(i);

                            loginDataBaseAdapter.insert_DistriButorBeat(jsonObject.getString("code"), jsonObject.getString("distributor_code"), jsonObject.getString("beat_code"), jsonObject.getString("status"));

                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table Distributor Beat is syncing");
                                }
                            });

                        }

                        for (int i = 0; i < credit_profile.length(); i++) {

                            JSONObject jsonObject = credit_profile.getJSONObject(i);

                            loginDataBaseAdapter.insert_credit_profile("", jsonObject.getString("customer_code"), jsonObject.getString("customer_code"), "", "", "", "", jsonObject.getString("credit_limit"), jsonObject.getString("amount_outstanding"), jsonObject.getString("amount_overdue"));

                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table Credit Profile is syncing");
                                }
                            });


                        }

                        for (int i = 0; i < txn_reasons.length(); i++) {

                            JSONObject jsonObject = txn_reasons.getJSONObject(i);

                            loginDataBaseAdapter.insertReason(jsonObject.getString("id"), jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getString("description"),
                                    jsonObject.getString("detail1"));

                            ((Activity) context).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    tablename.setText("Table Reason is syncing");
                                }
                            });


                        }


                        //dbvoc.update_stockChecks(s_code,s_stock);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                        SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm:ss");

                        String Current_Date = sdf.format(c.getTime());
                        String Current_Time = sdf_time.format(c.getTime());


                        dbvoc.getDeleteTable("order_details");
                        LoginDataBaseAdapter loginDataBaseAdapter = new LoginDataBaseAdapter(context);
                        loginDataBaseAdapter = loginDataBaseAdapter.open();

                        loginDataBaseAdapter.insert_order_details(Current_Date, Current_Time);

                        final String finalResponse_result1 = response_result;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                SimpleDateFormat df_time = new SimpleDateFormat("dd-MM-yyyy hh.mm aa", Locale.ENGLISH);
                                String formattedDatetime = df_time.format(c.getTime());
                                SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                                SharedPreferences.Editor editor2 = spf.edit();
                                editor2.putString("Sync_date_time", formattedDatetime);
                                editor2.commit();

                                context.startService(new Intent(context, MyService.class));
                                Global_Data.Custom_Toast(context, context.getResources().getString(R.string.items_Sync_Successfully), "");
                                device_dialog.dismiss();
                                t_total = 0;
                                achived_total = 0;
                                getTargetDataservice(context);

                            }
                        });


                    }

                    //  finish();
                    // }

                    // output.setText(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                    device_dialog.dismiss();
                }


                // dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                //  finish();
                customdialog.dismiss();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            device_dialog.dismiss();

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    public static void getTargetDataservice(final Context contextn) {
        t_total = 0;
        achived_total = 0;


        calendarn = Calendar.getInstance();
        year = calendarn.get(Calendar.YEAR);
        loginDataBaseAdapter = new LoginDataBaseAdapter(contextn);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        dbvoc = new DataBaseHelper(contextn);
        dialog = new ProgressDialog(contextn, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(context.getResources().getString(R.string.Please_Wait));
        dialog.setTitle(contextn.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        SharedPreferences spf = contextn.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

        try {

            SharedPreferences sp = contextn.getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";
            device_id = sp.getString("devid", "");
            String domain = service_url;

            Log.i("volley", "domain: " + domain);
            Log.i("volley", "email: " + user_email);
            Log.i("target url", "target url " + domain + "targets?email=" + user_email);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(domain + "targets?email=" + user_email, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);
                    //  Log.i("volley", "response reg Length: " + response.length());

                    try {
                        //   for (int a = 0; a < response.length(); a++) {

//	                        JSONObject person = (JSONObject) response.getJSONArray(response);
                        //
                        //   String name = response.getString("result44");

                        String response_result = "";
                        if (response.has("result")) {
                            response_result = response.getString("result");
                        } else {
                            response_result = "data";
                        }


                        if (response_result.equalsIgnoreCase("User doesn't exist")) {


                            Global_Data.Custom_Toast(contextn, response_result, "");

                        } else {

                            dbvoc.getDeleteTable("targets");

                            JSONArray targets = response.getJSONArray("targets");


                            Log.i("volley", "response reg targets Length: " + targets.length());

                            Log.d("States", "targets" + targets.toString());

                            //
                            for (int i = 0; i < targets.length(); i++) {

                                JSONObject jsonObject = targets.getJSONObject(i);


                                loginDataBaseAdapter.insertTargets("", "", "",
                                        jsonObject.getString("year"), jsonObject.getString("month"), jsonObject.getString("target"),
                                        jsonObject.getString("achieved"), "", "");

                                if (jsonObject.getString("year").equalsIgnoreCase(String.valueOf(year))) {
                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("target").toString())) {
                                        if (!jsonObject.getString("target").equalsIgnoreCase("null") && !jsonObject.getString("target").equalsIgnoreCase(null) & !jsonObject.getString("target").equalsIgnoreCase("") & !jsonObject.getString("target").equalsIgnoreCase(" ")) {
                                            t_total += Float.valueOf(jsonObject.getString("target").toString());
                                        } else {
                                            t_total += Float.valueOf("0.0");
                                        }
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(jsonObject.getString("achieved").toString())) {
                                        if (!jsonObject.getString("achieved").equalsIgnoreCase("null") && !jsonObject.getString("achieved").equalsIgnoreCase(null) & !jsonObject.getString("achieved").equalsIgnoreCase("") & !jsonObject.getString("achieved").equalsIgnoreCase(" ")) {
                                            achived_total += Float.valueOf(jsonObject.getString("achieved").toString());
                                        } else {
                                            achived_total += Float.valueOf("0.0");
                                        }
                                    }
                                }
                            }

                            SharedPreferences spf = contextn.getSharedPreferences("SimpleLogic", 0);
                            SharedPreferences.Editor editor = spf.edit();

                            //editor.putString("UserID", "admin");
                            //editor.putString("pwd", "test");
                            editor.putFloat("Target", t_total);
                            editor.putFloat("Achived", achived_total);
                            //editor.putString("SimID", simSerial);
                            editor.commit();
                            dialog.dismiss();

//                            if(Global_Data.sync == "yes" && Global_Data.sync!=""){
                                Intent intentn = new Intent(context, MainActivity.class);
                                context.startActivity(intentn);
                                //((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                ((Activity) context).finish();
//                            }else {
//                                Intent intentn = new Intent(context, ViewpagerActivity.class);
//                                context.startActivity(intentn);
//                                //((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                                ((Activity) context).finish();

                            //}


//                            Intent intent = new Intent(getActivity(), Target.class);
//                            startActivity(intent);
//                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                            //finish();

                        }


                        // }

                        // output.setText(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        Intent intentn = new Intent(context, MainActivity.class);
                        context.startActivity(intentn);
                        //((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        ((Activity) context).finish();

                    }


                    dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.i("volley", "error: " + error);
                    //Toast.makeText(contextn, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();
                    Intent intentn = new Intent(context, MainActivity.class);
                    context.startActivity(intentn);
                    // ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    ((Activity) context).finish();
                    dialog.dismiss();

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(contextn);
            // queue.add(jsObjRequest);
            jsObjRequest.setShouldCache(false);
            int socketTimeout = 200000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);

        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();

        }
    }

    public static class OrderQuoteAyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // Here you can show progress bar or something on the similar lines.
            // Since you are in a UI thread here.
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // After completing execution of given task, control will return here.
            // Hence if you want to populate UI elements with fetched data, do it here.
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // You can track you progress update here
        }

        @Override
        protected Void doInBackground(Void... params) {


            String uploadImage = "";
            dbvoc = new DataBaseHelper(context);

            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
            final String formattedDate = df.format(c.getTime());
            final String formattedDate2 = df2.format(c.getTime());
            JSONObject jsonBody = new JSONObject();

            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL", null);

            try {

                JSONArray customer = new JSONArray();
                JSONArray product = new JSONArray();
                JSONArray order = new JSONArray();
                JSONObject product_valuenew = new JSONObject();

                int a = 0;
                String s = "";

//                List<Local_Data> customers_contacts = dbvoc.getAllRetailer_cre();
//                if (customers_contacts.size() > 0) {
//                    //  Retailer_Flag = "true";
//                } else {
//                    // Retailer_Flag = "false";
//                }
//
//                for (Local_Data cn : customers_contacts) {
//                    JSONObject product_value = new JSONObject();
//                    product_value.put("user_email", cn.getemail());
//                    product_value.put("code", cn.getLEGACY_CUSTOMER_CODE());
//                    product_value.put("name", cn.getCUSTOMER_NAME());
//                    product_value.put("shop_name", cn.getCUSTOMER_SHOPNAME());
//                    product_value.put("address", cn.getADDRESS());
//                    product_value.put("street", cn.getSTREET());
//                    product_value.put("landmark", cn.getLANDMARK());
//                    product_value.put("pincode", cn.getPIN_CODE());
//                    product_value.put("mobile_no", cn.getMOBILE_NO());
//                    product_value.put("email", cn.getEMAIL_ADDRESS());
//                    product_value.put("status", cn.getSTATUS());
//                    product_value.put("state_code", cn.getSTATE_ID());
//                    product_value.put("city_code", cn.getCITY_ID());
//                    product_value.put("beat_code", cn.getBEAT_ID());
//                    product_value.put("vatin", cn.getvatin());
//                    product_value.put("latitude", cn.getlatitude());
//                    product_value.put("longitude", cn.getlongitude());
//                    customer.put(product_value);
//
//                }

                byte b5[];
                signature_path = "";
                other_image_path = "";

                Long randomPIN = System.currentTimeMillis();
                String PINString = String.valueOf(randomPIN);

                JSONObject product_value = new JSONObject();
                //product_value.put("order_number", cn.get_category_code());
                product_value.put("order_number", "QNO" + PINString);
                Order_number = "QNO" + PINString;
                // product_value.put("order_date", cn.getCUSTOMER_ORDER_DATE());
                // product_value.put("order_take_by", "");
                //product_value.put("customer_code", cn.get_category_id());
                product_value.put("customer_code", Global_Data.cust_str);
                product_value.put("email", user_email);
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LATITUDE) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LONGITUDE)) {

                    product_value.put("latitude", Global_Data.GLOvel_LATITUDE);
                    product_value.put("longitude", Global_Data.GLOvel_LONGITUDE);
                } else {
                    product_value.put("latitude", Global_Data.GLOvel_LATITUDE);
                    product_value.put("longitude", Global_Data.GLOvel_LONGITUDE);
                }
                product_value.put("distributor_code", Global_Data.QuoteDisId);
                product_value.put("details1", Global_Data.orderDetail1);
                product_value.put("details2", Global_Data.orderDetail2);
                product_value.put("details3", Global_Data.orderDetailName);
                product_value.put("order_category_code", "");
                product_value.put("address", Global_Data.address);
                product_value.put("offline_date", "");
                needByDate = Global_Data.orderDetail1;
                customer_id = Global_Data.cust_str;
                // product_value.put("customer_account_code", cn.getCUSTOMER_ID());
                // product_value.put("remarks", cn.getCUSTOMER_REMARKS());
                //product_value.put("signature_image_name", uploadImage);

                //if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getSignature_image())) {
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.QuoteSignaturePath)) {
                    order_image_url = Global_Data.QuoteSignaturePath.trim();
                    // File filepath = new File(cn.getimg_ordersign());
                    // String  path =  "file://"+filepath.getPath();
                    try {
                        Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(Global_Data.QuoteSignaturePath));
                        ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
                        mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
                        b5 = bos5.toByteArray();

                        String getsign_str = Base64.encodeToString(b5, Base64.DEFAULT);
                        product_value.put("signature_path", getsign_str);
                        signature_path = Global_Data.QuoteSignaturePath;

                    } catch (Exception e) {
                        e.printStackTrace();
                        product_value.put("signature_path", "");
                    }

                } else {
                    product_value.put("signature_path", "");
                }


                //if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getimg_ordersign())) {
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.QuoteOrderImage)) {
                    order_image_url = Global_Data.QuoteOrderImage.trim();
                    // File filepath = new File(cn.getimg_ordersign());
                    // String  path =  "file://"+filepath.getPath();
                    try {
                        Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(Global_Data.QuoteOrderImage));
                        ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
                        mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
                        b5 = bos5.toByteArray();

                        String getsign_str = Base64.encodeToString(b5, Base64.DEFAULT);
                        product_value.put("order_image_string", getsign_str);

                        other_image_path = Global_Data.QuoteOrderImage;

                    } catch (Exception e) {
                        e.printStackTrace();
                        product_value.put("order_image_string", "");
                    }


                } else {
                    product_value.put("order_image_string", "");
                }


                // product_value.put("signature_path", cn.getSignature_image());
                customer_id = Global_Data.cust_str;
                // product_value.put("customer_account_code", cn.getCUSTOMER_ID());
                // product_value.put("remarks", cn.getCUSTOMER_REMARKS());
                //product_value.put("signature_image_name", uploadImage);
                product_value.put("email", user_email);

//                if (cn.get_shedule_payment_mode().equalsIgnoreCase("Secondary Sales / Retail Sales")) {
//                    s = "Retail Sales";
//                } else {
//                    s = cn.get_shedule_payment_mode();
//                }
                //product_value.put("order_type", Global_Data.quoteOrderType);
                product_value.put("order_type", "Institutional Sales");
                product_value.put("quotation_id", Global_Data.quotationId);
                // product_value.put("conference_code", cn.getconference_id());
                order.put(product_value);

                JSONObject item = new JSONObject();
                item.put("order_number", Order_number);
                item.put("item_number", Global_Data.quoteItemNo);
                item.put("total_qty", Global_Data.total_qty);
                item.put("MRP", "");
                item.put("amount", Global_Data.quoteOrderAmount);

                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava("")) {
                    item.put("scheme_id", "");
                }

                item.put("detail1", "");
                item.put("detail2", "");
                item.put("detail3", "");
                item.put("detail4", "");
                item.put("detail5", "");
                item.put("detail6", "");

                total_ammount += Double.valueOf(Global_Data.quoteOrderAmount);

                //item.put("scheme_amount", cnp.get_Target_Text());
                //item.put("item_number", cnp.get_delivery_product_id());
                //item.put("discount_type", cnp.get_stocks_product_text());
                product.put(item);


//                Log.d("count", "a" + ++a);
//                //delete_order_no = cn.getORDER_NUMBER();
//                List<Local_Data> contactsproduct = dbvoc.Get_OrderProducts(cn.get_category_code());
//                for (Local_Data cnp : contactsproduct) {
//                    JSONObject item = new JSONObject();
//                    item.put("order_number", cnp.get_category_code());
//                    item.put("item_number", cnp.get_delivery_product_id());
//                    item.put("total_qty", cnp.get_stocks_product_quantity());
//                    item.put("MRP", cnp.getMRP());
//                    item.put("amount", cnp.get_Claims_amount());
//
//                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnp.getSche_code())) {
//                        item.put("scheme_id", cnp.getSche_code());
//                    }
//
//                    item.put("detail1", cnp.getRemarks());
//                    item.put("detail2", cnp.getDetail1());
//                    item.put("detail3", cnp.getDetail2());
//                    item.put("detail4", cnp.getDetail3());
//                    item.put("detail5", cnp.getDetail4());
//                    item.put("detail6", cnp.getDetail5());
//
//                    total_ammount += Double.valueOf(cnp.get_Claims_amount());
//
//                    //item.put("scheme_amount", cnp.get_Target_Text());
//                    //item.put("item_number", cnp.get_delivery_product_id());
//                    //item.put("discount_type", cnp.get_stocks_product_text());
//                    product.put(item);
//                    //Log.d("quantity","quantity"+cnp.getquantity());
//                }


//                List<Local_Data> contacts = dbvoc.GetOrders("Secondary Sales / Retail Sales", Global_Data.GLOvel_GORDER_ID);
//                //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");
//
//                for (Local_Data cn : contacts) {
//                    JSONObject product_value = new JSONObject();
//                    product_value.put("order_number", cn.get_category_code());
//
//                    Order_number = cn.get_category_code();
//                    // product_value.put("order_date", cn.getCUSTOMER_ORDER_DATE());
//                    // product_value.put("order_take_by", "");
//                    product_value.put("customer_code", cn.get_category_id());
//                    product_value.put("email", Global_Data.GLOvel_USER_EMAIL);
//                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getlatitude()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getlongitude())) {
//
//                        product_value.put("latitude", cn.getlatitude());
//                        product_value.put("longitude", cn.getlongitude());
//                    } else {
//                        product_value.put("latitude", Global_Data.GLOvel_LATITUDE);
//                        product_value.put("longitude", Global_Data.GLOvel_LONGITUDE);
//                    }
//                    product_value.put("distributor_code", cn.getDISTRIBUTER_ID());
//                    product_value.put("details1", cn.getOrder_detail1());
//                    product_value.put("details2", cn.getOrder_detail2());
//                    product_value.put("details3", cn.getOrder_detail3());
//                    product_value.put("order_category_code", cn.getOrder_category_type());
//                    product_value.put("address", Global_Data.address);
//                    product_value.put("offline_date", cn.getCreated_at());
//
//
//                    customer_id = cn.get_category_id();
//                    // product_value.put("customer_account_code", cn.getCUSTOMER_ID());
//                    // product_value.put("remarks", cn.getCUSTOMER_REMARKS());
//                    //product_value.put("signature_image_name", uploadImage);
//
//                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getSignature_image())) {
//                        order_image_url = cn.getSignature_image().trim();
//                        // File filepath = new File(cn.getimg_ordersign());
//                        // String  path =  "file://"+filepath.getPath();
//                        try {
//                            Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(cn.getSignature_image()));
//                            ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
//                            mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
//                            b5 = bos5.toByteArray();
//
//                            String getsign_str = Base64.encodeToString(b5, Base64.DEFAULT);
//                            product_value.put("signature_path", getsign_str);
//                            signature_path = cn.getSignature_image();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            product_value.put("signature_path", "");
//                        }
//
//
//                    } else {
//                        product_value.put("signature_path", "");
//                    }
//
//
//                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getimg_ordersign())) {
//                        order_image_url = cn.getimg_ordersign().trim();
//                        // File filepath = new File(cn.getimg_ordersign());
//                        // String  path =  "file://"+filepath.getPath();
//                        try {
//                            Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(cn.getimg_ordersign()));
//                            ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
//                            mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
//                            b5 = bos5.toByteArray();
//
//                            String getsign_str = Base64.encodeToString(b5, Base64.DEFAULT);
//                            product_value.put("order_image_string", getsign_str);
//
//                            other_image_path = cn.getimg_ordersign();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            product_value.put("order_image_string", "");
//                        }
//
//
//                    } else {
//                        product_value.put("order_image_string", "");
//                    }
//
//
//                    // product_value.put("signature_path", cn.getSignature_image());
//                    customer_id = cn.get_category_id();
//                    // product_value.put("customer_account_code", cn.getCUSTOMER_ID());
//                    // product_value.put("remarks", cn.getCUSTOMER_REMARKS());
//                    //product_value.put("signature_image_name", uploadImage);
//                    product_value.put("device_code", Global_Data.device_id);
//
//                    if (cn.get_shedule_payment_mode().equalsIgnoreCase("Secondary Sales / Retail Sales")) {
//                        s = "Retail Sales";
//                    } else {
//                        s = cn.get_shedule_payment_mode();
//                    }
//                    product_value.put("order_type", s);
//                    // product_value.put("conference_code", cn.getconference_id());
//                    order.put(product_value);
//                    Log.d("count", "a" + ++a);
//                    //delete_order_no = cn.getORDER_NUMBER();
//                    List<Local_Data> contactsproduct = dbvoc.Get_OrderProducts(cn.get_category_code());
//                    for (Local_Data cnp : contactsproduct) {
//                        JSONObject item = new JSONObject();
//                        item.put("order_number", cnp.get_category_code());
//                        item.put("item_number", cnp.get_delivery_product_id());
//                        item.put("total_qty", cnp.get_stocks_product_quantity());
//                        item.put("MRP", cnp.getMRP());
//                        item.put("amount", cnp.get_Claims_amount());
//
//                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnp.getSche_code())) {
//                            item.put("scheme_id", cnp.getSche_code());
//                        }
//
//                        item.put("detail1", cnp.getRemarks());
//                        item.put("detail2", cnp.getDetail1());
//                        item.put("detail3", cnp.getDetail2());
//                        item.put("detail4", cnp.getDetail3());
//                        item.put("detail5", cnp.getDetail4());
//                        item.put("detail6", cnp.getDetail5());
//
//                        total_ammount += Double.valueOf(cnp.get_Claims_amount());
//
//                        //item.put("scheme_amount", cnp.get_Target_Text());
//                        //item.put("item_number", cnp.get_delivery_product_id());
//                        //item.put("discount_type", cnp.get_stocks_product_text());
//                        product.put(item);
//                        //Log.d("quantity","quantity"+cnp.getquantity());
//                    }
//                }

//                List<Local_Data> contacts = dbvoc.GetOrders("Secondary Sales / Retail Sales", Global_Data.GLOvel_GORDER_ID);
//
//                //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");
//
//                for (Local_Data cn : contacts) {
//
//                    Global_Data.p_code.clear();
//                    Global_Data.p_mrp.clear();
//                    Global_Data.p_amount.clear();
//                    Global_Data.p_qty.clear();
//
//                    List<Local_Data> contactsproduct = dbvoc.Get_OrderProducts(cn.get_category_code());
//                    for (Local_Data cnp : contactsproduct) {
//                        JSONObject item2 = new JSONObject();
//                        item2.put("order_number", cnp.get_category_code());
//                        item2.put("item_number", cnp.get_delivery_product_id());
//                        item2.put("total_qty", cnp.get_stocks_product_quantity());
//                        item2.put("MRP", cnp.getMRP());
//                        item2.put("amount", cnp.get_Claims_amount());
//
//                        Global_Data.p_code.add(cnp.get_delivery_product_id());
//                        Global_Data.p_mrp.add(cnp.getMRP());
//                        Global_Data.p_amount.add(cnp.get_Claims_amount());
//                        Global_Data.p_qty.add(cnp.get_stocks_product_quantity());
//
//                    }
//                }


                product_valuenew.put("orders", order);
                product_valuenew.put("order_products", product);
                product_valuenew.put("customers", customer);
                product_valuenew.put("email", user_email);
                Log.d("customers", customer.toString());

                Log.d("Orders", order.toString());

                Log.d("order_products", product.toString());

                Log.d("product_valuenew", product_valuenew.toString());

                // HashMap<String, String> params = new HashMap<String, String>();
                //params.put("token", json.toString());


                // RequestQueue queue = Volley.newRequestQueue(getBaseContext());
                // PreferencesHelper Prefs = new PreferencesHelper(MasterSyncData.this);


                //String URL = Prefs.GetPreferences("URL");
                SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
                String Cust_domain = sp.getString("Cust_Service_Url", "");
                String service_url = Cust_domain + "metal/api/v1/";

                String domain = service_url;

                Log.i("volley", "domain: " + domain);
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "orders/save_orders", product_valuenew, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("volley", "response: " + response);


                        String response_result = "";
                        //if (response.has("result")) {
                        try {
                            response_result = response.getString("result");

//                         if (response_result.equalsIgnoreCase("Device not found.")) {
//                             Toast toast = Toast.makeText(context, "Device Not Found", Toast.LENGTH_LONG);
//                             toast.setGravity(Gravity.CENTER, 0, 0);
//                             toast.show();
//                             dialog.dismiss();
//                         }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {

                                    dialog.dismiss();
                                }
                            });
                        }

                        if (response_result.equalsIgnoreCase("Device not found.")) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {


                                    Global_Data.Custom_Toast(context, "Device Not Found", "yes");
                                    dialog.dismiss();


                                }
                            });

                        } else {
//                 else
//                 {
//                     response_result = "data";
//                 }


                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {


                                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Order_Sync_Successfully), "");
                                    dialog.dismiss();


                                }
                            });
                            mobile_numbers.clear();

                            if (!Global_Data.customer_MobileNumber.equalsIgnoreCase(null) && !Global_Data.customer_MobileNumber.equalsIgnoreCase("null") && !Global_Data.customer_MobileNumber.equalsIgnoreCase("") && !Global_Data.customer_MobileNumber.equalsIgnoreCase(" ")) {
                                mobile_numbers.add(Global_Data.customer_MobileNumber);
                            }

                            if (!Global_Data.cus_MAnager_mobile.equalsIgnoreCase(null) && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("null") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase(" ")) {
                                mobile_numbers.add(Global_Data.cus_MAnager_mobile);
                            }

                            String gaddress = "";
                            try {
                                if (Global_Data.address.equalsIgnoreCase("null")) {
                                    gaddress = "";
                                } else {
                                    gaddress = Global_Data.address;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {


                                        dialog.dismiss();
                                    }
                                });
                            }
                            String sms_body = "Dear " + Global_Data.CUSTOMER_NAME_NEW + " ," + "\n" + " Thank you for your order for " + Global_Data.order_retailer + " at " + Global_Data.CUSTOMER_ADDRESS_NEW + " at " + formattedDate + " for Rs. " + String.valueOf(total_ammount) + "." + "\n\n" + " Thank you." + "\n" + " " + Global_Data.USER_FIRST_NAME + " " + Global_Data.USER_LAST_NAME + "\n" + " " + gaddress;

                            //sendSMS("8454858739",sms_body,context);

                            //Uri uri = Uri.parse("file://"+Environment.getExternalStorageDirectory()+"/test.png");
                            //sendLongSMS("8454858739",sms_body,context);


                            if (!mobile_numbers.isEmpty() && mobile_numbers.size() > 0) {

                                for (int i = 0; i < mobile_numbers.size(); i++) {
                                    String message = sms_body;
                                    String tempMobileNumber = mobile_numbers.get(i).toString();
                                    //  Global_Data.sendSMS(tempMobileNumber, message,context);
                                }
                            }

                            // dbvoc.getDeleteTable("order_products");
                            //dbvoc.getDeleteTable("orders");
                            List<Local_Data> contactsn = dbvoc.Getcustomer_email(customer_id);
                            //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");
                            for (Local_Data cn : contactsn) {
                                email_adress = cn.get_Description();
                            }
                            Global_Data.GLOvel_GORDER_ID = "";
                            String val = "";
                            dbvoc.updateCustomerby_CreateAt(val);
                            dbvoc.getDeleteTableorder_bycustomer(Global_Data.order_retailer.trim(), "Secondary Sales / Retail Sales", Order_number);
                            dbvoc.getDeleteTableorderproduct_bycustomer(Global_Data.order_retailer.trim(), "Secondary Sales / Retail Sales", Order_number);

                            try {
                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(signature_path)) {
                                    Uri uri = Uri.parse(signature_path);
                                    File fdelete = new File(uri.getPath());
                                    if (fdelete.exists()) {
                                        if (fdelete.delete()) {
                                            System.out.println("file Deleted :" + signature_path);
                                        } else {
                                            System.out.println("file not Deleted :" + signature_path);
                                        }
                                    }
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(other_image_path)) {

                                    Uri uri = Uri.parse(other_image_path);
                                    File fdelete = new File(uri.getPath());
                                    if (fdelete.exists()) {
                                        if (fdelete.delete()) {
                                            System.out.println("file Deleted :" + other_image_path);
                                        } else {
                                            System.out.println("file not Deleted :" + other_image_path);
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {

                                    dialog.dismiss();
                                    String order_idd = Global_Data.GLObalOrder_id;
                                    Global_Data.GLObalOrder_id = "";
                                    final Dialog dialog1 = new Dialog(context);
                                    dialog1.setCancelable(false);

                                    //tell the Dialog to use the dialog.xml as it's layout description
                                    dialog1.setContentView(R.layout.dialog);
                                    dialog1.setTitle(context.getResources().getString(R.string.Order_Status));

                                    TextView txt = (TextView) dialog1.findViewById(R.id.txtOrderID);

                                    txt.setText(context.getResources().getString(R.string.Order_is_generated));
                                    TextView txtMessage = (TextView) dialog1.findViewById(R.id.txtMessage);
                                    TextView txtEmail = (TextView) dialog1.findViewById(R.id.txtEmail);

                                    txtEmail.setText(context.getResources().getString(R.string.Mail_has_been_sent_to) + email_adress);
                                    if (!mobile_numbers.isEmpty() && mobile_numbers.size() > 0) {
                                        txtMessage.setText(context.getResources().getString(R.string.Sms_Send_Successfully));
                                    }

                                    ImageView dialogButton = (ImageView) dialog1.findViewById(R.id.dialogButton);
                                    dialogButton.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog1.dismiss();
                                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            //getTargetDataservice(context);
                                            Intent intentn = new Intent(context, Invoice_order.class);
                                            intentn.putExtra("order_date", formattedDate2);
                                            intentn.putExtra("order_number", order_idd); //Order_number
                                            intentn.putExtra("total_ammount", total_ammount);
                                            try {
                                                if (!needByDate.equalsIgnoreCase("")) {
                                                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                                    Date newDate = df.parse(needByDate);
                                                    String formattedNeedDate = df2.format(newDate);
                                                    intentn.putExtra("need_bydate", formattedNeedDate);
                                                } else {
                                                    intentn.putExtra("need_bydate", needByDate);
                                                }
                                            } catch (Exception ex) {
                                                intentn.putExtra("need_bydate", needByDate);
                                                ex.printStackTrace();
                                            }
                                            context.startActivity(intentn);
                                            ((Activity) context).finish();
                                        }
                                    });
                                    dialog1.show();
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {


                                    Global_Data.Custom_Toast(context,
                                            context.getResources().getString(R.string.internet_connection_error), "");
                                }
                            });
                        } else if (error instanceof AuthFailureError) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {


                                    Global_Data.Custom_Toast(context,
                                            "Server AuthFailureError  Error", "");
                                }
                            });
                        } else if (error instanceof ServerError) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {


                                    Global_Data.Custom_Toast(context,
                                            context.getResources().getString(R.string.Server_Errors), "");
                                }
                            });
                        } else if (error instanceof NetworkError) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {


                                    Global_Data.Custom_Toast(context,
                                            context.getResources().getString(R.string.internet_connection_error), "");
                                }
                            });
                        } else if (error instanceof ParseError) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {


                                    Global_Data.Custom_Toast(context,
                                            "ParseError   Error", "");
                                }
                            });
                        } else {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {


                                    Global_Data.Custom_Toast(context, error.getMessage(), "");
                                }
                            });

                        }
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                            }
                        });
                        // finish();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                // queue.add(jsObjRequest);
                int socketTimeout = 30000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsObjRequest.setRetryPolicy(policy);
                requestQueue.add(jsObjRequest);

            } catch (JSONException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }

            return null;
        }
    }

    public static class OrderAyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // Here you can show progress bar or something on the similar lines.
            // Since you are in a UI thread here.
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // After completing execution of given task, control will return here.
            // Hence if you want to populate UI elements with fetched data, do it here.
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // You can track you progress update here
        }

        @Override
        protected Void doInBackground(Void... params) {


            String uploadImage = "";
            dbvoc = new DataBaseHelper(context);

            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
            final String formattedDate = df.format(c.getTime());
            final String formattedDate2 = df2.format(c.getTime());
            JSONObject jsonBody = new JSONObject();

            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL", null);

            try {

                JSONArray customer = new JSONArray();
                JSONArray product = new JSONArray();
                JSONArray order = new JSONArray();
                JSONObject product_valuenew = new JSONObject();

                int a = 0;
                String s = "";

//                List<Local_Data> customers_contacts = dbvoc.getAllRetailer_cre();
//                if (customers_contacts.size() > 0) {
//                    //  Retailer_Flag = "true";
//                } else {
//                    // Retailer_Flag = "false";
//                }
//
//                for (Local_Data cn : customers_contacts) {
//                    JSONObject product_value = new JSONObject();
//                    product_value.put("user_email", cn.getemail());
//                    product_value.put("code", cn.getLEGACY_CUSTOMER_CODE());
//                    product_value.put("name", cn.getCUSTOMER_NAME());
//                    product_value.put("shop_name", cn.getCUSTOMER_SHOPNAME());
//                    product_value.put("address", cn.getADDRESS());
//                    product_value.put("street", cn.getSTREET());
//                    product_value.put("landmark", cn.getLANDMARK());
//                    product_value.put("pincode", cn.getPIN_CODE());
//                    product_value.put("mobile_no", cn.getMOBILE_NO());
//                    product_value.put("email", cn.getEMAIL_ADDRESS());
//                    product_value.put("status", cn.getSTATUS());
//                    product_value.put("state_code", cn.getSTATE_ID());
//                    product_value.put("city_code", cn.getCITY_ID());
//                    product_value.put("beat_code", cn.getBEAT_ID());
//                    product_value.put("vatin", cn.getvatin());
//                    product_value.put("latitude", cn.getlatitude());
//                    product_value.put("longitude", cn.getlongitude());
//                    customer.put(product_value);
//                }

                byte b5[];
                signature_path = "";
                other_image_path = "";

                List<Local_Data> contacts = dbvoc.GetOrders("Secondary Sales / Retail Sales", Global_Data.GLOvel_GORDER_ID);

                //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");

                for (Local_Data cn : contacts) {
                    JSONObject product_value = new JSONObject();
                    product_value.put("order_number", cn.get_category_code());

                    Order_number = cn.get_category_code();
                    Global_Data.orderid = Order_number;
                    // product_value.put("order_date", cn.getCUSTOMER_ORDER_DATE());
                    // product_value.put("order_take_by", "");
                    product_value.put("customer_code", cn.get_category_id());
                    product_value.put("email", user_email);
                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getlatitude()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getlongitude())) {

                        product_value.put("latitude", cn.getlatitude());
                        product_value.put("longitude", cn.getlongitude());
                    } else {
                        product_value.put("latitude", Global_Data.GLOvel_LATITUDE);
                        product_value.put("longitude", Global_Data.GLOvel_LONGITUDE);
                    }
                    product_value.put("distributor_code", cn.getDISTRIBUTER_ID());
                    product_value.put("details1", cn.getOrder_detail1());
                    product_value.put("details2", cn.getOrder_detail2());
                    product_value.put("details3", cn.getOrder_detail3());
                    product_value.put("order_category_code", cn.getOrder_category_type());
                    product_value.put("address", Global_Data.address);
                    product_value.put("offline_date", cn.getCreated_at());
                    needByDate = cn.getOrder_detail1();

                    customer_id = cn.get_category_id();
                    // product_value.put("customer_account_code", cn.getCUSTOMER_ID());
                    // product_value.put("remarks", cn.getCUSTOMER_REMARKS());
                    //product_value.put("signature_image_name", uploadImage);

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getSignature_image())) {
                        order_image_url = cn.getSignature_image().trim();
                        // File filepath = new File(cn.getimg_ordersign());
                        // String  path =  "file://"+filepath.getPath();
                        try {
                            Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(cn.getSignature_image()));
                            ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
                            mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
                            b5 = bos5.toByteArray();

                            String getsign_str = Base64.encodeToString(b5, Base64.DEFAULT);
                            product_value.put("signature_path", getsign_str);
                            signature_path = cn.getSignature_image();

                        } catch (Exception e) {
                            e.printStackTrace();
                            product_value.put("signature_path", "");
                        }


                    } else {
                        product_value.put("signature_path", "");
                    }


                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getimg_ordersign())) {
                        order_image_url = cn.getimg_ordersign().trim();
                        // File filepath = new File(cn.getimg_ordersign());
                        // String  path =  "file://"+filepath.getPath();
                        try {
                            Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(cn.getimg_ordersign()));
                            ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
                            mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
                            b5 = bos5.toByteArray();

                            String getsign_str = Base64.encodeToString(b5, Base64.DEFAULT);
                            product_value.put("order_image_string", getsign_str);
                            other_image_path = cn.getimg_ordersign();

                        } catch (Exception e) {
                            e.printStackTrace();
                            product_value.put("order_image_string", "");
                        }


                    } else {
                        product_value.put("order_image_string", "");
                    }


                    // product_value.put("signature_path", cn.getSignature_image());
                    customer_id = cn.get_category_id();
                    // product_value.put("customer_account_code", cn.getCUSTOMER_ID());
                    // product_value.put("remarks", cn.getCUSTOMER_REMARKS());
                    //product_value.put("signature_image_name", uploadImage);
                    product_value.put("email", user_email);

                    if (cn.get_shedule_payment_mode().equalsIgnoreCase("Secondary Sales / Retail Sales")) {
                        s = "Retail Sales";
                    } else {
                        s = cn.get_shedule_payment_mode();
                    }
                    product_value.put("order_type", s);
                    // product_value.put("conference_code", cn.getconference_id());
                    order.put(product_value);
                    Log.d("count", "a" + ++a);
                    //delete_order_no = cn.getORDER_NUMBER();

                    Global_Data.p_code.clear();
                    Global_Data.p_mrp.clear();
                    Global_Data.p_amount.clear();
                    Global_Data.p_qty.clear();

                    List<Local_Data> contactsproduct = dbvoc.Get_OrderProducts(cn.get_category_code());
                    for (Local_Data cnp : contactsproduct) {
                        JSONObject item = new JSONObject();
                        item.put("order_number", cnp.get_category_code());
                        item.put("item_number", cnp.get_delivery_product_id());
                        item.put("total_qty", cnp.get_stocks_product_quantity());
                        item.put("MRP", cnp.getMRP());
                        item.put("amount", cnp.get_Claims_amount());

                        Global_Data.p_code.add(cnp.get_delivery_product_id());
                        Global_Data.p_mrp.add(cnp.getMRP());
                        Global_Data.p_amount.add(cnp.get_Claims_amount());
                        Global_Data.p_qty.add(cnp.get_stocks_product_quantity());

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnp.getSche_code())) {
                            item.put("scheme_id", cnp.getSche_code());
                        }

                        item.put("detail1", cnp.getRemarks());
                        item.put("detail2", cnp.getDetail1());
                        item.put("detail3", cnp.getDetail2());
                        item.put("detail4", cnp.getDetail3());
                        item.put("detail5", cnp.getDetail4());
                        item.put("detail6", cnp.getDetail5());

                        total_ammount += Double.valueOf(cnp.get_Claims_amount());

                        //item.put("scheme_amount", cnp.get_Target_Text());
                        //item.put("item_number", cnp.get_delivery_product_id());
                        //item.put("discount_type", cnp.get_stocks_product_text());
                        product.put(item);
                        //Log.d("quantity","quantity"+cnp.getquantity());
                    }
                }
                product_valuenew.put("orders", order);
                product_valuenew.put("order_products", product);
                product_valuenew.put("customers", customer);
                product_valuenew.put("email", user_email);
                Log.d("customers", customer.toString());

                Log.d("Orders", order.toString());

                Log.d("order_products", product.toString());

                Log.d("product_valuenew", product_valuenew.toString());

                // HashMap<String, String> params = new HashMap<String, String>();
                //params.put("token", json.toString());


                // RequestQueue queue = Volley.newRequestQueue(getBaseContext());
                // PreferencesHelper Prefs = new PreferencesHelper(MasterSyncData.this);


                //String URL = Prefs.GetPreferences("URL");
                SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
                String Cust_domain = sp.getString("Cust_Service_Url", "");
                String service_url = Cust_domain + "metal/api/v1/";

                String domain = service_url;

                Log.i("volley", "domain: " + domain + "orders/save_orders" + product_valuenew.toString());
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "orders/save_orders", product_valuenew, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("volley", "response: " + response);


                        String response_result = "";
                        //if (response.has("result")) {
                        try {
                            response_result = response.getString("result");

//                         if (response_result.equalsIgnoreCase("Device not found.")) {
//                             Toast toast = Toast.makeText(context, "Device Not Found", Toast.LENGTH_LONG);
//                             toast.setGravity(Gravity.CENTER, 0, 0);
//                             toast.show();
//                             dialog.dismiss();
//                         }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {

                                    dialog.dismiss();
                                }
                            });
                        }

                        if (response_result.equalsIgnoreCase("Device not found.")) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {


                                    Global_Data.Custom_Toast(context, "Device Not Found", "yes");
                                    dialog.dismiss();


                                }
                            });

                        } else {
//                 else
//                 {
//                     response_result = "data";
//                 }


                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {
                                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Order_Sync_Successfully), "");
                                    dialog.dismiss();
                                }
                            });
                            mobile_numbers.clear();

                            if (!Global_Data.customer_MobileNumber.equalsIgnoreCase(null) && !Global_Data.customer_MobileNumber.equalsIgnoreCase("null") && !Global_Data.customer_MobileNumber.equalsIgnoreCase("") && !Global_Data.customer_MobileNumber.equalsIgnoreCase(" ")) {
                                mobile_numbers.add(Global_Data.customer_MobileNumber);
                            }

                            if (!Global_Data.cus_MAnager_mobile.equalsIgnoreCase(null) && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("null") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase(" ")) {
                                mobile_numbers.add(Global_Data.cus_MAnager_mobile);
                            }

                            String gaddress = "";
                            try {
                                if (Global_Data.address.equalsIgnoreCase("null")) {
                                    gaddress = "";
                                } else {
                                    gaddress = Global_Data.address;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {


                                        dialog.dismiss();
                                    }
                                });
                            }
                            String sms_body = "Dear " + Global_Data.CUSTOMER_NAME_NEW + " ," + "\n" + " Thank you for your order for " + Global_Data.order_retailer + " at " + Global_Data.CUSTOMER_ADDRESS_NEW + " at " + formattedDate + " for Rs. " + String.valueOf(total_ammount) + "." + "\n\n" + " Thank you." + "\n" + " " + Global_Data.USER_FIRST_NAME + " " + Global_Data.USER_LAST_NAME + "\n" + " " + gaddress;

                            //sendSMS("8454858739",sms_body,context);

                            //Uri uri = Uri.parse("file://"+Environment.getExternalStorageDirectory()+"/test.png");
                            //sendLongSMS("8454858739",sms_body,context);


                            if (!mobile_numbers.isEmpty() && mobile_numbers.size() > 0) {

                                for (int i = 0; i < mobile_numbers.size(); i++) {
                                    String message = sms_body;
                                    String tempMobileNumber = mobile_numbers.get(i).toString();
                                    //  Global_Data.sendSMS(tempMobileNumber, message,context);
                                }
                            }

                            // dbvoc.getDeleteTable("order_products");
                            //dbvoc.getDeleteTable("orders");
                            List<Local_Data> contactsn = dbvoc.Getcustomer_email(customer_id);
                            //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");
                            for (Local_Data cn : contactsn) {
                                email_adress = cn.get_Description();
                            }
                            String val = "";
                            dbvoc.updateCustomerby_CreateAt(val);
                            dbvoc.getDeleteTableorder_bycustomer(Global_Data.order_retailer.trim(), "Secondary Sales / Retail Sales", Order_number);
                            dbvoc.getDeleteTableorderproduct_bycustomer(Global_Data.order_retailer.trim(), "Secondary Sales / Retail Sales", Order_number);

                            try {
                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(signature_path)) {
                                    Uri uri = Uri.parse(signature_path);
                                    File fdelete = new File(uri.getPath());
                                    if (fdelete.exists()) {
                                        if (fdelete.delete()) {
                                            System.out.println("file Deleted :" + signature_path);
                                        } else {
                                            System.out.println("file not Deleted :" + signature_path);
                                        }
                                    }
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(other_image_path)) {

                                    Uri uri = Uri.parse(other_image_path);
                                    File fdelete = new File(uri.getPath());
                                    if (fdelete.exists()) {
                                        if (fdelete.delete()) {
                                            System.out.println("file Deleted :" + other_image_path);
                                        } else {
                                            System.out.println("file not Deleted :" + other_image_path);
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();
                                    String order_idd = Global_Data.GLOvel_GORDER_ID;
                                    Global_Data.GLOvel_GORDER_ID = "";
                                    final Dialog dialog1 = new Dialog(context);
                                    dialog1.setCancelable(false);

                                    //tell the Dialog to use the dialog.xml as it's layout description
                                    dialog1.setContentView(R.layout.dialog);
                                    dialog1.setTitle(context.getResources().getString(R.string.Order_Status));

                                    TextView txt = (TextView) dialog1.findViewById(R.id.txtOrderID);

                                    txt.setText(context.getResources().getString(R.string.Order_is_generated));
                                    TextView txtMessage = (TextView) dialog1.findViewById(R.id.txtMessage);
                                    TextView txtEmail = (TextView) dialog1.findViewById(R.id.txtEmail);
                                    viewKonfetti = dialog1.findViewById(R.id.viewKonfetti);

                                    txtEmail.setText(context.getResources().getString(R.string.Mail_has_been_sent_to) + email_adress);
                                    // if (!mobile_numbers.isEmpty() && mobile_numbers.size() > 0) {
                                    txtMessage.setText(context.getResources().getString(R.string.Sms_Send_Successfully));
                                    // }


                                    ImageView dialogButton = (ImageView) dialog1.findViewById(R.id.dialogButton);

                                    dialogButton.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            dialog1.dismiss();
                                            // getTargetDataservice(context);
                                            Intent intentn = new Intent(context, Invoice_order.class);
                                            intentn.putExtra("order_date", formattedDate2);
                                            intentn.putExtra("order_number", order_idd);
                                            intentn.putExtra("total_ammount", total_ammount);

                                            try {
                                                if (!needByDate.equalsIgnoreCase("")) {
                                                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                                    Date newDate = df.parse(needByDate);
                                                    String formattedNeedDate = df2.format(newDate);
                                                    intentn.putExtra("need_bydate", formattedNeedDate);
                                                } else {
                                                    intentn.putExtra("need_bydate", needByDate);
                                                }

                                            } catch (Exception ex) {
                                                intentn.putExtra("need_bydate", needByDate);
                                                ex.printStackTrace();
                                            }

                                            context.startActivity(intentn);
                                            ((Activity) context).finish();

//                                            viewKonfetti.setVisibility(View.VISIBLE);
//
//                                            viewKonfetti.build()
//                                                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
//                                                    .setDirection(0.0, 359.0)
//                                                    .setSpeed(1f, 5f)
//                                                    .setFadeOutEnabled(true)
//                                                    .setTimeToLive(2000L)
//                                                    .addShapes(Shape.RECT, Shape.CIRCLE)
//                                                    .addSizes(new Size(12, 5))
//                                                    .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
//                                                    .streamFor(300, 5000L);
//
//                                            handler.postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                     dialog1.dismiss();
//                                                     getTargetDataservice(context);
//                                                }
//                                            }, 5000);


                                        }
                                    });

                                    dialog1.show();
                                }
                            });


                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {

                                    Global_Data.Custom_Toast(context,
                                            context.getResources().getString(R.string.internet_connection_error), "");
                                }
                            });
                        } else if (error instanceof AuthFailureError) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {

                                    Global_Data.Custom_Toast(context,
                                            "Server AuthFailureError  Error", "");
                                }
                            });
                        } else if (error instanceof ServerError) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {


                                    Global_Data.Custom_Toast(context,
                                            context.getResources().getString(R.string.Server_Errors), "");
                                }
                            });
                        } else if (error instanceof NetworkError) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {


                                    Global_Data.Custom_Toast(context,
                                            context.getResources().getString(R.string.internet_connection_error), "");
                                }
                            });
                        } else if (error instanceof ParseError) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {


                                    Global_Data.Custom_Toast(context,
                                            "ParseError   Error", "");
                                }
                            });
                        } else {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {


                                    Global_Data.Custom_Toast(context, error.getMessage(), "");
                                }
                            });

                        }
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
                            }
                        });
                        // finish();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                // queue.add(jsObjRequest);
                int socketTimeout = 30000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsObjRequest.setRetryPolicy(policy);
                requestQueue.add(jsObjRequest);

            } catch (JSONException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

            }

            return null;
        }
    }

    public static class AllOrderAyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // Here you can show progress bar or something on the similar lines.
            // Since you are in a UI thread here.
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
                //dialog.dismiss();
            }

            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage(context.getResources().getString(R.string.Order_Sync_in_Progress));
            dialog.setTitle(context.getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // After completing execution of given task, control will return here.
            // Hence if you want to populate UI elements with fetched data, do it here.
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // You can track you progress update here
        }

        @Override
        protected Void doInBackground(Void... params) {

            PRODUCTOrder_ids.clear();
            //ArrayList productList = new ArrayList();
//            final ProgressDialog dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            String uploadImage = "";
            dbvoc = new DataBaseHelper(context);
            final Calendar c = Calendar.getInstance();

            //String strDate = sdf.format(c.getTime());


            final ArrayList<String> order_results = new ArrayList<String>();


            final JSONObject jsonBody = new JSONObject();


            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL", null);

            try {

                JSONArray customer = new JSONArray();
                JSONArray product = new JSONArray();
                JSONArray order = new JSONArray();

                JSONArray product_return = new JSONArray();
                JSONArray order_return = new JSONArray();

                JSONArray no_orders = new JSONArray();

                JSONObject product_valuenew = new JSONObject();

                int a = 0;
                int order_count = 0;
                String s = "";

//                List<Local_Data> customers_contacts = dbvoc.getAllRetailer_cre();
//                if (customers_contacts.size() > 0) {
//                    Retailer_Flag = "true";
//                } else {
//                    Retailer_Flag = "false";
//                }
//
//                for (Local_Data cn : customers_contacts) {
//                    JSONObject product_value = new JSONObject();
//                    product_value.put("user_email", cn.getemail());
//                    product_value.put("code", cn.getLEGACY_CUSTOMER_CODE());
//                    product_value.put("name", cn.getCUSTOMER_NAME());
//                    product_value.put("shop_name", cn.getCUSTOMER_SHOPNAME());
//                    product_value.put("address", cn.getADDRESS());
//                    product_value.put("street", cn.getSTREET());
//                    product_value.put("landmark", cn.getLANDMARK());
//                    product_value.put("pincode", cn.getPIN_CODE());
//                    product_value.put("mobile_no", cn.getMOBILE_NO());
//                    product_value.put("email", cn.getEMAIL_ADDRESS());
//                    product_value.put("status", cn.getSTATUS());
//                    product_value.put("state_code", cn.getSTATE_ID());
//                    product_value.put("city_code", cn.getCITY_ID());
//                    product_value.put("beat_code", cn.getBEAT_ID());
//                    product_value.put("vatin", cn.getvatin());
//                    product_value.put("latitude", cn.getlatitude());
//                    product_value.put("longitude", cn.getlongitude());
//                    customer.put(product_value);
//
//                }


                List<Local_Data> contacts = dbvoc.GetAllOrders("Secondary Sales / Retail Sales");

                if (contacts.size() > 0) {
                    order_Flag = "true";
                } else {
                    order_Flag = "false";
                }

                byte b5[];
                //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");
                for (Local_Data cn : contacts) {
                    JSONObject product_value = new JSONObject();
                    product_value.put("order_number", cn.get_category_code());

                    Order_number = cn.get_category_code();
                    Global_Data.GLOvel_GORDER_ID = Order_number;

                    order_results.add(Order_number);
                    ++order_count;
                    // product_value.put("order_date", cn.getCUSTOMER_ORDER_DATE());
                    // product_value.put("order_take_by", "");
                    product_value.put("customer_code", cn.get_category_id());
                    Global_Data.GLOvel_CUSTOMER_ID = cn.get_category_id();

                    product_value.put("email", cn.getuser_email());
                    product_value.put("distributor_code", cn.getDISTRIBUTER_ID());

                    product_value.put("details1", cn.getOrder_detail1());
                    product_value.put("details2", cn.getOrder_detail2());
                    product_value.put("details3", cn.getOrder_detail3());
                    product_value.put("order_category_code", cn.getOrder_category_type());
                    product_value.put("offline_date", cn.getCreated_at());

                    needByDate = cn.getOrder_detail1();

                    // product_value.put("signature", cn.getSignature_image());
                    //product_value.put("distributor_id", cn.getDISTRIBUTER_ID());

                    customer_id = cn.get_category_id();
                    // product_value.put("customer_account_code", cn.getCUSTOMER_ID());
                    // product_value.put("remarks", cn.getCUSTOMER_REMARKS());
                    //product_value.put("signature_image_name", uploadImage);
                    product_value.put("email", user_email);

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getlatitude()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getlongitude())) {

                        product_value.put("latitude", cn.getlatitude());
                        product_value.put("longitude", cn.getlongitude());

                        try {
                            LocationAddress locationAddress = new LocationAddress();
                            LocationAddress.getAddressFromLocation(Double.valueOf(cn.getlatitude()), Double.valueOf(cn.getlongitude()),
                                    context, new GeocoderHandler());
                            product_value.put("address", Global_Data.address);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            addressn(Double.valueOf(cn.getlatitude()), Double.valueOf(cn.getlongitude()), context);
                            product_value.put("address", Global_Data.address);
                        }

                    } else {
                        product_value.put("latitude", Global_Data.GLOvel_LATITUDE);
                        product_value.put("longitude", Global_Data.GLOvel_LONGITUDE);

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(String.valueOf(Global_Data.GLOvel_LATITUDE)) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(String.valueOf(Global_Data.GLOvel_LONGITUDE))) {

                            try {
                                LocationAddress locationAddress = new LocationAddress();
                                LocationAddress.getAddressFromLocation(Double.valueOf(Global_Data.GLOvel_LATITUDE), Double.valueOf(Global_Data.GLOvel_LONGITUDE),
                                        context, new GeocoderHandler());
                                product_value.put("address", Global_Data.address);

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                addressn(Double.valueOf(Global_Data.GLOvel_LATITUDE), Double.valueOf(Global_Data.GLOvel_LONGITUDE), context);
                                product_value.put("address", Global_Data.address);
                            }
                        }
                    }


                    // product_value.put("signature_path", cn.getSignature_image());
                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getSignature_image())) {
                        order_image_url = cn.getSignature_image().trim();
                        // File filepath = new File(cn.getimg_ordersign());
                        // String  path =  "file://"+filepath.getPath();
                        try {
                            Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(cn.getSignature_image()));
                            ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
                            mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
                            b5 = bos5.toByteArray();

                            String getsign_str = Base64.encodeToString(b5, Base64.DEFAULT);
                            product_value.put("signature_path", getsign_str);

                        } catch (Exception e) {
                            e.printStackTrace();
                            product_value.put("signature_path", "");
                        }


                    } else {
                        product_value.put("signature_path", "");
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getimg_ordersign())) {
                        order_image_url = cn.getimg_ordersign().trim();
                        // File filepath = new File(cn.getimg_ordersign());
                        // String  path =  "file://"+filepath.getPath();
                        try {
                            Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(cn.getimg_ordersign()));
                            ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
                            mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
                            b5 = bos5.toByteArray();

                            String getsign_str = Base64.encodeToString(b5, Base64.DEFAULT);
                            product_value.put("order_image_string", getsign_str);

                        } catch (Exception e) {
                            e.printStackTrace();
                            product_value.put("order_image_string", "");
                        }


                    } else {
                        product_value.put("order_image_string", "");
                    }

                    if (cn.get_shedule_payment_mode().equalsIgnoreCase("Secondary Sales / Retail Sales")) {
                        s = "Retail Sales";
                    } else {
                        s = cn.get_shedule_payment_mode();
                    }
                    product_value.put("order_type", s);
                    // product_value.put("conference_code", cn.getconference_id());
                    order.put(product_value);
                    Log.d("count", "a" + ++a);
                    //delete_order_no = cn.getORDER_NUMBER();
                    List<Local_Data> contactsproduct = dbvoc.Get_OrderProducts(cn.get_category_code());
                    for (Local_Data cnp : contactsproduct) {
                        JSONObject item = new JSONObject();
                        item.put("order_number", cnp.get_category_code());
                        item.put("item_number", cnp.get_delivery_product_id());
                        item.put("total_qty", cnp.get_stocks_product_quantity());
                        item.put("MRP", cnp.getMRP());
                        item.put("amount", cnp.get_Claims_amount());
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnp.getSche_code())) {
                            item.put("scheme_id", cnp.getSche_code());
                        }

                        item.put("detail1", cnp.getRemarks());
                        item.put("detail2", cnp.getDetail1());
                        item.put("detail3", cnp.getDetail2());
                        item.put("detail4", cnp.getDetail3());
                        item.put("detail5", cnp.getDetail4());
                        item.put("detail6", cnp.getDetail5());

                        PRODUCTOrder_ids.add(cnp.get_category_code());


                        //item.put("scheme_amount", cnp.get_Target_Text());
                        //item.put("item_number", cnp.get_delivery_product_id());
                        //item.put("discount_type", cnp.get_stocks_product_text());
                        product.put(item);
                        //Log.d("quantity","quantity"+cnp.getquantity());
                    }
                }

//         for (int i = 0; i < 10; i++)
//         {
//
//
//
//         }

                List<Local_Data> return_order_con = dbvoc.GetOrders_return_All("Secondary Sales / Retail Sales");

                if (return_order_con.size() > 0) {
                    return_order_Flag = "true";
                } else {
                    return_order_Flag = "false";
                }

                for (Local_Data cn : return_order_con) {
                    JSONObject product_value = new JSONObject();
                    product_value.put("order_number", cn.get_category_code());

                    Order_number = cn.get_category_code();
                    // product_value.put("order_date", cn.getCUSTOMER_ORDER_DATE());
                    // product_value.put("order_take_by", "");
                    customer_id = cn.get_category_id();
                    product_value.put("customer_code", customer_id);

                    product_value.put("email", user_email);


                    product_value.put("latitude", cn.getlatitude());
                    product_value.put("longitude", cn.getlongitude());
                    product_value.put("signature_path", cn.getSignature_image());
                    product_value.put("distributor_code", cn.getDISTRIBUTER_ID());
                    // product_value.put("customer_account_code", cn.getCUSTOMER_ID());
                    // product_value.put("remarks", cn.getCUSTOMER_REMARKS());
                    //product_value.put("signature_image_name", uploadImage);
                    product_value.put("email", user_email);
                    product_value.put("offline_date", cn.getCreated_at());

                    if (cn.get_shedule_payment_mode().equalsIgnoreCase("Secondary Sales / Retail Sales")) {
                        s = "Retail Sales";
                    } else {
                        s = cn.get_shedule_payment_mode();
                    }
                    product_value.put("order_type", s);
                    // product_value.put("conference_code", cn.getconference_id());
                    order_return.put(product_value);
                    Log.d("count", "a" + ++a);
                    //delete_order_no = cn.getORDER_NUMBER();
                    List<Local_Data> contactsproduct = dbvoc.Get_OrderProducts_return(cn.get_category_code());
                    for (Local_Data cnp : contactsproduct) {
                        JSONObject item = new JSONObject();
                        item.put("order_number", cnp.get_category_code());
                        item.put("item_number", cnp.get_delivery_product_id());
                        item.put("total_return_qty", cnp.get_stocks_product_quantity());
                        item.put("MRP", cnp.getMRP());
                        item.put("amount", cnp.get_Claims_amount());

                        ++items_count;
                        //item.put("scheme_amount", cnp.get_Target_Text());
                        //item.put("item_number", cnp.get_delivery_product_id());
                        //item.put("discount_type", cnp.get_stocks_product_text());
                        product_return.put(item);
                        //Log.d("quantity","quantity"+cnp.getquantity());
                    }
                }

                List<Local_Data> no_order_con = dbvoc.getNoOrders();

                if (no_order_con.size() > 0) {
                    no_order_Flag = "true";
                } else {
                    no_order_Flag = "false";
                }

                for (Local_Data cn : no_order_con) {
                    JSONObject product_value = new JSONObject();
                    product_value.put("customer_code", cn.getLEGACY_CUSTOMER_CODE());
                    product_value.put("order_number", cn.getorder_number());

                    if (cn.getreason_type().equalsIgnoreCase("Other")) {
                        product_value.put("reason_name", cn.getreason_code());
                    } else {
                        product_value.put("reason_code", cn.getreason_code());
                    }


                    product_value.put("user_email", cn.getuser_email());
                    product_value.put("latitude", cn.getlatitude());
                    product_value.put("longitude", cn.getlongitude());
                    product_value.put("offline_date", cn.getCreated_at());

                    no_orders.put(product_value);

                }


                if (order_Flag.equalsIgnoreCase("true") || return_order_Flag.equalsIgnoreCase("true") || no_order_Flag.equalsIgnoreCase("true") || Retailer_Flag.equalsIgnoreCase("true")) {
                    Final_Flag_ORDER_N += " " + "order";
                } else {
                    Final_Flag_ORDER_N = "";
                }

                if (Final_Flag_ORDER_N.equalsIgnoreCase("")) {

                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {


                            Global_Data.Custom_Toast(context, context.getResources().getString(R.string.No_record_found_for_sync), "");
                            dialog.dismiss();
                        }
                    });

                } else {
                    product_valuenew.put("orders", order);
                    product_valuenew.put("order_products", product);
                    product_valuenew.put("return_orders", order_return);
                    product_valuenew.put("return_order_products", product_return);
                    product_valuenew.put("no_orders", no_orders);
                    product_valuenew.put("customers", customer);
                    product_valuenew.put("email", user_email);

                    Log.d("Orders", order.toString());
                    Log.d("order_products", product.toString());
                    Log.d("return_orders", order_return.toString());
                    Log.d("return_order_products", product_return.toString());
                    Log.d("no_orders", no_orders.toString());
                    Log.d("customers", customer.toString());

                    // HashMap<String, String> params = new HashMap<String, String>();
                    //params.put("token", json.toString());


                    // RequestQueue queue = Volley.newRequestQueue(getBaseContext());
                    // PreferencesHelper Prefs = new PreferencesHelper(MasterSyncData.this);
                    Global_Data.SYNC_ORDER_COUNT = String.valueOf(order_count);

                    //String URL = Prefs.GetPreferences("URL");
                    SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
                    String Cust_domain = sp.getString("Cust_Service_Url", "");
                    String service_url = Cust_domain + "metal/api/v1/";

                    String domain = service_url;

                    Log.i("volley", "domain: " + domain + "uploads/upload_orders");
                    final LoginDataBaseAdapter finalLoginDataBaseAdapter = loginDataBaseAdapter;
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "uploads/upload_orders", product_valuenew, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("volley", "response: " + response);
                            //  Toast.makeText(context, "Order Sync Successfully", Toast.LENGTH_LONG).show();

                            String response_result = "";
//                        if(response.has("result"))
//                        {
                            try {
                                response_result = response.getString("result");

//                                if(response_result.equalsIgnoreCase("Device not found."))
//                                {
//                                    Toast toast = Toast.makeText(context, "Device Not Found", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//                                    dialog.dismiss();
//                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();
                                    }
                                });
                            }
//                        }
//                        else
//                        {
//                            response_result = "data";
//                        }
                            if (response_result.equalsIgnoreCase("Device not found.")) {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {


                                        Global_Data.Custom_Toast(context, "Device Not Found", "yes");
                                        dialog.dismiss();
                                    }
                                });

                            } else if (response_result.equalsIgnoreCase("Created Data Successfully.")) {

                                final String finalResponse_result = response_result;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {


                                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Created_Data_Successfully), "yes");
                                    }
                                });
                                String val = "";
                                dbvoc.getDeleteTableorder("Secondary Sales / Retail Sales");

                                try {
                                    if (PRODUCTOrder_ids.isEmpty()) {
                                        Log.d("Product array empty", "EMPTY");
                                    } else {
                                        for (int i = 0; i < PRODUCTOrder_ids.size(); i++) {
                                            dbvoc.getDeleteTableorderproduct_byOrder_id(PRODUCTOrder_ids.get(i));
                                        }
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        public void run() {

                                            dialog.dismiss();
                                        }
                                    });
                                }

                                PRODUCTOrder_ids.clear();
                                dbvoc.getDeleteTable("no_orders");
                                dbvoc.getDeleteTable("returnordernew");
                                dbvoc.getDeleteTable("returnorder_products_new");
                                dbvoc.updateCustomerby_CreateAt(val);
                                for (int i = 0; i < order_results.size(); i++) {
                                    dbvoc.getDeleteTableorderproduct(order_results.get(i));
                                }

                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        try {
                                            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Metal_Signature");

                                            File dir1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Metal");

                                            if (dir.isDirectory()) {
                                                String[] children = dir.list();
                                                for (int i = 0; i < children.length; i++) {
                                                    new File(dir, children[i]).delete();
                                                }
                                            }

                                            if (dir1.isDirectory()) {
                                                String[] children = dir1.list();
                                                for (int i = 0; i < children.length; i++) {
                                                    new File(dir1, children[i]).delete();
                                                }
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                        dialog.dismiss();
                                    }
                                });

                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                                SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm:ss");

                                String Current_Date = sdf.format(c.getTime());
                                String Current_Time = sdf_time.format(c.getTime());


                                dbvoc.getDeleteTable("order_details");
                                LoginDataBaseAdapter loginDataBaseAdapter = new LoginDataBaseAdapter(context);
                                loginDataBaseAdapter = loginDataBaseAdapter.open();

                                loginDataBaseAdapter.insert_order_details(Current_Date, Current_Time);

                                if (Global_Data.order_pushflag.equalsIgnoreCase("yes")) {
                                    Global_Data.order_pushflag = "";


                                    ActivityManager mngr = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

                                    List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

                                    if (taskList.get(0).numActivities == 1 &&
                                            taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                                        Log.i("LastActivity", "This is last activity in the stack");

                                        Intent intentn = new Intent(context, SplashScreenActivity.class);
                                        context.startActivity(intentn);
                                        ((Activity) context).finish();
                                    } else {
                                        ((Activity) context).finish();
                                    }
                                } else {
                                    Intent intentn = new Intent(context, AllOrders_Sync.class);
                                    context.startActivity(intentn);
                                }


                            } else {
                                final String finalResponse_result1 = response_result;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();

                                        Global_Data.Custom_Toast(context, finalResponse_result1, "yes");
                                    }
                                });
                            }

                            // dbvoc.getDeleteTable("order_products");
                            //dbvoc.getDeleteTable("orders");
                            //List<Local_Data> contactsn = dbvoc.Getcustomer_email(customer_id);
                            //List<Local_Data> contacts = dbvoc.getAllOrderby_cusID("1012");

                            //((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            //((Activity) context).finish();

                            //Intent intentn = new Intent(context, MainActivity.class);
                            //context.startActivity(intentn);
                            //context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            //((Activity) context).finish();
//                 List<Local_Data> contacts = dbvoc.GetOrders(Global_Val.customer_id);
//                 for (Local_Data cn : contacts)
//                 {
//                     // JSONObject product_value = new JSONObject();
//                     //product_value.put("order_number", cn.getORDER_NUMBER());
//
//                     dbvoc.deleteOrderproductByOCID(cn.getORDER_NUMBER());
//                     dbvoc.deleteOrderTABLE_QuantityValue(cn.getORDER_NUMBER());
//                     dbvoc.deleteBarcode_ByOrder(cn.getORDER_NUMBER());
//                     dbvoc.deleteORDERSNEW(cn.getORDER_NUMBER());
//
//                 }

                            //dbvoc.deleteOrderByOCID(Global_Val.customer_id);
                            //dbvoc.getDeleteTable("DESIGN_CHECK");

//                 Intent i = new Intent(MasterSyncData.this, MyAndroidAppActivity.class);
//                 //				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                 i.putExtra("user_name", user_name);
//                 i.putExtra("confrence_name", confrence_name);
//                 i.putExtra("BackFlag", "nothing");
//                 Global_Val.STOCK_SERVICE_FLAG = "TRUE";
                            //				i.putExtra("Barcode_Number", userInput.getText().toString());
                            //				i.putExtra("BackFlag","Barcode");
//                 startActivity(i);
//                 MasterSyncData.this.finish();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("volley", "error: " + error);
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {

                                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Server_Error), "");
                                    dialog.dismiss();
                                }
                            });
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    // queue.add(jsObjRequest);
                    int socketTimeout = 30000;//30 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    jsObjRequest.setRetryPolicy(policy);
                    requestQueue.add(jsObjRequest);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Server_Error), "");
                        dialog.dismiss();
                    }
                });
            }
            return null;
        }
    }

    public static class TimesheetAyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // Since you are in a UI thread here.
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
                //dialog.dismiss();
            }

            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage(context.getResources().getString(R.string.Please_Wait));
            dialog.setTitle(context.getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // After completing execution of given task, control will return here.
            // Hence if you want to populate UI elements with fetched data, do it here.
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // You can track you progress update here
        }

        @Override
        protected Void doInBackground(Void... params) {
            final ArrayList<String> order_results = new ArrayList<String>();
            final JSONObject jsonBody = new JSONObject();
            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL", null);

            try {
                JSONArray timesheet_arr = new JSONArray();
                JSONObject timesheet_valuenew = new JSONObject();

                final DataBaseHelper dbvoc = new DataBaseHelper(context);
                List<Local_Data> timesheet_con = dbvoc.getAllTimesheet();

                for (Local_Data cn : timesheet_con) {
                    {
                        JSONObject timesheet_value = new JSONObject();
                        timesheet_value.put("from_time", cn.getFrom_time());
                        timesheet_value.put("to_time", cn.getTo_time());
                        timesheet_value.put("task_title", cn.getTask_time());
                        timesheet_value.put("details1", cn.getDetail1_time());
                        timesheet_value.put("details2", cn.getDetail2_time());
                        timesheet_value.put("remark", cn.getRemark_time());
                        timesheet_value.put("is_favourite", cn.getFavourite_status());
                        timesheet_arr.put(timesheet_value);
                    }
                }

                if (Final_Flag_ORDER_N.equalsIgnoreCase("sdf")) {

                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            Global_Data.Custom_Toast(context, context.getResources().getString(R.string.No_record_found_for_sync), "");
                            dialog.dismiss();
                        }
                    });

                } else {
                    timesheet_valuenew.put("timesheets", timesheet_arr);
                    timesheet_valuenew.put("date", Global_Data.date_arr);
                    timesheet_valuenew.put("email", user_email);

                    Log.d("timesheets", timesheet_valuenew.toString());
                    //Log.d("customers", customer.toString());

                    //String URL = Prefs.GetPreferences("URL");
                    SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
                    String Cust_domain = sp.getString("Cust_Service_Url", "");
                    String service_url = Cust_domain + "metal/api/v1/";

                    String domain = service_url;

                    Log.i("volley", "domain: " + domain + "time_sheets");
                    dbvoc.getDeleteTable("timesheet");
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "time_sheets", timesheet_valuenew, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("volley", "response: " + response);
                            //  Toast.makeText(context, "Order Sync Successfully", Toast.LENGTH_LONG).show();

                            String response_result = "";
//                        if(response.has("result"))
//                        {
                            try {
                                response_result = response.getString("result");
                                dialog.dismiss();
//                                if(response_result.equalsIgnoreCase("Device not found."))
//                                {
//                                    Toast toast = Toast.makeText(context, "Device Not Found", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//                                    dialog.dismiss();
//                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();
                                    }
                                });
                            }
//                        }
//                        else
//                        {
//                            response_result = "data";
//                        }
                            if (response_result.equalsIgnoreCase("Device not found.")) {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {


                                        Global_Data.Custom_Toast(context, "Device Not Found", "yes");
                                        dialog.dismiss();
                                    }
                                });

                            } else if (response_result.equalsIgnoreCase("Timesheet created successfully.")) {

                                final String finalResponse_result = response_result;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dbvoc.getDeleteTable("timesheet");
                                        Global_Data.ShowTimesheetArr.clear();
                                        Global_Data.date_arr.clear();

                                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.timesheet_created_successfully), "yes");

                                        Intent intentn = new Intent(context, TimeSheetActivity.class);
                                        context.startActivity(intentn);
                                        ((Activity) context).finish();

                                    }
                                });

                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();
                                    }
                                });

                            } else {
                                final String finalResponse_result1 = response_result;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();

                                        Global_Data.Custom_Toast(context, finalResponse_result1, "yes");
                                    }
                                });
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("volley", "error: " + error);
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {

                                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Server_Error), "");
                                    dialog.dismiss();
                                }
                            });
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    // queue.add(jsObjRequest);
                    int socketTimeout = 30000;//30 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    jsObjRequest.setRetryPolicy(policy);
                    requestQueue.add(jsObjRequest);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Server_Error), "");
                        dialog.dismiss();
                    }
                });
            }
            return null;
        }
    }

    public static class AllDataAyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // Here you can show progress bar or something on the similar lines.
            // Since you are in a UI thread here.
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
                //dialog.dismiss();
            }

            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage(context.getResources().getString(R.string.Order_Sync_in_Progress));
            dialog.setTitle(context.getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // After completing execution of given task, control will return here.
            // Hence if you want to populate UI elements with fetched data, do it here.
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // You can track you progress update here
        }

        @Override
        protected Void doInBackground(Void... params) {

            final Calendar c = Calendar.getInstance();
            String reason_code = "";
            try {

                String domain = "";
                String device_id = "";
                String customeremail = "";

                SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
                String Cust_domain = sp.getString("Cust_Service_Url", "");
                String service_url = Cust_domain + "metal/api/v1/";
                device_id = sp.getString("devid", "");
                domain = service_url;

                SharedPreferences spfa = context.getSharedPreferences("SimpleLogic", 0);
                String user_email = spfa.getString("USER_EMAIL", null);

                JsonObjectRequest jsObjRequest = null;
                try {

                    Log.d("Server url", "Server url" + domain + "customer_service_feedbacks");

                    JSONArray order = new JSONArray();
                    JSONArray CUSTOMERSN = new JSONArray();
                    JSONArray RetailerN = new JSONArray();
                    JSONArray fR = new JSONArray();
                    JSONArray COMP = new JSONArray();
                    JSONArray CLAIM = new JSONArray();
                    JSONArray COMPS = new JSONArray();
                    JSONArray PICTURE = new JSONArray();
                    JSONArray MARKET_SURVEY = new JSONArray();
                    JSONArray TRAVEL_EXPENSES = new JSONArray();
                    JSONArray MISC_EXPENSES = new JSONArray();
                    JSONArray CALENDER_DATA = new JSONArray();
                    //JSONObject product_value = new JSONObject();
                    JSONObject product_value_n = new JSONObject();
                    JSONArray product_imei = new JSONArray();

                    final DataBaseHelper dbvoc = new DataBaseHelper(context);

                    List<Local_Data> contacts = dbvoc.getCustomers_createdateCheck();
                    if (contacts.size() > 0) {
                        Retailer_Flag = "true";
                    } else {
                        Retailer_Flag = "false";
                    }

//                    for (Local_Data cn2 : contacts){
//                        customeremail = cn2.getEMAIL_ADDRESS();
//                    }

                    //  if(Global_Data.emailcustomer.equalsIgnoreCase(customeremail) ){


                    for (Local_Data cn : contacts) {
                        JSONObject product_value = new JSONObject();


                        product_value.put("user_email", user_email);
                        product_value.put("code", cn.getCust_Code());
                        product_value.put("name", cn.getCUSTOMER_NAME());
                        product_value.put("shop_name", cn.getCUSTOMER_SHOPNAME());
                        product_value.put("address", cn.getADDRESS());
                        product_value.put("street", cn.getSTREET());
                        product_value.put("landmark", cn.getLANDMARK());
                        product_value.put("pincode", cn.getPIN_CODE());
                        product_value.put("mobile_no", cn.getMOBILE_NO());
                        product_value.put("email", cn.getEMAIL_ADDRESS());
                        product_value.put("status", cn.getSTATUS());
                        product_value.put("state_code", cn.getSTATE_ID());
                        product_value.put("city_code", cn.getCITY_ID());
                        product_value.put("beat_code", cn.getBEAT_ID());
                        product_value.put("vatin", cn.getvatin());
                        product_value.put("latitude", cn.getlatitude());
                        product_value.put("longitude", cn.getlongitude());

                        product_value.put("address", cn.getAddress());
                        product_value.put("shop_name", cn.getCUSTOMER_SHOPNAME());
                        product_value.put("google_address", cn.getADDRESS());
                        product_value.put("street", cn.getSTREET());
                        product_value.put("landmark", cn.getLANDMARK());
                        product_value.put("pincode", cn.getPIN_CODE());
                        product_value.put("mobile1", cn.getMOBILE_NO());
                        product_value.put("mobile2", cn.getMobile2());
                        product_value.put("landline_no", cn.getLANDLINE_NO());
                        //     product_value.put("customer_email", cn.getEMAIL_ADDRESS());
                        product_value.put("gstin", cn.getvatin());
                        product_value.put("bank_account_name", cn.getBank_account_name());
                        product_value.put("bank_account_no", cn.getBank_account_no());
                        product_value.put("bank_account_ifsc", cn.getBank_account_ifsc());
                        product_value.put("aadhar_no", cn.getAdhar_no());
                        product_value.put("pan_card_no", cn.getPan_card_no());
                        product_value.put("shop_anniversary_date", cn.getShop_anniversary_date());
                        product_value.put("date_of_birthday", cn.getDate_of_birthday());
                        product_value.put("customer_flag", "customer");


                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getVisitc_img()) && cn.getVisitc_img().indexOf("http:") == -1) {
                            image_url1 = cn.getVisitc_img();
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getInshop_img()) && cn.getInshop_img().indexOf("http:") == -1) {
                            image_url2 = cn.getInshop_img();

                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getSignboard_img()) && cn.getSignboard_img().indexOf("http:") == -1) {
                            image_url3 = cn.getSignboard_img();

                        }


                        CUSTOMERSN.put(product_value);
                    }


                    List<Local_Data> contactsre = dbvoc.getRetailer_createdateCheck();
                    if (contactsre.size() > 0) {
                        Retailer_Flag_create = "true";
                    } else {
                        Retailer_Flag_create = "false";
                    }

                    for (Local_Data cnt1 : contactsre) {
                        JSONObject product_value = new JSONObject();
                        product_value.put("code", cnt1.getCode());
                        product_value.put("name", cnt1.getCUSTOMER_NAME());
                        product_value.put("shop_name", cnt1.getCUSTOMER_SHOPNAME());
                        product_value.put("customer_type_id", cnt1.getCustomer_type_id());
                        product_value.put("customer_category_id", cnt1.getCustomerCategory_id());
                        product_value.put("beat_code", cnt1.getBEAT_ID());
                        product_value.put("city_code", cnt1.getCITY_ID());
                        product_value.put("state_code", cnt1.getSTATE_ID());
                        product_value.put("address", cnt1.getAddress());
                        product_value.put("street", cnt1.getSTREET());
                        product_value.put("landmark", cnt1.getLANDMARK());
                        product_value.put("pincode", cnt1.getPIN_CODE());
                        product_value.put("mobile_no", cnt1.getMOBILE_NO());
                        product_value.put("retailer_email", cnt1.getEMAIL_ADDRESS());
                        product_value.put("gstin", cnt1.getvatin());
                        product_value.put("customer_flag", "retailer");
                        product_value.put("cust", "");

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnt1.getVisitc_img()) && cnt1.getVisitc_img().indexOf("http:") == -1) {
                            image_url1 = cnt1.getVisitc_img();
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnt1.getInshop_img()) && cnt1.getInshop_img().indexOf("http:") == -1) {
                            image_url2 = cnt1.getInshop_img();

                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnt1.getSignboard_img()) && cnt1.getSignboard_img().indexOf("http:") == -1) {
                            image_url3 = cnt1.getSignboard_img();

                        }


                        RetailerN.put(product_value);
                    }


                    List<Local_Data> market_s = dbvoc.Get_SURVEY_ANSWERS();
                    if (market_s.size() > 0) {
                        market_survey_Flag = "true";
                    } else {
                        market_survey_Flag = "false";
                    }

                    for (Local_Data cn : market_s) {
                        JSONObject markets = new JSONObject();
                        // markets.put("code", cn.getCode());
                        markets.put("user_email", cn.getuser_email());
                        markets.put("answer_date", cn.getanswer_date());
                        markets.put("survey_code", cn.getsurvey_code());
                        markets.put("customer_code", cn.getCust_Code());
                        markets.put("question_code", cn.getquestion_code());
                        markets.put("customer_choice", cn.getcustomer_choice());
                        markets.put("latitude", cn.getlatitude());
                        markets.put("longitude", cn.getlongitude());

                        MARKET_SURVEY.put(markets);
                    }

                    byte b5[];
                    List<Local_Data> tr_ex = dbvoc.getAllTravelExpenses();
                    if (tr_ex.size() > 0) {
                        travel_expenses_Flag = "true";
                    } else {
                        travel_expenses_Flag = "false";
                    }

                    for (final Local_Data cn : tr_ex) {
                        JSONObject tt = new JSONObject();
                        tt.put("code", cn.getCode());
                        tt.put("user_email", cn.getuser_email());
                        tt.put("travel_from", cn.get_travel_from());
                        tt.put("travel_to", cn.get_travel_to());
                        tt.put("travel_date", cn.get_travel_date());
                        tt.put("travel_mode", cn.get_travel_mode());
                        tt.put("travel_cost", cn.get_travel_cost());
                        tt.put("travel_text", cn.get_travel_text());
                        //tt.put("travel_text", cn.get_travel_pdf());
                        //tt.put("travel_text", cn.get_travel_text());
//                        String fgd = cn.get_travel_pdf();
//                        System.out.println(fgd);

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getimg_url())) {
                            order_image_url = cn.getimg_url().trim();
                            // File filepath = new File(cn.getimg_ordersign());
                            // String  path =  "file://"+filepath.getPath();
                            try {
                                Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(cn.getimg_url()));
                                ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
                                mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
                                b5 = bos5.toByteArray();

                                String getsign_str = Base64.encodeToString(b5, Base64.DEFAULT);
                                tt.put("expenses_travel_image", getsign_str);

                            } catch (Exception e) {
                                e.printStackTrace();
                                tt.put("expenses_travel_image", "");
                            }

                        } else {
                            tt.put("expenses_travel_image", "");
                        }

//                        if(cn.get_travel_pdf().length()>0)
//                        {
//                            ((Activity) context).runOnUiThread(new Runnable() {
//                                public void run() {
//                                    new doFileUploadPdf().execute(cn.getCode(),cn.get_travel_pdf());
//                                }
//                            });
//                        }


                        TRAVEL_EXPENSES.put(tt);


                    }


                    List<Local_Data> misc_ex = dbvoc.getAllMisceExpenses();
                    if (misc_ex.size() > 0) {
                        misc_expenses_Flag = "true";
                    } else {
                        misc_expenses_Flag = "false";
                    }

                    for (Local_Data cn : misc_ex) {
                        JSONObject misc = new JSONObject();
                        misc.put("code", cn.getCode());
                        misc.put("user_email", cn.getuser_email());
                        misc.put("misc_date", cn.getmisc_date());
                        misc.put("misc_amount", cn.getmisc_cost());
                        misc.put("misc_text", cn.getmisc_text());

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getimg_url())) {
                            order_image_url = cn.getimg_url().trim();
                            // File filepath = new File(cn.getimg_ordersign());
                            // String  path =  "file://"+filepath.getPath();
                            try {
                                Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(cn.getimg_url()));
                                ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
                                mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
                                b5 = bos5.toByteArray();

                                String getsign_str = Base64.encodeToString(b5, Base64.DEFAULT);
                                misc.put("expenses_misc_image", getsign_str);

                            } catch (Exception e) {
                                e.printStackTrace();
                                misc.put("expenses_misc_image", "");
                            }


                        } else {
                            misc.put("expenses_misc_image", "");
                        }

                        MISC_EXPENSES.put(misc);
                    }

                    List<Local_Data> calender = dbvoc.getCalender_EventAllNEW();
                    if (calender.size() > 0) {
                        calenderdata_Flag = "true";
                    } else {
                        calenderdata_Flag = "false";
                    }

                    for (Local_Data cn : calender) {
                        JSONObject calenderdata = new JSONObject();
                        calenderdata.put("code", cn.getcalender_id());
                        calenderdata.put("user_email", cn.getuser_email());
                        calenderdata.put("entry_type", cn.getcalender_type());
                        calenderdata.put("from_date", cn.getfrom_date());
                        calenderdata.put("to_date", cn.getto_date());
                        calenderdata.put("details", cn.getcalender_details());
                        calenderdata.put("latitude", cn.getlatitude());
                        calenderdata.put("longitude", cn.getlongitude());
                        calenderdata.put("flag", cn.getcalender_delete_flag());
                        //calenderdata.put("time", cn.gettime1());
                        CALENDER_DATA.put(calenderdata);
                    }

                    List<Local_Data> confeed = dbvoc.getAllFeedback_CREATEDATE();
                    if (confeed.size() > 0) {
                        FEED_Flag = "true";
                    } else {
                        FEED_Flag = "false";
                    }

                    for (final Local_Data cn : confeed) {
                        JSONObject Feed = new JSONObject();
                        Feed.put("code", cn.getCode());
                        Feed.put("customer_code", cn.getCust_Code());
                        Feed.put("user_email", cn.getEMAIL_ADDRESS());
                        Feed.put("date", cn.getC_Date());
                        Feed.put("text", cn.get_Description());
                        Feed.put("latitude", cn.getlatitude());
                        Feed.put("longitude", cn.getlongitude());
                        feedPhotoCode = cn.getCode();
                        feedPhotoPath = cn.get_complaints();

                        if (feedPhotoPath.length() > 0) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {
                                    new doFileUpload().execute(feedPhotoCode, feedPhotoPath);
                                }
                            });
                        }

//                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.get_complaints())) {
//                            order_image_url = cn.get_complaints().trim();
//                            // File filepath = new File(cn.getimg_ordersign());
//                            // String  path =  "file://"+filepath.getPath();
//                            try {
//                                Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(cn.get_complaints()));
//                                ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
//                                mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
//                                b5 = bos5.toByteArray();
//
//                                String getsign_str = Base64.encodeToString(b5, Base64.DEFAULT);
//                                Feed.put("feedback_image", getsign_str);
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                Feed.put("feedback_image", "");
//                            }
//
//
//                        } else {
//                            Feed.put("feedback_image", "");
//                        }
//
                        fR.put(Feed);
                    }

                    List<Local_Data> concomp = dbvoc.getAllComplaints_BYCUSTOMERID();
                    if (concomp.size() > 0) {
                        COMP_Flag = "true";
                    } else {
                        COMP_Flag = "false";
                    }


                    for (Local_Data cn : concomp) {
                        if (cn.getProject_id().equalsIgnoreCase("1")) {

                            JSONObject cm = new JSONObject();
                            cm.put("code", cn.getCode());
                            cm.put("customer_code", cn.getCust_Code());
                            cm.put("user_email", cn.getEMAIL_ADDRESS());
                            cm.put("date", cn.getC_Date());
                            cm.put("text", cn.get_Description());
                            cm.put("latitude", cn.getlatitude());
                            cm.put("longitude", cn.getlongitude());
                            complaintCode = cn.getCode();
                            complaintPhotoPath = cn.get_complaints();

                            if (complaintPhotoPath.length() > 0) {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {
                                        new doFileUpload2().execute(complaintCode, complaintPhotoPath);
                                    }
                                });
                            }

                            COMP.put(cm);
                        }

                    }

                    List<Local_Data> conclaims = dbvoc.getAllClaims_BYCUSTOMERID();
                    if (conclaims.size() > 0) {
                        CLAIM_Flag = "true";
                    } else {
                        CLAIM_Flag = "false";
                    }

                    for (Local_Data cn : conclaims) {
                        if (cn.getProject_id().equalsIgnoreCase("1")) {
                            JSONObject cl = new JSONObject();
                            cl.put("code", cn.getCode());
                            cl.put("customer_code", cn.getCust_Code());
                            cl.put("user_email", cn.getEMAIL_ADDRESS());
                            cl.put("date", cn.getC_Date());
                            cl.put("text", cn.get_Description());
                            cl.put("amount", cn.get_Claims_amount());
                            cl.put("latitude", cn.getlatitude());
                            cl.put("longitude", cn.getlongitude());
                            claimPhotoCode = cn.getCode();
                            claimPhotoPath = cn.get_Claims();

                            if (claimPhotoPath.length() > 0) {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {
                                        new doFileUpload1().execute(claimPhotoCode, claimPhotoPath);
                                    }
                                });
                            }

                            CLAIM.put(cl);
                        }
                    }

                    List<Local_Data> conccomss = dbvoc.getAllCOMPETITION_STOCKS_BYCUSTOMERID();
                    if (conccomss.size() > 0) {
                        COMPS_Flag = "true";
                    } else {
                        COMPS_Flag = "false";
                    }

                    for (Local_Data cn : conccomss) {
                        JSONObject cll = new JSONObject();
                        cll.put("code", cn.getCode());
                        cll.put("customer_code", cn.getCust_Code());
                        cll.put("user_email", cn.getEMAIL_ADDRESS());
                        //cll.put("category_id", cn.get_category_id());
                        // cll.put("product_id", cn.get_product_code());
                        cll.put("product_code", cn.get_variants_code());
                        cll.put("competition_product_text", cn.get_Description());
                        cll.put("competition_product_quantity", cn.get_stocks_product_quantity());
                        cll.put("latitude", cn.getlatitude());
                        cll.put("longitude", cn.getlongitude());
                        cll.put("competitor_product_price", cn.getPrice());

                        String ssss = cn.getCometitor_product();
                        String sss = cn.getCometitor_name();
                        cll.put("competition_product_name", ssss);
                        cll.put("competitor_name", sss);

                        competitionStockCode = cn.getCode();
                        competitionStockPhotoPath = cn.getimg_url();

                        if (competitionStockPhotoPath.length() > 0) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {
                                    new doFileUpload3().execute(competitionStockCode, competitionStockPhotoPath);
                                }
                            });
                        }

//                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getimg_url())) {
//                           order_image_url = cn.getimg_url().trim();
//
//                            // File filepath = new File(cn.getimg_ordersign());
//                            // String  path =  "file://"+filepath.getPath();
//                            try {
//                                Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(cn.getimg_url()));
//                                ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
//                                mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
//                                b5 = bos5.toByteArray();
//
//                                String getsign_str = Base64.encodeToString(b5, Base64.DEFAULT);
//
//                                cll.put("compstock_image", getsign_str);
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                cll.put("compstock_image", "");
//                            }
//
//
//                        } else {
//                            cll.put("compstock_image", "");
//                        }


                        COMPS.put(cll);
                    }

//                List<Local_Data> concphoto= dbvoc.getAllPICTURESD_BYCUSTOMERID();
//                if(concphoto.size() > 0)
//                {
//                    PICTURE_Flag = "true";
//                }
//                else
//                {
//                    PICTURE_Flag = "false";
//                }
//
//                for (Local_Data cn : concphoto)
//                {
//                    JSONObject picture = new JSONObject();
//                    picture.put("customer_code", cn.getCust_Code());
//                    picture.put("user_email", cn.getEMAIL_ADDRESS());
//                    picture.put("media_type", cn.get_mediaType());
//                    picture.put("media_text", cn.get_mediaDisc());
//                    picture.put("location", cn.get_mediaUrl());
//                    PICTURE.put(picture);
//                }


                    if (Retailer_Flag.equalsIgnoreCase("true") || Retailer_Flag_create.equalsIgnoreCase("true") || FEED_Flag.equalsIgnoreCase("true") || COMP_Flag.equalsIgnoreCase("true") || CLAIM_Flag.equalsIgnoreCase("true") || COMPS_Flag.equalsIgnoreCase("true") || travel_expenses_Flag.equalsIgnoreCase("true") || misc_expenses_Flag.equalsIgnoreCase("true") || calenderdata_Flag.equalsIgnoreCase("true") || market_survey_Flag.equalsIgnoreCase("true")) {
                        Final_Flag_N += " " + "Retailer";
                    } else {
                        Final_Flag_N = "";
                    }

                    if (Final_Flag_N.equalsIgnoreCase("")) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {

                                Global_Data.Custom_Toast(context, context.getResources().getString(R.string.No_record_found_for_sync), "");
                                dialog.dismiss();
                            }
                        });

                    } else {

                        SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                        String user_email1 = spf.getString("USER_EMAIL", null);

                        // product_imei.put(Global_Data.device_id);
                        // product_value_n.put("customers", order);
                        product_value_n.put("customers", CUSTOMERSN);
                        product_value_n.put("retailer", RetailerN);
                        product_value_n.put("survey_answers", MARKET_SURVEY);
                        product_value_n.put("expenses_travels", TRAVEL_EXPENSES);
                        product_value_n.put("expenses_miscs", MISC_EXPENSES);
                        product_value_n.put("calender_entries", CALENDER_DATA);
                        product_value_n.put("feedbacks", fR);
                        product_value_n.put("complaints", COMP);
                        product_value_n.put("claims", CLAIM);
                        product_value_n.put("stocks", COMPS);
                        // product_value_n.put("customer_service_media", PICTURE);
                        product_value_n.put("email", user_email1);

                        // Log.d("customers",product_value_n.toString());

                        Log.d("customers Service", product_value_n.toString());

                        //Log.d("expenses_travels",product_value_n.toString());

//
//
//				 //product_value.put("email", Global_Data.GLOvel_USER_EMAIL);
//				// product_value.put("email", Global_Data.GLOvel_USER_EMAIL);

                        Log.d("domain", "domain" + domain + "uploads/upload_masters_data" + product_value_n.toString());
//
                        jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "uploads/upload_masters_data", product_value_n, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("volley", "response: " + response);

                                Log.d("jV", "JV length" + response.length());
                                //JSONObject json = new JSONObject(new JSONTokener(response));
                                try {

                                    String response_result = "";
//                                if(response.has("result"))
//                                {
                                    response_result = response.getString("result");
//                                    if(response_result.equalsIgnoreCase("Device not found."))
//                                    {
//                                        Toast toast = Toast.makeText(context, "Device Not Found", Toast.LENGTH_LONG);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();
//                                        dialog.dismiss();
//                                    }
//                                }
//                                else
//                                {
//                                    response_result = "data";
//                                }


                                    if (response_result.equalsIgnoreCase("Device not found.")) {

                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            public void run() {


                                                Global_Data.Custom_Toast(context, "Device Not Found", "yes");
                                                dialog.dismiss();
                                            }
                                        });

                                    } else if (response_result.equalsIgnoreCase("Created Data Successfully.")) {

                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            public void run() {

                                                dialog.dismiss();

                                                String val = "";

                                                dbvoc.getDeleteTable("expenses_travels");
                                                dbvoc.getDeleteTable("expenses_miscs");
                                                dbvoc.getDeleteTable("Survey_Answers");
                                                dbvoc.getDeleteTablecalender_event_BYFLAG("YES");

                                                dbvoc.updateCustomerby_CreateAt(val);
                                                dbvoc.updateRetailerby_CreateAt(val);
                                                dbvoc.updateORDER_feedback(val);
                                                dbvoc.updateORDER_claims(val);
                                                dbvoc.updateORDER_complaints(val);
                                                dbvoc.updateORDER_stocks(val);
                                                // dbvoc.updateORDER_feedback(val);
                                                dbvoc.updateORDER_calenderevent(val);

                                                image_url1 = "";
                                                image_url2 = "";
                                                image_url3 = "";

//                                                ((Activity) context).runOnUiThread(new Runnable() {
//                                                    public void run() {
//                                                        new doFileUpload().execute();
//                                                    }
//                                                });


                                                // new doFileUpload().execute();


                                                try {
                                                    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_Expenses");
                                                    if (dir.isDirectory()) {
                                                        String[] children = dir.list();
                                                        for (int i = 0; i < children.length; i++) {
                                                            new File(dir, children[i]).delete();
                                                        }
                                                    }
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        });


                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                                        SimpleDateFormat sdf_time = new SimpleDateFormat("hh:mm:ss");

                                        String Current_Date = sdf.format(c.getTime());
                                        String Current_Time = sdf_time.format(c.getTime());


                                        dbvoc.getDeleteTable("order_details");
                                        LoginDataBaseAdapter loginDataBaseAdapter = new LoginDataBaseAdapter(context);
                                        loginDataBaseAdapter = loginDataBaseAdapter.open();

                                        loginDataBaseAdapter.insert_order_details(Current_Date, Current_Time);

                                        Calendar calendar = Calendar.getInstance();

                                        int yearnew = calendar.get(Calendar.YEAR);
                                        int monthnew = calendar.get(Calendar.MONTH);
                                        int daynew = calendar.get(Calendar.DAY_OF_MONTH);

                                        int month = calendar.get(Calendar.MONTH);
                                        int year = calendar.get(Calendar.YEAR);

                                        Formatter fmt = new Formatter();
                                        // fmt.format("%tB %tb %tm", calendar, calendar, calendar);

                                        String mm = fmt.format("%tB", calendar).toString();


                                        Calendar calendarold = Calendar.getInstance();
                                        calendarold.add(calendarold.MONTH, -1);
                                        int yearold = calendarold.get(calendarold.YEAR);
                                        int monthold = calendarold.get(calendarold.MONTH);
                                        int dayold = calendarold.get(calendarold.DAY_OF_MONTH);

                                        Formatter fmtt = new Formatter();
                                        // fmt.format("%tB %tb %tm", calendar, calendar, calendar);

                                        String mmm = fmtt.format("%tB", calendarold).toString();

                                        Log.d("year", "YEAR" + yearnew);
                                        Log.d("yearold", "YEAROLD" + yearold);
                                        Log.d("month", "MONTH" + monthnew);
                                        Log.d("monthold", "MONTHOLD" + monthold);
                                        Log.d("month String", "MONTH String" + mm);
                                        Log.d("monthold String", "MONTHOLD String" + mmm);

                                        String cureent_month = mm + "-" + yearnew;
                                        String old_month = mmm + "-" + yearold;

                                        Date current = new Date();
                                        //create a date one day after current date


                                        try {
                                            List<Local_Data> contacts2 = dbvoc.getAllCalender_EventValue();

                                            if (contacts2.size() != 0) {
                                                for (Local_Data cn : contacts2) {

                                                    String from_date = cn.getfrom_date();


                                                    //create date object
                                                    Date next = new Date(from_date);

                                                    if (next.after(current)) {
                                                        System.out.println("The date is future day");
                                                    } else {
                                                        if (from_date.contains("-")) {
                                                            String from_date_Array[] = from_date.split("-");
                                                            String final_fromdate = from_date_Array[1] + "-" + from_date_Array[2];

                                                            if (final_fromdate.equalsIgnoreCase(cureent_month) || final_fromdate.equalsIgnoreCase(old_month)) {
                                                                Log.d("From Data", "From Date" + cn.getfrom_date());
                                                            } else {
                                                                dbvoc.getDeleteTableCalenderEntity(cn.getfrom_date());
                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                public void run() {
                                                    dialog.dismiss();
                                                }
                                            });
                                        }

                                        final String finalResponse_result = response_result;
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            public void run() {
                                                Global_Data.Custom_Toast(context, finalResponse_result, "");
                                                Intent a = new Intent(context, AllOrders_Sync.class);
                                                context.startActivity(a);
                                                ((Activity) context).finish();
                                            }
                                        });
//add


                                    } else {

                                        final String finalResponse_result1 = response_result;
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                                Global_Data.Custom_Toast(context, finalResponse_result1, "");
                                            }
                                        });

                                        // Toast.makeText(context.getApplicationContext(), response_result, Toast.LENGTH_LONG).show();
//                                    String val = "";
//
//                                    dbvoc.getDeleteTable("expenses_travels");
//                                    dbvoc.getDeleteTable("expenses_miscs");
//                                    dbvoc.getDeleteTable("Survey_Answers");
//                                    dbvoc.getDeleteTablecalender_event_BYFLAG("YES");
//
//                                    dbvoc.updateORDER_feedback(val);
//                                    dbvoc.updateORDER_claims(val);
//                                    dbvoc.updateORDER_complaints(val);
//                                    dbvoc.updateORDER_stocks(val);
//
//                                    Toast toast = Toast.makeText(context,response_result, Toast.LENGTH_SHORT);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//                                    Intent a = new Intent(context,MainActivity.class);
//                                    context.startActivity(a);
//                                    ((Activity)context).finish();


                                    }

                                    //  finish();
                                    // }

                                    // output.setText(data);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();

                                        }
                                    });

                                }


                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();

                                    }
                                });


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("volley", "error: " + error);
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {
                                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Server_Error), "");

                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            public void run() {

                                                dialog.dismiss();

                                            }
                                        });
                                    }
                                });

                            }
                        });

                        RequestQueue requestQueue = Volley.newRequestQueue(context);

                        int socketTimeout = 300000;//30 seconds - change to what you want
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        jsObjRequest.setRetryPolicy(policy);
                        // requestQueue.se
                        //requestQueue.add(jsObjRequest);
                        jsObjRequest.setShouldCache(false);
                        requestQueue.getCache().clear();
                        requestQueue.add(jsObjRequest);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();

                        }
                    });

                }


            } catch (Exception e) {
                // TODO: handle exception
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();

                    }
                });
                Log.e("DATA", e.getMessage());
            }


            return null;
        }
    }

    public static String addressn(Double lat, Double longi, Context c_context) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(c_context, Locale.getDefault());
        StringBuilder sb = new StringBuilder();
        sb.append("");

        try {
            addresses = geocoder.getFromLocation(lat, longi, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getAddressLine(0)) + " ");
            if (!(sb.indexOf(addresses.get(0).getLocality()) > 0)) {
                sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getLocality()) + " ");
            }

            if (!(sb.indexOf(addresses.get(0).getAdminArea()) > 0)) {
                sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getAdminArea()) + " ");
            }

            if (!(sb.indexOf(addresses.get(0).getCountryName()) > 0)) {
                sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getCountryName()) + " ");
            }

            if (!(sb.indexOf(addresses.get(0).getPostalCode()) > 0)) {
                sb.append(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getPostalCode()) + " ");
            }
            //String knownName = addresses.get(0).getFeatureName();

            Global_Data.address = sb.toString();
            //sb.append(knownName).append(" ");
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private static class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress = "";
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    //locationAddress = " ";
            }
            //  LOCATION.setText(locationAddress);


            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewwithzeron(locationAddress)) {
                Global_Data.address = locationAddress;
                Log.d("GLOBEL ADDRESS G", "V" + locationAddress);

            } else {
                Global_Data.address = "";
                Log.d("GLOBEL ADDRESS G", "address not found.");
            }
        }
    }

    public static class doFileUpload extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... response) {

            String domain = context.getResources().getString(R.string.service_domain) + "customer_service_feedbacks/customer_service_upload_images";
            Log.i("volley", "domain: " + domain);

            Uri uri1 = Uri.parse(response[1]);
            final File file1 = new File(uri1.getPath());


            // String urlString = domain + "sub_dealers";
            try {

                String charset = "UTF-8";
                //File uploadFile1 = new File("/sdcard/myvideo.mp4");

                MultipartUtility multipart = new MultipartUtility(domain, charset);

//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("Test-Header", "Header-Value");

                //multipart.addFormField("feedback_image", claimPhotoCode);

                //for feedback
                if (!response[0].equalsIgnoreCase("")) {
                    multipart.addFormField("feedback_code", response[0]);
                    multipart.addFormField("feedback_type", "feedback");
                }

                if (!response[1].equalsIgnoreCase("")) {
                    multipart.addFilePart("feedback_image", file1);
                }

//                //for claim
//                if (!claimPhotoCode.equalsIgnoreCase("")) {
//                    multipart.addFormField("claim_code", claimPhotoCode);
//                    multipart.addFormField("claim_type", "claim");
//                }
//
//                if (!claimPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("claim_image", file2);
//                }
//
//                //for complaint
//                if (!complaintCode.equalsIgnoreCase("")) {
//                    multipart.addFormField("complaint_code", complaintCode);
//                    multipart.addFormField("complaint_type", "complaint");
//                }
//
//                if (!complaintPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("complaint_image", file3);
//                }

//                //for comp stock
//                if (!competitionStockPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFormField("competition_stock_code", competitionStockCode);
//                    multipart.addFormField("competition_stock_type", "competition_stock");
//                }
//
//                if (!complaintPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("competition_stock_image", file4);
//                }


                // multipart.addFormField("claim_type", "claim");
                //multipart.addFormField("feedback_image", claimPhotoCode);


//                if (!claimPhotoCode.equalsIgnoreCase("")) {
//                    multipart.addFormField("claim_code", claimPhotoCode);
//                }


                //multipart.addFormField("sub_dealer_code", competitionStockCode);
                //  multipart.addFormField("userid", "Java,upload,Spring");


//                if (!claimPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("claim_image", file2);
//                }

//                if (!claimPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_proprietor1", file2);
//                }
//
//                if (!complaintPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_proprietor2", file3);
//                }
//
//                if (!competitionStockPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_rubber_stamp1", file4);
//                }

//                if (!Visiting_Card_mCurrentPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_visiting_card_front1", file5);
//                }
//
//                if (!Outlet_Display_mCurrentPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_outlet_display1", file6);
//                }
//
//                if (!InShop_Display_mCurrentPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_in_shop_display1", file7);
//                }
//
//                if (!signature_img.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_signature", file8);
//                }


                List<String> response1 = multipart.finish();

                Log.v("rht", "SERVER REPLIED:");

                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

                for (String line : response1) {
                    Log.v("rht", "Line : " + line);
                    response_result_image = line;
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            try {

                                JSONObject obj = new JSONObject(response_result_image);


                                if (obj.getString("message").equalsIgnoreCase("Customer Services uploaded images successfully.")) {
                                    Global_Data.Custom_Toast(context, obj.getString("message"), "");
//                                    Intent a = new Intent(context, AllOrders_Sync.class);
//                                    context.startActivity(a);
//                                    ((Activity) context).finish();
                                    dialog.dismiss();
//                                    Log.d("anchor_st","anchor_stocking_products"+sub_dealer_anchor_stocking_products_table_flag);
//                                    dealer_check_value_Globel = dbvoc.subdealrer_details_value_byscode(Globel_Data.GLOvel_Distributer_Code);
//                                    if (dealer_check_value_Globel.size() > 0 && !sub_dealer_nature_of_business_table_flag.equalsIgnoreCase("") && !sub_dealer_business_product_categories_table_flag.equalsIgnoreCase("") && !sub_dealer_stocking_product_categories_table_flag.equalsIgnoreCase("")) {

                                    // sub_dealer_approval_stag_val = "All mandatory fields of sub-dealer are filled & hence has been sent to HO for approval";
                                    if (file1.exists()) {
                                        if (file1.delete()) {
                                            System.out.println("file Deleted :" + feedPhotoPath);
                                        } else {
                                            System.out.println("file not Deleted :" + feedPhotoPath);
                                        }
                                    }

//                                        if (file2.exists()) {
//                                            if (file2.delete()) {
//                                                System.out.println("file Deleted :" + claimPhotoPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + claimPhotoPath);
//                                            }
//                                        }
//
//                                        if (file3.exists()) {
//                                            if (file3.delete()) {
//                                                System.out.println("file Deleted :" + complaintPhotoPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + complaintPhotoPath);
//                                            }
//                                        }
//
//                                        if (file4.exists()) {
//                                            if (file4.delete()) {
//                                                System.out.println("file Deleted :" + competitionStockPhotoPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + competitionStockPhotoPath);
//                                            }
//                                        }

//                                        if (file5.exists()) {
//                                            if (file5.delete()) {
//                                                System.out.println("file Deleted :" + Visiting_Card_mCurrentPhotoPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + Visiting_Card_mCurrentPhotoPath);
//                                            }
//                                        }
//
//                                        if (file6.exists()) {
//                                            if (file6.delete()) {
//                                                System.out.println("file Deleted :" + Outlet_Display_mCurrentPhotoPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + Outlet_Display_mCurrentPhotoPath);
//                                            }
//                                        }
//
//                                        if (file7.exists()) {
//                                            if (file7.delete()) {
//                                                System.out.println("file Deleted :" + InShop_Display_mCurrentPhotoPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + InShop_Display_mCurrentPhotoPath);
//                                            }
//                                        }
//
//                                        if (file8.exists()) {
//                                            if (file8.delete()) {
//                                                System.out.println("file Deleted :" + signature_img);
//                                            } else {
//                                                System.out.println("file not Deleted :" + signature_img);
//                                            }
//                                        }
//                                    }
//                                    else
//                                    {
//                                        //sub_dealer_approval_stag_val = "All mandatory fields are not filled. Hence, sub-dealer will be in partially filled stage";
//                                    }

                                    feedPhotoPath = "";
//                                    claimPhotoPath = "";
//                                    complaintPhotoPath = "";
//                                    competitionStockPhotoPath = "";

                                } else {
                                    // Sub_Dealer_Approval_Stage(context, sub_dealer_approval_stag_val);
                                }

                                Log.d("My App", obj.toString());

                            } catch (Throwable t) {
                                Log.e("My App", "Could not parse malformed JSON: \"" + response_result_image + "\"");

                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();

                                        // Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                                    }
                                });
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                        //Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                    }
                });
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            ((Activity) context).runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                    // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                }
            });

        }

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Image Sync in Progress, Please Wait");
            dialog.setTitle("Metal");
            dialog.setCancelable(false);
            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    public static class doFileUpload1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... response) {
            String domain = context.getResources().getString(R.string.service_domain) + "customer_service_feedbacks/customer_service_upload_images";
            Log.i("volley", "domain: " + domain);

//            Uri uri1 = Uri.parse(feedPhotoPath);
//            final File file1 = new File(uri1.getPath());

            Uri uri2 = Uri.parse(response[1]);
            final File file2 = new File(uri2.getPath());

//            Uri uri3 = Uri.parse(complaintPhotoPath);
//            final File file3 = new File(uri3.getPath());
//
//            Uri uri4 = Uri.parse(competitionStockPhotoPath);
//            final File file4 = new File(uri4.getPath());

//            Uri uri5 = Uri.parse(Visiting_Card_mCurrentPhotoPath);
//            final File file5 = new File(uri5.getPath());
//
//            Uri uri6 = Uri.parse(Outlet_Display_mCurrentPhotoPath);
//            final File file6 = new File(uri6.getPath());
//
//            Uri uri7 = Uri.parse(InShop_Display_mCurrentPhotoPath);
//            final File file7 = new File(uri7.getPath());
//
//            Uri uri8 = Uri.parse(signature_img);
//            final File file8 = new File(uri8.getPath());


            // String urlString = domain + "sub_dealers";
            try {

                String charset = "UTF-8";
                //File uploadFile1 = new File("/sdcard/myvideo.mp4");

                MultipartUtility multipart = new MultipartUtility(domain, charset);

//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("Test-Header", "Header-Value");

                //multipart.addFormField("feedback_image", claimPhotoCode);

//                //for feedback
//                if (!feedPhotoCode.equalsIgnoreCase("")) {
//                    multipart.addFormField("feedback_code", feedPhotoCode);
//                    multipart.addFormField("feedback_type", "feedback");
//                }
//
//                if (!feedPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("feedback_image", file1);
//                }

                //for claim
                if (!response[0].equalsIgnoreCase("")) {
                    multipart.addFormField("claim_code", response[0]);
                    multipart.addFormField("claim_type", "claim");
                }

                if (!response[1].equalsIgnoreCase("")) {
                    multipart.addFilePart("claim_image", file2);
                }

//                //for complaint
//                if (!complaintCode.equalsIgnoreCase("")) {
//                    multipart.addFormField("complaint_code", complaintCode);
//                    multipart.addFormField("complaint_type", "complaint");
//                }
//
//                if (!complaintPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("complaint_image", file3);
//                }

//                //for comp stock
//                if (!competitionStockPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFormField("competition_stock_code", competitionStockCode);
//                    multipart.addFormField("competition_stock_type", "competition_stock");
//                }
//
//                if (!complaintPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("competition_stock_image", file4);
//                }


                // multipart.addFormField("claim_type", "claim");
                //multipart.addFormField("feedback_image", claimPhotoCode);


//                if (!claimPhotoCode.equalsIgnoreCase("")) {
//                    multipart.addFormField("claim_code", claimPhotoCode);
//                }


                //multipart.addFormField("sub_dealer_code", competitionStockCode);
                //  multipart.addFormField("userid", "Java,upload,Spring");


//                if (!claimPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("claim_image", file2);
//                }

//                if (!claimPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_proprietor1", file2);
//                }
//
//                if (!complaintPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_proprietor2", file3);
//                }
//
//                if (!competitionStockPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_rubber_stamp1", file4);
//                }

//                if (!Visiting_Card_mCurrentPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_visiting_card_front1", file5);
//                }
//
//                if (!Outlet_Display_mCurrentPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_outlet_display1", file6);
//                }
//
//                if (!InShop_Display_mCurrentPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_in_shop_display1", file7);
//                }
//
//                if (!signature_img.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_signature", file8);
//                }


                List<String> response1 = multipart.finish();

                Log.v("rht", "SERVER REPLIED:");

                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

                for (String line : response1) {
                    Log.v("rht", "Line : " + line);
                    response_result_image = line;
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            try {

                                JSONObject obj = new JSONObject(response_result_image);

                                if (obj.getString("message").equalsIgnoreCase("Customer Services uploaded images successfully.")) {
                                    Global_Data.Custom_Toast(context, obj.getString("message"), "");
//                                    Intent a = new Intent(context, AllOrders_Sync.class);
//                                    context.startActivity(a);
//                                    ((Activity) context).finish();
                                    dialog.dismiss();
//                                    Log.d("anchor_st","anchor_stocking_products"+sub_dealer_anchor_stocking_products_table_flag);
//                                    dealer_check_value_Globel = dbvoc.subdealrer_details_value_byscode(Globel_Data.GLOvel_Distributer_Code);
//                                    if (dealer_check_value_Globel.size() > 0 && !sub_dealer_nature_of_business_table_flag.equalsIgnoreCase("") && !sub_dealer_business_product_categories_table_flag.equalsIgnoreCase("") && !sub_dealer_stocking_product_categories_table_flag.equalsIgnoreCase("")) {

                                    // sub_dealer_approval_stag_val = "All mandatory fields of sub-dealer are filled & hence has been sent to HO for approval";
//                                    if (file1.exists()) {
//                                        if (file1.delete()) {
//                                            System.out.println("file Deleted :" + feedPhotoPath);
//                                        } else {
//                                            System.out.println("file not Deleted :" + feedPhotoPath);
//                                        }
//                                    }

                                    if (file2.exists()) {
                                        if (file2.delete()) {
                                            System.out.println("file Deleted :" + claimPhotoPath);
                                        } else {
                                            System.out.println("file not Deleted :" + claimPhotoPath);
                                        }
                                    }

//                                    if (file3.exists()) {
//                                        if (file3.delete()) {
//                                            System.out.println("file Deleted :" + complaintPhotoPath);
//                                        } else {
//                                            System.out.println("file not Deleted :" + complaintPhotoPath);
//                                        }
//                                    }
//
//                                        if (file4.exists()) {
//                                            if (file4.delete()) {
//                                                System.out.println("file Deleted :" + competitionStockPhotoPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + competitionStockPhotoPath);
//                                            }
//                                        }

//                                        if (file5.exists()) {
//                                            if (file5.delete()) {
//                                                System.out.println("file Deleted :" + Visiting_Card_mCurrentPhotoPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + Visiting_Card_mCurrentPhotoPath);
//                                            }
//                                        }
//
//                                        if (file6.exists()) {
//                                            if (file6.delete()) {
//                                                System.out.println("file Deleted :" + Outlet_Display_mCurrentPhotoPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + Outlet_Display_mCurrentPhotoPath);
//                                            }
//                                        }
//
//                                        if (file7.exists()) {
//                                            if (file7.delete()) {
//                                                System.out.println("file Deleted :" + InShop_Display_mCurrentPhotoPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + InShop_Display_mCurrentPhotoPath);
//                                            }
//                                        }
//
//                                        if (file8.exists()) {
//                                            if (file8.delete()) {
//                                                System.out.println("file Deleted :" + signature_img);
//                                            } else {
//                                                System.out.println("file not Deleted :" + signature_img);
//                                            }
//                                        }
//                                    }
//                                    else
//                                    {
//                                        //sub_dealer_approval_stag_val = "All mandatory fields are not filled. Hence, sub-dealer will be in partially filled stage";
//                                    }

                                    //feedPhotoPath = "";
                                    claimPhotoPath = "";
//                                    complaintPhotoPath = "";
//                                    competitionStockPhotoPath = "";

                                } else {
                                    // Sub_Dealer_Approval_Stage(context, sub_dealer_approval_stag_val);
                                }

                                Log.d("My App", obj.toString());

                            } catch (Throwable t) {
                                Log.e("My App", "Could not parse malformed JSON: \"" + response_result_image + "\"");

                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();

                                        // Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                                    }
                                });
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                        //Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                    }
                });
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            ((Activity) context).runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                    // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                }
            });

        }

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Image Sync in Progress, Please Wait");
            dialog.setTitle("Metal");
            dialog.setCancelable(false);
            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public static class doFileUpload2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... response) {

            String domain = context.getResources().getString(R.string.service_domain) + "customer_service_feedbacks/customer_service_upload_images";
            Log.i("volley", "domain: " + domain);

            Uri uri3 = Uri.parse(response[1]);
            final File file3 = new File(uri3.getPath());

            // String urlString = domain + "sub_dealers";
            try {

                String charset = "UTF-8";
                //File uploadFile1 = new File("/sdcard/myvideo.mp4");

                MultipartUtility multipart = new MultipartUtility(domain, charset);

//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("Test-Header", "Header-Value");

                //multipart.addFormField("feedback_image", claimPhotoCode);

                //for complaint
                if (!response[0].equalsIgnoreCase("")) {
                    multipart.addFormField("complaint_code", response[0]);
                    multipart.addFormField("complaint_type", "complaint");
                }

                if (!response[1].equalsIgnoreCase("")) {
                    multipart.addFilePart("complaint_image", file3);
                }

//                //for comp stock
//                if (!competitionStockPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFormField("competition_stock_code", competitionStockCode);
//                    multipart.addFormField("competition_stock_type", "competition_stock");
//                }
//
//                if (!complaintPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("competition_stock_image", file4);
//                }


                // multipart.addFormField("claim_type", "claim");
                //multipart.addFormField("feedback_image", claimPhotoCode);


//                if (!claimPhotoCode.equalsIgnoreCase("")) {
//                    multipart.addFormField("claim_code", claimPhotoCode);
//                }


                //multipart.addFormField("sub_dealer_code", competitionStockCode);
                //  multipart.addFormField("userid", "Java,upload,Spring");


//                if (!claimPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("claim_image", file2);
//                }

//                if (!claimPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_proprietor1", file2);
//                }
//
//                if (!complaintPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_proprietor2", file3);
//                }
//
//                if (!competitionStockPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_rubber_stamp1", file4);
//                }


                List<String> response1 = multipart.finish();

                Log.v("rht", "SERVER REPLIED:");

                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

                for (String line : response1) {
                    Log.v("rht", "Line : " + line);
                    response_result_image = line;
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            try {

                                JSONObject obj = new JSONObject(response_result_image);


                                if (obj.getString("message").equalsIgnoreCase("Customer Services uploaded images successfully.")) {
                                    Global_Data.Custom_Toast(context, obj.getString("message"), "");
//                                    Intent a = new Intent(context, AllOrders_Sync.class);
//                                    context.startActivity(a);
//                                    ((Activity) context).finish();
                                    dialog.dismiss();

//                                    Log.d("anchor_st","anchor_stocking_products"+sub_dealer_anchor_stocking_products_table_flag);
//                                    dealer_check_value_Globel = dbvoc.subdealrer_details_value_byscode(Globel_Data.GLOvel_Distributer_Code);
//                                    if (dealer_check_value_Globel.size() > 0 && !sub_dealer_nature_of_business_table_flag.equalsIgnoreCase("") && !sub_dealer_business_product_categories_table_flag.equalsIgnoreCase("") && !sub_dealer_stocking_product_categories_table_flag.equalsIgnoreCase("")) {

                                    // sub_dealer_approval_stag_val = "All mandatory fields of sub-dealer are filled & hence has been sent to HO for approval";
//                                    if (file1.exists()) {
//                                        if (file1.delete()) {
//                                            System.out.println("file Deleted :" + feedPhotoPath);
//                                        } else {
//                                            System.out.println("file not Deleted :" + feedPhotoPath);
//                                        }
//                                    }

//                                    if (file2.exists()) {
//                                        if (file2.delete()) {
//                                            System.out.println("file Deleted :" + claimPhotoPath);
//                                        } else {
//                                            System.out.println("file not Deleted :" + claimPhotoPath);
//                                        }
//                                    }

                                    if (file3.exists()) {
                                        if (file3.delete()) {
                                            System.out.println("file Deleted :" + complaintPhotoPath);
                                        } else {
                                            System.out.println("file not Deleted :" + complaintPhotoPath);
                                        }
                                    }

//                                    }
//                                    else
//                                    {
//                                        //sub_dealer_approval_stag_val = "All mandatory fields are not filled. Hence, sub-dealer will be in partially filled stage";
//                                    }

                                    //feedPhotoPath = "";
                                    // claimPhotoPath = "";
                                    complaintPhotoPath = "";
//                                    competitionStockPhotoPath = "";

                                } else {
                                    // Sub_Dealer_Approval_Stage(context, sub_dealer_approval_stag_val);
                                }

                                Log.d("My App", obj.toString());

                            } catch (Throwable t) {
                                Log.e("My App", "Could not parse malformed JSON: \"" + response_result_image + "\"");

                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();

                                        // Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                                    }
                                });
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                        //Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                    }
                });
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            ((Activity) context).runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                    // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                }
            });

        }

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Image Sync in Progress, Please Wait");
            dialog.setTitle("Metal");
            dialog.setCancelable(false);
            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    public static class doFileUpload3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... response) {

            String domain = context.getResources().getString(R.string.service_domain) + "customer_service_feedbacks/customer_service_upload_images";
            Log.i("volley", "domain: " + domain);

//            Uri uri1 = Uri.parse(feedPhotoPath);
//            final File file1 = new File(uri1.getPath());

//            Uri uri2 = Uri.parse(response[1]);
//            final File file2 = new File(uri2.getPath());

//            Uri uri3 = Uri.parse(response[1]);
//            final File file3 = new File(uri3.getPath());
//
            Uri uri4 = Uri.parse(response[1]);
            final File file4 = new File(uri4.getPath());

//            Uri uri5 = Uri.parse(Visiting_Card_mCurrentPhotoPath);
//            final File file5 = new File(uri5.getPath());
//
//            Uri uri6 = Uri.parse(Outlet_Display_mCurrentPhotoPath);
//            final File file6 = new File(uri6.getPath());
//
//            Uri uri7 = Uri.parse(InShop_Display_mCurrentPhotoPath);
//            final File file7 = new File(uri7.getPath());
//
//            Uri uri8 = Uri.parse(signature_img);
//            final File file8 = new File(uri8.getPath());


            // String urlString = domain + "sub_dealers";
            try {

                String charset = "UTF-8";
                //File uploadFile1 = new File("/sdcard/myvideo.mp4");

                MultipartUtility multipart = new MultipartUtility(domain, charset);

//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("Test-Header", "Header-Value");

                //multipart.addFormField("feedback_image", claimPhotoCode);

//                //for feedback
//                if (!feedPhotoCode.equalsIgnoreCase("")) {
//                    multipart.addFormField("feedback_code", feedPhotoCode);
//                    multipart.addFormField("feedback_type", "feedback");
//                }
//
//                if (!feedPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("feedback_image", file1);
//                }

//                //for claim
//                if (!response[0].equalsIgnoreCase("")) {
//                    multipart.addFormField("claim_code", response[0]);
//                    multipart.addFormField("claim_type", "claim");
//                }
//
//                if (!response[1].equalsIgnoreCase("")) {
//                    multipart.addFilePart("claim_image", file2);
//                }

//                //for complaint
//                if (!response[0].equalsIgnoreCase("")) {
//                    multipart.addFormField("complaint_code", response[0]);
//                    multipart.addFormField("complaint_type", "complaint");
//                }
//
//                if (!response[1].equalsIgnoreCase("")) {
//                    multipart.addFilePart("complaint_image", file3);
//                }

                //for comp stock
                if (!response[0].equalsIgnoreCase("")) {
                    multipart.addFormField("competition_stock_code", response[0]);
                    multipart.addFormField("competition_stock_type", "competition_stock");
                }

                if (!response[1].equalsIgnoreCase("")) {
                    multipart.addFilePart("competition_stock_image", file4);
                }


                // multipart.addFormField("claim_type", "claim");
                //multipart.addFormField("feedback_image", claimPhotoCode);


//                if (!claimPhotoCode.equalsIgnoreCase("")) {
//                    multipart.addFormField("claim_code", claimPhotoCode);
//                }


                //multipart.addFormField("sub_dealer_code", competitionStockCode);
                //  multipart.addFormField("userid", "Java,upload,Spring");


//                if (!claimPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("claim_image", file2);
//                }

//                if (!claimPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_proprietor1", file2);
//                }
//
//                if (!complaintPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_proprietor2", file3);
//                }
//
//                if (!competitionStockPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_rubber_stamp1", file4);
//                }

//                if (!Visiting_Card_mCurrentPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_visiting_card_front1", file5);
//                }
//
//                if (!Outlet_Display_mCurrentPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_outlet_display1", file6);
//                }
//
//                if (!InShop_Display_mCurrentPhotoPath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_shop_in_shop_display1", file7);
//                }
//
//                if (!signature_img.equalsIgnoreCase("")) {
//                    multipart.addFilePart("pic_signature", file8);
//                }


                List<String> response1 = multipart.finish();

                Log.v("rht", "SERVER REPLIED:");

                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

                for (String line : response1) {
                    Log.v("rht", "Line : " + line);
                    response_result_image = line;
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            try {

                                JSONObject obj = new JSONObject(response_result_image);

                                if (obj.getString("message").equalsIgnoreCase("Customer Services uploaded images successfully.")) {
                                    Global_Data.Custom_Toast(context, obj.getString("message"), "");
//                                    Intent a = new Intent(context, AllOrders_Sync.class);
//                                    context.startActivity(a);
//                                    ((Activity) context).finish();
                                    dialog.dismiss();
//                                    Log.d("anchor_st","anchor_stocking_products"+sub_dealer_anchor_stocking_products_table_flag);
//                                    dealer_check_value_Globel = dbvoc.subdealrer_details_value_byscode(Globel_Data.GLOvel_Distributer_Code);
//                                    if (dealer_check_value_Globel.size() > 0 && !sub_dealer_nature_of_business_table_flag.equalsIgnoreCase("") && !sub_dealer_business_product_categories_table_flag.equalsIgnoreCase("") && !sub_dealer_stocking_product_categories_table_flag.equalsIgnoreCase("")) {

                                    // sub_dealer_approval_stag_val = "All mandatory fields of sub-dealer are filled & hence has been sent to HO for approval";
//                                    if (file1.exists()) {
//                                        if (file1.delete()) {
//                                            System.out.println("file Deleted :" + feedPhotoPath);
//                                        } else {
//                                            System.out.println("file not Deleted :" + feedPhotoPath);
//                                        }
//                                    }

//                                    if (file2.exists()) {
//                                        if (file2.delete()) {
//                                            System.out.println("file Deleted :" + claimPhotoPath);
//                                        } else {
//                                            System.out.println("file not Deleted :" + claimPhotoPath);
//                                        }
//                                    }

//                                    if (file3.exists()) {
//                                        if (file3.delete()) {
//                                            System.out.println("file Deleted :" + complaintPhotoPath);
//                                        } else {
//                                            System.out.println("file not Deleted :" + complaintPhotoPath);
//                                        }
//                                    }

                                    if (file4.exists()) {
                                        if (file4.delete()) {
                                            System.out.println("file Deleted :" + competitionStockPhotoPath);
                                        } else {
                                            System.out.println("file not Deleted :" + competitionStockPhotoPath);
                                        }
                                    }

//                                        if (file5.exists()) {
//                                            if (file5.delete()) {
//                                                System.out.println("file Deleted :" + Visiting_Card_mCurrentPhotoPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + Visiting_Card_mCurrentPhotoPath);
//                                            }
//                                        }
//
//                                        if (file6.exists()) {
//                                            if (file6.delete()) {
//                                                System.out.println("file Deleted :" + Outlet_Display_mCurrentPhotoPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + Outlet_Display_mCurrentPhotoPath);
//                                            }
//                                        }
//
//                                        if (file7.exists()) {
//                                            if (file7.delete()) {
//                                                System.out.println("file Deleted :" + InShop_Display_mCurrentPhotoPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + InShop_Display_mCurrentPhotoPath);
//                                            }
//                                        }
//
//                                        if (file8.exists()) {
//                                            if (file8.delete()) {
//                                                System.out.println("file Deleted :" + signature_img);
//                                            } else {
//                                                System.out.println("file not Deleted :" + signature_img);
//                                            }
//                                        }
//                                    }
//                                    else
//                                    {
//                                        //sub_dealer_approval_stag_val = "All mandatory fields are not filled. Hence, sub-dealer will be in partially filled stage";
//                                    }

                                    //feedPhotoPath = "";
                                    // claimPhotoPath = "";
                                    //complaintPhotoPath = "";
                                    competitionStockPhotoPath = "";

                                } else {
                                    // Sub_Dealer_Approval_Stage(context, sub_dealer_approval_stag_val);
                                }

                                Log.d("My App", obj.toString());

                            } catch (Throwable t) {
                                Log.e("My App", "Could not parse malformed JSON: \"" + response_result_image + "\"");

                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();

                                        // Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                                    }
                                });
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                        //Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                    }
                });
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            ((Activity) context).runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                    // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                }
            });

        }

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Image Sync in Progress, Please Wait");
            dialog.setTitle("Metal");
            dialog.setCancelable(false);
            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

//    public class doFileUpload extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... response) {
//            // String domain = getResources().getString(R.string.service_domain);
//            String domain = context.getString(R.string.service_domain) + "users/update_user_image?employee_code="+prefManager.getEmp_code();
//            Log.i("volley", "domain: " + domain);
//            File file1 = new File(response[0]);
//
//            try {
//                String charset = "UTF-8";
//                MultipartUtility multipart = new MultipartUtility(domain, charset);
//
//                if (!pictureImagePath.equalsIgnoreCase("")) {
//                    multipart.addFilePart("avatar", file1);
//                }
//
//                List<String> response1 = multipart.finish();
//                Log.v("rht", "SERVER REPLIED:");
//
//                ((Activity) context).runOnUiThread(new Runnable() {
//                    public void run() {
//                        dialog.dismiss();
//                    }
//                });
//
//                for (String line : response1) {
//                    Log.v("rht", "Line : " + line);
//                    response_result_image = line;
//                    ((Activity) context).runOnUiThread(new Runnable() {
//                        public void run() {
//
//                            try {
//
//                                JSONObject obj = new JSONObject(response_result_image);
//                                dialog.dismiss();
//                                Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//
//                                prefManager.setimage_url(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(pictureImagePath_new));
//
//                                if (obj.getString("message").equalsIgnoreCase("Profile Updated successfully")) {
//                                    if (file.exists()) {
//                                        if (file.delete()) {
//                                            System.out.println("file Deleted :" + pictureImagePath);
//                                        } else {
//                                            System.out.println("file not Deleted :" + pictureImagePath);
//                                        }
//                                    }
//                                    pictureImagePath = "";
//                                    //Activity_Back_Flag = "Home";
//                                    Intent a = new Intent(context.getApplicationContext(), MainActivity.class);
//                                    a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    context.getApplicationContext().startActivity(a);
//                                    //context.finish();
//                                }
//
//                                Log.d("My App", obj.toString());
//
//                            } catch (Throwable t) {
//                                Log.e("My App", "Could not parse malformed JSON: \"" + response_result_image + "\"");
//                                Intent a = new Intent(context.getApplicationContext(), MainActivity.class);
//                                context.startActivity(a);
//                            }
//                        }
//                    });
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                ((Activity) context).runOnUiThread(new Runnable() {
//                    public void run() {
//                        // Activity_Back_Flag = "Home";
//                        Intent a = new Intent(context.getApplicationContext(), MainActivity.class);
//                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        context.startActivity(a);
//                        //finish();
//                        dialog.dismiss();
//                    }
//                });
//            }
//            return "Executed";
//        }  //
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            ((Activity) context).runOnUiThread(new Runnable() {
//                public void run() {
//                    Intent a = new Intent(context, MainActivity.class);
//                    a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(a);
//                    //finish();
//                    dialog.dismiss();
//                    dialog.dismiss();
//                }
//            });
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            dialog.setMessage("Upload Image in Progress, Please Wait");
//            dialog.setTitle("Intranet App");
//            dialog.setCancelable(false);
//            dialog.show();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//        }
//    }

    public static class doFileUploadPdf extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... response) {

            String domain = context.getResources().getString(R.string.service_domain) + "customer_service_feedbacks/customer_service_upload_images";
            Log.i("volley", "domain: " + domain);

//            Uri uri1 = Uri.parse(feedPhotoPath);
//            final File file1 = new File(uri1.getPath());

            Uri uri2 = Uri.parse(response[1]);
            final File file2 = new File(uri2.getPath());

//            Uri uri3 = Uri.parse(complaintPhotoPath);
//            final File file3 = new File(uri3.getPath());

            // String urlString = domain + "sub_dealers";
            try {
                String charset = "UTF-8";
                //File uploadFile1 = new File("/sdcard/myvideo.mp4");
                MultipartUtility multipart = new MultipartUtility(domain, charset);

//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("Test-Header", "Header-Value");

                //multipart.addFormField("feedback_image", claimPhotoCode);

                //for claim
                if (!response[0].equalsIgnoreCase("")) {
                    multipart.addFormField("travel_code", response[0]);
                    multipart.addFormField("travel_type", "travel");
                }
                if (!response[1].equalsIgnoreCase("")) {
                    multipart.addFilePart("claim_image", file2);
                }

                List<String> response1 = multipart.finish();
                Log.v("rht", "SERVER REPLIED:");

                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                    }
                });
                for (String line : response1) {
                    Log.v("rht", "Line : " + line);
                    response_result_image = line;
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                JSONObject obj = new JSONObject(response_result_image);

                                if (obj.getString("message").equalsIgnoreCase("Customer Services uploaded images successfully.")) {
                                    Global_Data.Custom_Toast(context, obj.getString("message"), "");

                                    dialog.dismiss();

                                    if (file2.exists()) {
                                        if (file2.delete()) {
                                            System.out.println("file Deleted :" + claimPhotoPath);
                                        } else {
                                            System.out.println("file not Deleted :" + claimPhotoPath);
                                        }
                                    }

//                                    if (file3.exists()) {
//                                        if (file3.delete()) {
//                                            System.out.println("file Deleted :" + complaintPhotoPath);
//                                        } else {
//                                            System.out.println("file not Deleted :" + complaintPhotoPath);
//                                        }
//                                    }

//                                    }
//                                    else
//                                    {
//                                        //sub_dealer_approval_stag_val = "All mandatory fields are not filled. Hence, sub-dealer will be in partially filled stage";
//                                    }

                                    claimPhotoPath = "";

                                } else {
                                    // Sub_Dealer_Approval_Stage(context, sub_dealer_approval_stag_val);
                                }

                                Log.d("My App", obj.toString());

                            } catch (Throwable t) {
                                Log.e("My App", "Could not parse malformed JSON: \"" + response_result_image + "\"");

                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();

                                        // Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                                    }
                                });
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                        //Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                    }
                });
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            ((Activity) context).runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                    // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                }
            });
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Image Sync in Progress, Please Wait");
            dialog.setTitle("Metal");
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public static void SYNReturnOrder(Context contextn, String return_order_ids) {
        context = contextn;
        return_order_id = return_order_ids;

        dialog = new ProgressDialog(contextn, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog.setMessage(context.getResources().getString(R.string.dialog_wait_message));
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        new ReturnOrderAyncTask().execute();


    }

    public static class ReturnOrderAyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Void... params) {


            String uploadImage = "";
            dbvoc = new DataBaseHelper(context);

            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            final String formattedDate = df.format(c.getTime());
            JSONObject jsonBody = new JSONObject();

            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL", null);
            String Cust_domain = spf.getString("Cust_Service_Url", null);
            String domain = Cust_domain + "metal/api/v1/";


            try {

                JSONArray customer = new JSONArray();
                JSONArray product = new JSONArray();
                JSONArray order = new JSONArray();
                JSONObject product_valuenew = new JSONObject();

                String charset = "UTF-8";

                try {

                    MultipartUtility multipart = new MultipartUtility(domain + "return_orders/save_revert_orders", charset);

                    int a = 0;
                    String s = "";
                    byte[] b5;
                    List<Local_Data> return_order_con = dbvoc.GetOrders_return_byorder_id(return_order_id);

                    for (Local_Data cn : return_order_con) {
                        JSONObject product_value = new JSONObject();

                        //multipart.addFormField("order_number",  cn.get_category_code());

                        Order_number = cn.get_category_code();
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getLatitude()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cn.getLongitude())) {

                            multipart.addFormField("latitude", cn.getLatitude());
                            multipart.addFormField("longitude", cn.getLongitude());

                        } else {
                            multipart.addFormField("latitude", Global_Data.GLOvel_LATITUDE);
                            multipart.addFormField("longitude", Global_Data.GLOvel_LONGITUDE);
                        }

                        //email_adress = cn.getSub_dealer_email();
                        //Sub_MOBILE = cn.getSub_dealer_mobile();

                        multipart.addFormField("customer_code", cn.get_category_id());
                        multipart.addFormField("email", user_email);

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getImage_url1())) {
                            image_url1 = cn.getImage_url1().trim();

                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getImage_url2())) {
                            image_url2 = cn.getImage_url2().trim();

                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getImage_url3())) {
                            image_url3 = cn.getImage_url3().trim();

                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getImage_url4())) {
                            image_url4 = cn.getImage_url4().trim();

                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getImage_url5())) {
                            image_url5 = cn.getImage_url5().trim();

                        }

                        Log.d("count", "a" + ++a);
                        List<Local_Data> contactsproduct = dbvoc.Get_OrderProducts_return(cn.get_category_code());
                        for (Local_Data cnp : contactsproduct) {
                            JSONObject item = new JSONObject();

//                            multipart.addFormField("product_code", cnp.get_delivery_product_id());
//                            multipart.addFormField("quantity", cnp.get_stocks_product_quantity());
//                            multipart.addFormField("batch_no", cnp.getBatch_no());
//                            multipart.addFormField("refund_amount", cnp.getRefund_amount());
//                            multipart.addFormField("details", cnp.getRemarks());


                            total_ammount += Double.valueOf(cnp.get_Claims_amount());

                            item.put("product_code", cnp.get_delivery_product_id());
                            item.put("quantity", cnp.get_stocks_product_quantity());
                            item.put("batch_no", cnp.getBatch_no());
                            item.put("refund_amount", cnp.getRefund_amount());
                            item.put("details", cnp.getRemarks());
                            product.put(item);

                        }
                    }


                    Log.i("volley", "domain: " + domain);
                    Log.i("volley", "product Array: " + product.toString());

                    multipart.addFormField("return_order_details", product.toString());

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(image_url1)) {

                        Uri uri1 = Uri.parse(image_url1);
                        final File file1 = new File(uri1.getPath());

                        if (!image_url1.equalsIgnoreCase("")) {
                            //multipart.addFormField("is_picture1", "true");
                            multipart.addFilePart("picture1", file1);
                        } else {
                            // multipart.addFormField("is_picture1", "false");
                        }
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(image_url2)) {

                        Uri uri2 = Uri.parse(image_url2);
                        final File file2 = new File(uri2.getPath());

                        if (!image_url2.equalsIgnoreCase("")) {
                            //multipart.addFormField("is_picture1", "true");
                            multipart.addFilePart("picture2", file2);
                        } else {
                            // multipart.addFormField("is_picture1", "false");
                        }
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(image_url3)) {

                        Uri uri3 = Uri.parse(image_url3);
                        final File file3 = new File(uri3.getPath());

                        if (!image_url3.equalsIgnoreCase("")) {
                            //multipart.addFormField("is_picture1", "true");
                            multipart.addFilePart("picture3", file3);
                        } else {
                            // multipart.addFormField("is_picture1", "false");
                        }
                    }


                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(image_url4)) {

                        Uri uri4 = Uri.parse(image_url4);
                        final File file4 = new File(uri4.getPath());

                        if (!image_url4.equalsIgnoreCase("")) {
                            //multipart.addFormField("is_picture1", "true");
                            multipart.addFilePart("picture4", file4);
                        } else {
                            // multipart.addFormField("is_picture1", "false");
                        }
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(image_url5)) {

                        Uri uri5 = Uri.parse(image_url5);
                        final File file5 = new File(uri5.getPath());

                        if (!image_url5.equalsIgnoreCase("")) {
                            //multipart.addFormField("is_picture1", "true");
                            multipart.addFilePart("picture5", file5);
                        } else {
                            // multipart.addFormField("is_picture1", "false");
                        }
                    }


//                    Uri uri3 = Uri.parse(image_url3);
//                    final File file3 = new File(uri3.getPath());
//
//                    Uri uri4 = Uri.parse(image_url4);
//                    final File file4 = new File(uri4.getPath());
//
//                    Uri uri5 = Uri.parse(image_url5);
//                    final File file5 = new File(uri5.getPath());


                    //File uploadFile1 = new File("/sdcard/myvideo.mp4");


                    // multipart.addFormField("sub_dealer_order_details", product.toString());
                    //  multipart.addFormField("imei_no", Global_Data.device_id);


//                    if (!image_url2.equalsIgnoreCase("")) {
//                        multipart.addFilePart("picture2", file2);
//                    }
//
//                    if (!image_url3.equalsIgnoreCase("")) {
//                        multipart.addFilePart("picture3", file3);
//                    }
//
//                    if (!image_url4.equalsIgnoreCase("")) {
//                        multipart.addFilePart("picture4", file4);
//                    }
//
//                    if (!image_url5.equalsIgnoreCase("")) {
//                        multipart.addFilePart("picture5", file5);
//                    }

                    List<String> response1 = multipart.finish();

                    Log.v("rht", "SERVER REPLIED:");

                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
                        }
                    });

                    for (String line : response1) {
                        Log.v("rht", "Line : " + line);
                        response_result = line;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {

                                try {
                                    JSONObject obj = null;
                                    try {
                                        obj = new JSONObject(response_result);
                                    } catch (JSONException err) {
                                        Log.d("Error", err.toString());
                                    }

                                    dialog.dismiss();
                                    //Successcul message issue on submit. Message should be "Promotional activity submitted successfully"
                                    if (!obj.getString("result").equalsIgnoreCase("User Not Found")) {

                                        // Global_Data.Custom_Toast(context, obj.getString("result"),"");

                                        String gaddress = "";
                                        try {
                                            if (Global_Data.address.equalsIgnoreCase("null")) {
                                                gaddress = "";
                                            } else {
                                                gaddress = Global_Data.address;
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                public void run() {


                                                    dialog.dismiss();
                                                }
                                            });
                                        }

                                        String val = "";
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            public void run() {

                                                dialog.dismiss();
                                                image_url1 = "";
                                                image_url2 = "";
                                                image_url3 = "";
                                                image_url4 = "";
                                                image_url5 = "";

                                                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Return Order");
                                                if (dir.isDirectory()) {
                                                    String[] children = dir.list();
                                                    for (int i = 0; i < children.length; i++) {
                                                        new File(dir, children[i]).delete();
                                                    }
                                                }

                                                Global_Data.Search_Category_name = "";
                                                Global_Data.return_oredr.clear();
                                                Global_Data.GLObalOrder_id_return = "";
                                                Global_Data.GLOvel_GORDER_ID_RETURN = "";
                                                Global_Data.Orderproduct_remarks.clear();
                                                Global_Data.Orderproduct_detail1.clear();

                                                Global_Data.Custom_Toast(context, context.getResources().getString(R.string.PNO_Sucess), "yes");

                                                Intent i = new Intent(context, Neworderoptions.class);
                                                context.startActivity(i);
                                                ((Activity) context).finish();

//                                                final Dialog dialog1 = new Dialog(context);
//                                                dialog1.setCancelable(false);
//
//                                                //tell the Dialog to use the dialog.xml as it's layout description
//                                                dialog1.setContentView(R.layout.dialog);
//                                                dialog1.setTitle("Return Order Status :");
//
//                                                TextView txt = dialog1.findViewById(R.id.txtOrderID);
//
//                                                txt.setText("Return Order is generated.");
//                                                TextView txtMessage = dialog1.findViewById(R.id.txtMessage);
//                                                TextView txtEmail = dialog1.findViewById(R.id.txtEmail);
//                                                ImageView image = dialog1.findViewById(R.id.image);
//
//                                                txtEmail.setText("Mail will be sent to " + email_adress);
//                                                txtEmail.setVisibility(View.GONE);
//                                                image.setVisibility(View.GONE);
////                                                if (!Sub_MOBILE.equalsIgnoreCase("")) {
////                                                    txtMessage.setText("Sms Send Successfully");
////                                                }
//
//
//                                                ImageView dialogButton = dialog1.findViewById(R.id.dialogButton);
//
//                                                dialogButton.setOnClickListener(new OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        dialog1.dismiss();
//                                                        Intent intentn = new Intent(context, MainActivity.class);
//                                                        context.startActivity(intentn);
//                                                        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                                                        ((Activity) context).finish();
//                                                    }
//                                                });
//
//                                                dialog1.show();
                                            }
                                        });


                                    } else {

                                        Global_Data.Custom_Toast(context, obj.getString("result"), "yes");

                                    }

                                    Log.d("My App", obj.toString());

                                } catch (Throwable t) {
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response_result + "\"");

                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        public void run() {

                                            dialog.dismiss();

                                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");


                                        }
                                    });
                                }


                            }
                        });


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();

                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

            }
            return null;
        }
    }


    public static void SYNStock(Context contextn, String Stock_statuss, String Detailss, String pic1s, String pic2s, String pic3s) {
        context = contextn;
        Stock_status = Stock_statuss;
        Details = Detailss;
        pic1 = pic1s;
        pic2 = pic2s;
        pic3 = pic3s;

        dialog = new ProgressDialog(contextn, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog.setMessage(context.getResources().getString(R.string.dialog_wait_message));
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        new UpdateStockrAyncTask().execute();


    }

    public static class UpdateStockrAyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Void... params) {


            String uploadImage = "";
            dbvoc = new DataBaseHelper(context);

            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            final String formattedDate = df.format(c.getTime());
            JSONObject jsonBody = new JSONObject();

            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL", null);
            String shopcode = spf.getString("shopcode", null);
            String Cust_domain = spf.getString("Cust_Service_Url", null);
            String domain = Cust_domain + "metal/api/v1/";


            try {

                JSONArray customer = new JSONArray();
                JSONArray product = new JSONArray();
                JSONArray order = new JSONArray();
                JSONObject product_valuenew = new JSONObject();

                String charset = "UTF-8";

                try {

                    MultipartUtility multipart = new MultipartUtility(domain + "customer_stocks/create_customer_stock_details\n", charset);

                    int a = 0;
                    String s = "";
                    byte[] b5;

                    multipart.addFormField("customer_code", shopcode);
                    multipart.addFormField("latitude", Global_Data.GLOvel_LATITUDE);
                    multipart.addFormField("longitude", Global_Data.GLOvel_LONGITUDE);
                    multipart.addFormField("email", user_email);
                    multipart.addFormField("stock_status", Stock_status);
                    multipart.addFormField("details", Details);
                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(pic1) && pic1.indexOf("http:") == -1) {
                        image_url1 = pic1;
                    }
                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(pic2) && pic2.indexOf("http:") == -1) {
                        image_url2 = pic2;

                    }
                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(pic3) && pic3.indexOf("http:") == -1) {
                        image_url3 = pic3;

                    }

                    Log.d("count", "a" + ++a);
                    if (!(Global_Data.Order_hashmap.isEmpty())) {
                        try {
                            for (Object name : Global_Data.Order_hashmap.keySet()) {
                                JSONObject item = new JSONObject();
                                Object key = name.toString();
                                String key_array[] = String.valueOf(key).split("&");
                                String key_array2[] = String.valueOf(key).split("@");
                                String key_product_code[] = String.valueOf(key_array2[0]).split("&");
                                Object value = Global_Data.Order_hashmap.get(key);
                                item.put("product_code", key_product_code[1]);
                                item.put("id", key_array2[1]);
                                item.put("stock_qty", value.toString());
                                product.put(item);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }


                    multipart.addFormField("customer_stock", product.toString());
                    Log.i("data", "data: " + product.toString());
                    Log.i("volley", "domain: " + domain);


                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(image_url1)) {
                        Uri uri1 = Uri.parse(image_url1);
                        final File file1 = new File(uri1.getPath());
                        multipart.addFilePart("picture1", file1);
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(image_url2)) {
                        Uri uri2 = Uri.parse(image_url2);
                        final File file2 = new File(uri2.getPath());
                        multipart.addFilePart("picture2", file2);
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(image_url3)) {
                        Uri uri3 = Uri.parse(image_url3);
                        final File file3 = new File(uri3.getPath());
                        multipart.addFilePart("picture3", file3);
                    }

                    List<String> response1 = multipart.finish();

                    Log.v("rht", "SERVER REPLIED:");

                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
                        }
                    });

                    for (String line : response1) {
                        Log.v("rht", "Line : " + line);
                        response_result = line;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {

                                try {

                                    JSONObject obj = null;
                                    try {
                                        obj = new JSONObject(response_result);
                                    } catch (JSONException err) {
                                        Log.d("Error", err.toString());
                                    }

                                    dialog.dismiss();
                                    //Successcul message issue on submit. Message should be "Promotional activity submitted successfully"
                                    if (!obj.getString("result").equalsIgnoreCase("Device Not Found") && !obj.getString("result").equalsIgnoreCase("Invalid parameters")) {
//                                        Toast toast = Toast.makeText(context, obj.getString("result"),
//                                                Toast.LENGTH_SHORT);

                                        String gaddress = "";
                                        try {
                                            if (Global_Data.address.equalsIgnoreCase("null")) {
                                                gaddress = "";
                                            } else {
                                                gaddress = Global_Data.address;
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                public void run() {


                                                    dialog.dismiss();
                                                }
                                            });
                                        }

                                        String val = "";
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            public void run() {

                                                dialog.dismiss();
                                                image_url1 = "";
                                                image_url2 = "";
                                                image_url3 = "";
                                                image_url4 = "";
                                                image_url5 = "";
                                                Global_Data.Order_hashmap.clear();
                                                Kot_Gloval.Companion.setStock_pick1("");
                                                Kot_Gloval.Companion.setStock__pick2("");
                                                Kot_Gloval.Companion.setStock__pick3("");
                                                Global_Data.image_counter = 0;
                                                Global_Data.Number.clear();

                                                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AnchorUpdateStock");
                                                if (dir.isDirectory()) {
                                                    String[] children = dir.list();
                                                    for (int i = 0; i < children.length; i++) {
                                                        new File(dir, children[i]).delete();
                                                    }
                                                }


//                                                File storageDir  = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AnchorUpdateStock");
//
//                                                if (storageDir.exists()) {
//                                                    storageDir.delete();
//                                                }


                                                Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Stock_Updated_successfully), "yes");
                                                Intent i = new Intent(context, Sales_Dash.class);
                                                context.startActivity(i);
                                                ((Activity) context).finish();
                                            }
                                        });


                                    } else {


                                        Global_Data.Custom_Toast(context, obj.getString("result"), "yes");

                                    }

                                    Log.d("My App", obj.toString());

                                } catch (Throwable t) {
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response_result + "\"");

                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        public void run() {

                                            dialog.dismiss();

                                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");


                                        }
                                    });
                                }


                            }
                        });


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();

                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

            }
            return null;
        }
    }

    public static void SYNCustomer(Context contextn, String customer_flagg, String retailer_codes) {
        context = contextn;
        customer_flag = customer_flagg;
        retailer_code = retailer_codes;
        dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog.setMessage(context.getResources().getString(R.string.dialog_wait_message));
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        new UpdateCustomerrAyncTask().execute();

    }

    public static void SyncMarketingMaterial(Context contextn, String picturepath, String cName, String POSMText, String ChillersText, String HangersText, String OthersText, String txtRemark) {
        context = contextn;

        dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog.setMessage(context.getResources().getString(R.string.dialog_wait_message));
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        new MarketingMaterialAyncTask().execute(picturepath, cName, POSMText, ChillersText, HangersText, OthersText, txtRemark);

    }


    public static void SyncFeedbackDynamic(Context contextn, String tech_picpath, String dealer_picpath, String tech_signpath, String dealer_signpath) {
        context = contextn;

        dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog.setMessage(context.getResources().getString(R.string.dialog_wait_message));
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        new FeedbackDynamicAyncTask().execute(tech_picpath, dealer_picpath, tech_signpath, dealer_signpath);

    }

    public static void SyncRating(Context contextn, String cName, String POSMText, String ChillersText) {
        context = contextn;

        dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog.setMessage(context.getResources().getString(R.string.dialog_wait_message));
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        new RatingAyncTask().execute(cName, POSMText, ChillersText);

    }


    public static void SyncEditProfile(Context contextn, String picturepath, String first_name, String last_name, String designation, String contact_no, String address, String pan_no, String adhar_no, String vehicle_no, String birthday) {
        context = contextn;

        dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog.setMessage(context.getResources().getString(R.string.dialog_wait_message));
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        new EditProfileAyncTask().execute(picturepath, first_name, last_name, designation, contact_no, address, pan_no, adhar_no, vehicle_no, birthday);

    }


    public static class UpdateCustomerrAyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Void... params) {


            String uploadImage = "";
            dbvoc = new DataBaseHelper(context);

            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            final String formattedDate = df.format(c.getTime());
            JSONObject jsonBody = new JSONObject();

            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL", null);
            String shopcode = spf.getString("shopcode", null);
            String Cust_domain = spf.getString("Cust_Service_Url", null);
            String domain = Cust_domain + "metal/api/v1/";

            try {

                JSONArray customer = new JSONArray();
                JSONArray product = new JSONArray();
                JSONArray order = new JSONArray();
                JSONObject product_valuenew = new JSONObject();

                String charset = "UTF-8";
                try {

                    MultipartUtility multipart = new MultipartUtility(domain + "customers/submit_cust_details", charset);

                    multipart.addHeaderField("Connection", "close");

                    if (customer_flag.equalsIgnoreCase("customer")) {

                        multipart.addFormField("email", user_email);
                        multipart.addFormField("customer_code", shopcode);
                        multipart.addFormField("latitude", Global_Data.GLOvel_LATITUDE);
                        multipart.addFormField("longitude", Global_Data.GLOvel_LONGITUDE);

                        List<Local_Data> cont1 = dbvoc.getCustomername(Global_Data.GLOvel_CUSTOMER_ID);
                        if (cont1.size() > 0) {

                            for (Local_Data cnt1 : cont1) {

                                multipart.addFormField("address", cnt1.getAddress());
                                multipart.addFormField("shop_name", cnt1.getCUSTOMER_SHOPNAME());
                                multipart.addFormField("google_address", cnt1.getADDRESS());
                                multipart.addFormField("street", cnt1.getSTREET());
                                multipart.addFormField("landmark", cnt1.getLANDMARK());
                                multipart.addFormField("pincode", cnt1.getPIN_CODE());
                                multipart.addFormField("mobile1", cnt1.getMOBILE_NO());
                                multipart.addFormField("mobile2", cnt1.getMobile2());
                                multipart.addFormField("landline_no", cnt1.getLANDLINE_NO());
                                multipart.addFormField("customer_email", cnt1.getEMAIL_ADDRESS());
                                multipart.addFormField("gstin", cnt1.getvatin());
                                multipart.addFormField("bank_account_name", cnt1.getBank_account_name());
                                multipart.addFormField("bank_account_no", cnt1.getBank_account_no());
                                multipart.addFormField("bank_account_ifsc", cnt1.getBank_account_ifsc());
                                multipart.addFormField("aadhar_no", cnt1.getAdhar_no());
                                multipart.addFormField("pan_card_no", cnt1.getPan_card_no());
                                multipart.addFormField("shop_anniversary_date", cnt1.getShop_anniversary_date());
                                multipart.addFormField("date_of_birthday", cnt1.getDate_of_birthday());
                                multipart.addFormField("customer_flag", "customer");


                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnt1.getVisitc_img()) && cnt1.getVisitc_img().indexOf("http:") == -1) {
                                    image_url1 = cnt1.getVisitc_img();
                                }
                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnt1.getInshop_img()) && cnt1.getInshop_img().indexOf("http:") == -1) {
                                    image_url2 = cnt1.getInshop_img();

                                }
                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnt1.getSignboard_img()) && cnt1.getSignboard_img().indexOf("http:") == -1) {
                                    image_url3 = cnt1.getSignboard_img();

                                }
                            }
                        }


                    } else {

                        multipart.addFormField("email", user_email);
                        multipart.addFormField("customer_code", shopcode);
                        // multipart.addFormField("customer_email", Global_Data.GLOvel_CUSTOMER_ID);
                        multipart.addFormField("latitude", Global_Data.GLOvel_LATITUDE);
                        multipart.addFormField("longitude", Global_Data.GLOvel_LONGITUDE);

                        List<Local_Data> cont1 = dbvoc.getRetailer_bycode(retailer_code);
                        if (cont1.size() > 0) {

                            for (Local_Data cnt1 : cont1) {

                                multipart.addFormField("code", cnt1.getCode());
                                multipart.addFormField("name", cnt1.getCUSTOMER_NAME());
                                multipart.addFormField("shop_name", cnt1.getCUSTOMER_SHOPNAME());
                                multipart.addFormField("customer_type_id", cnt1.getCustomer_type_id());
                                multipart.addFormField("customer_category_id", cnt1.getCustomerCategory_id());
                                multipart.addFormField("beat_code", cnt1.getBEAT_ID());
                                multipart.addFormField("city_code", cnt1.getCITY_ID());
                                multipart.addFormField("state_code", cnt1.getSTATE_ID());
                                multipart.addFormField("address", cnt1.getAddress());
                                multipart.addFormField("street", cnt1.getSTREET());
                                multipart.addFormField("landmark", cnt1.getLANDMARK());
                                multipart.addFormField("pincode", cnt1.getPIN_CODE());
                                multipart.addFormField("mobile_no", cnt1.getMOBILE_NO());
                                multipart.addFormField("retailer_email", cnt1.getEMAIL_ADDRESS());
                                multipart.addFormField("gstin", cnt1.getvatin());
                                multipart.addFormField("customer_flag", "retailer");
                                multipart.addFormField("cust", "");

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnt1.getVisitc_img()) && cnt1.getVisitc_img().indexOf("http:") == -1) {
                                    image_url1 = cnt1.getVisitc_img();
                                }
                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnt1.getInshop_img()) && cnt1.getInshop_img().indexOf("http:") == -1) {
                                    image_url2 = cnt1.getInshop_img();

                                }
                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cnt1.getSignboard_img()) && cnt1.getSignboard_img().indexOf("http:") == -1) {
                                    image_url3 = cnt1.getSignboard_img();

                                }
                            }
                        }
                    }

                    Log.i("data", "data: " + product.toString());
                    Log.i("volley", "domain: " + domain);

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(image_url1)) {
                        Uri uri1 = Uri.parse(image_url1);
                        final File file1 = new File(uri1.getPath());

                        try {
                            if (file1.exists()) {
                                multipart.addFilePart("inshop_display", file1);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(image_url2)) {
                        Uri uri2 = Uri.parse(image_url2);
                        final File file2 = new File(uri2.getPath());

                        try {
                            if (file2.exists()) {
                                multipart.addFilePart("visiting_card", file2);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(image_url3)) {
                        Uri uri3 = Uri.parse(image_url3);
                        final File file3 = new File(uri3.getPath());

                        try {
                            if (file3.exists()) {
                                multipart.addFilePart("signboard", file3);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }

                    List<String> response1 = multipart.finish();

                    Log.v("rht", "SERVER REPLIED:");

                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    for (String line : response1) {
                        Log.v("rht", "Line : " + line);
                        response_result = line;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject obj = null;
                                    try {
                                        obj = new JSONObject(response_result);
                                    } catch (JSONException err) {
                                        Log.d("Error", err.toString());
                                    }

                                    //Successcul message issue on submit. Message should be "Promotional activity submitted successfully"

                                    if (obj.getString("result").equalsIgnoreCase("Email has already been taken,Aadhar no has already been taken")) {
                                        dialog.dismiss();
                                        Global_Data.Custom_Toast(context, "Email and Aadhar no. has already been taken", "Yes");
                                    } else if (!obj.getString("result").equalsIgnoreCase("Device Not Found") && !obj.getString("result").equalsIgnoreCase("Invalid parameters") && !obj.getString("result").equalsIgnoreCase("User Not Found") && !obj.getString("result").equalsIgnoreCase("Email has already been taken,Aadhar no has already been taken")) {
//                                        Toast toast = Toast.makeText(context, obj.getString("result"),
//                                                Toast.LENGTH_SHORT);

                                        String gaddress = "";
                                        try {
                                            if (Global_Data.address.equalsIgnoreCase("null")) {
                                                gaddress = "";
                                            } else {
                                                gaddress = Global_Data.address;
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                public void run() {
                                                    dialog.dismiss();
                                                }
                                            });
                                        }

                                        String val = "";
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                                String val = "";
                                                if (!customer_flag.equalsIgnoreCase("customer")) {
                                                    Global_Data.Custom_Toast(context, "Retailer Saved Successfully", "Yes");
                                                    try {
                                                        dbvoc.updateRetailerby_CreateAt(val);
                                                        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MetalAppRetailer");
                                                        if (dir.isDirectory()) {
                                                            String[] children = dir.list();
                                                            for (int i = 0; i < children.length; i++) {
                                                                if (children[i].equalsIgnoreCase(image_url1)) {
                                                                    new File(dir, children[i]).delete();
                                                                }

                                                                if (children[i].equalsIgnoreCase(image_url2)) {
                                                                    new File(dir, children[i]).delete();
                                                                }

                                                                if (children[i].equalsIgnoreCase(image_url3)) {
                                                                    new File(dir, children[i]).delete();
                                                                }

                                                            }
                                                        }
                                                    } catch (Exception ex) {
                                                        ex.printStackTrace();
                                                    }

                                                    dbvoc.getDeleteTableretailer(retailer_code);
                                                    image_url1 = "";
                                                    image_url2 = "";
                                                    image_url3 = "";
                                                    image_url4 = "";
                                                    image_url5 = "";
                                                    Intent i = new Intent(context, MainActivity.class);
                                                    context.startActivity(i);
                                                    ((Activity) context).finish();
                                                } else {
                                                    dbvoc.updateCustomerby_CreateAt(val);
                                                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.CEDIT), "yes");
                                                    image_url1 = "";
                                                    image_url2 = "";
                                                    image_url3 = "";
                                                    image_url4 = "";
                                                    image_url5 = "";
                                                    Intent i = new Intent(context, Sales_Dash.class);
                                                    context.startActivity(i);
                                                    ((Activity) context).finish();
                                                }


                                            }
                                        });


                                    } else {

                                        Global_Data.Custom_Toast(context, obj.getString("result"), "yes");
                                    }

                                    Log.d("My App", obj.toString());

                                } catch (Throwable t) {
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response_result + "\"");

                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        public void run() {

                                            dialog.dismiss();

                                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");


                                        }
                                    });
                                }


                            }
                        });


                    }
                } catch (Exception e) {
                    Log.i("eror","eror"+e.toString());
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();

                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.6", "yes");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        try {
                            if ((dialog != null) && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } catch (final IllegalArgumentException e) {
                            // Handle or log or ignore
                        } catch (final Exception e) {
                            // Handle or log or ignore
                        } finally {
                            dialog = null;
                        }
                    }
                });

            }
            return null;
        }
    }


    public static class MarketingMaterialAyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            String user_email22 = spf.getString("USER_EMAIL", null);
            String shop_code = spf.getString("shopcode", null);

            try {
                String charset = "UTF-8";
                String domain = context.getResources().getString(R.string.service_domain);
                try {
                    final MultipartUtility multipart = new MultipartUtility(domain + "marketing_materials/create_marketing_materials", charset);

                    if (params[0].length() > 0) {
                        Uri uri1 = Uri.parse(params[0]);
                        final File file1 = new File(uri1.getPath());
                        multipart.addFilePart("picture1", file1);
                    } else {
                        // multipart.addFilePart("expenses_travel_image", null);
                    }

                    multipart.addFormField("email", user_email22);
                    multipart.addFormField("customer_code", shop_code);

                    if (params[1].length() > 0) {
                        multipart.addFormField("competitor_name", params[1]);
                    }

                    if (params[2].length() > 0) {
                        multipart.addFormField("posm", params[2]);
                    }

                    if (params[3].length() > 0) {
                        multipart.addFormField("chillers", params[3]);
                    }

                    if (params[4].length() > 0) {
                        multipart.addFormField("hangers", params[4]);
                    }

                    if (params[5].length() > 0) {
                        multipart.addFormField("others", params[5]);
                    }

                    if (params[6].length() > 0) {
                        multipart.addFormField("remark", params[6]);
                    }

                    Log.i("volley", "domain: " + domain);
                    List<String> response1 = multipart.finish();
                    Log.v("rht", "SERVER REPLIED:");

                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    for (String line : response1) {
                        Log.v("rht", "Line : " + line);
                        response_result = line;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject obj = new JSONObject(response_result);
                                    //dialog.dismiss();
                                    //Successcul message issue on submit. Message should be "Promotional activity submitted successfully"
                                    if (!obj.getString("result").equalsIgnoreCase("User Not Found")) {
                                        Toast toast = Toast.makeText(context, obj.getString("result"), Toast.LENGTH_SHORT);
                                        params[0] = "";

//                                        if (expenseImageFile.exists()) {
//                                            if (expenseImageFile.delete()) {
//                                                System.out.println("file Deleted :" + pictureImagePath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + pictureImagePath);
//                                            }
//                                        }

                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                                File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Return Order");
                                                if (!storageDir.exists()) {
                                                    storageDir.delete();
                                                }
                                                Global_Data.Custom_Toast(context, "Marketing Material Submitted Successfully", "yes");
                                                ((Activity) context).finish();
                                            }
                                        });
                                    } else {


                                        Global_Data.Custom_Toast(context, obj.getString("result"), "yes");
                                    }
                                    Log.d("My App", obj.toString());
                                } catch (Throwable t) {
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response_result + "\"");

                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();

                                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");
                                        }
                                    });
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();

                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });
            }
            return null;
        }
    }

    public static class FeedbackDynamicAyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            String user_email22 = spf.getString("USER_EMAIL", null);
            String shop_code = spf.getString("shopcode", null);

            try {
                String charset = "UTF-8";
                String domain = context.getResources().getString(R.string.service_domain);
                try {
                    final MultipartUtility multipart = new MultipartUtility(domain + "pro_feedback/create_pro_feedback", charset);

                    multipart.addFormField("email", user_email22);
                    multipart.addFormField("customer_code", shop_code);

                    if (params[0].length() > 0) {
                        Uri uri1 = Uri.parse(params[0]);
                        final File file1 = new File(uri1.getPath());
                        multipart.addFilePart("user_photo", file1);
                    } else {
                        // multipart.addFilePart("expenses_travel_image", null);
                    }

                    if (params[1].length() > 0) {
                        Uri uri2 = Uri.parse(params[1]);
                        final File file2 = new File(uri2.getPath());
                        multipart.addFilePart("dealer_photo", file2);
                    } else {
                        // multipart.addFilePart("expenses_travel_image", null);
                    }

                    if (params[2].length() > 0) {
                        Uri uri3 = Uri.parse(params[2]);
                        final File file3 = new File(uri3.getPath());
                        multipart.addFilePart("user_signature", file3);
                    } else {
                        // multipart.addFilePart("expenses_travel_image", null);
                    }

                    if (params[3].length() > 0) {
                        Uri uri4 = Uri.parse(params[3]);
                        final File file4 = new File(uri4.getPath());
                        multipart.addFilePart("dealer_signature", file4);
                    } else {
                        // multipart.addFilePart("expenses_travel_image", null);
                    }

                    Log.i("volley", "domain: " + domain);
                    List<String> response1 = multipart.finish();
                    Log.v("rht", "SERVER REPLIED:");

                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    for (String line : response1) {
                        Log.v("rht", "Line : " + line);
                        response_result = line;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject obj = new JSONObject(response_result);
                                    if (!obj.getString("result").equalsIgnoreCase("User Not Found")) {
                                        Toast toast = Toast.makeText(context, obj.getString("result"), Toast.LENGTH_SHORT);
                                        params[0] = "";

//                                        if (expenseImageFile.exists()) {
//                                            if (expenseImageFile.delete()) {
//                                                System.out.println("file Deleted :" + pictureImagePath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + pictureImagePath);
//                                            }
//                                        }

                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                                File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Return Order");
                                                if (!storageDir.exists()) {
                                                    storageDir.delete();
                                                }
                                                Global_Data.Custom_Toast(context, "Feedback Submitted Successfully", "yes");
                                                ((Activity) context).finish();
                                            }
                                        });
                                    } else {

                                        Global_Data.Custom_Toast(context, obj.getString("result"), "yes");
                                    }
                                    Log.d("My App", obj.toString());
                                } catch (Throwable t) {
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response_result + "\"");

                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();

                                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");
                                        }
                                    });
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();

                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });
            }
            return null;
        }
    }

    public static class RatingAyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            String user_email22 = spf.getString("USER_EMAIL", null);
            String shop_code = spf.getString("shopcode", null);

            try {
                String charset = "UTF-8";
                String domain = context.getResources().getString(R.string.service_domain);
                try {
                    final MultipartUtility multipart = new MultipartUtility(domain + "marketing_materials/create_competitor_rating", charset);


                    multipart.addFormField("email", user_email22);
                    multipart.addFormField("customer_code", shop_code);

                    String aaa = params[0];
                    String bbb = params[1];
                    String ccc = params[2];

                    if (aaa.length() > 0) {
                        multipart.addFormField("competitor_name", aaa);
                    }

                    if (bbb.length() > 0) {
                        multipart.addFormField("customer_happiness_on_product", bbb);
                    }

                    if (ccc.length() > 0) {
                        multipart.addFormField("product_quality", ccc);
                    }

                    Log.i("volley", "domain: " + domain);
                    List<String> response1 = multipart.finish();
                    Log.v("rht", "SERVER REPLIED:");

                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    for (String line : response1) {
                        Log.v("rht", "Line : " + line);
                        response_result = line;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject obj = new JSONObject(response_result);
                                    //dialog.dismiss();
                                    //Successcul message issue on submit. Message should be "Promotional activity submitted successfully"
                                    if (!obj.getString("result").equalsIgnoreCase("User Not Found")) {
                                        Toast toast = Toast.makeText(context, obj.getString("result"), Toast.LENGTH_SHORT);

                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                                File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Return Order");
                                                if (!storageDir.exists()) {
                                                    storageDir.delete();
                                                }
                                                Global_Data.Custom_Toast(context, "Rating Submitted Successfully", "yes");
                                                ((Activity) context).finish();
                                            }
                                        });
                                    } else {

                                        Global_Data.Custom_Toast(context, obj.getString("result"), "yes");
                                    }
                                    Log.d("My App", obj.toString());
                                } catch (Throwable t) {
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response_result + "\"");

                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();

                                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");
                                        }
                                    });
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();

                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });
            }
            return null;
        }
    }


    public static class EditProfileAyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            String user_email22 = spf.getString("USER_EMAIL", null);

            try {
                String charset = "UTF-8";
                String domain = context.getResources().getString(R.string.service_domain);
                try {
                    final MultipartUtility multipart = new MultipartUtility(domain + "users/update_user_profile", charset);

                    if (params[0].length() > 0) {
                        Uri uri1 = Uri.parse(params[0]);
                        final File file1 = new File(uri1.getPath());
                        multipart.addFilePart("profile_image", file1);
                    } else {
                        // multipart.addFilePart("expenses_travel_image", null);
                    }

                    multipart.addFormField("email", user_email22);

                    String firstname = params[1];
                    String lastname = params[2];
                    String designation = params[3];
                    String mob_no = params[4];
                    String address = params[5];
                    String pan_card_no = params[6];
                    String aadhar_no = params[7];
                    String vehicle_no = params[8];
                    String birth_date = params[9];

                    if (firstname.length() > 0) {
                        multipart.addFormField("first_name", firstname);
                    }

                    if (lastname.length() > 0) {
                        multipart.addFormField("last_name", lastname);
                    }

                    if (designation.length() > 0) {
                        multipart.addFormField("designation", designation);
                    }

                    if (mob_no.length() > 0) {
                        multipart.addFormField("mob_no", mob_no);
                    }

                    if (address.length() > 0) {
                        multipart.addFormField("address", address);
                    }

                    if (pan_card_no.length() > 0) {
                        multipart.addFormField("pan_card_no", pan_card_no);
                    }

                    if (vehicle_no.length() > 0) {
                        multipart.addFormField("vehicle_no", vehicle_no);
                    }

                    if (birth_date.length() > 0) {
                        multipart.addFormField("birth_date", birth_date);
                    }

                    if (aadhar_no.length() > 0) {
                        multipart.addFormField("aadhar_no", aadhar_no);
                    }

                    Log.i("volley", "domain: " + domain);
                    List<String> response1 = multipart.finish();
                    Log.v("rht", "SERVER REPLIED:");

                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    for (String line : response1) {
                        Log.v("rht", "Line : " + line);
                        response_result = line;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject obj = new JSONObject(response_result);
                                    //dialog.dismiss();
                                    //Successcul message issue on submit. Message should be "Promotional activity submitted successfully"
                                    if (!obj.getString("result").equalsIgnoreCase("User Not Found")) {
                                        Toast toast = Toast.makeText(context, obj.getString("result"), Toast.LENGTH_SHORT);

                                        if (params[0].length() > 0) {
                                            SharedPreferences pref = context.getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor edit = pref.edit();
                                            edit.putString("img_path", params[0]);
                                            edit.commit();
                                        }

                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                                File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Return Order");
                                                if (!storageDir.exists()) {  //((Activity) context).getResources().getString(R.string.PNO_Sucess)
                                                    storageDir.delete();
                                                }
                                                Global_Data.Custom_Toast(context, "Profile Created Successfully", "yes");
                                                Intent a = new Intent(context, MainActivity.class);
                                                context.startActivity(a);
                                                ((Activity) context).finish();
                                            }
                                        });
                                    } else {

                                        Global_Data.Custom_Toast(context, obj.getString("result"), "yes");
                                    }
                                    Log.d("My App", obj.toString());
                                } catch (Throwable t) {
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response_result + "\"");

                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();

                                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");
                                        }
                                    });
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();

                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }
            return null;
        }
    }

    public static void PMeeting_Data(Context contextn, String check_flags, String action_items, String assign_tos, String statuss, String descriptions, String pdf_paths, String mp3_paths, String ids, String m_types, String s_ids) {
        context = contextn;
        check_flag = check_flags;
        action_item = action_items;
        assign_to = assign_tos;
        status = statuss;
        description = descriptions;
        pdf_path = pdf_paths;
        mp3_path = mp3_paths;
        id = ids;
        m_type = m_types;
        s_id = s_ids;

        //detailsLists = list;
        dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog.setMessage(context.getResources().getString(R.string.dialog_wait_message));
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        new PMeeting_DataAyncTask().execute();

    }


    public static class PMeeting_DataAyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Void... params) {


            String uploadImage = "";
            dbvoc = new DataBaseHelper(context);

            Calendar c = Calendar.getInstance();
            System.out.println("Current time =&gt; " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            final String formattedDate = df.format(c.getTime());
            JSONObject jsonBody = new JSONObject();

            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL", null);
            String shopcode = spf.getString("shopcode", null);
            String Cust_domain = spf.getString("Cust_Service_Url", null);
            String domain = Cust_domain + "metal/api/v1/";


            try {

                JSONArray customer = new JSONArray();
                JSONArray product = new JSONArray();
                JSONArray order = new JSONArray();
                JSONObject product_valuenew = new JSONObject();

                String charset = "UTF-8";
                try {

                    MultipartUtility multipart;


                    if (m_type.equalsIgnoreCase("executive")) {

                        multipart = new MultipartUtility(domain + "promotional_meetings/add_executive_meeting", charset);
                        if (!id.equalsIgnoreCase("")) {
                            multipart.addFormField("meeting_id", id);
                        }

                        if (!s_id.equalsIgnoreCase("")) {
                            multipart.addFormField("id", s_id);
                        }

                        multipart.addFormField("checked", check_flag);
                        multipart.addFormField("name", action_item);
                        multipart.addFormField("status", status);
                        multipart.addFormField("description", description);


                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(pdf_path)) {
                            image_url1 = pdf_path;
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(mp3_path)) {
                            image_url2 = mp3_path;

                        }


                        multipart.addFormField("assigned_to", assign_to);
                        multipart.addFormField("latitude", Global_Data.GLOvel_LATITUDE);
                        multipart.addFormField("longitude", Global_Data.GLOvel_LONGITUDE);
                        multipart.addFormField("email", user_email.trim());
                        multipart.addFormField("dummy", "dummy");

                    } else {

                        multipart = new MultipartUtility(domain + "promotional_meetings/add_standard_meeting", charset);
                        if (!id.equalsIgnoreCase("")) {
                            multipart.addFormField("meeting_id", id);
                        }

                        if (!s_id.equalsIgnoreCase("")) {
                            multipart.addFormField("id", s_id);
                        }


                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(pdf_path)) {
                            image_url1 = pdf_path;
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(mp3_path)) {
                            image_url2 = mp3_path;

                        }


                        if (!(Global_Data.Order_hashmap.isEmpty())) {
                            try {
                                for (Object name : Global_Data.Order_hashmap.keySet()) {
                                    JSONObject item = new JSONObject();
                                    Object key = name.toString();
                                    Object value = Global_Data.Order_hashmap.get(key);

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(value.toString()) && key.toString().contains("YES")) {
                                        item.put("name", value.toString());
                                        product.put(item);
                                    } else {
                                        if (!key.toString().contains("YES")) {
                                            item.put("name", value.toString());
                                            item.put("description_id", key);
                                            product.put(item);
                                        }

                                    }


                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        multipart.addFormField("promotional_descriptions", product.toString());
                        multipart.addFormField("checked", check_flag);
                        multipart.addFormField("latitude", Global_Data.GLOvel_LATITUDE);
                        multipart.addFormField("longitude", Global_Data.GLOvel_LONGITUDE);
                        multipart.addFormField("email", user_email.trim());
                        multipart.addFormField("dummy", "dummyparameter");

                    }

                    Log.i("data", "data: " + product.toString());
                    Log.i("volley", "domain: " + domain);


                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(image_url1)) {
                        Uri uri1 = Uri.parse(image_url1);
                        final File file1 = new File(uri1.getPath());

                        try {
                            if (file1.exists()) {
                                multipart.addFilePart("file_pdf", file1);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(image_url2)) {
                        Uri uri2 = Uri.parse(image_url2);
                        final File file2 = new File(uri2.getPath());

                        try {
                            if (file2.exists()) {
                                multipart.addFilePart("file_audio", file2);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }


                    List<String> response1 = multipart.finish();

                    Log.v("rht", "SERVER REPLIED:");

                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
                        }
                    });

                    for (String line : response1) {
                        Log.v("rht", "Line : " + line);
                        response_result = line;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {

                                try {

                                    JSONObject obj = null;
                                    try {
                                        obj = new JSONObject(response_result);
                                    } catch (JSONException err) {
                                        Log.d("Error", err.toString());
                                    }

                                    dialog.dismiss();
                                    //Successcul message issue on submit. Message should be "Promotional activity submitted successfully"
                                    if (!obj.getString("result").equalsIgnoreCase("Device Not Found") && !obj.getString("result").equalsIgnoreCase("Invalid parameters")) {
//                                        Toast toast = Toast.makeText(context, obj.getString("result"),
//                                                Toast.LENGTH_SHORT);

                                        String gaddress = "";
                                        try {
                                            if (Global_Data.address.equalsIgnoreCase("null")) {
                                                gaddress = "";
                                            } else {
                                                gaddress = Global_Data.address;
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                public void run() {


                                                    dialog.dismiss();
                                                }
                                            });
                                        }

                                        String val = "";
                                        JSONObject finalObj = obj;
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            public void run() {

                                                dialog.dismiss();

                                                try {
                                                    Global_Data.Custom_Toast(context, finalObj.getString("result"), "Yes");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                Global_Data.Order_hashmap.clear();
                                                try {
                                                    File fdelete = new File(mp3_path);
                                                    if (fdelete.exists()) {
                                                        if (fdelete.delete()) {
                                                            System.out.println("file Deleted :" + mp3_path);
                                                        } else {
                                                            System.out.println("file not Deleted :" + mp3_path);
                                                        }
                                                    }
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }


                                                image_url1 = "";
                                                image_url2 = "";
                                                image_url3 = "";
                                                image_url4 = "";
                                                image_url5 = "";
                                                Intent i = new Intent(context, ActivityPlanner.class);
                                                context.startActivity(i);
                                                ((Activity) context).finish();
                                            }
                                        });


                                    } else {
//                                        Toast toast = Toast.makeText(context, obj.getString("result"),
//                                                Toast.LENGTH_SHORT);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();
                                        Global_Data.Custom_Toast(context, obj.getString("result"), "yes");

                                    }

                                    Log.d("My App", obj.toString());

                                } catch (Throwable t) {
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response_result + "\"");

                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        public void run() {

                                            dialog.dismiss();


                                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");


                                        }
                                    });
                                }


                            }
                        });


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();

                            Global_Data.Custom_Toast(context, "Something went wrong,Please try again.", "yes");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        try {
                            if ((dialog != null) && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        } catch (final IllegalArgumentException e) {
                            // Handle or log or ignore
                        } catch (final Exception e) {
                            // Handle or log or ignore
                        } finally {
                            dialog = null;
                        }
                    }
                });

            }
            return null;
        }
    }

    public static class MarketingSurveyAyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // Since you are in a UI thread here.
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
                //dialog.dismiss();
            }

            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage(context.getResources().getString(R.string.Please_Wait));
            dialog.setTitle(context.getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // After completing execution of given task, control will return here.
            // Hence if you want to populate UI elements with fetched data, do it here.
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // You can track you progress update here
        }

        @Override
        protected Void doInBackground(Void... params) {

            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL", null);

            try {
                JSONArray timesheet_arr = new JSONArray();
                JSONObject timesheet_valuenew = new JSONObject();

                hashkey.clear();
                hashvalue.clear();
                Iterator myVeryOwnIterator = Global_Data.quastionFeedback.keySet().iterator();
                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    String value = (String) Global_Data.quastionFeedback.get(key);
                    Log.d("map key", "Map key" + key);
                    Log.d("map value", "Map value" + value);
                    hashkey.add(key);
                    hashvalue.add(value);
                }
                if (hashvalue.contains("")) {
                    for (int i = 0; i < hashkey.size(); i++) {
                        if (hashvalue.get(i).equalsIgnoreCase("")) {
                            String code_value[] = hashkey.get(i).split(":");


                            Global_Data.Custom_Toast(context, context.getResources().getString(R.string.survey_quastion_validation_message) + " " + code_value[1], "yes");
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < hashkey.size(); i++) {
                        String code_value[] = hashkey.get(i).split(":");
                        String que_id = code_value[0];
                        String answer = hashvalue.get(i);

                        JSONObject timesheet_value = new JSONObject();
                        timesheet_value.put("question_id", que_id);
                        timesheet_value.put("answer", answer);
                        timesheet_arr.put(timesheet_value);
                    }
                }

                if (Final_Flag_ORDER_N.equalsIgnoreCase("sdf")) {

                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            Global_Data.Custom_Toast(context, context.getResources().getString(R.string.No_record_found_for_sync), "");
                            dialog.dismiss();
                        }
                    });

                } else {

                    timesheet_valuenew.put("answers", timesheet_arr);
                    // timesheet_valuenew.put("multi_answers", Global_Data.multicheck_arr);
                    timesheet_valuenew.put("email", user_email);
                    timesheet_valuenew.put("cust_code", Global_Data.cust_str);

                    Log.d("question_answer", timesheet_valuenew.toString());

                    SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
                    String Cust_domain = sp.getString("Cust_Service_Url", "");
                    String service_url = Cust_domain + "metal/api/v1/";

                    String domain = service_url;
                    Log.i("volley", "service_url: " + domain+"question_answer/answers_of_question_modules"+ timesheet_valuenew.toString());


                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "question_answer/answers_of_question_modules", timesheet_valuenew, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("volley", "response: " + response);

                            String response_result = "";
//                        if(response.has("result"))
//                        {
                            try {
                                response_result = response.getString("result");
                                dialog.dismiss();
//                                if(response_result.equalsIgnoreCase("Device not found."))
//                                {
//                                    Toast toast = Toast.makeText(context, "Device Not Found", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//                                    dialog.dismiss();
//                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();
                                    }
                                });
                            }
//                        }
//                        else
//                        {
//                            response_result = "data";
//                        }
                            if (response_result.equalsIgnoreCase("Device not found.")) {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        Global_Data.Custom_Toast(context, "Device Not Found", "yes");
                                        dialog.dismiss();
                                    }
                                });

                            } else if (response_result.equalsIgnoreCase("Answers saved successfully.")) {
                                final String finalResponse_result = response_result;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {
                                        //dbvoc.getDeleteTable("timesheet");
                                        //Global_Data.ShowTimesheetArr.clear();
                                        //Global_Data.date_arr.clear();
                                        Global_Data.cust_str = "";
                                        Global_Data.quastionFeedback.clear();


                                        Global_Data.Custom_Toast(context, "Answers Saved Successfully.", "yes");
                                        //Intent intentn = new Intent(context, Marketing.class);
                                        Intent intentn = new Intent(context, MainActivity.class);
                                        context.startActivity(intentn);
                                        ((Activity) context).finish();
                                    }
                                });
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();
                                    }
                                });
                            } else {
                                final String finalResponse_result1 = response_result;
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();

                                        Global_Data.Custom_Toast(context, finalResponse_result1, "yes");
                                    }
                                });
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("volley", "error: " + error);
                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {

                                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Server_Error), "");
                                    dialog.dismiss();
                                }
                            });
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    // queue.add(jsObjRequest);
                    int socketTimeout = 30000;//30 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    jsObjRequest.setRetryPolicy(policy);
                    requestQueue.add(jsObjRequest);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {

                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Server_Error), "");
                        dialog.dismiss();
                    }
                });
            }
            return null;
        }
    }


}
