package com.msimplelogic.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class MapMultiUser extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    ProgressDialog dialog;
    double lat, lon;
    int j = 1;
    Toolbar toolbar;
    // JSON Node names
    private static final String TAG_RESULTS = "results";
    private static final String TAG_GEOMETRY = "geometry";
    private static final String TAG_VIEWPORT = "viewport";
    private static final String TAG_NORTHEAST = "northeast";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LNG = "lng";
    // contacts JSONArray
    JSONArray results = null;
    private GoogleMap googleMap;
    private ArrayList<LatLng> listLatLng;
    private LinearLayout rlMapLayout;
    HashMap<Marker, Local_Data> hashMapMarker = new HashMap<Marker, Local_Data>();
    GoogleApiClient mGoogleApiClient;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    ArrayList<LatLngBean> arrayList;
    LatLngBean bean;
    ArrayList<Local_Data> list_cities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beatonmap);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        bean = new LatLngBean();

        dialog = new ProgressDialog(MapMultiUser.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        arrayList = new ArrayList<LatLngBean>();
        rlMapLayout = (LinearLayout) findViewById(R.id.rlMapLayout);
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
//            mTitleTextView.setText(getResources().getString(R.string.Beat_On_Map));
//
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = MapMultiUser.this.getSharedPreferences("SimpleLogic", 0);
//
////		       if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
////		       //	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
////				   todaysTarget.setText("Target/Acheived : Rs "+String.format(sp.getFloat("Target",0)+"/"+sp.getFloat("Achived", 0)));
////				}
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
//                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
//                } else {
//                    int age = (int) Math.round(age_float);
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    }
//                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                }
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
//                //       	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
//                todaysTarget.setText("Today's Target Acheived");
//            }
//
//            mActionBar.setCustomView(mCustomView);
//            mActionBar.setDisplayShowCustomEnabled(true);
//            mActionBar.setHomeButtonEnabled(true);
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


        setUpMapIfNeeded();
        //setData();
        LoadingGoogleMap(Global_Data.list_mapdata);
    }

    /**
     * @author Hasmukh Bhadani
     * Set googleMap if require
     */

    private void setUpMapIfNeeded() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Google Play Services are not available
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {
            if (googleMap == null) {
                ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(MapMultiUser.this);
                if (googleMap != null) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                }
            }
        }
    }

    /**
     * @author Hasmukh Bhadani
     * Loading Data to the GoogleMap
     */
    // -------------------------Google Map
    void LoadingGoogleMap(ArrayList<Local_Data> arrayList) {
        if (googleMap != null) {
            googleMap.clear();
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            if (arrayList.size() > 0) {
                try {
                    listLatLng = new ArrayList<LatLng>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        Local_Data bean = arrayList.get(i);
                        if (bean.getLatitude().length() > 0 && bean.getLongitude().length() > 0) {
                            double lat = Double.parseDouble(bean.getLatitude());
                            double lon = Double.parseDouble(bean.getLongitude());

                            if (j == 1) {

                                Global_Data.marker_color = "HUE_RED";
                                Marker marker1;
                                String name = bean.getBeatName();
                                View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.maptitle, null);
                                TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
                                numTxt.setText(name);
                                marker1 = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, lon))
                                       // .title(name)
                                        .snippet(bean.getSnippet())
                                      //  .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
                                marker1.showInfoWindow();


                              //  googleMap.addMarker(marker1).showInfoWindow();

                                hashMapMarker.put(marker1, bean);
                                j = 2;
                            } else if (j == 2) {
                                Global_Data.marker_color = "HUE_GREEN";
                                Marker marker2;
                                View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.maptitle, null);
                                TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
                                numTxt.setText(bean.getBeatName());
                                marker2 = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, lon))
                                       // .title(bean.getBeatName())
                                        .snippet(bean.getSnippet())
                                       // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));

                                marker2.showInfoWindow();
                                hashMapMarker.put(marker2, bean);
                                j = 3;
                            } else if (j == 3) {
                                Global_Data.marker_color = "HUE_CYAN";
                                Marker marker3;
                                View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.maptitle, null);
                                TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
                                numTxt.setText(bean.getBeatName());
                                marker3 = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, lon))
                                     //   .title(bean.getBeatName())
                                        .snippet(bean.getSnippet())
                                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));

                                marker3.showInfoWindow();
                                hashMapMarker.put(marker3, bean);
                                j = 4;
                            } else if (j == 4) {
                                Global_Data.marker_color = "HUE_BLUE";
                                Marker marker4;
                                View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.maptitle, null);
                                TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
                                numTxt.setText(bean.getBeatName());
                                marker4 = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, lon))
                                       // .title(bean.getBeatName())
                                        .snippet(bean.getSnippet())
                                   //     .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
                                marker4.showInfoWindow();
                                hashMapMarker.put(marker4, bean);
                                j = 5;
                            } else if (j == 5) {
                                Global_Data.marker_color = "HUE_AZURE";
                                Marker marker5;
                                View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.maptitle, null);
                                TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
                                numTxt.setText(bean.getBeatName());
                                marker5 = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, lon))
                                      //  .title(bean.getBeatName())
                                        .snippet(bean.getSnippet())
                                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
                                marker5.showInfoWindow();
                                hashMapMarker.put(marker5, bean);
                                j = 6;
                            } else if (j == 6) {
                                Global_Data.marker_color = "HUE_MAGENTA";
                                Marker marker6;
                                View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.maptitle, null);
                                TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
                                numTxt.setText(bean.getBeatName());
                                marker6 = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, lon))
                                       // .title(bean.getBeatName())
                                        .snippet(bean.getSnippet())
