package com.weather.weathermvvmapp.di.modules

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.weather.weathermvvmapp.data.network.API_KEY
import com.weather.weathermvvmapp.data.network.ApiWeatherInterface
import com.weather.weathermvvmapp.data.network.UNITS
import com.weather.weathermvvmapp.data.network.WEATHER_URL
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    private val requestInterceptor = Interceptor { chain ->

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

        return@Interceptor chain.proceed(request)
    }

    /**
     * Provide the Api Service implementation
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the api service implementation
     */
    @Provides
    @Singleton
    fun createApiInterface(): ApiWeatherInterface {
        val okClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(requestInterceptor) // add key as query parameter for all calls
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okClient)
            .build()
        return retrofit.create(ApiWeatherInterface::class.java)
    }
}