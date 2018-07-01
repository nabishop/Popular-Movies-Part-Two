package com.example.android.popularmoviesparttwo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String id;
    private String title;
    private String picture;
    private String overview;
    private String date;
    private double rating;

    public Movie(String id, String title, String picture,
                 String overview, String date, double rating) {
        this.id = id;
        this.title = title;
        this.picture = picture;
        this.overview = overview;
        this.date = date;
        this.rating = rating;
    }

    private Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.picture = in.readString();
        this.overview = in.readString();
        this.date = in.readString();
        this.rating = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(picture);
        dest.writeString(overview);
        dest.writeString(date);
        dest.writeDouble(rating);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public double getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getDate() {
        return date;
    }

    public String getPicture() {
        return picture;
    }

    public String getTitle() {
        return title;
    }
}
