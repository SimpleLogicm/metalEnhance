package com.msimplelogic.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.msimplelogic.helper.BJobSchedular_helper;

public class BJobSchedularServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        BJobSchedular_helper.scheduleJob(context);
    }
}
