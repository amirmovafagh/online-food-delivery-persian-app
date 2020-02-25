package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.viewmodels.RestaurantViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.UserProfileViewModel;

public class UserProfileViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private RestaurantRepository restaurantRepository;
    private UserRepository userRepository;


    public UserProfileViewModelFactory(Context context, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserProfileViewModel.class)) {
            return (T) new UserProfileViewModel(context, restaurantRepository,userRepository);
        }
        throw new IllegalArgumentException("Unknown " + UserProfileViewModelFactory.class.getSimpleName() + " class");
    }
}