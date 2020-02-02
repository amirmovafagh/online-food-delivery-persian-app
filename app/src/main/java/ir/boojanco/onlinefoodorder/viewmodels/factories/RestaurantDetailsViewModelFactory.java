package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantDetailsViewModel;

public class RestaurantDetailsViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private RestaurantRepository restaurantRepository;
    private CartDataSource cartDataSource;

    public RestaurantDetailsViewModelFactory(Context context, RestaurantRepository restaurantRepository, CartDataSource cartDataSource) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
        this.cartDataSource = cartDataSource;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantDetailsViewModel.class)) {
            return (T) new RestaurantDetailsViewModel(context, restaurantRepository,cartDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
