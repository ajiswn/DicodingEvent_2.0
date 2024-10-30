package com.ajiswn.dicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ajiswn.dicodingevent.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: List<EventEntity>)

    @Query("SELECT * FROM event WHERE active = 1 ORDER BY beginTime DESC")
    fun getUpcomingEvent(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE active = 0 ORDER BY beginTime DESC")
    fun getFinishedEvent(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE id = :id")
    fun getDetailEvent(id: Int): LiveData<EventEntity>

    @Query("SELECT * FROM event WHERE favorite = 1")
    fun getFavoriteEvent(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event WHERE name LIKE '%' || :name || '%'")
    fun searchEvent(name: String): LiveData<List<EventEntity>>

    @Query("DELETE FROM event WHERE favorite = 0 AND active = :active")
    suspend fun deleteEvent(active: Int)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("SELECT EXISTS(SELECT * FROM event WHERE id = :id AND favorite = 1)")
    suspend fun isEventFavorite(id: Int?): Boolean
}