package com.mozo.bustraveler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class homePageConductor extends AppCompatActivity {

    // Initialize Firebase Authentication
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_conductor);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarLogOut);
    }

    public void userLogOut(View view){
        // Show ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        // Log out the user from Firebase Authentication
        mAuth.signOut();

        // Redirect to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}