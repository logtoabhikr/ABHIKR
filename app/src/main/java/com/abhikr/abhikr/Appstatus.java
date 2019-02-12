package com.abhikr.abhikr;

/**
 * Created by ak on 5/2/2016.
 */
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class Appstatus {

    private static Appstatus instance = new Appstatus();
    static Context context;
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

    public static Appstatus getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
}
