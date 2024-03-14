package com.kta.app.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Schedule(
    val id_member: Int? = null,
    val status_absent: String? = null,
    val id: Int,
    val name: String,
    val location: String,
    val time: String,
    val date: String,
    val absent: String,
    val status: String,
    val pic: String,
    val note: String
) : Parcelable



