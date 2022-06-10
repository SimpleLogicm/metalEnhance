package com.msimplelogic.activities;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.FileProvider;

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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cpm.simplelogic.helper.CashCollection_Data;

/**
 * Created by vv on 9/6/2017.
 */

public class Cash_Submit extends BaseActivity {
    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    LoginDataBaseAdapter loginDataBaseAdapter;
    static String final_response = "";
    String response_result = "";
    ProgressDialog dialog;
    String encr_iamgecode = "";
    byte b5[];
    Spinner cash_type, submit_to;
    private String[] arraySpinner, arraySpinner1;
    LinearLayout mContent;
    signature mSignature;//soar
    Button mClear, mGetSign, mCancel;
    int m_sign_flag = 0;
    private Bitmap mBitmap;
    View mView;
    EditText submit_amount, submit_code, submit_remark, cash_inhand;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    ImageView cam_btn;
    Boolean B_flag;
    private String pictureImagePath = "";
    ArrayList<String> submit_toArray = new ArrayList<String>();
    HashMap<String, String> submit_toArray_hash = new HashMap<String, String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.cash_submit);
        cash_type = (Spinner) findViewById(R.id.cash_type);
        submit_to = (Spinner) findViewById(R.id.submit_to);
        cash_inhand = (EditText) findViewById(R.id.cash_inhand);
        submit_amount = (EditText) findViewById(R.id.submit_amount);
        submit_code = (EditText) findViewById(R.id.submit_code);
        submit_remark = (EditText) findViewById(R.id.submit_remark);

        mContent = (LinearLayout) findViewById(R.id.linearLayout);
        mSignature = new signature(this, null);
        mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        mClear = (Button) findViewById(R.id.submit_clear);
        mClear.setBackgroundColor(Color.parseColor("#414042"));
        mGetSign = (Button) findViewById(R.id.submit);
        mGetSign.setBackgroundColor(Color.parseColor("#414042"));

        cam_btn = (ImageView) findViewById(R.id.cam_submit);

        Random rand = new Random();
        submit_code.setText("" + rand.nextInt(10000));

        loginDataBaseAdapter = new LoginDataBaseAdapter(Cash_Submit.this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        mView = mContent;
        cd = new ConnectionDetector(getApplicationContext());

        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));

            LayoutInflater mInflater = LayoutInflater.from(this);
            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText(getResources().getString(R.string.Cash_Submit));

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = this.getSharedPreferences("SimpleLogic", 0);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            try {
                int target = (int) Math.round(sp.getFloat("Target", 0));
                int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;

                if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                    int age = (int) Math.round(age_float);

                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                    }
                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                } else {
                    int age = (int) Math.round(age_float);

                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    }
                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
