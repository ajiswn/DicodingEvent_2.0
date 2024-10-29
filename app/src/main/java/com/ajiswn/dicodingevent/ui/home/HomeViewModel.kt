package com.ajiswn.dicodingevent.ui.home

import androidx.lifecycle.ViewModel
import com.ajiswn.dicodingevent.data.EventRepository

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getUpcomingEvent() = eventRepository.getUpcomingEvent()
    fun getFinishedEvent() = eventRepository.getFinishedEvent()

}
