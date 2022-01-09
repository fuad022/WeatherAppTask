package com.example.weatherapptask.data.remote

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.weatherapptask.data.database.FavoritesEntity
import com.example.weatherapptask.data.remote.model.LocationModel
import com.example.weatherapptask.data.remote.other.NetworkResult
import com.example.weatherapptask.data.repo.MainRepository
import com.example.weatherapptask.data.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class LocationForecastVM(
    private val mainRepository: MainRepository,
    application: Application
) : AndroidViewModel(application) {

    /** ROOM DATABASE */
    val readLocationForecast: LiveData<LocationModel> = mainRepository.local.readLocationForecast().asLiveData()
    // New changes
    val readFavoriteForecasts: LiveData<List<FavoritesEntity>> = mainRepository.local.readFavoriteForecasts().asLiveData()
    // New changes

    private fun insertLocationForecast(locationModel: LocationModel) =
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.local.insertLocationForecast(locationModel)
        }

    // New changes
    fun insertFavoriteForecast(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.local.insertFavoriteForecast(favoritesEntity)
        }

    private fun deleteFavoriteForecast(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.local.deleteFavoriteForecast(favoritesEntity)
        }

    private fun deleteAllFavoriteForecasts() =
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.local.deleteAllFavoriteForecasts()
        }
    // New changes

    /** RETROFIT */
    private val _locationForecastData = MutableLiveData<NetworkResult<LocationModel>>()
    val locationForecastData: LiveData<NetworkResult<LocationModel>> get() = _locationForecastData

    fun sendData(lat: String, lon: String, units: String, apiKey: String) =
        viewModelScope.launch {
            getCurrentForecastSafeCall(lat, lon, units, apiKey)
        }

    private suspend fun getCurrentForecastSafeCall(lat: String, lon: String, units: String, apiKey: String) {
        _locationForecastData.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = mainRepository.remote.getCurrentForecastByCoordinates(lat, lon, units, apiKey)
                _locationForecastData.value = handleCurrentForecastResponse(response)

                val locationForecast = _locationForecastData.value!!.data
                if (locationForecast != null) {
                    offlineCacheLocationForecast(locationForecast)
                }
            } catch (e: Exception) {
                _locationForecastData.value = NetworkResult.Error("Location Forecast not found.")
            }
        } else {
            _locationForecastData.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheLocationForecast(locationForecast: LocationModel) {
        insertLocationForecast(locationForecast)
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