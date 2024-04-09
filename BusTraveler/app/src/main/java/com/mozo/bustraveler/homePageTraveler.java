package com.mozo.bustraveler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class homePageTraveler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_traveler);
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
}