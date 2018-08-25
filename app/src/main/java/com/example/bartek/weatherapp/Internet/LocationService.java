package com.example.bartek.weatherapp.Internet;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.bartek.weatherapp.Database.DatabaseRepo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocationService extends Service {

    private final String TAG = "LocationService";

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Location location;
    private LocationRequest locationRequest;
    private DatabaseRepo databaseRepo;

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

        IntentFilter filter = new IntentFilter();
        filter.addAction(InternetReceiver.intentAction);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "onReceive: " + intent.getBooleanExtra(InternetReceiver.intentAction, false));
                boolean bool = intent.getBooleanExtra(InternetReceiver.intentAction, false);
                if (!bool){
                    try{
                        final Task<Void> voidTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    }catch (SecurityException exp) {
                        Log.e(TAG, " Security exception while removeLocationUpdates");
                    }
                }else {
                    createLocationRequest();
                    getLastLocation();
                    requestLocationUpdates();
                }
            }
        };

        registerReceiver(broadcastReceiver,filter);

    }

    private void onNewLocation(Location location) {
        Log.d(TAG, "NewLocation: " + location);
        databaseRepo.insertFromLocation(location);
        databaseRepo.insert5days(location);
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * 60 * 15);
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
