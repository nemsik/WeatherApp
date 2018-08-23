package com.example.bartek.weatherapp.Retrofit;

import com.github.leonardoxh.livedatacalladapter.LiveDataCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit instance;

    public static Retrofit getInstance(){
        if(instance == null){
            instance = new Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                    .build();
        }
        return instance;
    }
}
