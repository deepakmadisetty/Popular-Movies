package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MovieAdapter movieAdapter;
    private GridView gridView;
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private ArrayList<Movie> moviesList;
    private String sortBy = POPULARITY_DESC;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            moviesList = new ArrayList<Movie>();
        } else {
            moviesList = savedInstanceState.getParcelableArrayList("movies");
        }
        setHasOptionsMenu(true);
        String temp;
        if(savedInstanceState == null)
            temp = "Null";
        else
            temp = "NotNull";
        Log.v("SAVED", temp);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", moviesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_sort_by_popularity:
                updateMovies(POPULARITY_DESC);
                break;
            case R.id.action_sort_by_rating:
                updateMovies(RATING_DESC);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieAdapter = new MovieAdapter(getActivity(), moviesList);

        // Get a reference to the ListView, and attach this adapter to it.
        gridView = (GridView) rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(movieAdapter);

        Log.v("SORT",sortBy);
        updateMovies(sortBy);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie movie = movieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("movie", (Parcelable) movie);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Tried updating the movies from here and onCreateView no change
        // updateMovies(sortBy);
    }

    private void updateMovies(String sortBy) {

        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute(sortBy);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private List<Movie> getMovieDataFromJson(String movieJsonStr) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_RESULTS = "results";
            final String TMDB_MOVIE_ID = "id";
            final String TMDB_MOVIE_TITLE ="original_title";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_BACKDROP_PATH = "backdrop_path";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_USER_RATING = "vote_average";
            final String TMDB_RELEASE_DATE = "release_date";

            int MOVIE_ID;
            String MOVIE_TITLE, POSTER_PATH, BACKDROP_PATH, OVERVIEW, USER_RATING, RELEASE_DATE;

            JSONObject movie = new JSONObject(movieJsonStr);
            JSONArray movieArray = movie.getJSONArray(TMDB_RESULTS);

            List<Movie> results = new ArrayList<>();
            for (int i = 0; i < movieArray.length(); i++) {

                // Get the JSON object representing each Movie
                JSONObject movieResult = movieArray.getJSONObject(i);

                MOVIE_ID =movieResult.getInt(TMDB_MOVIE_ID);
                MOVIE_TITLE = movieResult.getString(TMDB_MOVIE_TITLE);
                POSTER_PATH = movieResult.getString(TMDB_POSTER_PATH);
                BACKDROP_PATH = movieResult.getString(TMDB_BACKDROP_PATH);
                OVERVIEW = movieResult.getString(TMDB_OVERVIEW);
                USER_RATING = movieResult.getString(TMDB_USER_RATING);
                RELEASE_DATE = movieResult.getString(TMDB_RELEASE_DATE);

                Movie movieModel = new Movie(MOVIE_ID, MOVIE_TITLE, POSTER_PATH, BACKDROP_PATH, OVERVIEW, USER_RATING, RELEASE_DATE);

                results.add(movieModel);

            }
            return results;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

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
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORTING_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(SORTING_PARAM, params[0])
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
                movieJsonStr = buffer.toString();
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
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                movieAdapter.clear();
                movieAdapter.addAll(movies);
            }
        }
    }
}
