<a href="http://www.localz.co/"><img alt="Localz logo" align="right" width="50" height="50" src="http://www.localz.co/assets/images/logo-round.png" /></a> Spotz Push Android SDK
=================

[Spotz Push](http://spotz.localz.co/) is a push notification platform.

Changelog
=========

**1.0.0**	
* Initial public release.

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
    
  3. Create a Spotz Push project using the [Spotz Push console](http://spotzpush.localz.com).

  4. Log into the [Google Developer Console](https://console.developers.google.com/project) and ensure to have a project created. Note the 'Project Number' on the overview screen for the project, and ensure to enable 'Cloud Messaging for Android' on the APIs screen.
    
  5. Insert your Spotz Push project ID and client key into MainActivity.java - these can be found in the Spotz console under your project and use the *Android* client key:

        ...
        SpotzPushService.init(this, "your-google-project-number", "your-spotz-push-project-id", "your-spotz-push-client-key");
        ...

  5. Run it!


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
    compile 'com.google.android.gms:play-services-location:6.5.87'
    compile 'com.google.android.gms:play-services-base:6.5.87'

    compile 'com.google.code.gson:gson:2.3.1'
    compile 'com.google.http-client:google-http-client:1.20.0'
    compile 'com.google.http-client:google-http-client-gson:1.20.0'
}
...

How to use the SDK
==================

There are two main ways to get started with the SDK.

The quickest and easiest way is to utilise the defaults provided by the SDK with the following changes:

###AndroidManifest.xml
Add the following within the <manifest> element.

...
    <permission
        android:name="com.localz.spotzpush.sdk.permission.C2D_MESSAGE"
        android:protectionLevel="signature" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="com.localz.spotzpush.sdk.permission.C2D_MESSAGE" />
    <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
...

Add the following within the <applicatino> element

...
    <receiver android:name="com.localz.spotzpush.sdk.receiver.DefaultBroadcastReceiver" android:permission="com.google.android.c2dm.permission.SEND">
        <intent-filter>
            <action android:name="com.google.android.c2dm.intent.RECEIVE" />
        </intent-filter>
    </receiver>

    <service android:name="com.localz.spotzpush.sdk.service.DefaultIntentService" />
...

Within the app, include the following

...
    SpotzPushService.init(this, "your-google-project-number", "your-spotz-push-project-id", "your-spotz-push-client-key");
...

This call should be made periodically to ensure that any updates to the push token is captured. Also ensure to check whether the application has Google Play Services available before initialising the SpotzPushService. The sample code includes an example of this in MainActivity.checkPlayServices();

Your project is now ready to start using the Spotz Push SDK!

When the SpotzPushService is initialised, the app will automatically grab a device token from Google Cloud Messaging, and register this with Spotz Push asynchronously in the background. Once this is done, a device ID will be issued to be used for pushing notifications to.

Contribution
============

For bugs, feature requests, or other questions, [file an issue](https://github.com/localz/spotz-push-sdk-android/issues/new).

License
=======

Copyright 2015 [Localz Pty Ltd](http://www.localz.com/)
