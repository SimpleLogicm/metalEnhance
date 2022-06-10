package com.msimplelogic.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.msimplelogic.activities.Check_Null_Value;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.Local_Data;
import com.msimplelogic.activities.MapsActivity;
import com.msimplelogic.activities.NewOrderActivity;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.kotlinFiles.Edit_cutomer_details;
import com.msimplelogic.activities.kotlinFiles.kotGlobal.QuickOrder;
import com.msimplelogic.model.CustomerModelMap;

import java.util.List;

import cpm.simplelogic.helper.ConnectionDetector;


public class CustomerMapAdapter extends RecyclerView.Adapter<CustomerMapAdapter.ContactViewHolder> {

    static ConnectionDetector cd;
    static Boolean isInternetPresent = false;
    private List<CustomerModelMap> contactList;
    private List<CustomerModelMap> contactListfilter;
    String name = "";
    DataBaseHelper dbvoc;
    String activity_name = "";
    GoogleMap mmap;
    Marker mmarker;


    static Context mcontext;
    MapsActivity mapsActivity;
    public CustomerMapAdapter(List<CustomerModelMap> contactList, Context context,MapsActivity activity) {
        this.contactList = contactList;
        mcontext = context;
        mapsActivity =activity;
        //this.contactListfilter.addAll(this.contactList);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, final int i) {
        final CustomerModelMap ci = contactList.get(i);
        contactViewHolder.d_name.setText("Shop Name : " + ci.getName());
        final String shopname =ci.getName();
        final String address =ci.getAddress();
        contactViewHolder.p_delaer_address.setText("Address : " + ci.getAddress());
        contactViewHolder.p_distance.setText("Distance : " + ci.getDistance());
        //contactViewHolder.p_delaer_name.setText("Firm Name : " + ci.name);
        contactViewHolder.p_proprietor_mobile1.setText("Proprietor Mobile : " + ci.getMobile());
        contactViewHolder.p_mobi.setText(ci.getMobile());
        //contactViewHolder.p_proprietor_name1.setText("proprietor Name : " + ci.proprietor_name1);
        //contactViewHolder.p_proprietor_email1.setText("proprietor Email : " + ci.proprietor_email1);
       // contactViewHolder.p_proprietor_gst_no.setText("Gst No : " + ci.gst_no);


        // contactViewHolder.p_dealer_city.setText("City : " + ci.city);
        //  contactViewHolder.p_stages.setText("Stage : " + ci.Stage);
        contactViewHolder.p_dealer_code.setText(ci.getCode());
        contactViewHolder.p_lati.setText(ci.getLati());
        contactViewHolder.p_longi.setText(ci.getLongi());



//        if(Global_Data.selectedPosition==i)
//            contactViewHolder.d_name.setBackgroundColor(Color.parseColor("#cc2303"));
//        else
//            contactViewHolder.d_name.setBackgroundColor(Color.parseColor("#1D3A58"));


        contactViewHolder.pro_click_order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                dbvoc = new DataBaseHelper(mcontext);
                Global_Data.GLOvel_CUSTOMER_ID = contactViewHolder.p_dealer_code.getText().toString();
                List<Local_Data> cont1  = dbvoc.getCustomername(Global_Data.GLOvel_CUSTOMER_ID);
                if (cont1.size() > 0) {

                    Global_Data.customer_MobileNumber = ci.mobile;
                    Global_Data.CUSTOMER_NAME_NEW = shopname;
                    Global_Data.CUSTOMER_ADDRESS_NEW = address;
                    Global_Data.CustLandlineNo = "";

                    Global_Data.GLObalOrder_id = "";
                    Global_Data.GLOvel_GORDER_ID = "";
                    SharedPreferences sp  = mcontext.getSharedPreferences("SimpleLogic", 0);
                    SharedPreferences.Editor spreedit = sp.edit();
                    spreedit.putString("shopname",shopname);
                    spreedit.putString("shopadd",address);
                    spreedit.putString("shopcode",contactViewHolder.p_dealer_code.getText().toString());
                    spreedit.putString("c_mobile_number",ci.mobile);
                    spreedit.putString("c_mobile_email",ci.email);
                    spreedit.putString("customer_landline","");
                    spreedit.putString("c_address", address);
                    spreedit.commit();

                    Intent intent = new Intent(mcontext, QuickOrder.class);
                    intent.putExtra("shopname", shopname);
                    mcontext.startActivity(intent);
                }
                else
                {

                    Global_Data.Custom_Toast(mcontext, mcontext.getString(R.string.Customernotfountbeat),"");
                }

               // ((Activity)mcontext).finish();

            }
        });

        contactViewHolder.pro_check_distance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String names = contactViewHolder.d_name.getText().toString();
                String lati = contactViewHolder.p_lati.getText().toString();
                String longi = contactViewHolder.p_longi.getText().toString();
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(lati) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(longi)) {


                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr="+lati+","+longi));
                    mcontext.startActivity(intent);

                }


            }
        });

        contactViewHolder.sub_h_container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Global_Data.Sub_Dealer_Code = contactViewHolder.p_dealer_code.getText().toString();
                String names = contactViewHolder.d_name.getText().toString();
                String lati = contactViewHolder.p_lati.getText().toString();
                String longi = contactViewHolder.p_longi.getText().toString();
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(lati) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(longi)) {
                    mmap = mapsActivity.getmap();
                    mmarker = mapsActivity.getmarker();
                    LatLng mLatLng = new LatLng(Double.valueOf(lati), Double.valueOf(longi));

                    if (mmarker != null && mmarker.isVisible()) {
                        mmarker.remove();
                    }
                    mmarker = mmap.addMarker(new MarkerOptions().position(mLatLng).title(names).snippet(names).icon(BitmapDescriptorFactory.fromResource(R.drawable.fevstar)));

                    //hashMapMarker.put(code, marker);
                    LatLngBounds.Builder buildern = new LatLngBounds.Builder();

                    try {
                        buildern.include(mmarker.getPosition());
                        Global_Data.mMarkers.get(i).showInfoWindow();
                        //infowindow.open(map,marker);
                    } catch (Exception ex) {

                    }


                    try {
                        LatLngBounds bounds = buildern.build();

                        int width = mcontext.getResources().getDisplayMetrics().widthPixels;
                        int height = mcontext.getResources().getDisplayMetrics().heightPixels;
                        int padding = (int) (width * 0.005); // offset from edges of the map 10% of
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                        mmap.animateCamera(cu);

                        mapsActivity.setmarker(mmarker);
                    } catch (Exception ex) {

                    }


                }

                Global_Data.selectedPosition=i;
                notifyDataSetChanged();


            }
        });

        contactViewHolder.pro_mobile_click.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String mobile = contactViewHolder.p_mobi.getText().toString();
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(mobile)) {
                    requestPHONEPermission(mobile);
                }
                else {

                    Global_Data.Custom_Toast(mcontext, "Mobile Number Not Found.","yes");
                }

            }
        });

        contactViewHolder.pro_click_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                dbvoc = new DataBaseHelper(mcontext);
                Global_Data.GLOvel_CUSTOMER_ID = contactViewHolder.p_dealer_code.getText().toString();
                List<Local_Data> cont1  = dbvoc.getCustomername(Global_Data.GLOvel_CUSTOMER_ID);
                if (cont1.size() > 0) {
                    Global_Data.customer_MobileNumber = ci.mobile;
                    Global_Data.CUSTOMER_NAME_NEW = shopname;
                    Global_Data.CUSTOMER_ADDRESS_NEW = address;
                    Global_Data.CustLandlineNo = "";

                    SharedPreferences sp  = mcontext.getSharedPreferences("SimpleLogic", 0);
                    SharedPreferences.Editor spreedit = sp.edit();
                    spreedit.putString("shopname",shopname);
                    spreedit.putString("shopadd",address);
                    spreedit.putString("shopcode",contactViewHolder.p_dealer_code.getText().toString());
                    spreedit.putString("c_mobile_number",ci.mobile);
                    spreedit.putString("c_mobile_email",ci.email);
                    spreedit.putString("customer_landline","");
                    spreedit.putString("c_address", address);
                    spreedit.commit();

                    mcontext.startActivity(new Intent(mcontext, Edit_cutomer_details.class));
                }
                else
                {
                    Global_Data.Custom_Toast(mcontext, mcontext.getString(R.string.Customernotfountbeat),"");
                }

            }
        });



    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.customermapadapter, viewGroup, false);
        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView d_name;
        protected TextView p_delaer_address;
        protected TextView p_distance;
        // protected TextView p_delaer_name;
        protected TextView p_proprietor_mobile1;
        //  protected TextView p_proprietor_name1;
        protected TextView p_proprietor_email1;
        protected TextView p_proprietor_gst_no;
        // protected TextView p_dealer_city;
        protected TextView p_dealer_code;
        protected TextView p_stages,p_lati,p_longi,p_mobi;
        RelativeLayout sub_h_container;
        Button pro_mobile_click,pro_check_distance,pro_click_order,pro_click_edit;


        public ContactViewHolder(View v) {
            super(v);

            d_name = v.findViewById(R.id.d_name);
            p_delaer_address = v.findViewById(R.id.p_delaer_address);
            p_distance = v.findViewById(R.id.p_distance);
            // p_delaer_name = v.findViewById(R.id.p_delaer_name);
            p_proprietor_mobile1 = v.findViewById(R.id.p_proprietor_mobile1);
            // p_proprietor_name1 = v.findViewById(R.id.p_proprietor_name1);
            p_proprietor_email1 = v.findViewById(R.id.p_proprietor_email1);
            p_proprietor_gst_no = v.findViewById(R.id.p_proprietor_gst_no);
            //  p_dealer_city = v.findViewById(R.id.p_dealer_city);
            p_dealer_code = v.findViewById(R.id.p_dealer_code);
            p_stages = v.findViewById(R.id.p_stages);
            p_lati = v.findViewById(R.id.p_lati);
            p_longi = v.findViewById(R.id.p_longi);
            p_mobi = v.findViewById(R.id.p_mobi);
            sub_h_container = v.findViewById(R.id.sub_h_container);
            pro_mobile_click = v.findViewById(R.id.pro_mobile_click);
            pro_check_distance = v.findViewById(R.id.pro_check_distance);
            pro_click_order = v.findViewById(R.id.pro_click_order);
            pro_click_edit = v.findViewById(R.id.pro_click_edit);



        }

        // Handles the row being being clicked

    }



    /**
     * Requesting GPS permission
     * This uses single permission model from dexter
     * Once the permission granted, opens the camera
     * On permanent denial opens settings dialog
     */
    public void requestPHONEPermission(final String mobile_number) {
        Dexter.withActivity((Activity)mcontext)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile_number));
                        mcontext.startActivity(intent);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Need Permissions");
        builder.setCancelable(false);
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        Uri uri = Uri.fromParts("package", mcontext.getPackageName(), null);
        intent.setData(uri);
        ((Activity)mcontext).startActivityForResult(intent, 101);
    }



}

