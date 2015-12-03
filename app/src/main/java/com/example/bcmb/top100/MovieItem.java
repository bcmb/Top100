package com.example.bcmb.top100;

import java.io.Serializable;

public class MovieItem implements Serializable {
    private String posterUrl;
    private String title;
    private String year;
    private String director;
    private String rating;
    private String plot;
    private String genres;
    private boolean isFavourite;

    public MovieItem (String posterUrl, String title, String year, String director, String rating, String plot, String genres) {
        this.posterUrl = posterUrl;
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.plot = plot;
        this.genres = genres;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public String getRating() {
        return rating;
    }

    public String getPlot() {
        return plot;
    }

    public String getGenres() {
        return genres;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }
}
