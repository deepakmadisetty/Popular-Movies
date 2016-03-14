package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
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
public class DetailFragment extends Fragment implements AsyncResponse{

    public static final String MOVIE_DETAIL ="MOVIE_DETAIL";
    public static final String TRAILER_URI = "http://www.youtube.com/watch?v=";
    public static final String TAG = DetailFragment.class.getSimpleName();

    private int isFavourite;
    private Movie movie;
    private Trailer trailer;
    private String TRAILER_KEY;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private ArrayList<Trailer> trailerList = new ArrayList<Trailer>();
    private ArrayList<Review> reviewList = new ArrayList<Review>();
    public ShareActionProvider mShareActionProvider;

    @Bind(R.id.da_movie_title) TextView movieTitle;
    @Bind(R.id.da_user_rating) TextView userRating;
    @Bind(R.id.da_release_date) TextView releaseDate;
    @Bind(R.id.da_overview) TextView overview;
    @Bind(R.id.favorite) CheckBox checkBox;
    @Bind(R.id.detail_layout) ScrollView detailLayout;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (movie != null) {
            super.onCreateOptionsMenu(menu, inflater);
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.menu_detail, menu);

            MenuItem menuShareItem = menu.findItem(R.id.action_share);

            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuShareItem);
<<<<<<< HEAD
//            if (mShareActionProvider!= null) {
//                mShareActionProvider.setShareIntent(createShareMovieIntent());
//            }
=======
>>>>>>> Popular-Movies-Branch
        }
    }

    public void createShareMovieIntent(Trailer trailer) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, movie.getMovieTitle() + " " +
                TRAILER_URI + trailer.getKey());

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            movie = arguments.getParcelable(DetailFragment.MOVIE_DETAIL);
        }
        View rootView =  inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView posterImage = (ImageView) rootView.findViewById(R.id.da_poster_image);
        ImageView backdropImage = (ImageView) rootView.findViewById(R.id.da_backdrop_image);

        if(movie != null) {
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


            isFavourite = Utility.isFavorite(getActivity(), movie.getMovieId());
            if(isFavourite == 1) {
                checkBox.setChecked(true);
                checkBox.setText("Remove from Favourites");
            }

            detailLayout = (ScrollView) rootView.findViewById(R.id.detail_layout);

            if (movie != null) {
                detailLayout.setVisibility(View.VISIBLE);
            } else {
                detailLayout.setVisibility(View.INVISIBLE);
            }
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (movie != null) {
                        int isFavourite = Utility.isFavorite(getActivity(), movie.getMovieId());

                        if (isFavourite == 1) {
                            getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                                    new String[]{Integer.toString(movie.getMovieId())}
                            );
                            Toast toast = Toast.makeText(getActivity(), "Removed from Favourites", Toast.LENGTH_SHORT);
                            toast.show();
                            checkBox.setText("Mark as Favourite");
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
                            checkBox.setText("Remove from Favourites");
                        }
                    }
                }
            });

            trailerAdapter = new TrailerAdapter(getActivity(), trailerList);
            LinearListView trailerListView = (LinearListView) rootView.findViewById(R.id.trailer_list_view);
            trailerListView.setAdapter(trailerAdapter);
            updateTrailers();

            trailerListView.setOnItemClickListener(new LinearListView.OnItemClickListener() {

                @Override
                public void onItemClick(LinearListView parent, View view, int position, long id) {
                    trailer = trailerAdapter.getItem(position);
                    TRAILER_KEY = trailer.getKey();
                    Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
<<<<<<< HEAD
                    trailerIntent.setData(Uri.parse( TRAILER_URI + TRAILER_KEY));
=======
                    trailerIntent.setData(Uri.parse(TRAILER_URI + trailer.getKey()));
>>>>>>> Popular-Movies-Branch
                    startActivity(trailerIntent);
                }
            });

            reviewAdapter = new ReviewAdapter(getActivity(), reviewList);
            LinearListView reviewListView = (LinearListView) rootView.findViewById(R.id.review_list_view);
            reviewListView.setAdapter(reviewAdapter);
            updateReviews();

        }
        return rootView;
    }

    private void updateTrailers() {
<<<<<<< HEAD
        FetchTrailersTask trailersTask = new FetchTrailersTask(trailerAdapter,this);
=======
        FetchTrailersTask trailersTask = new FetchTrailersTask(trailerAdapter, this);
>>>>>>> Popular-Movies-Branch
        trailersTask.execute(Integer.toString(movie.getMovieId()));
    }

    private void updateReviews() {
        FetchReviewsTask reviewsTask = new FetchReviewsTask(reviewAdapter);
        reviewsTask.execute(Integer.toString(movie.getMovieId()));
    }

    @Override
    public void processFinish(Trailer trailer) {
        TRAILER_KEY = trailer.getKey();
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }
    }
}
