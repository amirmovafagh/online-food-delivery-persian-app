package ir.boojanco.onlinefoodorder.ui.fragments.cart;

import android.view.View;

import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.data.database.RestaurantItem;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;

public interface RecyclerViewCartClickListener {
    void onRecyclerViewItemClick(View v, CartItem cartItem);
    void onRecyclerViewRestaurantCartItemClick(View v, RestaurantItem restaurantItem);
}
