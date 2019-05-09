package com.sample.spotzpush

import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.localz.spotzpush.sdk.fcm.service.FirebaseService

class CustomFirebaseService : FirebaseService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage != null) {
            Log.d("onMessageReceived", "RemoteMessage received: ${remoteMessage.data}")
        }
    }

    override fun onNonLocalzMessageReceived(remoteMessage: RemoteMessage?) {
        if (remoteMessage != null) {
            Log.d("onNonLocalzMessage", "RemoteMessage received: ${remoteMessage.data}")
        }
    }
}