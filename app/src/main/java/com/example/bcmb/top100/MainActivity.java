package com.example.bcmb.top100;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String SHOW_ALL = "com.example.bcmb.top100.showAllMovies";
    public static MovieAdapter mAdapter;
    public static ArrayList<MovieItem> movieList = new ArrayList<>();
    public static ArrayList<MovieItem> favoriteMoviesList = new ArrayList<>();
    public static ArrayList<MovieItem> tempMovieList = new ArrayList<>();
    public static boolean showAll = true;
    private RecyclerView mRecyclerView;
    public static FavoriteMoviesAdapter mFAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final int SETTINGS_REQ_CODE = 0;
    private SharedPreferences sharedPrefs;
    private static String filename = "moviesStorage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MovieAdapter(MainActivity.this, MainActivity.movieList);
        mFAdapter = new FavoriteMoviesAdapter(MainActivity.this, MainActivity.favoriteMoviesList);
        new FetchMovies(MainActivity.this, mAdapter, mRecyclerView).execute();
        readMovieDataFromFile();
        loadPrefs();
        updateMovieListAfterReopen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu_item:
                Intent i = new Intent(this, SettingsActivity.class);
                i.putExtra(SHOW_ALL, showAll);
                startActivityForResult(i, SETTINGS_REQ_CODE);
                return true;
            case R.id.refresh_action:
                updateUI();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (resultCode != Activity.RESULT_OK) {
            return;
        }
       if (requestCode == SETTINGS_REQ_CODE) {
           if (data == null) {
                return;
           }
           showAll = data.getBooleanExtra(SHOW_ALL, true);
           if (showAll) {
               mRecyclerView.setAdapter(mAdapter);
               mAdapter.notifyDataSetChanged();
                } else {
               mRecyclerView.setAdapter(mFAdapter);
               mFAdapter.notifyDataSetChanged();
           }
       }
    }

    @Override
    protected void onStop() {
        super.onStop();
        syncMoveLists();
    }

    @Override
    protected void onStart() {
        super.onStart();
        syncMoveLists();
        updateMovieListAfterReopen();
    }


    private void syncMoveLists() {
        favoriteMoviesList.clear();
        favoriteMoviesList.addAll(tempMovieList);
    }

    private void updateUI() {
        syncMoveLists();
        mAdapter.notifyDataSetChanged();
        mFAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePrefs();
        writeMovieDataToFile();
    }

    private void savePrefs() {
        sharedPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(SHOW_ALL, showAll);
        editor.commit();
    }

    private void loadPrefs() {
        sharedPrefs = getPreferences(MODE_PRIVATE);
        showAll = sharedPrefs.getBoolean(SHOW_ALL, true);
    }

    private void writeMovieDataToFile() {

        try {
            FileOutputStream fos = this.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(favoriteMoviesList);
            oos.close();
            fos.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        syncMoveLists();
        updateMovieListAfterReopen();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMovieListAfterReopen();
        mAdapter.notifyDataSetChanged();
    }

    public void readMovieDataFromFile() {
        try {
            FileInputStream fis = this.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            tempMovieList = (ArrayList<MovieItem>) ois.readObject();
            ois.close();
            fis.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return;
        }
    }

    public static void updateMovieListAfterReopen() {
        for (MovieItem mi : MainActivity.favoriteMoviesList) {
            for (int i = 0; i < MainActivity.movieList.size(); i++) {
                if (MainActivity.movieList.get(i).getTitle().equals(mi.getTitle())) {
                    MainActivity.movieList.get(i).setIsFavourite(true);
                    break;
                }
            }
        }
    }
}
