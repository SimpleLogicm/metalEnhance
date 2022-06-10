package com.msimplelogic.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class CameraVideoActivity extends BaseActivity {
    private static final String TAG = CameraVideoActivity.class.getSimpleName();
    private static final String FILE_TYPE = "image/*";
    static String image_path = "";
    static String video_path = "";
    String video_code = "";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    String response_result = "";
    String filename = "";
    String image_url = "";
    String final_media_path = "";
    String RE_ID = "";
    Toolbar toolbar;
    ProgressDialog dialog;
    LoginDataBaseAdapter loginDataBaseAdapter;
    ConnectionDetector cd;
    Button btnSubmit;
    Boolean isInternetPresent = false;
    private String Current_Date = "";
    private String SCAN_PATH;
    private MediaScannerConnection conn;
    private String selectedPath;
    private Uri mImageCaptureUri;
    private Uri fileUri; // file url to store image/video
    private VideoView videoPreview;
    private String name, CP_NAME, RE_TEXT;
    Bitmap bitmap;
    String picturePath = "";
    String video_option = "";
    String image_option = "";
    List<String> C_Array = new ArrayList<String>();
    EditText discription;
    String media_coden = "";
    String media_text = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameravideo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        DataBaseHelper dbvoc = new DataBaseHelper(this);

        btnSubmit = (Button) findViewById(R.id.btn_submit);

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        dialog = new ProgressDialog(CameraVideoActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = sdf.format(c.getTime());
        Current_Date = sdf.format(c.getTime());
        cd = new ConnectionDetector(getApplicationContext());

        Intent i = getIntent();
        name = i.getStringExtra("retialer");
        CP_NAME = i.getStringExtra("CP_NAME");
        RE_TEXT = i.getStringExtra("RE_TEXT");

        Global_Data.Default_Image_Path = "";
        Global_Data.Default_video_Path = "";


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String formattedDate = df.format(c.getTime());

                SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
                final String formattedDatef = dff.format(c.getTime());

                List<Local_Data> contacts = dbvoc.getRetailer(RE_TEXT);
                for (Local_Data cn : contacts) {

                    RE_ID = cn.get_Retailer_id();
                }

                if (CP_NAME.equals("video") || CP_NAME.equals("Image")) {

                    try {
                        if (CP_NAME.equals("video")) {

                            if (video_option.equalsIgnoreCase("Gallery") && (selectedPath == null || selectedPath.equals(""))) {
                                //Toast.makeText(getApplicationContext(), "Please Capture Media First", Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Video_First),"Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Video_First), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
                            } else if(fileUri.getPath() == null || fileUri.getPath().equals("")) {
                                //Toast.makeText(getApplicationContext(), "Please Capture Media First", Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Video_First),"Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Video_First), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
                            } else if (discription.getText().toString() == null || discription.getText().toString().equals("")) {
                                //Toast.makeText(getApplicationContext(), "Please Enter Description", Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Enter_Description),"Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Enter_Description), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

                            } else {

                                //Toast.makeText(getApplicationContext(), discription.getText().toString(), Toast.LENGTH_LONG).show();

                                //image_url = getStringImage(bitmap);
                                //new UploadFileToServer().execute();

                                //executeMultipartPost();LoadDatabaseAsyncTask
                                media_text = discription.getText().toString();

                                if (video_option.equalsIgnoreCase("Gallery")) {
                                    final_media_path = selectedPath;
                                } else {
                                    final_media_path = fileUri.getPath();
                                }

                                filename = final_media_path.substring(final_media_path.lastIndexOf("/") + 1);


                                AlertDialog alertDialog = new AlertDialog.Builder(CameraVideoActivity.this).create(); //Read Update

                                alertDialog.setMessage(getResources().getString(R.string.Offline_dialog_message));
//}

                                alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Save_Offline), new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogn, int which) {
                                        // TODO Auto-generated method stub
                                        try
                                        {
                                            AppLocationManager appLocationManager = new AppLocationManager(CameraVideoActivity.this);
                                            Log.d("Class LAT LOG","Class LAT LOG"+appLocationManager.getLatitude()+" "+ appLocationManager.getLongitude());
                                            Log.d("Service LAT LOG","Service LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);
                                            PlayService_Location PlayServiceManager = new PlayService_Location(CameraVideoActivity.this);

                                            if(PlayServiceManager.checkPlayServices(CameraVideoActivity.this))
                                            {
                                                Log.d("Play LAT LOG","Play LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);

                                            }
                                            else
                                            if(!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)  && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null))
                                            {
                                                Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                                Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                            }

                                        }catch(Exception ex){ex.printStackTrace();}


                                        Long randomPIN = System.currentTimeMillis();
                                        String PINString = String.valueOf(randomPIN);

                                        SharedPreferences spf = CameraVideoActivity.this.getSharedPreferences("SimpleLogic", 0);
                                        String user_email = spf.getString("USER_EMAIL",null);

                                        loginDataBaseAdapter.insertCustomerServiceMedia("",Global_Data.GLOvel_CUSTOMER_ID,CP_NAME, RE_ID, user_email,
                                                "", discription.getText().toString(),Current_Date, Current_Date,Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE,Global_Data.Default_video_Path,PINString);

                                        loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "VIDEO", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                        Intent intent = new Intent(CameraVideoActivity.this, Neworderoptions.class);
                                        startActivity(intent);
                                        finish();

                                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Save_Successfully),"Yes");
//										Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Save_Successfully), Toast.LENGTH_LONG);
//										toast.setGravity(Gravity.CENTER, 0, 0);
//										toast.show();

                                        dialogn.cancel();

                                    }
                                });

                                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.Online_Sync), new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogn, int which) {


                                        isInternetPresent = cd.isConnectingToInternet();
                                        if (isInternetPresent)
                                        {
                                            btnSubmit.setClickable(false);
                                            btnSubmit.setEnabled(false);

                                            response_result = "";

                                            runOnUiThread(new Runnable()
                                            {
                                                public void run()
                                                {
                                                    dialog = new ProgressDialog(CameraVideoActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                                    dialog.setMessage(getResources().getString(R.string.Please_Wait));
                                                    dialog.setTitle(getResources().getString(R.string.app_name));
                                                    dialog.setCancelable(false);
                                                    dialog.show();
                                                }
                                            });

                                            SharedPreferences spf = CameraVideoActivity.this.getSharedPreferences("SimpleLogic", 0);
                                            String user_email = spf.getString("USER_EMAIL",null);

                                            //media_coden = encodeVideoFile(final_media_path);

                                            SyncMediaData(CP_NAME, video_code, discription.getText().toString(), Global_Data.GLOvel_CUSTOMER_ID, user_email, filename);

//									new Mediaperation().execute(CP_NAME, video_code, discription.getText().toString(), Global_Data.GLOvel_CUSTOMER_ID, user_email, filename);
                                        }
                                        else
                                        {
                                            try
                                            {
                                                AppLocationManager appLocationManager = new AppLocationManager(CameraVideoActivity.this);
                                                Log.d("Class LAT LOG","Class LAT LOG"+appLocationManager.getLatitude()+" "+ appLocationManager.getLongitude());
                                                Log.d("Service LAT LOG","Service LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);
                                                PlayService_Location PlayServiceManager = new PlayService_Location(CameraVideoActivity.this);

                                                if(PlayServiceManager.checkPlayServices(CameraVideoActivity.this))
                                                {
                                                    Log.d("Play LAT LOG","Play LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);

                                                }
                                                else
                                                if(!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)  && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null))
                                                {
                                                    Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                                    Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                                }
                                            }catch(Exception ex){ex.printStackTrace();}

                                            Long randomPIN = System.currentTimeMillis();
                                            String PINString = String.valueOf(randomPIN);

                                            SharedPreferences spf = CameraVideoActivity.this.getSharedPreferences("SimpleLogic", 0);
                                            String user_email = spf.getString("USER_EMAIL",null);

                                            loginDataBaseAdapter.insertCustomerServiceMedia("",Global_Data.GLOvel_CUSTOMER_ID,CP_NAME, RE_ID, user_email,
                                                    "", discription.getText().toString(),Current_Date, Current_Date,Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE,Global_Data.Default_video_Path,PINString);

                                            loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "VIDEO", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Offline_save_message),"Yes");
//											Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Offline_save_message), Toast.LENGTH_LONG);
//											toast.setGravity(Gravity.CENTER, 0, 0);
//											toast.show();

                                            Intent intent = new Intent(CameraVideoActivity.this, Neworderoptions.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            finish();
                                        }
                                        dialogn.cancel();
                                    }
                                });

                                alertDialog.setCancelable(false);
                                alertDialog.show();

                                //new LoadDatabaseAsyncTask().execute();
                                //uploadFile();

                            }

                        } else {
                            if (image_option.equalsIgnoreCase("Gallery") && (picturePath == null || picturePath.equals(""))) {
                                //	Toast.makeText(getApplicationContext(), "Please Capture Media First", Toast.LENGTH_LONG).show();

                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First),"Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

                            } else if (fileUri.getPath() == null || fileUri.getPath().equals("") || fileUri.equals("")) {
                                //Toast.makeText(getApplicationContext(), "Please Capture Media First", Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First),"Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

                            } else if (discription.getText().toString() == null || discription.getText().toString().equals("")) {
                                //Toast.makeText(getApplicationContext(), "Please Enter Description", Toast.LENGTH_LONG).show();

                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Enter_Description),"Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Enter_Description), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
                            } else {

                                //Toast.makeText(getApplicationContext(), discription.getText().toString(), Toast.LENGTH_LONG).show();
                                image_url = "";


//									loginDataBaseAdapter.insertCustomerServiceMedia("",Global_Data.GLOvel_CUSTOMER_ID,CP_NAME,Global_Data.GLOvel_CUSTOMER_ID,Global_Data.GLOvel_USER_EMAIL,
//											image_url,discription.getText().toString(), Current_Date, Current_Date);
//									//get_dialogC();


                                media_text = discription.getText().toString();
                                if (image_option.equalsIgnoreCase("Gallery")) {
                                    final_media_path = picturePath;
                                } else {
                                    final_media_path = fileUri.getPath();
                                }

                                filename = final_media_path.substring(final_media_path.lastIndexOf("/") + 1);

                                AlertDialog alertDialog = new AlertDialog.Builder(CameraVideoActivity.this).create(); //Read Update

                                alertDialog.setMessage(getResources().getString(R.string.Save_offline_image_message));
                                //}

                                alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Save_Offline), new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogn, int which) {
                                        // TODO Auto-generated method stub

                                        try
                                        {
                                            AppLocationManager appLocationManager = new AppLocationManager(CameraVideoActivity.this);
                                            Log.d("Class LAT LOG","Class LAT LOG"+appLocationManager.getLatitude()+" "+ appLocationManager.getLongitude());
                                            Log.d("Service LAT LOG","Service LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);
                                            PlayService_Location PlayServiceManager = new PlayService_Location(CameraVideoActivity.this);

                                            if(PlayServiceManager.checkPlayServices(CameraVideoActivity.this))
                                            {
                                                Log.d("Play LAT LOG","Play LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);

                                            }
                                            else
                                            if(!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)  && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null))
                                            {
                                                Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                                Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                            }

                                        }catch(Exception ex){ex.printStackTrace();}

                                        Long randomPIN = System.currentTimeMillis();
                                        String PINString = String.valueOf(randomPIN);

                                        SharedPreferences spf = CameraVideoActivity.this.getSharedPreferences("SimpleLogic", 0);
                                        String user_email = spf.getString("USER_EMAIL",null);

                                        loginDataBaseAdapter.insertCustomerServiceMedia("",Global_Data.GLOvel_CUSTOMER_ID,CP_NAME, RE_ID, user_email,
                                                "", discription.getText().toString(),Current_Date, Current_Date,Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE,Global_Data.Default_Image_Path,PINString);

                                        loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "IMAGE", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                        Intent intent = new Intent(CameraVideoActivity.this, Neworderoptions.class);
                                        startActivity(intent);
                                        finish();

                                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Save_Successfully),"Yes");
