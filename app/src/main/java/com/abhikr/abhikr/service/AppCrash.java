package com.abhikr.abhikr.service;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.fabric.sdk.android.Fabric;


public class AppCrash extends Application {

    FirebaseUser user;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        user= FirebaseAuth.getInstance().getCurrentUser();
        Fabric.with(this, new Crashlytics());
       /* Crashlytics.setUserName("");
        Crashlytics.setUserEmail("");
        Crashlytics.setUserIdentifier("");*/
       try
       {
           abhiprofile();
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
    }
    void abhiprofile()
    {

        if (user != null)
        {
                try
                {
                    Crashlytics.setUserName(user.getUid());
                    Crashlytics.setUserEmail(user.getEmail());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
    }
}
