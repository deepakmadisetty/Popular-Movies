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

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private Movie movie;

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

        Picasso.with(getContext()).load(backdropImageURL).into(backdropImage);
        Picasso.with(getContext()).load(posterImageURL).into(posterImage);

        TextView movieTitle = (TextView) rootView.findViewById(R.id.da_movie_title);
        TextView userRating = (TextView) rootView.findViewById(R.id.da_user_rating);
        TextView releaseDate = (TextView) rootView.findViewById(R.id.da_release_date);
        TextView overview = (TextView) rootView.findViewById(R.id.da_overview);

        movieTitle.setText(movie.getMovieTitle());
        userRating.setText("User Rating: "+ movie.getUserRating());
        releaseDate.setText("Release Date: " + movie.getReleaseDate());
        overview.setText(movie.getOverview());

        return rootView;

    }

}
