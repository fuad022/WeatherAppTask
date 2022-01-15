package com.example.weatherapptask.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.weatherapptask.R
import com.example.weatherapptask.data.database.FavoritesEntity
import com.example.weatherapptask.data.remote.LocationForecastVM
import com.example.weatherapptask.databinding.FragmentFavoritesBinding
import com.example.weatherapptask.ui.favorites.adapter.FavoritesForecastsAdapter
import com.example.weatherapptask.util.observeOnce
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private val binding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private val locationForecastVM: LocationForecastVM by viewModel()
    private val favoritesForecastsAdapter: FavoritesForecastsAdapter by lazy { FavoritesForecastsAdapter(requireActivity(), locationForecastVM) }

//    private var list = arrayListOf<FavoritesEntity>() // new add

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
//        (activity as AppCompatActivity).supportActionBar?.title = "Example"
        init()
        return binding.root
    }

    private fun init() {
        locationForecastVM.readFavoriteForecasts.observe(viewLifecycleOwner, {

//            favoritesForecastsAdapter.storeListEntity(it)
            favoritesForecastsAdapter.submitList(it.toMutableList())

            Log.d("ItEntityList", it.size.toString())

//            it.forEach { entity ->
//                list.add(entity)
//            }
//
//            if (it.isEmpty()) {
//                binding.title.isVisible = false
//                binding.favoriteForecastRv.isVisible = false
//                binding.noDataTxt.isVisible = true
//            }
        })
        binding.favoriteForecastRv.adapter = favoritesForecastsAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        favoritesForecastsAdapter.clearContextualActionMode()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_forecasts_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll_favorite_forecasts_menu) {
            locationForecastVM.deleteAllFavoriteForecasts()
            showSnackBar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackBar() {
        Snackbar.make(binding.root, "All forecasts removed.", Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
    }
}