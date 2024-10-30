package com.ajiswn.dicodingevent.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.ajiswn.dicodingevent.data.local.entity.EventEntity
import com.ajiswn.dicodingevent.data.local.room.EventDao
import com.ajiswn.dicodingevent.data.remote.response.DetailEventResponse
import com.ajiswn.dicodingevent.data.remote.response.EventResponse
import com.ajiswn.dicodingevent.data.remote.retrofit.ApiService
import com.ajiswn.dicodingevent.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
){
    private val result = MediatorLiveData<Result<List<EventEntity>>>()

    fun getUpcomingEvent(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUpcomingEvent()
            val listEvents = response.listEvents
            val eventList = listEvents.map { event ->
                val isFavorite = eventDao.isEventFavorite(event.id)
                EventEntity(
                    event.id,
                    event.summary,
                    event.mediaCover,
                    event.registrants,
                    event.imageLogo,
                    event.link,
                    event.description,
                    event.ownerName,
                    event.cityName,
                    event.quota,
                    event.name,
                    event.beginTime,
                    event.endTime,
                    event.category,
                    isFavorite,
                    active = 1
                )
            }
            eventDao.deleteEvent(1)
            eventDao.insertEvent(eventList)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventEntity>>> =
            eventDao.getUpcomingEvent().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getFinishedEvent(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFinishedEvent()
            val listEvents = response.listEvents
            val eventList = listEvents.map { event ->
                val isFavorite = eventDao.isEventFavorite(event.id)
                EventEntity(
                    event.id,
                    event.summary,
                    event.mediaCover,
                    event.registrants,
                    event.imageLogo,
                    event.link,
                    event.description,
                    event.ownerName,
                    event.cityName,
                    event.quota,
                    event.name,
                    event.beginTime,
                    event.endTime,
                    event.category,
                    isFavorite,
                    active = 0
                )
            }
            eventDao.deleteEvent(0)
            eventDao.insertEvent(eventList)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventEntity>>> =
            eventDao.getFinishedEvent().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getDetailEvent(id: Int): LiveData<Result<EventEntity>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailEvent(id)
            val event = response.event
            val isFavorite = eventDao.isEventFavorite(event.id)
            val eventDetail = EventEntity(
                event.id,
                event.summary,
                event.mediaCover,
                event.registrants,
                event.imageLogo,
                event.link,
                event.description,
                event.ownerName,
                event.cityName,
                event.quota,
                event.name,
                event.beginTime,
                event.endTime,
                event.category,
                isFavorite,
            )

            eventDao.insertEvent(listOf(eventDetail))
            emit(Result.Success(eventDetail))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<EventEntity>> =
            eventDao.getDetailEvent(id).map { Result.Success(it) }
        emitSource(localData)
    }

    fun searchEvent(keyword: String): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.searchEvent(keyword = keyword)
            val listEvents = response.listEvents
            val eventList = listEvents.map { event ->
                val isFavorite = eventDao.isEventFavorite(event.id)
                EventEntity(
                    event.id,
                    event.summary,
                    event.mediaCover,
                    event.registrants,
                    event.imageLogo,
                    event.link,
                    event.description,
                    event.ownerName,
                    event.cityName,
                    event.quota,
                    event.name,
                    event.beginTime,
                    event.endTime,
                    event.category,
                    isFavorite,
                    active = -1
                )
            }
            eventDao.deleteEvent(-1)
            eventDao.insertEvent(eventList)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventEntity>>> = eventDao.searchEvent(keyword).map { Result.Success(it) }
        emitSource(localData)
    }



    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}