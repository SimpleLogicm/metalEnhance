package com.msimplelogic.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

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
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.webservice.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class NoOrderActivity extends BaseActivity {
    static int cityID, beatID, retailerID, reasonID;
    Spinner spinner1;
    String Noorder_res = "";
    ProgressDialog dialog;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Button buttonnoOrderSave, buttonnoOrdercancel;
    EditText edittextNoOrderreason;
    CardView ed_cradview;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    List<String> listReasons;
    ArrayAdapter<String> dataAdapter;
    LoginDataBaseAdapter loginDataBaseAdapter;
    HashMap<String, String> reasonsMap;
    String reasonOther = "", mobile = "", emailID = "", retailer_code = "";
    TextView no_order_head;
    Toolbar toolbar;
    String reason_name;
    String reason_codes;
    String response_result;
    String user_email = "";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    SharedPreferences sp;
    ImageView hedder_theame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_order);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        SharedPreferences spf = this.getSharedPreferences("SimpleLogic", 0);
        user_email = spf.getString("USER_EMAIL", null);


        spinner1 = (Spinner) findViewById(R.id.spnReason);
        edittextNoOrderreason = (EditText) findViewById(R.id.edittextNoOrderreason);
        ed_cradview = (CardView) findViewById(R.id.ed_cradview);
        no_order_head = (TextView) findViewById(R.id.editTextTarget);
        hedder_theame = findViewById(R.id.hedder_theame);
        Intent i = getIntent();
        String name = i.getStringExtra("retialer");
        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);

        }

        cd = new ConnectionDetector(getApplicationContext());

        loginDataBaseAdapter = new LoginDataBaseAdapter(getApplicationContext());
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        String norderstr = spf1.getString("var_norder", "");

        if (norderstr.length() > 0) {
            no_order_head.setText(norderstr);
        } else {
            no_order_head.setText(getResources().getString(R.string.No_Order));
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
            mTitleTextView.setText(Global_Data.order_retailer);
            //mTitleTextView.setText(Global_Data.order_retailer + " " + "(" + Global_Data.AmountOutstanding + "/" + Global_Data.AmountOverdue + ")");
            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = NoOrderActivity.this.getSharedPreferences("SimpleLogic", 0);

//        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//        	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//		}

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
                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                } else {
                    int age = (int) Math.round(age_float);
                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    }
                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
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


        edittextNoOrderreason.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edittextNoOrderreason.getRight() - edittextNoOrderreason.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        View view = NoOrderActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        //autoCompleteTextView1.setText("");
                        //Product_Variant.showDropDown();
                        promptSpeechInput();
                        return true;
                    }
                }
                return false;
            }
        });

        reasonsMap = new HashMap<String, String>();
        listReasons = new ArrayList<String>();
        //myDbHelper = new DatabaseHandler(getApplicationContext());
        LoadReasonsAsyncTask loadReasonsAsyncTask = new LoadReasonsAsyncTask(NoOrderActivity.this);
        loadReasonsAsyncTask.execute();

        SharedPreferences sp1 = NoOrderActivity.this.getSharedPreferences("SimpleLogic", 0);

        //userID=sp1.getInt("UserID", 0);
        cityID = sp1.getInt("CityID", 0);
        beatID = sp1.getInt("BeatID", 0);
        retailerID = sp1.getInt("RetailerID", 0);
        retailer_code = sp1.getString("RetailerCode", "");
        mobile = sp1.getString("Mobile", "");
        emailID = sp1.getString("EmailId", "");

    	/*listReasons.add("Product not moving");
    	listReasons.add("Already stocked");
    	listReasons.add("Seasonality");
    	listReasons.add("Other");*/

        dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, listReasons);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(dataAdapter);

        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                // TODO Auto-generated method stub
                //Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),Toast.LENGTH_SHORT).show();

                if (parent.getItemAtPosition(pos).toString().equalsIgnoreCase(getResources().getString(R.string.Other))) {
                    edittextNoOrderreason.setVisibility(View.VISIBLE);
                    ed_cradview.setVisibility(View.VISIBLE);
                    reasonID = 0;

                } else {
                    if (parent.getSelectedItemId() != 0) {
                        //reasonID=dataReasons.get(pos-1).getReason_Id();
                        reasonID = 0;
                        edittextNoOrderreason.setVisibility(View.INVISIBLE);
                        ed_cradview.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        buttonnoOrderSave = (Button) findViewById(R.id.buttonnoOrderSave);
        buttonnoOrderSave.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                // TODO Auto-generated method stub

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    //    b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //down event
                    //   b.setBackgroundColor(Color.parseColor("#910505"));

                    if (spinner1.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Select_Reason))) {
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_select_reason), "yes");
//                        Toast toast1 = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_select_reason), Toast.LENGTH_SHORT);
//                        toast1.setGravity(Gravity.CENTER, 0, 0);
//                        toast1.show();
                    } else {
                        if (spinner1.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Other))) {
                            reasonOther = edittextNoOrderreason.getText().toString();
                            if (reasonOther.length() == 0) {
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Enter_reason), "yes");

