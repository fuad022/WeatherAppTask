package com.example.weatherapptask.ui.forecastreport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.weatherapptask.R
import com.example.weatherapptask.data.remote.other.Constants
import com.example.weatherapptask.data.remote.other.Constants.Companion.API_KEY
import com.example.weatherapptask.data.remote.other.Constants.Companion.EXCLUDE_CURRENT
import com.example.weatherapptask.data.remote.other.Constants.Companion.UNITS
import com.example.weatherapptask.databinding.FragmentForecastReportBinding
import com.example.weatherapptask.ui.forecastreport.adapter.DailyForecastAdapter
import com.example.weatherapptask.ui.forecastreport.viewmodel.DailyForecastVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForecastReportFragment : Fragment() {
    private val binding by lazy { FragmentForecastReportBinding.inflate(layoutInflater) }
    private val args: ForecastReportFragmentArgs by navArgs()
    private val dailyForecastVM: DailyForecastVM by viewModel()
    private val dailyForecastAdapter = DailyForecastAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
        observeDailyForecast()
        return binding.root
    }

    private fun init() {
        binding.city.text = args.locationModel.cityName
        dailyForecastVM.sendData(
            args.locationModel.cityCoordinate.lat.toString(),
            args.locationModel.cityCoordinate.lon.toString(), UNITS, EXCLUDE_CURRENT, API_KEY
        )
    }

    private fun observeDailyForecast() {
        dailyForecastVM.dailyForecastData.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    dailyForecastAdapter.submitList(it.daily.toMutableList())
                }
            }
        })
        binding.dailyRv.adapter = dailyForecastAdapter
    }
}