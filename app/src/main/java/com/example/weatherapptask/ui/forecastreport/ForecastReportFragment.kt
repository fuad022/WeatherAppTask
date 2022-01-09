package com.example.weatherapptask.ui.forecastreport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.example.weatherapptask.R
import com.example.weatherapptask.data.database.FavoritesEntity
import com.example.weatherapptask.data.remote.LocationForecastVM
import com.example.weatherapptask.data.remote.other.Constants.Companion.API_KEY
import com.example.weatherapptask.data.remote.other.Constants.Companion.EXCLUDE_CURRENT
import com.example.weatherapptask.data.remote.other.Constants.Companion.UNITS
import com.example.weatherapptask.data.remote.other.NetworkResult
import com.example.weatherapptask.databinding.FragmentForecastReportBinding
import com.example.weatherapptask.ui.forecastreport.adapter.DailyForecastAdapter
import com.example.weatherapptask.ui.forecastreport.viewmodel.DailyForecastVM
import com.example.weatherapptask.util.Util.convertDate
import com.example.weatherapptask.util.Util.displayToast
import com.example.weatherapptask.util.Util.getWeatherAnimation
import com.example.weatherapptask.util.Util.getWholeNum
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForecastReportFragment : Fragment() {
    private val binding by lazy { FragmentForecastReportBinding.inflate(layoutInflater) }
    private val args: ForecastReportFragmentArgs by navArgs()
    private val dailyForecastVM: DailyForecastVM by viewModel()
    private val locationForecastVM: LocationForecastVM by viewModel() // New changes
    private val dailyForecastAdapter = DailyForecastAdapter()
    var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
//        observeDailyForecast()
        return binding.root
    }

    private fun init() {
        binding.city.text = args.locationModel.cityName

        // New changes
        binding.date.text = convertDate(args.locationModel.dateTime.toString(), "1")
        binding.weather.text = args.locationModel.weather[0].currentWeather
        binding.img.setAnimation(getWeatherAnimation(args.locationModel.weather[0].icon))
        binding.img.playAnimation()
        binding.temperature.text = getWholeNum(args.locationModel.temperatureInfo.temp).plus("°c")
        binding.tempNum.text = getWholeNum(args.locationModel.temperatureInfo.temp).plus("°c")
        binding.humidyNum.text = args.locationModel.temperatureInfo.humidity.toString().plus("%")
        binding.windNum.text = getWholeNum(args.locationModel.wind.speed).plus("m/sec")

        binding.favoriteIcon.setOnClickListener {
            saveToFavorites()
        }
        // New changes

        /*
        dailyForecastVM.sendData(
            args.locationModel.cityCoordinate.lat.toString(),
            args.locationModel.cityCoordinate.lon.toString(), UNITS, EXCLUDE_CURRENT, API_KEY
        )*/
    }

    // New changes
    private fun saveToFavorites() {
        val favoritesEntity = FavoritesEntity(
            args.locationModel.id,
            args.locationModel.cityCoordinate,
            args.locationModel.dateTime,
            args.locationModel.temperatureInfo,
            args.locationModel.cityName,
            args.locationModel.weather,
            args.locationModel.wind
        )
        locationForecastVM.insertFavoriteForecast(favoritesEntity)
        changeFavoriteIconColor(R.color.yelow)
        showSnackBar("Forecast saved.")
    }
    // New changes

    // New changes
    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.reportLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}.show()
    }
    // New changes

    // New changes
    private fun changeFavoriteIconColor(color: Int) {
        binding.favoriteIcon.setColorFilter(
            ContextCompat.getColor(requireContext(), color),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )
    }
    // New changes

    /*
    private fun observeDailyForecast() {
        dailyForecastVM.dailyForecastData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        dailyForecastAdapter.submitList(it.daily.toMutableList())
                    }
                }
                is NetworkResult.Error -> {
                    displayToast(response.message.toString(), requireContext())
                }
            }
        })
        binding.dailyRv.adapter = dailyForecastAdapter
    }*/

    override fun onPause() {
        if (toast != null) toast!!.cancel()
        super.onPause()
    }
}