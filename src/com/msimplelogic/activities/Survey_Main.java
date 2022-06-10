package com.msimplelogic.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.msimplelogic.activities.R;
import com.msimplelogic.activities.kotlinFiles.Marketing;
import com.msimplelogic.webservice.ConnectionDetector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cpm.simplelogic.helper.OnOptionSelected;
import cpm.simplelogic.helper.QuestionModel;

/**
 * Created by vinod on 04-10-2016.
 */

public class Survey_Main extends BaseActivity implements OnOptionSelected {
    Button bAdd;
    String survey_code = "";
    String active_from = "";
    String active_to = "";
    ProgressDialog dialog;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    private RecyclerView mRecyclerView;
    private List<QuestionModel> questionModels;
    private ElementListAdapter questionAdapter;
    private ArrayList<String> hashkey = new ArrayList<String>();
    private ArrayList<String> hashvalue = new ArrayList<String>();
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    LoginDataBaseAdapter loginDataBaseAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_quastion_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView =(RecyclerView)findViewById(R.id.survey_List);
        bAdd = (Button) findViewById(R.id.bAdd);
        mRecyclerView.setHasFixedSize(true);

        cd = new ConnectionDetector(getApplicationContext());

        loginDataBaseAdapter=new LoginDataBaseAdapter(getApplicationContext());
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        questionModels=new ArrayList<QuestionModel>();

        List<Local_Data> quastions = dbvoc.Get_SURVEY_QUESTIONS();

        for (Local_Data q_data : quastions) {

            QuestionModel questionModel=new QuestionModel();
            questionModel.setQuestion(q_data.getquestion());
            questionModel.setQuestioncode(q_data.getquestion_code());

            Global_Data.quastionmap.put(q_data.getquestion_code()+":"+q_data.getquestion(),"");

            questionModel.setoption1value(q_data.getoption_1());
            questionModel.setoption2value(q_data.getoption_2());
            questionModel.setoption3value(q_data.getoption_3());
            questionModel.setoption4value(q_data.getoption_4());
            questionModel.setoption5value(q_data.getoption_5());

            survey_code = q_data.getsurvey_code();
            active_from = q_data.getactive_from();
            active_to = q_data.getactive_to();

            questionModels.add(questionModel);
        }


        questionAdapter =new ElementListAdapter(Survey_Main.this,questionModels);
        //questionAdapter.setOnOptionSelected(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
      //  questionAdapter.setQuestionModels(questionModels);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(questionAdapter);

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences spf = Survey_Main.this.getSharedPreferences("SimpleLogic", 0);
                String user_email = spf.getString("USER_EMAIL",null);
//                isInternetPresent = cd.isConnectingToInternet();
//                if (isInternetPresent)
//                {
                    hashkey.clear();
                    hashvalue.clear();
                    Iterator myVeryOwnIterator = Global_Data.quastionmap.keySet().iterator();
                    while(myVeryOwnIterator.hasNext()) {
                        String key=(String)myVeryOwnIterator.next();
                        String value=(String)Global_Data.quastionmap.get(key);
                        Log.d("map key","Map key"+key);
                        Log.d("map value","Map value"+value);
                        hashkey.add(key);
                        hashvalue.add(value);
                    }
                    if(hashvalue.contains(""))
                    {
                        for(int i=0; i< hashkey.size(); i++)
                        {
                            if(hashvalue.get(i).equalsIgnoreCase(""))
                            {
                                String code_value [] = hashkey.get(i).split(":");
                               // Toast.makeText(Survey_Main.this, "Please select at least one option of " +code_value[1] , Toast.LENGTH_LONG).show();

//                                Toast toast = Toast.makeText(Survey_Main.this, getResources().getString(R.string.survey_quastion_validation_message) + " " + code_value[1], Toast.LENGTH_LONG);
//                                toast.setGravity(Gravity.CENTER, 0, 0);
//                                toast.show();
                                Global_Data.Custom_Toast(Survey_Main.this, getResources().getString(R.string.survey_quastion_validation_message) + " " + code_value[1],"yes");

                                break;
                            }
                        }
                    }
                    else
                    {
                        //SyncSurveyDataData();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        //get current date time with Date()
                        Date date = new Date();
                        System.out.println(dateFormat.format(date));
                        try
                        {
                            AppLocationManager appLocationManager = new AppLocationManager(Survey_Main.this);
                            Log.d("Class LAT LOG","Class LAT LOG"+appLocationManager.getLatitude()+" "+ appLocationManager.getLongitude());
                            Log.d("Service LAT LOG","Service LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);
                            PlayService_Location PlayServiceManager = new PlayService_Location(Survey_Main.this);
                            if(PlayServiceManager.checkPlayServices(Survey_Main.this))
                            {
                                Log.d("Play LAT LOG","Play LAT LOG"+Global_Data.GLOvel_LATITUDE+" "+ Global_Data.GLOvel_LONGITUDE);
                            }
                            else
                            if(!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)  && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null))
                            {
                                Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                            }
                        }catch(Exception ex){ex.printStackTrace();}
                        for(int i=0; i< hashkey.size(); i++)
                        {
                            String code_value [] = hashkey.get(i).split(":");
                            Long randomPIN = System.currentTimeMillis();
                            String PINString = String.valueOf(randomPIN);
                            loginDataBaseAdapter.insert_Survey_Answers(user_email,dateFormat.format(date),survey_code, Global_Data.cust_str,code_value[0],hashvalue.get(i),Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE,PINString);
                        }

//                        Toast toast = Toast.makeText(getApplicationContext(),
//                                getResources().getString(R.string.Survey_Data_Save_Successfully), Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Global_Data.Custom_Toast(getApplicationContext(),
                                getResources().getString(R.string.Survey_Data_Save_Successfully),"yes");

                        Intent intentn = new Intent(getApplicationContext(), Marketing.class);
                        startActivity(intentn);
                        finish();
                    }

               // }
