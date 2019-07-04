package com.weather.weathermvvmapp.data.database.future_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.weathermvvmapp.data.database.FUTURE_WEATHER_DATABASE_NAME
import com.weather.weathermvvmapp.data.database.FUTURE_WEATHER_ID
import io.reactivex.Observable


@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherEntries: FutureWeatherModel)

    @Query("select * from $FUTURE_WEATHER_DATABASE_NAME where id = $FUTURE_WEATHER_ID")
    fun getFutureWeather(): Observable<FutureWeatherModel>
}