package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import java.util.List;

import ir.boojanco.onlinefoodorder.models.stateCity.AllCitiesList;
import ir.boojanco.onlinefoodorder.models.stateCity.AllStatesList;

public interface RestaurantFragmentInterface {
    void onStarted();

    void onSuccess();

    void onFailure(String error);

    void expandSortView();

    void expandFilterView();

    void openBottomSheet();

    void closeBottomSheet();

    void tryAgain();

    void updateRestaurantsRecyclerView(Object categoryList, Object city, Object restaurantName, Object deliveryFilter,
                                       Object discountFilter, Object servingFilter, Object getInPlaceFilter,
                                       Object latitude, Object longitude, Object sortBy);

    void showStateCityCustomDialog(List<AllStatesList> statesLists);

    void onSuccessGetCities(List<AllCitiesList> citiesLists);
}
