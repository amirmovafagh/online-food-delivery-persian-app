package ir.boojanco.onlinefoodorder.ui.activities.restaurantFood;

import android.view.View;

import ir.boojanco.onlinefoodorder.models.food.getAllFood.AllFoodList;

public interface RecyclerViewRestaurantFoodClickListener {
    void onRecyclerViewItemClick(View v, AllFoodList allFoodList);
}
