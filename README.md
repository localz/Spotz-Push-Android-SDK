Spotz Push Android SDK
=================

Spotz Push is a push notification platform.

Changelog
=========

**2.3.0**

* Upgraded to Spotz Push SDK 4.0.0
* SpotzPushService -> LocalzPushSDK
* BaseJsonResponse -> BaseResponse
* DeviceJsonResponse -> DeviceResponse

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

Include these dependencies in the dependencies closure for the app:

    // Project build.gradle
    allprojects {
        repositories {
            jcenter()
            google()
            maven {
                url 'https://jitpack.io'
                credentials { username authToken }
            }
        }
    }

    // App build.gradle
    android {
        ...

        // Ensure Java 8 is enabled
        compileOptions {
            sourceCompatibility JavaVersion.VERSION_1_8
            targetCompatibility JavaVersion.VERSION_1_8
        }
    }

    dependencies {
        implementation 'com.android.support:support-v4:27.1.1'

        //Only required for FCM
        fcmImplementation "com.google.firebase:firebase-messaging:$playServicesVersion"
        fcmImplementation "com.github.localz:spotz-push-sdk-android-libs:4.0.0"

        //Only required for pushy
        pushyImplementation "com.github.localz:spotz-push-sdk-android-libs:4.0.0:pushy@aar"

        //Only required for pusher
        pusherImplementation "com.github.localz:spotz-push-sdk-android-libs:4.0.0:pusher@aar"

        //Only required for socket.io
        socketImplementation "com.github.localz:spotz-push-sdk-android-libs:4.0.0:socket@aar"

        ...
    }
    ...


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
    
    LocalzPushSDK.init(android.content.Context, "your-spotz-push-project-id", "your-spotz-push-client-key");

    ...

For Pusher variant:
    
    ...
    
    LocalzPushSDK.init(android.content.Context, "your-pusher-app-key", "your-pusher-app-cluster", "your-spotz-push-project-id", "your-spotz-push-client-key");

    ...

Ensure to check whether the application has Google Play Services available before initialising the LocalzPushSDK. The sample code includes an example of this in MainActivity.checkPlayServices();

Your project is now ready to start using the Spotz Push SDK!

When the LocalzPushSDK is initialised, the app will automatically grab a device token from Firebase Cloud Messaging, and register this with Spotz Push asynchronously in the background. Once this is done, a device ID will be issued to be used for pushing notifications to.

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
