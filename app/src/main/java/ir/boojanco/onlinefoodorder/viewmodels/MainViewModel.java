package ir.boojanco.onlinefoodorder.viewmodels;

import android.app.Application;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import ir.boojanco.onlinefoodorder.ui.base.BaseViewModel;
import ir.boojanco.onlinefoodorder.ui.navigator.MainNavigator;

public class MainViewModel extends BaseViewModel<MainNavigator> {
    private static final String TAG =MainViewModel.class.getSimpleName();
    private Application application;

    public MainViewModel(Application application){
        this.application = application;
    }

    public boolean onNavigationClick(@NonNull MenuItem item){
        /*switch (item.getItemId()){

        }*/
        Log.i(TAG,""+item.getItemId());
        return true;
    }
}
