package com.example.android.popularmoviesparttwo;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import com.example.android.popularmoviesparttwo.data.Contract;
import com.example.android.popularmoviesparttwo.model.Movie;
import com.example.android.popularmoviesparttwo.utils.LoadMovies;
import com.example.android.popularmoviesparttwo.utils.MovieAdapter;
import com.example.android.popularmoviesparttwo.utils.URLParsing;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static GridView gridView;
    private static ArrayList<Movie> movies;
    private static MovieAdapter movieAdapter;
    private static String title;
    private static double rating;
    private static String date;
    private static String description;
    private static String poster;
    private static String id;
    private static final String MOVIE_LIST_LIFE_KEY = "movieList";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            System.out.println("SAVED INSTANCE STATE " + savedInstanceState.toString() + "\n");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridView);
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_LIST_LIFE_KEY)) {
            movies = savedInstanceState.getParcelableArrayList(MOVIE_LIST_LIFE_KEY);
        } else {
            movies = new ArrayList<>();
        }
        movieAdapter = new MovieAdapter(this, movies);
        gridView.setAdapter(movieAdapter);
        helperOnItemClick();
        // asynctask
        new MovieASyncTask().execute(URLParsing.popular);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.top_rated_menu) {
            new MovieASyncTask().execute(URLParsing.toprated);
        } else if (id == R.id.popular_menu) {
            new MovieASyncTask().execute(URLParsing.popular);
        } else {
            favoritesMenuSelectedHelper();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void favoritesMenuSelectedHelper() {
        Cursor movies = getContentResolver().query(Contract.MovieEntry.CONTENT_URI, null,
                null, null, Contract.MovieEntry.COLUMN_MOVIEID);
        startManagingCursor(movies);

        setContentView(R.layout.favorite_movies);
        gridView=findViewById(R.id.favorites_screen);
        ArrayList<Movie> favoritedMovies = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, favoritedMovies);
        gridView.setAdapter(movieAdapter);
        helperOnItemClick();
    }

    private void helperOnItemClick() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                Intent intent = new Intent(MainActivity.this, MovieDetails.class);
                title = movieAdapter.getItem(position).getTitle();
                intent.putExtra("title", title);
                rating = movieAdapter.getItem(position).getRating();
                intent.putExtra("rating", rating);
                date = movieAdapter.getItem(position).getDate();
                intent.putExtra("date", date);
                description = movieAdapter.getItem(position).getOverview();
                intent.putExtra("description", description);
                poster = movieAdapter.getItem(position).getPicture();
                intent.putExtra("poster", poster);
                id = movieAdapter.getItem(position).getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_LIST_LIFE_KEY, movies);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        movies = savedInstanceState.getParcelableArrayList(MOVIE_LIST_LIFE_KEY);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private static class MovieASyncTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... strings) {
            if (strings.length < 1 || strings[0] == null) {
                return null;
            }
            return LoadMovies.getMovieList(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            movieAdapter.clear();
            if (movies != null && !movies.isEmpty()) {
                movieAdapter.addAll(movies);
            }
        }
    }


}