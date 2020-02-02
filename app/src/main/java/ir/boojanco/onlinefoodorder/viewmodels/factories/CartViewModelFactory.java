package ir.boojanco.onlinefoodorder.viewmodels.factories;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ir.boojanco.onlinefoodorder.data.database.CartDataSource;
import ir.boojanco.onlinefoodorder.viewmodels.CartViewModel;

public class CartViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private CartDataSource cartDataSource;

    public CartViewModelFactory(Context context, CartDataSource cartDataSource) {
        this.context = context;
        this.cartDataSource = cartDataSource;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CartViewModel.class)) {
            return (T) new CartViewModel(context, cartDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
