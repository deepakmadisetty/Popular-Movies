package com.example.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ShareActionProvider;

import com.example.android.popularmovies.adapters.TrailerAdapter;
import com.example.android.popularmovies.models.Trailer;

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
public class FetchTrailersTask extends AsyncTask<String, Void, List<Trailer>> {

    private TrailerAdapter trailerAdapter;
<<<<<<< HEAD
    private DetailFragment detailFragment;
    ShareActionProvider mShareActionProvider;

    public FetchTrailersTask(TrailerAdapter trailerAdapter, DetailFragment activity) {
        this.trailerAdapter = trailerAdapter;
        detailFragment = activity;
=======
    public Trailer trailer;

    public AsyncResponse delegate;

    public FetchTrailersTask(TrailerAdapter trailerAdapter, AsyncResponse delegate) {
        this.trailerAdapter = trailerAdapter;
        this.delegate = delegate;
>>>>>>> Popular-Movies-Branch
    }

    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

    final String TMDB_TRAILER_RESULTS = "results";

    private List<Trailer> getTrailerDataFromJson(String trailerJsonStr) throws JSONException {

        JSONObject trailerObject = new JSONObject(trailerJsonStr);
        JSONArray trailerArray = trailerObject.getJSONArray(TMDB_TRAILER_RESULTS);

        List<Trailer> results = new ArrayList<>();
        for (int i = 0; i < trailerArray.length(); i++) {
            JSONObject trailerResult = trailerArray.getJSONObject(i);
            Trailer trailerModel = new Trailer(trailerResult);
            results.add(trailerModel);
        }

        return results;
    }

    @Override
    protected List<Trailer> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String trailerJsonStr = null;

        try {

            final String TRAILERS_BASE_URL =
                    "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";;
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(TRAILERS_BASE_URL).buildUpon()
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
            trailerJsonStr = buffer.toString();
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
            return getTrailerDataFromJson(trailerJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        if (trailers != null && trailers.size() != 0) {
            trailerAdapter.clear();
            trailerAdapter.addAll(trailers);
<<<<<<< HEAD
            detailFragment.createShareMovieIntent(trailers.get(0));
=======
            trailer = trailers.get(0);
            delegate.processFinish(trailer);
>>>>>>> Popular-Movies-Branch
        }
    }
}
