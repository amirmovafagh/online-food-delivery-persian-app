package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.viewmodels.ForgotPassViewModel;

public class ForgotPassViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private UserRepository userRepository;


    public ForgotPassViewModelFactory(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ForgotPassViewModel.class)) {
            return (T) new ForgotPassViewModel(context, userRepository);
        }
        throw new IllegalArgumentException("Unknown " + ForgotPassViewModelFactory.class.getSimpleName() + " class");
    }
}