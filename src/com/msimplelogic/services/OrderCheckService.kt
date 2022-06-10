package com.msimplelogic.services

import android.app.IntentService
import android.content.Intent
import com.msimplelogic.activities.DataBaseHelper
import com.msimplelogic.activities.R

class OrderCheckService : IntentService("OrderCheckService") {
    override fun onHandleIntent(intent: Intent?) {


        val dbvoc = DataBaseHelper(applicationContext)
        val contcustomer = dbvoc.getOrderAll("Secondary Sales / Retail Sales")

        if (contcustomer.size > 0) {

            NotificationHelper.displayNotification(applicationContext, getResources().getString(R.string.Order_title_pushmessage),getResources().getString(R.string.Order_body_pushmessage)+" "+contcustomer.size+" "+getResources().getString(R.string.Order_body_pushmessage2),getResources().getString(R.string.Order_content_title_pushmessage),"","","service")


        }

    }

    companion object {
        private const val NOTIFICATION_ID = 3
    }
}