//                                Toast toast1 = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Enter_reason), Toast.LENGTH_SHORT);
//                                toast1.setGravity(Gravity.CENTER, 0, 0);
//                                toast1.show();
                            } else {
//									isInternetPresent = cd.isConnectingToInternet();
//									if (isInternetPresent)
//				                    {
                                showDialogueBox();

                                //call_service();
//				                    }
//					   	        	else
//					   	        	{
//					   	        	 //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//										Toast toast = Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG);
//										toast.setGravity(Gravity.CENTER, 0, 0);
//										toast.show();
//					   	        	}

                            }

                        } else {
//							  isInternetPresent = cd.isConnectingToInternet();
//								if (isInternetPresent)
//			                    {
                            showDialogueBox();

                            //call_service();
//			                    }
//				   	        	else
//				   	        	{
//
//									Toast toast = Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG);
//									toast.setGravity(Gravity.CENTER, 0, 0);
//									toast.show();
//				   	        	}
                        }


                    }
                }
                return false;
            }
        });

        buttonnoOrdercancel = (Button) findViewById(R.id.buttonnoOrdercancel);

        buttonnoOrdercancel.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View b, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //up event
                    // b.setBackgroundColor(Color.parseColor("#414042"));
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //down event
                    //  b.setBackgroundColor(Color.parseColor("#910505"));
                    onBackPressed();
                }
                return false;
            }
        });
    	/*buttonnoOrdercancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setBackgroundColor(Color.parseColor("#910505"));

			}
		});*/

    }

    private void showDialogueBox() {
        AlertDialog alertDialog = new AlertDialog.Builder(NoOrderActivity.this)
                .create(); // Read Update
        alertDialog.setTitle(getResources().getString(R.string.Confirmation));
        alertDialog.setMessage(getResources().getString(R.string.Continue_dialog));
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
//						InsertOrderAsyncTask insertOrderAsyncTask = new InsertOrderAsyncTask(
//								NoOrderActivity.this);
//						insertOrderAsyncTask.execute();

                        call_service();
                        dialog.cancel();


                    }
                });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();


        AlertDialog alertDialog = new AlertDialog.Builder(NoOrderActivity.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Warning));
        alertDialog.setMessage(getResources().getString(R.string.PRE_page_back_message));
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                NoOrderActivity.this.finish();
            }
        });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });


        alertDialog.show();
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
                SharedPreferences sp = NoOrderActivity.this.getSharedPreferences("SimpleLogic", 0);
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


    protected void onPause() {
        super.onPause();
        System.gc();
    }

    public void call_service() {
        System.gc();

        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        final String formattedDaten = df.format(c.getTime());

        String reason_code = "";
        try {

            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = originalFormat.parse(getDateTime());
            String formattedDate = targetFormat.format(date1);


            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDatetrack = df.format(c.getTime());

            SimpleDateFormat dffn = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDatefn = dffn.format(c.getTime());


            if (spinner1.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Other))) {
                Noorder_res = edittextNoOrderreason.getText().toString();
            } else {

                Noorder_res = spinner1.getSelectedItem().toString().trim();
                List<Local_Data> contacts = dbvoc.get_reason_code(spinner1.getSelectedItem().toString().trim());

                if (contacts.size() <= 0) {

                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Reason_code_Not_Found), "yes");

