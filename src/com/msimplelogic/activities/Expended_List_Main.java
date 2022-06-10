package com.msimplelogic.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.msimplelogic.activities.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.List;

public class Expended_List_Main extends BaseActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    LinkedHashMap<String, List<String>> expandableListDetail;
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    Spinner spndropdown_month;
    private ArrayList<String> Months_list = new ArrayList<String>();
    ArrayAdapter<String> MonthsAdapter;
    TextView mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_main);
        Global_Data.CALENDER_READONLY_Address = "";
        Global_Data.CALENDER_READONLY_Date = "";
        spndropdown_month = (Spinner) findViewById(R.id.spndropdown_month);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        Global_Data.Globel_Month = 0;


        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                String st[] =  expandableListTitle.get(groupPosition).split(" ");
                String finaldate = st[0]+"-"+st[1]+"-"+year;
                List<Local_Data> contactsDetail = dbvoc.getCalender_EventValue_byFDATE(finaldate,"NO");

                if(contactsDetail.size() <=0)
                {
                  //  Toast.makeText(Expended_List_Main.this, "No event found.", Toast.LENGTH_SHORT).show();

                   // Toast toast = Toast.makeText(Expended_List_Main.this, "No event found.", Toast.LENGTH_SHORT);
                   // toast.setGravity(Gravity.CENTER, 0, 0);
                  //  toast.show();

                }
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();


            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getApplicationContext(),
//                        expandableListTitle.get(groupPosition)
//                                + " -> "
//                                + expandableListDetail.get(
//                                expandableListTitle.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT
//                ).show();

                Global_Data.CALENDER_READONLY_Address = expandableListDetail.get(
                        expandableListTitle.get(groupPosition)).get(
                        childPosition).trim();
                Global_Data.CALENDER_READONLY_Date = expandableListTitle.get(groupPosition);
                Intent intent = new Intent(getApplicationContext(), Calender_ReadonlyView.class);
                startActivity(intent);
               // finish();
                return false;
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        Formatter fmt = new Formatter();
        final String mm = fmt.format("%tB", calendar).toString();
        Months_list.clear();
        Formatter fmt1 = new Formatter();
        Calendar calendar1 = Calendar.getInstance();
        int year1 = calendar1.get(Calendar.YEAR);
        final int month = calendar1.get(Calendar.MONTH);
        final String mm1 = fmt1.format("%tB", calendar1).toString();
        Months_list.add(mm1 + " " + year1);

        Formatter fmt2 = new Formatter();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, 1);
        final int month2 = calendar2.get(Calendar.MONTH);
        final String mm2 = fmt2.format("%tB", calendar2).toString();
        Months_list.add(mm2 + " " + year1);


        Formatter fmt3 = new Formatter();
        Calendar calendar3 = Calendar.getInstance();
        calendar3.add(Calendar.MONTH, 2);
        final int month3 = calendar3.get(Calendar.MONTH);
        final String mm3 = fmt3.format("%tB", calendar3).toString();
        Months_list.add(mm3 + " " + year1);

        Global_Data.Globel_Month = month;

        MonthsAdapter = new ArrayAdapter<String>(Expended_List_Main.this, android.R.layout.simple_spinner_item, Months_list);
        MonthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spndropdown_month.setAdapter(MonthsAdapter);

//        expandableListDetail = ExpandableListDataPump.getData(Expended_List_Main.this);
//        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
//        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
//        expandableListView.setAdapter(expandableListAdapter);
//
//        for(int j=0; j < expandableListAdapter.getGroupCount(); j++) {
//            expandableListView .expandGroup(j);
//        }

        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);
            Intent i = getIntent();
            String name = i.getStringExtra("retialer");
            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);

            // int month = calendar.get(Calendar.MONTH);
            // int day = calendar.get(Calendar.DAY_OF_MONTH);

            //Calendar cal = Calendar.getInstance();
            //int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
            // Formatter fmt = new Formatter();
            // fmt.format("%tB %tb %tm", calendar, calendar, calendar);

            mTitleTextView.setText(getResources().getString(R.string.CALENDAR) + " - " + mm + " " + year);
            mTitleTextView.setTextSize(15);

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            todaysTarget.setVisibility(View.INVISIBLE);
            SharedPreferences sp = Expended_List_Main.this.getSharedPreferences("SimpleLogic", 0);

            ImageView H_LOGO = (ImageView) mCustomView.findViewById(R.id.Header_logo);
            H_LOGO.setImageResource(R.drawable.cal);
            H_LOGO.setVisibility(View.VISIBLE);


            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        spndropdown_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                String s = (String) parent.getSelectedItem();

                Log.d("PPOSI", "PPOSI" + parent.getSelectedItemPosition());
                if (parent.getSelectedItemPosition() == 0) {
                    Global_Data.Globel_Month = month;
                    mTitleTextView.setText(getResources().getString(R.string.CALENDAR) + " - " + mm1 + " " + year);
                } else if (parent.getSelectedItemPosition() == 1) {
                    Global_Data.Globel_Month = month2;
                    mTitleTextView.setText(getResources().getString(R.string.CALENDAR) + " - " + mm2 + " " + year);
                } else if (parent.getSelectedItemPosition() == 2) {
                    Global_Data.Globel_Month = month3;
                    mTitleTextView.setText(getResources().getString(R.string.CALENDAR) + " - " + mm3 + " " + year);
                }

                expandableListDetail = ExpandableListDataPump.getData(Expended_List_Main.this);
                expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                expandableListAdapter = new CustomExpandableListAdapter(Expended_List_Main.this, expandableListTitle, expandableListDetail);
                expandableListView.setAdapter(expandableListAdapter);
//                expandableListAdapter.notifyAll();

                for (int i = 0; i < expandableListAdapter.getGroupCount(); i++) {
                    expandableListView.expandGroup(i);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

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
        // TODO Auto-generated method stub
        //super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        this.finish();
    }
}
