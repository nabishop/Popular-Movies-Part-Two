package com.example.android.popularmoviesparttwo.utils;

import android.content.Context;
import android.database.Cursor;

import com.example.android.popularmoviesparttwo.data.Contract;
import com.example.android.popularmoviesparttwo.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 7/8/2018.
 */

public class CursorHelper {
    public static Movie getMovieFromCursor(Cursor cursor) {
        String movieName = cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_NAME));
        String movieId = cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_MOVIEID));
        String picture = cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_PICTURE));
        String overview = cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_OVERVIEW));
        String rating = cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_RATING));
        String date = cursor.getString(cursor.getColumnIndex(Contract.MovieEntry.COLUMN_DATE));
        double ratingConverted = Double.parseDouble(rating);
        return new Movie(movieId, movieName, picture, overview, date, ratingConverted);
    }

    public static ArrayList<Movie> queryAllFavouriteMoviesFromDb(Context context) {
        String[] columnsToRetrieve = {Contract.MovieEntry.COLUMN_NAME,
                Contract.MovieEntry.COLUMN_MOVIEID,
                Contract.MovieEntry.COLUMN_PICTURE,
                Contract.MovieEntry.COLUMN_OVERVIEW,
                Contract.MovieEntry.COLUMN_RATING,
                Contract.MovieEntry.COLUMN_DATE,
        };
        Cursor cursor = context.getContentResolver().query(Contract.MovieEntry.CONTENT_URI,
                columnsToRetrieve, null, null, Contract.MovieEntry.COLUMN_MOVIEID);
        if (cursor.moveToFirst()) {
            Movie movie = getMovieFromCursor(cursor);
            ArrayList<Movie> MovieList = new ArrayList<>();
            MovieList.add(movie);
            while (cursor.moveToNext()) {
                Movie nextMovie = getMovieFromCursor(cursor);
                MovieList.add(nextMovie);
            }
            return MovieList;
        } else {
            return null;
        }
    }
}
