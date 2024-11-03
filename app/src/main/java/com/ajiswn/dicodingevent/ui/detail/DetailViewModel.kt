package com.ajiswn.dicodingevent.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajiswn.dicodingevent.data.EventRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getDetailEvent(id: Int) = eventRepository.getDetailEvent(id)

    fun setFavoriteEvent(id: Int) {
        viewModelScope.launch {
            eventRepository.setFavoriteEvent(id, true)
        }
    }

    fun unsetFavoriteEvent(id: Int) {
        viewModelScope.launch {
            eventRepository.setFavoriteEvent(id, false)
        }
    }
}