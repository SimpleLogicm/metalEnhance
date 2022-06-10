
package com.msimplelogic.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.Html;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.transition.Slide;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.multidex.MultiDex;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.msimplelogic.App.AppController;
import com.msimplelogic.activities.kotlinFiles.Url_Setting;
import com.msimplelogic.model.CustomSharedPreference;
import com.msimplelogic.model.UserObject;
import com.msimplelogic.services.StartLocationAlert;
import com.msimplelogic.services.getServices;
import com.msimplelogic.webservice.ConnectionDetector;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cpm.simplelogic.helper.BCrypt;
import cpm.simplelogic.helper.CheckNullValue;
import cpm.simplelogic.helper.GPSTracker;

public class LoginActivity extends BaseActivity {
    private DataBaseHelper dbvoc = null;
    SharedPreferences sp;
    ProgressDialog progress;
    Handler h;
    Dialog dialogEmail;
    String Current_Date = "";
    Dialog device_dialog;
    String devid;
    private int passwordNotVisible=1;
    ArrayList<HashMap<String, String>> arraylist1, arraylist2;
    ConnectivityManager cn;
    NetworkInfo nf;
    ProgressDialog dialog;
    Boolean isInternetPresent = false;
    TextView textViewVersion, tvSensorAuth, link_fpwd;
    ConnectionDetector cd;
    GPSTracker gps;
    String result = "";
    SessionManager session;
    Button buttonLogin, buttonReg;
    EditText editText1, editText2;
    String current_date;
    Bitmap blob_data_logo;
    LoginDataBaseAdapter loginDataBaseAdapter;

    ImageView logo_img, url_setting, fingurePrintIcon;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    String gps_redirection_flag = "";

    public static String CHANNEL_ID = "Metal";
    String CHANNEL_NAME = "Metal";
    String CHANNEL_DESC = "Metal App";
    private ArrayList<String> Language_List = new ArrayList<String>();
    ArrayAdapter<String> LanguageAdapter;
    private static final String KEY_NAME = "METAL";
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;

    private FingerprintHandler fingerprintHandler;

    private static final String FINGERPRINT_KEY = "key_name";

    private static final int REQUEST_USE_FINGERPRINT = 300;

    protected static Gson mGson;
    protected static CustomSharedPreference mPref;
    private static UserObject mUser;
    private static String userString;
    public TextView textView2;
    UpdateManager mUpdateManager;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        setupWindowAnimations();
        handleSSLHandshake();

        mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.IMMEDIATE);
        mUpdateManager.start();

        mUpdateManager.getAvailableVersionCode(new UpdateManager.onVersionCheckListener() {
            @Override
            public void onReceiveVersionCode(final int code) {
                Log.d("New Version Code","Code"+code);
            }
        });

        /* Firebase code Start */
        FirebaseApp.initializeApp(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);

                SharedPreferences spf = LoginActivity.this.getSharedPreferences("SimpleLogic", 0);
                SharedPreferences.Editor editor = spf.edit();
                editor.putString("fcm_token", newToken);

                editor.commit();

//       getActivity().getPreferences(Context.MODE_PRIVATE).edit().putString("fb", newToken).apply();
            }
        });

        //  getdetailsDevice();

        //creating notification channel if android version is greater than or equals to oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        /* firebase code end */

        dbvoc = new DataBaseHelper(this);
        logo_img = (ImageView) findViewById(R.id.imageView1);
        url_setting = (ImageView) findViewById(R.id.url_setting);
        link_fpwd = (TextView) findViewById(R.id.forget_pwd);
        fingurePrintIcon = (ImageView) findViewById(R.id.fingur_print_sign);
        tvSensorAuth = (TextView) findViewById(R.id.tv_sensorauth);
        device_dialog = new Dialog(LoginActivity.this);

        mGson = ((AppController)getApplication()).getGsonObject();
        mPref = ((AppController)getApplication()).getShared();
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        String logostr = spf1.getString("logo_data", "");

        if (logostr.length() > 0) {
            byte[] decodedString = Base64.decode(logostr, Base64.DEFAULT);
            blob_data_logo = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            logo_img.setImageBitmap(blob_data_logo);
        }

//        final Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(LoginActivity.this, "dfghd", Toast.LENGTH_SHORT).show();
//                handler.postDelayed(this, 5000);
//            }
//        };
//
//        //Start
//        handler.postDelayed(runnable, 5000);

        SharedPreferences pref_devid = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
        devid = pref_devid.getString("devid", "");

//        if (devid.length() > 0) {
//        } else {
//            link_fpwd.setVisibility(View.GONE);
//        }

        link_fpwd.setVisibility(View.VISIBLE);

        link_fpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Forget_Pwd.class));
            }
        });

        url_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, Url_Setting.class));
            }
        });




        if (Build.VERSION.SDK_INT < 23) {
            // Handle the mechanism where the SDK is older.
        }else{
            // Handle the mechanism where the SDK is 23 or later.
            fingerprintHandler = new FingerprintHandler(this);
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        }


        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        // Initializing both Android Keyguard Manager and Fingerprint Manager
