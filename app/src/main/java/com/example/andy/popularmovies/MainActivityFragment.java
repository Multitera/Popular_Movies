package com.example.andy.popularmovies;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andy.popularmovies.model.Movie;
import com.example.andy.popularmovies.model.Results;
import com.example.andy.popularmovies.service.MovieResultsServiceHelper;

import org.parceler.Parcels;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements Callback<Results>, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String SEARCH_TYPE_KEY = "searchType";
    private static final String MOVIE_KEY = "movie";
    private static final int LOADER = 0;
    private List<Movie> movies;
    private GridView gridView;
    private int searchType = -1;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);
        View emptyView = view.findViewById(R.id.empty_grid);
        gridView.setEmptyView(emptyView);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                launchDetailsActivity(view, movies.get(i));
            }
        });
        if (savedInstanceState == null)
            queryMoviePosters(R.string.sort_popularity);
        else {
            searchType = savedInstanceState.getInt(SEARCH_TYPE_KEY);
            movies = Parcels.unwrap(savedInstanceState.getParcelable(MOVIE_KEY));
            displayMoviePosters();
        }
        return view;
    }

    private void launchDetailsActivity(View sharedImage, Movie movie) {
        Intent intent;
        Parcelable parcelable = Parcels.wrap(movie);
        intent = new Intent(this.getActivity(), DetailsActivity.class);
        intent.putExtra(MOVIE_KEY, parcelable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this.getActivity(), sharedImage, getString(R.string.shared_poster_image));
            startActivity(intent, options.toBundle());
        } else
            startActivity(intent);
    }

    public void queryMoviePosters(int searchType) {
        if (searchType != this.searchType) {
            this.searchType = searchType;
            if (searchType == R.string.sort_favorites) {
                getLoaderManager().initLoader(LOADER, null, this);
            } else
                MovieResultsServiceHelper.queryResults(getActivity(), getString(searchType), this);
        }
    }

    private void displayMoviePosters() {
        gridView.setAdapter(new MovieAdapter(this.getActivity(), movies));
    }

    @Override
    public void success(Results results, Response response) {
        if (results.getResults() != null) {
            this.movies = results.getResults();
            displayMoviePosters();
        }
    }

    @Override
    public void failure(RetrofitError error) {
        Log.e(MainActivityFragment.class.getSimpleName(), error.getMessage());
        Toast.makeText(this.getActivity(), R.string.failed_retrofit, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SEARCH_TYPE_KEY, searchType);
        Parcelable parcelable = Parcels.wrap(movies);
        outState.putParcelable(MOVIE_KEY, parcelable);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i) {
            case LOADER:
                return new CursorLoader(getActivity(), Uri.parse("content://" + getString(R.string.provider_authority) + "/" + MovieSchema.TABLE_NAME), null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        this.movies.clear();
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    Boolean isAdult = cursor.getLong(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_ADULT)) == 1;
                    String backdrop = cursor.getString(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_BACKDROP));
                    String genreString = cursor.getString(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_GENRE));
                    String[] genreArray = genreString.replace("[", "").replace("]", "").split(", ");
                    int[] genreIds = new int[genreArray.length];
                    for (int i = 0; i < genreArray.length; i++)
                        genreIds[i] = Integer.parseInt(genreArray[i]);
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_MOVIE_ID));
                    String original_language = cursor.getString(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_LANGUAGE));
                    String original_title = cursor.getString(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_ORIGINAL_TITLE));
                    String overview = cursor.getString(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_OVERVIEW));
                    String release_date = cursor.getString(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_DATE));
                    String poster_path = cursor.getString(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_POSTER));
                    double popularity = cursor.getDouble(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_POPULARITY));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_TITLE));
                    boolean video = cursor.getInt(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_VIDEO)) == 1;
                    double vote_average = cursor.getDouble(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_VOTE_AVG));
                    int vote_count = cursor.getInt(cursor.getColumnIndexOrThrow(MovieSchema.COLUMN_NAME_VOTE_COUNT));
                    movies.add(new Movie(isAdult, backdrop, genreIds, id, original_language, original_title, overview, release_date, poster_path, popularity, title, video, vote_average, vote_count));
                }
            } finally {
                cursor.close();
            }
        }
        if (movies.isEmpty()) {
            TextView textView = (TextView) getActivity().findViewById(R.id.empty_grid);
            textView.setText(R.string.no_favorites);
            gridView.setEmptyView(textView);
        }
        displayMoviePosters();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
