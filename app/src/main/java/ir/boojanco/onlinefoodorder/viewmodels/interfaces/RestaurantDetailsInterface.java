package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;

public interface RestaurantDetailsInterface {
    void onStarted();
    void initRatingViews( int colors[], int raters[]);
    void onSuccess(RestaurantInfoResponse restaurantInfo );
    void onFailure(String error);

    void setUserClubPoint(int points);
}
