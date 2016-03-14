package com.example.android.popularmovies.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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

        Trailer trailer  = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_trailer_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        if(trailer == null)
            viewHolder.trailerTextView.setText("No Trailers to Show");

        String image_url = "http://img.youtube.com/vi/"+trailer.getKey()+ "/0.jpg";

        Picasso.with(mContext)
                .load(image_url)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.error_poster_placeholder)
                .into(viewHolder.trailerImageView);

        viewHolder.trailerTextView.setText(trailer.getName());
        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.trailer_image) ImageView trailerImageView;
        @Bind(R.id.trailer_text) TextView trailerTextView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
