package com.msimplelogic.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.helper.MultipartUtility;
import com.msimplelogic.helper.PathUtil;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static java.sql.DriverManager.println;

public class ExpenseDetailsActivity extends BaseActivity {
    static String final_response = "";
    String response_result1 = "";
    String PINStringEx;
    File myFile;
    String _pickedDate1="";
    int year_first;
    int month_first;
    ImageView hedder_theameexp;
    int date_first;
    MultipartUtility multipart;
    File expenseImageFile, expensePdfFile;
    ProgressDialog dialog;
    static String response_result_image = "";
    Uri selectedImage;
    Date date2;
    Uri urifgh;
    String response_result = "";
    String pdfPath;
    private String Current_Date = "";
    private String Expenses_text;
    private String pictureImagePath = "";
    private String mCurrentPhotoPath = "";
    private String pdfmCurrentPhotoPath = "";
    String image_check = "";
    LoginDataBaseAdapter loginDataBaseAdapter;
    ConnectionDetector cd;
    Boolean B_flag;
    Boolean isInternetPresent = false;
    TextView schedule_txt, textView1sf;
    EditText etExpenseMisclnsDate, etExpenseMisclnsCost, etExpenseMisclnsDescr, etHotelCheckinDate, etHotelCheckOutDate, etHotelCost, etHotelName,
            etHotelAddress, etHotelDescr, etFoodDate, etFoodCost, etFoodHotelName, etFoodDescr, etTravelDate, etTravelCost, etTravelFrom, etTravelTo, etTravelMode, etTravelDescr;
    ImageView ivMiscCamera, ivMiscPdf, ivHotelCamera, ivHotelPdf, ivFoodCamera, ivFoodPdf, ivTravelCamera, ivTravelPdf;
    DatePickerDialog.OnDateSetListener date, date1;
    Calendar myCalendar;
    CardView cardMiscelaneous, cardHotel, cardTravel, cardFood;
    Button submitExpensesDetails;
    private int PICK_PDF_REQUEST = 1;
    private Uri filePath1;
    private String pdfPathStr = "";
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    private String upload_URL = "http://150.242.140.105:8005/metal/api/v1/customer_service_feedbacks/customer_service_upload_images";
    //private String upload_URL = "https://demonuts.com/Demonuts/JsonTest/Tennis/uploadfile.php?";
    private RequestQueue rQueue;
    private ArrayList<HashMap<String, String>> arraylist;
    String url = "https://www.google.com";
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        cardMiscelaneous = (CardView) findViewById(R.id.misc_cardview);
        cardHotel = (CardView) findViewById(R.id.hotel_cardview);
        cardTravel = (CardView) findViewById(R.id.travel_cardview);
        cardFood = (CardView) findViewById(R.id.food_cardview);

        etExpenseMisclnsDate = (EditText) findViewById(R.id.et_miscelns_date);
        etExpenseMisclnsCost = (EditText) findViewById(R.id.et_miscl_cost);
        etExpenseMisclnsDescr = (EditText) findViewById(R.id.et_miscl_descr);
        ivMiscCamera = (ImageView) findViewById(R.id.camera_expense);
        ivMiscPdf = (ImageView) findViewById(R.id.pdf_expense);
        hedder_theameexp = (ImageView) findViewById(R.id.hedder_theameexp);

        etHotelCheckinDate = (EditText) findViewById(R.id.et_checkin_date);
        etHotelCheckOutDate = (EditText) findViewById(R.id.et_checkout_date);
        etHotelCost = (EditText) findViewById(R.id.et_hotel_cost);
        etHotelName = (EditText) findViewById(R.id.et_hotel_name);
        etHotelAddress = (EditText) findViewById(R.id.et_hotel_adr);
        etHotelDescr = (EditText) findViewById(R.id.et_hotel_descr);
        ivHotelCamera = (ImageView) findViewById(R.id.camera_expensehotel);
        ivHotelPdf = (ImageView) findViewById(R.id.pdf_expensehotel);

        etTravelDate = (EditText) findViewById(R.id.et_travel_date);
        etTravelCost = (EditText) findViewById(R.id.et_travel_cost);
        etTravelFrom = (EditText) findViewById(R.id.et_travels_from);
        etTravelTo = (EditText) findViewById(R.id.et_travels_to);
        etTravelMode = (EditText) findViewById(R.id.et_travel_mode);
        etTravelDescr = (EditText) findViewById(R.id.et_travel_descr);
        ivTravelCamera = (ImageView) findViewById(R.id.camera_expensetravel);
        ivTravelPdf = (ImageView) findViewById(R.id.pdf_expensetravel);

        etFoodDate = (EditText) findViewById(R.id.et_food_date);
        etFoodCost = (EditText) findViewById(R.id.et_food_cost);
        etFoodHotelName = (EditText) findViewById(R.id.et_food_hotelname);
        etFoodDescr = (EditText) findViewById(R.id.et_food_descr);
        ivFoodCamera = (ImageView) findViewById(R.id.camera_expensepdf_expensefood);
        ivFoodPdf = (ImageView) findViewById(R.id.pdf_expensefood);

        submitExpensesDetails = (Button) findViewById(R.id.submit_expenses_details);

        cd = new ConnectionDetector(getApplicationContext());

