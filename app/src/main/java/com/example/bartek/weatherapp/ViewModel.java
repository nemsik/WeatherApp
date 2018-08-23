package com.example.bartek.weatherapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.bartek.weatherapp.Database.CurrentWeatherModel;
import com.example.bartek.weatherapp.Database.DatabaseRepo;
import com.example.bartek.weatherapp.Model.WeatherResult;
import com.example.bartek.weatherapp.Retrofit.IOpenWeatherMap;
import com.example.bartek.weatherapp.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewModel extends AndroidViewModel {
    private final String TAG = "ViewModel";

    private LiveData<CurrentWeatherModel> currentWeatherModelLiveData;
    private LiveData<List<CurrentWeatherModel>> listLiveData;
    private DatabaseRepo repo;

    public ViewModel(Application application){
        super(application);
        repo = DatabaseRepo.getInstance(application);
        currentWeatherModelLiveData = repo.getCurrentWeatherModel();
        listLiveData = repo.getListLiveData();
    }

    public LiveData<CurrentWeatherModel> getCurrentWeatherModelLiveData() {
        return currentWeatherModelLiveData;
    }

    public LiveData<List<CurrentWeatherModel>> getAll(){
        return listLiveData;
    }

    public void insert(WeatherResult weatherResult){
        repo.insert(weatherResult);
    }

}