//											Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Save_Successfully), Toast.LENGTH_LONG);
//											toast.setGravity(Gravity.CENTER, 0, 0);
//											toast.show();
                                        dialogn.cancel();

                                    }
                                });

                                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.Online_Sync), new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialogn, int which) {


                                        isInternetPresent = cd.isConnectingToInternet();
                                        if (isInternetPresent)
                                        {
                                            btnSubmit.setClickable(false);
                                            btnSubmit.setEnabled(false);
                                            response_result = "";

                                            dialog = new ProgressDialog(CameraVideoActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                            dialog.setMessage(getResources().getString(R.string.Please_Wait));
                                            dialog.setTitle(getResources().getString(R.string.app_name));
                                            dialog.setCancelable(false);
                                            dialog.show();


                                            //media_coden = getStringImage(bitmap);

                                            SharedPreferences spf = CameraVideoActivity.this.getSharedPreferences("SimpleLogic", 0);
                                            String user_email = spf.getString("USER_EMAIL",null);

                                            loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "IMAGE", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                            new CameraVideoActivity.Mediaperation().execute(CP_NAME, image_url, discription.getText().toString(), Global_Data.GLOvel_CUSTOMER_ID, user_email, filename);

//									SyncMediaData(CP_NAME, image_url, discription.getText().toString(), Global_Data.GLOvel_CUSTOMER_ID, user_email, filename);
                                        }
                                        else
                                        {
                                            //Toast.makeText(getApplicationContext(),"No Internet. Data saved on your phone. Please Sync when network available.",Toast.LENGTH_LONG).show();

                                            try
                                            {
                                                AppLocationManager appLocationManager = new AppLocationManager(CameraVideoActivity.this);
                                                Log.d("Class LAT LOG","Class LAT LOG"+appLocationManager.getLatitude()+" "+ appLocationManager.getLongitude());
                                                Log.d("Service LAT LOG","Service LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);
                                                PlayService_Location PlayServiceManager = new PlayService_Location(CameraVideoActivity.this);

                                                if(PlayServiceManager.checkPlayServices(CameraVideoActivity.this))
                                                {
                                                    Log.d("Play LAT LOG","Play LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);

                                                }
                                                else
                                                if(!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)  && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null))
                                                {
                                                    Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                                    Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                                }

                                            }catch(Exception ex){ex.printStackTrace();}

                                            Long randomPIN = System.currentTimeMillis();
                                            String PINString = String.valueOf(randomPIN);

                                            SharedPreferences spf = CameraVideoActivity.this.getSharedPreferences("SimpleLogic", 0);
                                            String user_email = spf.getString("USER_EMAIL",null);

                                            loginDataBaseAdapter.insertCustomerServiceMedia("",Global_Data.GLOvel_CUSTOMER_ID,CP_NAME, RE_ID, user_email,
                                                    "", discription.getText().toString(),Current_Date, Current_Date,Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE,Global_Data.Default_Image_Path,PINString);

                                            loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "IMAGE", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                            Intent intent = new Intent(CameraVideoActivity.this, Neworderoptions.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            finish();
                                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Offline_save_message),"Yes");
//												Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Offline_save_message), Toast.LENGTH_LONG);
//												toast.setGravity(Gravity.CENTER, 0, 0);
//												toast.show();
                                        }

                                        dialogn.cancel();
                                    }
                                });

                                alertDialog.setCancelable(false);
                                alertDialog.show();




                                //new LoadDatabaseAsyncTask().execute();


                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Please Capture Media First", Toast.LENGTH_LONG).show();

                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Media_First),"Yes");
