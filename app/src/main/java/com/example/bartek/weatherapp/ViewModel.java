package com.example.bartek.weatherapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.bartek.weatherapp.Database.DatabaseRepo;
import com.example.bartek.weatherapp.Database.Model.CityHoursWeather;
import com.example.bartek.weatherapp.Database.Model.CurrentWeather;
import com.example.bartek.weatherapp.Database.Model.SingleWeather;
import com.example.bartek.weatherapp.Model.WeatherResult;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private final String TAG = "ViewModel";

    private LiveData<CurrentWeather> currentWeatherLiveData;
    private LiveData<CityHoursWeather> cityHoursWeatherLiveData;
    private LiveData<List<SingleWeather>> singleWeatherLiveData;
    private DatabaseRepo repo;

    public ViewModel(Application application){
        super(application);
        repo = DatabaseRepo.getInstance(application);

        currentWeatherLiveData = repo.getCurrentWeatherLiveData();
        cityHoursWeatherLiveData = repo.getCityHoursWeatherLiveData();
        singleWeatherLiveData = repo.getSingleWeatherLiveData();
    }

    public LiveData<CurrentWeather> getCurrentWeatherLiveData() {
        return currentWeatherLiveData;
    }

    public LiveData<CityHoursWeather> getCityHoursWeatherLiveData() {
        return cityHoursWeatherLiveData;
    }

    public LiveData<List<SingleWeather>> getSingleWeatherLiveData() {
        return singleWeatherLiveData;
    }

    public void insert(WeatherResult weatherResult){
        repo.insert(weatherResult);
    }

}
