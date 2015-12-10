package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MovieAdapter movieAdapter;
    private GridView gridView;
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private ArrayList<Movie> moviesList = new ArrayList<Movie>();
    private static String sortBy = POPULARITY_DESC;

    public MainActivityFragment() {
    }

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
        String temp;
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
                break;
            case R.id.action_sort_by_rating:
                sortBy = RATING_DESC;
                break;
        }
        updateMovies(sortBy);
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
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("movie", movie);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
