package com.msimplelogic.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.adapter.ReturnOrderImageAdapter;
import com.msimplelogic.adapter.returnorder2daptor;
import com.msimplelogic.model.returnordermodel;
import com.msimplelogic.services.getServices;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cpm.simplelogic.helper.ConnectionDetector;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class return_order2 extends BaseActivity {
    cpm.simplelogic.helper.ConnectionDetector cd;

    private ArrayList<String> p_id = new ArrayList<String>();
    private ArrayList<String> p_batchno = new ArrayList<String>();
    private ArrayList<String> p_q = new ArrayList<String>();
    private ArrayList<String> p_price = new ArrayList<String>();
    private ArrayList<String> p_remarks = new ArrayList<String>();
    Boolean isInternetPresent = false;

    LoginDataBaseAdapter loginDataBaseAdapter;
    ImageView camerabtn;
    Boolean B_flag;
    String image_check = "";
    Button buttonPreviewOrder;
    String pictureImagePath = "";
    String pictureImagePath_new = "";
    RecyclerView recyclerView;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    ReturnOrderImageAdapter RecyclerViewHorizontalAdapter;
    LinearLayoutManager HorizontalLayout;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    List<returnordermodel> list;
    returnorder2daptor adaptor;
    RecyclerView rv;
    String q_check = "";
    ImageView hedder_theame;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_order2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        list = new ArrayList<returnordermodel>();
        camerabtn = (ImageView) findViewById(R.id.camerabtn);
        buttonPreviewOrder = findViewById(R.id.buttonPreviewOrder);
        hedder_theame = findViewById(R.id.hedder_theame);
        rv = findViewById(R.id.rv);
        recyclerView = findViewById(R.id.recyclerview1);
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        cd = new cpm.simplelogic.helper.ConnectionDetector(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        // rv.setLayoutManager(RecyclerViewLayoutManager);
        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);

        }

        if (Global_Data.array_of_pVarient.size() > 0) {
            StringBuilder ss = new StringBuilder();
            String[] mStringArray = new String[Global_Data.array_of_pVarient.size()];
            mStringArray = Global_Data.array_of_pVarient.toArray(mStringArray);
            for (int i = 0; i < Global_Data.array_of_pVarient.size(); i++) {

                ss.append('"' + Global_Data.array_of_pVarient.get(i) + '"');
                if ((Global_Data.array_of_pVarient.size() - 1) != i) {
                    ss.append(",");
                }

            }

            System.out.println(ss.toString());

            List<Local_Data> cont = dbvoc.getSearchProduct(ss.toString().trim());

            if (cont.size() <= 0) {
                // Toast.makeText(Schedule_List.this, "Sorry No Record Found.", Toast.LENGTH_SHORT).show();

                return_order2.this.runOnUiThread(new Runnable() {
                    public void run() {
//                        Toast toast = Toast.makeText(return_order2.this, "Sorry No Record Found.", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(return_order2.this, "Sorry No Record Found.","yes");

                        Intent i = new Intent(return_order2.this, ReturnOrder1.class);
                        startActivity(i);
                        finish();
                    }
                });
            } else {
                for (Local_Data cn1 : cont) {
                    String str_var = "" + cn1.getStateName();
                    String str_var1 = "" + cn1.getMRP();
                    String str_var2 = "" + cn1.get_Description();
                    String str_var3 = "" + cn1.getProduct_variant();
                    Global_Data.amnt = "" + cn1.get_Description();
                    Global_Data.amnt1 = "" + cn1.get_Claims();


                    list.add(new returnordermodel(str_var, str_var1, str_var3, cn1.getCode()));

                }
            }
            adaptor = new returnorder2daptor(return_order2.this, list);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(adaptor);
            adaptor.notifyDataSetChanged();

            //results2.add("Select Variant");

        }
        else
        {
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.GLOVEL_CATEGORY_SELECTION) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.Search_Product_name) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Global_Data.ProductVariant)) {
            {

                List<Local_Data> cont1 = dbvoc.getProductvarientbycategoryandproductandvarant(Global_Data.GLOVEL_CATEGORY_SELECTION, Global_Data.Search_Product_name, Global_Data.ProductVariant);
                if (cont1.size() <= 0) {
                    return_order2.this.runOnUiThread(new Runnable() {
                        public void run() {
//                            Toast toast = Toast.makeText(return_order2.this, "Sorry No Record Found.", Toast.LENGTH_SHORT);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
Global_Data.Custom_Toast(return_order2.this, "Sorry No Record Found.","yes");
                            Intent i = new Intent(return_order2.this, ReturnOrder1.class);
                            startActivity(i);
                            finish();
                        }
                    });


                }
                else
                {
                    for (Local_Data cn1 : cont1) {
                        String str_var = "" + cn1.getStateName();
                        String str_var1 = "" + cn1.getMRP();
                        String str_var2 = "" + cn1.get_Description();
                        String str_var3 = "" + cn1.getProduct_variant();
                        Global_Data.amnt = "" + cn1.get_Description();
                        Global_Data.amnt1 = "" + cn1.get_Claims();


                        list.add(new returnordermodel(str_var, str_var1, str_var3, cn1.getCode()));

                    }
                }
                adaptor = new returnorder2daptor(return_order2.this, list);
                rv.setLayoutManager(new LinearLayoutManager(this));
                rv.setAdapter(adaptor);
                adaptor.notifyDataSetChanged();

            }
            }

        }


        buttonPreviewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                p_id.clear();
                p_q.clear();
                p_price.clear();
                p_remarks.clear();
                p_batchno.clear();
                //  p_name.clear();
                //   adaptor.put();
                if (!(Global_Data.return_oredr.isEmpty())) {

                    try {
                        for (Object name : Global_Data.return_oredr.keySet()) {

                            Object key = name.toString();
                            Object value = Global_Data.return_oredr.get(name);
                            Object value_remarks = Global_Data.Orderproduct_remarks.get(name);
                            Object value_detail1 = Global_Data.Orderproduct_detail1.get(name);

                            //System.out.println(key + " " + value);
                            Log.d("KEY", "Key: " + key + " Value: " + value);
                            JSONObject item = new JSONObject();

                            String key_array[] = String.valueOf(key).split("&");
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value))) {

                                String key_value_array[] = String.valueOf(value).split("pq");
                              //  String key_value_price_array[] = key_value_array[1].split("comment");
                               // String key_value_batch_array[] = key_value_price_array[1].split("batchno");

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(key_value_array[0]) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(key_value_array[1])) {

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_remarks))) {
                                        p_remarks.add(String.valueOf(value_remarks));
                                    } else {
                                        p_remarks.add("");
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(String.valueOf(value_detail1))) {
                                        p_batchno.add(String.valueOf(value_detail1));
                                    } else {
                                        p_batchno.add("");
                                    }

                                    q_check = "yes";
                                    p_id.add(key_array[1]);
                                    p_q.add(key_value_array[0]);
                                    p_price.add(key_value_array[1]);
                                    Log.d("quantity", "quantity" + key_value_array[0]);


                                }

                            }
                        }

                        if (!p_id.isEmpty() && q_check.equalsIgnoreCase("yes"))
                        {
                            SharedPreferences spf = return_order2.this.getSharedPreferences("SimpleLogic", 0);
                            String user_email = spf.getString("USER_EMAIL", null);
                            Long randomPIN = System.currentTimeMillis();
                            String PINString = String.valueOf(randomPIN);
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = df.format(c.getTime());

                            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
                            String formattedDatef = dff.format(c.getTime());


                            Global_Data.GLObalOrder_id_return = "Ord" + PINString;
                            Global_Data.GLOvel_GORDER_ID_RETURN = "Ord" + PINString;


                            try {
                                AppLocationManager appLocationManager = new AppLocationManager(return_order2.this);
                                Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                                Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                PlayService_Location PlayServiceManager = new PlayService_Location(return_order2.this);

                                if (PlayServiceManager.checkPlayServices(return_order2.this)) {
                                    Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                                } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                    Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                    Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            Calendar c1 = Calendar.getInstance();
                            System.out.println("Current time =&gt; " + c1.getTime());

                            SimpleDateFormat fdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            final String formattedDaten = fdf.format(c1.getTime());

                            AlertDialog alertDialog = new AlertDialog.Builder(return_order2.this).create(); //Read Update
                            alertDialog.setTitle("Confirmation");
                            alertDialog.setMessage(" Are you sure you want to continue?");
                            alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cd = new ConnectionDetector(getApplicationContext());
                                    isInternetPresent = cd.isConnectingToInternet();
                                    if (isInternetPresent) {

                                        String urls1 = "";
                                        String urls2 = "";
                                        String urls3 = "";
                                        String urls4 = "";
                                        String urls5 = "";
                                        for (int i = 0; i < Global_Data.Number.size(); i++) {
                                            if (i == 0) {
                                                urls1 = Global_Data.Number.get(0);
                                            }
                                            if (i == 1) {
                                                urls2 = Global_Data.Number.get(1);
                                            }
                                            if (i == 2) {
                                                urls3 = Global_Data.Number.get(2);
                                            }

                                            if (i == 3) {
                                                urls4 = Global_Data.Number.get(3);
                                            }
                                            if (i == 4) {
                                                urls5 = Global_Data.Number.get(4);
                                            }
                                        }

                                        try {
                                            // String product_id = product_map.get(Product_Variant.getText().toString());
                                            loginDataBaseAdapter.insertReturnOrders("", Global_Data.GLOvel_GORDER_ID_RETURN, Global_Data.GLOvel_CUSTOMER_ID, Global_Data.order_retailer, user_email, Global_Data.order_city, Global_Data.order_beat, "", "", "", "", "", "", formattedDaten, "", Global_Data.order_retailer, Global_Data.order_state, Global_Data.order_city, Global_Data.sales_btnstring, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Glovel_BEAT_ID, urls1, urls2, urls3, urls4, urls5);

                                            loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "RETURN ORDER", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                            for (int k = 0; k < p_id.size(); k++) {
                                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                                                    loginDataBaseAdapter.insertReturnOrderProducts(" ", " ", Global_Data.GLOvel_GORDER_ID_RETURN, "", "", p_id.get(k), "", " ", "", " ", "", p_q.get(k), "", "", p_price.get(k), "", "", "", " ", p_id.get(k), " ", "", p_batchno.get(k),  p_price.get(k),  p_remarks.get(k));//Reading all

                                                }
                                            }

                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                        getServices.SYNReturnOrder(return_order2.this, Global_Data.GLOvel_GORDER_ID_RETURN);

                                    } else {

                                        String urls1 = "";
                                        String urls2 = "";
                                        String urls3 = "";
                                        String urls4 = "";
                                        String urls5 = "";
                                        for (int i = 0; i < Global_Data.Number.size(); i++) {
                                            if (i == 0) {
                                                urls1 = Global_Data.Number.get(0);
                                            }
                                            if (i == 1) {
                                                urls2 = Global_Data.Number.get(1);
                                            }
                                            if (i == 2) {
                                                urls3 = Global_Data.Number.get(2);
                                            }

                                            if (i == 3) {
                                                urls4 = Global_Data.Number.get(3);
                                            }
                                            if (i == 4) {
                                                urls5 = Global_Data.Number.get(4);
                                            }
                                        }

                                        try {
                                            // String product_id = product_map.get(Product_Variant.getText().toString());
                                            loginDataBaseAdapter.insertReturnOrders("", Global_Data.GLOvel_GORDER_ID_RETURN, Global_Data.GLOvel_CUSTOMER_ID, Global_Data.order_retailer, user_email, Global_Data.order_city, Global_Data.order_beat, "", "", "", "", "", "", formattedDaten, "", Global_Data.order_retailer, Global_Data.order_state, Global_Data.order_city, Global_Data.sales_btnstring, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Glovel_BEAT_ID, urls1, urls2, urls3, urls4, urls5);

                                            loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "RETURN ORDER", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                            for (int k = 0; k < p_id.size(); k++) {
                                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(p_q.get(k))) {
                                                    loginDataBaseAdapter.insertReturnOrderProducts(" ", " ", Global_Data.GLOvel_GORDER_ID_RETURN, "", "", p_id.get(k), "", " ", "", " ", "", p_q.get(k), "", "", p_price.get(k), "", "", "", " ", p_id.get(k), " ", "", p_batchno.get(k),  p_price.get(k),  p_remarks.get(k));//Reading all

                                                }
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }


                                        Global_Data.Search_Category_name = "";
                                        Global_Data.return_oredr.clear();
                                        Global_Data.GLObalOrder_id_return = "";
                                        Global_Data.GLOvel_GORDER_ID_RETURN = "";
                                        Global_Data.Orderproduct_remarks.clear();
                                        Global_Data.Orderproduct_detail1.clear();
                                        Global_Data.Custom_Toast(return_order2.this, getResources().getString(R.string.PNO_Sucess), "Yes");

                                        Intent i = new Intent(return_order2.this, Neworderoptions.class);
                                        startActivity(i);
                                        finish();

                                        // Global_Data.Custom_Toast(ReturnOrder1.this,getResources().getString(R.string.internet_connection_error),"Yes");


                                    }


                                }
                            });

                            alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            alertDialog.setCancelable(false);
                            alertDialog.show();

                    }
                    else
                    {
                        p_id.clear();
                        p_q.clear();
                        p_price.clear();
                        p_remarks.clear();
                        p_batchno.clear();
                        q_check = "";
                        Global_Data.Custom_Toast(return_order2.this, "Please Enter Quantity", "Yes");

                    }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    p_id.clear();
                    p_q.clear();
                    p_price.clear();
                    p_remarks.clear();
                    p_batchno.clear();
                    q_check = "";
                    Global_Data.Custom_Toast(return_order2.this, "Please Enter Quantity", "Yes");
                }


            }
        });


        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission2();
            }
        });


    }

    private void requestStoragePermission2() {

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

                                if (Global_Data.image_counter <= 4) {

                                    //final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Choose_from_Gallery), getResources().getString(R.string.Cancel)};
                                    final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Cancel)};

                                    AlertDialog.Builder builder = new AlertDialog.Builder(return_order2.this);

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
                                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                                // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                                Uri photoURI = FileProvider.getUriForFile(return_order2.this,
                                                        BuildConfig.APPLICATION_ID + ".provider",
                                                        photoFile);
                                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                startActivityForResult(cameraIntent, 1);

                                            }