        Expenses_text = getResources().getString(R.string.Travel);

        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {

            hedder_theameexp.setImageResource(R.drawable.dark_hedder);
        }
//
//        if (Global_Data.cam=="Yes" && Global_Data.cam!=""){
//            Global_Data.Custom_Toast(ExpenseDetailsActivity.this,"Image Captured Successfully","Yes");
//
//        }
        // for label change
        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        String schedulestr = spf1.getString("var_schedule", "");

//        if(schedulestr.length()>0)
//        {
//            Log.d("App Language", "App Language " + Locale.getDefault().getDisplayLanguage());
//            String locale = Locale.getDefault().getDisplayLanguage();
//            if (locale.equalsIgnoreCase("English")) {
//                schedule_txt.setText(schedulestr.toUpperCase());
//            } else {
//                schedule_txt.setText(getResources().getString(R.string.SCHEDULE));
//            }
//
//        }else{
//            schedule_txt.setText(getResources().getString(R.string.SCHEDULE));
//        }

//        try {
//            ActionBar mActionBar = getActionBar();
//            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            // mActionBar.setDisplayShowHomeEnabled(false);
//            // mActionBar.setDisplayShowTitleEnabled(false);
//            LayoutInflater mInflater = LayoutInflater.from(this);
//            Intent i = getIntent();
//            String name = i.getStringExtra("retialer");
//            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
//            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
//            mTitleTextView.setText(Global_Data.ExpenseName+" Details");
//
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = ExpenseDetailsActivity.this.getSharedPreferences("SimpleLogic", 0);
//
////	       if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
////	       	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
////			}
//
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
//
//                } else {
//                    int age = (int) Math.round(age_float);
//
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    }
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
////	       	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
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

        if (Global_Data.ExpenseId.length() > 0) {

            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                dialog = new ProgressDialog(ExpenseDetailsActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                dialog.setMessage("Please wait Loading....");
                dialog.setTitle("Metal App");
                dialog.setCancelable(false);
                dialog.show();
                ExpenseShowResult();
            } else {
              //  Toast.makeText(ExpenseDetailsActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
                Global_Data.Custom_Toast(ExpenseDetailsActivity.this, "You don't have internet connection.","");
            }
        }


        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }
        };

        ivMiscCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
            }
        });

        ivMiscPdf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        ivHotelCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
            }
        });

        ivHotelPdf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        ivTravelCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
            }
        });

        ivTravelPdf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                File photoFile = null;
                try {
                    photoFile = createImageFile1();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.i("Image TAG", "IOException");
                    pdfPath = "";
                }

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                Uri photoURI = FileProvider.getUriForFile(ExpenseDetailsActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, 1);
                //showFileChooser();
            }
        });

        ivFoodCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
            }
        });

        ivFoodPdf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        if (Global_Data.ExpenseName.equalsIgnoreCase("Miscellaneous")) {
            cardMiscelaneous.setVisibility(View.VISIBLE);

            etExpenseMisclnsDate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar c = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(ExpenseDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String _year = String.valueOf(year);
                            String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                            String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                            //     String _pickedDate = year + "-" + _month + "-" + _date;
                            String _pickedDate = _date + "/" + _month + "/" + year;
                            Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12

                            etExpenseMisclnsDate.setText(_pickedDate);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dialog.show();
                }
            });

        } else if (Global_Data.ExpenseName.equalsIgnoreCase("Hotel")) {
            cardHotel.setVisibility(View.VISIBLE);

            etHotelCheckinDate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    Calendar c = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(ExpenseDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String _year = String.valueOf(year);
                            String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                            String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                            //     String _pickedDate = year + "-" + _month + "-" + _date;
                            _pickedDate1 = _date + "/" + _month + "/" + year;

                            year_first=year;
                            month_first=month;
                            date_first=dayOfMonth;

                            Log.e("PickedDate: ", "Date: " + _pickedDate1); //2019-02-12





                            etHotelCheckinDate.setText(_pickedDate1);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dialog.show();
                }
            });
            //  mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            etHotelCheckOutDate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(etHotelCheckinDate.getText().length()==0){
                        Global_Data.Custom_Toast(ExpenseDetailsActivity.this,"Please select Check in Date ","yes");
                    }else{
                        Calendar c = Calendar.getInstance();
                        DatePickerDialog dialog = new DatePickerDialog(ExpenseDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String _year = String.valueOf(year);
                                String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                                String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                                //     String _pickedDate = year + "-" + _month + "-" + _date;
                                String _pickedDate = _date + "/" + _month + "/" + year;
                                Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12

                                etHotelCheckOutDate.setText(_pickedDate);

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String dateString = etHotelCheckinDate.getText().toString();
                        long timeMilli = 0;
                        try{
//formatting the dateString to convert it into a Date
                            Date date = sdf.parse(dateString);
                            timeMilli = date.getTime();
                        }catch(ParseException e){
                            e.printStackTrace();
                        }
          dialog.getDatePicker().setMinDate(timeMilli);
                        dialog.show();
                    }


                }
            });
        } else if (Global_Data.ExpenseName.equalsIgnoreCase("Food")) {
            cardFood.setVisibility(View.VISIBLE);
            etFoodDate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar c = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(ExpenseDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String _year = String.valueOf(year);
                            String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                            String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                            //     String _pickedDate = year + "-" + _month + "-" + _date;
                            String _pickedDate = _date + "/" + _month + "/" + year;
                            Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12

                            etFoodDate.setText(_pickedDate);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dialog.show();
                }
            });

        } else if (Global_Data.ExpenseName.equalsIgnoreCase("Conveyance")) {
            cardTravel.setVisibility(View.VISIBLE);
            etTravelDate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar c = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(ExpenseDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String _year = String.valueOf(year);
                            String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                            String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                            //     String _pickedDate = year + "-" + _month + "-" + _date;
                            String _pickedDate = _date + "/" + _month + "/" + year;
                            Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12

                            etTravelDate.setText(_pickedDate);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dialog.show();
                }
            });
        }

        submitExpensesDetails.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences spf = ExpenseDetailsActivity.this.getSharedPreferences("SimpleLogic", 0);
                final String user_email = spf.getString("USER_EMAIL", null);
                Date date1 = new Date();

//                if(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etTravelDate.getText().toString()))
//                {
//                    date2 = new Date(etTravelDate.getText().toString());
//                    Calendar cal1 = Calendar.getInstance();
//                    Calendar cal2 = Calendar.getInstance();
//                    cal1.setTime(date1);
//                    cal2.setTime(date1);
//                }

                if (Global_Data.ExpenseStatus.length() > 0) {
                    dialog = new ProgressDialog(ExpenseDetailsActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    dialog.setMessage("Expenses Sync in Progress, Please Wait");
                    dialog.setTitle("Metal App");
                    dialog.setCancelable(false);
                    dialog.show();

                    new EditViewImagePdfAyncTask().execute();
                } else {


                    if (Global_Data.ExpenseName.equalsIgnoreCase("Conveyance")) {
                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etTravelDate.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Select_Date), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Select_Date),"yes");
                        }
//                    else
//                    if(date2.compareTo(date1)>0)
//                    {
//                        Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Future_date_validation), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//                    }
                        else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etTravelCost.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Cost), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Cost),"yes");
                        } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etTravelDescr.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Description), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Description),"yes");
                        } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etTravelFrom.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Form_Field_Data), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Form_Field_Data),"yes");
                        } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etTravelTo.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_To_Field_Data), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_To_Field_Data), "yes");
                        } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etTravelMode.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Mode_of_Travel), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Mode_of_Travel),"yes");
                        } else {
                            dialog = new ProgressDialog(ExpenseDetailsActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            dialog.setMessage("Expenses Sync in Progress, Please Wait");
                            dialog.setTitle("Metal App");
                            dialog.setCancelable(false);
                            dialog.show();

                            new ImagePdfAyncTask().execute();
                            //new doFileUploadPdf().execute();

                        }
                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Miscellaneous")) {
                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etExpenseMisclnsDate.getText().toString())) {
                            // Toast.makeText(getApplicationContext(),"Please Select Date", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Select_Date), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Select_Date),"yes");

                        }
