package com.sample.spotzpush;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.widget.TextView;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences p = this.getSharedPreferences(Common.PUSH_PREFS, 0);
        String deviceId = p.getString(Common.PUSH_PREFS_DEVICE_ID, "");
        ((TextView) this.findViewById(R.id.deviceId)).setText(deviceId);

        //Initialise SpotzPushService with three keys: Google Project number for the app,
        //Spotz Push project ID, and the Spotz Push Android client key. They can all be directly
        //provided as a string.
        SpotzPushService.init(
                this,
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // allow app to always run in the background
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }
}
