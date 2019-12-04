package ir.boojanco.onlinefoodorder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;

public interface LoginAuth {
    void onStarted();
    void onSuccess(MutableLiveData<LoginUserResponse> loginMutableLiveData);
    void onFailure();
}
