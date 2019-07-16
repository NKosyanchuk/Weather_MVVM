package com.weather.weathermvvmapp.weather.ui.future_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.weather.weathermvvmapp.R
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherListObjectModel
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

        futureWeatherViewModel.liveData().observe(viewLifecycleOwner, Observer { viewObject ->
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
                else -> showFutureWeather(viewObject.data as ArrayList<FutureWeatherListObjectModel>?)
            }
        })

        swipeToRefresh.setOnRefreshListener {
            futureWeatherViewModel.refreshData()
        }
    }

    private fun initFutureWeatherListView() {
        futureWeatherAdapter = FutureWeatherAdapter { dayInMils ->
            showDetailedWeather(dayInMils)
        }
        futureWeatherRv.apply {
            adapter = futureWeatherAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun showDetailedWeather(dayInMils: Long) {
        val detailedWeatherFragment = DetailedWeatherFragment.newInstance(dayInMils)
        replaceFragment(detailedWeatherFragment)
    }

    private fun showProgress(progress: Boolean) {
        if (progress) {
            loadingGroup.visibility = View.VISIBLE
        } else {
            loadingGroup.visibility = View.GONE
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun showFutureWeather(futureWeather: ArrayList<FutureWeatherListObjectModel>?) {
        futureWeather?.let { futureWeatherAdapter.updateWeather(it) }
    }
}
