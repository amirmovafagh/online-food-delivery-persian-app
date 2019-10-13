package ir.boojanco.onlinefoodorder.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import ir.boojanco.onlinefoodorder.models.user.RegisterResponse;
import ir.boojanco.onlinefoodorder.networking.UserRepository;
import ir.boojanco.onlinefoodorder.ui.navigator.MainNavigator;
import ir.boojanco.onlinefoodorder.ui.base.BaseViewModel;

public class RegisterViewModel extends BaseViewModel<MainNavigator> {
    private final String TAG = RegisterViewModel.class.getSimpleName();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private MutableLiveData<RegisterResponse> mutableLiveData;
    private UserRepository userRepository;


    public void init() {
        if (mutableLiveData != null)
            return;
        userRepository = UserRepository.getInstance();

        //mutableLiveData = userRepository.registerUser(null);

    }

    public void onRegisterClicked() {
        Log.d(TAG, "1" + phoneNumber.getValue());
        if (phoneNumber.getValue() != null) {
            if (mutableLiveData == null) {
                mutableLiveData = userRepository.registerUser(phoneNumber.getValue());
                getNavigator().setObserver();
            } else mutableLiveData = userRepository.registerUser(phoneNumber.getValue());


        }


    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<RegisterResponse> getRegisterResponse() {


        return mutableLiveData;
    }
}