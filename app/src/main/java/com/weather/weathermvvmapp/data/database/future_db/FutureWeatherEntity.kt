package com.weather.weathermvvmapp.data.database.future_db


import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.weather.weathermvvmapp.data.database.FUTURE_WEATHER_DATABASE_NAME
import com.weather.weathermvvmapp.data.database.FUTURE_WEATHER_ID
import com.weather.weathermvvmapp.data.database.FutureWeatherConverter
import com.weather.weathermvvmapp.data.network.response.FutureWeather
import com.weather.weathermvvmapp.data.network.response.FutureWeatherListObject
import kotlinx.android.parcel.Parcelize

@Entity(tableName = FUTURE_WEATHER_DATABASE_NAME)
data class FutureWeatherModel(
    @Embedded
    @TypeConverters(FutureWeatherConverter::class)
    val listWeather: ArrayList<FutureWeatherListObjectModel>?
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = FUTURE_WEATHER_ID
}

@Parcelize
data class FutureWeatherListObjectModel(
    val clouds: Int,
    val deg: Int,
    val dt: Long,
    val humidity: Int,
    val pressure: Double,
    val snow: Double,
    val speed: Double,
    val max: Double,
    val min: Double,
    val description: String,
    val icon: String,
    val main: String
) : Parcelable

fun FutureWeather.fromApiDataToFutureWeatherModel(): FutureWeatherModel {
    return FutureWeatherModel(createArrayFromApiWeatherList(this.listWeather))
}

private fun createArrayFromApiWeatherList(listFutureWeather: ArrayList<FutureWeatherListObject>): ArrayList<FutureWeatherListObjectModel> {
    val listFutureWeatherModel = arrayListOf<FutureWeatherListObjectModel>()
    if (listFutureWeather.isNotEmpty()) {
        listFutureWeather.forEach {
            val futureWeatherListObjectModel = FutureWeatherListObjectModel(
                it.clouds,
                it.deg,
                it.dt,
                it.humidity,
                it.pressure,
                it.snow,
                it.speed,
                it.temp.max,
                it.temp.min,
                it.weather[0].description,
                it.weather[0].icon,
                it.weather[0].main
            )
            listFutureWeatherModel.add(futureWeatherListObjectModel)
        }
    }
    return listFutureWeatherModel
}
