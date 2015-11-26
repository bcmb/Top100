package com.example.bcmb.top100;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter {
    private ArrayList<MovieItem> mMovieData = new ArrayList();
    private Context mContext;
    private static MyClickListener myClickListener;

    public MovieAdapter(Context c, ArrayList<MovieItem> movies) {
        mMovieData = movies;
        mContext = c;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public ImageView posterImage;
        public TextView title;
        public TextView year;

        public MyViewHolder(View v) {
            super(v);
            this.posterImage = (ImageView) v.findViewById(R.id.iv_poster);
            this.title = (TextView) v.findViewById(R.id.tv_title);
            this.year = (TextView) v.findViewById(R.id.tv_year);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Picasso.with(mContext).load(mMovieData.get(position).getPosterUrl()).into(((MyViewHolder) holder).posterImage);
        ((MyViewHolder) holder).title.setText(mMovieData.get(position).getTitle());
        ((MyViewHolder) holder).year.setText(mMovieData.get(position).getYear());
    }

    @Override
    public int getItemCount() {
        return mMovieData.size();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
