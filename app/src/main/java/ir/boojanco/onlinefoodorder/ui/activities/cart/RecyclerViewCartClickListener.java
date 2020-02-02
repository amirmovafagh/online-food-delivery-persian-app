package ir.boojanco.onlinefoodorder.ui.activities.cart;

import android.view.View;

import ir.boojanco.onlinefoodorder.data.database.CartItem;

public interface RecyclerViewCartClickListener {
    void onRecyclerViewItemClick(View v, CartItem cartItem);
}
