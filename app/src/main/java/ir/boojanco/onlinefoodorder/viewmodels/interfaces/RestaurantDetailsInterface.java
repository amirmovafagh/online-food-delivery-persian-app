package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import androidx.lifecycle.MutableLiveData;

import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;

public interface RestaurantDetailsInterface {
    void onStarted();
    void onSuccess(MutableLiveData<RestaurantInfoResponse> liveData);
    void onFailure(String error);
}
