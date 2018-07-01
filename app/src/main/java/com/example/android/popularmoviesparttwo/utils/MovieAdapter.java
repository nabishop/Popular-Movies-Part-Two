package com.example.android.popularmoviesparttwo.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.popularmoviesparttwo.R;
import com.example.android.popularmoviesparttwo.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Nick on 6/19/2018.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for this position
        Movie movie = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            // fix this once created a layour
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_image, parent, false);
        }
        ImageView posterImageView = convertView.findViewById(R.id.custom_iv);
        Picasso.with(getContext()).load(movie.getPicture()).into(posterImageView);
        return convertView;
    }
}
