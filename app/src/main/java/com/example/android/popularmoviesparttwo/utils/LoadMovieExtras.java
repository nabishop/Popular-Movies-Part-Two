package com.example.android.popularmoviesparttwo.utils;

import android.net.Uri;

import com.example.android.popularmoviesparttwo.model.Review;
import com.example.android.popularmoviesparttwo.model.Video;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nick on 7/7/2018.
 */

public class LoadMovieExtras {
    private static final String RESULTS = "results";
    private static final String USER = "author";
    private static final String REVIEW = "content";
    private static final String WEBSITE = "site";
    private static final String YOUTUBE = "YouTube";
    private static final String VIDEO_NAME = "name";
    private static final String YOUTUBE_KEY = "key";
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    public static ArrayList<Review> getMovieReviews(String movieId) {
        String jsonResponse = getReviewsJsonResponse(movieId);
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.has(RESULTS)) {
                JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject review = jsonArray.getJSONObject(i);
                        String user = review.getString(USER);
                        String movieReview = review.getString(REVIEW);
                        reviews.add(new Review(user, movieReview));
                    }
                    return reviews;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static Video getFirstTrailer(String movieId) {
        String jsonResponse = getVideoJsonResponse(movieId);
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.has(RESULTS)) {
                JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);
                if (jsonArray.length() > 0) {
                    JSONObject video = jsonArray.getJSONObject(0);
                    if (video.getString(WEBSITE).equals(YOUTUBE)) {
                        String videoName = video.getString(VIDEO_NAME);
                        String key = video.getString(YOUTUBE_KEY);
                        String url = BASE_YOUTUBE_URL + key;
                        return new Video(videoName, url, key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private static String getReviewsJsonResponse(String movieId) {
        return Connection.urlMovieRequest(getRequestURL(URLParsing.BASE_URL,
                movieId, URLParsing.REQUEST_REVIEWS));
    }

    private static String getVideoJsonResponse(String movieId) {
        return Connection.urlMovieRequest(getRequestURL(URLParsing.BASE_URL,
                movieId, URLParsing.REQUEST_VIDEOS));
    }

    private static URL getRequestURL(String base, String movieId, String searchParameter) {
        Uri request = Uri.parse(base).buildUpon().appendPath(movieId)
                .appendPath(searchParameter).appendQueryParameter(URLParsing.API_KEY, URLParsing.LOGIN)
                .build();
        try {
            return new URL(request.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
