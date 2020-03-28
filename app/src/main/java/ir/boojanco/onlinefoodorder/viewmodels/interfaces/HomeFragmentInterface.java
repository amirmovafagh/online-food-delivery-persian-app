package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import java.util.List;

import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;

public interface HomeFragmentInterface {
    void onStarted();
    void onSuccess();
    void showStateCityCustomDialog(List<AllStatesList> statesLists);
    void onFailure(String error);

    void onSuccessGetcities(List<AllCitiesList> allCitiesLists);
}
