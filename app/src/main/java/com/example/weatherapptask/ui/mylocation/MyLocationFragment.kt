package com.example.weatherapptask.ui.mylocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
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
import com.example.weatherapptask.data.remote.other.Constants.Companion.API_KEY
import com.example.weatherapptask.data.remote.other.Constants.Companion.BAKU_CITY
import com.example.weatherapptask.data.remote.other.Constants.Companion.EXCLUDE
import com.example.weatherapptask.data.remote.other.Constants.Companion.LAT
import com.example.weatherapptask.data.remote.other.Constants.Companion.LON
import com.example.weatherapptask.data.remote.other.Constants.Companion.UNITS
import com.example.weatherapptask.databinding.FragmentMyLocationBinding
import com.example.weatherapptask.ui.mylocation.adapter.HourlyForecastAdapter
import com.example.weatherapptask.ui.mylocation.viewmodel.HourlyForecastVM
import com.example.weatherapptask.data.remote.LocationForecastVM
import com.example.weatherapptask.util.Util.convertDate
import com.example.weatherapptask.util.Util.getWeatherAnimation
import com.example.weatherapptask.util.Util.getWholeNum
import com.google.android.gms.location.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MyLocationFragment : Fragment() {
    private val binding by lazy { FragmentMyLocationBinding.inflate(layoutInflater) }
    private val locationForecastVM: LocationForecastVM by viewModel()
    private val hourlyForecastVM: HourlyForecastVM by viewModel()
    private val hourlyForecastAdapter = HourlyForecastAdapter()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest
    private var PERMISSION_ID = 52

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
        observeHourlyForecast()
        return binding.root
    }

    private fun init() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocation()
    }

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                locationForecastVM.sendData(getCityName(it.latitude,it.longitude), UNITS, API_KEY)
                hourlyForecastVM.sendData(it.latitude.toString(), it.longitude.toString(), UNITS, EXCLUDE, API_KEY)
                observeForecast(getCityName(it.latitude,it.longitude), getCountryName(it.latitude,it.longitude))
            }
        }
    }

    private fun getCityName(lat: Double, long: Double): String {
        var geoCoder = Geocoder(requireContext(), Locale.US)
        var address = geoCoder.getFromLocation(lat, long, 1)
        return address.get(0).locality
    }

    private fun getCountryName(lat: Double, long: Double): String {
        var geoCoder = Geocoder(requireContext(), Locale.US)
        var address = geoCoder.getFromLocation(lat, long, 1)
        return address.get(0).countryName
    }

/*
    //////////////////////////////////

    private fun getLastLocation(): ArrayList<String> {
        val result = arrayListOf<String>()
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location = task.result
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude
                        Log.d("latitude", latitude.toString())
                        Log.d("longitude", longitude.toString())

//                        result.addAll(listOf(latitude.toString(), longitude.toString()))
//                        listOf(latitude.toString(), longitude.toString()).forEach {
//                            result.add(it)
//                        }
//                        Log.d("result", result.toString())

                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please, enable your location service", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission()
        }

        return result
    }

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

    /*
        @SuppressLint("MissingPermission")
    private fun getNewLocation() {
        locationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 2
        }
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()!!
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val result = arrayListOf<String>()
            var lastLocation = p0.lastLocation
//            listOf(lastLocation.latitude, lastLocation.longitude)
            latitude = lastLocation.latitude
            longitude = lastLocation.longitude
            listOf(latitude.toString(), longitude.toString()).forEach {
                result.add(it)
            }
        }
    }*/

    //////////////////////////////////

 */

    private fun observeForecast(cityName: String, countryName: String) {
        locationForecastVM.locationForecastData.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    binding.city.text = cityName
                    binding.country.text = countryName
                    binding.date.text = convertDate(it.dateTime.toString(), "1")
                    binding.weather.text = it.weather[0].currentWeather
                    binding.img.setAnimation(getWeatherAnimation(it.weather[0].icon))
                    binding.temperature.text = getWholeNum(it.temperatureInfo.temp).plus("°c")
                    binding.tempNum.text = getWholeNum(it.temperatureInfo.temp).plus("°c")
                    binding.humidyNum.text = it.temperatureInfo.humidity.toString().plus("%")
                    binding.windNum.text = getWholeNum(it.wind.speed).plus("m/sec")
                }
            }
        })
    }

    private fun observeHourlyForecast() {
        hourlyForecastVM.hourlyForecastData.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    hourlyForecastAdapter.submitList(it.hourly.toMutableList())
                }
            }
        })
        binding.hourlyTempRv.adapter = hourlyForecastAdapter
    }
}


















