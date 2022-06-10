package com.msimplelogic.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.msimplelogic.activities.R;

import com.msimplelogic.adapter.PreviousSwipeCatalogueAdapter;

public class PreviousSwipCatelauge extends Activity {

    private PreviousSwipeCatalogueAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_fullscreen_view);

        viewPager = (ViewPager) findViewById(R.id.pager);


        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        adapter = new PreviousSwipeCatalogueAdapter(PreviousSwipCatelauge.this, Global_Data.catalogue_m);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }


}
