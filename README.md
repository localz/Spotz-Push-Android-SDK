Spotz Push Android SDK
=================

Spotz Push is a push notification platform.

Changelog
=========

**1.0.0**

* Initial public release.

**2.0.0**

* Incorporated new SDK version to make compatible with Android 6.

**2.0.3**

* Fixed issue with re-initialising SDK when the device is yet to be issued a deviceId.

**2.0.4**

* IntentService to handle notification can now be configured via the AndroidManifest rather than extending DefaultBroadcastReceiver.

**2.1.0**

* Added support for FCM

**2.2.0**

* Added changes to support Android 8 (Oreo).
* Added custom notification handler (see *"Customise behaviour of SDK"* section below).

Note: `GcmIntentService` has been deprecated.

**2.2.1**

* Added additional push message handler with access to original push message data.

**2.2.2**

* Upgraded Push SDK and updated documentation.

**2.2.3**

* Upgraded dependencies and documentation. Spotz Push no longer supports GCM.

What does the sample app do?
============================

The app demonstrates how to import the SDK to be able to use Spotz Push.

How to run the sample app (FCM variant)
=========================

The sample app requires devices running Android 4.0.3 or newer.

  1. Clone the repository:
  
        `git clone git@github.com:localz/Spotz-Push-Android-SDK.git`

  2. Import the project:

  3. Log into the [Firebase  Console](https://console.firebase.google.com/) and create  project containing an app with the package name of `com.sample.spotzpush.fcm`.

  4. Step through the [instructions available](https://firebase.google.com/docs/android/setup) in order to auto-generate the config file and server key.

  5. Save the google-services.json file in the `app/` directory for the sample Android app.

  6. Log into the [Google API Console](https://console.developers.google.com/) and copy the `Server key` that had been auto created by Google Service.

  7. Sign in and create a Spotz Push `Organisation` and `Application` via the [Spotz Push Console](https://console.localz.io/spotz-push).

  8. Upload your FCM server key credentials via the `Push Configuration` menu for your Spotz Push Application, and ensure to provide the package name of the sample app (com.sample.spotzpush).

  9. Insert your Spotz Push application ID and key into the app/build.gradle file - these can be found in the `App Settings` -> `App Keys` tab in the console, and use the Android client key:

        ...
        buildConfigField 'String', 'SPOTZ_PUSH_PROJECT_ID', '"11111111-aaaa-2222-bbbb-333333333333"'
        buildConfigField 'String', 'SPOTZ_PUSH_PROJECT_KEY', '"11111111-aaaa-2222-bbbb-333333333333"'
        ...

  10. Run it!


How to add the SDK to your own Project
======================================

If you are using **Gradle**, include the following:

For FCM variant only, include the following plugin in your project `build.gradle`:

    ...
    buildscript {
        dependencies {
            classpath 'com.google.gms:google-services:3.2.1'
        }
    }
    ...

For FCM variant only, include the following plugin in your app module `build.gradle`:
    
    // should follow the dependencies declaration 
    ...
    apply plugin: 'com.google.gms.google-services'
    ...

Include these common dependencies in the dependencies closure for the app:

    ...

    allprojects {
        repositories {
            maven { url "https://localz.github.io/mvn-repo" }
        }
    }

    dependencies {
        compile 'com.android.support:support-v4:27.1.1'
        compile 'com.google.code.gson:gson:2.8.2'
        compile('com.google.http-client:google-http-client:1.23.0') {
            exclude module: 'httpclient'
        }
        compile('com.google.http-client:google-http-client-gson:1.23.0') {
            exclude module: 'httpclient'
        }
        compile 'com.google.android.gms:play-services-location:11.8.0'
        ...
    }
    ...

Dependencies specific for FCM

    dependencies {
        ...
        compile 'com.google.firebase:firebase-messaging:11.8.0'
        compile 'com.google.android.gms:play-services-base:11.8.0'
        compile 'com.localz.spotzpush.sdk:spotz-push-sdk-fcm:2.2.1@aar'
        ...
    }

Dependencies specific for Socket IO

    dependencies {
        ...
        compile('io.socket:socket.io-client:1.0.0') {
            exclude group: 'org.json', module: 'json'
        }
        compile 'com.localz.spotzpush.sdk:spotz-push-sdk-socket:2.2.1@aar'
        ...
    }

Dependencies specific for Pusher

    dependencies {
        ...
        compile 'org.slf4j:slf4j-api:1.7.25'
        compile 'com.localz.spotzpush.sdk:spotz-push-sdk-pusher:2.2.1@aar'
        ...
    }

Dependencies specific for Pushy

    dependencies {
        ...
        compile 'com.localz.spotzpush.sdk:spotz-push-sdk-pushy:2.2.1@aar'
        ...
    }


How to use the SDK
==================

There are two main ways to get started with the SDK.

The quickest and easiest way is to utilise the defaults provided by the SDK with the following changes:

### AndroidManifest.xml

Add the following services, receivers and permission within the *application* element:

For FCM (if your project is not using automatic manifest merging)

    ...
    <manifest>
        <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
            
        <application>
            <service
                android:name="com.google.firebase.messaging.FirebaseMessagingService"
                android:exported="true" >
                <intent-filter android:priority="-500" >
                    <action android:name="com.google.firebase.MESSAGING_EVENT" />
                </intent-filter>
            </service>
            <service
                android:name="com.localz.spotzpush.sdk.service.FirebaseService"
                android:exported="false" >
                <intent-filter>
                    <action android:name="com.google.firebase.MESSAGING_EVENT" />
                </intent-filter>
            </service>
            <service
                android:name="com.localz.spotzpush.sdk.service.InstanceIdService"
                android:exported="false" >
                <intent-filter>
                    <action android:name="com.google.firebase.INSTANCE_ID_EVENT" />
                </intent-filter>
            </service>
        </application>
    </manifest>
    ...

For Socket IO (if your project is not using automatic manifest merging)

    ...
    <manifest>
        <application>
            <receiver android:name="com.localz.spotzpush.sdk.receiver.RestartReceiver" >
                <intent-filter>
                    <action android:name="android.intent.action.BOOT_COMPLETED" />
                </intent-filter>
            </receiver>
            
            <service android:name="com.localz.spotzpush.sdk.service.BaseBackgroundSocketIoService" />
        </application>
    </manifest>
    ...

For Pusher (if your project is not using automatic manifest merging)

    ...
    <manifest>
        <application>
            <receiver android:name="com.localz.spotzpush.sdk.receiver.RestartReceiver" >
                <intent-filter>
                    <action android:name="android.intent.action.BOOT_COMPLETED" />
                </intent-filter>
            </receiver>
            
            <service
                android:name="com.localz.spotzpush.sdk.service.BackgroundPusherService"
                android:exported="false" >
            </service>
        </application>
    </manifest>
    ...

For Pushy (if your project is not using automatic manifest merging)

    ...
    <manifest>
        <application>
            <receiver android:name="com.localz.spotzpush.sdk.receiver.PushyBroadcastReceiver" android:exported="false">
                <intent-filter>
                    <action android:name="pushy.me" />
                </intent-filter>
            </receiver>
    
            <receiver android:name="me.pushy.sdk.receivers.PushyUpdateReceiver">
                <intent-filter>
                    <action android:name="android.intent.action.PACKAGE_REPLACED" />
                    <data android:scheme="package" />
                </intent-filter>
            </receiver>
    
            <receiver android:name="me.pushy.sdk.receivers.PushyBootReceiver" >
                <intent-filter>
                    <action android:name="android.intent.action.BOOT_COMPLETED"/>
                </intent-filter>
            </receiver>
    
            <service android:name="me.pushy.sdk.services.PushySocketService"/>
        </application>
    </manifest>
    ...

Within the app, include the following

For FCM, Socket IO and Pushy variants:
    
    ...
    
    SpotzPushService.init(android.content.Context, "your-spotz-push-project-id", "your-spotz-push-client-key");

    ...

For Pusher variant:
    
    ...
    
    SpotzPushService.init(android.content.Context, "your-pusher-app-key", "your-pusher-app-cluster", "your-spotz-push-project-id", "your-spotz-push-client-key");

    ...

Ensure to check whether the application has Google Play Services available before initialising the SpotzPushService. The sample code includes an example of this in MainActivity.checkPlayServices();

Your project is now ready to start using the Spotz Push SDK!

When the SpotzPushService is initialised, the app will automatically grab a device token from Firebase Cloud Messaging, and register this with Spotz Push asynchronously in the background. Once this is done, a device ID will be issued to be used for pushing notifications to.

Customise behaviour of SDK
============
### com.localz.spotzpush.sdk.receiver.AbstractSpotzPushBroadcastReceiver
In order to customise how the notification appears on the device, this BroadcastReceiver will need to be extended, implementing one of these methods:

    /**
     * Executed when a new push notification is triggered.
     *
     * @param context
     * @param notificationId notification id
     * @param title notification title
     * @param message notification message
     * @param soundType ringtone type
     * @param imageUrl image url
     */
    protected abstract void handleNotification(Context context, int notificationId, String title, String message, int soundType, String imageUrl);

    /**
     * Customer handler for a push notification.
     *
     * @param context
     * @param extras all original data received from push server
     * @return <code>true</code> if a notification has been handled and no further action required,
     * otherwise <code>false</code>
     */
    protected boolean handleNotification(Context context, @Nullable Bundle extras)

Note: see `CustomSpotzPushBroadcastReceiver.java` from fcm flavour.

The BroadcastReceiver must be registered in the AndroidManifest.xml file:

    <receiver android:name="com.sample.spotzpush.CustomSpotzPushBroadcastReceiver" android:exported="false">
        <intent-filter>
            <action android:name="com.localz.spotzpush.sdk.NOTIFICATION"/>
        </intent-filter>
    </receiver>

From this method, the `NotificationManager` can be freely called to display or manipulate the notification as desired.


Contribution
============

For bugs, feature requests, or other questions, [file an issue](https://github.com/localz/spotz-push-sdk-android/issues/new).

License
=======

Copyright 2018 [Localz Pty Ltd](http://www.localz.com/)