//        	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
                todaysTarget.setText("Today's Target Acheived");
            }

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.arraySpinner = new String[]{
                getResources().getString(R.string.Instrument_Type), getResources().getString(R.string.Cash), getResources().getString(R.string.Cheque), getResources().getString(R.string.Other)
        };

        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arraySpinner);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        cash_type.setAdapter(adapter);


        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            SharedPreferences spf = Cash_Submit.this.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL",null);

            SharedPreferences pref_devid = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
            String device_id = pref_devid.getString("devid", "");

            List<CashCollection_Data> cash_reci_Data = dbvoc.getAllCash_Reciept();

            if (cash_reci_Data.size() > 0) {

                dialog = new ProgressDialog(Cash_Submit.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                dialog.setMessage(getResources().getString(R.string.Please_Wait));
                dialog.setTitle(getResources().getString(R.string.app_name));
                dialog.setCancelable(false);
                dialog.show();

                SyncCashrecieptDatan(Cash_Submit.this, Global_Data.device_id, user_email);

            } else {
                Log.d("CashRe NODATA", "CashRe NODATA Found");
                dialog = new ProgressDialog(Cash_Submit.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                dialog.setMessage(getResources().getString(R.string.Please_Wait));
                dialog.setTitle(getResources().getString(R.string.app_name));
                dialog.setCancelable(false);
                dialog.show();
                getCashierData(Global_Data.device_id, user_email);
            }


        } else {

            Global_Data.Custom_Toast(Cash_Submit.this, getResources().getString(R.string.internet_connection_error),"Yes");
            //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//            Toast toast = Toast.makeText(Cash_Submit.this, getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();

            Intent i = new Intent(Cash_Submit.this, MainActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            startActivity(i);
            finish();
        }

        submit_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null && !s.toString().equals("") && !cash_inhand.getText().toString().equalsIgnoreCase("") && !cash_inhand.getText().toString().equalsIgnoreCase("0") && !cash_inhand.getText().toString().equalsIgnoreCase("null")) {
                    Double d = Double.valueOf(s.toString());
                    //Double d = Double.parseDouble(collect_amount.getText().toString());
                    if (d <= Double.valueOf(cash_inhand.getText().toString())) {
                        String str = submit_amount.getText().toString();
                    } else {
                        submit_amount.setText("");
                        Global_Data.Custom_Toast(Cash_Submit.this, getResources().getString(R.string.a_wallet_validation_message),"");
                  //      Toast.makeText(Cash_Submit.this, getResources().getString(R.string.a_wallet_validation_message), Toast.LENGTH_SHORT).show();
                    }
                } else {

                    if (cash_inhand.getText().toString().equalsIgnoreCase("") || cash_inhand.getText().toString().equalsIgnoreCase("0") || cash_inhand.getText().toString().equalsIgnoreCase("null")) {
                       Global_Data.Custom_Toast(Cash_Submit.this, getResources().getString(R.string.not_wallet_m_message),"");
                        //Toast.makeText(Cash_Submit.this, getResources().getString(R.string.not_wallet_m_message), Toast.LENGTH_SHORT).show();
                    }

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(submit_amount.getText().toString())) {
                        submit_amount.setText("");
                    }

                }
            }
        });

        mClear.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //down event
                    b.setBackgroundColor(Color.parseColor("#910505"));
                    Log.v("log_tag", "Panel Cleared");
                    mSignature.clear();
                    m_sign_flag = 0;
                    submit_amount.setText("");
                    submit_remark.setText("");

                    int spinnerPosition = adapter.getPosition(getResources().getString(R.string.Instrument_Type));
                    cash_type.setSelection(spinnerPosition);

                    int spinnerPosition1 = adapter1.getPosition(getResources().getString(R.string.Submit_To));
                    submit_to.setSelection(spinnerPosition1);
                    //  mGetSign.setEnabled(false);
                }
                return false;
            }
        });

        cam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestStoragePermission();

                }
        });

        mGetSign.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    b.setBackgroundColor(Color.parseColor("#910505"));
                    mView.setDrawingCacheEnabled(true);
                    LinearLayout content = (LinearLayout) findViewById(R.id.linearLayout);
                    content.setDrawingCacheEnabled(true);
                    final Bitmap bitmap = content.getDrawingCache();

                    if (cash_type.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Instrument_Type))) {
                        Global_Data.Custom_Toast(Cash_Submit.this, getResources().getString(R.string.Select_Instrument_Type),"Yes");
//                        Toast toast = Toast.makeText(Cash_Submit.this, getResources().getString(R.string.Select_Instrument_Type), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 50, 50);
//                        toast.show();
                    } else if (submit_to.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Submit_To))) {
                        Global_Data.Custom_Toast(Cash_Submit.this, getResources().getString(R.string.Please_Select_Name),"Yes");
//                        Toast toast = Toast.makeText(Cash_Submit.this, getResources().getString(R.string.Please_Select_Name), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 50, 50);
//                        toast.show();
                    }
