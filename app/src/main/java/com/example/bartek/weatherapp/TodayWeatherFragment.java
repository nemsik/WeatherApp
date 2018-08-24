package com.example.bartek.weatherapp;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment implements Observer<CurrentWeather> {

    @BindView(R.id.img_weather)
    ImageView img_weather;
    @BindView(R.id.textview_city_name)
    TextView textview_city_name;
    @BindView(R.id.textview_temperature)
    TextView textview_temperature;
    @BindView(R.id.textview_description)
    TextView textview_description;

    //@BindView(R.id.weather_panel) LinearLayout weather_panel;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.textview_humidity)
    TextView textview_humidity;
    @BindView(R.id.textview_sunrise)
    TextView textview_sunrise;
    @BindView(R.id.textview_sunset)
    TextView textview_sunset;
    @BindView(R.id.textview_pressure)
    TextView textview_pressure;
    //@BindView(R.id.textview_date_time) TextView textview_date_time;
    @BindView(R.id.textview_wind)
    TextView textview_wind;
    @BindView(R.id.lineChart)
    LineChart lineChart;

    static TodayWeatherFragment instance;
    private ViewModel viewModel;

    private String TAG = "fragment";

    private ArrayList<Entry> entries;

    private String[] text;


    public static TodayWeatherFragment getInstance() {
        if (instance == null) instance = new TodayWeatherFragment();
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

        entries = new ArrayList<>();

        text = new String[9];

        viewModel = ViewModelProviders.of(getActivity()).get(ViewModel.class);
        viewModel.getCurrentWeatherLiveData().observe(this, this);
        viewModel.getSingleWeatherLiveData().observe(this, new Observer<List<SingleWeather>>() {
            @Override
            public void onChanged(@Nullable List<SingleWeather> singleWeathers) {
                if (singleWeathers != null) {
                    Log.d(TAG, "onChanged: SINGLE");
                    Log.e(TAG, "sizse SINGLE" + singleWeathers.size());
                    entries.clear();
                    if (singleWeathers.size() == 10) {
                        for (int i = 0; i < singleWeathers.size() - 1; i++) {
                            Log.e(TAG, "SINGLE " + singleWeathers.get(i).getTemp() + "DATE " + dateConv(singleWeathers.get(i).getDt()));
                            entries.add(new Entry(i, (float) singleWeathers.get(i).getTemp()));
                            text[i] = dateConv(singleWeathers.get(i).getDt());
                        }
                        updateChart(entries);
                        Log.d(TAG, "size " + text.length);
                        for (int i = 0; i < text.length; i++) {
                            Log.d(TAG, " " + text[i]);
                        }
                    }
                }
            }
        });

        viewModel.getCityHoursWeatherLiveData().observe(this, new Observer<CityHoursWeather>() {
            @Override
            public void onChanged(@Nullable CityHoursWeather cityHoursWeather) {
                if (cityHoursWeather != null) Log.e(TAG, "FRAGMENT " + cityHoursWeather.getId());
            }
        });
        return view;
    }


    private String dateConv(long dt) {
        Date date = new Date(dt * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formatted = sdf.format(date);
        return formatted;
    }

    private void setWeatherInformation(CurrentWeather data) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            textview_city_name.setVisibility(View.INVISIBLE);
            textview_description.setVisibility(View.INVISIBLE);
            textview_temperature.setVisibility(View.INVISIBLE);
            textview_wind.setVisibility(View.INVISIBLE);
            textview_pressure.setVisibility(View.INVISIBLE);
            textview_humidity.setVisibility(View.INVISIBLE);
            textview_sunrise.setVisibility(View.INVISIBLE);
            textview_sunset.setVisibility(View.INVISIBLE);

            textview_city_name.setText(data.getCity_name() + ", " + data.getCountry());
            textview_description.setText(data.getWeather_desc());
            textview_temperature.setText(String.valueOf(data.getTemp() + "°C"));
            textview_wind.setText("wind: " + String.valueOf(data.getWind_speed()) + " m/s");
            textview_pressure.setText("pressure: " + String.valueOf(data.getPressure()) + " hpa");
            textview_humidity.setText("humidity: " + String.valueOf(data.getHumidity()) + " %");
            textview_sunrise.setText("sunrise: " + String.valueOf(dateConv(data.getSunrise())));
            textview_sunset.setText("sunset: " + String.valueOf(dateConv(data.getSunset())));

            String path = "https://openweathermap.org/img/w/" + data.getWeather_icon().toString() + ".png";
            Picasso.get().load(path).into(img_weather);


            textview_city_name.setVisibility(View.VISIBLE);
            textview_description.setVisibility(View.VISIBLE);
            textview_temperature.setVisibility(View.VISIBLE);
            textview_wind.setVisibility(View.VISIBLE);
            textview_pressure.setVisibility(View.VISIBLE);
            textview_humidity.setVisibility(View.VISIBLE);
            textview_sunrise.setVisibility(View.VISIBLE);
            textview_sunset.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
        }
    }

    private void updateChart(ArrayList<Entry> entries) {
        LineDataSet dataset = new LineDataSet(entries, "Temperature");
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setCubicIntensity(0.15f);
        dataset.setLineWidth(4.8f);
        dataset.setColor(Color.BLUE);
        dataset.setDrawValues(true);
        XAxis xAxis = lineChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(text));
        dataset.notifyDataSetChanged();
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getXAxis().setDrawLabels(true);
        lineChart.getLegend().setEnabled(true);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(4f, 0));
        barEntries.add(new BarEntry(6f, 0));
        barEntries.add(new BarEntry(4f, 0));
        barEntries.add(new BarEntry(2f, 0));
        barEntries.add(new BarEntry(4f, 0));
        barEntries.add(new BarEntry(6f, 0));
        barEntries.add(new BarEntry(2f, 0));
        barEntries.add(new BarEntry(8f, 0));
        barEntries.add(new BarEntry(1f, 0));
        BarDataSet barDataSet = new BarDataSet(barEntries, "elo");

        LineData lineData = new LineData(dataset);
        lineChart.setData(lineData);

        lineChart.setScaleEnabled(false);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    @Override
    public void onChanged(@Nullable CurrentWeather currentWeather) {
        if (currentWeather != null) {
//            Log.d(TAG, "onChanged: observer" + currentWeatherModel.getCity_name());
//            Log.d(TAG, "onChanged: observer" + currentWeatherModel.getMax_temp());
//            Log.d(TAG, "onChanged: observer" + currentWeatherModel.getMin_temp());
            Log.e(TAG, "CURRENT ID" + currentWeather.getId());
            setWeatherInformation(currentWeather);
        }
    }


}
