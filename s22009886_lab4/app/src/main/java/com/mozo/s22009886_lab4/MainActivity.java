package com.mozo.s22009886_lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Declaring varibles
    private TextView textView, alertTitle;
    private SensorManager sensorManager;
    private Sensor tempSensor;
    private Boolean isTempratureSensorAvailable;
    private ImageView hotImage;
    MediaPlayer mp;
    // Boolean to track whether audio is currently playing
    boolean isRunning = false;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assigning elements by id
        textView = findViewById(R.id.textSensorData);
        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        alertTitle = findViewById(R.id.alertTitle);
        hotImage = findViewById(R.id.hotImage);

        // Checking the sensor availability
        if(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            isTempratureSensorAvailable = true;
        }else{
            textView.setText("Temprature is not available");
            isTempratureSensorAvailable = false;
        }
    }

    @Override
    // When sensor value changes
    public void onSensorChanged(SensorEvent event){
        textView.setText(event.values[0]+ " C");
        // Initializing thresholdValue
        int thresholdValue = 86;
        // If sensor value is greater than thresholdValue and audio is not running
        if(event.values[0] > thresholdValue && !isRunning){
            isRunning = true;
            mp = new MediaPlayer();
            try {
                // Show temperature rising alert
                alertTitle.setVisibility(TextView.VISIBLE);
                // Show image
                hotImage.setVisibility(ImageView.VISIBLE);
                // Getting resource URI
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.s22009886);
                mp.setDataSource(this, uri);
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                // IF IO problems exist
                e.printStackTrace();
            }
        } else if (event.values[0] <= thresholdValue && isRunning) {
            // If sensor value is less than or equal to threshold and audio is running
            isRunning = false;
            mp.stop();
            mp.release();
            mp = null;
            // Hide temperature rising alert
            alertTitle.setVisibility(TextView.GONE);
            // Hide image
            hotImage.setVisibility(ImageView.GONE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    // Registering the sensor listener with normal delay
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    // Unregistering the sensor listener
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

}