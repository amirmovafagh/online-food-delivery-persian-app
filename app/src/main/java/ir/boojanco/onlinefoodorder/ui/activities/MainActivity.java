package ir.boojanco.onlinefoodorder.ui.activities;


import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.ViewModelProviders;

import ir.boojanco.onlinefoodorder.R;

import ir.boojanco.onlinefoodorder.databinding.ActivityMainBinding;
import ir.boojanco.onlinefoodorder.ui.base.BaseActivity;
import ir.boojanco.onlinefoodorder.ui.navigator.MainNavigator;
import ir.boojanco.onlinefoodorder.viewmodels.RegisterViewModel;

public class MainActivity extends BaseActivity implements MainNavigator {
    private static String TAG = "regTest";
    private RegisterViewModel registerViewModel;
    ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get view model
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setRegisterViewModel(registerViewModel); // connect activity_Main variable to ViewModel class
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
        registerViewModel.setNavigator(this);

        registerViewModel.init();

    }

    @Override
    public void setObserver() {
        registerViewModel.getRegisterResponse().observe(this, registerResponse -> Toast.makeText(MainActivity.this, "" + registerResponse.getMessage() + "verifyCode: " + registerResponse.getVerificationCode(), Toast.LENGTH_SHORT).show());
    }

}
