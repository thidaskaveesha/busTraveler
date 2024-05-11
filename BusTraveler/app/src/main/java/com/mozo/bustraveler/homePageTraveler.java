package com.mozo.bustraveler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class homePageTraveler extends AppCompatActivity {

    // Declare sensor management light class
    private SensorManagerLight sensorManagerLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_traveler);

        // Initialize the SensorManagerLight instance
        sensorManagerLight = SensorManagerLight.getInstance(this);
    }
    // override on resume if there is light sensor register the listener
    @Override
    protected void onResume() {
        super.onResume();
        // Register the sensor listener when the activity resumes
        if (sensorManagerLight != null) {
            sensorManagerLight.registerListener();
        }
    }

    // override on pause if there is light sensor unregister the listener
    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listener when the activity pauses
        if (sensorManagerLight != null) {
            sensorManagerLight.unregisterListener();
        }
    }


    public void goToAccountPage(View view) {
        Intent intent = new Intent(this, userAccountPage.class);
        startActivity(intent);
    }

    public void goTomenuPage(View view) {
        Intent intent = new Intent(this, menuPage.class);
        startActivity(intent);
    }

    public void gotoSecurityTipsPage(View view) {
        Intent intent = new Intent(this, securityTips.class);
        startActivity(intent);
    }

    public void goToEmergencyNumber(View view) {
        Intent intent = new Intent(this, emergencyPhoneNumber.class);
        startActivity(intent);
    }

    public void goToReultsPage(View view) {
        EditText startEditText = findViewById(R.id.startLocation);
        EditText endEditText = findViewById(R.id.endLocation);

        String startLocation = startEditText.getText().toString().trim();
        String endLocation = endEditText.getText().toString().trim();

        // Validate email and password
        if (startLocation.isEmpty() || endLocation.isEmpty()) {
            Toast.makeText(this, "current location and destination is required", Toast.LENGTH_SHORT).show();
            return;
        }else{
            /*Intent intent = new Intent(homePageTraveler.this, ResultsActivity.class);
            intent.putExtra("START_LOCATION", startLocation);
            intent.putExtra("END_LOCATION", endLocation);
            startActivity(intent);*/
        }
    }

    public void goToPaymentMethod(View view) {
        Intent intent = new Intent(this, payment_methods.class);
        startActivity(intent);
        finish();
    }
}