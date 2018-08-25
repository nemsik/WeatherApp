package com.example.bartek.weatherapp.Database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.bartek.weatherapp.Database.Model.CurrentWeather;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CurrentWeatherDao {
    @Query("SELECT * FROM CurrentWeather ORDER BY id DESC LIMIT 1")
    LiveData<CurrentWeather> getCurrent();

    @Query("SELECT * FROM CurrentWeather")
    LiveData<List<CurrentWeather>> getAll();

    @Insert(onConflict = REPLACE)
    void insertWeather(CurrentWeather currentWeather);

    @Update
    void updateWeather(CurrentWeather currentWeather);
}
