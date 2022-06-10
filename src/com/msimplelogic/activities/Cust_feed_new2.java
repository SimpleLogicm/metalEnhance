package com.msimplelogic.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.msimplelogic.adapter.Cust_feed_new2_adaptor;
import com.msimplelogic.model.Cust_feed_new2_Model;
import com.msimplelogic.model.ExpensesModel;
import com.msimplelogic.webservice.ConnectionDetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class Cust_feed_new2 extends BaseActivity {
    public RecyclerView rv;
    Toolbar toolbar;
    List<Cust_feed_new2_Model> list;
    Cust_feed_new2_adaptor adaptor;
    static String final_response = "";
    String response_result = "";
    ProgressDialog dialog;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    TextView tv_shopname;
    TextView tv_shopadd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_feed_new2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setTitle(getResources().getString(R.string.Feedback));
        cd = new ConnectionDetector(getApplicationContext());

        rv = findViewById(R.id.rv);
        list = new ArrayList<>();
        tv_shopname=findViewById(R.id.tv_shopname);
        tv_shopadd=findViewById(R.id.tv_shopadd);

        adaptor =new Cust_feed_new2_adaptor(Cust_feed_new2.this,list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(Cust_feed_new2.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adaptor);


        SharedPreferences spf = this.getSharedPreferences("SimpleLogic", 0);
        String sname = spf.getString("shopname",null);
        String add = spf.getString("shopadd",null);

        tv_shopname.setText(sname);
        tv_shopadd.setText(add);




        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
            dialog = new ProgressDialog(Cust_feed_new2.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Please wait Loading....");
            dialog.setTitle("Metal App");
            dialog.setCancelable(false);
            dialog.show();

            FeedbackResult();

        }
        else
        {
            //Toast.makeText(Cust_feed_new2.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
            Global_Data.Custom_Toast(Cust_feed_new2.this, "You don't have internet connection.","");
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
                Global_Data.CatlogueStatus = "";
                return true;
        }
//        return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add:
                String targetNew = "";
                SharedPreferences sp = Cust_feed_new2.this.getSharedPreferences("SimpleLogic", 0);
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

    public void FeedbackResult()
    {
        String domain = getResources().getString(R.string.service_domain);

        SharedPreferences spf = Cust_feed_new2.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);

        Log.i("volley", "domain: " + domain);

        StringRequest jsObjRequest = null;
        String service_url = "";

        service_url = domain + "question_answer/feedback_question_options_lists?email=" + user_email + "&type=" + "Survey";

        //service_url = domain + "expenses/get_expenses_index?email=" + user_email + "&type=" + type+ "&year=" + "2020"+ "&month=" + "01";

        Log.i("volley", "service_url: " + service_url);

        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;

                new Cust_feed_new2.FeedbackTask().execute();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Intent intent = new Intent(Cust_feed_new2.this, MainActivity.class);
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
//                                    Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(),
                                    "ParseError   Error","");
                        } else {
                        //    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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


    private class FeedbackTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {
            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("result")) {
                    response_result = response.getString("result");

                } else {
                    response_result = "data";
                }

                if (response_result.equalsIgnoreCase("Expenses Record Not Found.")) {

                    Cust_feed_new2.this.runOnUiThread(new Runnable() {
                        public void run() {
//                            Toast toast = Toast.makeText(Cust_feed_new2.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            dialog.dismiss();
                            Global_Data.Custom_Toast(Cust_feed_new2.this, response_result,"yes");

                        }
                    });

                }
                else

                if(response_result.equalsIgnoreCase("User Not Found")) {
                    Cust_feed_new2.this.runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
//                            Toast toast = Toast.makeText(Cust_feed_new2.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(Cust_feed_new2.this, response_result,"yes");

                        }
                    });
                }
                else {
                    JSONArray feedback_questions = response.getJSONArray("questions");

                    Log.i("volley", "response orders Length: " + feedback_questions.length());
                    Log.d("volley", "orders" + feedback_questions.toString());

                    list.clear();

                        if (feedback_questions.length() == 0) {
                            Cust_feed_new2.this.runOnUiThread(new Runnable() {
                                public void run() {

//                                    Toast toast = Toast.makeText(Cust_feed_new2.this, "Record not exist", Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    dialog.dismiss();
                                    Global_Data.Custom_Toast(Cust_feed_new2.this, "Record not exist","yes");

                                }
                            });
                        }else{
                            for (int i = 0; i < feedback_questions.length(); i++) {
                                JSONObject jsonObject = feedback_questions.getJSONObject(i);
                                JSONArray options = jsonObject.getJSONArray("options");

                                Log.d("volley", "orders" + options.length());

                                if(options.length()>2)
                                {
                                    list.add(new Cust_feed_new2_Model(jsonObject.getString("question"), jsonObject.getString("question_type"), options.get(0).toString(), options.get(1).toString(), options.get(2).toString(),options.get(3).toString()));
                                }else{
                                    list.add(new Cust_feed_new2_Model(jsonObject.getString("question"), jsonObject.getString("question_type"), options.get(0).toString(), options.get(1).toString(), "",""));
                                }

                            }
                            Cust_feed_new2.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();

                                }
                            });
                        }

                    Cust_feed_new2.this.runOnUiThread(new Runnable()
                    {
                        public void run() {

                            adaptor.notifyDataSetChanged();
                        }
                    });

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Intent intent = new Intent(Cust_feed_new2.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}