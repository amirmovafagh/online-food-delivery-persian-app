package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;

public interface StateCityDialogInterface {
    void onStateItemClick(AllStatesList state);
    void onCityItemClick(AllCitiesList city);

}
