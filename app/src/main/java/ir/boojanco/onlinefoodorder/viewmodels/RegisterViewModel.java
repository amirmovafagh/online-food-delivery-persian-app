package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.R;
import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.user.ActivateUserBody;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.models.user.VerificationNewUserResponse;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RegisterAuth;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RegisterViewModel extends ViewModel {
    private static final String TAG = RegisterViewModel.class.getSimpleName();

    public RegisterAuth fragmentInterface;
    public MutableLiveData<String> buttonTimerLiveData;
    public MutableLiveData<Boolean> buttonTimerStateLiveData;// enable or disable
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumberError = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> passwordError = new MutableLiveData<>();
    public MutableLiveData<String> verificationCode;
    public MutableLiveData<String> buttonRegisterTxtLiveData;
    private UserRepository userRepository;
    private Context context;
    private boolean verificationCodeState = false;

    public RegisterViewModel(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
        verificationCode = new MutableLiveData<>();
        buttonRegisterTxtLiveData = new MutableLiveData<>();
        buttonRegisterTxtLiveData.setValue("تایید شماره موبایل");
        buttonTimerLiveData = new MutableLiveData<>();
        buttonTimerStateLiveData = new MutableLiveData<>();
    }

    public void setRegisterInterface(RegisterAuth fragmentInterface) {
        this.fragmentInterface = fragmentInterface;
    }

    public void onRegisterClick() {
        if (!verificationCodeState) {
            getRegisterVerificationCode();
        } else
            checkVerificationCode();
    }

    public void getVerificationCodeAgainOnClick() {
        if (verificationCodeState)
            getRegisterVerificationCode();
    }

    public void getRegisterVerificationCode() {
        if (isValidPhoneNumber(phoneNumber.getValue())) {
            fragmentInterface.onStarted();
            Observable<Long> observable = userRepository.registerUser(phoneNumber.getValue());
            if (observable != null) {
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        fragmentInterface.tryAgain();
                        if (e instanceof NoNetworkConnectionException)
                            fragmentInterface.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                fragmentInterface.onFailure(jsonObject.getString("message"));
                            } catch (Exception d) {
                                fragmentInterface.onFailure(d.getMessage());
                                Log.i(TAG, "" + d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(Long time) {
                        buttonRegisterTxtLiveData.setValue(context.getString(R.string.register));
                        setVerificationCodeTimer(time);
                        verificationCodeState = true;
                        fragmentInterface.onGetVerificationCode();
                    }
                });
            }
        }
    }

    public void setVerificationCodeTimer(long verificationCodeTimer) {

        new CountDownTimer(verificationCodeTimer, 1000) { //Set Timer for 5 seconds
            public void onTick(long millisUntilFinished) {
                buttonTimerLiveData.setValue("دریافت مجدد کد " + "(" + millisUntilFinished / 1000 + ")");
                buttonTimerStateLiveData.setValue(false);
            }

            @Override
            public void onFinish() {
                buttonTimerLiveData.setValue("دریافت مجدد کد");
                buttonTimerStateLiveData.setValue(true);
            }
        }.start();
    }

    public void checkVerificationCode() {
        if (checkPasswordStrength(password.getValue()) && verificationCode != null && verificationCode.getValue() != null && verificationCode.getValue().length() == 5) {
            Observable<VerificationNewUserResponse> observable = userRepository.verifyNewUser(verificationCode.getValue(), phoneNumber.getValue());
            if (observable != null) {
                fragmentInterface.onStarted();
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<VerificationNewUserResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        fragmentInterface.tryAgain();
                        fragmentInterface.onFailure("خطا در بررسی کد فعالسازی");
                        if (e instanceof NoNetworkConnectionException)
                            fragmentInterface.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Log.i(TAG + " Verification Func ", "" + jsonObject.getString("message"));
                                fragmentInterface.onFailure(jsonObject.getString("message"));
                            } catch (Exception d) {
                                Log.i(TAG, "" + d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(VerificationNewUserResponse verificationNewUserResponse) {
                        fragmentInterface.onFailure(verificationNewUserResponse.getStatus());
                        Log.e(TAG,verificationNewUserResponse.getStatus()+"");
                        activateUser(verificationNewUserResponse.getStatus(), verificationNewUserResponse.getMobile(), password.getValue());
                    }
                });
            }
        }
    }

    private void activateUser(String status, String userPhoneNumber, String userPassword) {
        if (status != null && !status.equals("") && userPhoneNumber != null && userPassword != null) {
            ActivateUserBody activeUserBody = new ActivateUserBody(userPassword, userPhoneNumber, userPassword);
            Observable<Response<Void>> observable = userRepository.activateNewUser(activeUserBody);
            if (observable != null) {
                fragmentInterface.onStarted();
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        fragmentInterface.tryAgain();
                        fragmentInterface.onFailure("خطا در فعالسازی حساب کاربری");
                        if (e instanceof NoNetworkConnectionException)
                            fragmentInterface.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Log.i(TAG + " activateUser Function ", "" + jsonObject.getString("message"));
                                fragmentInterface.onFailure(jsonObject.getString("message"));
                            } catch (Exception d) {
                                Log.i(TAG, "" + d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(Response<Void> response) {

                        //if (response.isSuccessful()) {
                            doLogin();
                        //} else fragmentInterface.onFailure("خطا در فعالسازی حساب کاربری");

                    }
                });
            }
        }
    }

    private void doLogin() {
        Observable<LoginUserResponse> observable = userRepository.loginUser(phoneNumber.getValue(), password.getValue());
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LoginUserResponse>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    fragmentInterface.tryAgain();
                    fragmentInterface.onFailure("خطا در ورود به سیستم");
                    if (e instanceof NoNetworkConnectionException)
                        fragmentInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            fragmentInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            Log.i(TAG, "" + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(LoginUserResponse loginUserResponse) {
                    fragmentInterface.onLoginSuccess(loginUserResponse);
                }
            });
        }
    }

    private boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            phoneNumberError.setValue("لطفا شماره موبایل را وارد کنید");
            return false;
        } else {
            if (Patterns.PHONE.matcher(phone).matches() && phone.length() > 10) {
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
        } else if (pass.length() < 6) {
            passwordError.setValue("حداقل کلمه عبور 6 کاراکتر");
            return false;
        } else {
            passwordError.setValue(null);
            return true;
        }
    }


}
