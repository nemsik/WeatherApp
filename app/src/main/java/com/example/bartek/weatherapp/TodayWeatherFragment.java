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
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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
    @BindView(R.id.textview_wind)
    TextView textview_wind;
    @BindView(R.id.textviewRain)
    TextView textviewRain;
    @BindView(R.id.lineChart)
    CombinedChart mChart;

    static TodayWeatherFragment instance;
    private ViewModel viewModel;

    private String TAG = "fragment";

    private ArrayList<Entry> entriesTemp;
    private ArrayList<BarEntry> entriesRain;

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
        //Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);
        ButterKnife.bind(this, view);

        progressBar.setVisibility(View.VISIBLE);
        textview_city_name.setVisibility(View.INVISIBLE);
        textview_description.setVisibility(View.INVISIBLE);
        textview_temperature.setVisibility(View.INVISIBLE);
        textview_wind.setVisibility(View.INVISIBLE);
        textview_pressure.setVisibility(View.INVISIBLE);
        textview_humidity.setVisibility(View.INVISIBLE);
        textview_sunrise.setVisibility(View.INVISIBLE);
        textview_sunset.setVisibility(View.INVISIBLE);
        textviewRain.setVisibility(View.INVISIBLE);

        entriesTemp = new ArrayList<>();
        entriesRain = new ArrayList<>();

        text = new String[9];

        viewModel = ViewModelProviders.of(getActivity()).get(ViewModel.class);
        viewModel.getCurrentWeatherLiveData().observe(this, this);
        viewModel.getSingleWeatherLiveData().observe(this, new Observer<List<SingleWeather>>() {
            @Override
            public void onChanged(@Nullable List<SingleWeather> singleWeathers) {
                if (singleWeathers != null) {
                    Log.e(TAG, "onChanged: SINGLE");
                    //Log.e(TAG, "sizse SINGLE" + singleWeathers.size());
                    if (singleWeathers.size() == 10) {
                        entriesTemp.clear();
                        entriesRain.clear();
                        for (int i = 0; i < 9; i++) {
                            entriesTemp.add(new Entry(i, (float) singleWeathers.get(i).getTemp()));
                            entriesRain.add(new BarEntry(i, (float) singleWeathers.get(i).getRain()));
                            text[i] = dateConv(singleWeathers.get(i).getDt());
                        }
                        updateChart(entriesTemp, entriesRain);
                        Log.d(TAG, "size " + text.length);
                        for (int i = 0; i < text.length; i++) {
                            Log.d(TAG, " " + text[i]);
                        }
                    }
                }
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
            textviewRain.setVisibility(View.INVISIBLE);

            textview_city_name.setText(data.getCity_name() + ", " + data.getCountry());
            textview_description.setText(data.getWeather_desc());
            textview_temperature.setText(String.valueOf(data.getTemp() + "°C"));
            textview_wind.setText("wind: " + String.valueOf(data.getWind_speed()) + " m/s");
            textview_pressure.setText("pressure: " + String.valueOf(data.getPressure()) + " hpa");
            textview_humidity.setText("humidity: " + String.valueOf(data.getHumidity()) + " %");
            textview_sunrise.setText("sunrise: " + String.valueOf(dateConv(data.getSunrise())));
            textview_sunset.setText("sunset: " + String.valueOf(dateConv(data.getSunset())));
            textviewRain.setText("precipitation: " + String.valueOf(data.getRain()) +" mm");

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
            textviewRain.setVisibility(View.VISIBLE);

            progressBar.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
        }
    }

    private void updateChart(ArrayList<Entry> entriesTemp, ArrayList<BarEntry> entriesRain) {

        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);

        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int)value + "°C";
            }
        });
        leftAxis.setTextColor(Color.BLUE);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                return (int)value +"mm";
            }
        });
        rightAxis.setTextColor(Color.GRAY);
        rightAxis.setDrawGridLines(false);



        XAxis xAxis = mChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);


        xAxis.setValueFormatter(new IndexAxisValueFormatter(text));

        CombinedData combinedData = new CombinedData();

        LineDataSet lineDataset = new LineDataSet(entriesTemp, "Temperature");
        lineDataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataset.setCubicIntensity(0.15f);
        lineDataset.setLineWidth(4.8f);
        lineDataset.setColor(Color.BLUE);
        lineDataset.setDrawValues(false);
        lineDataset.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet barDataSet = new BarDataSet(entriesRain, "Precipitation");
        barDataSet.setDrawValues(false);
        barDataSet.setColor(Color.GRAY);
        barDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);


        LineData lineData = new LineData(lineDataset);
        BarData barData = new BarData(barDataSet);


        combinedData.setData(lineData);
        combinedData.setData(barData);

        leftAxis.setAxisMaximum(combinedData.getYMax() + 1.5f);
        leftAxis.setAxisMinimum(lineData.getYMin() - 1.5f);

        mChart.setData(combinedData);
        mChart.setScaleEnabled(false);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: " + e.toString());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    @Override
    public void onChanged(@Nullable CurrentWeather currentWeather) {
        if (currentWeather != null) {
            Log.d(TAG, "CURRENT ID" + currentWeather.getId());
            setWeatherInformation(currentWeather);
        }
    }


}
