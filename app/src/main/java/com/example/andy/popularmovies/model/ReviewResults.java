package com.example.andy.popularmovies.model;

import java.util.List;

/**
 * Created by Andy on 1/20/2016.
 */
public class ReviewResults implements MovieExtra {
    private int id;
    private int page;
    private List<Review> results;
    private int total_pages;
    private int total_results;

    public ReviewResults(int id, int page, List<Review> results, int total_pages, int total_results) {
        this.id = id;
        this.page = page;
        this.results = results;
        this.total_pages = total_pages;
        this.total_results = total_results;
    }

    public List<Review> getResults() {
        return results;
    }
}
