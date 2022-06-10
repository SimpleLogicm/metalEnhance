package com.msimplelogic.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.msimplelogic.adapter.AdapterFeedbackDynamic;
import com.msimplelogic.adapter.MarketSurveyAdapter;
import com.msimplelogic.model.FeedbackDynamicModel;
import com.msimplelogic.services.getServices;
import com.msimplelogic.webservice.ConnectionDetector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class MarketSurveyActivity extends BaseActivity {
    private RecyclerView packageRecyclerView;
    List<FeedbackDynamicModel> list;
    AdapterFeedbackDynamic adapter;
    static String final_response = "";
    String response_result = "";
    ProgressDialog dialog;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Toolbar toolbar;
    ImageView hedder_theame;
    SharedPreferences sp;
    Button butSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketsurvey);
        cd = new ConnectionDetector(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        packageRecyclerView = (RecyclerView) findViewById(R.id.rv);
        butSubmit = (Button) findViewById(R.id.submit);
        hedder_theame =  findViewById(R.id.hedder_theame);
        list = new ArrayList<>();
        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);

        }
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this);
        packageRecyclerView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(packageRecyclerView.getContext(),
                        recyclerLayoutManager.getOrientation());
        packageRecyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new AdapterFeedbackDynamic(list,MarketSurveyActivity.this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(MarketSurveyActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        packageRecyclerView.setLayoutManager(layoutManager);
        packageRecyclerView.setAdapter(adapter);

        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
            dialog = new ProgressDialog(MarketSurveyActivity.this,  AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Please wait Loading....");
            dialog.setTitle("Metal App");
            dialog.setCancelable(false);
            dialog.show();

            MarketSurveyResult("Survey");
        }
        else
        {
           // Toast.makeText(MarketSurveyActivity.this, "You don't have internet connection.", Toast.LENGTH_SHORT).show();
            Global_Data.Custom_Toast(MarketSurveyActivity.this, "You don't have internet connection.","");
        }

//        butSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String message = "";
//                // get the value of selected answers from custom adapter
//                for (int i = 0; i < MarketSurveyAdapter.selectedAnswers.size(); i++) {
//                    message = message + "\n" + (i + 1) + " " + MarketSurveyAdapter.selectedAnswers.get(i);
//                }
//                // display the message on screen with the help of Toast.
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//            }
//        });

        butSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(Global_Data.quastionFeedback.size()>0)
                {
                    getServices.SyncMarketingSurveyRecord(MarketSurveyActivity.this);
                }else{
                  //  Toast.makeText(MarketSurveyActivity.this, "Please Answer the questions", Toast.LENGTH_SHORT).show();
                    Global_Data.Custom_Toast(MarketSurveyActivity.this, "Please Answer the questions","");
                }



            }
        });
    }

    public void MarketSurveyResult(String survey)
    {
        String domain = getResources().getString(R.string.service_domain);
        SharedPreferences spf = MarketSurveyActivity.this.getSharedPreferences("SimpleLogic", 0);
        String user_email = spf.getString("USER_EMAIL",null);
        Log.i("volley", "domain: " + domain);
        StringRequest jsObjRequest = null;
        String service_url = "";

        service_url = domain + "question_answer/feedback_question_options_lists?email=" + user_email + "&type=" + survey;
        //service_url = domain + "expenses/get_expenses_index?email=" + user_email + "&type=" + type+ "&year=" + "2020"+ "&month=" + "01";

        Log.i("volley", "service_url: " + service_url);
        jsObjRequest = new StringRequest(service_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", "response: " + response);
                final_response = response;
                new MarketSurveyActivity.MarketSurveyTask().execute();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Intent intent = new Intent(MarketSurveyActivity.this, MainActivity.class);
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

    private class MarketSurveyTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... responsenew) {
            try {
                JSONObject response = new JSONObject(final_response);
                if (response.has("result")) {
                    response_result = response.getString("result");
                } else {
                    response_result = "data";
                }
                if(response_result.equalsIgnoreCase("User Not Found")) {
                    MarketSurveyActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            dialog.dismiss();
//                            Toast toast = Toast.makeText(MarketSurveyActivity.this, response_result, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            Global_Data.Custom_Toast(MarketSurveyActivity.this, response_result,"yes");

                        }
                    });
                }
                else {
                    JSONArray feedback_questions = response.getJSONArray("questions");

                    Log.i("volley", "response orders Length: " + feedback_questions.length());
                    Log.d("volley", "orders" + feedback_questions.toString());

                    list.clear();

                    if (feedback_questions.length() == 0) {
                        MarketSurveyActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

//                                Toast toast = Toast.makeText(MarketSurveyActivity.this, "Record not exist", Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                dialog.dismiss();
                                Global_Data.Custom_Toast(MarketSurveyActivity.this, "Record not exist", "yes");

                            }
                        });
                    }else{
                        for (int i = 0; i < feedback_questions.length(); i++) {
                            JSONObject jsonObject = feedback_questions.getJSONObject(i);
                            JSONArray options = jsonObject.getJSONArray("options");

                            Log.d("volley", "orders" + options.length());

                            if(options.length()==4)
                            {
                                List<String> priceList = new ArrayList<String>();
                                priceList.add(options.get(0).toString());
                                priceList.add(options.get(1).toString());
                                priceList.add(options.get(2).toString());
                                priceList.add(options.get(3).toString());

                                list.add(new FeedbackDynamicModel(jsonObject.getString("question_id"),jsonObject.getString("question"),jsonObject.getString("question_type"), priceList));

                                //list.add(new FeedbackDynamicModel(jsonObject.getString("question"), jsonObject.getString("question_type"), options.get(0).toString(), options.get(1).toString(), options.get(2).toString(),options.get(3).toString()));
                            }else if(options.length()==3) {
                                List<String> priceList = new ArrayList<String>();
                                priceList.add(options.get(0).toString());
                                priceList.add(options.get(1).toString());
                                priceList.add(options.get(2).toString());
                                list.add(new FeedbackDynamicModel(jsonObject.getString("question_id"),jsonObject.getString("question"),jsonObject.getString("question_type"), priceList));

                            }else

                            {

                                List<String> priceList = new ArrayList<String>();
                                priceList.add(options.get(0).toString());
                                priceList.add(options.get(1).toString());
//                              priceList.add(options.get(2).toString());
//                              priceList.add(options.get(3).toString());

                                list.add(new FeedbackDynamicModel(jsonObject.getString("question_id"),jsonObject.getString("question"),jsonObject.getString("question_type"), priceList));

                                //list.add(new FeedbackDynamicModel(jsonObject.getString("question"), jsonObject.getString("question_type"), options.get(0).toString(), options.get(1).toString(), "",""));
                            }

                        }
                        MarketSurveyActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();

                            }
                        });
                    }

                    MarketSurveyActivity.this.runOnUiThread(new Runnable()
                    {
                        public void run() {

                            adapter.notifyDataSetChanged();
                        }
                    });

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Intent intent = new Intent(MarketSurveyActivity.this, MainActivity.class);
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
                SharedPreferences sp = MarketSurveyActivity.this.getSharedPreferences("SimpleLogic", 0);
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

}