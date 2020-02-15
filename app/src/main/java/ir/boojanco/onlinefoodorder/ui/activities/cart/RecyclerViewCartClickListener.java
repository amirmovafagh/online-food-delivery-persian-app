package ir.boojanco.onlinefoodorder.ui.activities.cart;

import android.view.View;

import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;

public interface RecyclerViewCartClickListener {
    void onRecyclerViewItemClick(View v, CartItem cartItem);
    void onRecyclerViewAddressClick(View v, UserAddressList userAddress);
}
