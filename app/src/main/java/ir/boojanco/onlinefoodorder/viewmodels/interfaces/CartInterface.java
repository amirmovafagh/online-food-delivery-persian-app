package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import java.util.List;

import ir.boojanco.onlinefoodorder.data.database.CartItem;

public interface CartInterface {
    void onStarted();
    void onSuccess(List<CartItem> cartItems);
    void onFailure(String Error);
}
