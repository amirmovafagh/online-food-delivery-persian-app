package ir.boojanco.onlinefoodorder.viewmodels;

import android.app.Application;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ir.boojanco.onlinefoodorder.ui.base.BaseViewModel;
import ir.boojanco.onlinefoodorder.ui.navigator.MainNavigator;

public class MainViewModel extends BaseViewModel<MainNavigator> implements BottomNavigationView.OnNavigationItemSelectedListener{
    private static final String TAG =MainViewModel.class.getSimpleName();


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.e(TAG, ""+menuItem.getItemId());
        return false;
    }
}