//        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        dialog = new ProgressDialog(LoginActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        cd = new ConnectionDetector(getApplicationContext());
        gps_redirection_flag = "";
        requestGPSPermissionsigna();

        SharedPreferences spff = LoginActivity.this.getSharedPreferences("SimpleLogic", 0);
        String Language = spff.getString("Language", "");

        if (!Language.equalsIgnoreCase("hi") && !Language.equalsIgnoreCase("en")) {
            Language_Select_Dialog();
        }

        final FingerprintManager finalFingerprintManager = fingerprintManager;

        fingurePrintIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerprintHandler.completeFingerAuthentication(finalFingerprintManager, cryptoObject);
            }
        });

        //if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
        // Check whether the device has a Fingerprint sensor.

        boolean isFingerprintSupported = fingerprintManager != null && fingerprintManager.isHardwareDetected();

        //if(!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT) && !fingerprintManager.isHardwareDetected()){
        if(!isFingerprintSupported){
            /**
             * An error message will be displayed if the device does not contain the fingerprint hardware.
             * However if you plan to implement a default authentication method,
             * you can redirect the user to a default authentication activity from here.
             * Example:
             * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
             * startActivity(intent);
             */
            tvSensorAuth.setVisibility(View.GONE);
            fingurePrintIcon.setVisibility(View.GONE);
            tvSensorAuth.setText("Your Device does not have a Fingerprint Sensor");
        }else {
            tvSensorAuth.setVisibility(View.GONE);
            fingurePrintIcon.setVisibility(View.VISIBLE);
            tvSensorAuth.setText("You must Register and SignIn for FingurePrint");
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                tvSensorAuth.setText("Fingerprint authentication permission not enabled");
            }else{
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    tvSensorAuth.setText("Register at least one fingerprint in Settings");
                }else{
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {
                        tvSensorAuth.setText("Lock screen security not enabled in Settings");
                    }else{
//                        tvSensorAuth.setText("Fingerprint authentication Successful");
//                        fingerprintHandler.completeFingerAuthentication(finalFingerprintManager, cryptoObject);

                        generateKey();
                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintHandler helper = new FingerprintHandler(this);
                            //helper.completeFingerAuthentication(fingerprintManager, cryptoObject);
                            fingerprintHandler.completeFingerAuthentication(finalFingerprintManager, cryptoObject);
                        }
                    }
                }
            }
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = sdf.format(c.getTime());
        Current_Date = sdf.format(c.getTime());
        h = new Handler();

        // Session Manager
        session = new SessionManager(getApplicationContext());

        arraylist1 = new ArrayList<HashMap<String, String>>();
        arraylist2 = new ArrayList<HashMap<String, String>>();

        cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        nf = cn.getActiveNetworkInfo();

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewVersion = (TextView) findViewById(R.id.textViewVersion);
        buttonReg = (Button) findViewById(R.id.buttonReg);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);

//        editText1.setText("Priyankafsp");
//        editText2.setText("password");
//        editText1.setText("aakash");
//        editText2.setText("aakash12345");

//        editText1.setText("Atulfsp");
//        editText2.setText("atule00c");

//        editText1.setText("sujit123");
//        editText2.setText("12345678");
//        editText1.setText("sujit123");
//        editText2.setText("password123");
        // editText2.setText("12345678");
//        editText1.setText("Dnyanada");
//        editText2.setText("Dnyanada123");

//        editText1.setText("sujit123");
//        editText2.setText("sujit68fe");

        editText1.setText("Dharmendra");
        editText2.setText("dharmendra7aea");

//        editText1.setText("Priyanka");
//        editText2.setText("priyanka9a4a");

//        editText1.setText("Kartik");
//        editText2.setText("kartik4869");

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            textViewVersion.setText("Mobile Sales App, v. " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            textViewVersion.setText("Mobile Sales App, v. 1.3.1");
        }

        SharedPreferences spf = LoginActivity.this.getSharedPreferences("SimpleLogic", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("UserID", "admin");
        editor.putString("pwd", "test");
        // editor.putFloat("Target", 5000);
        //editor.putString("SimID", simSerial);
        editor.commit();

//    LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
//    boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//    // Check if enabled and if not send user to the GPS settings
//    if (!enabled) {
//       Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

//       startActivity(intent);
//    }

        // create a instance of SQLite Database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        //Reading all
        List<Local_Data> contacts = dbvoc.getAllMain();
        for (Local_Data cn : contacts) {
            Global_Data.local_user = "" + cn.getUser();
            Global_Data.local_pwd = "" + cn.getPwd();
            System.out.println("Local Values:-" + Global_Data.local_user + "," + Global_Data.local_pwd);
            //Toast.makeText(LoginActivity.this, "Login:"+Global_Data.local_user,Toast.LENGTH_SHORT).show();
        }
        //manager=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //int simState=manager.getSimState();

        editText2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (editText2.getRight() - editText2.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        View view = LoginActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        //autoCompleteTextView1.setText("");
                        if (passwordNotVisible == 1) {
                            editText2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            passwordNotVisible = 0;
                            editText2.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_visibility_black_24dp, 0);
                        } else {
                            editText2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            passwordNotVisible = 1;
                            editText2.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                        }

                        return true;
                    }
                }
                return false;
            }
        });

//    SharedPreferences pref_usrnm = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//    usr_email=pref_usrnm.getString("login_email", "");
//
//    if(devid.length()>0)
//    {
//       link_fpwd.setVisibility(View.VISIBLE);
//    }else{
//       link_fpwd.setVisibility(View.GONE);
//    }

