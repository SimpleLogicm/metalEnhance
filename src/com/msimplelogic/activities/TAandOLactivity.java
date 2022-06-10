package com.msimplelogic.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

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
import com.msimplelogic.activities.R;
import com.msimplelogic.adapter.AdapterTaOl;
import com.msimplelogic.helper.SwipeController;
import com.msimplelogic.helper.SwipeControllerActions;
import com.msimplelogic.model.TargetOrderListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cpm.simplelogic.helper.ConnectionDetector;

public class TAandOLactivity extends Activity {
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private CoordinatorLayout coordinatorLayout;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    SwipeController swipeController = null;
    private AdapterTaOl mAdapter;
    static String final_response = "";
    static String final_response1 = "";
    ProgressDialog dialog;
    String response_result = "";
    String response_result1 = "";
    RecyclerView recyclerView;
    List<TargetOrderListModel> target_order_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta_ol);
        cd  = new ConnectionDetector(getApplicationContext());
        //setOrderListData();
        setupRecyclerView();
    }

    private void setupRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.taorder_list);
        mAdapter = new AdapterTaOl(target_order_list,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
              //  Toast.makeText(TAandOLactivity.this, "View", Toast.LENGTH_SHORT).show();
                Global_Data.Custom_Toast(TAandOLactivity.this, "View", "yes");
//                mAdapter.players.remove(position);
//                mAdapter.notifyItemRemoved(position);
//                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
            }

