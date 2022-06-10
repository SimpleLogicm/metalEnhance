package com.msimplelogic.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.msimplelogic.helper.BJobSchedular_helper;

public class BJobService extends JobService {
    private static final String TAG = "SyncService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Intent service = new Intent(getApplicationContext(), BJobScheduleService.class);
        try {
            getApplicationContext().startService(service);
        }
        catch ( Exception IllegalStateException) {
            // intent.putExtra(NEED_FOREGROUND_KEY, true)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getApplicationContext().startForegroundService(service);
            }
            else {
                getApplicationContext().startService(service);
            }
        }

        try
        {
            BJobSchedular_helper.scheduleJob(getApplicationContext()); // reschedule the job
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    private void doMyWork() {
        Log.d("JOB","JOB");
    }
}