//    link_fpwd.setOnClickListener(new OnClickListener() {
//       @Override
//       public void onClick(View view)
//       {
//          if (isInternetPresent)
//          {
//
//          }
//          else
//          {
//             // Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();--------------
//             Toast toast = Toast.makeText(LoginActivity.this,"You don't have internet connection.", Toast.LENGTH_LONG);
//             toast.setGravity(Gravity.CENTER, 0, 0);
//             toast.show();
//          }
//
////            Boolean compare_computed = BCrypt.checkpw(usr_name, pwd);
////            //Boolean compare_computed = BCrypt.checkpw(test_passwd, test_hash);
////            String s = String.valueOf(compare_computed);
////
//////          String phoneNo = textPhoneNo.getText().toString();
//////          String sms = "Your Password is "+password;
////
////            try {
////               SmsManager smsManager = SmsManager.getDefault();
////               //smsManager.sendTextMessage(phoneNo, null, sms, null, null);
////               Toast.makeText(getApplicationContext(), "SMS Sent!",
////                     Toast.LENGTH_LONG).show();
////            } catch (Exception e) {
////               Toast.makeText(getApplicationContext(),
////                     "SMS faild, please try again later!",
////                     Toast.LENGTH_LONG).show();
////               e.printStackTrace();
////            }
//       }
//    });

        buttonReg.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                isInternetPresent = cd.isConnectingToInternet();
                List<Local_Data> contacts2 = dbvoc.getAllMain();

                if (contacts2.size() > 0) {
                    // Toast.makeText(LoginActivity.this, "Your Device Already Register.", Toast.LENGTH_SHORT).show();
//                    Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.device_register_message), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.device_register_message),"yes");
                } else {
                    if (isInternetPresent) {
                        requestPhoneStatePermission();

                    } else {
                        // Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//                        Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.internet_connection_error),"yes");
                    }
                }
            }
        });

        buttonLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                if (CheckNullValue.findNullValue(editText1.getText().toString().trim()) == true) {
                    // Toast.makeText(LoginActivity.this, "Please Enter UserName", Toast.LENGTH_SHORT).show();

//                    Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.user_name_validation_message), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.user_name_validation_message),"yes");
                } else if (CheckNullValue.findNullValue(editText2.getText().toString().trim()) == true) {
                    // Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
//
//                    Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.user_password_validation_message), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.user_password_validation_message), "yes");
                } else {

                    gps = new GPSTracker(LoginActivity.this);
                    if (!gps.canGetLocation()) {

                        gps_redirection_flag = "yes";
                        Activity mContext = LoginActivity.this; //change this your activity name
                        StartLocationAlert startLocationAlert = new StartLocationAlert(mContext);
                        //startLocationAlert.

                    } else {
                        SharedPreferences pref_devid = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
                        String Device_id = pref_devid.getString("devid", "");
                        Global_Data.device_id = Device_id;
                        Global_Data.imei_no = Device_id;

//                        String Device_id = "354733074410810";
//                        Global_Data.device_id = "354733074410810";
//                        Global_Data.imei_no = "354733074410810";

                        List<Local_Data> contacts2 = dbvoc.getUSERBY_Device(Global_Data.imei_no);
                        if (contacts2.size() > 0) {
                            Validate_Email_Pass(editText1.getText().toString().trim(), editText2.getText().toString().trim());
                        } else {
                            //  Toast.makeText(LoginActivity.this, "Your Device id not found in database, Please register first.", Toast.LENGTH_SHORT).show();

//                            Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.device_not_found_message), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.device_not_found_message),"yes");
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //mUpdateManager.continueUpdate();
        // Reading all
        try {
            device_dialog.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List<Local_Data> contacts = dbvoc.getAllMain();
        for (Local_Data cn : contacts) {
            Global_Data.local_user = "" + cn.getUser();
            Global_Data.local_pwd = "" + cn.getPwd();
            //Global_Data.local_imeino = ""+cn.getImei();
            System.out.println("Local Values:-" + Global_Data.local_user + "," + Global_Data.local_pwd);
            //Toast.makeText(LoginActivity.this, "Login Invalid"+Global_Data.local_user,Toast.LENGTH_SHORT).show();
        }


//        FingerprintManager fingerprintManager = (FingerprintManager) LoginActivity.this.getSystemService(Context.FINGERPRINT_SERVICE);
//        if (!fingerprintManager.isHardwareDetected()) {
//            // Device doesn't support fingerprint authentication
//            fingurePrintIcon.setVisibility(View.GONE);
//            //Toast.makeText(this, "Device doesn't support fingerprint authentication", Toast.LENGTH_SHORT).show();
//        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
//            // User hasn't enrolled any fingerprints to authenticate with
//            fingurePrintIcon.setVisibility(View.VISIBLE);
//        } else {
//            // Everything is ready for fingerprint authentication
//            fingurePrintIcon.setVisibility(View.VISIBLE);
//        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    public void getserviceData() {

        sp = LoginActivity.this.getSharedPreferences("SimpleLogic", 0);
        boolean hasURL = sp.contains("Cust_Service_Url");

        if (hasURL == true) {
            String Cust_Service_Url = sp.getString("Cust_Service_Url", "");

            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(Cust_Service_Url)) {

                UserEmailDialog(Cust_Service_Url);

            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(getResources().getString(R.string.server_continue_message))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.OK_Button_label), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            UserEmailDialog("");
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            startActivity(new Intent(LoginActivity.this, Url_Setting.class));
                        }
                    });

            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle(getResources().getString(R.string.app_name));
            alert.show();
        }
    }

    public void reg_call(String domainn,String user_email) {
        try {
            dialog.setMessage(getResources().getString(R.string.dialog_wait_message));
            dialog.setTitle(getResources().getString(R.string.app_name));
            dialog.setCancelable(false);
            dialog.show();

//       TelephonyManager tm = (TelephonyManager) LoginActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
//       final String Device_id =  tm.getDeviceId();
//
            String domain = getResources().getString(R.string.service_domain);
            String domain1 = getResources().getString(R.string.service_domain1);

            SharedPreferences pref_devid = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
            devid = pref_devid.getString("devid", "");
            final String Device_id = pref_devid.getString("devid", "");

//            devid = "354733074410810";
//            final String Device_id = "354733074410810";

//            if (devid.length() > 0) {
//                link_fpwd.setVisibility(View.VISIBLE);
//            } else {
//                link_fpwd.setVisibility(View.GONE);
//            }

//             Global_Val global_Val = new Global_Val();
//             if(URL.equalsIgnoreCase(null) || URL.equalsIgnoreCase("null") || URL.equalsIgnoreCase("") || URL.equalsIgnoreCase(" ")) {
//                 domain = getResources().getString(R.string.service_domain);
//             }
//             else
//             {
//                 domain = URL.toString();
//             }


            String service_domain = "";
            if (domainn.equalsIgnoreCase("")) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Cust_Service_Url", domain1);
                editor.commit();

                service_domain = domain;

            } else {
                service_domain = domainn + "metal/api/v1/";
            }

            Log.i("volley", "domain: " + service_domain);
            Log.i("volley", "Device_id: " + Device_id);
            // Log.i("volley", "Sim_Number: " + Global_Val.Sim_Number);
            Log.i("volley", "Service url: " + service_domain + "menus/registration?email=" + URLEncoder.encode(user_email, "UTF-8"));

            JsonObjectRequest jsObjRequest = new JsonObjectRequest(service_domain + "menus/registration?email=" + URLEncoder.encode(user_email, "UTF-8"), null, new Response.Listener<JSONObject>() {
                // JsonObjectRequest jsObjRequest = new JsonObjectRequest(domain+"/menus/registration?imei_no="+ URLEncoder.encode("911305401754123", "UTF-8"),null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response.toString());
                    //  Log.i("volley", "response reg Length: " + response.length());

                    try {
                        String response_result = "";
                        if (response.has("result")) {
                            response_result = response.getString("result");
                        } else {
                            response_result = "data";
                        }

                        if (response_result.equalsIgnoreCase("User Not Found")) {

                            // Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG).show();

//                      Toast toast = Toast.makeText(LoginActivity.this,response_result, Toast.LENGTH_LONG);
//                      toast.setGravity(Gravity.CENTER, 0, 0);
//                      toast.show();

                            device_dialog = new Dialog(LoginActivity.this);
                            device_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            device_dialog.setCancelable(false);

                            //tell the Dialog to use the dialog.xml as it's layout description
                            device_dialog.setContentView(R.layout.device_dialog);

                            //device_dialog.setTitle("Device not registered");


                            TextView txt_click = (TextView) device_dialog.findViewById(R.id.txt_click);

                            txt_click.setClickable(true);
                            txt_click.setMovementMethod(LinkMovementMethod.getInstance());
                            String text = "administrator, <a href='http://trackfieldsales.com:8000/admin/login'>click here.</a>";
                            txt_click.setText(Html.fromHtml(text));

                            Button btn_yes = (Button) device_dialog.findViewById(R.id.btn_yes);

                            btn_yes.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    device_dialog.dismiss();


                                }
                            });

                            device_dialog.show();

                        } else {

                            dbvoc.getDeleteTable("USERS");

                            JSONArray users = response.getJSONArray("users");
//
                            Log.i("volley", "response reg users Length: " + users.length());

                            if (users.length() <= 0) {
                                dialog.dismiss();
                                //Toast.makeText(LoginActivity.this, "User not found, Please contact with it team.", Toast.LENGTH_SHORT).show();
//                                Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.user_not_found_message), Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.user_not_found_message),"yes");
                            } else {
                                Log.d("users", "users" + users.toString());

                                for (int i = 0; i < users.length(); i++) {

                                    JSONObject jsonObject = users.getJSONObject(i);

                                    loginDataBaseAdapter.insertEntry(jsonObject.getString("user_name"), jsonObject.getString("encrypted_password"), jsonObject.getString("date_of_joining"), jsonObject.getString("mob_no"), jsonObject.getString("email"), jsonObject.getString("reporting_to"),
                                            jsonObject.getString("first_name"), jsonObject.getString("last_name"), "", "", "", "", "",
                                            "", Device_id, "", jsonObject.getString("address"), "", "", "", "", "");
                                }

                                //Toast.makeText(getApplicationContext(), "Register successfully.", Toast.LENGTH_LONG).show();

//                                Handler handler = new Handler(Looper.getMainLooper());
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Gson gson = ((AppController)getApplication()).getGsonObject();
//                                        UserObject userData = new UserObject(Device_id,"","");
//                                        String userDataString = gson.toJson(userData);
//                                        CustomSharedPreference preff = ((AppController)getApplication()).getShared();
//                                        preff.setUserData(userDataString);
//                                    }
//                                }, 200);
//
//                                //fingurePrintIcon.setVisibility(View.VISIBLE);
//
//                                FingerprintManager fingerprintManager = (FingerprintManager) LoginActivity.this.getSystemService(Context.FINGERPRINT_SERVICE);
//                                if (!fingerprintManager.isHardwareDetected()) {
//                                    // Device doesn't support fingerprint authentication
//                                    fingurePrintIcon.setVisibility(View.GONE);
//                                    //Toast.makeText(this, "Device doesn't support fingerprint authentication", Toast.LENGTH_SHORT).show();
//                                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
//                                    // User hasn't enrolled any fingerprints to authenticate with
//                                    fingurePrintIcon.setVisibility(View.VISIBLE);
//                                } else {
//                                    // Everything is ready for fingerprint authentication
//                                    fingurePrintIcon.setVisibility(View.VISIBLE);
//                                }

//                                Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.Register_successfully_message), Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.Register_successfully_message),"yes");
                                dialog.dismiss();
                                dialogEmail.dismiss();
                            }
//                                                        //finish();
                        }

                        // }

                        // output.setText(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        finish();
                    }


                    dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("volley", "error: " + error);
                    // Toast.makeText(getApplicationContext(), "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();
