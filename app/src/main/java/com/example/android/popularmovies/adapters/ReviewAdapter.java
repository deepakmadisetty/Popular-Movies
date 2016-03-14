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

    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, 0, reviews);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Review review  = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_review_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.reviewAuthor.setText(review.getAuthor());
        viewHolder.reviewContent.setText(review.getContent());
        return convertView;
    }

    public static class ViewHolder {
        @Bind(R.id.review_author) TextView reviewAuthor;
        @Bind(R.id.review_content) TextView reviewContent;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

