package com.mozo.bustraveler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class userAccountPage extends AppCompatActivity {

    private EditText editName, editBio, editEmail, editPhone;
    private Button editButton;
    private ProgressBar progressBar;

    private FirebaseFirestore db;
    private DocumentReference userRef;

    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_page);

        editName = findViewById(R.id.editName);
        editBio = findViewById(R.id.editBio);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editButton = findViewById(R.id.editButton);
        progressBar = findViewById(R.id.progressBarChanges);

        db = FirebaseFirestore.getInstance();
        // Get current user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Reference to the user's document in Firestore
        userRef = db.collection("users").document(userId);

        // Fetch user data from Firestore and show EditText fields
        fetchUserData();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditing) {
                    // Enable editing
                    enableEditing();
                    editButton.setText("Confirm");
                } else {
                    // Save updated data to Firestore
                    saveUserData();
                    disableEditing();
                    editButton.setText("Edit");
                }
                isEditing = !isEditing;
            }
        });
    }

    private void fetchUserData() {
        progressBar.setVisibility(View.VISIBLE);
        // Fetch user data from Firestore
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // show EditText fields with user data
                        editName.setText(document.getString("name"));
                        editBio.setText(document.getString("bio"));
                        editEmail.setText(document.getString("email"));
                        editPhone.setText(document.getString("phone"));

                        // Make text typeface normal
                        editName.setTypeface(null, Typeface.NORMAL);
                        editBio.setTypeface(null, Typeface.NORMAL);
                        editEmail.setTypeface(null, Typeface.NORMAL);
                        editPhone.setTypeface(null, Typeface.NORMAL);

                        // change text color
                        int grayColor = Color.parseColor("#808080");
                        editName.setTextColor(grayColor);
                        editBio.setTextColor(grayColor);
                        editEmail.setTextColor(grayColor);
                        editPhone.setTextColor(grayColor);
                    } else {
                        // Document not exist
                        Toast.makeText(userAccountPage.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error fetching document
                    Toast.makeText(userAccountPage.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserData() {
        progressBar.setVisibility(View.VISIBLE);
        // Get updated data from EditText fields
        String newName = editName.getText().toString().trim();
        String newBio = editBio.getText().toString().trim();
        String newEmail = editEmail.getText().toString().trim();
        String newPhone = editPhone.getText().toString().trim();

        // Update user document in Firestore
        userRef.update("name", newName,
                        "bio", newBio,
                        "email", newEmail,
                        "phone", newPhone)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(userAccountPage.this, "User data updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(userAccountPage.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void enableEditing() {
        // enable editing
        editName.setEnabled(true);
        editBio.setEnabled(true);
        editEmail.setEnabled(true);
        editPhone.setEnabled(true);
        // change text color
        int blackColor = Color.parseColor("#000000");
        editName.setTextColor(blackColor);
        editBio.setTextColor(blackColor);
        editEmail.setTextColor(blackColor);
        editPhone.setTextColor(blackColor);

    }

    private void disableEditing() {
        // Disable editing
        editName.setEnabled(false);
        editBio.setEnabled(false);
        editEmail.setEnabled(false);
        editPhone.setEnabled(false);
        // change text color
        int grayColor = Color.parseColor("#808080");
        editName.setTextColor(grayColor);
        editBio.setTextColor(grayColor);
        editEmail.setTextColor(grayColor);
        editPhone.setTextColor(grayColor);
    }

    public void goBack(View view){
        String sourcePage = getIntent().getStringExtra("sourcePage");
        if (sourcePage != null && sourcePage.equals("conductor")) {
            Intent intent = new Intent(this, homePageConductor.class);
            startActivity(intent);
        } else if (sourcePage != null && sourcePage.equals("traveler")) {
            Intent intent = new Intent(this, homePageTraveler.class);
            startActivity(intent);
        }
        finish();
    }
}
