package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Deepak on 12/3/15.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    private Context mContext;

    public MovieAdapter(Activity context, List<Movie> movies) {

        super(context, 0, movies);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Gets the Movie object from the ArrayAdapter at the appropriate position
        Movie movie  = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        //ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_image);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_image);

        String image_url = "http://image.tmdb.org/t/p/w185/"+movie.getPosterImage();

        Picasso.with(mContext)
                .load(image_url)
                .into(imageView);
        return convertView;
    }
}