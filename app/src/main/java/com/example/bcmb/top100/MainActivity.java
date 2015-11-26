package com.example.bcmb.top100;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "taggy";
    private ProgressDialog pDialog;
    private ArrayList<MovieItem> movieList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetMovies().execute();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i = new Intent(getApplicationContext(), MovieDetailedInfo.class);
                        i.putExtra("moveDetails", formatDataForDetailedView(movieList, position));
                        startActivity(i);
                    }
                })
        );

    }

    private class GetMovies extends AsyncTask<String, Void, ArrayList<MovieItem>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected ArrayList<MovieItem> doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String json;
            ArrayList<MovieItem> movies = new ArrayList<>();
            try {
                URL url = new URL("http://api.myapifilms.com/imdb/top?start=1&end=20&token=452e19d8-0763-4b1e-8c52-a559b5d35fe8&format=json&data=0");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    json = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    json = null;
                }
                json = buffer.toString();
            } catch (IOException e) {
                json = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                JSONObject jsonObj = new JSONObject(json);
                JSONObject data = jsonObj.getJSONObject("data");
                JSONArray movieObjectsArray = data.getJSONArray("movies");
                for (int i = 0; i < movieObjectsArray.length(); i++) {
                    JSONObject movieItem = movieObjectsArray.getJSONObject(i);
                    String posterUrl = movieItem.getString("urlPoster");
                    String title = movieItem.getString("title");
                    String year = movieItem.getString("year");
                    JSONArray directors = movieItem.getJSONArray("directors");
                    String director = "";
                    for (int j = 0; j < directors.length(); j++) {
                        JSONObject directorItem = directors.getJSONObject(j);
                        director += directorItem.getString("name");
                    }
                    String rating = movieItem.getString("rating");
                    String plot = movieItem.getString("plot");
                    JSONArray genres = movieItem.getJSONArray("genres");
                    String genre = genres.getString(0);
                    movies.add(new MovieItem(posterUrl, title, year, director, rating, plot, genre));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movies;
        }
        @Override
        protected void onPostExecute(ArrayList<MovieItem> result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            movieList = result;
            mAdapter = new MovieAdapter(getApplicationContext(), movieList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private String[] formatDataForDetailedView(ArrayList<MovieItem> mi, int index) {
        String[] extrasArray = new String[] {
                (mi.get(index)).getPosterUrl(),
                (mi.get(index)).getTitle(),
                (mi.get(index)).getYear(),
                (mi.get(index)).getDirector(),
                (mi.get(index)).getRating(),
                (mi.get(index)).getPlot(),
                (mi.get(index)).getGenres()
        };
        return extrasArray;
    }
}
