package com.weather.weathermvvmapp.weather.model

import androidx.lifecycle.LiveData
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherListObjectModel
import com.weather.weathermvvmapp.data.repository.WeatherRepositoryProvider
import com.weather.weathermvvmapp.weather.BaseWeatherViewModel
import javax.inject.Inject

class DetailedWeatherViewModel @Inject constructor(
    private val weatherRepositoryProvider: WeatherRepositoryProvider
) : BaseWeatherViewModel<FutureWeatherListObjectModel>() {

    private var dayInMils: Long? = 0

    override fun refreshData() {
        weatherRepositoryProvider.getSpecificFutureWeatherModelObject(dayInMils)
    }

    init {
        fetchData()
        weatherRepositoryProvider.getSpecificFutureWeatherModelObject(dayInMils)
    }

    fun setDayInMils(dayInMils: Long?) {
        this.dayInMils = dayInMils
        refreshData()
    }

    override fun createLiveData(): LiveData<FutureWeatherListObjectModel>? = weatherRepositoryProvider.getSpecificWeatherModel(dayInMils)

//    companion object {
//        fun getInstance(
//            fragment: Fragment,
//            dayInMils: Long?
//        ): DetailedWeatherViewModel {
//            return ViewModelProviders.of(fragment, object : ViewModelProvider.Factory {
//                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                    val weatherRepositoryProvider = WeatherRepositoryProvider(
//                        createApiInterface(),
//                        WeatherDatabase.invoke(fragment.requireContext()),
//                        NetworkProvider(fragment.requireContext())
//                    )
//                    return DetailedWeatherViewModel(weatherRepositoryProvider, dayInMils) as T
//                }
//            })[DetailedWeatherViewModel::class.java]
//        }
//    }
}
