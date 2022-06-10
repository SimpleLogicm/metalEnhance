package com.msimplelogic.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.msimplelogic.activities.R;

import com.msimplelogic.webservice.ConnectionDetector;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class DashboardActivity extends BaseActivity implements OnItemSelectedListener{
    //Button retail_sales, institute_sales;
    ImageView retail_sales, institute_sales,customer_services,quote_status,schedule_listn,C_profile;
    ImageView orderAnalysis,targetAchievement,productAnalysis,customerAnalysis;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    TextView schedule_txt,textView1sf;
    Toolbar toolbar;
    CardView cardOrderAnalysis,cardProductAnalysis,cardCustomerAnalysis;
    ImageView orderimg,productimg,customerimg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        Global_Data.CUSTOMER_SERVICE_FLAG = "";

        cardOrderAnalysis = (CardView) findViewById(R.id.card_orderanalysis);
        cardProductAnalysis = (CardView) findViewById(R.id.card_productanalysis);
        //targetAchievement = (ImageView) findViewById(R.id.customer_services);
        cardCustomerAnalysis = (CardView) findViewById(R.id.card_customeranalysis);
        orderimg=findViewById(R.id.orderimg);
        productimg=findViewById(R.id.productimg);
        customerimg=findViewById(R.id.customerimg);

        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1){

orderimg.setImageResource(R.drawable.orderanalysis_dark);
            productimg.setImageResource(R.drawable.productanalysis_dark);
            customerimg.setImageResource(R.drawable.customeranalysis_dark);



        }



        //quote_status = (ImageView) findViewById(R.id.quote_status);
//        schedule_listn = (ImageView) findViewById(R.id.schedule_listn);
//        C_profile = (ImageView) findViewById(R.id.C_profile);
//        //schedule_txt = (TextView) findViewById(R.id.schedule_txt);
//        textView1sf = (TextView) findViewById(R.id.textView1sf);

        Global_Data.G_BEAT_IDC = "";
        Global_Data.G_BEAT_VALUEC = "";
        Global_Data.G_BEAT_service_flag = "";
        Global_Data.G_RadioG_valueC = "";

        cd = new ConnectionDetector(getApplicationContext());

//        // for label change
//        SharedPreferences spf1=this.getSharedPreferences("SimpleLogic",0);
//        String schedulestr=spf1.getString("var_schedule", "");
//
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

        cardOrderAnalysis.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), OrderAnalysisActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

//        C_profile.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Customer_info_main.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//        });

        cardProductAnalysis.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Global_Data.PieStatus="yes";
                Intent intent = new Intent(getApplicationContext(), ProductAnalysisActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

//        targetAchievement.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), TargetAchievementActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//        });

        cardCustomerAnalysis.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Global_Data.PieStatus="yes";
                Intent intent = new Intent(getApplicationContext(), CustomerAnalysisActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

//        quote_status.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                isInternetPresent = cd.isConnectingToInternet();
//                if (isInternetPresent)
//                {
//                    Intent a = new Intent(DashboardActivity.this,Status_Act.class);
//                    startActivity(a);
//                    finish();
//                }
//                else
//                {
//                    //Toast.makeText(getApplicationContext(),"You don't have internet connection.",Toast.LENGTH_LONG).show();
//
//                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection_error), Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                }
//            }
//        });

//        schedule_listn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Global_Data.sales_btnstring="Secondary Sales / Retail Sales";
//                Global_Data.CUSTOMER_SERVICE_FLAG = "SCHEDULE" ;
//                Intent intent = new Intent(getApplicationContext(), Order.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//        });

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
//            mTitleTextView.setText(getResources().getString(R.string.DashBoard));
//
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = DashboardActivity.this.getSharedPreferences("SimpleLogic", 0);
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
//
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

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

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
                String targetNew="";
                SharedPreferences sp = DashboardActivity.this.getSharedPreferences("SimpleLogic", 0);
                try {
                    int target = (int) Math.round(sp.getFloat("Target", 0));
                    int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                    Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
                    if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                        int age = (int) Math.round(age_float);
                        if (Global_Data.rsstr.length() > 0) {
                            targetNew="T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
                            //todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        } else {
                            targetNew="T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                    } else {
                        int age = (int) Math.round(age_float);
                        if (Global_Data.rsstr.length() > 0) {
                            targetNew="T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
                            // todaysTarget.setText();
                        } else {
                            targetNew="T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
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
        //super.onBackPressed();

        Intent i = new Intent(DashboardActivity.this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i);
        finish();
    }
}
