package com.kta.app.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ScheduleEntity")
data class ScheduleEntity(

    @ColumnInfo(name = "id_member")
    val id_member: Int? = null,

    @ColumnInfo(name = "status_absent")
    val status_absent: String? = null,

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "time")
    val time: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "absent")
    val absent: String,

    @ColumnInfo(name = "status")
    var status: String,

    @ColumnInfo(name = "pic")
    var pic: String,

    @ColumnInfo(name = "note")
    var note: String,
)

