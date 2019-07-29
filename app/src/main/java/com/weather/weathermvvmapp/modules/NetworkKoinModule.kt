package com.weather.weathermvvmapp.modules

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.weather.weathermvvmapp.data.network.ApiWeatherInterface
import com.weather.weathermvvmapp.data.network.NetworkProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


const val API_KEY = "9559b7571ec00e4dd0b80e9337083adc"
const val UNITS = "metric"
const val WEATHER_URL = "https://api.openweathermap.org//data/2.5/"
const val WEATHER_ICON_URL = "https://openweathermap.org/img/w/"


val networkKoinModule = module {

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        Interceptor { chain ->

            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("appid", API_KEY)
                .addQueryParameter("units", UNITS)
                .build()
            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            chain.proceed(request)
        }
    }

    factory {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<Interceptor>())
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(get<OkHttpClient.Builder>().build())
            .build()
        retrofit.create(ApiWeatherInterface::class.java)
    }

    factory {
        NetworkProvider(androidContext())
    }
}