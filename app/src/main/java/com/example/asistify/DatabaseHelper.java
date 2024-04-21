package com.example.asistify;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOGIN_ES = "myapp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String INFO_ES = "users";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";


    public DatabaseHelper(Context context) {
        super(context, LOGIN_ES, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + INFO_ES + " ( " + COLUMN_EMAIL + " TEXT PRIMARY KEY, " + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + INFO_ES);
        onCreate(db);
    }

    public boolean checkUser (String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + INFO_ES + " WHERE " + COLUMN_EMAIL + " =? AND " + COLUMN_PASSWORD + " =?", new String[]{email, password});
        int count = cursor.getCount();
        return count>0;
    }
}
