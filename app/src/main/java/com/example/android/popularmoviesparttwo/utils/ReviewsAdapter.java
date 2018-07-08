package com.example.android.popularmoviesparttwo.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class ReviewsAdapter extends RecyclerView.Adapter {
    private List<Review> reviews;

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int inflateLayoutId = R.layout.recyclerview_reviews;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParent = false;
        View view = inflater.inflate(inflateLayoutId, parent, shouldAttachToParent);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReviewsViewHolder reviewsViewHolder = (ReviewsViewHolder) holder;
        Review review = reviews.get(position);
        System.out.println("USER: " + review.getUser() + "\nREVIEW: " + review.getReview() + "\n");
        reviewsViewHolder.user.setText(review.getUser());
        reviewsViewHolder.review.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
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

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public void clearReviews(){
        this.reviews=new ArrayList<>();
        notifyDataSetChanged();
    }
}
