package com.mozo.bustraveler;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    // Declare currentUser as a field of MainActivity
    private FirebaseUser currentUser;
    // Declare sensor management light class
    private SensorManagerLight sensorManagerLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the SensorManagerLight instance
        sensorManagerLight = SensorManagerLight.getInstance(this);

        // Check if the user is already logged in
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, redirect to dashboard
            redirectToDashboard();
        } else {
            // User is not logged in, show onboarding, registration, and login pages
            showOnboarding();
        }
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

    private void showOnboarding() {
        // Start the onboarding activity
        Intent intent = new Intent(MainActivity.this, onBoarding.class);
        startActivity(intent);
        finish(); // Optional, to prevent the user from returning to this activity
    }

    private void redirectToDashboard() {
        // Get user role from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String role = documentSnapshot.getString("role");
                    // Redirect to appropriate dashboard based on role
                    if ("Conductor".equals(role)) {
                        startActivity(new Intent(MainActivity.this, homePageConductor.class));
                    } else if ("Traveler".equals(role)) {
                        startActivity(new Intent(MainActivity.this, homePageTraveler.class));
                    }
                    finish(); // Optional, to prevent the user from returning to this activity
                } else {
                    // Handle case where user document does not exist
                    startActivity(new Intent(MainActivity.this, onBoarding.class));
                    finish(); // Optional, to prevent the user from returning to this activity
                }
            }
        });
    }

}
