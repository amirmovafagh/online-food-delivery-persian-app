package ir.boojanco.onlinefoodorder.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.Toast;

import ir.boojanco.onlinefoodorder.LoginAuth;
import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.databinding.ActivityLoginBinding;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.viewmodels.LoginViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.LoginViewModelFactory;

public class LoginActivity extends AppCompatActivity implements LoginAuth {
    private LoginViewModel loginViewModel;
    ActivityLoginBinding binding;
    EditText password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginViewModelFactory factory =
                new LoginViewModelFactory(getApplication());
        // get view model
        loginViewModel = ViewModelProviders.of(this,factory).get(LoginViewModel.class);
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setUserLogin(loginViewModel); // connect activity_Main variable to ViewModel class
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
        loginViewModel.loginAuth = this;

        loginViewModel.init();
        password = findViewById(R.id.loginPasswordEdtText);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());
    }


    @Override
    public void onStarted() {
        Toast.makeText(this, "AAA", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(LoginUserResponse loginUserResponse) {
        if (loginUserResponse != null)
            Toast.makeText(LoginActivity.this, "" + loginUserResponse.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
    }
}
