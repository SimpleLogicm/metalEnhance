package com.msimplelogic.activities;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.kotlinFiles.AttendanceLog;
import com.msimplelogic.activities.kotlinFiles.ShiftRoaster;
import com.msimplelogic.services.StartLocationAlert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cpm.simplelogic.helper.ConnectionDetector;
import cpm.simplelogic.helper.GPSTracker;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static com.msimplelogic.activities.Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString;

public class BasicMapDemoActivity extends BaseActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    String datenn;
    String in_out_flag = "";
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private Toolbar toolbar;
    LatLng latLng;
    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;
    Marker currLocationMarker;
    ProgressDialog dialog;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Double lat;
    Double longi;
    StringBuilder str;
    Button at_in, at_out, att_log, att_shftro;
    LoginDataBaseAdapter loginDataBaseAdapter;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    static String final_response = "";
    String response_result = "";
    ImageView Morelocationdetail;
    GPSTracker gps;
    String gps_redirection_flag = "";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        cd = new ConnectionDetector(getApplicationContext());
        dialog = new ProgressDialog(BasicMapDemoActivity.this);
        Morelocationdetail = (ImageView) findViewById(R.id.Morelocationdetail);
        at_in = findViewById(R.id.at_in);
        at_out = findViewById(R.id.at_out);
        att_log = findViewById(R.id.att_log);
        att_shftro = findViewById(R.id.att_shftro);

        gps_redirection_flag = "";

        str = new StringBuilder();
        str.append(" ");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a yyyy-MM-dd");
        DateFormat date_onlyn = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String daten = sdf.format(date);
        datenn = sdf.format(date);
        String date_only_ss = date_onlyn.format(date);
        Date new_current_daten = null;
        try {
            new_current_daten = date_onlyn.parse(date_only_ss);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millisecondn = new_current_daten.getTime();

//        if(contacts2.size() > 0) {
//            for (Local_Data cn : contacts2) {
//                if (cn.getServer_flag().equalsIgnoreCase("true")) {
//                    dbvoc.getDeleteattendance_datebyserver_flag("true");
//                }
//            }
//        }

        // List<Local_Data> newone = dbvoc.getAllAttendance_Data_bydate(date_only_s);

        List<Local_Data> a_checkn = dbvoc.getAllAttendanceF_Data_byDate(date_only_ss);
        if (a_checkn.size() <= 0) {

            dbvoc.getDeleteTable("attendence_f");
            loginDataBaseAdapter.insertattendence_flag("false", date_only_ss);
        }

        if (!(DateUtils.isToday(millisecondn))) {
            dbvoc.getDeleteTable("attendence_f");
            loginDataBaseAdapter.insertattendence_flag("false", date_only_ss);

        }

        mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);

        mFragment.getMapAsync(this);


        Morelocationdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, BasicMapDemoActivity.this);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                Intent i = new Intent(BasicMapDemoActivity.this, Geo_Data.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(i);
                finish();
            }
        });

        att_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(BasicMapDemoActivity.this, AttendanceLog.class);
                startActivity(i);
                finish();
            }
        });

        att_shftro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i = new Intent(BasicMapDemoActivity.this, ShiftRoaster.class);
//                startActivity(i);
//                finish();
            }
        });

        at_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gps = new GPSTracker(BasicMapDemoActivity.this);
                if (!gps.canGetLocation()) {

                    gps_redirection_flag = "in";
                    Activity mContext = BasicMapDemoActivity.this; //change this your activity name
                    StartLocationAlert startLocationAlert = new StartLocationAlert(mContext);
                    //startLocationAlert.

                }
                else
                {
                    AttinCall();
                }




            }
        });

        at_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gps = new GPSTracker(BasicMapDemoActivity.this);
                if (!gps.canGetLocation()) {

                     gps_redirection_flag = "out";
                    Activity mContext = BasicMapDemoActivity.this; //change this your activity name
                    StartLocationAlert startLocationAlert = new StartLocationAlert(mContext);
                    //startLocationAlert.

                }
                else
                {
                    attoutCall();
                }




            }
        });
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        try {
            mGoogleMap = gMap;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);

            buildGoogleApiClient();

            mGoogleApiClient.connect();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        // Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        try {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setTrafficEnabled(true);
            mGoogleMap.setIndoorEnabled(true);
            mGoogleMap.setBuildingsEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {

            // Toast.makeText(this,"onConnected",Toast.LENGTH_SHORT).show();
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                //place marker at current position
                //mGoogleMap.clear();
                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                markerOptions.title(getResources().getString(R.string.Current_Position));
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                currLocationMarker = mGoogleMap.addMarker(markerOptions);
            }

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000); //5 seconds
            mLocationRequest.setFastestInterval(3000); //3 seconds
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        //place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        try {

            lat = location.getLatitude();
            longi = location.getLongitude();

