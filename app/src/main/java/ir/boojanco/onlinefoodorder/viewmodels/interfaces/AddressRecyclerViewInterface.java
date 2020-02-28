package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import android.view.View;

import ir.boojanco.onlinefoodorder.models.user.UserAddressList;

public interface AddressRecyclerViewInterface {
    void onRecyclerViewAddressClick(View v, UserAddressList userAddress);
}
