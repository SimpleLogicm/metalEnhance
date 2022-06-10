package com.msimplelogic.activities;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
//import androidx.work.ExistingPeriodicWorkPolicy;
//import androidx.work.PeriodicWorkRequest;
//import androidx.work.WorkManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.JsonToken;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.jsibbold.zoomage.ZoomageView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.activities.kotlinFiles.Invoice_order;
import com.msimplelogic.activities.kotlinFiles.Visitlog;
import com.msimplelogic.helper.MultipartUtility;
//import com.msimplelogic.helper.MyPeriodicwork;
import com.msimplelogic.services.getServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static com.msimplelogic.activities.Config.NOTIFICATION_ID_NEW;

public class Visitlogsubmit extends AppCompatActivity {
    Toolbar toolbar;
    Button btnsubmit;
    ZoomageView imgdialog;
    String order_image_url;
    String serviceurlsubmit;
    Context context;
    private String pictureImagePath = "";
    private String mCurrentPhotoPath = "";
    ImageView crossimg, img_show;
    LinearLayout uploadephoto;
    String image_check = "";
    ProgressDialog dialog;
    String response_result = "";
    Boolean B_flag;
    String serviceurl;
    Spinner Logoutspin, visittypespin;
    EditText editText_description;
    ArrayAdapter<String> adapter_visittype, adaptor_hour;
    private ArrayList<String> results = new ArrayList<String>();
    private ArrayList<String> results2 = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitlogsubmit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        context = Visitlogsubmit.this;

        editText_description = findViewById(R.id.editText_description);
        btnsubmit = findViewById(R.id.btnsubmit);
        crossimg = findViewById(R.id.crossimg);
        img_show = findViewById(R.id.img_show);
        uploadephoto = findViewById(R.id.uploadephoto);
        Logoutspin = findViewById(R.id.Logoutspin);
        visittypespin = findViewById(R.id.visittypespin);

        results.clear();
        results.add("Select Visit Type");
        results2.clear();
        results2.add("Select Chek-out Time");
        results2.add("15 min");
        results2.add("30 min");
        results2.add("1 hour");
        results2.add("2 hour");
        results2.add("3 hour");

//        SharedPreferences spf = getSharedPreferences("SimpleLogic", 0);
//        String time = spf.getString("Logouthour", null);
//
//        if (time != null) {
//            try {
//                PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
//                        MyPeriodicwork.class, Long.parseLong(time) * 60, TimeUnit.MINUTES
//                ).addTag("otpValidator").build();
//                WorkManager.getInstance().enqueueUniquePeriodicWork("Work",
//                        ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//
//        }


        visitdorpdown();


        adaptor_hour = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, results2);
        adaptor_hour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Logoutspin.setAdapter(adaptor_hour);