//            prefManager.setLATITUDE(String.valueOf(lat));
//            prefManager.setLONGITUDE(String.valueOf(longi));

            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                try {
                    //  LocationAddress locationAddress = new LocationAddress();
//                    locationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(),
//                            BasicMapDemoActivity.this, new BasicMapDemoActivity.GeocoderHandler());
                    Geocoder geo = new Geocoder(BasicMapDemoActivity.this.getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses.isEmpty()) {
                        //yourtextfieldname.setText("Waiting for Location");
                        markerOptions.title(getResources().getString(R.string.Waiting_for_Location));
                        // markerOptions.title("Current Position");
                    } else {
                        if (addresses.size() > 0) {
//                            Address returnAddress = addresses.get(0);
//                            String localityString = returnAddress.getLocality();
//                            String city = returnAddress.getCountryName();
//                            String region_code = returnAddress.getCountryCode();
//                            String zipcode = returnAddress.getPostalCode();
                            str = new StringBuilder();
                            str.append(isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getAddressLine(0)) + " ");
                            if (!(str.indexOf(addresses.get(0).getLocality()) > 0)) {
                                str.append(isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getLocality()) + " ");
                            }

                            if (!(str.indexOf(addresses.get(0).getAdminArea()) > 0)) {
                                str.append(isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getAdminArea()) + " ");
                            }

                            if (!(str.indexOf(addresses.get(0).getCountryName()) > 0)) {
                                str.append(isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getCountryName()) + " ");
                            }

                            if (!(str.indexOf(addresses.get(0).getPostalCode()) > 0)) {
                                str.append(isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(addresses.get(0).getPostalCode()) + " ");
                            }

                            str.append("\n");

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(str.toString())) {
                                Global_Data.address = str.toString();
                            }

                            markerOptions.title(str.toString());

                        } else {
                            if (Global_Data.address == null) {
                                getaddress();
                            }
                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }


            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mGoogleMap.addMarker(markerOptions);

            //Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

            Log.d("Location change event", "Location Change");

            //zoom to current position:
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(16).build();

            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

        } catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    private void getaddress() throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        Global_Data.address = address;
        Global_Data.address = str.toString();
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
//		return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add:
                String targetNew="";
                SharedPreferences sp = BasicMapDemoActivity.this.getSharedPreferences("SimpleLogic", 0);
                try {
                    int target = (int) Math.round(sp.getFloat("Target", 0));
                    int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                    Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
                    if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                        int age = (int) Math.round(age_float);
                        if (Global_Data.rsstr.length() > 0) {
                            targetNew="T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
                            //todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        } else {
                            targetNew="T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                    } else {
                        int age = (int) Math.round(age_float);
                        if (Global_Data.rsstr.length() > 0) {
                            targetNew="T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
                            // todaysTarget.setText();
                        } else {
                            targetNew="T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(BasicMapDemoActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();

    }

    @Override
    public void onResume() {
        mFragment.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        dialog.dismiss();
        mFragment.onPause();
        //mGoogleMap.clear();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            dialog.dismiss();
            mGoogleMap.clear();
            mFragment.onDestroy();
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        SupportMapFragment f = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        if (!BasicMapDemoActivity.this.isFinishing()) {
            if (f != null) {
                mFragment.onDestroy();
                mGoogleMap.clear();
                // LocationServices.FusedLocationApi.removeLocationUpdates()
            }
        }


    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mFragment.onLowMemory();
        mGoogleMap.clear();
    }

    public void showDialogn(String time, String address) {
        final Dialog dialognew = new Dialog(BasicMapDemoActivity.this);
        dialognew.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialognew.setCancelable(false);
        dialognew.setContentView(R.layout.attendance_dialog);

        TextView inh = (TextView) dialognew.findViewById(R.id.inh);
        TextView att_time = (TextView) dialognew.findViewById(R.id.att_time);
        TextView att_address = (TextView) dialognew.findViewById(R.id.att_address);
        Button atok = (Button) dialognew.findViewById(R.id.atok);


        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a yyyy-MM-dd");
        Date date = new Date();
        // String daten = sdf.format(date);
        String datenew = sdf.format(date);

        if (in_out_flag.equalsIgnoreCase("OUT")) {
            inh.setText(getResources().getString(R.string.Out_DETAILS));
        } else {
            inh.setText(getResources().getString(R.string.In_DETAILS));
        }


        inh.setPaintFlags(inh.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.address)) {
            String str = Global_Data.address.trim().replaceAll("\n", " ");
            att_address.setText(getResources().getString(R.string.Place) + str);
        } else {
            att_address.setText(getResources().getString(R.string.Place_Not_Found));
        }

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(datenew)) {
            try {
                String timenewarray[] = datenew.split("-");
                String month = getMonthForInt(Integer.parseInt(timenewarray[1]) - 1);
                att_time.setText(getResources().getString(R.string.Times) + timenewarray[0] + "-" + month + "-" + timenewarray[2]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            att_time.setText(getResources().getString(R.string.Time_Not_Found));
        }

        atok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialognew.dismiss();

            }
        });

        dialognew.show();

    }


    public void Attendance_data(String user_email, String daten, String lat, String longi, String IN, String addresss) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
        String Cust_domain = sp.getString("Cust_Service_Url", "");
        String service_url = Cust_domain + "metal/api/v1/";

        String domain = service_url;
