package com.example.andy.popularmovies;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Andy on 10/27/2015.
 */
public final class MovieSchema {

    public static  final int DATABASE_VERSION = 1;
    public static final String AUTHORITY = "com.example.andy.popularmovies.movieprovider/";
    public static final String DATABASE_NAME = "Movies.db";
    private static final String COMMA = ",";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    public static final String TABLE_NAME = "favoriteMovies";
    public static final String _ID = "_id";
    public static final String COLUMN_NAME_MOVIE_ID = "movieId";
    public static final String COLUMN_NAME_ADULT = "isAdult";
    public static final String COLUMN_NAME_BACKDROP = "backdrop";
    public static final String COLUMN_NAME_GENRE = "genre";
    public static final String COLUMN_NAME_LANGUAGE = "language";
    public static final String COLUMN_NAME_ORIGINAL_TITLE = "originalTitle";
    public static final String COLUMN_NAME_OVERVIEW = "overview";
    public static final String COLUMN_NAME_DATE = "releaseDate";
    public static final String COLUMN_NAME_POSTER = "poster";
    public static final String COLUMN_NAME_POPULARITY = "popularity";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_VIDEO = "hasVideo";
    public static final String COLUMN_NAME_VOTE_AVG = "voteAvg";
    public static final String COLUMN_NAME_VOTE_COUNT = "voteCount";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private MovieSchema() {}

    public static  abstract class MovieEntry implements BaseColumns {

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_MOVIE_ID + INTEGER_TYPE + COMMA +
                COLUMN_NAME_ADULT + INTEGER_TYPE + COMMA +
                COLUMN_NAME_BACKDROP + TEXT_TYPE + COMMA +
                COLUMN_NAME_GENRE + TEXT_TYPE + COMMA +
                COLUMN_NAME_LANGUAGE + TEXT_TYPE + COMMA +
                COLUMN_NAME_ORIGINAL_TITLE + TEXT_TYPE + COMMA +
                COLUMN_NAME_OVERVIEW + TEXT_TYPE + COMMA +
                COLUMN_NAME_DATE + TEXT_TYPE + COMMA +
                COLUMN_NAME_POSTER + TEXT_TYPE + COMMA +
                COLUMN_NAME_POPULARITY + REAL_TYPE + COMMA +
                COLUMN_NAME_TITLE + TEXT_TYPE + COMMA +
                COLUMN_NAME_VIDEO + INTEGER_TYPE + COMMA +
                COLUMN_NAME_VOTE_AVG + REAL_TYPE + COMMA +
                COLUMN_NAME_VOTE_COUNT + INTEGER_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
