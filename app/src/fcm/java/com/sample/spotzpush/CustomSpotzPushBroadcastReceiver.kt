package com.sample.spotzpush

import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log

import com.localz.spotzpush.sdk.receiver.AbstractSpotzPushBroadcastReceiver

/**
 * Created by aleksey on 31/08/2017.
 */

class CustomSpotzPushBroadcastReceiver : AbstractSpotzPushBroadcastReceiver() {

    override fun handleNotification(context: Context, notificationId: Int, title: String?, message: String?, soundType: Int, imageUrl: String?) {
        Log.i(TAG, "handleNotification")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, MainActivity.NOTIFICATION_CHANNEL_ID)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))

        builder.setSmallIcon(R.drawable.localz_logo)

        //message is a mandatory field, but title is optional for spotz-push
        if (title == null) {
            builder.setContentTitle(message)
        } else {
            builder.setContentTitle(title).setContentText(message)
        }

        if (soundType == 0) {
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        } else if (soundType > 0) {
            builder.setSound(RingtoneManager.getDefaultUri(soundType))
        }

        notificationManager.notify(notificationId, builder.build())
    }

    companion object {

        private val TAG = CustomSpotzPushBroadcastReceiver::class.java.simpleName
    }
}
