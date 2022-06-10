package com.msimplelogic.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.msimplelogic.activities.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by sujit on 3/8/2017.
 */

public class Forget_Pwd extends BaseActivity {
    EditText email_id;
    Button forgetpwd_btn;
    String devid, str_email;
    ProgressDialog dialog;
    String resultstr;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.forget_pwd);
        forgetpwd_btn = (Button) findViewById(R.id.forgetpwd_btn);
        email_id = (EditText) findViewById(R.id.edit_email);
        //email_id.setTextColor(Color.BLACK);
        str_email = email_id.getText().toString().trim();
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        dialog = new ProgressDialog(Forget_Pwd.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

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
//            mTitleTextView.setText(getResources().getString(R.string.Forget_Password));
//
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = Forget_Pwd.this.getSharedPreferences("SimpleLogic", 0);
//            devid = sp.getString("devid", "");
//
//            forgetpwd_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(Forget_Pwd.this, "fsg", Toast.LENGTH_SHORT).show();
//                    Fun_Fpwd();
//                }
//            });
//
////    //Reading all
////    List<Local_Data> contacts = dbvoc.getAllMain();
////    for (Local_Data cn : contacts) {
////        Global_Data.local_user = ""+cn.getUser();
////        Global_Data.local_pwd = ""+cn.getemail();
////        System.out.println("Local Values:-"+Global_Data.local_user+","+Global_Data.local_pwd);
////        //Toast.makeText(LoginActivity.this, "Login:"+Global_Data.local_user,Toast.LENGTH_SHORT).show();
////    }
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
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        SharedPreferences sp = Forget_Pwd.this.getSharedPreferences("SimpleLogic", 0);
        devid = sp.getString("devid", "");

        forgetpwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Fun_Fpwd();
            }
        });
    }

    public void Fun_Fpwd() {
        dialog.setMessage("Please wait...");
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();

        if (email_id.getText().toString().length() > 0) {
            try {
                SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
                String Cust_domain = sp.getString("Cust_Service_Url", "");
                String service_url = Cust_domain + "metal/api/v1/";

                String domain = service_url;
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(domain + "/users/forgot_password?&email=" + email_id.getText().toString().trim(), null, new Response.Listener<JSONObject>() {
                    // JsonObjectRequest jsObjRequest = new JsonObjectRequest(domain+"/menus/registration?imei_no="+ URLEncoder.encode("911305401754123", "UTF-8"),null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("volley", "response: " + response.toString());
                        //  Log.i("volley", "response reg Length: " + response.length());

//                        try {
//                            String response_result = "";
//                            if (response.has("result")) {
//                                response_result = response.getString("result");
//                                if(response_result.equalsIgnoreCase("Device not found."))
//                                {
//                                    Toast toast = Toast.makeText(Forget_Pwd.this, response_result, Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
//                                }
//                            } else {
//                                response_result = "data";
//                            }
//                            dialog.dismiss();
//                            Log.d("response_result","response_result"+response_result);
//                            Log.d("response","response"+response);
//
//                            Toast.makeText(getApplicationContext(), "Register successfully."+response, Toast.LENGTH_LONG).show();


                        if (response.has("result")) {
                            String response_result = "";
                            try {
                                resultstr = response.getString("result");
                                if (resultstr.equalsIgnoreCase("Device not found.")) {
                                    dialog.dismiss();
                                    // Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG).show();

//                                    Toast toast = Toast.makeText(Forget_Pwd.this, resultstr, Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(Forget_Pwd.this, resultstr,"yes");
                                } else if (resultstr.equalsIgnoreCase("User Not Found")) {
//                                    Toast toast = Toast.makeText(Forget_Pwd.this, resultstr, Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.CENTER, 0, 0);
//                                    toast.show();
                                    Global_Data.Custom_Toast(Forget_Pwd.this, resultstr,"yes");
                                } else {
                                    try {
                                        resultstr = response.getString("result");
                                        if (resultstr.equalsIgnoreCase("Password updated successfully")) {
                                            Global_Data.forgetPwdStatus= "forget_password";
                                            SharedPreferences userNameSpf = Forget_Pwd.this.getSharedPreferences("SimpleLogic", 0);
                                            String userName = userNameSpf.getString("USER_NAME",null);

                                            String password = response.getString("password");
                                            dbvoc.update_password(password, email_id.getText().toString().trim());

//                                            Boolean compare_computed = BCrypt.checkpw(response[1], dfss);
//                                            //Boolean compare_computed = BCrypt.checkpw(test_passwd, test_hash);
//                                            String s = String.valueOf(compare_computed);

//                                            Gson gson = ((AppController)getApplication()).getGsonObject();
//                                            UserObject userData = new UserObject(Global_Data.imei_no, userName, password);
//                                            String userDataString = gson.toJson(userData);
//                                            CustomSharedPreference preff = ((AppController)getApplication()).getShared();
//                                            preff.setUserData(userDataString);

                                            dialog.dismiss();
//                                            Toast toast = Toast.makeText(Forget_Pwd.this, "Password Successfully sent to Email Id", Toast.LENGTH_LONG);
//                                            //Toast toast = Toast.makeText(Forget_Pwd.this, resultstr, Toast.LENGTH_LONG);
//                                            toast.setGravity(Gravity.CENTER, 0, 0);
//                                            toast.show();
                                            Global_Data.Custom_Toast(Forget_Pwd.this, "Password Successfully sent to Email Id","yes");
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

//                                for (int i = 0; i < users.length(); i++) {
//
//                                    JSONObject jsonObject = users.getJSONObject(i);
//
//                                    SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor edit = pref.edit();
//                                    edit.putString("login_usernm", jsonObject.getString("user_name"));
//                                    edit.putString("login_pwd", jsonObject.getString("encrypted_password"));
//                                    edit.putString("login_dtofjn", jsonObject.getString("date_of_joining"));
//                                    edit.putString("login_mobno", jsonObject.getString("mob_no"));
//                                    edit.putString("login_email", jsonObject.getString("email"));
//                                    edit.putString("login_repto", jsonObject.getString("reporting_to"));
//                                    edit.putString("login_firstnm", jsonObject.getString("first_name"));
//                                    edit.putString("login_lastnm", jsonObject.getString("last_name"));
//                                    edit.putString("login_devid", Device_id);
//                                    edit.putString("login_adrs", jsonObject.getString("address"));
//                                    edit.commit();
//

//                                    loginDataBaseAdapter.updaEntry(jsonObject.getString("user_name"), jsonObject.getString("encrypted_password"), jsonObject.getString("date_of_joining"), jsonObject.getString("mob_no"), jsonObject.getString("email"), jsonObject.getString("reporting_to"),
//                                            jsonObject.getString("first_name"), jsonObject.getString("last_name"),"", "", "", "", "",
//                                            "", Device_id, "", jsonObject.getString("address"), "", "", "", "", "");
//                                }


//                            String response_result = "";
//                            if (response_result.equalsIgnoreCase("Device not found.")) {
//
//                                // Toast.makeText(getApplicationContext(), response_result, Toast.LENGTH_LONG).show();
//
//                                Toast toast = Toast.makeText(Forget_Pwd.this, response_result, Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
//
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Register successfully.", Toast.LENGTH_LONG).show();
//                                //dbvoc.getDeleteTable("USERS");
//
////                                JSONArray users = response.getJSONArray("users");
//////
////                                Log.i("volley", "response reg users Length: " + users.length());
////
////                                if (users.length() <= 0) {
////                                    dialog.dismiss();
////                                    //Toast.makeText(LoginActivity.this, "User not found, Please contact with it team.", Toast.LENGTH_SHORT).show();
////                                    Toast toast = Toast.makeText(Forget_Pwd.this, "User not found, Please contact with it team.", Toast.LENGTH_LONG);
////                                    toast.setGravity(Gravity.CENTER, 0, 0);
////                                    toast.show();
////                                } else {
//////                                    Log.d("users", "users" + users.toString());
//////
//////
//////                                    for (int i = 0; i < users.length(); i++) {
//////
//////                                        JSONObject jsonObject = users.getJSONObject(i);
//////
//////                                        SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//////                                        SharedPreferences.Editor edit = pref.edit();
//////                                        edit.putString("login_usernm", jsonObject.getString("user_name"));
//////                                        edit.putString("login_pwd", jsonObject.getString("encrypted_password"));
//////
//////                                        edit.commit();
//////
////////                                loginDataBaseAdapter.insertEntry(jsonObject.getString("user_name"), jsonObject.getString("encrypted_password"), jsonObject.getString("date_of_joining"), jsonObject.getString("mob_no"), jsonObject.getString("email"), jsonObject.getString("reporting_to"),
////////                                        jsonObject.getString("first_name"), jsonObject.getString("last_name"),"", "", "", "", "",
////////                                        "", Device_id, "", jsonObject.getString("address"), "", "", "", "", "");
//////                                    }
////
////                                    //Toast.makeText(getApplicationContext(), "Register successfully.", Toast.LENGTH_LONG).show();
////
////
////                                    Toast toast = Toast.makeText(Forget_Pwd.this, "Register successfully.", Toast.LENGTH_LONG);
////                                    toast.setGravity(Gravity.CENTER, 0, 0);
////                                    toast.show();
////                                    dialog.dismiss();
////                                }
//	                          	                            //finish();
//                            }

                        // }

                        // output.setText(data);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            dialog.dismiss();
//                            finish();
//                        }


                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("volley", "error: " + error);
                        // Toast.makeText(getApplicationContext(), "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();
//                        Toast toast = Toast.makeText(Forget_Pwd.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(Forget_Pwd.this, getResources().getString(R.string.Server_Error),"yes");
                        dialog.dismiss();

                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                jsObjRequest.setShouldCache(false);
                int socketTimeout = 300000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsObjRequest.setRetryPolicy(policy);
                requestQueue.add(jsObjRequest);

            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }
        } else {
            dialog.dismiss();
           // Toast.makeText(this, getResources().getString(R.string.Email_ID_validation_Toast), Toast.LENGTH_SHORT).show();
            Global_Data.Custom_Toast(this, getResources().getString(R.string.Email_ID_validation_Toast),"");
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
        // TODO Auto-generated method stub
        //super.onBackPressed();
        Intent i = new Intent(Forget_Pwd.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i);
        finish();
    }
}
