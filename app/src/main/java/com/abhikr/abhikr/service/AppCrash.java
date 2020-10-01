package com.abhikr.abhikr.service;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatDelegate;

import com.abhikr.abhikr.firepush.SharedPrefManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.FirebaseDatabase;

public class AppCrash extends Application {

    public  static final String TAG = AppCrash.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private FirebaseUser user;
    public static final String NIGHT_MODE = "NIGHT_MODE";
    private boolean isNightModeEnabled = false;
    private static AppCrash mInstance = null;

    public static AppCrash getInstance() {

        if(mInstance == null)
        {
            mInstance = new AppCrash();
        }
        return mInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        //FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        user= FirebaseAuth.getInstance().getCurrentUser();
       try
       {
           abhiprofile();
           FirebaseDatabase.getInstance().setPersistenceEnabled(true);
       }
       catch (Exception e)
       {
           e.printStackTrace();
           FirebaseCrashlytics.getInstance().recordException(e);
       }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        SharedPrefManager.InitABHIKRPref(this);
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.isNightModeEnabled = mPrefs.getBoolean(NIGHT_MODE, false);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            //loading image for use
          /*  Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 199 * 1024 * 1024); //100mb
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();*/
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    void abhiprofile()
    {

        if (user != null)
        {
                try
                {
                    FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
                    crashlytics.setUserId(user.getUid());

                    crashlytics.log("ABHIKR: Crash happen");

                    crashlytics.setCustomKey("bool_key",true);

                    crashlytics.setCustomKey("double_key",42.0);

                    crashlytics.setCustomKey("float_key",42.0F);

                    crashlytics.setCustomKey("int_key",42);

                    crashlytics.setCustomKey("long_key",42L);

                    crashlytics.setCustomKey("str_key","42");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
            }
    }
    public boolean isNightModeEnabled() {
        return isNightModeEnabled;
    }

    public void setIsNightModeEnabled(boolean isNightModeEnabled) {
        this.isNightModeEnabled = isNightModeEnabled;

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(NIGHT_MODE, isNightModeEnabled);
        editor.apply();
    }
}