//        uploadephoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                requestStoragePermission();
//
//            }
//        });


        img_show = findViewById(R.id.img_show);
        crossimg = findViewById(R.id.crossimg);
        crossimg.setVisibility(View.INVISIBLE);

        crossimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletedialog();
            }
        });

        img_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentPhotoPath != "") {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Visitlogsubmit.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.dialogeimage, null);
                    imgdialog = dialogLayout.findViewById(R.id.imageView);
                    Glide.with(Visitlogsubmit.this).load(mCurrentPhotoPath).into(imgdialog);
                    builder.setPositiveButton("OK", null);
                    builder.setView(dialogLayout);
                    builder.show();
                }
            }
        });


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (visittypespin.getSelectedItem().toString()
                        .equalsIgnoreCase("Select Visit Type")) {

                    Global_Data.Custom_Toast(Visitlogsubmit.this,
                            getResources().getString(R.string.select_visittype), "yes");

                } else if(editText_description.getText().toString().equalsIgnoreCase("")){
                    Global_Data.Custom_Toast(Visitlogsubmit.this,
                            "Please Enter Description", "yes");
//                }
//
//
//                 else if (pictureImagePath.equalsIgnoreCase("")) {
//                    Global_Data.Custom_Toast(Visitlogsubmit.this,
//                            "Please Capture photo or select from gallery", "yes");

                } else {

                    SharedPreferences spf = Visitlogsubmit.this.getSharedPreferences("SimpleLogic", 0);
                    String user_email = spf.getString("USER_EMAIL", null);
                    String datetime = spf.getString("Datein", null);

                    SharedPreferences spf2 = Visitlogsubmit.this.getSharedPreferences("SimpleLogic", 0);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("Logouthour", Logoutspin.getSelectedItem().toString());
                    editor.commit();

                    SharedPreferences spf4 = getSharedPreferences("SimpleLogic", 0);
                    String time = spf4.getString("Logouthour", null);


                    dialog = new ProgressDialog(Visitlogsubmit.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    dialog.setMessage(Visitlogsubmit.this.getResources().getString(R.string.data_Sync_in_Progress));
                    dialog.setTitle(Visitlogsubmit.this.getResources().getString(R.string.app_name));
                    dialog.setCancelable(false);
                    dialog.show();

                    if (Logoutspin.getSelectedItem().toString()
                            .equalsIgnoreCase("Select Chek-out Time")) {

                        new Submitdetails().execute(pictureImagePath, user_email, datetime, Global_Data.Instatus, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.address, visittypespin.getSelectedItem().toString(), editText_description.getText().toString(), "0 hours");


                    }else {
                        new Submitdetails().execute(pictureImagePath, user_email, datetime, Global_Data.Instatus, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.address, visittypespin.getSelectedItem().toString(), editText_description.getText().toString(), Logoutspin.getSelectedItem().toString());

                    }

                    // submitdetails(editText_description.getText().toString(),user_email,datetime,Global_Data.Instatus,Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE,Global_Data.address,visittypespin.getSelectedItem().toString());

                }

            }
        });


    }


    public class Submitdetails extends AsyncTask<String, Void, String> {


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
            SharedPreferences spf = Visitlogsubmit.this.getSharedPreferences("SimpleLogic", 0);
            String user_email22 = spf.getString("USER_EMAIL", null);

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a yyyy-MM-dd");
            DateFormat date_only = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String daten = sdf.format(date);
            String daten2 = date_only.format(date);
            String datenn = sdf.format(date);


            try {
                String charset = "UTF-8";
                String domain = context.getResources().getString(R.string.service_domain);
                try {
                    final MultipartUtility multipart = new MultipartUtility(domain + "visit_logs/create?email=" + URLEncoder.encode(user_email22, "UTF-8"), charset);

                    if (params[0].length() > 0) {
                        Uri uri1 = Uri.parse(params[0]);
                        final File file1 = new File(uri1.getPath());
                        multipart.addFilePart("attachment", file1);
                    } else {
                        // multipart.addFilePart("expenses_travel_image", null);
                    }

                    multipart.addFormField("email", user_email22);

                    String useremail = params[1];
                    String datetime = params[2];
                    String Instatus = params[3];
                    String lattitude = params[4];
                    String longitude = params[5];
                    String address = params[6];
                    String visittype = params[7];
                    String discription = params[8];
                    String logouttime = params[9];

//                    if (logouttime.equalsIgnoreCase("Select Chek-out Time")){
//                        logouttime= params[10];
//
//                    }

                    if (datetime.length() > 0) {
                        multipart.addFormField("check_in_time", datetime);
                    }

                    if (Instatus.length() > 0) {
                        multipart.addFormField("status", Instatus);
                    }

                    if (lattitude.length() > 0) {
                        multipart.addFormField("check_in_latitude", lattitude);
                    }

                    if (longitude.length() > 0) {
                        multipart.addFormField("check_in_longitude", longitude);
                    }

                    if (address.length() > 0) {
                        multipart.addFormField("check_in_address", address);
                    }

                    if (visittype.length() > 0) {
                        multipart.addFormField("visit_name", visittype.trim());
                    }
                    if (discription.length() > 0) {
                        multipart.addFormField("description", discription.trim());
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
                                                Global_Data.Custom_Toast(context, "VisitLog Saved Successfully", "yes");

                                                int requestID = (int) System.currentTimeMillis();
//
//                                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                                                String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
//
//                                                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(Visitlogsubmit.this, NOTIFICATION_CHANNEL_ID);
////                                                Intent intent = new Intent(Visitlogsubmit.this, Visitlog.class);
////                                                intent.putExtra("Notification_intent", "SHOW");
//                                                // intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                // PendingIntent pendingIntent = PendingIntent.getActivity(Visitlogsubmit.this, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                                                notificationBuilder.setContentIntent(pendingIntent);
//                                                notificationBuilder.setAutoCancel(true)
//                                                        .setDefaults(Notification.DEFAULT_ALL)
//                                                        .setWhen(System.currentTimeMillis())
//                                                        .setSmallIcon(R.drawable.metal_pro)
//                                                        .setTicker("Metal Pro")
//                                                        //.setPriority(Notification.PRIORITY_MAX)
//                                                        .setContentTitle("Visit Log Chek-Out Notification")
//                                                        .setContentText("Your expected check-out time is " + logouttime + " hours")
//                                                        .setContentInfo("Information");
//                                                notificationManager.notify(1, notificationBuilder.build());


                                                Intent intent = new Intent(Visitlogsubmit.this, Visitlog.class);
                                                intent.putExtra("Notification_intent", "SHOW");
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                                                PendingIntent pendingIntent = PendingIntent.getActivity(Visitlogsubmit.this, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT);


//                                                val pendingIntent = PendingIntent.getActivity(
//                                                        context,
//                                                        0,
//                                                        intent,
//                                                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT
//                                                )

                                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, LoginActivity.CHANNEL_ID)
                                                        .setSmallIcon(com.msimplelogic.activities.R.drawable.ic_notification)
                                                        .setColor(Color.BLUE)
                                                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), com.msimplelogic.activities.R.drawable.metal_pro))
                                                        .setTicker("Metal Pro")
                                                        .setContentIntent(pendingIntent)
                                                        .setContentTitle("Visit Log Chek-Out Notification")
                                                        .setContentText("Your expected check-out time is " + logouttime)
                                                        .setContentInfo("Information")
                                                        .setAutoCancel(true)
                                                        .setContentIntent(pendingIntent)
                                                        .addAction(R.drawable.metal_pro, "Click for Check-out", pendingIntent);
                                                // .setPriority(NotificationCompat.PRIORITY_HIGH)

