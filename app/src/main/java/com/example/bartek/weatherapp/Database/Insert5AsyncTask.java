package com.example.bartek.weatherapp.Database;

import android.os.AsyncTask;

public class Insert5AsyncTask extends AsyncTask<FiveHoursWeather, Void, Void> {

    private FiveHoursWeatherDao dao;
    public Insert5AsyncTask(FiveHoursWeatherDao dao){
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(FiveHoursWeather... params) {
        FiveHoursWeather fiveHoursWeather = params[0];
        dao.insert(fiveHoursWeather);
        return null;
    }
}
