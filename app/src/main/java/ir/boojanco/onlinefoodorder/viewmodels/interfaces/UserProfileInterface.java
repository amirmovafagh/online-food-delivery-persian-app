package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import java.util.List;

import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;

public interface UserProfileInterface {
    void onStarted();
    void updateAddressRecyclerView(int position);
    void showAddressBottomSheet();
    void showMapDialogFragment();
    void showStateCityCustomDialog();
    void onSuccessGetUserProfileInfo();
    void onSuccessGetStates(List<AllStatesList> statesLists);
    void onSuccessGetcities(List<AllCitiesList> citiesLists);
    void onSuccessGetAddress();
    void onLogoutUser();
    void onEditUserProfile();
    void onFailure(String Error);
}
