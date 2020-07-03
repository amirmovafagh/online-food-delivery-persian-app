package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;

public interface LoginAuth {
    void onStarted();

    void onLoginSuccess(LoginUserResponse loginUserResponse);

    void onFailure(String Error);

    void goToRegisterFragment();

    void goToForgotPassFragment();
}
