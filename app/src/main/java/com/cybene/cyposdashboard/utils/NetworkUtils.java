package com.cybene.cyposdashboard.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.provider.Settings;
import android.widget.Toast;

public class NetworkUtils {
    // Modern way (Android 6.0+)
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return false;

        Network network = manager.getActiveNetwork();
        if (network == null) return false;

        NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    public static void showNoInternetDialog(final Context context, boolean allowExit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please enable WiFi or mobile data to continue.");

        // Open Settings
        builder.setPositiveButton("Enable Internet", (dialog, which) -> {
            context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            dialog.dismiss();
        });

        // Retry
        builder.setNeutralButton("Retry", (dialog, which) -> {
            if (isNetworkAvailable(context)) {
                Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT).show();
            } else {
                showNoInternetDialog(context, allowExit);
            }
        });

        // Exit App (optional)
        if (allowExit) {
            builder.setNegativeButton("Exit", (dialog, which) -> {
                if (context instanceof android.app.Activity) {
                    ((android.app.Activity) context).finish();
                }
            });
        }

        builder.setCancelable(false);
        builder.show();
    }

    // Optional: Listen for network changes dynamically
    public static void registerNetworkCallback(Context context, ConnectivityManager.NetworkCallback callback) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            manager.registerDefaultNetworkCallback(callback);
        }
    }
}
