package com.weather.weathermvvmapp.weather.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.weather.weathermvvmapp.data.database.entity.FutureWeatherListObject

class DetailedWeatherViewModel(
    private val futureWeatherListObject: FutureWeatherListObject?
) : ViewModel() {

    private var detailWeatherMutable: MutableLiveData<FutureWeatherListObject> = MutableLiveData()
    private var detailWeatherLiveData: LiveData<FutureWeatherListObject> = detailWeatherMutable

    init {
        detailWeatherMutable.value = futureWeatherListObject
    }

    fun liveData(): LiveData<FutureWeatherListObject> {
        return detailWeatherLiveData
    }

    companion object {
        fun getInstance(fragment: Fragment, dayInMils: FutureWeatherListObject?): DetailedWeatherViewModel {
            return ViewModelProviders.of(fragment, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return DetailedWeatherViewModel(dayInMils) as T
                }
            })[DetailedWeatherViewModel::class.java]
        }
    }
}
