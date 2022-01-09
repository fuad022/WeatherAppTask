package com.example.weatherapptask.ui.forecastreport.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.weatherapptask.data.remote.model.DailyForecastModel
import com.example.weatherapptask.data.remote.other.NetworkResult
import com.example.weatherapptask.data.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class DailyForecastVM(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _dailyForecastData = MutableLiveData<NetworkResult<DailyForecastModel>>()
    val dailyForecastData: LiveData<NetworkResult<DailyForecastModel>> get() = _dailyForecastData

    fun sendData(lat: String, lon: String, units: String, exclude: String, appid: String) =
        viewModelScope.launch {
            getDailyForecastSafeCall(lat, lon, units, exclude, appid)
        }

    private suspend fun getDailyForecastSafeCall(lat: String, lon: String, units: String, exclude: String, appid: String) {
        _dailyForecastData.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.getDailyForecast(lat, lon, units, exclude, appid)
                _dailyForecastData.value = handleDailyForecastResponse(response)
            } catch (e: Exception) {
                _dailyForecastData.value = NetworkResult.Error("Daily Forecast not found.")
            }
        } else {
            _dailyForecastData.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleDailyForecastResponse(response: Response<DailyForecastModel>): NetworkResult<DailyForecastModel>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.daily.isNullOrEmpty() -> {
                return NetworkResult.Error("Daily Forecast not found.")
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