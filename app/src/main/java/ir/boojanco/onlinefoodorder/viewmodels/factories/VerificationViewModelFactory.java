package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.viewmodels.UserProfileViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.VerificationViewModel;

public class VerificationViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private UserRepository userRepository;


    public VerificationViewModelFactory(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(VerificationViewModel.class)) {
            return (T) new VerificationViewModel(context, userRepository);
        }
        throw new IllegalArgumentException("Unknown " + VerificationViewModelFactory.class.getSimpleName() + " class");
    }
}