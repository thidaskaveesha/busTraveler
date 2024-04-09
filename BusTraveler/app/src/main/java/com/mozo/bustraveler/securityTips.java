package com.mozo.bustraveler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class securityTips extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_tips);
    }

    public void goBack(View view){
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