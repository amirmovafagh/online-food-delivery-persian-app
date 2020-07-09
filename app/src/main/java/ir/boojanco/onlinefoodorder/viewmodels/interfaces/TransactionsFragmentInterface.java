package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import ir.boojanco.onlinefoodorder.models.user.GetUserOrderCommentResponse;

public interface TransactionsFragmentInterface {
    void onStarted();
    void onSuccess();
    void onFailure(String error);
}
