package com.example.bcmb.top100;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailedInfo extends AppCompatActivity {
    private String[] movieDetails;
    private String mPosterUrl;
    private String mTitle;
    private String mYear;
    private String mDirectors;
    private String mRating;
    private String mPlot;
    private String mGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detailed_info);
        movieDetails = getIntent().getStringArrayExtra("moveDetails");
        getMovieDataFormArray();
        ImageView poster = (ImageView) findViewById(R.id.iv_poster_detailed);
        TextView title = (TextView) findViewById(R.id.tv_title_detailed);
        TextView year = (TextView) findViewById(R.id.tv_year_detailed);
        TextView directors = (TextView) findViewById(R.id.tv_director_detailed);
        TextView rating = (TextView) findViewById(R.id.tv_rating_detailed);
        TextView plot = (TextView) findViewById(R.id.tv_plot_detailed);
        TextView genres = (TextView) findViewById(R.id.tv_genre_detailed);
        Picasso.with(getApplicationContext()).load(mPosterUrl).into(poster);
        title.setText(mTitle);
        year.setText("Year: "+mYear);
        directors.setText("Director: "+mDirectors);
        rating.setText("IMDB rating: "+mRating);
        plot.setText(mPlot);
        genres.setText("Genre: "+mGenres);
    }

    private void getMovieDataFormArray() {
        for (int i = 0; i < movieDetails.length; i++) {
            switch (i) {
                case (0):
                    this.mPosterUrl = movieDetails[i];
                    break;
                case (1):
                    this.mTitle = movieDetails[i];
                    break;
                case (2):
                    this.mYear = movieDetails[i];
                    break;
                case (3):
                    this.mDirectors = movieDetails[i];
                    break;
                case (4):
                    this.mRating = movieDetails[i];
                    break;
                case (5):
                    this.mPlot = movieDetails[i];
                    break;
                case (6):
                    this.mGenres = movieDetails[i];
                    break;
            }
        }
    }
}
