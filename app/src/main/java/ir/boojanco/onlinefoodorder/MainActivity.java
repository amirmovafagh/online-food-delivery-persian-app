package ir.boojanco.onlinefoodorder;



import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import ir.boojanco.onlinefoodorder.models.User;

import ir.boojanco.onlinefoodorder.models.user.RegisterResponse;
import ir.boojanco.onlinefoodorder.networking.RetrofitService;
import ir.boojanco.onlinefoodorder.networking.APIInterface;
import ir.boojanco.onlinefoodorder.viewmodels.LoginViewModel;
import ir.boojanco.onlinefoodorder.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private static String TAG = "regTest";
    ActivityMainBinding binding;
    //private ActivityMainBinding binding;
    private LoginViewModel viewModel;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get view model
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLoginViewModel(viewModel); // connect activity_Main variable to ViewModel class
        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);

        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.getEmail().length() > 0 || user.getPassword().length() > 0)
                    Toast.makeText(getApplicationContext(), "email : " + user.getEmail() + " password " + user.getPassword(), Toast.LENGTH_SHORT).show();
            }
        });

        apiInterface = RetrofitService.getClient().create(APIInterface.class);

        registerUser("09132837154");
    }

    private void registerUser(String mobile) {
        RegisterResponse register = new RegisterResponse(mobile);
        Call<RegisterResponse> registerCall = apiInterface.registerUser(register);
        registerCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse registerResponse = response.body();
                if (registerResponse != null) {
                    Toast.makeText(MainActivity.this, "your Verification code is :" +
                            registerResponse.getVerificationCode(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "code: " + registerResponse.getCode());
                    Log.e(TAG, "message: " + registerResponse.getMessage());
                    Log.e(TAG, "your Verification code is : " + registerResponse.getVerificationCode());
                    Log.e(TAG, "mobile: " + registerResponse.getMobile());
                    Log.e(TAG, "mobile: " + registerResponse.getReason());
                } else
                    Toast.makeText(MainActivity.this, "dont register", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                //Log.i(TAG, "mobile: " ,t);
                Toast.makeText(MainActivity.this, "dont register", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
