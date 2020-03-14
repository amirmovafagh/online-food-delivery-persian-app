package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import android.view.View;

import ir.boojanco.onlinefoodorder.models.user.OrderItem;

public interface OrdersRecyclerViewInterface {
    void onRecyclerViewOrderClick(View v, OrderItem orderItem);
}
