package com.weather.weathermvvmapp.weather.ui.detailed_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import com.weather.weathermvvmapp.R
import com.weather.weathermvvmapp.data.database.entity.FutureWeatherListObject
import com.weather.weathermvvmapp.data.network.WEATHER_ICON_URL
import com.weather.weathermvvmapp.extensions.roundDoubleToString
import com.weather.weathermvvmapp.weather.model.DetailedWeatherViewModel
import kotlinx.android.synthetic.main.detailed_weather_fragment.*


const val DATA_MILLS_ARGS = "data"

class DetailedWeatherFragment : Fragment() {

    companion object {
        fun newInstance(futureWeatherListObject: FutureWeatherListObject): DetailedWeatherFragment {
            val detailedWeatherFragment = DetailedWeatherFragment()
            val fragmentBundle = Bundle()
            fragmentBundle.putParcelable(DATA_MILLS_ARGS, futureWeatherListObject)
            detailedWeatherFragment.arguments = fragmentBundle
            return detailedWeatherFragment
        }
    }

    private lateinit var detailedWeatherViewModel: DetailedWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detailed_weather_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var futureWeatherListObject: FutureWeatherListObject? = null
        if (arguments != null) {
            futureWeatherListObject = arguments!!.getParcelable(DATA_MILLS_ARGS) as FutureWeatherListObject
        }
        detailedWeatherViewModel = DetailedWeatherViewModel.getInstance(this, futureWeatherListObject)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailedWeatherViewModel.liveData().observe(this, Observer { viewObject ->
            when (viewObject) {
                null -> return@Observer
                else -> showDetailedWeather(viewObject)
            }
        })
    }

    private fun showDetailedWeather(futureWeatherListObject: FutureWeatherListObject) {
        temperatureTv.text = getTemperatureString(futureWeatherListObject)
        descriptionTv.text = futureWeatherListObject.weather[0].description
        setupWeatherIcon(futureWeatherListObject)
        windTv.text = requireContext().getString(R.string.wind, futureWeatherListObject.speed.toString())
    }

    private fun getTemperatureString(futureWeatherListObject: FutureWeatherListObject): String {
        return requireContext().getString(
            R.string.tempFormat,
            futureWeatherListObject.temp.min.roundDoubleToString(),
            futureWeatherListObject.temp.max.roundDoubleToString()
        )
    }

    private fun setupWeatherIcon(futureWeatherListObject: FutureWeatherListObject) {
        //https//openweathermap.org/img/w/03d.png
        val iconURL = WEATHER_ICON_URL + futureWeatherListObject.weather[0].icon + ".png"
        Picasso.get()
            .load(iconURL)
            .into(weatherIv)
    }
}