//										else if (options[item].equals(getResources().getString(R.string.Choose_from_Gallery)))
//
//										{
//
//											image_check = "gallery";
//											Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//											startActivityForResult(intent, 2);
//
//
//										}
                                            else if (options[item].equals(getResources().getString(R.string.Cancel))) {

                                                dialog.dismiss();

                                            }

                                        }

                                    });

                                    builder.show();


                                } else {
                                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.pick_validation), "");
                                    //   Toast.makeText(Cash_Collect.this, "You can not take more than 5 picture.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.no_camera), "");
                                //Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_camera), Toast.LENGTH_LONG).show();
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
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(return_order2.this);
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

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "Return";
        File storageDir = null;

        storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Return Order");


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
        pictureImagePath_new = image.getAbsolutePath();
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
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
                SharedPreferences sp = return_order2.this.getSharedPreferences("SimpleLogic", 0);
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

    @Override
    public void onBackPressed() {
        Global_Data.GLOVEL_CATEGORY_SELECTION = "";
        Intent i = new Intent(return_order2.this, ReturnOrder1.class);
        startActivity(i);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
//		try {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            recyclerView.setVisibility(View.VISIBLE);
            Global_Data.Number.add(pictureImagePath_new);

            RecyclerViewHorizontalAdapter = new ReturnOrderImageAdapter(return_order2.this, Global_Data.Number);

            HorizontalLayout = new LinearLayoutManager(return_order2.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(HorizontalLayout);

            recyclerView.setAdapter(RecyclerViewHorizontalAdapter);

            ++Global_Data.image_counter;

//            File imgFils = new File(pictureImagePath_new);
//            if (imgFils.exists()) {
//
//                img_show = findViewById(R.id.img_show);
//                 crossimg.setVisibility(View.VISIBLE);
//                  img_show.setVisibility(View.VISIBLE);
//                 img_layout.setVisibility(View.VISIBLE);
//                img_show.setRotation((float) 90.0);
//
//                Glide.with(ReturnOrder1.this).load(pictureImagePath_new).into(img_show);
//
//                img_show.setImageURI(Uri.fromFile(imgFils));
//            }


        }
//		else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
//			beginCrop(data.getData());
//		} else if (requestCode == Crop.REQUEST_CROP) {
//
//			try {
//				if (data == null) {
//					imageview.setVisibility(View.VISIBLE);
//					imageview.setImageDrawable(Glovel_Drawable);
//					// imageview.setTag(Glovel_bitmap);
//					//imageviewr.setImageBitmap(Glovel_bitmap);
//				} else {
//					handleCrop(resultCode, data, data.getData());
//				}
//			} catch (Exception ex) {
//				ex.printStackTrace();
//				imageview.setVisibility(View.VISIBLE);
//				imageview.setImageDrawable(Glovel_Drawable);
//				// imageview.setTag(Glovel_bitmap);
//				// imageviewr.setImageBitmap(Glovel_bitmap);
//			}
//		}


        //fileUri = getOutputMediaFileUri(selectedPath);
        //textView.setText(selectedPath);
        if (requestCode == 2 && resultCode == RESULT_OK) {

            try {
                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                pictureImagePath = "file:" + c.getString(columnIndex);


                c.close();

                //new Expenses.LongOperation().execute();
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }


//		}catch (Exception ex){ex.printStackTrace();
//			Toast.makeText(Customer_Feed.this, "Please Select Media File.", Toast.LENGTH_LONG).show();
//		}
    }


}
