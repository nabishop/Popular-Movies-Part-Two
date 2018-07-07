package com.example.android.popularmoviesparttwo;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesparttwo.data.Contract;
import com.squareup.picasso.Picasso;

/**
 * Created by Nick on 6/19/2018.
 */

public class MovieDetails extends AppCompatActivity {
    private static String id;
    private static String title;
    private static String description;
    private static String date;
    private static String poster;
    private static double rating;
    private static String review;
    private ScrollView scrollView;
    private static String SAVE_INSTANCE_STRING = "SCROLL_LOCATION";

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
        ratingTextView.setText("Rating - " + String.valueOf(rating) + "/10");

        // get release date into view
        TextView dateTextView = findViewById(R.id.date_tv);
        dateTextView.setText("Release Date - " + date);

        // get description text into view
        TextView descriptionTextView = findViewById(R.id.description_tv);
        descriptionTextView.setText("Description\n" + description);
    }

    public void onClickAddFavorite(View view) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.MovieEntry.COLUMN_NAME, title);
        contentValues.put(Contract.MovieEntry.COLUMN_MOVIEID, id);
        contentValues.put(Contract.MovieEntry.COLUMN_PICTURE, poster);
        contentValues.put(Contract.MovieEntry.COLUMN_OVERVIEW, description);
        contentValues.put(Contract.MovieEntry.COLUMN_RATING, rating);
        contentValues.put(Contract.MovieEntry.COLUMN_DATE, date);
        getContentResolver().insert(Contract.MovieEntry.CONTENT_URI, contentValues);
        Toast.makeText(this, "Added " + title + " to Favorites",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putIntArray(SAVE_INSTANCE_STRING, new int[]{scrollView.getScrollX(), scrollView.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray(SAVE_INSTANCE_STRING);
        if (position != null) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(position[0], position[1]);
                }
            });
        }
    }

    private class ReviewTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            review = null;
        }
    }
}
