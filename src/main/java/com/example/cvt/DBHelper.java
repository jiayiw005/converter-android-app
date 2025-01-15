package com.example.cvt;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "Converter_demo1.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {

    }

    public void insertUserData (String userid, String username, String email){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("username",userid);
        contentValues.put("email",email);
    }
}
