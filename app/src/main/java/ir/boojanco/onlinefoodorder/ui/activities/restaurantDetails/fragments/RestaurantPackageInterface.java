package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments;

import android.view.View;

import ir.boojanco.onlinefoodorder.models.restaurantPackage.RestaurantPackageItem;

public interface RestaurantPackageInterface {
    public void onPackageItemClick(View v, RestaurantPackageItem packageItem);
    public void removeSelectedPackage();
}
