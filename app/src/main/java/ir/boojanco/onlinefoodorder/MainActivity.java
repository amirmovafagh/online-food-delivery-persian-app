package ir.boojanco.onlinefoodorder;



import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import ir.boojanco.onlinefoodorder.models.User;

import ir.boojanco.onlinefoodorder.models.user.RegisterResponse;
import ir.boojanco.onlinefoodorder.networking.RetrofitService;
import ir.boojanco.onlinefoodorder.networking.UserApiInterface;
import ir.boojanco.onlinefoodorder.viewmodels.LoginViewModel;
import ir.boojanco.onlinefoodorder.databinding.ActivityMainBinding;
import ir.boojanco.onlinefoodorder.viewmodels.RegisterViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "regTest";
    private RegisterViewModel registerViewModel;
    ActivityMainBinding binding;
    //private ActivityMainBinding binding;
    //private LoginViewModel viewModel;
    UserApiInterface userApiInterface;

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


        registerViewModel.init("09133445565");
        registerViewModel.getRegisterResponse().observe(this, new Observer<RegisterResponse>() {
                    @Override
                    public void onChanged(RegisterResponse registerResponse) {

                        Toast.makeText(MainActivity.this, ""+registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //userApiInterface = RetrofitService.getService().create(UserApiInterface.class);

                //registerUser("09022021302");
    }

    private void registerUser(String mobile) {
        RegisterResponse register = new RegisterResponse(mobile);
        Call<RegisterResponse> registerCall = userApiInterface.registerUser(register);
        registerCall.enqueue(new Callback<RegisterResponse>() {
            //enqueue will get json in background thread
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

    /*public void sendPost(String title, String body) {

        // RxJava
        userApiInterface.registerUser("123456").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RegisterResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RegisterResponse registerResponse) {
                        Toast.makeText(MainActivity.this, ""+registerResponse.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }*/
}