//						Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Media_First), Toast.LENGTH_LONG);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();

//						Intent intent = new Intent(Customer_Feed.this, Order.class);
//						intent.putExtra("filePath", "");
//
//						startActivity(intent);
//						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            }
        });



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
                Global_Data.CatlogueStatus = "";
                return true;
        }
//        return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add:
                String targetNew="";
                SharedPreferences sp = CameraVideoActivity.this.getSharedPreferences("SimpleLogic", 0);
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
        // TODO Auto-generated method stub
        //super.onBackPressed();
        C_Array.clear();

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        fileUri = getOutputMediaFileUrinew(MEDIA_TYPE_IMAGE);


//        if (CP_NAME.equals("Image")) {
//
//            Object tag = imageview.getTag();
//
//            if (!tag.equals(R.drawable.white_background)) {
//                if (fileUri.getPath().equalsIgnoreCase("null") || fileUri.getPath() != null) {
//                    get_dialogC_Back();
//                } else {
//                    this.finish();
//                }
//            } else {
//
//                this.finish();
//            }
//
//        } else if (CP_NAME.equals("video")) {
//
//            if (imageview.getVisibility() == View.GONE) {
//                get_dialogC_Back();
//            } else {
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                this.finish();
//            }
//
//        } else {
//            File mediaStorageDir = new File(
//                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                    Config.IMAGE_DIRECTORY_NAME + "/" + "CUSTOMER_SERVICES");
//
//            try {
//                //delete(mediaStorageDir);
//                if (mediaStorageDir.isDirectory()) {
//                    String[] children = mediaStorageDir.list();
//                    for (int i = 0; i < children.length; i++) {
//                        new File(mediaStorageDir, children[i]).delete();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            this.finish();
//        }
    }


    public void SyncMediaData(String media_type, String media_code, String discription, String CUSTOMER_ID, String GLOvel_USER_EMAIL, String file_name) {
        System.gc();
        String reason_code = "";
        try {

            btnSubmit.setClickable(false);
            btnSubmit.setEnabled(false);

//			dialog = new ProgressDialog(Customer_Feed.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//			dialog.setMessage("Please wait....");
//			dialog.setTitle("Metal");
//			dialog.setCancelable(false);
//			dialog.show();

            String domain = "";
            String device_id = "";


//			if(media_type.equalsIgnoreCase("Image"))
//			{
//
//				Customer_Feed.this.runOnUiThread(new Runnable() {
//					public void run() {
//						media_coden = getStringImage(bitmap);
//					}
//				});
//			}
//			else
//			{
//				Customer_Feed.this.runOnUiThread(new Runnable() {
//					public void run() {
//						media_coden = encodeVideoFile(final_media_path);
//					}
//				});
//
//			}

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDatef = dff.format(c.getTime());

            SharedPreferences spf = CameraVideoActivity.this.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL",null);

            loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "VIDEO", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

            SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";
            device_id = sp.getString("devid", "");
            domain = service_url;

            String email="";

            SharedPreferences sp2 = CameraVideoActivity.this.getSharedPreferences("SimpleLogic", 0);
            email = sp2.getString("USER_EMAIL", null);
            JsonObjectRequest jsObjRequest = null;
            try {


                Log.d("Server url", "Server url" + domain + "customer_service_media");


                JSONArray CUSTOMERSN = new JSONArray();
                JSONArray PICTURE = new JSONArray();
                //JSONObject product_value = new JSONObject();
                JSONObject product_value_n = new JSONObject();
                JSONArray product_imei = new JSONArray();

                final DataBaseHelper dbvoc = new DataBaseHelper(CameraVideoActivity.this);

                List<Local_Data> contacts = dbvoc.getAllRetailer_cre();

                for (Local_Data cn : contacts) {
                    JSONObject product_value = new JSONObject();
                    product_value.put("user_email", cn.getemail());
                    product_value.put("code", cn.getLEGACY_CUSTOMER_CODE());
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
                    CUSTOMERSN.put(product_value);
                }

                Long randomPIN = System.currentTimeMillis();
                String PINString = String.valueOf(randomPIN);

                JSONObject picture = new JSONObject();
                picture.put("code", PINString);
                picture.put("customer_code", CUSTOMER_ID);
                picture.put("user_email", GLOvel_USER_EMAIL);
                picture.put("media_type", media_type);
                picture.put("media_text", discription);
                picture.put("media_data", media_coden);
                picture.put("filename", file_name);
                PICTURE.put(picture);

                product_value_n.put("customers", CUSTOMERSN);
                product_value_n.put("media", PICTURE);
                product_value_n.put("email", email);
                Log.d("customers Service", product_value_n.toString());

                jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "customer_service_media", product_value_n, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("volley", "response: " + response);

                        Log.d("jV", "JV length" + response.length());
                        //JSONObject json = new JSONObject(new JSONTokener(response));
                        try {

                            String response_result = "";
                            if (response.has("result")) {
                                response_result = response.getString("result");
                            } else {
                                response_result = "data";
                            }


                            if (response_result.equalsIgnoreCase("success")) {

                                dialog.dismiss();
                                btnSubmit.setClickable(true);
                                btnSubmit.setEnabled(true);
                                //Toast.makeText(Customer_Feed.this, "Media Upload Successfully.", Toast.LENGTH_LONG).show();

                                String val = "";
                                dbvoc.updateCustomerby_CreateAt(val);

                                try {
                                    File file = new File(Global_Data.Default_video_Path);
                                    if (file.exists()) {

                                        file.delete();
                                        dbvoc.getDeleteMediaBYID(Global_Data.Default_video_Path);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }


                                Global_Data.Custom_Toast(CameraVideoActivity.this, getResources().getString(R.string.Media_Upload_Successfully),"Yes");

//								Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Media_Upload_Successfully), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

                                Intent a = new Intent(CameraVideoActivity.this, Neworderoptions.class);
                                startActivity(a);
                                finish();


                            } else {

                                dialog.dismiss();
                                btnSubmit.setClickable(true);
                                btnSubmit.setEnabled(true);
                                Global_Data.Custom_Toast(CameraVideoActivity.this, response_result,"Yes");
//								Toast toast = Toast.makeText(Customer_Feed.this, response_result, Toast.LENGTH_SHORT);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
//                                    Intent a = new Intent(context,Order.class);
//                                    context.startActivity(a);
//                                    ((Activity)context).finish();
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
                        //Toast.makeText(Customer_Feed.this, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();

                        Global_Data.Custom_Toast(CameraVideoActivity.this, getResources().getString(R.string.Server_Error),"Yes");
//                        Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();


                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                        } catch (JSONException e) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException errorn) {

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        dialog.dismiss();
                        btnSubmit.setClickable(true);
                        btnSubmit.setEnabled(true);
                    }
                });


                RequestQueue requestQueue = Volley.newRequestQueue(CameraVideoActivity.this);

                int socketTimeout = 2000000;//90 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsObjRequest.setRetryPolicy(policy);
                // requestQueue.se
                //requestQueue.add(jsObjRequest);
                jsObjRequest.setShouldCache(false);
                requestQueue.getCache().clear();
                requestQueue.add(jsObjRequest);

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

    public  class Mediaperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... response) {
            System.gc();
            String reason_code = "";
            try {

                CameraVideoActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        btnSubmit.setClickable(false);
                        btnSubmit.setEnabled(false);
                    }
                });

                String domain = "";
                String device_id = "";

