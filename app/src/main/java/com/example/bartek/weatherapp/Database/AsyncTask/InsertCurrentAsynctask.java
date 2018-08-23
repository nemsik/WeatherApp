package com.example.bartek.weatherapp.Database.AsyncTask;

import android.os.AsyncTask;


import com.example.bartek.weatherapp.Database.Dao.CurrentWeatherDao;
import com.example.bartek.weatherapp.Database.Model.CurrentWeather;

public class InsertCurrentAsynctask extends AsyncTask<CurrentWeather, Void, Void> {

    private CurrentWeatherDao dao;

    public InsertCurrentAsynctask(CurrentWeatherDao dao) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(CurrentWeather... params) {
        CurrentWeather currentWeather = params[0];
        dao.insertWeather(currentWeather);
        return null;
    }
}
