package com.donkingliang.photograph;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String name = "database";
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_sql = "create table task(" +
                "id integer primary key autoincrement," +
                "task_id text," +
                "status text" +
                ")";
        db.execSQL(create_sql);
        Log.d("DATABASE", "Create database success.");
    }

    public DatabaseHelper(Context context, String name, int version)
    {
        this(context,name,null,version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