//                    Toast toast = Toast.makeText(NoOrderActivity.this,
//                            getResources().getString(R.string.Reason_code_Not_Found), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                } else {

                    for (Local_Data cn : contacts) {

                        //Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();\
                        reason_code = cn.getreason_code();

                    }
                }
            }


//		    dialog = new ProgressDialog(NoOrderActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//	        dialog.setMessage("Please wait....");
//	        dialog.setTitle("Metal");
//	        dialog.setCancelable(false);
//	        dialog.show();


            try {


                if (spinner1.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.Other))) {

                    try {
                        AppLocationManager appLocationManager = new AppLocationManager(NoOrderActivity.this);
                        Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                        Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                        PlayService_Location PlayServiceManager = new PlayService_Location(NoOrderActivity.this);

                        if (PlayServiceManager.checkPlayServices(NoOrderActivity.this)) {
                            Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                        } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                            Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                            Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    Long randomPIN = System.currentTimeMillis();
                    String PINString = String.valueOf(randomPIN);

                    SharedPreferences spf = NoOrderActivity.this.getSharedPreferences("SimpleLogic", 0);
                    String user_email = spf.getString("USER_EMAIL", null);

                    cd = new ConnectionDetector(NoOrderActivity.this);
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        reason_name = Noorder_res;
                        reason_codes = "";
                        loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "NO ORDER", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDatetrack, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatefn);
                        new NoOrderActivity.Varientsave().execute();
                    } else {

                        loginDataBaseAdapter.insertNoOrder("", Global_Data.GLOvel_CUSTOMER_ID, Global_Data.GLOvel_CUSTOMER_ID, "",
                                user_email, "", "", "", "no", "", formattedDaten, "", Noorder_res, "Other", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, PINString);
                        String gaddress = "";
                        try {
                            if (Global_Data.address.equalsIgnoreCase("null")) {
                                gaddress = "";
                            } else {
                                gaddress = Global_Data.address;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.No_Order_Save_Successfully), "yes");

//                        Toast toast = Toast.makeText(getApplicationContext(),
//                                getResources().getString(R.string.No_Order_Save_Successfully), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Intent a = new Intent(NoOrderActivity.this, Neworderoptions.class);
                        startActivity(a);
                        finish();
                    }


                } else {

                    try {
                        AppLocationManager appLocationManager = new AppLocationManager(NoOrderActivity.this);
                        Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                        Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                        PlayService_Location PlayServiceManager = new PlayService_Location(NoOrderActivity.this);

                        if (PlayServiceManager.checkPlayServices(NoOrderActivity.this)) {
                            Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                        } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                            Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                            Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    Long randomPIN = System.currentTimeMillis();
                    String PINString = String.valueOf(randomPIN);

                    SharedPreferences spf = NoOrderActivity.this.getSharedPreferences("SimpleLogic", 0);
                    String user_email = spf.getString("USER_EMAIL", null);

                    cd = new ConnectionDetector(NoOrderActivity.this);
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        reason_name = "";
                        reason_codes = reason_code;
                        loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "NO ORDER", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDatetrack, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatefn);
                        new NoOrderActivity.Varientsave().execute();
                    } else {
                        loginDataBaseAdapter.insertNoOrder("", Global_Data.GLOvel_CUSTOMER_ID, Global_Data.GLOvel_CUSTOMER_ID, "",
                                user_email, "", "", "", "no", "", formattedDaten, "", reason_code, "", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, PINString);

                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.No_Order_Save_Successfully), "yes");

