package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import java.util.List;

import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;

public interface UserProfileInterface {
    void onStarted();
    void showAddressBottomSheet();
    void showMapDialogFragment();
    void showStateCityCustomDialog();
    void onSuccessGetStates(List<AllStatesList> statesLists);
    void onSuccessGetcities(List<AllCitiesList> citiesLists);
    void onSuccessGetAddress();
    void onFailure(String Error);
}
