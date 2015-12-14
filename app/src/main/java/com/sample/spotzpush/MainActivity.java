package com.sample.spotzpush;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.localz.spotzpush.sdk.model.response.BaseJsonResponse;
import com.localz.spotzpush.sdk.model.response.DeviceJsonResponse;
import com.localz.spotzpush.sdk.service.SpotzPushService;
import com.localz.spotzpush.sdk.task.BaseDeviceRegisterOrUpdateTask;
import com.localz.spotzpush.sdk.util.Common;

/**
 * Sample activity which includes the initialisation methods required to start using Spotz Push
 */
public class MainActivity extends Activity {

    public static final int  PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 9010;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkPlayServices()) {
            //Initialise SpotzPushService with three keys: Google Project number for the app,
            //Spotz Push project ID, and the Spotz Push Android client key. They can all be directly
            //provided as a string.
            SpotzPushService.init(
                    this,
                    BuildConfig.PLAY_PROJECT_ID,
                    BuildConfig.SPOTZ_PUSH_PROJECT_ID,
                    BuildConfig.SPOTZ_PUSH_PROJECT_KEY,
                    //Optional callback to process tasks after registration is complete, can be null.
                    new BaseDeviceRegisterOrUpdateTask.Callback() {
                        @Override
                        public void onCompleted(DeviceJsonResponse deviceJsonResponse) {
                            ((TextView) MainActivity.this.findViewById(R.id.deviceId)).setText(deviceJsonResponse.deviceId);
                        }

                        @Override
                        public void onError(BaseJsonResponse e) {
                            ((TextView) MainActivity.this.findViewById(R.id.deviceId)).setText(e.message);
                        }
                    }
            );

            //If location push functions are to be utilised, then need to ask the user's permission to ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
                }
            }
        }
        else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences p = this.getSharedPreferences(Common.PUSH_PREFS, 0);
        String deviceId = p.getString(Common.PUSH_PREFS_DEVICE_ID, "");
        ((TextView) this.findViewById(R.id.deviceId)).setText(deviceId);
    }

    /**
     * Helper method to ensure that Google Play services are available and updated on the device.
     * This is a prerequisite to be able to use push notifications.
     */
    private boolean checkPlayServices() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (result != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
                GooglePlayServicesUtil.getErrorDialog(result, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else {
                Log.i(TAG, "This device is not supported.");
            }

            return false;
        }

        return true;
    }
}
