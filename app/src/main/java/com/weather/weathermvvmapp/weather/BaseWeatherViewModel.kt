package com.weather.weathermvvmapp.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


data class ViewObject<T>(
    val data: T?,
    var progress: Boolean,
    val error: Boolean,
    val throwable: Throwable?
)

abstract class BaseWeatherViewModel<T> : ViewModel() {

    private val mutableLiveData = MutableLiveData<ViewObject<T>>().apply {
        value = ViewObject(
            data = null,
            progress = false,
            error = false,
            throwable = null
        )
    }

    protected abstract fun createLiveData(): LiveData<T>?

    fun liveData(): LiveData<ViewObject<T>> = mutableLiveData

    abstract fun refreshData()

    protected fun fetchData() {
        val currentData = mutableLiveData.value
        if (currentData?.progress == true) {
            return
        }
        mutableLiveData.postValue(currentData?.copy(progress = true))

        if (createLiveData() != null) {
            createLiveData()!!.observeForever {
                mutableLiveData.postValue(
                    currentData?.copy(
                        data = it,
                        progress = false,
                        error = false,
                        throwable = null
                    )
                )
            }
        } else {
            mutableLiveData.postValue(
                currentData?.copy(
                    data = null,
                    progress = false,
                    error = true,
                    throwable = Throwable(message = "No location permission or location found")
                )
            )
        }
    }
}