package com.example.weatherapptask.ui.forecastreport.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapptask.data.remote.model.DailyForecastModel
import com.example.weatherapptask.data.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class DailyForecastVM(private val repository: Repository) : ViewModel() {
    private val _dailyForecastData = MutableLiveData<Response<DailyForecastModel>>()
    val dailyForecastData: LiveData<Response<DailyForecastModel>> get() = _dailyForecastData

    fun sendData(lat: String, lon: String, units: String, exclude: String, appid: String) =
        viewModelScope.launch {
            _dailyForecastData.value = repository.getDailyForecast(lat, lon, units, exclude, appid)
        }
}