//                    Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.Server_Error),"yes");
                    dialog.dismiss();

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            // queue.add(jsObjRequest);
            jsObjRequest.setShouldCache(false);
            int socketTimeout = 300000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);

        } catch (Exception e) {
            e.printStackTrace();
            dialog.dismiss();
        }
    }

    public void Validate_Email_Pass(String username, String passwordnew) {

        requestStoragegpsPermissionsigna(username, passwordnew);

    }

    private class LOGINOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(final String... response) {
            List<Local_Data> contacts2 = dbvoc.getUSERBY_Name(response[0]);
            if (contacts2.size() > 0) {
                for (Local_Data cn : contacts2) {
                    String str_beat = "" + cn.getUser();

                    SharedPreferences spf = LoginActivity.this.getSharedPreferences("SimpleLogic", 0);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("USER_EMAIL", cn.getuser_email());
                    editor.putString("USER_NAMEs", cn.getfirst_name() + " " + cn.getlast_name());
                    editor.commit();

                    Global_Data.GLOvel_USER_EMAIL = cn.getuser_email();
                    Global_Data.GLOVEL_USER_ID = cn.get_shedule_order_id();
                    Global_Data.customer_reportingTo = cn.getreporting_to();
                    Global_Data.USER_FIRST_NAME = cn.getfirst_name();
                    Global_Data.USER_LAST_NAME = cn.getlast_name();

                    List<Local_Data> cont = dbvoc.getManager_mobile(Global_Data.customer_reportingTo);

                    if (cont.size() <= 0) {
                        Global_Data.cus_MAnager_mobile = "";
                    } else {
                        for (Local_Data cnn : cont) {

                            if (!cnn.getMOBILE_NO().equalsIgnoreCase(null) && !cnn.getMOBILE_NO().equalsIgnoreCase("null") && !cnn.getMOBILE_NO().equalsIgnoreCase("") && !cnn.getMOBILE_NO().equalsIgnoreCase(" ")) {
                                Global_Data.cus_MAnager_mobile = cnn.getMOBILE_NO();
                                Global_Data.USER_MANAGER_NAME = cnn.getfirst_name() + " " + cnn.getlast_name();
                            } else {
                                Global_Data.cus_MAnager_mobile = "";
                            }
                        }
                    }

                    Log.d("user email", "user email" + cn.getuser_email());
                    Log.d("user id", "user id" + cn.get_shedule_order_id());

                    dbvoc.getDeleteTable("user_email");

                    loginDataBaseAdapter.insert_user_email(cn.getuser_email());
                    //Global_Data.local_pwd = ""+cn.getPwd();

                    //String test_passwd = "password";
                    //String test_hash = "$2a$10$qvO88dxBtHvZ2jAeyhLuMenX97XRxspL4zV3DAU.3ZQyeexZxBu86";

                    try {
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(cn.getPwd())) {
                            String dfss=cn.getPwd();
                            Boolean compare_computed = BCrypt.checkpw(response[1], dfss);
                            //Boolean compare_computed = BCrypt.checkpw(test_passwd, test_hash);
                            String s = String.valueOf(compare_computed);

                            Log.d("check authente", "value" + s);

                            if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("TRUE")) {

                                loginDataBaseAdapter.open();

                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                        editText1.setText("");
                                        editText2.setText("");
                                    }
                                });

//                              dialog.dismiss();
//                              startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                            finish();

                                List<Local_Data> conta = dbvoc.getDateBY_Device(devid, Global_Data.GLOvel_USER_EMAIL);
                                for (Local_Data cn1 : conta) {
                                    current_date = cn1.getCur_date();
                                    //current_date="28/02/2017";
                                }

                                if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(current_date)) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                    Date date1 = sdf.parse(current_date);

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                    String formattedDate = df.format(c.getTime());
                                    Date to_ddd = df.parse(formattedDate);

                                    SimpleDateFormat df_time = new SimpleDateFormat("dd-MM-yyyy hh.mm aa", Locale.ENGLISH);
                                    String formattedDatetime = df_time.format(c.getTime());
                                    //String formattedDate = df.format(c.getTime());

                                    if (to_ddd.compareTo(date1) > 0) {
//                               finish();
                                        isInternetPresent = cd.isConnectingToInternet();
                                        if (isInternetPresent) {

                                            dbvoc.update_user_createDate(formattedDate, Global_Data.GLOvel_USER_EMAIL);

                                            SharedPreferences.Editor editor2 = spf.edit();
                                            editor2.putString("Sync_date_time", formattedDatetime);
                                            editor2.commit();

                                            LoginActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Handler handler = new Handler(Looper.getMainLooper());
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Gson gson = ((AppController)getApplication()).getGsonObject();
                                                            UserObject userData = new UserObject(devid, response[0],response[1]);
                                                            String userDataString = gson.toJson(userData);
                                                            CustomSharedPreference preff = ((AppController)getApplication()).getShared();
                                                            preff.setUserData(userDataString);

                                                        }
                                                    }, 200);

                                                    //fingurePrintIcon.setVisibility(View.VISIBLE);

