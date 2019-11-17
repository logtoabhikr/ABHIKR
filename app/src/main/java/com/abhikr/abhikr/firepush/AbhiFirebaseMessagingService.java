package com.abhikr.abhikr.firepush;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.abhikr.abhikr.SampleActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

public class AbhiFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "AbhiFirebaseMS";
    private static final String TITLE = "title";
    private static final String EMPTY = "";
    private static final String MESSAGE = "message";
    private static final String IMAGE = "image";
    private static final String ACTION = "action";
    private static final String URL = "url";
    private static final String ACTION_DESTINATION = "action_destination";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        storeToken(s);
        FirebaseMessaging.getInstance().subscribeToTopic("Abhikr");
        FirebaseMessaging.getInstance().subscribeToTopic("ABHIKR");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            handleData(data);

        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.

    }
    private void storeToken(String token) {
        //we will save the token in sharedpreferences later
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
        SharedPrefManager.PUTTOKEN(token);
    }
    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {
        String message = RemoteMsgNotification.getBody();
        String title = RemoteMsgNotification.getTitle();
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        Intent resultIntent = new Intent(getApplicationContext(), SampleActivity.class);
        NotificationBase notificationUtils = new NotificationBase(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        //notificationUtils.playNotificationSound();
    }

    private void handleData(Map<String, String> data) {
        String title = data.get(TITLE);
        String message = data.get(MESSAGE);
        String iconUrl = data.get(IMAGE);
        String action = data.get(ACTION);
        String actionDestination = data.get(ACTION_DESTINATION);
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);
        notificationVO.setIconUrl(iconUrl);
        notificationVO.setAction(action);
        notificationVO.setActionDestination(actionDestination);

        Intent resultIntent = new Intent(getApplicationContext(), SampleActivity.class);

        NotificationBase notificationUtils = new NotificationBase(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        //notificationUtils.playNotificationSound(); // create new channel with sounduri and it will work perfectly
    }
   /* private void PushSaving(Map<String, String> data)
    {
        if (!URL.equalsIgnoreCase(data.get(ACTION))) {
            return;
        }
        StringRequest pushRequest=new StringRequest(Request.Method.POST, Config.URL_PUSH_SAVING, response -> {
            try {
                JSONObject jsonObject=new JSONObject(response);
                Log.d(TAG,"push response :"+jsonObject.getString(Config.TAG_RESPONSE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        })
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params=new HashMap<>();
                params.put("user_id",SharedPrefManager.getInstance(getApplicationContext()).getKeyUserId());
                params.put("title",data.get(TITLE));
                params.put("message",data.get(MESSAGE));
                params.put("image",data.get(IMAGE));
                params.put("url",data.get(ACTION_DESTINATION));
                return params;
            }
        };
        MyAbhi.getInstance().addToRequestQueue(pushRequest);
    }*/
}

 /* void SchedulePush(final String title,final String message,final String image,final String url)
    {
        Data data =new Data.Builder()
                .putString("title", title)
                .putString("message", message)
                .putString("image", image)
                .putString("url", url)
                .build();
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false) // you can add as many constraints as you want
                .build();

        final OneTimeWorkRequest workRequest =
                new OneTimeWorkRequest.Builder(PushWorker.class)
                        .setInputData(data)
                        .setConstraints(constraints)
                        .setBackoffCriteria(
                                BackoffPolicy.LINEAR,
                                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                                TimeUnit.MILLISECONDS)
                        .build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniqueWork("DctPush", ExistingWorkPolicy.KEEP,workRequest);
        //https://www.simplifiedcoding.net/android-workmanager-tutorial/
    }
    worker class code to handle
     @Override
    public Result doWork() {
        try
        {
            Data data=getInputData();
            PushSaving(data.getString("title"), data.getString("message"), data.getString("image"), data.getString("url"));
            return Result.success(data);

        } catch (Exception e) {
            return Result.failure();
        }
    }
    */
