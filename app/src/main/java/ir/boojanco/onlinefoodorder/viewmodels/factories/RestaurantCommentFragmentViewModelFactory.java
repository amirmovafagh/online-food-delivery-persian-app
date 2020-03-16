package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments.RestaurantCommentViewModel;
import ir.boojanco.onlinefoodorder.ui.activities.restaurantDetails.fragments.RestaurantInfoViewModel;

public class RestaurantCommentFragmentViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private RestaurantRepository restaurantRepository;

    public RestaurantCommentFragmentViewModelFactory(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestaurantCommentViewModel.class)) {
            return (T) new RestaurantCommentViewModel(context, restaurantRepository);
        }
        throw new IllegalArgumentException(RestaurantCommentFragmentViewModelFactory.class.getSimpleName()+" Unknown ViewModel class");
    }
}
