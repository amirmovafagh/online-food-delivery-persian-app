package ir.boojanco.onlinefoodorder.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.databinding.ActivityRegisterBinding;
import ir.boojanco.onlinefoodorder.viewmodels.RegisterViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.RegisterViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RegisterAuth;

public class RegisterActivity extends AppCompatActivity implements RegisterAuth {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private String verificationCodeTimerKeyExtra = "verificationCodeTimer";
    private String phoneNumberKeyExtra = "phoneNumber";
    private String passwordKeyExtra = "password";
    RegisterViewModel registerViewModel;
    ActivityRegisterBinding binding;

    @Inject
    Application application;
    @Inject
    RegisterViewModelFactory factory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // assign singleton instances to fields
        // We need to cast to `App` in order to get the right method
        ((App) getApplicationContext()).getComponent().inject(this);

        // get view model
        registerViewModel = new ViewModelProvider(this, factory).get(RegisterViewModel.class);
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setUserRegister(registerViewModel); // connect activity_Main variable to ViewModel class
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
        registerViewModel.registerAuth = this;

        EditText password = binding.registerPasswordEdtText;
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());

    }

    @Override
    public void onStarted() {
        binding.cvWaitingResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(Long time, String phoneNumber, String password) {

        Toast.makeText(application, "" + time, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VerificationActivity.class);
        i.putExtra(verificationCodeTimerKeyExtra, time);
        i.putExtra(phoneNumberKeyExtra, phoneNumber);
        i.putExtra(passwordKeyExtra, password);
        binding.cvWaitingResponse.setVisibility(View.GONE);
        /*i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
        startActivity(i);

    }

    @Override
    public void onGoToLoginActivity() {
        if(getIntent()!=null){
            onBackPressed();
        }else startActivity(new Intent(this, LoginActivity.class));

    }

    @Override
    public void onFailure(String Error) {
        binding.cvWaitingResponse.setVisibility(View.GONE);
        Toast.makeText(application, "" + Error, Toast.LENGTH_SHORT).show();
    }
}
