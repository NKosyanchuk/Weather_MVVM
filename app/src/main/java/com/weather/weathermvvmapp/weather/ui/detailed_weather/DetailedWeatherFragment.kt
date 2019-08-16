package com.weather.weathermvvmapp.weather.ui.detailed_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.weather.weathermvvmapp.R
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherListObjectModel
import com.weather.weathermvvmapp.data.network.WEATHER_ICON_URL
import com.weather.weathermvvmapp.extensions.*
import com.weather.weathermvvmapp.weather.model.DetailedWeatherViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.detailed_weather_fragment.*
import javax.inject.Inject


const val DATA_MILLS_ARGS = "day_mils"

class DetailedWeatherFragment : DaggerFragment() {

    companion object {
        fun newInstance(dayInMils: Long?): DetailedWeatherFragment {
            val detailedWeatherFragment = DetailedWeatherFragment()
            val fragmentBundle = Bundle()
            dayInMils?.let { fragmentBundle.putLong(DATA_MILLS_ARGS, it) }
            detailedWeatherFragment.arguments = fragmentBundle
            return detailedWeatherFragment
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var detailedWeatherViewModel: DetailedWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detailed_weather_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var dayInMils: Long? = null
        if (arguments != null) {
            dayInMils = arguments!!.getLong(DATA_MILLS_ARGS)
        }
        detailedWeatherViewModel = viewModelProvider(viewModelFactory)
        detailedWeatherViewModel.setDayInMils(dayInMils)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailedWeatherViewModel.liveData().observe(viewLifecycleOwner, Observer { viewObject ->
            when {
                viewObject.error -> {
                    if (viewObject.throwable != null) {
                        viewObject.throwable.message?.let { showToast(it) }
                    } else {
                        showToast("Error")
                    }
                }
                viewObject.data == null -> return@Observer
                else -> showDetailedWeather(viewObject.data)
            }
        })
    }

    private fun showDetailedWeather(futureWeatherListObjectModel: FutureWeatherListObjectModel) {
        setupTitle(getDateFromString(futureWeatherListObjectModel.dt * 1000))
        temperatureTv.text = getTemperatureString(futureWeatherListObjectModel)
        descriptionTv.text = futureWeatherListObjectModel.description
        setupWeatherIcon(futureWeatherListObjectModel)
        windTv.text = requireContext().getString(R.string.wind, futureWeatherListObjectModel.speed.toString())
    }

    private fun getTemperatureString(futureWeatherListObject: FutureWeatherListObjectModel): String {
        return requireContext().getString(
            R.string.tempFormat,
            futureWeatherListObject.min.roundDoubleToString(),
            futureWeatherListObject.max.roundDoubleToString()
        )
    }

    private fun setupWeatherIcon(futureWeatherListObject: FutureWeatherListObjectModel) {
        //https//openweathermap.org/img/w/03d.png
        val iconURL = WEATHER_ICON_URL + futureWeatherListObject.icon + ".png"
        Picasso.get()
            .load(iconURL)
            .into(weatherIv)
    }
}