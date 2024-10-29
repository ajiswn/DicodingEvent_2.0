package com.ajiswn.dicodingevent.ui.finished

import androidx.lifecycle.ViewModel
import com.ajiswn.dicodingevent.data.EventRepository

class FinishedViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getFinishedEvent() = eventRepository.getFinishedEvent()
}