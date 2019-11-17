package com.abhikr.abhikr.firepush;

/**
 * Created by ak on 06/20/17.
 */
import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefManager {
    private static final String ABHIKR_USER_PREF="com.abhikr.android.innovations.USERPREF";
    private static final String ABHIKR_PREF_USERTOKEN="com.abhikr.android.innovations.USERTOken";
    private static final String ABHIKR_PREF_USERNAME="com.abhikr.android.innovations.USERNaMe";
    private static final String ABHIKR_PREF_USERID="com.abhikr.android.innovations.USERiD";
    private static final String ABHIKR_PREF_USERPASS="com.abhikr.android.innovations.USERPAsS";
    private static final String ABHIKR_PREF_USERSIGNIN="com.abhikr.android.innovations.USERSIGNIN_STATUS";
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";
    private static SharedPreferences mSharedPreferences,mSharedpreftoken;
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }
    public static void PUTTOKEN(String token)
    {
        SharedPreferences.Editor editor=mSharedpreftoken.edit();
        editor.putString(ABHIKR_PREF_USERTOKEN,token);
        editor.apply();
    }

    public static void InitABHIKRPref(Context context){

        if(mSharedPreferences == null){
            mSharedpreftoken=context.getSharedPreferences("com.abhikr.android.innovations.USERFCMTOken",Context.MODE_PRIVATE);
            mSharedPreferences = context.getSharedPreferences(ABHIKR_USER_PREF , Context.MODE_PRIVATE);
        }
    }
    public static String GETTOKEN()
    {
        return mSharedpreftoken.getString(ABHIKR_PREF_USERTOKEN,null);
    }
    public static void PUTUSERID(String userid)
    {
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putString(ABHIKR_PREF_USERID,userid);
        editor.apply();
    }
    public static void PUTUSERNAME(String username)
    {
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putString(ABHIKR_PREF_USERNAME,username);
        editor.apply();
    }
    public static String GETUSERNAME()
    {
        return mSharedPreferences.getString(ABHIKR_PREF_USERNAME,null);
    }
    public static String GETUSERID()
    {
        return mSharedPreferences.getString(ABHIKR_PREF_USERID,null);
    }
    public static void PUTUSERPASS(String userpass)
    {
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putString(ABHIKR_PREF_USERPASS,userpass);
        editor.apply();
    }
    public static String GETUSERPASS()
    {
        return mSharedPreferences.getString(ABHIKR_PREF_USERPASS,null);
    }

    public static void PUTSIGNIN_STATUS()
    {
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putBoolean(ABHIKR_PREF_USERSIGNIN,true);
        editor.apply();
    }
    public static boolean GETSIGNIN_STATUS()
    {
        return mSharedPreferences.getBoolean(ABHIKR_PREF_USERSIGNIN,false);
    }
    public static void SHAREDPREF_Clear()
    {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}