package com.ajiswn.dicodingevent.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ajiswn.dicodingevent.data.remote.response.EventResponse
import com.ajiswn.dicodingevent.data.remote.response.ListEventsItem
import com.ajiswn.dicodingevent.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<List<ListEventsItem>>()
    val searchResults: LiveData<List<ListEventsItem>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun searchEvent(keyword: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchEvent(keyword = keyword)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _searchResults.value = response.body()?.listEvents ?: emptyList()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Failed to load upcoming events: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Failed to load upcoming events: ${t.message}"
            }
        })
    }
}
