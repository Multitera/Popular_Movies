package com.example.andy.popularmovies;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andy.popularmovies.model.Movie;
import com.example.andy.popularmovies.service.MovieDatabaseHelper;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String MOVIE_KEY = "movie";
    private static final int LOADER = 0;
    private TextView mMovieTitle;
    private TextView mRating;
    private TextView mDateReleased;
    private TextView mOverview;
    private ImageView mPoster;
    private Button mFavorite;
    private Movie movie;

    public DetailsActivityFragment() {
    }

    private void checkFavorites() {
        getLoaderManager().initLoader(LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        movie = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(MOVIE_KEY));
        mMovieTitle = (TextView) rootView.findViewById(R.id.movieTitleText);
        mMovieTitle.setText(movie.getTitle());
        mRating = (TextView) rootView.findViewById(R.id.rating);
        mRating.setText(movie.getVote_average());
        mDateReleased = (TextView) rootView.findViewById(R.id.dateReleasedText);
        mDateReleased.setText(movie.getRelease_date());
        mOverview = (TextView) rootView.findViewById(R.id.overviewText);
        mOverview.setText(movie.getOverview());
        mPoster = (ImageView) rootView.findViewById(R.id.posterImage);
        Picasso.with(getActivity()).load(getActivity().getString(R.string.poster_url) + movie.getPoster_path()).into(mPoster);
        mFavorite = (Button) rootView.findViewById(R.id.favoriteButton);
        checkFavorites();
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i) {
            case LOADER:
                return new CursorLoader(getActivity(), Uri.parse(getString(R.string.provider_uri)), new String[]{MovieSchema._ID}, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        boolean isFavorite = false;
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    if (movie.getId() == cursor.getInt(cursor.getColumnIndexOrThrow(MovieSchema._ID))) {
                        isFavorite = true;
                        break;
                    }
                }
            } finally {
                cursor.close();
            }
        }
        if (isFavorite) {
            mFavorite.setText(R.string.unfavorite_button);
            mFavorite.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    MovieDatabaseHelper movieDatabaseHelper = new MovieDatabaseHelper(getActivity());
                    SQLiteDatabase db = movieDatabaseHelper.getWritableDatabase();
                    db.delete(MovieSchema.DATABASE_NAME, MovieSchema._ID, new String[]{Integer.toString(movie.getId())});
                    Toast.makeText(getActivity(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                    checkFavorites();
                }
            });
        } else {
            mFavorite.setText(R.string.favorite_button);
            mFavorite.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    MovieDatabaseHelper movieDatabaseHelper = new MovieDatabaseHelper(getActivity());
                    SQLiteDatabase db = movieDatabaseHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(MovieSchema._ID, movie.getId());
                    values.put(MovieSchema.COLUMN_NAME_ADULT, movie.isAdult());
                    values.put(MovieSchema.COLUMN_NAME_BACKDROP, movie.getBackdrop_path());
                    values.put(MovieSchema.COLUMN_NAME_GENRE, movie.getGenre_ids().toString());
                    values.put(MovieSchema.COLUMN_NAME_DATE, movie.getRelease_date());
                    values.put(MovieSchema.COLUMN_NAME_LANGUAGE, movie.getOriginal_language());
                    values.put(MovieSchema.COLUMN_NAME_ORIGINAL_TITLE, movie.getOriginal_title());
                    values.put(MovieSchema.COLUMN_NAME_OVERVIEW, movie.getOverview());
                    values.put(MovieSchema.COLUMN_NAME_POPULARITY, movie.getPopularity());
                    values.put(MovieSchema.COLUMN_NAME_POSTER, movie.getPoster_path());
                    values.put(MovieSchema.COLUMN_NAME_TITLE, movie.getTitle());
                    values.put(MovieSchema.COLUMN_NAME_VIDEO, movie.isVideo());
                    values.put(MovieSchema.COLUMN_NAME_VOTE_AVG, movie.getVote_average());
                    values.put(MovieSchema.COLUMN_NAME_VOTE_COUNT, movie.getVote_count());
                    db.insert(MovieSchema.DATABASE_NAME, null, values);
                    Toast.makeText(getActivity(), "Added to favorites", Toast.LENGTH_SHORT).show();
                    checkFavorites();
                }
            });
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
