package com.mozo.bustraveler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;

public class payment_activity extends AppCompatActivity {

    private Spinner spinner;
    private TextView selectedPriceTextView;
    private ImageView addImageView, minusImageView;
    private int currentSelectedValue = 0; // Keeps track of the current selected value in the spinner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialize views
        spinner = findViewById(R.id.spinnerPayments);
        selectedPriceTextView = findViewById(R.id.selectedPrice);
        addImageView = findViewById(R.id.add);
        minusImageView = findViewById(R.id.minus);
        Button paymentButton = findViewById(R.id.payBtn);

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Set listener for spinner item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update current selected value
                currentSelectedValue = Integer.parseInt(parent.getItemAtPosition(position).toString());
                selectedPriceTextView.setText(String.valueOf(currentSelectedValue)  + " LKR");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set click listener for add ImageView
        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectedValue < 1500) { // Check if currentSelectedValue is less than 1500
                    currentSelectedValue++;
                    selectedPriceTextView.setText(String.valueOf(currentSelectedValue)  + " LKR");
                }
            }
        });

        // Set click listener for minus ImageView
        minusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectedValue > 0) {
                    currentSelectedValue--;
                    selectedPriceTextView.setText(String.valueOf(currentSelectedValue) + "LKR");
                }
            }
        });

        // Set click listener for payment button
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectedValue == 0) {
                    // Show alert dialog
                    new AlertDialog.Builder(payment_activity.this)
                            .setTitle("Alert")
                            .setMessage("Please select an option.")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {
                    // Proceed with payment
                }
            }
        });
    }
}
