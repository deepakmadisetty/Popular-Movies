package com.example.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.example.android.popularmovies.data.MovieContract;

/**
 * Created by Deepak on 3/2/16.
 */
public class Utility {

    public static int isFavorite(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[] { Integer.toString(id) },
                null
        );
        int numRows = cursor.getCount();
        cursor.close();
        return numRows;
    }

}
