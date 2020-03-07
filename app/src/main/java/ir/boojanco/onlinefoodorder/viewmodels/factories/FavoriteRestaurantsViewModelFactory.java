package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.viewmodels.FavoriteRestaurantsViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.OrdersViewModel;

public class FavoriteRestaurantsViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private UserRepository userRepository;


    public FavoriteRestaurantsViewModelFactory(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FavoriteRestaurantsViewModel.class)) {
            return (T) new FavoriteRestaurantsViewModel(context,userRepository);
        }
        throw new IllegalArgumentException("Unknown " + FavoriteRestaurantsViewModelFactory.class.getSimpleName() + " class");
    }
}