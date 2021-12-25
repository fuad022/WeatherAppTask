package com.example.weatherapptask.ui.forecastreport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.weatherapptask.R
import com.example.weatherapptask.databinding.FragmentForecastReportBinding

class ForecastReportFragment : Fragment() {
    private val binding by lazy { FragmentForecastReportBinding.inflate(layoutInflater) }
    private val args: ForecastReportFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

}