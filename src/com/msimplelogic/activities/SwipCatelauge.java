package com.msimplelogic.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.viewpager.widget.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import com.msimplelogic.activities.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.msimplelogic.adapter.AdapterImageSwipe;
import com.msimplelogic.adapter.swipCatalauge_Adapter;

import java.util.ArrayList;
import java.util.List;

public class SwipCatelauge extends Activity {
    public static List<String> imageUrlArr = new ArrayList<>();
    private swipCatalauge_Adapter adapter;
    private ViewPager viewPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static ViewPager mPager;

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

        adapter = new swipCatalauge_Adapter(SwipCatelauge.this,
                Global_Data.catalogue_m);

        viewPager.setAdapter(adapter);
        viewPager.beginFakeDrag();
        // displaying selected image first
        viewPager.setCurrentItem(position);

        init();
    }

    private void init() {
//        imageUrlArr.add("https://demonuts.com/Demonuts/SampleImages/W-03.JPG");
//        imageUrlArr.add("https://demonuts.com/Demonuts/SampleImages/W-13.JPG");
//        imageUrlArr.add("https://demonuts.com/Demonuts/SampleImages/W-17.JPG");
//        imageUrlArr.add("https://demonuts.com/Demonuts/SampleImages/W-08.JPG");
//        imageUrlArr.add("https://demonuts.com/Demonuts/SampleImages/W-21.JPG");

        mPager = (ViewPager) findViewById(R.id.pager_imageswipe);
        mPager.setAdapter(new AdapterImageSwipe(SwipCatelauge.this,Global_Data.imageUrlArr1));

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

       //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = Global_Data.imageUrlArr1.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 2000, 5000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
}
