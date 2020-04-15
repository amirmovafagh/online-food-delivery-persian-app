package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments;

import android.view.View;

import ir.boojanco.onlinefoodorder.models.restaurantPackage.RestaurantPackageItem;

public interface RestaurantPackageInterface {
    void onPackageItemClick(View v, RestaurantPackageItem packageItem);
    void onPackageMessage(String msg);
}
