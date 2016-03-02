package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.adapters.TrailerAdapter;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Trailer;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private Movie movie;
    private Trailer trailer;
    private TrailerAdapter trailerAdapter;
    private ArrayList<Trailer> trailerList = new ArrayList<Trailer>();

    @Bind(R.id.da_movie_title) TextView movieTitle;
    @Bind(R.id.da_user_rating) TextView userRating;
    @Bind(R.id.da_release_date) TextView releaseDate;
    @Bind(R.id.da_overview) TextView overview;

    public DetailActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        // Tried updating the movies from here and onCreateView no change
        updateTrailers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);
        final Intent intent = getActivity().getIntent();

        movie = intent.getParcelableExtra("movie");

        getActivity().setTitle(movie.getMovieTitle());

        ImageView posterImage = (ImageView) rootView.findViewById(R.id.da_poster_image);
        ImageView backdropImage = (ImageView) rootView.findViewById(R.id.da_backdrop_image);

        String backdropImageURL = "http://image.tmdb.org/t/p/w500/"+movie.getBackdropImage();
        String posterImageURL = "http://image.tmdb.org/t/p/w185/"+movie.getPosterImage();

        Picasso.with(getContext())
                .load(backdropImageURL)
                .placeholder(R.drawable.backdrop_placholder)
                .error(R.drawable.error_backdrop_placeholder)
                .into(backdropImage);
        Picasso.with(getContext())
                .load(posterImageURL)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.error_poster_placeholder)
                .into(posterImage);

        ButterKnife.bind(this, rootView);
        movieTitle.setText(movie.getMovieTitle());
        userRating.setText("User Rating: "+ movie.getUserRating());
        releaseDate.setText("Release Date: " + movie.getReleaseDate());
        overview.setText(movie.getOverview());

        trailerAdapter = new TrailerAdapter(getActivity(), trailerList);

        LinearListView listView = (LinearListView) rootView.findViewById(R.id.trailer_list_view);
        listView.setAdapter(trailerAdapter);

        listView.setOnItemClickListener(new LinearListView.OnItemClickListener(){

            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                Trailer trailer = trailerAdapter.getItem(position);
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                trailerIntent.setData(Uri.parse("http://www.youtube.com/watch?v="+trailer.getKey()));
                startActivity(trailerIntent);
            }
        });

        return rootView;

    }

    private void updateTrailers() {

        FetchTrailersTask trailersTask = new FetchTrailersTask(trailerAdapter);
        trailersTask.execute(Integer.toString(movie.getMovieId()));
    }


}
