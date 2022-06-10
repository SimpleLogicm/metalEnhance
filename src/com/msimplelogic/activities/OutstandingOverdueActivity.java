package com.msimplelogic.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.adapter.AdapterOutstandingOverdue;
import com.msimplelogic.model.OutstandingOverdueModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import cpm.simplelogic.helper.ConnectionDetector;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class OutstandingOverdueActivity extends BaseActivity {
    private static Double payTotal;
    private ShimmerRecyclerView outstandingOverdueList;
    AdapterOutstandingOverdue adapterOutstandingOverdue;
    List<OutstandingOverdueModel> outstandingOverdueArrlist = new ArrayList<>();
    ConnectionDetector cd;
    ProgressDialog dialog;
    static TextView txtTotal;
    static String final_response = "";
    String response_result = "";
    Button btnPay, btn_history;
    ArrayList<String> amountTotal = new ArrayList<String>();
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    Toolbar toolbar;
    //private PrefManager prefManager;
    SharedPreferences sp;
    ImageView hedder_theame;
    LoginDataBaseAdapter loginDataBaseAdapter = new LoginDataBaseAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_outstanding_main);
        DataBaseHelper dbvoc = new DataBaseHelper(this);
        outstandingOverdueList = findViewById(R.id.outstanding_overdue_list);
        txtTotal = findViewById(R.id.txt_total);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(" ");
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        cd = new ConnectionDetector(getApplicationContext());
        btnPay = findViewById(R.id.btn_pay);
        btn_history = findViewById(R.id.btn_history);
        hedder_theame = findViewById(R.id.hedder_theame);
        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);

        }

        adapterOutstandingOverdue = new AdapterOutstandingOverdue(getApplicationContext(), outstandingOverdueArrlist);
        //annoucement_adapter = new Annoucement_Adapter(getApplicationContext(), ann_list, Announcement.this, Announcement.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        outstandingOverdueList.setLayoutManager(mLayoutManager);
        outstandingOverdueList.setItemAnimator(new DefaultItemAnimator());
        outstandingOverdueList.setAdapter(adapterOutstandingOverdue);
        outstandingOverdueList.showShimmerAdapter();

        //SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        //ttl_price = spf1.getString("var_total_price", "");
//        try {
//            ActionBar mActionBar = getActionBar();
//            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            LayoutInflater mInflater = LayoutInflater.from(this);
//            Intent i = getIntent();
//            String name = i.getStringExtra("retialer");
//            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
//            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
//            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
//            mTitleTextView.setText("Outstanding Overdue");
//
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = OutstandingOverdueActivity.this.getSharedPreferences("SimpleLogic", 0);
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
//                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
//                } else {
//                    int age = (int) Math.round(age_float);
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    }
//                    //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                }
//
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
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payDialog(payTotal);
            }
        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OutstandingOverdueActivity.this, Payhistory.class);
                startActivity(intent);
            }
        });

        if (cd.isConnectingToInternet()) {
            OutstandingOverdueResult();
        } else {
//            Toast toast = Toast.makeText(OutstandingOverdueActivity.this, "You don't have internet connection.", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();

            Global_Data.Custom_Toast(OutstandingOverdueActivity.this, "You don't have internet connection.", "yes");
            Intent order_home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(order_home);
            finish();
        }
    }

    private void payDialog(final Double value1) {
        // Create custom dialog object
        final Dialog dialog = new Dialog(OutstandingOverdueActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.pay_dialog);
        // Set dialog title
        dialog.setTitle("PAY");
        dialog.setCancelable(false);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ImageView payCloseBtn = (ImageView) dialog.findViewById(R.id.payclose_btn);
        Button payOkBtn = (Button) dialog.findViewById(R.id.payok_btn);
        Button payCancelBtn = (Button) dialog.findViewById(R.id.paycancel_btn);
        final EditText payEdit = (EditText) dialog.findViewById(R.id.payamount_edit);

        //final Double payTotal=Double.valueOf(value);

        dialog.show();

        //Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
        // if decline button is clicked, close the custom dialog
        payCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

        payOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = payEdit.getText().toString().trim();
                if (str.length() > 0) {

                    if (!str.equalsIgnoreCase("0")) {
                        final Double editPayValue = (Double.valueOf(Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJavanewzpochecck(payEdit.getText().toString())));
                        if (editPayValue > value1) {
                           // Toast.makeText(OutstandingOverdueActivity.this, "Value is greater than Total", Toast.LENGTH_SHORT).show();
                            Global_Data.Custom_Toast(OutstandingOverdueActivity.this, "Value is greater than Total","yes");
                        } else {
                            PayService(payEdit.getText().toString());
                        }
                    } else {
                      //  Toast.makeText(OutstandingOverdueActivity.this, "Please Enter Valid Amount", Toast.LENGTH_SHORT).show();
                        Global_Data.Custom_Toast(OutstandingOverdueActivity.this, "Please Enter Valid Amount","yes");
                    }

                } else {
                    //Toast.makeText(OutstandingOverdueActivity.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                    Global_Data.Custom_Toast(OutstandingOverdueActivity.this, "Please Enter Amount","yes");
                }


                // Close dialog
                dialog.dismiss();
            }
        });

        payCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
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
                return true;
        }
