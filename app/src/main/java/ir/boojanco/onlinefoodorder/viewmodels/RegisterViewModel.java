package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.util.Patterns;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import ir.boojanco.onlinefoodorder.models.user.RegisterUserResponse;
import ir.boojanco.onlinefoodorder.data.repositories.UserRepository;
import ir.boojanco.onlinefoodorder.ui.activities.LoginActivity;
import ir.boojanco.onlinefoodorder.ui.base.BaseViewModel;
import ir.boojanco.onlinefoodorder.ui.navigator.MainNavigator;
import ir.boojanco.onlinefoodorder.util.NoNetworkConnectionException;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RegisterAuth;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterViewModel extends BaseViewModel<MainNavigator> {
    private final static String TAG = RegisterViewModel.class.getSimpleName();
    public RegisterAuth registerAuth;
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumberError = new MutableLiveData<>();
    public MutableLiveData<String> enterVerificationCode = new MutableLiveData<>();
    private MutableLiveData<RegisterUserResponse> mutableLiveData;
    private UserRepository userRepository;
    private Context context;

    public RegisterViewModel(Context context, UserRepository userRepository){
        this.context = context;
        this.userRepository = userRepository;
    }

   /* public void init() {
        if (mutableLiveData != null)
            return;
        userRepository = UserRepository.getInstance(context);
    }*/

    public void onRegisterClick(View view) {
        if (isValidPhoneNumber(phoneNumber.getValue())) {
            registerAuth.onStarted();
            Observable<Long> observable = userRepository.registerUser(phoneNumber.getValue());
            if(observable != null){
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e instanceof NoNetworkConnectionException)
                            registerAuth.onFailure(e.getMessage());
                        if(e instanceof HttpException){
                            Response response = ((HttpException) e).response();
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                registerAuth.onFailure(jsonObject.getString("message"));
                            } catch (Exception d) {
                                registerAuth.onFailure(d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(Long time) {
                        registerAuth.onSuccess(time);
                    }
                });
            }
        }
    }

    public  void goToLoginActivity (View view){
        Intent i = new Intent(view.getContext(), LoginActivity.class);
        view.getContext().startActivity(i);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<RegisterUserResponse> getRegisterResponse() {
        return mutableLiveData;
    }

    private boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            phoneNumberError.setValue("لطفا شماره موبایل را وارد کنید");
            return false;
        } else {
            if (Patterns.PHONE.matcher(phone).matches() && phoneNumber.getValue().length()>10) {
                phoneNumberError.setValue(null);
                return true;
            } else {
                phoneNumberError.setValue("لطفا شماره موبایل را صحیح وارد کنید");
                return false;
            }
        }

    }

    public void checkRegisterState(RegisterUserResponse mRegisterUserResponse){
        /*switch(mRegisterUserResponse.getState()){
            case "1":   // new register
                if(authenticateVerificationCode(mRegisterUserResponse.getVerificationCode())){
                    getNavigator().onSuccessRegister();
                }
                return;
            case "2":   // is regitered already but dont check the verification code yet!
                return;
            case "3":   //
                return;
            case "4":
                return;
        }*/
    }

    public boolean authenticateVerificationCode(String serverVerificationCode) {

        return serverVerificationCode.equalsIgnoreCase(enterVerificationCode.getValue());

    }
}