//                                                RingtoneManager alarmSound = new RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//                                                mBuilder.setSound(alarmSound);
                                                NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
                                                // notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL

                                                mNotificationMgr.notify(NOTIFICATION_ID_NEW, mBuilder.build());


//                                                String settings = context.getSharedPreferences("SimpleLogic", 0)
//                                                settings.edit().remove("Dateout").commit()
                                                SharedPreferences settings = context.getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
                                                settings.edit().remove("Dateout").commit();

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

    private void visitdorpdown() {


        //   final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //making the progressbar visible
        // progressBar.setVisibility(View.VISIBLE);


        SharedPreferences spf = Visitlogsubmit.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

        String domain = getResources().getString(R.string.service_domain);
        String domain1 = getResources().getString(R.string.service_domain1);


        try {
            serviceurl = domain + "visit_types/display_visit_type_details?email=" + URLEncoder.encode(user_email, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.e("service", "service" + serviceurl);

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serviceurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        //progressBar.setVisibility(View.INVISIBLE);


                        try {

                            if (response.contains("result")) {

                                Global_Data.Custom_Toast(Visitlogsubmit.this, "Visit Type Not Found", "yes");

                            } else {
                                results.clear();
                                results.add("Select Visit Type");

                                //getting the whole json object from the response
                                JSONObject obj = new JSONObject(response);

                                //we have the array named hero inside the object
                                //so here we are getting that json array
                                JSONArray heroArray = obj.getJSONArray("visit");

                                //now looping through all the elements of the json array
                                for (int i = 0; i < heroArray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject heroObject = heroArray.getJSONObject(i);

                                    String vistname = heroObject.getString("visit_name");

                                    results.add(vistname);

                                    //creating a hero object and giving them the values from json object
                                    //     Hero hero = new Hero(heroObject.getString("name"), heroObject.getString("imageurl"));

                                    //adding the hero to herolist
                                    // heroList.add(hero);
                                }

                                adapter_visittype = new ArrayAdapter<String>(Visitlogsubmit.this, android.R.layout.simple_spinner_item, results);
                                adapter_visittype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                visittypespin.setAdapter(adapter_visittype);


                            }


                            //creating custom adapter object
//                            ListViewAdapter adapter = new ListViewAdapter(heroList, getApplicationContext());
//
//                            //adding the adapter to listview
//                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(Visitlogsubmit.this, getResources().getString(R.string.Server_Error), "yes");

                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // queue.add(jsObjRequest);
        stringRequest.setShouldCache(false);
        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "Visitlog";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Visit_log");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        pictureImagePath = "file:" + image.getAbsolutePath();

        mCurrentPhotoPath = image.getAbsolutePath();

        Log.i("imagepath", "" + pictureImagePath);
        Log.i("imagepath", "" + mCurrentPhotoPath);

        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void requestStoragePermission() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            B_flag = isDeviceSupportCamera();

                            if (B_flag == true) {


                                final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Choose_from_Gallery), getResources().getString(R.string.Cancel)};


                                AlertDialog.Builder builder = new AlertDialog.Builder(Visitlogsubmit.this);

                                builder.setTitle(getResources().getString(R.string.Add_Photo));

                                builder.setItems(options, new DialogInterface.OnClickListener() {

                                    @Override

                                    public void onClick(DialogInterface dialog, int item) {

                                        if (options[item].equals(getResources().getString(R.string.Take_Photo))) {
                                            image_check = "photo";

                                            File photoFile = null;
                                            try {
                                                photoFile = createImageFile();
                                            } catch (IOException ex) {
                                                // Error occurred while creating the File
                                                Log.i("Image TAG", "IOException");
                                                pictureImagePath = "";
                                            }

                                            Uri outputFileUri = Uri.fromFile(photoFile);

                                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                                            startActivityForResult(cameraIntent, 1);

//                                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                                            //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                                            Uri photoURI = FileProvider.getUriForFile(Visitlogsubmit.this,
//                                                    BuildConfig.APPLICATION_ID + ".provider",
//                                                    photoFile);
//                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                                            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                                            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
//                                            for (ResolveInfo resolveInfo : resInfoList) {
//                                                String packageName = resolveInfo.activityInfo.packageName;
//                                                context.grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                            }
//
//                                            startActivityForResult(cameraIntent, 1);

                                        } else if (options[item].equals(getResources().getString(R.string.Choose_from_Gallery))) {

                                            image_check = "gallery";
                                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                            startActivityForResult(intent, 2);


                                        } else if (options[item].equals(getResources().getString(R.string.Cancel))) {

                                            dialog.dismiss();

                                        }

                                    }

                                });

                                builder.show();


                            } else {
                                // Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_camera), Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.no_camera), "");
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
                        //   Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            //if (requestCode == 1 && resultCode == RESULT_OK) {


            File imgFils = new File(mCurrentPhotoPath);
            if (imgFils.exists()) {

                img_show = findViewById(R.id.img_show);
                crossimg.setVisibility(View.VISIBLE);
                img_show.setVisibility(View.VISIBLE);
                //img_show.setRotation((float) 90.0);
                Glide.with(Visitlogsubmit.this).load(mCurrentPhotoPath).into(img_show);
                //img_show.setImageURI(Uri.fromFile(imgFils));
            }

            //new Expenses.LongOperation().execute();

//			try {
//
//				//dbvoc.updateORDER_order_image(pictureImagePath, Global_Data.GLObalOrder_id);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

        } else if (requestCode == 2 && resultCode == RESULT_OK) {

            try {
                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                pictureImagePath = "file:" + c.getString(columnIndex);
                mCurrentPhotoPath = c.getString(columnIndex);
                File imgFils = new File(mCurrentPhotoPath);
                if (imgFils.exists()) {

                    img_show = findViewById(R.id.img_show);
                    crossimg.setVisibility(View.VISIBLE);
                    img_show.setVisibility(View.VISIBLE);
                    Glide.with(Visitlogsubmit.this).load(mCurrentPhotoPath).into(img_show);
                    //img_show.setImageURI(Uri.fromFile(imgFils));
                }

                c.close();

                //new Expenses.LongOperation().execute();
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Visitlogsubmit.this);
        builder.setTitle(getResources().getString(R.string.need_permission_message));
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.need_permission_setting_message));
        builder.setPositiveButton(getResources().getString(R.string.GOTO_SETTINGS), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
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
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void submitdetails(String discription, String user_email, String datetime, String instatus, String GLOvel_LATITUDE, String GLOvel_LONGITUDE, String address, String s) {

        JSONObject main = new JSONObject();
        JSONObject content = new JSONObject();
        byte b5[];

        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(pictureImagePath)) {
            order_image_url = pictureImagePath.trim();
            // File filepath = new File(cn.getimg_ordersign());
            // String  path =  "file://"+filepath.getPath();
            try {
                Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(pictureImagePath));
                ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
                mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
                b5 = bos5.toByteArray();

                String getsign_str = Base64.encodeToString(b5, Base64.DEFAULT);
                content.put("attachment", getsign_str);
                content.put("check_in_time", datetime);
                content.put("check_in_latitude", GLOvel_LATITUDE);
                content.put("check_in_longitude", GLOvel_LONGITUDE);
                content.put("status", Global_Data.Instatus);
                content.put("description", discription);
                content.put("check_in_address", address);
                content.put("visit_name", s);
                // other_image_path = cn.getimg_ordersign();

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    content.put("attachment", "");
                    content.put("check_in_time", datetime);
                    content.put("check_in_latitude", GLOvel_LATITUDE);
                    content.put("check_in_longitude", GLOvel_LONGITUDE);
                    content.put("status", Global_Data.Instatus);
                    content.put("description", discription);
                    content.put("check_in_address", address);
                    content.put("visit_name", s);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }


        } else {
            try {

                content.put("attachment", "");
                content.put("check_in_time", datetime);
                content.put("check_in_latitude", GLOvel_LATITUDE);
                content.put("check_in_longitude", GLOvel_LONGITUDE);
                content.put("status", Global_Data.Instatus);
                content.put("description", discription);
                content.put("check_in_address", address);
                content.put("visit_name", s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


//        try {
//           // main.put("email",user_email);
//          //  main.put("visit_log",content);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        SharedPreferences spf = Visitlogsubmit.this.getSharedPreferences("SimpleLogic", 0);
        String domain = getResources().getString(R.string.service_domain);

        try {
            serviceurlsubmit = domain + "visit_logs/create?email=" + URLEncoder.encode(user_email, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.i("serviceirl", "serviceurl" + serviceurlsubmit + main.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, serviceurlsubmit, content, new Response.Listener<JSONObject>() {
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
                            Global_Data.Custom_Toast(context, "Data submit", "");
                            dialog.dismiss();
                        }
                    });

                    //sendSMS("8454858739",sms_body,context);

                    //Uri uri = Uri.parse("file://"+Environment.getExternalStorageDirectory()+"/test.png");
                    //sendLongSMS("8454858739",sms_body,context);


                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();


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
                String targetNew = "";
                SharedPreferences sp = Visitlogsubmit.this.getSharedPreferences("SimpleLogic", 0);
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

    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    private void deletedialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(Visitlogsubmit.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Warning));
        alertDialog.setMessage(getResources().getString(R.string.image_dialog_warning_message));
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                File file = new File(mCurrentPhotoPath);
                boolean delete = file.delete();

                File file1 = new File(pictureImagePath);
                boolean delete1 = file1.delete();

                img_show.setVisibility(View.INVISIBLE);
                crossimg.setVisibility(View.INVISIBLE);

//				Toast toast = Toast.makeText(Expenses.this, getResources().getString(R.string.image_dialog_delete_success),
//						Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.CENTER, 0, 0);
//				toast.show();

                Global_Data.Custom_Toast(Visitlogsubmit.this, getResources().getString(R.string.image_dialog_delete_success), "yes");

                //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        SharedPreferences settings = context.getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
        settings.edit().remove("Datein").commit();
        Intent i = new Intent(Visitlogsubmit.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();

    }
}