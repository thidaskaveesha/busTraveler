package com.mozo.bustraveler;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registerPage extends AppCompatActivity {

    // Initializing classes to varibles
    private EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Spinner spinnerRole;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private boolean registrationInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // finding the elements
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        buttonRegister = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.progressBar);


        // Create a new ArrayAdapter to populate the Spinner with items
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles, android.R.layout.simple_spinner_item);
        // Set the layout resource for each item in the Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the adapter to the Spinner
        spinnerRole.setAdapter(adapter);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {registerUser();}
        });
    }

    @Override
    public void onBackPressed() {
        if (!registrationInProgress) {
            super.onBackPressed();
        }
    }

    public void navigateToLogin(View view){
        // Navigate to login page
        Intent intent = new Intent(this, loginPage.class);
        startActivity(intent);
    }
    // method to register user
    private void registerUser() {
        // Show Progress Bar
        progressBar.setVisibility(View.VISIBLE);
        // Mark registration in progress
        registrationInProgress = true;
        // Disable User Interactions
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        //check whether input is empty or not
        if (name.isEmpty()) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            registrationInProgress = false;
            // Hide ProgressBar
            progressBar.setVisibility(View.GONE);
            // Enable user interactions
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            registrationInProgress = false;
            // Hide ProgressBar
            progressBar.setVisibility(View.GONE);
            // Enable user interactions
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            registrationInProgress = false;
            // Hide ProgressBar
            progressBar.setVisibility(View.GONE);
            // Enable user interactions
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            return;
        }

        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.setError("Confirm Password is required");
            editTextConfirmPassword.requestFocus();
            registrationInProgress = false;
            // Hide ProgressBar
            progressBar.setVisibility(View.GONE);
            // Enable user interactions
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            editTextConfirmPassword.requestFocus();
            registrationInProgress = false;
            // Hide ProgressBar
            progressBar.setVisibility(View.GONE);
            // Enable user interactions
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            return;
        }

        //authenticate user with firebase authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Hide ProgressBar
                        progressBar.setVisibility(View.GONE);
                        // Enable user interactions
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        // Mark registration is done
                        registrationInProgress = false;
                        if (task.isSuccessful()) {
                            // Registration success
                            // Here you can save additional user details to your database
                            saveUserDetailsToDatabase(name, email, role);
                            Toast.makeText(registerPage.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            // Redirect user to appropriate homepage based on role
                            // If registered check the role and show appopriate page
                            Intent intent;
                            if (role.equals("Conductor")) {
                                intent = new Intent(registerPage.this, homePageConductor.class);
                            }else {
                                intent = new Intent(registerPage.this, homePageTraveler.class);
                            }
                            startActivity(intent);
                        } else {
                            // if problem arries show
                            Toast.makeText(registerPage.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserDetailsToDatabase(String name, String email, String role) {
        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user object with the provided details
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("role", role);

        // Use the user's UID as the document ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Set the user document in Firestore with the user ID as the document ID
        db.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("register_success", "DocumentSnapshot added with ID: " + userId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("register_failure", "Error adding document", e);
                    }
                });
    }
}