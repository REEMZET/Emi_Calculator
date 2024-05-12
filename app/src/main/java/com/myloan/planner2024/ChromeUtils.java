package com.myloan.planner2024;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

public class ChromeUtils {

    // Function to check if Chrome is installed on the device
    public static boolean isChromeInstalled(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo("com.android.chrome", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    // Function to launch a URL in Chrome
    public static void openUrlInChrome(Context context,String url) {
            try {
                // Create an Intent with ACTION_VIEW and the URL
                Intent chromeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                // Set the package name for Chrome
                chromeIntent.setPackage("com.android.chrome");

                // Start the activity
                context.startActivity(chromeIntent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Error launching Chrome", Toast.LENGTH_SHORT).show();
            }

    }
}
