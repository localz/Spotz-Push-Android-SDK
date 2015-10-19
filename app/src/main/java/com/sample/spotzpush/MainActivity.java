package com.sample.spotzpush;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.localz.spotzpush.sdk.model.response.BaseJsonResponse;
import com.localz.spotzpush.sdk.model.response.DeviceJsonResponse;
import com.localz.spotzpush.sdk.service.SpotzPushService;
import com.localz.spotzpush.sdk.task.DeviceRegisterOrUpdateTask;

/**
 * Sample activity which includes the initialisation methods required to start using Spotz Push
 */
public class MainActivity extends Activity {
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
                    new DeviceRegisterOrUpdateTask.Callback() {
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
        }
        else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
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
