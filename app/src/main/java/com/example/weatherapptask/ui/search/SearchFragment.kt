package com.example.weatherapptask.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import com.example.weatherapptask.R
import com.example.weatherapptask.data.remote.LocationForecastVM
import com.example.weatherapptask.data.remote.other.Constants.Companion.API_KEY
import com.example.weatherapptask.data.remote.other.Constants.Companion.BAKU_CITY
import com.example.weatherapptask.data.remote.other.Constants.Companion.UNITS
import com.example.weatherapptask.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private val locationForecastVM: LocationForecastVM by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.search.setupClearButtonWithAction()
        initBtn()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    fun EditText.setupClearButtonWithAction() {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(ed: Editable?) {
//                if (ed?.trim()?.isEmpty() == true) binding.card.isVisible = false
                getCity(ed.toString().trim())
                val clearIcon = if (ed?.isNotEmpty() == true) R.drawable.ic_clear else 0
                setCompoundDrawablesWithIntrinsicBounds(clearIcon, 0, R.drawable.ic_search_on, 0)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        setOnTouchListener(View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (this.left - this.compoundPaddingRight)) {
                    this.setText(null)
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
            getCity(BAKU_CITY)
        }
    }

    private fun getCity(text: String) {
//        if (text.isBlank()) binding.card.isVisible = false
        binding.card.isVisible = true
        locationForecastVM.sendData(text.lowercase(), UNITS, API_KEY)
        observeForecast()
    }

    private fun observeForecast() {
        locationForecastVM.locationForecastData.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                response.body()?.let {
                    binding.temp.text = it.temperatureInfo.temp.toString()
                    binding.city.text = it.cityName
                }
            }
        })
    }
}