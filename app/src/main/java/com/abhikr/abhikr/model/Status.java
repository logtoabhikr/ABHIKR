package com.abhikr.abhikr.model;


import androidx.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Keep
public class Status{
    public boolean isOnline;
    public long timestamp;

    public Status(){
        isOnline = false;
        timestamp = 0;
    }
}
