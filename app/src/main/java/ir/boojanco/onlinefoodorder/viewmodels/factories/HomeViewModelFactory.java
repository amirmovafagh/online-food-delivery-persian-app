package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.viewmodels.HomeViewModel;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private RestaurantRepository restaurantRepository;

    public HomeViewModelFactory(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(context, restaurantRepository);
        }
        throw new IllegalArgumentException("Unknown " + HomeViewModelFactory.class.getSimpleName() + " class");
    }
}