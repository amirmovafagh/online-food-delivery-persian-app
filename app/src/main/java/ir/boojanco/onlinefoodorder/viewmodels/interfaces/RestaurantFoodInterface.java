package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import androidx.lifecycle.MutableLiveData;

import ir.boojanco.onlinefoodorder.models.food.getAllFood.GetAllFoodResponse;

public interface RestaurantFoodInterface {
    void onStarted();
    void onSuccess(MutableLiveData<GetAllFoodResponse> liveData);
    void onFailure(String error);
}
