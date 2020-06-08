package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;

public interface LoginRegisterAuth {
    void onStarted();
    void onLoginSuccess(LoginUserResponse loginUserResponse);
    void onRegisterSuccess(Long time, String phoneNumber, String password);
    void onFailure(String Error);
}
