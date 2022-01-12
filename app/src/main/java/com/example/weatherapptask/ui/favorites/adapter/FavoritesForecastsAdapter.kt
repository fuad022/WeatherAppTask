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
import com.example.weatherapptask.data.remote.LocationForecastVM
import com.example.weatherapptask.databinding.FavoriteForecastRowItemBinding
import com.example.weatherapptask.ui.favorites.FavoritesFragmentDirections
import com.example.weatherapptask.util.Util.getWeatherAnimation
import com.example.weatherapptask.util.Util.getWholeNum
import com.google.android.material.snackbar.Snackbar

class FavoritesForecastsAdapter(
    private val requireActivity: FragmentActivity,
    private val locationForecastVM: LocationForecastVM
) : ListAdapter<FavoritesEntity, FavoritesForecastsAdapter.ItemHolder>(DiffCallback()), ActionMode.Callback {

    private var multiSelection = false
    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View ////
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
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView ////

        val currentForecast = getItem(position)
        holder.bind(currentForecast)

        /**
         * Single click listener
         */
        holder.binding.root.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentForecast)
            } else {
                val action =
                    FavoritesFragmentDirections.actionFavoritesToForecastReportFragment(currentForecast)
                holder.itemView.findNavController().navigate(action)
            }
        }

        /**
         * Long click listener
         */
        holder.binding.root.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this@FavoritesForecastsAdapter)
                applySelection(holder, currentForecast)
                true
            } else {
                applySelection(holder, currentForecast)
                true
            }
        }
    }

    private fun applySelection(holder: ItemHolder, currentForecast: FavoritesEntity) {
        if (selectedForecasts.contains(currentForecast)) {
            selectedForecasts.remove(currentForecast)
            changeForecastStyle(holder, R.color.light_black, R.color.light_black)
            applyActionModeTitle()
        } else {
            selectedForecasts.add(currentForecast)
            changeForecastStyle(holder, R.color.card_view, R.color.card_view)
            applyActionModeTitle()
        }
    }

    private fun changeForecastStyle(holder: ItemHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.innerLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.binding.favoriteRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedForecasts.size) {
            0 -> {
                mActionMode.finish()
                multiSelection = false
            }
            1 -> {
                mActionMode.title = "${selectedForecasts.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedForecasts.size} items selected"
            }
        }
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
        mActionMode = actionMode!!
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_favorite_forecast_menu) {
            selectedForecasts.forEach {
                locationForecastVM.deleteFavoriteForecast(it)
            }
            showSnackBar("${selectedForecasts.size} Forecast/s removed.")

            multiSelection = false
            selectedForecasts.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeForecastStyle(holder, R.color.light_black, R.color.light_black)
        }
        multiSelection = false
        selectedForecasts.clear()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }

    /*
    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }*/
}