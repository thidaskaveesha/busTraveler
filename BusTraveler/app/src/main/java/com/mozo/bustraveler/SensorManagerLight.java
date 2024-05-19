package com.mozo.bustraveler;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorManagerLight implements SensorEventListener {
    // Declaring classes
    private static SensorManagerLight instance;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Activity activity;

    // Creating the constructor
    private SensorManagerLight(Activity activity) {
        this.activity = activity;
        sensorManager = (android.hardware.SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    // Getter
    public static SensorManagerLight getInstance(Activity activity) {
        if (instance == null) {
            instance = new SensorManagerLight(activity);
        }
        return instance;
    }

    // Register listener to light sensor
    public void registerListener() {
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d("registerSensor", "Listener registered for light sensor.");
    }

    // unregister listener to light sensor
    public void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    // Change themes on sensor changes
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightLevel = event.values[0];
            Log.d("LightLevel", "Light Level : " + lightLevel);
            if (lightLevel < 10) {
                // Set dark mode
                ThemeManager.applyDarkTheme( activity);
                Log.d("applied", "Theme : dark" );
            } else {
                // Set light mode
                ThemeManager.applyLightTheme( activity);
                Log.d("applied", "Theme : Light" );
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
