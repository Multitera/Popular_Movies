package com.example.andy.popularmovies.model;

import java.util.List;

/**
 * Created by Andy on 1/20/2016.
 */
public class ClipInfoResults implements MovieExtra {
    private int id;
    private List<ClipInfo> results;

    public ClipInfoResults(int id, List<ClipInfo> results) {
        this.id = id;
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public List<ClipInfo> getResults() {
        return results;
    }
}
