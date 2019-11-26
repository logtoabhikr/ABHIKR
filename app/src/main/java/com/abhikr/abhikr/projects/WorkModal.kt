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
data class WorkModal (var Client:String?=null,
                      var Description:String?=null,
                      var Duration:String?=null,
                      var Logo:String?=null,
                      var PlayStore:String?=null,
                      @ServerTimestamp var TimeStamp: Timestamp?=null,
                      var Title:String?=null,
                      var Website:String?=null
                     ) : Parcelable