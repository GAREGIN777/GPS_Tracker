package com.example.gps_tracker.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class AppLauncher {

    public static void launchApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // App not found, handle the situation accordingly
            // For example, you can display a message to the user
        }
    }
}
