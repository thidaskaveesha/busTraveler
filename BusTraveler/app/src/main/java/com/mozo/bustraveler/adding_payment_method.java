package com.mozo.bustraveler;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class adding_payment_method extends AppCompatActivity {
    // initializing classes
    private EditText editTextName, editTextCardNumber, editTextCvn, editTextExpiryDate, editTextPassword;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_payment_method);
        // Get elements by id
        editTextName = findViewById(R.id.editTextName);
        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextCvn = findViewById(R.id.editTextCvn);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        editTextPassword = findViewById(R.id.editTextPassword);
        databaseManager = new DatabaseManager(this);
    }

    public void goBack(View view) {
        finish();
    }

    public void goToReultsPage(View view) {
        String name = editTextName.getText().toString().trim();
        String cardNumber = editTextCardNumber.getText().toString().trim();
        String cvn = editTextCvn.getText().toString().trim();
        String expiryDate = editTextExpiryDate.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (name.isEmpty() || cardNumber.isEmpty() || cvn.isEmpty() || expiryDate.isEmpty() || password.isEmpty()) {
            // Show error message to the user
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        } else {
            databaseManager.addPaymentMethod(name, cardNumber, cvn, expiryDate, password);
            // Optionally, show a success message and clear the input fields
            Toast.makeText(this, "Payment method added", Toast.LENGTH_SHORT).show();
            editTextName.setText("");
            editTextCardNumber.setText("");
            editTextCvn.setText("");
            editTextExpiryDate.setText("");
            editTextPassword.setText("");
            finish();
        }
    }
}