package com.msimplelogic.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import android.util.Log;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

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
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.PlayService_Location;
import com.msimplelogic.activities.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cpm.simplelogic.helper.Check_Null_Value;
import cpm.simplelogic.helper.ConnectionDetector;

import static com.msimplelogic.activities.Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString;

public class BJobScheduleService extends Service implements LocationListener{
    private static final String TAG = "LocationActivity";
    Date start;
    Date end;
    String timenew;
    Date userDate;
    //LoginDataBaseAdapter loginDataBaseAdapter;
    ArrayList<String> results = new ArrayList<String>();
    String user_name = "";
    String sync_date = "";

    ArrayList<String> AT_results = new ArrayList<String>();
    int timeOfDay;

    //SharedPreferences spf=getSharedPreferences("SimpleLogic",0);
//	SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 10000 * 6;
    TextView btnFusedLocation,cancel_loc;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    private FirebaseAnalytics mFirebaseAnalytics;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    Geocoder geocoder;
    String line;
    //PreferencesHelper Prefs;
    SharedPreferences sp ;
    BufferedReader in = null;
    String lat_val,long_val;
    //    HttpPost http_post;
//    HttpResponse http_resp;
//    HttpClient http_client;
//    List<NameValuePair> http_nmvalpair;
//    HttpEntity http_entity;
    public LocationManager locationManager;
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;
    private String provider;
    // flag for GPS status
    boolean canGetLocation = false;
    Boolean isInternetPresent = false;

    ConnectionDetector cd;
    Location location; // location
    public double latitude; // latitude
    public double longitude; // longitude
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 0 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 35000 ; // 1 second
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Query the database and show alarm if it applies
        //	Prefs = new PreferencesHelper(this);
        sp= this.getSharedPreferences("SimpleLogic", 0);

