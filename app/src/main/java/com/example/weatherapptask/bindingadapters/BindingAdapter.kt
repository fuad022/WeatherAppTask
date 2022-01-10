package com.example.weatherapptask.bindingadapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapptask.data.database.FavoritesEntity
import com.example.weatherapptask.ui.favorites.adapter.FavoritesForecastsAdapter

class BindingAdapter {

    companion object {

        @BindingAdapter("viewVisibility", "setData", requireAll = false)
        @JvmStatic
        fun satDataAndViewVisibility(
            view: View,
            favoritesEntity: List<FavoritesEntity>?,
            favoritesForecastsAdapter: FavoritesForecastsAdapter?
        ) {
            if (favoritesEntity.isNullOrEmpty()) {
                when (view) {
                    is TextView -> {
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            } else {
                when (view) {
                    is TextView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        favoritesForecastsAdapter?.submitList(favoritesEntity.toMutableList())
                    }
                }
            }
        }

    }

}