//                        Toast toast = Toast.makeText(getApplicationContext(),
//                                getResources().getString(R.string.No_Order_Save_Successfully), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Intent a = new Intent(NoOrderActivity.this, Neworderoptions.class);
                        startActivity(a);
                        finish();
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
                //dialog.dismiss();
            }


        } catch (Exception e) {
            // TODO: handle exception
            Log.e("DATA", e.getMessage());
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public class LoadReasonsAsyncTask extends AsyncTask<Void, Void, Void> {

        /**
         * progress dialog to show user that the backup is processing.
         */
        private ProgressDialog dialog;
        /**
         * application context.
         */
        private Activity activity;

        private Context context;


        public LoadReasonsAsyncTask(Activity activity) {
            this.activity = activity;
            context = activity;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            this.dialog.setMessage("Loading Reasons");
            this.dialog.show();
            listReasons.clear();
            listReasons.add(getResources().getString(R.string.Select_Reason));

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                List<Local_Data> contacts = dbvoc.getAllnoorder_reason();

                if (contacts.size() <= 0) {
                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Reason_Not_Found), "yes");

//                    Toast toast = Toast.makeText(NoOrderActivity.this,
//                        getResources().getString(R.string.Reason_Not_Found), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                } else {
                    listReasons.clear();
                    for (Local_Data cn : contacts) {

                        //Global_Data.GLOvel_CUSTOMER_ID = cn.getCust_Code();
                        if (!cn.getreason_name().equalsIgnoreCase(getResources().getString(R.string.Other))) {
                            listReasons.add(cn.getreason_name());
                        }

                    }
                }

                listReasons.add(getResources().getString(R.string.Other));
            } catch (Exception e) {
                // TODO: handle exception
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (dialog.isShowing()) {
                dialog.dismiss();

            }

            dataAdapter.notifyDataSetChanged();
            dataAdapter.setDropDownViewResource(R.layout.spinner_item);
            spinner1.setAdapter(dataAdapter);

        }
    }

    private class Varientsave extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... response) {

            cd = new ConnectionDetector(NoOrderActivity.this);
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            buttonnoOrderSave.setEnabled(false);
                            buttonnoOrderSave.setText("Wait...");
                            buttonnoOrdercancel.setEnabled(false);
                            AppLocationManager appLocationManager = new AppLocationManager(NoOrderActivity.this);
                            Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                            Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                            PlayService_Location PlayServiceManager = new PlayService_Location(NoOrderActivity.this);

                            if (PlayServiceManager.checkPlayServices(NoOrderActivity.this)) {
                                Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                            } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }
                });


                JSONArray no_order = new JSONArray();
                JSONObject product_value = new JSONObject();
                JSONObject product_valuenew = new JSONObject();
                JSONObject no_order_object = new JSONObject();

                try {

                    String PINStrings = new SimpleDateFormat("yyMdHms").format(Calendar.getInstance().getTime());
                    no_order_object.put("order_number", PINStrings);
                    if (!reason_name.equalsIgnoreCase("")) {
                        no_order_object.put("reason_name", reason_name);

                    } else {
                        no_order_object.put("reason_code", reason_codes);

                    }
                    no_order_object.put("user_email", Global_Data.GLOvel_USER_EMAIL);
                    no_order_object.put("customer_code", Global_Data.GLOvel_CUSTOMER_ID);
                    no_order_object.put("latitude", Global_Data.GLOvel_LATITUDE);
                    no_order_object.put("longitude", Global_Data.GLOvel_LONGITUDE);

                    no_order.put(no_order_object);

                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            buttonnoOrderSave.setEnabled(true);
                            buttonnoOrderSave.setText("Submit");
                            buttonnoOrdercancel.setEnabled(true);
                            dialog.dismiss();

                        }
                    });

                }


                Double pp = 0.0;
                try {

                    String domain = NoOrderActivity.this.getResources().getString(R.string.service_domain);
                    String service_url = "";
                    service_url = domain + "no_orders/save_no_orders";
                    product_valuenew.put("no_orders", no_order);
                    product_valuenew.put("email", user_email);
                    Log.d("No Order", no_order.toString());

                    Log.i("volley", "domain: " + domain);
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, service_url, product_valuenew, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("volley", "response: " + response);


                            response_result = "";
                            //if (response.has("result")) {
                            try {
                                response_result = response.getString("result");
                            } catch (JSONException e) {
                                e.printStackTrace();

                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog.dismiss();
                                        buttonnoOrderSave.setEnabled(true);
                                        buttonnoOrderSave.setText("Submit");
                                        buttonnoOrdercancel.setEnabled(true);
                                    }
                                });
                            }

                            if (response_result.equalsIgnoreCase("Device not found.")) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Global_Data.Custom_Toast(getApplicationContext(), response_result, "yes");
