package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;

public class RestaurantViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private RestaurantRepository restaurantRepository;


    public RestaurantViewModelFactory(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository =restaurantRepository;

    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantViewModel.class)) {
            return (T) new RestaurantViewModel(context, restaurantRepository);
        }
        throw new IllegalArgumentException("Unknown "+ RestaurantViewModelFactory.class.getSimpleName()+" class");
    }
}