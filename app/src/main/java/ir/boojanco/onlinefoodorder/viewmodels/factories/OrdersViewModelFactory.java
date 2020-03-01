package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.viewmodels.OrdersViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.UserProfileViewModel;

public class OrdersViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;
    private UserRepository userRepository;


    public OrdersViewModelFactory(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OrdersViewModel.class)) {
            return (T) new OrdersViewModel(context,userRepository);
        }
        throw new IllegalArgumentException("Unknown " + OrdersViewModelFactory.class.getSimpleName() + " class");
    }
}