//                    else
//                    if(date2.compareTo(date1)>0)
//                    {
//                        Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Future_date_validation), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//                    }
                        else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etExpenseMisclnsCost.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Cost), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Cost),"yes");
                        } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etExpenseMisclnsDescr.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Description), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Description),"yes");
                        } else {

                            dialog = new ProgressDialog(ExpenseDetailsActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            dialog.setMessage("Expenses Sync in Progress, Please Wait");
                            dialog.setTitle("Metal App");
                            dialog.setCancelable(false);
                            dialog.show();

                            new ImagePdfAyncTask().execute();


//                        Long randomPIN = System.currentTimeMillis();
//                        String PINString = String.valueOf(randomPIN);
//                        loginDataBaseAdapter.insertExpencesMiscs("1", "1", user_email, "1", etExpenseMisclnsDate.getText().toString(), etExpenseMisclnsCost.getText().toString(), etExpenseMisclnsDescr.getText().toString(), "", "", "",
//                                "", Current_Date, Current_Date, PINString, pictureImagePath, pdfPath);
//
//                        Toast toast = Toast.makeText(getApplicationContext(),
//                                getResources().getString(R.string.Miscellaneous_Expense_Save_Successfully), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//
//                        Intent a = new Intent(ExpenseDetailsActivity.this,MainActivity.class);
//                        startActivity(a);
//                        finish();

//						  isInternetPresent = cd.isConnectingToInternet();
//
//							if (isInternetPresent)
//		                    {
//								call_service_Expenses_MISC(user_email, exp_date.getText().toString(), exp_cost.getText().toString(), exp_discr.getText().toString());
//		                    }
//			   	        	else
//			   	        	{
//			   	        	 //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//
//								Toast toast = Toast.makeText(Expenses.this,"You don't have internet connection.", Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
//			   	        	}
                        }
                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Hotel")) {

                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etHotelCheckinDate.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Select_Date), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Select_Date), "yes");
                        }
                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etHotelCheckOutDate.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Select_Date), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Select_Date),"yes");
                        }
//                    else
//                    if(date2.compareTo(date1)>0)
//                    {
//                        Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Future_date_validation), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//                    }
                        else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etHotelCost.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Cost), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Cost), "yes");
                        } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etHotelDescr.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Description), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Description),"yes" );
                        }
//                    else
//                    if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etTravelFrom.getText().toString()))
//                    {
//                        Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Form_Field_Data), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//                    }
//                    else
//                    if(!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etTravelTo.getText().toString()))
//                    {
//                        Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_To_Field_Data), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//                    }

                        else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etHotelAddress.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Hotel_Address), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Hotel_Address),"yes");
                        } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etHotelName.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Hotel_Name), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Hotel_Name),"yes");
                        } else {

                            dialog = new ProgressDialog(ExpenseDetailsActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            dialog.setMessage("Expenses Sync in Progress, Please Wait");
                            dialog.setTitle("Metal App");
                            dialog.setCancelable(false);
                            dialog.show();

                            new ImagePdfAyncTask().execute();


//                        Long randomPIN = System.currentTimeMillis();
//                        String PINString = String.valueOf(randomPIN);
//
//                        loginDataBaseAdapter.insertExpenceHotel("1", "1",user_email,
//                                "1", etHotelCheckinDate.getText().toString(), etHotelCheckOutDate.getText().toString(), etHotelCost.getText().toString(), etHotelName.getText().toString(), etHotelAddress.getText().toString(), etHotelDescr.getText().toString(),
//                                "", "", "", "", Current_Date, PINString, pictureImagePath, pdfPath);
//
//                        Toast toast = Toast.makeText(getApplicationContext(),
//                                getResources().getString(R.string.Hotel_Expense_Save_Successfully), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//
//                        Intent a = new Intent(ExpenseDetailsActivity.this,MainActivity.class);
//                        startActivity(a);
//                        finish();
                        }

                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Food")) {
                        if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etFoodDate.getText().toString())) {
                            // Toast.makeText(getApplicationContext(),"Please Select Date", Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Select_Date), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Select_Date),"yes");
                        }
//                    else
//                    if(date2.compareTo(date1)>0)
//                    {
//                        Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Future_date_validation), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
//                    }
                        else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etFoodCost.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Cost), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Cost),"yes");
                        } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etFoodHotelName.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Hotel_Name), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Hotel_Name),"yes");
                        } else if (!Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(etFoodDescr.getText().toString())) {
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Description), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, getResources().getString(R.string.Enter_Description),"yes");
                        } else {
                            dialog = new ProgressDialog(ExpenseDetailsActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            dialog.setMessage("Expenses Sync in Progress, Please Wait");
                            dialog.setTitle("Metal App");
                            dialog.setCancelable(false);
                            dialog.show();

                            new ImagePdfAyncTask().execute();

                        }
                    }
                }
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

        switch (item.getItemId()) {
            case R.id.add:
                String targetNew = "";
                SharedPreferences sp = ExpenseDetailsActivity.this.getSharedPreferences("SimpleLogic", 0);
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
        // TODO Auto-generated method stub
        pictureImagePath = "";
        pdfPath = "";
        Global_Data.ExpenseId = "";
        Global_Data.ExpenseStatus = "";
        Global_Data.ExpenseName = "";
        Intent i = new Intent(ExpenseDetailsActivity.this, ExpensesNewActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void updateLabel() {
        //String myFormat = "yyyy/MM/dd"; //In which you need put here
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (Global_Data.ExpenseName.equalsIgnoreCase("Miscellaneous")) {
            etExpenseMisclnsDate.setText(sdf.format(myCalendar.getTime()));
        } else if (Global_Data.ExpenseName.equalsIgnoreCase("Hotel")) {
            etHotelCheckinDate.setText(sdf.format(myCalendar.getTime()));
        } else if (Global_Data.ExpenseName.equalsIgnoreCase("Food")) {
            etFoodDate.setText(sdf.format(myCalendar.getTime()));
        } else if (Global_Data.ExpenseName.equalsIgnoreCase("Conveyance")) {
            etTravelDate.setText(sdf.format(myCalendar.getTime()));
            String dsfds = etTravelDate.getText().toString();
            System.out.println(dsfds);
        }
    }

    private void updateLabel1() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etHotelCheckOutDate.setText(sdf.format(myCalendar.getTime()));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            urifgh = data.getData();
            String uriString = urifgh.toString();
            myFile = new File(uriString);
            pdfPath = myFile.getAbsolutePath();
            String displayName = null;

            Uri fileuri = data.getData();
//            String mm = getMimeType(ExpenseDetailsActivity.this, fileuri);
//            String r_path = getRealPathFromURI(ExpenseDetailsActivity.this, fileuri);
//            String file_name = getFileName(fileuri);

            try {
                pdfPath = "file:" + PathUtil.getPath(ExpenseDetailsActivity.this, fileuri);
                Global_Data.Custom_Toast(ExpenseDetailsActivity.this, "Pdf Uploaded Successfully", "Yes");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

//            File folder = new File(Environment.getExternalStorageDirectory() + "/EmailClient/");
//
//            folder.mkdirs();
//            File file = new File(folder, String.valueOf(myFile));
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(urifgh, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("nameeeee>>>>  ", displayName);
                        //saveTextToFile(ExpenseDetailsActivity.this,displayName,uriString);
                        //uploadMultipart();
                        // uploadPDF(displayName,urifgh);

                        final String finalDisplayName = displayName;
                        ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                //new doFileUpload().execute(feedPhotoCode,feedPhotoPath);
                                //new doFileUploadPdf.execute();
                                //new doFileUploadPdf().execute(pdfPath,pictureImagePath);
                            }
                        });

                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("nameeeee>>>>  ", displayName);
            }

            //new Expenses.LongOperation().execute();

