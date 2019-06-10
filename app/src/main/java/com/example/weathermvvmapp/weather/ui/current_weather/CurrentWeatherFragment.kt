package com.example.weathermvvmapp.weather.ui.current_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.weathermvvmapp.database.current_db.CurrentWeather
import com.example.weathermvvmapp.extensions.showToast
import com.example.weathermvvmapp.network.WEATHER_ICON_URL
import com.example.weathermvvmapp.repository.LocationProvider
import com.example.weathermvvmapp.weather.model.CurrentWeatherViewModel
import com.squareup.picasso.Picasso
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
        return inflater.inflate(com.example.weathermvvmapp.R.layout.current_weather_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentWeatherViewModel = CurrentWeatherViewModel.getInstance(this, LocationProvider(48.5, 37.5))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentWeatherViewModel.data().observe(this, Observer { viewObject ->
            showProgress(viewObject.progress)
            when {
                viewObject.data == null -> return@Observer
                viewObject.error -> showToast("Error")
                else -> showCurrentWeather(viewObject.data)
            }
        })
    }

    private fun showCurrentWeather(currentWeather: CurrentWeather) {
        val weather = currentWeather.weather[0]
        val temperatureObject = currentWeather.main
        descriptionTv.text = weather.description
        temperatureTv.text = temperatureObject.temp.toString()
        minTemperatureTv.text = temperatureObject.tempMin.toString()
        maxTemperatureTv.text = temperatureObject.tempMax.toString()
        setupWeatherIcon(currentWeather)
    }

    private fun setupWeatherIcon(currentWeather: CurrentWeather) {
        //https//openweathermap.org/img/w/03d.png
        val iconURL = WEATHER_ICON_URL + currentWeather.weather[0].icon + ".png"
        Picasso.get()
            .load(iconURL)
            .into(weatherIv)
    }

    private fun showProgress(progress: Boolean) {
        if (progress) {
            loadingGroup.visibility = View.VISIBLE
        } else {
            loadingGroup.visibility = View.GONE
        }
    }
}
