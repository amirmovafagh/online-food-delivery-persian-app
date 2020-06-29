package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;

public interface ForgotPassInterface {
    void onStarted();

    void onSuccess(LoginUserResponse loginUserResponse);

    void onFailure(String Error);

    void onGetVerificationCode();

    void tryAgain();

    void goBackToLoginFragment();
}
