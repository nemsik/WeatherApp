package com.example.bartek.weatherapp.Database;

import android.os.AsyncTask;

public class InsertAsynctask extends AsyncTask<CurrentWeatherModel, Void, Void> {

    private CurrentWeatherModelDao dao;

    public InsertAsynctask(CurrentWeatherModelDao dao) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(CurrentWeatherModel... params) {
        CurrentWeatherModel currentWeatherModel = params[0];
        dao.insertWeather(currentWeatherModel);
        return null;
    }
}
