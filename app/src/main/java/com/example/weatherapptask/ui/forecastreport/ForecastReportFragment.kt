package com.example.weatherapptask.ui.forecastreport

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
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
import com.example.weatherapptask.util.observeOnce
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception

class ForecastReportFragment : Fragment() {
    private val binding by lazy { FragmentForecastReportBinding.inflate(layoutInflater) }
    private val args: ForecastReportFragmentArgs by navArgs()
    private val dailyForecastVM: DailyForecastVM by viewModel()
    private val locationForecastVM: LocationForecastVM by viewModel() // New changes
    private val dailyForecastAdapter = DailyForecastAdapter()
    var toast: Toast? = null
    private var forecastSaved = false
    private var savedForecastId = 0
    private lateinit var menuItem: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        init()
//        observeDailyForecast()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.report_menu, menu)
        menuItem = menu.findItem(R.id.save_to_favorites_menu)
        checkSavedForecasts(menuItem)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_to_favorites_menu && !forecastSaved) {
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_to_favorites_menu && forecastSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        binding.city.text = args.favoritesEntity.cityName

        binding.date.text = convertDate(args.favoritesEntity.dateTime.toString(), "1")
        binding.weather.text = args.favoritesEntity.weather[0].currentWeather
        binding.img.setAnimation(getWeatherAnimation(args.favoritesEntity.weather[0].icon))
        binding.img.playAnimation()
        binding.temperature.text = getWholeNum(args.favoritesEntity.temperatureInfo.temp).plus("°c")
        binding.tempNum.text = getWholeNum(args.favoritesEntity.temperatureInfo.temp).plus("°c")
        binding.humidyNum.text = args.favoritesEntity.temperatureInfo.humidity.toString().plus("%")
        binding.windNum.text = getWholeNum(args.favoritesEntity.wind.speed).plus("m/sec")

        /*
//        checkSavedForecasts()

//        binding.favoriteIcon.setOnClickListener {
//            if (!forecastSaved) {
//                saveToFavorites()
//            } else if (forecastSaved) {
//                removeFromFavorites()
//            }
////            when {
////                !forecastSaved -> saveToFavorites()
////                forecastSaved ->  removeFromFavorites()
////            }
//        }
        */

        /*
        dailyForecastVM.sendData(
            args.locationModel.cityCoordinate.lat.toString(),
            args.locationModel.cityCoordinate.lon.toString(), UNITS, EXCLUDE_CURRENT, API_KEY
        )*/
    }

    private fun checkSavedForecasts(menuItem: MenuItem) {
        locationForecastVM.readFavoriteForecasts.observe(this, { favoritesEntity ->
            try {
                for (savedForecast in favoritesEntity) {
                    if (savedForecast.id == args.favoritesEntity.id) {
                        changeFavoriteIconColor(menuItem, R.color.yelow)
                        savedForecastId = savedForecast.id
                        forecastSaved = true
                    }
                }
            } catch (e: Exception) {
                Log.d("ForecastReportFragment", e.message.toString())
            }
        })
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(
            args.favoritesEntity.id,
            args.favoritesEntity.cityCoordinate,
            args.favoritesEntity.dateTime,
            args.favoritesEntity.temperatureInfo,
            args.favoritesEntity.cityName,
            args.favoritesEntity.weather,
            args.favoritesEntity.wind
        )
        locationForecastVM.insertFavoriteForecast(favoritesEntity)
        changeFavoriteIconColor(item, R.color.yelow)
        showSnackBar("Forecast saved.")
        forecastSaved = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(
            savedForecastId,
            args.favoritesEntity.cityCoordinate,
            args.favoritesEntity.dateTime,
            args.favoritesEntity.temperatureInfo,
            args.favoritesEntity.cityName,
            args.favoritesEntity.weather,
            args.favoritesEntity.wind
        )
        locationForecastVM.deleteFavoriteForecast(favoritesEntity)
        changeFavoriteIconColor(item, R.color.white)
        showSnackBar("Removed from Favorites.")
        forecastSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.reportLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("OK") {}.show()
    }

    private fun changeFavoriteIconColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(requireContext(), color))
    }

    override fun onPause() {
        if (toast != null) toast!!.cancel()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
//        changeFavoriteIconColor(R.color.white)
        changeFavoriteIconColor(menuItem, R.color.white)
    }

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

    /*

//    private fun checkSavedForecasts() {
//        locationForecastVM.readFavoriteForecasts.observe(this, { favoritesEntity ->
//            try {
//                for (savedForecast in favoritesEntity) {
//                    if (savedForecast.id == args.favoritesEntity.id) {
//                        changeFavoriteIconColor(R.color.yelow)
//                        savedForecastId = savedForecast.id
//                        forecastSaved = true
//                    }
//                }
//            } catch (e: Exception) {
//                Log.d("ForecastReportFragment", e.message.toString())
//            }
//        })
//    }

//    private fun saveToFavorites() {
//        val favoritesEntity = FavoritesEntity(
//            args.favoritesEntity.id,
//            args.favoritesEntity.cityCoordinate,
//            args.favoritesEntity.dateTime,
//            args.favoritesEntity.temperatureInfo,
//            args.favoritesEntity.cityName,
//            args.favoritesEntity.weather,
//            args.favoritesEntity.wind
//        )
//        locationForecastVM.insertFavoriteForecast(favoritesEntity)
//        changeFavoriteIconColor(R.color.yelow)
//        showSnackBar("Forecast saved.")
//        forecastSaved = true
//    }

//    private fun removeFromFavorites() {
//        val favoritesEntity = FavoritesEntity(
//            savedForecastId,
//            args.favoritesEntity.cityCoordinate,
//            args.favoritesEntity.dateTime,
//            args.favoritesEntity.temperatureInfo,
//            args.favoritesEntity.cityName,
//            args.favoritesEntity.weather,
//            args.favoritesEntity.wind
//        )
//        locationForecastVM.deleteFavoriteForecast(favoritesEntity)
//        changeFavoriteIconColor(R.color.white)
//        showSnackBar("Removed from Favorites.")
//        forecastSaved = false
//    }

//    private fun changeFavoriteIconColor(color: Int) {
//        binding.favoriteIcon.setColorFilter(
//            ContextCompat.getColor(requireContext(), color),
//            android.graphics.PorterDuff.Mode.MULTIPLY
//        )
//    }

     */
}