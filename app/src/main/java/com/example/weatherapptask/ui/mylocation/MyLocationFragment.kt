package com.example.weatherapptask.ui.mylocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherapptask.data.remote.other.Constants.Companion.API_KEY
import com.example.weatherapptask.data.remote.other.Constants.Companion.EXCLUDE
import com.example.weatherapptask.data.remote.other.Constants.Companion.UNITS
import com.example.weatherapptask.databinding.FragmentMyLocationBinding
import com.example.weatherapptask.ui.mylocation.adapter.HourlyForecastAdapter
import com.example.weatherapptask.ui.mylocation.viewmodel.HourlyForecastVM
import com.example.weatherapptask.data.remote.LocationForecastVM
import com.example.weatherapptask.data.remote.other.Constants.Companion.PERMISSION_ID
import com.example.weatherapptask.data.remote.other.NetworkResult
import com.example.weatherapptask.util.Util.convertDate
import com.example.weatherapptask.util.Util.displayToast
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
    private lateinit var locationRequest: LocationRequest
    var toast: Toast? = null

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
                    val a = database[0].hourlyForecastModel.hourly.toMutableList()
                    Log.d("readHourlyForecastDatabase", a.toString())
                    database[0].hourlyForecastModel.let {
                        hourlyForecastAdapter.submitList(it.hourly.toMutableList())
                    }
                } else {
                    observeHourlyForecast()
                }
            })
            binding.hourlyTempRv.adapter = hourlyForecastAdapter
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

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        getNewLocation()
                    } else {
                        initForecastData(location.latitude.toString(), location.longitude.toString())
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please, enable your location service", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
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
        override fun onLocationResult(result: LocationResult) {
            val lastLocation = result.lastLocation
            initForecastData(lastLocation.latitude.toString(), lastLocation.longitude.toString())
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_ID
        )
    }

    private fun initForecastData(lat: String, lon: String) {
        locationForecastVM.sendData(lat, lon, UNITS, API_KEY)
        hourlyForecastVM.sendData(lat, lon, UNITS, EXCLUDE, API_KEY)
        observeForecast()
    }

    ///////////////////////////////New check permission

    private fun observeForecast() {
        locationForecastVM.locationForecastData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
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
                    val a = database[0].hourlyForecastModel.hourly.toMutableList()
                    Log.d("loadHourlyForecastDataFromCache", a.toString())
                    database[0].hourlyForecastModel.let {
                        hourlyForecastAdapter.submitList(it.hourly.toMutableList())
                    }
//                    hourlyForecastAdapter.submitList(database[0].hourlyForecastModel.hourly.toMutableList())
                }
            })
            binding.hourlyTempRv.adapter = hourlyForecastAdapter
        }
    }

    override fun onPause() {
        if (toast != null) toast!!.cancel()
        super.onPause()
    }
}