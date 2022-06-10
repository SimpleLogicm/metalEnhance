package com.msimplelogic.activities;

/**
 * Created by vinod on 04-10-2016.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.msimplelogic.activities.R;

import com.msimplelogic.adapter.CallLogActivity_Adapter;
import com.msimplelogic.slidingmenu.AddRetailerFragment;
import com.msimplelogic.webservice.ConnectionDetector;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cpm.simplelogic.helper.Customer_Info;

public class CallLogActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    HashMap<String, String> hm = new HashMap<String, String>();

    DataBaseHelper dbvoc = new DataBaseHelper(this);
    ProgressDialog dialog1;
    RecyclerView recList;
    private static final String TAG = "CallLog";
    private static final int URL_LOADER = 1;
    CallLogActivity_Adapter ca;
    String s[];
    ArrayList<String> All_Contacts = new ArrayList<String>();
    ArrayList<String> All_Contacts_Mobile_Numbers = new ArrayList<String>();
    //Double amt_outstanding;
    AutoCompleteTextView autoCompleteTextView1;
    Button contact_skip;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    List<Customer_Info> result = new ArrayList<Customer_Info>();
    List<Customer_Info> result_customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.calllogactivity);

        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.contact_outocomplete);
        recList = (RecyclerView) findViewById(R.id.contact_info_card);
        contact_skip = (Button) findViewById(R.id.contact_skip);

        cd = new ConnectionDetector(getApplicationContext());


        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        SharedPreferences spf = CallLogActivity.this.getSharedPreferences("SimpleLogic", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("order", "new");
        editor.commit();

        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText("User Call log");

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = CallLogActivity.this.getSharedPreferences("SimpleLogic", 0);

            try {
                int target = (int) Math.round(sp.getFloat("Target", 0));
                int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
                if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                    int age = (int) Math.round(age_float);

                    todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                } else {
                    int age = (int) Math.round(age_float);

                    todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        dialog1 = new ProgressDialog(CallLogActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog1.setMessage("Please wait Call Log Loading....");
        dialog1.setTitle("Metal App");
        dialog1.setCancelable(false);
        dialog1.show();

        initialize();

        contact_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Global_Data.Glovel_Contact_name = "";
                Global_Data.Glovel_Contact_Mobile_Number = "";
                Intent intent = new Intent(CallLogActivity.this, AddRetailerFragment.class);
                startActivity(intent);
                finish();

            }
        });

        autoCompleteTextView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (autoCompleteTextView1.getRight() - autoCompleteTextView1.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        View view = CallLogActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        //autoCompleteTextView1.setText("");
                        autoCompleteTextView1.showDropDown();
                        return true;
                    }
                }
                return false;
            }
        });

        autoCompleteTextView1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (autoCompleteTextView1.getText().toString().trim().length() == 0) {
                    ca = new CallLogActivity_Adapter(result, CallLogActivity.this);
                    recList.setAdapter(ca);
                    ca.notifyDataSetChanged();

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                //Toast.makeText(Order.this," selected", Toast.LENGTH_LONG).show();

                Global_Data.hideSoftKeyboard(CallLogActivity.this);

                String customer_name = "";
                customer_name = autoCompleteTextView1.getText().toString().trim();

                result_customer = new ArrayList<Customer_Info>();

                for (int i = 0; i < result.size(); i++) {
                    Customer_Info ci = result.get(i);

                    if (ci.contact_name.equalsIgnoreCase(customer_name)) {

                        Customer_Info ci1 = result.get(i);

                        ci1.contact_name = ci.contact_name;
                        ci1.contact_mobile = ci.contact_mobile;
                        ci1.contact_mobileh = ci.contact_mobileh;
                        ci1.contact_call_type = ci.contact_call_type;
                        ci1.contact_call_duration = ci.contact_call_duration;
                        ci1.contact_timestamp = ci.contact_timestamp;

                        result_customer.add(ci1);
                    }
                }

                ca = new CallLogActivity_Adapter(result_customer, CallLogActivity.this);
                recList.setAdapter(ca);
                ca.notifyDataSetChanged();


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
    public void onBackPressed() {


        Intent intent = new Intent(CallLogActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    private void initialize() {
        Log.d("initialize", "initialize()");


        getLoaderManager().initLoader(URL_LOADER, null, CallLogActivity.this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        Log.d(TAG, "onCreateLoader() >> loaderID : " + loaderID);

        switch (loaderID) {
            case URL_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        this,   // Parent activity context
                        CallLog.Calls.CONTENT_URI,        // Table to query
                        null,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        CallLog.Calls.DATE + " DESC"             // Default sort order
                );
            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor managedCursor) {
        Log.d(TAG, "onLoadFinished()");

        recList.setVisibility(View.VISIBLE);
        All_Contacts.clear();
        All_Contacts_Mobile_Numbers.clear();


        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        if (managedCursor.getCount() <= 0) {
            dialog1.dismiss();
          //  Toast.makeText(this, "Call log not found.", Toast.LENGTH_SHORT).show();
            Global_Data.Custom_Toast(this, "Call log not found.","");
            Intent intent = new Intent(CallLogActivity.this, AddRetailerFragment.class);
            startActivity(intent);
            finish();
        } else {
            while (managedCursor.moveToNext()) {
                Customer_Info ci = new Customer_Info();

                String name1 = managedCursor.getString(name);
                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString(duration);
                String dir = null;

                int callTypeCode = Integer.parseInt(callType);
                switch (callTypeCode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "Outgoing";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "Incoming";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "Missed";
                        break;
                }


                if (!All_Contacts_Mobile_Numbers.contains(phNumber)) {

                    All_Contacts_Mobile_Numbers.add(phNumber);
                    if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(name1)) {
                        ci.contact_name = name1;

                        if (!All_Contacts.contains(ci.contact_name)) {
                            All_Contacts.add(name1);
                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(phNumber)) {
                                ci.contact_mobile = "Contact Number : " + phNumber;
                                ci.contact_mobileh = phNumber;
                            } else {
                                ci.contact_mobile = "Contact Number Not Found";
                                ci.contact_mobileh = "";
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(dir)) {
                                ci.contact_call_type = "Contact Call Type : " + dir;
                            } else {
                                ci.contact_call_type = "Call Type Not Found";
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(callDuration)) {
                                ci.contact_call_duration = "Call Duration : " + callDuration;
                            } else {
                                ci.contact_call_duration = "Call Duration Not Found";
                            }

                            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(callDayTime.toString())) {
                                ci.contact_timestamp = "Call Time : " + callDayTime.toString();
                            } else {
                                ci.contact_timestamp = "Call Time Not Found";
                            }

                            result.add(ci);
                        }

                    } else {
                        ci.contact_name = "Unknown";
                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(phNumber)) {
                            ci.contact_mobile = "Contact Number : " + phNumber;
                            ci.contact_mobileh = phNumber;
                        } else {
                            ci.contact_mobile = "Contact Number Not Found";
                            ci.contact_mobileh = "";
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(dir)) {
                            ci.contact_call_type = "Contact Call Type : " + dir;
                        } else {
                            ci.contact_call_type = "Call Type Not Found";
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(callDuration)) {
                            ci.contact_call_duration = "Call Duration : " + callDuration;
                        } else {
                            ci.contact_call_duration = "Call Duration Not Found";
                        }

                        if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(callDayTime.toString())) {
                            ci.contact_timestamp = "Call Time : " + callDayTime.toString();
                        } else {
                            ci.contact_timestamp = "Call Time Not Found";
                        }

                        result.add(ci);
                    }

                }


            }

            managedCursor.close();

            dialog1.dismiss();
            ca = new CallLogActivity_Adapter(result, CallLogActivity.this);
            recList.setAdapter(ca);
            ca.notifyDataSetChanged();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CallLogActivity.this, android.R.layout.simple_spinner_dropdown_item,
                    All_Contacts);
            autoCompleteTextView1.setThreshold(1);// will start working from
            // first character
            autoCompleteTextView1.setAdapter(adapter);// setting the adapter
            // data into the
            // AutoCompleteTextView
            autoCompleteTextView1.setTextColor(Color.BLACK);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset()");
        // do nothing
    }

    private class CustomerASN extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... response) {


            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            //dialog.dismiss();
            CallLogActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    dialog1.dismiss();
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
