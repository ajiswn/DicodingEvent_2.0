package com.ajiswn.dicodingevent.ui.upcoming

import androidx.lifecycle.ViewModel
import com.ajiswn.dicodingevent.data.EventRepository

class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getUpcomingEvent() = eventRepository.getUpcomingEvent()
}