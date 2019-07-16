package com.weather.weathermvvmapp.weather.model

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.weather.weathermvvmapp.data.database.future_db.FutureWeatherListObjectModel

class DetailedWeatherViewModel(
    futureWeatherListObject: FutureWeatherListObjectModel?
) : ViewModel() {

    private var detailWeatherMutable: MutableLiveData<FutureWeatherListObjectModel> = MutableLiveData()
    private var detailWeatherLiveData: LiveData<FutureWeatherListObjectModel> = detailWeatherMutable

    init {
        detailWeatherMutable.value = futureWeatherListObject
    }

    fun liveData(): LiveData<FutureWeatherListObjectModel> {
        return detailWeatherLiveData
    }

    companion object {
        fun getInstance(fragment: Fragment, futureWeatherListObjectModel: FutureWeatherListObjectModel?): DetailedWeatherViewModel {
            return ViewModelProviders.of(fragment, object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return DetailedWeatherViewModel(futureWeatherListObjectModel) as T
                }
            })[DetailedWeatherViewModel::class.java]
        }
    }
}
