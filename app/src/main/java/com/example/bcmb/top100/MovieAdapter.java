package com.example.bcmb.top100;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter {
    private ArrayList<MovieItem> mMovieData = new ArrayList();
    private Context mContext;
    private MyViewHolder myViewHolder;

    public MovieAdapter(Context c, ArrayList<MovieItem> movies) {
        mContext = c;
        mMovieData = movies;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView posterImage;
        public TextView title;
        public TextView year;
        public CheckBox favourites;

        public MyViewHolder(View v) {
            super(v);
            this.posterImage = (ImageView) v.findViewById(R.id.iv_poster);
            this.title = (TextView) v.findViewById(R.id.tv_title);
            this.year = (TextView) v.findViewById(R.id.tv_year);
            this.favourites = (CheckBox) v.findViewById(R.id.favorites_star);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Picasso.with(mContext).load(MainActivity.movieList.get(position).getPosterUrl()).into(((MyViewHolder) holder).posterImage);
        ((MyViewHolder) holder).title.setText(MainActivity.movieList.get(position).getTitle());
        ((MyViewHolder) holder).year.setText(MainActivity.movieList.get(position).getYear());
        ((MyViewHolder) holder).favourites.setChecked(MainActivity.movieList.get(position).isFavourite());
        ((MyViewHolder) holder).favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieItem mv = MainActivity.movieList.get(position);
                if (mv.isFavourite()) {
                    mv.setIsFavourite(false);
                } else {
                    mv.setIsFavourite(true);
                }
                if (MainActivity.favoriteMoviesList.contains(mv)) {
                    MainActivity.favoriteMoviesList.remove(mv);
                    MainActivity.tempMovieList.remove(mv);
                } else {
                    MainActivity.favoriteMoviesList.add(mv);
                    MainActivity.tempMovieList.add(mv);
                }
            }
        });

        ((MyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, MovieDetailedInfo.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("moveDetails", formatDataForDetailedView(mMovieData, position));
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieData.size();
    }

    private String[] formatDataForDetailedView(ArrayList<MovieItem> mi, int index) {
        return new String[] {
                (mi.get(index)).getPosterUrl(),
                (mi.get(index)).getTitle(),
                (mi.get(index)).getYear(),
                (mi.get(index)).getDirector(),
                (mi.get(index)).getRating(),
                (mi.get(index)).getPlot(),
                (mi.get(index)).getGenres()
        };
    }
}
