package ir.boojanco.onlinefoodorder.viewmodels;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ir.boojanco.onlinefoodorder.models.User;
import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;
import ir.boojanco.onlinefoodorder.networking.UserRepository;

public class LoginViewModel extends ViewModel {

    private static final String TAG =LoginViewModel.class.getSimpleName();

    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<LoginUserResponse> loginMutableLiveData;
    private UserRepository userRepository;

    public void init(){
        if(userRepository != null)
            return;
        userRepository = UserRepository.getInstance();
    }

    /*public void onLoginClicked() {
        if(isValidPhoneNumber()){
            if(loginMutableLiveData == null){
                loginMutableLiveData = userRepository.loginUser(phoneNumber.getValue());
                getNavigatr.setLoginObserver();
            }else loginMutableLiveData = userRepository.loginUser(phoneNumber.getValue());
        }
    }
*/
    private boolean isValidPhoneNumber() {
        return phoneNumber.getValue() != null && phoneNumber.getValue().length() > 10;
    }

}


//an example
/*private MutableLiveData<User> userMutableLiveData;

    public MutableLiveData<String> errorPassword = new MutableLiveData<>();
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<Integer> busy;

    public MutableLiveData<Integer> getBusy() {
        if (busy == null) {
            busy = new MutableLiveData<>();
            busy.setValue(8);
        }
        return busy;
    }


    public LoginViewModel() {

    }

    public LiveData<User> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }

    public void onLoginClicked(){
        getBusy().setValue(0); //View.VISIBLE
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = new User(email.getValue(), password.getValue());

                if(!user.isEmailValid()){
                    errorEmail.setValue("Enter a valid email address");
                }else errorEmail.setValue(null);

                if(!user.isPasswordLengthGreaterThan5()){
                    errorPassword.setValue("Password Length should be greater than 5");
                }else errorPassword.setValue(null);
                userMutableLiveData.setValue(user);
                busy.setValue(8); //View.GONE
            }
        },1000);
    }*/