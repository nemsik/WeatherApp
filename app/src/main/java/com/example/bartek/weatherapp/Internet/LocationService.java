package com.example.bartek.weatherapp.Internet;

import android.app.Service;
import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.bartek.weatherapp.Database.CurrentWeatherModel;
import com.example.bartek.weatherapp.Database.Database;
import com.example.bartek.weatherapp.Database.DatabaseRepo;
import com.example.bartek.weatherapp.MainActivity;
import com.example.bartek.weatherapp.Model.WeatherResult;
import com.example.bartek.weatherapp.Retrofit.IOpenWeatherMap;
import com.example.bartek.weatherapp.Retrofit.RetrofitClient;
import com.example.bartek.weatherapp.ViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationService extends Service {

    private final String TAG = "LocationService";

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Location location;
    private LocationRequest locationRequest;
    private DatabaseRepo databaseRepo;
//    private IOpenWeatherMap iOpenWeatherMap;
//    private Retrofit retrofit;
//    private ViewModel viewModel;

    public LocationService( ) {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");

        databaseRepo = DatabaseRepo.getInstance(getApplication());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };
        createLocationRequest();
        getLastLocation();
        requestLocationUpdates();

//        retrofit = RetrofitClient.getInstance();
//        iOpenWeatherMap = retrofit.create(IOpenWeatherMap.class);

    }

    private void onNewLocation(Location location) {
        Log.d(TAG, "NewLocation: " + location);
        //updateRoom(location);
        databaseRepo.insertFromLocation(location);
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * 60);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void getLastLocation() {
        try {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        location = task.getResult();
                        Log.d(TAG, "onComplete: " + location.toString());
                    } else Log.i(TAG, "Failed to get location");
                }
            });
        } catch (SecurityException securityException) {
            Log.i(TAG, "Lost location permission " + securityException);
        }

    }

    private void requestLocationUpdates() {
        Log.d(TAG, "request location updates");
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        } catch (SecurityException securityException) {
            Log.i(TAG, "Lost location permission. Could not request updates. " + securityException);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    private void updateRoom(Location location) {
//        try {
//            Log.d(TAG, "loadData: ");
//            iOpenWeatherMap.getWeatherByLatLng(String.valueOf(location.getLatitude()),
//                    String.valueOf(location.getLongitude())
//                    , MainActivity.api_key, "metric").enqueue(new Callback<WeatherResult>() {
//                @Override
//                public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
//                    Log.d(TAG, "onResponse: " + response.body().getName());
//                    databaseRepo.insert(response.body());
//                }
//
//                @Override
//                public void onFailure(Call<WeatherResult> call, Throwable t) {
//                    Log.e(TAG, "onFailure: " + t.toString());
//                }
//            });
//        } catch (Exception e) {
//            Log.e(TAG, "loadData: Error " + e.toString());
//        }
//    }

}
