package com.kta.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ScheduleEntity::class], version = 1)
abstract class ScheduleDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var instance: ScheduleDatabase? = null

        fun getInstance(context: Context): ScheduleDatabase {
            return synchronized(this) {
                instance ?: Room.databaseBuilder(context, ScheduleDatabase::class.java, "schedule.db")
                    .build()
            }
        }
    }
}

