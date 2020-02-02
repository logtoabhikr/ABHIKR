package com.abhikr.abhikr.model;


import androidx.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Keep
public class Message{
    public String idSender;
    public String idReceiver;
    public String text;
    public long timestamp;
}