package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import android.view.View;

import ir.boojanco.onlinefoodorder.models.restaurant.FavoriteRestaurants;

public interface FavoriteRestaurantsRecyclerViewInterface {
    void onRecyclerViewFaveRestaurantClick(View v, FavoriteRestaurants favoriteRestaurant);
}
