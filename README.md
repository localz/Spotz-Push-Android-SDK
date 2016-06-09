Spotz Push Android SDK
=================

Spotz Push is a push notification platform.

Changelog
=========

**1.0.0**

* Initial public release.

**1.0.1**

* Incorporated changes required for version 1.0.1 of the Spotz Push SDK.

**2.0.0**

* Incorporated new SDK version to make compatible with Android 6.

**2.0.1**

* Bumped to latest SDK version and updated documentation.

**2.0.2**

* Bumped to latest SDK version and updated documentation.

**2.0.3**

* Fixed issue with re-initialising SDK when the device is yet to be issued a deviceId.

**2.0.4**

* IntentService to handle notification can now be configured via the AndroidManifest rather than extending DefaultBroadcastReceiver.

**2.0.5**

* Bumped to latest SDK version and updated documentation.

**2.0.6**

* Bumped to latest SDK version and dependencies.

**2.0.7**

* Bumped to latest SDK version and dependencies.


What does the sample app do?
============================

The app demonstrates how to import the SDK to be able to use Spotz Push.

How to run the sample app
=========================

The sample app requires devices running Android 4.0.3 or newer.

  1. Clone the repository:
  
        git clone git@github.com:localz/spotz-push-sdk-android.git

  2. Import the project:
    
    If you're using **Android Studio**, simply 'Open' the project.

  3. Log into the [Google Developer Console](https://console.developers.google.com/project) and ensure to have a project created with the package name of `com.sample.spotzpush`. Note the 'Project Number' on the overview screen for the project, and ensure to enable 'Cloud Messaging for Android' on the APIs screen.

  4. **After enabling Google Cloud Messaging for your Google project**, create an API Key.

  5. Create your `google-services.json` file by selecting the newly created project and package name at the [Google Services](https://developers.google.com/mobile/add) page, and save this file in the `app/` directory for the sample Android app.

  6. Sign in and create a Spotz Push `Organisation` and `Application` via the [Spotz Push Console](https://console.localz.io/spotz-push).

  7. Upload your GCM API key credentials via the `Push Configuration` menu for your Spotz Push Application, and ensure to provide the package name of the sample app (com.sample.spotzpush).

  8. Insert your Spotz Push application ID and key into the app/build.gradle file - these can be found in the `App Settings` -> `App Keys` tab in the console, and use the Android client key:

        ...
        buildConfigField 'String', 'PLAY_PROJECT_ID', '"123456789012"'
        buildConfigField 'String', 'SPOTZ_PUSH_PROJECT_ID', '"11111111-aaaa-2222-bbbb-333333333333"'
        buildConfigField 'String', 'SPOTZ_PUSH_PROJECT_KEY', '"11111111-aaaa-2222-bbbb-333333333333"'
        ...

  9. Run it!


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
        compile 'com.google.android.gms:play-services-base:8.3.0'
        compile 'com.google.android.gms:play-services-gcm:8.3.0'
        compile 'com.google.android.gms:play-services-location:8.3.0'

        compile 'com.google.code.gson:gson:2.4'
        compile('com.google.http-client:google-http-client:1.20.0') {
            exclude module: 'httpclient'
        }
        compile('com.google.http-client:google-http-client-gson:1.20.0') {
            exclude module: 'httpclient'
        }

        compile 'com.android.support:support-v4:23.1.1'
        compile 'com.localz.spotzpush.sdk:spotz-push-sdk-gcm:1.2.3@aar'
    }
    
    ...

How to use the SDK
==================

There are two main ways to get started with the SDK.

The quickest and easiest way is to utilise the defaults provided by the SDK with the following changes:

###AndroidManifest.xml

Add the following within the *application* element

    ...
    <manifest>
        <!--Permission to use only if default service for Spotz Push SDK is used.
            To add customisations to how notifications are handled, create your own version which extends
            com.localz.spotzpush.sdk.service.AbstractGcmIntentService -->
        <permission
            android:name="com.localz.spotzpush.sdk.permission.C2D_MESSAGE"
            android:protectionLevel="signature" />

        <uses-permission android:name="com.localz.spotzpush.sdk.permission.C2D_MESSAGE" />

        <application>
            <!--Default service for Spotz Push. To add customisations to how notifications are handled,
                create your own version which extends com.localz.spotzpush.sdk.service.AbstractGcmIntentService -->
            <service android:name="com.localz.spotzpush.sdk.service.GcmIntentService" />
            <!--IntentService to handle the notification. Ensure that it matches the above service class!-->
            <meta-data android:name="com.localz.spotzpush.sdk.handleNotification" android:value="com.localz.spotzpush.sdk.service.GcmIntentService" />

            <receiver android:name="com.localz.spotzpush.sdk.receiver.GcmBroadcastReceiver" android:permission="com.google.android.c2dm.permission.SEND">
                <intent-filter>
                    <action android:name="com.google.android.c2dm.intent.RECEIVE" />
                </intent-filter>
            </receiver>
        </application>
    </manifest>
    ...

Within the app, include the following

    ...
    
    SpotzPushService.init(android.content.Context, "your-google-project-number", "your-spotz-push-project-id", "your-spotz-push-client-key");

    ...

This call should be made periodically to ensure that any updates to the push token is captured. Also ensure to check whether the application has Google Play Services available before initialising the SpotzPushService. The sample code includes an example of this in MainActivity.checkPlayServices();

Your project is now ready to start using the Spotz Push SDK!

When the SpotzPushService is initialised, the app will automatically grab a device token from Google Cloud Messaging, and register this with Spotz Push asynchronously in the background. Once this is done, a device ID will be issued to be used for pushing notifications to.

Customise behaviour of SDK
============
###com.localz.spotzpush.sdk.service.AbstractGcmIntentService
In order to customise how the notification appears on the device, this class will need to be extended, implementing the method:

    public abstract void handleNotification(Bundle extras, String title, String message, String sound, String imageUrl)

The `extras` parameter contains the push notification parsed into a bundle. The bundle contains the default (title, message, sound, imageUrl) and custom attributes of the notification. From this method, the `NotificationManager` can be freely called to display or manipulate the notification as desired.

###com.localz.spotzpush.sdk.receiver.DefaultBroadcastReceiver
If AbstractGcmIntentService is extended, this class will also need to be extended with the minimum of:

    public class CustomBroadcastReceiver extends DefaultBroadcastReceiver {

        public CustomBroadcastReceiver() {
             //CustomIntentService extends AbstractGcmIntentService
             super(CustomIntentService.class.getName());
        }
    }

Modify the AndroidManifest.xml similar to:

    <service android:name="com.example.CustomIntentService" />

    <receiver android:name="com.example.CustomBroadcastReceiver" android:permission="com.google.android.c2dm.permission.SEND">
        <intent-filter>
            <action android:name="com.google.android.c2dm.intent.RECEIVE" />
        </intent-filter>
    </receiver>

Contribution
============

For bugs, feature requests, or other questions, [file an issue](https://github.com/localz/spotz-push-sdk-android/issues/new).

License
=======

Copyright 2015 [Localz Pty Ltd](http://www.localz.com/)
