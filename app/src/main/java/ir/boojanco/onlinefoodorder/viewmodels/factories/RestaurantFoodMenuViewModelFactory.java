package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments.RestaurantFoodMenuViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantViewModel;

public class RestaurantFoodMenuViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private RestaurantRepository restaurantRepository;


    public RestaurantFoodMenuViewModelFactory(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository =restaurantRepository;

    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantFoodMenuViewModel.class)) {
            return (T) new RestaurantFoodMenuViewModel(context, restaurantRepository);
        }
        throw new IllegalArgumentException("Unknown "+ RestaurantFoodMenuViewModel.class.getSimpleName()+" class");
    }
}