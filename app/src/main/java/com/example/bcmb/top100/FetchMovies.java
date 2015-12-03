package com.example.bcmb.top100;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

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

class FetchMovies extends AsyncTask<String, Void, ArrayList<MovieItem>> {
    private ProgressDialog pDialog;
    private Context mContext;
    private MovieAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public FetchMovies(Context c, MovieAdapter adapter, RecyclerView rv) {
        mContext = c;
        mAdapter = adapter;
        mRecyclerView =rv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
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
        MainActivity.movieList = result;
        MainActivity.mAdapter = new MovieAdapter(mContext, MainActivity.movieList);
        MainActivity.mFAdapter = new FavoriteMoviesAdapter(mContext, MainActivity.favoriteMoviesList);
        if (MainActivity.showAll) {
            mRecyclerView.setAdapter(MainActivity.mAdapter);
        } else {
            mRecyclerView.setAdapter(MainActivity.mFAdapter);
        }
    }
}
