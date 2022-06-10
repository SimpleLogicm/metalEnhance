package com.msimplelogic.activities;

/**
 * Created by vinod on 04-10-2016.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.msimplelogic.activities.R;

import com.msimplelogic.webservice.ConnectionDetector;
import java.util.List;
import cpm.simplelogic.helper.Customer_Info;

public class Customer_info_main_adapter extends RecyclerView.Adapter<Customer_info_main_adapter.ContactViewHolder> {
    static ConnectionDetector cd;
    static  Boolean isInternetPresent = false;
    private List<Customer_Info> contactList;
    private List<Customer_Info> contactListfilter;
    static String ss_amountsn = "";
    private Animation animationUp;
    SharedPreferences sp;
    private int lastCheckedPosition2 = -1;
    private int lastCheckedPosition = -1;
    private Animation animationDown;
    static Context mcontext;
    String pos="0";
    public Customer_info_main_adapter(List<Customer_Info> contactList, Context context) {
        this.contactList = contactList;
        this.mcontext = context;
        //this.contactListfilter.addAll(this.contactList);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {

        sp = mcontext.getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1){
contactViewHolder.c_call.setImageResource(R.drawable.telephone_cust_prof_dark);
contactViewHolder.payment.setImageResource(R.drawable.rupee_cust_prof_dark);
contactViewHolder.c_location.setImageResource(R.drawable.placeholder_cust_prof_dark);
contactViewHolder.topimg.setImageResource(R.drawable.dark_theme_oredroptions_headder);
contactViewHolder.bottomimg.setImageResource(R.drawable.darkh_theme_orderoption_footer);



        }
        Customer_Info ci = contactList.get(i);
        contactViewHolder.c_name.setText(ci.shop_name);
        contactViewHolder.c_name2.setText(ci.name);
        contactViewHolder.c_address.setText(ci.address);
        contactViewHolder.c_credit_profile.setText(ci.credit_limit);
        contactViewHolder.c_ammount1.setText(ci.amount1);
        contactViewHolder.c_ammount2.setText(ci.amount2);
        contactViewHolder.c_latlon.setText(ci.latlong);
        contactViewHolder.c_beat_name.setText(ci.Beat_name);
        contactViewHolder.c_city_name.setText(ci.city_name);
        contactViewHolder.c_mobile_number.setText(ci.mobile);
        contactViewHolder.cus_code.setText(ci.cus_code);
        contactViewHolder.c_business_type.setText(ci.c_business_type);


        animationUp = AnimationUtils.loadAnimation(mcontext, R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(mcontext, R.anim.slide_down);

     //   contactViewHolder.L1.

        contactViewHolder.L1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if( contactViewHolder.lin2.isShown()){
                    contactViewHolder.lin2.setVisibility(View.GONE);
                    contactViewHolder.lin2.startAnimation(animationUp);
                    contactViewHolder.plusMinusView.setImageResource(R.drawable.pluscust_profile);
                }
                else{
                    lastCheckedPosition2 = i;
                    lastCheckedPosition = i;
                //    pos=contactList.get(i).toString();
                    contactViewHolder.lin2.setVisibility(View.VISIBLE);
                    contactViewHolder.lin2.startAnimation(animationDown);
                    contactViewHolder.plusMinusView.setImageResource(R.drawable.minusiconcust_profile);
                    notifyDataSetChanged();
                }

            }
        });

        if (i == lastCheckedPosition2) {

            contactViewHolder.lin2.setVisibility(View.VISIBLE);
                   contactViewHolder.lin2.startAnimation(animationDown);
                    contactViewHolder.plusMinusView.setImageResource(R.drawable.minusiconcust_profile);
        } else {
            contactViewHolder.lin2.setVisibility(View.GONE);
                   contactViewHolder.lin2.startAnimation(animationUp);
                   contactViewHolder.plusMinusView.setImageResource(R.drawable.pluscust_profile);
        }

        if (lastCheckedPosition == -1) {
            contactViewHolder.lin2.setVisibility(View.GONE);
            contactViewHolder.lin2.startAnimation(animationUp);
            contactViewHolder.plusMinusView.setImageResource(R.drawable.pluscust_profile);
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.customer_info_main_adapter, viewGroup, false);
        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView c_name;
        protected TextView c_name2;
        protected TextView c_address;
        protected TextView c_credit_profile;
        protected TextView c_ammount1;
        protected TextView c_ammount2;
        protected TextView c_beat_name;
        protected TextView c_city_name;
        protected TextView c_latlon;
        protected TextView c_mobile_number;
        protected TextView cus_code;
        protected TextView c_business_type;
        protected ImageView c_call;
        protected ImageView c_location, payment, outstanding_overdue,plusMinusView,arroew,topimg,bottomimg;
        protected RelativeLayout L1;
        protected RelativeLayout lin2;

        public ContactViewHolder(View v) {
            super(v);

            L1 = v.findViewById(R.id.L1);
            lin2 = v.findViewById(R.id.lin2);
            lin2.setVisibility(View.GONE);
            c_name =  (TextView) v.findViewById(R.id.c_name);
            c_name2 =  (TextView) v.findViewById(R.id.c_name2);
            c_address = (TextView)  v.findViewById(R.id.c_address);
            c_credit_profile = (TextView)  v.findViewById(R.id.c_credit_profile);
            c_ammount1 = (TextView) v.findViewById(R.id.c_ammount1);
            c_ammount2 = (TextView) v.findViewById(R.id.c_ammount2);
            c_latlon = (TextView) v.findViewById(R.id.c_latlon);
            c_mobile_number = (TextView) v.findViewById(R.id.c_mobile_number);
            cus_code = (TextView) v.findViewById(R.id.cus_code);
            c_beat_name = (TextView) v.findViewById(R.id.c_beat_name);
            c_call = (ImageView) v.findViewById(R.id.c_call);
            c_city_name = (TextView) v.findViewById(R.id.c_city_name);
            c_business_type = (TextView) v.findViewById(R.id.c_business_type);
            c_location = (ImageView) v.findViewById(R.id.c_location);
            payment = (ImageView) v.findViewById(R.id.payment);
            outstanding_overdue = (ImageView) v.findViewById(R.id.outstanding_overdue);
            plusMinusView = (ImageView) v.findViewById(R.id.plusminus_view);
            bottomimg = (ImageView) v.findViewById(R.id.bottomimg);
            topimg = (ImageView) v.findViewById(R.id.topimg);
            arroew = (ImageView) v.findViewById(R.id.arroew);

            payment.setOnClickListener(this);
            c_call.setOnClickListener(this);
            c_location.setOnClickListener(this);
            outstanding_overdue.setOnClickListener(this);
        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            cd = new ConnectionDetector(view.getContext());
            isInternetPresent = cd.isConnectingToInternet();

            if (view.getId() == c_location.getId()) {
                String s = "";
                String latlong = c_latlon.getText().toString().trim();
                String address = c_address.getText().toString().trim();
                // Toast.makeText(view.getContext(), s.trim(), Toast.LENGTH_SHORT).show();
                Log.d("latlong", "latlong" + latlong);

                if (isInternetPresent) {
                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(latlong) && !(latlong.equalsIgnoreCase("0.0,0.0")) && !(latlong.equalsIgnoreCase(","))) {

//                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                                Uri.parse("http://maps.google.com/maps?saddr=" + Global_Data.GLOvel_LATITUDE + "," + Global_Data.GLOvel_LONGITUDE + "&daddr=" + latlong + ""));
//                        ((Activity) view.getContext()).startActivity(intent);

                        try {
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(address)) {
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?saddr=" + Global_Data.GLOvel_LATITUDE + "," + Global_Data.GLOvel_LONGITUDE + "&daddr=" + address + ""));
                                ((Activity) view.getContext()).startActivity(intent);
                            } else {
                                Global_Data.Custom_Toast(mcontext, "Address not found","");
                               // Toast.makeText(mcontext, "Address not found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    } else {
                        //Toast.makeText(mcontext, "Wait..", Toast.LENGTH_SHORT).show();
//                        GeocodingLocation locationAddress = new GeocodingLocation();
//                        locationAddress.getAddressFromLocation(address,
//                                (Activity) view.getContext(), new GeocoderHandler());

                        try {
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(address)) {
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?saddr=" + Global_Data.GLOvel_LATITUDE + "," + Global_Data.GLOvel_LONGITUDE + "&daddr=" + address + ""));
                                ((Activity) view.getContext()).startActivity(intent);
                            } else {
                                Global_Data.Custom_Toast(mcontext, "Address not found","");
                               // Toast.makeText(mcontext, "Address not found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        // Toast.makeText((Activity)view.getContext(), "Customer location not found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Global_Data.Custom_Toast((Activity) view.getContext(), view.getContext().getResources().getString(R.string.internet_connection_error),"Yes");
//                    Toast toast = Toast.makeText((Activity) view.getContext(), view.getContext().getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                }
            }
            else if (view.getId() == c_call.getId())
            {
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(c_mobile_number.getText().toString().trim()))
                {
                    requestPhoneCallPermission(c_mobile_number.getText().toString().trim());
                }
            } else if (view.getId() == payment.getId()) {
                // c_ammount1.getText().toString().trim();
                //Toast.makeText(mcontext, "amount"+Global_Data.amt_outstanding, Toast.LENGTH_SHORT).show();
                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(c_ammount1.getText().toString().trim()) && !c_ammount1.getText().toString().trim().equalsIgnoreCase("Amount Outstanding Not Found")) {
                    Intent i = new Intent(mcontext, Cash_Collect.class);
                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(c_name.getText().toString().trim())) {
                        i.putExtra("CUS_NAME", c_name.getText().toString().trim());

                    } else {
                        i.putExtra("CUS_NAME", "");
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(c_ammount1.getText().toString())) {

                        String ss_amounts = "";
                        if (c_ammount1.getText().toString().trim().indexOf(":") > 0) {
                            String sss[] = c_ammount1.getText().toString().trim().split(":");
                            if(sss.length >1)
                            {
                                ss_amounts = sss[1];
                                ss_amountsn = sss[1];
                            }
                            else
                            {
                                ss_amountsn = "";
                            }

                        }
                        i.putExtra("OUT_STANDING", ss_amounts);
                    } else {
                        i.putExtra("OUT_STANDING", "");
                    }

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(c_ammount2.getText().toString().trim()) && !c_ammount2.getText().toString().trim().equalsIgnoreCase("Amount Overdue Not Found")) {

                        String ss_amounts = "";
                        if (c_ammount2.getText().toString().trim().indexOf(":") > 0) {
                            String sss[] = c_ammount2.getText().toString().trim().split(":");
                            if(sss.length >1)
                            {
                                ss_amounts = sss[1];
                            }

                        }

                        i.putExtra("OVER_DUE", ss_amounts);
                    } else {
                        i.putExtra("OVER_DUE", "");
                    }


                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cus_code.getText().toString().trim())) {
                        i.putExtra("CUS_CODE", cus_code.getText().toString().trim());
                    } else {
                        i.putExtra("CUS_CODE", "");
                    }
                    DataBaseHelper dbvoc = new DataBaseHelper(mcontext);
                    AutoCompleteTextView v_auto = (AutoCompleteTextView) ((Activity) mcontext).findViewById(R.id.autoCompleteTextView1);
                    List<Local_Data> contacts = dbvoc.getCustomerCode(v_auto.getText().toString().trim());

                    if (contacts.size() > 0) {
                        Global_Data.Globelo_OU_CUST_name = v_auto.getText().toString().trim();
                    } else {
                        Global_Data.Globelo_OU_CUST_name = "";
                    }

//                    mcontext.startActivity(i);
//                       ((Activity) mcontext).finish();

                    //i.putExtra("CUS_NAME", c_name.getText().toString().trim());

                    //cit

                    if(ss_amountsn.equalsIgnoreCase(""))
                    {
                        Global_Data.Custom_Toast(mcontext, "Outstanding amount not found.","");
                      //  Toast.makeText(mcontext, "Outstanding amount not found.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        mcontext.startActivity(i);
                        ((Activity) mcontext).finish();
                    }

                } else {
                    ss_amountsn = "";
                    Global_Data.Custom_Toast(mcontext, "Outstanding amount not found.","");
                   // Toast.makeText(mcontext, "Outstanding amount not found.", Toast.LENGTH_SHORT).show();
                }
            }else if (view.getId() == outstanding_overdue.getId())
            {
                Intent m = new Intent(mcontext, OutstandingOverdueActivity.class);
                m.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               // m.putExtra("ANN_ID", Ann_Model.getAnn_id());
//                m.putExtra("REGION_ID", list.getDescription());
                mcontext.startActivity(m);

//                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(c_mobile_number.getText().toString().trim()))
//                {
//                    requestPhoneCallPermission(c_mobile_number.getText().toString().trim());
//                }
            }
        }
    }

    private static class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }

            if(locationAddress.equalsIgnoreCase("Location not found for this address"))
            {
                Global_Data.Custom_Toast(mcontext, "Customer location not found.","");
              //  Toast.makeText(mcontext, "Customer location not found.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr="+Global_Data.GLOvel_LATITUDE+","+Global_Data.GLOvel_LONGITUDE+"&daddr="+locationAddress+""));
                ((Activity)mcontext).startActivity(intent);
            }
        }
    }

    public static void requestPhoneCallPermission(final String mobile_number) {
        Dexter.withActivity((Activity) mcontext)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + mobile_number));
                        mcontext.startActivity(callIntent);

                        return;

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
    private static void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle(mcontext.getResources().getString(R.string.need_permission_message));
        builder.setCancelable(false);
        builder.setMessage(mcontext.getResources().getString(R.string.need_permission_setting_message));
        builder.setPositiveButton(mcontext.getResources().getString(R.string.GOTO_SETTINGS), new DialogInterface.OnClickListener() {
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
    private static void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mcontext.getPackageName(), null);
        intent.setData(uri);
        ((Activity) mcontext).startActivityForResult(intent, 101);
    }
}
