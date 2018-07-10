package com.example.android.popularmoviesparttwo.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesparttwo.R;
import com.example.android.popularmoviesparttwo.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nick on 7/9/2018.
 */

public class FavoriteMovieAdapter extends ArrayAdapter<Movie> {

    public FavoriteMovieAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for this position
        Movie movie = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            // fix this once created a layour
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.favorite_movie_view, parent, false);
        }
        ImageView posterImageView = convertView.findViewById(R.id.favorite_poster);
        Picasso.with(getContext()).load(movie.getPicture()).into(posterImageView);

        Button movieTitle = convertView.findViewById(R.id.favorite_title);
        movieTitle.setText("Unfavorite " + movie.getTitle());
        return convertView;
    }

}