//                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
                                marker6.showInfoWindow();
                                hashMapMarker.put(marker6, bean);
                                j = 7;
                            } else if (j == 7) {
                                Global_Data.marker_color = "HUE_ORANGE";
                                Marker marker7;
                                View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.maptitle, null);
                                TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
                                numTxt.setText(bean.getBeatName());
                                marker7 = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, lon))
                                        .title(bean.getBeatName())
                                        .snippet(bean.getSnippet())
                                       //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
                                marker7.showInfoWindow();
                                hashMapMarker.put(marker7, bean);
                                j = 1;
                            }

                            //Set Zoom Level of Map pin
                            LatLng object = new LatLng(lat, lon);
                            listLatLng.add(object);
                        }
                    }
                    SetZoomlevel(listLatLng);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker arg0) {

                        Local_Data bean = hashMapMarker.get(arg0);
                        //Toast.makeText(getApplicationContext(), bean.getTitle(),Toast.LENGTH_SHORT).show();
//                        List<Local_Data> contacts2 = dbvoc.getBeatCode(bean.getTitle());
//                        for (Local_Data cn : contacts2) {
                        //results_beat.add(cn.getNamemap());
                        //String city=cn.getNamemap();
                        Geocoder geocoder = new Geocoder(MapMultiUser.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocationName(bean.getTitle(), 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Address address = addresses.get(0);
                        if (addresses.size() > 0) {
                            Global_Data.maplat = String.valueOf(addresses.get(0).getLatitude());
                            Global_Data.maplon = String.valueOf(addresses.get(0).getLongitude());
                        }
                        // }

                        View_NearestCustomer();
//                        startActivity(new Intent(MapMultiUser.this,MapMultiUser1.class));

                        return true;
                    }
                });

