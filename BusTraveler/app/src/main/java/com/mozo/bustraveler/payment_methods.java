package com.mozo.bustraveler;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class payment_methods extends AppCompatActivity {

    private DatabaseManager databaseManager;
    private PaymentMethodsAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);

        listView = findViewById(R.id.paymentItems);
        databaseManager = new DatabaseManager(this);

        Cursor cursor = databaseManager.getAllPaymentMethods();
        adapter = new PaymentMethodsAdapter(this, cursor);
        listView.setAdapter(adapter);
    }

    // Going back to the previous page
    public void goBack(View view) {
        finish();
    }

    // Go to add payment method
    public void gotoAddPaymentMethod(View view) {
        Intent intent = new Intent(this, adding_payment_method.class);
        startActivity(intent);
        finish();
    }
}
