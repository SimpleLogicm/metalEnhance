package com.msimplelogic.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

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
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.msimplelogic.activities.R;
import com.msimplelogic.adapter.AdapterCatalogueScheme;
import com.msimplelogic.model.CatalogueSchemeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cpm.simplelogic.helper.ConnectionDetector;

public class CatalogueSchemeActivity extends Activity {
    private ShimmerRecyclerView catalogueSchemeList;
    AdapterCatalogueScheme adapterCatalogueScheme;
    List<CatalogueSchemeModel> catalogueSchemeArrlist = new ArrayList<>();
    ConnectionDetector cd;
    static String final_response = "";
    String response_result = "";
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    //private PrefManager prefManager;
    LoginDataBaseAdapter loginDataBaseAdapter = new LoginDataBaseAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_catalogue_scheme);
        DataBaseHelper dbvoc = new DataBaseHelper(this);
        catalogueSchemeList=findViewById(R.id.catalogue_scheme_list);

        loginDataBaseAdapter = loginDataBaseAdapter.open();
        cd  = new ConnectionDetector(getApplicationContext());

        adapterCatalogueScheme = new AdapterCatalogueScheme(getApplicationContext(), catalogueSchemeArrlist);
        //annoucement_adapter = new Annoucement_Adapter(getApplicationContext(), ann_list, Announcement.this, Announcement.this);
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        catalogueSchemeList.setLayoutManager(new GridLayoutManager(this, 2));
        //catalogueSchemeList.setLayoutManager(mLayoutManager);
        catalogueSchemeList.setItemAnimator(new DefaultItemAnimator());
        catalogueSchemeList.setAdapter(adapterCatalogueScheme);
        catalogueSchemeList.showShimmerAdapter();

        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        //ttl_price = spf1.getString("var_total_price", "");
        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            LayoutInflater mInflater = LayoutInflater.from(this);
            Intent i = getIntent();
            String name = i.getStringExtra("retialer");
            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText("Catalogue Scheme");

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = CatalogueSchemeActivity.this.getSharedPreferences("SimpleLogic", 0);

//	       if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//	       	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//			}

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
//	       	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
                todaysTarget.setText("Today's Target Acheived");
            }

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (cd.isConnectingToInternet())
        {
            CatalogueSchemeResult();
        }
        else
        {
            Global_Data.Custom_Toast(CatalogueSchemeActivity.this,"You don't have internet connection.","Yes");
//            Toast toast = Toast.makeText(CatalogueSchemeActivity.this,"You don't have internet connection.", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();

            Intent order_home = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(order_home);
            finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CatalogueSchemeActivity.this, Customer_info_main.class);
        startActivity(i);
        finish();
    }

    public void CatalogueSchemeResult()
    {
        String domain = getResources().getString(R.string.service_domain);

        Log.i("volley", "domain: " + domain);

        StringRequest jsObjRequest = null;
        String service_url = "";

         //service_url = "http://iportaltest.anchor-group.in/api/v1/announcement/show_announcement_type?employee_code=A02105";

        //Log.i("volley", "service_url: " + service_url);

        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;

                new CatalogueSchemeTask().execute(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Intent intent = new Intent(CatalogueSchemeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Network Error","");
//                            Toast.makeText(getApplicationContext(),
//                                    "Network Error",
//                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Server AuthFailureError  Error","");
//                            Toast.makeText(getApplicationContext(),
//                                    "Server AuthFailureError  Error",
//                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Server   Error","");
//                            Toast.makeText(getApplicationContext(),
//                                    "Server   Error",
//                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "Network   Error","");
//                            Toast.makeText(getApplicationContext(),
//                                    "Network   Error",
//                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Global_Data.Custom_Toast((getApplicationContext()),"ParseError   Error","");
//                            Toast.makeText(getApplicationContext(),
//                                    "ParseError   Error",
//                                    Toast.LENGTH_LONG).show();
                        } else {
                            Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(),"");
                          //  Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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

    private class CatalogueSchemeTask extends AsyncTask<String, Void, String> {
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

                    CatalogueSchemeActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            catalogueSchemeList.hideShimmerAdapter();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

                            Global_Data.Custom_Toast(CatalogueSchemeActivity.this, response_result,"Yes");
//                            Toast toast = Toast.makeText(CatalogueSchemeActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Intent intent = new Intent(CatalogueSchemeActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                else
                if(response_result.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    CatalogueSchemeActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            catalogueSchemeList.hideShimmerAdapter();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();

                            Global_Data.Custom_Toast(CatalogueSchemeActivity.this, response_result,"Yes");
//                            Toast toast = Toast.makeText(CatalogueSchemeActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Intent intent = new Intent(CatalogueSchemeActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                else {

                    JSONArray users = response.getJSONArray("types");
                    Log.i("volley", "response users Length: " + users.length());
                    Log.d("volley", "users" + users.toString());
                    if (users.length() <= 0) {

                        CatalogueSchemeActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                catalogueSchemeList.hideShimmerAdapter();
                                Global_Data.Custom_Toast(CatalogueSchemeActivity.this, "Record not exist","Yes");
//                                Toast toast = Toast.makeText(CatalogueSchemeActivity.this, "Record not exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();

//                                Intent intent = new Intent(Org_Events.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();
                            }
                        });
                    } else {

                        catalogueSchemeArrlist.clear();
                        //ann_list.clear();
                        //All_Employees.clear();
                        for (int i = 0; i < users.length(); i++)
                        {

                            JSONObject jsonObject = users.getJSONObject(i);

                            catalogueSchemeArrlist.add(new CatalogueSchemeModel(jsonObject.getString("type"),jsonObject.getString("id"),""));
                            //list_announcement.add(jsonObject.getString("type"));
                            loginDataBaseAdapter.insert_outstanding_overdue(jsonObject.getString("type"), jsonObject.getString("id"), jsonObject.getString("type"), jsonObject.getString("type"), jsonObject.getString("type"), jsonObject.getString("type"),
                                    jsonObject.getString("type"), jsonObject.getString("type"));

                            //All_Employees.add(emp_list.get(i));

                        }


                        CatalogueSchemeActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {

                                catalogueSchemeList.hideShimmerAdapter();
                                adapterCatalogueScheme.notifyDataSetChanged();

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

//
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

                    CatalogueSchemeActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            catalogueSchemeList.hideShimmerAdapter();
                        }
                    });
                    //	dialog.dismiss();
                    //finish();
                }

                // }

                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();

                Intent intent = new Intent(CatalogueSchemeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                CatalogueSchemeActivity.this.runOnUiThread(new Runnable() {
                    public void run() {

                        catalogueSchemeList.hideShimmerAdapter();
                    }
                });

            }


            CatalogueSchemeActivity.this.runOnUiThread(new Runnable() {
                public void run() {

                    catalogueSchemeList.hideShimmerAdapter();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            CatalogueSchemeActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    catalogueSchemeList.hideShimmerAdapter();
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
