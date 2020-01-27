package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;

public interface RestaurantInfoFragmentInterface {
    void onStarted();
    void onSuccess( );
    void onFailure(String error);
}
