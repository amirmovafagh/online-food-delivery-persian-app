package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import android.view.View;

import java.util.List;

import ir.boojanco.onlinefoodorder.models.user.OrderFoodList;

public interface OrdersRecyclerViewInterface {
    void onRecyclerViewOrderClick(View v, List<OrderFoodList> foodLists);
}
