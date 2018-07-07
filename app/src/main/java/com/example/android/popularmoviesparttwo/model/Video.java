package com.example.android.popularmoviesparttwo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nick on 7/7/2018.
 */

public class Video implements Parcelable {
    private String videoName;
    private String urlLink;
    private String key;

    public Video(String videoName, String urlLink, String key) {
        this.videoName = videoName;
        this.urlLink = urlLink;
        this.key = key;
    }

    private Video(Parcel source) {
        videoName = source.readString();
        urlLink = source.readString();
        key = source.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoName);
        dest.writeString(urlLink);
        dest.writeString(key);
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {

            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getUrlLink() {
        return urlLink;
    }
}
