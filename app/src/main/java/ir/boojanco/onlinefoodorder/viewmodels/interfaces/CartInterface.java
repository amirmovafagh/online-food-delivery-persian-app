package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import java.util.List;

import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;

public interface CartInterface {
    void onStarted();
    void onSuccess(List<CartItem> cartItems);
    void onSuccessGetAddress(List<UserAddressList> addressLists);
    void onFailure(String Error);
}
