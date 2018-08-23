package com.example.bartek.weatherapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface FiveHoursWeatherDao {
    @Query("SELECT * FROM fiveWeather ORDER BY id DESC LIMIT 1")
    LiveData<FiveHoursWeather> getAllFive();

    @Insert
    void insert(FiveHoursWeather fiveHoursWeather);

}
