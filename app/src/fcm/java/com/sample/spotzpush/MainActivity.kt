package com.sample.spotzpush

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.TextView

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.localz.sdk.core.util.LocalzEnvironment
import com.localz.spotzpush.sdk.fcm.service.LocalzPushSDK
import com.localz.spotzpush.sdk.model.response.BaseResponse
import com.localz.spotzpush.sdk.model.response.DeviceResponse
import com.localz.spotzpush.sdk.task.BaseDeviceRegisterOrUpdateTask
import com.localz.spotzpush.sdk.util.Common

/**
 * Sample activity which includes the initialisation methods required to start using Spotz Push
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(this)
        }
    }

    override fun onResume() {
        super.onResume()

        val p = getSharedPreferences(Common.PUSH_PREFS, 0)
        val deviceId = p.getString(Common.PUSH_PREFS_DEVICE_ID, "")
        (findViewById<View>(R.id.deviceId) as TextView).text = deviceId

        if (checkPlayServices()) {
            //Initialise LocalzPushSDK with three keys: Google Project number for the app,
            //Spotz Push project ID, and the Spotz Push Android client key. They can all be directly
            //provided as a string.
            LocalzPushSDK.init(
                    this,
                    BuildConfig.SPOTZ_PUSH_PROJECT_ID,
                    BuildConfig.SPOTZ_PUSH_PROJECT_KEY,
                    //Optional callback to process tasks after registration is complete, can be null.
                    object : BaseDeviceRegisterOrUpdateTask.Callback() {
                        override fun onCompleted(deviceResponse: DeviceResponse) {
                            Log.d("MainActivity", deviceResponse.deviceId)
                            (this@MainActivity.findViewById<View>(R.id.deviceId) as TextView).text = deviceResponse.deviceId
                        }

                        override fun onError(e: BaseResponse) {
                            (this@MainActivity.findViewById<View>(R.id.deviceId) as TextView).text = e.message
                        }
                    }
            )

            //If location push functions are to be utilised, then need to ask the user's permission to ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
                }
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    /**
     * Helper method to ensure that Google Play services are available and updated on the device.
     * This is a prerequisite to be able to use push notifications.
     */
    private fun checkPlayServices(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()

        val result = googleApiAvailability.isGooglePlayServicesAvailable(this)

        if (result != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(result)) {
                googleApiAvailability.getErrorDialog(this, result, PLAY_SERVICES_RESOLUTION_REQUEST).show()
            } else {
                Log.i(TAG, "This device is not supported.")
            }

            return false
        }

        return true
    }

    companion object {
        const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 9010
        const val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        const val NOTIFICATION_CHANNEL_ID = "com.sample.spotzpush.fcm.notification.id"

        private val TAG = MainActivity::class.java.simpleName

        @TargetApi(Build.VERSION_CODES.O)
        private fun createNotificationChannel(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var notificationChannel: NotificationChannel? = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
            if (notificationChannel == null) {
                // The user-visible name of the channel.
                val channelName = "Spotz Push Notifications"
                // The user-visible description of the channel.
                val channelDescription = "Spotz Push Sample App Notification Channel"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
                // Configure the notification channel.
                notificationChannel.description = channelDescription
                notificationChannel.enableLights(true)
                // Sets the notification light color for notifications posted to this
                // channel, if the device supports this feature.
                notificationChannel.lightColor = Color.RED
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
    }
}
