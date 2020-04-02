package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments.RestaurantFoodMenuViewModel;

public class RestaurantFoodMenuViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private RestaurantRepository restaurantRepository;
    private CartDataSource cartDataSource;


    public RestaurantFoodMenuViewModelFactory(Context context, RestaurantRepository restaurantRepository, CartDataSource cartDataSource) {
        this.context = context;
        this.restaurantRepository =restaurantRepository;
        this.cartDataSource = cartDataSource;

    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantFoodMenuViewModel.class)) {
            return (T) new RestaurantFoodMenuViewModel(context, restaurantRepository, cartDataSource);
        }
        throw new IllegalArgumentException("Unknown "+ RestaurantFoodMenuViewModelFactory.class.getSimpleName()+" class");
    }
}