//        String domain = prefManager.get_SERVER_DOMAIN();

//        Log.i("volley", "domain: " + domain);
//        Log.i("volley", "email: " + user_email);
//        Log.i("user list url", "order list url " + domain + "customers/send_customer_address?imei_no=" + prefManager.get_DEVICE_ID() +"&email="+user_email+"&conference_code="+prefManager.get_ORDER_TYPE_ID().trim());


        JSONArray order = new JSONArray();
        JSONObject product_valuenew = new JSONObject();

        JSONObject product_value = new JSONObject();


        try {
            //  product_value.put("order_number", Global_Data.Order_Id);

            // product_value.put("user_id", user_email);
            product_value.put("punched_on", daten);
            product_value.put("punched_at_latitude", lat);
            product_value.put("punched_at_longitude", longi);
            product_value.put("punched_button", IN);
//            product_value.put("conference_code", conference_id);
//            product_value.put("business_vertical_code", vertical_id);
            product_value.put("punched_at_address", addresss);

            order.put(product_value);

            //  product_valuenew.put("order_number", Global_Data.Order_Id);
            product_valuenew.put("attendance", product_value);


            Log.i("attendances value", product_valuenew.toString());

            //String service_domain = domain + "attendances?imei_no=" + Global_Data.device_id + "&email=" + user_email;
            String service_domain = domain + "attendances?&email=" + user_email;

            Log.i("volley", "domain: " + service_domain);


            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, service_domain, product_valuenew, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);
                    final_response = response.toString();

                    new BasicMapDemoActivity.getINAttendanceresponse().execute(final_response);

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();