//                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                    @Override
//                    public void onInfoWindowClick(Marker position)
//                    {
//                        Local_Data bean=hashMapMarker.get(position);
//                        //Toast.makeText(getApplicationContext(), bean.getTitle(),Toast.LENGTH_SHORT).show();
//                        List<Local_Data> contacts2 = dbvoc.getBeatCode(bean.getTitle());
//                        for (Local_Data cn : contacts2) {
//                            //results_beat.add(cn.getNamemap());
//                            //String city=cn.getNamemap();
//                            Geocoder geocoder = new Geocoder(MapMultiUser.this, Locale.getDefault());
//                            List<Address> addresses = null;
//                            try {
//                                addresses = geocoder.getFromLocationName(bean.getTitle(), 1);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            //Address address = addresses.get(0);
//                            if(addresses.size() > 0) {
//                                Global_Data.maplat = String.valueOf(addresses.get(0).getLatitude());
//                                Global_Data.maplon = String.valueOf(addresses.get(0).getLongitude());
//                                //LatLng latLng = new LatLng(latitude, longitude);
////            mMap.addMarker(new MarkerOptions().position(latLng));
////            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
////            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
//                            }
//
//                            //cn.setTitle(cn.getNamemap());
////                            cn.setSnippet("Hello" +","+ cn.getNamemap());
////                            cn.setLatitude(String.valueOf(lat));
////                            cn.setLongitude(String.valueOf(lon));
////                            //arrayList.add(bean);
////                            list_cities.add(cn);
////                            LoadingGoogleMap(list_cities);
//                        }
//                        startActivity(new Intent(MapMultiUser.this,MapMultiUser1.class));
//                    }
//                });
            }
        } else {
            //Toast.makeText(getApplicationContext(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @author Hasmukh Bhadani
     * Set Zoom level all pin withing screen on GoogleMap
     */
    public void SetZoomlevel(ArrayList<LatLng> listLatLng) {
        if (listLatLng != null && listLatLng.size() == 1) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(0), 10));
        } else if (listLatLng != null && listLatLng.size() > 1) {
            final LatLngBounds.Builder builder = LatLngBounds.builder();
            for (int i = 0; i < listLatLng.size(); i++) {
                builder.include(listLatLng.get(i));
            }

            final ViewTreeObserver treeObserver = rlMapLayout.getViewTreeObserver();
            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    if (googleMap != null) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), findViewById(R.id.map)
                                .getWidth(), findViewById(R.id.map).getHeight(), 100));
                        rlMapLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMapp) {
        googleMap = googleMapp;
        try {
            googleMap = googleMapp;
            // mGoogleMap.setMyLocationEnabled(true);

            buildGoogleApiClient();

            mGoogleApiClient.connect();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setUpMapIfNeeded();
        //setData();
        LoadingGoogleMap(Global_Data.list_mapdata);
        //setUpMap();
    }

    protected synchronized void buildGoogleApiClient() {
        // Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        try {
            googleMap.setMyLocationEnabled(true);
            googleMap.setTrafficEnabled(true);
            googleMap.setIndoorEnabled(true);
            googleMap.setBuildingsEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);

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
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void View_NearestCustomer() {

        dialog.setMessage(getResources().getString(R.string.Please_Wait));
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        new LOGINOperation().execute();

    }

    private class LOGINOperation extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            SharedPreferences spf = MapMultiUser.this.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL",null);

            SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";
            String domain = service_url;
            Global_Data.device_id = sp.getString("devid", "");
            try {
                Log.d("Server url", "Server url" + domain + "customer_list?latitude="
                        + URLEncoder.encode(Global_Data.maplat, "UTF-8") + "&longitude="
                        + URLEncoder.encode(Global_Data.maplon, "UTF-8") + "&email="
                        + user_email);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            StringRequest stringRequest = null;
            try {
                stringRequest = new StringRequest(Request.Method.GET, domain + "customer_list?latitude="
                        + URLEncoder.encode(Global_Data.maplat, "UTF-8") + "&longitude="
                        + URLEncoder.encode(Global_Data.maplon, "UTF-8") + "&email="
                        + user_email,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //showJSON(response);
                                // Log.d("jV", "JV" + response);
                                Log.d("jV", "JV length" + response.length());
                                // JSONObject person = (JSONObject) (response);

                                // new LOGINOperation().execute(response);
                                try {
                                    //JSONObject json = new JSONObject(String.valueOf(response));
                                    JSONObject json = new JSONObject(new JSONTokener(response));
                                    try {

                                        String response_result = "";
                                        if (json.has("message")) {
                                            response_result = json.getString("message");
                                        } else {
                                            response_result = "data";
                                        }

                                        if (response_result.equalsIgnoreCase("User not found")) {

                                            //Toast.makeText(Nearest_Customer.this, response_result, Toast.LENGTH_LONG).show();

                                            MapMultiUser.this.runOnUiThread(new Runnable() {
                                                public void run() {
//                                                    Toast toast = Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_LONG);
//                                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                                    toast.show();
                                                    Global_Data.Custom_Toast(getApplicationContext(), "User not found","yes");
                                                }
                                            });


                                            Intent a = new Intent(MapMultiUser.this, MainActivity.class);
                                            startActivity(a);
                                            finish();

                                        }
                                        if (response_result.equalsIgnoreCase("Device not registered")) {

                                            //Toast.makeText(Nearest_Customer.this, response_result, Toast.LENGTH_LONG).show();

//                                            Toast toast = Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG);
//                                            toast.setGravity(Gravity.CENTER, 0, 0);
//                                            toast.show();
                                            Global_Data.Custom_Toast(getApplicationContext(), response_result,"yes");

                                            Intent a = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(a);
                                            finish();

                                        }
                                        if (response_result.equalsIgnoreCase("Customers not found")) {

                                            // Toast.makeText(Nearest_Customer.this, response_result, Toast.LENGTH_LONG).show();

                                            MapMultiUser.this.runOnUiThread(new Runnable() {
                                                public void run() {
//                                                    Toast toast = Toast.makeText(getApplicationContext(), "Customers not found", Toast.LENGTH_LONG);
//                                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                                    toast.show();
                                                    Global_Data.Custom_Toast(getApplicationContext(), "Customers not found","yes");
                                                }
                                            });


                                            Intent a = new Intent(MapMultiUser.this, MainActivity.class);
                                            startActivity(a);
                                            finish();
                                        } else if (json.getJSONArray("data").length() <= 0) {

                                            // Toast.makeText(Nearest_Customer.this, "Customers not found", Toast.LENGTH_LONG).show();

                                            MapMultiUser.this.runOnUiThread(new Runnable() {
                                                public void run() {
//                                                    Toast toast = Toast.makeText(getApplicationContext(), "Customers not found", Toast.LENGTH_LONG);
//                                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                                    toast.show();
                                                    Global_Data.Custom_Toast(getApplicationContext(), "Customers not found","yes");
                                                }
                                            });

                                            Intent a = new Intent(MapMultiUser.this, MainActivity.class);
                                            startActivity(a);
                                            finish();
                                        } else {

                                            JSONArray data = json.getJSONArray("data");
                                            Log.d("data", "data" + data.toString());
                                            Log.d("data", "data length" + data.length());

                                            // if (data.length() < 10) {
                                            for (int i = 0; i < 10; i++) {

                                                JSONObject jsonObject = data.getJSONObject(i);
                                                HashMap<String, String> map = new HashMap<String, String>();
//                                            map.put(CUSTOMER_NAME, jsonObject.getString("customer_name"));
//                                            map.put(CUSTOMER_ADDRESS, jsonObject.getString("customer_address"));
//                                            map.put(CUSTOMER_DISTANCE, jsonObject.getString("distance"));
                                                String str = jsonObject.getString("customer_name");
                                                String str1 = jsonObject.getString("customer_address");

                                                String str2 = jsonObject.getString("distance");


                                                //Float st = Float.parseFloat((str2.substring(0, str2.length() - 3)));

                                                //Local_Data bean = new Local_Data();

                                                new Task().execute(str1, str);

                                            }


                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                public void run() {
                                                    // Actions to do after 10 seconds
                                                    startActivity(new Intent(MapMultiUser.this, MapMultiUser1.class));
                                                    dialog.dismiss();
                                                }
                                            }, 10000);

//                                            } else {
//                                                Toast.makeText(MapMultiUser.this, "Customer Not Found", Toast.LENGTH_SHORT).show();
//                                                dialog.dismiss();
//                                            }
                                        }

                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                        dialog.dismiss();
                                        MapMultiUser.this.runOnUiThread(new Runnable() {
                                            public void run() {

                                                Intent a = new Intent(MapMultiUser.this, MainActivity.class);
                                                startActivity(a);
                                                finish();
                                            }
                                        });
                                    }

                                    //dialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //  finish();
                                    //dialog.dismiss();

                                    MapMultiUser.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Intent a = new Intent(MapMultiUser.this, MainActivity.class);
                                            startActivity(a);
                                            finish();
                                        }
                                    });
                                }

                                // dialog.dismiss();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Toast.makeText(Nearest_Customer.this, error.getMessage(), Toast.LENGTH_LONG).show();

                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                                    Toast toast = Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(getApplicationContext(), "Network Error","yes");
                                } else if (error instanceof AuthFailureError) {
//                              Toast.makeText(Nearest_Customer.this,
//                                      "Server AuthFailureError  Error",
//                                      Toast.LENGTH_LONG).show();
//                                    Toast toast = Toast.makeText(getApplicationContext(), "Server AuthFailureError  Error", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(getApplicationContext(), "Server AuthFailureError  Error","yes");
                                } else if (error instanceof ServerError) {

//                                    Toast toast = Toast.makeText(getApplicationContext(), "Customers not found", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(getApplicationContext(), "Customers not found","yes");
                                } else if (error instanceof NetworkError) {

//                                    Toast toast = Toast.makeText(getApplicationContext(), "Network   Error", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(getApplicationContext(), "Network   Error", "yes");
                                } else if (error instanceof ParseError) {
//                                    Toast toast = Toast.makeText(getApplicationContext(), "ParseError   Error", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(getApplicationContext(), "ParseError   Error","yes");
                                } else {
                                    // Toast.makeText(Nearest_Customer.this, error.getMessage(), Toast.LENGTH_LONG).show();
//
//                                    Toast toast = Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(),"yes");
                                }
                                dialog.dismiss();
                                Intent a = new Intent(MapMultiUser.this, MainActivity.class);
                                startActivity(a);
                                finish();
                                // finish();
                            }
                        });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Intent a = new Intent(MapMultiUser.this, MainActivity.class);
                startActivity(a);
                finish();
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

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            //dialog.dismiss();
//            MapMultiUser1.this.runOnUiThread(new Runnable() {
//                public void run() {
//                    dialog.dismiss();
//                }
//            });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                Global_Data.list_mapdata.clear();
//                startActivity(new Intent(this, MainActivity.class));
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
                Global_Data.list_mapdata.clear();
                startActivity(new Intent(this, MainActivity.class));
                return true;
        }

        // toggle nav drawer on selecting action bar app icon/title
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.add:
                String targetNew="";
                SharedPreferences sp = MapMultiUser.this.getSharedPreferences("SimpleLogic", 0);
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
        finish();
        Global_Data.list_mapdata.clear();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private class Task extends AsyncTask<String, Void, Long> {
        @Override
        protected Long doInBackground(String... params) {
            String str = params[0];
            String str1 = params[1];
            System.out.println(str);
            Local_Data bean = new Local_Data();
//            str = str.replaceAll(" ", "%20");
//            String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
//                    URLEncoder.encode(str) + "&sensor=false&key=";
//            HttpGet httpGet = new HttpGet(uri);
//            HttpClient client = new DefaultHttpClient();
//            HttpResponse response;
//            StringBuilder stringBuilder = new StringBuilder();

//            try {
//                response = client.execute(httpGet);
//                HttpEntity entity = response.getEntity();
//                InputStream stream = entity.getContent();
//                int b;
//                while ((b = stream.read()) != -1) {
//                    stringBuilder.append((char) b);
//                }
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject = new JSONObject(stringBuilder.toString());
//
//                if (jsonObject.length() <= 0 || jsonObject.has("error_message")) {
//                    MapMultiUser.this.runOnUiThread(new Runnable() {
//                        public void run() {
//                            Toast.makeText(MapMultiUser.this, getResources().getString(R.string.latlong_not_found), Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                            finish();
//
//                        }
//                    });
//
//                } else {

//                    Double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
//                            .getJSONObject("geometry").getJSONObject("location")
//                            .getDouble("lng");
//
//                    Double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
//                            .getJSONObject("geometry").getJSONObject("location")
//                            .getDouble("lat");


                    Geocoder geocoder = new Geocoder(MapMultiUser.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocationName(params[0], 1);
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




                    if (lat > 0 && lon > 0) {

                        bean.setTitle(str1);
                        //cn.setSnippet("Hello" +","+ cn.getNamemap());
                        bean.setLatitude(String.valueOf(lat));
                        bean.setLongitude(String.valueOf(lon));
                        //arrayList.add(bean);
                        Global_Data.list_cities.add(bean);
                        //dialog.dismiss();

//                        bean.setTitle(str1);
//                        //bean.setSnippet("Hello" + "," + str);
//                        bean.setLatitude(String.valueOf(lat));
//                        bean.setLongitude(String.valueOf(lng));
//                        Global_Data.list_cities.add(bean);
//                        dialog.dismiss();
                    }
//                }
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return null;
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Long result) {


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}