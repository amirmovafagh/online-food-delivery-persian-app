package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import ir.boojanco.onlinefoodorder.models.user.RegisterUserResponse;

public interface RegisterAuth {
    void onStarted();

    void onSuccess(Long time, String phoneNumber, String password);
    void onGoToLoginActivity();

    void onFailure(String Error);
}
