package com.weather.weathermvvmapp.weather.ui.future_weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.weathermvvmapp.R
import com.weather.weathermvvmapp.data.database.future_db.FutureWeather
import com.weather.weathermvvmapp.extensions.showToast
import com.weather.weathermvvmapp.weather.model.FutureWeatherViewModel
import kotlinx.android.synthetic.main.future_weather_fragment.*

class FutureWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = FutureWeatherFragment()
    }

    private lateinit var futureWeatherViewModel: FutureWeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        futureWeatherViewModel = FutureWeatherViewModel.getInstance(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_weather_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        futureWeatherViewModel.liveData().observe(this, Observer { viewObject ->
            showProgress(viewObject.progress)
            when {
                viewObject.data == null -> return@Observer
                viewObject.error -> {
                    if (viewObject.throwable != null) {
                        viewObject.throwable.message?.let { showToast(it) }
                    } else {
                        showToast("Error")
                    }
                }
                else -> showFutureWeather(viewObject.data)
            }
        })
    }

    private fun showProgress(progress: Boolean) {
        if (progress) {
            loadingGroup.visibility = View.VISIBLE
        } else {
            loadingGroup.visibility = View.GONE
        }
    }


    private fun showFutureWeather(futureWeather: FutureWeather) {
        Log.d("Test", futureWeather.toString())
    }
}
