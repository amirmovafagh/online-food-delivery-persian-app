package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.viewmodels.LoginRegisterViewModel;

public class LoginRegisterViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private UserRepository userRepository;

    public LoginRegisterViewModelFactory(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginRegisterViewModel.class)) {
            return (T) new LoginRegisterViewModel(context, userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
