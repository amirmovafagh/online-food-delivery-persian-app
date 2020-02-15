package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.viewmodels.CartViewModel;

public class CartViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private CartDataSource cartDataSource;
    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;

    public CartViewModelFactory(Context context, CartDataSource cartDataSource, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.cartDataSource = cartDataSource;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CartViewModel.class)) {
            return (T) new CartViewModel(context, cartDataSource, userRepository, restaurantRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
