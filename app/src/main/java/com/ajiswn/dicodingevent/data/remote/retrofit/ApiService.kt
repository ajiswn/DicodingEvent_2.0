package com.ajiswn.dicodingevent.data.remote.retrofit

import com.ajiswn.dicodingevent.data.remote.response.DetailEventResponse
import com.ajiswn.dicodingevent.data.remote.response.EventResponse
import retrofit2.http.*

interface ApiService {
    @GET("events")
    suspend fun getUpcomingEvent(
        @Query("active") active: Int = 1,
    ): EventResponse

    @GET("events")
    suspend fun getFinishedEvent(
        @Query("active") active: Int = 0,
    ): EventResponse

    @GET("events/{id}")
    suspend fun getDetailEvent(
        @Path("id") eventId: Int
    ): DetailEventResponse

    @GET("events")
    suspend fun searchEvent(
        @Query("active") active: Int = -1,
        @Query("q") keyword: String
    ): EventResponse
}