//			try {
//
//				//dbvoc.updateORDER_order_image(pictureImagePath, Global_Data.GLObalOrder_id);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            File imgFile = new File(mCurrentPhotoPath);
            if (imgFile.exists()) {
                Global_Data.Custom_Toast(ExpenseDetailsActivity.this, "Image Captured Successfully", "Yes");
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {

            try {
                selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                pictureImagePath = "file:" + c.getString(columnIndex);
                Global_Data.Custom_Toast(ExpenseDetailsActivity.this, "Image Selected Successfully", "Yes");
                c.close();
                //new Expenses.LongOperation().execute();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath1 = data.getData();
            //new File(filePath1.getPath());
        }
        else {
            try {
                File fdelet = new File(pdfmCurrentPhotoPath);
                if (fdelet.exists()) {
                    if (fdelet.delete()) {
                        println("file Deleted :" + pdfmCurrentPhotoPath);
                    } else {
                        println("file not Deleted :" + pdfmCurrentPhotoPath);
                    }
                }
               // pdfPath="";
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "exp";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_Expenses");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
      //  pdfPath = "";
        pictureImagePath = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createImageFile1() throws IOException {
        // Create an image file name
        String imageFileName = "exp";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_Expenses");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".pdf",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        pdfPath = "file:" + image.getAbsolutePath();
        pdfmCurrentPhotoPath = image.getAbsolutePath();
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

                                AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseDetailsActivity.this);

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
                                            //  cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                            Global_Data.cam = "Yes";
                                            Uri photoURI = FileProvider.getUriForFile(ExpenseDetailsActivity.this,
                                                    BuildConfig.APPLICATION_ID + ".provider",
                                                    photoFile);
                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            startActivityForResult(cameraIntent, 3);
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
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.no_camera),"yes");
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
                      //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(),"");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseDetailsActivity.this);
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

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }


    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public class ImagePdfAyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {

            SharedPreferences spf = ExpenseDetailsActivity.this.getSharedPreferences("SimpleLogic", 0);
            String user_email22 = spf.getString("USER_EMAIL", null);
            try {
                String charset = "UTF-8";
                String domain = ExpenseDetailsActivity.this.getResources().getString(R.string.service_domain);
                try {


                    if (Global_Data.ExpenseName.equalsIgnoreCase("Conveyance")) {
                        multipart = new MultipartUtility(domain + "expenses_travles/save_travel_expenses", charset);


                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pdfPath)) {
                            Uri uri2 = Uri.parse(pdfPath);
                            final File file2 = new File(uri2.getPath());
                            try {
                                if (file2.exists()) {
                                    multipart.addFilePart("expenses_travel_attachment", file2);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } else {
                            // multipart.addFilePart("expenses_travel_attachment", null);
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pictureImagePath)) {
                            Uri uri1 = Uri.parse(pictureImagePath);
                            final File file1 = new File(uri1.getPath());
                            try {
                                if (file1.exists()) {
                                    multipart.addFilePart("expenses_travel_image", file1);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            // multipart.addFilePart("expenses_travel_image", null);
                        }

                        Long randomPIN = System.currentTimeMillis();
                        PINStringEx = String.valueOf(randomPIN);

                        multipart.addFormField("email", user_email22);
                        multipart.addFormField("code", PINStringEx);
                        multipart.addFormField("travel_date", etTravelDate.getText().toString().trim());
                        multipart.addFormField("travel_cost", etTravelCost.getText().toString().trim());
                        multipart.addFormField("travel_from", etTravelFrom.getText().toString().trim());
                        multipart.addFormField("travel_to", etTravelTo.getText().toString().trim());
                        multipart.addFormField("travel_mode", etTravelMode.getText().toString().trim());
                        multipart.addFormField("travel_text", etTravelDescr.getText().toString().trim());

                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Miscellaneous")) {
                        multipart = new MultipartUtility(domain + "expenses_miscs/save_misc_expenses", charset);

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pictureImagePath)) {
                            Uri uri1 = Uri.parse(pictureImagePath);
                            File expenseImageFileMisc = new File(uri1.getPath());
                            try {


                                if (expenseImageFileMisc.exists()) {

                                    multipart.addFilePart("expenses_misc_image", expenseImageFileMisc);

                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            // multipart.addFilePart("expenses_travel_image", null);
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pdfPath)) {
                            Uri uri2 = Uri.parse(pdfPath);
                            File expensePdfFileMisc = new File(uri2.getPath());
                            try {
                                if (expensePdfFileMisc.exists()) {
                                    multipart.addFilePart("expenses_misc_attachment", expensePdfFileMisc);

                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            // multipart.addFilePart("expenses_travel_attachment", null);
                        }
                        Long randomPIN = System.currentTimeMillis();
                        PINStringEx = String.valueOf(randomPIN);

                        multipart.addFormField("email", user_email22);
                        multipart.addFormField("code", PINStringEx);
                        multipart.addFormField("misc_date", etExpenseMisclnsDate.getText().toString().trim());
                        multipart.addFormField("misc_amount", etExpenseMisclnsCost.getText().toString().trim());
                        multipart.addFormField("misc_text", etExpenseMisclnsDescr.getText().toString().trim());

                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Food")) {
                        multipart = new MultipartUtility(domain + "expenses/save_food_expenses", charset);

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pictureImagePath)) {
                            Uri uri1 = Uri.parse(pictureImagePath);
                            File expenseImageFileFood = new File(uri1.getPath());
                            try {
                                if (expenseImageFileFood.exists()) {
                                    multipart.addFilePart("expenses_food_image", expenseImageFileFood);

                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            // multipart.addFilePart("expenses_travel_image", null);
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pdfPath)) {
                            Uri uri2 = Uri.parse(pdfPath);
                            File expensePdfFileFood = new File(uri2.getPath());
                            try {
                                if (expensePdfFileFood.exists()) {
                                    multipart.addFilePart("expenses_food_attachment", expensePdfFileFood);

                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            // multipart.addFilePart("expenses_travel_attachment", null);
                        }

                        Long randomPIN = System.currentTimeMillis();
                        PINStringEx = String.valueOf(randomPIN);

                        multipart.addFormField("email", user_email22);
                        multipart.addFormField("code", PINStringEx);
                        multipart.addFormField("food_date", etFoodDate.getText().toString().trim());
                        multipart.addFormField("amount", etFoodCost.getText().toString().trim());
                        multipart.addFormField("description", etFoodDescr.getText().toString().trim());
                        multipart.addFormField("hotel_name", etFoodHotelName.getText().toString().trim());

                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Hotel")) {
                        multipart = new MultipartUtility(domain + "expenses/save_hotel_expenses", charset);

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pictureImagePath)) {
                            Uri uri1 = Uri.parse(pictureImagePath);
                            final File file1 = new File(uri1.getPath());
                            try {
                                if (file1.exists()) {
                                    multipart.addFilePart("expenses_hotel_image", file1);

                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            // multipart.addFilePart("expenses_travel_image", null);
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pdfPath)) {
                            Uri uri2 = Uri.parse(pdfPath);
                            final File file2 = new File(uri2.getPath());
                            try {
                                if (file2.exists()) {
                                    multipart.addFilePart("expenses_hotel_attachment", file2);

                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            // multipart.addFilePart("expenses_travel_attachment", null);
                        }

                        Long randomPIN = System.currentTimeMillis();
                        PINStringEx = String.valueOf(randomPIN);

                        multipart.addFormField("email", user_email22);
                        multipart.addFormField("code", PINStringEx);
                        multipart.addFormField("hotel_checkin", etHotelCheckinDate.getText().toString().trim());
                        multipart.addFormField("hotel_date", etHotelCheckinDate.getText().toString().trim());
                        multipart.addFormField("hotel_checkout", etHotelCheckOutDate.getText().toString().trim());
                        multipart.addFormField("amount", etHotelCost.getText().toString().trim());
                        multipart.addFormField("hotel_name", etHotelName.getText().toString().trim());
                        multipart.addFormField("hotel_address", etHotelAddress.getText().toString().trim());
                        multipart.addFormField("description", etHotelDescr.getText().toString().trim());

                    }

                    Log.i("volley", "domain: " + domain);
                    List<String> response1 = multipart.finish();

                    Log.v("rht", "SERVER REPLIED:");

                    ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    for (String line : response1) {
                        Log.v("rht", "Line : " + line);
                        response_result = line;
                        ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject obj = new JSONObject(response_result);
                                    //dialog.dismiss();
                                    //Successcul message issue on submit. Message should be "Promotional activity submitted successfully"
                                    if (!obj.getString("result").equalsIgnoreCase("User Not Found")) {
                                        Toast toast = Toast.makeText(ExpenseDetailsActivity.this, obj.getString("result"), Toast.LENGTH_SHORT);
                                        pictureImagePath = "";
                                        pdfPath = "";
                                        PINStringEx = "";

//                                        if (expenseImageFile.exists()) {
//                                            if (expenseImageFile.delete()) {
//                                                System.out.println("file Deleted :" + pictureImagePath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + pictureImagePath);
//                                            }
//                                        }
//
//                                        if (expensePdfFile.exists()) {
//                                            if (expensePdfFile.delete()) {
//                                                System.out.println("file Deleted :" + pdfPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + pdfPath);
//                                            }
//                                        }

                                        ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                                File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Return Order");

                                                if (!storageDir.exists()) {
                                                    storageDir.delete();
                                                }

                                                Global_Data.Custom_Toast(ExpenseDetailsActivity.this, "Expenses Submited Successfully", "yes");


                                                Intent intent = new Intent(ExpenseDetailsActivity.this, ExpensesNewActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });


                                    } else {
//                                        Toast toast = Toast.makeText(ExpenseDetailsActivity.this, obj.getString("result"),
//                                                Toast.LENGTH_SHORT);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();

Global_Data.Custom_Toast(ExpenseDetailsActivity.this, obj.getString("result"),"yes");
                                    }

                                    Log.d("My App", obj.toString());

                                } catch (Throwable t) {
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response_result + "\"");

                                    ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {

                                            dialog.dismiss();
//                                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, "Something went wrong,Please try again.",
//                                                    Toast.LENGTH_SHORT);
//                                            toast.setGravity(Gravity.CENTER, 0, 0);
//                                            toast.show();

                                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, "Something went wrong,Please try again.","yes");
                                        }
                                    });
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, "Something went wrong,Please try again.",
//                                    Toast.LENGTH_SHORT);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, "Something went wrong,Please try again.","yes");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });
            }
            return null;
        }
    }

    public class EditViewImagePdfAyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {

            SharedPreferences spf = ExpenseDetailsActivity.this.getSharedPreferences("SimpleLogic", 0);
            String user_email22 = spf.getString("USER_EMAIL", null);
            try {
                String charset = "UTF-8";
                String domain = ExpenseDetailsActivity.this.getResources().getString(R.string.service_domain);
                try {

                    if (Global_Data.ExpenseName.equalsIgnoreCase("Conveyance")) {
                        multipart = new MultipartUtility(domain + "expenses/edit_expenses", charset);

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pictureImagePath)) {
                            Uri uri1 = Uri.parse(pictureImagePath);
                            final File file1 = new File(uri1.getPath());
                            try {
                                if (file1.exists()) {
                                    multipart.addFilePart("expenses_travel_image", file1);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } else {
                            // multipart.addFilePart("expenses_travel_image", null);
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pdfPath)) {
                            Uri uri2 = Uri.parse(pdfPath);
                            final File file2 = new File(uri2.getPath());
                            try {


                                if (file2.exists()) {
                                    multipart.addFilePart("expenses_travel_attachment", file2);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } else {
                            // multipart.addFilePart("expenses_travel_attachment", null);
                        }


                        multipart.addFormField("id", Global_Data.ExpenseId);
                        multipart.addFormField("type", "expenses_travel");
                        multipart.addFormField("email", user_email22);

                        String ddd = etTravelDate.getText().toString();

                        if (ddd.length() > 0) {
                            multipart.addFormField("travel_date", etTravelDate.getText().toString().trim());
                        }

                        if (etTravelCost.getText().toString().trim().length() > 0) {
                            multipart.addFormField("travel_cost", etTravelCost.getText().toString().trim());
                        }

                        if (etTravelFrom.getText().toString().trim().length() > 0) {
                            multipart.addFormField("travel_from", etTravelFrom.getText().toString().trim());
                        }

                        if (etTravelTo.getText().toString().trim().length() > 0) {
                            multipart.addFormField("travel_to", etTravelTo.getText().toString().trim());
                        }

                        if (etTravelMode.getText().toString().trim().length() > 0) {
                            multipart.addFormField("travel_mode", etTravelMode.getText().toString().trim());
                        }

                        if (etTravelDescr.getText().toString().trim().length() > 0) {
                            multipart.addFormField("travel_text", etTravelDescr.getText().toString().trim());
                        }

                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Miscellaneous")) {
                        multipart = new MultipartUtility(domain + "expenses/edit_expenses", charset);

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pictureImagePath)) {
                            Uri uri1 = Uri.parse(pictureImagePath);
                            File expenseImageFileMisc = new File(uri1.getPath());
                            try {
                                if (expenseImageFileMisc.exists()) {
                                    multipart.addFilePart("expenses_misc_image", expenseImageFileMisc);

                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            // multipart.addFilePart("expenses_travel_image", null);
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pdfPath)) {
                            Uri uri2 = Uri.parse(pdfPath);
                            File expensePdfFileMisc = new File(uri2.getPath());
                            try {
                                if (expensePdfFileMisc.exists()) {
                                    multipart.addFilePart("expenses_misc_attachment", expensePdfFileMisc);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            // multipart.addFilePart("expenses_travel_attachment", null);
                        }

                        multipart.addFormField("id", Global_Data.ExpenseId);
                        multipart.addFormField("type", "expenses_misc");
                        multipart.addFormField("email", user_email22);

                        if (etExpenseMisclnsDate.getText().toString().length() > 0) {
                            multipart.addFormField("misc_date", etExpenseMisclnsDate.getText().toString().trim());
                        }

                        if (etExpenseMisclnsCost.getText().toString().length() > 0) {
                            multipart.addFormField("misc_amount", etExpenseMisclnsCost.getText().toString().trim());
                        }

                        if (etExpenseMisclnsDescr.getText().toString().length() > 0) {
                            multipart.addFormField("misc_text", etExpenseMisclnsDescr.getText().toString().trim());
                        }


                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Food")) {
                        multipart = new MultipartUtility(domain + "expenses/edit_expenses", charset);

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pictureImagePath)) {
                            Uri uri1 = Uri.parse(pictureImagePath);
                            File expenseImageFileFood = new File(uri1.getPath());
                            try {
                                if (expenseImageFileFood.exists()) {
                                    multipart.addFilePart("expenses_food_image", expenseImageFileFood);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            // multipart.addFilePart("expenses_travel_image", null);
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pdfPath)) {
                            Uri uri2 = Uri.parse(pdfPath);
                            File expensePdfFileFood = new File(uri2.getPath());
                            try {


                                if (expensePdfFileFood.exists()) {
                                    multipart.addFilePart("expenses_food_attachment", expensePdfFileFood);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } else {
                            // multipart.addFilePart("expenses_travel_attachment", null);
                        }

                        multipart.addFormField("id", Global_Data.ExpenseId);
                        multipart.addFormField("type", "expenses_food");
                        multipart.addFormField("email", user_email22);

                        if (etFoodDate.getText().toString().length() > 0) {
                            multipart.addFormField("food_date", etFoodDate.getText().toString().trim());
                        }

                        if (etFoodCost.getText().toString().length() > 0) {
                            multipart.addFormField("amount", etFoodCost.getText().toString().trim());
                        }

                        if (etFoodDescr.getText().toString().length() > 0) {
                            multipart.addFormField("description", etFoodDescr.getText().toString().trim());
                        }

                        if (etFoodHotelName.getText().toString().length() > 0) {
                            multipart.addFormField("hotel_name", etFoodHotelName.getText().toString().trim());
                        }


                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Hotel")) {
                        multipart = new MultipartUtility(domain + "expenses/edit_expenses", charset);

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pictureImagePath)) {
                            Uri uri1 = Uri.parse(pictureImagePath);
                            final File file1 = new File(uri1.getPath());
                            try {


                                if (file1.exists()) {
                                    multipart.addFilePart("expenses_hotel_image", file1);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } else {
                            // multipart.addFilePart("expenses_travel_image", null);
                        }
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanew(pdfPath)) {
                            Uri uri2 = Uri.parse(pdfPath);
                            final File file2 = new File(uri2.getPath());
                            try {


                                if (file2.exists()) {
                                    multipart.addFilePart("expenses_hotel_attachment", file2);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } else {
                            // multipart.addFilePart("expenses_travel_attachment", null);
                        }

                        multipart.addFormField("id", Global_Data.ExpenseId);
                        multipart.addFormField("type", "expenses_hotel");
                        multipart.addFormField("email", user_email22);

                        if (etHotelCheckinDate.getText().toString().length() > 0) {
                            multipart.addFormField("hotel_checkin", etHotelCheckinDate.getText().toString().trim());
                            multipart.addFormField("hotel_date", etHotelCheckinDate.getText().toString().trim());
                        }

                        if (etHotelCheckOutDate.getText().toString().length() > 0) {
                            multipart.addFormField("hotel_checkout", etHotelCheckOutDate.getText().toString().trim());
                        }

                        if (etHotelCost.getText().toString().length() > 0) {
                            multipart.addFormField("amount", etHotelCost.getText().toString().trim());
                        }

                        if (etHotelName.getText().toString().length() > 0) {
                            multipart.addFormField("hotel_name", etHotelName.getText().toString().trim());
                        }

                        if (etHotelAddress.getText().toString().length() > 0) {
                            multipart.addFormField("hotel_address", etHotelAddress.getText().toString().trim());
                        }

                        if (etHotelDescr.getText().toString().length() > 0) {
                            multipart.addFormField("description", etHotelDescr.getText().toString().trim());
                        }


                    }

                    Log.i("volley", "domain: " + domain);
                    List<String> response1 = multipart.finish();

                    Log.v("rht", "SERVER REPLIED:");

                    ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    for (String line : response1) {
                        Log.v("rht", "Line : " + line);
                        response_result = line;
                        ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject obj = new JSONObject(response_result);
                                    //dialog.dismiss();
                                    //Successcul message issue on submit. Message should be "Promotional activity submitted successfully"
                                    if (!obj.getString("result").equalsIgnoreCase("User Not Found")) {
                                        Toast toast = Toast.makeText(ExpenseDetailsActivity.this, obj.getString("result"), Toast.LENGTH_SHORT);
                                        pictureImagePath = "";
                                        pdfPath = "";
                                        PINStringEx = "";

//                                        if (expenseImageFile.exists()) {
//                                            if (expenseImageFile.delete()) {
//                                                System.out.println("file Deleted :" + pictureImagePath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + pictureImagePath);
//                                            }
//                                        }
//
//                                        if (expensePdfFile.exists()) {
//                                            if (expensePdfFile.delete()) {
//                                                System.out.println("file Deleted :" + pdfPath);
//                                            } else {
//                                                System.out.println("file not Deleted :" + pdfPath);
//                                            }
//                                        }

                                        ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                                File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Return Order");

                                                if (!storageDir.exists()) {
                                                    storageDir.delete();
                                                }

                                                Global_Data.Custom_Toast(ExpenseDetailsActivity.this, "Expenses Submited Successfully", "yes");

                                                Intent intent = new Intent(ExpenseDetailsActivity.this, ExpensesNewActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });


                                    } else {
//                                        Toast toast = Toast.makeText(ExpenseDetailsActivity.this, obj.getString("result"),
//                                                Toast.LENGTH_SHORT);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();

                                        Global_Data.Custom_Toast(ExpenseDetailsActivity.this, obj.getString("result"),"yes");

                                    }

                                    Log.d("My App", obj.toString());

                                } catch (Throwable t) {
                                    Log.e("My App", "Could not parse malformed JSON: \"" + response_result + "\"");

                                    ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {

                                            dialog.dismiss();
//                                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, "Something went wrong,Please try again.",
//                                                    Toast.LENGTH_SHORT);
//                                            toast.setGravity(Gravity.CENTER, 0, 0);
//                                            toast.show();
                                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, "Something went wrong,Please try again.","yes");

                                        }
                                    });
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
//                            Toast toast = Toast.makeText(ExpenseDetailsActivity.this, "Something went wrong,Please try again.",
//                                    Toast.LENGTH_SHORT);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, "Something went wrong,Please try again.","yes");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();
                    }
                });
            }
            return null;
        }
    }

    public void ExpenseShowResult() {
        String domain = getResources().getString(R.string.service_domain);

        SharedPreferences spf = ExpenseDetailsActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

        Log.i("volley", "domain: " + domain);
        Log.i("volley", "email: " + user_email);
        //Log.i("user list url", "user list url " + domain + "expenses/get_expenses_index?email=" + user_email + "&type=" + type+ "&year=" + currentYear+ "&month=" + selectedMonth) ;
        StringRequest jsObjRequest = null;
        String service_url = "";

        if (Global_Data.ExpenseName.equalsIgnoreCase("Conveyance")) {
            service_url = domain + "expenses/show_expenses?email=" + user_email + "&type=" + "expenses_travel" + "&id=" + Global_Data.ExpenseId;
        } else if (Global_Data.ExpenseName.equalsIgnoreCase("Food")) {
            service_url = domain + "expenses/show_expenses?email=" + user_email + "&type=" + "expenses_food" + "&id=" + Global_Data.ExpenseId;
        } else if (Global_Data.ExpenseName.equalsIgnoreCase("Hotel")) {
            service_url = domain + "expenses/show_expenses?email=" + user_email + "&type=" + "expenses_hotel" + "&id=" + Global_Data.ExpenseId;
        } else if (Global_Data.ExpenseName.equalsIgnoreCase("Miscellaneous")) {
            service_url = domain + "expenses/show_expenses?email=" + user_email + "&type=" + "expenses_misc" + "&id=" + Global_Data.ExpenseId;
        }

        Log.i("volley", "service_url: " + service_url);

        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;
                new ExpenseDetailsActivity.GetAllUserListn().execute(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Intent intent = new Intent(ExpenseDetailsActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Network Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Network Error","");
                        } else if (error instanceof AuthFailureError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Server AuthFailureError  Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Server AuthFailureError  Error","");
                        } else if (error instanceof ServerError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Server   Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Server   Error","");
                        } else if (error instanceof NetworkError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "Network   Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Network   Error","");
                        } else if (error instanceof ParseError) {
//                            Toast.makeText(getApplicationContext(),
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "ParseError   Error","");
                        } else {
                           // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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
        jsObjRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        requestQueue.add(jsObjRequest);
    }

    private class GetAllUserListn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {
            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("result")) {
                    response_result1 = response.getString("result");
                } else {
                    response_result1 = "data";
                }

                if (response_result1.equalsIgnoreCase("Expenses Record Not Found.")) {
                    ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();

                            Global_Data.Custom_Toast(ExpenseDetailsActivity.this, response_result1, "Yes");

//                            Intent intent = new Intent(ExpenseDetailsActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    });
                } else {
                    if (Global_Data.ExpenseName.equalsIgnoreCase("Conveyance")) {

                        JSONObject jsonObjectTravel = response.getJSONObject("expenses_record");

                        ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    //Global_Data.ExpenseStatus="approved";
                                    if (Global_Data.ExpenseStatus.equalsIgnoreCase("pending")) {
                                        etTravelDate.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObjectTravel.getString("travel_date")));
                                        etTravelCost.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObjectTravel.getString("travel_cost")));
                                        etTravelFrom.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObjectTravel.getString("travel_from")));
                                        etTravelTo.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObjectTravel.getString("travel_to")));
                                        etTravelMode.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObjectTravel.getString("travel_mode")));
                                        etTravelDescr.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObjectTravel.getString("travel_text")));

                                        //ivTravelPdf = (ImageView) findViewById(R.id.pdf_expensetravel);
                                        Glide.with(ExpenseDetailsActivity.this)
                                                .load(jsonObjectTravel.getString("image_url"))
                                                //.load(R.drawable.hotel)
                                                .thumbnail(0.5f)
                                                //.centerCrop()
                                                .placeholder(R.drawable.not_found)
                                                .error(R.drawable.not_found)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(ivTravelCamera);
                                    } else {

                                        etTravelDate.setEnabled(false);
                                        etTravelCost.setEnabled(false);
                                        etTravelFrom.setEnabled(false);
                                        etTravelTo.setEnabled(false);
                                        etTravelMode.setEnabled(false);
                                        etTravelDescr.setEnabled(false);
                                        submitExpensesDetails.setVisibility(View.GONE);
                                        ivTravelCamera.setVisibility(View.GONE);
                                        ivTravelPdf.setVisibility(View.GONE);

                                        etTravelDate.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObjectTravel.getString("travel_date")));
                                        etTravelCost.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObjectTravel.getString("travel_cost")));
                                        etTravelFrom.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObjectTravel.getString("travel_from")));
                                        etTravelTo.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObjectTravel.getString("travel_to")));
                                        etTravelMode.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObjectTravel.getString("travel_mode")));
                                        etTravelDescr.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObjectTravel.getString("travel_text")));

                                        //ivTravelPdf = (ImageView) findViewById(R.id.pdf_expensetravel);
                                        Glide.with(ExpenseDetailsActivity.this)
                                                .load(jsonObjectTravel.getString("image_url"))
                                                //.load(R.drawable.hotel)
                                                .thumbnail(0.5f)
                                                //.centerCrop()
                                                .placeholder(R.drawable.not_found)
                                                .error(R.drawable.not_found)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(ivTravelCamera);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Food")) {

                        JSONObject jsonObject = response.getJSONObject("expenses_record");

                        ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                try {

                                    if (Global_Data.ExpenseStatus.equalsIgnoreCase("pending")) {

                                        etFoodDate.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("food_date")));
                                        etFoodCost.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("amount")));
                                        etFoodHotelName.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("hotel_name")));
                                        etFoodDescr.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("description")));

                                        //ivTravelPdf = (ImageView) findViewById(R.id.pdf_expensetravel);
                                        Glide.with(ExpenseDetailsActivity.this)
                                                .load(response.getString("image_url"))
                                                //.load(R.drawable.hotel)
                                                .thumbnail(0.5f)
                                                //.centerCrop()
                                                .placeholder(R.drawable.not_found)
                                                .error(R.drawable.not_found)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(ivFoodCamera);


                                    } else {
                                        etFoodDate.setEnabled(false);
                                        etFoodCost.setEnabled(false);
                                        etFoodHotelName.setEnabled(false);
                                        etFoodDescr.setEnabled(false);
                                        submitExpensesDetails.setVisibility(View.GONE);
                                        ivFoodCamera.setVisibility(View.GONE);
                                        ivFoodPdf.setVisibility(View.GONE);

                                        etFoodDate.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("food_date")));
                                        etFoodCost.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("amount")));
                                        etFoodHotelName.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("hotel_name")));
                                        etFoodDescr.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("description")));

                                        //ivTravelPdf = (ImageView) findViewById(R.id.pdf_expensetravel);
                                        Glide.with(ExpenseDetailsActivity.this)
                                                .load(response.getString("image_url"))
                                                //.load(R.drawable.hotel)
                                                .thumbnail(0.5f)
                                                //.centerCrop()
                                                .placeholder(R.drawable.not_found)
                                                .error(R.drawable.not_found)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(ivFoodCamera);


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Hotel")) {

                        JSONObject jsonObject = response.getJSONObject("expenses_record");

                        ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    if (Global_Data.ExpenseStatus.equalsIgnoreCase("pending")) {
                                        etHotelCheckinDate.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("hotel_checkin")));
                                        etHotelCheckOutDate.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("hotel_checkout")));
                                        etHotelCost.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("amount")));
                                        etHotelName.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("hotel_name")));
                                        etHotelAddress.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("hotel_address")));
                                        etHotelDescr.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("description")));
                                        //ivTravelPdf = (ImageView) findViewById(R.id.pdf_expensetravel);
                                        Glide.with(ExpenseDetailsActivity.this)
                                                .load(response.getString("image_url"))
                                                //.load(R.drawable.hotel)
                                                .thumbnail(0.5f)
                                                //.centerCrop()
                                                .placeholder(R.drawable.not_found)
                                                .error(R.drawable.not_found)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(ivHotelCamera);
                                    } else {

                                        etHotelCheckinDate.setEnabled(false);
                                        etHotelCheckOutDate.setEnabled(false);
                                        etHotelCost.setEnabled(false);
                                        etHotelName.setEnabled(false);
                                        etHotelAddress.setEnabled(false);
                                        etHotelDescr.setEnabled(false);
                                        submitExpensesDetails.setVisibility(View.GONE);
                                        ivHotelCamera.setVisibility(View.GONE);
                                        ivHotelPdf.setVisibility(View.GONE);

                                        etHotelCheckinDate.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("hotel_checkin")));
                                        etHotelCheckOutDate.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("hotel_checkout")));
                                        etHotelCost.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("amount")));
                                        etHotelName.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("hotel_name")));
                                        etHotelAddress.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("hotel_address")));
                                        etHotelDescr.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("description")));
                                        //ivTravelPdf = (ImageView) findViewById(R.id.pdf_expensetravel);
                                        Glide.with(ExpenseDetailsActivity.this)
                                                .load(response.getString("image_url"))
                                                //.load(R.drawable.hotel)
                                                .thumbnail(0.5f)
                                                //.centerCrop()
                                                .placeholder(R.drawable.not_found)
                                                .error(R.drawable.not_found)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(ivHotelCamera);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else if (Global_Data.ExpenseName.equalsIgnoreCase("Miscellaneous")) {

                        JSONObject jsonObject = response.getJSONObject("expenses_record");

                        ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                try {

                                    if (Global_Data.ExpenseStatus.equalsIgnoreCase("pending")) {
                                        etExpenseMisclnsDate.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("date")));
                                        etExpenseMisclnsCost.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("amount")));
                                        etExpenseMisclnsDescr.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("description")));

                                        //ivTravelPdf = (ImageView) findViewById(R.id.pdf_expensetravel);
                                        Glide.with(ExpenseDetailsActivity.this)
                                                .load(response.getString("image_url"))
                                                //.load(R.drawable.hotel)
                                                .thumbnail(0.5f)
                                                //.centerCrop()
                                                .placeholder(R.drawable.not_found)
                                                .error(R.drawable.not_found)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(ivMiscCamera);

                                    } else {
                                        etExpenseMisclnsDate.setEnabled(false);
                                        etExpenseMisclnsCost.setEnabled(false);
                                        etExpenseMisclnsDescr.setEnabled(false);
                                        submitExpensesDetails.setVisibility(View.GONE);
                                        ivMiscCamera.setVisibility(View.GONE);
                                        ivMiscPdf.setVisibility(View.GONE);

                                        etExpenseMisclnsDate.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("date")));
                                        etExpenseMisclnsCost.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("amount")));
                                        etExpenseMisclnsDescr.setText(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavaString(jsonObject.getString("description")));

                                        //ivTravelPdf = (ImageView) findViewById(R.id.pdf_expensetravel);
                                        Glide.with(ExpenseDetailsActivity.this)
                                                .load(response.getString("image_url"))
                                                //.load(R.drawable.hotel)
                                                .thumbnail(0.5f)
                                                //.centerCrop()
                                                .placeholder(R.drawable.not_found)
                                                .error(R.drawable.not_found)
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .into(ivMiscCamera);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Intent intent = new Intent(ExpenseDetailsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }
            ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            ExpenseDetailsActivity.this.runOnUiThread(new Runnable() {
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
}
