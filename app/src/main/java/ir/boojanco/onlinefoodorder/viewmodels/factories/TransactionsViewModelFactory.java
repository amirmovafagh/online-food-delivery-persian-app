package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.viewmodels.OrdersViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.TransactionsViewModel;

public class TransactionsViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private UserRepository userRepository;


    public TransactionsViewModelFactory(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TransactionsViewModel.class)) {
            return (T) new TransactionsViewModel(context,userRepository);
        }
        throw new IllegalArgumentException("Unknown " + TransactionsViewModelFactory.class.getSimpleName() + " class");
    }
}