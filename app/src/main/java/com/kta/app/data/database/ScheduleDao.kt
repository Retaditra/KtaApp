package com.kta.app.data.database

import androidx.room.*

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM ScheduleEntity")
    fun getAllSchedule(): List<ScheduleEntity>

    @Insert
    fun insertAll(schedule: List<ScheduleEntity>)

    @Query("SELECT * FROM ScheduleEntity WHERE status = 0")
    fun getScheduleSoon(): List<ScheduleEntity>

    @Query("SELECT * FROM ScheduleEntity WHERE status = 1")
    fun getScheduleOn(): List<ScheduleEntity>

    @Query("SELECT * FROM ScheduleEntity WHERE status = 2")
    fun getScheduleEnd(): List<ScheduleEntity>

    @Query("DELETE FROM ScheduleEntity")
    fun deleteAll()
}

/*
    @RawQuery(observedEntities = [ScheduleEntity::class])
    fun getNearestSchedule(query: SupportSQLiteQuery): LiveData<ScheduleEntity?>

 */
