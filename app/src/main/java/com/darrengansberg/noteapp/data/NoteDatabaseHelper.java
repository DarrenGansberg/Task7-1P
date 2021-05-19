package com.darrengansberg.noteapp.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.darrengansberg.noteapp.util.Util;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    public NoteDatabaseHelper (@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public NoteDatabaseHelper (@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + Util.NOTES_TABLE_NAME + "(";
        query += Util.NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,";
        query += Util.NOTE_CONTENT + " TEXT NOT NULL)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: implementation in version 1.1
        //Need an appropriate migration strategy to handle database upgrades.
        //Cannot use technique of simply dropping and recreating the database
        //because that would result in complete loss of the user's note data. 
    }


}
