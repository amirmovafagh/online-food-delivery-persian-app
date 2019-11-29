package ir.boojanco.onlinefoodorder.ui.activities;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.ActivityLoginBinding;
import ir.boojanco.onlinefoodorder.ui.base.BaseActivity;
import ir.boojanco.onlinefoodorder.ui.navigator.LoginNavigator;
import ir.boojanco.onlinefoodorder.viewmodels.LoginViewModel;

public class LoginActivity extends BaseActivity implements LoginNavigator {
    private LoginViewModel loginViewModel;
    ActivityLoginBinding binding;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get view model
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setUserLogin(loginViewModel); // connect activity_Main variable to ViewModel class
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
        loginViewModel.setNavigator(this);

        loginViewModel.init();
        password = findViewById(R.id.loginPasswordEdtText);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());
    }

    @Override
    public void setObserver() {

    }
}
