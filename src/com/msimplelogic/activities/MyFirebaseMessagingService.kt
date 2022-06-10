package com.msimplelogic.activities

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.msimplelogic.helper.NotificationUtils
import com.msimplelogic.services.NotificationHelper
import org.apache.http.util.TextUtils

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG: String = MyFirebaseMessagingService::class.java.getSimpleName()
    private var notificationUtils: NotificationUtils? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e(TAG, "From: " + remoteMessage.from)

        if (remoteMessage!!.notification != null) {
            Log.d("notification in", "notification in");
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body

            // Check if message contains a data payload.
            if (remoteMessage.data.size > 0) {
                Log.e(TAG, "Data Payload: " + remoteMessage.data.toString())
                try { //JSONObject json = new JSONObject(remoteMessage.getData().toString());
                   // Log.d("description", "" + remoteMessage.data["description"])

                    if(title.equals("Activity Planner",ignoreCase = false))
                    {
                        Log.d("reminder_date", "" + remoteMessage.data["reminder_date"])
                        Log.d("latitude", "" + remoteMessage.data["location_latitude"])
                        Log.d("longtitude", "" + remoteMessage.data["location_longtitude"])
                        Log.d("description", "" + remoteMessage.data["description"])

                        NotificationHelper.displayNotification(applicationContext, title!!,remoteMessage.data["description"],remoteMessage.data["reminder_date"],remoteMessage.data["location_latitude"],remoteMessage.data["location_longtitude"],"FCM")
                    }
                    else
                    if(title.equals("Promotional Meeting",ignoreCase = false))
                    {
                        Log.d("meeting_date", "" + remoteMessage.data["meeting_date"])
                        Log.d("latitude", "" + remoteMessage.data["location_latitude"])
                        Log.d("longtitude", "" + remoteMessage.data["location_longtitude"])
                        Log.d("description", "" + remoteMessage.data["description"])

                        NotificationHelper.displayNotification(applicationContext, title!!, remoteMessage.data["description"],remoteMessage.data["meeting_date"],remoteMessage.data["location_latitude"],remoteMessage.data["location_longtitude"],"FCM")
                    }
                    else
                    {
                        Log.d("schedule_date", "" + remoteMessage.data["schedule_date"])
                        Log.d("latitude", "" + remoteMessage.data["location_latitude"])
                        Log.d("longtitude", "" + remoteMessage.data["location_longtitude"])
                        Log.d("body", "" + remoteMessage.notification!!.body)

                        NotificationHelper.displayNotification(applicationContext, title!!, remoteMessage.notification!!.body,remoteMessage.data["schedule_date"],remoteMessage.data["location_latitude"],remoteMessage.data["location_longtitude"],"FCM")
                    }



//                    handleDataMessage(remoteMessage.data["post_user_name"]!!, remoteMessage.data["post_content"]!!, remoteMessage.data["post_user_photo"]!!, remoteMessage.data["post_user_designation"]!!, remoteMessage.data["post_attachment_content_type"]!!, remoteMessage.data["post_attachment_url"]!!)
                } catch (e: Exception) {
                    Log.e(TAG, "Exception: " + e.message)
                }
            }


        }
    }

    private fun handleNotification(message: String) {
        if (!NotificationUtils.isAppIsInBackground(applicationContext)) { // app is in foreground, broadcast the push message
            val pushNotification = Intent(Config.PUSH_NOTIFICATION)
            pushNotification.putExtra("message", message)
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
            // play notification sound
            val notificationUtils = NotificationUtils(applicationContext)
            notificationUtils.playNotificationSound()
        } else { // If the app is in background, firebase itself handles the notification
        }
    }

    private fun handleDataMessage(post_user_name1: String, post_content1: String, post_user_photo1: String, post_user_designation1: String, post_attachment_content_type1: String, post_attachment_url1: String) {
        try { // JSONObject data = json.getJSONObject("data");
            if (!NotificationUtils.isAppIsInBackground(applicationContext)) { // app is in foreground, broadcast the push message
                val pushNotification = Intent(Config.PUSH_NOTIFICATION)
                pushNotification.putExtra("post_user_name", post_user_name1)
                pushNotification.putExtra("post_content", post_content1)
                pushNotification.putExtra("post_user_photo", post_user_photo1)
                pushNotification.putExtra("post_user_designation", post_user_designation1)
                pushNotification.putExtra("post_attachment_content_type", post_attachment_content_type1)
                pushNotification.putExtra("post_attachment_url", post_attachment_url1)
                //                pushNotification.putExtra("is_background", isBackground);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
                // play notification sound
                val notificationUtils = NotificationUtils(applicationContext)
                notificationUtils.playNotificationSound()
            } else { // app is in background, show the notification in notification tray
                val resultIntent = Intent(applicationContext, MainActivity::class.java)
                resultIntent.putExtra("post_user_name", post_user_name1)
                resultIntent.putExtra("post_content", post_content1)
                resultIntent.putExtra("post_user_photo", post_user_photo1)
                resultIntent.putExtra("post_user_designation", post_user_designation1)
                resultIntent.putExtra("post_attachment_content_type", post_attachment_content_type1)
                resultIntent.putExtra("post_attachment_url", post_attachment_url1)
                // check for image attachment
                if (TextUtils.isEmpty(post_attachment_url1)) {
                    showNotificationMessage(applicationContext, post_user_name1, post_content1, "", resultIntent)
                } else { // image is present, show notification with image
                    showNotificationMessageWithBigImage(applicationContext, post_user_name1, post_content1, "", resultIntent, post_attachment_url1)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Json Exception: " + e.message)
        }
    }

    /**
     * Showing notification with text only
     */
    private fun showNotificationMessage(context: Context, title: String, message: String, timeStamp: String, intent: Intent) {
        notificationUtils = NotificationUtils(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils!!.showNotificationMessage(title, message, timeStamp, intent)
    }

    /**
     * Showing notification with text and image
     */
    private fun showNotificationMessageWithBigImage(context: Context, title: String, message: String, timeStamp: String, intent: Intent, imageUrl: String) {
        notificationUtils = NotificationUtils(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils!!.showNotificationMessage(title, message, timeStamp, intent, imageUrl)
    }
}