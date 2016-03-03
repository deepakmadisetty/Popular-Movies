package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Deepak on 12/9/15.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, Void> {

    private final Context mContext;
    public FetchMoviesTask(Context context) {
        this.mContext = context;
    }

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    private void getMovieDataFromJson(String movieJsonStr) throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String TMDB_RESULTS = "results";
        final String TMDB_MOVIE_ID = "id";
        final String TMDB_MOVIE_TITLE ="original_title";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_BACKDROP_PATH = "backdrop_path";
        final String TMDB_OVERVIEW = "overview";
        final String TMDB_USER_RATING = "vote_average";
        final String TMDB_RELEASE_DATE = "release_date";

        String MOVIE_ID;
        String MOVIE_TITLE, POSTER_PATH, BACKDROP_PATH, OVERVIEW, USER_RATING, RELEASE_DATE;

        try {
            JSONObject movieObject = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieObject.getJSONArray(TMDB_RESULTS);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

            for (int i = 0; i < movieArray.length(); i++) {

                // Get the JSON object representing each Movie
                JSONObject movieResult = movieArray.getJSONObject(i);

                ContentValues contentValues = new ContentValues();

                MOVIE_ID =movieResult.getString(TMDB_MOVIE_ID);
                MOVIE_TITLE = movieResult.getString(TMDB_MOVIE_TITLE);
                POSTER_PATH = movieResult.getString(TMDB_POSTER_PATH);
                BACKDROP_PATH = movieResult.getString(TMDB_BACKDROP_PATH);
                OVERVIEW = movieResult.getString(TMDB_OVERVIEW);
                USER_RATING = movieResult.getString(TMDB_USER_RATING);
                RELEASE_DATE = movieResult.getString(TMDB_RELEASE_DATE);

                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,MOVIE_ID);
                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, MOVIE_TITLE);
                contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,POSTER_PATH);
                contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,BACKDROP_PATH);
                contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, OVERVIEW);
                contentValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, USER_RATING);
                contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, RELEASE_DATE);

                cVVector.add(contentValues);
            }
            int inserted = 0;
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }

    @Override
    protected Void doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {

            final String MOVIES_BASE_URL =
                    "https://api.themoviedb.org/3/discover/movie?";
            final String SORTING_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";
            final String VOTE_PARAM = "vote_count.gte";

            final String minimumVotes = "100";

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(SORTING_PARAM, params[0])
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DATABASE_API_KEY)
                    .appendQueryParameter(VOTE_PARAM,minimumVotes)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
            getMovieDataFromJson(movieJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        }catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }
}

