package com.example.android.popularmoviesparttwo.utils;

import android.net.Uri;

import com.example.android.popularmoviesparttwo.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 6/18/2018.
 */

public class LoadMovies {
    public static class JSONParsing {
        private static final String resultsKey = "results";
        private static final String idKey = "id";
        private static final String titleKey = "title";
        private static final String overviewKey = "overview";
        private static final String ratingKey = "vote_average";
        private static final String releaseDateKey = "release_date";
        private static final String imagePathKey = "poster_path";
    }

    public static List<Movie> getMovieList(String sort) {
        System.out.println("PASSING IN " + sort + " IN getMovieList");
        if (sort != null) {
            switch (sort) {
                case URLParsing.toprated:
                    return loadMovies(sort.substring(0, sort.length() - 1));
                case URLParsing.popular:
                    return loadMovies(sort.substring(0, sort.length() - 1));
            }
        }
        return null;
    }

    private static List<Movie> loadMovies(String sort) {
        System.out.println("PASSING IN " + sort + " IN loadMovies");
        URL request = getMovieURL(URLParsing.BASE_URL, sort, URLParsing.LOGIN);
        System.out.println("URL REQUEST IN loadMovies: " + request.toString());

        if (request != null) {
            String response = Connection.urlMovieRequest(request);

            if (response != null) {
                List<Movie> movieList = parseResponse(response,
                        URLParsing.IMAGE_BASE_URL + URLParsing.IMAGE_SIZE);
                if (movieList != null && movieList.size() > 0) {
                    return movieList;
                }
            }
        }
        return null;
    }


    // get movie list from response from website
    private static List<Movie> parseResponse(String response, String imageBase) {
        try {
            // iterate through movie list
            JSONArray results = new JSONObject(response).getJSONArray(JSONParsing.resultsKey);
            List<Movie> movieList = new ArrayList<>();
            for (int x = 0; x < results.length(); x++) {
                // current movie
                JSONObject movie = results.getJSONObject(x);

                // movie attributes
                String id = movie.getInt(JSONParsing.idKey) + "";
                String title = movie.getString(JSONParsing.titleKey);
                String overview = movie.getString(JSONParsing.overviewKey);
                double rating = movie.getDouble(JSONParsing.ratingKey);
                String picture = getImageUrl(movie, imageBase);
                String date = getDate(movie);

                //make and add movie to list
                movieList.add(new Movie(id, title, picture, overview, date, rating));

            }
            return movieList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // helper to get the date of the movie
    private static String getDate(JSONObject jsonObject) {
        try {
            return jsonObject.getString(JSONParsing.releaseDateKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // helper to get the image url for a poster of a movie
    private static String getImageUrl(JSONObject jsonObject, String imageBaseUrl) {
        try {
            String poster = jsonObject.getString(JSONParsing.imagePathKey);
            Uri imageUri = Uri.parse(imageBaseUrl).buildUpon().appendPath(poster)
                    .build();
            return new URL(URLDecoder.decode(imageUri.toString(), "utf-8")).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // helper to build my URL based on sorting method (popularity/rating)
    private static URL getMovieURL(String baseUrl, String sortBy, String apiKey) {
        Uri reguesting = Uri.parse(baseUrl).buildUpon().appendPath(sortBy)
                .appendQueryParameter(URLParsing.API_KEY, apiKey).build();
        try {
            return new URL(reguesting.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}