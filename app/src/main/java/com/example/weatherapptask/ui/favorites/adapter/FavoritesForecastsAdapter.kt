package com.example.weatherapptask.ui.favorites.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapptask.data.database.FavoritesEntity
import com.example.weatherapptask.databinding.FavoriteForecastRowItemBinding
import com.example.weatherapptask.ui.favorites.FavoritesFragmentDirections
import com.example.weatherapptask.util.Util.getWeatherAnimation
import com.example.weatherapptask.util.Util.getWholeNum

class FavoritesForecastsAdapter : ListAdapter<FavoritesEntity, FavoritesForecastsAdapter.ItemHolder>(DiffCallback()) {
    var setOnItemClick: ((FavoritesEntity) -> Unit)? = null

    inner class ItemHolder(private val binding: FavoriteForecastRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: FavoritesEntity?) {
            /*
//            binding.favoritesEntity = favoritesEntity
//            binding.executePendingBindings()
            binding.favoriteCity.text = model.cityName
            binding.favoriteWeather.text = model.weather[0].currentWeather
            binding.favoriteTemp.text = getWholeNum(model.temperatureInfo.temp).plus("°c")
            binding.favoriteImg.setAnimation(getWeatherAnimation(model.weather[0].icon))
            binding.favoriteImg.playAnimation()
            */
            ///////

            model?.let { m ->
                binding.favoriteCity.text = m.cityName
                binding.favoriteWeather.text = m.weather[0].currentWeather
                binding.favoriteTemp.text = getWholeNum(m.temperatureInfo.temp).plus("°c")
                binding.favoriteImg.setAnimation(getWeatherAnimation(m.weather[0].icon))
                binding.favoriteImg.playAnimation()

                /**
                 * Single click listener
                 */
                binding.root.setOnClickListener {
                    m.let { entity ->
                        setOnItemClick?.invoke(entity)
                        Navigation.findNavController(it).navigate(
                            FavoritesFragmentDirections.actionFavoritesToForecastReportFragment(m)
                        )
                    }
                }
            }
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