//                        Intent intent = new Intent(Attendance_Map.this, Attendance_Map.class);
//                        startActivity(intent);
//                        finish();
                            //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Network Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "Network Error","");
                            } else if (error instanceof AuthFailureError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Server AuthFailureError  Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "Server AuthFailureError  Error","");
                            } else if (error instanceof ServerError) {
//                                Toast.makeText(getApplicationContext(),
//                                        getResources().getString(R.string.Server_Errors),
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        getResources().getString(R.string.Server_Errors),"");
                            } else if (error instanceof NetworkError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Network   Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "Network   Error","");
                            } else if (error instanceof ParseError) {
//                                Toast.makeText(getApplicationContext(),
//                                        "ParseError   Error",
//                                        Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(),
                                        "ParseError   Error","");
                            } else {
                              //  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(),"");
                            }
                            dialog.dismiss();
                            // finish();
                        }
                    });


            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            int socketTimeout = 300000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            // requestQueue.se
            //requestQueue.add(jsObjRequest);
            jsObjRequest.setShouldCache(true);
            //requestQueue.getCache().clear();
            requestQueue.add(jsObjRequest);
            //  Volley.newRequestQueue(this).add(jsObjRequest);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class getINAttendanceresponse extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {


            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("message")) {
                    response_result = response.getString("message");
                } else {
                    response_result = "data";
                }

                if (response_result.equalsIgnoreCase("User doesn't exist")) {


                    //Toast.makeText(Order.this, response_result, Toast.LENGTH_LONG).show();

                    BasicMapDemoActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
                            // recyclerView.setVisibility(View.INVISIBLE);
                            Global_Data.Custom_Toast(BasicMapDemoActivity.this, response_result,"Yes");
//                            Toast toast = Toast.makeText(BasicMapDemoActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                        }
                    });


                } else if (response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(Attendance_Map.this, response_result, Toast.LENGTH_LONG).show();

                    BasicMapDemoActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

                            Global_Data.Custom_Toast(BasicMapDemoActivity.this, response_result,"Yes");

//                            Toast toast = Toast.makeText(BasicMapDemoActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                        }
                    });

                } else if (response_result.equalsIgnoreCase("Device not registered")) {

                    //Toast.makeText(Attendance_Map.this, response_result, Toast.LENGTH_LONG).show();

                    BasicMapDemoActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

                            Global_Data.Custom_Toast(BasicMapDemoActivity.this, response_result,"Yes");

//                            Toast toast = Toast.makeText(BasicMapDemoActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();


                        }
                    });


                } else if (response_result.equalsIgnoreCase("Conference not found")) {

                    //Toast.makeText(Attendance_Map.this, response_result, Toast.LENGTH_LONG).show();

                    BasicMapDemoActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();


                            Global_Data.Custom_Toast(BasicMapDemoActivity.this, response_result,"Yes");
//                            Toast toast = Toast.makeText(BasicMapDemoActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();


                        }
                    });


                } else {

                    BasicMapDemoActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

                            Global_Data.Custom_Toast(BasicMapDemoActivity.this, response_result,"Yes");

//                            Toast toast = Toast.makeText(BasicMapDemoActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            showDialogn(datenn, str.toString());


                        }
                    });


                    //dbvoc.getDeleteTable("attendance");
                    dbvoc.getDeleteattendance_daten(datenn, in_out_flag);

                    DateFormat date_onlyn = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    String date_only_s = date_onlyn.format(date);

                    if (in_out_flag.equalsIgnoreCase("IN")) {
                        dbvoc.getDeleteTable("attendence_f");
                        loginDataBaseAdapter.insertattendence_flag("true", date_only_s);
                    } else {
                        dbvoc.getDeleteTable("attendence_f");
                        loginDataBaseAdapter.insertattendence_flag("false", date_only_s);
                    }


                    BasicMapDemoActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();

                BasicMapDemoActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });

            }


            BasicMapDemoActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            BasicMapDemoActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                }
            });


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
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
                str = new StringBuilder();
                //prefManager.setAddress(locationAddress);
                Log.d("GLOBEL ADDRESS G", "V" + locationAddress);
                str.append("");
                str.append(locationAddress);

            } else {
                str = new StringBuilder();
                Log.d("GLOBEL ADDRESS G", "address not found.");
                //prefManager.setAddress("");
                str.append("");
            }


        }
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {

            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .showInputMethodPicker();

            Global_Data.Custom_Toast(this, "Barcode Scanner detected. Please turn OFF Hardware/Physical keyboard to enable softkeyboard to function.","");
          //  Toast.makeText(this, "Barcode Scanner detected. Please turn OFF Hardware/Physical keyboard to enable softkeyboard to function.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                       if(gps_redirection_flag.equalsIgnoreCase("in"))
                       {
                           AttinCall();
                       }
                       else
                       if(gps_redirection_flag.equalsIgnoreCase("out"))
                       {
                           attoutCall();
                       }

                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        gps_redirection_flag = "";
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    public void AttinCall()
    {
        try {


            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, BasicMapDemoActivity.this);
            try {

                at_in.setBackgroundColor(Color.parseColor("#075b97"));
                at_in.setTextColor(Color.WHITE);
                at_out.setBackgroundColor(Color.WHITE);
                at_out.setTextColor(Color.BLACK);
//                        String user_email = prefManager.get_USERNAME().trim();
//                        String conference_id = prefManager.get_ORDER_TYPE_ID().trim();
//                        String vertical_id = prefManager.get_BRAND_ID().trim();

                //android.text.format.DateFormat df = new android.text.format.DateFormat();
                // String daten = df.format("hh:mm a yyyy-MM-dd", new java.util.Date()).toString();

                SharedPreferences spf = BasicMapDemoActivity.this.getSharedPreferences("SimpleLogic", 0);
                String user_email = spf.getString("USER_EMAIL", null);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a yyyy-MM-dd");
                DateFormat date_only = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String daten = sdf.format(date);
                datenn = sdf.format(date);
                String date_only_s = date_only.format(date);

                String a_check_datan = "";
                List<Local_Data> a_checkn = dbvoc.getAllAttendanceF_Data();
                if (a_checkn.size() > 0) {
                    for (Local_Data cn : a_checkn) {
                        a_check_datan = cn.getName();
                    }
                } else {
                    loginDataBaseAdapter.insertattendence_flag("false", date_only_s);
                    a_check_datan = "false";
                }

                List<Local_Data> contacts2 = dbvoc.getAllAttendance_Data_bydate(date_only_s);

                if (contacts2.size() <= 0 && a_check_datan.equalsIgnoreCase("false")) {

                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {

                        dialog.setMessage(getResources().getString(R.string.Please_Wait));
                        dialog.setTitle(getResources().getString(R.string.app_name));
                        dialog.setCancelable(false);
                        dialog.show();

                        in_out_flag = "IN";

                        Attendance_data(user_email, daten, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, "IN", Global_Data.address);
                    } else {
                        in_out_flag = "IN";
                        Global_Data.Custom_Toast(BasicMapDemoActivity.this, getResources().getString(R.string.att_net_message),"");
                        //  Toast.makeText(BasicMapDemoActivity.this, getResources().getString(R.string.att_net_message), Toast.LENGTH_SHORT).show();
                        //loginDataBaseAdapter.insert_attendance_data(user_email, daten, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, "IN","false",date_only_s);
                        loginDataBaseAdapter.insert_attendance_data(user_email, daten, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, "IN", Global_Data.address, "false", date_only_s);


                        dbvoc.getDeleteTable("attendence_f");
                        loginDataBaseAdapter.insertattendence_flag("true", date_only_s);
                        Global_Data.Custom_Toast(BasicMapDemoActivity.this, getResources().getString(R.string.Attendance_punch_successfully),"");
                        //  Toast.makeText(BasicMapDemoActivity.this, getResources().getString(R.string.Attendance_punch_successfully), Toast.LENGTH_SHORT).show();
                        showDialogn(daten, str.toString());
                    }

                } else {
//                            for (Local_Data cn : contacts2)
//                            {
                    //  String database_date = cn.getCurrent_date_only();
                    // String server_flag = cn.getServer_flag();
                    // String button_flag = cn.getPunched_button();
                    // Date date_check_date = date_only.parse(database_date);
                    Date new_current_date = date_only.parse(date_only_s);
                    //Date newdate = format.parse(daten);
                    long millisecond = new_current_date.getTime();
                    //Date newdate = format.parse(daten);

                    String a_check_data = "";
                    List<Local_Data> a_check = dbvoc.getAllAttendanceF_Data();
                    if (a_check.size() > 0) {
                        for (Local_Data cn : a_check) {
                            a_check_data = cn.getName();
                        }
                    }

                    // Toast.makeText(Attendance_Map.this, ""+a_check_data, Toast.LENGTH_SHORT).show();

                    if (DateUtils.isToday(millisecond) && a_check_data.equalsIgnoreCase("true")) {
//
//                                   Toast.makeText(BasicMapDemoActivity.this, "You already punch attendance at "+database_date, Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(BasicMapDemoActivity.this, getResources().getString(R.string.Already_Punched_Your_Attendance),"");
                        // Toast.makeText(BasicMapDemoActivity.this, getResources().getString(R.string.Already_Punched_Your_Attendance), Toast.LENGTH_SHORT).show();

                    } else if (DateUtils.isToday(millisecond) && a_check_data.equalsIgnoreCase("false")) {

                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent) {

                            dialog.setMessage(getResources().getString(R.string.Please_Wait));
                            dialog.setTitle(getResources().getString(R.string.app_name));
                            dialog.setCancelable(false);
                            dialog.show();

                            in_out_flag = "IN";
                            Attendance_data(user_email, daten, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, "IN", Global_Data.address);
                        } else {
                            in_out_flag = "IN";
                            Global_Data.Custom_Toast(BasicMapDemoActivity.this, getResources().getString(R.string.att_net_message),"");
                            //Toast.makeText(BasicMapDemoActivity.this, getResources().getString(R.string.att_net_message), Toast.LENGTH_SHORT).show();
                            loginDataBaseAdapter.insert_attendance_data(user_email, daten, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, "IN", Global_Data.address, "false", date_only_s);

                            dbvoc.getDeleteTable("attendence_f");
                            loginDataBaseAdapter.insertattendence_flag("true", date_only_s);
                            Global_Data.Custom_Toast(BasicMapDemoActivity.this, getResources().getString(R.string.Attendance_punch_successfully),"");
                            //Toast.makeText(BasicMapDemoActivity.this, getResources().getString(R.string.Attendance_punch_successfully), Toast.LENGTH_SHORT).show();
                            showDialogn(daten, str.toString());
                        }
                    }
                    //}
                }

//
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void attoutCall()
    {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, BasicMapDemoActivity.this);
            try {

                at_out.setBackgroundColor(Color.parseColor("#075b97"));
                at_out.setTextColor(Color.WHITE);
                at_in.setBackgroundColor(Color.WHITE);
                at_in.setTextColor(Color.BLACK);

//                        String user_email = prefManager.get_USERNAME().trim();
//                        String conference_id = prefManager.get_ORDER_TYPE_ID().trim();
//                        String vertical_id = prefManager.get_BRAND_ID().trim();


                SharedPreferences spf = BasicMapDemoActivity.this.getSharedPreferences("SimpleLogic", 0);
                String user_email = spf.getString("USER_EMAIL", null);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a yyyy-MM-dd");
                DateFormat date_only = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String daten = sdf.format(date);
                String date_only_s = date_only.format(date);
                String datenn = sdf.format(date);

                String a_check_data = "";
                List<Local_Data> a_check = dbvoc.getAllAttendanceF_Data();
                if (a_check.size() > 0) {
                    for (Local_Data cn : a_check) {
                        a_check_data = cn.getName();
                    }
                }

                if (a_check_data.equalsIgnoreCase("true")) {
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {


                        dialog.setMessage(getResources().getString(R.string.Please_Wait));
                        dialog.setTitle(getResources().getString(R.string.app_name));
                        dialog.setCancelable(false);
                        dialog.show();

                        in_out_flag = "OUT";
                        Attendance_data(user_email, daten, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, "OUT", Global_Data.address);
                        //break;
                    } else {
                        in_out_flag = "OUT";
                        loginDataBaseAdapter.insert_attendance_data(user_email, daten, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, "OUT", Global_Data.address, "false", date_only_s);
                        Global_Data.Custom_Toast(BasicMapDemoActivity.this, getResources().getString(R.string.Out_successfully),"");
                        // Toast.makeText(BasicMapDemoActivity.this, getResources().getString(R.string.Out_successfully), Toast.LENGTH_SHORT).show();

                        dbvoc.getDeleteTable("attendence_f");
                        loginDataBaseAdapter.insertattendence_flag("false", date_only_s);

                        showDialogn(daten, str.toString());
                        // break;
                    }
                } else {
                    Global_Data.Custom_Toast(BasicMapDemoActivity.this, getResources().getString(R.string.Please_Press_IN_Button),"");
                    // Toast.makeText(BasicMapDemoActivity.this, getResources().getString(R.string.Please_Press_IN_Button), Toast.LENGTH_SHORT).show();
                    // prefManager.setATTENDANCE_FLAG("false");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}