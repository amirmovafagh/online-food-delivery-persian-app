package ir.boojanco.onlinefoodorder.viewmodels;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ir.boojanco.onlinefoodorder.LoginAuth;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.networking.UserRepository;
import ir.boojanco.onlinefoodorder.ui.base.BaseViewModel;
import ir.boojanco.onlinefoodorder.ui.navigator.LoginNavigator;

public class LoginViewModel extends BaseViewModel<LoginNavigator> {
    private static final String TAG = LoginViewModel.class.getSimpleName();
    public LoginAuth loginAuth;
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumberError = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> passwordError = new MutableLiveData<>();
    private MutableLiveData<LoginUserResponse> loginMutableLiveData;
    private UserRepository userRepository;

    public LoginViewModel() {

    }

    public void init() {
        if (userRepository != null)
            return;
        userRepository = UserRepository.getInstance();
    }

    public void onLoginClick() {

        if (isValidPhoneNumber(phoneNumber.getValue()) && isPasswordNull(password.getValue())) {
            loginAuth.onStarted();

            loginMutableLiveData = userRepository.loginUser(phoneNumber.getValue(), password.getValue());
            if(loginMutableLiveData != null)
                loginAuth.onSuccess(loginMutableLiveData);

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