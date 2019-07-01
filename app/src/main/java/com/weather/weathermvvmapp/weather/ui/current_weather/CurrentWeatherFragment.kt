package com.weather.weathermvvmapp.weather.ui.current_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import com.weather.weathermvvmapp.R
import com.weather.weathermvvmapp.data.database.current_db.CurrentWeather
import com.weather.weathermvvmapp.data.database.entity.Weather
import com.weather.weathermvvmapp.data.network.WEATHER_ICON_URL
import com.weather.weathermvvmapp.extensions.roundDoubleToString
import com.weather.weathermvvmapp.extensions.showToast
import com.weather.weathermvvmapp.weather.model.CurrentWeatherViewModel

import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.android.synthetic.main.future_weather_fragment.loadingGroup


class CurrentWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = CurrentWeatherFragment()
    }

    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentWeatherViewModel = CurrentWeatherViewModel.getInstance(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentWeatherViewModel.liveData().observe(this, Observer { viewObject ->
            showProgress(viewObject.progress)
            when {
                viewObject.error -> {
                    if (viewObject.throwable != null) {
                        viewObject.throwable.message?.let { showToast(it) }
                    } else {
                        showToast("Error")
                    }
                }
                viewObject.data == null -> return@Observer
                else -> showCurrentWeather(viewObject.data)
            }
        })
    }

    private fun showCurrentWeather(currentWeather: CurrentWeather) {
        val weather = currentWeather.weather[0]
        val temperatureObject = currentWeather.main
        descriptionTv.text = weather.description
        temperatureTv.text = formatTemperatureStringWithSign(temperatureObject.temp)
        minTemperatureTv.text = formatTemperatureString(temperatureObject.tempMin, true)
        maxTemperatureTv.text = formatTemperatureString(temperatureObject.tempMax, false)
        windTv.text = requireContext().getString(R.string.wind, currentWeather.wind.speed.toString())
        setupWeatherIcon(weather)
    }

    private fun setupWeatherIcon(currentWeather: Weather) {
        //https//openweathermap.org/img/w/03d.png
        val iconURL = WEATHER_ICON_URL + currentWeather.icon + ".png"
        Picasso.get()
            .load(iconURL)
            .into(weatherIv)
    }

    private fun formatTemperatureString(temperatureDouble: Double, isMin: Boolean): String {
        return if (isMin) {
            getString(R.string.minTemp) + formatTemperatureStringWithSign(temperatureDouble)
        } else {
            getString(R.string.maxTemp) + formatTemperatureStringWithSign(temperatureDouble)
        }
    }

    private fun formatTemperatureStringWithSign(temperatureDouble: Double): String {
        return temperatureDouble.roundDoubleToString() + requireContext().getString(R.string.celsiusSign)
    }

    private fun showProgress(progress: Boolean) {
        if (progress) {
            loadingGroup.visibility = View.VISIBLE
        } else {
            loadingGroup.visibility = View.GONE
        }
    }
}
