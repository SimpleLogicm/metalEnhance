package com.msimplelogic.helper;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.msimplelogic.services.BJobService;


public class BJobSchedular_helper {

    // schedule the start of the service every 10 - 30 seconds
    public static void scheduleJob(Context context) {
        int inter;

        SharedPreferences spf2 = context.getSharedPreferences("SimpleLogic", 0);
        String interval = spf2.getString("Interval", null);

        if (interval.equalsIgnoreCase("")){
            inter = 5;
        }else{
            inter = Integer.parseInt(interval);
        }
//        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
//        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
//        builder.setMinimumLatency(1 * 300000); // wait at least
//        builder.setOverrideDeadline(3 * 700000); // maximum delay

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ComponentName serviceComponent = new ComponentName(context, BJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(1, serviceComponent);
            builder.setMinimumLatency(1000 * 60 * inter);
            builder.setOverrideDeadline(3 * 700000);
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());
        } else {
            ComponentName serviceComponent = new ComponentName(context, BJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(1, serviceComponent);
            builder.setPeriodic(1000 * 60 * inter);
            // builder.setOverrideDeadline(3 * 700000);
            try
            {
                JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
                jobScheduler.schedule(builder.build());
            }catch (Exception ex) {
                ex.printStackTrace();
            }



        }


    }

}
