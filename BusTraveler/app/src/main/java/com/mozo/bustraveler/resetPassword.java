package com.mozo.bustraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class resetPassword extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private Button resetButton;
    private ProgressBar progressBarReset;
    private RelativeLayout resetText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // assigning elements by id
        emailEditText = findViewById(R.id.editTextEmailLogin);
        resetButton = findViewById(R.id.buttonLogin);
        progressBarReset = findViewById(R.id.progressBarReset);
        resetText = findViewById(R.id.resetText);

        //whenever user click on the reset button it trigger resetPassword function
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    //to
    private void resetPassword() {
        // taking email
        String email = emailEditText.getText().toString().trim();
        // check email is empty or not
        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        // show a progress bar bcz it start the process
        progressBarReset.setVisibility(View.VISIBLE);
        // using firebase reset pass word function
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBarReset.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Password reset email sent successfully and show the massage
                            resetText.setVisibility(View.VISIBLE);
                        } else {
                            // Failed to send password reset email
                            Toast.makeText(getApplicationContext(), "Failed to send reset email. Please check your email address.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}