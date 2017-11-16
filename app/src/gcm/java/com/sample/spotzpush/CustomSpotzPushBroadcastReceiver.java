package com.sample.spotzpush;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.localz.spotzpush.sdk.receiver.AbstractSpotzPushBroadcastReceiver;

import java.util.Set;

/**
 * Created by aleksey on 16/11/2017.
 */

public class CustomSpotzPushBroadcastReceiver extends AbstractSpotzPushBroadcastReceiver {

    private static final String TAG = CustomSpotzPushBroadcastReceiver.class.getSimpleName();

    @Override
    protected boolean handleNotification(@Nullable Bundle extras) {
        if (extras != null) {
            Set<String> extrasKeys = extras.keySet();

            for (String key : extrasKeys) {
                Log.d(TAG, "Key: " + key + " value: " + extras.get(key));
            }
        }
        return true;
    }
}
