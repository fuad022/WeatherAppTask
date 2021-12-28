package com.example.weatherapptask.data.remote

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.weatherapptask.data.remote.model.LocationModel
import com.example.weatherapptask.data.remote.other.NetworkResult
import com.example.weatherapptask.data.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class LocationForecastVM(private val repository: Repository, application: Application) : AndroidViewModel(application) {

    private val _locationForecastData = MutableLiveData<NetworkResult<LocationModel>>()
    val locationForecastData: LiveData<NetworkResult<LocationModel>> get() = _locationForecastData

    fun sendData(city: String, units: String, apiKey: String) =
        viewModelScope.launch {
            getCurrentForecastSafeCall(city, units, apiKey)
        }

    private suspend fun getCurrentForecastSafeCall(city: String, units: String, apiKey: String) {
        _locationForecastData.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.getCurrentForecast(city, units, apiKey)
                _locationForecastData.value = handleCurrentForecastResponse(response)
            } catch (e: Exception) {
                _locationForecastData.value = NetworkResult.Error("Location Forecast not found.")
            }
        } else {
            _locationForecastData.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleCurrentForecastResponse(response: Response<LocationModel>): NetworkResult<LocationModel>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body() == null -> {
                return NetworkResult.Error("Location Forecast not found.")
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