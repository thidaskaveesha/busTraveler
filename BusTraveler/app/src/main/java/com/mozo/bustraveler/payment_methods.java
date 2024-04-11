package com.mozo.bustraveler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class payment_methods extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);
    }

    // Going back to previous page
    public void goBack(View view) {
        finish();
    }

    // Go to add payment method
    public void gotoAddPaymentMethod(View view) {
        Intent intent = new Intent(this, adding_payment_method.class);
        startActivity(intent);
        finish();
    }
}