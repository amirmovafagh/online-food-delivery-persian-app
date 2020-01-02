package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

import android.view.View;

import ir.boojanco.onlinefoodorder.models.food.getAllFood.AllFoodList;

public interface RecyclerViewRestaurantFoodMenuClickListener {
    void onRecyclerViewItemClick(View v, AllFoodList allFoodList);
}
