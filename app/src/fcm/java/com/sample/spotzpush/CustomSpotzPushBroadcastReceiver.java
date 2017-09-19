package com.sample.spotzpush;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.localz.spotzpush.sdk.receiver.AbstractSpotzPushBroadcastReceiver;

/**
 * Created by aleksey on 31/08/2017.
 */

public class CustomSpotzPushBroadcastReceiver extends AbstractSpotzPushBroadcastReceiver {

    private static final String TAG = CustomSpotzPushBroadcastReceiver.class.getSimpleName();

    @Override
    protected void handleNotification(Context context, int notificationId, String title, String message, int soundType, String imageUrl) {
        Log.i(TAG, "handleNotification");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.NOTIFICATION_CHANNEL_ID)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        builder.setSmallIcon(com.localz.spotzpush.sdk.R.drawable.localz_logo);

        //message is a mandatory field, but title is optional for spotz-push
        if (title == null) {
            builder.setContentTitle(message);
        } else {
            builder.setContentTitle(title).setContentText(message);
        }

        if (soundType == 0) {
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        } else if (soundType > 0) {
            builder.setSound(RingtoneManager.getDefaultUri(soundType));
        }

        notificationManager.notify(notificationId, builder.build());
    }
}
