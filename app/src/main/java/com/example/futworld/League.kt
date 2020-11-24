package com.example.futworld

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.example.futworld.Club

data class League(
    val createrId :String = "",
    val title: String = "",
    val badge: String = "",
    val size: Int = 0
)