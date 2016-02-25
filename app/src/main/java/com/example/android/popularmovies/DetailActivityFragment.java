package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private Movie movie;
    @Bind(R.id.da_movie_title) TextView movieTitle;
    @Bind(R.id.da_user_rating) TextView userRating;
    @Bind(R.id.da_release_date) TextView releaseDate;
    @Bind(R.id.da_overview) TextView overview;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();

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

        return rootView;

    }

}
