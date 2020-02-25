package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;

public class UserProfileViewModel extends ViewModel {
    private Context context;
    private RestaurantRepository restaurantRepository;
    private UserRepository userRepository;

    public UserProfileViewModel(Context context, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }
}
