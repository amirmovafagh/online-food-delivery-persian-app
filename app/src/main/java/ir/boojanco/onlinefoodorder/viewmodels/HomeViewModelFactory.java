package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;


    public HomeViewModelFactory(Context context) {
        this.context = context;

    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(context);
        }
        throw new IllegalArgumentException("Unknown "+HomeViewModelFactory.class.getSimpleName()+" class");
    }
}