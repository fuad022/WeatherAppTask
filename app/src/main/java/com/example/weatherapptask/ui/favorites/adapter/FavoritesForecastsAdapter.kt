package com.example.weatherapptask.ui.favorites.adapter

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapptask.R
import com.example.weatherapptask.data.database.FavoritesEntity
import com.example.weatherapptask.databinding.FavoriteForecastRowItemBinding
import com.example.weatherapptask.ui.favorites.FavoritesFragmentDirections
import com.example.weatherapptask.util.Util.getWeatherAnimation
import com.example.weatherapptask.util.Util.getWholeNum

class FavoritesForecastsAdapter(
    private val requireActivity: FragmentActivity
) : ListAdapter<FavoritesEntity, FavoritesForecastsAdapter.ItemHolder>(DiffCallback()), ActionMode.Callback {

    private var multiSelection = false
    private var selectedForecasts = arrayListOf<FavoritesEntity>()
    private var myViewHolders = arrayListOf<ItemHolder>()

    class ItemHolder(val binding: FavoriteForecastRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoritesEntity: FavoritesEntity?) {
//            binding.favoritesEntity = favoritesEntity
//            binding.executePendingBindings()

            favoritesEntity?.let { m ->
                binding.favoriteCity.text = m.cityName
                binding.favoriteWeather.text = m.weather[0].currentWeather
                binding.favoriteTemp.text = getWholeNum(m.temperatureInfo.temp).plus("°c")
                binding.favoriteImg.setAnimation(getWeatherAnimation(m.weather[0].icon))
                binding.favoriteImg.playAnimation()
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
//        myViewHolders.add(holder)

        val selectedForecast = getItem(position)
        holder.bind(selectedForecast)

        /**
         * Single click listener
         */
        holder.binding.root.setOnClickListener {
            val action =
                FavoritesFragmentDirections.actionFavoritesToForecastReportFragment(selectedForecast)
            holder.itemView.findNavController().navigate(action)
        }

        /**
         * Long click listener
         */
        holder.binding.root.setOnLongClickListener {
            requireActivity.startActionMode(this@FavoritesForecastsAdapter)
            true
        }
    }

    private fun applySelection(holder: ItemHolder, currentForecast: FavoritesEntity) {
        if (selectedForecasts.contains(currentForecast)) {
            selectedForecasts.remove(currentForecast)
            changeForecastStyle(holder, R.color.light_black, R.color.light_black)
        } else {
            selectedForecasts.add(currentForecast)
            changeForecastStyle(holder, R.color.card_view, R.color.card_view)
        }
    }

    private fun changeForecastStyle(holder: ItemHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.innerLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.binding.favoriteRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
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

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)

        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
//        myViewHolders.forEach { holder ->
//            changeForecastStyle(holder, R.color.background, R.color.background)
//        }
//        multiSelection = false
//        selectedForecasts.clear()
    }

    /*
    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }*/
}