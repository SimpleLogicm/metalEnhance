package com.msimplelogic.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.msimplelogic.services.JobSchedular_Ohelper;

public class OrderCheckReceiver extends BroadcastReceiver {
    public OrderCheckReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        JobSchedular_Ohelper.scheduleJob(context);

    }
}