//                                                    FingerprintManager fingerprintManager = (FingerprintManager) LoginActivity.this.getSystemService(Context.FINGERPRINT_SERVICE);
//                                                    if (!fingerprintManager.isHardwareDetected()) {
//                                                        // Device doesn't support fingerprint authentication
//                                                        fingurePrintIcon.setVisibility(View.GONE);
//                                                        //Toast.makeText(this, "Device doesn't support fingerprint authentication", Toast.LENGTH_SHORT).show();
//                                                    } else if (!fingerprintManager.hasEnrolledFingerprints()) {
//                                                        // User hasn't enrolled any fingerprints to authenticate with
//                                                        fingurePrintIcon.setVisibility(View.VISIBLE);
//                                                    } else {
//                                                        // Everything is ready for fingerprint authentication
//                                                        fingurePrintIcon.setVisibility(View.VISIBLE);
//                                                    }
                                                    SharedPreferences spf = LoginActivity.this.getSharedPreferences("SimpleLogic", 0);
                                                    SharedPreferences.Editor editor = spf.edit();
                                                    editor.putString("USER_NAME", response[0]);
                                                    editor.commit();

                                                    getServices.sendRequestnew(LoginActivity.this, getResources().getString(R.string.first_sync_dialog_message));
                                                }
                                            });
                                        } else {
                                            LoginActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    dialog.dismiss();
                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                    finish();
                                                }
                                            });
                                        }
                                    } else {

                                        if(Global_Data.forgetPwdStatus.equalsIgnoreCase("forget_password"))
                                        {
                                            Global_Data.forgetPwdStatus="";
                                            LoginActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Handler handler = new Handler(Looper.getMainLooper());
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Gson gson = ((AppController)getApplication()).getGsonObject();
                                                            UserObject userData = new UserObject(devid, response[0],response[1]);
                                                            String userDataString = gson.toJson(userData);
                                                            CustomSharedPreference preff = ((AppController)getApplication()).getShared();
                                                            preff.setUserData(userDataString);

                                                        }
                                                    }, 200);

                                                }
                                            });
                                        }else{
                                            dialog.dismiss();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            finish();
                                        }

