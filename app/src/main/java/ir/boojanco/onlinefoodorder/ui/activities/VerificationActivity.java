package ir.boojanco.onlinefoodorder.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.dagger.App;
import ir.boojanco.onlinefoodorder.data.MySharedPreferences;
import ir.boojanco.onlinefoodorder.databinding.ActivityVerificationBinding;
import ir.boojanco.onlinefoodorder.viewmodels.VerificationViewModel;
import ir.boojanco.onlinefoodorder.viewmodels.factories.VerificationViewModelFactory;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.VerificationInterface;

public class VerificationActivity extends AppCompatActivity implements VerificationInterface {
    private static String TAG = VerificationActivity.class.getSimpleName();
    ActivityVerificationBinding binding;
    private VerificationViewModel viewModel;

    private String verificationCodeTimerKeyExtra = "verificationCodeTimer";
    private String phoneNumberKeyExtra = "phoneNumber";
    private String passwordKeyExtra = "password";

    @Inject
    MySharedPreferences sharedPreferences;
    @Inject
    VerificationViewModelFactory factory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplicationContext()).getComponent().inject(this);
        viewModel = new ViewModelProvider(this, factory).get(VerificationViewModel.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_verification);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.setVerificationInterface(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            viewModel.setPhoneNumber(extras.getString(phoneNumberKeyExtra));
            viewModel.setVerificationCodeTimer(extras.getLong(verificationCodeTimerKeyExtra));
            viewModel.setPassword(extras.getString(passwordKeyExtra));
        }
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onSuccess() {
        Intent i = new Intent(this,LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onFailure(String Error) {
        Toast.makeText(this, ""+Error, Toast.LENGTH_SHORT).show();
    }
}