        try {
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(String.valueOf(sp.getString("USER_EMAIL", "")))) {
                user_name = sp.getString("USER_EMAIL", "");
            } else {
                user_name = Global_Data.GLOvel_USER_EMAIL;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(String.valueOf(sp.getString("Sync_date_time", "")))) {
                sync_date = sp.getString("Sync_date_time", "");
            }
            else {
                sync_date = "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }



        cd = new ConnectionDetector(this);

        latitude = 0.0;
        longitude = 0.0;

        Calendar c = Calendar.getInstance();
        timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        DateFormat dateFormat2 = new SimpleDateFormat("HH:mm");
        final Date date2 = new Date();

        timenew = dateFormat2.format(date2).toString();
        try {
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
            userDate = parser.parse(timenew);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("time now", "Time " + timenew);





        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date date = new Date();
        System.out.println(dateFormat.format(date));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // If GPS enabled, get latitude/longitude using GPS Services
            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d("GPS Enabled", "GPS Enabled");

                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        //isLocationAvailable = true; // setting a flag that
                        // location is available

                        Global_Data.GLOvel_LATITUDE =  String.valueOf(latitude);
                        Global_Data.GLOvel_LONGITUDE =  String.valueOf(longitude);

                        //Global_Val.lat_val = Double.toString(c);
                        //Global_Val.long_val = Double.toString(d);
                        SharedPreferences spf=this.getSharedPreferences("SimpleLogic",0);
                        SharedPreferences.Editor editor=spf.edit();
                        //editor.putString("USER_EMAIL", Global_Data.GLOvel_USER_EMAIL);
                        editor.putString("LATVAL", String.valueOf(latitude));
                        editor.putString("LONGVAL", String.valueOf(longitude));

                        Log.d("GPS LOCATION","GPS LOCATION"+ latitude + longitude);


                        editor.commit();



                    }
                }
            }

            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            try
            {

                PlayService_Location PlayServiceManager = new PlayService_Location(this);
                if(PlayServiceManager.checkPlayServices(this) && (String.valueOf(latitude).equalsIgnoreCase("0.0") || String.valueOf(latitude).equalsIgnoreCase("") || String.valueOf(longitude).equalsIgnoreCase("") || String.valueOf(longitude).equalsIgnoreCase("0.0")))
                {
                    Log.d("Play LAT LOG","Play LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LATITUDE) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GLOvel_LONGITUDE)) {
                        latitude = Double.valueOf(Global_Data.GLOvel_LATITUDE);
                        longitude = Double.valueOf(Global_Data.GLOvel_LONGITUDE);
                    }



                }
                else
                {
                    if (isNetworkEnabled && !isGPSEnabled) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        //
                        // location = locationManager.getLastKnownLocation(provider);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            Global_Data.GLOvel_LATITUDE = String.valueOf(latitude);
                            Global_Data.GLOvel_LONGITUDE = String.valueOf(longitude);

                            //Global_Val.lat_val = Double.toString(c);
                            //Global_Val.long_val = Double.toString(d);
                            SharedPreferences spf = this.getSharedPreferences("SimpleLogic", 0);
                            SharedPreferences.Editor editor = spf.edit();
                            //editor.putString("USER_EMAIL", Global_Data.GLOvel_USER_EMAIL);
                            editor.putString("LATVAL", String.valueOf(latitude));
                            editor.putString("LONGVAL", String.valueOf(longitude));

                            Log.d("NETWORK LOCATION","NETWORK LOCATION"+ latitude + longitude);

                            editor.commit();

                        }
                    }
                }
            }catch(Exception es){es.printStackTrace();}






            if (String.valueOf(latitude).equalsIgnoreCase("0.0") || String.valueOf(latitude).equalsIgnoreCase("") || String.valueOf(longitude).equalsIgnoreCase("") || String.valueOf(longitude).equalsIgnoreCase("0.0")) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                //
                // location = locationManager.getLastKnownLocation(provider);

                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Global_Data.GLOvel_LATITUDE = String.valueOf(latitude);
                    Global_Data.GLOvel_LONGITUDE = String.valueOf(longitude);

                    //Global_Val.lat_val = Double.toString(c);
                    //Global_Val.long_val = Double.toString(d);
                    SharedPreferences spf = this.getSharedPreferences("SimpleLogic", 0);
                    SharedPreferences.Editor editor = spf.edit();
                    //editor.putString("USER_EMAIL", Global_Data.GLOvel_USER_EMAIL);
                    editor.putString("LATVAL", String.valueOf(latitude));
                    editor.putString("LONGVAL", String.valueOf(longitude));

                    Log.d("NETWORK LOCATION","NETWORK LOCATION"+ latitude + longitude);


                    editor.commit();


                }
            }




            //Toast.makeText(this,"vals:++++++++++++++++++++++++++++++++>"+latitude+" "+longitude, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();

        }

        Log.i("sendlocation","Every 5 minutes it will appear in Log Console"+latitude+" "+longitude);

        new Thread(new Runnable() {
            public void run() {
                try {
                    // Obtain the FirebaseAnalytics instance.
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(getBaseContext());
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, sp.getString("USER_EMAIL", ""));
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, sp.getString("USER_NAMEs", ""));

                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                    /* Firebase Code End */

                    SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
                    String Cust_domain = sp.getString("Cust_Service_Url", "");
                    String service_url = Cust_domain + "metal/api/v1/";
                    String domain = service_url;


                    SharedPreferences spf = getSharedPreferences("SimpleLogic", 0);
                    Global_Data.device_id = spf.getString("devid", "");

                    Log.i("volley", "domain: " + domain);
                    Log.i("volley", "Device_id: " + Global_Data.device_id);

                    isInternetPresent = cd.isConnectingToInternet();





                    try {


                        SharedPreferences spf2 = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
                        String StartTime = spf2.getString("StartTime", null);
                        String Endtime = spf2.getString("Endtime", null);

                        try {
                            if (StartTime.equalsIgnoreCase("")) {
                                String starttime = "07:00";
                                SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                                start = parser.parse(starttime);

                            } else {
                                SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                                start = parser.parse(StartTime);

                            }
                            if (Endtime.equalsIgnoreCase("")) {
                                String endtim = "22:00";
                                SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                                end = parser.parse(endtim);

                            } else {
                                SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                                end = parser.parse(Endtime);

                            }

                        }catch (Exception e) {
                            String endtim = "22:00";
                            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                            end = parser.parse(endtim);

                            String starttime = "07:00";
                            start = parser.parse(starttime);
                            e.printStackTrace();
                        }


                        if (isInternetPresent && userDate.after(start) && userDate.before(end) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck_b(String.valueOf(latitude)) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck_b(String.valueOf(longitude))) {


                            BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
                            int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                            //int level = bm.getIntProperty(BatteryManager.EXTRA_LEVEL);

                            JSONArray PICTURE = new JSONArray();
                            //JSONObject product_value = new JSONObject();
                            JSONObject product_value_n = new JSONObject();
                            JSONArray product_imei = new JSONArray();
                            JSONArray at_array = new JSONArray();


                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date date = new Date();
                            System.out.println(dateFormat.format(date));

                            JSONObject picture = new JSONObject();
                            picture.put("latitude", latitude);
                            picture.put("longitude", longitude);

                            if (isInternetPresent && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(String.valueOf(Global_Data.GLOvel_LATITUDE)) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(String.valueOf(Global_Data.GLOvel_LONGITUDE))) {
                                try {
//									new Handler(Looper.getMainLooper()).post(new Runnable() {
//										public void run() {
//											LocationAddress locationAddress = new LocationAddress();
//											LocationAddress.getAddressFromLocation(Double.valueOf(Global_Data.GLOvel_LATITUDE), Double.valueOf(Global_Data.GLOvel_LONGITUDE),
//													getApplicationContext(), new GeocoderHandler());
//										}
//									});

                                    picture.put("address", "");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    //addressn(Double.valueOf(Global_Data.GLOvel_LATITUDE),Double.valueOf(Global_Data.GLOvel_LONGITUDE));
                                    picture.put("address", "");
                                }
                            } else {
                                picture.put("address", "");
                            }

                            picture.put("location_date", dateFormat.format(date));
                            picture.put("battery_status", batLevel);
                            picture.put("synce_time", sync_date);
                            PICTURE.put(picture);


                            product_value_n.put("user_location_histories", PICTURE);
                            //product_value_n.put("attendances", at_array);
                          //  product_value_n.put("imei_no", Global_Data.device_id);
                            product_value_n.put("email", user_name);
                            Log.d("user_location_histories", product_value_n.toString());


                            Log.i("volley", "Service url: " + domain + "update_user_with_current_location");

                            Log.i("batLevel", "batLevel " + batLevel+"%");
                            Log.i("sync_date", "sync_date " + sync_date);

                            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "update_user_with_current_location", product_value_n, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    Log.i("volley", "response: " + response.toString());

                                    try {

                                        String response_result = "";
                                        if (response.has("result")) {
                                            response_result = response.getString("result");
                                            // dbvoc.getDeleteTable("geo_data");
                                        } else {
                                            response_result = "data";
                                        }


                                        if (response_result.equalsIgnoreCase("Device not found")) {

                                            Log.d("LOC RESULT", "LOC RESULT" + response_result);

                                        } else {

//
                                            Log.d("LOC RESULT", "LOC RESULT" + response_result);


//
                                        }

                                    } catch (JSONException e) {

                                        e.printStackTrace();

                                    }


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                                        Log.d("BACK Error", "Network Error");
                                    } else if (error instanceof AuthFailureError) {

                                        Log.d("BACK Error", "Server AuthFailureError");
                                    } else if (error instanceof ServerError) {
                                        Log.d("BACK Error", "Server   Error");
                                    } else if (error instanceof NetworkError) {
                                        Log.d("BACK Error", "Network Error");

                                    } else if (error instanceof ParseError) {
                                        Log.d("BACK Error", "ParseError   Error");
                                    } else {
                                        Log.d("BACK Error", error.getMessage());
                                    }

                                }
                            });

                            RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                            // queue.add(jsObjRequest);
                            jsObjRequest.setShouldCache(false);
                            int socketTimeout = 400000;//30 seconds - change to what you want
                            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            jsObjRequest.setRetryPolicy(policy);
                            requestQueue.add(jsObjRequest);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        //dbvoc.getDeleteBACKGROUND_SERVICE_CHECK();
                        //dialog.dismiss();
                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        return START_NOT_STICKY;
    }

    private class GeocoderHandler extends Handler {
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


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    @Override
//    public void onLocationChanged(Location arg0) {
//        // TODO Auto-generated method stub
//
//    }

    @Override
    public void onDestroy() {
        // I want to restart this service again in one hour
//        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
//        alarm.set(
//                AlarmManager.RTC_WAKEUP,
//                System.currentTimeMillis() + (1000 * 60 * 5),
//                PendingIntent.getService(this, 0, new Intent(this, JobScheduleMyService.class), 0)
//        );
    }

    @Override
    public void onLocationChanged(Location location) {

        //int lat = (int) (location.getLatitude());
        //int lng = (int) (location.getLongitude());

        Global_Data.GLOvel_LATITUDE =  String.valueOf(location.getLatitude());
        Global_Data.GLOvel_LONGITUDE =  String.valueOf(location.getLongitude());

        Log.d("Location change","CHANGE "+Global_Data.GLOvel_LATITUDE+" "+Global_Data.GLOvel_LONGITUDE);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

//    @Override
//    public void onConnected(Bundle bundle) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//
//    }

    public String addressn(Double lat, Double longi) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        StringBuilder sb = new StringBuilder();
        sb.append("");

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            sb.append(isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getAddressLine(0))+" ");
            if(!(sb.indexOf(addresses.get(0).getLocality()) > 0))
            {
                sb.append(isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getLocality())+" ");
            }

            if(!(sb.indexOf(addresses.get(0).getAdminArea()) > 0))
            {
                sb.append(isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getAdminArea())+" ");
            }

            if(!(sb.indexOf(addresses.get(0).getCountryName()) > 0))
            {
                sb.append(isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getCountryName())+" ");
            }

            if(!(sb.indexOf(addresses.get(0).getPostalCode()) > 0))
            {
                sb.append(isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getPostalCode())+" ");
            }
            //String knownName = addresses.get(0).getFeatureName();

            Global_Data.address = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

    }

    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.notifi)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }



}
