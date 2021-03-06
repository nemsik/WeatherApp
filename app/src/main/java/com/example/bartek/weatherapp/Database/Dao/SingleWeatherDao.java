package com.example.bartek.weatherapp.Database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.bartek.weatherapp.Database.Model.SingleWeather;

import java.util.List;
import java.util.ListIterator;

@Dao
public interface SingleWeatherDao {
    @Insert
    void insert(SingleWeather singleWeather);

    @Query("SElECT * FROM SingleWeather WHERE fiveId=:fiveId ORDER BY id ASC LIMIT 10")
    LiveData<List<SingleWeather>> getAllSingleWeather(final int fiveId);


}
