package com.msimplelogic.activities;

/**
 * Created by sujit on 11/20/2017.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.msimplelogic.activities.R;

import com.msimplelogic.adapter.PendingTransactionAdapter;
import com.msimplelogic.model.CollectionItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by sujit on 11/15/2017.
 */

public class PendingTransaction extends Activity {
    RecyclerView collection_recview;
    private DatePickerDialog fromDatePickerDialog, fromDatePickerDialog1;
    private SimpleDateFormat dateFormatter;
    EditText from_date, to_date;
    TextView top_first, top_middle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.collect_statement);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        collection_recview = (RecyclerView) findViewById(R.id.collection_card);
        from_date = (EditText) findViewById(R.id.from_date);
        to_date = (EditText) findViewById(R.id.to_date);
        top_first = (TextView) findViewById(R.id.sheader1);
        top_middle = (TextView) findViewById(R.id.sheader2);

//        radio_outstanding=(RadioButton)findViewById(R.id.radio_outstanding);
//        sortby_beat=(Spinner)findViewById(R.id.sortby_beat);
//        filter_submit=(Button)findViewById(R.id.filter_submit);

        dateFormatter = new SimpleDateFormat("MMMM-yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText(bundle.getString("title"));

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = this.getSharedPreferences("SimpleLogic", 0);

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
//        	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
                todaysTarget.setText("Today's Target Acheived");
            }

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        // Initializing list view with the custom adapter
        ArrayList<CollectionItem> itemList = new ArrayList<CollectionItem>();

        PendingTransactionAdapter itemArrayAdapter = new PendingTransactionAdapter(PendingTransaction.this, R.layout.list_item, itemList);
        collection_recview = (RecyclerView) findViewById(R.id.collection_card);
        collection_recview.setLayoutManager(new LinearLayoutManager(this));
        collection_recview.setItemAnimator(new DefaultItemAnimator());
//        collection_recview.addItemDecoration(new DividerItemDecoration(this,android.R.drawable.divider_horizontal_bright));
        collection_recview.setAdapter(itemArrayAdapter);

        if (bundle != null) {
            String string = bundle.getString("topfirst");
            String string1 = bundle.getString("topmiddle");
            top_first.setText(string);
            top_middle.setText(string1);
        }

//        if(bundle != null) {
//            String string = bundle.getString("topmiddle1");
//            top_middle.setText(string);
//        }


//        // Populating list items
//        for (int i = 0; i < 100; i++) {
//            itemList.add(new CollectionItem("Item " + i));
//        }

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String yr_reg = Integer.toString(year);
                String mnth_reg = Integer.toString(monthOfYear + 1);
                String date_reg = Integer.toString(dayOfMonth);

                from_date.setText(date_reg + "-" + (dateFormatter.format(newDate.getTime())));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.getWindow().getAttributes();
                fromDatePickerDialog.show();
            }
        });

        fromDatePickerDialog1 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String yr_reg = Integer.toString(year);
                String mnth_reg = Integer.toString(monthOfYear + 1);
                String date_reg = Integer.toString(dayOfMonth);

                to_date.setText(date_reg + "-" + (dateFormatter.format(newDate.getTime())));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog1.getWindow().getAttributes();
                fromDatePickerDialog1.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
