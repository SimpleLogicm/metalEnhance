package com.msimplelogic.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.msimplelogic.activities.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by vinod on 26-04-2017.
 */

public class Geo_Data extends Activity {

    TextView lati_val, bearin_val, longi_val, altitude_value, lo_Accuracy_value, speed_value;
    TextView distance_value, time_value, average_speed_value, average_pace_value;
    Button g_gps_status, g_reset;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    LocationManager manager;

    String formattedDatef = "";

    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geo_data);

        g_gps_status = (Button) findViewById(R.id.g_gps_status);
        g_reset = (Button) findViewById(R.id.g_reset);

        lati_val = (TextView) findViewById(R.id.lati_val);
        bearin_val = (TextView) findViewById(R.id.bearin_val);
        longi_val = (TextView) findViewById(R.id.longi_val);
        altitude_value = (TextView) findViewById(R.id.altitude_value);
        lo_Accuracy_value = (TextView) findViewById(R.id.lo_Accuracy_value);
        speed_value = (TextView) findViewById(R.id.speed_value);

        distance_value = (TextView) findViewById(R.id.distance_value);
        time_value = (TextView) findViewById(R.id.time_value);
        average_speed_value = (TextView) findViewById(R.id.average_speed_value);
        average_pace_value = (TextView) findViewById(R.id.average_pace_value);

        lati_val.setText(Global_Data.GLOvel_LATITUDE);
        longi_val.setText(Global_Data.GLOvel_LONGITUDE);
        bearin_val.setText(Global_Data.GLOvel_Bearing);
        altitude_value.setText(Global_Data.GLOvel_Altitude);
        lo_Accuracy_value.setText(Global_Data.GLOvel_Accuracy);
        speed_value.setText(Global_Data.GLOvel_Speed);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            g_gps_status.setText("GPS OFF");
        } else {
            g_gps_status.setText("GPS ON");
        }

        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText("Cumulative Data");

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            todaysTarget.setVisibility(View.INVISIBLE);
            ImageView H_LOGO = (ImageView) mCustomView.findViewById(R.id.Header_logo);
            SharedPreferences sp = Geo_Data.this.getSharedPreferences("SimpleLogic", 0);


//		if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//			todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//		}
//		if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)<0) {
////	        	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
//			todaysTarget.setText("Today's Target Acheived");
//		}

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        long i = 0;
        String f_lat = "";
        String f_long = "";
        Double final_distance = 0.0d;
        Double distance = 0.0d;
        Double final_time = 0.0d;

        int hours = 0;
        int minute = 0;
        int second = 0;

        int final_hours = 0;
        int final_minute = 0;
        int final_second = 0;

        int count_flag = 0;

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
        formattedDatef = dff.format(cal.getTime());


        dbvoc.getDelete_TRACK_MOVEMENT_new(formattedDatef);
        //List<Local_Data> contacts2 = dbvoc.get_TRACK_MOVEMENT();
        List<Local_Data> contacts2 = dbvoc.get_TRACK_MOVEMENT_ByCurrentDate(formattedDatef);
        for (Local_Data cn : contacts2) {

            try {


                if (i == 0) {
                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getlatitude()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getlongitude())) {

                        f_lat = cn.getlatitude();
                        f_long = cn.getlongitude();

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        Date d = df.parse(cn.getTrack_mov_datetime());
                        cal.setTime(d);
                        hours = cal.get(Calendar.HOUR_OF_DAY);
                        minute = cal.get(Calendar.MINUTE);
                        second = cal.get(Calendar.SECOND);


                        ++i;
                    }

                } else {
                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getlatitude()) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getlongitude())) {

                        ++count_flag;

                        final_distance += Double.valueOf(distFrom(Float.valueOf(f_lat), Float.valueOf(f_long), Float.valueOf(cn.getlatitude()), Float.valueOf(cn.getlongitude())));

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        Date d = df.parse(cn.getTrack_mov_datetime());
                        cal.setTime(d);
                        final_hours += cal.get(Calendar.HOUR_OF_DAY) - hours;
                        final_minute += cal.get(Calendar.MINUTE) - minute;
                        final_second += cal.get(Calendar.SECOND) - second;

                        hours = cal.get(Calendar.HOUR_OF_DAY);
                        minute = cal.get(Calendar.MINUTE);
                        second = cal.get(Calendar.SECOND);

                        f_lat = cn.getlatitude();
                        f_long = cn.getlongitude();

                    }

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        if (count_flag != 0) {
            final_time = Double.valueOf((final_hours) + (final_minute / 60) + final_second / 3600);
            distance_value.setText(String.valueOf(new DecimalFormat("###.###").format((final_distance / 1000))) + " " + "km");

            Double distancein_km = final_distance / 1000;

            time_value.setText(String.valueOf(final_hours + ":" + final_minute));
            average_speed_value.setText(String.valueOf(new DecimalFormat("###.###").format((distancein_km / final_time))) + " kph");
            average_pace_value.setText(String.valueOf(new DecimalFormat("###.###").format((60 * (final_time / distancein_km)))));
        } else {
            distance_value.setText("0");
            time_value.setText("0");
            average_speed_value.setText("0");
            average_pace_value.setText("0");
        }


        g_gps_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                }

            }
        });


        g_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbvoc.getDelete_TRACK_MOVEMENT(formattedDatef);

                distance_value.setText("");
                time_value.setText("");
                average_speed_value.setText("");
                average_pace_value.setText("");

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
    protected void onResume() {
        super.onResume();

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            g_gps_status.setText("GPS OFF");
        } else {
            g_gps_status.setText("GPS ON");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            // manager.
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        // super.onBackPressed();


        Intent i = new Intent(Geo_Data.this, BasicMapDemoActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        startActivity(i);
        finish();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
