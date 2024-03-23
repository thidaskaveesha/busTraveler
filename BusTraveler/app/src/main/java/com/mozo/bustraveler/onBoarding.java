package com.mozo.bustraveler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class onBoarding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
    }

    // Method to get Start btn
    public void getStart(View view) {
        // Navigate to register page
        Intent intent = new Intent(this, registerPage.class);
        startActivity(intent);
    }
}