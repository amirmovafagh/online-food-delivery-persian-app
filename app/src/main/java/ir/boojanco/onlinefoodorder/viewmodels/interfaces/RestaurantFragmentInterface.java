package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import androidx.lifecycle.LiveData;

import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantResponse;

public interface RestaurantFragmentInterface {
    void onStarted();
    void onSuccess(LiveData<LastRestaurantResponse> liveData);
    void onFailure(String error);
}
