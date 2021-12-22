package com.example.weatherapptask.ui.mylocation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapptask.data.remote.model.Hourly
import com.example.weatherapptask.databinding.HourlyTempItemBinding
import com.example.weatherapptask.util.Util.getWeatherAnimation

class HourlyForecastAdapter : ListAdapter<Hourly, HourlyForecastAdapter.ItemHolder>(DiffCallback()) {

    class ItemHolder(private val binding: HourlyTempItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Hourly) {
            binding.img.setAnimation(getWeatherAnimation(model.weather[0].icon))
            binding.time.text = model.dt.toString()
            binding.temp.text = model.temp.toString().plus("c")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            HourlyTempItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<Hourly>() {
        override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean =
            oldItem == newItem

    }

    override fun submitList(list: MutableList<Hourly>?) {
        super.submitList(list?.map { it.copy() })
    }
}