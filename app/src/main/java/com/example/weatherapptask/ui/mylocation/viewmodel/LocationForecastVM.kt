package com.example.weatherapptask.ui.mylocation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapptask.data.remote.model.LocationModel
import com.example.weatherapptask.data.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class LocationForecastVM(private val repository: Repository) : ViewModel() {
    private val _locationForecastData = MutableLiveData<Response<LocationModel>>()
    val locationForecastData: LiveData<Response<LocationModel>> get() = _locationForecastData

//    suspend fun sendData(city: String, units: String, apiKey: String) {
//        _locationForecastData.value = repository.getCurrentForecast(city, units, apiKey)
//    }

    fun sendData(city: String, units: String, apiKey: String) =
        viewModelScope.launch {
            _locationForecastData.value = repository.getCurrentForecast(city, units, apiKey)
        }
}