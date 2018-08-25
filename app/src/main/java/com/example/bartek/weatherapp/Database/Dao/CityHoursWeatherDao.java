package com.example.bartek.weatherapp.Database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.example.bartek.weatherapp.Database.Model.CityHoursWeather;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CityHoursWeatherDao {
    @Query("SELECT * FROM cityHoursWeather ORDER BY id DESC LIMIT 1")
    LiveData<CityHoursWeather> getCurrent();

    @Insert(onConflict = REPLACE)
    void insert(CityHoursWeather fiveHoursWeather);

}
