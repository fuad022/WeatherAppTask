package com.example.weatherapptask.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.example.weatherapptask.data.remote.LocationForecastVM
import com.example.weatherapptask.data.remote.other.Constants.Companion.API_KEY
import com.example.weatherapptask.data.remote.other.Constants.Companion.UNITS
import com.example.weatherapptask.data.remote.other.NetworkResult
import com.example.weatherapptask.databinding.FragmentSearchBinding
import com.example.weatherapptask.ui.search.viewmodel.SearchForecastVM
import com.example.weatherapptask.util.Util.displayToast
import com.example.weatherapptask.util.Util.getCityName
import com.example.weatherapptask.util.Util.getWeatherAnimation
import com.example.weatherapptask.util.Util.getWholeNum
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private val locationForecastVM: LocationForecastVM by viewModel()
    private val searchForecastVM: SearchForecastVM by viewModel()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        clickSearchBtn()
        hideKeyboard()
        initBtn()
        return binding.root
    }

    /*
    @SuppressLint("ClickableViewAccessibility")
    fun EditText.setupClearButtonWithAction() {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(ed: Editable?) {
                getCurrentCityForecast(ed.toString())
                val clearIcon = if (ed?.isNotEmpty() == true) R.drawable.ic_clear else 0
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_on, 0, clearIcon, 0)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        setOnTouchListener(View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                    this.setText("")
                    binding.card.isVisible = false
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
    }

    private fun initBtn() {
        binding.btn.setOnClickListener {
            binding.search.setText("")
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fetchLocation()
        }
    }*/

    private fun clickSearchBtn() {
        binding.searchBtn.setOnClickListener {
            val cityNameText = binding.searchText.text.toString()
            if (cityNameText.isEmpty()) {
                Toast.makeText(it.context, "Type city name, please!", Toast.LENGTH_SHORT).show()
            } else {
                getCityForecastByCityName(cityNameText)
//                getCurrentCityForecast(cityNameText)
                binding.btn.isClickable = true
            }
        }
    }

    private fun getCityForecastByCityName(cityNameText: String) {
        searchForecastVM.sendData(cityNameText, UNITS, API_KEY)
        observeSearchForecast()
    }

    private fun initBtn() {
        binding.btn.setOnClickListener {
            binding.searchText.setText("")
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fetchLocation()
            binding.btn.isClickable = false
        }
    }

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
            getCurrentCityForecastByCoordinates(it.latitude.toString(),it.longitude.toString())
//            getCurrentCityForecast(getCityName(it.latitude,it.longitude, requireContext()))
        }
    }

    private fun getCurrentCityForecastByCoordinates(lat: String, lon: String) {
        locationForecastVM.sendData(lat, lon, UNITS, API_KEY)
        observeForecast()
    }

//    private fun getCurrentCityForecast(text: String) {
//        locationForecastVM.sendData(text.lowercase(), UNITS, API_KEY)
//        observeForecast()
//    }

    private fun observeForecast() {
        locationForecastVM.locationForecastData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.card.isVisible = true
                    response.data?.let {
                        binding.temp.text = getWholeNum(it.temperatureInfo.temp).plus("°c")
                        binding.img.setAnimation(getWeatherAnimation(it.weather[0].icon))
                        binding.img.playAnimation()
                        binding.weather.text = it.weather[0].currentWeather
                        binding.city.text = it.cityName
                        binding.card.setOnClickListener { view ->
                            val action = SearchFragmentDirections.actionSearchToForecastReportFragment(it)
                            view.findNavController().navigate(action)
                        }
                    }
                }
                is NetworkResult.Error -> {
                    displayToast(response.message.toString(), requireContext())
                }
            }
        })
    }

    private fun observeSearchForecast() {
        searchForecastVM.searchForecastData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.card.isVisible = true
                    response.data?.let {
                        binding.temp.text = getWholeNum(it.temperatureInfo.temp).plus("°c")
                        binding.img.setAnimation(getWeatherAnimation(it.weather[0].icon))
                        binding.img.playAnimation()
                        binding.weather.text = it.weather[0].currentWeather
                        binding.city.text = it.cityName
                        binding.card.setOnClickListener { view ->
                            val action = SearchFragmentDirections.actionSearchToForecastReportFragment(it)
                            view.findNavController().navigate(action)
                        }
                    }
                }
                is NetworkResult.Error -> {
                    displayToast(response.message.toString(), requireContext())
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun hideKeyboard() {
        binding.mainLayout.setOnTouchListener { view, motionEvent ->
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }

    override fun onPause() {
        if (toast != null) toast!!.cancel()
        super.onPause()
    }
}














