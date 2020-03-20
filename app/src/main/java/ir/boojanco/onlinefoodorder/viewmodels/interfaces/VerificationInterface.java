package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import ir.boojanco.onlinefoodorder.models.user.LoginUserResponse;

public interface VerificationInterface {
    void onStarted();
    void onSuccess(LoginUserResponse loginUserResponse);
    void onFailure(String Error);
}
