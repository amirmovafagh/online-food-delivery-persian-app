package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ir.boojanco.onlinefoodorder.models.food.GetAllFoodResponse;
import ir.boojanco.onlinefoodorder.models.restaurantPackage.AllPackagesResponse;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.ListItemType;

public interface RestaurantFoodMenuInterface {
    void onStarted();

    void onSuccess(ArrayList<ListItemType> items, MutableLiveData<GetAllFoodResponse> allFoodMutableLiveData, ArrayList<String> foodTypeIndex);

    void onSuccessRestaurantPackages(MutableLiveData<AllPackagesResponse> allPackagesMutableLiveData);

    void onFailure(String error);

    void goToCartFragment();
}
