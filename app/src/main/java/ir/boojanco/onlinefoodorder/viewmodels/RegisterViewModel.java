package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RegisterAuth;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterViewModel extends ViewModel {
    private final static String TAG = RegisterViewModel.class.getSimpleName();
    public RegisterAuth registerAuth;
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumberError = new MutableLiveData<>();
    public MutableLiveData<String> passwordError = new MutableLiveData<>();
    private UserRepository userRepository;
    private Context context;

    public RegisterViewModel(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }

   /* public void init() {
        if (mutableLiveData != null)
            return;
        userRepository = UserRepository.getInstance(context);
    }*/

    public void onRegisterClick(View view) {
        if (isValidPhoneNumber(phoneNumber.getValue()) && checkPasswordStrength(password.getValue())) {
            registerAuth.onStarted();
            Observable<Long> observable = userRepository.registerUser(phoneNumber.getValue());
            if (observable != null) {
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NoNetworkConnectionException)
                            registerAuth.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                registerAuth.onFailure(jsonObject.getString("message"));
                            } catch (Exception d) {
                                registerAuth.onFailure(d.getMessage());
                                Log.i(TAG, "" + d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(Long time) {
                        registerAuth.onSuccess(time, phoneNumber.getValue(), password.getValue());
                    }
                });
            }
        }
    }

    public void goToLoginActivity(View view) {
        registerAuth.onGoToLoginActivity();
    }

    private boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            phoneNumberError.setValue("لطفا شماره موبایل را وارد کنید");
            return false;
        } else {
            if (Patterns.PHONE.matcher(phone).matches() && phoneNumber.getValue().length() > 10) {
                phoneNumberError.setValue(null);
                return true;
            } else {
                phoneNumberError.setValue("لطفا شماره موبایل را صحیح وارد کنید");
                return false;
            }
        }

    }

    private boolean checkPasswordStrength(String pass) {
        if (pass == null || pass.isEmpty()) {
            passwordError.setValue("لطفا کلمه عبور را وارد کنید");
            return false;
        } else if (pass.length() < 8) {
            passwordError.setValue("حداقل کلمه عبور 8 کاراکتر");
            return false;
        } else {
            passwordError.setValue(null);
            return true;
        }
    }

}