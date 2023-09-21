package com.kta.app.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ScheduleEntity")
data class ScheduleEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "task")
    val nama: String,

    @ColumnInfo(name = "time")
    val waktu: String,

    @ColumnInfo(name = "date")
    val tanggal: String,

    @ColumnInfo(name = "location")
    val lokasi: String,

    @ColumnInfo(name = "status")
    var status: String,

    @ColumnInfo(name = "pic")
    var pic: String,

    @ColumnInfo(name = "action")
    val aksi: String,

    @ColumnInfo(name = "minutes")
    var notulensi: String,
)

