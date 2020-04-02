package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.ui.fragments.restaurantDetails.fragments.RestaurantInfoViewModel;

public class RestaurantInfoFragmentViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private RestaurantRepository restaurantRepository;

    public RestaurantInfoFragmentViewModelFactory(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantInfoViewModel.class)) {
            return (T) new RestaurantInfoViewModel(context, restaurantRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
