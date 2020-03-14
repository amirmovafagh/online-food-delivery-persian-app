package ir.boojanco.onlinefoodorder.viewmodels.interfaces;

import ir.boojanco.onlinefoodorder.models.user.GetUserOrderCommentResponse;

public interface OrdersFragmentInterface {
    void onStarted();
    void onSuccess();
    void onSuccessOrderComment(GetUserOrderCommentResponse commentResponse);
    void onFailure(String error);
}
