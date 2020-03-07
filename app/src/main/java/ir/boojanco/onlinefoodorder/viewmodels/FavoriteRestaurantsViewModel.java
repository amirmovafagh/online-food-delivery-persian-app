package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.FavoriteRestaurantsFragmentInterface;

public class FavoriteRestaurantsViewModel extends ViewModel {
    private Context context;
    private UserRepository userRepository;
    private FavoriteRestaurantsFragmentInterface fragmentInterface;

    public void setFragmentInterface(FavoriteRestaurantsFragmentInterface fragmentInterface) {
        this.fragmentInterface = fragmentInterface;
    }

    public FavoriteRestaurantsViewModel(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

}