//                    else if (cash_inhand.getText().length() == 0) {
//                        Toast toast = Toast.makeText(Cash_Submit.this, "Please Enter Cash In hand", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 50, 50);
//                        toast.show();
//                    }
                    else if (submit_amount.getText().length() == 0) {
                        Global_Data.Custom_Toast(Cash_Submit.this, getResources().getString(R.string.Please_Enter_Amount),"Yes");
//                        Toast toast = Toast.makeText(Cash_Submit.this, getResources().getString(R.string.Please_Enter_Amount), Toast.LENGTH_SHORT);
////                        toast.setGravity(Gravity.CENTER, 50, 50);
////                        toast.show();
                    } else if (Double.valueOf(submit_amount.getText().toString()) > Double.valueOf(cash_inhand.getText().toString())) {
                      Global_Data.Custom_Toast(Cash_Submit.this, getResources().getString(R.string.a_wallet_validation_message),"Yes");
//                        Toast toast = Toast.makeText(Cash_Submit.this, getResources().getString(R.string.a_wallet_validation_message), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 50, 50);
//                        toast.show();
                        submit_amount.setText("");
                    } else if (m_sign_flag == 0) {
                        Global_Data.Custom_Toast(Cash_Submit.this, getResources().getString(R.string.Please_Sign),"Yes");
//                        Toast toast = Toast.makeText(Cash_Submit.this, getResources().getString(R.string.Please_Sign), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 50, 50);
//                        toast.show();
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(Cash_Submit.this).create(); //Read Update
                        alertDialog.setTitle(getResources().getString(R.string.Confirmation));
                        alertDialog.setMessage(getResources().getString(R.string.Continue_dialog));
                        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                } else
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                File storagePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_DIRECTORY_NAME + "/" + Global_Data.GLOvel_CUSTOMER_ID);
                                storagePath.mkdirs();

                                File myImage = new File(storagePath, Long.toString(System.currentTimeMillis()) + ".png");

                                String uploadImage = "";

                                try {
                                    FileOutputStream out = new FileOutputStream(myImage);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 10, out);
                                    out.flush();
                                    out.close();
                                    uploadImage = getStringImage(bitmap);
                                    mSignature.clear();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    //delete(mediaStorageDir);
                                    if (storagePath.isDirectory()) {
                                        String[] children = storagePath.list();
                                        for (int i = 0; i < children.length; i++) {
                                            new File(storagePath, children[i]).delete();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                isInternetPresent = cd.isConnectingToInternet();

                                if (isInternetPresent) {
                                    try {
                                        LocationAddress locationAddress = new LocationAddress();
                                        LocationAddress.getAddressFromLocation(Double.valueOf(Global_Data.GLOvel_LATITUDE), Double.valueOf(Global_Data.GLOvel_LONGITUDE),
                                                Cash_Submit.this, new Cash_Submit.GeocoderHandler());
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        // String address = addressn(latitude, longitude);
                                    }

                                    String address = Global_Data.address;

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String formattedDate = df.format(c.getTime());

                                    String s_code = submit_toArray_hash.get(submit_to.getSelectedItem().toString().trim());
                                    SyncCashdepositData(s_code, submit_amount.getText().toString().trim(), uploadImage, submit_code.getText().toString().trim(), submit_remark.getText().toString().trim(), formattedDate, address);
                                } else {
                                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.internet_connection_error),"");
//                                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                                    toast.show();
                                }
                            }
                        });

                        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        });

                        alertDialog.setCancelable(false);
                        alertDialog.show();

                    }
                    //  mGetSign.setEnabled(false);

                }
                return false;
            }
        });
    }

    public class signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v, String mypath) {
            Log.e("log_tag", "Width: " + v.getWidth());
            Log.e("log_tag", "Height: " + v.getHeight());
            if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
                ;
            }
            Canvas canvas = new Canvas(mBitmap);
            try {
                FileOutputStream mFileOutStream = new FileOutputStream(mypath);

                v.draw(canvas);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();
                String url = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "title", null);
                Log.e("log_tag", "mypath: " + mypath);

                Log.e("log_tag", "url: " + url);
                //In case you want to delete the file
                //boolean deleted = mypath.delete();
                //Log.v("log_tag","deleted: " + mypath.toString() + deleted);
                //If you want to convert the image to string use base64 converter

            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            m_sign_flag = 1;
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
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

    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        // String encodedImage =imageBytes.toString();

        return encodedImage;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
