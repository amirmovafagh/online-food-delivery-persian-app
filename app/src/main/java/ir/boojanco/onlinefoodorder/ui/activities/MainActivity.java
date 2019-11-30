package ir.boojanco.onlinefoodorder.ui.activities;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ir.boojanco.onlinefoodorder.R;

import ir.boojanco.onlinefoodorder.databinding.ActivityMainBinding;
import ir.boojanco.onlinefoodorder.models.user.RegisterUserResponse;
import ir.boojanco.onlinefoodorder.ui.base.BaseActivity;
import ir.boojanco.onlinefoodorder.ui.navigator.MainNavigator;
import ir.boojanco.onlinefoodorder.viewmodels.MainViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.RegisterViewModel;

@BindingMethods({
        @BindingMethod(
                type = BottomNavigationView.class,
                attribute = "app:onNavigationItemSelected",
                method = "setOnNavigationItemSelectedListener"
        ),
})
public class MainActivity extends BaseActivity implements MainNavigator {
    private static String TAG = "regTest";
    private MainViewModel mainViewModel;
    private RegisterUserResponse registerUserResponse;
    ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get view model
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMain(mainViewModel); // connect activity_Main variable to ViewModel class
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);

    }


}
