package com.example.andy.popularmovies;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.andy.popularmovies.model.ClipInfo;
import com.example.andy.popularmovies.model.ClipInfoResults;
import com.example.andy.popularmovies.model.Movie;
import com.example.andy.popularmovies.model.Review;
import com.example.andy.popularmovies.model.ReviewResults;
import com.example.andy.popularmovies.service.MovieDatabaseHelper;
import com.example.andy.popularmovies.service.MovieResultsServiceHelper;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment implements Callback, LoaderManager.LoaderCallbacks<Cursor>, MovieDetailsAdapter.AdapterInterface {

    public static final String MOVIE_KEY = "movie";
    private static final String YOUTUBE_URI = "https://www.youtube.com/watch?v=";
    private static final int LOADER = 0;
    private boolean checkedFavorites;
    private List<Review> reviews;
    private List<ClipInfo> clipInfoList;
    private RecyclerView mRecyclerView;
    private MovieDetailsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
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
        movie = Parcels.unwrap(getArguments().getParcelable(MOVIE_KEY));
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MovieDetailsAdapter(this.getActivity(), this, movie);
        mRecyclerView.setAdapter(mAdapter);
        MovieResultsServiceHelper.queryClipInfo(getActivity(), Integer.toString(movie.getId()), this);
        MovieResultsServiceHelper.queryReviews(getActivity(), Integer.toString(movie.getId()), this);
        checkFavorites();
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i) {
            case LOADER:
                return new CursorLoader(getActivity(),
                        Uri.parse("content://" + getString(R.string.provider_authority) + "/" + MovieSchema.TABLE_NAME),
                        new String[]{MovieSchema.COLUMN_NAME_MOVIE_ID},
                        MovieSchema.COLUMN_NAME_MOVIE_ID + "=?",
                        new String[]{String.valueOf(movie.getId())},
                        null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        boolean isFavorite = false;
        if (cursor != null) {
            try {
                if (cursor.getCount() != 0)
                    isFavorite = true;
            } finally {
                cursor.close();
            }
        }
        mAdapter.setIsFavorite(isFavorite);
        mAdapter.notifyDataSetChanged();
        checkedFavorites = true;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void success(Object extra, Response response) {
        if (extra instanceof ClipInfoResults) {
            ClipInfoResults clipInfoResults = (ClipInfoResults) extra;
            if (clipInfoResults.getResults() != null) {
                this.clipInfoList = clipInfoResults.getResults();
                mAdapter.addClipInfoList(clipInfoList);
                if (reviews != null) {
                    mAdapter.addReviews(reviews);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } else if (extra instanceof ReviewResults) {
            ReviewResults reviewResults = (ReviewResults) extra;
            if (reviewResults.getResults() != null) {
                this.reviews = reviewResults.getResults();
                if (clipInfoList != null) {
                    mAdapter.addReviews(reviews);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void failure(RetrofitError error) {
        Log.e(MainActivityFragment.class.getSimpleName(), error.getMessage());
    }

    @Override
    public void ClipViewCardClick(int position) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URI + clipInfoList.get(position).getKey())));
    }

    @Override
    public void FavoriteButtonPressed(boolean isFavorite) {
        if (checkedFavorites) {
            if (isFavorite) {
                MovieDatabaseHelper movieDatabaseHelper = new MovieDatabaseHelper(getActivity());
                SQLiteDatabase db = movieDatabaseHelper.getWritableDatabase();
                db.delete(MovieSchema.TABLE_NAME, MovieSchema.COLUMN_NAME_MOVIE_ID + "=" + Integer.toString(movie.getId()), null);
                Toast.makeText(getActivity(), "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                MovieDatabaseHelper movieDatabaseHelper = new MovieDatabaseHelper(getActivity());
                SQLiteDatabase db = movieDatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(MovieSchema.COLUMN_NAME_MOVIE_ID, movie.getId());
                values.put(MovieSchema.COLUMN_NAME_ADULT, movie.isAdult());
                values.put(MovieSchema.COLUMN_NAME_BACKDROP, movie.getBackdrop_path());
                values.put(MovieSchema.COLUMN_NAME_GENRE, Arrays.toString(movie.getGenre_ids()));
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
                db.insert(MovieSchema.TABLE_NAME, null, values);
                Toast.makeText(getActivity(), "Added to favorites", Toast.LENGTH_SHORT).show();
            }
            checkedFavorites = false;
            checkFavorites();
        }
    }

    @Override
    public void StartTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getActivity().startPostponedEnterTransition();
    }
}
