package ir.boojanco.onlinefoodorder.viewmodels.interfaces;


import java.util.ArrayList;
import java.util.List;

import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.ui.activities.cart.FinalPaymentPrice;
import ir.boojanco.onlinefoodorder.util.OrderType;

public interface CartInterface {
    void onStarted();

    void onSuccess(List<CartItem> cartItems);

    void onSuccessGetAddress();

    void onSuccessGetStates(List<AllStatesList> allStatesLists);

    void onSuccessGetcities(List<AllCitiesList> allStatesLists);

    void showAddressBottomSheet();

    void showStateCityCustomDialog();

    void showMapDialogFragment();

    void acceptOrder(ArrayList<FinalPaymentPrice> finalPaymentPrices, List<CartItem> cartItems, int totalAllPrice,
                     int totalRawPrice, int totalDiscount, int packingCost,
                     int taxAndService, int shippingCost, OrderType orderType, long restaurantId, long restaurantPackageId, long shippingAddressId);

    void onSuccessGetReverseAddress(ReverseFindAddressResponse reverseFindAddressResponses);

    void onFailure(String Error);
}
