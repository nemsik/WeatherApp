package com.example.bartek.weatherapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface SingleWeatherDao {
    @Insert
    void insert(SingleWeather singleWeather);

    @Query("SElECT * FROM SingleWeather")
    LiveData<SingleWeather> getAllSingleWeather();
}
