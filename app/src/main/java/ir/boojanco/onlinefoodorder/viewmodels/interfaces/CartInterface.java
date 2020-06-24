package ir.boojanco.onlinefoodorder.viewmodels.interfaces;


import java.util.ArrayList;
import java.util.List;

import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.data.database.RestaurantItem;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.ui.fragments.cart.FinalPaymentPrice;
import ir.boojanco.onlinefoodorder.util.OrderType;

public interface CartInterface {
    void onStarted();

    void onSuccessCartItems(List<CartItem> cartItems);
    void onSuccessRestaurantsCarts(List<RestaurantItem> restaurantItems);

    void onSuccessGetAddress();

    void onSuccessGetStates(List<AllStatesList> allStatesLists);

    void onSuccessGetCities(List<AllCitiesList> allCitiesLists);

    void showAddressBottomSheet();

    void showStateCityCustomDialog();

    void showMapDialogFragment();

    void acceptOrder(ArrayList<FinalPaymentPrice> finalPaymentPrices, List<CartItem> cartItems, int totalAllPrice,
                     int totalRawPrice, int totalDiscount, int packingCost,
                     int taxAndService, int shippingCost, OrderType orderType, long restaurantId, long restaurantPackageId, long shippingAddressId);

    void onSuccessGetReverseAddress(ReverseFindAddressResponse reverseFindAddressResponses);

    void onFailure(String Error);

    void hideAddressBottomSheet(String msg);

    void resetAddressRecyclerView();
}
