package com.example.weatherapptask.ui.mylocation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapptask.R
import com.example.weatherapptask.databinding.FragmentMyLocationBinding

class MyLocationFragment : Fragment() {
    private val binding by lazy { FragmentMyLocationBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

}