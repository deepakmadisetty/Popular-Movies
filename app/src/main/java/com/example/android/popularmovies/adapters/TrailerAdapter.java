package com.example.android.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Deepak on 3/1/16.
 */

public class TrailerAdapter extends ArrayAdapter<Trailer> {

    private Context mContext;

    public TrailerAdapter(Context context, List<Trailer> trailers) {

        super(context, 0, trailers);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Gets the Movie object from the ArrayAdapter at the appropriate position
        Trailer trailer  = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_trailer_item, parent, false);
        }

        //ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_image);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.trailer_image);

        String image_url = "http://img.youtube.com/vi/"+trailer.getKey()+ "/0.jpg";

        Picasso.with(mContext)
                .load(image_url)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.error_poster_placeholder)
                .into(imageView);
        return convertView;
    }
}
