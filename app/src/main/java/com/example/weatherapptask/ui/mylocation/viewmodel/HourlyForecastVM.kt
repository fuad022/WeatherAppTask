package com.example.weatherapptask.ui.mylocation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.weatherapptask.data.remote.model.HourlyForecastModel
import com.example.weatherapptask.data.remote.other.NetworkResult
import com.example.weatherapptask.data.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class HourlyForecastVM(private val repository: Repository, application: Application) : AndroidViewModel(application) {

    private val _hourlyForecastData = MutableLiveData<NetworkResult<HourlyForecastModel>>()
    val hourlyForecastData: LiveData<NetworkResult<HourlyForecastModel>> get() = _hourlyForecastData

    fun sendData(lat: String, lon: String, units: String, exclude: String, appid: String) =
        viewModelScope.launch {
            getHourlyForecastSafeCall(lat, lon, units, exclude, appid)
        }

    private suspend fun getHourlyForecastSafeCall(lat: String, lon: String, units: String, exclude: String, appid: String) {
        _hourlyForecastData.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.getHourlyForecast(lat, lon, units, exclude, appid)
                _hourlyForecastData.value = handleHourlyForecastResponse(response)
            } catch (e: Exception) {
                _hourlyForecastData.value = NetworkResult.Error("Hourly Forecast not found.")
            }
        } else {
            _hourlyForecastData.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleHourlyForecastResponse(response: Response<HourlyForecastModel>): NetworkResult<HourlyForecastModel>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.hourly.isNullOrEmpty() -> {
                return NetworkResult.Error("Hourly Forecast not found.")
            }
            response.isSuccessful -> {
                val locationForecast = response.body()
                return NetworkResult.Success(locationForecast!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}