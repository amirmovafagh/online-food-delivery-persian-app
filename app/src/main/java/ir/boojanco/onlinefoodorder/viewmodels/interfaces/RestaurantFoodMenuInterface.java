package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import java.util.ArrayList;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.ListItemType;

public interface RestaurantFoodMenuInterface {
    void onStarted();
    void onSuccess(ArrayList<ListItemType> items);
    void onFailure(String error);
}
