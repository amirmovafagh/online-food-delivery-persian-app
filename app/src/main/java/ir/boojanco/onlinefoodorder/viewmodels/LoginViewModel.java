package ir.boojanco.onlinefoodorder.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;

import ir.boojanco.onlinefoodorder.LoginAuth;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.networking.UserRepository;
import ir.boojanco.onlinefoodorder.ui.base.BaseViewModel;
import ir.boojanco.onlinefoodorder.ui.navigator.LoginNavigator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    private static final String TAG = LoginViewModel.class.getSimpleName();
    public LoginAuth loginAuth;
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumberError = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> passwordError = new MutableLiveData<>();
    private MutableLiveData<LoginUserResponse> loginMutableLiveData;
    private UserRepository userRepository;
    private Context context;

    public LoginViewModel(Context context) {
        this.context = context;
    }

    public void init() {
        if (userRepository != null)
            return;
        userRepository = UserRepository.getInstance();
    }

    public void onLoginClick() {
        Toast.makeText(context, "Test Factory Class", Toast.LENGTH_SHORT).show();
        if (isValidPhoneNumber(phoneNumber.getValue()) && isPasswordNull(password.getValue())) {
            loginAuth.onStarted();
            Observable<LoginUserResponse> observable = userRepository.loginUser(phoneNumber.getValue(), password.getValue());
            if (observable != null) {
                observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LoginUserResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loginAuth.onFailure(e.getMessage());
                        if (e instanceof HttpException) {
                            Response response = ((HttpException) e).response();

                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Log.i(TAG,""+jObjError.getString("message"));
                                loginAuth.onFailure(jObjError.getString("message"));


                            } catch (Exception d) {
                                loginAuth.onFailure(d.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onNext(LoginUserResponse loginUserResponse) {
                        loginAuth.onSuccess(loginUserResponse);
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
            if (Patterns.PHONE.matcher(phone).matches()) {
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


}