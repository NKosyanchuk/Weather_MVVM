package com.weather.weathermvvmapp.data.database.current_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.weathermvvmapp.data.database.CURRENT_WEATHER_DATABASE_NAME
import com.weather.weathermvvmapp.data.database.CURRENT_WEATHER_ID
import io.reactivex.Observable

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherEntry: CurrentWeather)

    @Query("select * from $CURRENT_WEATHER_DATABASE_NAME where id = $CURRENT_WEATHER_ID")
    fun getCurrentWeather(): Observable<CurrentWeather>
}