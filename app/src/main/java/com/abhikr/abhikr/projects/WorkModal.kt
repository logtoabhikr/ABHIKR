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
data class WorkModal (var Title:String="",var Description:String="",var Duration:String="",var PlayStore:String=""
                     ,var Website:String="",var Client:String=""
                     ,var Logo:String="",@ServerTimestamp var TimeStamp: Timestamp?=null
                     ) : Parcelable