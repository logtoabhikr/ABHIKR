package com.abhikr.abhikr;

/**
 * Created by ak on 07/05/17.
 */
public class Config {
    //URLs to register.php and confirm.php file
    public static final String REGISTER_URL = "http://www.abhikr.com/abhikr_app/AndroidOTP/register.php";
    public static final String CONFIRM_URL = "http://www.abhikr.com/abhikr_app/AndroidOTP/confirm.php";

    //Keys to send username, password, phone and otp
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_OTP = "otp";

    //JSON Tag from response from server
    //public static final String TAG_RESPONSE= "ErrorMessage";
    public static final String TAG_RESPONSE= "message";
}
