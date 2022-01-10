package com.example.weatherapptask.ui.forecastreport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapptask.data.remote.model.Daily
import com.example.weatherapptask.databinding.DailyTempItemBinding
import com.example.weatherapptask.util.Util
import com.example.weatherapptask.util.Util.convertDate
import com.example.weatherapptask.util.Util.getWeekDayName

class DailyForecastAdapter : ListAdapter<Daily, DailyForecastAdapter.ItemHolder>(DiffCallback()) {

    class ItemHolder(private val binding: DailyTempItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: Daily) {
            binding.dayOfWeek.text = getWeekDayName(model.dt.toString())
            binding.day.text = convertDate(model.dt.toString(), "4")
            binding.temp.text = Util.getWholeNum(model.temp.day).plus("°c")
            binding.img.setAnimation(Util.getWeatherAnimation(model.weather[0].icon))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            DailyTempItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<Daily>() {
        override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean =
            oldItem == newItem
    }

    override fun submitList(list: MutableList<Daily>?) {
        super.submitList(list?.map { it.copy() })
    }
}