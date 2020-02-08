package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import java.util.List;

import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;

public interface CartInterface {
    void onStarted();
    void onSuccess(List<CartItem> cartItems);
    void onSuccessGetAddress(List<UserAddressList> addressLists);
    void onSuccessGetReverseAddress(ReverseFindAddressResponse reverseFindAddressResponses);
    void onFailure(String Error);
}
