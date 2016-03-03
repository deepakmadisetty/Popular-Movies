package com.example.android.popularmovies.data;

/**
 * Created by Deepak on 3/3/16.
 */

import android.test.AndroidTestCase;


        import android.content.ContentValues;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.test.AndroidTestCase;
        import android.util.Log;

public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void testDeleteDb() throws Throwable {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    public void testInsertProvider() throws Throwable {
        SQLiteDatabase db = new MovieDbHelper(mContext).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, "281952");
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, "The Revenant");
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "/oXUWEc5i3wYyFnL1Ycu8ppxxPvs.jpg");
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, "/uS1SkjVviraGfFNgkDwe7ohTm8B.jpg");
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "In the 1820s, a frontiersman, Hugh Glass, sets out on a path of vengeance against those who left him for dead after a bear mauling.");
        values.put(MovieContract.MovieEntry.COLUMN_USER_RATING, "7.26");
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2015-12-25");


        long rowId;
        rowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);

        assertTrue(rowId != -1);
        Log.d(LOG_TAG, "New row id: " + rowId);


        String[] colums = {
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
                MovieContract.MovieEntry.COLUMN_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_USER_RATING,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
        };


        Cursor c1 = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        Cursor c = mContext.getContentResolver().query(
                MovieContract.MovieEntry.buildMovieUri(281952),
                null,
                null,
                null,
                null
        );

        Log.d(LOG_TAG, String.valueOf(c1.getCount()));

        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
            c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE));
            c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
            c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH));
            c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
            Log.d(LOG_TAG, " Movie id " + id);

            assertEquals("281952", id);

        }
    }

    public void testGetType() throws Throwable {

        String type = mContext.getContentResolver().getType(MovieContract.MovieEntry.CONTENT_URI);
        assertEquals(MovieContract.MovieEntry.CONTENT_TYPE, type);

        Log.d(LOG_TAG, type);

        String testMovieID = "281952";
        type = mContext.getContentResolver().getType(MovieContract.MovieEntry.buildMoviewithId(testMovieID));
        assertEquals(MovieContract.MovieEntry.CONTENT_ITEM_TYPE, type);

        Log.d(LOG_TAG, type);


    }
}