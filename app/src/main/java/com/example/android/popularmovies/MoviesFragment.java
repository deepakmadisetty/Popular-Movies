package com.example.android.popularmovies;

import android.database.Cursor;
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

import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private MovieAdapter movieAdapter;
    private GridView gridView;
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private ArrayList<Movie> moviesList = new ArrayList<Movie>();
    private static String sortBy = POPULARITY_DESC;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_USER_RATING,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE
    };

    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_MOVIE_TITLE = 2;
    public static final int COL_POSTER_IMAGE = 3;
    public static final int COL_BACKDROP_IMAGE = 4;
    public static final int COL_OVERVIEW = 5;
    public static final int COL_USER_RATING = 6;
    public static final int COL_RELEASE_DATE = 7;

    @Override
    public void onStart() {
        super.onStart();
        // Tried updating the movies from here and onCreateView no change
        updateMovies(sortBy);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public interface Callback {
        void onItemSelected(Movie movie);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", moviesList);
        outState.putString("sort_key", sortBy);
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
                sortBy = POPULARITY_DESC;
                updateMovies(sortBy);
                break;
            case R.id.action_sort_by_rating:
                sortBy = RATING_DESC;
                updateMovies(sortBy);
                break;
            case R.id.action_sort_by_favourites:
                FavoriteMoviesTask();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMovies(String sortBy) {

        FetchMoviesTask moviesTask = new FetchMoviesTask(movieAdapter);
        moviesTask.execute(sortBy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieAdapter = new MovieAdapter(getActivity(), moviesList);
        // Get a reference to the ListView, and attach this adapter to it.
        gridView = (GridView) rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(movieAdapter);

        if(savedInstanceState != null && savedInstanceState.containsKey("movies") ) {
            moviesList = savedInstanceState.getParcelableArrayList("movies");
            sortBy = savedInstanceState.getString("sort_key");
            movieAdapter.addAll(moviesList);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie movie = movieAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(movie);

            }
        });
        return rootView;
    }

    private void FavoriteMoviesTask()  {


        Log.d("FavouriteTask", "Enter");
            Cursor cursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
            Log.d("FavouriteTask", String.valueOf(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)));
        List<Movie> results = new ArrayList<>();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                Movie movie = new Movie(cursor);
                    results.add(movie);
                    movieAdapter = new MovieAdapter(getActivity(), results);
                    movieAdapter.notifyDataSetChanged();
                    gridView.setAdapter(movieAdapter);
                }
                cursor.close();
            }
    }
}
