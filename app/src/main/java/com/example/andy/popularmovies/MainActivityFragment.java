package com.example.andy.popularmovies;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
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
public class MainActivityFragment extends Fragment implements Callback<Results> {

    private static final String SEARCH_TYPE_KEY = "searchType";
    private static final String MOVIE_KEY = "movie";
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
        Toast.makeText(this.getActivity(), "error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SEARCH_TYPE_KEY, searchType);
        Parcelable parcelable = Parcels.wrap(movies);
        outState.putParcelable(MOVIE_KEY, parcelable);
    }
}
