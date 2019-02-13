package com.sample.spotzpush;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.localz.spotzpush.sdk.fcm.service.LocalzPushSDK;
import com.localz.spotzpush.sdk.model.response.BaseResponse;
import com.localz.spotzpush.sdk.model.response.DeviceResponse;
import com.localz.spotzpush.sdk.task.BaseDeviceRegisterOrUpdateTask;
import com.localz.spotzpush.sdk.util.Common;

import org.jetbrains.annotations.NotNull;

/**
 * Sample activity which includes the initialisation methods required to start using Spotz Push
 */
public class MainActivity extends Activity {
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 9010;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String NOTIFICATION_CHANNEL_ID = "com.sample.spotzpush.fcm.notification.id";

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences p = this.getSharedPreferences(Common.PUSH_PREFS, 0);
        String deviceId = p.getString(Common.PUSH_PREFS_DEVICE_ID, "");
        ((TextView) this.findViewById(R.id.deviceId)).setText(deviceId);
        if (checkPlayServices()) {
            //Initialise LocalzPushSDK with three keys: Google Project number for the app,
            //Spotz Push project ID, and the Spotz Push Android client key. They can all be directly
            //provided as a string.
            LocalzPushSDK.init(
                    this,
                    BuildConfig.SPOTZ_PUSH_PROJECT_ID,
                    BuildConfig.SPOTZ_PUSH_PROJECT_KEY,
                    //Optional callback to process tasks after registration is complete, can be null.
                    new BaseDeviceRegisterOrUpdateTask.Callback() {
                        @Override
                        public void onCompleted(@NotNull DeviceResponse deviceJsonResponse) {
                            ((TextView) MainActivity.this.findViewById(R.id.deviceId)).setText(deviceJsonResponse.deviceId);
                        }

                        @Override
                        public void onError(@NotNull BaseResponse e) {
                            ((TextView) MainActivity.this.findViewById(R.id.deviceId)).setText(e.message);
                        }
                    }
            );

            //If location push functions are to be utilised, then need to ask the user's permission to ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
                }
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String permissions[], @NotNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Helper method to ensure that Google Play services are available and updated on the device.
     * This is a prerequisite to be able to use push notifications.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (result != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(result)) {
                googleApiAvailability.getErrorDialog(this, result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }

            return false;
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
        if (notificationChannel == null) {
            // The user-visible name of the channel.
            CharSequence channelName = "Spotz Push Notifications";
            // The user-visible description of the channel.
            String channelDescription = "Spotz Push Sample App Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            // Configure the notification channel.
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
