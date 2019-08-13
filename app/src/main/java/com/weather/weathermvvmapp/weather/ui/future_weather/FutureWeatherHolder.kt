package com.weather.weathermvvmapp.weather.ui.future_weather

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.weather.weathermvvmapp.R
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherListObjectModel
import com.weather.weathermvvmapp.data.network.WEATHER_ICON_URL
import com.weather.weathermvvmapp.extensions.getDateFromString
import com.weather.weathermvvmapp.extensions.roundDoubleToString
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.detailed_weather_fragment.*
import kotlinx.android.synthetic.main.item_future_weather_list.view.*


class FutureWeatherHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private lateinit var futureWeatherListObject: FutureWeatherListObjectModel

    fun bindUI(futureWeatherListObject: FutureWeatherListObjectModel) {
        this.futureWeatherListObject = futureWeatherListObject

        containerView.dateTv.text = getDateFromString(futureWeatherListObject.dt * 1000)
        containerView.temperatureTv.text = getTemperatureString(futureWeatherListObject)
        containerView.descriptionTv.text = futureWeatherListObject.description
        setupWeatherIcon(futureWeatherListObject)
    }

    private fun setupWeatherIcon(futureWeatherListObjectModel: FutureWeatherListObjectModel) {
        //https//openweathermap.org/img/w/03d.png
        val iconURL = WEATHER_ICON_URL + futureWeatherListObjectModel.icon + ".png"
        weatherIv.load(iconURL)
    }

    private fun getTemperatureString(futureWeatherListObjectModel: FutureWeatherListObjectModel): String {
        return containerView.context.getString(
            R.string.tempFormat,
            futureWeatherListObjectModel.min.roundDoubleToString(),
            futureWeatherListObjectModel.max.roundDoubleToString()
        )
    }
}