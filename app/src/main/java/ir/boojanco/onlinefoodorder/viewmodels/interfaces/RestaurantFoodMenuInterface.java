package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ir.boojanco.onlinefoodorder.models.food.getAllFood.GetAllFoodResponse;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.ListItemType;

public interface RestaurantFoodMenuInterface {
    void onStarted();
    void onSuccess(ArrayList<ListItemType> items, MutableLiveData<GetAllFoodResponse> allFoodMutableLiveData, ArrayList<String> foodTypeIndex);
    void onFailure(String error);
}
