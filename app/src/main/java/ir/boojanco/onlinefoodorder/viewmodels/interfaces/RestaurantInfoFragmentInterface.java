package ir.boojanco.onlinefoodorder.viewmodels.interfaces;


import java.util.List;

import ir.boojanco.onlinefoodorder.models.restaurant.MenuType;

public interface RestaurantInfoFragmentInterface {
    void onStarted();
    void onSuccess(List<MenuType> menuType);
    void onFailure(String error);

}
