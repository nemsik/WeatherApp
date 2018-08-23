package com.example.bartek.weatherapp;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.bartek.weatherapp.Database.Model.CityHoursWeather;
import com.example.bartek.weatherapp.Database.Model.CurrentWeather;
import com.example.bartek.weatherapp.Database.Model.SingleWeather;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment implements Observer<CurrentWeather> {

    @BindView(R.id.img_weather) ImageView img_weather;
    @BindView(R.id.textview_city_name) TextView textview_city_name;
    @BindView(R.id.textview_temperature) TextView textview_temperature;
    @BindView(R.id.textview_temperature_max) TextView textview_temp_max;
    @BindView(R.id.textview_temperature_min) TextView textview_temp_min;
    @BindView(R.id.textview_description) TextView textview_description;

    //@BindView(R.id.weather_panel) LinearLayout weather_panel;
    @BindView(R.id.progressBar) ProgressBar progressBar;

//    @BindView(R.id.textview_humidity) TextView textview_humidity;
//    @BindView(R.id.textview_sunrise) TextView textview_sunrise;
//    @BindView(R.id.textview_sunset) TextView textview_sunset;
//    @BindView(R.id.textview_pressure) TextView textview_pressure;
//@BindView(R.id.textview_date_time) TextView textview_date_time;
//@BindView(R.id.textview_wind) TextView textview_wind;

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
        viewModel.getCurrentWeatherLiveData().observe(this, this);
        viewModel.getSingleWeatherLiveData().observe(this, new Observer<List<SingleWeather>>() {
            @Override
            public void onChanged(@Nullable List<SingleWeather> singleWeathers) {
                if(singleWeathers != null){
                    Log.d(TAG, "onChanged: SINGLE");
                    for(int i=0; i<singleWeathers.size()-1; i++){
                        Log.e(TAG, "SINGLE TEMP "+singleWeathers.get(i).getTemp());
                    }
                }
            }
        });

        viewModel.getCityHoursWeatherLiveData().observe(this, new Observer<CityHoursWeather>() {
            @Override
            public void onChanged(@Nullable CityHoursWeather cityHoursWeather) {
                if(cityHoursWeather != null) Log.e(TAG, "FRAGMENT " +cityHoursWeather.getId());
            }
        });
        return view;
    }


    private String dateConv(long dt){
        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm EEE MM yyyy");
        String formatted = sdf.format(date);
        return formatted;
    }

    private void setWeatherInformation(CurrentWeather data){
        try{
            progressBar.setVisibility(View.VISIBLE);
            textview_city_name.setVisibility(View.INVISIBLE);
            textview_description.setVisibility(View.INVISIBLE);
            textview_temperature.setVisibility(View.INVISIBLE);
            textview_temp_max.setVisibility(View.INVISIBLE);
            textview_temp_min.setVisibility(View.INVISIBLE);


            textview_city_name.setText(data.getCity_name()+", "+data.getCountry());
            textview_description.setText(data.getWeather_desc());
            textview_temperature.setText(String.valueOf(data.getTemp() + "°C"));
            textview_temp_max.setText("max: "+ String.valueOf(data.getMax_temp() + "°C"));
            textview_temp_min.setText("min: "+String.valueOf(data.getMin_temp() + "°C"));

            String path = "https://openweathermap.org/img/w/" + data.getWeather_icon().toString() + ".png";
            Picasso.get().load(path).into(img_weather);


            textview_city_name.setVisibility(View.VISIBLE);
            textview_description.setVisibility(View.VISIBLE);
            textview_temperature.setVisibility(View.VISIBLE);
            textview_temp_max.setVisibility(View.VISIBLE);
            textview_temp_min.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }catch (Exception e){}
    }

    @Override
    public void onChanged(@Nullable CurrentWeather currentWeather) {
        if(currentWeather != null) {
//            Log.d(TAG, "onChanged: observer" + currentWeatherModel.getCity_name());
//            Log.d(TAG, "onChanged: observer" + currentWeatherModel.getMax_temp());
//            Log.d(TAG, "onChanged: observer" + currentWeatherModel.getMin_temp());
            Log.e(TAG, "CURRENT ID" + currentWeather.getId());
            setWeatherInformation(currentWeather);
        }
    }


}
