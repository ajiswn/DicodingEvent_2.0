package com.ajiswn.dicodingevent.di

import android.content.Context
import com.ajiswn.dicodingevent.data.EventRepository
import com.ajiswn.dicodingevent.data.local.room.EventDatabase
import com.ajiswn.dicodingevent.data.remote.retrofit.ApiConfig
import com.ajiswn.dicodingevent.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, dao, appExecutors)
    }
}