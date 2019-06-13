package com.weather.weathermvvmapp.weather.ui.future_weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weathermvvmapp.R
import com.weather.weathermvvmapp.data.database.entity.FutureWeatherListObject

class FutureWeatherAdapter(private val futureWeatherClickListener: FutureWeatherClickListener) :
    RecyclerView.Adapter<FutureWeatherHolder>() {

    private var futureWeatherList: ArrayList<FutureWeatherListObject> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FutureWeatherHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_future_weather_list, parent, false)
        return FutureWeatherHolder(itemView)
    }

    override fun getItemCount(): Int = futureWeatherList.size

    override fun onBindViewHolder(holder: FutureWeatherHolder, position: Int) {
        holder.bindUI(futureWeatherList[position])
        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                futureWeatherClickListener.invoke(futureWeatherList[holder.adapterPosition].dt) //date in mills
            }
        }
    }

    fun updateWeather(futureWeatherList: ArrayList<FutureWeatherListObject>) {
        this.futureWeatherList = futureWeatherList
        notifyDataSetChanged()
    }
}

typealias FutureWeatherClickListener = (Long) -> Unit