//		return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add:
                String targetNew = "";
                SharedPreferences sp = OutstandingOverdueActivity.this.getSharedPreferences("SimpleLogic", 0);
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
        super.onBackPressed();
        Global_Data.SpeedometerStatus = "yes";
        Intent i = new Intent(OutstandingOverdueActivity.this, OutstandingOverdueActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        startActivity(i);
        finish();
    }

    public void OutstandingOverdueResult() {
        String domain = getResources().getString(R.string.service_domain);

        Log.i("volley", "domain: " + domain);

        StringRequest jsObjRequest = null;
        String service_url = "";
        // http://150.242.140.105:8005/metal/api/v1/customers/get_customer_outstanding_records?customer_id=236

        SharedPreferences spf = this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

        service_url = domain + "customers/get_customer_outstanding_records?customer_code=" + Global_Data.GLOvel_CUSTOMER_ID + "&email=" + user_email;
        //  service_url = domain + "customers/get_customer_outstanding_records?customer_code=" + "New10114913";
        //service_url = "http://150.242.140.105:8004/m_catalogue/api/v1/primary_category/display_list";

        Log.i("url", "url" + service_url);

        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;

                new OutstandingOverdueTask().execute(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Intent intent = new Intent(OutstandingOverdueActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

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
                                  //  Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "ParseError   Error","");
                        } else {
                           // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(),"");
                        }

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

    private class OutstandingOverdueTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {

            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("message")) {
                    response_result = response.getString("message");
                } else {
                    response_result = "data";
                }

                if (response_result.equalsIgnoreCase("AnnouncementTypes Not Found")) {

                    OutstandingOverdueActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            outstandingOverdueList.hideShimmerAdapter();

//                            Toast toast = Toast.makeText(OutstandingOverdueActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(OutstandingOverdueActivity.this, response_result,"yes");

                            Intent intent = new Intent(OutstandingOverdueActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else if (response_result.equalsIgnoreCase("User not registered")) {

                    OutstandingOverdueActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            outstandingOverdueList.hideShimmerAdapter();

//                            Toast toast = Toast.makeText(OutstandingOverdueActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(OutstandingOverdueActivity.this, response_result,"yes");

                            Intent intent = new Intent(OutstandingOverdueActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {

                    JSONArray users = response.getJSONArray("records");
                    Log.i("volley", "response users Length: " + users.length());
                    Log.d("volley", "users" + users.toString());
                    if (users.length() <= 0) {

                        OutstandingOverdueActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                outstandingOverdueList.hideShimmerAdapter();
                                Global_Data.Custom_Toast(OutstandingOverdueActivity.this, "Record not exist", "Yes");
//                                Toast toast = Toast.makeText(OutstandingOverdueActivity.this, "Record not exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

                                Intent intent = new Intent(OutstandingOverdueActivity.this, Neworderoptions.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {

                        outstandingOverdueArrlist.clear();
                        amountTotal.clear();

                        for (int i = 0; i < users.length(); i++) {

                            JSONObject jsonObject = users.getJSONObject(i);

                            outstandingOverdueArrlist.add(new OutstandingOverdueModel(jsonObject.getString("order_date"), jsonObject.getString("order_amount"), jsonObject.getString("pending_amount")));
//                            outstandingOverdueArrlist.add(new OutstandingOverdueModel("2019-08-10","44000","3200"));
//                            outstandingOverdueArrlist.add(new OutstandingOverdueModel("2019-08-10","404000","32000"));
//                            outstandingOverdueArrlist.add(new OutstandingOverdueModel("2019-08-10","44000","3200"));
//                            outstandingOverdueArrlist.add(new OutstandingOverdueModel("01/07/2019","4500.0","7960.0"));
//                            outstandingOverdueArrlist.add(new OutstandingOverdueModel("08/08/2019","4500","7960"));
//                            amountTotal.add("7960");
                            amountTotal.add(jsonObject.getString("pending_amount"));
//                            amountTotal.add("44000");
//                            amountTotal.add("44000");
//                            amountTotal.add("44000");
//                            amountTotal.add("44000");
//                            amountTotal.add("44000");

//                            loginDataBaseAdapter.insert_outstanding_overdue(jsonObject.getString("type"), jsonObject.getString("id"), jsonObject.getString("type"), jsonObject.getString("type"), jsonObject.getString("type"), jsonObject.getString("type"),
//                                    jsonObject.getString("type"), jsonObject.getString("type"));

                        }

                        OutstandingOverdueActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                Double sum = 0.0;
                                for (int m = 0; m < amountTotal.size(); m++) {
                                    sum += Double.valueOf(amountTotal.get(m));
                                }
                                updateSum(sum);

                                outstandingOverdueList.hideShimmerAdapter();
                                adapterOutstandingOverdue.notifyDataSetChanged();

//                                List<Local_Data> contacts = dbvoc.getOutstandingOverdue();
//                                if(contacts.size() <= 0)
//                                {
//                                    Toast toast = Toast.makeText(OutstandingOverdueActivity.this,
//                                    getResources().getString(R.string.Customer_Not_Found), Toast.LENGTH_SHORT);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//                                }
//                                else {
//                                    for (Local_Data cn : contacts) {
//
//                                        String sss = cn.getOutstanding_overdue_custid();
//                                        Toast.makeText(OutstandingOverdueActivity.this, sss, Toast.LENGTH_SHORT).show();
////                                        Global_Data.customer_MobileNumber = cn.getMOBILE_NO();
////                                        Global_Data.CUSTOMER_NAME_NEW = cn.getCUSTOMER_NAME();
////                                        Global_Data.CUSTOMER_ADDRESS_NEW = cn.getAddress();
//
//                                    }
//                                }

//                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(OutstandingOverdueActivity.this,
//                                        android.R.layout.simple_spinner_dropdown_item,list_announcement);
//                                autoSearchAnnouoncement.setThreshold(1);// will start working from
//// first character
//                                autoSearchAnnouoncement.setAdapter(adapter);// setting the adapter
//// data into the
//// AutoCompleteTextView
//                                autoSearchAnnouoncement.setTextColor(Color.BLACK);

                                //recyclerView.hideShimmerAdapter();
                                // Org_Events_adapter.notifyDataSetChanged();

                                //AutoCompleteEmployeeArrayAdapter adapter = new AutoCompleteEmployeeArrayAdapter(Org_Events.this,R.layout.emp_search_layout, All_Employees);

                                // autoCompleteTextView.setAdapter(adapter);

                                //autoCompleteTextView.setTextColor(Color.BLACK);
                                // autoCompleteTextView.setDropDownBackgroundDrawable(new ColorDrawable(getApplicationContext().getResources().getColor(R.color.white)));
                            }
                        });
                    }

                    OutstandingOverdueActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            outstandingOverdueList.hideShimmerAdapter();
                        }
                    });
                    //	dialog.dismiss();
                    //finish();
                }

                // }

                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();

                Intent intent = new Intent(OutstandingOverdueActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                OutstandingOverdueActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        outstandingOverdueList.hideShimmerAdapter();
                    }
                });
            }

            OutstandingOverdueActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    outstandingOverdueList.hideShimmerAdapter();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            OutstandingOverdueActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    outstandingOverdueList.hideShimmerAdapter();
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

    public static void updateSum(Double sum) {
        payTotal = sum;
        txtTotal.setText("" + sum);
//        if (price_str.length() > 0) {
//            txttotalPreview.setText(price_str + " : " + sum);
//            txttotalPreview1.setText(""+sum);
//        } else {
//            txttotalPreview.setText(c_Total + sum);
//            txttotalPreview1.setText(""+sum);
//        }
        //txttotalPreview.setText("Total		:		"+sum);
    }

    public void PayService(String edit_value) {
        String domain = "";

        dialog = new ProgressDialog(OutstandingOverdueActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(getResources().getString(R.string.Please_Wait));
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        SharedPreferences spf = this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL", null);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
        String Cust_domain = sp.getString("Cust_Service_Url", "");
        String service_url = Cust_domain + "metal/api/v1/";
        domain = service_url;

        StringRequest stringRequest = null;

        Log.i("service", "service" + domain + "customers/paid_outstanding_amount?customer_code=" + Global_Data.GLOvel_CUSTOMER_ID + "&paid_amount=" + edit_value + "&email=" + user_email);
        stringRequest = new StringRequest(Request.Method.POST, domain + "customers/paid_outstanding_amount?customer_code=" + Global_Data.GLOvel_CUSTOMER_ID + "&paid_amount=" + edit_value + "&email=" + user_email,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("jV", "JV length" + response.length());
                        // JSONObject person = (JSONObject) (response);
                        try {
                            JSONObject json = new JSONObject(new JSONTokener(response));
                            try {

                                String response_result = "";
                                if (json.has("message")) {
                                    response_result = json.getString("message");
                                } else {
                                    response_result = "data";
                                }

                                if (response_result.equalsIgnoreCase("product not found")) {
                                    //Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG).show();
//
//                                    Toast toast = Toast.makeText(OutstandingOverdueActivity.this, response_result, Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();

                                    Global_Data.Custom_Toast(OutstandingOverdueActivity.this, response_result,"yes");

                                    Intent a = new Intent(OutstandingOverdueActivity.this, MainActivity.class);
                                    startActivity(a);
                                    finish();
                                } else if (response_result.equalsIgnoreCase("Device not registered")) {

                                    // Toast.makeText(Status_Activity.this, response_result, Toast.LENGTH_LONG).show();

//                                    Toast toast = Toast.makeText(OutstandingOverdueActivity.this, response_result, Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(OutstandingOverdueActivity.this, response_result, "yes");

                                } else {

//                                    Toast toast = Toast.makeText(OutstandingOverdueActivity.this, response_result, Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();

                                    Global_Data.Custom_Toast(OutstandingOverdueActivity.this, response_result,"yes");

                                    dialog.dismiss();
                                    OutstandingOverdueResult();
                                }

                            } catch (JSONException e) {

                                e.printStackTrace();
                                dialog.dismiss();
                                Intent a = new Intent(OutstandingOverdueActivity.this, MainActivity.class);
                                startActivity(a);
                                finish();
                            }


                            //dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  finish();
                            dialog.dismiss();
                            Intent a = new Intent(OutstandingOverdueActivity.this, MainActivity.class);
                            startActivity(a);
                            finish();
                        }
                        //dialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Network Error",
//	                              Toast.LENGTH_LONG).show();
//                            Toast toast = Toast.makeText(OutstandingOverdueActivity.this, "Network Error", Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(OutstandingOverdueActivity.this, "Network Error","yes");

                        } else if (error instanceof AuthFailureError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Server AuthFailureError  Error",
//	                              Toast.LENGTH_LONG).show();
//                            Toast toast = Toast.makeText(OutstandingOverdueActivity.this, "Server AuthFailureError  Error", Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(OutstandingOverdueActivity.this, "Server AuthFailureError  Error","yes");
                        } else if (error instanceof ServerError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Server   Error",
//	                              Toast.LENGTH_LONG).show();
//                            Toast toast = Toast.makeText(OutstandingOverdueActivity.this,
//                                    getResources().getString(R.string.Server_Errors),
//                                    Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(OutstandingOverdueActivity.this,
                                    getResources().getString(R.string.Server_Errors),"yes");
                        } else if (error instanceof NetworkError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "Network   Error",
//	                              Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(OutstandingOverdueActivity.this,
//                                    "Network   Error",
//                                    Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Global_Data.Custom_Toast(OutstandingOverdueActivity.this,
                                    "Network   Error","yes");
                        } else if (error instanceof ParseError) {
//	                      Toast.makeText(Status_Activity.this,
//	                              "ParseError   Error",
//	                              Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(OutstandingOverdueActivity.this,
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(OutstandingOverdueActivity.this,
                                    "ParseError   Error","yes");
                        } else {
                            //Toast.makeText(Status_Activity.this, error.getMessage(), Toast.LENGTH_LONG).show();

//                            Toast toast = Toast.makeText(OutstandingOverdueActivity.this,
//                                    error.getMessage(),
//                                    Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(OutstandingOverdueActivity.this,
                                    error.getMessage(),"yes");
                        }

                        dialog.dismiss();
                        Intent a = new Intent(OutstandingOverdueActivity.this, MainActivity.class);
                        startActivity(a);
                        finish();
                        // finish();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 300000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // requestQueue.se
        //requestQueue.add(jsObjRequest);
        stringRequest.setShouldCache(false);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

}
