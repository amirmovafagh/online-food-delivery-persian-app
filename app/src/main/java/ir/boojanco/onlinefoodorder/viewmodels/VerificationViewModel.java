package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.models.user.ActivateUserBody;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.models.user.RegisterUserResponse;
import ir.boojanco.onlinefoodorder.models.user.VerificationNewUserResponse;
import ir.boojanco.onlinefoodorder.ui.activities.LoginActivity;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RegisterAuth;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.VerificationInterface;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VerificationViewModel extends ViewModel {
    private final static String TAG = VerificationViewModel.class.getSimpleName();
    public MutableLiveData<String> verificationCode;
    public MutableLiveData<String> buttonTimerLiveData;
    public MutableLiveData<Boolean> buttonTimerStateLiveData;// enable or disable
    private String phoneNumber;
    private String password;
    private long verificationCodeTimer;
    private VerificationInterface verificationInterface;
    private UserRepository userRepository;
    private Context context;

    public VerificationViewModel(Context context, UserRepository userRepository) {
        this.context = context;
        this.userRepository = userRepository;
        verificationCode = new MutableLiveData<>();
        buttonTimerLiveData = new MutableLiveData<>();
        buttonTimerStateLiveData = new MutableLiveData<>();

    }

    public void setVerificationInterface(VerificationInterface verificationInterface) {
        this.verificationInterface = verificationInterface;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
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
        if (verificationCode != null && verificationCode.getValue() != null && verificationCode.getValue().length() == 5) {
            Observable<VerificationNewUserResponse> observable = userRepository.verifyNewUser(verificationCode.getValue(), phoneNumber);
            if (observable != null) {
                verificationInterface.onStarted();
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<VerificationNewUserResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NoNetworkConnectionException)
                            verificationInterface.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Log.i(TAG+" Verification Func ", "" + jsonObject.getString("message"));
                                verificationInterface.onFailure(jsonObject.getString("message"));
                            } catch (Exception d) {
                                Log.i(TAG, "" + d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(VerificationNewUserResponse verificationNewUserResponse) {
                        activateUser(verificationNewUserResponse.getId(), verificationNewUserResponse.getMobile(), password);
                    }
                });
            }
        }
    }

    private void activateUser(long userId, String userPhoneNumber, String userPassword) {
        if (userId != 0 && userPhoneNumber != null && userPassword != null) {
            ActivateUserBody activeUserBody = new ActivateUserBody(userId, userPhoneNumber, userPassword);
            Observable<Response<Void>> observable = userRepository.activateNewUser(activeUserBody);
            if (observable != null) {
                verificationInterface.onStarted();
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NoNetworkConnectionException)
                            verificationInterface.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Log.i(TAG +" activateUser Function " , "" + jsonObject.getString("message"));
                                verificationInterface.onFailure(jsonObject.getString("message"));
                            } catch (Exception d) {
                                Log.i(TAG, "" + d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(Response<Void> response) {

                        if (response.isSuccessful()) {
                            doLogin();
                        }else verificationInterface.onFailure("خطا در فعالسازی حساب کاربری");

                    }
                });
            }
        }
    }

    private void doLogin (){
        Observable<LoginUserResponse> observable = userRepository.loginUser(phoneNumber, password);
        if (observable != null) {
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LoginUserResponse>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof NoNetworkConnectionException)
                        verificationInterface.onFailure(e.getMessage());
                    if (e instanceof HttpException) {
                        Response response = ((HttpException) e).response();

                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            verificationInterface.onFailure(jsonObject.getString("message"));


                        } catch (Exception d) {
                            Log.i(TAG, "" + d.getMessage());
                        }
                    }
                }

                @Override
                public void onNext(LoginUserResponse loginUserResponse) {
                    verificationInterface.onSuccess(loginUserResponse);
                }
            });
        }
    }


}