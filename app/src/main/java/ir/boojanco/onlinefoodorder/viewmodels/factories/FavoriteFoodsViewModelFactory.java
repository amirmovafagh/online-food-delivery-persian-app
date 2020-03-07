package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.viewmodels.FavoriteFoodsViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.FavoriteRestaurantsViewModel;

public class FavoriteFoodsViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private UserRepository userRepository;


    public FavoriteFoodsViewModelFactory(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FavoriteFoodsViewModel.class)) {
            return (T) new FavoriteFoodsViewModel(context,userRepository);
        }
        throw new IllegalArgumentException("Unknown " + FavoriteFoodsViewModelFactory.class.getSimpleName() + " class");
    }
}