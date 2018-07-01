package com.example.android.popularmoviesparttwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Nick on 6/19/2018.
 */

public class MovieDetails extends AppCompatActivity {
    private String id;
    private String title;
    private String description;
    private String date;
    private String poster;
    private double rating;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpUI(savedInstanceState);
        populateUI();
    }

    private void setUpUI(Bundle savedInstanceState) {
        setContentView(R.layout.movie_details);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        date = intent.getStringExtra("date");
        poster = intent.getStringExtra("poster");
        rating = intent.getDoubleExtra("rating", 0.0);
    }

    private void populateUI() {
        // get image into view
        ImageView imageView = findViewById(R.id.poster_iv);
        Picasso.with(getApplicationContext()).load(poster).into(imageView);

        // get title text into view
        TextView titleTextView = findViewById(R.id.movie_tv);
        titleTextView.setText(title);

        // get rating into view
        TextView ratingTextView = findViewById(R.id.rating_tv);
        ratingTextView.setText("Rating " + String.valueOf(rating));

        // get release date into view
        TextView dateTextView = findViewById(R.id.date_tv);
        dateTextView.setText("Release Date " + date);

        // get description text into view
        TextView descriptionTextView = findViewById(R.id.description_tv);
        descriptionTextView.setText("Description\n" + description);
    }
}
