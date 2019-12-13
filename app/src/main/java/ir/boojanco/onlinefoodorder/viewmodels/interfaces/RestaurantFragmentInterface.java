package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantResponse;

public interface RestaurantFragmentInterface {
    void onStarted();
    void onSuccess(MutableLiveData<LastRestaurantResponse> liveData);
    void onFailure(String error);
}
