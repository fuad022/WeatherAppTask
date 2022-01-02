package com.example.weatherapptask.ui.mylocation

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import com.example.weatherapptask.data.remote.other.Constants.Companion.API_KEY
import com.example.weatherapptask.data.remote.other.Constants.Companion.EXCLUDE
import com.example.weatherapptask.data.remote.other.Constants.Companion.UNITS
import com.example.weatherapptask.databinding.FragmentMyLocationBinding
import com.example.weatherapptask.ui.mylocation.adapter.HourlyForecastAdapter
import com.example.weatherapptask.ui.mylocation.viewmodel.HourlyForecastVM
import com.example.weatherapptask.data.remote.LocationForecastVM
import com.example.weatherapptask.data.remote.other.NetworkResult
import com.example.weatherapptask.util.Util.convertDate
import com.example.weatherapptask.util.Util.displayToast
import com.example.weatherapptask.util.Util.getCityName
import com.example.weatherapptask.util.Util.getCountryName
import com.example.weatherapptask.util.Util.getWeatherAnimation
import com.example.weatherapptask.util.Util.getWholeNum
import com.example.weatherapptask.util.observeOnce
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyLocationFragment : Fragment() {

    private val binding by lazy { FragmentMyLocationBinding.inflate(layoutInflater) }
    private val locationForecastVM: LocationForecastVM by viewModel()
    private val hourlyForecastVM: HourlyForecastVM by viewModel()
    private val hourlyForecastAdapter = HourlyForecastAdapter()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var toast: Toast? = null

    private var PERMISSION_ID = 52 //
    private lateinit var locationRequest: LocationRequest //

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        readDatabase()
        swipe()
        return binding.root
    }

    private fun swipe() {
        binding.swipe.setOnRefreshListener {
            //fetchLocation()///
            getLastLocation()
            binding.swipe.isRefreshing = false
        }
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            locationForecastVM.readLocationForecast.observeOnce(viewLifecycleOwner, {
                if (it != null) {
                    binding.city.text = it.cityName
                    binding.date.text = convertDate(it.dateTime.toString(), "1")
                    binding.weather.text = it.weather[0].currentWeather
                    binding.img.setAnimation(getWeatherAnimation(it.weather[0].icon))
                    binding.img.playAnimation()
                    binding.temperature.text = getWholeNum(it.temperatureInfo.temp).plus("°c")
                    binding.tempNum.text = getWholeNum(it.temperatureInfo.temp).plus("°c")
                    binding.humidyNum.text = it.temperatureInfo.humidity.toString().plus("%")
                    binding.windNum.text = getWholeNum(it.wind.speed).plus("m/sec")
                } else {
                    //fetchLocation()////
                    getLastLocation()
                }
                readHourlyForecastDatabase()
            })
        }
    }

    private fun readHourlyForecastDatabase() {
        lifecycleScope.launch {
            hourlyForecastVM.readHourlyForecast.observeOnce(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    var a = database[0].hourlyForecastModel.hourly.toMutableList()
                    Log.d("readHourlyForecastDatabase", a.toString())
                    hourlyForecastAdapter.submitList(database[0].hourlyForecastModel.hourly.toMutableList())
                } else {
                    observeHourlyForecast()
                }
            })
        }
    }

    /*
    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
//            locationForecastVM.sendData(getCityName(it.latitude,it.longitude, requireContext()), UNITS, API_KEY)
            locationForecastVM.sendData(it.latitude.toString(), it.longitude.toString(), UNITS, API_KEY)
            hourlyForecastVM.sendData(it.latitude.toString(), it.longitude.toString(), UNITS, EXCLUDE, API_KEY)
//            observeForecast(getCityName(it.latitude,it.longitude, requireContext()), getCountryName(it.latitude,it.longitude, requireContext()))
            observeForecast()
        }
    }*/

    ///////////////////////////////New check permission

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug", "You have the permission")
            }
        }
    }

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location = task.result
                    if (location == null) {
                        getNewLocation()
                    } else {
                        locationForecastVM.sendData(location.latitude.toString(), location.longitude.toString(), UNITS, API_KEY)
                        hourlyForecastVM.sendData(location.latitude.toString(), location.longitude.toString(), UNITS, EXCLUDE, API_KEY)

                        observeForecast()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please, enable your location service", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocation() {
        locationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 2
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()!!
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation = p0.lastLocation
            locationForecastVM.sendData(lastLocation.latitude.toString(), lastLocation.longitude.toString(), UNITS, API_KEY)
            hourlyForecastVM.sendData(lastLocation.latitude.toString(), lastLocation.longitude.toString(), UNITS, EXCLUDE, API_KEY)

            observeForecast()
        }
    }

    ///////////////////////////////New check permission

//    private fun observeForecast(cityName: String, countryName: String) {
    private fun observeForecast() {
        locationForecastVM.locationForecastData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
//                        binding.city.text = cityName
//                        binding.country.text = countryName
                        binding.city.text = it.cityName
                        binding.date.text = convertDate(it.dateTime.toString(), "1")
                        binding.weather.text = it.weather[0].currentWeather
                        binding.img.setAnimation(getWeatherAnimation(it.weather[0].icon))
                        binding.img.playAnimation()
                        binding.temperature.text = getWholeNum(it.temperatureInfo.temp).plus("°c")
                        binding.tempNum.text = getWholeNum(it.temperatureInfo.temp).plus("°c")
                        binding.humidyNum.text = it.temperatureInfo.humidity.toString().plus("%")
                        binding.windNum.text = getWholeNum(it.wind.speed).plus("m/sec")
//                        binding.executePendingBindings()
                    }
                    observeHourlyForecast()
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    displayToast(response.message.toString(), requireContext())
                }
            }
        })
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            locationForecastVM.readLocationForecast.observe(viewLifecycleOwner, {
                if (it != null) {
                    binding.city.text = it.cityName
                    binding.date.text = convertDate(it.dateTime.toString(), "1")
                    binding.weather.text = it.weather[0].currentWeather
                    binding.img.setAnimation(getWeatherAnimation(it.weather[0].icon))
                    binding.img.playAnimation()
                    binding.temperature.text = getWholeNum(it.temperatureInfo.temp).plus("°c")
                    binding.tempNum.text = getWholeNum(it.temperatureInfo.temp).plus("°c")
                    binding.humidyNum.text = it.temperatureInfo.humidity.toString().plus("%")
                    binding.windNum.text = getWholeNum(it.wind.speed).plus("m/sec")
                }
            })
        }
    }

    private fun observeHourlyForecast() {
        hourlyForecastVM.hourlyForecastData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        hourlyForecastAdapter.submitList(it.hourly.toMutableList())
                    }
                }
                is NetworkResult.Error -> {
                    loadHourlyForecastDataFromCache()
                    displayToast(response.message.toString(), requireContext())
                }
            }
        })
        binding.hourlyTempRv.adapter = hourlyForecastAdapter
    }

    private fun loadHourlyForecastDataFromCache() {
        lifecycleScope.launch {
            hourlyForecastVM.readHourlyForecast.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    hourlyForecastAdapter.submitList(database[0].hourlyForecastModel.hourly.toMutableList())
                }
            })
        }
    }

    override fun onPause() {
        if (toast != null) toast!!.cancel()
        super.onPause()
    }
}