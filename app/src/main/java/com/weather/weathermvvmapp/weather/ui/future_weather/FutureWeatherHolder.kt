package com.weather.weathermvvmapp.weather.ui.future_weather

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.weather.weathermvvmapp.R
import com.weather.weathermvvmapp.data.database.entity.FutureWeatherListObject
import com.weather.weathermvvmapp.data.network.WEATHER_ICON_URL
import com.weather.weathermvvmapp.extensions.getDateFromString
import com.weather.weathermvvmapp.extensions.roundDoubleToString
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_future_weather_list.view.*


class FutureWeatherHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private lateinit var futureWeatherListObject: FutureWeatherListObject

    fun bindUI(futureWeatherListObject: FutureWeatherListObject) {
        this.futureWeatherListObject = futureWeatherListObject

        containerView.dateTv.text = getDateFromString(futureWeatherListObject.dt * 1000)
        containerView.temperatureTv.text = getTemperatureString(futureWeatherListObject)
        containerView.descriptionTv.text = futureWeatherListObject.weather[0].description
        setupWeatherIcon(futureWeatherListObject)
    }

    private fun setupWeatherIcon(futureWeatherListObject: FutureWeatherListObject) {
        //https//openweathermap.org/img/w/03d.png
        val iconURL = WEATHER_ICON_URL + futureWeatherListObject.weather[0].icon + ".png"
        Picasso.get()
            .load(iconURL)
            .into(containerView.weatherIv)
    }

    private fun getTemperatureString(futureWeatherListObject: FutureWeatherListObject): String {
        return containerView.context.getString(
            R.string.tempFormat,
            futureWeatherListObject.temp.min.roundDoubleToString(),
            futureWeatherListObject.temp.max.roundDoubleToString()
        )
    }
}