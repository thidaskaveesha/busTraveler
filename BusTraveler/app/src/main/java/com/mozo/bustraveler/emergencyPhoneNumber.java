package com.mozo.bustraveler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class emergencyPhoneNumber extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_phone_number);

    }

    // Calling police
    public void callPolice(View v) {
        // Open phone and call the number
        String phoneNumber = "119";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    // Calling Ambulance
    public void callAmbulance(View view){
        // Open phone and call the number
        String phoneNumber = "1990";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    // Calling Fire Brigade
    public void callFireBrigade(View view){
        // Open phone and call the number
        String phoneNumber = "+94112421111";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    //navigate back to previous page
    public void goBack(View view) {
        String sourcePage = getIntent().getStringExtra("sourcePage");
        if (sourcePage != null && sourcePage.equals("conductor")) {
            Intent intent = new Intent(this, homePageConductor.class);
            startActivity(intent);
        } else if (sourcePage != null && sourcePage.equals("traveler")) {
            Intent intent = new Intent(this, homePageTraveler.class);
            startActivity(intent);
        }
        finish();
    }
}