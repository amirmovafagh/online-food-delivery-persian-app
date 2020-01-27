package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;

import ir.boojanco.onlinefoodorder.models.restaurant.RestaurantInfoResponse;

public interface RestaurantDetailsInterface {
    void onStarted();
    void onSuccess(RestaurantInfoResponse restaurantInfo );
    void onFailure(String error);
}
