package com.example.weatherapptask.ui.mylocation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapptask.data.remote.model.HourlyForecastModel
import com.example.weatherapptask.data.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class HourlyForecastVM(private val repository: Repository) : ViewModel() {
    private val _hourlyForecastData = MutableLiveData<Response<HourlyForecastModel>>()
    val hourlyForecastData: LiveData<Response<HourlyForecastModel>> get() = _hourlyForecastData

    fun sendData(lat: String, lon: String, units: String, exclude: String, appid: String) =
        viewModelScope.launch {
            _hourlyForecastData.value =
                repository.getHourlyForecast(lat, lon, units, exclude, appid)
        }
}