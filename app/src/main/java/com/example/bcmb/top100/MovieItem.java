package com.example.bcmb.top100;

public class MovieItem {
    private String posterUrl;
    private String title;
    private String year;
    private String director;
    private String rating;
    private String plot;
    private String genres;

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
}
