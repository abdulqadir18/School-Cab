package com.example.schoolcab;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO = "userABC";



    @Override
    public void onNewToken(String token) {

        Log.d("hello", "hello " );
        super.onNewToken(token);

        Log.d(TAG, "onNewToken: " + token);


        /*
          This method is invoked whenever the token refreshes
          OPTIONAL: If you want to send messages to this application instance
          or manage this apps subscriptions on the server side,
          you can send this token to your server.
        */


        // Once the token is generated, subscribe to topic with the userId
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
        Log.i(TAG, "onTokenRefresh completed with token: " + token);
    }
}