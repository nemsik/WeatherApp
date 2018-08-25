package com.example.bartek.weatherapp.Internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;

public class InternetReceiver extends BroadcastReceiver {

    public final static String intentAction = "InternetReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent().setAction(intentAction);
        if (isConnected()){
            Log.e("NetReceiver", "Internet is connected");
            i.putExtra(intentAction, true);
        }
        else {
            Log.e("NetReceiver", "Internet is not connected");
            i.putExtra(intentAction, false);
        }

        context.sendBroadcast(i);
    }

    public boolean isConnected() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { Log.e("ERROR", "IOException",e); }
        catch (InterruptedException e) { Log.e("ERROR", "InterruptedException",e); }

        return false;
    }

}
