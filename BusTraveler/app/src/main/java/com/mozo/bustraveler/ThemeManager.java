package com.mozo.bustraveler;

import android.app.Activity;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {
    public static void applyDarkTheme(Activity activity) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        Log.d("dark theme", "Theme : Dark" );
    }

    public static void applyLightTheme(Activity activity) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Log.d("light theme", "Theme : Light" );
    }
}
