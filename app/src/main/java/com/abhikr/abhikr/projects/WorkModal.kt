package com.abhikr.abhikr.projects


import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
@IgnoreExtraProperties
@Keep
data class WorkModal(var Title:String="",var Description:String="",var Duration:String="",var PlayStore:String=""
                     ,var Website:String="",var Client:String=""
                     ,var Logo:String="",var timestamp: String="")