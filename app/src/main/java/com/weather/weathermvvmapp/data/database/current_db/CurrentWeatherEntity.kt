package com.weather.weathermvvmapp.data.database.current_db


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.weather.weathermvvmapp.data.database.CURRENT_WEATHER_DATABASE_NAME
import com.weather.weathermvvmapp.data.database.CURRENT_WEATHER_ID
import com.weather.weathermvvmapp.data.database.WeatherConverter
import com.weather.weathermvvmapp.data.database.entity.Coord
import com.weather.weathermvvmapp.data.database.entity.Main
import com.weather.weathermvvmapp.data.database.entity.Weather
import com.weather.weathermvvmapp.data.database.entity.Wind

@Entity(tableName = CURRENT_WEATHER_DATABASE_NAME)
data class CurrentWeather(
    @SerializedName("base")
    val base: String,

    @Embedded(prefix = "coord_")
    @SerializedName("coord")
    val coord: Coord,

    @SerializedName("dt")
    val dt: Double,

    @Embedded(prefix = "main_")
    @SerializedName("main")
    val main: Main,

    @SerializedName("name")
    val name: String,

    @SerializedName("visibility")
    val visibility: Int,

    @SerializedName("weather")
    @Embedded
    @TypeConverters(WeatherConverter::class)
    val weather: ArrayList<Weather>,

    @Embedded(prefix = "wind_")
    @SerializedName("wind")
    val wind: Wind
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}