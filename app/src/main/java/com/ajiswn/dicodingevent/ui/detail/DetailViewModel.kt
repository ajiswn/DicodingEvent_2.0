package com.ajiswn.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ajiswn.dicodingevent.data.remote.response.DetailEventResponse
import com.ajiswn.dicodingevent.data.remote.response.Event
import com.ajiswn.dicodingevent.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _eventDetail = MutableLiveData<Event?>()
    val eventDetail: LiveData<Event?> get() = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun getDetailEvent(id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailEvent(id)
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _eventDetail.value = response.body()?.event
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Failed to load detail events: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Failed to load detail events: ${t.message}"
            }
        })
    }
}