//                                        LoginActivity.this.runOnUiThread(new Runnable() {
//                                            public void run() {
//                                                if(Global_Data.forgetPwdStatus.equalsIgnoreCase("forget_password"))
//                                                {
//                                                    Global_Data.forgetPwdStatus="";
//
//
//                                                }else{
//                                                    dialog.dismiss();
//                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                                                    finish();
//                                                }
//
//                                            }
//                                        });

//                                        dialog.dismiss();
//                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                                        finish();
                                    }
//                           }catch (ParseException e)
//                            {
//                             e.printStackTrace();
//                            }
                                } else {
                                    isInternetPresent = cd.isConnectingToInternet();

                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                    String formattedDate = df.format(c.getTime());

                                    if (isInternetPresent) {
                                        dbvoc.update_user_createDate(formattedDate, Global_Data.GLOvel_USER_EMAIL);

                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {

                                                Handler handler = new Handler(Looper.getMainLooper());
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Gson gson = ((AppController)getApplication()).getGsonObject();

                                                        UserObject userData = new UserObject(devid, response[0],response[1]);
                                                        String userDataString = gson.toJson(userData);
                                                        CustomSharedPreference preff = ((AppController)getApplication()).getShared();
                                                        preff.setUserData(userDataString);

                                                    }
                                                }, 200);

                                                //fingurePrintIcon.setVisibility(View.VISIBLE);

//                                                FingerprintManager fingerprintManager = (FingerprintManager) LoginActivity.this.getSystemService(Context.FINGERPRINT_SERVICE);
//                                                if (!fingerprintManager.isHardwareDetected()) {
//                                                    // Device doesn't support fingerprint authentication
//                                                    fingurePrintIcon.setVisibility(View.GONE);
//                                                    //Toast.makeText(this, "Device doesn't support fingerprint authentication", Toast.LENGTH_SHORT).show();
//                                                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
//                                                    // User hasn't enrolled any fingerprints to authenticate with
//                                                    fingurePrintIcon.setVisibility(View.VISIBLE);
//                                                } else {
//                                                    // Everything is ready for fingerprint authentication
//                                                    fingurePrintIcon.setVisibility(View.VISIBLE);
//                                                }
                                                SharedPreferences spf = LoginActivity.this.getSharedPreferences("SimpleLogic", 0);
                                                SharedPreferences.Editor editor = spf.edit();
                                                editor.putString("USER_NAME", response[0]);
                                                editor.commit();

                                                getServices.sendRequestnew(LoginActivity.this, getResources().getString(R.string.first_sync_dialog_message));
                                            }
                                        });
                                    } else {
                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
//                                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                                toast.show();
                                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.internet_connection_error),"yes");
                                            }
                                        });
                                    }
                                }

                            } else {

                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                        editText2.setText("");
//                                        Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.password_not_match_message), Toast.LENGTH_SHORT);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();
                                        Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.password_not_match_message),"yes");
                                    }
                                });
                            }
                        } else {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();
                                    editText1.setText("");
                                    editText2.setText("");

//                                    Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.Credential_not_match_message), Toast.LENGTH_SHORT);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.Credential_not_match_message),"yes");
                                }
                            });
                        }

                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
                                editText1.setText("");
                                editText2.setText("");
                                //Toast.makeText(LoginActivity.this, "Your Credential doesnot match Please Try Again.", Toast.LENGTH_SHORT).show();

