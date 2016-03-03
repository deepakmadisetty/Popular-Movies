package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.ReviewAdapter;
import com.example.android.popularmovies.adapters.TrailerAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private Movie movie;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private ArrayList<Trailer> trailerList = new ArrayList<Trailer>();
    private ArrayList<Review> reviewList = new ArrayList<Review>();

    @Bind(R.id.da_movie_title) TextView movieTitle;
    @Bind(R.id.da_user_rating) TextView userRating;
    @Bind(R.id.da_release_date) TextView releaseDate;
    @Bind(R.id.da_overview) TextView overview;
    @Bind(R.id.star) CheckBox checkBox;
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
        userRating.setText("User Rating: " + movie.getUserRating());
        releaseDate.setText("Release Date: " + movie.getReleaseDate());
        overview.setText(movie.getOverview());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isFavourite = Utility.isFavorite(getActivity(), movie.getMovieId());

                if (isFavourite == 1) {
                    getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{Integer.toString(movie.getMovieId())}
                    );
                    Toast toast = Toast.makeText(getActivity(), "Removed from Favourites", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getMovieId());
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getMovieTitle());
                    values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterImage());
                    values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropImage());
                    values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                    values.put(MovieContract.MovieEntry.COLUMN_USER_RATING, movie.getUserRating());
                    values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

                    getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
                    Toast toast = Toast.makeText(getActivity(), "Added to Favourites", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        trailerAdapter = new TrailerAdapter(getActivity(), trailerList);

        LinearListView trailerListView = (LinearListView) rootView.findViewById(R.id.trailer_list_view);
        trailerListView.setAdapter(trailerAdapter);

        trailerListView.setOnItemClickListener(new LinearListView.OnItemClickListener(){

            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                Trailer trailer = trailerAdapter.getItem(position);
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                trailerIntent.setData(Uri.parse("http://www.youtube.com/watch?v="+trailer.getKey()));
                startActivity(trailerIntent);
            }
        });

        reviewAdapter = new ReviewAdapter(getActivity(), reviewList);

        LinearListView reviewListView = (LinearListView) rootView.findViewById(R.id.review_list_view);
        reviewListView.setAdapter(reviewAdapter);

        return rootView;

    }

    private void updateTrailers() {

        FetchTrailersTask trailersTask = new FetchTrailersTask(trailerAdapter);
        trailersTask.execute(Integer.toString(movie.getMovieId()));
        FetchReviewsTask reviewsTask = new FetchReviewsTask(reviewAdapter);
        reviewsTask.execute(Integer.toString(movie.getMovieId()));
    }

}
