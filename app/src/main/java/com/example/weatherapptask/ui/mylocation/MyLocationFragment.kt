package com.example.weatherapptask.ui.mylocation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapptask.data.remote.other.Constants.Companion.API_KEY
import com.example.weatherapptask.data.remote.other.Constants.Companion.BAKU_CITY
import com.example.weatherapptask.data.remote.other.Constants.Companion.EXCLUDE
import com.example.weatherapptask.data.remote.other.Constants.Companion.LAT
import com.example.weatherapptask.data.remote.other.Constants.Companion.LON
import com.example.weatherapptask.data.remote.other.Constants.Companion.UNITS
import com.example.weatherapptask.databinding.FragmentMyLocationBinding
import com.example.weatherapptask.ui.mylocation.adapter.HourlyForecastAdapter
import com.example.weatherapptask.ui.mylocation.viewmodel.HourlyForecastVM
import com.example.weatherapptask.ui.mylocation.viewmodel.LocationForecastVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyLocationFragment : Fragment() {
    private val binding by lazy { FragmentMyLocationBinding.inflate(layoutInflater) }
    private val locationForecastVM: LocationForecastVM by viewModel()
    private val hourlyForecastVM: HourlyForecastVM by viewModel()
    private val hourlyForecastAdapter = HourlyForecastAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
        observeForecast()
        observeHourlyForecast()
        return binding.root
    }

    private fun init() {
        locationForecastVM.sendData(BAKU_CITY, UNITS, API_KEY)
        hourlyForecastVM.sendData(LAT, LON, UNITS, EXCLUDE, API_KEY)
    }

    private fun observeForecast() {
        locationForecastVM.locationForecastData.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    binding.city.text = it.cityName
                    binding.date.text = it.dateTime.toString()
                    binding.temperature.text = it.temperatureInfo.temp.toString()
                    binding.tempNum.text = it.temperatureInfo.temp.toString()
                    binding.humidyNum.text = it.temperatureInfo.humidity.toString()
                    binding.windNum.text = it.wind.speed.toString()
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


















