package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.user.RecreatePass;
import ir.boojanco.onlinefoodorder.models.user.VerificationNewUserResponse;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.ForgotPassInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ForgotPassViewModel extends ViewModel {
    private final static String TAG = ForgotPassViewModel.class.getSimpleName();
    public MutableLiveData<String> verificationCode;
    public MutableLiveData<String> buttonTimerLiveData;
    public MutableLiveData<Boolean> buttonTimerStateLiveData;// enable or disable
    public MutableLiveData<String> phoneNumber;
    public MutableLiveData<String> phoneNumberError;
    public MutableLiveData<String> password;
    public MutableLiveData<String> passwordError;
    public MutableLiveData<String> buttonResetTxtLiveData;
    private boolean verificationCodeState = false;
    private long verificationCodeTimer;
    private ForgotPassInterface fragmentInterface;
    private UserRepository userRepository;
    private Context context;

    public ForgotPassViewModel(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
        verificationCode = new MutableLiveData<>();
        buttonTimerLiveData = new MutableLiveData<>();
        buttonTimerStateLiveData = new MutableLiveData<>();
        phoneNumber = new MutableLiveData<>();
        phoneNumberError = new MutableLiveData<>();
        password = new MutableLiveData<>();
        passwordError = new MutableLiveData<>();
        buttonResetTxtLiveData = new MutableLiveData<>();
        buttonResetTxtLiveData.setValue("دریافت کد تایید");

    }

    public void setForgotPassInterface(ForgotPassInterface forgotPassInterface) {
        this.fragmentInterface = forgotPassInterface;
    }


    public void setVerificationCodeTimer(long verificationCodeTimer) {
        this.verificationCodeTimer = verificationCodeTimer;
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
            Observable<VerificationNewUserResponse> observable = userRepository.verifyForgotPass(verificationCode.getValue(), phoneNumber.getValue());
            if (observable != null) {
                fragmentInterface.onStarted();
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<VerificationNewUserResponse>() {
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
                                Log.i(TAG + " Verification Func ", "" + jsonObject.getString("message"));
                                fragmentInterface.onFailure(jsonObject.getString("message"));
                            } catch (Exception d) {
                                Log.i(TAG, "" + d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(VerificationNewUserResponse verificationNewUserResponse) {
                        RecreatePass pass = new RecreatePass(password.getValue(), verificationNewUserResponse.getMobile(), password.getValue());
                        recreateNewPassword(pass);
                    }
                });
            }
        }
    }

    private void recreateNewPassword(RecreatePass pass) {
        if (checkPasswordStrength(password.getValue()) && verificationCode != null && verificationCode.getValue() != null && verificationCode.getValue().length() == 5) {
            Observable<Response<Void>> observable = userRepository.recreatePass(pass);
            if (observable != null) {
                fragmentInterface.onStarted();
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response<Void>>() {
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
                                Log.i(TAG + " RecreatePass Func ", "" + jsonObject.getString("message"));
                                fragmentInterface.onFailure(jsonObject.getString("message"));
                            } catch (Exception d) {
                                Log.i(TAG, "" + d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.isSuccessful()) {
                            fragmentInterface.onFailure("کلمه عبور جدید تایید شد.");
                            fragmentInterface.goBackToLoginFragment();
                        } else fragmentInterface.onFailure("خطا در تغییر کلمه عبور.");

                    }
                });
            }
        }
    }

    public void onResetPasswordClick() {
        if (!verificationCodeState)
            getVerificationCode();
        else {
            checkVerificationCode();
        }
    }

    public void getVerificationCodeAgainOnClick() {
        if (verificationCodeState)
            getVerificationCode();
    }

    private void getVerificationCode() {
        if (isValidPhoneNumber(phoneNumber.getValue())) {
            fragmentInterface.onStarted();
            Observable<Long> observable = userRepository.forgotPassword(phoneNumber.getValue());
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
                        buttonResetTxtLiveData.setValue("تایید کلمه عبور جدید");
                        setVerificationCodeTimer(time);
                        verificationCodeState = true;
                        fragmentInterface.onGetVerificationCode();
                    }
                });
            }
        }
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