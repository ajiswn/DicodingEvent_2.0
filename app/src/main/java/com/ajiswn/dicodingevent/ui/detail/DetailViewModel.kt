package com.ajiswn.dicodingevent.ui.detail

import androidx.lifecycle.ViewModel
import com.ajiswn.dicodingevent.data.EventRepository

class DetailViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getDetailEvent(id: Int) = eventRepository.getDetailEvent(id)


}