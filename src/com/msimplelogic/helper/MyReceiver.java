package com.msimplelogic.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.msimplelogic.services.MyNewIntentService;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, MyNewIntentService.class);
        context.startService(intent1);
    }


}