//                                Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.Credential_not_match_message), Toast.LENGTH_SHORT);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.Credential_not_match_message), "yes");
                            }
                        });

                    }
                    dialog.dismiss();

                }
            } else {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                        editText1.setText("");
                        editText2.setText("");
                        //Toast.makeText(LoginActivity.this, "Your Credential doesnot match Please Try Again.", Toast.LENGTH_SHORT).show();

//                        Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.user_name_not_match_message), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.user_name_not_match_message),"yes");
                    }
                });

            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            //dialog.dismiss();
            LoginActivity.this.runOnUiThread(new Runnable() {
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

// public String decryption(String strEncryptedText){
//    String seedValue = "YourSecKey";
//    String strDecryptedText="";
//    try {
//       strDecryptedText = AESHelper.decrypt(seedValue, strEncryptedText);
//    } catch (Exception e) {
//       e.printStackTrace();
//    }
//    return strDecryptedText;
// }

    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//       HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//          @Override
//          public boolean verify(String arg0, SSLSession arg1) {
//             return true;
//          }
//       });
        } catch (Exception ignored) {
        }
    }

    /**
     * Requesting GPS permission
     * This uses single permission model from dexter
     * Once the permission granted, opens the camera
     * On permanent denial opens settings dialog
     */
    private void requestPhoneStatePermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_PHONE_STATE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

//                        Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
//                        startActivity(loginIntent);

                        getserviceData();
                        return;

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog(LoginActivity.this);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }




                }).check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

    private void requestStoragegpsPermissionsigna(final String username, final String passwordnew) {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            dialog.setMessage(getResources().getString(R.string.please_wait_dialog_messagen));
                            dialog.setTitle(getResources().getString(R.string.app_name));
                            dialog.setCancelable(false);
                            dialog.show();

                            new LOGINOperation().execute(username, passwordnew);
                        }

                        // check for permanent denial of any permission
//                        if (report.isAnyPermissionPermanentlyDenied()) {
//                            // show alert dialog navigating to Settings
//                            showSettingsDialog(LoginActivity.this);
//                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(),"");
                    }
                })
                .onSameThread()
                .check();
    }

    private void requestGPSPermissionsigna() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            gps = new GPSTracker(LoginActivity.this);

                            Global_Data.LOCATION_SERVICE_HIT = "TRUE";

                            if (!gps.canGetLocation()) {

                                Activity mContext = LoginActivity.this; //change this your activity name
                                StartLocationAlert startLocationAlert = new StartLocationAlert(mContext);
                            }


                            // requestLocationUpdates();


                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog(LoginActivity.this);
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
                        //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(),"");
                    }
                })
                .onSameThread()
                .check();
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
                        // All required changes were successfully made

                        if (gps_redirection_flag.equalsIgnoreCase("yes")) {
                            SharedPreferences pref_devid = getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE);
                            String Device_id = pref_devid.getString("devid", "");
                            Global_Data.device_id = Device_id;
                            Global_Data.imei_no = Device_id;

