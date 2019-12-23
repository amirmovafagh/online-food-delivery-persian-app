package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.viewmodels.LoginViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantFoodViewModel;

public class RestaurantFoodViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private RestaurantRepository restaurantRepository;

    public RestaurantFoodViewModelFactory(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantFoodViewModel.class)) {
            return (T) new RestaurantFoodViewModel(context, restaurantRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
