package com.mozo.bustraveler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class registerPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
    }

    public void navigateToLogin(View view){
        // Navigate to login page
        Intent intent = new Intent(this, loginPage.class);
        startActivity(intent);
    }
}