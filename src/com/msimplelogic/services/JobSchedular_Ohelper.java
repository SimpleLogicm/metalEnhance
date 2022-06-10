package com.msimplelogic.services;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;



public class JobSchedular_Ohelper {

    // schedule the start of the service every 10 - 30 seconds
    public static void scheduleJob(Context context) {
//        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
//        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
//        builder.setMinimumLatency(1 * 300000); // wait at least
//        builder.setOverrideDeadline(3 * 700000); // maximum delay

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ComponentName serviceComponent = new ComponentName(context, TestJobServiceOrder.class);
            JobInfo.Builder builder = new JobInfo.Builder(1, serviceComponent);
            builder.setMinimumLatency(960000*2);
            builder.setOverrideDeadline(3 * 960000);
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());
        } else {
            ComponentName serviceComponent = new ComponentName(context, TestJobServiceOrder.class);
            JobInfo.Builder builder = new JobInfo.Builder(1, serviceComponent);
            builder.setPeriodic(960000*2);
            // builder.setOverrideDeadline(3 * 700000);
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());


        }
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not

    }

}
