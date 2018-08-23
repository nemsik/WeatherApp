package com.example.bartek.weatherapp;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.example.bartek.weatherapp.Adapter.ViewPageAdapter;
import com.example.bartek.weatherapp.Database.CurrentWeatherModel;
import com.example.bartek.weatherapp.Database.CurrentWeatherModelDao;
import com.example.bartek.weatherapp.Database.Database;
import com.example.bartek.weatherapp.Database.DatabaseRepo;
import com.example.bartek.weatherapp.Internet.InternetReceiver;
import com.example.bartek.weatherapp.Internet.LocationService;
import com.example.bartek.weatherapp.Model.WeatherResult;
import com.example.bartek.weatherapp.Retrofit.IOpenWeatherMap;
import com.example.bartek.weatherapp.Retrofit.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static String api_key = "6c2e5dce1340e58db354e229ebfc54b2";
    private final String TAG = "MainActivity";
    public static Location current_location = null;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CoordinatorLayout coordinatorLayout;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Location location;
    private LocationRequest locationRequest;
    private DatabaseRepo databaseRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Retrofit retrofit = RetrofitClient.getInstance();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.root_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseRepo = DatabaseRepo.getInstance(getApplication());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        Dexter.withActivity(this).withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startLocationService();
                createLocationRequest();
                getLastLocation();
                requestLocationUpdates();
                setupViewPager(viewPager);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                Snackbar.make(coordinatorLayout, "Permission Denied", Snackbar.LENGTH_LONG).show();
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        }).check();

    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(TodayWeatherFragment.getInstance(), "Today");
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void startLocationService(){
        startService(new Intent(this, LocationService.class));
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * 30);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    private void onNewLocation(Location location) {
        Log.d(TAG, "NewLocation: " + location);
        //updateRoom(location);
        databaseRepo.insertFromLocation(location);
        databaseRepo.insert5days(location);
    }


}
