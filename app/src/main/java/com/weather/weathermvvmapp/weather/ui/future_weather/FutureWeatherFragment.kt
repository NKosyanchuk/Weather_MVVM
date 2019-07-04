package com.weather.weathermvvmapp.weather.ui.future_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.weather.weathermvvmapp.R
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherModel
import com.weather.weathermvvmapp.data.network.response.FutureWeatherListObject
import com.weather.weathermvvmapp.extensions.replaceFragment
import com.weather.weathermvvmapp.extensions.showToast
import com.weather.weathermvvmapp.weather.model.FutureWeatherViewModel
import com.weather.weathermvvmapp.weather.ui.detailed_weather.DetailedWeatherFragment
import kotlinx.android.synthetic.main.future_weather_fragment.*

class FutureWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = FutureWeatherFragment()
    }

    private lateinit var futureWeatherViewModel: FutureWeatherViewModel
    private lateinit var futureWeatherAdapter: FutureWeatherAdapter

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

        initFutureWeatherListView()

        futureWeatherViewModel.liveData().observe(this, Observer { viewObject ->
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
                else -> showFutureWeather(viewObject.data)
            }
        })
    }

    private fun initFutureWeatherListView() {
        futureWeatherAdapter = FutureWeatherAdapter { futureWeatherObjectDate ->
            //            showDetailedWeather(futureWeatherObjectDate)
        }
        futureWeatherRv.apply {
            adapter = futureWeatherAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showDetailedWeather(futureWeatherListObject: FutureWeatherListObject) {
        val detailedWeatherFragment = DetailedWeatherFragment.newInstance(futureWeatherListObject)
        replaceFragment(detailedWeatherFragment)
    }

    private fun showProgress(progress: Boolean) {
        if (progress) {
            loadingGroup.visibility = View.VISIBLE
        } else {
            loadingGroup.visibility = View.GONE
        }
    }

    private fun showFutureWeather(futureWeather: FutureWeatherModel) {
        futureWeather.listWeather?.let { futureWeatherAdapter.updateWeather(it) }
    }
}
