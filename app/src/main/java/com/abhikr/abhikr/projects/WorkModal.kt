package com.abhikr.abhikr.projects


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize

//does not define a no-argument constructor to get ride initialize value for it ex var ak:String=""
@Parcelize
@IgnoreExtraProperties
@Keep
data class WorkModal (var client:String?=null,
                      var description:String?=null,
                      var duration:String?=null,
                      var logo:String?=null,
                      var playStore:String?=null,
                      @ServerTimestamp var timeStamp: Timestamp?=null,
                      var title:String?=null,
                      var website:String?=null
                     ) : Parcelable