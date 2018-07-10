package com.example.android.popularmoviesparttwo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesparttwo.data.Contract;
import com.example.android.popularmoviesparttwo.data.MovieContentProvider;
import com.example.android.popularmoviesparttwo.model.Movie;
import com.example.android.popularmoviesparttwo.model.Review;
import com.example.android.popularmoviesparttwo.model.Video;
import com.example.android.popularmoviesparttwo.utils.CursorHelper;
import com.example.android.popularmoviesparttwo.utils.LoadMovieExtras;
import com.example.android.popularmoviesparttwo.utils.ReviewsAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmoviesparttwo.data.Contract.MovieEntry.TABLE_NAME;

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
    private ScrollView scrollView;
    private static ArrayList<Review> reviewList;

    private static final String SAVE_INSTANCE_STRING = "SCROLL_LOCATION";
    private static final String SAVE_INSTANCE_REVIEWS = "reviewsListKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpUI(savedInstanceState);
        populateUI();

        reviewList = new ArrayList<>();
        loadReviews();
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

        MovieTrailerASyncTask trailerASyncTask = new MovieTrailerASyncTask();
        trailerASyncTask.execute(id);
        Button trailer = findViewById(R.id.trailer_button);
        trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Video video = LoadMovieExtras.getFirstTrailer(id);
                System.out.println(video);
                if (video.getUrlLink() != null) {
                    i.setData(Uri.parse(video.getUrlLink()));
                    startActivity(i);
                } else {
                    Toast.makeText(getBaseContext(), "No Trailer Available",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
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
        SpannableString ratingBold = new SpannableString(rating + "/10");
        ratingBold.setSpan(new StyleSpan(Typeface.BOLD), 0, ratingBold.length(), 0);
        ratingTextView.setText(ratingBold);

        // get release date into view
        TextView dateTextView = findViewById(R.id.date_tv);
        dateTextView.setText(date);

        // get description text into view
        TextView descriptionTextView = findViewById(R.id.description_tv);
        SpannableString descItalics = new SpannableString(description);
        descItalics.setSpan(new StyleSpan(Typeface.ITALIC), 0, descItalics.length(), 0);
        descriptionTextView.setText(descItalics);
    }

    private void loadReviews() {
        System.out.println("LOADING REVIEWS\n\n");
        MovieReviewASyncTask movieReviewASyncTask = new MovieReviewASyncTask(getBaseContext());
        movieReviewASyncTask.execute(id);

    }

    public void onClickAddFavorite(View view) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.MovieEntry.COLUMN_NAME, title);
        contentValues.put(Contract.MovieEntry.COLUMN_MOVIEID, id);
        contentValues.put(Contract.MovieEntry.COLUMN_PICTURE, poster);
        contentValues.put(Contract.MovieEntry.COLUMN_OVERVIEW, description);
        contentValues.put(Contract.MovieEntry.COLUMN_RATING, rating);
        contentValues.put(Contract.MovieEntry.COLUMN_DATE, date);
        System.out.println(contentValues);
        if (!isAlreadyInDatabase(id)) {
            getContentResolver().insert(Contract.MovieEntry.CONTENT_URI, contentValues);
            Toast.makeText(this, "Added " + title + " to Favorites",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, title + " is already in Favorites", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isAlreadyInDatabase(String movieId) {
        List<Movie> favoritedMovies = CursorHelper.queryAllFavouriteMoviesFromDb(this);
        if(favoritedMovies==null){
            return false;
        }
        for (Movie favMovieEntry : favoritedMovies) {
            if (favMovieEntry.getId().equals(movieId)) {
                System.out.println("ALREADY IN DATABASE");
                return true;
            }
        }
        System.out.println("NOT IN DATABASE");
        return false;
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (reviewList != null) {
            outState.putParcelableArrayList(SAVE_INSTANCE_REVIEWS, reviewList);
        }
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
        reviewList = savedInstanceState.getParcelableArrayList(SAVE_INSTANCE_REVIEWS);
    }

    private static class MovieTrailerASyncTask extends AsyncTask<String, Void, Video> {

        @Override
        protected Video doInBackground(String... strings) {
            if (strings.length < 1 || strings[0] == null) {
                return null;
            }
            return LoadMovieExtras.getFirstTrailer(strings[0]);
        }

        @Override
        protected void onPostExecute(Video video) {
            super.onPostExecute(video);
        }
    }

    private class MovieReviewASyncTask extends AsyncTask<String, Void, ArrayList<Review>> {
        private Context context;

        public MovieReviewASyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<Review> doInBackground(String... strings) {
            if (strings.length < 1 || strings[0] == null) {
                return null;
            }
            return LoadMovieExtras.getMovieReviews(id);
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            System.out.println("POPULATING REVIEW LIST");
            RecyclerView reviewRecyclerView = findViewById(R.id.review_recyclerview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            reviewRecyclerView.setLayoutManager(layoutManager);
            reviewRecyclerView.setHasFixedSize(true);
            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(reviews);
            reviewRecyclerView.setAdapter(reviewsAdapter);
            if (reviews == null || reviews.size() == 0) {
                Toast.makeText(context, "No Reviews Found for " + title, Toast.LENGTH_SHORT).show();
                return;
            }
            super.onPostExecute(reviews);
        }
    }
}