//                else
//                {
//                    // Internet connection is not present
//                    // Ask user to connect to Internet
//                    // showAlertDialog(AndroidDetectInternetConnectionActivity.this, "No Internet Connection",
//                    //        "You don't have internet connection.", false);
//                    Toast.makeText(getApplicationContext(),"You don't have internet connection",Toast.LENGTH_LONG).show();
//                }
            }
        });
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
//            mTitleTextView.setText(getResources().getString(R.string.MARKET_SURVEY));
//
//            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
//            SharedPreferences sp = Survey_Main.this.getSharedPreferences("SimpleLogic", 0);
//
////	       if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
////	       	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
////			}
//
//            try
//            {
//                int target  = (int) Math.round(sp.getFloat("Target",0));
//                int achieved  = (int) Math.round(sp.getFloat("Achived",0));
//                Float age_float = (sp.getFloat("Achived",0)/sp.getFloat("Target",0))*100;
//                if(String.valueOf(age_float).equalsIgnoreCase("infinity"))
//                {
//                    int age = (int) Math.round(age_float);
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
//                    }
//                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
//                }else
//                {
//                    int age = (int) Math.round(age_float);
//                    if (Global_Data.rsstr.length() > 0) {
//                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    } else {
//                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
//                    }
//                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
//                }
//
//            }catch(Exception ex){ex.printStackTrace();}
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


       // GetSurveey_Datan();
    }


    @Override
    public void onOptionSelected(int position, int itemSelected) {
        questionModels.get(position).setSeleectedAnswerPosition(itemSelected);
        switch (itemSelected){
            case 1:
                ((QuestionModel)questionModels.get(position)).setOp1Sel(true);
                break;

            case 2:
                ((QuestionModel)questionModels.get(position)).setOp2Sel(true);
                break;
            case 3:
                ((QuestionModel)questionModels.get(position)).setOp3Sel(true);
                break;
            case 4:
                ((QuestionModel)questionModels.get(position)).setOp4Sel(true);
                break;
            case 5:
                ((QuestionModel)questionModels.get(position)).setOp5Sel(true);
                break;
        }
        //questionAdapter.setQuestionModels(questionModels);
        questionAdapter =new ElementListAdapter(Survey_Main.this,questionModels);
        questionAdapter.notifyDataSetChanged();
        // mRecyclerView.setAdapter(questionAdapter);



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

        AlertDialog alertDialog = new AlertDialog.Builder(Survey_Main.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Warning));
        alertDialog.setMessage(getResources().getString(R.string.Survey_dialog_message));
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                Intent intentn = new Intent(getApplicationContext(), Marketing.class);
                startActivity(intentn);
                finish();
                //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });
        alertDialog.show();
    }







}