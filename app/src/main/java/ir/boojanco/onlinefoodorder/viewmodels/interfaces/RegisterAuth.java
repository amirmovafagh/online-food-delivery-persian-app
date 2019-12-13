package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import ir.boojanco.onlinefoodorder.models.user.RegisterUserResponse;

public interface RegisterAuth {
    void onStarted();
    void onSuccess(Long time);
    void onFailure(String Error);
}
