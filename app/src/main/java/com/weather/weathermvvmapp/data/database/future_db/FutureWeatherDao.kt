package com.weather.weathermvvmapp.data.database.future_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weather.weathermvvmapp.data.database.FUTURE_WEATHER_DATABASE_NAME


@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFutureListAll(futureWeatherListObjectModel: List<FutureWeatherListObjectModel>)

    @Query("SELECT * FROM $FUTURE_WEATHER_DATABASE_NAME")
    fun getFutureList(): List<FutureWeatherListObjectModel>

    @Query("SELECT * FROM $FUTURE_WEATHER_DATABASE_NAME WHERE dt = :day")
    fun getSpecificFutureObject(day: Long): FutureWeatherListObjectModel
}