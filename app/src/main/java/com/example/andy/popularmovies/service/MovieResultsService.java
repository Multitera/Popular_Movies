package com.example.andy.popularmovies.service;

import com.example.andy.popularmovies.model.Results;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Andy on 7/29/2015.
 */
public interface MovieResultsService {

    @GET("/3/discover/movie")
    void result(@Query("sort_by") String sort, @Query("api_key") String api_key, Callback<Results> callback);
}
