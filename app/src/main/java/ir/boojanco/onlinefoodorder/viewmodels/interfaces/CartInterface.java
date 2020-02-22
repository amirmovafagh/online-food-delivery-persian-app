package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ir.boojanco.onlinefoodorder.data.database.CartItem;
import ir.boojanco.onlinefoodorder.models.map.ReverseFindAddressResponse;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;
import ir.boojanco.onlinefoodorder.models.user.UserAddressList;
import ir.boojanco.onlinefoodorder.ui.activities.cart.FinalPaymentPrice;

public interface CartInterface {
    void onStarted();

    void onSuccess(List<CartItem> cartItems);

    void onSuccessGetAddress(List<UserAddressList> addressLists);

    void onSuccessGetStates(List<AllStatesList> allStatesLists);

    void onSuccessGetcities(List<AllCitiesList> allStatesLists);

    void showAddressBottomSheet();

    void showStateCityCustomDialog();

    void showMapDialogFragment();

    void acceptOrder(ArrayList<FinalPaymentPrice> finalPaymentPrices, List<CartItem> cartItems, MutableLiveData<String> totalAllPriceLiveData,
                     MutableLiveData<Long> totalRawPriceLiveData,MutableLiveData<String> totalDiscountLiveData,
                     MutableLiveData<Integer> packingCostLiveData,MutableLiveData<String> restaurantShippingCostLiveData,
                     MutableLiveData<Integer> taxAndServiceLivedata);

    void onSuccessGetReverseAddress(ReverseFindAddressResponse reverseFindAddressResponses);

    void onFailure(String Error);
}
