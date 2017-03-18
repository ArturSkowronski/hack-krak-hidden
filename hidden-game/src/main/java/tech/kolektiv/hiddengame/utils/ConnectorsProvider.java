package tech.kolektiv.hiddengame.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by arturskowronski on 07/07/16.
 */
public class ConnectorsProvider {

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void checkConnectivity(Activity context, Class clazz) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gpsEnabled) {
            Intent i = new Intent(context, clazz);
            context.startActivity(i);
        } else {
            new AlertDialog.Builder(context)
                    .setTitle("Enable GPS")
                    .setMessage("This Application requires the use of Mobile GPS. " +
                            "Do you wish to enable this feature")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                    })
                    .show();
        }
    }
}
