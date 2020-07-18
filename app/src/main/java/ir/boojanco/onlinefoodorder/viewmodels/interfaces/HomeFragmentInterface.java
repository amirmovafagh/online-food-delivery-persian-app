package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import java.util.List;

import ir.boojanco.onlinefoodorder.models.food.FoodCategories;
import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;

public interface HomeFragmentInterface {
    void onStarted();

    void onSuccess(List<FoodCategories> categories);

    void showStateCityCustomDialog(List<AllStatesList> statesLists);

    void onFailure(String error);

    void onSuccessGetCities(List<AllCitiesList> allCitiesLists);

    void searchRestaurantOnClick();

    void setCityAndState(String state, String city);

    void tryAgain();

    void searchRestaurantsByScore();

    void searchRestaurantsByNewestDate();

    void searchRestaurantsBySelectedLocationDate(double latitude, double longitude);

    void openMapDialog();
}
