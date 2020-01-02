package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;


import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RestaurantFoodInterface;


public class RestaurantDetailsViewModel extends ViewModel {
    private static final String TAG = RestaurantDetailsViewModel.class.getSimpleName();
    public RestaurantFoodInterface foodInterface;
    private Context context;
    private RestaurantRepository restaurantRepository;

    public RestaurantDetailsViewModel(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
    }

}
