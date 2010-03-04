package com.totsp.restaurant;

import android.app.Application;

import com.totsp.restaurant.data.Review;

public class RestaurantFinderApplication extends Application {

    private Review currentReview;
    private String reviewCriteriaCuisine;
    private String reviewCriteriaLocation;

    public RestaurantFinderApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public Review getCurrentReview() {
        return this.currentReview;
    }

    public String getReviewCriteriaCuisine() {
        return this.reviewCriteriaCuisine;
    }

    public String getReviewCriteriaLocation() {
        return this.reviewCriteriaLocation;
    }

    public void setCurrentReview(Review currentReview) {
        this.currentReview = currentReview;
    }

    public void setReviewCriteriaCuisine(String reviewCriteriaCuisine) {
        this.reviewCriteriaCuisine = reviewCriteriaCuisine;
    }

    public void setReviewCriteriaLocation(String reviewCriteriaLocation) {
        this.reviewCriteriaLocation = reviewCriteriaLocation;
    }
}
