package com.example.android.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Deepak on 12/3/15.
 */
public class MovieAdapter extends CursorAdapter {

    private Context mContext;

    public MovieAdapter(Context context, Cursor c, int flags) {

        super(context, c, 0);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView)view.findViewById(R.id.movie_image);
        String image_url = "http://image.tmdb.org/t/p/w185/"+cursor.getColumnIndexOrThrow("poster_path");
        System.out.println(image_url);
        Picasso.with(mContext)
                .load(image_url)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.error_poster_placeholder)
                .into(imageView);
    }
}