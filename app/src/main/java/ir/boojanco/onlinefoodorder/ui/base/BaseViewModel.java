package ir.boojanco.onlinefoodorder.ui.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;


import java.lang.ref.WeakReference;

public abstract class BaseViewModel<N> extends ViewModel {

    private WeakReference<N> mNavigator;


    public N getNavigator() {
        return mNavigator.get();
    }

    public void setNavigator(N navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }
}
