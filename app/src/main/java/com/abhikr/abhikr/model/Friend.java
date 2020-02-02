package com.abhikr.abhikr.model;


import androidx.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Keep
public class Friend extends User{
    public String id;
    public String idRoom;
}
