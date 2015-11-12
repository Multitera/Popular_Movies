package com.example.andy.popularmovies.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.andy.popularmovies.MovieSchema;

/**
 * Created by Andy on 10/27/2015.
 */
public class MovieDatabaseHelper extends SQLiteOpenHelper {
    public MovieDatabaseHelper(Context context) {
        super(context, MovieSchema.DATABASE_NAME, null, MovieSchema.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MovieSchema.MovieEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(MovieSchema.MovieEntry.DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
