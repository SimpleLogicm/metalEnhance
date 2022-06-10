package com.msimplelogic.Receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.NameNotFoundException
import android.view.Gravity
import android.widget.Toast
import com.msimplelogic.activities.Config
import com.msimplelogic.activities.Global_Data
import com.msimplelogic.activities.R
import com.msimplelogic.activities.kotlinFiles.AllOrders_Sync
import com.msimplelogic.webservice.ConnectionDetector


class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("CONFIRM", ignoreCase = true)) {

            var isInternetPresent = false
            var cd: ConnectionDetector? = null
            cd = ConnectionDetector(context)
            isInternetPresent = cd!!.isConnectingToInternet
            if (isInternetPresent) {

                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(Config.NOTIFICATION_ID_NEW)


                try {


                    val closeIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
                    context.sendBroadcast(closeIntent)


                } catch (e: NameNotFoundException) {
                    e.printStackTrace()
                }
                Global_Data.order_pushflag = "yes"



                val intent = Intent(context, AllOrders_Sync::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent)
               // finish()
            } else {

                Global_Data.Custom_Toast(context, context.resources.getString(R.string.internet_connection_error),"yes")
            }

        } else if (intent.action.equals("CANCEL", ignoreCase = true)) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(11111)
        }
    }
}