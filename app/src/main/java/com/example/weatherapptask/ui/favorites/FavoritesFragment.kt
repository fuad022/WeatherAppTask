package com.example.weatherapptask.ui.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.weatherapptask.R
import com.example.weatherapptask.data.remote.LocationForecastVM
import com.example.weatherapptask.databinding.FragmentFavoritesBinding
import com.example.weatherapptask.ui.favorites.adapter.FavoritesForecastsAdapter
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private val locationForecastVM: LocationForecastVM by viewModel()
    private val favoritesForecastsAdapter: FavoritesForecastsAdapter by lazy { FavoritesForecastsAdapter(requireActivity(), locationForecastVM) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        setHasOptionsMenu(true)
        init()
        return binding.root
    }

    private fun init() {
        locationForecastVM.readFavoriteForecasts.observe(viewLifecycleOwner, {
            favoritesForecastsAdapter.submitList(it.toMutableList())
        })
        binding.favoriteForecastRv.adapter = favoritesForecastsAdapter
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.favorite_forecasts_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.deleteAll_favorite_forecasts_menu) {
//            locationForecastVM.deleteAllFavoriteForecasts()
//            showSnackBar("All forecasts removed.")
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun showSnackBar(message: String) {
//        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
//    }

    override fun onDestroy() {
        super.onDestroy()
        favoritesForecastsAdapter.clearContextualActionMode()
    }
}