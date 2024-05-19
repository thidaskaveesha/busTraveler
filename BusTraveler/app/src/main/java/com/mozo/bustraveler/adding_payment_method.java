package com.mozo.bustraveler;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class adding_payment_method extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_payment_method);
    }

    public void goBack(View view) {
        finish();
    }

    public void goToReultsPage(View view) {
    }
}