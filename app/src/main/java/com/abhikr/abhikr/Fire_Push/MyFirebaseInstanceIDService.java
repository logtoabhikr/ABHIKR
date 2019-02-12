package com.abhikr.abhikr.Fire_Push;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ak on 07/08/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {
        //saving the token on shared preferences
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }
}