package com.msimplelogic.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.msimplelogic.activities.R;

import com.msimplelogic.adapter.RecyclerViewAdapter;
import com.msimplelogic.model.MenuItems;
import java.util.ArrayList;
import java.util.List;
import cpm.simplelogic.helper.DividerItemDecoration;

/**
 * Created by sujit on 12/27/2017.
 */

public class MenuOptimization extends Activity {
    private LinearLayoutManager lLayout;
    ImageView minus_toggle;
    Boolean isInternetPresent = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_optimization);
        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText(getResources().getString(R.string.Menu));

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            SharedPreferences sp = MenuOptimization.this.getSharedPreferences("SimpleLogic", 0);

//        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//        	todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//		}
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

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        List<MenuItems> rowListItem = getAllItemList();
        lLayout = new LinearLayoutManager(MenuOptimization.this);
        minus_toggle = (ImageView) findViewById(R.id.minus_toggle);

        minus_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(MenuOptimization.this,Home.class));
            }
        });

        RecyclerView rView = (RecyclerView) findViewById(R.id.menu_list);
        rView.setHasFixedSize(true);
        rView.setItemViewCacheSize(20);
        rView.setDrawingCacheEnabled(true);
        rView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rView.setLayoutManager(lLayout);
        rView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(MenuOptimization.this, rowListItem);
        rView.setAdapter(rcAdapter);
    }

    private List<MenuItems> getAllItemList() {

        List<MenuItems> allItems = new ArrayList<MenuItems>();
        allItems.add(new MenuItems(getResources().getString(R.string.Attendance), R.drawable.attendance_menu));
        allItems.add(new MenuItems(getResources().getString(R.string.Nearest_Customer), R.drawable.customer_menu));
        allItems.add(new MenuItems(getResources().getString(R.string.Location_On_Map), R.drawable.locationmap_menu));
        allItems.add(new MenuItems(getResources().getString(R.string.Beat_On_Map), R.drawable.beat_menu));
        //allItems.add(new MenuItems("Attendance", R.drawable.attendance_menu));
        allItems.add(new MenuItems(getResources().getString(R.string.Schedule_List), R.drawable.list_menu));
        allItems.add(new MenuItems(getResources().getString(R.string.Outstanding), R.drawable.outstanding_menu));
        allItems.add(new MenuItems(getResources().getString(R.string.Order_List), R.drawable.order_list));
        allItems.add(new MenuItems(getResources().getString(R.string.Return_Order_List), R.drawable.return_order));
        allItems.add(new MenuItems(getResources().getString(R.string.Cash_Deposit), R.drawable.cash_depositn));
        return allItems;
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
        //dialog.d

        Global_Data.G_BEAT_IDC = "";
        Global_Data.G_BEAT_VALUEC = "";
        Global_Data.G_BEAT_service_flag = "";
        Global_Data.G_RadioG_valueC = "";
        Global_Data.G_CBUSINESS_TYPE = "";

        // Intent i = new Intent(Customer_info_main.this, Sales_Dash.class);
        //    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        ///    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //    startActivity(i);
        finish();

    }

}
