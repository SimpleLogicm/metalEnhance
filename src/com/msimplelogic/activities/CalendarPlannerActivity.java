package com.msimplelogic.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.msimplelogic.activities.kotlinFiles.ActivityTask;
import com.msimplelogic.webservice.ConnectionDetector;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class CalendarPlannerActivity extends BaseActivity {
        ConnectionDetector cd;
        Boolean isInternetPresent = false;
        ImageView taskBtn,leaveManagementBtn,tourProgrammeBtn,notesBtn,meetingBtn,timeSheetBtn;
        Toolbar toolbar;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_planner);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            assert getSupportActionBar() != null;   //null check
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            cd = new ConnectionDetector(getApplicationContext());
            taskBtn=findViewById(R.id.task_btn);
            leaveManagementBtn=findViewById(R.id.leavem_btn);
            tourProgrammeBtn=findViewById(R.id.tourp_btn);
            notesBtn=findViewById(R.id.note_btn);
            meetingBtn=findViewById(R.id.meeting_btn);
            timeSheetBtn=findViewById(R.id.timesheet_btn);

            taskBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(getApplicationContext(), ActivityTask.class);
                    intent.putExtra("activity_flag","Task");
                    startActivity(intent);


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

            switch (item.getItemId()) {
                case R.id.add:
                    String targetNew="";
                    SharedPreferences sp = CalendarPlannerActivity.this.getSharedPreferences("SimpleLogic", 0);
                    try {
                        int target = (int) Math.round(sp.getFloat("Target", 0));
                        int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                        Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
                        if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                            int age = (int) Math.round(age_float);
                            if (Global_Data.rsstr.length() > 0) {
                                targetNew="T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";

                            } else {
                                targetNew="T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";

                            }

                        } else {
                            int age = (int) Math.round(age_float);
                            if (Global_Data.rsstr.length() > 0) {
                                targetNew="T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]";

                            } else {
                                targetNew="T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]";

                            }

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

            Intent i = new Intent(CalendarPlannerActivity.this,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            startActivity(i);
            finish();
        }


    }
