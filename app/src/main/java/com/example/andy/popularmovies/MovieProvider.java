package com.example.andy.popularmovies;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.andy.popularmovies.service.MovieDatabaseHelper;

/**
 * Created by Andy on 11/3/2015.
 */
public class MovieProvider extends ContentProvider {
    private static final int NO_MATCH = 0;
    private static final int TABLE = 1;
    private static final int ROW = 2;
    private static final UriMatcher URI_MATCHER = buildUriMatcher();
    MovieDatabaseHelper mHelper;

    private static UriMatcher buildUriMatcher() {
        //0 is no match, 1 is a table, 2 is a single row
        UriMatcher uriMatcher = new UriMatcher(NO_MATCH);
        uriMatcher.addURI(MovieSchema.AUTHORITY, MovieSchema.TABLE_NAME, TABLE);
        uriMatcher.addURI(MovieSchema.AUTHORITY, MovieSchema.TABLE_NAME+"/*", ROW);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mHelper = new MovieDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(MovieSchema.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case TABLE:
                return "vnd.android.cursor.dir/vnd."+MovieSchema.AUTHORITY+MovieSchema.TABLE_NAME;
            case ROW:
                return "vnd.android.cursor.item/vnd."+MovieSchema.AUTHORITY+MovieSchema.TABLE_NAME;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id = db.insert(MovieSchema.TABLE_NAME, null, contentValues);
        return uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db.delete(MovieSchema.TABLE_NAME, s, strings);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db.update(MovieSchema.TABLE_NAME, contentValues, s, strings);
    }
}
