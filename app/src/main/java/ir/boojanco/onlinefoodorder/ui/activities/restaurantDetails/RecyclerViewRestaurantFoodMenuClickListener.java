package ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails;

import android.view.View;

import java.util.ArrayList;

import ir.boojanco.onlinefoodorder.models.food.getAllFood.AllFoodList;

public interface RecyclerViewRestaurantFoodMenuClickListener {
    void onRecyclerViewItemClick(View v, ListItemType itemType);
}
