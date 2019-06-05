package com.example.weathermvvmapp.weather.ui.detailed_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.weathermvvmapp.R
import com.example.weathermvvmapp.weather.model.DetailedWeatherViewModel

class DetailedWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = DetailedWeatherFragment()
    }

    private lateinit var viewModel: DetailedWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detailed_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailedWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
