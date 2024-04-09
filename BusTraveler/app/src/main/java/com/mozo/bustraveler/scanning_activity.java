package com.mozo.bustraveler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class scanning_activity extends AppCompatActivity {

    private Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        scanButton = findViewById(R.id.scanBtn);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize ZXing integrator
                IntentIntegrator integrator = new IntentIntegrator(scanning_activity.this);
                integrator.setPrompt("Scan a QR code");
                integrator.setOrientationLocked(false);
                integrator.initiateScan(); // Start QR code scanner
            }
        });
    }

    // Handle QR code scan result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                String qrContent = result.getContents();
                // Share QR content with payment activity
                Intent intent = new Intent(scanning_activity.this, payment_activity.class);
                intent.putExtra("QR_CONTENT", qrContent);
                startActivity(intent);
            } else {
                // Handle canceled scan
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
