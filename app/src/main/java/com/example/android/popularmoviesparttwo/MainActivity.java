package com.example.android.popularmoviesparttwo;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import com.example.android.popularmoviesparttwo.data.Contract;
import com.example.android.popularmoviesparttwo.model.Movie;
import com.example.android.popularmoviesparttwo.utils.CursorHelper;
import com.example.android.popularmoviesparttwo.utils.FavoriteMovieAdapter;
import com.example.android.popularmoviesparttwo.utils.LoadMovies;
import com.example.android.popularmoviesparttwo.utils.MovieAdapter;
import com.example.android.popularmoviesparttwo.utils.URLParsing;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static GridView gridView;
    private static ArrayList<Movie> movies;
    private static ArrayList<Movie> favoritedMovies;
    private static MovieAdapter movieAdapter;
    private static FavoriteMovieAdapter favoriteMovieAdapter;
    private static String title;
    private static double rating;
    private static String date;
    private static String description;
    private static String poster;
    private static String id;
    private static final String MOVIE_LIST_LIFE_KEY = "movieList";
    private static boolean onFavorites = false;
    private static boolean firstOpen = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            System.out.println("SAVED INSTANCE STATE " + savedInstanceState.toString() + "\n");
        }
        super.onCreate(savedInstanceState);
        if (!onFavorites) {
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
            if (firstOpen) {
                new MovieASyncTask().execute(URLParsing.popular);
            }
        } else {
            favoritedMovies = CursorHelper.queryAllFavouriteMoviesFromDb(this);
            favoritesMenuSelectedHelper();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.top_rated_menu) {
            onFavorites = false;
            firstOpen = false;
            setContentView(R.layout.activity_main);
            movieAdapter = new MovieAdapter(this, movies);
            gridView = findViewById(R.id.gridView);
            gridView.setAdapter(movieAdapter);
            helperOnItemClick();
            new MovieASyncTask().execute(URLParsing.toprated);
        } else if (id == R.id.popular_menu) {
            onFavorites = false;
            firstOpen = false;
            setContentView(R.layout.activity_main);
            movieAdapter = new MovieAdapter(this, movies);
            gridView = findViewById(R.id.gridView);
            gridView.setAdapter(movieAdapter);
            helperOnItemClick();
            new MovieASyncTask().execute(URLParsing.popular);
        } else {
            onFavorites = true;
            firstOpen = false;
            favoritesMenuSelectedHelper();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void favoritesMenuSelectedHelper() {
        setContentView(R.layout.favorite_movies);
        gridView = findViewById(R.id.favorites_screen);
        favoritedMovies = CursorHelper.queryAllFavouriteMoviesFromDb(this);
        System.out.println("FAVORITED MOVIES: " + favoritedMovies);
        if (favoritedMovies != null) {
            favoriteMovieAdapter = new FavoriteMovieAdapter(this, favoritedMovies);
            gridView.setAdapter(favoriteMovieAdapter);
            favoritesHelperOnClick();
        }
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

    private void favoritesHelperOnClick() {
        System.out.println("CLICKER CLICKED");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                Intent intent = new Intent(MainActivity.this, MovieDetails.class);
                System.out.println("MOVIE NAME ACCESSING " + favoriteMovieAdapter.getItem(position).getTitle());
                title = favoriteMovieAdapter.getItem(position).getTitle();
                intent.putExtra("title", title);
                rating = favoriteMovieAdapter.getItem(position).getRating();
                intent.putExtra("rating", rating);
                date = favoriteMovieAdapter.getItem(position).getDate();
                intent.putExtra("date", date);
                description = favoriteMovieAdapter.getItem(position).getOverview();
                intent.putExtra("description", description);
                poster = favoriteMovieAdapter.getItem(position).getPicture();
                intent.putExtra("poster", poster);
                id = favoriteMovieAdapter.getItem(position).getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        System.out.println("SAVED");
        outState.putParcelableArrayList(MOVIE_LIST_LIFE_KEY, movies);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        System.out.println("RESTORED");
        movies = savedInstanceState.getParcelableArrayList(MOVIE_LIST_LIFE_KEY);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        System.out.println("STARTED");
        if (onFavorites) {
            favoritesMenuSelectedHelper();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        System.out.println("STOPPED");
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