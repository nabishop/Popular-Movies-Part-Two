package com.example.android.popularmoviesparttwo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.android.popularmoviesparttwo.data.Contract.MovieEntry.COLUMN_DATE;
import static com.example.android.popularmoviesparttwo.data.Contract.MovieEntry.COLUMN_MOVIEID;
import static com.example.android.popularmoviesparttwo.data.Contract.MovieEntry.COLUMN_NAME;
import static com.example.android.popularmoviesparttwo.data.Contract.MovieEntry.COLUMN_OVERVIEW;
import static com.example.android.popularmoviesparttwo.data.Contract.MovieEntry.COLUMN_PICTURE;
import static com.example.android.popularmoviesparttwo.data.Contract.MovieEntry.COLUMN_RATING;
import static com.example.android.popularmoviesparttwo.data.Contract.MovieEntry.TABLE_NAME;

/**
 * Created by Nick on 7/1/2018.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "movieList.db";

    public DbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT NOT NULL, " + COLUMN_MOVIEID + " TEXT , "
                + COLUMN_PICTURE + " TEXT, " + COLUMN_OVERVIEW + " TEXT, "
                + COLUMN_RATING + " TEXT, " + COLUMN_DATE + " TEXT  " + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
