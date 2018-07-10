package com.example.android.popularmoviesparttwo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.popularmoviesparttwo.data.Contract.MovieEntry.CONTENT_URI;
import static com.example.android.popularmoviesparttwo.data.Contract.MovieEntry.TABLE_NAME;

/**
 * Created by Nick on 7/1/2018.
 */

public class MovieContentProvider extends ContentProvider {

    private DbHelper mDbHelper;
    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_MOVIE, MOVIE);
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_MOVIE + "/#", MOVIE_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new DbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;

        switch (match) {
            case MOVIE:
                retCursor = db.query(TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case MOVIE_ID:
                String id = uri.getLastPathSegment();
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                retCursor = db.query(TABLE_NAME, projection, mSelection, mSelectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }


        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;
        switch (match) {
            case MOVIE:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int retInt;

        switch (match) {
            case MOVIE_ID:
                String movieId = uri.getLastPathSegment();
                retInt = db.delete(TABLE_NAME, "_id=?",
                        new String[]{movieId});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        if (selection == null || retInt != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return retInt;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int retInt;

        switch (match) {
            case MOVIE_ID:
                String movieId = uri.getLastPathSegment();
                retInt = db.update(TABLE_NAME, values,
                        "_id=?", new String[]{movieId});
                break;
            default:
                throw new UnsupportedOperationException("Unknown or unimplemented Uri " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return retInt;
    }

    public DbHelper getDatabase(){
        return mDbHelper;
    }
}
