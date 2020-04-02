package ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails;

import android.view.View;

import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments.FoodItem;

public interface RecyclerViewRestaurantFoodMenuClickListener {
    void onRecyclerViewItemClick(View v, FoodItem items);
    void onRecyclerViewFaveToggleClick( FoodItem items, boolean isChecked);
    void onCartItemCountUpdate();
}
