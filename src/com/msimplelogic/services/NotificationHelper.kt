package com.msimplelogic.services

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.msimplelogic.Receiver.NotificationActionReceiver
import com.msimplelogic.activities.*
import com.msimplelogic.activities.Config.NOTIFICATION_ID_NEW


object NotificationHelper {

    fun displayNotification(context: Context, title: String?, body: String?, dates: String?, latitude: String?, longitude: String?, service_flag: String?) {

        Global_Data.push_activity_flag = "yes"

        val pref = context.getSharedPreferences("SimpleLogic", Context.MODE_PRIVATE)
        val edit = pref.edit()
        edit.putString("push_activity_flag", "yes")
        edit.commit()

        if (!service_flag.equals("service")) {
            if (Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(latitude) && Check_Null_Value.isNotNullNotEmptyNotWhiteSpaceOnlyByJava(longitude)) {

                val intent = Intent(context, Notification_View::class.java)
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)


                val pendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT
                )

                val mBuilder = NotificationCompat.Builder(context, LoginActivity.CHANNEL_ID)
                        .setSmallIcon(com.msimplelogic.activities.R.drawable.ic_notification)
                        .setColor(Color.BLUE)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), com.msimplelogic.activities.R.drawable.metal_pro))
                        .setContentTitle(dates)
                        .setSubText(title)
                        .setContentText(body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .addAction(R.drawable.location, context.getString(R.string.location), pendingIntent);
                // .setPriority(NotificationCompat.PRIORITY_HIGH)

                val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                mBuilder.setSound(alarmSound)
                val mNotificationMgr = NotificationManagerCompat.from(context)
                // notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
                mNotificationMgr.notify(NOTIFICATION_ID_NEW, mBuilder.build())

            }
            else {
//            val intent = Intent(context, Notification_View::class.java)
//            intent.putExtra("latitude", latitude);
//            intent.putExtra("longitude", longitude);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//
//
//            val pendingIntent = PendingIntent.getActivity(
//                    context,
//                    0,
//                    intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT
//            )

                val mBuilder = NotificationCompat.Builder(context, LoginActivity.CHANNEL_ID)
                        .setSmallIcon(com.msimplelogic.activities.R.drawable.ic_notification)
                        .setColor(Color.BLUE)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), com.msimplelogic.activities.R.drawable.metal_pro))
                        .setContentTitle(dates)
                        .setSubText(title)
                        .setContentText(body)
                        //.setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                //.setContentIntent(pendingIntent)
                //.addAction(R.drawable.location, context.getString(R.string.location), pendingIntent);
                // .setPriority(NotificationCompat.PRIORITY_HIGH)

                val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                mBuilder.setSound(alarmSound)
                val mNotificationMgr = NotificationManagerCompat.from(context)
                // notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
                mNotificationMgr.notify(NOTIFICATION_ID_NEW, mBuilder.build())
            }
        }
        else {

            val intent = Intent(context, SplashScreenActivity::class.java)
            intent.putExtra("fromNotification", "book_ride")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val intentConfirm = Intent(context, NotificationActionReceiver::class.java)
            intentConfirm.action = "CONFIRM"
            intentConfirm.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
           // intentConfirm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)


            val intentCancel = Intent(context, NotificationActionReceiver::class.java)
            intentCancel.action = "CANCEL"
            intentCancel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intentConfirm,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT
            )

//This Intent will be called when Notification will be clicked by user.
            //This Intent will be called when Notification will be clicked by user.
//            val pendingIntent = PendingIntent.getActivity(context, 0 /* Request
//  code */, intent,
//                    PendingIntent.FLAG_ONE_SHOT)

//This Intent will be called when Confirm button from notification will be
//clicked by user.
            //This Intent will be called when Confirm button from notification will be
//clicked by user.
            val pendingIntentConfirm = PendingIntent.getBroadcast(context, 0, intentConfirm, PendingIntent.FLAG_CANCEL_CURRENT)

//This Intent will be called when Cancel button from notification will be
//clicked by user.
            //This Intent will be called when Cancel button from notification will be
//clicked by user.
            val pendingIntentCancel = PendingIntent.getBroadcast(context, 1, intentCancel, PendingIntent.FLAG_CANCEL_CURRENT)

            val mBuilder = NotificationCompat.Builder(context, LoginActivity.CHANNEL_ID)
                    .setSmallIcon(com.msimplelogic.activities.R.drawable.ic_notification)
                    .setColor(Color.BLUE)
                    //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), com.msimplelogic.activities.R.drawable.metal_pro))
                    .setContentTitle(dates)
                    .setSubText(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
            //.setContentIntent(pendingIntent)
            //.addAction(R.drawable.location, context.getString(R.string.location), pendingIntent);
            // .setPriority(NotificationCompat.PRIORITY_HIGH)

            mBuilder.addAction(R.drawable.auth_success, "Sync", pendingIntentConfirm);
           // mBuilder.addAction(R.drawable.auth_error, "Cancel", pendingIntentCancel);


            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            mBuilder.setSound(alarmSound)
            val mNotificationMgr = NotificationManagerCompat.from(context)
            //notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
            mNotificationMgr.notify(NOTIFICATION_ID_NEW, mBuilder.build())
        }


    }

}