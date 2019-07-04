package com.weather.weathermvvmapp.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable


data class ViewObject<T>(
    val data: T?,
    var progress: Boolean,
    val error: Boolean,
    val throwable: Throwable?
)

abstract class BaseWeatherViewModel<T>(
    private val workerScheduler: Scheduler,
    private val resultScheduler: Scheduler
) : ViewModel() {

    private val mutableLiveData = MutableLiveData<ViewObject<T>>().apply {
        value = ViewObject(
            data = null,
            progress = false,
            error = false,
            throwable = null
        )
    }

    private val disposables = CompositeDisposable()

    protected abstract fun createDataObservable(): Observable<T>?

    fun liveData(): LiveData<ViewObject<T>> = mutableLiveData

    fun refreshData() {
        fetchData()
    }

    protected fun fetchData() {
        val currentData = mutableLiveData.value
        if (currentData?.progress == true) {
            return
        }
        mutableLiveData.postValue(currentData?.copy(progress = true))

        if (createDataObservable() != null) {
            disposables.add(
                createDataObservable()!!
                    .subscribeOn(workerScheduler)
                    .observeOn(resultScheduler)
                    .subscribe(
                        {
                            mutableLiveData.postValue(
                                currentData?.copy(
                                    data = it,
                                    progress = false,
                                    error = false,
                                    throwable = null
                                )
                            )
                        },
                        {
                            mutableLiveData.postValue(
                                currentData?.copy(
                                    data = null,
                                    progress = false,
                                    error = true,
                                    throwable = it
                                )
                            )
                        }
                    ))
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

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}