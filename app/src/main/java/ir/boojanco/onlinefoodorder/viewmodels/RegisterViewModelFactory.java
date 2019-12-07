package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RegisterViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public RegisterViewModelFactory(Context context) {
        this.context = context;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new RegisterViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}