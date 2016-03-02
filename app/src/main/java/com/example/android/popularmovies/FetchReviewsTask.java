package com.example.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.adapters.ReviewAdapter;
import com.example.android.popularmovies.models.Review;

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

/**
 * Created by Deepak on 3/2/16.
 */
public class FetchReviewsTask extends AsyncTask<String, Void, List<Review>> {

    public ReviewAdapter reviewAdapter;

    public FetchReviewsTask(ReviewAdapter reviewAdapter) {
        this.reviewAdapter = reviewAdapter;
    }

    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

    final String TMDB_REVIEW_RESULTS = "results";

    private List<Review> getReviewDataFromJson(String reviewJsonStr) throws JSONException {

        JSONObject reviewObject = new JSONObject(reviewJsonStr);
        JSONArray reviewArray = reviewObject.getJSONArray(TMDB_REVIEW_RESULTS);

        List<Review> results = new ArrayList<>();
        for (int i = 0; i < reviewArray.length(); i++) {
            JSONObject reviewResult = reviewArray.getJSONObject(i);
            Review reviewModel = new Review(reviewResult);
            results.add(reviewModel);
        }

        return results;
    }

    @Override
    protected List<Review> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String reviewJsonStr = null;

        try {

            final String REVIEWS_BASE_URL =
                    "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";;
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(REVIEWS_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_DATABASE_API_KEY)
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
            reviewJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
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

        try {
            return getReviewDataFromJson(reviewJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        if (reviews != null) {
            reviewAdapter.clear();
            reviewAdapter.addAll(reviews);
        }
    }
}
