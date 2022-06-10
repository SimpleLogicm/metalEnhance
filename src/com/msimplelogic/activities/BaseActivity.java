package com.msimplelogic.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.msimplelogic.activities.R;
import com.msimplelogic.helper.ThemeUtil;

import static com.msimplelogic.helper.ThemeUtil.THEME_BLUE;

public class BaseActivity extends AppCompatActivity {

//    public static final long DISCONNECT_TIMEOUT = 300000; // 5 min = 5 * 60 * 1000 ms
	
	//public static final long DISCONNECT_TIMEOUT = 6000; //  == > 5 sec
	
	//public static final long DISCONNECT_TIMEOUT = 72000; //  == > 1 min
	
	public static final long DISCONNECT_TIMEOUT = 1080000; //  == > 15 mins
    public static int mTheme = THEME_BLUE;
    public static boolean mIsNightMode = false;
    SharedPreferences sp;

  
    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        	
        }
    };



    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
        	
        //	Toast.makeText(getApplicationContext(), "Session timeout", Toast.LENGTH_LONG).show();

            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Session_timeout),"Yes");

//            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Session_timeout), Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();

        	finish();
			Intent i=new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(i);
        }
    };

    public void resetDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
        resetDisconnectTimer();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            sp = getSharedPreferences("SimpleLogic", 0);
            int current_theme = sp.getInt("CurrentTheme",0);
            setTheme(ThemeUtil.getThemeId(current_theme));

        }catch (Exception ex)
        {
            setTheme(ThemeUtil.getThemeId(mTheme));
            ex.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }
}