//				if(response[0].equalsIgnoreCase("Image"))
//				{
//
//					Customer_Feed.this.runOnUiThread(new Runnable() {
//						public void run() {
//							media_coden = getStringImage(bitmap);
//						}
//					});
//				}
//				else
//				{
//					Customer_Feed.this.runOnUiThread(new Runnable() {
//						public void run() {
//							media_coden = encodeVideoFile(final_media_path);
//						}
//					});
//
//				}

                SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
                String Cust_domain = sp.getString("Cust_Service_Url", "");
                String service_url = Cust_domain + "metal/api/v1/";
                device_id = sp.getString("devid", "");
                domain = service_url;

                String email="";

                SharedPreferences spf = CameraVideoActivity.this.getSharedPreferences("SimpleLogic", 0);
                email = spf.getString("USER_EMAIL", null);

                JsonObjectRequest jsObjRequest = null;
                try
                {
                    Log.d("Server url","Server url"+domain+"customer_service_media");

                    JSONArray CUSTOMERSN = new JSONArray();
                    JSONArray PICTURE = new JSONArray();
                    //JSONObject product_value = new JSONObject();
                    JSONObject product_value_n = new JSONObject();
                    JSONArray product_imei = new JSONArray();

                    final DataBaseHelper dbvoc = new DataBaseHelper(CameraVideoActivity.this);

                    List<Local_Data> contacts = dbvoc.getAllRetailer_cre();

                    for (Local_Data cn : contacts)
                    {
                        JSONObject product_value = new JSONObject();
                        product_value.put("user_email", cn.getemail());
                        product_value.put("code", cn.getLEGACY_CUSTOMER_CODE());
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
                        CUSTOMERSN.put(product_value);

                    }

                    Long randomPIN = System.currentTimeMillis();
                    String PINString = String.valueOf(randomPIN);

                    JSONObject picture = new JSONObject();
                    picture.put("code", PINString);
                    picture.put("customer_code",response[3]);
                    picture.put("user_email", response[4]);
                    picture.put("media_type", response[0]);
                    picture.put("media_text", response[2]);
                    picture.put("media_data", media_coden);
                    picture.put("filename", response[5]);
                    PICTURE.put(picture);

                    product_value_n.put("customers", CUSTOMERSN);
                    product_value_n.put("media", PICTURE);
                    product_value_n.put("email", email);
                    Log.d("customers Service",product_value_n.toString());

                    jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain+"customer_service_media", product_value_n, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("volley", "response: " + response);

                            Log.d("jV", "JV length" + response.length());
                            //JSONObject json = new JSONObject(new JSONTokener(response));
                            try{
                                if(response.has("result"))
                                {
                                    response_result = response.getString("result");
                                }
                                else
                                {
                                    response_result = "data";
                                }

                                //return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));


                                if(response_result.equalsIgnoreCase("success")) {

                                    CameraVideoActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();
                                            btnSubmit.setClickable(true);
                                            btnSubmit.setEnabled(true);
                                        }
                                    });

                                    //Toast.makeText(Customer_Feed.this, "Media Upload Successfully.", Toast.LENGTH_LONG).show();

                                    String val = "";
                                    dbvoc.updateCustomerby_CreateAt(val);

                                    try {

                                        File file = new File(Global_Data.Default_Image_Path);
                                        if (file.exists()) {
//
                                            file.delete();
                                            dbvoc.getDeleteMediaBYID(Global_Data.Default_Image_Path);


                                        }
                                    }catch(Exception ex){ex.printStackTrace();}

                                    CameraVideoActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Global_Data.Custom_Toast(CameraVideoActivity.this, getResources().getString(R.string.Media_Upload_Successfully),"Yes");