//        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GlobeloPname)) {
//            Global_Data.GlobeloPname = "";
//            Global_Data.GlobeloPAmount = "";
//            Intent i = new Intent(Cash_Submit.this, MainActivity.class);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            startActivity(i);
//            finish();
//        } else {
        Intent i = new Intent(Cash_Submit.this, MainActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            startActivity(i);
            finish();
        // }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N){
//
//            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
//                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos5);
//                b5 = bos5.toByteArray();
//
//                encr_iamgecode= Base64.encodeToString(b5,Base64.DEFAULT);
//
//                try {
//
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                //get_icon.setImageBitmap(imageBitmap);
//            }
//        }
//        else
//        {
        if (requestCode == 1) {

            new Cash_Submit.CameraOperation().execute();

        }
        //}


//        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
//            if(fileUri != null){
//                Uri selectedImage = imageToUploadUri;
//                getContentResolver().notifyChange(selectedImage, null);
//                Bitmap reducedSizeBitmap = getBitmap(imageToUploadUri.getPath());
//                if(reducedSizeBitmap != null){
//                    ImgPhoto.setImageBitmap(reducedSizeBitmap);
//                    Button uploadImageButton = (Button) findViewById(R.id.uploadUserImageButton);
//                    uploadImageButton.setVisibility(View.VISIBLE);
//                }else{
//                    Toast.makeText(this,"Error while capturing Image",Toast.LENGTH_LONG).show();
//                }
//            }else{
//                Toast.makeText(this,"Error while capturing Image",Toast.LENGTH_LONG).show();
//            }
//        }

//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//
//
//            ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
//            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos5);
//            b5 = bos5.toByteArray();
//
//            encr_iamgecode= Base64.encodeToString(b5,Base64.DEFAULT);
//
//            try {
//
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            //get_icon.setImageBitmap(imageBitmap);
//        }

    }

    private class CameraOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap bm400 = getScaledBitmap(myBitmap, 800, 800);
                Bitmap bm405 = rotateImage(bm400, 90);
                ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
                bm405.compress(Bitmap.CompressFormat.PNG, 100, bos5);
                b5 = bos5.toByteArray();

                encr_iamgecode = Base64.encodeToString(b5, Base64.DEFAULT);
                // dialog.dismiss();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            if (dialog != null) {
                dialog.dismiss();
                Global_Data.Custom_Toast(Cash_Submit.this, getResources().getString(R.string.Image_Captured),"");
                //Toast.makeText(Cash_Submit.this, getResources().getString(R.string.Image_Captured), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {

            //dialog.dismiss();

