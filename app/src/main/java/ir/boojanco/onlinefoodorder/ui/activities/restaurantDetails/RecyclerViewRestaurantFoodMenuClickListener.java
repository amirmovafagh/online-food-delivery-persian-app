package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

import android.view.View;

import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments.FoodItem;

public interface RecyclerViewRestaurantFoodMenuClickListener {
    void onRecyclerViewItemClick(int position ,View v, FoodItem items);
}
