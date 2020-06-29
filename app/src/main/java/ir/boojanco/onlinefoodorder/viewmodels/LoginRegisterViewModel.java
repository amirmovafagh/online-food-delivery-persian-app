package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.LoginRegisterAuth;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LoginRegisterViewModel extends ViewModel {
    private static final String TAG = LoginRegisterViewModel.class.getSimpleName();

    public LoginRegisterAuth loginRegisterAuth;
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumberError = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> passwordError = new MutableLiveData<>();
    private MutableLiveData<LoginUserResponse> loginMutableLiveData;
    private UserRepository userRepository;
    private Context context;

    public LoginRegisterViewModel(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
    }


    public void onLoginClick() {

        if (isValidPhoneNumber(phoneNumber.getValue()) && isPasswordNull(password.getValue())) {
            loginRegisterAuth.onStarted();
            Observable<LoginUserResponse> observable = userRepository.loginUser(phoneNumber.getValue(), password.getValue());
            if (observable != null) {
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LoginUserResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NoNetworkConnectionException)
                            loginRegisterAuth.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();

                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                loginRegisterAuth.onFailure(jsonObject.getString("message"));


                            } catch (Exception d) {
                                loginRegisterAuth.onFailure(d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(LoginUserResponse loginUserResponse) {
                        loginUserResponse.setMobile(phoneNumber.getValue());
                        loginRegisterAuth.onLoginSuccess(loginUserResponse);

                    }
                });
            }

        }
    }

    public void onRegisterClick() {
        if (isValidPhoneNumber(phoneNumber.getValue()) && checkPasswordStrength(password.getValue())) {
            loginRegisterAuth.onStarted();
            Observable<Long> observable = userRepository.registerUser(phoneNumber.getValue());
            if (observable != null) {
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NoNetworkConnectionException)
                            loginRegisterAuth.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                loginRegisterAuth.onFailure(jsonObject.getString("message"));
                            } catch (Exception d) {
                                loginRegisterAuth.onFailure(d.getMessage());
                                Log.i(TAG, "" + d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(Long time) {
                        loginRegisterAuth.onRegisterSuccess(time, phoneNumber.getValue(), password.getValue());
                    }
                });
            }
        }
    }

    public void goToForgotPassFragment(){
        loginRegisterAuth.goToForgotPassFragment();
    }


    private boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            phoneNumberError.setValue("لطفا شماره موبایل را وارد کنید");
            return false;
        } else {
            // if ()
            if (Patterns.PHONE.matcher(phone).matches() && phone.length() > 10) {
                phoneNumberError.setValue(null);
                return true;
            } else {
                phoneNumberError.setValue("لطفا شماره موبایل را صحیح وارد کنید");
                return false;
            }
        }

    }

    private boolean isPasswordNull(String pass) {
        if (pass == null || pass.isEmpty()) {
            passwordError.setValue("لطفا کلمه عبور را وارد کنید");
            return false;
        } else {
            passwordError.setValue(null);
            return true;
        }
    }

    private boolean checkPasswordStrength(String pass) {
        if (pass == null || pass.isEmpty()) {
            passwordError.setValue("لطفا کلمه عبور را وارد کنید");
            return false;
        } else if (pass.length() < 6) {
            passwordError.setValue("حداقل کلمه عبور 6 کاراکتر");
            return false;
        } else {
            passwordError.setValue(null);
            return true;
        }
    }

}
