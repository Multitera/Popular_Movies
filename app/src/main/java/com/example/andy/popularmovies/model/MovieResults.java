package com.example.andy.popularmovies.model;

import java.util.List;

/**
 * Created by Andy on 7/29/2015.
 */
public class MovieResults {
    private int id;
    private int page;
    private List<Movie> results;
    private int total_pages;
    private int total_results;

    public MovieResults(int total_results, int page, List<Movie> results, int total_pages) {
        this.total_results = total_results;
        this.page = page;
        this.results = results;
        this.total_pages = total_pages;
    }

    public List<Movie> getResults() {

        return results;
    }
}
