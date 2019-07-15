package com.weather.weathermvvmapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


const val API_KEY = "9559b7571ec00e4dd0b80e9337083adc"
const val UNITS = "metric"
const val WEATHER_URL = "https://api.openweathermap.org//data/2.5/"
const val WEATHER_ICON_URL = "https://openweathermap.org/img/w/"

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

class NetworkProvider(val context: Context?) {

    fun isDeviceOnline(): Boolean {
        val connectivityManager = context?.getSystemService(
            Context
                .CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BaseUrlChangingInterceptor : Interceptor {

    private var httpUrl: HttpUrl? = HttpUrl.parse(WEATHER_URL)


    fun setInterceptor(url: String) {
        httpUrl = HttpUrl.parse(url)!!
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request()
            .newBuilder()
            .url(httpUrl)
            .build()
            .url()
            .newBuilder()
            .addQueryParameter("appid", API_KEY)
            .addQueryParameter("units", UNITS)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }

    companion object {
        private var sInterceptor: BaseUrlChangingInterceptor? = null

        fun get(): BaseUrlChangingInterceptor {
            if (sInterceptor == null) {
                sInterceptor = BaseUrlChangingInterceptor()
            }
            return sInterceptor as BaseUrlChangingInterceptor
        }
    }
}