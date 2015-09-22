package com.example.andy.popularmovies.service;

import android.content.Context;

import com.example.andy.popularmovies.R;
import com.example.andy.popularmovies.model.Results;

import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by Andy on 8/4/2015.
 */
public class MovieResultsServiceHelper {

    public static void queryResults(Context context, String sort, Callback<Results> callback) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(context.getString(R.string.themoviedb_url)).setLogLevel(RestAdapter.LogLevel.FULL).build();

        MovieResultsService service = restAdapter.create(MovieResultsService.class);

        service.result(sort, context.getString(R.string.api_key), callback);
    }
}
