package com.example.bartek.weatherapp;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bartek.weatherapp.Database.CurrentWeatherModel;
import com.example.bartek.weatherapp.Database.CurrentWeatherModelDao;
import com.example.bartek.weatherapp.Database.Database;
import com.example.bartek.weatherapp.Database.DatabaseRepo;
import com.example.bartek.weatherapp.Model.WeatherResult;
import com.example.bartek.weatherapp.Retrofit.IOpenWeatherMap;
import com.example.bartek.weatherapp.Retrofit.RetrofitClient;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment implements Observer<CurrentWeatherModel> {

    @BindView(R.id.img_weather) ImageView img_weather;
    @BindView(R.id.textview_city_name) TextView textview_city_name;
    @BindView(R.id.textview_humidity) TextView textview_humidity;
    @BindView(R.id.textview_sunrise) TextView textview_sunrise;
    @BindView(R.id.textview_sunset) TextView textview_sunset;
    @BindView(R.id.textview_pressure) TextView textview_pressure;
    @BindView(R.id.textview_temperature) TextView textview_temperature;
    @BindView(R.id.textview_description) TextView textview_description;
    @BindView(R.id.textview_date_time) TextView textview_date_time;
    @BindView(R.id.textview_wind) TextView textview_wind;
    @BindView(R.id.weather_panel) LinearLayout weather_panel;
    @BindView(R.id.progrssbar) ProgressBar progressBar;

    static TodayWeatherFragment instance;
    private ViewModel viewModel;

    private String TAG = "fragment";
    

    public static TodayWeatherFragment getInstance() {
        if(instance == null) instance = new TodayWeatherFragment();
        return instance;
    }


    public TodayWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);
        ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(getActivity()).get(ViewModel.class);
        viewModel.getCurrentWeatherModelLiveData().observe(this, this);
        return view;
    }


    private String dateConv(long dt){
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm EEE MM yyyy");
        String formatted = sdf.format(date);
        return formatted;
    }

    private void setWeatherInformation(CurrentWeatherModel data){
        try{
            textview_city_name.setText(data.getCity_name());
            textview_description.setText(data.getCity_name());
            textview_temperature.setText(String.valueOf(data.getTemp()));
            textview_date_time.setText(dateConv(data.getDt()));
            textview_pressure.setText(String.valueOf(data.getPressure()));
            textview_humidity.setText(String.valueOf(data.getHumidity()));
            progressBar.setVisibility(View.INVISIBLE);
        }catch (Exception e){}
    }

    @Override
    public void onChanged(@Nullable CurrentWeatherModel currentWeatherModel) {
        Log.d(TAG, "onChanged: observer" + currentWeatherModel.getCity_name());
        setWeatherInformation(currentWeatherModel);
    }
}
