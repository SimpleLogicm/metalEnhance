package com.msimplelogic.adapter;

/**
 * Created by sujit on 12/27/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.activities.AppLocationManager;
import com.msimplelogic.activities.BasicMapDemoActivity;
import com.msimplelogic.activities.Cash_Submit;
import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.Customer_Outstanding;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.Local_Data;
import com.msimplelogic.activities.LoginDataBaseAdapter;
import com.msimplelogic.activities.MapMultiUser;
import com.msimplelogic.activities.MyService;
import com.msimplelogic.activities.Nearest_Customer;
import com.msimplelogic.activities.PlayService_Location;
import com.msimplelogic.activities.Previous_Order;
import com.msimplelogic.activities.Previous_returnOrder;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.Schedule_List;
import com.msimplelogic.services.StartLocationAlert;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import cpm.simplelogic.helper.GPSTracker;

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    String gps_redirection_flag = "";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    GPSTracker gps;
    static String final_response = "";
    String response_result = "";
    Boolean isInternetPresent = false;
    public TextView ItemName;
    public ImageView ItemImage;
    ConnectionDetector cd;
    ProgressDialog dialog;
    DataBaseHelper dbvoc = new DataBaseHelper(itemView.getContext());
    double lat, lon;
    LoginDataBaseAdapter loginDataBaseAdapter;
    Context context;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        context = itemView.getContext();
        cd = new ConnectionDetector(itemView.getContext());
        loginDataBaseAdapter = new LoginDataBaseAdapter(itemView.getContext());
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        dialog = new ProgressDialog(itemView.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        ItemName = (TextView) itemView.findViewById(R.id.item_name);
        ItemImage = (ImageView) itemView.findViewById(R.id.item_image);
    }

    @Override
    public void onClick(final View view) {

        if (getPosition() == 0) {

            gps = new GPSTracker(context);
            if (!gps.canGetLocation()) {

                gps_redirection_flag = "yes";
                Activity mContext = (Activity) context; //change this your activity name
                StartLocationAlert startLocationAlert = new StartLocationAlert(mContext);
                //startLocationAlert.

            }else{
                Intent map = new Intent(view.getContext(), BasicMapDemoActivity.class);
                view.getContext().startActivity(map);
            }

//            Intent map = new Intent(view.getContext(), BasicMapDemoActivity.class);
//            view.getContext().startActivity(map);
            //finish();
            //view.getContext().startActivity(new Intent(view.getContext(), Nearest_Customer.class));
            // Toast.makeText(view.getContext(), "near loc", Toast.LENGTH_SHORT).show();
        } else if (getPosition() == 1) {
            requestGPSPermissionsignane();

        } else if (getPosition() == 2) {

            gps = new GPSTracker(context);
            if (!gps.canGetLocation()) {

                gps_redirection_flag = "yes";
                Activity mContext = (Activity) context; //change this your activity name
                StartLocationAlert startLocationAlert = new StartLocationAlert(mContext);
                //startLocationAlert.

            }else{
                requestGPSPermissionsigna();
            }

            //finish();
            //view.getContext().startActivity(new Intent(view.getContext(), Order.class));
        } else if (getPosition() == 3) {

            requestGPSPermissionsignamap();

        } else if (getPosition() == 4) {


            isInternetPresent = cd.isConnectingToInternet();

            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create(); //Read Update
            alertDialog.setTitle(itemView.getContext().getResources().getString(R.string.Scheduleo));
            alertDialog.setMessage(itemView.getContext().getResources().getString(R.string.schedule_offline_message));
            alertDialog.setButton(Dialog.BUTTON_POSITIVE, itemView.getContext().getResources().getString(R.string.Online), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog1, int which) {
                    dialog1.cancel();

                    if (isInternetPresent) {
                        //getScheduleDataFORALLCUSTOMERS();

                        //calendarn = Calendar.getInstance();
                        //year = calendarn.get(Calendar.YEAR);
                        loginDataBaseAdapter = new LoginDataBaseAdapter(itemView.getContext());
                        loginDataBaseAdapter = loginDataBaseAdapter.open();
                        dialog = new ProgressDialog(itemView.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        dialog.setMessage(itemView.getContext().getResources().getString(R.string.Please_Wait));
                        dialog.setTitle(itemView.getContext().getResources().getString(R.string.app_name));
                        dialog.setCancelable(false);
                        dialog.show();

                        SharedPreferences sp = view.getContext().getSharedPreferences("SimpleLogic", 0);
                        String Cust_domain = sp.getString("Cust_Service_Url", "");
                        String service_url = Cust_domain + "metal/api/v1/";
                        String device_id = sp.getString("devid", "");
                        String domain = service_url;

                        SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
                        String user_email = spf.getString("USER_EMAIL",null);


                        Log.i("volley", "domain: " + domain);
                        Log.i("volley", "email: " + user_email);
                        Log.i("target url", "target url " + domain + "delivery_schedules/send_all_schedules?email=" + user_email);

                        StringRequest jsObjRequest = null;

//					 jsObjRequest = new StringRequest(domain+"delivery_schedules/send_all_schedules?imei_no="+"358187071078870"+"&email="+"vinod.raghuwanshi@simplelogic.in", new Response.Listener<String>() {


                        jsObjRequest = new StringRequest(domain + "delivery_schedules/send_all_schedules?email=" + user_email, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.i("volley", "response: " + response);
                                final_response = response;

                                new scheduleoperation().execute(response);

                            }
                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        dialog.dismiss();
                                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                                            Global_Data.Custom_Toast(itemView.getContext(),
                                                    "Network Error","");
                                        } else if (error instanceof AuthFailureError) {

                                            Global_Data.Custom_Toast(itemView.getContext(),
                                                    "Server AuthFailureError  Error","");
                                        } else if (error instanceof ServerError) {

                                            Global_Data.Custom_Toast(itemView.getContext(),
                                                    itemView.getContext().getResources().getString(R.string.Server_Errors),"");
                                        } else if (error instanceof NetworkError) {

                                            Global_Data.Custom_Toast(itemView.getContext(),
                                                    "Network   Error","");
                                        } else if (error instanceof ParseError) {

                                            Global_Data.Custom_Toast(itemView.getContext(),
                                                    "ParseError   Error","");
                                        } else {
                                            Global_Data.Custom_Toast(itemView.getContext(), error.getMessage(), "");
                                        }
                                        dialog.dismiss();
                                        // finish();
                                    }
                                });

                        RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
                        int socketTimeout = 300000;//30 seconds - change to what you want
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        jsObjRequest.setRetryPolicy(policy);
                        // requestQueue.se
                        //requestQueue.add(jsObjRequest);
                        jsObjRequest.setShouldCache(false);
                        requestQueue.getCache().clear();
                        requestQueue.add(jsObjRequest);

                    } else {


                        Global_Data.Custom_Toast(itemView.getContext(), itemView.getContext().getResources().getString(R.string.internet_connection_error),"yes");
                    }
                }
            });

            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, itemView.getContext().getResources().getString(R.string.Offline), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog1, int which) {

                    List<Local_Data> contacts3 = dbvoc.getSchedule_ListAll();

                    if (contacts3.size() <= 0) {

                        Global_Data.Custom_Toast(itemView.getContext(), itemView.getContext().getResources().getString(R.string.Sorry_No_Record_Found),"yes");

                    } else {
                        Global_Data.Schedule_FLAG = "ALLCUSTOMER";
                        Intent intent = new Intent(itemView.getContext(),
                                Schedule_List.class);
                        itemView.getContext().startActivity(intent);
                        //itemView.getContext().finish();
                        //itemView.getContext().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                    dialog1.cancel();

                }
            });
            alertDialog.show();
        } else if (getPosition() == 5) {
            Intent out = new Intent(view.getContext(), Customer_Outstanding.class);
            view.getContext().startActivity(out);
            //view.getContext().startActivity(new Intent(view.getContext(), Order.class));
        } else if (getPosition() == 6) {
            List<Local_Data> contcustomer = dbvoc.getOrderAll("Secondary Sales / Retail Sales");

            if (contcustomer.size() <= 0) {

                Global_Data.Custom_Toast(view.getContext(), itemView.getContext().getResources().getString(R.string.NO_order_found),"yes");
            } else {
                Intent a = new Intent(view.getContext(), Previous_Order.class);
                view.getContext().startActivity(a);
                //finish();
            }
        } else if (getPosition() == 7) {
            List<Local_Data> contcust = dbvoc.getOrderAllReturn("Secondary Sales / Retail Sales");

            if (contcust.size() <= 0) {
                //Toast.makeText(getApplicationContext(), "NO Returnorder found", Toast.LENGTH_LONG).show();


                Global_Data.Custom_Toast(view.getContext(), itemView.getContext().getResources().getString(R.string.NO_Return_order_found),"yes");
            } else {
                Intent b = new Intent(view.getContext(), Previous_returnOrder.class);
                view.getContext().startActivity(b);

            }
        } else if (getPosition() == 8) {
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                Intent csub = new Intent(view.getContext(), Cash_Submit.class);
                view.getContext().startActivity(csub);
            } else {



                Global_Data.Custom_Toast(view.getContext(), view.getContext().getResources().getString(R.string.internet_connection_error),"yes");
            }
        }
    }

    private class scheduleoperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {

            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("result")) {
                    response_result = response.getString("result");
                } else {
                    response_result = "data";
                }

                if (response_result.equalsIgnoreCase("Schedule doesn't exist")) {


                    //Toast.makeText(Order.this, response_result, Toast.LENGTH_LONG).show();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            // do something
                            dialog.dismiss();
                            Global_Data.Custom_Toast(itemView.getContext(), response_result,"yes");
                        }
                    });



                } else {

                    //dbvoc.getDeleteTable("delivery_products");
                    //dbvoc.getDeleteTable("delivery_schedules");
                    //dbvoc.getDeleteTable("credit_profile");
                    dbvoc.getDeletedelivery_schedulesAll();
                    dbvoc.getDeletedelivery_productsAll();
                    dbvoc.getDeletecredit_limitsAll();

                    JSONArray delivery_products = response.getJSONArray("delivery_products");
                    JSONArray delivery_schedules = response.getJSONArray("delivery_schedules");
                    JSONArray credit_profile = response.getJSONArray("credit_profiles");

                    Log.i("volley", "response reg delivery_products Length: " + delivery_products.length());
                    Log.i("volley", "response reg delivery_schedules Length: " + delivery_schedules.length());
                    Log.i("volley", "response reg credit_profile Length: " + credit_profile.length());

                    Log.d("volley", "delivery_products" + delivery_products.toString());
                    Log.d("volley", "delivery_schedules" + delivery_schedules.toString());
                    Log.d("volley", "credit_profile" + credit_profile.toString());

                    if (delivery_schedules.length() <= 0) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                // do something
                                dialog.dismiss();

                                Global_Data.Custom_Toast(itemView.getContext(), itemView.getContext().getResources().getString(R.string.Delivery_Schedule_Not_Found),"yes");
                            }
                        });

                    } else {

                        Global_Data.GLOvel_CUSTOMER_ID = "";
                        for (int i = 0; i < delivery_products.length(); i++) {

                            JSONObject jsonObject = delivery_products.getJSONObject(i);

                            loginDataBaseAdapter.insertDeliveryProducts("", jsonObject.getString("customer_code"), jsonObject.getString("order_number"), "", "", "", "", "", jsonObject.getString("order_quantity"), jsonObject.getString("delivered_quantity"), jsonObject.getString("truck_number"), jsonObject.getString("transporter_details"), "", "", jsonObject.getString("product_name") + "" + "" + "");


                        }

                        for (int i = 0; i < delivery_schedules.length(); i++) {

                            JSONObject jsonObject = delivery_schedules.getJSONObject(i);

                            loginDataBaseAdapter.insertDeliverySchedule("", jsonObject.getString("customer_code"), jsonObject.getString("customer_code"), jsonObject.getString("order_number"), "", jsonObject.getString("user_email"), jsonObject.getString("dispatch_date"), jsonObject.getString("delivery_date"), jsonObject.getString("order_amount"), jsonObject.getString("accepted_payment_mode"), "", jsonObject.getString("collected_amount"), jsonObject.getString("outstanding_amount"), "", "", jsonObject.getString("customer_address"));


                        }


                        for (int i = 0; i < credit_profile.length(); i++) {

                            JSONObject jsonObject = credit_profile.getJSONObject(i);

                            loginDataBaseAdapter.insert_credit_profile("", jsonObject.getString("customer_code"), jsonObject.getString("customer_code"), "", "", "", "", jsonObject.getString("credit_limit"), jsonObject.getString("amount_outstanding"), jsonObject.getString("amount_overdue"));

                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                // do something
                                Global_Data.Schedule_FLAG = "ALLCUSTOMER";
                                Intent intent = new Intent(itemView.getContext(), Schedule_List.class);
                                itemView.getContext().startActivity(intent);
//                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                                finish();
                                dialog.dismiss();
                            }
                        });

//                        MainActivity.this.runOnUiThread(new Runnable() {
//                            public void run() {
//
//                                Global_Data.Schedule_FLAG = "ALLCUSTOMER";
//                                Intent intent = new Intent(itemView.getContext(), Schedule_List.class);
//                                itemView.getContext().startActivity(intent);
////                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
////                                finish();
//                                dialog.dismiss();
//                            }
//                        });
                    }

//                    MainActivity.this.runOnUiThread(new Runnable() {
//                        public void run() {
//
//                            dialog.dismiss();
//                        }
//                    });
                    //dialog.dismiss();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            // do something
                            dialog.dismiss();
                        }
                    });
                    //finish();

                }


                // }

                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        // do something
                        dialog.dismiss();
                    }
                });

//                MainActivity.this.runOnUiThread(new Runnable() {
//                    public void run() {
//
//                        dialog.dismiss();
//                    }
//                });

            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    // do something
                    dialog.dismiss();
                }
            });

//            MainActivity.this.runOnUiThread(new Runnable() {
//                public void run() {
//
//                    dialog.dismiss();
//                }
//            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    // do something
                    dialog.dismiss();
                }
            });

//            MainActivity.this.runOnUiThread(new Runnable() {
//                public void run() {
//                    dialog.dismiss();
//                }
//            });


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void requestGPSPermissionsigna() {
        Dexter.withActivity((Activity) context)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            Intent map = new Intent(context, BasicMapDemoActivity.class);
                            context.startActivity(map);
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

                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                    }
                })
                .onSameThread()
                .check();
    }

    private void requestGPSPermissionsignane() {
        Dexter.withActivity((Activity) context)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            isInternetPresent = cd.isConnectingToInternet();
                            if (isInternetPresent) {


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
                                // device_id = telephonyManager.getDeviceId();
                                SharedPreferences pref_devid = context.getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
                                final String Device_id = pref_devid.getString("devid", "");
                                Log.d("device_id ", "device_id" + Device_id);
                                Log.d("Address", "Address" + Global_Data.address);
                                Log.d("Latitude", "Latitude" + Global_Data.GLOvel_LATITUDE);
                                Log.d("Longitude", "Longitude" + Global_Data.GLOvel_LONGITUDE);

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

                                    if (Global_Data.GLOvel_LATITUDE.equalsIgnoreCase("") || Global_Data.GLOvel_LATITUDE == null || Global_Data.GLOvel_LONGITUDE.equalsIgnoreCase("") || Global_Data.GLOvel_LONGITUDE == null || Global_Data.GLOvel_LATITUDE.equalsIgnoreCase("null") || Global_Data.GLOvel_LONGITUDE.equalsIgnoreCase("null")) {


                                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.latlong_not_found), "yes");
                                    } else {
                                        //Toast.makeText(getApplicationContext(), "not blank ", Toast.LENGTH_LONG).show();

                                        //getServices.View_NearestCustomer(MainActivity.this,Global_Data.GLOvel_ADDRESS,Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE);
                                        Intent a = new Intent(context, Nearest_Customer.class);
                                        context.startActivity(a);
                                        //view.getContext().finish();
                                    }
                                } catch (Exception ex) {

                                    Global_Data.Custom_Toast(context, context.getResources().getString(R.string.latlong_not_found),"yes");
                                    context.startService(new Intent(context, MyService.class));
                                    ex.printStackTrace();
                                }
                            } else {

                                Global_Data.Custom_Toast(context, context.getResources().getString(R.string.internet_connection_error),"yes");
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

                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                    }
                })
                .onSameThread()
                .check();
    }


    private void requestGPSPermissionsignamap() {
        Dexter.withActivity((Activity) context)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            isInternetPresent = cd.isConnectingToInternet();
                            if (isInternetPresent) {
                                dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                dialog.setMessage(context.getResources().getString(R.string.Please_Wait));
                                dialog.setTitle(context.getResources().getString(R.string.app_name));
                                dialog.setCancelable(false);
                                dialog.show();
                                List<Local_Data> contacts2 = dbvoc.get_BeatsData();
                                for (Local_Data cn : contacts2) {
                                    //results_beat.add(cn.getNamemap());
                                    String beat_address = "";

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getBeatName())) {
                                        beat_address = cn.getBeatName();

                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getCityName())) {
                                        beat_address += ", " + cn.getCityName();
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getStateName())) {
                                        beat_address += ", " + cn.getStateName();
                                    }

                                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                    List<Address> addresses = null;
                                    try {
                                        addresses = geocoder.getFromLocationName(beat_address, 1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //Address address = addresses.get(0);
                                    //if (addresses != null) {

                                    if (addresses.size() > 0) {
                                        lat = addresses.get(0).getLatitude();
                                        lon = addresses.get(0).getLongitude();
                                        //LatLng latLng = new LatLng(latitude, longitude);
//            mMap.addMarker(new MarkerOptions().position(latLng));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                                    }
                                    //}
                                    cn.setTitle(beat_address);
                                    //cn.setSnippet("Hello" +","+ cn.getNamemap());
                                    cn.setLatitude(String.valueOf(lat));
                                    cn.setLongitude(String.valueOf(lon));
                                    //arrayList.add(bean);
                                    Global_Data.list_mapdata.add(cn);
                                }
                                Intent con = new Intent(context, MapMultiUser.class);
                                context.startActivity(con);
                                // dialog.dismiss();
                            } else {

                                Global_Data.Custom_Toast(context, context.getResources().getString(R.string.internet_connection_error),"yes");
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


                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Error_occurredd) + error.toString(),"");
                    }
                })
                .onSameThread()
                .check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.need_permission_message));
        builder.setCancelable(false);
        builder.setMessage(context.getResources().getString(R.string.need_permission_setting_message));
        builder.setPositiveButton(context.getResources().getString(R.string.GOTO_SETTINGS), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        ((Activity) context).startActivityForResult(intent, 101);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        // All required changes were successfully made

                        if (gps_redirection_flag.equalsIgnoreCase("yes")) {
                            requestGPSPermissionsigna();
                        }

                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        //Toast.makeText(LoginActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }
}