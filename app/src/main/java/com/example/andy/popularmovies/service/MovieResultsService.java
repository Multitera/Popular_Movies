package com.example.andy.popularmovies.service;

import com.example.andy.popularmovies.model.ClipInfoResults;
import com.example.andy.popularmovies.model.MovieResults;
import com.example.andy.popularmovies.model.ReviewResults;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Andy on 7/29/2015.
 */
public interface MovieResultsService {

    @GET("/3/discover/movie")
    void result(@Query("sort_by") String sort, @Query("api_key") String api_key, Callback<MovieResults> callback);

    @GET("/3/movie/{movieId}/videos")
    void clipInfo(@Path("movieId") String movieID, @Query("api_key") String api_key, Callback<ClipInfoResults> callback);

    @GET("/3/movie/{movieId}/reviews")
    void reviews(@Path("movieId") String movieID, @Query("api_key") String api_key, Callback<ReviewResults> callback);
}
