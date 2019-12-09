package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;

public class LoginViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private UserRepository userRepository;

    public LoginViewModelFactory(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(context, userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