//											Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Media_Upload_Successfully), Toast.LENGTH_LONG);
//											toast.setGravity(Gravity.CENTER, 0, 0);
//											toast.show();
                                        }
                                    });


                                    Intent a = new Intent(CameraVideoActivity.this,Neworderoptions.class);
                                    startActivity(a);
                                    finish();


                                }
                                else
                                {

                                    CameraVideoActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();
                                            btnSubmit.setClickable(true);
                                            btnSubmit.setEnabled(true);
                                            Global_Data.Custom_Toast(CameraVideoActivity.this,response_result,"Yes");
//											Toast toast = Toast.makeText(Customer_Feed.this,response_result, Toast.LENGTH_SHORT);
//											toast.setGravity(Gravity.CENTER, 0, 0);
//											toast.show();
                                        }
                                    });


//                                    Intent a = new Intent(context,Order.class);
//                                    context.startActivity(a);
//                                    ((Activity)context).finish();
                                }

                                //  finish();
                                // }

                                // output.setText(data);
                            }catch(JSONException e){e.printStackTrace();

                                CameraVideoActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                });
                            }


                            CameraVideoActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();
                                }
                            });
                            //dialog.dismiss();
                            //dialog.dismiss();




                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("volley", "error: " + error);
                            //Toast.makeText(Customer_Feed.this, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();

                            CameraVideoActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Global_Data.Custom_Toast(CameraVideoActivity.this, getResources().getString(R.string.Server_Error),"Yes");
