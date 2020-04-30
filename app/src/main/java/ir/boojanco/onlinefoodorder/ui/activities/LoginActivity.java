package ir.boojanco.onlinefoodorder.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

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
    LoginViewModel viewModel;
    ActivityLoginBinding binding;
    EditText password, phoneNum;

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

        if (sharedPreferences.getUserAuthTokenKey() != null) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        // get view model
        viewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setUserLogin(viewModel); // connect activity_Main variable to ViewModel class
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
        viewModel.loginAuth = this;

        phoneNum = binding.loginPhoneEdtText;
        //set font on password editText
        password = binding.loginPasswordEdtText;
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());

        password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                viewModel.onLoginClick();
                return true;
            }
            return false;
        });

        binding.buttonRegisterActivity.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        binding.buttonEnterAsGuest.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            binding.cvWaitingResponse.setVisibility(View.GONE);
            startActivity(i);
        });
    }


    @Override
    public void onStarted() {
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(LoginUserResponse loginUserResponse) {
        if (loginUserResponse != null) {
            sharedPreferences.setUserAuthTokenKey(loginUserResponse.getId());
            sharedPreferences.setPhoneNumber(loginUserResponse.getMobile());
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            binding.cvWaitingResponse.setVisibility(View.GONE);
            startActivity(i);

        }

    }

    @Override
    public void onFailure(String error) {
        binding.cvWaitingResponse.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(binding.mainContent, "" + error, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
