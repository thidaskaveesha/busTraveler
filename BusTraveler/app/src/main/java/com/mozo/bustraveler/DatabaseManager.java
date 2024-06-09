package com.mozo.bustraveler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "SecurityTipsDB";
    private static final int DATABASE_VERSION = 1;

    // SecurityTips table name and columns
    public static final String TABLE_SECURITY_TIPS = "SecurityTips";
    public static final String COLUMN_TIP_ID = "id";
    public static final String COLUMN_TIP = "tip";

    // PaymentMethods table name and columns
    public static final String TABLE_PAYMENT_METHODS = "PaymentMethods";
    public static final String COLUMN_PAYMENT_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CARD_NUMBER = "card_number";
    public static final String COLUMN_CVN = "cvn";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";
    public static final String COLUMN_PASSWORD = "password";

    // Constructor
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create SecurityTips table
        String CREATE_SECURITY_TIPS_TABLE = "CREATE TABLE " + TABLE_SECURITY_TIPS + "("
                + COLUMN_TIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TIP + " TEXT" + ")";
        db.execSQL(CREATE_SECURITY_TIPS_TABLE);

        // Create PaymentMethods table
        String CREATE_PAYMENT_METHODS_TABLE = "CREATE TABLE " + TABLE_PAYMENT_METHODS + "("
                + COLUMN_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_CARD_NUMBER + " TEXT,"
                + COLUMN_CVN + " TEXT,"
                + COLUMN_EXPIRY_DATE + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_PAYMENT_METHODS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECURITY_TIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT_METHODS);
        // Create tables again
        onCreate(db);
    }

    // Method to add a tip to the database
    public void addTip(String tip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIP, tip);
        db.insert(TABLE_SECURITY_TIPS, null, values);
        db.close();
    }

    // Method to get all tips from the database
    public Cursor getAllTips() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SECURITY_TIPS, null);
    }

    // Method to add a payment method to the database
    public void addPaymentMethod(String name, String cardNumber, String cvn, String expiryDate, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_CARD_NUMBER, cardNumber);
        values.put(COLUMN_CVN, cvn);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_PAYMENT_METHODS, null, values);
        db.close();
    }
    // method to fetch  all data
    public Cursor getAllPaymentMethods() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PAYMENT_METHODS, null);
    }
}