//									Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//									toast.setGravity(Gravity.CENTER, 0, 0);
//									toast.show();
                                }
                            });



                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8" );
                                JSONObject jsonObject = new JSONObject( responseBody );
                            }
                            catch ( JSONException e ) {
                                //Handle a malformed json response
                            } catch (UnsupportedEncodingException errorn){

                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }


                            CameraVideoActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();
                                    btnSubmit.setClickable(true);
                                    btnSubmit.setEnabled(true);
                                }
                            });

                        }
                    });



                    RequestQueue requestQueue = Volley.newRequestQueue(CameraVideoActivity.this);

                    int socketTimeout = 2000000;//90 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    jsObjRequest.setRetryPolicy(policy);
                    // requestQueue.se
                    //requestQueue.add(jsObjRequest);
                    jsObjRequest.setShouldCache(false);
                    requestQueue.getCache().clear();
                    requestQueue.add(jsObjRequest);

                }catch(Exception e)
                {
                    e.printStackTrace();

                    CameraVideoActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                        }
                    });

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




            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            CameraVideoActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //dialog.dismiss();
                }
            });
            //dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    public Uri getOutputMediaFileUrinew(int type) {
        try
        {
            return Uri.fromFile(getOutputMediaFilenew(type));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private static File getOutputMediaFilenew(int type) {

        File mediaStorageDir;
        // External sdcard location
        if (type == MEDIA_TYPE_IMAGE) {
            mediaStorageDir = new File(
                    //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),Config.IMAGE_DIRECTORY_NAME);
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_PICTURE");


            image_path = mediaStorageDir.getPath();
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "Oops! Failed create "
                            + Config.IMAGE_DIRECTORY_NAME + " directory");
                    return null;
                } else {
                    mediaStorageDir.mkdirs();
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "IMG_" + timeStamp + ".jpg");

            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_VIDEO");

            video_path = mediaStorageDir.getPath();
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "Oops! Failed create "
                            + Config.IMAGE_DIRECTORY_NAME + " directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "IMG_" + timeStamp + ".jpg");


            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        }


        // Create the storage directory if it does not exist
        return null;
    }

    public void get_dialogC_Back()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(CameraVideoActivity.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Confirmation));
        if(CP_NAME.equals("video")) {
            alertDialog.setMessage(getResources().getString(R.string.video_back_button_message));
        }
        else
        {
            alertDialog.setMessage(getResources().getString(R.string.image_back_button_message));
        }

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub


                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//								   InsertOrderAsyncTask insertOrderAsyncTask =new InsertOrderAsyncTask(CaptureSignature.this);
//								   insertOrderAsyncTask.execute();

                Intent intent = new Intent(CameraVideoActivity.this, Neworderoptions.class);
//				if(fileUri.getPath() != null)
//				{
                intent.putExtra("filePath", fileUri.getPath());


                fileUri = null;
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                dialog.cancel();

//				}
//				else
//				{
//					intent.putExtra("filePath", "");
//				}

            }
        });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                //Toast.makeText(getApplicationContext(),"Your Data Submit Successfuly", Toast.LENGTH_LONG).show();


                dialog.cancel();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
