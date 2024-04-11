package com.mozo.bustraveler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class homePageConductor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_conductor);

    }


    public void goTomenuPage(View view) {
        Intent intent = new Intent(this, menuPage.class);
        intent.putExtra("sourcePage", "conductor");
        startActivity(intent);
        finish();
    }

    public void goToAccountPage(View view) {
        Intent intent = new Intent(this, userAccountPage.class);
        intent.putExtra("sourcePage", "conductor");
        startActivity(intent);
        finish();
    }

    public void gotoSecurityTipsPage(View view) {
        Intent intent = new Intent(this, securityTips.class);
        intent.putExtra("sourcePage", "conductor");
        startActivity(intent);
        finish();
    }
}