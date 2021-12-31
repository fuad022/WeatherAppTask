package com.example.weatherapptask.ui.search.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapptask.data.remote.model.LocationModel
import com.example.weatherapptask.data.remote.other.NetworkResult
import com.example.weatherapptask.data.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class SearchForecastVM(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _searchForecastData = MutableLiveData<NetworkResult<LocationModel>>()
    val searchForecastData: LiveData<NetworkResult<LocationModel>> get() = _searchForecastData

    fun sendData(cityName: String, units: String, appid: String) =
        viewModelScope.launch {
            getDailyForecastSafeCall(cityName, units, appid)
        }

    private suspend fun getDailyForecastSafeCall(cityName: String, units: String, appid: String) {
        _searchForecastData.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.getCurrentForecast(cityName, units, appid)
                _searchForecastData.value = handleSearchForecastResponse(response)
            } catch (e: Exception) {
                _searchForecastData.value = NetworkResult.Error("Forecast by search not found.")
            }
        } else {
            _searchForecastData.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleSearchForecastResponse(response: Response<LocationModel>): NetworkResult<LocationModel>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body() == null -> {
                return NetworkResult.Error("Forecast by search not found.")
            }
            response.isSuccessful -> {
                val forecast = response.body()
                return NetworkResult.Success(forecast!!)
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