package com.example.andy.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.andy.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Andy on 8/4/2015.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        Context context = getContext();
        if (convertView == null) {
            convertView = new ImageView(context);
            convertView.setId(R.id.posterImage);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                convertView.setTransitionName(context.getString(R.string.shared_poster_image));
        }
        String posterPath = context.getString(R.string.poster_url) + movie.getPoster_path();
        Picasso.with(context).load(posterPath).into((ImageView) convertView);
        return convertView;
    }
}
