package com.example.android.popularmoviesparttwo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nick on 7/7/2018.
 */

public class Review implements Parcelable {
    private String user;
    private String review;

    public Review(String user, String review) {
        this.user = user;
        this.review = review;
    }

    private Review(Parcel in) {
        user = in.readString();
        review = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user);
        dest.writeString(review);
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getReview() {
        return review;
    }

    public String getUser() {
        return user;
    }
}
