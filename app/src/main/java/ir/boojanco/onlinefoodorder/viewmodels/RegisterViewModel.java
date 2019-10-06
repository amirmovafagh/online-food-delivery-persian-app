package ir.boojanco.onlinefoodorder.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ir.boojanco.onlinefoodorder.models.user.RegisterResponse;
import ir.boojanco.onlinefoodorder.networking.UserRepository;

public class RegisterViewModel extends ViewModel {
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private MutableLiveData<RegisterResponse> mutableLiveData;
    private UserRepository userRepository;

    public void init(String phoneNumber){
        if(mutableLiveData != null)
            return;
        userRepository = UserRepository.getInstance();
        mutableLiveData = userRepository.registerUser(phoneNumber);
    }

    public void onRegisterClicked(){
        if(phoneNumber.getValue() != null)
            init(phoneNumber.toString());
    }

    public LiveData<RegisterResponse> getRegisterResponse(){
        return mutableLiveData;
    }
}