package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Deepak on 3/2/16.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {
    private Context mContext;
    @Bind(R.id.review_author) TextView reviewAuthor;
    @Bind(R.id.review_content) TextView reviewContent;

    public ReviewAdapter(Context context, List<Review> reviews) {

        super(context, 0, reviews);
        mContext = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Gets the Movie object from the ArrayAdapter at the appropriate position
        Review review  = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_review_item, parent, false);
        }

        ButterKnife.bind(this, convertView);
        reviewAuthor.setText(review.getAuthor());
        reviewContent.setText(review.getContent());
        return convertView;
    }
}