//
//                                        Toast toast = Toast.makeText(NoOrderActivity.this, "Device Not Found", Toast.LENGTH_LONG);
//                                        toast.setGravity(Gravity.CENTER, 0, 0);
//                                        toast.show();
//                                        dialog.dismiss();
                                        buttonnoOrderSave.setEnabled(true);
                                        buttonnoOrderSave.setText("Submit");
                                        buttonnoOrdercancel.setEnabled(true);
                                    }
                                });

                            } else {

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        buttonnoOrderSave.setEnabled(true);
                                        buttonnoOrderSave.setText("Submit");
                                        buttonnoOrdercancel.setEnabled(true);

                                        Global_Data.Custom_Toast(getApplicationContext(), response_result, "yes");

//                                        Toast.makeText(NoOrderActivity.this, response_result, Toast.LENGTH_LONG).show();
                                        NoOrderActivity.this.finish();

                                        dialog.dismiss();

                                    }
                                });

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        Global_Data.Custom_Toast(getApplicationContext(), "your internet connection is not working, saving locally. Please sync when Internet is available", "");
//
//                                        Toast.makeText(NoOrderActivity.this,
//                                                "your internet connection is not working, saving locally. Please sync when Internet is available",
//                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else if (error instanceof AuthFailureError) {
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        Global_Data.Custom_Toast(getApplicationContext(), "Server AuthFailureError  Error", "");
//
//                                        Toast.makeText(NoOrderActivity.this,
//                                                "Server AuthFailureError  Error",
//                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else if (error instanceof ServerError) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Global_Data.Custom_Toast(getApplicationContext(), "Server   Error", "");

//                                        Toast.makeText(NoOrderActivity.this,
//                                                "Server   Error",
//                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else if (error instanceof NetworkError) {
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        Global_Data.Custom_Toast(getApplicationContext(), "your internet connection is not working, saving locally. Please sync when Internet is available", "");

//                                        Toast.makeText(NoOrderActivity.this,
//                                                "your internet connection is not working, saving locally. Please sync when Internet is available",
//                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else if (error instanceof ParseError) {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Global_Data.Custom_Toast(getApplicationContext(), "ParseError   Error", "");

//                                        Toast.makeText(NoOrderActivity.this,
//                                                "ParseError   Error",
//                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(), "");


//                                        Toast.makeText(NoOrderActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    buttonnoOrderSave.setEnabled(true);
                                    buttonnoOrderSave.setText("Submit");
                                    buttonnoOrdercancel.setEnabled(true);

                                    dialog.dismiss();
                                }
                            });

                            // finish();
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(NoOrderActivity.this);
                    // queue.add(jsObjRequest);
                    int socketTimeout = 30000;//30 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    jsObjRequest.setRetryPolicy(policy);
                    requestQueue.add(jsObjRequest);

                } catch (Exception ex) {
                    ex.printStackTrace();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            buttonnoOrderSave.setEnabled(true);
                            buttonnoOrderSave.setText("Submit");
                            buttonnoOrdercancel.setEnabled(true);
                            dialog.dismiss();

                        }
                    });
                }
            } else {
                // dialog.dismiss();
                runOnUiThread(new Runnable() {
                    public void run() {
                        buttonnoOrderSave.setEnabled(true);
                        buttonnoOrderSave.setText("Submit");
                        buttonnoOrdercancel.setEnabled(true);

                        dialog.dismiss();
                        Global_Data.Custom_Toast(getApplicationContext(), "You don't have internet connection.", "");

                        //  Toast.makeText(NoOrderActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
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
            NoOrderActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });


        }

        @Override
        protected void onPreExecute() {


            NoOrderActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    if (dialog == null)
                        dialog = new ProgressDialog(NoOrderActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    dialog.setMessage("Please wait....");
                    dialog.setTitle("Smart Anchor App");
                    dialog.setCancelable(false);
                    dialog.show();
                }
            });
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
//            Toast.makeText(getApplicationContext(),
//                    getString(R.string.speech_not_supported),
//                    Toast.LENGTH_SHORT).show();
            Global_Data.Custom_Toast(getApplicationContext(),
                    getString(R.string.speech_not_supported),"");
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    // String s = "";
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edittextNoOrderreason.setText(result.get(0).trim());


                }
                break;
            }

        }
    }

}
