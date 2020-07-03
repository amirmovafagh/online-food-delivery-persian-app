package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;

public interface RegisterAuth {
    void onStarted();

    void onLoginSuccess(LoginUserResponse loginUserResponse);

    void onFailure(String Error);

    void onGetVerificationCode();

    void tryAgain();
}
