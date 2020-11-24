package com.example.futworld

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Club(val id: String, val name: String, val badge: String, val size: Int, val clubs: List<Club>?): Parcelable