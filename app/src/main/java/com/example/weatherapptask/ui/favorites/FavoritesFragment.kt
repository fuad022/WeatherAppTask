package com.example.weatherapptask.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapptask.data.remote.LocationForecastVM
import com.example.weatherapptask.databinding.FragmentFavoritesBinding
import com.example.weatherapptask.ui.favorites.adapter.FavoritesForecastsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private val favoritesForecastsAdapter = FavoritesForecastsAdapter()
    private val locationForecastVM: LocationForecastVM by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
        return binding.root
    }

    private fun init() {
        locationForecastVM.readFavoriteForecasts.observe(viewLifecycleOwner, {
            favoritesForecastsAdapter.submitList(it.toMutableList())
        })
        binding.favoriteForecastRv.adapter = favoritesForecastsAdapter
    }

}