//            if(dialog != null) {
//                dialog.dismiss();
//                dialog = null;
            dialog = new ProgressDialog(Cash_Submit.this);
            dialog.setMessage(getResources().getString(R.string.Please_Wait));
            dialog.setTitle(getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();
            // }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight) {
        Matrix m = new Matrix();

        m.setRectToRect(new RectF(0, 0, b.getWidth(), b.getHeight()), new RectF(0, 0, reqWidth, reqHeight), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    public void SyncCashrecieptDatan(Context context, final String device_id, final String user_email) {
        System.gc();
        String reason_code = "";
        try {


            SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";
            String domain = service_url;

            JsonObjectRequest jsObjRequest = null;
            try {

                Log.d("Server url", "Server url" + domain + "cash_receipts/save_receipts");

                JSONArray COLLECTION_RECJARRAY = new JSONArray();
                //JSONObject product_value = new JSONObject();
                JSONObject COLLECTION_RECJOBJECT = new JSONObject();
                JSONArray product_imei = new JSONArray();

                List<CashCollection_Data> cash_reci_Data = dbvoc.getAllCash_Reciept();

                if (cash_reci_Data.size() > 0) {
                    for (CashCollection_Data c : cash_reci_Data) {

                        JSONObject INNER_CASH_JOB = new JSONObject();
                        INNER_CASH_JOB.put("code", c.getCode());
                        INNER_CASH_JOB.put("reason_code", c.getReason_name());
                        INNER_CASH_JOB.put("amount", c.getAmount());
                        INNER_CASH_JOB.put("detail1", c.getCash_detail1());
                        INNER_CASH_JOB.put("detail2", c.getCash_detail2());
                        INNER_CASH_JOB.put("detail3", c.getCash_detail3());
                        INNER_CASH_JOB.put("detail4", c.getCash_detail4());
                        INNER_CASH_JOB.put("detail5", c.getCash_detail5());
                        INNER_CASH_JOB.put("received_at", c.getReceived_at());
                        INNER_CASH_JOB.put("customer_code", c.getReceived_from());
                        //INNER_CASH_JOB.put("received_from_name", c.getReceived_from_name());
                        if (c.getReceived_loc_latlong().indexOf(",") > 0) {
                            String latlong[] = c.getReceived_loc_latlong().split(",");
                            INNER_CASH_JOB.put("latitude", latlong[0]);
                            INNER_CASH_JOB.put("longitude", latlong[1]);
                        } else {
                            INNER_CASH_JOB.put("latitude", c.getReceived_loc_latlong());
                            INNER_CASH_JOB.put("longitude", c.getReceived_loc_latlong());
                        }
                        INNER_CASH_JOB.put("address", c.getReceived_loc_address());
                        INNER_CASH_JOB.put("signature", c.getReceived_signature());
                        INNER_CASH_JOB.put("remarks", c.getReceived_remarks());
                        //INNER_CASH_JOB.put("random_value", c.getPunched_on());
                        INNER_CASH_JOB.put("amount_overdue", c.getColle_overdue());
                        INNER_CASH_JOB.put("amount_outstanding", c.getColle_outstanding());
                        COLLECTION_RECJARRAY.put(INNER_CASH_JOB);

                    }
                }

                COLLECTION_RECJOBJECT.put("receipts", COLLECTION_RECJARRAY);
                COLLECTION_RECJOBJECT.put("imei_no", device_id);
                COLLECTION_RECJOBJECT.put("email", user_email);
                Log.d("customers Service", COLLECTION_RECJOBJECT.toString());

                jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "cash_receipts/save_receipts", COLLECTION_RECJOBJECT, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("volley", "response: " + response);

                        Log.d("jV", "JV length" + response.length());
                        try {

                            String response_result = "";
                            if (response.has("result")) {
                                response_result = response.getString("result");
                            } else {
                                response_result = "data";
                            }


                            if (response_result.equalsIgnoreCase("Receipts created successfully.")) {

                                Log.d("CASHR Response", "CASHR Response " + response_result);

                                dbvoc.getDeleteTable("cash_receipt");

                                getCashierData(device_id, user_email);
                                //  dialog.dismiss();

                            } else {
                                Log.d("CASHR Response", "CASHR Response " + response_result);
                                getCashierData(device_id, user_email);
                                // dialog.dismiss();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            getCashierData(device_id, user_email);
                            //dialog.dismiss();

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        // dialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Log.d("ErrorC", "ErrorC your internet connection is not working, saving locally. Please sync when Internet is available");
                        } else if (error instanceof AuthFailureError) {
                            Log.d("ErrorC", "ErrorC Server AuthFailureError  Error");
                        } else if (error instanceof ServerError) {
                            Log.d("ErrorC", "ErrorC Server Error");
                        } else if (error instanceof NetworkError) {
                            Log.d("ErrorC", "ErrorC your internet connection is not working, saving locally. Please sync when Internet is available");
                        } else if (error instanceof ParseError) {
                            Log.d("ErrorC", "ErrorC arseError   Error");
                        } else {
                            Log.d("ErrorC", "ErrorC " + error.getMessage());
                        }
                        getCashierData(device_id, user_email);

                    }
                });


                RequestQueue requestQueue = Volley.newRequestQueue(context);

                int socketTimeout = 120000;//90 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsObjRequest.setRetryPolicy(policy);
                // requestQueue.se
                //requestQueue.add(jsObjRequest);
                jsObjRequest.setShouldCache(false);
                requestQueue.getCache().clear();
                requestQueue.add(jsObjRequest);

            } catch (Exception e) {
                e.printStackTrace();
                getCashierData(device_id, user_email);

            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("DATA", e.getMessage());
            dialog.dismiss();
            getCashierData(device_id, user_email);
        }
    }

    public void getCashierData(String device_id, String email) {
        try {
            SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";
            String domain = service_url;
            Log.i("volley", "domain: " + domain);
            Log.i("volley", "email: " + email);
            Log.i("send_initial_data url", "cash_de send_initial_data url " + domain + "cash_deposits/send_initial_data?imei_no=" + device_id + "&email=" + email);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(domain + "cash_deposits/send_initial_data?imei_no=" + device_id + "&email=" + email, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);
                    //  Log.i("volley", "response reg Length: " + response.length());


                    try {

                        String response_result = "";
                        if (response.has("result")) {
                            response_result = response.getString("result");
                        } else {
                            response_result = "data";
                        }


                        if (response_result.equalsIgnoreCase("User doesn't found.")) {


                            dialog.dismiss();
                            //Toast.makeText(Order.this, response_result, Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(Cash_Submit.this, response_result, "Yes");

//                            Toast toast = Toast.makeText(Cash_Submit.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Intent intent = new Intent(Cash_Submit.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();

                        } else if (response_result.equalsIgnoreCase("User doesn't exist")) {

                            //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                            dialog.dismiss();
                            Global_Data.Custom_Toast(Cash_Submit.this, response_result,"Yes");
//                            Toast toast = Toast.makeText(Cash_Submit.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Intent intent = new Intent(Cash_Submit.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();

                        } else {

                            JSONArray Cash_listData = response.getJSONArray("users");
                            String cash_inh = response.getString("wallet_balance");


                            Log.i("volley", "response reg Cash_listData Length: " + Cash_listData.length());

                            Log.d("Cash_listData", "Cash_listData" + Cash_listData.toString());
                            if (Cash_listData.length() <= 0 || !Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cash_inh)) {
                                dialog.dismiss();
                                //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

                                if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(cash_inh)) {
                                    Global_Data.Custom_Toast(Cash_Submit.this, getResources().getString(R.string.Wallet_amount_Not_Found),"Yes");
//                                    Toast toast = Toast.makeText(Cash_Submit.this, getResources().getString(R.string.Wallet_amount_Not_Found), Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();

                                    Intent intent = new Intent(Cash_Submit.this, MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    finish();
                                } else {
                                    Global_Data.Custom_Toast(Cash_Submit.this, getResources().getString(R.string.Cashier_Not_Found),"Yes");
//                                    Toast toast = Toast.makeText(Cash_Submit.this, getResources().getString(R.string.Cashier_Not_Found), Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();

                                    Intent intent = new Intent(Cash_Submit.this, MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    finish();
                                }

                            } else {
                                submit_toArray_hash.clear();
                                submit_toArray.clear();
                                submit_toArray.add(getResources().getString(R.string.Submit_To));
                                cash_inhand.setText(cash_inh);
                                for (int i = 0; i < Cash_listData.length(); i++) {

                                    JSONObject jsonObject = Cash_listData.getJSONObject(i);
                                    submit_toArray.add(jsonObject.getString("user_name"));
                                    submit_toArray_hash.put(jsonObject.getString("user_name"), jsonObject.getString("email"));

                                }

                                adapter1 = new ArrayAdapter<String>(Cash_Submit.this, R.layout.spinner_item, submit_toArray);
                                adapter1.setDropDownViewResource(R.layout.spinner_item);
                                submit_to.setAdapter(adapter1);

                                try {
                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GlobeloPAmount)) {
                                        submit_amount.setText(Global_Data.GlobeloPAmount);
                                    }

                                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(Global_Data.GlobeloPname)) {
                                        int spinnerPosition1 = adapter1.getPosition(Global_Data.GlobeloPname);
                                        submit_to.setSelection(spinnerPosition1);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }




                                dialog.dismiss();
                            }
                            dialog.dismiss();


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                    }


                    dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volley", "error: " + error);
                    //Toast.makeText(Order.this, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();

                    Global_Data.Custom_Toast(Cash_Submit.this, getResources().getString(R.string.Server_Error),"yes");
//                    Toast toast = Toast.makeText(Cash_Submit.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    dialog.dismiss();

                    Intent intent = new Intent(Cash_Submit.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(Cash_Submit.this);
            // queue.add(jsObjRequest);
            jsObjRequest.setShouldCache(false);
            int socketTimeout = 200000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);

        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();


        }
    }

    public String firstThree(String str) {
        return str.length() < 3 ? str : str.substring(0, 3);
    }

    public void SyncCashdepositData(String submit_to_code, String submit_amount, String uploadImage_signature, String submit_code, String submit_remark, String current_d_t, String address) {
        System.gc();
        String reason_code = "";
        try {

            SharedPreferences spf = Cash_Submit.this.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL",null);

            dialog = new ProgressDialog(Cash_Submit.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage(getResources().getString(R.string.Please_Wait));
            dialog.setTitle(getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();

            String code = firstThree(user_email) + firstThree(submit_code) + current_d_t;


            String device_id = "";

            SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";
            device_id = sp.getString("devid", "");
            String domain = service_url;

            JsonObjectRequest jsObjRequest = null;
            try {

                Log.d("Server url", "Server url" + domain + "cash_deposits");

                JSONArray DEPOSIT_RECJARRAY = new JSONArray();
                //JSONObject product_value = new JSONObject();
                JSONObject COLLECTION_RECJOBJECT = new JSONObject();
                JSONArray product_imei = new JSONArray();

                JSONObject INNER_CASH_JOB = new JSONObject();
                INNER_CASH_JOB.put("code", code);
                INNER_CASH_JOB.put("accepted_by", submit_to_code);
                INNER_CASH_JOB.put("amount", submit_amount);
                INNER_CASH_JOB.put("accepted_at", current_d_t);
                INNER_CASH_JOB.put("latitude", Global_Data.GLOvel_LATITUDE);
                INNER_CASH_JOB.put("longitude", Global_Data.GLOvel_LONGITUDE);
                INNER_CASH_JOB.put("address", address);
                INNER_CASH_JOB.put("remarks", submit_remark);
                INNER_CASH_JOB.put("otp", submit_code);
                INNER_CASH_JOB.put("signature", uploadImage_signature);

                DEPOSIT_RECJARRAY.put(INNER_CASH_JOB);

                COLLECTION_RECJOBJECT.put("deposit", INNER_CASH_JOB);
                COLLECTION_RECJOBJECT.put("imei_no", Global_Data.device_id);
                COLLECTION_RECJOBJECT.put("email", user_email);
                Log.d("customers Service", COLLECTION_RECJOBJECT.toString());

                jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "cash_deposits", COLLECTION_RECJOBJECT, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("volley", "response: " + response);

                        Log.d("jV", "JV length" + response.length());
                        try {

                            String response_result = "";
                            if (response.has("result")) {
                                response_result = response.getString("result");
                            } else {
                                response_result = "data";
                            }


                            if (!response_result.equalsIgnoreCase("data")) {

                                dialog.dismiss();

                                Global_Data.Custom_Toast(Cash_Submit.this, response_result,"Yes");
//                                Toast toast = Toast.makeText(Cash_Submit.this, response_result, Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

                                Intent i = new Intent(Cash_Submit.this, MainActivity.class);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                startActivity(i);
                                finish();


                            } else {
                                dialog.dismiss();
                                Global_Data.Custom_Toast(Cash_Submit.this, response_result,"Yes");
//                                Toast toast = Toast.makeText(Cash_Submit.this, response_result, Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Intent i = new Intent(Cash_Submit.this, Customer_info_main.class);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                startActivity(i);
                                finish();

                            }


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
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(Cash_Submit.this,
//                                    "your internet connection is not working, saving locally. Please sync when Internet is available",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(Cash_Submit.this,
                                    "your internet connection is not working, saving locally. Please sync when Internet is available","");
                        } else if (error instanceof AuthFailureError) {
//                            Toast.makeText(Cash_Submit.this,
//                                    "Server AuthFailureError  Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(Cash_Submit.this,
                                    "Server AuthFailureError  Error","");
                        } else if (error instanceof ServerError) {
//                            Toast.makeText(Cash_Submit.this,
//                                    getResources().getString(R.string.Server_Errors),
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(Cash_Submit.this,
                                    getResources().getString(R.string.Server_Errors),"");
                        } else if (error instanceof NetworkError) {
//                            Toast.makeText(Cash_Submit.this,
//                                    "your internet connection is not working, saving locally. Please sync when Internet is available",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(Cash_Submit.this,
                                    "your internet connection is not working, saving locally. Please sync when Internet is available","");
                        } else if (error instanceof ParseError) {
//                            Toast.makeText(Cash_Submit.this,
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(Cash_Submit.this,
                                    "ParseError   Error","");
                        } else {
                            Global_Data.Custom_Toast(Cash_Submit.this, error.getMessage(),"");
                          //  Toast.makeText(Cash_Submit.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                        // finish();
                    }
                });


                RequestQueue requestQueue = Volley.newRequestQueue(Cash_Submit.this);

                int socketTimeout = 150000;//90 seconds - change to what you want
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

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("DATA", e.getMessage());
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
                Global_Data.address = locationAddress;
                Log.d("GLOBEL ADDRESS G", "V" + locationAddress);

            } else {
                Global_Data.address = "";
                Log.d("GLOBEL ADDRESS G", "address not found.");
            }


        }
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

                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                String imageFileName = timeStamp + ".png";
                                File storageDir = Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES);
                                pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                                File file = new File(pictureImagePath);
                               // Uri outputFileUri = Uri.fromFile(file);
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                               // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                                Uri photoURI = FileProvider.getUriForFile(Cash_Submit.this,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        file);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivityForResult(cameraIntent, 1);

                            } else {
                                Global_Data.Custom_Toast(getApplicationContext(), "no camera on this device","");
                              //  Toast.makeText(getApplicationContext(), "no camera on this device", Toast.LENGTH_LONG).show();
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
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(),"");
                      //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Cash_Submit.this);
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
}
