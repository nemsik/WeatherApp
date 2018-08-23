package com.example.bartek.weatherapp.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.Update;

import com.example.bartek.weatherapp.Model.WeatherResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CurrentWeatherModelDao {
    @Query("SELECT * FROM CurrentWeatherModel ORDER BY id DESC LIMIT 1")
    LiveData<CurrentWeatherModel> getCurrent();

    @Query("SELECT * FROM CurrentWeatherModel")
    LiveData<List<CurrentWeatherModel>> getAll();

    @Insert(onConflict = REPLACE)
    void insertWeather(CurrentWeatherModel currentWeatherModel);

    @Update
    void updateWeather(CurrentWeatherModel currentWeatherModel);
}
