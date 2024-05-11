package com.mozo.bustraveler;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class securityTips extends AppCompatActivity {
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_tips);
        // Initialize DatabaseManager
        databaseManager = new DatabaseManager(this);
    }

    // Method to handle click on "Add your tips" button
    public void addTip(View view) {
        // Show input dialog
        showInputDialog();
    }

    // Method to fetch tips from the database and display them using StringBuffer
    public void getTips(View view) {
        Cursor cursor = databaseManager.getAllTips();
        if (cursor != null) {
            StringBuffer buffer = new StringBuffer();
            try {
                if (cursor.moveToFirst()) {
                    int tipIndex = cursor.getColumnIndex(DatabaseManager.COLUMN_TIP);
                    do {
                        String tip = cursor.getString(tipIndex);
                        buffer.append(tip).append("\n");
                    } while (cursor.moveToNext());
                } else {
                    // Handle case when no tips are available
                    buffer.append("No tips available.");
                }
            } finally {
                cursor.close();
            }
            showTipsDialog(buffer.toString());
        }
    }

    // Method to show a dialog containing the security tips
    private void showTipsDialog(String tips) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Security Tips");
        builder.setMessage(tips);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    // Method to show input dialog
    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Security Tip");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tip = input.getText().toString().trim();
                if (!TextUtils.isEmpty(tip)) {
                    // Add tip to database
                    databaseManager.addTip(tip);
                    // Update UI (you can add the new tip dynamically to the layout)
                    // For simplicity, let's just refresh the activity
                    recreate();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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