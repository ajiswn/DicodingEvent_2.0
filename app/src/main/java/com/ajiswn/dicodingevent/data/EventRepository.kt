package com.ajiswn.dicodingevent.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.distinctUntilChanged
import com.ajiswn.dicodingevent.data.local.entity.EventEntity
import com.ajiswn.dicodingevent.data.local.room.EventDao
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

    fun getUpcomingEvent(): LiveData<Result<List<EventEntity>>> {
        result.value = Result.Loading
        val client = apiService.getUpcomingEvent()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val listEvents = response.body()?.listEvents
                    val eventList = ArrayList<EventEntity>()
                    appExecutors.diskIO.execute {
                        listEvents?.forEach { event ->
                            val isFavorite = eventDao.isEventFavorite(event.id)
                            val events = EventEntity(
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
                            eventList.add(events)
                        }
                        eventDao.deleteEvent(1)
                        eventDao.insertEvent(eventList)
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = eventDao.getUpcomingEvent()
        result.addSource(localData.distinctUntilChanged()) { newData ->
            result.value = Result.Success(newData)
        }
        return result
    }

    fun getFinishedEvent(): LiveData<Result<List<EventEntity>>> {
        result.value = Result.Loading
        val client = apiService.getFinishedEvent()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val listEvents = response.body()?.listEvents
                    val eventList = ArrayList<EventEntity>()
                    appExecutors.diskIO.execute {
                        listEvents?.forEach { event ->
                            val isFavorite = eventDao.isEventFavorite(event.id)
                            val events = EventEntity(
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
                            eventList.add(events)
                        }
                        eventDao.deleteEvent(0)
                        eventDao.insertEvent(eventList)
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = eventDao.getFinishedEvent()
        result.addSource(localData.distinctUntilChanged()) { newData ->
            result.value = Result.Success(newData)
        }
        return result
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