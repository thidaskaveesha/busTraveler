package com.mozo.bustraveler;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PaymentMethodsAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;
    private LayoutInflater inflater;

    public PaymentMethodsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        cursor.moveToPosition(position);
        return cursor;
    }

    @Override
    public long getItemId(int position) {
        cursor.moveToPosition(position);
        return cursor.getLong(cursor.getColumnIndexOrThrow("id"));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.payment_method_item, parent, false);
        }

        cursor.moveToPosition(position);

        TextView paymentName = convertView.findViewById(R.id.paymentName);

        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        paymentName.setText(name);

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, scanning_activity.class);
            intent.putExtra("id", cursor.getLong(cursor.getColumnIndexOrThrow("id")));
            context.startActivity(intent);
        });

        return convertView;
    }
}
