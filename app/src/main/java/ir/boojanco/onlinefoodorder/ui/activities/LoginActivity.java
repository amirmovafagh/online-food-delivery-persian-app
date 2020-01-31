package ir.boojanco.onlinefoodorder.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.LoginAuth;
import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.databinding.ActivityLoginBinding;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.viewmodels.LoginViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.LoginViewModelFactory;

public class LoginActivity extends AppCompatActivity implements LoginAuth {
    LoginViewModel loginViewModel;
    ActivityLoginBinding binding;
    EditText password;

    @Inject
    Application application;
    @Inject
    LoginViewModelFactory factory;
    @Inject
    MySharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // assign singleton instances to fields
        // We need to cast to `App` in order to get the right method
        ((App) getApplicationContext()).getComponent().inject(this);



        // get view model
        loginViewModel = new ViewModelProvider(this,factory).get(LoginViewModel.class);
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setUserLogin(loginViewModel); // connect activity_Main variable to ViewModel class
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
        loginViewModel.loginAuth = this;

        //set font on password editText
        password = findViewById(R.id.loginPasswordEdtText);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());
    }


    @Override
    public void onStarted() {
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(LoginUserResponse loginUserResponse) {
        if (loginUserResponse != null){
            sharedPreferences.setUserAuthTokenKey(loginUserResponse.getId());
            Intent i = new Intent(this,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            binding.cvWaitingResponse.setVisibility(View.GONE);
            startActivity(i);

        }

    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);

    }
}
