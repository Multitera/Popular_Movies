package com.example.andy.popularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andy.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public static final String MOVIE_KEY = "movie";
    private TextView mMovieTitle;
    private TextView mRating;
    private TextView mDateReleased;
    private TextView mOverview;
    private ImageView mPoster;
    private Button mFavorite;
    private Movie movie;

    public DetailsActivityFragment() {
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
        return rootView;
    }
}
