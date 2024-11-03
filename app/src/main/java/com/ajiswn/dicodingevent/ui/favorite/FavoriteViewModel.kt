package com.ajiswn.dicodingevent.ui.favorite

import androidx.lifecycle.ViewModel
import com.ajiswn.dicodingevent.data.EventRepository

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getFavoriteEvent() = eventRepository.getFavoriteEvent()
}