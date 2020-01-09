package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

import android.view.View;

import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments.FoodItem;

public interface RecyclerViewRestaurantFoodTypeClickListener {
    void onRecyclerViewTypeItemClick(View v, String foodTypeName);
}
