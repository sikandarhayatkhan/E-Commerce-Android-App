package com.example.anammumtaz.mad_final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AppDB.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "User";

    private static SQLiteHelper mInstance = null;

    // User Table Columns names
    public static final String username = "_name";
    public static final int phone_number = Integer.parseInt("phone");
    public static final String password = "password";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteHelper getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new SQLiteHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USER + "("

                + username + " TEXT,"
                + phone_number + " integer,"
                + password + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    public long insert(String name, int address, String age) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(username, name);
        values.put(String.valueOf(phone_number), address);
        values.put(password, age);

        // Inserting Row
        long result = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
        return result;
    }

    public int delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String clause = username + " = ?";
        String[] clauseArg = new String[] {String.valueOf(id)};
        int result = db.delete(TABLE_USER, clause, clauseArg);
        db.close();
        return result;
    }

    public Cursor query(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        int clause = Integer.parseInt(phone_number + " = ?");
        String[] clauseArg = new String[] {name};
        Cursor cursor = db.query(TABLE_USER, null, String.valueOf(clause), clauseArg, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public int update(String name, int phone, String passwordss, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(username, name);
        values.put(String.valueOf(phone_number), phone);
        values.put(password, passwordss);

        String clause = username + " = ?";
        String[] clauseArg = new String[]{String.valueOf(id)};
        int result = db.update(TABLE_USER, values, clause, clauseArg);
        db.close();
        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

