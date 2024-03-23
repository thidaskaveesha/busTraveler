package com.mozo.bustraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class loginPage extends AppCompatActivity {

    private static final String TAG = "LoginPage";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ProgressBar progressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressBarLogin = findViewById(R.id.progressBarLogin);
    }

    // Method to navigate to the register page
    public void navigateToRegister(View view){
        // Navigate to register page
        Intent intent = new Intent(this, registerPage.class);
        startActivity(intent);
    }

    // Method to handle login button click
    public void loginUser(View view) {
        EditText emailEditText = findViewById(R.id.editTextEmailLogin);
        EditText passwordEditText = findViewById(R.id.editTextPasswordLogin);

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate email and password
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and Password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress bar and disable user interactions
        progressBarLogin.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        // Sign in user with Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Hide progress bar and enable user interactions
                        progressBarLogin.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // If user is authenticated, check user role
                                checkUserRole(user.getUid());
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(loginPage.this, "Authentication failed. Please check your credentials.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Method to check user role in Firestore
    private void checkUserRole(String userId) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String role = document.getString("role");
                                if (role != null) {
                                    // Redirect user based on role
                                    Intent intent;
                                    if (role.equals("Conductor")) {
                                        intent = new Intent(loginPage.this, homePageConductor.class);
                                    } else {
                                        intent = new Intent(loginPage.this, homePageTraveler.class);
                                    }
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.d(TAG, "User role is null");
                                    Toast.makeText(loginPage.this, "User role not found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d(TAG, "No such document");
                                Toast.makeText(loginPage.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            Toast.makeText(loginPage.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
