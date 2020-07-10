package ir.boojanco.onlinefoodorder.viewmodels.factories;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.viewmodels.MainViewModel;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private RestaurantRepository restaurantRepository;

    public MainViewModelFactory( RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel( restaurantRepository);
        }
        throw new IllegalArgumentException("Unknown " + MainViewModelFactory.class.getSimpleName() + " class");
    }
}