//                            String Device_id = "354733074410810";
//                            Global_Data.device_id = "354733074410810";
//                            Global_Data.imei_no = "354733074410810";

                            List<Local_Data> contacts2 = dbvoc.getUSERBY_Device(Global_Data.imei_no);

                            if (contacts2.size() > 0) {
                                Validate_Email_Pass(editText1.getText().toString().trim(), editText2.getText().toString().trim());
                            } else {
                                //  Toast.makeText(LoginActivity.this, "Your Device id not found in database, Please register first.", Toast.LENGTH_SHORT).show();

//                                Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.device_not_found_message), Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.device_not_found_message),"yes");
                            }
                        }

                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        //Toast.makeText(LoginActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    public void Language_Select_Dialog() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.language_dialog);
        // dialog.setTitle(getResources().getString(R.string.PDistributors));

        Language_List.clear();
        Language_List.add(getResources().getString(R.string.Select_Language));
        Language_List.add(getResources().getString(R.string.English));
        Language_List.add(getResources().getString(R.string.Hindi));

        final Spinner spnLanguage = dialog.findViewById(R.id.spnLanguage);
        LanguageAdapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_spinner_item, Language_List);
        LanguageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLanguage.setAdapter(LanguageAdapter);

        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOk);
        dialogButtonOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spnLanguage.getSelectedItem().toString()
                        .equalsIgnoreCase(getResources().getString(R.string.Select_Language))) {
//                    Toast toast = Toast.makeText(LoginActivity.this,
//                            getResources().getString(R.string.Please_Select_Language), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(LoginActivity.this,
                            getResources().getString(R.string.Please_Select_Language),"yes");
                } else if (spnLanguage.getSelectedItem().toString()
                        .equalsIgnoreCase(getResources().getString(R.string.Hindi))) {

                    SharedPreferences spf = LoginActivity.this.getSharedPreferences("SimpleLogic", 0);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("Language", "hi");
                    editor.commit();

                    Locale myLocale = new Locale("hi");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);

                    recreate();
                    dialog.dismiss();
                } else if (spnLanguage.getSelectedItem().toString()
                        .equalsIgnoreCase(getResources().getString(R.string.English))) {

                    SharedPreferences spf = LoginActivity.this.getSharedPreferences("SimpleLogic", 0);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("Language", "en");
                    editor.commit();

                    Locale myLocale = new Locale("en");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);

                    recreate();

                    dialog.dismiss();
                }
            }
        });

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        dialogButtonCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void UserEmailDialog(String domainurl) {
        dialogEmail = new Dialog(LoginActivity.this);
        dialogEmail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEmail.setContentView(R.layout.useremail_dialog);

        EditText btnEditDialog = (EditText) dialogEmail.findViewById(R.id.et_useremail);
        Button btnOkDialog = (Button) dialogEmail.findViewById(R.id.btnok_useremaildialog);
        btnOkDialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnEditDialog.getText().toString().trim().length()>0)
                {
                    reg_call(domainurl,btnEditDialog.getText().toString().trim());
                }else{
//                    Toast toast = Toast.makeText(LoginActivity.this, "Please Enter EmailId", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    Global_Data.Custom_Toast(LoginActivity.this, "Please Enter EmailId","yes");
                }
            }
        });

        Button btnCancelDialog = (Button) dialogEmail.findViewById(R.id.btncancel_useremaildialog);
        btnCancelDialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEmail.dismiss();
            }
        });
        dialogEmail.show();
    }


    public class FingerprintHandler extends FingerprintManager.AuthenticationCallback{

        private final String TAG = FingerprintHandler.class.getSimpleName();

        private Context context;

        public FingerprintHandler(Context context){
            this.context = context;
        }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            Log.d(TAG, "Error message " + errorCode + ": " + errString);
            if(errString.equals("Too many attempts. Try again later.")){
                tvSensorAuth.setText(errString);
            }

        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
            //Toast.makeText(context, "Auth Successful", Toast.LENGTH_LONG).show();
            tvSensorAuth.setText("Too many attempts. Try again later.");
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);

            userString = mPref.getUserData();
            mUser = mGson.fromJson(userString, UserObject.class);
            //Global_Data.forgetPwdStatus="forget_password";
            if(!Global_Data.forgetPwdStatus.equalsIgnoreCase("forget_password")) {
                if (mUser != null) {
                    fingurePrintIcon.setVisibility(View.VISIBLE);
                    tvSensorAuth.setVisibility(View.VISIBLE);
                    fingurePrintIcon.setImageResource(R.drawable.auth_success);
                    tvSensorAuth.setText("Fingureprint Authentication Successful");
                    //Toast.makeText(context, "Auth Successful", Toast.LENGTH_LONG).show();
                    if (mUser.isLoginOption()) {
                        // login with fingerprint and password
//                    showPasswordAuthentication(context);
                    } else {
                        List<Local_Data> contacts2 = dbvoc.getUSERBY_Device(devid);

                        if (contacts2.size() > 0) {

                            UserObject mUserObject = mGson.fromJson(userString, UserObject.class);
                            String fdg = mUserObject.getPassword();

                            Validate_Email_Pass(mUserObject.getUsername(), fdg);

                        } else {
                            //  Toast.makeText(LoginActivity.this, "Your Device id not found in database, Please register first.", Toast.LENGTH_SHORT).show();

//                            Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.device_not_found_message), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(LoginActivity.this, getResources().getString(R.string.device_not_found_message),"yes");
                        }

                        // login with only fingerprint
//                    Intent userIntent = new Intent(context, MainActivity.class);
//                    userIntent.putExtra("USER_BIO", userString);
//                    context.startActivity(userIntent);

                    }
                } else {
                    //   Toast.makeText(context, "You must register and login first for fingerprint", Toast.LENGTH_LONG).show();
                    Global_Data.Custom_Toast(context, "You must register and login first for fingerprint","");
                }
            }else{
                //Global_Data.forgetPwdStatus="";
                //    Toast.makeText(context, "Please Signin First with New Password", Toast.LENGTH_SHORT).show();
                Global_Data.Custom_Toast(context, "Please Signin First with New Password","");
            }
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            fingurePrintIcon.setVisibility(View.VISIBLE);
            tvSensorAuth.setVisibility(View.VISIBLE);

            userString = mPref.getUserData();
            if(userString.length()>0)
            {
                fingurePrintIcon.setImageResource(R.drawable.auth_error);
                tvSensorAuth.setText("Fingerprint Authentication failed.");
            }
        }

        public void completeFingerAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try{
                fingerprintManager.authenticate(cryptoObject, new CancellationSignal(), 0, this, null);
            }catch (SecurityException ex) {
                Log.d(TAG, "An error occurred:\n" + ex.getMessage());
            } catch (Exception ex) {
                Log.d(TAG, "An error occurred\n" + ex.getMessage());
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }


    private boolean isSensorAvialable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED &&
                    this.getSystemService(FingerprintManager.class).isHardwareDetected();
        } else {
            return FingerprintManagerCompat.from(this).isHardwareDetected();
        }
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }

    public void getdetailsDevice()
    {
        String  details =  "VERSION.RELEASE : "+Build.VERSION.RELEASE
                +"\nVERSION.INCREMENTAL : "+Build.VERSION.INCREMENTAL
                +"\nVERSION.SDK.NUMBER : "+Build.VERSION.SDK_INT
                +"\nBOARD : "+Build.BOARD
                +"\nBOOTLOADER : "+Build.BOOTLOADER
                +"\nBRAND : "+Build.BRAND
                +"\nCPU_ABI : "+Build.CPU_ABI
                +"\nCPU_ABI2 : "+Build.CPU_ABI2
                +"\nDISPLAY : "+Build.DISPLAY
                +"\nFINGERPRINT : "+Build.FINGERPRINT
                +"\nHARDWARE : "+Build.HARDWARE
                +"\nHOST : "+Build.HOST
                +"\nID : "+Build.ID
                +"\nMANUFACTURER : "+Build.MANUFACTURER
                +"\nMODEL : "+Build.MODEL
                +"\nPRODUCT : "+Build.PRODUCT
                +"\nSERIAL : "+Build.SERIAL
                +"\nTAGS : "+Build.TAGS
                +"\nTIME : "+Build.TIME
                +"\nTYPE : "+Build.TYPE
                +"\nUNKNOWN : "+Build.UNKNOWN
                +"\nUSER : "+Build.USER;

        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.d("Android","Android ID : "+android_id);

        Log.d("Devices Details","Device Detail"+details);
    }
}



