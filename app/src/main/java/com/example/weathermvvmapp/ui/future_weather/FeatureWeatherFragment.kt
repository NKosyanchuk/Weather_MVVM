package com.example.weathermvvmapp.ui.future_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.weathermvvmapp.R

class FeatureWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = FeatureWeatherFragment()
    }

    private lateinit var viewModel: FeatureWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.feature_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FeatureWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
