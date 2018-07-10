package com.example.android.popularmoviesparttwo.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesparttwo.R;
import com.example.android.popularmoviesparttwo.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 7/7/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    private List<Review> reviews;

    public ReviewsAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int inflateLayoutId = R.layout.recyclerview_reviews;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParent = false;
        View view = inflater.inflate(inflateLayoutId, parent, shouldAttachToParent);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ReviewsViewHolder holder, int position) {
        ReviewsViewHolder reviewsViewHolder = holder;
        Review review = reviews.get(position);
        System.out.println("USER: " + review.getUser() + "\nREVIEW: " + review.getReview() + "\n");

        SpannableString userBold = new SpannableString(review.getUser());
        userBold.setSpan(new StyleSpan(Typeface.BOLD), 0, userBold.length(), 0);
        reviewsViewHolder.user.setText(userBold);
        reviewsViewHolder.review.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        if (reviews != null) {
            return reviews.size();
        }
        return 0;
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        protected TextView user;
        protected TextView review;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            System.out.println("SETTING VIEW STUFF");
            user = itemView.findViewById(R.id.recycler_review_user);
            review = itemView.findViewById(R.id.recycler_review_comments);
        }
    }
}
