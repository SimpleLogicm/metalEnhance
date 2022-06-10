package com.msimplelogic.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.msimplelogic.activities.R;

public class MyNewIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public MyNewIntentService() {
        super("MyNewIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

//        NotificationCompat.Builder b = new NotificationCompat.Builder(this);
//        //b.setOngoing(true)
//       // b.setAutoCancel(true)
//                b.setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                //.setSmallIcon(R.drawable.anchor_logo)
//                .setColor(getResources().getColor(R.color.mdtp_red))
//                .setTicker("Metal")
//                .setContentTitle("Task")
//                .setContentText("Task Pending")
//                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
//                // .setContentIntent(contentIntent)
//                .setContentInfo("Info");
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            b.setSmallIcon(R.drawable.notifi);
//            b.setColor(getResources().getColor(R.color.darkorrange));
//        } else {
//            b.setSmallIcon(R.drawable.notifi);
//            b.setColor(getResources().getColor(R.color.darkorrange));
//        }
//
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            /* Create or update. */
//            NotificationChannel channel = new NotificationChannel("my_channel_01",
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(1, b.build());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notifi)
                .setColor(getResources().getColor(R.color.darkorrange))
                .setContentTitle("gfh")
                .setContentText("gfhgfh");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }
}
