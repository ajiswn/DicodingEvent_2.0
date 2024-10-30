package com.ajiswn.dicodingevent.ui.search

import androidx.lifecycle.ViewModel
import com.ajiswn.dicodingevent.data.EventRepository

class SearchViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun searchEvent(keyword : String) = eventRepository.searchEvent(keyword)
}
