package com.msimplelogic.activities;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.widget.ImageView;

import com.msimplelogic.activities.kotlinFiles.Video_Main_List;

import java.util.Arrays;
import java.util.Locale;

public class SplashScreenActivity extends Activity {
    ImageView splash_logo;
    Bitmap blob_data_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getActionBar().hide();
        setContentView(R.layout.activity_splash_screen);

        splash_logo = (ImageView) findViewById(R.id.splash_logo);

        SharedPreferences spf1 = this.getSharedPreferences("SimpleLogic", 0);
        String logostr = spf1.getString("splash_data", "");

        String Language = spf1.getString("Language", "");
        if (Language.equalsIgnoreCase("hi")) {
            Locale myLocale = new Locale("hi");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

        } else if (Language.equalsIgnoreCase("en")) {
            Locale myLocale = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }

        if (logostr.length() > 0) {
            byte[] decodedString = Base64.decode(logostr, Base64.DEFAULT);
            blob_data_logo = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            splash_logo.setImageBitmap(blob_data_logo);
        }

//        if (Build.VERSION.SDK_INT >= 25) {
//            createShorcut();
//        }else{
//            removeShorcuts();
//        }else

        Handler h = new Handler();
        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("splash", "splash");
                startActivity(i);
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash_screen, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
        System.exit(0);
    }

    @TargetApi(25)
    private void createShorcut() {
        ShortcutManager sM = getSystemService(ShortcutManager.class);

        Intent intent1 = new Intent(getApplicationContext(), Video_Main_List.class);
        intent1.setAction(Intent.ACTION_VIEW);

        ShortcutInfo shortcut1 = new ShortcutInfo.Builder(this, "shortcut1")
                .setIntent(intent1)
                .setShortLabel(getString(R.string.action_settings))
                .setLongLabel("Shortcut 1")
                .setShortLabel("This is the shortcut 1")
                .setDisabledMessage("Login to open this")
                .setIcon(Icon.createWithResource(this, R.drawable.marketing))
                .build();

        sM.setDynamicShortcuts(Arrays.asList(shortcut1));
    }

    @TargetApi(25)
    private void removeShorcuts() {
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        //shortcutManager.disableShortcuts(Arrays.asList("shortcut1"));
        shortcutManager.removeAllDynamicShortcuts();
    }

}
