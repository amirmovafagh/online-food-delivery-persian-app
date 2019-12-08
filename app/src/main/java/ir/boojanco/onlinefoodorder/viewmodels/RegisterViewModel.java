package ir.boojanco.onlinefoodorder.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ir.boojanco.onlinefoodorder.models.user.RegisterUserResponse;
import ir.boojanco.onlinefoodorder.networking.UserRepository;
import ir.boojanco.onlinefoodorder.ui.activities.LoginActivity;
import ir.boojanco.onlinefoodorder.ui.base.BaseViewModel;
import ir.boojanco.onlinefoodorder.ui.navigator.MainNavigator;
import ir.boojanco.onlinefoodorder.viewmodels.interfaces.RegisterAuth;

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
        if (isValidPhoneNumber()) {
            if (mutableLiveData == null) {
                mutableLiveData = userRepository.registerUser(phoneNumber.getValue());
                //getNavigator().setObserver();
            } else  {
                mutableLiveData = userRepository.registerUser(phoneNumber.getValue());
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

    private boolean isValidPhoneNumber() {
        return phoneNumber.getValue() != null && phoneNumber.getValue().length() > 10;
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