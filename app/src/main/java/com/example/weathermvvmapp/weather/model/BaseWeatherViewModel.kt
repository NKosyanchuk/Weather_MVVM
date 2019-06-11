package com.example.weathermvvmapp.weather.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

abstract class BaseWeatherViewModel<T>(
    private val workerScheduler: Scheduler,
    private val resultScheduler: Scheduler
) : ViewModel() {

    private val data = MutableLiveData<ViewObject<T>>().apply {
        value = ViewObject(data = null, progress = false, error = false, throwable = null)

    }

    private val disposables = CompositeDisposable()

    protected abstract fun createDataObservable(): Observable<T>?

    fun liveData(): LiveData<ViewObject<T>> = data

    protected fun fetchData() {
        val currentData = data.value
        if (currentData?.progress == true) {
            return
        }
        data.postValue(currentData?.copy(progress = true))

        if (createDataObservable() != null) {
            disposables.add(
                createDataObservable()!!
                    .subscribeOn(workerScheduler)
                    .observeOn(resultScheduler)
                    .subscribe(
                        {
                            data.postValue(currentData?.copy(data = it, progress = false, error = false, throwable = null))
                        },
                        {
                            data.postValue(currentData?.copy(data = null, progress = false, error = true, throwable = it))
                        }
                    ))
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}

data class ViewObject<T>(
    val data: T?,
    var progress: Boolean,
    val error: Boolean,
    val throwable: Throwable?
)