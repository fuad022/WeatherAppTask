package com.example.weatherapptask.ui.favorites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapptask.data.database.FavoritesEntity
import com.example.weatherapptask.databinding.FavoriteForecastRowItemBinding
import com.example.weatherapptask.ui.favorites.FavoritesFragment
import com.example.weatherapptask.ui.favorites.FavoritesFragmentDirections
import com.example.weatherapptask.util.Util
import com.example.weatherapptask.util.Util.getWeatherAnimation
import com.example.weatherapptask.util.Util.getWholeNum

class FavoritesForecastsAdapter : ListAdapter<FavoritesEntity, FavoritesForecastsAdapter.ItemHolder>(DiffCallback()) {

    class ItemHolder(private val binding: FavoriteForecastRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: FavoritesEntity) {
//            binding.favoritesEntity = favoritesEntity
//            binding.executePendingBindings()
            binding.favoriteCity.text = model.cityName
            binding.favoriteWeather.text = model.weather[0].currentWeather
            binding.favoriteTemp.text = getWholeNum(model.temperatureInfo.temp).plus("°c")
            binding.favoriteImg.setAnimation(getWeatherAnimation(model.weather[0].icon))
            binding.favoriteImg.playAnimation()

//            binding.forecastRowItem.setOnClickListener {
//                val action = FavoritesFragmentDirections.actionFavoritesToForecastReportFragment(model)
//            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            FavoriteForecastRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<FavoritesEntity>() {
        override fun areItemsTheSame(oldItem: FavoritesEntity, newItem: FavoritesEntity): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: FavoritesEntity, newItem: FavoritesEntity): Boolean =
            oldItem == newItem
    }

    override fun submitList(list: MutableList<FavoritesEntity>?) {
        super.submitList(list?.map { it.copy() })
    }
}