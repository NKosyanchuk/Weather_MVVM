package com.example.weathermvvmapp.network

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val API_KEY = "b6907d289e10d714a6e88b30761fae22"
const val WEATHER_URL = "https://openweathermap.org/data/2.5/"
const val WEATHER_ICON_URL = "https://openweathermap.org/img/w/"

private val requestInterceptor = Interceptor { chain ->

    val url = chain.request()
        .url()
        .newBuilder()
        .addQueryParameter("appid", API_KEY)
        .build()
    val request = chain.request()
        .newBuilder()
        .url(url)
        .build()

    return@Interceptor chain.proceed(request)
}

fun createApiInterface(): ApiWeatherInterface {
    val okClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(requestInterceptor) // add key as query parameter for all calls
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(WEATHER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okClient)
        .build()
    return retrofit.create(ApiWeatherInterface::class.java)
}

fun getWorkerScheduler() = Schedulers.io()

fun getResultScheduler(): Scheduler = AndroidSchedulers.mainThread()