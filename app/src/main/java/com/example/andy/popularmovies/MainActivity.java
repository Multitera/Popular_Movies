package com.example.andy.popularmovies;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.andy.popularmovies.model.Movie;

import org.parceler.Parcels;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.FragmentMessenger, DetailsActivityFragment.FragmentMessenger {
    private static final String DETAIL_FRAGMENT_TAG = "DF";
    public static final String MOVIE_KEY = "movie";
    private ShareActionProvider mShareActionProvider;

    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTwoPane = findViewById(R.id.fragment_details) != null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (menu.findItem(R.id.share_trailer) != null) {
            MenuItem item = menu.findItem(R.id.share_trailer);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.popularity:
                changeSearchType(R.string.sort_popularity);
                return true;
            case R.id.rating:
                changeSearchType(R.string.sort_rating);
                return true;
            case R.id.favorites:
                changeSearchType(R.string.sort_favorites);
                return true;
            case R.id.share_trailer:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeSearchType(int searchID) {
        MainActivityFragment fragment = (MainActivityFragment) getFragmentManager().findFragmentById(R.id.fragment_main);
        fragment.queryMoviePosters(searchID);
    }

    @Override
    public void gridItemClicked(View sharedImage, Movie movie) {
        Parcelable parcelable = Parcels.wrap(movie);
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(MOVIE_KEY, parcelable);
            DetailsActivityFragment fragment = new DetailsActivityFragment();
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.fragment_details, fragment, DETAIL_FRAGMENT_TAG).commit();
        } else {
            Intent intent;
            intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(MOVIE_KEY, parcelable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, sharedImage, getString(R.string.shared_poster_image));
                startActivity(intent, options.toBundle());
            } else
                startActivity(intent);
        }
    }

    @Override
    public void moviesDisplayed(Movie movie) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            Parcelable parcelable = Parcels.wrap(movie);
            args.putParcelable(MOVIE_KEY, parcelable);
            DetailsActivityFragment fragment = new DetailsActivityFragment();
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().add(R.id.fragment_details, fragment, DETAIL_FRAGMENT_TAG).commit();
        }
    }

    @Override
    public void setShareIntent(String trailerURL) {
        if (mShareActionProvider != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, trailerURL);
            mShareActionProvider.setShareIntent(intent);
        }
    }
}
