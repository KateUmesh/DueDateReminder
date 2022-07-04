package com.duedatereminder.firebase

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.duedatereminder.utils.Constant
import com.duedatereminder.utils.LocalSharedPreference
import com.duedatereminder.view.activities.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.HttpURLConnection
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {

    val CHANNEL_ID = "Compliance Reminder"
    val CHANNEL_DESC ="Compliance Reminder"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        LocalSharedPreference.putStringValue(Constant.firebase_token,token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        //create notification channel
        //create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        //check if push notification has notification payload or not

        //check if push notification has notification payload or not
        if (message.getNotification() != null) {

            //get the title and body
            val title: String = message.notification?.getTitle()!!
            val body: String = message.notification?.getBody()!!
            Log.d("TAG", "Notification Title: $title - Body: $body")

            //show notification
            if(message.notification?.imageUrl!=null) {
                showImageNotification(
                    title,
                    body,
                    getBitmapFromUrl(message.notification?.imageUrl.toString())!!
                )
            }else{
                showTextNotification(title, body)
            }

        }
    }

    public fun getToken():String{
        return LocalSharedPreference.getStringValue(Constant.firebase_token)!!
    }

    private fun showTextNotification(title: String, body: String) {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, HomeActivity::class.java)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, "channel_id")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    applicationContext,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        notificationManager.notify(1, builder.build())
    }

    private fun showImageNotification(title: String, body: String,bitmap: Bitmap) {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, HomeActivity::class.java)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, "channel_id")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    applicationContext,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        notificationManager.notify(1, builder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            "channel_id", CHANNEL_ID,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = CHANNEL_DESC
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun getBitmapFromUrl(imageUrl: String?): Bitmap? {
        val bitmap: Bitmap
        return try {
            val url = URL(imageUrl)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            bitmap = BitmapFactory.decodeStream(input)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}