//            @Override
//            public void onLeftClicked(int position) {
//                super.onLeftClicked(position);
//                Toast.makeText(MainActivity.this, "Edit", Toast.LENGTH_SHORT).show();
//            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });


        if (cd.isConnectingToInternet())
        {
//            dialog = new ProgressDialog(TargetAnalysisActivity.this,  android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//            dialog.setMessage("Please wait Target Loading....");
//            dialog.setTitle("Metal App");
//            dialog.setCancelable(false);
//            dialog.show();

            TargetOrderResult();
        }
        else
        {
//            Toast toast = Toast.makeText(TAandOLactivity.this,"You don't have internet connection.", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();

            Global_Data.Custom_Toast(TAandOLactivity.this,"You don't have internet connection.","yes");

            Intent order_home = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(order_home);
            finish();
        }


    }

    public void TargetOrderResult()
    {
        SharedPreferences spf = TAandOLactivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        String domain = getResources().getString(R.string.service_domain);

        Log.i("volley", "domain: " + domain);

        StringRequest jsObjRequest = null;
        String service_url = "";

        //service_url = domain + "beats/beats_performance?imei_no=" + Global_Data.CityName + "&email=" + Global_Data.CityName;
//        service_url = domain + "primary_category/"+item_cat+"/show_item_list";

        service_url = domain + "users/order_list?email=" + user_email + "&imei_no=" + Global_Data.imei_no;


        Log.i("volley", "service_url: " + service_url);

        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response1 = response;

                new TAandOLactivity.TargetOrderTask().execute(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Intent intent = new Intent(TAandOLactivity.this, MainActivity.class);
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
                            Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(), "");
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

    private class TargetOrderTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {

            try {
                JSONObject response = new JSONObject(final_response1);
                if (response.has("message")) {
                    response_result1 = response.getString("message");
                } else {
                    response_result1 = "data";
                }

                if (response_result1.equalsIgnoreCase("No order found")) {

                    //Toast.makeText(Order.this, response_result, Toast.LENGTH_LONG).show();

                    TAandOLactivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            //adapter.notifyDataSetChanged();
                            //recyclerView.hideShimmerAdapter();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();
                            //dialog.dismiss();
//                            Toast toast = Toast.makeText(TAandOLactivity.this, response_result1, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(TAandOLactivity.this, response_result1,"yes");

//                            Intent intent = new Intent(AnchorFanActivity.this, MainActivity.class);
//                            startActivity(intent);
                            finish();
                        }
                    });
                }
                else
                if(response_result1.equalsIgnoreCase("User not registered")) {

                    //Toast.makeText(MainActivity.this, response_result, Toast.LENGTH_LONG).show();

                    TAandOLactivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            //adapter.notifyDataSetChanged();
                            //recyclerView.hideShimmerAdapter();
                            //Toast.makeText(Order.this, "Delivery Schedule Not Found.", Toast.LENGTH_LONG).show();
                            //dialog.dismiss();
//                            Toast toast = Toast.makeText(TAandOLactivity.this, response_result1, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(TAandOLactivity.this, response_result1,"yes");

                            Intent intent = new Intent(TAandOLactivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
                else {

                    JSONArray regions = response.getJSONArray("order_products");
                    Log.i("volley", "response regions Length: " + regions.length());
                    Log.d("volley", "regions" + regions.toString());

                    if (regions.length() <= 0) {

                        TAandOLactivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                //adapter.notifyDataSetChanged();
                                //recyclerView.hideShimmerAdapter();
//                                Toast toast = Toast.makeText(TAandOLactivity.this, "Record not exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(TAandOLactivity.this, "Record not exist","yes");
                                //dialog.dismiss();
                                Intent intent = new Intent(TAandOLactivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {

                        //list_Offices.clear();
                        target_order_list.clear();
                        for (int i = 0; i < regions.length(); i++)
                        {

                            JSONObject jsonObject = regions.getJSONObject(i);

//                            HashMap<String, String> mapp = new HashMap<String, String>();
//                            mapp.put(TAG_ORDERID, jsonObject.getString("order_number"));
//                            mapp.put(TAG_PRODUCTNM, jsonObject.getString("product_name"));
//                            mapp.put(TAG_TOTALQTY, jsonObject.getString("total_qty"));
//                            mapp.put(TAG_RETAILPRICE, jsonObject.getString("retail_price"));
//                            mapp.put(TAG_MRP, jsonObject.getString("mrp"));
//                            mapp.put(TAG_AMOUNT, jsonObject.getString("amount"));
//                            mapp.put(TAG_CUSTSHOPNAME, jsonObject.getString("customer_shop_name"));
//
//                            //txttotalPreview.setText("Total		:		"+cnt1.getAmount());
//                            //Toast.makeText(NewOrderActivity.this, "Login:"+Global_Data.order_id,Toast.LENGTH_SHORT).show();
//                            SwipeList.add(mapp);

                            target_order_list.add(new TargetOrderListModel(jsonObject.getString("order_number"), jsonObject.getString("product_name"), jsonObject.getString("total_qty"), jsonObject.getString("retail_price"), jsonObject.getString("mrp"), jsonObject.getString("amount"), jsonObject.getString("customer_shop_name")));
                            //list_Offices.add(jsonObject.getString("name"));

                        }
//                        office_list.add(new office_List_Model("More", "", ""));
//                        list_Offices.add("More");

                        TAandOLactivity.this.runOnUiThread(new Runnable()
                        {
                            public void run() {

                                // adapter = new AdapterTargetAnalysisOrderList(TargetAnalysisActivity.this, SwipeList);
                                //dialog.dismiss();
//                                recyclerView.hideShimmerAdapter();
//                                office_list_adapter.notifyDataSetChanged();
//
//                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Office_List.this,
//                                        android.R.layout.simple_spinner_dropdown_item,list_Offices);
//                                autoCompleteTextView.setThreshold(1);// will start working from
//// first character
//                                autoCompleteTextView.setAdapter(adapter);// setting the adapter
//// data into the
//// AutoCompleteTextView
//                                autoCompleteTextView.setTextColor(Color.BLACK);
                            }
                        });
                    }

                    TAandOLactivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            //dialog.dismiss();
                            //recyclerView.hideShimmerAdapter();
                        }
                    });
                    //	dialog.dismiss();
                    //finish();
                }
                // }

                // output.setText(data);
            } catch (JSONException e) {
                e.printStackTrace();
                //dialog.dismiss();
                Intent intent = new Intent(TAandOLactivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                TAandOLactivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        //adapter.notifyDataSetChanged();
                        //recyclerView.hideShimmerAdapter();
                    }
                });
            }

            TAandOLactivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //dialog.dismiss();
                    //adapter.notifyDataSetChanged();
                    // recyclerView.hideShimmerAdapter();
                }
            });

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            TAandOLactivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    //dialog.dismiss();
                    //adapter.notifyDataSetChanged();
                    //recyclerView.hideShimmerAdapter();
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
