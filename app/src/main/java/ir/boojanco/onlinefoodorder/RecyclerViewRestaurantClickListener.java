package ir.boojanco.onlinefoodorder;

import android.view.View;

import ir.boojanco.onlinefoodorder.models.restaurant.LastRestaurantList;

public interface RecyclerViewRestaurantClickListener {
    void onRecyclerViewItemClick(View v, LastRestaurantList restaurantList);
}
