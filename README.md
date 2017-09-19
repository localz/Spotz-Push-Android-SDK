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

* Added changes to support Android O.
* Added custom notification handler (see *"Customise behaviour of SDK"* section below).

Note: `GcmIntentService` has been deprecated.


What does the sample app do?
============================

The app demonstrates how to import the SDK to be able to use Spotz Push.

How to run the sample app
=========================

The sample app requires devices running Android 4.0.3 or newer.

  1. Clone the repository:
  
        git clone git@github.com:localz/spotz-push-sdk-android.git

  2. Import the project:
    
    If you're using **Android Studio 1.5 or higher**, simply 'Open' the project.

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

If you are using **Gradle**, include the following in the dependencies closure for the app.

    ...

    allprojects {
        repositories {
            maven { url "http://localz.github.io/mvn-repo" }
        }
    }

    dependencies {
        compile 'com.google.firebase:firebase-messaging:11.2.0'
        compile 'com.google.android.gms:play-services-base:11.2.0'
        compile 'com.google.android.gms:play-services-location:11.2.0'

        compile 'com.google.code.gson:gson:2.8.1'
        compile('com.google.http-client:google-http-client:1.20.0') {
            exclude module: 'httpclient'
        }
        compile('com.google.http-client:google-http-client-gson:1.20.0') {
            exclude module: 'httpclient'
        }

        compile 'com.android.support:support-v4:26.0.0'
        compile 'com.localz.spotzpush.sdk:spotz-push-sdk-fcm:2.1.0@aar'
    }
    
    ...

How to use the SDK
==================

There are two main ways to get started with the SDK.

The quickest and easiest way is to utilise the defaults provided by the SDK with the following changes:

### AndroidManifest.xml

Add the following within the *application* element. Note the FirebaseInstanceIdService is already included in the AndroidManifest by the SDK.

    ...
    <manifest>
        <application>
            <!--Default service to handle incoming notifications for Spotz Push. -->
            <service android:name="com.localz.spotzpush.sdk.service.FirebaseService" android:exported="false">
                <intent-filter>
                    <action android:name="com.google.firebase.MESSAGING_EVENT"/>
                </intent-filter>
            </service>
        </application>
    </manifest>
    ...

Within the app, include the following

    ...
    
    SpotzPushService.init(android.content.Context, "your-spotz-push-project-id", "your-spotz-push-client-key");

    ...

Ensure to check whether the application has Google Play Services available before initialising the SpotzPushService. The sample code includes an example of this in MainActivity.checkPlayServices();

Your project is now ready to start using the Spotz Push SDK!

When the SpotzPushService is initialised, the app will automatically grab a device token from Firebase Cloud Messaging, and register this with Spotz Push asynchronously in the background. Once this is done, a device ID will be issued to be used for pushing notifications to.

Customise behaviour of SDK
============
### com.localz.spotzpush.sdk.receiver.AbstractSpotzPushBroadcastReceiver
In order to customise how the notification appears on the device, this BroadcastReceiver will need to be extended, implementing the method:

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

Copyright 2017 [Localz Pty Ltd](http://www.localz.com/)
