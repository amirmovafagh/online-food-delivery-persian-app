package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import ir.boojanco.onlinefoodorder.data.repositories.RestaurantRepository;
import ir.boojanco.onlinefoodorder.viewmodels.PaymentViewModel;

public class PaymentViewModelFactory implements ViewModelProvider.Factory {
    private Context context;
    private RestaurantRepository restaurantRepository;

    public PaymentViewModelFactory(Context context, RestaurantRepository restaurantRepository) {
        this.context = context;
        this.restaurantRepository = restaurantRepository;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PaymentViewModel.class)) {
            return (T) new PaymentViewModel(context, restaurantRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
