package com.example.andy.popularmovies;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.andy.popularmovies.service.MovieDatabaseHelper;

/**
 * Created by Andy on 11/3/2015.
 */
public class MovieProvider extends ContentProvider {
    MovieDatabaseHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new MovieDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(MovieSchema.DATABASE_NAME, strings, s, strings1, null, null, s1);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id = db.insert(MovieSchema.DATABASE_NAME, null, contentValues);
        return uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db.delete(MovieSchema.DATABASE_NAME, s, strings);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db.update(MovieSchema.DATABASE_NAME, contentValues, s, strings);
    }
}
