package com.example.weatherapptask.ui.favorites.adapter

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
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
) : ListAdapter<FavoritesEntity, FavoritesForecastsAdapter.ItemHolder>(DiffCallback()),
    ActionMode.Callback {
    var setOnItemClick: ((FavoritesEntity) -> Unit)? = null

    inner class ItemHolder(private val binding: FavoriteForecastRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: FavoritesEntity?) {
//            binding.favoritesEntity = favoritesEntity
//            binding.executePendingBindings()

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

                /**
                 * Long click listener
                 */
                binding.root.setOnLongClickListener {
                    requireActivity.startActionMode(this@FavoritesForecastsAdapter)
                    true
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

    }

    /*
    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }*/
}