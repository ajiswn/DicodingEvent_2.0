package com.ajiswn.dicodingevent.data.remote.retrofit

import com.ajiswn.dicodingevent.data.remote.response.DetailEventResponse
import com.ajiswn.dicodingevent.data.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getUpcomingEvent(
        @Query("active") active: Int = 1,
    ): Call<EventResponse>

    @GET("events")
    fun getFinishedEvent(
        @Query("active") active: Int = 0,
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") eventId: Int
    ): Call<DetailEventResponse>

    @GET("events")
    fun searchEvent(
        @Query("active") active: Int = -1,
        @Query("q") keyword: String
    ): Call<EventResponse>
}